package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command for a user to resign from a game.
 */
public class Resign extends UserGameCommand {

    private int gameID;

    public Resign(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resign)) return false;
        if (!super.equals(o)) return false;
        Resign that = (Resign) o;
        return gameID == that.gameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID);
    }
}