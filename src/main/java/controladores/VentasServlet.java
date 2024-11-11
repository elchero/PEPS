package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelos.Productos;
import modelos.Ventas;
import modelosDAO.ProductosDAO;
import modelosDAO.VentasDAO;

/**
 *
 * @author vladi
 */
@WebServlet(name = "VentasServlet", urlPatterns = {"/VentasServlet"})
public class VentasServlet extends HttpServlet {

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
            out.println("<title>Servlet VentasServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VentasServlet at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        String mensaje = (String) request.getSession().getAttribute("mensaje");
        String tipoMensaje = (String) request.getSession().getAttribute("tipoMensaje");

        if (mensaje != null) {
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", tipoMensaje);
            // Limpiar mensajes de la sesión
            request.getSession().removeAttribute("mensaje");
            request.getSession().removeAttribute("tipoMensaje");
        }

        listarVentas(request);
        request.getRequestDispatcher("ventas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String action = request.getParameter("action");
        String mensaje = "";
        String tipoMensaje = "success";

        try {
            VentasDAO ventasDAO = new VentasDAO();

            if ("create".equals(action)) {
                int idProducto = Integer.parseInt(request.getParameter("id_producto"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                double precioVentaUnitario = Double.parseDouble(request.getParameter("precio_venta_unitario"));

                Ventas venta = new Ventas();
                venta.setId_producto(idProducto);
                venta.setCantidad(cantidad);
                venta.setPrecio_venta_unitario(precioVentaUnitario);

                boolean exito = ventasDAO.registrarVenta(venta, precioVentaUnitario);

                if (exito) {
                    mensaje = "Venta registrada exitosamente";
                    tipoMensaje = "success";
                } else {
                    mensaje = "Error al registrar la venta. Verifique el stock disponible.";
                    tipoMensaje = "danger";
                }
            } else if ("delete".equals(action)) {
                int idVenta = Integer.parseInt(request.getParameter("id_venta"));
                boolean exito = ventasDAO.eliminarVenta(idVenta);

                if (exito) {
                    mensaje = "Venta eliminada exitosamente y stock restaurado";
                    tipoMensaje = "success";
                } else {
                    mensaje = "Error al eliminar la venta";
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
        response.sendRedirect("VentasServlet");
    }

    private void listarVentas(HttpServletRequest request) {
        try {
            VentasDAO ventasDAO = new VentasDAO();
            ProductosDAO productosDAO = new ProductosDAO();

            List<Ventas> ventas = ventasDAO.listarVentas();
            List<Productos> productos = productosDAO.listarProductos();

            request.setAttribute("listaVentas", ventas);
            request.setAttribute("listaProductos", productos);
        } catch (Exception e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
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
