package view;

import javax.swing.JOptionPane;
import controller.PosController;

public class PosView {
    private PosController posController;
    public void render() {
        JOptionPane.showMessageDialog(null, "Welcome to the POS System!");
    }

    public void loginMenu() {
        JOptionPane.showMessageDialog(null, "Login Menu");
        JOptionPane.showInputDialog(null, "Enter NIK:");
    }

    
    public void initializationMenu() {
        
    }

    public void createOrderMenu() {
    }

    public void addProductToOrderMenu() {
    }

    public void processPaymentMenu() {
    }

    public void endSessionMenu() {
    }
}
