package model;

public class AuthData {
    private String authToken;
    private String username;

    public AuthData(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername(){
        return username;
    }

    public String getAuthToken(){
        return authToken;
    }
}
