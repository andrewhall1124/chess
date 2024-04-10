package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command for a user to leave a game.
 */
public class Leave extends UserGameCommand {

    private int gameID;

    public Leave(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leave)) return false;
        if (!super.equals(o)) return false;
        Leave that = (Leave) o;
        return gameID == that.gameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID);
    }
}