package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;


public class UserService {

    private final MemoryUserDAO dataAccess;

    public UserService(MemoryUserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear(){
        dataAccess.deleteUsers();
    }
}
