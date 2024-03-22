package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import server.ServerFacade;
import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Game {
    private final ServerFacade server;
    public Game(String serverUrl){
        this.server = new ServerFacade(serverUrl);
    }

    public String run(ChessGame game){
        System.out.println(drawBlackBoard(game.getBoard()));
        System.out.println(drawWhiteBoard(game.getBoard()));
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("Left the game")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
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
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - leave
                """;
    }
    public String leave() throws ResponseException{
        return "Left the game";
    }
    public String drawWhiteBoard(ChessBoard board){
        StringBuilder result = new StringBuilder();
        String letters = "ABCDEFGH";
        String numbers = "87654321";

        //Top border
        result.append( SET_BG_COLOR_BLACK + EMPTY);
        for(char letter : letters.toCharArray()) {
            result.append(SET_BG_COLOR_BLACK);
            result.append(SET_TEXT_COLOR_WHITE);
            result.append(String.format("\u2003%s ", letter));
        }
        result.append( SET_BG_COLOR_BLACK + EMPTY);
        result.append(RESET_ALL + "\n");
        //Pieces + main board
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                //Left border numbers
                if(j == 1){
                    result.append(SET_BG_COLOR_BLACK);
                    result.append(SET_TEXT_COLOR_WHITE);
                    result.append(String.format("\u2003%s ",numbers.charAt(i-1)));
                }
                //Color Checkers
                if((i - j) % 2 == 0){
                    result.append(SET_BG_COLOR_LIGHT_GREY);
                } else{
                    result.append(SET_BG_COLOR_DARK_GREY);
                }
                //Color pieces
                ChessPosition curPos = new ChessPosition(i, j);
                ChessPiece curPiece = board.getPiece(curPos);
                if(curPiece != null){
                    result.append(drawPiece(curPiece));
                }
                else{
                    result.append(EMPTY);
                }
                if(j == 8){
                    result.append(SET_BG_COLOR_BLACK + EMPTY);
                }
            }
            //Reset
            result.append(RESET_ALL);
            result.append(RESET_TEXT_COLOR);
            result.append("\n");
        }
        //Bottom border
        for(int i = 1; i <= 10; i++){
            result.append(SET_BG_COLOR_BLACK + EMPTY);
        }
        result.append(RESET_ALL);
        return result.toString();
    }

    public String drawBlackBoard(ChessBoard board){
        StringBuilder result = new StringBuilder();
        String letters = "HGFEDCBA";
        String numbers = "87654321";

        //Top border
        result.append( SET_BG_COLOR_BLACK + EMPTY);
        for(char letter : letters.toCharArray()) {
            result.append(SET_BG_COLOR_BLACK);
            result.append(SET_TEXT_COLOR_WHITE);
            result.append(String.format("\u2003%s ", letter));
        }
        result.append( SET_BG_COLOR_BLACK + EMPTY);
        result.append(RESET_ALL + "\n");
        //Pieces + main board
        for(int i = 8; i > 0; i--){
            for(int j = 8; j > 0; j--){
                //Left border numbers
                if(j == 8){
                    result.append(SET_BG_COLOR_BLACK);
                    result.append(SET_TEXT_COLOR_WHITE);
                    result.append(String.format("\u2003%s ",numbers.charAt(i-1)));
                }
                //Color Checkers
                if((i - j) % 2 == 0){
                    result.append(SET_BG_COLOR_LIGHT_GREY);
                } else{
                    result.append(SET_BG_COLOR_DARK_GREY);
                }
                //Color pieces
                ChessPosition curPos = new ChessPosition(i, j);
                ChessPiece curPiece = board.getPiece(curPos);
                if(curPiece != null){
                    result.append(drawPiece(curPiece));
                }
                else{
                    result.append(EMPTY);
                }
                if(j == 1){
                    result.append(SET_BG_COLOR_BLACK + EMPTY);
                }
            }
            //Reset
            result.append(RESET_ALL);
            result.append(RESET_TEXT_COLOR);
            result.append("\n");
        }
        //Bottom border
        for(int i = 1; i <= 10; i++){
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
    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
