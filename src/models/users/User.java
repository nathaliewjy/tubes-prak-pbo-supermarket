package models.users;

public abstract class User {
    private int userID;
    private String name;
    private Role role;

    public User(int userID, String name, Role role) {
        this.userID = userID;
        this.name = name;
        this.role = role;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
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
