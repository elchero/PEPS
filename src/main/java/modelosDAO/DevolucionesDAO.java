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

    public boolean registrarDevolucion(Devoluciones devolucion, String tipoDevolucion) throws SQLException {
        try {
            con.setAutoCommit(false);

            // 1. Verificar el stock actual y la venta
            String sqlVerificarVenta = "SELECT \n"
                    + "                v.cantidad as cantidad_vendida,\n"
                    + "                COALESCE((\n"
                    + "                    SELECT SUM(d.cantidad)\n"
                    + "                    FROM devoluciones d\n"
                    + "                    WHERE d.id_lote = v.id_lote \n"
                    + "                    AND d.id_producto = v.id_producto\n"
                    + "                    AND d.tipo_devolucion IN ('venta', 'defectuoso')\n"
                    + "                    AND d.fecha_devolucion >= v.fecha_venta\n"
                    + "                ), 0) as cantidad_devuelta,\n"
                    + "                l.costo_unitario,\n"
                    + "                li.cantidad_disponible as stock_actual\n"
                    + "            FROM ventas v\n"
                    + "            JOIN lotes l ON v.id_lote = l.id_lote\n"
                    + "            JOIN lote_inventario li ON l.id_lote = li.id_lote\n"
                    + "            WHERE v.id_lote = ? AND v.id_producto = ?\n"
                    + "            AND v.fecha_venta = (\n"
                    + "                SELECT MIN(fecha_venta) \n"
                    + "                FROM ventas \n"
                    + "                WHERE id_lote = ? AND id_producto = ?\n"
                    + "            )";

            int cantidadVendida = 0;
            int cantidadDevuelta = 0;
            double costoUnitario = 0;
            int stockActual = 0;

            try (PreparedStatement psVenta = con.prepareStatement(sqlVerificarVenta)) {
                psVenta.setInt(1, devolucion.getId_lote());
                psVenta.setInt(2, devolucion.getId_producto());
                psVenta.setInt(3, devolucion.getId_lote());
                psVenta.setInt(4, devolucion.getId_producto());

                ResultSet rs = psVenta.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Venta no encontrada");
                }

                cantidadVendida = rs.getInt("cantidad_vendida");
                cantidadDevuelta = rs.getInt("cantidad_devuelta");
                costoUnitario = rs.getDouble("costo_unitario");
                stockActual = rs.getInt("stock_actual");
            }

            // 2. Validar la cantidad a devolver
            int cantidadDisponibleParaDevolver = cantidadVendida - cantidadDevuelta;
            if (devolucion.getCantidad() > cantidadDisponibleParaDevolver) {
                throw new SQLException("La cantidad a devolver (" + devolucion.getCantidad()
                        + ") excede la cantidad disponible para devolución (" + cantidadDisponibleParaDevolver + ")");
            }

            // 3. Registrar la devolución
            String tipoDevolucionFinal = "defectuoso".equals(tipoDevolucion) ? "defectuoso" : "venta";
            String sqlDevolucion = "INSERT INTO devoluciones (id_producto, id_lote, cantidad, tipo_devolucion, razon)\n"
                    + "            VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement psDevolucion = con.prepareStatement(sqlDevolucion)) {
                psDevolucion.setInt(1, devolucion.getId_producto());
                psDevolucion.setInt(2, devolucion.getId_lote());
                psDevolucion.setInt(3, devolucion.getCantidad());
                psDevolucion.setString(4, tipoDevolucionFinal);
                psDevolucion.setString(5, devolucion.getRazon());
                psDevolucion.executeUpdate();
            }

            // 4. Actualizar inventario si el producto no es defectuoso
            if (!"defectuoso".equals(tipoDevolucion)) {
                String sqlActualizarInventario = "UPDATE lote_inventario \n"
                        + "                SET cantidad_disponible = cantidad_disponible + ?\n"
                        + "                WHERE id_lote = ?";

                try (PreparedStatement psInventario = con.prepareStatement(sqlActualizarInventario)) {
                    psInventario.setInt(1, devolucion.getCantidad());
                    psInventario.setInt(2, devolucion.getId_lote());
                    int filasActualizadas = psInventario.executeUpdate();

                    if (filasActualizadas == 0) {
                        throw new SQLException("No se pudo actualizar el inventario");
                    }
                }
            }

            // 5. Registrar movimiento de inventario
            String sqlMovimiento = "  INSERT INTO movimientos_inventario \n"
                    + "            (tipo_movimiento, id_producto, id_lote, cantidad, costo_unitario, iva)\n"
                    + "            VALUES ('devolucion', ?, ?, ?, ?, ?)";

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
            throw e; // Relanzamos la excepción original
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restablecer autocommit: " + e.getMessage());
            }
        }
    }

    public List<VentaDevolucion> obtenerVentasParaDevolucion() {
        List<VentaDevolucion> ventas = new ArrayList<>();
        String sql = "WITH VentasConDevoluciones AS (\n"
                + "            SELECT \n"
                + "                v.id_venta,\n"
                + "                v.id_producto,\n"
                + "                v.id_lote,\n"
                + "                v.cantidad as cantidad_original,\n"
                + "                v.fecha_venta,\n"
                + "                COALESCE(SUM(d.cantidad), 0) as cantidad_devuelta\n"
                + "            FROM ventas v\n"
                + "            LEFT JOIN devoluciones d ON v.id_lote = d.id_lote \n"
                + "                AND v.id_producto = d.id_producto\n"
                + "                AND d.tipo_devolucion IN ('venta', 'defectuoso')\n"
                + "                AND d.fecha_devolucion >= v.fecha_venta\n"
                + "            GROUP BY v.id_venta, v.id_producto, v.id_lote, v.cantidad, v.fecha_venta\n"
                + "        )\n"
                + "        SELECT \n"
                + "            v.id_venta,\n"
                + "            v.id_producto,\n"
                + "            v.id_lote,\n"
                + "            v.cantidad_original,\n"
                + "            v.cantidad_devuelta,\n"
                + "            (v.cantidad_original - v.cantidad_devuelta) as cantidad_disponible,\n"
                + "            v.fecha_venta,\n"
                + "            p.nombre as nombre_producto,\n"
                + "            li.cantidad_disponible as stock_actual\n"
                + "        FROM VentasConDevoluciones v\n"
                + "        JOIN productos p ON v.id_producto = p.id_producto\n"
                + "        JOIN lotes l ON v.id_lote = l.id_lote\n"
                + "        JOIN lote_inventario li ON l.id_lote = li.id_lote\n"
                + "        WHERE (v.cantidad_original - v.cantidad_devuelta) > 0\n"
                + "        ORDER BY l.fecha_ingreso ASC, v.fecha_venta ASC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VentaDevolucion venta = new VentaDevolucion();
                venta.setId_venta(rs.getInt("id_venta"));
                venta.setId_producto(rs.getInt("id_producto"));
                venta.setId_lote(rs.getInt("id_lote"));
                venta.setCantidad_original(rs.getInt("cantidad_original"));
                venta.setCantidad_disponible(rs.getInt("cantidad_disponible")); // Esta es la cantidad que aún se puede devolver
                venta.setFecha_venta(rs.getTimestamp("fecha_venta"));
                venta.setNombre_producto(rs.getString("nombre_producto"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return ventas;
    }

    public List<CompraDevolucion> obtenerComprasParaDevolucion() {
        List<CompraDevolucion> compras = new ArrayList<>();
        String sql
                = "SELECT c.id_compra, c.id_producto, c.id_lote, c.cantidad as cantidad_original, "
                + "l.costo_unitario, l.fecha_ingreso, c.fecha_compra, p.nombre as nombre_producto, "
                + "li.cantidad_disponible "
                + "FROM compras c "
                + "JOIN productos p ON c.id_producto = p.id_producto "
                + "JOIN lotes l ON c.id_lote = l.id_lote "
                + "JOIN lote_inventario li ON l.id_lote = li.id_lote "
                + "ORDER BY l.fecha_ingreso ASC, c.fecha_compra ASC";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CompraDevolucion compra = new CompraDevolucion();
                compra.setId_compra(rs.getInt("id_compra"));
                compra.setId_producto(rs.getInt("id_producto"));
                compra.setId_lote(rs.getInt("id_lote"));
                compra.setCantidad_original(rs.getInt("cantidad_original"));
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

    public boolean revertirDevoluciones() {
        try {
            con.setAutoCommit(false);

            // 1. Restaurar cantidades afectadas por las devoluciones
            String sqlRestaurarCantidades
                    = "UPDATE lote_inventario li "
                    + "JOIN ( "
                    + "    SELECT d.id_lote, "
                    + "    SUM(CASE "
                    + "        WHEN d.tipo_devolucion = 'venta' AND d.tipo_devolucion != 'defectuoso' THEN -d.cantidad "
                    + // Quita lo que se devolvió en buen estado
                    "        WHEN d.tipo_devolucion = 'compra' THEN d.cantidad "
                    + // Restaura lo que se devolvió al proveedor
                    "        ELSE 0 END"
                    + "    ) as cantidad_ajuste "
                    + "    FROM devoluciones d "
                    + "    GROUP BY d.id_lote "
                    + ") ajustes ON li.id_lote = ajustes.id_lote "
                    + "SET li.cantidad_disponible = li.cantidad_disponible + ajustes.cantidad_ajuste";

            // 2. Eliminar registros de movimientos relacionados con devoluciones
            String sqlEliminarMovimientos
                    = "DELETE FROM movimientos_inventario WHERE tipo_movimiento = 'devolucion'";

            // 3. Eliminar todas las devoluciones
            String sqlEliminarDevoluciones = "DELETE FROM devoluciones";

            // Ejecutar las consultas en orden
            try (Statement stmt = con.createStatement()) {
                // Primero restaurar cantidades
                stmt.executeUpdate(sqlRestaurarCantidades);

                // Luego eliminar movimientos
                stmt.executeUpdate(sqlEliminarMovimientos);

                // Finalmente eliminar devoluciones
                stmt.executeUpdate(sqlEliminarDevoluciones);
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al revertir devoluciones: " + e.getMessage());
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
}
