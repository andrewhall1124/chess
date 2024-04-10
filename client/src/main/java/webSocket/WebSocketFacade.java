package webSocket;

import chess.ChessGame;
import client.Game;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.userCommands.JoinPlayer;

import java.io.IOException;
import java.net.URI;
import javax.websocket.*;
public class WebSocketFacade extends Endpoint {
    private Game game;
    public Session session;

    public WebSocketFacade() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.print(message);
            }
        });
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
}
