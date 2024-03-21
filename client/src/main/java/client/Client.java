package client;

import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.ServerFacade;

public class Client {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> list();
//                case "join" -> join(params);
//                case "observe" -> observe(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if (authToken == null) {
            return """
                    - help
                    - register <username> <password> <email>
                    - login <username> <password>
                    - quit
                    """;
        }
        return """
                - help
                - logout
                - create <name>
                - list
                - join  <ID> [BLACK | WHITE | empty]
                - observe <ID>
                - quit
                """;
    }

    public String register(String... params) throws ResponseException {
        if(params.length >= 3){
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse response = server.register(request);
            authToken = response.authToken();
            return "Successfully registered as " + response.username();
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }
    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResponse response = server.login(request);
            authToken = response.authToken();
            return String.format("You logged in as %s.", response.username());
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout(authToken);
        authToken = null;
        return "Logged out";
    }

    public String createGame(String ...params) throws ResponseException {
        assertSignedIn();
        if(params.length >= 1){
            CreateGameRequest request = new CreateGameRequest(params[0]);
            CreateGameResponse response = server.createGame(request, authToken);
            return String.format("Game ID: %s.", response.gameID());
        }
        throw new ResponseException(400, "Expected: <name>");
    }

    public String list() throws ResponseException{
        assertSignedIn();
        ListGamesResponse response = server.list(authToken);
        StringBuilder result = new StringBuilder();
        var count = 1;
        for(GameData game : response.games()){
            result.append(String.format("%s. ",count));
            result.append(String.format("%s\n",game.gameName()));
            result.append(String.format("    White: %s\n",game.whiteUsername()));
            result.append(String.format("    Black: %s\n",game.blackUsername()));
            result.append("\n");
            count ++;
        }
        return result.toString();
    }

    private void assertSignedIn() throws ResponseException {
        if (authToken == null) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
