package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import webSocket.WebSocketHandler;

public class Server{
    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();
    private final GameService gameService = new GameService();
    private final WebSocketHandler ws = new WebSocketHandler();


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

        Spark.webSocket("/connect", ws);

        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.post("/session", this::loginHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clearHandler(Request req, Response res){
        Gson gson = new Gson();
        try{
            gameService.clear();
            authService.clear();
            userService.clear();
            res.status(200);
            return "{}";
        }
        catch(DataAccessException exception){
            ErrorResponse response = new ErrorResponse(exception.getMessage());
            res.status(getStatus(exception.getMessage()));
            return gson.toJson(response, ErrorResponse.class);
        }

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

    private Object logoutHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try{
            authService.logout(authToken);
            res.status(200);
            return "{}";
        }
        catch(DataAccessException exception){
            ErrorResponse response = new ErrorResponse(exception.getMessage());
            res.status(getStatus(exception.getMessage()));
            return gson.toJson(response, ErrorResponse.class);
        }
    }

    private Object loginHandler(Request req, Response res){
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        try{
            String username = userService.login(request);
            String authToken = authService.login(request);
            LoginResponse response = new LoginResponse(username,authToken);
            res.status(200);
            return gson.toJson(response, LoginResponse.class);
        }
        catch(DataAccessException exception){
            ErrorResponse response = new ErrorResponse(exception.getMessage());
            res.status(getStatus(exception.getMessage()));
            return gson.toJson(response, ErrorResponse.class);
        }
    }
    private Object createGameHandler(Request req, Response res){
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("authorization");
        try{
            authService.verify(authToken);
            CreateGameResponse response = gameService.createGame(request);
            res.status(200);
            return gson.toJson(response, CreateGameResponse.class);
        }
        catch(DataAccessException exception){
            ErrorResponse response = new ErrorResponse(exception.getMessage());
            res.status(getStatus(exception.getMessage()));
            return gson.toJson(response, ErrorResponse.class);
        }
    }

    private Object listGamesHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try{
            authService.verify(authToken);
            ListGamesResponse response = gameService.listGames();
            res.status(200);
            return gson.toJson(response, ListGamesResponse.class);
        }
        catch(DataAccessException exception){
            ErrorResponse response = new ErrorResponse(exception.getMessage());
            res.status(getStatus(exception.getMessage()));
            return gson.toJson(response, ErrorResponse.class);
        }
    }

    private Object joinGameHandler(Request req, Response res){
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("authorization");
        try{
            String username = authService.getUsername(authToken);
            gameService.joinGame(request, username);
            res.status(200);
            return "{}";
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
