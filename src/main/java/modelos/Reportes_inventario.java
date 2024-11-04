package modelos;

public class Reportes_inventario {

    private int id_reporte, id_movimiento;

    public Reportes_inventario() {
    }

    public Reportes_inventario(int id_reporte, int id_movimiento) {
        this.id_reporte = id_reporte;
        this.id_movimiento = id_movimiento;
    }

    public Reportes_inventario(int id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public int getId_reporte() {
        return id_reporte;
    }

    public void setId_reporte(int id_reporte) {
        this.id_reporte = id_reporte;
    }

    public int getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(int id_movimiento) {
        this.id_movimiento = id_movimiento;
    }
    
}
