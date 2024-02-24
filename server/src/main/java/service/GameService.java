package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    private final MemoryGameDAO gameDAO;
    private final MemoryAuthDAO authDAO;

    public GameService(MemoryGameDAO gameAccess, MemoryAuthDAO authAccess) {
        this.gameDAO = gameAccess;
        this.authDAO = authAccess;
    }

    public void clear(){
        gameDAO.deleteGames();
    }

    public String createGame(String authToken, String gameName){
        if(authDAO.verifyToken(authToken) != null){
            GameData newGame = new GameData(gameName);
            return gameDAO.addGame(newGame);
        }
        else{
            return null;
        }
    }

    public ArrayList<GameData> getGames(String authToken){
        if(authDAO.verifyToken(authToken) != null){
            return gameDAO.getGames();
        }
        else{
            return null;
        }
    }

    public void joinGame(String playerColor, String gameId, String authToken){
        AuthData user = authDAO.verifyToken(authToken);
        if(gameDAO.getGame(gameId) != null){
            gameDAO.joinGame(playerColor,gameId,user.getUsername());
        }
    }
}
