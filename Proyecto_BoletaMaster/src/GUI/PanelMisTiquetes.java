package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import Tiquetes.Tiquete;
import Usuarios.Cliente;

public class PanelMisTiquetes extends JPanel {

    private final Cliente cliente;
    private final DefaultListModel<Tiquete> modeloLista;
    private final JList<Tiquete> listaTiquetes;
    private final JButton botonVer;

    /**
     * Crea un panel para ver la lista de tiquetes de un cliente.
     * @pre cliente != null
     * @post Se crea un panel con barra de título, lista de tiquetes del cliente y un botón "Ver tiquete".
     * @post Se cargan en la lista los tiquetes retornados por cliente.obtenerTiquetesComprados().
     * @param cliente dueño de los tiquetes
     */
    public PanelMisTiquetes(Cliente cliente) {
        this.cliente = cliente;
        this.modeloLista = new DefaultListModel<Tiquete>();
        this.listaTiquetes = new JList<Tiquete>(modeloLista);
        this.botonVer = new JButton("Ver tiquete");

        setLayout(new BorderLayout());
        initUI();
        cargarTiquetes();
    }

    /**
     * Inicia la interfaz del panel de tiquetes.
     * @pre modeloLista y listaTiquetes han sido inicializados
     * @post Se configura un panel de título en la parte superior con la marca y un texto.
     * @post La lista se configura para selección única y con un renderer personalizado.
     * @post Se agrega la lista en el centro y el botón "Ver tiquete" abajo.
     * @post El botón queda asociado a la acción de verTiqueteSeleccionado().
     */
    private void initUI() {
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(25, 70, 120));
        panelTitulo.setPreferredSize(new Dimension(100, 40));

        JLabel lblMarca = new JLabel("BoletaMaster");
        lblMarca.setForeground(Color.WHITE);
        lblMarca.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel lblTexto = new JLabel("Seleccione su tiquete");
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setHorizontalAlignment(JLabel.RIGHT);
        lblTexto.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        panelTitulo.add(lblMarca, BorderLayout.WEST);
        panelTitulo.add(lblTexto, BorderLayout.EAST);

        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiquetes.setCellRenderer(new RendererTiquete());

        JScrollPane scroll = new JScrollPane(listaTiquetes);
        scroll.setPreferredSize(new Dimension(400, 250));

        JPanel panelBoton = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        panelBoton.add(botonVer, gbc);

        add(panelTitulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        botonVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verTiqueteSeleccionado();
            }
        });
    }

    /**
     * Carga en la lista los tiquetes del cliente.
     * @pre cliente != null y cliente.obtenerTiquetesComprados() no retorna null
     * @post modeloLista se limpia y luego contiene todos los tiquetes del cliente en orden.
     */
    private void cargarTiquetes() {
        modeloLista.clear();
        for (Tiquete t : cliente.obtenerTiquetesComprados()) {
            modeloLista.addElement(t);
        }
    }

    /**
     * Abre el diálogo de detalle para el tiquete seleccionado.
     * @pre listaTiquetes está inicializada y puede tener un elemento seleccionado
     * @post Si no hay selección, no se realiza ninguna acción.
     * @post Si hay selección, se construye el contenido QR, se crea un DialogoTiquete y se muestra (setVisible(true)).
     */
    private void verTiqueteSeleccionado() {
        Tiquete seleccionado = listaTiquetes.getSelectedValue();
        if (seleccionado == null) {
            return;
        }

        String contenidoQR = generarContenidoQR(seleccionado);

        DialogoTiquete dialogo = new DialogoTiquete(
                SwingUtilities.getWindowAncestor(this),
                seleccionado,
                contenidoQR
        );
        dialogo.setVisible(true);
    }

    /**
     * Genera el contenido de texto para el código QR de un tiquete.
     * @pre t != null
     * @post Se construye una cadena de texto con campos clave del tiquete (evento, id, localidad, dueño, precio y total).
     * @param t tiquete del cual se extrae la información para el QR
     * @return cadena con formato "EVENTO:...|ID_EVENTO:...|LOCALIDAD:...|DUENO:...|PRECIO:...|TOTAL:..."
     */
    private String generarContenidoQR(Tiquete t) {
        String evento = "";
        String idEvento = "";
        String localidad = "";
        String dueno = "";

        if (t.getEvento() != null) {
            if (t.getEvento().getNombre() != null) {
                evento = t.getEvento().getNombre();
            }
            idEvento = String.valueOf(t.getEvento().getId());
        }

        if (t.getLocalidad() != null && t.getLocalidad().getNombre() != null) {
            localidad = t.getLocalidad().getNombre();
        }

        if (t.getDueno() != null && t.getDueno().getLogin() != null) {
            dueno = t.getDueno().getLogin();
        }

        return "EVENTO:" + evento
                + "|ID_EVENTO:" + idEvento
                + "|LOCALIDAD:" + localidad
                + "|DUENO:" + dueno
                + "|PRECIO:" + t.getPrecio()
                + "|TOTAL:" + t.calcularCostoTotal();
    }

    private static class RendererTiquete extends JLabel implements ListCellRenderer {

        /**
         * Constructor del renderer para las celdas de la lista de tiquetes.
         * @pre true
         * @post El renderer queda opaco y con un borde interno mínimo para espaciar el texto.
         */
        public RendererTiquete() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        }

        /**
         * Configuración para representar un tiquete en la lista.
         * @pre list != null
         * @pre value == null o value es instancia de Tiquete
         * @post Si value es un Tiquete, el texto del JLabel representa evento, localidad y estado
         * del tiquete (Vigente, Usado o Reembolsado).
         * @post Los colores de fondo y texto se ajustan según si la celda está seleccionada.
         * @param list lista que contiene el elemento a renderizar
         * @param value objeto a mostrar en la celda (se espera un Tiquete)
         * @param index índice del elemento en la lista
         * @param isSelected indica si la celda está seleccionada
         * @param cellHasFocus indica si la celda tiene foco
         * @return JLabel configurado como componente de celda
         */
        
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            String evento = "";
            String localidad = "";
            String estado = "";

            if (value instanceof Tiquete) {
                Tiquete t = (Tiquete) value;

                if (t.getEvento() != null && t.getEvento().getNombre() != null) {
                    evento = t.getEvento().getNombre();
                }
                if (t.getLocalidad() != null && t.getLocalidad().getNombre() != null) {
                    localidad = t.getLocalidad().getNombre();
                }

                if (t.isReembolsado()) {
                    estado = "Reembolsado";
                } else if (t.isUsado()) {
                    estado = "Usado";
                } else {
                    estado = "Vigente";
                }
            }

            setText(evento + " - " + localidad + " - " + estado);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }
}
