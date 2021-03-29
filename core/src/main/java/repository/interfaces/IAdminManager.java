package repository.interfaces;

import repository.pojos.Admin;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface IAdminManager extends IManager {
    Admin getAdminById(int adminId) throws SQLException;
    Admin getAdminByUsername(String username) throws SQLException;
    String login(String username, String password) throws SQLException, NoSuchAlgorithmException;
    boolean validateToken(String username, String token) throws SQLException;
    boolean logout(String username) throws SQLException;
}
