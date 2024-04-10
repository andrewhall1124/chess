package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents an error message sent from the server to clients.
 */
public class Error extends ServerMessage {

    private String errorMessage;

    public Error(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return "Error: " + errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Error)) return false;
        if (!super.equals(o)) return false;
        Error that = (Error) o;
        return getErrorMessage().equals(that.getErrorMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getErrorMessage());
    }
}