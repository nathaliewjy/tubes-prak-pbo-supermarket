package models.users;

import java.util.UUID;

public class Member extends User {
    private String phone;
    private int point;

    public Member(String phone, int point, UUID userID, String name) {
        super(userID, name, Role.MEMBER);
        this.phone = phone;
        this.point = point;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPoint() {
        return this.point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.phone + " " + this.point;
    }
}
