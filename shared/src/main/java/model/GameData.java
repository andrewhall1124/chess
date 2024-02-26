package model;

import chess.ChessGame;

public record GameData(String gameID, String whiteUserName, String blackUserName, String gameName, ChessGame game) {
}
