package modelo;

import java.util.Date;

public class Factura {
    private int idFactura;
    private Date fechaEmision;
    private double total;
    private Pedido pedido;

    public Factura(int idFactura, Pedido pedido) {
        this.idFactura = idFactura;
        this.fechaEmision = new Date();
        this.pedido = pedido;
        this.total = pedido.calcularTotal();
    }

    public double getTotal() {
        return total;
    }

    public void mostrarFactura() {
    System.out.println("\n--- FACTURA #" + idFactura + " ---");
    System.out.println("Cliente: " + pedido.getNombreCliente());  // ¡CAMBIO!
    System.out.println("Mesa: " + pedido.getNumeroMesa());        // ¡CAMBIO!
    System.out.println("Fecha: " + fechaEmision);
    System.out.println("Total: S/" + total);
    System.out.println("--------------------------\n");
    }
}   