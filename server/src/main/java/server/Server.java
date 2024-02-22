package server;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import service.UserService;
import service.GameService;
import service.AuthService;
import spark.*;

public class Server {
    private final GameService gameService = new GameService(new MemoryGameDAO());
    private final UserService userService = new UserService(new MemoryUserDAO());
    private final AuthService authService = new AuthService(new MemoryAuthDAO());

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);


        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request req, Response res) {
        System.out.println("Clear endpoint hit");
        gameService.clear();
        userService.clear();
        authService.clear();
        Gson gson = new Gson();
        String response = gson.toJson("Hello world");
        res.type("application/json");
        res.status(200);
        return response;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}