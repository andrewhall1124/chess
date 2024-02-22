package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;

public class GameService {
    private final MemoryGameDAO dataAccess;

    public GameService(MemoryGameDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear(){
        System.out.println("Game service called");
        dataAccess.deleteGames();
    }
}