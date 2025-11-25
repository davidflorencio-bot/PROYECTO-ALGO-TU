package modelo;

public class Cocinero {
    private int idCocinero;
    private String nombre;
    private String especialidad;
    private boolean disponible;

    public Cocinero(int idCocinero, String nombre, String especialidad) {
        this.idCocinero = idCocinero;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.disponible = true;
    }

    public void prepararPlato(Plato p) {
        System.out.println(nombre + " está preparando el plato: " + p.getNombre());
    }

    public void marcarPedidoListo(Pedido pedido) {
        pedido.setEstado("Listo");
        System.out.println("El cocinero " + nombre + " ha marcado el pedido " + pedido.getIdPedido() + " como listo.");
    }
}