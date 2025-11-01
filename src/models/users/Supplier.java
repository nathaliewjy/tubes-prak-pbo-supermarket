package models.users;

public class Supplier extends User {
    private String company;

    public Supplier(String company, String userID, String name, Role role) {
        super(userID, name, Role.SUPPLIER);
        this.company = company;
    }
}
