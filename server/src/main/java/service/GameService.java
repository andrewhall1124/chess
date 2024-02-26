package service;

import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import model.GameData;
import request.CreateGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

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
}
