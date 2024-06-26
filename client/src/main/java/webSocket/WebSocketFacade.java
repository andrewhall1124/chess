package webSocket;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import static ui.EscapeSequences.*;

import java.io.IOException;
import java.net.URI;
import javax.websocket.*;

public class WebSocketFacade extends Endpoint {
    private ChessGame game;
    private ChessGame.TeamColor teamColor;
    public Session session;
    private String reset = "\n\n" + RESET +  ">>> " + GREEN;

    public WebSocketFacade(String serverUrl, ChessGame.TeamColor teamColor) throws Exception {
        this.teamColor = teamColor;
        String url = serverUrl.replace("http", "ws");
        URI uri = new URI(url + "/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case NOTIFICATION -> handleNotification(message);
                    case LOAD_GAME -> handleLoadGame(message);
                    case ERROR -> handleError(message);
                }
            }
        });
    }

    public void handleNotification(String message){
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.print(notification.getMessage());
    }

    public void handleLoadGame(String message){
        LoadGame load = new Gson().fromJson(message, LoadGame.class);
        this.game = load.getGame();
        System.out.print(redraw());
    }

    public void handleError(String message){
        Error error = new Gson().fromJson(message, Error.class);
        System.out.println(error.getErrorMessage());
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            var command = new JoinPlayer(authToken, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws ResponseException {
        try {
            var command = new JoinObserver(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            var command = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public String resign(String authToken, int gameID) throws ResponseException {
        try {
            var command = new Resign(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

        return "";
    }

    public String leave(String authToken, int gameID) throws ResponseException {
        try {
            var command = new Leave(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

        return "Left the game";
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public String redraw(){
        if(teamColor.equals(ChessGame.TeamColor.WHITE)){
            return '\n' + drawBoard(game.getBoard(), ChessGame.TeamColor.WHITE) + reset;
        }
        else if (teamColor.equals(ChessGame.TeamColor.BLACK)){
            return '\n' + drawBoard(game.getBoard(), ChessGame.TeamColor.BLACK) + reset;
        }
        else{
            return '\n' + drawBoard(game.getBoard(), ChessGame.TeamColor.WHITE)
                    + '\n' + drawBoard(game.getBoard(), ChessGame.TeamColor.BLACK) + reset;
        }
    }
    public String drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        StringBuilder result = new StringBuilder();
        String letters = "ABCDEFGH";
        String numbers = "87654321";

        // Top border
        result.append(SET_BG_COLOR_BLACK + EMPTY);
        for (char letter : letters.toCharArray()) {
            result.append(SET_BG_COLOR_BLACK);
            result.append(SET_TEXT_COLOR_WHITE);
            result.append(String.format("\u2003%s ", letter));
        }
        result.append(SET_BG_COLOR_BLACK + EMPTY);
        result.append(RESET_ALL + "\n");

        // Pieces + main board
        for (int i = 1; i <= 8; i++) {
            int row = perspective == ChessGame.TeamColor.WHITE ? i : 9 - i; // Adjust row based on perspective

            for (int j = 1; j <= 8; j++) {
                int col = j;

                // Left border numbers
                if (j == 1) {
                    result.append(SET_BG_COLOR_BLACK);
                    result.append(SET_TEXT_COLOR_WHITE);
                    result.append(String.format("\u2003%s ", numbers.charAt(row - 1)));
                }

                // Color Checkers
                if ((row - col) % 2 == 0) {
                    result.append(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    result.append(SET_BG_COLOR_DARK_GREY);
                }

                // Color pieces
                ChessPosition curPos = new ChessPosition(-(row-9), col);
                ChessPiece curPiece = board.getPiece(curPos);
                if (curPiece != null) {
                    result.append(drawPiece(curPiece));
                } else {
                    result.append(EMPTY);
                }

                if (j == 8) {
                    result.append(SET_BG_COLOR_BLACK + EMPTY);
                }
            }

            // Reset
            result.append(RESET_ALL);
            result.append(RESET_TEXT_COLOR);
            result.append("\n");
        }

        // Bottom border
        for (int i = 1; i <= 10; i++) {
            result.append(SET_BG_COLOR_BLACK + EMPTY);
        }
        result.append(RESET_ALL);

        return result.toString();
    }

    private String drawPiece(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return switch(piece.getPieceType()) {
                case KING -> SET_TEXT_COLOR_BLUE + WHITE_KING;
                case QUEEN -> SET_TEXT_COLOR_BLUE + WHITE_QUEEN;
                case BISHOP -> SET_TEXT_COLOR_BLUE + WHITE_BISHOP;
                case KNIGHT -> SET_TEXT_COLOR_BLUE + WHITE_KNIGHT;
                case ROOK -> SET_TEXT_COLOR_BLUE + WHITE_ROOK;
                case PAWN -> SET_TEXT_COLOR_BLUE + WHITE_PAWN;
            };
        }
        else{
            return switch(piece.getPieceType()) {
                case KING -> SET_TEXT_COLOR_RED + WHITE_KING;
                case QUEEN -> SET_TEXT_COLOR_RED + WHITE_QUEEN;
                case BISHOP -> SET_TEXT_COLOR_RED + WHITE_BISHOP;
                case KNIGHT -> SET_TEXT_COLOR_RED + WHITE_KNIGHT;
                case ROOK -> SET_TEXT_COLOR_RED + WHITE_ROOK;
                case PAWN -> SET_TEXT_COLOR_RED + WHITE_PAWN;
            };
        }
    }
}
