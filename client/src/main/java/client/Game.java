package client;

import chess.*;
import exception.ResponseException;
import request.CreateGameRequest;
import response.CreateGameResponse;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Game {
    private final String serverUrl;
    private String authToken;
    private int gameID;
    private ChessGame.TeamColor teamColor;
    private  WebSocketFacade ws;
    private boolean isPlayer = true;
    private String reset = "\n" + RESET + ">>> " + GREEN;
    public Game(String serverUrl){
        this.serverUrl = serverUrl;
    }

    public String runAsPlayer(String authToken, int gameID, String teamColor){
        this.authToken = authToken;
        this.gameID = gameID;

        ChessGame.TeamColor teamColorClass = null;
        if(teamColor.equals("WHITE")){
            teamColorClass = ChessGame.TeamColor.WHITE;
            this.teamColor = teamColorClass;
        }
        if(teamColor.equals("BLACK")){
            teamColorClass = ChessGame.TeamColor.BLACK;
            this.teamColor = teamColorClass;
        }

        try{
            ws = new WebSocketFacade(teamColorClass);
            ws.joinPlayer(authToken,gameID,teamColorClass);
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
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "\n";
    }

    public String runAsObserver(String authToken, int gameID){
        this.authToken = authToken;
        this.gameID = gameID;

        isPlayer = false;
        try{
            ws = new WebSocketFacade(ChessGame.TeamColor.OBSERVER);
            ws.joinObserver(authToken,gameID);
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
        }

        catch(Exception e){
            System.out.println(e.getMessage());
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
                    case "leave" -> leave();
                    case "move" -> makeMove(params);
                    case "redraw" -> ws.redraw();
                    default -> helpPlayer();
                };
            }
            else{
                return switch (cmd) {
                    case "leave" -> leave();
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
                - highlight
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
            ChessMove move = new ChessMove(to, from, promotion);
            ws.makeMove(authToken,gameID, move);
            return "Made move";
        }
        throw new ResponseException(400, "Expected: <from> <to>");
    }

    private ChessPosition convertPosString(String location){
        char[] array = location.toCharArray();
        int x = 0;
        int y = array[1];
        switch(array[0]){
            case 'a' ->  x=1;
            case 'b' ->  x=2;
            case 'c' ->  x=3;
            case 'd' ->  x=4;
            case 'e' ->  x=5;
            case 'f' ->  x=6;
            case 'g' ->  x=7;
            case 'h' ->  x=8;
        }
        return new ChessPosition(x,y);
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

    public void notify(ServerMessage serverMessage) {
        System.out.println(RED + serverMessage.getMessage());
        printPrompt();
    }
}
