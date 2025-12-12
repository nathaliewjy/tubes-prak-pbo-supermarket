package view;

import controller.InventoryController;
import models.jobdesk.RequestRestock;
import models.products.Product;
import models.products.ProductCategory;
import models.users.Employee;
import repository.EmployeeRepository;
import repository.ProductRepository;
import repository.RequestRestockRepository;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryView extends JFrame {

    private InventoryController inventoryController;
    private Employee currentEmployee;

    // Tables & models
    private DefaultTableModel productTableModel;
    private JTable productTable;

    private DefaultTableModel requestTableModel;
    private JTable requestTable;

    // Login
    private JTextField nikField;

    // Inputs used across tabs
    private JTextField txtExpiredDays;
    private JComboBox<ProductCategory> cbCategory;
    private JTextField txtProductName;
    private JTextField txtShelf;
    private JTextField txtStorage;

    // User info labels
    private JLabel lblEmployeeName, lblEmployeeRole, lblEmployeeNIK;

    // Main components
    private JTabbedPane tabbedPane;

    public InventoryView() {
        inventoryController = new InventoryController(
                new ProductRepository(),
                new RequestRestockRepository(),
                new EmployeeRepository());

        setTitle("Inventory Management System");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

    }

    private void initUI() {
        // Main container uses BorderLayout
        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        // Start with login panel shown in center
        JPanel loginPanel = initLoginPanel();
        root.add(loginPanel, BorderLayout.CENTER);
    }

    // ========== LOGIN ==========
    private JPanel initLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel title = new JLabel("Inventory System Login");
        JLabel subtitle = new JLabel("For Stocker & Manager Only");

        nikField = new JTextField(15);
        JButton btnLogin = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(title, gbc);

        gbc.gridy = 1;
        loginPanel.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Enter NIK:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(nikField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String nik = nikField.getText().trim();

            if (nik.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your NIK!");
                return;
            }

            if (inventoryController.Login(nik)) {
                this.currentEmployee = inventoryController.getCurrentEmployee();
                showDashboard();
                updateUserInfo();
                nikField.setText("");
                JOptionPane.showMessageDialog(this, "Welcome, " + currentEmployee.getName() + "!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid NIK or Unauthorized!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                nikField.setText("");
            }
        });

        return loginPanel;
    }

    // ========== DASHBOARD (TABBED PANE) ==========
    private void showDashboard() {
        // If already showing tabbed pane, just ensure it's selected
        if (tabbedPane != null) {
            // replace center component with tabbedPane if not already
            getContentPane().removeAll();
            getContentPane().add(createTopPanel(), BorderLayout.NORTH);
            getContentPane().add(tabbedPane, BorderLayout.CENTER);
            revalidate();
            repaint();
            return;
        }

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Products", createProductsTab());
        tabbedPane.addTab("Expired", createExpiredTab());
        tabbedPane.addTab("Category", createCategoryTab());
        tabbedPane.addTab("Update Stock", createUpdateTab());
        tabbedPane.addTab("My Requests", createMyRequestsTab());

        // Listen for tab changes. When Products tab is selected, auto-load all
        // products.
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int idx = tabbedPane.getSelectedIndex();
                String title = tabbedPane.getTitleAt(idx);
                if ("Products".equals(title)) {
                    displayProducts(inventoryController.getAllProducts());
                } else if ("My Requests".equals(title)) {
                    refreshMyRequests();
                }
                // other tabs can remain reactive to their own buttons
            }
        });

        getContentPane().removeAll();
        getContentPane().add(createTopPanel(), BorderLayout.NORTH);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        revalidate();
        repaint();

        // Auto-select Products tab (and it will load products via the listener)
        tabbedPane.setSelectedIndex(0);
    }

    // Top panel with user info and logout
    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        lblEmployeeName = new JLabel("User: -");
        lblEmployeeRole = new JLabel("| Role: -");
        lblEmployeeNIK = new JLabel("| NIK: -");

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                currentEmployee = null;
                updateUserInfo();
                // return to login screen
                getContentPane().removeAll();
                getContentPane().add(initLoginPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        userInfo.add(lblEmployeeName);
        userInfo.add(lblEmployeeRole);
        userInfo.add(lblEmployeeNIK);
        userInfo.add(Box.createHorizontalStrut(20));
        userInfo.add(btnLogout);

        top.add(userInfo, BorderLayout.CENTER);
        return top;
    }

    private void updateUserInfo() {
        if (currentEmployee != null) {
            lblEmployeeName.setText("User: " + currentEmployee.getName());
            lblEmployeeRole.setText("| Role: " + currentEmployee.getRole());
            // keep your getter name as in your model
            try {
                lblEmployeeNIK.setText("| NIK: " + currentEmployee.getNik());
            } catch (Exception ex) {
                // fallback if different getter exists
                try {
                    lblEmployeeNIK.setText("| NIK: " + currentEmployee.getNik());
                } catch (Exception ignored) {
                    lblEmployeeNIK.setText("| NIK: -");
                }
            }
        } else {
            lblEmployeeName.setText("User: -");
            lblEmployeeRole.setText("| Role: -");
            lblEmployeeNIK.setText("| NIK: -");
        }
    }

    // ========== TAB CONTENTS ==========

    // Products tab: table that auto-shows all products when tab activated
    private JPanel createProductsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] productColumns = {
                "Name", "Category", "Shelf Stock", "Storage Stock", "Expiry Date"
        };

        productTableModel = new DefaultTableModel(productColumns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        productTable = new JTable(productTableModel);
        productTable.setRowHeight(24);

        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // bottom action panel with a Refresh button (optional)
        JPanel bottom = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> displayProducts(inventoryController.getAllProducts()));
        bottom.add(btnRefresh);

        panel.add(bottom, BorderLayout.SOUTH);

        // when selecting a row, autofill update fields (if update tab exists)
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                int r = productTable.getSelectedRow();
                txtProductName.setText(productTable.getValueAt(r, 0).toString());
                txtShelf.setText(productTable.getValueAt(r, 2).toString());
                txtStorage.setText(productTable.getValueAt(r, 3).toString());
                // optionally switch to update tab:
                // tabbedPane.setSelectedIndex(3); // index of Update Stock tab
            }
        });

        return panel;
    }

    private JPanel createExpiredTab() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel form = new JPanel();
        txtExpiredDays = new JTextField(6);
        JButton btnSearch = new JButton("Search");

        form.add(new JLabel("Days to expiry:"));
        form.add(txtExpiredDays);
        form.add(btnSearch);

        // reuse product table view to show results
        DefaultTableModel expiredModel = new DefaultTableModel(
                new String[] { "Name", "Category", "Shelf Stock", "Storage Stock", "Expiry Date" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable expiredTable = new JTable(expiredModel);
        expiredTable.setRowHeight(24);

        btnSearch.addActionListener(e -> {
            try {
                int days = Integer.parseInt(txtExpiredDays.getText().trim());
                ArrayList<Product> expired = inventoryController.getAllExpiredProducts(days);
                expiredModel.setRowCount(0);
                if (expired == null || expired.isEmpty()) {
                    info("No expired products found");
                } else {
                    for (Product pdt : expired) {
                        expiredModel.addRow(new Object[] {
                                pdt.getBrand(),
                                pdt.getCategory(),
                                pdt.getStockInShelf(),
                                pdt.getStockInStorage(),
                                pdt.getExpiryDate()
                        });
                    }
                }
            } catch (Exception ex) {
                error("Days must be a number");
            }
            expiredTable.revalidate();
            expiredTable.repaint();
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(expiredTable), BorderLayout.CENTER);
        return p;
    }

    private JPanel createCategoryTab() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel form = new JPanel();
        cbCategory = new JComboBox<>(ProductCategory.values());
        JButton btnSearch = new JButton("Search");

        form.add(new JLabel("Category:"));
        form.add(cbCategory);
        form.add(btnSearch);

        DefaultTableModel catModel = new DefaultTableModel(
                new String[] { "Name", "Category", "Shelf Stock", "Storage Stock", "Expiry Date" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable catTable = new JTable(catModel);
        catTable.setRowHeight(24);

        btnSearch.addActionListener(e -> {
            ProductCategory cat = (ProductCategory) cbCategory.getSelectedItem();
            ArrayList<Product> list = inventoryController.getProductsByCategory(cat);
            catModel.setRowCount(0);
            if (list == null || list.isEmpty()) {
                info("No products found for category: " + cat);
                return;
            }
            for (Product pdt : list) {
                catModel.addRow(new Object[] {
                        pdt.getBrand(),
                        pdt.getCategory(),
                        pdt.getStockInShelf(),
                        pdt.getStockInStorage(),
                        pdt.getExpiryDate()
                });
            }
            catTable.revalidate();
            catTable.repaint();
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(catTable), BorderLayout.CENTER);
        return p;
    }

    private JPanel createUpdateTab() {
        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));

        txtProductName = new JTextField();
        txtShelf = new JTextField();
        txtStorage = new JTextField();

        JButton btnSubmit = new JButton("Update");

        p.add(new JLabel("Product Name:"));
        p.add(txtProductName);
        p.add(new JLabel("Shelf Stock:"));
        p.add(txtShelf);
        p.add(new JLabel("Storage Stock:"));
        p.add(txtStorage);
        p.add(new JLabel());
        p.add(btnSubmit);

        btnSubmit.addActionListener(e -> updateStock());

        return p;
    }

    private JPanel createMyRequestsTab() {
        JPanel p = new JPanel(new BorderLayout());

        String[] columns = { "Product ID", "Quantity", "Status", "Date" };
        requestTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        requestTable = new JTable(requestTableModel);
        requestTable.setRowHeight(24);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> refreshMyRequests());

        JPanel bottom = new JPanel();
        bottom.add(btnRefresh);

        p.add(new JScrollPane(requestTable), BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    // ========== HELPERS / LOGIC ==========

    private void displayProducts(ArrayList<Product> products) {
        if (productTableModel == null)
            return; // guard
        productTableModel.setRowCount(0);

        if (products == null || products.isEmpty()) {
            info("No products found");
            productTable.revalidate();
            productTable.repaint();
            return;
        }

        for (Product p : products) {
            productTableModel.addRow(new Object[] {
                    p.getBrand(),
                    p.getCategory(),
                    p.getStockInShelf(),
                    p.getStockInStorage(),
                    p.getExpiryDate()
            });
        }

        productTable.revalidate();
        productTable.repaint();
    }

    private void updateStock() {
        try {
            String name = txtProductName.getText().trim();
            int shelf = Integer.parseInt(txtShelf.getText().trim());
            int storage = Integer.parseInt(txtStorage.getText().trim());

            boolean success = inventoryController.updateStockByName(name, shelf, storage);

            info(success ? "Stock updated" : "Product not found");

            if (success) {
                // If Products tab exists, refresh it
                if (tabbedPane != null && "Products".equals(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()))) {
                    displayProducts(inventoryController.getAllProducts());
                }
            }

        } catch (Exception e) {
            error("Invalid input");
        }
    }

    private void refreshMyRequests() {
        if (requestTableModel == null)
            return;
        requestTableModel.setRowCount(0);

        if (currentEmployee == null) {
            error("No employee logged in");
            return;
        }

        ArrayList<RequestRestock> requests;
        try {
            // try common getter names
            requests = inventoryController.checkMyRequest(currentEmployee.getUserID());
        } catch (Exception ex) {
            try {
                requests = inventoryController.checkMyRequest(currentEmployee.getUserID());
            } catch (Exception ex2) {
                requests = null;
            }
        }

        if (requests == null || requests.isEmpty()) {
            info("No requests found");
            return;
        }

        for (RequestRestock rr : requests) {
            requestTableModel.addRow(new Object[] {
                    rr.getProductID().toString(),
                    rr.getQuantityToRestock(),
                    rr.getRequestStatus()
            });
        }

        requestTable.revalidate();
        requestTable.repaint();
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryView().setVisible(true));
    }
}
