package dataAccess;
import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO {
    private final ArrayList<UserData> userList = new ArrayList<>();

    public UserData getUser(String username) throws DataAccessException{
        for(UserData user : userList){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        throw new DataAccessException("Unauthorized");
    }

    public void clearUsers()throws DataAccessException{
        userList.clear();
    }

    public void createUser(String username, String password, String email) throws DataAccessException{
        UserData newUser = new UserData(username,password,email);
        userList.add(newUser);
    }
}
