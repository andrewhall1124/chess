package dataAccess;

import com.google.gson.Gson;

public interface UserDAO {
    void deleteUsers();
    String createUser(String username, String password, String email);
}

