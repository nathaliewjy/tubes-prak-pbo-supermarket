package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import models.users.Members;
import util.Database;

public class MembersRepository implements IMembersRepository{
    
    public Members findByPhone(String phone){
        String sql = "SELECT u.UserID, u.Name, u.deletedAt, m.Phone, m.Points FROM users u INNER JOIN member m ON u.UserID = m.MemberID WHERE m.Phone = ? AND u.deletedAt IS NULL";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetMembers(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addMembers(Members m){
        String sqlUsers = "INSERT INTO users (UserID, Name, Role) VALUES (?, ?, ?)";
        String sqlMember = "INSERT INTO member (MemberID, Phone, Points) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmtUsers = conn.prepareStatement(sqlUsers);
             PreparedStatement stmtMember = conn.prepareStatement(sqlMember)) {

            stmtUsers.setString(1, m.getUserID().toString());
            stmtUsers.setString(2, m.getName());
            stmtUsers.setString(3, m.getRole().name());
            stmtUsers.executeUpdate();

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

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            stmt.executeUpdate();

        } catch (SQLException e3) {
            e3.printStackTrace();
        }
    }

    public ArrayList<Members> getAllMembers() {
        ArrayList<Members> membersList = new ArrayList<>();

        String sql = "SELECT u.UserID, u.Name, u.deletedAt, m.Phone, m.Points FROM users u INNER JOIN member m ON u.UserID = m.MemberID WHERE u.deletedAt IS NULL";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
        Date deletedAt = rs.getDate("deletedAt");
        String phone = rs.getString("Phone");
        int points = rs.getInt("Points");

        Members m = new Members(userID, name, deletedAt, phone, points);

        return m;
    }

    @Override
    public void updatePoints(UUID memberID, int addPoints) {
        String sql = "UPDATE member SET Points = Points + ? WHERE MemberID = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, addPoints);
            stmt.setString(2, memberID.toString());

            stmt.executeUpdate();

        } catch (SQLException e5) {
            e5.printStackTrace();
        }
    }
}
