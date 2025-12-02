package repository;

import models.jobdesk.RequestRestock;
import models.jobdesk.RequestStatus;
import util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class RequestRestockRepository implements IRequestRestockRepository {
    private RequestRestock resultSetRequestRestock (ResultSet rs) throws SQLException {
        UUID requestID = UUID.fromString(rs.getString("RequestID"));
        UUID productID = UUID.fromString(rs.getString("ProductID"));
        int quantity = rs.getInt("QuantityToRestock");
        RequestStatus requestStatus = RequestStatus.valueOf(rs.getString("RequestStatus"));
        UUID managerID = UUID.fromString(rs.getString("ManagerID"));
        UUID stockerID = UUID.fromString(rs.getString("StockerID"));

        RequestRestock r = new RequestRestock(requestID, productID, quantity, requestStatus, managerID, stockerID);

        return r;
    }

    @Override
    public void createRequest(RequestRestock req) {
        String sql = "INSERT INTO jobdesk (RequestID, ProductID, QuantityToRestock, RequestStatus, ManagerID, StockerID) VALUES (?,?,?,?,?,?)";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, req.getRequestID().toString());
            stmt.setString(2, req.getProductID().toString());
            stmt.setInt(3, req.getQuantityToRestock());
            stmt.setString(4, req.getRequestStatus().name());
            stmt.setString(5, req.getManagerID().toString());
            stmt.setString(6, req.getStockerID().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(UUID requestID, RequestStatus newStatus) {
        String sql = "UPDATE jobdesk SET RequestStatus = ? WHERE RequestID = ?";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus.name());
            stmt.setString(2, requestID.toString());
            stmt.executeUpdate();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public ArrayList<RequestRestock> getPendingRequest(UUID stockerID) {
        ArrayList<RequestRestock> reqList = new ArrayList<>();

        String sql = "SELECT * FROM jobdesk WHERE StockerID = ? AND RequestStatus != 'COMPLETED'";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, stockerID.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reqList.add(resultSetRequestRestock(rs));
            }
        } catch (SQLException e3) {
            e3.printStackTrace();
        }

        return reqList;
    }

    @Override
    public ArrayList<RequestRestock> getAllRequests() {
        ArrayList<RequestRestock> reqList = new ArrayList<>();

        String sql = "SELECT * FROM jobdesk";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reqList.add(resultSetRequestRestock(rs));
            }
        } catch (SQLException e4) {
            e4.printStackTrace();
        }

        return reqList;
    }

}
