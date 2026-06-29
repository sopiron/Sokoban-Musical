package vista;

import modelo.ResultadoNivel;

import javax.swing.*;
import java.awt.*;

public class PopupResultadoNivel extends JDialog {

    private AccionPopupNivel accionSeleccionada;

    public PopupResultadoNivel(
            JFrame padre,
            ResultadoNivel resultado,
            int nivelCompletado,
            boolean haySiguiente
    ) {
        super(padre, "Resultado del nivel", true);

        setUndecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(520, 520);
        setLocationRelativeTo(padre);
        setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

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

        JLabel titulo = new JLabel("♪ NIVEL " + nivelCompletado + " COMPLETADO", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(224, 171, 74));
        titulo.setBounds(0, 30, 520, 45);
        panel.add(titulo);

        JLabel lblTiempo = crearLabel("Tiempo: " + resultado.getTiempoFormateado(), 95);
        JLabel lblMovimientos = crearLabel("Movimientos: " + resultado.getMovimientos(), 130);
        JLabel lblEmpujes = crearLabel("Empujes: " + resultado.getEmpujes(), 165);
        JLabel lblUndo = crearLabel("Uso de undo: " + resultado.getUsosUndo(), 200);

        panel.add(lblTiempo);
        panel.add(lblMovimientos);
        panel.add(lblEmpujes);
        panel.add(lblUndo);

        JLabel lblNotasTitulo = new JLabel("Notas:", SwingConstants.CENTER);
        lblNotasTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblNotasTitulo.setForeground(Color.WHITE);
        lblNotasTitulo.setBounds(0, 240, 520, 35);
        panel.add(lblNotasTitulo);

        JLabel lblNotas = new JLabel(resultado.getNotas(), SwingConstants.CENTER);
        lblNotas.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblNotas.setForeground(new Color(224, 171, 74));
        lblNotas.setBounds(0, 280, 520, 45);
        panel.add(lblNotas);

        JLabel lblPuntaje = new JLabel("Puntaje final: " + resultado.getPuntajeFinal(), SwingConstants.CENTER);
        lblPuntaje.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblPuntaje.setForeground(Color.WHITE);
        lblPuntaje.setBounds(0, 340, 520, 45);
        panel.add(lblPuntaje);

        if (haySiguiente) {
            BotonRedondeado btnSiguiente = new BotonRedondeado(
                    "▶  Pasar al próximo nivel",
                    new Color(91, 53, 29),
                    new Color(166, 103, 45)
            );

            btnSiguiente.setBounds(90, 400, 340, 50);
            btnSiguiente.setFocusable(false);
            btnSiguiente.addActionListener(e -> {
                accionSeleccionada = AccionPopupNivel.SIGUIENTE;
                dispose();
            });

            panel.add(btnSiguiente);
        }

        BotonRedondeado btnHome = new BotonRedondeado(
                "🏠  Menú principal",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );

        btnHome.setBounds(90, haySiguiente ? 460 : 410, 340, 50);
        btnHome.setFocusable(false);
        btnHome.addActionListener(e -> {
            accionSeleccionada = AccionPopupNivel.HOME;
            dispose();
        });

        panel.add(btnHome);

        setContentPane(panel);
    }

    private JLabel crearLabel(String texto, int y) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 21));
        label.setForeground(Color.WHITE);
        label.setBounds(0, y, 520, 30);
        return label;
    }

    public AccionPopupNivel getAccionSeleccionada() {
        return accionSeleccionada;
    }
}