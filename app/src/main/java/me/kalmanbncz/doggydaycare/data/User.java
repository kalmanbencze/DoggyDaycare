package me.kalmanbncz.doggydaycare.data;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class User {

    private final String username;

    private int id;

    private String token;

    public User(int id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return id >= 0 && username != null && !username.isEmpty() && token != null && !token.isEmpty();
    }
}
