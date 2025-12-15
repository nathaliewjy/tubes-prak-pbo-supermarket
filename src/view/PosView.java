package view;

import controller.IPosController;
import models.orders.PaymentMethod;
import models.products.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PosView extends JFrame {
    private IPosController controller;

    // Layout Manager
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Login
    private JPanel loginPanel;
    private JTextField nikField;

    // Initialization
    private JPanel initPanel;
    private JTextField startingCashField;

    // Main POS
    private JPanel posPanel;
    private JLabel lblMemberName, lblPoints, lblTotal;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JTextField skuField, qtyField;

    public PosView() {
        setTitle("Minimarket POS System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize Screens
        initLoginScreen();
        initSetupScreen();
        initMainPosScreen();

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(initPanel, "SETUP");
        mainPanel.add(posPanel, "POS");

        add(mainPanel);
    }

    public void setController(IPosController controller) {
        this.controller = controller;
    }

    // =========================================
    // LOGIN UI
    // =========================================
    private void initLoginScreen() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("System Login");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        nikField = new JTextField(15);
        JButton btnLogin = new JButton("Login");

        // Action: Call Controller
        btnLogin.addActionListener(e -> {
            String nik = nikField.getText();
            if (controller.userAuthentication(nik)) {
                // Success: Move to next screen
                cardLayout.show(mainPanel, "SETUP");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid NIK or Unauthorized!");
            }
        });

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(title, gbc);
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Enter NIK:"), gbc);
        gbc.gridy = 2;
        loginPanel.add(nikField, gbc);
        gbc.gridy = 3;
        loginPanel.add(btnLogin, gbc);
    }

    // =========================================
    // INITIALIZATION
    // =========================================
    private void initSetupScreen() {
        initPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Shift Initialization");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        startingCashField = new JTextField(10);
        JButton btnStart = new JButton("Start Session");

        btnStart.addActionListener(e -> {
            try {
                double cash = Double.parseDouble(startingCashField.getText());
                controller.initializeSession(cash);
                cardLayout.show(mainPanel, "POS"); // Move to Main Screen
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Amount!");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        initPanel.add(title, gbc);
        gbc.gridy = 1;
        initPanel.add(new JLabel("Enter Starting Cash:"), gbc);
        gbc.gridy = 2;
        initPanel.add(startingCashField, gbc);
        gbc.gridy = 3;
        initPanel.add(btnStart, gbc);
    }

    // =========================================
    // MAIN POS INTERFACE
    // =========================================
    private void initMainPosScreen() {
        posPanel = new JPanel(new BorderLayout());

        // --- TOP: Info Panel ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBackground(Color.LIGHT_GRAY);

        lblMemberName = new JLabel("Member: Guest");
        lblPoints = new JLabel("Points: 0");
        JButton btnAddMember = new JButton("Add Member");

        btnAddMember.addActionListener(e -> {
            String phone = JOptionPane.showInputDialog(this, "Enter Phone Number:");
            if (phone != null && !phone.isEmpty()) {
                controller.addMemberToSale(phone);
            }
        });

        topPanel.add(lblMemberName);
        topPanel.add(lblPoints);
        topPanel.add(btnAddMember);

        // --- CENTER: Cart Table ---
        String[] columns = { "Product SKU", "Name", "Price", "Qty", "Subtotal" };
        tableModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);

        // --- BOTTOM: Actions & Total ---
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Input Section
        JPanel inputPanel = new JPanel(new FlowLayout());
        skuField = new JTextField(10);
        qtyField = new JTextField(5);
        JButton btnAddItem = new JButton("Add Item");

        btnAddItem.addActionListener(e -> handleAddItem());

        inputPanel.add(new JLabel("SKU:"));
        inputPanel.add(skuField);
        inputPanel.add(new JLabel("Qty:"));
        inputPanel.add(qtyField);
        inputPanel.add(btnAddItem);

        // Total & Pay Section
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: Rp 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        JButton btnPay = new JButton("PAY");
        JButton btnEnd = new JButton("End Session");

        btnPay.setBackground(Color.GREEN);
        btnEnd.setBackground(Color.RED);

        btnPay.addActionListener(e -> handlePayment());
        btnEnd.addActionListener(e -> handleEndSession());

        actionPanel.add(lblTotal);
        actionPanel.add(btnPay);
        actionPanel.add(btnEnd);

        bottomPanel.add(inputPanel, BorderLayout.NORTH);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        posPanel.add(topPanel, BorderLayout.NORTH);
        posPanel.add(scrollPane, BorderLayout.CENTER);
        posPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // =========================================
    // HANDLERS (Connecting Logic)
    // =========================================

    private void handleAddItem() {
        String sku = skuField.getText();
        String qtyStr = qtyField.getText();

        if (sku.isEmpty())
            return;

        if (qtyStr.isEmpty()) {
            controller.addItemToCart(sku);
        } else {
            try {
                int qty = Integer.parseInt(qtyStr);
                controller.addItemToCart(sku, qty);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Quantity");
            }
        }
        skuField.setText("");
        qtyField.setText("");
    }

    private void handlePayment() {
        // 1. Check Points
        boolean usePoints = false;
        int points = controller.getMemberPoints(); 
        if (points > 0) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Member has " + points + " points. Redeem?", "Points", JOptionPane.YES_NO_OPTION);
            usePoints = (choice == JOptionPane.YES_OPTION);
        }

        // 2. Choose Method
        PaymentMethod[] methods = PaymentMethod.values();
        PaymentMethod selectedMethod = (PaymentMethod) JOptionPane.showInputDialog(
                this, "Select Payment Method", "Payment",
                JOptionPane.QUESTION_MESSAGE, null, methods, methods[0]);

        if (selectedMethod == null)
            return;

        double cashReceived = 0;
        if (selectedMethod == PaymentMethod.CASH) {
            String cashStr = JOptionPane.showInputDialog(this, "Enter Cash Received:");
            try {
                cashReceived = Double.parseDouble(cashStr);
                controller.finalizeSale(cashReceived, usePoints);
            } catch (Exception e) {
                return;
            }
        } else {
            controller.finalizeSale(selectedMethod, usePoints);
        }

    }

    public void showPaymentSuccess(double change) {
        String message = "Transaction Successful!";
        if (change > 0) {
            message += String.format("\nChange: Rp %,.2f", change);
        }
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        resetUI();
    }

    public void showPaymentSuccess() {
        JOptionPane.showMessageDialog(this, "Transaction Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        resetUI();
    }

    public void showPaymentFailure(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Payment Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void handleEndSession() {
        String cashStr = JOptionPane.showInputDialog(this, "Enter Actual Cash in Drawer:");
        try {
            double actualCash = Double.parseDouble(cashStr);
            if (controller.endSession(actualCash)) {
                JOptionPane.showMessageDialog(this, "Session Ended Successfully.");
                cardLayout.show(mainPanel, "LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Cash discrepancy detected! Please check.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    // =========================================
    // UPDATE METHODS (Controller calls these)
    // =========================================

    public void resetUI() {
        updateCartTable(new HashMap<>());
        lblTotal.setText("Total: Rp 0");
        updateMemberInfo("Guest", 0);
        skuField.setText("");
        qtyField.setText("");
    }

    public void updateCartTable(Map<Product, Integer> cart) {
        tableModel.setRowCount(0); // Clear table
        double total = 0;

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            double subtotal = p.getPrice() * qty;
            total += subtotal;

            tableModel.addRow(new Object[] {
                    p.getProdID(), // Or p.getSku() if you added that field
                    p.getBrand(),
                    p.getPrice(),
                    qty,
                    subtotal
            });
        }
        lblTotal.setText("Total: Rp " + total);
    }

    public void updateMemberInfo(String name, int points) {
        lblMemberName.setText("Member: " + name);
        lblPoints.setText("Points: " + points);
    }

    public void updateTotalAmount(double amount) {
        lblTotal.setText("Total: Rp " + amount);
    }
}