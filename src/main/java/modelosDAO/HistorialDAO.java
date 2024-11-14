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
                "SELECT 'COMPRA' as tipo_operacion,\n"
                + "       c.id_compra as id_operacion,\n"
                + "       p.nombre as nombre_producto,\n"
                + "       CONCAT('Lote #', c.id_lote) as numero_lote,\n"
                + "       c.cantidad,\n"
                + "       l.costo_unitario,\n"
                + "       (l.costo_unitario * 0.13) as iva,\n"
                + "       c.costo_total,\n"
                + "       NULL as razon,\n"
                + "       c.fecha_compra as fecha\n"
                + "FROM compras c\n"
                + "JOIN productos p ON c.id_producto = p.id_producto\n"
                + "JOIN lotes l ON c.id_lote = l.id_lote\n"
                + "\n"
                + "UNION ALL\n"
                + "\n"
                + "SELECT 'VENTA' as tipo_operacion,\n"
                + "       v.id_venta as id_operacion,\n"
                + "       p.nombre as nombre_producto,\n"
                + "       CONCAT('Lote #', v.id_lote) as numero_lote,\n"
                + "       v.cantidad,\n"
                + "       l.costo_unitario,\n"
                + "       (l.costo_unitario * 0.13) as iva,\n"
                + "       (v.cantidad * l.costo_unitario) as costo_total,\n"
                + "       NULL as razon,\n"
                + "       v.fecha_venta as fecha\n"
                + "FROM ventas v\n"
                + "JOIN productos p ON v.id_producto = p.id_producto\n"
                + "JOIN lotes l ON v.id_lote = l.id_lote\n"
                + "\n"
                + "UNION ALL\n"
                + "\n"
                + "SELECT \n"
                + "    CASE \n"
                + "        WHEN d.tipo_devolucion = 'venta' THEN 'DEVOLUCIÓN VENTA'\n"
                + "        WHEN d.tipo_devolucion = 'compra' THEN 'DEVOLUCIÓN COMPRA'\n"
                + "        WHEN d.tipo_devolucion = 'defectuoso' THEN 'DEVOLUCIÓN DEFECTUOSO VENTA'\n"
                + "    END as tipo_operacion,\n"
                + "    d.id_devolucion as id_operacion,\n"
                + "    p.nombre as nombre_producto,\n"
                + "    CONCAT('Lote #', d.id_lote) as numero_lote,\n"
                + "    d.cantidad,\n"
                + "    l.costo_unitario,\n"
                + "    (l.costo_unitario * 0.13) as iva,\n"
                + "    (d.cantidad * l.costo_unitario) as costo_total,\n"
                + "    d.razon,\n"
                + "    d.fecha_devolucion as fecha\n"
                + "FROM devoluciones d\n"
                + "JOIN productos p ON d.id_producto = p.id_producto\n"
                + "JOIN lotes l ON d.id_lote = l.id_lote\n"
                + "\n"
                + "ORDER BY fecha DESC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> movimiento = new HashMap<>();
                movimiento.put("tipoOperacion", rs.getString("tipo_operacion"));
                movimiento.put("idOperacion", rs.getInt("id_operacion"));
                movimiento.put("nombreProducto", rs.getString("nombre_producto"));
                movimiento.put("numeroLote", rs.getString("numero_lote")); // Nuevo campo
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
