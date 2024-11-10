package modelos;

import java.sql.Timestamp;

public class Movimientos_inventario {

    private int id_movimiento;
    private String tipo_movimiento;
    private int id_producto, id_lote, cantidad;
    private double costo_unitario, iva;
    private Timestamp fecha_movimiento;

    public Movimientos_inventario() {
    }

    public Movimientos_inventario(int id_movimiento, String tipo_movimiento, int id_producto, int id_lote, int cantidad, double costo_unitario, double iva, Timestamp fecha_movimiento) {
        this.id_movimiento = id_movimiento;
        this.tipo_movimiento = tipo_movimiento;
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.costo_unitario = costo_unitario;
        this.iva = iva;
        this.fecha_movimiento = fecha_movimiento;
    }

    public Movimientos_inventario(String tipo_movimiento, int id_producto, int id_lote, int cantidad, double costo_unitario, double iva, Timestamp fecha_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
        this.id_producto = id_producto;
        this.id_lote = id_lote;
        this.cantidad = cantidad;
        this.costo_unitario = costo_unitario;
        this.iva = iva;
        this.fecha_movimiento = fecha_movimiento;
    }

    public int getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(int id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
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

    public double getCosto_unitario() {
        return costo_unitario;
    }

    public void setCosto_unitario(double costo_unitario) {
        this.costo_unitario = costo_unitario;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public Timestamp getFecha_movimiento() {
        return fecha_movimiento;
    }

    public void setFecha_movimiento(Timestamp fecha_movimiento) {
        this.fecha_movimiento = fecha_movimiento;
    }
}
