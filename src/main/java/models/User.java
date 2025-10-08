package models;

import java.sql.Timestamp;

public class User {
    private String username;
    private String password;
    private String token;

    public User(){};

    public User(String username, String password ) {

        this.username = username;
        this.password = password;


    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public String getUserName() {
        return this.username;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }




}
