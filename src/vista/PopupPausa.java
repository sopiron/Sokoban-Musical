package vista;

import controller.JuegoController;
import modelo.GestorSonido;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PopupPausa extends JDialog {

    public PopupPausa(JFrame padre, JuegoController controller, Runnable alReiniciar, Runnable alVolverAlMenu) {
        super(padre, "Pausa", true);

        setUndecorated(true);
        setSize(420, 320);
        setLocationRelativeTo(padre);
        setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 8, 3, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(224, 171, 74));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(null);

        // ── Botón cerrar (X) arriba a la derecha ──
        JLabel btnCerrar = new JLabel("✕", SwingConstants.CENTER);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 22));
        btnCerrar.setForeground(new Color(224, 171, 74));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBounds(365, 15, 40, 40);

        btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cerrar(controller);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCerrar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCerrar.setForeground(new Color(224, 171, 74));
            }
        });

        panel.add(btnCerrar);

        // ── Título ──
        JLabel titulo = new JLabel("  PAUSADO", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(224, 171, 74));
        titulo.setBounds(0, 20, 420, 50);
        panel.add(titulo);

        // ── Notas actuales ──
        String notas = controller.getNotasActuales();
        JLabel lblNotas = new JLabel("Notas: " + notas, SwingConstants.CENTER);
        lblNotas.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNotas.setForeground(Color.WHITE);
        lblNotas.setBounds(0, 85, 420, 40);
        panel.add(lblNotas);

        // ── Botón Reiniciar nivel ──
        BotonRedondeado btnReiniciar = new BotonRedondeado(
                "Reiniciar nivel",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnReiniciar.setBounds(60, 150, 300, 50);
        btnReiniciar.setFocusable(false);
        btnReiniciar.addActionListener(e -> {
            dispose();
            alReiniciar.run();
        });
        panel.add(btnReiniciar);

        // ── Botón Menú principal ──
        BotonRedondeado btnMenu = new BotonRedondeado(
                "Menu principal",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnMenu.setBounds(60, 215, 300, 50);
        btnMenu.setFocusable(false);
        btnMenu.addActionListener(e -> {
            GestorSonido.getInstance().detenerMusica();
            dispose();
            alVolverAlMenu.run();
        });
        panel.add(btnMenu);

        setContentPane(panel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrar(controller);
            }
        });
    }

    private void cerrar(JuegoController controller) {
        controller.reanudar();
        dispose();
    }
}