package webSocket;

import static ui.EscapeSequences.*;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService = new GameService();
    private final AuthService authService = new AuthService();

    private final String reset = '\n' + '\n' + RESET +  ">>> " + GREEN;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> handleJoinPlayerCommand(message, session);
            case JOIN_OBSERVER -> handleJoinObserverCommand(message, session);
            case MAKE_MOVE -> handleMakeMoveCommand(message, session);
            case LEAVE -> handleLeaveCommand(message, session);
            case RESIGN -> handleResignCommand(message, session);
        }
    }

    private void handleJoinPlayerCommand(String message, Session session) throws Exception {
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);

        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        String userName = authService.getUsername(authToken);

        connections.add(gameID,authToken, session);

        String result = String.format("%s joined game", userName) + reset;
        Notification notification = new Notification(result);
        ChessGame game = gameService.getGameByID(gameID);
        LoadGame load = new LoadGame(game);
        connections.notifyAll(gameID,authToken,notification);
        connections.sendLoadTo(gameID,authToken, load);
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
