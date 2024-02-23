package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;

import java.util.HashMap;
import java.util.Map;


public class UserService {

    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;

    public UserService(MemoryUserDAO userAccess, MemoryAuthDAO authAccess) {
        this.userDAO = userAccess;
        this.authDAO = authAccess;
    }

    public void clear(){
        userDAO.deleteUsers();
    }

    public String register(String username, String password, String email){
        if(userDAO.getUser(username) == null){
            userDAO.createUser(username,password,email);
            return authDAO.addToken(username);
        }
        return null;
    }

    public HashMap<String, String> login(String username, String password) {
        UserData credentials = userDAO.getUser(username);
        if (credentials != null && credentials.getPassword().equals(password)) {
            String authToken = authDAO.addToken(username);
            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("username", username);
            resultMap.put("authToken", authToken);
            return resultMap;
        }
        return null;
    }

    public void logout(String authToken){
        authDAO.deleteToken(authToken);
    }
}
