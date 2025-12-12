package view;

import controller.ManagerController;
import exception.InvalidInputException;
import models.jobdesk.RequestRestock;
import models.users.Employee;
import models.users.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ManagerView extends JFrame {

    private ManagerController controller;

    // CardLayout buat ganti-ganti halaman
    private CardLayout cardLayout;
    private JPanel mainPanel; // panel buat nampung login & dashboard

    private JTable tableEmployee, tableRestock; // table buat tampilannya
    private DefaultTableModel modelEmployee, modelRestock; // def tab model buat isinya
    private JLabel lblTotalUang, lblTotalBarang;

    public ManagerView(ManagerController controller) {
        this.controller = controller;

        setTitle("MANAGER");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // biar kalo di exit jd berenti run
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout); // ky tumpukan kartu

        mainPanel.add(initLoginPanel(), "login");
        mainPanel.add(initDashboardTabs(), "dashboard");
        add(mainPanel);
        cardLayout.show(mainPanel, "login"); // biar muncul duluan
    }

    private JPanel initLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); // biar d tengah
        JPanel box = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("LOGIN MANAGER", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField txtNik = new JTextField();
        txtNik.setBorder(BorderFactory.createTitledBorder("NIK (6 Digit) : "));

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));

        box.add(title);
        box.add(txtNik);
        box.add(new JLabel("")); // Spacer
        box.add(btnLogin);

        box.setPreferredSize(new Dimension(300, 250));
        panel.add(box);

        // Logic Login
        btnLogin.addActionListener(e -> {
            try {
                String nik = txtNik.getText();

                Employee user = controller.loginManager(nik);

                JOptionPane.showMessageDialog(this, "Welcome " + user.getName() + "!");

                // refresh dulu biar pas kebuka datanya update
                refreshEmployeeTable();
                refreshRestockTable();

                // ganti panel ke dashboard
                cardLayout.show(mainPanel, "dashboard");
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "System Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JTabbedPane initDashboardTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // bikin 3 tab di dashboard
        tabbedPane.addTab("Employee", initEmployeePanel());
        tabbedPane.addTab("Restock", initRestockPanel());
        tabbedPane.addTab("Assets", initAssetPanel());

        return tabbedPane;
    }

    private JPanel initEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] kolom = {"NIK", "Name", "Role", "Salary", "Working Hours"};
        modelEmployee = new DefaultTableModel(kolom, 0);
        tableEmployee = new JTable(modelEmployee);
        panel.add(new JScrollPane(tableEmployee), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnHire = new JButton("Hire");
        JButton btnFire = new JButton("Fire");
        JButton btnGaji = new JButton("Salary Check");
        JButton btnRole = new JButton("Change Role");
        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);

        btnPanel.add(btnRefresh);
        btnPanel.add(btnHire);
        btnPanel.add(btnFire);
        btnPanel.add(btnGaji);
        btnPanel.add(btnRole);
        btnPanel.add(btnLogout);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are u sure want to logout?");
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "login"); // udh logout trus munculin panel login
            }
        });

        btnRefresh.addActionListener(e -> refreshEmployeeTable());
        btnHire.addActionListener(e -> showHireDialog());
        btnFire.addActionListener(e -> showFireDialog());

        btnGaji.addActionListener(e -> {
            int row = tableEmployee.getSelectedRow(); // ngecek baris yg dipilih
            if (row != -1) {
                try {
                    String nik = (String) modelEmployee.getValueAt(row, 0);
                    double gaji = controller.calculateSalary(nik);
                    JOptionPane.showMessageDialog(this, "Salary : Rp " + (long)gaji);
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select employee first!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRole.addActionListener(e -> {
            int row = tableEmployee.getSelectedRow();
            if (row != -1) {
                showChangeRoleDialog(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select employee first!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel initRestockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] kolom = {"Req ID", "Product ID", "Stocker ID", "Quantity", "Status"};
        modelRestock = new DefaultTableModel(kolom, 0);
        tableRestock = new JTable(modelRestock);
        panel.add(new JScrollPane(tableRestock), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnAssign = new JButton("Assign Jobdesk");
        btnPanel.add(btnRefresh);
        btnPanel.add(btnAssign);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> refreshRestockTable());
        btnAssign.addActionListener(e -> showRestockDialog());
        return panel;
    }

    private JPanel initAssetPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        lblTotalBarang = new JLabel("Total products : -", SwingConstants.CENTER);
        lblTotalBarang.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotalUang = new JLabel("Total revenue : Rp -", SwingConstants.CENTER);
        lblTotalUang.setFont(new Font("Arial", Font.BOLD, 20));
        JButton btnCek = new JButton("CALCULATE");

        panel.add(new JLabel(""));
        panel.add(lblTotalBarang);
        panel.add(lblTotalUang);
        panel.add(btnCek);

        btnCek.addActionListener(e -> {
            try {
                double uang = controller.monitorTotalPendapatan();
                int barang = controller.monitorTotalBarang();
                lblTotalUang.setText(String.format("Total revenue : Rp %,.0f", uang));
                lblTotalBarang.setText("Product stock : " + barang + " items");
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    private void refreshEmployeeTable() {
        modelEmployee.setRowCount(0);
        try {
            ArrayList<Employee> list = controller.getAllEmployees();
            for (Employee e : list) {
                modelEmployee.addRow(new Object[]{e.getNik(), e.getName(), e.getRole(), (long)e.getSalary(), e.getWorkingHours()});
            }
        } catch (Exception e) {}
    }

    private void refreshRestockTable() {
        modelRestock.setRowCount(0);
        try {
            ArrayList<RequestRestock> list = controller.monitorAllRestock();
            for (RequestRestock r : list) {
                modelRestock.addRow(new Object[]{r.getRequestID(), r.getProductID(), r.getStockerID(), r.getQuantityToRestock(), r.getRequestStatus()});
            }
        } catch (Exception e) {}
    }

    private void showHireDialog() {
        JDialog dialog = new JDialog(this, "Form Hire", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField txtName = new JTextField();
        JTextField txtNik = new JTextField();
        JTextField txtSalary = new JTextField();
        JTextField txtHours = new JTextField();
        JComboBox<Role> comboRole = new JComboBox<>(new Role[]{Role.CASHIER, Role.STOCKER, Role.MANAGER});
        JButton btnSimpan = new JButton("HIRE");

        dialog.add(new JLabel(" Name:")); dialog.add(txtName);
        dialog.add(new JLabel(" NIK:")); dialog.add(txtNik);
        dialog.add(new JLabel(" Role:")); dialog.add(comboRole);
        dialog.add(new JLabel(" Salary:")); dialog.add(txtSalary);
        dialog.add(new JLabel(" Working hours:")); dialog.add(txtHours);
        dialog.add(new JLabel("")); dialog.add(btnSimpan);

        btnSimpan.addActionListener(e -> {
            try {
                controller.hireEmployee(txtName.getText(), (Role)comboRole.getSelectedItem(), Integer.parseInt(txtSalary.getText()), Integer.parseInt(txtHours.getText()), txtNik.getText());
                JOptionPane.showMessageDialog(dialog, "Hired successfully!");
                refreshEmployeeTable();
                dialog.dispose();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Wrong input", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    private void showFireDialog() {
        JDialog dialog = new JDialog(this, "Form Fire", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(3, 1));
        JTextField txtNik = new JTextField();
        JButton btnFire = new JButton("FIRE");
        btnFire.setBackground(Color.RED); btnFire.setForeground(Color.WHITE);
        dialog.add(new JLabel("NIK:", SwingConstants.CENTER));
        dialog.add(txtNik); dialog.add(btnFire);
        btnFire.addActionListener(e -> {
            try {
                controller.fireEmployee(txtNik.getText());
                JOptionPane.showMessageDialog(dialog, "Fired successfully!");
                refreshEmployeeTable();
                dialog.dispose();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    private void showChangeRoleDialog(int row) {
        String nik = (String) modelEmployee.getValueAt(row, 0);
        JDialog dialog = new JDialog(this, "Change Role", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(3, 1));
        JComboBox<Role> combo = new JComboBox<>(Role.values());
        JButton btn = new JButton("Change");
        dialog.add(new JLabel("Select new role :", SwingConstants.CENTER));
        dialog.add(combo); dialog.add(btn);
        btn.addActionListener(e -> {
            try {
                controller.changeRole(nik, (Role)combo.getSelectedItem());
                JOptionPane.showMessageDialog(dialog, "Role changed successfully!");
                refreshEmployeeTable();
                dialog.dispose();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    private void showRestockDialog() {
        JDialog dialog = new JDialog(this, "Assign Restock", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2));
        JTextField m = new JTextField(); JTextField s = new JTextField();
        JTextField p = new JTextField(); JTextField q = new JTextField();
        JButton btn = new JButton("ASSIGN");
        dialog.add(new JLabel(" NIK Manager : ")); dialog.add(m);
        dialog.add(new JLabel(" NIK Stocker : ")); dialog.add(s);
        dialog.add(new JLabel(" SKU : ")); dialog.add(p);
        dialog.add(new JLabel(" Quantity : ")); dialog.add(q);
        dialog.add(new JLabel("")); dialog.add(btn);
        btn.addActionListener(e -> {
            try {
                controller.assignRestock(m.getText(), s.getText(), p.getText(), Integer.parseInt(q.getText()));
                JOptionPane.showMessageDialog(dialog, "Restock assigned successfully!");
                refreshRestockTable();
                dialog.dispose();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }
}