//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

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
    }
}