package reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import db.cn;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import modelos.Compras;

public class GeneradorOrdenCompra {

    private Connection con;
    private Compras compra;
    private String nombreEmpresa = "DevConta S.A.";
    private String rucEmpresa = "20505688516";
    private String direccionEmpresa = "Av. Los Programadores 123, San Isidro";
    private String telefonoEmpresa = "(01) 555-1234";
    private String emailEmpresa = "compras@devconta.com";

    // Variables para los cálculos y datos adicionales
    private String nombreProducto;
    private String descripcionProducto;
    private String proveedor;
    private double costoUnitario;
    private double subtotal;
    private double iva;
    private double total;

    public GeneradorOrdenCompra(Compras compra) throws ClassNotFoundException {
        this.compra = compra;
        cn conexion = new cn();
        this.con = conexion.getCon();
        cargarDatosAdicionales();
        calcularTotales();
    }

    private void cargarDatosAdicionales() {
        try {
            String sql = "SELECT p.nombre, p.descripcion, p.proveedor, l.costo_unitario "
                    + "FROM productos p "
                    + "JOIN lotes l ON p.id_producto = l.id_producto "
                    + "WHERE p.id_producto = ? AND l.id_lote = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, compra.getId_producto());
                ps.setInt(2, compra.getId_lote());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    this.nombreProducto = rs.getString("nombre");
                    this.descripcionProducto = rs.getString("descripcion");
                    this.proveedor = rs.getString("proveedor");
                    this.costoUnitario = rs.getDouble("costo_unitario");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcularTotales() {
        this.subtotal = this.costoUnitario * compra.getCantidad();
        this.iva = this.subtotal * 0.13;
        this.total = this.subtotal + this.iva;
    }

    public byte[] generarPDF() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Metadatos
        document.addTitle("Orden de Compra - " + compra.getId_compra());
        document.addSubject("Orden de Compra");
        document.addKeywords("orden, compra, " + nombreProducto);
        document.addCreator("DevConta Sistema");

        // Logo
        try {
            String logoUrl = "https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/LogotransIndex.png?alt=media&token=b9c0500b-d054-4399-83b0-24080d296073";
            Image logo = Image.getInstance(new URL(logoUrl));
            logo.scaleToFit(150, 80);
            logo.setAlignment(Element.ALIGN_RIGHT);
            document.add(logo);
        } catch (Exception e) {
            Font logoFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(44, 62, 80));
            Paragraph logoText = new Paragraph("DevConta", logoFont);
            logoText.setAlignment(Element.ALIGN_RIGHT);
            document.add(logoText);
        }

        // Fuentes
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(44, 62, 80));
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        // Título
        Paragraph title = new Paragraph("ORDEN DE COMPRA", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Información de la empresa
        addSection("DATOS DE LA EMPRESA", document, headerFont);
        addParagraphWithLabel("Empresa: ", nombreEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("RUC: ", rucEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Dirección: ", direccionEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Teléfono: ", telefonoEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Email: ", emailEmpresa, document, headerFont, normalFont);

        // Información de la orden
        addSection("INFORMACIÓN DE LA ORDEN", document, headerFont);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("es", "PE"));
        DecimalFormat df = new DecimalFormat("#,##0.00");
        addParagraphWithLabel("No. Orden: ", String.valueOf(compra.getId_compra()), document, headerFont, normalFont);
        addParagraphWithLabel("Fecha: ", sdf.format(compra.getFecha_compra()), document, headerFont, normalFont);
        addParagraphWithLabel("No. Lote: ", String.valueOf(compra.getId_lote()), document, headerFont, normalFont);

        // Datos del proveedor
        addSection("DATOS DEL PROVEEDOR", document, headerFont);
        addParagraphWithLabel("Proveedor: ", proveedor, document, headerFont, normalFont);

        // Detalles de la compra
        addSection("DETALLE DE LA COMPRA", document, headerFont);
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        addTableHeader(table, new String[]{
            "Código", "Producto", "Descripción", "Cantidad", "Precio Unit.", "Subtotal"
        });

        // Agregar la fila de datos
        addTableRow(table, new String[]{
            String.valueOf(compra.getId_producto()),
            nombreProducto,
            descripcionProducto != null ? descripcionProducto : "N/A",
            String.valueOf(compra.getCantidad()),
            "$/ " + df.format(costoUnitario),
            "$/ " + df.format(subtotal)
        });

        document.add(table);

        // Totales
        PdfPTable totalsTable = new PdfPTable(2);
        totalsTable.setWidthPercentage(40);
        totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalsTable.setSpacingBefore(20);

        addTotalRow(totalsTable, "Subtotal:", "$/ " + df.format(subtotal));
        addTotalRow(totalsTable, "IVA (13%):", "$/ " + df.format(iva));
        addTotalRow(totalsTable, "Total:", "$/ " + df.format(total));

        document.add(totalsTable);

        // Términos y condiciones
        addSection("TÉRMINOS Y CONDICIONES", document, headerFont);
        Paragraph terms = new Paragraph();
        terms.add(new Chunk("1. El proveedor se compromete a entregar los productos en las condiciones especificadas\n", normalFont));
        terms.add(new Chunk("2. La entrega debe realizarse en la dirección indicada\n", normalFont));
        terms.add(new Chunk("3. Los productos deben cumplir con las especificaciones de calidad acordadas\n", normalFont));
        terms.add(new Chunk("4. El pago se realizará según los términos acordados con el proveedor\n", normalFont));
        document.add(terms);

        // Firmas
        addSection("FIRMAS", document, headerFont);
        PdfPTable signatures = new PdfPTable(2);
        signatures.setWidthPercentage(100);
        signatures.setSpacingBefore(50f);

        PdfPCell cell1 = new PdfPCell(new Phrase(
                "_____________________\nAutorizado por\nFecha: ________________", normalFont));
        PdfPCell cell2 = new PdfPCell(new Phrase(
                "_____________________\nRecibido por\nFecha: ________________", normalFont));

        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);

        signatures.addCell(cell1);
        signatures.addCell(cell2);

        document.add(signatures);

        document.close();
        return baos.toByteArray();
    }

    // Métodos auxiliares para el formato del PDF
    private void addSection(String title, Document document, Font headerFont) throws DocumentException {
        Paragraph section = new Paragraph(title, headerFont);
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);
        document.add(section);
        document.add(new LineSeparator());
    }

    private void addParagraphWithLabel(String label, String value, Document document,
            Font headerFont, Font normalFont) throws DocumentException {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label, headerFont));
        p.add(new Chunk(value, normalFont));
        document.add(p);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(255, 255, 255));
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(44, 62, 80));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, String[] values) {
        Font cellFont = new Font(Font.HELVETICA, 9, Font.NORMAL);
        for (String value : values) {
            PdfPCell cell = new PdfPCell(new Phrase(value, cellFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void addTotalRow(PdfPTable table, String label, String value) {
        Font totalFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        PdfPCell labelCell = new PdfPCell(new Phrase(label, totalFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, totalFont));

        labelCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        if (label.equals("Total:")) {
            labelCell.setBackgroundColor(new Color(240, 240, 240));
            valueCell.setBackgroundColor(new Color(240, 240, 240));
        }

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    public void cerrarConexion() {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
