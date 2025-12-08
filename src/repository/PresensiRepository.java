package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import models.presensi.Presensi;
import models.presensi.StatusKehadiran;
import util.Database;

public class PresensiRepository implements IPresensiRepository {

    private Presensi resultSetPresensi(ResultSet rs) throws SQLException {
    
        Presensi presensi = new Presensi(
                rs.getDate("TanggalKehadiran"),
                StatusKehadiran.valueOf(rs.getString("StatusKehadiran")),
                rs.getString("NIK"),
                UUID.fromString(rs.getString("EmployeeID")));

        presensi.setPresensiID(UUID.fromString(rs.getString("PresensiID")));
        return presensi;
    }

    @Override
    public void addPresensi(Presensi presensi) {
        String sql = "INSERT INTO presensi (PresensiID, EmployeeID, TanggalKehadiran, StatusKehadiran) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, presensi.getPresensiID().toString());
            pstmt.setString(2, presensi.getEmpID().toString()); // Use getEmpID()
            pstmt.setDate(3, new java.sql.Date(presensi.getDate().getTime()));
            pstmt.setString(4, presensi.getStatus().name());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Presensi> getPresensiListByNIK(String nik) {
        ArrayList<Presensi> presensiList = new ArrayList<>();
        String sql = "SELECT p.* FROM presensi p " +
                "JOIN employee e ON p.EmployeeID = e.EmployeeID " +
                "WHERE e.NIK = ?";

        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nik);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    presensiList.add(resultSetPresensi(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return presensiList;
    }
}
