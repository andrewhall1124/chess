package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
public class UserService {
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    public String register(RegisterRequest request) throws DataAccessException {
        if(userDAO.readUser(request.username()) != null){
            throw new DataAccessException("error: already taken");
        }
        UserData user = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(user);
        return user.username();
    }

    public String login(LoginRequest request) throws DataAccessException{
        if(userDAO.readUser(request.username()) == null){
            throw new DataAccessException("error: unauthorized");
        }
        String password = userDAO.readUser(request.username()).password();
        if(!request.password().equals(password)){
            throw new DataAccessException("error: unauthorized");
        }
        return userDAO.readUser(request.username()).username();
    }
}
