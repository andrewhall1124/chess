package model;

import chess.ChessGame;

public record GameData(Integer gameID, String whiteUserName, String blackUserName, String gameName, ChessGame game) {
}
