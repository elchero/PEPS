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
        String mensaje = (String) request.getSession().getAttribute("mensaje");
        String tipoMensaje = (String) request.getSession().getAttribute("tipoMensaje");

        if (mensaje != null) {
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", tipoMensaje);
            // Limpiar mensajes de la sesión
            request.getSession().removeAttribute("mensaje");
            request.getSession().removeAttribute("tipoMensaje");
        }

        listarCompras(request);
        request.getRequestDispatcher("/Vistas/compras.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String action = request.getParameter("action");
        String mensaje = "";
        String tipoMensaje = "success"; // valor por defecto

        try {
            ComprasDAO comprasDAO = new ComprasDAO();

            if ("create".equals(action)) {
                int idProducto = Integer.parseInt(request.getParameter("id_producto"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                double costoUnitario = Double.parseDouble(request.getParameter("costo_unitario"));

                Compras compra = new Compras();
                compra.setId_producto(idProducto);
                compra.setCantidad(cantidad);
                compra.setCosto_total(cantidad * costoUnitario);

                Lotes lote = new Lotes();
                lote.setId_producto(idProducto);
                lote.setCosto_unitario(costoUnitario);

                boolean exito = comprasDAO.registrarCompraYCrearLote(compra, lote);
                if (exito) {
                    mensaje = "Compra registrada exitosamente";
                    tipoMensaje = "success";
                } else {
                    mensaje = "Error: No se puede registrar la compra porque no existe un inventario inicial. Debe registrar primero el inventario inicial.";
                    tipoMensaje = "danger";
                }
            } else if ("delete".equals(action)) {
                int idCompra = Integer.parseInt(request.getParameter("id_compra"));
                int idLote = Integer.parseInt(request.getParameter("id_lote"));
                boolean exito = comprasDAO.eliminarCompraYRelacionados(idCompra, idLote);

                if (exito) {
                    mensaje = "Compra eliminada exitosamente";
                    tipoMensaje = "success";
                } else {
                    mensaje = "Error al eliminar la compra";
                    tipoMensaje = "danger";
                }
            }

        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
            tipoMensaje = "danger";
        }

        // Guardar mensaje y tipo en la sesión
        request.getSession().setAttribute("mensaje", mensaje);
        request.getSession().setAttribute("tipoMensaje", tipoMensaje);

        // Redireccionar a GET
        response.sendRedirect("ComprasServlet");
    }

    // Método para cargar las listas de compras
    private void listarCompras(HttpServletRequest request) {
        try {
            ComprasDAO comprasDAO = new ComprasDAO();
            List<Compras> listaCompras = comprasDAO.listarCompras();
            List<Productos> listaProductos = comprasDAO.listarProductos();

            request.setAttribute("listaCompras", listaCompras);
            request.setAttribute("listaProductos", listaProductos);
        } catch (Exception e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
