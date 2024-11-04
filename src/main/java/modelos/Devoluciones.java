package modelos;

import java.sql.Date;

public class Devoluciones {

    private int id_devolucion, id_producto, id_lote, cantidad;
    private String tipo_devolucion, razon;
    private Date fecha_devolucion;

    public Devoluciones() {
    }

    public Devoluciones(int id_devolucion, int id_producto, int id_lote, int cantidad, String tipo_devolucion, String razon, Date fecha_devolucion) {
        this.id_devolucion = id_devolucion;
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.tipo_devolucion = tipo_devolucion;
        this.razon = razon;
        this.fecha_devolucion = fecha_devolucion;
    }

    public Devoluciones(int id_producto, int id_lote, int cantidad, String tipo_devolucion, String razon, Date fecha_devolucion) {
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.tipo_devolucion = tipo_devolucion;
        this.razon = razon;
        this.fecha_devolucion = fecha_devolucion;
    }

    public int getId_devolucion() {
        return id_devolucion;
    }

    public void setId_devolucion(int id_devolucion) {
        this.id_devolucion = id_devolucion;
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

    public String getTipo_devolucion() {
        return tipo_devolucion;
    }

    public void setTipo_devolucion(String tipo_devolucion) {
        this.tipo_devolucion = tipo_devolucion;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public Date getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(Date fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

}
