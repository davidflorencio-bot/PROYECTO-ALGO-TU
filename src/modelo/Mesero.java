package modelo;

public class Mesero {
    private int idMesero;
    private String nombre;
    private String turno;

    public Mesero(int idMesero, String nombre, String turno) {
        this.idMesero = idMesero;
        this.nombre = nombre;
        this.turno = turno;
    }

    public Pedido tomarPedido(Cliente cliente, Mesa mesa) {
        Pedido pedido = new Pedido(cliente, this, mesa);
        System.out.println("El mesero " + nombre + " ha tomado el pedido del cliente "
                + cliente.getNombre() + " en la mesa " + mesa.getNumero());
        return pedido;
    }

    public void entregarPedido(Pedido pedido) {
        System.out.println("El mesero " + nombre + " entrega el pedido " + pedido.getIdPedido());
        pedido.setEstado("Entregado");
    }

    public void cobrarCuenta(Factura factura) {
        System.out.println("El mesero " + nombre + " cobra S/" + factura.getTotal());
    }
}