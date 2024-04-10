package webSocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import static ui.EscapeSequences.*;

import java.io.IOException;
import java.net.URI;
import javax.websocket.*;

public class WebSocketFacade extends Endpoint {
    private ChessGame game;
    private ChessGame.TeamColor teamColor;
    public Session session;
    private String reset = "\n\n" + RESET +  ">>> " + GREEN;

    public WebSocketFacade(ChessGame.TeamColor teamColor) throws Exception {
        this.teamColor = teamColor;

        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case NOTIFICATION -> handleNotification(message);
                    case LOAD_GAME -> handleLoadGame(message);
//                    case ERROR -> handleError(message, session);
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

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            var command = new JoinPlayer(authToken, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public String redraw(){
        if(teamColor.equals(ChessGame.TeamColor.WHITE)){
            return drawWhiteBoard(game.getBoard()) + reset;
        }
        else if (teamColor.equals(ChessGame.TeamColor.BLACK)){
            return drawBlackBoard(game.getBoard()) + reset;
        }
        else{
            return drawWhiteBoard(game.getBoard()) + drawBlackBoard(game.getBoard()) + reset;
        }
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
}
