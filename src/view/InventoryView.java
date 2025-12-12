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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryView extends JFrame {


    private InventoryController inventoryController; // controllerr

    private Employee currentEmployee; // user yg login

    // login
    private JTextField nikField;

    // tabs & tables
    private JTabbedPane tabPane;

    private DefaultTableModel productTableModel;
    private JTable productTable;

    private DefaultTableModel requestTableModel;
    private JTable requestTable;

    // inputs
    private JTextField txtExpiredDays;
    private JComboBox<ProductCategory> cbCategory;

    private JTextField txtProductName;
    private JTextField txtShelf;
    private JTextField txtStorage;

    public InventoryView() {

        inventoryController = new InventoryController(
                new ProductRepository(),
                new RequestRestockRepository(),
                new EmployeeRepository());

        setTitle("Inventory Management");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLogin();
    }

    // LOGIN SCREEN
    private void showLogin() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Login");
        nikField = new JTextField(15);
        JButton btnLogin = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(lblTitle, gbc);

        gbc.gridy = 1;
        loginPanel.add(new JLabel("Enter NIK:"), gbc);

        gbc.gridy = 2;
        loginPanel.add(nikField, gbc);

        gbc.gridy = 3;
        loginPanel.add(btnLogin, gbc);

        getContentPane().removeAll();
        getContentPane().add(loginPanel);
        revalidate();
        repaint();

        btnLogin.addActionListener(e -> {
            String nik = nikField.getText();

            boolean check = inventoryController.Login(nik);

            if (check) { // cek nik
                currentEmployee = inventoryController.getCurrentEmployee();
                showMainUI();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid NIK");
            }
        });
    }

    // MAIN TABBED UI
    private void showMainUI() {

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JLabel info = new JLabel("Logged in as: " + currentEmployee.getName());

        JPanel top = new JPanel();
        top.add(info);
        add(top, BorderLayout.NORTH);

        tabPane = new JTabbedPane();

        tabPane.add("Products", productsTab());
        tabPane.add("Expired", expiredTab());
        tabPane.add("Category", categoryTab());
        tabPane.add("Update Stock", updateTab());
        tabPane.add("My Requests", requestTab());

        add(tabPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    // TAB: PRODUCTS
    private JPanel productsTab() {
        JPanel p = new JPanel(new BorderLayout());

        String[] cols = { "Name", "Category", "Shelf", "Storage", "Expiry" };
        productTableModel = new DefaultTableModel(cols, 0);
        productTable = new JTable(productTableModel);

        p.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnLoad = new JButton("Load All Products");
        bottom.add(btnLoad);

        btnLoad.addActionListener(e -> displayProducts(inventoryController.getAllProducts()));

        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    // TAB: EXPIRED
    private JPanel expiredTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel();

        txtExpiredDays = new JTextField(6);
        JButton btnSearch = new JButton("Search");

        form.add(new JLabel("Days:"));
        form.add(txtExpiredDays);
        form.add(btnSearch);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Name", "Category", "Shelf", "Storage", "Expiry" }, 0);
        JTable table = new JTable(model);

        btnSearch.addActionListener(e -> {
            try {
                int days = Integer.parseInt(txtExpiredDays.getText());
                ArrayList<Product> expired = inventoryController.getAllExpiredProducts(days);

                model.setRowCount(0);
                if (expired != null) {
                    for (Product x : expired) {
                        model.addRow(new Object[] {
                                x.getBrand(),
                                x.getCategory(),
                                x.getStockInShelf(),
                                x.getStockInStorage(),
                                x.getExpiryDate()
                        });
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Days must be number");
            }
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        return p;
    }

    // TAB: CATEGORY
    private JPanel categoryTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel();

        cbCategory = new JComboBox<>(ProductCategory.values());
        JButton btnSearch = new JButton("Search");

        form.add(new JLabel("Category:"));
        form.add(cbCategory);
        form.add(btnSearch);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Name", "Category", "Shelf", "Storage", "Expiry" }, 0);
        JTable table = new JTable(model);

        btnSearch.addActionListener(e -> {
            ProductCategory cat = (ProductCategory) cbCategory.getSelectedItem();
            ArrayList<Product> list = inventoryController.getProductsByCategory(cat);

            model.setRowCount(0);
            if (list != null) {
                for (Product x : list) {
                    model.addRow(new Object[] {
                            x.getBrand(),
                            x.getCategory(),
                            x.getStockInShelf(),
                            x.getStockInStorage(),
                            x.getExpiryDate()
                    });
                }
            }
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // TAB: UPDATE STOCK
    private JPanel updateTab() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));

        txtProductName = new JTextField();
        txtShelf = new JTextField();
        txtStorage = new JTextField();

        JButton btnUpdate = new JButton("Update");

        p.add(new JLabel("Product Name:"));
        p.add(txtProductName);

        p.add(new JLabel("Shelf:"));
        p.add(txtShelf);

        p.add(new JLabel("Storage:"));
        p.add(txtStorage);

        p.add(new JLabel());
        p.add(btnUpdate);

        btnUpdate.addActionListener(e -> updateStock());

        return p;
    }

    // TAB: REQUEST RESTOCK
    private JPanel requestTab() {
        JPanel p = new JPanel(new BorderLayout());

        String[] cols = { "Product ID", "Qty", "Status", "Date" };
        requestTableModel = new DefaultTableModel(cols, 0);
        requestTable = new JTable(requestTableModel);

        JButton btn = new JButton("Load Requests");

        btn.addActionListener(e -> loadMyRequests());

        JPanel bottom = new JPanel();
        bottom.add(btn);

        p.add(new JScrollPane(requestTable), BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    // TABLE DISPLAY
    private void displayProducts(ArrayList<Product> products) {
        productTableModel.setRowCount(0);

        if (products != null) {
            for (Product p : products) {
                productTableModel.addRow(new Object[] {
                        p.getBrand(),
                        p.getCategory(),
                        p.getStockInShelf(),
                        p.getStockInStorage(),
                        p.getExpiryDate()
                });
            }
        }
    }

    private void loadMyRequests() {
        requestTableModel.setRowCount(0);

        ArrayList<RequestRestock> list = inventoryController.checkMyRequest(currentEmployee.getUserID());

        if (list != null) {
            for (RequestRestock r : list) {
                requestTableModel.addRow(new Object[] {
                        r.getProductID(),
                        r.getQuantityToRestock(),
                        r.getRequestStatus()
                });
            }
        }
    }

    private void updateStock() {
        try {
            String name = txtProductName.getText();
            int shelf = Integer.parseInt(txtShelf.getText());
            int storage = Integer.parseInt(txtStorage.getText());

            boolean check = inventoryController.updateStockByName(name, shelf, storage);

            if (check) {
                JOptionPane.showMessageDialog(this, "Updated");
            } else {
                JOptionPane.showMessageDialog(this, "Product not found");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid number");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryView().setVisible(true));
    }
}
