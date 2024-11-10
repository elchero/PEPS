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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String action = request.getParameter("action");
        String mensaje = "";

        try {
            ComprasDAO comprasDAO = new ComprasDAO();

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
                boolean exito = comprasDAO.registrarCompraYCrearLote(compra, lote);
                if (exito) {
                    mensaje = "Compra registrada exitosamente";
                    request.setAttribute("tipoMensaje", "success");
                } else {
                    mensaje = "Error: No se puede registrar la compra porque no existe un inventario inicial. Debe registrar primero el inventario inicial.";
                    request.setAttribute("tipoMensaje", "danger");
                }
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

                boolean exito = comprasDAO.actualizarCompraYActualizarLote(compra, lote);
                mensaje = exito ? "Compra actualizada exitosamente" : "Error al actualizar la compra";

            } else if ("delete".equals(action)) {
                // Eliminar una compra
                int idCompra = Integer.parseInt(request.getParameter("id_compra"));
                int idLote = Integer.parseInt(request.getParameter("id_lote"));
                boolean exito = comprasDAO.eliminarCompraYRelacionados(idCompra, idLote);
                mensaje = exito ? "Compra eliminada exitosamente" : "Error al eliminar la compra";
            }

        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
        }

        // Listar nuevamente y redirigir
        listarComprasYLotes(request);
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("compras.jsp").forward(request, response);
    }

    // Método para cargar las listas de compras y lotes
    private void listarComprasYLotes(HttpServletRequest request) {
        try {
            ComprasDAO comprasDAO = new ComprasDAO();
            List<Compras> listaCompras = comprasDAO.listarCompras();
            List<Productos> listaProductos = comprasDAO.listarProductos();

            request.setAttribute("listaCompras", listaCompras);
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
