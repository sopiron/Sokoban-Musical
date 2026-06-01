import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalView menu = new MenuPrincipalView();
            menu.setVisible(true);
        });
    }
}
