package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryUserDAO {
    private final ArrayList<GameData> userList = new ArrayList<>();

    public void deleteUsers(){
        userList.clear();
    }
}
