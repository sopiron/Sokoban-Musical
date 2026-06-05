package vista;

import javax.swing.*;

import modelo.Tablero;
import util.NivelLoader;
import controller.JuegoController; // MELANIE AGRUEGUE EL IMPORT DEL CONTROLLER

public class App {
    public static void main(String[] args) {

        // PRUEBA TEMPORAL
        NivelLoader loader = new NivelLoader();

        Tablero tablero =
                loader.cargarNivel("src/niveles/nivel1.txt");

        System.out.println("Paredes: "
                + tablero.getParedes().size());

        System.out.println("Cajas: "
                + tablero.getCajas().size());

        System.out.println("Destinos: "
                + tablero.getDestinos().size());

        SwingUtilities.invokeLater(() -> {
            MenuPrincipalView menu = new MenuPrincipalView();
            menu.setVisible(true);

            // Programamos qué pasa cuando hacen clic en el botón Jugar
            menu.getBtnJugar().addActionListener(e -> {

                // 1. Creamos una ventana nueva para el juego
                JFrame ventanaJuego = new JFrame("Sokoban - Jugando");
                ventanaJuego.setSize(800, 600);
                ventanaJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventanaJuego.setLocationRelativeTo(null);

                // 2. Creamos nuestro lienzo y lo agregamos a la ventana
                JuegoPanel panelJuego = new JuegoPanel(tablero);
                ventanaJuego.add(panelJuego);

                // 3. Conectamos el controlador al NUEVO panel
                JuegoController controlador = new JuegoController(tablero, panelJuego);
                panelJuego.addKeyListener(controlador);
                panelJuego.setFocusable(true);

                // 4. Ocultamos el menú y mostramos el juego
                menu.setVisible(false);
                ventanaJuego.setVisible(true);

                // Obligamos a que el teclado apunte al juego
                panelJuego.requestFocusInWindow();

            });
        });
    }
}