package webSocketMessages.userCommands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command for a user to make a move in a game.
 */
public class MakeMove extends UserGameCommand {

    private int gameID;
    private ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MakeMove)) return false;
        if (!super.equals(o)) return false;
        MakeMove that = (MakeMove) o;
        return gameID == that.gameID && move.equals(that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID, move);
    }
}