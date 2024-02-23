package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    private final MemoryGameDAO dataAccess;

    public GameService(MemoryGameDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear(){
        System.out.println("Game service called");
        dataAccess.deleteGames();
    }

    public void add(GameData game){
        dataAccess.addGame(game);
    }

    public ArrayList<GameData> get(){
        return dataAccess.getGames();
    }
}
