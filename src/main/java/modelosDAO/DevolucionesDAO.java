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
import otras_funcionalidades.CompraDevolucion;
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

            // 1. Verificar si el lote existe y obtener información necesaria
            String sqlVerificarLote = "SELECT l.costo_unitario, l.fecha_ingreso, li.cantidad_disponible, "
                    + "(SELECT SUM(cantidad) FROM ventas WHERE id_lote = l.id_lote) as cantidad_vendida "
                    + "FROM lotes l "
                    + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                    + "WHERE l.id_lote = ?";

            double costoUnitario = 0;
            int cantidadDisponible = 0;
            int cantidadVendida = 0;

            try (PreparedStatement psLote = con.prepareStatement(sqlVerificarLote)) {
                psLote.setInt(1, devolucion.getId_lote());
                ResultSet rs = psLote.executeQuery();
                if (rs.next()) {
                    costoUnitario = rs.getDouble("costo_unitario");
                    cantidadDisponible = rs.getInt("cantidad_disponible");
                    cantidadVendida = rs.getInt("cantidad_vendida");
                } else {
                    throw new SQLException("Lote no encontrado");
                }
            }

            // 2. Validar la cantidad a devolver
            if (devolucion.getCantidad() > cantidadVendida) {
                throw new SQLException("La cantidad a devolver es mayor que la cantidad vendida del lote");
            }

            // 3. Verificar si hay lotes más antiguos con ventas pendientes (PEPS)
            String sqlVerificarPEPS = "SELECT COUNT(*) as lotes_antiguos "
                    + "FROM lotes l "
                    + "JOIN ventas v ON l.id_lote = v.id_lote "
                    + "WHERE l.id_producto = ? "
                    + "AND l.fecha_ingreso < (SELECT fecha_ingreso FROM lotes WHERE id_lote = ?) "
                    + "AND NOT EXISTS (SELECT 1 FROM devoluciones d WHERE d.id_lote = l.id_lote)";

            try (PreparedStatement psPEPS = con.prepareStatement(sqlVerificarPEPS)) {
                psPEPS.setInt(1, devolucion.getId_producto());
                psPEPS.setInt(2, devolucion.getId_lote());
                ResultSet rs = psPEPS.executeQuery();
                if (rs.next() && rs.getInt("lotes_antiguos") > 0) {
                    throw new SQLException("Existen lotes más antiguos que deben ser devueltos primero (PEPS)");
                }
            }

            // 4. Verificar si existen ventas más antiguas pendientes de devolución
            String sqlVerificarDevolucionesPrevias
                    = "SELECT COUNT(*) as devoluciones_pendientes "
                    + "FROM ventas v "
                    + "JOIN lotes l ON v.id_lote = l.id_lote "
                    + "WHERE v.id_producto = ? "
                    + "AND l.fecha_ingreso <= (SELECT fecha_ingreso FROM lotes WHERE id_lote = ?) "
                    + "AND v.fecha_venta < (SELECT fecha_venta FROM ventas WHERE id_lote = ? AND cantidad = ?) "
                    + "AND NOT EXISTS (SELECT 1 FROM devoluciones d WHERE d.id_lote = v.id_lote)";

            try (PreparedStatement psVerificar = con.prepareStatement(sqlVerificarDevolucionesPrevias)) {
                psVerificar.setInt(1, devolucion.getId_producto());
                psVerificar.setInt(2, devolucion.getId_lote());
                psVerificar.setInt(3, devolucion.getId_lote());
                psVerificar.setInt(4, devolucion.getCantidad());
                ResultSet rs = psVerificar.executeQuery();
                if (rs.next() && rs.getInt("devoluciones_pendientes") > 0) {
                    throw new SQLException("Existen ventas más antiguas que deben ser devueltas primero (PEPS)");
                }
            }

            // 5. Registrar la devolución
            String sqlDevolucion = "INSERT INTO devoluciones (id_producto, id_lote, cantidad, tipo_devolucion, razon) "
                    + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psDevolucion = con.prepareStatement(sqlDevolucion)) {
                psDevolucion.setInt(1, devolucion.getId_producto());
                psDevolucion.setInt(2, devolucion.getId_lote());
                psDevolucion.setInt(3, devolucion.getCantidad());
                psDevolucion.setString(4, tipoDevolucion.equals("defectuoso") ? "defectuoso" : "venta");
                psDevolucion.setString(5, devolucion.getRazon());
                psDevolucion.executeUpdate();
            }

            // 6. Actualizar inventario solo si no es defectuoso
            if (!"defectuoso".equals(tipoDevolucion)) {
                String sqlActualizarInventario = "UPDATE lote_inventario "
                        + "SET cantidad_disponible = cantidad_disponible + ? "
                        + "WHERE id_lote = ?";
                try (PreparedStatement psInventario = con.prepareStatement(sqlActualizarInventario)) {
                    psInventario.setInt(1, devolucion.getCantidad());
                    psInventario.setInt(2, devolucion.getId_lote());
                    psInventario.executeUpdate();
                }
            }

            // 7. Registrar movimiento de inventario
            String sqlMovimiento = "INSERT INTO movimientos_inventario "
                    + "(tipo_movimiento, id_producto, id_lote, cantidad, costo_unitario, iva) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
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

                // Como tipo_operacion no está en la base de datos, lo inferimos del tipo_devolucion
                if (rs.getString("tipo_devolucion").equals("compra")) {
                    devolucion.setTipo_operacion("compra");
                } else {
                    devolucion.setTipo_operacion("venta");
                }

                devoluciones.add(devolucion);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar devoluciones: " + e.getMessage());
        }
        return devoluciones;
    }

    public List<VentaDevolucion> obtenerVentasParaDevolucion() {
        List<VentaDevolucion> ventas = new ArrayList<>();
        String sql
                = "SELECT v.id_venta, v.id_producto, v.id_lote, v.cantidad, "
                + "v.fecha_venta, p.nombre as nombre_producto, "
                + "COALESCE((SELECT SUM(d.cantidad) FROM devoluciones d "
                + "WHERE d.id_lote = v.id_lote AND d.tipo_devolucion IN ('venta', 'defectuoso')), 0) as total_devuelto "
                + "FROM ventas v "
                + "JOIN productos p ON v.id_producto = p.id_producto "
                + "JOIN lotes l ON v.id_lote = l.id_lote "
                + "WHERE v.cantidad > "
                + "(COALESCE((SELECT SUM(d.cantidad) FROM devoluciones d "
                + "WHERE d.id_lote = v.id_lote AND d.tipo_devolucion IN ('venta', 'defectuoso')), 0)) "
                + "ORDER BY l.fecha_ingreso ASC, v.fecha_venta ASC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VentaDevolucion venta = new VentaDevolucion();
                venta.setId_venta(rs.getInt("id_venta"));
                venta.setId_producto(rs.getInt("id_producto"));
                venta.setId_lote(rs.getInt("id_lote"));
                int cantidadOriginal = rs.getInt("cantidad");
                int totalDevuelto = rs.getInt("total_devuelto");
                venta.setCantidad(cantidadOriginal - totalDevuelto); // Cantidad disponible para devolución
                venta.setFecha_venta(rs.getTimestamp("fecha_venta"));
                venta.setNombre_producto(rs.getString("nombre_producto"));

                // Solo agregar si aún hay cantidad disponible para devolver
                if (cantidadOriginal > totalDevuelto) {
                    ventas.add(venta);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return ventas;
    }

    // Método para verificar disponibilidad PEPS
    private int verificarDisponibilidadPEPS(int idProducto, int cantidadRequerida) {
        String sql = "SELECT l.id_lote, li.cantidad_disponible "
                + "FROM lotes l "
                + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                + "WHERE l.id_producto = ? AND li.cantidad_disponible > 0 "
                + "ORDER BY l.fecha_ingreso ASC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            int cantidadDisponible = 0;
            while (rs.next() && cantidadDisponible < cantidadRequerida) {
                cantidadDisponible += rs.getInt("cantidad_disponible");
            }
            return cantidadDisponible;
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
            return 0;
        }
    }

    //Devolucion sobre compra
    public List<CompraDevolucion> obtenerComprasParaDevolucion() {
        List<CompraDevolucion> compras = new ArrayList<>();
        String sql = "SELECT c.id_compra, c.id_producto, c.id_lote, c.cantidad, "
                + "l.costo_unitario, l.fecha_ingreso, c.fecha_compra, p.nombre as nombre_producto, "
                + "li.cantidad_disponible "
                + "FROM compras c "
                + "JOIN productos p ON c.id_producto = p.id_producto "
                + "JOIN lotes l ON c.id_lote = l.id_lote "
                + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                + "WHERE li.cantidad_disponible > 0 "
                + "ORDER BY l.fecha_ingreso ASC, c.fecha_compra ASC";  // PEPS: primero entrado

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CompraDevolucion compra = new CompraDevolucion();
                compra.setId_compra(rs.getInt("id_compra"));
                compra.setId_producto(rs.getInt("id_producto"));
                compra.setId_lote(rs.getInt("id_lote"));
                compra.setCantidad(rs.getInt("cantidad"));
                compra.setCantidad_disponible(rs.getInt("cantidad_disponible"));
                compra.setCosto_unitario(rs.getDouble("costo_unitario"));
                compra.setFecha_compra(rs.getTimestamp("fecha_compra"));
                compra.setNombre_producto(rs.getString("nombre_producto"));
                compras.add(compra);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener compras: " + e.getMessage());
        }
        return compras;
    }

    public boolean registrarDevolucionCompra(Devoluciones devolucion) {
        try {
            con.setAutoCommit(false);

            // 1. Verificar lotes más antiguos (PEPS)
            String sqlVerificarPEPS = "SELECT COUNT(*) as lotes_antiguos "
                    + "FROM lotes l "
                    + "JOIN compras c ON l.id_lote = c.id_lote "
                    + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                    + "WHERE l.id_producto = ? "
                    + "AND l.fecha_ingreso < (SELECT fecha_ingreso FROM lotes WHERE id_lote = ?) "
                    + "AND li.cantidad_disponible > 0 "
                    + "AND NOT EXISTS (SELECT 1 FROM devoluciones d WHERE d.id_lote = l.id_lote)";

            try (PreparedStatement psPEPS = con.prepareStatement(sqlVerificarPEPS)) {
                psPEPS.setInt(1, devolucion.getId_producto());
                psPEPS.setInt(2, devolucion.getId_lote());
                ResultSet rs = psPEPS.executeQuery();
                if (rs.next() && rs.getInt("lotes_antiguos") > 0) {
                    throw new SQLException("Hay lotes más antiguos que deben ser devueltos primero (PEPS)");
                }
            }

            // 2. Verificar información del lote y disponibilidad
            String sqlVerificar = "SELECT li.cantidad_disponible, l.costo_unitario, "
                    + "c.cantidad as cantidad_comprada "
                    + "FROM lote_inventario li "
                    + "JOIN lotes l ON li.id_lote = l.id_lote "
                    + "JOIN compras c ON l.id_lote = c.id_lote "
                    + "WHERE l.id_lote = ?";

            int cantidadDisponible = 0;
            double costoUnitario = 0;
            int cantidadComprada = 0;

            try (PreparedStatement psVerificar = con.prepareStatement(sqlVerificar)) {
                psVerificar.setInt(1, devolucion.getId_lote());
                ResultSet rs = psVerificar.executeQuery();
                if (rs.next()) {
                    cantidadDisponible = rs.getInt("cantidad_disponible");
                    costoUnitario = rs.getDouble("costo_unitario");
                    cantidadComprada = rs.getInt("cantidad_comprada");
                } else {
                    throw new SQLException("Lote no encontrado");
                }
            }

            // 3. Validar cantidades
            if (cantidadDisponible < devolucion.getCantidad()) {
                throw new SQLException("Cantidad a devolver mayor que la disponible");
            }

            if (devolucion.getCantidad() > cantidadComprada) {
                throw new SQLException("Cantidad a devolver mayor que la cantidad comprada originalmente");
            }

            // 4. Verificar si ya existe una devolución para este lote
            String sqlVerificarDevolucion = "SELECT SUM(cantidad) as cantidad_devuelta "
                    + "FROM devoluciones "
                    + "WHERE id_lote = ?";

            try (PreparedStatement psVerificarDev = con.prepareStatement(sqlVerificarDevolucion)) {
                psVerificarDev.setInt(1, devolucion.getId_lote());
                ResultSet rs = psVerificarDev.executeQuery();
                if (rs.next()) {
                    int cantidadDevuelta = rs.getInt("cantidad_devuelta");
                    if (cantidadDevuelta + devolucion.getCantidad() > cantidadComprada) {
                        throw new SQLException("La cantidad total de devoluciones superaría la cantidad comprada");
                    }
                }
            }

            // 5. Registrar la devolución
            String sqlDevolucion = "INSERT INTO devoluciones (id_producto, id_lote, cantidad, tipo_devolucion, razon) "
                    + "VALUES (?, ?, ?, 'compra', ?)";
            try (PreparedStatement psDevolucion = con.prepareStatement(sqlDevolucion)) {
                psDevolucion.setInt(1, devolucion.getId_producto());
                psDevolucion.setInt(2, devolucion.getId_lote());
                psDevolucion.setInt(3, devolucion.getCantidad());
                psDevolucion.setString(4, devolucion.getRazon());
                psDevolucion.executeUpdate();
            }

            // 6. Actualizar inventario
            String sqlInventario = "UPDATE lote_inventario SET cantidad_disponible = cantidad_disponible - ? "
                    + "WHERE id_lote = ?";
            try (PreparedStatement psInventario = con.prepareStatement(sqlInventario)) {
                psInventario.setInt(1, devolucion.getCantidad());
                psInventario.setInt(2, devolucion.getId_lote());
                psInventario.executeUpdate();
            }

            // 7. Registrar movimiento de inventario
            String sqlMovimiento = "INSERT INTO movimientos_inventario "
                    + "(tipo_movimiento, id_producto, id_lote, cantidad, costo_unitario, iva) "
                    + "VALUES ('devolucion', ?, ?, ?, ?, ?)";
            try (PreparedStatement psMovimiento = con.prepareStatement(sqlMovimiento)) {
                psMovimiento.setInt(1, devolucion.getId_producto());
                psMovimiento.setInt(2, devolucion.getId_lote());
                psMovimiento.setInt(3, devolucion.getCantidad());
                psMovimiento.setDouble(4, costoUnitario);
                psMovimiento.setDouble(5, costoUnitario * 0.13);
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
            System.err.println("Error al registrar devolución de compra: " + e.getMessage());
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
