package modelo;

public class Inventario {
    private int idInsumo;
    private String nombreInsumo;
    private int cantidad;

    public Inventario(int idInsumo, String nombreInsumo, int cantidad) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.cantidad = cantidad;
    }

    public void actualizarStock(int cant) {
        cantidad += cant;
    }

    public boolean verificarDisponibilidad() {
        return cantidad > 0;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public int getCantidad() {
        return cantidad;
    }
}