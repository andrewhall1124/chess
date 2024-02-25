package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clearGames() throws DataAccessException;
    String addGame(GameData game) throws DataAccessException;
    ArrayList<GameData> getGames() throws DataAccessException;
    GameData getGame(String gameId) throws DataAccessException;
}
