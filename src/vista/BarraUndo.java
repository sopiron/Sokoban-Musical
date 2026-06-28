package vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BarraUndo extends JPanel {

    private static final int MAX_USOS = 3;

    private JLabel texto;
    private int usosRestantes;
    private boolean hover = false;
    private Runnable accionUndo;

    public BarraUndo(Runnable accionUndo) {
        this.accionUndo = accionUndo;
        this.usosRestantes = MAX_USOS;

        setOpaque(false);
        setLayout(new BorderLayout());
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        texto = new JLabel(textoActual());
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("SansSerif", Font.BOLD, 20));
        texto.setHorizontalAlignment(SwingConstants.CENTER);

        add(texto, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (usosRestantes > 0) {
                    hover = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (usosRestantes > 0) {
                    accionUndo.run();
                }
            }
        });
    }

    public void actualizarUsos(int usosRestantes) {
        this.usosRestantes = usosRestantes;
        texto.setText(textoActual());
        texto.setForeground(usosRestantes > 0 ? Color.WHITE : new Color(150, 150, 150));
        setCursor(new Cursor(usosRestantes > 0 ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        repaint();
    }

    // Se llama cuando el jugador hace un movimiento real (resetea usos consecutivos)
    public void resetearUsos() {
        actualizarUsos(MAX_USOS);
    }

    private String textoActual() {
        if (usosRestantes <= 0) {
            return "↩  Undo   |   Sin usos restantes";
        }
        return "↩  Undo   |   Usos restantes: " + usosRestantes + "/" + MAX_USOS;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo: se ilumina un poco al hacer hover
        Color fondo = hover && usosRestantes > 0
                ? new Color(40, 20, 0, 180)
                : new Color(0, 0, 0, 130);

        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Borde: dorado si hay usos, gris si no
        Color borde = usosRestantes > 0
                ? new Color(224, 171, 74)
                : new Color(100, 100, 100);

        g2.setColor(borde);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);

        g2.dispose();

        super.paintComponent(g);
    }
}