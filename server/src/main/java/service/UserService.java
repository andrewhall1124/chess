package service;

import com.google.gson.Gson;
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

    public String register(String username, String password, String email){
        if(dataAccess.getUser(username) == null){
            return dataAccess.createUser(username,password,email);
        }
        else{
            return null;
        }
    }
}
