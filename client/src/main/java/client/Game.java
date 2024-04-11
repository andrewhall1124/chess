package client;

import chess.*;
import exception.ResponseException;
import request.CreateGameRequest;
import response.CreateGameResponse;
import server.ServerFacade;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Game {
    private final String serverUrl;
    private ServerFacade server;
    private String authToken;
    private int gameID;
    private ChessGame.TeamColor teamColor;
    private  WebSocketFacade ws;
    private boolean isPlayer = true;
    private String reset = "\n" + RESET + ">>> " + GREEN;
    public Game(String serverUrl){
        this.serverUrl = serverUrl;
    }

    public String run(String authToken, int gameID, String teamColor, boolean isPlayerMode) {
        this.authToken = authToken;
        this.gameID = gameID;
        isPlayer = isPlayerMode;

        ChessGame.TeamColor teamColorClass = null;
        if (teamColor != null) {
            if (teamColor.equals("WHITE")) {
                teamColorClass = ChessGame.TeamColor.WHITE;
                this.teamColor = teamColorClass;
            } else if (teamColor.equals("BLACK")) {
                teamColorClass = ChessGame.TeamColor.BLACK;
                this.teamColor = teamColorClass;
            }
        }

        try {
            if (isPlayerMode) {
                ws = new WebSocketFacade(serverUrl, teamColorClass);
                ws.joinPlayer(authToken, gameID, teamColorClass);
            } else {
                ws = new WebSocketFacade(serverUrl, ChessGame.TeamColor.OBSERVER);
                ws.joinObserver(authToken, gameID);
            }

            Scanner scanner = new Scanner(System.in);
            var result = "";
            while (!result.equals("Left the game")) {
                String line = scanner.nextLine();
                try {
                    result = eval(line);
                    System.out.print(BLUE + result);
                } catch (Throwable e) {
                    var msg = e.toString();
                    System.out.print(msg);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return "\n";
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(isPlayer){
                return switch (cmd) {
                    case "leave" -> ws.leave(authToken,gameID);
                    case "move" -> makeMove(params);
                    case "redraw" -> ws.redraw();
                    case "resign" -> ws.resign(authToken,gameID);
                    default -> helpPlayer();
                };
            }
            else{
                return switch (cmd) {
                    case "leave" -> ws.leave(authToken,gameID);
                    default -> helpObserver();
                };
            }

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String helpPlayer() {
        return """
                - help
                - redraw
                - leave
                - move <from letter number> <to letter number>
                - resign
                """
                + reset;
    }

    public String helpObserver() {
        return """
                - help
                - leave
                """
                + reset;
    }
    public String leave() throws ResponseException{

        return "Left the game";
    }

    public String makeMove(String ...params) throws ResponseException {
        if(params.length >= 2){
            ChessPosition from = convertPosString(params[0]);
            ChessPosition to = convertPosString(params[1]);
            ChessPiece.PieceType promotion = null;
            if(params.length > 2){
                promotion = convertPieceString(params[2]).getPieceType();
            }
            ChessMove move = new ChessMove(from, to, promotion);
            ws.makeMove(authToken,gameID, move);
            return "";
        }
        throw new ResponseException(400, "Expected: <from> <to>");
    }

    private ChessPosition convertPosString(String location){

        char col = location.charAt(0);
        char row = location.charAt(1);

        int y = col - 'a' + 1;
        int x = Character.digit(row, 10);
        ChessPosition pos = new ChessPosition(x,y);
        return pos;
    }

    private ChessPiece convertPieceString(String piece) {
        switch(piece){
            case "QUEEN" -> {
                return new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
            }
            case "ROOK" -> {
                return new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            }
            case "BISHOP" -> {
                return new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            }
            case "KNIGHT" -> {
                return new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            }
        }
        return null;
    }


    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
