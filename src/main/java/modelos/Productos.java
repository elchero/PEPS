package modelos;

import java.sql.Date;

public class Productos {

    private int id_producto;
    private String nombre, descripcion, proveedor;
    private double precio;
    private Date fecha_registro;
    private String estado;

    public Productos() {
    }

    public Productos(int id_producto, String nombre, String descripcion, String proveedor, double precio, Date fecha_registro, String estado) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.precio = precio;
        this.fecha_registro = fecha_registro;
        this.estado = estado;
    }

    public Productos(String nombre, String descripcion, String proveedor, double precio, Date fecha_registro, String estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.precio = precio;
        this.fecha_registro = fecha_registro;
        this.estado = estado;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
