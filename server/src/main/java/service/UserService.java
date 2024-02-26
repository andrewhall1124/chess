package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import model.UserData;
import request.RegisterRequest;
//Service is responsible for returning the appropriate part of it's corresponding response, and throwing errors
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
}
