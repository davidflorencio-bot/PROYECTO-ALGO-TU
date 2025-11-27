package modelo;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String dni;
    private String tipoServicio; 

    public Cliente(int idCliente, String nombre, String dni, String tipoServicio) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.dni = dni;
        this.tipoServicio = tipoServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void realizarPedido(Pedido pedido) {
        System.out.println(nombre + " ha realizado un pedido con ID " + pedido.getIdPedido());
    }

    public void consultarEstado(Pedido pedido) {
        System.out.println("El estado del pedido " + pedido.getIdPedido() + " es: " + pedido.getEstado());
    }

    public void solicitarCuenta(Factura factura) {
        System.out.println(nombre + " ha solicitado la cuenta. Total a pagar: S/" + factura.getTotal());
    }
}