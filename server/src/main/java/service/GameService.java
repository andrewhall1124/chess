package service;

import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

import java.util.Objects;
import java.util.UUID;

public class GameService {
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();

    public CreateGameResponse createGame(CreateGameRequest request){
        String gameID = UUID.randomUUID().toString();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID,"","", request.gameName(),game);
        gameDAO.createGame(gameData);
        return  new CreateGameResponse(gameID);
    }

    public ListGamesResponse listGames(){
        return new ListGamesResponse(gameDAO.readAllGames());
    }

    public void joinGame(JoinGameRequest request, String username){
        String playerColor = request.playerColor();
        String gameID = request.gameID();
        GameData oldGame = gameDAO.readGame(gameID);
        String whiteUserName = playerColor.equals("WHITE") ? username : oldGame.whiteUserName();
        String blackUserName = playerColor.equals("BLACK") ? username : oldGame.blackUserName();
        String gameName = oldGame.gameName();
        ChessGame game = oldGame.game();
        GameData newGame = new GameData(gameID,whiteUserName,blackUserName,gameName,game);
        gameDAO.deleteGame(gameID);
        gameDAO.createGame(newGame);
    }
}
