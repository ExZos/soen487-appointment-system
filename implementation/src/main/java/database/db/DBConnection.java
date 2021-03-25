package database.db;

import org.json.simple.JSONObject;
import utilities.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // Read config file
    private static JSONObject jo;
    static {
        try {
            jo = ConfigReader.DB_CONFIG_FILE.getConfigFileAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Assign from config file
    private static final String jdbcDriver = (String) jo.get("jdbcDriver");
    private static final String dbHost = (String) jo.get("dbHost");
    private static final String dbName = (String) jo.get("dbName");
    private static final String dbUser =  (String) jo.get("dbUser");
    private static final String dbPassword = (String) jo.get("dbPassword");

    private static Connection conn = null;

    // Setup and return connection
    public static Connection getConnection() {
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(dbHost + dbName, dbUser, dbPassword);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection()  {
        try {
            if (conn != null)
                conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBConnection.getConnection();
    }
}
