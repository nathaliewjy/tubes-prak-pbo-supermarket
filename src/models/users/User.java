package models.users;

import java.sql.Date;
import java.util.UUID;

public abstract class User {
    private UUID userID;
    private String name;
    private Role role;
    private Date deletedAt;

    public User(String name, Role role, Date deletedAt) {
        this.userID = UUID.randomUUID();
        this.name = name;
        this.role = role;
        this.deletedAt = deletedAt;
    }

    public UUID getUserID() {
        return this.userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return this.userID + " " + this.name + " " + this.role + " " + this.deletedAt;
    }
}
