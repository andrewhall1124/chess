package client;

import java.util.Arrays;
import exception.ResponseException;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;
import server.ServerFacade;
import static ui.EscapeSequences.*;

public class PreLogin {
    private final ServerFacade server;
    private final PostLogin postLogin;
    public PreLogin(String serverUrl){
        server = new ServerFacade(serverUrl);
        postLogin = new PostLogin(serverUrl);
    }

    public String eval(String input) throws ResponseException{
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - help
                - register <username> <password> <email>
                - login <username> <password>
                - quit
                """;
    }

    public String register(String... params) throws ResponseException {
        if(params.length >= 3){
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse response = server.register(request);
            System.out.println(BLUE + "Successfully registered as " + response.username());
            return postLogin.run(response.authToken());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }
    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResponse response = server.login(request);
            System.out.println(String.format(BLUE + "You logged in as %s.", response.username()));
            return postLogin.run(response.authToken());

        }
        throw new ResponseException(400, "Expected: <yourname>");
    }
}
