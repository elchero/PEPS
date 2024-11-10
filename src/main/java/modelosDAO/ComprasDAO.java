package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import modelos.Compras;
import modelos.Lotes;
import modelos.Productos;

public class ComprasDAO {

    private Connection con;

    public ComprasDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    // Método para registrar una compra y crear un lote
    public boolean registrarCompraYCrearLote(Compras compra, Lotes lote) {
        // Verificar registro previo
        String sqlVerificar = "SELECT COUNT(*) FROM compras";

        try {
            // Verificar registros previos
            try (PreparedStatement psVerificar = con.prepareStatement(sqlVerificar)) {
                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        return false;
                    }
                }
            }

            con.setAutoCommit(false);

            String sqlCompra = "INSERT INTO compras (id_producto, id_lote, cantidad, costo_total, fecha_compra) VALUES (?, ?, ?, ?, ?)";
            String sqlLote = "INSERT INTO lotes (id_producto, costo_unitario, fecha_ingreso) VALUES (?, ?, ?)";
            String sqlLoteInventario = "INSERT INTO lote_inventario (id_lote, cantidad_total, cantidad_disponible) VALUES (?, ?, ?)";
            String sqlMovimiento = "INSERT INTO movimientos_inventario (tipo_movimiento, id_producto, id_lote, cantidad, costo_unitario, iva, fecha_movimiento) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Calcular el IVA (13% del costo unitario)
            double iva = lote.getCosto_unitario() * 0.13;

            // Crear el lote
            int idLote;
            try (PreparedStatement psLote = con.prepareStatement(sqlLote, Statement.RETURN_GENERATED_KEYS)) {
                psLote.setInt(1, lote.getId_producto());
                psLote.setDouble(2, lote.getCosto_unitario());
                psLote.setTimestamp(3, lote.getFecha_ingreso());
                psLote.executeUpdate();

                ResultSet rsLote = psLote.getGeneratedKeys();
                if (rsLote.next()) {
                    idLote = rsLote.getInt(1);
                    lote.setId_lote(idLote);
                    compra.setId_lote(idLote);
                } else {
                    throw new SQLException("No se pudo obtener el ID del nuevo lote");
                }
            }

            // Registrar la compra
            double costoTotal = compra.getCantidad() * (lote.getCosto_unitario() + iva);
            try (PreparedStatement psCompra = con.prepareStatement(sqlCompra)) {
                psCompra.setInt(1, compra.getId_producto());
                psCompra.setInt(2, compra.getId_lote());
                psCompra.setInt(3, compra.getCantidad());
                psCompra.setDouble(4, costoTotal);
                psCompra.setTimestamp(5, compra.getFecha_compra());
                psCompra.executeUpdate();
            }

            // Registrar en lote_inventario
            try (PreparedStatement psLoteInventario = con.prepareStatement(sqlLoteInventario)) {
                psLoteInventario.setInt(1, lote.getId_lote());
                psLoteInventario.setInt(2, compra.getCantidad());
                psLoteInventario.setInt(3, compra.getCantidad());
                psLoteInventario.executeUpdate();
            }

            // Registrar el movimiento
            try (PreparedStatement psMovimiento = con.prepareStatement(sqlMovimiento)) {
                psMovimiento.setString(1, "compra");
                psMovimiento.setInt(2, compra.getId_producto());
                psMovimiento.setInt(3, compra.getId_lote());
                psMovimiento.setInt(4, compra.getCantidad());
                psMovimiento.setDouble(5, lote.getCosto_unitario());
                psMovimiento.setDouble(6, iva);
                psMovimiento.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                psMovimiento.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error en rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error en registrar compra y crear lote: " + e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Error al restablecer auto-commit: " + ex.getMessage());
            }
        }
    }

    // Método para listar todas las compras
    public List<Compras> listarCompras() {
        List<Compras> listaCompras = new ArrayList<>();
        String sql = "SELECT "
                + "c.id_compra, "
                + "p.nombre, "
                + "c.id_lote, "
                + "c.cantidad, "
                + "l.costo_unitario, "
                + "c.costo_total, "
                + "c.fecha_compra, "
                + "c.id_producto "
                + "FROM compras c "
                + "JOIN lotes l ON c.id_lote = l.id_lote "
                + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                + "JOIN productos p ON c.id_producto = p.id_producto "
                + "WHERE c.fecha_compra > (SELECT MIN(fecha_compra) FROM compras)";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Compras compra = new Compras(
                        rs.getInt("id_compra"),
                        rs.getInt("id_producto"),
                        rs.getInt("id_lote"),
                        rs.getInt("cantidad"),
                        rs.getDouble("costo_total"),
                        rs.getTimestamp("fecha_compra")
                );
                compra.setNombre(rs.getString("nombre"));
                compra.setCosto_unitario(rs.getDouble("costo_unitario"));
                listaCompras.add(compra);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
        return listaCompras;
    }

    public List<Productos> listarProductos() {
        List<Productos> listaProductos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre FROM productos";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Productos producto = new Productos();
                producto.setId_producto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return listaProductos;
    }

    public boolean eliminarCompraYRelacionados(int idCompra, int idLote) {
        String sqlEliminarMovimiento = "DELETE FROM movimientos_inventario WHERE id_lote = ? AND tipo_movimiento = 'compra'";
        String sqlEliminarCompra = "DELETE FROM compras WHERE id_compra = ?";
        String sqlEliminarLoteInventario = "DELETE FROM lote_inventario WHERE id_lote = ?";
        String sqlEliminarLote = "DELETE FROM lotes WHERE id_lote = ?";

        try {
            con.setAutoCommit(false);

            // Primero eliminar el movimiento
            try (PreparedStatement psEliminarMovimiento = con.prepareStatement(sqlEliminarMovimiento)) {
                psEliminarMovimiento.setInt(1, idLote);
                psEliminarMovimiento.executeUpdate();
            }

            // Luego eliminar la compra
            try (PreparedStatement psEliminarCompra = con.prepareStatement(sqlEliminarCompra)) {
                psEliminarCompra.setInt(1, idCompra);
                psEliminarCompra.executeUpdate();
            }

            // Eliminar el registro de inventario
            try (PreparedStatement psEliminarLoteInventario = con.prepareStatement(sqlEliminarLoteInventario)) {
                psEliminarLoteInventario.setInt(1, idLote);
                psEliminarLoteInventario.executeUpdate();
            }

            // Por último eliminar el lote
            try (PreparedStatement psEliminarLote = con.prepareStatement(sqlEliminarLote)) {
                psEliminarLote.setInt(1, idLote);
                psEliminarLote.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error en rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error al eliminar compra y registros relacionados: " + e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Error al restablecer auto-commit: " + ex.getMessage());
            }
        }
    }

//Metodo para mostrar inventario, usado en otro servlet llamado inventarioInicialServlet
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
