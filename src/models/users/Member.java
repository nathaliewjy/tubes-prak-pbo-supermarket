package models.users;

public class Member extends Customer {
    private int point;

    public Member(int point, String userID, String name, Role role) {
        super(userID, name, Role.CUSTOMER);
        this.point = point;
    }
}
