package models;

import java.sql.Timestamp;

public class User {
    private String username;
    private String password;
    private String email;
    private String vorname;
    private String nachname;
    private Timestamp createdAt;
    public User(){};

    public User(String vorname,String nachname, String username, String email ) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.email = email;

    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public String getPassword() {
        return this.password;
    }
    public String getVorname() {
        return this.vorname;
    }
    public String getNachname() {
        return this.nachname;
    }
    public String getEmail() {
        return this.email;
    }
    public String getUserName() {
        return this.username;
    }
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }



}
