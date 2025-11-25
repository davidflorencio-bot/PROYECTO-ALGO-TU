package modelo;

public class Cocina {
    private int idCocina;
    private String estado;
    private Cocinero cocinero;

    public Cocina(int idCocina, Cocinero cocinero) {
        this.idCocina = idCocina;
        this.estado = "Operativa";
        this.cocinero = cocinero;
    }

    public void recibirPedido(Pedido p) {
        System.out.println("La cocina ha recibido el pedido #" + p.getIdPedido());
        for (Plato plato : p.getPlatos()) {
            cocinero.prepararPlato(plato);
        }
        cocinero.marcarPedidoListo(p);
    }
}
