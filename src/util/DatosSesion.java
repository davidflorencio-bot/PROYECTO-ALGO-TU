package util;

import java.util.ArrayList;
import modelo.Usuario;
import modelo.Plato;

public class DatosSesion {

     public static Usuario usuarioLogueado;
    
    public static ArrayList<Plato> menuGlobal = new ArrayList<>();

    
    private static ArrayList<String[]> pedidos = new ArrayList<>();

    
    public static void agregarPedido(String nombrePlato, double precio, String cliente) {
        pedidos.add(new String[]{nombrePlato, String.valueOf(precio), "Pendiente", cliente});
    }

    
    public static ArrayList<String[]> obtenerPedidos() {
        return pedidos;
    }

    
    public static void cambiarEstado(int index, String nuevoEstado) {
        if (index >= 0 && index < pedidos.size()) {
            pedidos.get(index)[2] = nuevoEstado;
        }
    }

    
    public static ArrayList<String[]> obtenerPedidosPorCliente(String cliente) {
        ArrayList<String[]> lista = new ArrayList<>();
        for (String[] p : pedidos) {
            if (p[3].equalsIgnoreCase(cliente)) {
                lista.add(p);
            }
        }
        return lista;
    }

    
    public static void limpiarPedidos() {
        pedidos.clear();
    }
}