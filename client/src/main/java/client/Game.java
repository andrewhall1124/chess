package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Game {
    private final String serverUrl;
    private  WebSocketFacade ws;
    private String reset = "\n" + RESET + ">>> " + GREEN;
    public Game(String serverUrl){
        this.serverUrl = serverUrl;
    }

    public String run(String authToken, int gameID, String teamColor){

        ChessGame.TeamColor teamColorClass = null;
        if(teamColor.equals("WHITE")){
            teamColorClass = ChessGame.TeamColor.WHITE;
        }
        if(teamColor.equals("BLACK")){
            teamColorClass = ChessGame.TeamColor.BLACK;
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
            System.out.println("Here: " + e.getMessage());
        }
        return "\n";
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> leave();
                case "redraw" -> ws.redraw();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - help
                - redraw
                - leave
                - move
                - resign
                - highlight
                """
                + reset;
    }
    public String leave() throws ResponseException{
        return "Left the game";
    }


    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

    public void notify(ServerMessage serverMessage) {
        System.out.println(RED + serverMessage.getMessage());
        printPrompt();
    }
}
