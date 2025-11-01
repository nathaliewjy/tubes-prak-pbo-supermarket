package models.users;

public class Customer extends User {

    public Customer(String userID, String name, Role role) {
        super(userID, name, Role.CUSTOMER);
    }
}
