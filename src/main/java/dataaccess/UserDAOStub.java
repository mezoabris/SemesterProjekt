package dataaccess;

import models.User;

import java.sql.Connection;
import java.util.*;

public class UserDAOStub extends UserDAO{
    private final List<User> users = new ArrayList<>();
    public UserDAOStub(){
        User testUser1 = new User("test1", "pass1");
        testUser1.setUserID(users.size()+1);
        User testUser2 = new User("test2", "pass2");
        testUser2.setUserID(users.size()+1);
        User testUser3 = new User("test3", "pass3");
        testUser3.setUserID(users.size()+1);
        users.add(testUser1);
        users.add(testUser2);
        users.add(testUser3);
    }
    @Override
    public User create(Connection conn,User newUser){
        newUser.setUserID(users.size()+1);
        users.add(newUser);
        return newUser;
    }
    @Override
    public User findByUsername(Connection conn, String username){
        for(User user: users){
            if(Objects.equals(user.getUsername(), username)){
                return user;
            }
        }
        return null;
    }
    @Override
    public User findByUserID(Connection conn, int userID){
        for(User user: users){
            if(Objects.equals(user.getUserID(), userID)){
                return user;
            }
        }
        return null;
    }
    @Override
    public List<User> getUsers(Connection con){
        return users;
    }
    @Override
    public void updateToken(Connection conn, String username, String token) {
        User user = findByUsername(conn, username);
        if (user != null) {
            user.setToken(token);
        }
    }
    @Override
    public User editUser(Connection conn, User original, String field, String value) {
        switch (field) {
            case "username" -> original.setUsername(value);
            case "favoritegenre" -> original.setFavoriteGenre(value);
        }
        return original;
    }


}
