package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import models.users.Members;
import util.Database;

public class MembersRepository implements IMembersRepository{
    
    public Members findByPhone(String phone){
        Members memberFound = null;

        String sql = "SELECT u.UserID, u.Name, m.Phone, m.Points FROM users u INNER JOIN member m ON u.UserID = m.MemberID WHERE m.Phone = ? AND u.deletedAt IS NULL";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, phone);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                memberFound = resultSetMembers(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return memberFound;
    }

    public void addMembers(Members m){
        String sqlUsers = "INSERT INTO users (UserID, Name, Role) VALUES (?, ?, ?)";
        String sqlMember = "INSERT INTO member (MemberID, Phone, Points) VALUES (?, ?, ?)";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmtUsers = conn.prepareStatement(sqlUsers);
            stmtUsers.setString(1, m.getUserID().toString());
            stmtUsers.setString(2, m.getName());
            stmtUsers.setString(3, m.getRole().name());
            stmtUsers.executeUpdate();

            PreparedStatement stmtMember = conn.prepareStatement(sqlMember);
            stmtMember.setString(1, m.getUserID().toString());
            stmtMember.setString(2, m.getPhone());
            stmtMember.setInt(3, m.getPoint());
            stmtMember.executeUpdate();

        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    public void deleteMembers(String phone){
        String sql = "UPDATE users u INNER JOIN member m ON u.UserID = m.MemberID SET u.deletedAt = NOW(), m.deletedAt = NOW() WHERE m.Phone = ?";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, phone);
            stmt.executeUpdate();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }
    }

    public ArrayList<Members> getAllMembers() {
        ArrayList<Members> membersList = new ArrayList<>();

        String sql = "SELECT u.UserID, u.Name, m.Phone, m.Points FROM users u INNER JOIN member m ON u.UserID = m.MemberID WHERE u.deletedAt IS NULL";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Members m = resultSetMembers(rs);
                membersList.add(m);
            }
        } catch (SQLException e4) {
            e4.printStackTrace();
        }

        return membersList;
    }

    private Members resultSetMembers(ResultSet rs) throws SQLException {
        UUID userID = UUID.fromString(rs.getString("UserID"));
        String name = rs.getString("Name");
        String phone = rs.getString("Phone");
        int points = rs.getInt("Points");

        Members m = new Members(phone, name);

        m.setUserID(userID);
        m.setPoint(points);

        return m;
    }
}
