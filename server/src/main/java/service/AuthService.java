package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import request.RegisterRequest;

public class AuthService {
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();


    public String register(RegisterRequest request){
        return authDAO.createAuth(request.username());
    }
}
