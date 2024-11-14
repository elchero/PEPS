package reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import db.cn;
import modelos.Devoluciones;
import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GeneradorNotaCredito {

    private Connection con;
    private Devoluciones devolucion;
    private String nombreEmpresa = "DevConta S.A.";
    private String rucEmpresa = "20505688516";
    private String direccionEmpresa = "Av. Los Programadores 123, San Isidro";
    private String telefonoEmpresa = "(01) 555-1234";
    private String emailEmpresa = "devoluciones@devconta.com";

    // Variables para los cálculos
    private double precioUnitario;
    private double subtotal;
    private double iva;
    private double total;
    private String documentoOriginal;
    private String fechaOriginal;
    private String proveedor;
    private double montoOperacionOriginal;
    private int cantidadOperacionOriginal;

    public GeneradorNotaCredito(Devoluciones devolucion) throws ClassNotFoundException {
        this.devolucion = devolucion;
        cn conexion = new cn();
        this.con = conexion.getCon();
        cargarDatosMonetarios();
        cargarDatosOperacionOriginal();
    }

    private void cargarDatosMonetarios() {
        try {
            if (devolucion.getTipo_operacion().equals("compra")) {
                // Para devoluciones de compra, usar el costo del lote
                String sql = "SELECT l.costo_unitario FROM lotes l WHERE l.id_lote = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, devolucion.getId_lote());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        this.precioUnitario = rs.getDouble("costo_unitario");
                    }
                }
            } else {
                // Para devoluciones de venta, usar el precio de venta original
                String sql = "SELECT precio_venta_unitario FROM ventas WHERE id_lote = ? AND id_producto = ? "
                        + "ORDER BY fecha_venta DESC LIMIT 1";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, devolucion.getId_lote());
                    ps.setInt(2, devolucion.getId_producto());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        this.precioUnitario = rs.getDouble("precio_venta_unitario");
                    }
                }
            }

            this.subtotal = this.precioUnitario * devolucion.getCantidad();
            this.iva = this.subtotal * 0.13;
            this.total = this.subtotal + this.iva;
        } catch (Exception e) {
            e.printStackTrace();
            this.precioUnitario = 0.0;
            this.subtotal = 0.0;
            this.iva = 0.0;
            this.total = 0.0;
        }
    }

    private void cargarDatosOperacionOriginal() {
        try {
            if (devolucion.getTipo_operacion().equals("compra")) {
                String sql = "SELECT c.id_compra, c.fecha_compra, c.cantidad, c.costo_total, p.proveedor "
                        + "FROM compras c "
                        + "JOIN productos p ON c.id_producto = p.id_producto "
                        + "WHERE c.id_lote = ? AND c.id_producto = ? "
                        + "ORDER BY c.fecha_compra DESC LIMIT 1";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, devolucion.getId_lote());
                    ps.setInt(2, devolucion.getId_producto());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        this.documentoOriginal = "Factura de Compra N° " + rs.getString("id_compra");
                        this.fechaOriginal = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                .format(rs.getTimestamp("fecha_compra"));
                        this.proveedor = rs.getString("proveedor");
                        this.cantidadOperacionOriginal = rs.getInt("cantidad");
                        this.montoOperacionOriginal = rs.getDouble("costo_total");
                    }
                }
            } else {
                String sql = "SELECT v.id_venta, v.fecha_venta, v.cantidad, "
                        + "(v.cantidad * v.precio_venta_unitario) as monto_total "
                        + "FROM ventas v "
                        + "WHERE v.id_lote = ? AND v.id_producto = ? "
                        + "ORDER BY v.fecha_venta DESC LIMIT 1";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, devolucion.getId_lote());
                    ps.setInt(2, devolucion.getId_producto());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        this.documentoOriginal = "Factura de Venta N° " + rs.getString("id_venta");
                        this.fechaOriginal = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                .format(rs.getTimestamp("fecha_venta"));
                        this.cantidadOperacionOriginal = rs.getInt("cantidad");
                        this.montoOperacionOriginal = rs.getDouble("monto_total");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.documentoOriginal = "No disponible";
            this.fechaOriginal = "No disponible";
            this.proveedor = "No disponible";
            this.cantidadOperacionOriginal = 0;
            this.montoOperacionOriginal = 0.0;
        }
    }

    public byte[] generarPDF() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Metadatos
        document.addTitle("Nota de Crédito - " + devolucion.getId_devolucion());
        document.addSubject("Nota de Crédito para devolución");
        document.addKeywords("nota de crédito, devolución, " + devolucion.getTipo_devolucion());
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

        // Título y fuentes
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(44, 62, 80));
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        // Título
        Paragraph title = new Paragraph("NOTA DE CRÉDITO", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Datos de la empresa
        addSection("DATOS DE LA EMPRESA", document, headerFont);
        addParagraphWithLabel("Empresa: ", nombreEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("RUC: ", rucEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Dirección: ", direccionEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Teléfono: ", telefonoEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Email: ", emailEmpresa, document, headerFont, normalFont);

        // Información del documento
        addSection("INFORMACIÓN DEL DOCUMENTO", document, headerFont);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("es", "PE"));
        DecimalFormat df = new DecimalFormat("#,##0.00");
        addParagraphWithLabel("No. Nota de Crédito: ", String.valueOf(devolucion.getId_devolucion()), document, headerFont, normalFont);
        addParagraphWithLabel("Fecha de Emisión: ", sdf.format(devolucion.getFecha_devolucion()), document, headerFont, normalFont);
        addParagraphWithLabel("Documento que Modifica: ", documentoOriginal, document, headerFont, normalFont);
        addParagraphWithLabel("Fecha Doc. Original: ", fechaOriginal, document, headerFont, normalFont);
        addParagraphWithLabel("Cantidad Original: ", String.valueOf(cantidadOperacionOriginal), document, headerFont, normalFont);
        addParagraphWithLabel("Monto Original: ", "$/ " + df.format(montoOperacionOriginal), document, headerFont, normalFont);
        addParagraphWithLabel("Cantidad a Devolver: ", String.valueOf(devolucion.getCantidad()), document, headerFont, normalFont);

        // Datos del cliente o proveedor
        if (devolucion.getTipo_operacion().equals("compra")) {
            addSection("DATOS DEL PROVEEDOR", document, headerFont);
            addParagraphWithLabel("Proveedor: ", proveedor, document, headerFont, normalFont);
        } else {
            addSection("DATOS DEL CLIENTE", document, headerFont);
            addParagraphWithLabel("Cliente: ", "_________________________________", document, headerFont, normalFont);
            addParagraphWithLabel("DNI/RUC: ", "_________________________________", document, headerFont, normalFont);
            addParagraphWithLabel("Dirección: ", "_________________________________", document, headerFont, normalFont);
        }

        // Detalles de la devolución
        addSection("DETALLE DE LA DEVOLUCIÓN", document, headerFont);
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Encabezados y datos de la tabla
        addTableHeader(table, new String[]{
            "Producto", "Lote", "Cantidad", "P.Unit", "Subtotal", "Estado"
        });

        addTableRow(table, new String[]{
            devolucion.getNombre_producto(),
            String.valueOf(devolucion.getId_lote()),
            String.valueOf(devolucion.getCantidad()),
            "$/ " + df.format(precioUnitario),
            "$/ " + df.format(subtotal),
            formatearEstado(devolucion.getTipo_devolucion())
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

        // Razón de devolución
        addSection("RAZÓN DE LA DEVOLUCIÓN", document, headerFont);
        Paragraph razon = new Paragraph(devolucion.getRazon(), normalFont);
        document.add(razon);

        // Términos y condiciones específicos según el tipo de operación
        agregarTerminosYCondiciones(document, headerFont, normalFont);

        // Firmas
        addSection("FIRMAS", document, headerFont);
        PdfPTable signatures = new PdfPTable(2);
        signatures.setWidthPercentage(100);
        signatures.setSpacingBefore(50f);

        PdfPCell cell1 = new PdfPCell(new Phrase(
                "_____________________\nEmitido por\nFecha: ________________", normalFont));
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

    private void agregarTerminosYCondiciones(Document document, Font headerFont, Font normalFont) throws DocumentException {
        addSection("TÉRMINOS Y CONDICIONES", document, headerFont);
        Paragraph terms = new Paragraph();
        terms.add(new Chunk("1. Esta nota de crédito está sujeta a las disposiciones tributarias vigentes\n", normalFont));

        if (devolucion.getTipo_operacion().equals("venta")) {
            terms.add(new Chunk("2. La devolución física de los productos debe realizarse en un plazo máximo de 7 días\n", normalFont));
            terms.add(new Chunk("3. Los productos serán evaluados para verificar que cumplan con las condiciones de devolución\n", normalFont));
            terms.add(new Chunk("4. Solo se aceptan devoluciones con el empaque original y en buen estado\n", normalFont));
            terms.add(new Chunk("5. La restitución del valor se realizará según la forma de pago original\n", normalFont));
        } else {
            terms.add(new Chunk("2. La devolución al proveedor debe ser coordinada y autorizada previamente\n", normalFont));
            terms.add(new Chunk("3. Los productos defectuosos deben ser documentados con evidencia fotográfica\n", normalFont));
            terms.add(new Chunk("4. El proveedor debe emitir la nota de crédito correspondiente\n", normalFont));
            terms.add(new Chunk("5. La compensación se realizará en la siguiente orden de compra o según acuerdo\n", normalFont));
        }

        document.add(terms);
    }

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

    private String formatearEstado(String estado) {
        switch (estado.toLowerCase()) {
            case "defectuoso":
                return "Producto Defectuoso";
            case "venta":
                return "Devolución de Venta";
            case "compra":
                return "Devolución de Compra";
            default:
                return estado;
        }
    }

    // Método para limpiar la conexión
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
