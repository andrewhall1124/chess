package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a command for a user to join a game as a player.
 */
public class JoinPlayer extends UserGameCommand {

    private int gameID;
    private ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinPlayer)) return false;
        if (!super.equals(o)) return false;
        JoinPlayer that = (JoinPlayer) o;
        return gameID == that.gameID && playerColor == that.playerColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID, playerColor);
    }
}
