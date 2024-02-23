package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private final ArrayList<GameData> gameList = new ArrayList<>();

    public void deleteGames(){
        System.out.println("Game DAO called");
        gameList.clear();
    }

    public void addGame(GameData game){
        gameList.add(game);
    }

    public ArrayList<GameData> getGames(){
        return this.gameList;
    }

    public void joinGame(ChessGame.TeamColor teamColor, String gameId, String username){
        for (GameData game : gameList) {
            if (game.getId().equals(gameId)) {
                if (teamColor == ChessGame.TeamColor.WHITE) {
                    game.setWhiteUserName(username);
                } else if (teamColor == ChessGame.TeamColor.BLACK) {
                    game.setBlackUserName(username);
                }
            }
        }
    }
}
