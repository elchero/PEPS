package reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import modelos.Devoluciones;
import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GeneradorNotaCredito {

    private Devoluciones devolucion;
    private String nombreEmpresa = "DevConta S.A.";
    private String rucEmpresa = "20505688516";
    private String direccionEmpresa = "Av. Los Programadores 123, San Isidro";
    private String telefonoEmpresa = "(01) 555-1234";
    private String emailEmpresa = "devoluciones@devconta.com";

    public GeneradorNotaCredito(Devoluciones devolucion) {
        this.devolucion = devolucion;
    }

    public byte[] generarPDF() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Añadir metadatos
        document.addTitle("Nota de Crédito - " + devolucion.getId_devolucion());
        document.addSubject("Nota de Crédito para devolución");
        document.addKeywords("nota de crédito, devolución, " + devolucion.getTipo_devolucion());
        document.addCreator("DevConta Sistema");

        // Agregar logo (puedes reemplazar la ruta con tu logo)
        try {
            // Usar una URL directa para el logo
            String logoUrl = "https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/LogotransIndex.png?alt=media&token=b9c0500b-d054-4399-83b0-24080d296073";
            Image logo = Image.getInstance(new URL(logoUrl));
            logo.scaleToFit(150, 80);
            logo.setAlignment(Element.ALIGN_RIGHT);
            document.add(logo);
        } catch (Exception e) {
            // Agregar un texto alternativo si el logo no está disponible
            Font logoFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(44, 62, 80));
            Paragraph logoText = new Paragraph("DevConta", logoFont);
            logoText.setAlignment(Element.ALIGN_RIGHT);
            document.add(logoText);
        }

        // Título principal
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(44, 62, 80));
        Paragraph title = new Paragraph("NOTA DE CRÉDITO", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Información de la empresa
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        addSection("DATOS DE LA EMPRESA", document, headerFont);
        addParagraphWithLabel("Empresa: ", nombreEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("RUC: ", rucEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Dirección: ", direccionEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Teléfono: ", telefonoEmpresa, document, headerFont, normalFont);
        addParagraphWithLabel("Email: ", emailEmpresa, document, headerFont, normalFont);

        // Información del documento
        addSection("INFORMACIÓN DEL DOCUMENTO", document, headerFont);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("es", "PE"));
        addParagraphWithLabel("No. Nota de Crédito: ", String.valueOf(devolucion.getId_devolucion()), document, headerFont, normalFont);
        addParagraphWithLabel("Fecha de Emisión: ", sdf.format(devolucion.getFecha_devolucion()), document, headerFont, normalFont);
        addParagraphWithLabel("Tipo de Operación: ", devolucion.getTipo_operacion().toUpperCase(), document, headerFont, normalFont);

        // Detalles del producto
        addSection("DETALLE DE LA DEVOLUCIÓN", document, headerFont);

        // Crear tabla de detalles
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Encabezados de la tabla
        addTableHeader(table, new String[]{"Producto", "Lote", "Cantidad", "Estado", "Razón"});

        // Datos de la devolución
        addTableRow(table, new String[]{
            devolucion.getNombre_producto(),
            String.valueOf(devolucion.getId_lote()),
            String.valueOf(devolucion.getCantidad()),
            devolucion.getTipo_devolucion(),
            devolucion.getRazon()
        });

        document.add(table);

        // Términos y condiciones
        addSection("TÉRMINOS Y CONDICIONES", document, headerFont);
        Paragraph terms = new Paragraph();
        terms.add(new Chunk("1. Esta nota de crédito está sujeta a las disposiciones tributarias vigentes\n", normalFont));
        terms.add(new Chunk("2. La devolución física de los productos debe realizarse en un plazo máximo de 7 días\n", normalFont));
        terms.add(new Chunk("3. Los productos defectuosos serán evaluados por control de calidad\n", normalFont));
        terms.add(new Chunk("4. La restitución del valor se realizará según políticas de la empresa", normalFont));
        document.add(terms);

        // Firmas
        addSection("FIRMAS", document, headerFont);
        PdfPTable signatures = new PdfPTable(2);
        signatures.setWidthPercentage(100);
        signatures.setSpacingBefore(50f);

        PdfPCell cell1 = new PdfPCell(new Phrase("_____________________\nEmitido por", normalFont));
        PdfPCell cell2 = new PdfPCell(new Phrase("_____________________\nRecibido por", normalFont));

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
}
