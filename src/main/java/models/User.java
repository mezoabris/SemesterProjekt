package models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class User {
    @Getter
    @Setter
    private String username;
    @Getter  @Setter
    private String password;
    @Getter  @Setter
    private String token;
    @Getter @Setter
    private int userID;
    @Getter @Setter
    private String favoriteGenre;

    public User(){};

    public User(String username, String password ) {

        this.username = username;
        this.password = password;


    }







}
