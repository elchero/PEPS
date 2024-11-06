package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelos.Compras;
import modelos.Lotes;
import modelos.Productos;
import modelosDAO.ComprasDAO;

@WebServlet(name = "ComprasServlet", urlPatterns = {"/ComprasServlet"})
public class ComprasServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ComprasServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ComprasServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // processRequest(request, response);
        listarComprasYLotes(request);
        // Redirigir al formulario
        request.getRequestDispatcher("compras.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String mensaje;
        try {
            // Obtener los parámetros del formulario
            int idProducto = Integer.parseInt(request.getParameter("id_producto"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            double costoUnitario = Double.parseDouble(request.getParameter("costo_unitario"));

            // Crear objetos para compra y lote
            Compras compra = new Compras();
            compra.setId_producto(idProducto);
            compra.setCantidad(cantidad);
            compra.setCosto_total(cantidad * costoUnitario);

            Lotes lote = new Lotes();
            lote.setId_producto(idProducto);
            lote.setCosto_unitario(costoUnitario);

            // Llamar al DAO para registrar la compra y el lote
            ComprasDAO comprasDAO = new ComprasDAO();
            boolean exito = comprasDAO.registrarCompraYCrearLote(compra, lote);

            mensaje = exito ? "Compra registrada exitosamente" : "Error al registrar la compra";

        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
        }

        // Actualizar las listas para mostrarlas en la JSP
        listarComprasYLotes(request);
        request.setAttribute("mensaje", mensaje);

        // Redirigir a la misma página JSP
        request.getRequestDispatcher("compras.jsp").forward(request, response);
    }

    // Método para cargar las listas de compras y lotes
    private void listarComprasYLotes(HttpServletRequest request) {
        try {
            ComprasDAO comprasDAO = new ComprasDAO();
            List<Compras> listaCompras = comprasDAO.listarCompras();
            List<Lotes> listaLotes = comprasDAO.listarLotes(); // Suponiendo que este método existe
            List<Productos> listaProductos = comprasDAO.listarProductos();

            request.setAttribute("listaCompras", listaCompras);
            request.setAttribute("listaLotes", listaLotes);
            request.setAttribute("listaProductos", listaProductos);
        } catch (Exception e) {
            System.err.println("Error al listar compras o lotes: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
