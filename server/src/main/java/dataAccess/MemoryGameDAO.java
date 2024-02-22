package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private final ArrayList<GameData> gameList = new ArrayList<>();

    public void deleteGames(){
        System.out.println("Game DAO called");
        gameList.clear();
    }
}