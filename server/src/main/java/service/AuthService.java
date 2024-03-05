package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.SQLAuthDAO;
import request.LoginRequest;
import request.RegisterRequest;

public class AuthService {
    private final SQLAuthDAO authDAO = new SQLAuthDAO();

    public void clear() throws DataAccessException{
        authDAO.deleteAllAuth();
    }
    public String register(RegisterRequest request) throws DataAccessException{
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

    public String login(LoginRequest request) throws DataAccessException{
        return authDAO.createAuth(request.username());
    }

    public void verify(String authToken) throws DataAccessException {
        if(authDAO.readAuth(authToken) == null){
            throw new DataAccessException("error: unauthorized");
        }
    }

    public String getUsername(String authToken) throws DataAccessException{
        if(authDAO.readAuth(authToken) == null){
            throw new DataAccessException("error: unauthorized");
        }
        return authDAO.readAuth(authToken).username();
    }
}
