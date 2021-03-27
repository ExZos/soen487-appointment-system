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

        return UserDAO.selectUserById(id);
    }

    public User updateUserToken(int userId, String token) throws SQLException {
        Integer id = UserDAO.updateUserToken(userId, token);

        if(id == null)
            return null;

        return UserDAO.selectUserById(id);
    }

    // QUICK TESTING
    // BTW on IntelliJ, "ctrl + /" is a comment/uncomment key combination
    // So try highlighting multiple lines and press "ctrl + /"
//    public static void main(String[] args) {
//        try {
//            UserManager userManager = new UserManager();
//
//            System.out.println(userManager.createUser("email@email.com", null));
//            System.out.println(userManager.getUserById(1));
//            System.out.println(userManager.updateUserToken(1, "FAKETOKEN"));
//            System.out.println(userManager.getUserByEmail("email@email.com"));
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
