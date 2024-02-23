package dataAccess;
import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO {
    private final ArrayList<UserData> userList = new ArrayList<>();

    public UserData getUser(String username){
        for(UserData user : userList){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public void deleteUsers(){
        userList.clear();
    }

    public void createUser(String username, String password, String email){
        UserData newUser = new UserData(username,password,email);
        userList.add(newUser);
    }
}
