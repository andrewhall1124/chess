package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;

import java.util.HashMap;

public class UserService {

    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;

    public UserService(MemoryUserDAO userAccess, MemoryAuthDAO authAccess){
        this.userDAO = userAccess;
        this.authDAO = authAccess;
    }

    public void clear()throws DataAccessException{
        userDAO.clearUsers();
    }

    public String register(String username, String password, String email) throws DataAccessException{
        if(userDAO.getUser(username) == null){
            userDAO.createUser(username,password,email);
            return authDAO.addToken(username);
        }
        else{
            throw new DataAccessException("Already taken");
        }
    }

    public HashMap<String, String> login(String username, String password) throws DataAccessException {
        UserData credentials = userDAO.getUser(username);
        if (credentials != null && credentials.getPassword().equals(password)) {
            String authToken = authDAO.addToken(username);
            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("username", username);
            resultMap.put("authToken", authToken);
            return resultMap;
        }
        throw new DataAccessException("Unauthorized");
    }

    public void logout(String authToken) throws DataAccessException{
        authDAO.deleteToken(authToken);
    }
}
