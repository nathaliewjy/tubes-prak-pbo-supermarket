package models.users;

import java.util.UUID;

public abstract class User {
    private UUID userID;
    private String name;
    private Role role;

    public User(String name, Role role) {
        this.userID = UUID.randomUUID();
        this.name = name;
        this.role = role;
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

    @Override
    public String toString() {
        return this.userID + " " + this.name + " " + this.role;
    }
}
