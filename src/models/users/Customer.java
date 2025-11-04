package models.users;

public class Customer extends User {
    private String phone;

    public Customer( String phone, int userID, String name) {
        super(userID, name, Role.CUSTOMER);
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone =  phone;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.phone;
    }
}
