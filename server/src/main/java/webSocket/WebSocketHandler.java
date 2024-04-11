package webSocket;

import static ui.EscapeSequences.*;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

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
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        String userName;

        try {
            userName = authService.getUsername(authToken);
        } catch (Exception e) {
            Error error = new Error("Error: Bad auth token");
            connections.sendErrorTo(session, authToken, error);
            return; // Exit the method after sending the error
        }

        ChessGame.TeamColor teamColor = command.getPlayerColor();
        GameData game = gameService.getGameByID(gameID);

        if (game == null) {
            Error error = new Error("Error: Bad game ID");
            connections.sendErrorTo(session, authToken, error);
            return; // Exit the method after sending the error
        }

        // Check if the team color is already taken
        if (isTeamColorTaken(game, teamColor, userName)) {
            Error error = new Error("Error: Team color already taken");
            connections.sendErrorTo(session, authToken, error);
            return; // Exit the method after sending the error
        }

        // Check if the game is empty (no players have joined yet)
        if (game.whiteUsername() == null && game.blackUsername() == null) {
            Error error = new Error("Error: Game is empty, join through HTTP endpoint first");
            connections.sendErrorTo(session, authToken, error);
            return;
        }

        connections.add(gameID, authToken, session);

        String result = YELLOW + String.format("\n%s joined game as %s\n", userName, teamColor.toString()) + reset;
        Notification notification = new Notification(result);
        LoadGame load = new LoadGame(game.game());

        connections.notifyAll(gameID, authToken, notification);
        connections.sendLoadTo(gameID, authToken, load);
    }

    private boolean isTeamColorTaken(GameData game, ChessGame.TeamColor teamColor, String userName) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            String whiteUsername = game.whiteUsername();
            return whiteUsername != null && !Objects.equals(whiteUsername, userName);
        } else if (teamColor == ChessGame.TeamColor.BLACK) {
            String blackUserName = game.blackUsername();
            return blackUserName != null && !Objects.equals(blackUserName, userName);
        }
        return false;
    }

    private void handleJoinObserverCommand(String message, Session session) throws Exception {
        JoinObserver command = new Gson().fromJson(message, JoinObserver.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        String userName;
        try {
            userName = authService.getUsername(authToken);
        }
        catch(Exception e){
            Error error = new Error("Error: Bad auth token");
            connections.sendErrorTo(session,authToken,error);
            throw e;
        }        GameData game = gameService.getGameByID(gameID);

        if(game == null){
            Error error = new Error("Error: Bad game ID");
            connections.sendErrorTo(session,authToken,error);
        }

        connections.add(gameID,authToken, session);

        String result = String.format("%s joined game as an observer", userName) + reset;
        Notification notification = new Notification(result);
        LoadGame load = new LoadGame(game.game());
        connections.notifyAll(gameID,authToken,notification);
        connections.sendLoadTo(gameID,authToken, load);
    }

    private void handleMakeMoveCommand(String message, Session session) throws Exception{
        MakeMove command = new Gson().fromJson(message, MakeMove.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        String userName = authService.getUsername(authToken);

        String result = String.format("%s joined game as an observer", userName) + reset;
        Notification notification = new Notification(result);
        ChessGame game = gameService.getGameByID(gameID).game();
        LoadGame load = new LoadGame(game);
        connections.notifyAll(gameID,authToken,notification);
        connections.sendLoadTo(gameID,authToken, load);    }

    private void handleLeaveCommand(String message, Session session) {
        // Handle LEAVE command
    }

    private void handleResignCommand(String message, Session session) {
        // Handle RESIGN command
    }
}
