package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, HashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.computeIfAbsent(gameID, k -> new HashMap<>()).put(authToken, connection);
    }

    public void remove(Integer gameID, String authToken) {
        HashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null) {
            gameConnections.remove(authToken);
            if (gameConnections.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void notifyAllExcept(Integer gameID, String authToken, Notification notification) throws IOException {
        HashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null) {
            var removeList = new ArrayList<Connection>();
            for (Map.Entry<String, Connection> entry : gameConnections.entrySet()) {
                Connection c = entry.getValue();
                if (c.session.isOpen()) {
                    if (!c.authToken.equals(authToken)) {
                        Gson gson = new Gson();
                        c.send(gson.toJson(notification));
                    }
                } else {
                    removeList.add(c);
                }
            }
            // Clean up any connections that were left open.
            for (Connection c : removeList) {
                gameConnections.remove(c.authToken);
            }
            if (gameConnections.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void notifyAll(Integer gameID, Notification notification) throws IOException {
        HashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null) {
            var removeList = new ArrayList<Connection>();
            for (Map.Entry<String, Connection> entry : gameConnections.entrySet()) {
                Connection c = entry.getValue();
                if (c.session.isOpen()) {
                    Gson gson = new Gson();
                    c.send(gson.toJson(notification));
                } else {
                    removeList.add(c);
                }
            }
            // Clean up any connections that were left open.
            for (Connection c : removeList) {
                gameConnections.remove(c.authToken);
            }
            if (gameConnections.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void sendLoadTo(Integer gameID, String authToken, LoadGame load) throws IOException {
        HashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null) {
            Connection connection = gameConnections.get(authToken);
            if (connection != null) {
                Gson gson = new Gson();
                connection.send(gson.toJson(load));
            }
        }
    }

    public void sendErrorTo(Session session, String authToken, Error error) throws IOException {
        Connection connection = new Connection(authToken,session);
        Gson gson = new Gson();
        connection.send(gson.toJson(error));
    }

    public void loadAll(Integer gameID, LoadGame load) throws IOException {
        HashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null) {
            for (Map.Entry<String, Connection> entry : gameConnections.entrySet()) {
                Connection connection = entry.getValue();
                if (connection.session.isOpen()) {
                    Gson gson = new Gson();
                    connection.send(gson.toJson(load));
                }
            }
        }
    }
}