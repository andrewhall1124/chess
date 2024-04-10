package webSocket;

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
            case JOIN_PLAYER -> playerJoined(command, session);
        }
    }

    private void playerJoined(UserGameCommand command, Session session) throws IOException {
        String authToken = command.getAuthString();
        var message = String.format("%s joined game", authToken);
        var notification = new Notification(message);
        session.getRemote().sendString("WebSocket response: " + notification.getMessage());
    }
}
