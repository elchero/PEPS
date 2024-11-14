package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelosDAO.DashboardDAO;
import otras_funcionalidades.Dashboard;

@WebServlet(name = "DashboardController", urlPatterns = {"/DashboardController"})
public class DashboardController extends HttpServlet {

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
        try {
            DashboardDAO dao = new DashboardDAO();
            List<Dashboard> resumen = dao.obtenerResumenGeneral();

            System.out.println("Cantidad de productos en resumen: " + resumen.size());

            // Calcular totales
            double ventasNetas = 0;
            double ventasBrutas = 0;
            double costoVentasNeto = 0;
            int totalUnidadesVendidas = 0;
            int cantidadDevolucionesVenta = 0;
            int cantidadDevolucionesCompra = 0;
            double costoDevolucionesCompra = 0;
            int totalInventarioActual = 0;
            int totalInventarioInicial = 0;
            int totalLotes = 0;

            for (Dashboard item : resumen) {
                ventasNetas += item.getVentasNetas();
                ventasBrutas += item.getVentasBrutas();
                costoVentasNeto += item.getCostoVentasNeto();
                totalUnidadesVendidas += item.getUnidadesVendidas();
                cantidadDevolucionesVenta += item.getCantidadDevolucionesVenta();
                cantidadDevolucionesCompra += item.getCantidadDevolucionesCompra();
                costoDevolucionesCompra += item.getCostoDevolucionesCompra();
                totalInventarioActual += item.getInventarioActual();
                totalInventarioInicial += item.getInventarioInicial();
                totalLotes += item.getTotalLotes();
            }

            // Calcular porcentajes
            double porcentajeDevolucionesVenta = totalUnidadesVendidas > 0
                    ? (cantidadDevolucionesVenta * 100.0 / totalUnidadesVendidas) : 0;

            double margenBruto = ventasNetas - costoVentasNeto;
            double porcentajeMargen = ventasNetas > 0 ? (margenBruto * 100.0 / ventasNetas) : 0;

            // Establecer atributos
            request.setAttribute("resumenDashboard", resumen);
            request.setAttribute("ventasBrutas", ventasBrutas);
            request.setAttribute("ventasNetas", ventasNetas);
            request.setAttribute("costoVentasNeto", costoVentasNeto);
            request.setAttribute("margenBruto", margenBruto);
            request.setAttribute("porcentajeMargen", porcentajeMargen);
            request.setAttribute("unidadesVendidas", totalUnidadesVendidas);
            request.setAttribute("cantidadDevolucionesVenta", cantidadDevolucionesVenta);
            request.setAttribute("porcentajeDevolucionesVenta", porcentajeDevolucionesVenta);
            request.setAttribute("cantidadDevolucionesCompra", cantidadDevolucionesCompra);
            request.setAttribute("costoDevolucionesCompra", costoDevolucionesCompra);
            request.setAttribute("totalInventarioActual", totalInventarioActual);
            request.setAttribute("totalInventarioInicial", totalInventarioInicial);
            request.setAttribute("totalLotes", totalLotes);
            
              System.out.println("Enviando a JSP - Ventas Netas: " + ventasNetas);

            request.getRequestDispatcher("Vistas/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error en DashboardController: " + e.getMessage());
            request.setAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            request.getRequestDispatcher("Vistas/error.jsp").forward(request, response);
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
        processRequest(request, response);
        // String accion = request.getParameter("accion");

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
        processRequest(request, response);
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
