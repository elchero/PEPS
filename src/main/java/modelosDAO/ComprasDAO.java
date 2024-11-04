package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import modelos.Compras;
import modelos.Lotes;

public class ComprasDAO {

    private Connection con;

    public ComprasDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    // Método para registrar una compra y crear un lote
    public boolean registrarCompraYCrearLote(Compras compra, Lotes lote) {
        String sqlCompra = "INSERT INTO compras (id_lote, cantidad, costo_total, fecha_compra) VALUES (?, ?, ?, ?)";
        String sqlLote = "INSERT INTO lotes (id_producto, costo_unitario, fecha_ingreso) VALUES (?, ?, ?)";
        String sqlLoteInventario = "INSERT INTO lote_inventario (id_lote, cantidad_total, cantidad_disponible) VALUES (?, ?, ?)";

        try {
            con.setAutoCommit(false);

            // Crear el lote
            try (PreparedStatement psLote = con.prepareStatement(sqlLote, Statement.RETURN_GENERATED_KEYS)) {
                psLote.setInt(1, lote.getId_producto());
                psLote.setDouble(2, lote.getCosto_unitario());
                psLote.setDate(3, lote.getFecha_ingreso());
                psLote.executeUpdate();

                ResultSet rs = psLote.getGeneratedKeys();
                if (rs.next()) {
                    int idLote = rs.getInt(1);
                    lote.setId_lote(idLote); // Guardar el id generado
                    compra.setId_lote(idLote);
                }
            }

            // Insertar la compra
            try (PreparedStatement psCompra = con.prepareStatement(sqlCompra)) {
                psCompra.setInt(1, compra.getId_lote());
                psCompra.setInt(2, compra.getCantidad());
                psCompra.setDouble(3, compra.getCosto_total());
                psCompra.setDate(4, compra.getFecha_compra());
                psCompra.executeUpdate();
            }

            // Actualizar el lote en el inventario
            try (PreparedStatement psLoteInventario = con.prepareStatement(sqlLoteInventario)) {
                psLoteInventario.setInt(1, lote.getId_lote());
                psLoteInventario.setInt(2, compra.getCantidad());
                psLoteInventario.setInt(3, compra.getCantidad());
                psLoteInventario.executeUpdate();
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
}
