package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelos.Devoluciones;
import modelosDAO.DevolucionesDAO;
import otras_funcionalidades.CompraDevolucion;
import otras_funcionalidades.VentaDevolucion;
import reportes.GeneradorNotaCredito;

/**
 *
 * @author vladi
 */
@WebServlet(name = "DevolucionesServlet", urlPatterns = {"/DevolucionesServlet"})
public class DevolucionesServlet extends HttpServlet {

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
            out.println("<title>Servlet DevolucionesServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DevolucionesServlet at " + request.getContextPath() + "</h1>");
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
        // Recuperar mensaje y tipo de mensaje de la sesión
        String action = request.getParameter("action");

        // Si hay una acción específica
        if (action != null && !action.isEmpty()) {
            switch (action) {
                case "generarNotaCredito":
                    generarNotaCredito(request, response);
                    return; // Importante: retornar aquí para evitar la ejecución del código siguiente
                default:
                    // Manejar acción desconocida si es necesario
                    break;
            }
        }

        // Código existente para el caso por defecto
        String mensaje = (String) request.getSession().getAttribute("mensaje");
        String tipoMensaje = (String) request.getSession().getAttribute("tipoMensaje");

        if (mensaje != null) {
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", tipoMensaje);
            // Limpiar mensajes de la sesión
            request.getSession().removeAttribute("mensaje");
            request.getSession().removeAttribute("tipoMensaje");
        }

        listarDevoluciones(request);
        request.getRequestDispatcher("/Vistas/devoluciones.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // processRequest(request, response);
        String action = request.getParameter("action");
        String mensaje = "";
        String tipoMensaje = "success";

        try {
            DevolucionesDAO devolucionesDAO = new DevolucionesDAO();
            if ("revertir".equals(action)) {
                boolean exito = devolucionesDAO.revertirDevoluciones();
                if (exito) {
                    mensaje = "Las devoluciones han sido revertidas exitosamente";
                } else {
                    mensaje = "Error al revertir las devoluciones";
                    tipoMensaje = "danger";
                }
            } else if ("create".equals(action)) {
                String tipoOperacion = request.getParameter("tipo_operacion");
                int idProducto = Integer.parseInt(request.getParameter("id_producto"));
                int idLote = Integer.parseInt(request.getParameter("id_lote"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                String tipoDevolucion = request.getParameter("tipo_devolucion");
                String razon = request.getParameter("razon");

                // Validaciones básicas
                if (cantidad <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
                }
                if (razon == null || razon.trim().isEmpty()) {
                    throw new IllegalArgumentException("Debe especificar una razón para la devolución");
                }

                Devoluciones devolucion = new Devoluciones();
                devolucion.setId_producto(idProducto);
                devolucion.setId_lote(idLote);
                devolucion.setCantidad(cantidad);
                devolucion.setRazon(razon);
                devolucion.setTipo_devolucion(tipoDevolucion);

                try {
                    boolean exito;
                    if ("venta".equals(tipoOperacion)) {
                        exito = devolucionesDAO.registrarDevolucion(devolucion, tipoDevolucion);
                        if (exito) {
                            mensaje = "Devolución de venta registrada exitosamente";
                        } else {
                            tipoMensaje = "danger";
                            mensaje = "Error al registrar la devolución de venta. Verifique que se cumplan las reglas PEPS y las cantidades sean correctas";
                        }
                    } else {
                        exito = devolucionesDAO.registrarDevolucionCompra(devolucion);
                        if (exito) {
                            mensaje = "Devolución de compra registrada exitosamente";
                        } else {
                            tipoMensaje = "danger";
                            mensaje = "Error al registrar la devolución de compra. Verifique que se cumplan las reglas PEPS y las cantidades sean correctas";
                        }
                    }
                } catch (Exception e) {
                    tipoMensaje = "danger";
                    String errorMessage = e.getMessage().toLowerCase();
                    if (errorMessage.contains("peps")) {
                        mensaje = "No se puede procesar la devolución: " + e.getMessage();
                    } else if (errorMessage.contains("cantidad")) {
                        mensaje = "Error en las cantidades: " + e.getMessage();
                    } else if (errorMessage.contains("lote")) {
                        mensaje = "Error con el lote especificado: " + e.getMessage();
                    } else {
                        mensaje = "Error al procesar la devolución: " + e.getMessage();
                    }
                }
            }
        } catch (NumberFormatException e) {
            mensaje = "Error: Los datos ingresados no son válidos";
            tipoMensaje = "danger";
        } catch (IllegalArgumentException e) {
            mensaje = e.getMessage();
            tipoMensaje = "danger";
        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
            tipoMensaje = "danger";
        }

        // Guardar mensaje y tipo en la sesión
        request.getSession().setAttribute("mensaje", mensaje);
        request.getSession().setAttribute("tipoMensaje", tipoMensaje);

        // Redireccionar a GET
        response.sendRedirect("DevolucionesServlet");
    }

    private void listarDevoluciones(HttpServletRequest request) {
        try {
            DevolucionesDAO devolucionesDAO = new DevolucionesDAO();

            // Obtener todas las listas necesarias
            List<Devoluciones> devoluciones = devolucionesDAO.listarDevoluciones();
            List<VentaDevolucion> ventasDisponibles = devolucionesDAO.obtenerVentasParaDevolucion();
            List<CompraDevolucion> comprasDisponibles = devolucionesDAO.obtenerComprasParaDevolucion();

            // Establecer todos los atributos
            request.setAttribute("listaDevoluciones", devoluciones);
            request.setAttribute("ventasDisponibles", ventasDisponibles);
            request.setAttribute("comprasDisponibles", comprasDisponibles);
        } catch (Exception e) {
            System.err.println("Error al listar devoluciones: " + e.getMessage());
            request.setAttribute("mensaje", "Error al cargar los datos: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
        }
    }

    private void generarNotaCredito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idDevolucion = Integer.parseInt(request.getParameter("id"));
            DevolucionesDAO dao = new DevolucionesDAO();

            // Obtener la devolución específica
            List<Devoluciones> devoluciones = dao.listarDevoluciones();
            Devoluciones devolucion = devoluciones.stream()
                    .filter(d -> d.getId_devolucion() == idDevolucion)
                    .findFirst()
                    .orElseThrow(() -> new ServletException("Devolución no encontrada"));

            // Generar el PDF
            GeneradorNotaCredito generador = new GeneradorNotaCredito(devolucion);
            byte[] pdfBytes = generador.generarPDF();

            // Configurar la respuesta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=nota_credito_" + idDevolucion + ".pdf");
            response.setContentLength(pdfBytes.length);

            // Escribir el PDF en la respuesta
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(pdfBytes);
                out.flush();
            }

        } catch (Exception e) {
            // Manejar errores
            request.setAttribute("mensaje", "Error al generar la nota de crédito: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/devoluciones/index.jsp").forward(request, response);
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
