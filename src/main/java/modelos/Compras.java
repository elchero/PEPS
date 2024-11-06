package modelos;

import java.sql.Timestamp ;

public class Compras {

    private int id_compra, id_producto, id_lote, cantidad;
    private double costo_total;
    private Timestamp  fecha_compra;

    public Compras() {
    }

    public Compras(int id_compra, int id_producto, int id_lote, int cantidad, double costo_total, Timestamp  fecha_compra) {
        this.id_compra = id_compra;
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.costo_total = costo_total;
        this.fecha_compra = fecha_compra;
    }

    public Compras(int id_producto, int id_lote, int cantidad, double costo_total, Timestamp  fecha_compra) {
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.costo_total = costo_total;
        this.fecha_compra = fecha_compra;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
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

    public double getCosto_total() {
        return costo_total;
    }

    public void setCosto_total(double costo_total) {
        this.costo_total = costo_total;
    }

    public Timestamp getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(Timestamp  fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

}
