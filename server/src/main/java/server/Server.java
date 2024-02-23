package server;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import service.UserService;
import service.GameService;
import service.AuthService;
import spark.*;

import java.util.Map;

public class Server {
    private final MemoryUserDAO userDao = new MemoryUserDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final GameService gameService = new GameService(gameDAO);
    private final UserService userService = new UserService(userDao, authDao);
    private final AuthService authService = new AuthService(authDao);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/login", this::login);
        Spark.delete("/logout", this::logout);
        Spark.get("/game", this::listGames);
        Spark.delete("/game", this::createGame);
        Spark.delete("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request req, Response res) {
        gameService.clear();
        userService.clear();
        authService.clear();
        Gson gson = new Gson();
        res.type("application/json");
        res.status(200);
        return gson;
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
        return new Gson();
    }

    private Object logout(Request req, Response res){
        return new Gson();
    }

    private Object listGames(Request req, Response res){
        return new Gson();
    }

    private Object createGame(Request req, Response res){
        return new Gson();
    }

    private Object joinGame(Request req, Response res){
        return new Gson();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
