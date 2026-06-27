package vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import modelo.observer.ObserverBarraJuego;

public class BarraPuntos extends JPanel implements ObserverBarraJuego {

    private JLabel texto;

    public BarraPuntos() {
        setOpaque(false);
        setLayout(new BorderLayout());

        texto = new JLabel("♪ Nivel 1 | Tiempo: 00:00 | Notas: ♫♫♫");
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("SansSerif", Font.BOLD, 22));
        texto.setHorizontalAlignment(SwingConstants.CENTER);

        add(texto, BorderLayout.CENTER);
    }

    @Override
    public void actualizarBarra(int nivel, int segundos, String notas) {
        texto.setText(
                "♪ Nivel " + nivel +
                "   |   Tiempo: " + formatearTiempo(segundos) +
                "   |   Notas: " + notas
        );
    }

    private String formatearTiempo(int segundos) {
        int minutos = segundos / 60;
        int restoSegundos = segundos % 60;

        return String.format("%02d:%02d", minutos, restoSegundos);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        g2.setColor(new Color(224, 171, 74));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);

        g2.dispose();

        super.paintComponent(g);
    }
}