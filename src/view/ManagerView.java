//package view;
//
//import controller.ManagerController;
//import models.jobdesk.RequestRestock;
//import models.users.Employee;
//import models.users.Role;
//import exception.InvalidInputException;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.UUID;
//
//public class ManagerView extends JFrame {
//
//    private ManagerController managerController;
//    private JTable tableEmployee, tableRestock; // table tu buat visualnya
//    private DefaultTableModel modelEmployee, modelRestock; // default table model buat data"nya kyk CRUD kali
//    private JLabel lblTotalUang, lblTotalBarang;
//
//    public ManagerView(ManagerController managerController) {
//        this.managerController = managerController;
//
//        setTitle("Manager Dashboard");
//        setSize(900, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // biar programnya stop pas framenya di close
//        setLocationRelativeTo(null); // biar di tengah
//
//        // ini bikin 3 tab
//        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.addTab("Employee", initEmployeePanel());
//        tabbedPane.addTab("Restock", initRestockPanel());
//        tabbedPane.addTab("Assets", initAssetPanel());
//        add(tabbedPane);
//    }
//
//    private JPanel initEmployeePanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//
//        // Tabel
//        String[] kolom = {"NIK", "Name", "Role", "Salary", "Working Hours"};
//        modelEmployee = new DefaultTableModel(kolom, 0); // buat struktur data buat nyimpen datanya, kolom teh yg di atas itu yg array, 0 teh jumlah row awalnya
//        tableEmployee = new JTable(modelEmployee);
//        panel.add(new JScrollPane(tableEmployee), BorderLayout.CENTER);
//
//        // Tombol
//        JPanel btnPanel = new JPanel();
//        JButton btnRefresh = new JButton("Refresh");
//        JButton btnHire = new JButton("Hire Emp");
//        JButton btnFire = new JButton("Fire Emp");
//        JButton btnSalary = new JButton("Calculate Salary");
//        JButton btnRole = new JButton("Change Role");
//
//        btnPanel.add(btnRefresh);
//        btnPanel.add(btnHire);
//        btnPanel.add(btnFire);
//        btnPanel.add(btnSalary);
//        btnPanel.add(btnRole);
//        panel.add(btnPanel, BorderLayout.SOUTH); // south biar di abwah
//
//        btnRefresh.addActionListener(e -> refreshEmployeeTable());
//        btnHire.addActionListener(e -> showHireDialog());
//        btnFire.addActionListener(e -> showFireDialog());
//
//        btnSalary.addActionListener(e -> {
//            int row = tableEmployee.getSelectedRow();
//            if (row == -1) {
//                JOptionPane.showMessageDialog(this, "Pilih karyawan dulu"); // this teh objek ManagerView skrg which is tampilan JFrame nya, biar JOptionPane nya muncul di layer atas
//                return;
//            }
//            try {
//                String nik = (String) modelEmployee.getValueAt(row, 0);
//                double gaji = managerController.calculateSalary(nik);
//                JOptionPane.showMessageDialog(this, "Total salary : Rp " + gaji);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
//
//        btnRole.addActionListener(e -> {
//            int row = tableEmployee.getSelectedRow();
//            if (row == -1) {
//                JOptionPane.showMessageDialog(this, "Pilih karyawan dulu");
//                return;
//            }
//            showChangeRoleDialog(row);
//        });
//
//        refreshEmployeeTable(); // manggil helper biar tabelnya keisi pas dibuka
//        return panel;
//    }
//
//    private JPanel initRestockPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//
//        String[] kolom = {"RequestID", "ProductID", "StockerID", "Quantity", "Status"};
//        modelRestock = new DefaultTableModel(kolom, 0);
//        tableRestock = new JTable(modelRestock);
//        panel.add(new JScrollPane(tableRestock), BorderLayout.CENTER);
//
//        JPanel btnPanel = new JPanel();
//        JButton btnRefresh = new JButton("Refresh");
//        JButton btnAssign = new JButton("Assign Jobdesk");
//
//        btnPanel.add(btnRefresh);
//        btnPanel.add(btnAssign);
//        panel.add(btnPanel, BorderLayout.SOUTH);
//
//        btnRefresh.addActionListener(e -> refreshRestockTable());
//        btnAssign.addActionListener(e -> showRestockDialog());
//
//        refreshRestockTable(); // manggil helper biar tabelnya keisi pas dibuka
//        return panel;
//    }
//
//    private JPanel initAssetPanel() {
//        JPanel panel = new JPanel(new GridLayout(4, 1));
//
//        lblTotalBarang = new JLabel("Total Barang : ", SwingConstants.CENTER);
//        lblTotalBarang.setFont(new Font("Arial", Font.BOLD, 20));
//
//        lblTotalUang = new JLabel("Total Uang : Rp ", SwingConstants.CENTER);
//        lblTotalUang.setFont(new Font("Arial", Font.BOLD, 20));
//
//        JButton btnCek = new JButton("Monitor Asset");
//
//        panel.add(new JLabel(""));
//        panel.add(lblTotalBarang);
//        panel.add(lblTotalUang);
//        panel.add(btnCek);
//
//        btnCek.addActionListener(e -> {
//            try {
//                double uang = managerController.monitorTotalUang();
//                int barang = managerController.monitorTotalBarang();
//                lblTotalUang.setText(String.format("Total Uang: Rp %,.0f", uang));
//                lblTotalBarang.setText("Total Barang: " + barang + " items");
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        });
//        return panel;
//    }
//
//    private void refreshEmployeeTable() {
//        modelEmployee.setRowCount(0);
//        try {
//            ArrayList<Employee> empList = managerController.getAllEmployees();
//            for (Employee e : empList) {
//                modelEmployee.addRow(new Object[]{
//                        e.getNik(), e.getName(), e.getRole(), e.getSalary(), e.getWorkingHours()
//                });
//            }
//        } catch (Exception e) {
//            System.out.println("Gagal refresh employee");
//        }
//    }
//
//    private void refreshRestockTable() {
//        modelRestock.setRowCount(0);
//        try {
//            ArrayList<RequestRestock> reqList = managerController.monitorAllRestock();
//            for (RequestRestock r : reqList) {
//                modelRestock.addRow(new Object[]{
//                        r.getRequestID(), r.getProductID(), r.getStockerID(), r.getQuantityToRestock(), r.getRequestStatus()
//                });
//            }
//        } catch (Exception e) { System.out.println("Gagal load restock"); }
//    }
//
//    private void showHireDialog() {
//        JDialog dialog = new JDialog(this, "Form Hire : ", true); // this nya kyk tadi, true tuh artinya selama dialog ini kebuka, usernya gbs click" yg lain sblm kita close hire employee
//        dialog.setSize(400, 300);
//        dialog.setLocationRelativeTo(this);
//        dialog.setLayout(new GridLayout(6, 2, 10, 10));
//
//        JTextField txtName = new JTextField();
//        JTextField txtNik = new JTextField();
//        JTextField txtSalary = new JTextField();
//        JTextField txtHours = new JTextField();
//        JComboBox<Role> comboRole = new JComboBox<>(new Role[]{Role.CASHIER, Role.STOCKER, Role.MANAGER}); // buat dropdown
//        JButton btnSave = new JButton("Save");
//
//        dialog.add(new JLabel("  Name :"));        dialog.add(txtName);
//        dialog.add(new JLabel("  NIK :")); dialog.add(txtNik);
//        dialog.add(new JLabel("  Role:"));        dialog.add(comboRole);
//        dialog.add(new JLabel("  Salary :"));        dialog.add(txtSalary);
//        dialog.add(new JLabel("  Working hours:"));   dialog.add(txtHours);
//        dialog.add(new JLabel(""));               dialog.add(btnSave);
//
//        btnSave.addActionListener(e -> {
//            try {
//                int confirm = JOptionPane.showConfirmDialog(dialog, "Are u sure to hire this emp?");
//                if (confirm == JOptionPane.YES_OPTION) {
//                    managerController.hireEmployee(
//                            txtName.getText(),
//                            (Role) comboRole.getSelectedItem(),
//                            Integer.parseInt(txtSalary.getText()),
//                            Integer.parseInt(txtHours.getText()),
//                            txtNik.getText()
//                    );
//                    JOptionPane.showMessageDialog(dialog, "Emp hired!");
//                    refreshEmployeeTable();
//                    dialog.dispose(); // close dialog
//                }
//            } catch (InvalidInputException ex) {
//                JOptionPane.showMessageDialog(dialog, "Input Validation Error: " + ex.getMessage());
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(dialog, "Input Error: " + ex.getMessage());
//            }
//        });
//
//        dialog.setVisible(true);
//    }
//
//    private void showFireDialog() {
//        JDialog dialog = new JDialog(this, "Form Fire : ", true);
//        dialog.setSize(300, 150);
//        dialog.setLocationRelativeTo(this);
//        dialog.setLayout(new GridLayout(3, 1, 10, 10));
//
//        JTextField txtNik = new JTextField();
//        JButton btnFire = new JButton("FIRE!");
//        btnFire.setBackground(Color.RED);
//        btnFire.setForeground(Color.WHITE);
//
//        dialog.add(new JLabel("NIK Emp :", SwingConstants.CENTER));
//        dialog.add(txtNik);
//        dialog.add(btnFire);
//
//        btnFire.addActionListener(e -> {
//            try {
//                int confirm = JOptionPane.showConfirmDialog(dialog, "Are u sure to fire this emp?");
//                if (confirm == JOptionPane.YES_OPTION) {
//                    managerController.fireEmployee(txtNik.getText());
//                    JOptionPane.showMessageDialog(dialog, "Emp fired");
//                    refreshEmployeeTable();
//                    dialog.dispose();
//                }
//            } catch (InvalidInputException ex) {
//                JOptionPane.showMessageDialog(dialog, "Input Validation Error: " + ex.getMessage());
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
//            }
//        });
//
//        dialog.setVisible(true);
//    }
//
//    private void showChangeRoleDialog(int selectedRow) {
//        JDialog dialog = new JDialog(this, "Change Role", true);
//        dialog.setSize(300, 150);
//        dialog.setLocationRelativeTo(this);
//        dialog.setLayout(new GridLayout(3, 1));
//
//        String nik = (String) modelEmployee.getValueAt(selectedRow, 0);
//        String name = (String) modelEmployee.getValueAt(selectedRow, 1);
//
//        JComboBox<Role> comboNewRole = new JComboBox<>(Role.values());
//        JButton btnSave = new JButton("Update Role");
//
//        dialog.add(new JLabel("New role for " + name + ":", SwingConstants.CENTER));
//        dialog.add(comboNewRole);
//        dialog.add(btnSave);
//
//        btnSave.addActionListener(e -> {
//            try {
//                managerController.changeRole(nik, (Role) comboNewRole.getSelectedItem());
//                JOptionPane.showMessageDialog(dialog, "Role changed!");
//                refreshEmployeeTable();
//                dialog.dispose();
//            } catch (InvalidInputException ex) {
//                JOptionPane.showMessageDialog(dialog, "Role Change Error: " + ex.getMessage());
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
//            }
//        });
//
//        dialog.setVisible(true);
//    }
//
//    private void showRestockDialog() {
//        JDialog dialog = new JDialog(this, "Request Restock", true);
//        dialog.setSize(400, 250);
//        dialog.setLocationRelativeTo(this);
//        dialog.setLayout(new GridLayout(5, 2, 10, 10));
//
//        JTextField txtMgr = new JTextField();
//        JTextField txtStk = new JTextField();
//        JTextField txtProd = new JTextField();
//        JTextField txtQty = new JTextField();
//        JButton btnAssign = new JButton("ASSIGN");
//
//        dialog.add(new JLabel("Manager NIK:")); dialog.add(txtMgr);
//        dialog.add(new JLabel("Stocker NIK:")); dialog.add(txtStk);
//        dialog.add(new JLabel("Product SKU:")); dialog.add(txtProd);
//        dialog.add(new JLabel("Quantity:"));   dialog.add(txtQty);
//        dialog.add(new JLabel(""));              dialog.add(btnAssign);
//
//        btnAssign.addActionListener(e -> {
//            try {
//                managerController.assignRestock(
//                        txtMgr.getText(),
//                        txtStk.getText(),
//                        txtProd.getText(),
//                        Integer.parseInt(txtQty.getText())
//                );
//                JOptionPane.showMessageDialog(dialog, "Jobdesk assigned!");
//                refreshRestockTable();
//                dialog.dispose();
//            } catch (InvalidInputException ex) {
//                JOptionPane.showMessageDialog(dialog, "Assignment Error: " + ex.getMessage());
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
//            }
//        });
//
//        dialog.setVisible(true);
//    }
//}

package view;

import controller.ManagerController;
import models.jobdesk.RequestRestock;
import models.users.Employee;
import models.users.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class ManagerView extends JFrame {

    private ManagerController controller;

    // Komponen buat ganti-ganti halaman (CardLayout)
    private CardLayout cardLayout;
    private JPanel mainPanel; // Panel utama penampung kartu

    // Komponen Dashboard
    private JTable tableEmployee, tableRestock;
    private DefaultTableModel modelEmployee, modelRestock;
    private JLabel lblTotalUang, lblTotalBarang;

    public ManagerView(ManagerController controller) {
        this.controller = controller;

        // 1. Setup Window
        setTitle("MANAGER");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2. Setup CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Kartu 1: Login
        mainPanel.add(initLoginPanel(), "login");

        // Kartu 2: Dashboard
        mainPanel.add(initDashboardTabs(), "dashboard");

        // 4. Tampilkan Kartu Login dulu
        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private JPanel initLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); // biar d tengah
        JPanel box = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("LOGIN MANAGER", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField txtNik = new JTextField();
        txtNik.setBorder(BorderFactory.createTitledBorder("NIK (6 Digit)"));

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

                JOptionPane.showMessageDialog(this, "Selamat Datang, " + user.getName() + "!");

                // refresh dulu biar pas kebuka datanya update
                refreshEmployeeTable();
                refreshRestockTable();

                // ganti panel ke dashboard
                cardLayout.show(mainPanel, "dashboard");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JTabbedPane initDashboardTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Disini kita panggil panel-panel yang udah kita buat sebelumnya
        tabbedPane.addTab("Karyawan (HR)", initEmployeePanel());
        tabbedPane.addTab("Restock (Ops)", initRestockPanel());
        tabbedPane.addTab("Laporan Aset", initAssetPanel());

        // Tambah tombol Logout di pojok kanan atas (Opsional tapi keren)
        // (Di Swing agak ribet nambah tombol di tab bar, jadi kita skip dulu biar simpel)

        return tabbedPane;
    }

    // ==========================================
    // PANEL-PANEL DASHBOARD (SAMA KAYA SEBELUMNYA)
    // ==========================================

    private JPanel initEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] kolom = {"NIK", "Nama", "Role", "Gaji", "Jam Kerja"};
        modelEmployee = new DefaultTableModel(kolom, 0);
        tableEmployee = new JTable(modelEmployee);
        panel.add(new JScrollPane(tableEmployee), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnHire = new JButton("Hire");
        JButton btnFire = new JButton("Fire");
        JButton btnGaji = new JButton("Cek Gaji");
        JButton btnRole = new JButton("Ganti Role");
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

        // --- ACTION LISTENERS ---

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin mau logout?");
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "login");
            }
        });

        btnRefresh.addActionListener(e -> refreshEmployeeTable());
        btnHire.addActionListener(e -> showHireDialog());
        btnFire.addActionListener(e -> showFireDialog());

        btnGaji.addActionListener(e -> {
            int row = tableEmployee.getSelectedRow();
            if (row != -1) {
                try {
                    String nik = (String) modelEmployee.getValueAt(row, 0);
                    double gaji = controller.calculateSalary(nik);
                    JOptionPane.showMessageDialog(this, "Gaji: Rp " + (long)gaji);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris karyawan di tabel dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRole.addActionListener(e -> {
            int row = tableEmployee.getSelectedRow();
            if (row != -1) {
                showChangeRoleDialog(row);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris karyawan di tabel dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel initRestockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] kolom = {"Req ID", "Produk ID", "Stocker ID", "Qty", "Status"};
        modelRestock = new DefaultTableModel(kolom, 0);
        tableRestock = new JTable(modelRestock);
        panel.add(new JScrollPane(tableRestock), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnAssign = new JButton("Assign Tugas");
        btnPanel.add(btnRefresh);
        btnPanel.add(btnAssign);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> refreshRestockTable());
        btnAssign.addActionListener(e -> showRestockDialog());
        return panel;
    }

    private JPanel initAssetPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        lblTotalBarang = new JLabel("Total Barang: -", SwingConstants.CENTER);
        lblTotalBarang.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotalUang = new JLabel("Total Pendapatan: Rp -", SwingConstants.CENTER);
        lblTotalUang.setFont(new Font("Arial", Font.BOLD, 20));
        JButton btnCek = new JButton("HITUNG LAPORAN");

        panel.add(new JLabel(""));
        panel.add(lblTotalBarang);
        panel.add(lblTotalUang);
        panel.add(btnCek);

        btnCek.addActionListener(e -> {
            try {
                double uang = controller.monitorTotalPendapatan();
                int barang = controller.monitorTotalBarang();
                lblTotalUang.setText(String.format("Total Pendapatan: Rp %,.0f", uang));
                lblTotalBarang.setText("Stok Barang Gudang: " + barang + " items");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        return panel;
    }

    // ==========================================
    // HELPER & DIALOGS (SAMA KAYA SEBELUMNYA)
    // ==========================================

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
        JButton btnSimpan = new JButton("SIMPAN");

        dialog.add(new JLabel(" Nama:")); dialog.add(txtName);
        dialog.add(new JLabel(" NIK:")); dialog.add(txtNik);
        dialog.add(new JLabel(" Role:")); dialog.add(comboRole);
        dialog.add(new JLabel(" Gaji:")); dialog.add(txtSalary);
        dialog.add(new JLabel(" Jam:")); dialog.add(txtHours);
        dialog.add(new JLabel("")); dialog.add(btnSimpan);

        btnSimpan.addActionListener(e -> {
            try {
                controller.hireEmployee(txtName.getText(), (Role)comboRole.getSelectedItem(), Integer.parseInt(txtSalary.getText()), Integer.parseInt(txtHours.getText()), txtNik.getText());
                JOptionPane.showMessageDialog(dialog, "Sukses!");
                refreshEmployeeTable();
                dialog.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, ex.getMessage()); }
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
        dialog.add(new JLabel("Masukkan NIK:", SwingConstants.CENTER));
        dialog.add(txtNik); dialog.add(btnFire);
        btnFire.addActionListener(e -> {
            try {
                controller.fireEmployee(txtNik.getText());
                JOptionPane.showMessageDialog(dialog, "Sukses!");
                refreshEmployeeTable();
                dialog.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, ex.getMessage()); }
        });
        dialog.setVisible(true);
    }

    private void showChangeRoleDialog(int row) {
        String nik = (String) modelEmployee.getValueAt(row, 0);
        JDialog dialog = new JDialog(this, "Ganti Role", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(3, 1));
        JComboBox<Role> combo = new JComboBox<>(Role.values());
        JButton btn = new JButton("Simpan");
        dialog.add(new JLabel("Pilih Role Baru:", SwingConstants.CENTER));
        dialog.add(combo); dialog.add(btn);
        btn.addActionListener(e -> {
            try {
                controller.changeRole(nik, (Role)combo.getSelectedItem());
                JOptionPane.showMessageDialog(dialog, "Sukses!");
                refreshEmployeeTable();
                dialog.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, ex.getMessage()); }
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
        dialog.add(new JLabel(" NIK Mgr:")); dialog.add(m);
        dialog.add(new JLabel(" NIK Stk:")); dialog.add(s);
        dialog.add(new JLabel(" SKU:")); dialog.add(p);
        dialog.add(new JLabel(" Qty:")); dialog.add(q);
        dialog.add(new JLabel("")); dialog.add(btn);
        btn.addActionListener(e -> {
            try {
                controller.assignRestock(m.getText(), s.getText(), p.getText(), Integer.parseInt(q.getText()));
                JOptionPane.showMessageDialog(dialog, "Sukses!");
                refreshRestockTable();
                dialog.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, ex.getMessage()); }
        });
        dialog.setVisible(true);
    }
}