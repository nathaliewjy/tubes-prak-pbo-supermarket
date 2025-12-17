package view;

import controller.InventoryController;
import models.jobdesk.RequestRestock;
import models.products.Product;
import models.products.ProductCategory;
import models.users.Employee;
import repository.EmployeeRepository;
import repository.ProductRepository;
import repository.RequestRestockRepository;
import Exception.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryView extends JFrame {

    private InventoryController inventoryController;
    private Employee currentEmployee;

    // login
    private JTextField nikField;

    // tabs & tables
    private JTabbedPane tabPane;

    private DefaultTableModel productTableModel;
    private JTable productTable;

    private DefaultTableModel requestTableModel;
    private JTable requestTable;
    
    private DefaultTableModel emptyStockTableModel;
    private JTable emptyStockTable;

    // inputs
    private JTextField txtExpiredDays;
    private JComboBox<ProductCategory> cbCategory;

    private JTextField txtProductName;
    private JTextField txtStorage;
    
    // inputs for restock needed
    private JTextField txtProductNameRestock;
    private JTextField txtQuantityRestock;

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

            if (check) {
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
        tabPane.add("Update Storage Stock", updateStorageStock());
        tabPane.add("Restock Needed", restockNeededTab());
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

    // TAB: UPDATE Storage STOCK
    private JPanel updateStorageStock() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));

        txtProductName = new JTextField();
        txtStorage = new JTextField();

        JButton btnUpdate = new JButton("Update");

        p.add(new JLabel("Product Name:"));
        p.add(txtProductName);


        p.add(new JLabel("Storage:"));
        p.add(txtStorage);

        p.add(new JLabel());
        p.add(btnUpdate);

        btnUpdate.addActionListener(e -> updateStock());

        return p;
    }

    // TAB: RESTOCK NEEDED (NEW)
    private JPanel restockNeededTab() {
        JPanel p = new JPanel(new BorderLayout());

        String[] cols = { "Name", "Category", "Shelf", "Storage", "Expiry" };
        emptyStockTableModel = new DefaultTableModel(cols, 0);
        emptyStockTable = new JTable(emptyStockTableModel);

        p.add(new JScrollPane(emptyStockTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton btnLoad = new JButton("Load Empty Stock");
        JPanel loadPanel = new JPanel();
        loadPanel.add(btnLoad);

        btnLoad.addActionListener(e -> loadEmptyStock());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Update shelf Stock"));

        txtProductNameRestock = new JTextField();
        txtQuantityRestock = new JTextField();
        JButton btnUpdateRestock = new JButton("Update Stock");

        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(txtProductNameRestock);
        formPanel.add(new JLabel("Stock shelf to Add:"));
        formPanel.add(txtQuantityRestock);
        formPanel.add(new JLabel());
        formPanel.add(btnUpdateRestock);

        btnUpdateRestock.addActionListener(e -> updateStockShelf());

        bottomPanel.add(loadPanel, BorderLayout.NORTH);
        bottomPanel.add(formPanel, BorderLayout.CENTER);

        p.add(bottomPanel, BorderLayout.SOUTH);

        return p;
    }

    // TAB: REQUEST RESTOCK (MODIFIED)
    private JPanel requestTab() {
        JPanel p = new JPanel(new BorderLayout());

        String[] cols = { "Request ID", "Product ID", "Quantity Storage to Restock", "Status" };
        requestTableModel = new DefaultTableModel(cols, 0);
        requestTable = new JTable(requestTableModel);

        JPanel bottomPanel = new JPanel();
        JButton btnLoad = new JButton("Load Requests");
        JButton btnComplete = new JButton("Complete Selected Request");

        btnLoad.addActionListener(e -> loadMyRequests());
        btnComplete.addActionListener(e -> completeSelectedRequest());

        bottomPanel.add(btnLoad);
        bottomPanel.add(btnComplete);

        p.add(new JScrollPane(requestTable), BorderLayout.CENTER);
        p.add(bottomPanel, BorderLayout.SOUTH);

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

    private void loadEmptyStock() {
        emptyStockTableModel.setRowCount(0);

        ArrayList<Product> emptyList = inventoryController.checkEmptyStock();

        if (emptyList != null && !emptyList.isEmpty()) {
            for (Product p : emptyList) {
                emptyStockTableModel.addRow(new Object[] {
                        p.getBrand(),
                        p.getCategory(),
                        p.getStockInShelf(),
                        p.getStockInStorage(),
                        p.getExpiryDate()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No empty stock found");
        }
    }

    private void loadMyRequests() {
        requestTableModel.setRowCount(0);

        ArrayList<RequestRestock> list = inventoryController.checkMyRequest(currentEmployee.getUserID());

        if (list != null) {
            for (RequestRestock r : list) {
                String productName = inventoryController.getProductNameById(r.getProductID());
                requestTableModel.addRow(new Object[] {
                        r.getRequestID(),
                        productName,
                        r.getQuantityToRestock(),
                        r.getRequestStatus()
                });
            }
        }
    }

    // update storage stock
    private void updateStock() {
        try {
            String name = txtProductName.getText();
            int Storage = Integer.parseInt(txtStorage.getText());

            boolean check = inventoryController.updateStorageStock(name, Storage);

            if (check) {
                JOptionPane.showMessageDialog(this, "Updated");
            } else {
                JOptionPane.showMessageDialog(this, "Product not found");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid number");
        }
    }

    private void updateStockShelf() {
        try {
            String name = txtProductNameRestock.getText();
            int shelfQtyToAdd = Integer.parseInt(txtQuantityRestock.getText());

            boolean check = inventoryController.updateStockShelf(name, shelfQtyToAdd);

            if (check) {
                JOptionPane.showMessageDialog(this, "Stock updated successfully");
                loadEmptyStock();
                txtProductNameRestock.setText("");
                txtQuantityRestock.setText("");
            }

        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completeSelectedRequest() {
        int selectedRow = requestTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request first");
            return;
        }

        try {
            String requestID = requestTableModel.getValueAt(selectedRow, 0).toString();

            boolean check = inventoryController.completeRestockRequest(requestID);

            if (check) {
                JOptionPane.showMessageDialog(this, "Request completed successfully");
                loadMyRequests();
            }

        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}