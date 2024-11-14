package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import otras_funcionalidades.Dashboard;

public class DashboardDAO {

    private Connection con;

    public DashboardDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    // Método para obtener resumen general
    public List<Dashboard> obtenerResumenGeneral() {
        List<Dashboard> resumen = new ArrayList<>();
        String sql = " WITH InventarioMetricas AS (\n"
                + "            SELECT \n"
                + "                p.id_producto,\n"
                + "                p.nombre,\n"
                + "                COALESCE(SUM(li.cantidad_total), 0) as inventario_inicial,\n"
                + "                COALESCE(SUM(li.cantidad_disponible), 0) as inventario_actual,\n"
                + "                COUNT(DISTINCT l.id_lote) as total_lotes\n"
                + "            FROM productos p\n"
                + "            LEFT JOIN lotes l ON p.id_producto = l.id_producto\n"
                + "            LEFT JOIN lote_inventario li ON l.id_lote = li.id_lote\n"
                + "            GROUP BY p.id_producto, p.nombre\n"
                + "        ),\n"
                + "        VentasMetricas AS (\n"
                + "            SELECT \n"
                + "                p.id_producto,\n"
                + "                COALESCE(SUM(v.cantidad * v.precio_venta_unitario), 0) as ventas_brutas,\n"
                + "                COALESCE(SUM(v.cantidad), 0) as unidades_vendidas,\n"
                + "                COALESCE(SUM(v.cantidad * l.costo_unitario), 0) as costo_ventas\n"
                + "            FROM productos p\n"
                + "            LEFT JOIN ventas v ON p.id_producto = v.id_producto\n"
                + "            LEFT JOIN lotes l ON v.id_lote = l.id_lote\n"
                + "            GROUP BY p.id_producto\n"
                + "        ),\n"
                + "        DevolucionesMetricas AS (\n"
                + "            SELECT \n"
                + "                p.id_producto,\n"
                + "                COALESCE(SUM(CASE \n"
                + "                    WHEN d.tipo_devolucion = 'venta' THEN d.cantidad \n"
                + "                    ELSE 0 \n"
                + "                END), 0) as cantidad_devoluciones_venta,\n"
                + "                COALESCE(SUM(CASE \n"
                + "                    WHEN d.tipo_devolucion = 'venta' THEN (d.cantidad * l.costo_unitario)\n"
                + "                    ELSE 0 \n"
                + "                END), 0) as costo_devoluciones_venta,\n"
                + "                COALESCE(SUM(CASE \n"
                + "                    WHEN d.tipo_devolucion = 'compra' THEN d.cantidad \n"
                + "                    ELSE 0 \n"
                + "                END), 0) as cantidad_devoluciones_compra,\n"
                + "                COALESCE(SUM(CASE \n"
                + "                    WHEN d.tipo_devolucion = 'compra' THEN (d.cantidad * l.costo_unitario)\n"
                + "                    ELSE 0 \n"
                + "                END), 0) as costo_devoluciones_compra,\n"
                + "                COALESCE(SUM(CASE \n"
                + "                    WHEN d.tipo_devolucion = 'defectuoso' THEN d.cantidad \n"
                + "                    ELSE 0 \n"
                + "                END), 0) as productos_defectuosos\n"
                + "            FROM productos p\n"
                + "            LEFT JOIN devoluciones d ON p.id_producto = d.id_producto\n"
                + "            LEFT JOIN lotes l ON d.id_lote = l.id_lote\n"
                + "            GROUP BY p.id_producto\n"
                + "        )\n"
                + "        SELECT \n"
                + "            im.nombre as producto,\n"
                + "            im.inventario_inicial,\n"
                + "            im.inventario_actual,\n"
                + "            im.total_lotes,\n"
                + "            vm.ventas_brutas,\n"
                + "            vm.unidades_vendidas,\n"
                + "            vm.costo_ventas,\n"
                + "            dm.cantidad_devoluciones_venta,\n"
                + "            dm.costo_devoluciones_venta,\n"
                + "            dm.cantidad_devoluciones_compra,\n"
                + "            dm.costo_devoluciones_compra,\n"
                + "            dm.productos_defectuosos,\n"
                + "            (vm.ventas_brutas - dm.costo_devoluciones_venta) as ventas_netas,\n"
                + "            (vm.costo_ventas - dm.costo_devoluciones_venta) as costo_ventas_neto,\n"
                + "            CASE \n"
                + "                WHEN vm.unidades_vendidas > 0 \n"
                + "                THEN (dm.cantidad_devoluciones_venta * 100.0 / vm.unidades_vendidas)\n"
                + "                ELSE 0 \n"
                + "            END as porcentaje_devoluciones_venta,\n"
                + "            CASE \n"
                + "                WHEN vm.unidades_vendidas > 0 \n"
                + "                THEN (dm.cantidad_devoluciones_compra * 100.0 / vm.unidades_vendidas)\n"
                + "                ELSE 0 \n"
                + "            END as porcentaje_devoluciones_compra\n"
                + "        FROM InventarioMetricas im\n"
                + "        LEFT JOIN VentasMetricas vm ON im.id_producto = vm.id_producto\n"
                + "        LEFT JOIN DevolucionesMetricas dm ON im.id_producto = dm.id_producto\n"
                + "        ORDER BY im.nombre";

        try {
            // Primero, imprimimos la consulta para debug
            System.out.println("Ejecutando consulta: " + sql);

            try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

                System.out.println("Consulta ejecutada correctamente");

                while (rs.next()) {
                    Dashboard dash = new Dashboard();
                    dash.setProducto(rs.getString("producto"));
                    dash.setInventarioInicial(rs.getInt("inventario_inicial"));
                    dash.setInventarioActual(rs.getInt("inventario_actual"));
                    dash.setTotalLotes(rs.getInt("total_lotes"));
                    dash.setVentasBrutas(rs.getDouble("ventas_brutas"));
                    dash.setUnidadesVendidas(rs.getInt("unidades_vendidas"));
                    dash.setCostoVentas(rs.getDouble("costo_ventas"));
                    dash.setCantidadDevolucionesVenta(rs.getInt("cantidad_devoluciones_venta"));
                    dash.setCostoDevolucionesVenta(rs.getDouble("costo_devoluciones_venta"));
                    dash.setCantidadDevolucionesCompra(rs.getInt("cantidad_devoluciones_compra"));
                    dash.setCostoDevolucionesCompra(rs.getDouble("costo_devoluciones_compra"));
                    dash.setProductosDefectuosos(rs.getInt("productos_defectuosos"));
                    dash.setVentasNetas(rs.getDouble("ventas_netas"));
                    dash.setCostoVentasNeto(rs.getDouble("costo_ventas_neto"));
                    dash.setPorcentajeDevolucionesVenta(rs.getDouble("porcentaje_devoluciones_venta"));
                    dash.setPorcentajeDevolucionesCompra(rs.getDouble("porcentaje_devoluciones_compra"));

                    resumen.add(dash);
                    System.out.println("Agregado producto: " + dash.getProducto());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener resumen: " + e.getMessage());
            e.printStackTrace();
        }
        return resumen;
    }
    // Añade este método para pruebas

    public boolean testConnection() {
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM productos")) {
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Número de productos encontrados: " + count);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en test de conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
