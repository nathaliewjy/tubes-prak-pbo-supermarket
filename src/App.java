import controller.ManagerController;
import repository.*;
import view.ManagerView;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        IEmployeeRepository empRepo = new EmployeeRepository();
        IPresensiRepository presRepo = new PresensiRepository();
        IProductRepository prodRepo = new ProductRepository();
        IRequestRestockRepository reqRepo = new RequestRestockRepository();
        ITransactionRepository transRepo = new TransactionRepository();

        ManagerController managerController = new ManagerController(empRepo, presRepo, prodRepo, reqRepo, transRepo);

        SwingUtilities.invokeLater(() -> {
            ManagerView view = new ManagerView(managerController);
            view.setVisible(true);
        });

        // kata jarvis
        // kenapa ga lgsg new ManagerView() aja, kenapa harus pake SwingUtilities ...
        // karena waktu program di run, ada 2 thread (jalur) : main thread (psvm) sama event dispatch thread (khusus punyanya java swing, jd cmn ngurusin UI)
        // so si EDT itu gabole dirun di main thread karena bisa conflict
        // makanya pake SwingUtilities.invokeLater biar ManagerView ada di thread yg aman
    }
}