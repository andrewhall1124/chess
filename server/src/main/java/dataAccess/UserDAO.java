package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user);
    UserData readUser(String username);
    void deleteAllGames();

}
