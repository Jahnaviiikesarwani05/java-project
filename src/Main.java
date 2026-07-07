import gui.BillingGUI;

public class Main {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            new BillingGUI().setVisible(true);
        });
    }
}