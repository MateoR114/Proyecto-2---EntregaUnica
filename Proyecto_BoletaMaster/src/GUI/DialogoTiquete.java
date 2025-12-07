package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import Tiquetes.Tiquete;

public class DialogoTiquete extends JDialog {

    /**
     * Crea un diálogo para mostrar el detalle de un tiquete.
     * @pre tiquete != null
     * @pre contenidoQR != null
     * @post Se intenta registrar la impresión del tiquete llamando a tiquete.registrarImpresion().
     * Si no es posible (por ejemplo, por reimpresión no permitida), se captura IllegalStateException
     * y se continúa la ejecución sin interrumpir la interfaz.
     * @post Se construye un PanelTiquete con el tiquete y el contenido del QR y se agrega
     * dentro de un JScrollPane al centro del diálogo.
     * @param owner ventana padre sobre la cual se centrará el diálogo (puede ser null)
     * @param tiquete tiquete cuyo detalle se mostrará y sobre el que se intenta registrar impresión
     * @param contenidoQR contenido en texto que representa el código QR asociado al tiquete
     */
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
        setPreferredSize(new Dimension(900, 450));
        pack();
        setLocationRelativeTo(owner);
    }
}
