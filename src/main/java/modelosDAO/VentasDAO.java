package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelos.Ventas;
import otras_funcionalidades.LoteDisponible;

public class VentasDAO {

    private Connection con;

    public VentasDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    public List<LoteDisponible> obtenerLotesDisponibles(int idProducto, int cantidadRequerida) {
        List<LoteDisponible> lotesDisponibles = new ArrayList<>();
        String sql = "SELECT l.id_lote, l.costo_unitario, li.cantidad_disponible, l.fecha_ingreso "
                + "FROM lotes l "
                + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                + "WHERE l.id_producto = ? AND li.cantidad_disponible > 0 "
                + "ORDER BY l.fecha_ingreso ASC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            int cantidadAcumulada = 0;

            while (rs.next() && cantidadAcumulada < cantidadRequerida) {
                LoteDisponible lote = new LoteDisponible();
                lote.setId_lote(rs.getInt("id_lote"));
                lote.setCosto_unitario(rs.getDouble("costo_unitario"));
                lote.setCantidad_disponible(rs.getInt("cantidad_disponible"));
                lote.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));

                cantidadAcumulada += lote.getCantidad_disponible();
                lotesDisponibles.add(lote);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener lotes disponibles: " + e.getMessage());
        }
        return lotesDisponibles;
    }

    public boolean registrarVenta(Ventas venta, double precioVentaUnitario) {
        try {
            con.setAutoCommit(false);
            int cantidadPorVender = venta.getCantidad();
            List<LoteDisponible> lotesDisponibles = obtenerLotesDisponibles(venta.getId_producto(), cantidadPorVender);

            if (lotesDisponibles.isEmpty()) {
                throw new SQLException("No hay stock disponible para este producto");
            }

            // Verificar stock total
            int stockTotal = lotesDisponibles.stream()
                    .mapToInt(LoteDisponible::getCantidad_disponible)
                    .sum();
            if (stockTotal < cantidadPorVender) {
                throw new SQLException("Stock insuficiente. Stock disponible: " + stockTotal);
            }

            String sqlVenta = "INSERT INTO ventas (id_producto, id_lote, cantidad, precio_venta_unitario) VALUES (?, ?, ?, ?)";
            String sqlActualizarInventario = "UPDATE lote_inventario SET cantidad_disponible = cantidad_disponible - ? WHERE id_lote = ?";
            String sqlMovimiento = "INSERT INTO movimientos_inventario (tipo_movimiento, id_producto, id_lote, cantidad, costo_unitario, iva) VALUES (?, ?, ?, ?, ?, ?)";

            for (LoteDisponible lote : lotesDisponibles) {
                if (cantidadPorVender <= 0) {
                    break;
                }

                int cantidadDeLote = Math.min(cantidadPorVender, lote.getCantidad_disponible());

                // Registrar venta
                try (PreparedStatement psVenta = con.prepareStatement(sqlVenta)) {
                    psVenta.setInt(1, venta.getId_producto());
                    psVenta.setInt(2, lote.getId_lote());
                    psVenta.setInt(3, cantidadDeLote);
                    psVenta.setDouble(4, precioVentaUnitario);
                    psVenta.executeUpdate();
                }

                // Actualizar inventario
                try (PreparedStatement psInventario = con.prepareStatement(sqlActualizarInventario)) {
                    psInventario.setInt(1, cantidadDeLote);
                    psInventario.setInt(2, lote.getId_lote());
                    psInventario.executeUpdate();
                }

                // Registrar movimiento usando el costo del lote
                double iva = lote.getCosto_unitario() * 0.13; // IVA basado en el costo del lote
                try (PreparedStatement psMovimiento = con.prepareStatement(sqlMovimiento)) {
                    psMovimiento.setString(1, "venta");
                    psMovimiento.setInt(2, venta.getId_producto());
                    psMovimiento.setInt(3, lote.getId_lote());
                    psMovimiento.setInt(4, cantidadDeLote);
                    psMovimiento.setDouble(5, lote.getCosto_unitario()); // Usar costo del lote
                    psMovimiento.setDouble(6, iva);
                    psMovimiento.executeUpdate();
                }

                cantidadPorVender -= cantidadDeLote;
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar venta: " + e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restablecer autocommit: " + e.getMessage());
            }
        }
    }

// Corregir el método listarVentas
    public List<Ventas> listarVentas() {
        List<Ventas> ventas = new ArrayList<>();
        String sql = "SELECT v.*, p.nombre as nombre_producto "
                + // Corregir el alias
                "FROM ventas v "
                + "JOIN productos p ON v.id_producto = p.id_producto "
                + "ORDER BY v.fecha_venta DESC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ventas venta = new Ventas();
                venta.setId_venta(rs.getInt("id_venta"));
                venta.setId_producto(rs.getInt("id_producto"));
                venta.setId_lote(rs.getInt("id_lote"));
                venta.setCantidad(rs.getInt("cantidad"));
                venta.setPrecio_venta_unitario(rs.getDouble("precio_venta_unitario"));
                venta.setFecha_venta(rs.getTimestamp("fecha_venta"));
                venta.setNombre(rs.getString("nombre_producto")); // Usar el alias correcto
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }
        return ventas;
    }

    public boolean eliminarVenta(int idVenta) {
        try {
            con.setAutoCommit(false);

            // Primero obtener los detalles de la venta antes de eliminarla
            String sqlObtenerVenta = "SELECT id_producto, id_lote, cantidad FROM ventas WHERE id_venta = ?";
            int idProducto = 0;
            int idLote = 0;
            int cantidad = 0;

            try (PreparedStatement psObtener = con.prepareStatement(sqlObtenerVenta)) {
                psObtener.setInt(1, idVenta);
                ResultSet rs = psObtener.executeQuery();
                if (rs.next()) {
                    idProducto = rs.getInt("id_producto");
                    idLote = rs.getInt("id_lote");
                    cantidad = rs.getInt("cantidad");
                } else {
                    throw new SQLException("No se encontró la venta");
                }
            }

            // Restaurar el inventario
            String sqlRestaurarInventario = "UPDATE lote_inventario SET cantidad_disponible = cantidad_disponible + ? WHERE id_lote = ?";
            try (PreparedStatement psRestaurar = con.prepareStatement(sqlRestaurarInventario)) {
                psRestaurar.setInt(1, cantidad);
                psRestaurar.setInt(2, idLote);
                psRestaurar.executeUpdate();
            }

            // Eliminar el movimiento de inventario relacionado
            String sqlEliminarMovimiento = "DELETE FROM movimientos_inventario WHERE id_lote = ? AND tipo_movimiento = 'venta'";
            try (PreparedStatement psEliminarMov = con.prepareStatement(sqlEliminarMovimiento)) {
                psEliminarMov.setInt(1, idLote);
                psEliminarMov.executeUpdate();
            }

            // Finalmente eliminar la venta
            String sqlEliminarVenta = "DELETE FROM ventas WHERE id_venta = ?";
            try (PreparedStatement psEliminar = con.prepareStatement(sqlEliminarVenta)) {
                psEliminar.setInt(1, idVenta);
                psEliminar.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al eliminar venta: " + e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restablecer autocommit: " + e.getMessage());
            }
        }
    }
}
