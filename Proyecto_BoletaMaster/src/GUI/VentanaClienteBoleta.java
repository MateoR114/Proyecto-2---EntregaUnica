package GUI;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import Usuarios.Cliente;


public class VentanaClienteBoleta extends JFrame {

    private Cliente cliente;
    private PanelMisTiquetes panelMisTiquetes;

    public VentanaClienteBoleta(Cliente cliente) {
        this.cliente = cliente;
        setTitle("BoletaMaster - Tus Boletas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        construirMenu();
        construirEncabezado();
        construirContenido();

        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void construirMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> dispose());
        menuArchivo.add(itemSalir);

        JMenu menuVer = new JMenu("Ver");
        JMenuItem itemMisTiquetes = new JMenuItem("Mis tiquetes");
        itemMisTiquetes.addActionListener(e -> mostrarMisTiquetes());
        menuVer.add(itemMisTiquetes);

        barra.add(menuArchivo);
        barra.add(menuVer);

        setJMenuBar(barra);
    }

    private void construirEncabezado() {
        JPanel panelNorte = new JPanel(new BorderLayout());

        JLabel lblTitulo = new JLabel("BoletaMaster - Tus Boletas", JLabel.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));

        String nombre = cliente.getNombre();
        String login = cliente.getLogin();
        JLabel lblUsuario = new JLabel("Cliente: " + nombre + " (" + login + ")", JLabel.CENTER);
        lblUsuario.setFont(lblUsuario.getFont().deriveFont(14f));

        panelNorte.add(lblTitulo, BorderLayout.CENTER);
        panelNorte.add(lblUsuario, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);
    }

    private void construirContenido() {
        panelMisTiquetes = new PanelMisTiquetes(cliente);
        add(panelMisTiquetes, BorderLayout.CENTER);
    }

    private void mostrarMisTiquetes() {
        getContentPane().removeAll();
        construirEncabezado();
        panelMisTiquetes = new PanelMisTiquetes(cliente);
        add(panelMisTiquetes, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
