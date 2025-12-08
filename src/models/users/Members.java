package models.users;

import java.sql.Date;
import java.util.UUID;

public class Members extends User {
    private String phone;
    private int point;

    public Members(String phone, String name, Date deletedAt) {
        super(name, Role.MEMBER, deletedAt);
        this.phone = phone;
        this.point = 0;
    }

    public Members(UUID userID, String name, Date deletedAt, String phone, int point) {
        super(userID, name, Role.MEMBER, deletedAt);
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
