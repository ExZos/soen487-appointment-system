package impl;

import database.dao.UserDAO;
import repository.interfaces.IUserManager;
import repository.pojos.User;

import java.sql.SQLException;

public class UserManager implements IUserManager {
    public User getUserById(int userId) throws SQLException {
        return UserDAO.selectUserById(userId);
    }

    public User getUserByEmail(String email) throws SQLException {
        return UserDAO.selectUserByEmail(email);
    }

    public User createUser(String email, String token) throws SQLException {
        Integer id = UserDAO.insertUser(email, token);

        if(id == null)
            return null;

        return getUserById(id);
    }

    public String updateUserToken(String email, String token) throws SQLException {
        return UserDAO.updateUserToken(email, token);
    }

    public boolean authenticateUser(String email, String token) throws SQLException {
        User user = getUserByEmail(email);

        return user != null && token.equals(user.getToken());
    }

    public boolean removeUserToken(String email) throws SQLException {
        String test = UserDAO.updateUserToken(email, null);
        System.out.println(test);
        return  test == null;
    }
}
