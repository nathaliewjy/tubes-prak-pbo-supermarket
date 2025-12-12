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
    }
}