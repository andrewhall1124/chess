package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import java.util.Random;

public class GameService {
    private final SQLGameDAO gameDAO = new SQLGameDAO();

    public void clear() throws DataAccessException{
        gameDAO.deleteAllGames();
    }
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        if(request.gameName() == null){
            throw new DataAccessException("bad request");
        }
        Random random = new Random();
        Integer gameID = Math.abs(random.nextInt());
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID,null,null, request.gameName(),game);
        gameDAO.createGame(gameData);
        return  new CreateGameResponse(gameID);
    }

    public ListGamesResponse listGames() throws DataAccessException{
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

    public GameData getGameByID(int gameID) throws DataAccessException{
        return gameDAO.readGame(gameID);
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        gameDAO.updateGame(gameID,game);
    }

    public void clearWhiteUser(int gameID) throws DataAccessException {
        gameDAO.deleteWhiteUsername(gameID);
    }

    public void clearBlackUser(int gameID) throws DataAccessException {
        gameDAO.deleteBlackUsername(gameID);
    }

    public void endGame(int gameID, ChessGame game) throws DataAccessException {
        game.setTeamTurn(ChessGame.TeamColor.COMPLETE);
        gameDAO.updateGame(gameID,game);
    }
}
