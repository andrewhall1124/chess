package webSocket;

import java.net.URI;
import java.util.Scanner;
import javax.websocket.*;

public class WSClient {
    public Session session;

    public WSClient(String url) {
        //Initialization
        try {
            URI uri = new URI(url);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
        }
        catch(Exception e){
            System.out.println("Failed to connect to Web Socket");
        }

        //Recieve Messages
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    //Send messages
    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
