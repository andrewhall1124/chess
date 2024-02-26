package dataAccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> userMap = new HashMap<String, UserData>();

    public void createUser(UserData user){
        userMap.put(user.username(),user);
    }

    public UserData readUser(String username){
        return userMap.get(username);
    }
    public void deleteAllGames(){
        userMap.clear();
    }
}
