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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // processRequest(request, response);
        String action = request.getParameter("action");
        String mensaje = "";

        try {
            InventarioInicialDAO inventarioInicialDAO = new InventarioInicialDAO();
            // Verificar si ya existe un inventario inicial
            Compras inventarioInicialExistente = inventarioInicialDAO.obtenerInventarioInicial();
            if ("create".equals(action)) {
                if (inventarioInicialExistente != null) {
                    mensaje = "Ya existe un inventario inicial. No es posible agregar otro.";
                    request.setAttribute("mensaje", mensaje);
                    listarComprasYLotes(request);
                    request.getRequestDispatcher("inventario_inicial.jsp").forward(request, response);
                    return;
                }

                // Procede con la creación del inventario inicial
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

                boolean exito = inventarioInicialDAO.registrarCompraYCrearLote(compra, lote);
                mensaje = exito ? "Inventario Inicial registrado exitosamente" : "Error al registrar Inventario Inicial";
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
                mensaje = exito ? "Inventario Inicial actualizado exitosamente" : "Error al actualizar al actualizar Inventario Inicial";

            } else if ("delete".equals(action)) {
                int idCompra = Integer.parseInt(request.getParameter("id_compra"));
                int idLote = Integer.parseInt(request.getParameter("id_lote"));
                boolean exito = inventarioInicialDAO.eliminarCompraYRelacionados(idCompra, idLote);

                if (exito) {
                    mensaje = "Inventario Inicial eliminado exitosamente";
                } else {
                    mensaje = "No se puede eliminar el Inventario Inicial porque existen registros posteriores";
                }
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
            Compras inventarioInicial = inventarioInicialDAO.obtenerInventarioInicial();
            List<Productos> listaProductos = inventarioInicialDAO.listarProductos();

            request.setAttribute("inventarioInicial", inventarioInicial);
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
