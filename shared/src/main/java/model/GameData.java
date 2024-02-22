package model;

import chess.ChessGame;

public class GameData {
    private Number id;
    private String whiteUserName;
    private String blackUserName;
    private String gameName;
    private ChessGame game;

    public GameData(Number id, String whiteUserName, String blackUserName, String gameName, ChessGame game){
        this.id = id;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
        this.gameName = gameName;
        this.game = game;
    }
}
