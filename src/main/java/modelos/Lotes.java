package modelos;

import java.sql.Timestamp ;

public class Lotes {

    private int id_lote, id_producto;
    private double costo_unitario;
    private Timestamp  fecha_ingreso;

    public Lotes() {
    }

    public Lotes(int id_lote, int id_producto, double costo_unitario, Timestamp  fecha_ingreso) {
        this.id_lote = id_lote;
        this.id_producto = id_producto;
        this.costo_unitario = costo_unitario;
        this.fecha_ingreso = fecha_ingreso;
    }

    public Lotes(int id_producto, double costo_unitario, Timestamp  fecha_ingreso) {
        this.id_producto = id_producto;
        this.costo_unitario = costo_unitario;
        this.fecha_ingreso = fecha_ingreso;
    }

    public int getId_lote() {
        return id_lote;
    }

    public void setId_lote(int id_lote) {
        this.id_lote = id_lote;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public double getCosto_unitario() {
        return costo_unitario;
    }

    public void setCosto_unitario(double costo_unitario) {
        this.costo_unitario = costo_unitario;
    }

    public Timestamp  getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Timestamp  fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

}
