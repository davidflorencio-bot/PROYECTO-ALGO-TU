package modelo;

import java.util.ArrayList;
import java.util.Date;

public class Pedido {
    private int idPedido;
    private Date fecha;
    private String estado;
    private ArrayList<Plato> platos;
    private Cliente cliente;
    private Mesero mesero;
    private Mesa mesa;

    private static int contador = 1;

    public Pedido(Cliente cliente, Mesero mesero, Mesa mesa) {
        this.idPedido = contador++;
        this.fecha = new Date();
        this.estado = "En preparación";
        this.platos = new ArrayList<>();
        this.cliente = cliente;
        this.mesero = mesero;
        this.mesa = mesa;
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
        System.out.println("✅ Se agregó el plato '" + p.getNombre() + "' al pedido " + idPedido);
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

    public Cliente getCliente() {
        return cliente;
    }

    public Mesa getMesa() {
        return mesa;
    }
}
