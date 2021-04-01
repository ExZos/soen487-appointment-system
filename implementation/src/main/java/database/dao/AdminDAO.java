package database.dao;

import database.db.DBConnection;
import repository.pojos.Admin;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminDAO {
    public static Admin selectAdminById(int adminId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Admin WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, adminId);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            return null;

        return mapResultSetToAdmin(rs);
    }

    public static Admin selectAdminByUsername(String username) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Admin WHERE username = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            return null;

        return mapResultSetToAdmin(rs);
    }

    public static Integer updateAdmin(int adminId, String token, LocalDateTime tokenCreated) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE Admin SET token = ?, token_created = ? WHERE id = ?";
        Timestamp timestampTokenCreated = tokenCreated == null ? null : Timestamp.valueOf(tokenCreated);

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, token);
        stmt.setTimestamp(2, timestampTokenCreated);
        stmt.setInt(3, adminId);
        int row = stmt.executeUpdate();

        if(row <= 0)
            return null;

        return adminId;
    }

    private static Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        Timestamp timestampTokenCreated = rs.getTimestamp("token_created");

        admin.setAdminId(rs.getInt("id"));
        admin.setUsername(rs.getString("username"));
        admin.setPassword(rs.getString("password"));
        admin.setToken(rs.getString("token"));

        if(timestampTokenCreated != null)
            admin.setTokenCreated(timestampTokenCreated.toLocalDateTime());

        return admin;
    }
}
