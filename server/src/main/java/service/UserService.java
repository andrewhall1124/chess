package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.SQLUserDAO;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
public class UserService {
    private final SQLUserDAO userDAO = new SQLUserDAO();
    public void clear() throws DataAccessException  {
        userDAO.deleteAllUsers();
    }
    public String register(RegisterRequest request) throws DataAccessException {
        if(userDAO.readUser(request.username()) != null){
            throw new DataAccessException("error: already taken");
        }
        if(request.username() == null || request.password() == null || request.email() == null){
            throw new DataAccessException("error: bad request");
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
