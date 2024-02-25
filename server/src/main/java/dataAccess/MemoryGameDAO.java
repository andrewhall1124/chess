package dataAccess;

import chess.ChessGame;
import model.GameData;
import service.GameService;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryGameDAO implements GameDAO {
    private final ArrayList<GameData> gameList = new ArrayList<>();

    public void clearGames(){
        gameList.clear();
    }

    public String addGame(GameData game) throws DataAccessException{
        for(GameData gameIterator : gameList){
            if(gameIterator.getName().equals(game.getName())){
                throw new DataAccessException("Bad request");
            }
        }
        UUID uuid = UUID.randomUUID();
        game.setId(uuid.toString());
        gameList.add(game);
        return uuid.toString();
    }

    public ArrayList<GameData> getGames() throws DataAccessException{
        return this.gameList;
    }

    public GameData getGame(String gameId) throws DataAccessException{
        for(GameData game : gameList){
            if(game.getId().equals(gameId)){
                return game;
            }
        }
        return null;
    }

    public void joinGame(String playerColor, String gameId, String username) throws DataAccessException{
        for (GameData game : gameList) {
            if (game.getId().equals(gameId)) {
                if (playerColor.equals("WHITE")) {
                    if(game.getWhiteUserName().equals("")){
                        game.setWhiteUserName(username);
                    }
                    else{
                        throw new DataAccessException("Already taken");
                    }
                } else if (playerColor.equals("BLACK")) {
                    if (game.getBlackUserName().equals("")) {
                        game.setBlackUserName(username);
                    } else {
                        throw new DataAccessException("Already taken");
                    }
                }
            }
        }
    }
}
