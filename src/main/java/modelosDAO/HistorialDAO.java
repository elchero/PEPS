package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorialDAO {

    private Connection con;

    public HistorialDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    public List<Map<String, Object>> obtenerHistorialCompleto() {
        List<Map<String, Object>> historial = new ArrayList<>();

        String sql
                = // Compras
                "SELECT 'COMPRA' as tipo_operacion, "
                + "c.id_compra as id_operacion, p.nombre as nombre_producto, "
                + "c.cantidad, l.costo_unitario, (l.costo_unitario * 0.13) as iva, c.costo_total, "
                + "NULL as razon, c.fecha_compra as fecha "
                + "FROM compras c "
                + "JOIN productos p ON c.id_producto = p.id_producto "
                + "JOIN lotes l ON c.id_lote = l.id_lote "
                + "UNION ALL "
                + // Ventas - Usando movimientos_inventario para obtener el costo real
                "SELECT 'VENTA' as tipo_operacion, "
                + "v.id_venta as id_operacion, p.nombre as nombre_producto, "
                + "v.cantidad, mi.costo_unitario, mi.iva, "
                + "(v.cantidad * mi.costo_unitario) as costo_total, "
                + "NULL as razon, v.fecha_venta as fecha "
                + "FROM ventas v "
                + "JOIN productos p ON v.id_producto = p.id_producto "
                + "JOIN movimientos_inventario mi ON mi.id_producto = v.id_producto "
                + "AND mi.id_lote = v.id_lote "
                + "AND mi.tipo_movimiento = 'venta' "
                + "UNION ALL "
                + // Devoluciones
                "SELECT "
                + "CASE "
                + "   WHEN d.tipo_devolucion = 'venta' THEN 'DEVOLUCIÓN VENTA' "
                + "   WHEN d.tipo_devolucion = 'compra' THEN 'DEVOLUCIÓN COMPRA' "
                + "   WHEN d.tipo_devolucion = 'defectuoso' THEN 'DEVOLUCIÓN DEFECTUOSO VENTA' "
                + "END as tipo_operacion, "
                + "d.id_devolucion as id_operacion, p.nombre as nombre_producto, "
                + "d.cantidad, l.costo_unitario, (l.costo_unitario * 0.13) as iva, "
                + "(d.cantidad * l.costo_unitario) as costo_total, "
                + "d.razon, d.fecha_devolucion as fecha "
                + "FROM devoluciones d "
                + "JOIN productos p ON d.id_producto = p.id_producto "
                + "JOIN lotes l ON d.id_lote = l.id_lote "
                + "ORDER BY fecha DESC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> movimiento = new HashMap<>();
                movimiento.put("tipoOperacion", rs.getString("tipo_operacion"));
                movimiento.put("idOperacion", rs.getInt("id_operacion"));
                movimiento.put("nombreProducto", rs.getString("nombre_producto"));
                movimiento.put("cantidad", rs.getInt("cantidad"));
                movimiento.put("costoUnitario", rs.getDouble("costo_unitario"));
                movimiento.put("iva", rs.getDouble("iva"));
                movimiento.put("costoTotal", rs.getDouble("costo_total"));
                movimiento.put("razon", rs.getString("razon"));
                movimiento.put("fecha", rs.getTimestamp("fecha"));
                historial.add(movimiento);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }

        return historial;
    }
}
