package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryAuthDAO {
    private final ArrayList<GameData> tokensList = new ArrayList<>();

    public void deleteTokens(){
        tokensList.clear();
    }
}
