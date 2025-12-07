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

    /**
     * @pre cliente != null
     * @pre la ventana se crea  en el hilo de Swing 
     * @post Se crea una ventana con título "BoletaMaster - Tus Boletas", menú, encabezado y panel de tiquetes
     * @post La ventana queda empaquetada (pack()), centrada en pantalla y en estado maximizado
     * @param cliente cliente autenticado cuyos tiquetes se mostrarán en la ventana
     */
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

    /**
     * @pre true
     * @post Se configura la barra de menú con:
     * Menú "Archivo" con opción "Salir" que cierra la ventana
     * Menú "Ver" con opción "Mis tiquetes" que llama mostrarMisTiquetes()
     * @post La barra de menú queda asociada a la ventana vía setJMenuBar(...)
     */
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

    /**
     * @pre cliente != null
     * @post Se crea un panel en la zona norte con el título de la aplicación y
     * una etiqueta que muestra el nombre y login del cliente
     * @post El panel se añade en BorderLayout.NORTH del content pane
     */
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

    /**
     * @pre cliente != null
     * @post Se crea un PanelMisTiquetes asociado al cliente
     * @post El panel se añade en BorderLayout.CENTER del content pane
     */
    private void construirContenido() {
        panelMisTiquetes = new PanelMisTiquetes(cliente);
        add(panelMisTiquetes, BorderLayout.CENTER);
    }

    /**
     * @pre cliente != null
     * @post Se limpia el contenido actual de la ventana
     * @post Se reconstruye el encabezado y un nuevo PanelMisTiquetes con el cliente actual
     * @post El layout se revalida y repinta para reflejar los cambios en pantalla
     */
    private void mostrarMisTiquetes() {
        getContentPane().removeAll();
        construirEncabezado();
        panelMisTiquetes = new PanelMisTiquetes(cliente);
        add(panelMisTiquetes, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
