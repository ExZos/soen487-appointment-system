package repository.pojos;

public class User {
    private int userId;
    private String email;
    private String token;

    public User() { }

    public User(int userId, String email, String token) {
        this.userId = userId;
        this.email = email;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return String.format("ID: %d, EMAIL: %s", userId, email);
    }
}
