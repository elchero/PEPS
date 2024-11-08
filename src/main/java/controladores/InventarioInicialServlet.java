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
import modelosDAO.InventarioInicialDAO;

@WebServlet(name = "InventarioInicialServlet", urlPatterns = {"/InventarioInicialServlet"})
public class InventarioInicialServlet extends HttpServlet {

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
            out.println("<title>Servlet InventarioInicialServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InventarioInicialServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("inventario_inicial.jsp").forward(request, response);
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
        // processRequest(request, response);
        String action = request.getParameter("action");
        String mensaje = "";

        try {
            InventarioInicialDAO inventarioInicialDAO = new InventarioInicialDAO();

            if ("create".equals(action)) {
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

                // Registrar la compra y el lote
                boolean exito = inventarioInicialDAO.registrarCompraYCrearLote(compra, lote);
                mensaje = exito ? "Compra registrada exitosamente" : "Error al registrar la compra";

            } else if ("update".equals(action)) {
                // Editar una compra
                int idCompra = Integer.parseInt(request.getParameter("id_compra"));
                int idProducto = Integer.parseInt(request.getParameter("id_producto"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                double costoUnitario = Double.parseDouble(request.getParameter("costo_unitario"));

                Compras compra = new Compras();
                compra.setId_compra(idCompra);
                compra.setId_producto(idProducto);
                compra.setCantidad(cantidad);
                compra.setCosto_total(cantidad * costoUnitario);

                Lotes lote = new Lotes();
                lote.setCosto_unitario(costoUnitario);
                lote.setId_lote(idProducto);

                boolean exito = inventarioInicialDAO.actualizarCompraYActualizarLote(compra, lote);
                mensaje = exito ? "Compra actualizada exitosamente" : "Error al actualizar la compra";

            } else if ("delete".equals(action)) {
                // Eliminar una compra
                int idCompra = Integer.parseInt(request.getParameter("id_compra"));
                int idLote = Integer.parseInt(request.getParameter("id_lote"));
                boolean exito = inventarioInicialDAO.eliminarCompraYRelacionados(idCompra, idLote);
                mensaje = exito ? "Compra eliminada exitosamente" : "Error al eliminar la compra";
            }

        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
        }

        // Listar nuevamente y redirigir
        listarComprasYLotes(request);
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("inventario_inicial.jsp").forward(request, response);
    }
    // Método para cargar las listas de compras y lotes

    private void listarComprasYLotes(HttpServletRequest request) {
        try {
            InventarioInicialDAO inventarioInicialDAO = new InventarioInicialDAO();
            List<Compras> listaCompras = inventarioInicialDAO.listarCompras();
            List<Lotes> listaLotes = inventarioInicialDAO.listarLotes();
            List<Productos> listaProductos = inventarioInicialDAO.listarProductos();

            request.setAttribute("listaCompras", listaCompras);
            request.setAttribute("listaLotes", listaLotes);
            request.setAttribute("listaProductos", listaProductos);
        } catch (Exception e) {
            System.err.println("Error al listar compras o lotes: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
