package webSocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;

@WebSocket
public class WSServer {
    WSServer(){
        Spark.port(8080);
        //Initialization
        Spark.webSocket("/connect", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    //Recieve messages
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        //Send message
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
