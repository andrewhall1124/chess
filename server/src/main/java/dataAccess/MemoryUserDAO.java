package dataAccess;
import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO {
    private final ArrayList<UserData> userList = new ArrayList<>();

    public UserData getUser(String username) throws DataAccessException{
        for(UserData user : userList){
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    public void clearUsers(){
        userList.clear();
    }

    public void createUser(String username, String password, String email) throws DataAccessException{
        if(username == null || password == null || email == null){
            throw new DataAccessException("Bad request");
        }
        UserData newUser = new UserData(username,password,email);
        userList.add(newUser);
    }
}
