package otras_funcionalidades;

import java.sql.Timestamp;

/**
 *
 * @author vladi
 */
public class VentaDevolucion {
    private int id_venta;
    private int id_producto;
    private int id_lote;
    private int cantidad;
    private Timestamp fecha_venta;
    private String nombre_producto;
    private int cantidad_original, cantidad_disponible, total_devuelto;

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_lote() {
        return id_lote;
    }

    public void setId_lote(int id_lote) {
        this.id_lote = id_lote;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Timestamp getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(Timestamp fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getCantidad_original() {
        return cantidad_original;
    }

    public void setCantidad_original(int cantidad_original) {
        this.cantidad_original = cantidad_original;
    }

    public int getCantidad_disponible() {
        return cantidad_disponible;
    }

    public void setCantidad_disponible(int cantidad_disponible) {
        this.cantidad_disponible = cantidad_disponible;
    }

    public int getTotal_devuelto() {
        return total_devuelto;
    }

    public void setTotal_devuelto(int total_devuelto) {
        this.total_devuelto = total_devuelto;
    }

    
}
