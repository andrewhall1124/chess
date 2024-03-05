package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = userDAO.readUser(request.username()).password();

        if(!encoder.matches(request.password(), hashedPassword)){
            throw new DataAccessException("error: unauthorized");
        }
        return userDAO.readUser(request.username()).username();
    }
}
