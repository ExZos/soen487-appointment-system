package database.dao;

import database.db.DBConnection;
import repository.pojos.User;

import java.sql.*;

public class UserDAO {
    public static User selectUserById(int userId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM User WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            return null;

        return mapResultSetToUser(rs);
    }

    public static User selectUserByEmail(String email) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM User WHERE email = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            return null;

        return mapResultSetToUser(rs);
    }

    public static Integer insertUser(String email, String token) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO User(email, token) values (?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, email);
        stmt.setString(2, token);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();

        if(!rs.next())
            return null;

        return rs.getInt(1);
    }

    public static Integer updateUserToken(int userId, String token) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE User SET token = ? WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, token);
        stmt.setInt(2, userId);
        int row = stmt.executeUpdate();

        if(row <= 0)
            return null;

        return userId;
    }

    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setToken(rs.getString("token"));

        return user;
    }
}
