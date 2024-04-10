package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a message sent from the server to clients to load the current game state.
 */
public class LoadGame extends ServerMessage {

    private Object game;

    public LoadGame(Object game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public Object getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadGame)) return false;
        if (!super.equals(o)) return false;
        LoadGame that = (LoadGame) o;
        return getGame().equals(that.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGame());
    }
}