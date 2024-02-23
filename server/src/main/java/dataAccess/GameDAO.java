package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void deleteGames();
    void addGame(GameData game);
    ArrayList<GameData> getGames();
}
