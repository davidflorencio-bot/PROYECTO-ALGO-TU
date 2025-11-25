package vista;

import java.util.ArrayList;
import modelo.Plato;

public class DatosCompartidos {

    // 🔹 Menú global (disponible para todas las ventanas)
    public static ArrayList<Plato> menuGlobal = new ArrayList<>();

    // 🔹 Lista global de pedidos (cada pedido = [plato, precio, estado, cliente])
    private static ArrayList<String[]> pedidos = new ArrayList<>();

    // ✅ Agregar pedido (lo usan los clientes cuando generan su factura)
    public static void agregarPedido(String nombrePlato, double precio, String cliente) {
        pedidos.add(new String[]{nombrePlato, String.valueOf(precio), "Pendiente", cliente});
    }

    // ✅ Obtener la lista completa de pedidos (lo usan mesero y cocinero)
    public static ArrayList<String[]> obtenerPedidos() {
        return pedidos;
    }

    // ✅ Cambiar estado de un pedido (lo usan cocinero o mesero)
    public static void cambiarEstado(int index, String nuevoEstado) {
        if (index >= 0 && index < pedidos.size()) {
            pedidos.get(index)[2] = nuevoEstado;
        }
    }

    // ✅ Filtrar pedidos por cliente (para que cada cliente vea los suyos)
    public static ArrayList<String[]> obtenerPedidosPorCliente(String cliente) {
        ArrayList<String[]> lista = new ArrayList<>();
        for (String[] p : pedidos) {
            if (p[3].equalsIgnoreCase(cliente)) {
                lista.add(p);
            }
        }
        return lista;
    }

    // ✅ Limpiar pedidos (por si reinicias sesión o haces pruebas)
    public static void limpiarPedidos() {
        pedidos.clear();
    }
}