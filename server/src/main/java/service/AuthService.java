package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import request.LoginRequest;
import request.RegisterRequest;

public class AuthService {
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();


    public String register(RegisterRequest request){
        return authDAO.createAuth(request.username());
    }

    public void logout(String authToken) throws DataAccessException {
        if(authDAO.readAuth(authToken) != null){
            authDAO.deleteAuth(authToken);
        }
        else{
            throw new DataAccessException("error: unauthorized");
        }
    }

    public String login(LoginRequest request){
        return authDAO.createAuth(request.username());
    }
}
