package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a notification message sent from the server to clients.
 */
public class Notification extends ServerMessage {

    private String message;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        if (!super.equals(o)) return false;
        Notification that = (Notification) o;
        return getMessage().equals(that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMessage());
    }
}