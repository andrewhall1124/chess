package client;

import exception.ResponseException;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import server.ServerFacade;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PostLogin {
    private final ServerFacade server;
    private String authToken;
    private Game game;
    private HashMap<Integer, GameData> gameList = new HashMap<>();

    public PostLogin(String serverUrl){
        server = new ServerFacade(serverUrl);
        game = new Game(serverUrl);
    }

    public String run(String authToken){
        this.authToken = authToken;
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("Logged out")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        return "\n";
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - help
                - logout
                - create <name>
                - list
                - join  <ID> [BLACK | WHITE]
                - observe <ID>
                """;
    }
    public String logout() throws ResponseException {
        server.logout(authToken);
        authToken = null;
        return "Logged out";
    }

    public String createGame(String ...params) throws ResponseException {
        if(params.length >= 1){
            CreateGameRequest request = new CreateGameRequest(params[0]);
            CreateGameResponse response = server.createGame(request, authToken);
            return String.format("Game ID: %s.", response.gameID()) + '\n';
        }
        throw new ResponseException(400, "Expected: <name>");
    }

    public String list() throws ResponseException{
        gameList.clear();
        ListGamesResponse response = server.list(authToken);
        if(response.games().size() == 0){
            return "No current games\n";
        }
        else{
            StringBuilder result = new StringBuilder();
            var count = 1;
            for(GameData game : response.games()){
                gameList.put(count,game);
                result.append(String.format("%s. ",count));
                result.append(String.format("%s\n",game.gameName()));
                result.append(String.format("    White: %s\n",game.whiteUsername()));
                result.append(String.format("    Black: %s\n",game.blackUsername()));
                count ++;
            }
            return result.toString();
        }
    }

    public String join(String ...params) throws ResponseException{
        if(params.length >= 2){
            Integer gameListID = Integer.parseInt(params[0]);
            int gameID = gameList.get(gameListID).gameID();
            String teamColor = params[1].toUpperCase();
            System.out.println("Team color: " + teamColor);
            JoinGameRequest request = new JoinGameRequest(teamColor,gameID);
            server.join(request,authToken);
            GameData game = gameList.get(gameListID);
            System.out.println(BLUE + "Successfully joined " + game.gameName());
            return this.game.runAsPlayer(authToken, gameID, teamColor);
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE | BLACK | empty]");
    }

    public String observe(String ...params) throws ResponseException{
        if(params.length >= 1){
            Integer gameListID = Integer.parseInt(params[0]);
            int gameID = gameList.get(gameListID).gameID();
            JoinGameRequest request = new JoinGameRequest("",gameList.get(gameListID).gameID());
            server.join(request,authToken);
            GameData game = gameList.get(gameListID);
            System.out.println(BLUE + "Successfully observing " + game.gameName());
            return this.game.runAsObserver(authToken,gameID);
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
