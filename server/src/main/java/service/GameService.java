package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import java.util.Random;

public class GameService {
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void clear(){
        gameDAO.deleteAllGames();
    }
    public CreateGameResponse createGame(CreateGameRequest request){
        Random random = new Random();
        Integer gameID = Math.abs(random.nextInt());
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID,null,null, request.gameName(),game);
        gameDAO.createGame(gameData);
        return  new CreateGameResponse(gameID);
    }

    public ListGamesResponse listGames(){
        return new ListGamesResponse(gameDAO.readAllGames());
    }

    public void joinGame(JoinGameRequest request, String username) throws DataAccessException {
        if(gameDAO.readGame(request.gameID()) == null){
            throw new DataAccessException("error: bad request");
        }
        if(request.playerColor() != null){
            Integer gameID = request.gameID();
            GameData oldGame = gameDAO.readGame(gameID);
            if(request.playerColor().equals("WHITE") && oldGame.whiteUsername() != null){
                throw new DataAccessException("error: already taken");
            }
            if(request.playerColor().equals("BLACK") && oldGame.blackUsername() != null){
                throw new DataAccessException("error: already taken");
            }
            String playerColor = request.playerColor();
            String whiteUserName = playerColor.equals("WHITE") ? username : oldGame.whiteUsername();
            String blackUserName = playerColor.equals("BLACK") ? username : oldGame.blackUsername();
            String gameName = oldGame.gameName();
            ChessGame game = oldGame.game();
            GameData newGame = new GameData(gameID,whiteUserName,blackUserName,gameName,game);
            gameDAO.deleteGame(gameID);
            gameDAO.createGame(newGame);
        }
    }
}
