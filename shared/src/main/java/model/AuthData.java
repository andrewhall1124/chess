package model;

public class AuthData {
    private String authToken;
    private String username;

    public AuthData(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }
}
