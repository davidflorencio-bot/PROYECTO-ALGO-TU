package modelo;

import java.util.ArrayList;

public class Administrador {
    private String usuario;
    private String contrasena;

    public Administrador(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public boolean login(String u, String p) {
        return usuario.equals(u) && contrasena.equals(p);
    }

    public void verReportes(ArrayList<Pedido> pedidos) {
        System.out.println("\n--- Reporte de Ventas ---");
        double total = 0;
        for (Pedido p : pedidos) {
            total += p.calcularTotal();
            System.out.println("Pedido #" + p.getIdPedido() + " - Cliente: " + p.getCliente().getNombre()
                    + " - Mesa: " + p.getMesa().getNumero()
                    + " - Total: S/" + p.calcularTotal());
        }
        System.out.println("Total general vendido: S/" + total);
        System.out.println("--------------------------\n");
    }

    public void gestionarInventario(ArrayList<Inventario> inventario) {
        System.out.println("\n--- Inventario Actual ---");
        for (Inventario i : inventario) {
            System.out.println(i.getNombreInsumo() + " - Cantidad: " + i.getCantidad());
        }
        System.out.println("--------------------------");
    }
}
