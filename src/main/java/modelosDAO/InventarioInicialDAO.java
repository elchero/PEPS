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

public class InventarioInicialDAO {

    private Connection con;

    public InventarioInicialDAO() throws ClassNotFoundException {
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
    public Compras obtenerInventarioInicial() {
        Compras inventarioInicial = null;
        String sql = "SELECT \n"
                + "    c.id_compra,\n"
                + "    p.nombre,\n"
                + "    c.id_lote,\n"
                + "    c.cantidad,\n"
                + "    l.costo_unitario,\n"
                + "    c.costo_total,\n"
                + "    c.fecha_compra\n"
                + "FROM \n"
                + "    compras c\n"
                + "JOIN \n"
                + "    lotes l ON c.id_lote = l.id_lote\n"
                + "JOIN \n"
                + "    lote_inventario li ON l.id_lote = li.id_lote\n"
                + "JOIN \n"
                + "    productos p ON c.id_producto = p.id_producto\n"
                + "WHERE \n"
                + "    c.fecha_compra = (\n"
                + "        SELECT \n"
                + "            MIN(fecha_compra)\n"
                + "        FROM \n"
                + "            compras\n"
                + "    )\n"
                + "LIMIT 1;";

        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                inventarioInicial = new Compras();
                inventarioInicial.setId_compra(rs.getInt("id_compra"));
                inventarioInicial.setNombre(rs.getString("nombre"));
                inventarioInicial.setId_lote(rs.getInt("id_lote"));
                inventarioInicial.setCantidad(rs.getInt("cantidad"));
                inventarioInicial.setCosto_unitario(rs.getDouble("costo_unitario"));
                inventarioInicial.setCosto_total(rs.getDouble("costo_total"));
                inventarioInicial.setFecha_compra(rs.getTimestamp("fecha_compra"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener inventario inicial: " + e.getMessage());
        }

        return inventarioInicial;
    }

    public List<Compras> listarCompras() {
        List<Compras> compras = new ArrayList<>();
        String sql = "SELECT c.id_compra, p.nombre AS nombre_producto, c.id_lote, c.cantidad, c.costo_total, c.fecha_compra "
                + "FROM compras c "
                + "JOIN productos p ON c.id_producto = p.id_producto";

        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Compras compra = new Compras();
                compra.setId_compra(rs.getInt("id_compra"));
                compra.setNombre(rs.getString("nombre"));
                compra.setId_lote(rs.getInt("id_lote"));
                compra.setCantidad(rs.getInt("cantidad"));
                compra.setCosto_total(rs.getDouble("costo_total"));
                compra.setFecha_compra(rs.getTimestamp("fecha_compra"));
                compras.add(compra);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
        return compras;
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
        // Primero verificamos si hay compras posteriores
        String sqlVerificarCompras = "SELECT COUNT(*) FROM compras WHERE fecha_compra > (SELECT fecha_compra FROM compras WHERE id_compra = ?)";

        try {
            // Verificar si existen compras posteriores
            try (PreparedStatement psVerificar = con.prepareStatement(sqlVerificarCompras)) {
                psVerificar.setInt(1, idCompra);
                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Si hay compras posteriores, no permitir la eliminación
                        return false;
                    }
                }
            }

            // Si no hay compras posteriores, procedemos con la eliminación
            con.setAutoCommit(false);

            String sqlEliminarCompra = "DELETE FROM compras WHERE id_compra = ?";
            String sqlEliminarLoteInventario = "DELETE FROM lote_inventario WHERE id_lote = ?";
            String sqlEliminarLote = "DELETE FROM lotes WHERE id_lote = ?";

            // Eliminar la compra de 'compras'
            try (PreparedStatement psEliminarCompra = con.prepareStatement(sqlEliminarCompra)) {
                psEliminarCompra.setInt(1, idCompra);
                psEliminarCompra.executeUpdate();
            }

            // Eliminar el registro de inventario en 'lote_inventario'
            try (PreparedStatement psEliminarLoteInventario = con.prepareStatement(sqlEliminarLoteInventario)) {
                psEliminarLoteInventario.setInt(1, idLote);
                psEliminarLoteInventario.executeUpdate();
            }

            // Eliminar el lote de 'lotes'
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
}
