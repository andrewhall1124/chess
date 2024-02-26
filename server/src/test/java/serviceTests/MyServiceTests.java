package serviceTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import service.AuthService;
import service.GameService;
import service.UserService;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class MyServiceTests {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final AuthService authService = new AuthService();

    RegisterRequest registerRequest = new RegisterRequest("Andrew", "password", "andrew@me.com");
    CreateGameRequest createGameRequest = new CreateGameRequest("new chess game");
    LoginRequest loginRequest = new LoginRequest("Andrew", "password");
    @Test
    public void clearTest() throws DataAccessException {
        userService.register(registerRequest);
        authService.register(registerRequest);
        gameService.createGame(createGameRequest);
        userService.clear();
        authService.clear();
        gameService.clear();
    }

    @Test
    public void goodRegisterTest() throws DataAccessException{
        String username = userService.register(registerRequest);
        String authToken = authService.register(registerRequest);
    }

    @Test
    public void badRegisterTest(){
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            RegisterRequest badRequest = new RegisterRequest(null, null, null);
            String username = userService.register(badRequest);
            String authToken = authService.register(badRequest);
        });
        assertTrue(exception.getMessage().toLowerCase().contains("bad request"));
    }
    @Test
    public void goodLogoutTest() throws DataAccessException{
        String username = userService.register(registerRequest);
        String authToken = authService.register(registerRequest);
        authService.logout(authToken);
    }

    @Test
    public void badLogoutTest(){
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            String username = userService.register(registerRequest);
            String authToken = authService.register(registerRequest);
            String badToken = UUID.randomUUID().toString();
            authService.logout(badToken);
        });
        assertTrue(exception.getMessage().toLowerCase().contains("unauthorized"));
    }
    @Test
    public void goodLoginTest() throws DataAccessException{
        String username = userService.register(registerRequest);
        String authToken = authService.register(registerRequest);
        userService.login(loginRequest);
        authService.login(loginRequest);
    }

    @Test
    public void badLoginTest(){
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            String username = userService.register(registerRequest);
            String authToken = authService.register(registerRequest);
            LoginRequest badLoginRequest = new LoginRequest("Not Andrew", "wrong password");
            userService.login(badLoginRequest);
            authService.login(badLoginRequest);
        });
        assertTrue(exception.getMessage().toLowerCase().contains("unauthorized"));
    }
    @Test
    public void goodCreateGameTest() throws DataAccessException{
        String username = userService.register(registerRequest);
        String authToken = authService.register(registerRequest);
        authService.verify(authToken);
        gameService.createGame(createGameRequest);
    }

    @Test
    public void badCreateGameTest(){
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            String username = userService.register(registerRequest);
            String authToken = authService.register(registerRequest);
            CreateGameRequest badCreateGame = new CreateGameRequest(null);
            gameService.createGame(badCreateGame);
        });
        assertTrue(exception.getMessage().toLowerCase().contains("bad request"));
    }
    @Test
    public void goodListGamesTest() throws DataAccessException{
        String username = userService.register(registerRequest);
        String authToken = authService.register(registerRequest);
        authService.verify(authToken);
        gameService.createGame(createGameRequest);
        gameService.createGame(createGameRequest);
        gameService.createGame(createGameRequest);
        gameService.listGames();
    }

    @Test
    public void badListGamesTest(){
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            String username = userService.register(registerRequest);
            String authToken = authService.register(registerRequest);
            String badToken = UUID.randomUUID().toString();
            authService.verify(badToken);
            gameService.createGame(createGameRequest);
            gameService.createGame(createGameRequest);
            gameService.createGame(createGameRequest);
            gameService.listGames();
        });
        assertTrue(exception.getMessage().toLowerCase().contains("unauthorized"));
    }
    @Test
    public void goodJoinGameTest() throws DataAccessException{
        String username = userService.register(registerRequest);
        String authToken = authService.register(registerRequest);
        authService.verify(authToken);
        CreateGameResponse response = gameService.createGame(createGameRequest);
        Integer gameID = response.gameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest("Andrew", gameID);
        gameService.joinGame(joinGameRequest, "Andrew");
    }

    @Test
    public void badJoinGameTest(){
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            String username = userService.register(registerRequest);
            String authToken = authService.register(registerRequest);
            authService.verify(authToken);
            CreateGameResponse response = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest("Andrew", 2345679);
            gameService.joinGame(joinGameRequest, "Andrew");
        });
        assertTrue(exception.getMessage().toLowerCase().contains("bad request"));
    }
}
