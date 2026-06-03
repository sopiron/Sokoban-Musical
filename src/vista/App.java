package vista;
import javax.swing.SwingUtilities;

import modelo.Tablero;
import util.NivelLoader;




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


        // Menú principal
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalView menu = new MenuPrincipalView();
            menu.setVisible(true);
        });
    }
}
