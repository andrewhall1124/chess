package dataAccess;

import chess.ChessGame;
import model.GameData;
import service.GameService;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryGameDAO implements GameDAO {
    private final ArrayList<GameData> gameList = new ArrayList<>();

    public void deleteGames(){
        gameList.clear();
    }

    public String addGame(GameData game){
        UUID uuid = UUID.randomUUID();
        game.setId(uuid.toString());
        gameList.add(game);
        return uuid.toString();
    }

    public ArrayList<GameData> getGames(){
        return this.gameList;
    }

    public GameData getGame(String gameId){
        for(GameData game : gameList){
            if(game.getId().equals(gameId)){
                return game;
            }
        }
        return null;
    }

    public void joinGame(String playerColor, String gameId, String username){
        for (GameData game : gameList) {
            if (game.getId().equals(gameId)) {
                if (playerColor.equals("WHITE")) {
                    game.setWhiteUserName(username);
                } else if (playerColor.equals("BLACK")) {
                    game.setBlackUserName(username);
                }
            }
        }
    }
}
