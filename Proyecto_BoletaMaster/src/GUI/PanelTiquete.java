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

    public PanelTiquete(Tiquete tiquete, String contenidoQR) {
        this.tiquete = tiquete;
        this.contenidoQR = contenidoQR;

        configurarColores();
        generarImagenQR();
        cargarImagenArtista();

        setLayout(new BorderLayout());

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(colorFondo);

        JPanel panelInfo = construirPanelInfo();
        JLabel etiquetaArtista = construirEtiquetaArtista();
        JLabel etiquetaQR = construirEtiquetaQR();

        panelCentro.add(panelInfo, BorderLayout.WEST);
        panelCentro.add(etiquetaArtista, BorderLayout.CENTER);
        panelCentro.add(etiquetaQR, BorderLayout.EAST);

        add(panelCentro, BorderLayout.CENTER);
    }

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

    private void generarImagenQR() {
        iconoQR = null;
        if (contenidoQR == null) {
            return;
        }
        if (contenidoQR.isEmpty()) {
            return;
        }

        java.awt.image.BufferedImage img = QR.generarQRComoImagen(contenidoQR, 250, 250);
        if (img != null) {
            iconoQR = new ImageIcon(img);
        }
    }

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
            ruta = "src/Artistas/shakira.jpg";
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

    private JPanel construirPanelInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(0, 1, 2, 2));
        panel.setBackground(colorFondo);
        panel.setPreferredSize(new Dimension(280, 260));

        Evento evento = tiquete.getEvento();
        Localidad localidad = tiquete.getLocalidad();
        Cliente dueno = tiquete.getDueno();

        String nombreEvento = "";
        String fechaEvento = "";
        String venueNombre = "";
        String nombreLocalidad = "";
        String nombreDueno = "";
        String loginDueno = "";

        if (evento != null) {
            String n = evento.getNombre();
            if (n != null) {
                nombreEvento = n;
            }

            if (evento.getFecha() != null) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                fechaEvento = evento.getFecha().format(fmt);
            }

            if (evento.getVenue() != null && evento.getVenue().getNombre() != null) {
                venueNombre = evento.getVenue().getNombre();
            }
        }

        if (localidad != null && localidad.getNombre() != null) {
            nombreLocalidad = localidad.getNombre();
        }

        if (dueno != null) {
            if (dueno.getNombre() != null) {
                nombreDueno = dueno.getNombre();
            }
            if (dueno.getLogin() != null) {
                loginDueno = dueno.getLogin();
            }
        }

        String textoAsiento = "";
        if (tiquete instanceof Individual) {
            Individual ind = (Individual) tiquete;
            int num = ind.getNumeroAsiento();
            if (num > 0) {
                textoAsiento = "Asiento: " + num;
            } else {
                textoAsiento = "Asiento: No numerado";
            }
        }

        String textoPrecio = String.format("Valor base: %.2f", tiquete.getPrecio());
        String textoTotal = String.format("Total (precio + cargo + emisión): %.2f", tiquete.calcularCostoTotal());

        String textoEstadoUso = "Estado uso: ";
        if (tiquete.isReembolsado()) {
            textoEstadoUso = textoEstadoUso + "Reembolsado";
        } else if (tiquete.isUsado()) {
            textoEstadoUso = textoEstadoUso + "Usado";
        } else {
            textoEstadoUso = textoEstadoUso + "Vigente";
        }

        String textoTransferible;
        if (tiquete.isTransferible()) {
            textoTransferible = "Transferible: Sí";
        } else {
            textoTransferible = "Transferible: No";
        }

        JLabel lblTitulo = new JLabel(nombreEvento);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        aplicarColores(lblTitulo);
        panel.add(lblTitulo);

        JLabel lblFecha = new JLabel("Fecha: " + fechaEvento);
        aplicarColores(lblFecha);
        panel.add(lblFecha);

        JLabel lblLugar = new JLabel("Lugar: " + venueNombre);
        aplicarColores(lblLugar);
        panel.add(lblLugar);

        JLabel lblLoc = new JLabel("Localidad: " + nombreLocalidad);
        aplicarColores(lblLoc);
        panel.add(lblLoc);

        JLabel lblAsiento = new JLabel(textoAsiento);
        aplicarColores(lblAsiento);
        panel.add(lblAsiento);

        JLabel lblTitular = new JLabel("Titular: " + nombreDueno + " (" + loginDueno + ")");
        aplicarColores(lblTitular);
        panel.add(lblTitular);

        JLabel lblPrecio = new JLabel(textoPrecio);
        aplicarColores(lblPrecio);
        panel.add(lblPrecio);

        JLabel lblTotal = new JLabel(textoTotal);
        aplicarColores(lblTotal);
        panel.add(lblTotal);

        JLabel lblEstado = new JLabel(textoEstadoUso);
        aplicarColores(lblEstado);
        panel.add(lblEstado);

        JLabel lblTrans = new JLabel(textoTransferible);
        aplicarColores(lblTrans);
        panel.add(lblTrans);

        return panel;
    }

    private void aplicarColores(JLabel label) {
        label.setForeground(colorTexto);
        label.setBackground(colorFondo);
        label.setOpaque(false);
    }

    private JLabel construirEtiquetaQR() {
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setBackground(colorFondo);
        lbl.setOpaque(true);
        lbl.setPreferredSize(new Dimension(260, 260));
        if (iconoQR != null) {
            lbl.setIcon(iconoQR);
        } else {
            lbl.setText("QR no disponible");
            lbl.setForeground(colorTexto);
        }
        return lbl;
    }

    private JLabel construirEtiquetaArtista() {
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setBackground(colorFondo);
        lbl.setOpaque(true);
        lbl.setPreferredSize(new Dimension(320, 260));
        if (iconoArtista != null) {
            lbl.setIcon(iconoArtista);
        } else {
            lbl.setText("Imagen no disponible");
            lbl.setForeground(colorTexto);
        }
        return lbl;
    }
}

