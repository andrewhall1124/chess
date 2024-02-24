package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;
import service.UserService;
import service.GameService;
import service.AuthService;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final MemoryUserDAO userDao = new MemoryUserDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final GameService gameService = new GameService(gameDAO, authDao);
    private final UserService userService = new UserService(userDao, authDao);
    private final AuthService authService = new AuthService(authDao);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request req, Response res) {
        try{
            gameService.clear();
            userService.clear();
            authService.clear();
            res.type("application/json");
            res.status(200);
            return "{}";
        }
        catch(DataAccessException exception){
            res.type("application/json");
            res.status(500);
            String message = exception.getMessage();
            var obj = Map.of(
                    "Message", message
            );
            var serializer = new Gson();
            return serializer.toJson(obj);
        }
    }

    private Object register(Request req, Response res){
        var body = new Gson().fromJson(req.body(), Map.class);
        String username = body.get("username").toString();
        String password = body.get("password").toString();
        String email = body.get("email").toString();

        String authToken = userService.register(username,password,email);
        res.type("application/json");
        res.status(200);
        var obj = Map.of(
                "username", username,
                "authToken", authToken
        );
        var serializer = new Gson();
        return serializer.toJson(obj);
    }

    private Object login(Request req, Response res){
        var body = new Gson().fromJson(req.body(), Map.class);
        String username = body.get("username").toString();
        String password = body.get("password").toString();
        try{
            HashMap<String, String> response = userService.login(username,password);
            res.type("application/json");
            res.status(200);
            var serializer = new Gson();
            return serializer.toJson(response);
        }
        catch(DataAccessException exception){
            String message = exception.getMessage();
            if(message.equals("Unauthorized")){
                res.type("application/json");
                res.status(401);
                var obj = Map.of(
                        "Message", message
                );
                var serializer = new Gson();
                return serializer.toJson(obj);
            }
            else{
                res.type("application/json");
                res.status(500);
                var obj = Map.of(
                        "Message", message
                );
                var serializer = new Gson();
                return serializer.toJson(obj);
            }
        }
    }

    private Object logout(Request req, Response res){
        String authToken = req.headers("authorization");
        userService.logout(authToken);
        res.type("application/json");
        res.status(200);
        return "{}";
    }

    private Object listGames(Request req, Response res){
        String authToken = req.headers("authorization");
        ArrayList<GameData> games = gameService.getGames(authToken);
        HashMap<String, Object> response = new HashMap<>();
        response.put("games", games);
        res.type("application/json");
        res.status(200);
        var serializer = new Gson();
        return serializer.toJson(response);    }

    private Object createGame(Request req, Response res){
        var body = new Gson().fromJson(req.body(), Map.class);
        String authToken = req.headers("authorization");
        String gameName = body.get("gameName").toString();
        String gameId = gameService.createGame(authToken,gameName);
        HashMap<String, Object> response = new HashMap<>();
        response.put("gameId",gameId);
        res.type("application/json");
        res.status(200);
        var serializer = new Gson();
        return serializer.toJson(response);
    }

    private Object joinGame(Request req, Response res){
        var body = new Gson().fromJson(req.body(), Map.class);
        String authToken = req.headers("authorization");
        String playerColor = body.get("playerColor").toString();
        String gameId = body.get("gameID").toString();
        gameService.joinGame(playerColor, gameId, authToken);
        res.type("application/json");
        res.status(200);
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
