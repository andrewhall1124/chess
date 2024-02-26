package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.RegisterRequest;
import response.ErrorResponse;
import response.RegisterResponse;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();
    private final GameService gameService = new GameService();

    private int getStatus(String message){
        switch (message){
            case "error: bad request": return 400;
            case "error: unauthorized": return 401;
            case "error: already taken": return 403;
            default: return 500;
        }
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

//        Spark.delete("/db", this::clear);
        Spark.post("/user", this::registerHandler);
//        Spark.post("/session", this::login);
//        Spark.delete("/session", this::logout);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerHandler(Request req, Response res){
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        try{
            String username = userService.register(request);
            String authToken = authService.register(request);
            RegisterResponse response = new RegisterResponse(username,authToken);
            res.status(200);
            return gson.toJson(response, RegisterResponse.class);
        }
        catch(DataAccessException exception){
            ErrorResponse response = new ErrorResponse(exception.getMessage());
            res.status(getStatus(exception.getMessage()));
            return gson.toJson(response, ErrorResponse.class);
        }
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
