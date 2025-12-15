import controller.ManagerController;
import repository.*;
import view.ManagerView;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.PosController;
import repository.EmployeeRepository;
import repository.IEmployeeRepository;
import repository.IMembersRepository;
import repository.IOrderRepository;
import repository.IProductRepository;
import repository.ITransactionRepository;
import repository.MembersRepository;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.TransactionRepository;
import view.PosView;

public class App {
    public static void main(String[] args) {

        int selectedApp = JOptionPane.showOptionDialog(
                null,
                "Pilih Aplikasi yang Ingin Dijalankan",
                "Menu Pilihan",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "POS", "Manager", "Stock" },
                "POS");
        if (selectedApp == 0) {
            SwingUtilities.invokeLater(() -> {
                IOrderRepository orderRepo = new OrderRepository();
                ITransactionRepository transRepo = new TransactionRepository();
                IEmployeeRepository empRepo = new EmployeeRepository();
                IMembersRepository memberRepo = new MembersRepository();
                IProductRepository prodRepo = new ProductRepository();

                PosView view = new PosView();

                PosController controller = new PosController(view, orderRepo, transRepo, empRepo, memberRepo, prodRepo);

                view.setController(controller);

                view.setVisible(true);
            });
        } else if (selectedApp == 1) {
            IEmployeeRepository empRepo = new EmployeeRepository();
            IPresensiRepository presRepo = new PresensiRepository();
            IProductRepository prodRepo = new ProductRepository();
            IRequestRestockRepository reqRepo = new RequestRestockRepository();
            ITransactionRepository transRepo = new TransactionRepository();

            ManagerController managerController = new ManagerController(empRepo, presRepo, prodRepo, reqRepo,
                    transRepo);

            SwingUtilities.invokeLater(() -> {
                ManagerView view = new ManagerView(managerController);
                view.setVisible(true);
            });
        } else if (selectedApp == 2) {
            // runStockApp();
        } else {
            JOptionPane.showMessageDialog(null, "Tidak ada aplikasi yang dipilih. Program akan keluar.");
            System.exit(0);
        }

        // kata jarvis
        // kenapa ga lgsg new ManagerView() aja, kenapa harus pake SwingUtilities ...
        // karena waktu program di run, ada 2 thread (jalur) : main thread (psvm) sama
        // event dispatch thread (khusus punyanya java swing, jd cmn ngurusin UI)
        // so si EDT itu gabole dirun di main thread karena bisa conflict
        // makanya pake SwingUtilities.invokeLater biar ManagerView ada di thread yg
        // aman
    }
}