package repository.interfaces;

import repository.pojos.User;

import java.sql.SQLException;

public interface IUserManager extends IManager {
    User getUserById(int userId) throws SQLException;
    User getUserByEmail(String email) throws SQLException;
    User createUser(String email, String token) throws SQLException;
    String updateUserToken(String email, String token) throws SQLException;
    boolean validateToken(String email, String token) throws SQLException;
    boolean logout(String email) throws SQLException;
}
