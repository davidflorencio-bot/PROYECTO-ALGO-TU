package controlador;

import modelo.*;
import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Pedido> pedidos = new ArrayList<>();
    private static ArrayList<Inventario> inventario = new ArrayList<>();
    private static ArrayList<Plato> menu = new ArrayList<>();
    private static ArrayList<Mesa> mesas = new ArrayList<>();

    public static void main(String[] args) {
        inicializarDatos();

        System.out.println("========= SISTEMA DE GESTIÓN DEL RESTAURANTE =========");

        while (true) {
            System.out.print("\nIngrese usuario: ");
            String user = sc.nextLine();
            System.out.print("Ingrese contraseña: ");
            String pass = sc.nextLine();

            if (user.equals("admin") && pass.equals("1234")) {
                menuAdministrador();
            } else if (user.equals("mesero") && pass.equals("1111")) {
                menuMesero();
            } else if (user.equals("cocinero") && pass.equals("2222")) {
                menuCocinero();
            } else if (user.equals("cliente") && pass.equals("3333")) {
                menuCliente();
            } else {
                System.out.println("❌ Usuario o contraseña incorrectos.");
            }
        }
    }

    // ===================== MENÚ ADMINISTRADOR =====================
    public static void menuAdministrador() {
        Administrador admin = new Administrador("admin", "1234");
        int op;
        do {
            System.out.println("\n--- Menú Administrador ---");
            System.out.println("1. Ver reportes de ventas");
            System.out.println("2. Ver inventario");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> admin.verReportes(pedidos);
                case 2 -> admin.gestionarInventario(inventario);
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // ===================== MENÚ MESERO =====================
    public static void menuMesero() {
        Mesero mesero = new Mesero(1, "Mesero", "mañana");
        Cliente cliente = new Cliente(1, "Cliente", "78945612", "presencial");
        Pedido pedidoActual = null;
        Factura facturaActual = null;

        int op;
        do {
            System.out.println("\n--- Menú Mesero ---");
            System.out.println("1. Ver menú");
            System.out.println("2. Crear pedido (asignar mesa)");
            System.out.println("3. Agregar plato al pedido");
            System.out.println("4. Generar factura");
            System.out.println("5. Cobrar cuenta");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> mostrarMenu();
                case 2 -> {
                    System.out.println("\n--- Mesas disponibles ---");
                    for (Mesa m : mesas)
                        System.out.println(m.getNumero() + ". " + (m.isDisponible() ? "Libre" : "Ocupada"));
                    System.out.print("Seleccione el número de mesa: ");
                    int numMesa = sc.nextInt();
                    sc.nextLine();
                    Mesa mesaSel = buscarMesa(numMesa);
                    if (mesaSel != null && mesaSel.isDisponible()) {
                        mesaSel.reservar();
                        pedidoActual = mesero.tomarPedido(cliente, mesaSel);
                    } else System.out.println("❌ Mesa no disponible.");
                }
                case 3 -> {
                    if (pedidoActual == null) System.out.println("⚠️ Primero cree un pedido.");
                    else {
                        System.out.print("Ingrese ID del plato: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        Plato plato = buscarPlato(id);
                        if (plato != null) pedidoActual.agregarPlato(plato);
                        else System.out.println("❌ Plato no encontrado.");
                    }
                }
                case 4 -> {
                    if (pedidoActual == null) System.out.println("⚠️ No hay pedido creado.");
                    else {
                        facturaActual = new Factura(1, pedidoActual);
                        facturaActual.mostrarFactura();
                        pedidos.add(pedidoActual);
                    }
                }
                case 5 -> {
                    if (facturaActual == null) System.out.println("⚠️ No hay factura generada.");
                    else mesero.cobrarCuenta(facturaActual);
                }
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // ===================== MENÚ COCINERO =====================
    public static void menuCocinero() {
        Cocinero cocinero = new Cocinero(1, "Cocinero", "Platos criollos");
        Cocina cocina = new Cocina(1, cocinero);

        int op;
        do {
            System.out.println("\n--- Menú Cocinero ---");
            System.out.println("1. Ver pedidos pendientes");
            System.out.println("2. Preparar pedido");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    if (pedidos.isEmpty()) System.out.println("No hay pedidos.");
                    else for (Pedido p : pedidos)
                        System.out.println("Pedido #" + p.getIdPedido() + " - Estado: " + p.getEstado());
                }
                case 2 -> {
                    if (pedidos.isEmpty()) System.out.println("No hay pedidos para preparar.");
                    else {
                        System.out.print("Ingrese ID del pedido: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        for (Pedido p : pedidos) {
                            if (p.getIdPedido() == id) cocina.recibirPedido(p);
                        }
                    }
                }
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // ===================== MENÚ CLIENTE =====================
    public static void menuCliente() {
        Cliente cliente = new Cliente(1, "Cliente", "78945612", "presencial");

        int op;
        do {
            System.out.println("\n--- Menú Cliente ---");
            System.out.println("1. Ver menú");
            System.out.println("2. Consultar estado del pedido");
            System.out.println("3. Solicitar cuenta");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> mostrarMenu();
                case 2 -> {
                    if (pedidos.isEmpty()) System.out.println("No hay pedidos registrados.");
                    else cliente.consultarEstado(pedidos.get(pedidos.size() - 1));
                }
                case 3 -> {
                    if (pedidos.isEmpty()) System.out.println("No hay pedido facturado.");
                    else {
                        Factura factura = new Factura(99, pedidos.get(pedidos.size() - 1));
                        cliente.solicitarCuenta(factura);
                    }
                }
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // ===================== FUNCIONES AUXILIARES =====================
    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ DISPONIBLE ---");
        for (Plato p : menu) p.mostrarInfo();
    }

    private static Plato buscarPlato(int id) {
        for (Plato p : menu) if (p.getIdPlato() == id) return p;
        return null;
    }

    private static Mesa buscarMesa(int num) {
        for (Mesa m : mesas) if (m.getNumero() == num) return m;
        return null;
    }

    private static void inicializarDatos() {
        menu.add(new Plato(1, "Lomo Saltado", 22.5, "Principal"));
        menu.add(new Plato(2, "Aji de Gallina", 20.0, "Principal"));
        menu.add(new Plato(3, "Ceviche", 25.0, "Entrada"));
        menu.add(new Plato(4, "Chicha Morada", 5.0, "Bebida"));

        inventario.add(new Inventario(1, "Papas", 30));
        inventario.add(new Inventario(2, "Pollo", 20));
        inventario.add(new Inventario(3, "Cebolla", 50));
        inventario.add(new Inventario(4, "Limones", 40));

        mesas.add(new Mesa(1, 1, 4));
        mesas.add(new Mesa(2, 2, 2));
        mesas.add(new Mesa(3, 3, 6));
    }
}
