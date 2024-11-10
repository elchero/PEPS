
package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelos.Compras;
import modelosDAO.ComprasDAO;

/**
 *
 * @author magana
 */
@WebServlet(name = "InventarioInfoServlet", urlPatterns = {"/InventarioInfoServlet"})
public class InventarioInfoServlet extends HttpServlet {

  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Connection con = null;
        List<Compras> listaInventarioInfo = new ArrayList<>();
        try {
            String sql = "SELECT\n" +
"    c.id_compra,\n" +
"    p.nombre,\n" +
"    c.id_lote,\n" +
"    c.cantidad,\n" +
"    l.costo_unitario,\n" +
"    c.costo_total,\n" +
"    li.cantidad_disponible,\n" +
"    c.fecha_compra,\n" +
"    CASE\n" +
"        WHEN c.fecha_compra = (SELECT MIN(fecha_compra) FROM compras) THEN 'Inventario Inicial'\n" +
"        ELSE 'Compra'\n" +
"    END AS tipo_movimiento\n" +
"FROM compras c\n" +
"JOIN lotes l ON c.id_lote = l.id_lote\n" +
"JOIN lote_inventario li ON l.id_lote = li.id_lote\n" +
"JOIN productos p ON c.id_producto = p.id_producto;";

            // Ejecutar consulta
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Procesar resultados de la consulta
            while (rs.next()) {
                Compras compra = new Compras();
                compra.setId_compra(rs.getInt("id_compra"));
                compra.setNombre(rs.getString("nombre"));
                compra.setId_lote(rs.getInt("id_lote"));
                compra.setCantidad(rs.getInt("cantidad"));
                compra.setCosto_unitario(rs.getDouble("costo_unitario"));
                compra.setCosto_total(rs.getDouble("costo_total"));
                compra.setCantidad_disponible(rs.getInt("cantidad_disponible"));
                compra.setFecha_compra(rs.getTimestamp("fecha_compra"));
                compra.setTipo_movimiento(rs.getString("tipo_movimiento"));
                listaInventarioInfo.add(compra);
            }

            // Cerrar recursos
            rs.close();
            stmt.close();
            con.close();
            
        } catch (Exception e) {
            e.printStackTrace(); // Para manejar excepciones de base de datos
        }

        // Pasar los datos de inventario a la página JSP
        request.setAttribute("listaInventarioInfo", listaInventarioInfo);

        // Enviar la solicitud a la página JSP (inventario_info.jsp)
        request.getRequestDispatcher("/inventario_info.jsp").forward(request, response);
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
        // Obtener la lista de inventario desde la base de datos
        ComprasDAO comprasDAO = null;
        try {
            comprasDAO = new ComprasDAO();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InventarioInfoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Compras> listaInventarioInfo = comprasDAO.listarInventarioInfo();
        
        // Pasar los datos a la JSP
        request.setAttribute("listaInventarioInfo", listaInventarioInfo);
        
        // Redirigir a la página JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("inventario_info.jsp");
        dispatcher.forward(request, response);
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
    }

}
