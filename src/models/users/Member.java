package models.users;

public class Member extends Customer {
    private int point;

    public Member(int point, int userID, String name) {
        super(userID, name);
        this.point = point;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.point;
    }
}
