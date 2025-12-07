package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Eventos.Evento;
import Eventos.Localidad;
import Tiquetes.Individual;
import Tiquetes.Tiquete;
import Usuarios.Cliente;
import qr.QR;

public class PanelTiquete extends JPanel {

    private final Tiquete tiquete;
    private final String contenidoQR;

    private ImageIcon iconoQR;
    private ImageIcon iconoArtista;

    private Color colorFondo;
    private Color colorTexto;

    /**
     * Crea un panel que muestra la información de un tiquete junto con su QR e imagen asociada.
     * @pre tiquete != null
     * @pre contenidoQR puede ser null o vacío (en cuyo caso no se mostrará imagen de QR)
     * @post Se configura el panel con layout BorderLayout, colores, QR e imagen de artista según el evento.
     * @post Se agregan:
     *  Un panel izquierdo con la información básica del tiquete (evento, venue, fecha y valor).
     *  Un panel central con el título del evento, la imagen del artista y la fecha.
     *  Una etiqueta a la derecha con el código QR (o texto de ausencia).
     * @param tiquete tiquete a mostrar en el panel
     * @param contenidoQR contenido que se  codificará en el código QR
     */
    public PanelTiquete(Tiquete tiquete, String contenidoQR) {
        this.tiquete = tiquete;
        this.contenidoQR = contenidoQR;

        configurarColores();
        generarImagenQR();
        cargarImagenArtista();

        setLayout(new BorderLayout());
        setBackground(colorFondo);

        JPanel panelIzquierda = construirPanelIzquierdo();
        JPanel panelCentro = construirPanelCentro();
        JLabel etiquetaQR = construirEtiquetaQR();

        add(panelIzquierda, BorderLayout.WEST);
        add(panelCentro, BorderLayout.CENTER);
        add(etiquetaQR, BorderLayout.EAST);
    }

    /**
     * Ajusta los colores de fondo y texto según el nombre del evento del tiquete.
     * @pre tiquete != null
     * @post Si el evento o su nombre son null, se usan colores por defecto (blanco y negro).
     * @post En caso contrario, colorFondo y colorTexto se ajustan según el tipo/nombre del evento.
     */
    private void configurarColores() {
        colorFondo = Color.WHITE;
        colorTexto = Color.BLACK;

        Evento evento = tiquete.getEvento();
        if (evento == null) {
            return;
        }

        String nombre = evento.getNombre();
        if (nombre == null) {
            return;
        }

        String n = nombre.toLowerCase();
        boolean textoClaro = false;

        if (n.contains("rock al parque")) {
            colorFondo = new Color(25, 60, 35);
            textoClaro = true;
        } else if (n.contains("shakira")) {
            colorFondo = new Color(45, 30, 70);
            textoClaro = true;
        } else if (n.contains("silvestre")) {
            colorFondo = new Color(120, 20, 20);
            textoClaro = true;
        } else if (n.contains("j balvin")) {
            colorFondo = new Color(240, 230, 150);
            textoClaro = false;
        } else if (n.contains("gaiteros") || n.contains("san jacinto")) {
            colorFondo = new Color(200, 180, 140);
            textoClaro = false;
        }

        if (textoClaro) {
            colorTexto = Color.WHITE;
        } else {
            colorTexto = Color.BLACK;
        }
    }

    /**
     * Genera la imagen del código QR a partir del contenido de texto.
     * @pre contenidoQR puede ser null o vacío
     * @post Si contenidoQR es no null y no vacío, se genera un código QR y se asigna a iconoQR.
     * @post Si no se puede generar el QR o el contenido es inválido, iconoQR queda en null.
     */
    private void generarImagenQR() {
        iconoQR = null;
        if (contenidoQR == null) {
            return;
        }
        if (contenidoQR.isEmpty()) {
            return;
        }

        java.awt.image.BufferedImage img = QR.generarQRComoImagen(contenidoQR, 220, 220);
        if (img != null) {
            iconoQR = new ImageIcon(img);
        }
    }

    /**
     * Carga la imagen asociada al artista/evento si existe un recurso mapeado.
     * @pre tiquete != null
     * @post iconoArtista se establece según el nombre del evento o queda null si no hay imagen asociada.
     */
    private void cargarImagenArtista() {
        iconoArtista = null;

        Evento evento = tiquete.getEvento();
        if (evento == null) {
            return;
        }
        String nombre = evento.getNombre();
        if (nombre == null) {
            return;
        }

        String n = nombre.toLowerCase();
        String ruta = null;

        if (n.contains("rock al parque")) {
            ruta = "src/Artistas/RAP.jpeg";
        } else if (n.contains("shakira")) {
            ruta = "src/Artistas/shakiraf.jpeg";
        } else if (n.contains("j balvin")) {
            ruta = "src/Artistas/balvin.jpeg";
        } else if (n.contains("gaiteros") || n.contains("san jacinto")) {
            ruta = "src/Artistas/GaiterosSanJacinto.jpeg";
        } else if (n.contains("fuerza regida")) {
            ruta = "src/Artistas/FuerzaRegida.jpg";
        } else if (n.contains("silvestre")) {
            ruta = "src/Artistas/SilvestreDangond.jpeg";
        }

        if (ruta != null) {
            iconoArtista = new ImageIcon(ruta);
        }
    }

    /**
     * Construye el panel izquierdo con la información básica del tiquete.
     * @pre tiquete != null
     * @post Se construye un panel con información del evento, venue, fecha y valor total del tiquete.
     * @post El panel usa fondo azul oscuro y texto blanco, y tiene tamaño 220x260.
     * @return panel con la información textual resumida del tiquete.
     */
    private JPanel construirPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 40, 70));
        panel.setPreferredSize(new Dimension(220, 260));

        JPanel panelDatos = new JPanel(new java.awt.GridLayout(4, 1, 2, 2));
        panelDatos.setOpaque(false);

        Evento evento = tiquete.getEvento();
        Localidad localidad = tiquete.getLocalidad();
        Cliente dueno = tiquete.getDueno();

        String nombreEvento = "";
        String venueNombre = "";
        String fechaEvento = "";
        String valorTotal = "";

        if (evento != null) {
            if (evento.getNombre() != null) {
                nombreEvento = evento.getNombre();
            }
            if (evento.getVenue() != null && evento.getVenue().getNombre() != null) {
                venueNombre = evento.getVenue().getNombre();
            }
            if (evento.getFecha() != null) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                fechaEvento = evento.getFecha().format(fmt);
            }
        }

        if (tiquete != null) {
            double total = tiquete.calcularCostoTotal();
            valorTotal = String.format("$ %.2f", total);
        }

        JLabel lblNombre = new JLabel("Evento: " + nombreEvento);
        JLabel lblVenue = new JLabel("Venue: " + venueNombre);
        JLabel lblFecha = new JLabel("Fecha: " + fechaEvento);
        JLabel lblValor = new JLabel("Valor: " + valorTotal);

        aplicarColoresTextoClaro(lblNombre);
        aplicarColoresTextoClaro(lblVenue);
        aplicarColoresTextoClaro(lblFecha);
        aplicarColoresTextoClaro(lblValor);

        panelDatos.add(lblNombre);
        panelDatos.add(lblVenue);
        panelDatos.add(lblFecha);
        panelDatos.add(lblValor);

        JLabel lblMarca = new JLabel("BoletaMaster", SwingConstants.CENTER);
        lblMarca.setFont(lblMarca.getFont().deriveFont(Font.BOLD, 14f));
        lblMarca.setForeground(Color.WHITE);
        lblMarca.setOpaque(false);
        lblMarca.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 8, 0));

        panel.add(panelDatos, BorderLayout.CENTER);
        panel.add(lblMarca, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Construye el panel central con el título del evento, la imagen del artista y la fecha.
     * @pre tiquete != null
     * @post Se crea un panel con el nombre del evento en la parte superior,
     * la imagen del artista en el centro y la fecha en la parte inferior.
     * @post El panel usa colorFondo y colorTexto configurados previamente.
     * @return panel con la información visual principal del evento.
     */
    private JPanel construirPanelCentro() {
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(colorFondo);
        panelCentro.setPreferredSize(new Dimension(360, 260));

        Evento evento = tiquete.getEvento();
        String nombreEvento = "";
        String fechaEvento = "";

        if (evento != null) {
            if (evento.getNombre() != null) {
                nombreEvento = evento.getNombre();
            }
            if (evento.getFecha() != null) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                fechaEvento = evento.getFecha().format(fmt);
            }
        }

        JLabel lblTitulo = new JLabel(nombreEvento, SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        aplicarColores(lblTitulo);

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setOpaque(true);
        lblImagen.setBackground(colorFondo);
        if (iconoArtista != null) {
            lblImagen.setIcon(iconoArtista);
        } else {
            lblImagen.setText("Imagen no disponible");
            lblImagen.setForeground(colorTexto);
        }

        JLabel lblFecha = new JLabel(fechaEvento, SwingConstants.CENTER);
        lblFecha.setFont(lblFecha.getFont().deriveFont(14f));
        aplicarColores(lblFecha);

        panelCentro.add(lblTitulo, BorderLayout.NORTH);
        panelCentro.add(lblImagen, BorderLayout.CENTER);
        panelCentro.add(lblFecha, BorderLayout.SOUTH);

        return panelCentro;
    }

    /**
     * Aplica los colores configurados del panel a una etiqueta.
     * @pre label != null
     * @post El label usa colorTexto como color de texto y colorFondo como color de fondo.
     * @param label etiqueta a la que se le aplican los colores del panel.
     */
    private void aplicarColores(JLabel label) {
        label.setForeground(colorTexto);
        label.setBackground(colorFondo);
        label.setOpaque(false);
    }

    /**
     * Aplica colores para texto claro sobre fondo oscuro en el panel izquierdo.
     * @pre label != null
     * @post El label usa texto blanco sobre fondo azul oscuro y no es opaco.
     * @param label etiqueta a la que se le aplican los colores para el panel izquierdo.
     */
    private void aplicarColoresTextoClaro(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(20, 40, 70));
        label.setOpaque(false);
    }

    /**
     * Construye la etiqueta que mostrará el QR.
     * @pre true
     * @post Se construye una etiqueta centrada, con fondo colorFondo y tamaño 220x260.
     * @post Si iconoQR != null se establece como icono; de lo contrario el texto es "QR no disponible".
     * @return etiqueta configurada para mostrar el código QR (o mensaje de ausencia).
     */
    private JLabel construirEtiquetaQR() {
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setBackground(colorFondo);
        lbl.setOpaque(true);
        lbl.setPreferredSize(new Dimension(220, 260));
        if (iconoQR != null) {
            lbl.setIcon(iconoQR);
        } else {
            lbl.setText("QR no disponible");
            lbl.setForeground(colorTexto);
        }
        return lbl;
    }
}

