package view;

import controller.ManagerController;
import exception.InvalidInputException;
import models.jobdesk.RequestRestock;
import models.products.Product;
import models.users.Employee;
import models.users.Role;
import repository.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

// versi UI tapi full AI --> tar diganti deh soalnya ga ngerti
public class ManagerViewSwing extends JFrame {
    private ManagerController managerController;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JPanel mainPanel;

    public ManagerViewSwing() {
        IEmployeeRepository employeeRepo = new EmployeeRepository();
        IPresensiRepository presensiRepo = new PresensiRepository();
        IProductRepository productRepo = new ProductRepository();
        IRequestRestockRepository restockRepo = new RequestRestockRepository();

        this.managerController = new ManagerController(employeeRepo, presensiRepo, productRepo, restockRepo);

        setTitle("Manager Dashboard - Supermarket System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadEmployeeData();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Status Panel
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("MANAGER DASHBOARD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Employee & Inventory Management System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(189, 195, 199));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 1, 5, 5));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Menu"));

        // Employee Management
        JButton btnHire = createStyledButton("➕ Hire Employee", new Color(46, 204, 113));
        btnHire.addActionListener(e -> showHireDialog());

        JButton btnFire = createStyledButton("❌ Fire Employee", new Color(231, 76, 60));
        btnFire.addActionListener(e -> showFireDialog());

        JButton btnChangeRole = createStyledButton("🔄 Change Role", new Color(52, 152, 219));
        btnChangeRole.addActionListener(e -> showChangeRoleDialog());

        JButton btnCalculateSalary = createStyledButton("💰 Calculate Salary", new Color(241, 196, 15));
        btnCalculateSalary.addActionListener(e -> showCalculateSalaryDialog());

        // Inventory Management
        JButton btnAssignRestock = createStyledButton("📦 Assign Restock", new Color(155, 89, 182));
        btnAssignRestock.addActionListener(e -> showAssignRestockDialog());

        JButton btnViewRestock = createStyledButton("📋 View Restock", new Color(52, 73, 94));
        btnViewRestock.addActionListener(e -> showRestockRequests());

        // Monitoring
        JButton btnMonitorUang = createStyledButton("💵 Total Asset", new Color(26, 188, 156));
        btnMonitorUang.addActionListener(e -> showTotalUang());

        JButton btnMonitorBarang = createStyledButton("📊 Total Stock", new Color(22, 160, 133));
        btnMonitorBarang.addActionListener(e -> showTotalBarang());

        // Utilities
        JButton btnRefresh = createStyledButton("🔄 Refresh Data", new Color(127, 140, 141));
        btnRefresh.addActionListener(e -> {
            loadEmployeeData();
            setStatus("Data refreshed successfully", false);
        });

        JButton btnExit = createStyledButton("🚪 Exit", new Color(192, 57, 43));
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });

        panel.add(btnHire);
        panel.add(btnFire);
        panel.add(btnChangeRole);
        panel.add(btnCalculateSalary);
        panel.add(btnAssignRestock);
        panel.add(btnViewRestock);
        panel.add(btnMonitorUang);
        panel.add(btnMonitorBarang);
        panel.add(btnRefresh);
        panel.add(btnExit);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Employee List"));

        String[] columns = {"NIK", "Name", "Role", "Salary", "Working Hours", "Hire Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(statusLabel, BorderLayout.WEST);

        return panel;
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : new Color(46, 204, 113));
    }

    private void loadEmployeeData() {
        try {
            tableModel.setRowCount(0);
            ArrayList<Employee> employees = managerController.getAllEmployees();

            for (Employee emp : employees) {
                Object[] row = {
                        emp.getNik(),
                        emp.getName(),
                        emp.getRole(),
                        "Rp " + String.format("%,d", emp.getSalary()),
                        emp.getWorkingHours() + " hrs",
                        emp.getHireDate()
                };
                tableModel.addRow(row);
            }

            setStatus(employees.size() + " employees loaded", false);
        } catch (Exception e) {
            setStatus("Error loading data: " + e.getMessage(), true);
        }
    }

    // Dialog methods
    private void showHireDialog() {
        JDialog dialog = new JDialog(this, "Hire New Employee", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtName = new JTextField();
        JComboBox<Role> cmbRole = new JComboBox<>(Role.values());
        JTextField txtSalary = new JTextField();
        JTextField txtWorkingHours = new JTextField();
        JTextField txtNIK = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Role:"));
        panel.add(cmbRole);
        panel.add(new JLabel("Salary (Rp):"));
        panel.add(txtSalary);
        panel.add(new JLabel("Working Hours:"));
        panel.add(txtWorkingHours);
        panel.add(new JLabel("NIK (16 digits):"));
        panel.add(txtNIK);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                Role role = (Role) cmbRole.getSelectedItem();
                int salary = Integer.parseInt(txtSalary.getText().trim());
                int workingHours = Integer.parseInt(txtWorkingHours.getText().trim());
                String nik = txtNIK.getText().trim();

                managerController.hireEmployee(name, role, salary, workingHours, nik);

                JOptionPane.showMessageDialog(dialog, "Employee hired successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadEmployeeData();
                dialog.dispose();
                setStatus("New employee added: " + name, false);

            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Salary and Working Hours must be numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showFireDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nik = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to fire:\n" + name + " (NIK: " + nik + ")?",
                "Confirm Fire",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                managerController.fireEmployee(nik);
                JOptionPane.showMessageDialog(this, "Employee fired successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadEmployeeData();
                setStatus("Employee removed: " + name, false);
            } catch (InvalidInputException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                setStatus("Error: " + e.getMessage(), true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                setStatus("Error: " + e.getMessage(), true);
            }
        }
    }

    private void showChangeRoleDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nik = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String currentRole = tableModel.getValueAt(selectedRow, 2).toString();

        Role newRole = (Role) JOptionPane.showInputDialog(this,
                "Employee: " + name + "\nCurrent Role: " + currentRole + "\n\nSelect new role:",
                "Change Role",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Role.values(),
                Role.CASHIER);

        if (newRole != null) {
            try {
                // Get employee by NIK to get UUID
                ArrayList<Employee> employees = managerController.getAllEmployees();
                UUID employeeID = null;
                for (Employee e : employees) {
                    if (e.getNik().equals(nik)) {
                        employeeID = e.getUserID();
                        break;
                    }
                }

                if (employeeID != null) {
                    managerController.changeRole(employeeID, newRole);
                    JOptionPane.showMessageDialog(this, "Role changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployeeData();
                    setStatus("Role changed for: " + name, false);
                }
            } catch (InvalidInputException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                setStatus("Error: " + e.getMessage(), true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                setStatus("Error: " + e.getMessage(), true);
            }
        }
    }

    private void showCalculateSalaryDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nik = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String salaryStr = tableModel.getValueAt(selectedRow, 3).toString();

        try {
            ArrayList<Employee> employees = managerController.getAllEmployees();
            UUID employeeID = null;
            for (Employee e : employees) {
                if (e.getNik().equals(nik)) {
                    employeeID = e.getUserID();
                    break;
                }
            }

            if (employeeID != null) {
                double calculatedSalary = managerController.calculateSalary(employeeID);

                String message = String.format(
                        "=== SALARY CALCULATION ===\n\n" +
                                "Employee: %s\n" +
                                "NIK: %s\n" +
                                "Base Salary: %s\n" +
                                "Calculated Salary: Rp %,.2f\n\n" +
                                "(Based on attendance records)",
                        name, nik, salaryStr, calculatedSalary
                );

                JOptionPane.showMessageDialog(this, message, "Salary Calculation", JOptionPane.INFORMATION_MESSAGE);
                setStatus("Salary calculated for: " + name, false);
            }
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Error: " + e.getMessage(), true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Error: " + e.getMessage(), true);
        }
    }

    private void showAssignRestockDialog() {
        JDialog dialog = new JDialog(this, "Assign Restock", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Get stockers
        ArrayList<Employee> allEmployees = managerController.getAllEmployees();
        ArrayList<Employee> stockers = new ArrayList<>();
        for (Employee e : allEmployees) {
            if (e.getRole() == Role.STOCKER) {
                stockers.add(e);
            }
        }

        if (stockers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stockers available!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> cmbStocker = new JComboBox<>();
        for (Employee s : stockers) {
            cmbStocker.addItem(s.getNik() + " - " + s.getName());
        }

        JTextField txtProductID = new JTextField();
        JTextField txtQuantity = new JTextField();

        panel.add(new JLabel("Stocker:"));
        panel.add(cmbStocker);
        panel.add(new JLabel("Product ID:"));
        panel.add(txtProductID);
        panel.add(new JLabel("Quantity:"));
        panel.add(txtQuantity);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAssign = new JButton("Assign");
        JButton btnCancel = new JButton("Cancel");

        btnAssign.addActionListener(e -> {
            try {
                UUID managerID = getManagerID();
                if (managerID == null) {
                    JOptionPane.showMessageDialog(dialog, "Manager not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int selectedIndex = cmbStocker.getSelectedIndex();
                UUID stockerID = stockers.get(selectedIndex).getUserID();
                UUID productID = UUID.fromString(txtProductID.getText().trim());
                int quantity = Integer.parseInt(txtQuantity.getText().trim());

                managerController.assignRestock(managerID, stockerID, productID, quantity);

                JOptionPane.showMessageDialog(dialog, "Restock assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                setStatus("Restock task assigned", false);

            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input! UUID or Quantity format is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnAssign);
        buttonPanel.add(btnCancel);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showRestockRequests() {
        try {
            ArrayList<RequestRestock> requests = managerController.monitorAllRestock();

            String[] columns = {"Request ID", "Product ID", "Quantity", "Status", "Stocker ID"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            for (RequestRestock req : requests) {
                Object[] row = {
                        req.getRequestID().toString().substring(0, 8) + "...",
                        req.getProductID().toString().substring(0, 8) + "...",
                        req.getQuantityToRestock(),
                        req.getRequestStatus(),
                        req.getStockerID() != null ? req.getStockerID().toString().substring(0, 8) + "..." : "N/A"
                };
                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(700, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Restock Requests", JOptionPane.INFORMATION_MESSAGE);
            setStatus("Viewed restock requests", false);

        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            setStatus(e.getMessage(), false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Error: " + e.getMessage(), true);
        }
    }

    private void showTotalUang() {
        try {
            double totalUang = managerController.monitorTotalUang();

            String message = String.format(
                    "=== TOTAL ASSET VALUE ===\n\n" +
                            "Total Inventory Value:\n" +
                            "Rp %,.2f",
                    totalUang
            );

            JOptionPane.showMessageDialog(this, message, "Asset Monitoring", JOptionPane.INFORMATION_MESSAGE);
            setStatus("Total asset value: Rp " + String.format("%,.2f", totalUang), false);

        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Error: " + e.getMessage(), true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Error: " + e.getMessage(), true);
        }
    }

    private void showTotalBarang() {
        try {
            int totalBarang = managerController.monitorTotalBarang();

            String message = String.format(
                    "=== TOTAL STOCK ===\n\n" +
                            "Total Items in Inventory:\n" +
                            "%,d items",
                    totalBarang
            );

            JOptionPane.showMessageDialog(this, message, "Stock Monitoring", JOptionPane.INFORMATION_MESSAGE);
            setStatus("Total stock: " + totalBarang + " items", false);

        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Error: " + e.getMessage(), true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Error: " + e.getMessage(), true);
        }
    }

    private UUID getManagerID() {
        try {
            ArrayList<Employee> employees = managerController.getAllEmployees();
            for (Employee e : employees) {
                if (e.getRole() == Role.MANAGER) {
                    return e.getUserID();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ManagerViewSwing frame = new ManagerViewSwing();
            frame.setVisible(true);
        });
    }
}