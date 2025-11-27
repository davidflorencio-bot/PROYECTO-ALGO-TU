package modelo;

public class Inventario {
    private int id;
    private String nombreInsumo;
    private double cantidad;
    private String unidad;
    private double minimo;
    
    // Constructor
    public Inventario() {}
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }
    
    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    
    public double getMinimo() { return minimo; }
    public void setMinimo(double minimo) { this.minimo = minimo; }
}