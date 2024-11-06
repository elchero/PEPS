package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String sqlCompra = "INSERT INTO compras (id_producto, id_lote, cantidad, costo_total, fecha_compra) VALUES (?, ?, ?, ?, ?)";
        String sqlLote = "INSERT INTO lotes (id_producto, costo_unitario, fecha_ingreso) VALUES (?, ?, ?)";
        String sqlLoteInventario = "INSERT INTO lote_inventario (id_lote, cantidad_total, cantidad_disponible) VALUES (?, ?, ?)";

        try {
            con.setAutoCommit(false);

            // Crear el lote en 'lotes' y obtener su id
            try (PreparedStatement psLote = con.prepareStatement(sqlLote, Statement.RETURN_GENERATED_KEYS)) {
                psLote.setInt(1, lote.getId_producto());
                psLote.setDouble(2, lote.getCosto_unitario());
                psLote.setTimestamp(3, lote.getFecha_ingreso());
                psLote.executeUpdate();

                ResultSet rsLote = psLote.getGeneratedKeys();
                if (rsLote.next()) {
                    int idLote = rsLote.getInt(1);
                    lote.setId_lote(idLote); // Guardar el ID generado
                    compra.setId_lote(idLote);
                } else {
                    throw new SQLException("No se pudo obtener el ID del nuevo lote");
                }
            }

            // Registrar la compra en 'compras'
            try (PreparedStatement psCompra = con.prepareStatement(sqlCompra)) {
                psCompra.setInt(1, compra.getId_producto());
                psCompra.setInt(2, compra.getId_lote());
                psCompra.setInt(3, compra.getCantidad());
                psCompra.setDouble(4, compra.getCantidad() * lote.getCosto_unitario()); // Calcular el costo total
                psCompra.setTimestamp(5, compra.getFecha_compra());
                psCompra.executeUpdate();
            }

            // Insertar o actualizar la información del inventario en 'lote_inventario'
            try (PreparedStatement psLoteInventario = con.prepareStatement(sqlLoteInventario)) {
                psLoteInventario.setInt(1, lote.getId_lote());
                psLoteInventario.setInt(2, compra.getCantidad());
                psLoteInventario.setInt(3, compra.getCantidad());
                psLoteInventario.executeUpdate();
            }

            // Confirmar la transacción
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
        String sql = "SELECT c.id_compra, c.id_producto, p.nombre, c.id_lote, c.cantidad, c.costo_total, c.fecha_compra "
                + "FROM compras c "
                + "JOIN productos p ON c.id_producto = p.id_producto";

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
                listaCompras.add(compra);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
        return listaCompras;
    }

    public List<Lotes> listarLotes() {
        List<Lotes> listaLotes = new ArrayList<>();
        String sql = "SELECT l.id_lote, l.id_producto, p.nombre, l.costo_unitario, l.fecha_ingreso "
                + "FROM lotes l "
                + "JOIN productos p ON l.id_producto = p.id_producto";  // Corregí el nombre de la columna en el JOIN

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Crear objeto Lotes
                Lotes lote = new Lotes();
                lote.setId_lote(rs.getInt("id_lote"));
                lote.setId_producto(rs.getInt("id_producto"));
                lote.setCosto_unitario(rs.getDouble("costo_unitario"));
                lote.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));
                lote.setNombre(rs.getString("nombre"));

                // Agregar el objeto lote a la lista
                listaLotes.add(lote);
            }
        } catch (Exception e) {
            System.err.println("Error al listar lotes: " + e.getMessage());
        }
        return listaLotes;
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

}
