package dataAccess;

import model.GameData;
import java.util.HashMap;
public class MemoryGameDAO {
    private final HashMap<String, GameData> gameMap = new HashMap<>();

    public void createGame(GameData game){
        gameMap.put(game.gameID(), game);
    }

    public GameData readGame(String gameID){
        return gameMap.get(gameID);
    }

    public void deleteGame(String gameID){
        gameMap.remove(gameID);
    }
}
