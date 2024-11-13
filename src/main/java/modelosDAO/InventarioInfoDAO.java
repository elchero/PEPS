package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelos.Compras;

public class InventarioInfoDAO {

    private Connection con;

    public InventarioInfoDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    public List<Compras> listarInventarioInfo() {
        List<Compras> listaInventarioInfo = new ArrayList<>();
        String sql = "SELECT "
                + "c.id_compra, "
                + "p.nombre, "
                + "c.id_lote, "
                + "c.cantidad, "
                + "l.costo_unitario, "
                + "c.costo_total, "
                + "li.cantidad_disponible, "
                + "c.fecha_compra, "
                + "CASE "
                + "WHEN c.fecha_compra = (SELECT MIN(fecha_compra) FROM compras) THEN 'Inventario Inicial' "
                + "ELSE 'Compra' "
                + "END AS tipo_movimiento "
                + "FROM compras c "
                + "JOIN lotes l ON c.id_lote = l.id_lote "
                + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                + "JOIN productos p ON c.id_producto = p.id_producto";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Crear objeto Compra y asignar valores recuperados
                Compras compra = new Compras(
                        rs.getInt("id_compra"), // Recupera el id_compra
                        rs.getInt("id_lote"),
                        rs.getInt("cantidad"),
                        rs.getDouble("costo_total"),
                        rs.getTimestamp("fecha_compra")
                );
                compra.setId_compra(rs.getInt("id_compra"));
                compra.setNombre(rs.getString("nombre"));
                compra.setCosto_unitario(rs.getDouble("costo_unitario"));
                compra.setCantidad_disponible(rs.getInt("cantidad_disponible"));
                compra.setTipo_movimiento(rs.getString("tipo_movimiento"));
                listaInventarioInfo.add(compra);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
        return listaInventarioInfo;
    }
}
