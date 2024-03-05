package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData game) throws DataAccessException;
    GameData readGame(Integer gameID)throws DataAccessException;
    void deleteGame(Integer gameID)throws DataAccessException;
    ArrayList<GameData> readAllGames()throws DataAccessException;
    void deleteAllGames()throws DataAccessException;
}
