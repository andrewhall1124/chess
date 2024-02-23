package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;


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
}
