package modelos;

public class Lote_inventario {

    private int id_lote_inventario, id_lote, cantidad_total, cantidad_disponible;

    public Lote_inventario(int id_lote_inventario, int id_lote, int cantidad_total, int cantidad_disponible) {
        this.id_lote_inventario = id_lote_inventario;
        this.id_lote = id_lote;
        this.cantidad_total = cantidad_total;
        this.cantidad_disponible = cantidad_disponible;
    }

    public Lote_inventario() {
    }

    public Lote_inventario(int id_lote, int cantidad_total, int cantidad_disponible) {
        this.id_lote = id_lote;
        this.cantidad_total = cantidad_total;
        this.cantidad_disponible = cantidad_disponible;
    }

    public int getId_lote_inventario() {
        return id_lote_inventario;
    }

    public void setId_lote_inventario(int id_lote_inventario) {
        this.id_lote_inventario = id_lote_inventario;
    }

    public int getId_lote() {
        return id_lote;
    }

    public void setId_lote(int id_lote) {
        this.id_lote = id_lote;
    }

    public int getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(int cantidad_total) {
        this.cantidad_total = cantidad_total;
    }

    public int getCantidad_disponible() {
        return cantidad_disponible;
    }

    public void setCantidad_disponible(int cantidad_disponible) {
        this.cantidad_disponible = cantidad_disponible;
    }

    
}
