package vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import modelo.*;

public class MenuPrincipalFrame extends JFrame {

    private ArrayList<Plato> menu;
    private ArrayList<Inventario> inventario;
    private String rol;

    public MenuPrincipalFrame(String rol) {
        this.rol = rol;
        setTitle("Menú Principal - " + rol);
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 1, 10, 10));

        cargarDatosEjemplo();

        JLabel lblTitulo = new JLabel("Menú Principal - " + rol, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitulo);

        // Botones principales
        JButton btnMenu = new JButton("Ver Menú de Platos");
        JButton btnInventario = new JButton("Inventario");
        JButton btnReportes = new JButton("Reportes de Ventas");
        JButton btnPendientes = new JButton("Pedidos Pendientes (Cocina)");
        JButton btnCliente = new JButton("Mis Pedidos");
        JButton btnGestionPedidos = new JButton("Gestión de Pedidos (Mesero)");
        JButton btnSalir = new JButton("Cerrar Sesión");

        // Mostrar botones según el rol
        switch (rol.toLowerCase()) {

            case "administrador":
                add(btnMenu);
                add(btnInventario);
                add(btnReportes);
                break;

            case "mesero":
                add(btnMenu);
                add(btnGestionPedidos); // ve pedidos hechos por clientes
                break;

            case "cocinero":
                add(btnPendientes);  // ve pedidos y los marca como listos
                break;

            case "cliente":
                add(btnMenu);
                add(new JButton("Hacer Pedido") {{
                    addActionListener(e -> new PedidoFrame(DatosCompartidos.menuGlobal).setVisible(true));
                }});
                add(btnCliente); // ver el estado de sus pedidos
                break;
        }

        add(btnSalir);

        // Acciones
        btnMenu.addActionListener(e -> new MenuPlatosFrame(menu).setVisible(true));
        btnInventario.addActionListener(e -> new InventarioFrame(inventario).setVisible(true));
        btnReportes.addActionListener(e -> new ReportesFrame().setVisible(true));
        btnPendientes.addActionListener(e -> new PedidosPendientesFrame().setVisible(true));
        btnCliente.addActionListener(e -> new ClienteFrame().setVisible(true));
        btnGestionPedidos.addActionListener(e -> new PedidosMeseroFrame().setVisible(true));
        btnSalir.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    // Cargar datos simulados
    private void cargarDatosEjemplo() {
        menu = new ArrayList<>();
        menu.add(new Plato(1, "Ceviche", 25.0, "Entrada"));
        menu.add(new Plato(2, "Lomo Saltado", 30.0, "Fondo"));
        menu.add(new Plato(3, "Tiramisú", 15.0, "Postre"));
        DatosCompartidos.menuGlobal = menu;

        inventario = new ArrayList<>();
        inventario.add(new Inventario(1, "Papas", 30));
        inventario.add(new Inventario(2, "Pollo", 20));
        inventario.add(new Inventario(3, "Cebolla", 50));
    }
}