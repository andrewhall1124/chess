package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData game);
    GameData readGame(Integer gameID);
    void deleteGame(Integer gameID);
    ArrayList<GameData> readAllGames();
    void deleteAllGames();
}
