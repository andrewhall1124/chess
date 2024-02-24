package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;

public class AuthService {

    private final MemoryAuthDAO dataAccess;

    public AuthService(MemoryAuthDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() throws DataAccessException{
        dataAccess.clearTokens();
    }
}
