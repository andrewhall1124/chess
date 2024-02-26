package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
public class MemoryGameDAO {
    private final HashMap<Integer, GameData> gameMap = new HashMap<>();

    public void createGame(GameData game){
        gameMap.put(game.gameID(), game);
    }

    public GameData readGame(Integer gameID){
        return gameMap.get(gameID);
    }

    public void updateGame(GameData game){
        gameMap.remove(game.gameID());
        gameMap.put(game.gameID(),game);
    }

    public void deleteGame(Integer gameID){
        gameMap.remove(gameID);
    }

    public ArrayList<GameData> readAllGames(){
        return new ArrayList<>(gameMap.values());
    }
    public void deleteAllGames(){
        gameMap.clear();
    }
}
