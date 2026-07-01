package vista;

import javax.swing.*;

import controller.JuegoController; 

public class App {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JuegoController controller = JuegoController.getInstance();

            MenuPrincipalView menu = new MenuPrincipalView(controller);

            menu.setVisible(true);
        
        });
    }
}