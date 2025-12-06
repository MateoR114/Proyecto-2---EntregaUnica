package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

    public PanelMisTiquetes(Cliente cliente) {
        this.cliente = cliente;
        this.modeloLista = new DefaultListModel<Tiquete>();
        this.listaTiquetes = new JList<Tiquete>(modeloLista);
        this.botonVer = new JButton("Ver tiquete");

        setLayout(new BorderLayout());
        initUI();
        cargarTiquetes();
    }

    private void initUI() {
        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiquetes.setCellRenderer(new RendererTiquete());

        JScrollPane scroll = new JScrollPane(listaTiquetes);
        scroll.setPreferredSize(new Dimension(400, 250));

        JPanel panelBoton = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        panelBoton.add(botonVer, gbc);

        add(scroll, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        botonVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verTiqueteSeleccionado();
            }
        });
    }

    private void cargarTiquetes() {
        modeloLista.clear();
        for (Tiquete t : cliente.obtenerTiquetesComprados()) {
            modeloLista.addElement(t);
        }
    }

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

    private static class RendererTiquete extends JLabel implements ListCellRenderer<Tiquete> {

        public RendererTiquete() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Tiquete> list,
                Tiquete value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            String evento = "";
            String localidad = "";

            if (value.getEvento() != null && value.getEvento().getNombre() != null) {
                evento = value.getEvento().getNombre();
            }
            if (value.getLocalidad() != null && value.getLocalidad().getNombre() != null) {
                localidad = value.getLocalidad().getNombre();
            }

            String estado;
            if (value.isReembolsado()) {
                estado = "Reembolsado";
            } else if (value.isUsado()) {
                estado = "Usado";
            } else {
                estado = "Vigente";
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

