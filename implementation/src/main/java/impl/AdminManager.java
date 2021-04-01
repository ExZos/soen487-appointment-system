package impl;

import database.dao.AdminDAO;
import repository.interfaces.IAdminManager;
import repository.pojos.Admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AdminManager implements IAdminManager {
    private static final int TOKEN_LENGTH = 20;

    public Admin getAdminById(int adminId) throws SQLException {
        return AdminDAO.selectAdminById(adminId);
    }

    public Admin getAdminByUsername(String username) throws SQLException {
        return AdminDAO.selectAdminByUsername(username);
    }

    public String login(String username, String password) throws SQLException, NoSuchAlgorithmException {
        Admin admin = getAdminByUsername(username);

        if(admin == null)
            return null;
        else if(!admin.getPassword().equals(encryptPassword(password)))
            return null;

        String token = generateToken();
        AdminDAO.updateAdmin(admin.getAdminId(), token, LocalDateTime.now());

        return token;
    }

    public boolean validateToken(String username, String token) throws SQLException {
        Admin admin = getAdminByUsername(username);

        if(admin == null)
            return false;
        else if(admin.getToken() == null || !admin.getToken().equals(token))
            return false;
        else if(admin.getTokenCreated().plusMinutes(30).isBefore(LocalDateTime.now()))
            return false;

        return true;
    }

    public boolean logout(String username) throws SQLException {
        Admin admin = getAdminByUsername(username);

        if(admin == null)
            return false;

        return AdminDAO.updateAdmin(admin.getAdminId(), null, null) != null;
    }

    private String generateToken() {
        String CHAR_LOWER = "zyxwvutsrqponmlkjihgfedcba";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "9876543210";
        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Add password bytes to digest
        md.update(password.getBytes());

        // Get the hash's bytes
        byte[] bytes = md.digest();

        // Convert to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));

        // Get complete hashed password in hex format
        return sb.toString();
    }
}
