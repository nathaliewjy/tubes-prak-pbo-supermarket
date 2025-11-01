package models.users;

public abstract class User {
    private String userID;
    private String name;
    private Role role;

    public User(String userID, String name, Role role) {
        this.userID = userID;
        this.name = name;
        this.role = role;
    }
}
