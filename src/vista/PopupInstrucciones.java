package vista;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PopupInstrucciones extends JDialog {

    public PopupInstrucciones(JFrame parent) {
        super(parent, "Instrucciones", true);

        setSize(820, 720);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(12, 6, 3, 235));
        panel.setBorder(BorderFactory.createLineBorder(new Color(224, 171, 74), 4));

        JLabel titulo = new JLabel("Instrucciones", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setForeground(new Color(245, 196, 92));
        titulo.setBounds(0, 20, 820, 45);
        panel.add(titulo);

        JLabel cerrar = new JLabel("✕", SwingConstants.CENTER);
        cerrar.setFont(new Font("SansSerif", Font.BOLD, 32));
        cerrar.setForeground(new Color(224, 171, 74));
        cerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cerrar.setBounds(750, 18, 45, 45);
        cerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
            }
        });
        panel.add(cerrar);

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBounds(60, 90, 700, 580);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scroll);

        contenido.add(crearSubtitulo("Controles"));
        contenido.add(Box.createVerticalStrut(10));

        contenido.add(crearFila(
                "/images/teclas-flechas.png",
                "Mover personaje",
                "Usá las flechas para moverte por el tablero."
        ));

        contenido.add(crearFila(
                "/images/tecla-z.png",
                "Undo",
                "Presioná Z para deshacer movimientos."
        ));

        contenido.add(crearFila(
                "/images/tecla-espacio.png",
                "Pausa",
                "Presioná espacio para pausar el juego."
        ));

        contenido.add(Box.createVerticalStrut(18));
        contenido.add(crearSubtitulo("Objetivo"));
        contenido.add(Box.createVerticalStrut(8));

        JLabel objetivo = new JLabel(
                "<html>Empujá las cajas musicales hasta las notas para completar el nivel.</html>"
        );
        objetivo.setForeground(Color.WHITE);
        objetivo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        objetivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(objetivo);

        contenido.add(Box.createVerticalStrut(18));
        contenido.add(crearSubtitulo("Elementos del juego"));
        contenido.add(Box.createVerticalStrut(10));

        contenido.add(crearFila(
                "/images/cajaGuitarra.png",
                "Caja normal",
                "Se empuja en línea recta."
        ));

        contenido.add(crearFila(
                "/images/pisoResbaladizo.png",
                "Piso resbaladizo",
                "La caja se desliza hasta chocar con un obstáculo."
        ));

        contenido.add(crearFila(
                "/images/caja_fragil_rompiendose.gif",
                "Caja frágil",
                "Resiste 7 empujes antes de romperse."
        ));

        contenido.add(crearFila(
                "/images/cajaLlave.png",
                "Caja llave",
                "Activa el cerrojo y abre muros cerrados."
        ));

        setContentPane(panel);
    }

    private JLabel crearSubtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(245, 196, 92));
        label.setFont(new Font("SansSerif", Font.BOLD, 23));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel crearFila(String rutaImagen, String titulo, String descripcion) {
        JPanel fila = new JPanel(new BorderLayout(15, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(600, 70));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel imagen = new JLabel(cargarIcono(rutaImagen, 55, 55));
        imagen.setPreferredSize(new Dimension(70, 60));

        JLabel texto = new JLabel(
                "<html><b>" + titulo + "</b><br>" + descripcion + "</html>"
        );
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("SansSerif", Font.PLAIN, 17));

        fila.add(imagen, BorderLayout.WEST);
        fila.add(texto, BorderLayout.CENTER);

        return fila;
    }

    private ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        URL url = getClass().getResource(ruta);

        if (url == null) {
            throw new IllegalArgumentException("No se encontró la imagen: " + ruta);
        }

        if (ruta.toLowerCase().endsWith(".gif")) {
            return new ImageIcon(url);
        }

        Image imagen = new ImageIcon(url).getImage();
        Image imagenEscalada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

        return new ImageIcon(imagenEscalada);
    }
}