package modelo;

import java.util.ArrayList;
import java.util.Date;

public class Pedido {
    private int idPedido;
    private Date fecha;
    private String estado;
    private ArrayList<Plato> platos;
  
 
    private String nombreCliente;
    private String nombreMesero;
    private int numeroMesa;
    private int capacidadMesa;

    private static int contador = 1;

    public Pedido(String nombreCliente, String nombreMesero, int numeroMesa, int capacidadMesa) {
        this.idPedido = contador++;
        this.fecha = new Date();
        this.estado = "En preparación";
        this.platos = new ArrayList<>();
        this.nombreCliente = nombreCliente;
        this.nombreMesero = nombreMesero;
        this.numeroMesa = numeroMesa;
        this.capacidadMesa = capacidadMesa;
    }

    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public String getNombreMesero() {
        return nombreMesero;
    }
    
    public int getNumeroMesa() {
        return numeroMesa;
    }
    
    public int getCapacidadMesa() {
        return capacidadMesa;
    }
    
    
    public int getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void agregarPlato(Plato p) {
        platos.add(p);
        System.out.println(" Se agregó el plato '" + p.getNombre() + "' al pedido " + idPedido);
    }

    public double calcularTotal() {
        double total = 0;
        for (Plato p : platos) {
            total += p.getPrecio();
        }
        return total;
    }

    public ArrayList<Plato> getPlatos() {
        return platos;
    }
}