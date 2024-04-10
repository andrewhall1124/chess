package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }
    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void notifyAll(String authToken, Notification notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(authToken)) {
                    c.send(notification.getMessage());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void sendLoadTo(String authToken, LoadGame load) throws IOException {
        Connection connection = connections.get(authToken);
        Gson gson = new Gson();
        connection.send(gson.toJson(load));
    }
}
