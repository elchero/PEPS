package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelos.Devoluciones;
import otras_funcionalidades.VentaDevolucion;

public class DevolucionesDAO {

    private Connection con;

    public DevolucionesDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    public boolean registrarDevolucion(Devoluciones devolucion, String tipoDevolucion) {
        try {
            con.setAutoCommit(false);

            // Verificar si el lote existe y obtener su costo unitario
            String sqlObtenerLote = "SELECT l.costo_unitario FROM lotes l WHERE l.id_lote = ?";
            double costoUnitario = 0;

            try (PreparedStatement psLote = con.prepareStatement(sqlObtenerLote)) {
                psLote.setInt(1, devolucion.getId_lote());
                ResultSet rs = psLote.executeQuery();
                if (rs.next()) {
                    costoUnitario = rs.getDouble("costo_unitario");
                } else {
                    throw new SQLException("Lote no encontrado");
                }
            }

            // Registrar la devolución
            String sqlDevolucion = "INSERT INTO devoluciones (id_producto, id_lote, cantidad, tipo_devolucion, razon) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psDevolucion = con.prepareStatement(sqlDevolucion)) {
                psDevolucion.setInt(1, devolucion.getId_producto());
                psDevolucion.setInt(2, devolucion.getId_lote());
                psDevolucion.setInt(3, devolucion.getCantidad());
                psDevolucion.setString(4, tipoDevolucion);
                psDevolucion.setString(5, devolucion.getRazon());
                psDevolucion.executeUpdate();
            }

            // Si no es defectuoso, actualizar inventario
            if (!"defectuoso".equals(tipoDevolucion)) {
                String sqlActualizarInventario = "UPDATE lote_inventario SET cantidad_disponible = cantidad_disponible + ? WHERE id_lote = ?";
                try (PreparedStatement psInventario = con.prepareStatement(sqlActualizarInventario)) {
                    psInventario.setInt(1, devolucion.getCantidad());
                    psInventario.setInt(2, devolucion.getId_lote());
                    psInventario.executeUpdate();
                }
            }

            // Registrar movimiento de inventario
            String sqlMovimiento = "INSERT INTO movimientos_inventario (tipo_movimiento, id_producto, id_lote, cantidad, costo_unitario, iva) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psMovimiento = con.prepareStatement(sqlMovimiento)) {
                psMovimiento.setString(1, "devolucion");
                psMovimiento.setInt(2, devolucion.getId_producto());
                psMovimiento.setInt(3, devolucion.getId_lote());
                psMovimiento.setInt(4, devolucion.getCantidad());
                psMovimiento.setDouble(5, costoUnitario);
                psMovimiento.setDouble(6, costoUnitario * 0.13);
                psMovimiento.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar devolución: " + e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restablecer autocommit: " + e.getMessage());
            }
        }
    }

    public List<Devoluciones> listarDevoluciones() {
        List<Devoluciones> devoluciones = new ArrayList<>();
        String sql = "SELECT d.*, p.nombre as nombre_producto "
                + "FROM devoluciones d "
                + "JOIN productos p ON d.id_producto = p.id_producto "
                + "ORDER BY d.fecha_devolucion DESC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Devoluciones devolucion = new Devoluciones();
                devolucion.setId_devolucion(rs.getInt("id_devolucion"));
                devolucion.setId_producto(rs.getInt("id_producto"));
                devolucion.setId_lote(rs.getInt("id_lote"));
                devolucion.setCantidad(rs.getInt("cantidad"));
                devolucion.setTipo_devolucion(rs.getString("tipo_devolucion"));
                devolucion.setRazon(rs.getString("razon"));
                devolucion.setFecha_devolucion(rs.getTimestamp("fecha_devolucion"));
                devolucion.setNombre_producto(rs.getString("nombre_producto"));
                devoluciones.add(devolucion);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar devoluciones: " + e.getMessage());
        }
        return devoluciones;
    }

    public List<VentaDevolucion> obtenerVentasParaDevolucion() {
        List<VentaDevolucion> ventas = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.id_producto, v.id_lote, v.cantidad, "
                + "v.fecha_venta, p.nombre as nombre_producto "
                + "FROM ventas v "
                + "JOIN productos p ON v.id_producto = p.id_producto "
                + "ORDER BY v.fecha_venta DESC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VentaDevolucion venta = new VentaDevolucion();
                venta.setId_venta(rs.getInt("id_venta"));
                venta.setId_producto(rs.getInt("id_producto"));
                venta.setId_lote(rs.getInt("id_lote"));
                venta.setCantidad(rs.getInt("cantidad"));
                venta.setFecha_venta(rs.getTimestamp("fecha_venta"));
                venta.setNombre_producto(rs.getString("nombre_producto"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return ventas;
    }
}
