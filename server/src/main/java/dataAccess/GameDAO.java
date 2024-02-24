package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clearGames();
    String addGame(GameData game);
    ArrayList<GameData> getGames();
    GameData getGame(String gameId);
}
