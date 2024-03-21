package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.ServerFacade;
import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private String authToken;

    private HashMap<Integer, GameData> gameList = new HashMap<>();

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if (authToken == null) {
            return """
                    - help
                    - register <username> <password> <email>
                    - login <username> <password>
                    - quit
                    """;
        }
        return """
                - help
                - logout
                - create <name>
                - list
                - join  <ID> [BLACK | WHITE | empty]
                - observe <ID>
                - quit
                """;
    }

    public String register(String... params) throws ResponseException {
        if(params.length >= 3){
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse response = server.register(request);
            authToken = response.authToken();
            return "Successfully registered as " + response.username();
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }
    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResponse response = server.login(request);
            authToken = response.authToken();
            return String.format("You logged in as %s.", response.username());
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout(authToken);
        authToken = null;
        return "Logged out";
    }

    public String createGame(String ...params) throws ResponseException {
        assertSignedIn();
        if(params.length >= 1){
            CreateGameRequest request = new CreateGameRequest(params[0]);
            CreateGameResponse response = server.createGame(request, authToken);
            return String.format("Game ID: %s.", response.gameID());
        }
        throw new ResponseException(400, "Expected: <name>");
    }

    public String list() throws ResponseException{
        assertSignedIn();
        gameList.clear();
        ListGamesResponse response = server.list(authToken);
        StringBuilder result = new StringBuilder();
        var count = 1;
        for(GameData game : response.games()){
            gameList.put(count,game);
            result.append(String.format("%s. ",count));
            result.append(String.format("%s\n",game.gameName()));
            result.append(String.format("    White: %s\n",game.whiteUsername()));
            result.append(String.format("    Black: %s\n",game.blackUsername()));
            result.append("\n");
            count ++;
        }
        return result.toString();
    }

    public String join(String ...params) throws ResponseException{
        assertSignedIn();
        if(params.length >= 2){
            Integer gameID = Integer.parseInt(params[0]);
            JoinGameRequest request = new JoinGameRequest(params[1].toUpperCase(),gameList.get(gameID).gameID());
            server.join(request,authToken);
            StringBuilder result = new StringBuilder();
            GameData game = gameList.get(gameID);
            result.append(String.format("Joined %s\n", gameList.get(gameID).gameName()));
            result.append(drawWhiteBoard(game.game().getBoard())).append("\n");
            result.append(drawBlackBoard(game.game().getBoard())).append("\n");
            return result.toString();
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE | BLACK | empty]");
    }

    public String observe(String ...params) throws ResponseException{
        assertSignedIn();
        if(params.length >= 1){
            Integer gameID = Integer.parseInt(params[0]);
            JoinGameRequest request = new JoinGameRequest("",gameList.get(gameID).gameID());
            server.join(request,authToken);
            GameData game = gameList.get(gameID);
            StringBuilder result = new StringBuilder();
            result.append(String.format("Observing %s\n", game.gameName()));
            result.append(drawWhiteBoard(game.game().getBoard()) + "\n");
            result.append(drawBlackBoard(game.game().getBoard()) + "\n");
            return result.toString();
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String drawWhiteBoard(ChessBoard board){
        StringBuilder result = new StringBuilder();
        String letters = "ABCDEFGH";
        String numbers = "12345678";

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

    private void assertSignedIn() throws ResponseException {
        if (authToken == null) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
