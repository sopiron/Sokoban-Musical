import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class FondoPanel extends JPanel {

    private Image imagenFondo;

    public FondoPanel(String rutaImagen) {
        URL recurso = getClass().getResource(rutaImagen);

        if (recurso == null) {
            throw new IllegalArgumentException(
                    "No se encontró la imagen de fondo: " + rutaImagen
            );
        }

        imagenFondo = new ImageIcon(recurso).getImage();
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(
                imagenFondo,
                0,
                0,
                getWidth(),
                getHeight(),
                this
        );
    }
}