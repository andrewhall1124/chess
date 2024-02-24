package model;

import chess.ChessGame;

public class GameData {
    private String id = "";
    private String whiteUserName = "";
    private String blackUserName = "";
    private String gameName;
    private ChessGame game;

    public GameData(String gameName){
        this.gameName = gameName;
    }

    public void setWhiteUserName(String name){
        this.whiteUserName = name;
    }

    public void setBlackUserName(String name){
        this.blackUserName = name;
    }

    public void setId(String uuid){
        this.id = uuid;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.gameName;
    }

    public String getWhiteUserName(){
        return this.whiteUserName;
    }

    public String getBlackUserName(){
        return this.blackUserName;
    }
}
