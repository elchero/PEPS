package modelos;

import java.sql.Timestamp ;

public class Compras {

    private int id_compra, id_producto, id_lote, cantidad;
    private double costo_total;
    private Timestamp  fecha_compra;
    private String nombre; //ES el nombre del producto
    private double costo_unitario;
    private int cantidad_disponible;
    private String tipo_movimiento; //Es para la consulta de InventarioInfo, no es como tal un atributo de base de datos

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCosto_unitario() {
        return costo_unitario;
    }

    public void setCosto_unitario(double costo_unitario) {
        this.costo_unitario = costo_unitario;
    }

    public int getCantidad_disponible() {
        return cantidad_disponible;
    }

    public void setCantidad_disponible(int cantidad_disponible) {
        this.cantidad_disponible = cantidad_disponible;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

}
