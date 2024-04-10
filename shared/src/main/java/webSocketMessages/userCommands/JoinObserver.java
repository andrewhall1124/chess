package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command for a user to join a game as an observer.
 */
public class JoinObserver extends UserGameCommand {

    private int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinObserver)) return false;
        if (!super.equals(o)) return false;
        JoinObserver that = (JoinObserver) o;
        return gameID == that.gameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID);
    }
}