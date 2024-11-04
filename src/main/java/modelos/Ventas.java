package modelos;

import java.sql.Date;

public class Ventas {

    private int id_venta, id_producto, id_lote, cantidad;
    private double precio_venta_unitario;
    private Date fecha_venta;

    public Ventas() {
    }

    public Ventas(int id_venta, int id_producto, int id_lote, int cantidad, double precio_venta_unitario, Date fecha_venta) {
        this.id_venta = id_venta;
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.precio_venta_unitario = precio_venta_unitario;
        this.fecha_venta = fecha_venta;
    }

    public Ventas(int id_producto, int id_lote, int cantidad, double precio_venta_unitario, Date fecha_venta) {
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.precio_venta_unitario = precio_venta_unitario;
        this.fecha_venta = fecha_venta;
    }

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

    public double getPrecio_venta_unitario() {
        return precio_venta_unitario;
    }

    public void setPrecio_venta_unitario(double precio_venta_unitario) {
        this.precio_venta_unitario = precio_venta_unitario;
    }

    public Date getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(Date fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

}
