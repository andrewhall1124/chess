package webSocket;

import static ui.EscapeSequences.*;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> handleJoinPlayerCommand(message, session);
            case JOIN_OBSERVER -> handleJoinObserverCommand(message, session);
            case MAKE_MOVE -> handleMakeMoveCommand(message, session);
            case LEAVE -> handleLeaveCommand(message, session);
            case RESIGN -> handleResignCommand(message, session);        }
    }

    private void handleJoinPlayerCommand(String message, Session session) throws IOException {
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
        String authToken = command.getAuthString();
        var result = String.format("%s joined game", authToken) + '\n' + RESET +  ">>>" + GREEN;
        var notification = new Notification(result);
        session.getRemote().sendString("WebSocket response: " + notification.getMessage());
    }

    private void handleJoinObserverCommand(String message, Session session) {
        // Handle JOIN_OBSERVER command
    }

    private void handleMakeMoveCommand(String message, Session session) {
        // Handle MAKE_MOVE command
    }

    private void handleLeaveCommand(String message, Session session) {
        // Handle LEAVE command
    }

    private void handleResignCommand(String message, Session session) {
        // Handle RESIGN command
    }
}
