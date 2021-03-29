package repository.pojos;

import java.time.LocalDateTime;

public class Admin {
    private Integer adminId;
    private String username;
    private String password;
    private String token;
    private LocalDateTime tokenCreated;

    public Admin() { }

    public Admin(Integer adminId, String username, String password, String token, LocalDateTime tokenCreated) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.token = token;
        this.tokenCreated = tokenCreated;
    }


    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenCreated() { return tokenCreated; }

    public void setTokenCreated(LocalDateTime tokenCreated) { this.tokenCreated = tokenCreated; }

    public String toString() {
        return String.format("ID: %d, USERNAME: %s", adminId, username);
    }
}
