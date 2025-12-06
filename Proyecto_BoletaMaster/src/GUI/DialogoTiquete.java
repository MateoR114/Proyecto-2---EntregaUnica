package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import Tiquetes.Tiquete;

public class DialogoTiquete extends JDialog {

    public DialogoTiquete(Window owner, Tiquete tiquete, String contenidoQR) {
        super(owner, "Detalle de tiquete", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        try {
            tiquete.registrarImpresion();
        } catch (IllegalStateException e) {
            System.out.println("Intento de reimpresión bloqueado: " + e.getMessage());
        }

        PanelTiquete panel = new PanelTiquete(tiquete, contenidoQR);
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);

        setPreferredSize(new Dimension(700, 400));
        pack();
        setLocationRelativeTo(owner);
    }
}
