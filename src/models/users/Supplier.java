package models.users;

public class Supplier extends User {
    private String company;

    public Supplier(String company, int userID, String name) {
        super(userID, name, Role.SUPPLIER);
        this.company = company;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.company;
    }
}
