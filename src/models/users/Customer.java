package models.users;

public class Customer extends User {

    public Customer(int userID, String name) {
        super(userID, name, Role.CUSTOMER);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
