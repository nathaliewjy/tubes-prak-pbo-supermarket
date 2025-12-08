package view;

import java.util.UUID;

import javax.swing.JOptionPane;
import controller.PosController;
import util.CLIUtil;
import controller.IPosController;
public class PosView {
    private IPosController posController;

    public PosView(IPosController posController) {
        this.posController = posController;
    }

    public void render() {
        JOptionPane.showMessageDialog(null, "Welcome to the POS System!");
    }

    public boolean loginMenu() {
        JOptionPane.showMessageDialog(null, "Login Menu");
        String nik = JOptionPane.showInputDialog(null, "Enter NIK:");
        if (nik == null)
            return false; // User clicked Cancel
        boolean isAuthenticated = posController.userAuthentication(nik);
        if (isAuthenticated) {
            JOptionPane.showMessageDialog(null, "Login Successful!");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Login Failed! Unauthorized User.");
            return false;
        }
    }

    public void initializationMenu() {
        String cashInput = CLIUtil.getString("Enter starting cash amount:");
        try {
            double startingCash = Double.parseDouble(cashInput);
            posController.initializeSession(startingCash);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format!");
        }
    }

    public void createOrderMenu() {
        String memberPhone = CLIUtil.getString("Enter Member Phone Number (Leave empty if none):");
        if (memberPhone == null)
            return; // User clicked Cancel

        if (memberPhone.trim().isEmpty()) {
            posController.createOrder(null);
        } else {
            posController.addMemberToSale(memberPhone);
        }
    }

    public void addProductToOrderMenu() {
        // 1. Ask for SKU
        String sku = CLIUtil.getString("Enter Product SKU:");
        if (sku == null)
            return; // User clicked Cancel

        // 2. Ask for Quantity (Optional)
        String qtyInput = CLIUtil.getString("Enter Quantity (Leave empty for 1):");

        // 3. Logic to handle "Optional"
        if (qtyInput == null || qtyInput.trim().isEmpty()) {
            // Case A: User left it blank -> Call the "Default" method
            posController.addItemToCart(sku);
        } else {
            // Case B: User typed a number -> Call the "Specific" method
            try {
                int quantity = Integer.parseInt(qtyInput);
                posController.addItemToCart(sku, quantity);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format!");
            }
        }
    }

    public void processPaymentMenu() {
    }

    public void endSessionMenu() {
        int cashInRegister = CLIUtil.getInt("Enter cash amount in register:");
        if (cashInRegister == posController.getCurrentCashAmount()) {
            JOptionPane.showMessageDialog(null, "Session ended successfully. Cash amounts match.");
            posController.endSession();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Warning: Cash amounts do not match! Expected: " + posController.getCurrentCashAmount()
                            + ", Found: " + cashInRegister);
            
        }
        
    }
}
