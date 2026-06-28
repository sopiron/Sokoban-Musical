package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonRedondeado extends JButton {

    private final Color colorNormal;
    private final Color colorHover;

    public BotonRedondeado(String texto, Color colorNormal, Color colorHover) {
        super(texto);

        this.colorNormal = colorNormal;
        this.colorHover = colorHover;

        setBackground(colorNormal);
        setForeground(new Color(255, 245, 220));
        setFont(new Font("SansSerif", Font.BOLD, 18));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(colorHover);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(colorNormal);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(isEnabled() ? getBackground() : new Color(80, 80, 80));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 38, 38);

        g2.setColor(isEnabled() ? new Color(224, 171, 74) : new Color(120, 120, 120));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 38, 38);

        g2.dispose();

        super.paintComponent(g);
    }
}