package modelosDAO;

import db.cn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelos.Productos;

public class ProductosDAO {

    private Connection con;

    public ProductosDAO() throws ClassNotFoundException {
        cn conexion = new cn();
        this.con = conexion.getCon();
    }

    public boolean insertarProductos(Productos producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, proveedor, precio, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getProveedor());
            ps.setDouble(4, producto.getPrecio());
            ps.setString(5, producto.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Productos> listarProductos() {
        List<Productos> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Productos producto = new Productos(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("proveedor"),
                        rs.getDouble("precio"),
                        rs.getString("estado")
                );
                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return listaProductos;
    }

    public boolean eliminarProducto(int id_producto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_producto);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // Método para desactivar un producto (marcarlo como defectuoso)
    public boolean desactivarProducto(int idProducto) {
        String sql = "UPDATE productos SET estado = 'defectuoso' WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error al desactivar producto: " + e.getMessage());
            return false;
        }
    }

    public Productos obtenerProducto(int id_producto) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_producto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Productos(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("proveedor"),
                        rs.getDouble("precio"),
                        rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            // Utiliza un logger en lugar de System.err para registrar errores
            System.err.println("Error al obtener producto: " + e.getMessage());
        }
        return null; // Retornar null si no se encuentra el producto
    }

    public boolean actualizarProducto(Productos producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, proveedor = ?, precio = ?, estado = ? WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getProveedor());
            ps.setDouble(4, producto.getPrecio());
            ps.setString(5, producto.getEstado());
            ps.setInt(6, producto.getId_producto());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
}
