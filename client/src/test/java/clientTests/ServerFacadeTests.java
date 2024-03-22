package clientTests;

import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        var localhostUrl = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + localhostUrl);
        facade = new ServerFacade(localhostUrl);
    }

    @AfterAll
    static void stopServer() throws ResponseException{
        facade.clear();
        server.stop();
    }

    @BeforeEach
    public void clear() throws ResponseException {
        facade.clear();
    }

    @Test
    public void clearPositive() throws ResponseException {
        RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
        RegisterResponse registerRes = facade.register(registerReq);
        CreateGameRequest gameReq = new CreateGameRequest("test_game");
        CreateGameResponse gameRes = facade.createGame(gameReq, registerRes.authToken());
        facade.clear();
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void registerPositive() throws Exception {
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        RegisterResponse response = facade.register(request);
        assertTrue(response.authToken().length() > 10);
    }
    @Test
    public void registerNegative(){
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            RegisterRequest badRequest = new RegisterRequest(null, null, null);
            RegisterResponse response = facade.register(badRequest);
        });
    }
    @Test
    void loginPositive() throws Exception {
        RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
        RegisterResponse registerRes = facade.register(registerReq);
        facade.logout(registerRes.authToken());
        LoginRequest loginReq = new LoginRequest("username", "password");
        LoginResponse loginRes = facade.login(loginReq);
        assertTrue(loginRes.authToken().length() > 10);
    }

    @Test
    public void loginNegative(){
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
            RegisterResponse registerRes = facade.register(registerReq);
            facade.logout(registerRes.authToken());
            LoginRequest loginReq = new LoginRequest("username", "badpassword");
            facade.login(loginReq);
        });
    }

    @Test
    void logoutPositive() throws Exception {
        RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
        RegisterResponse registerRes = facade.register(registerReq);
        facade.logout(registerRes.authToken());
    }

    @Test
    public void logoutNegative(){
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
            RegisterResponse registerRes = facade.register(registerReq);
            facade.logout("badAuthToken");
        });
    }

    @Test
    void createGamePositive() throws Exception {
        RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
        RegisterResponse registerRes = facade.register(registerReq);
        CreateGameRequest gameReq = new CreateGameRequest("test_game");
        CreateGameResponse gameRes = facade.createGame(gameReq, registerRes.authToken());
        assertNotNull(gameRes.gameID());
    }

    @Test
    public void createGameNegative(){
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
            RegisterResponse registerRes = facade.register(registerReq);
            CreateGameRequest gameReq = new CreateGameRequest("test_game");
            CreateGameResponse gameRes = facade.createGame(gameReq,"badAuthToken");
        });
    }

    @Test
    void listGamesPositive() throws Exception {
        RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
        RegisterResponse registerRes = facade.register(registerReq);
        CreateGameRequest gameReq = new CreateGameRequest("test_game");
        facade.createGame(gameReq, registerRes.authToken());
        facade.createGame(gameReq, registerRes.authToken());
        facade.createGame(gameReq, registerRes.authToken());
        ListGamesResponse listRes = facade.list(registerRes.authToken());
        assertEquals(3, listRes.games().size());
    }

    @Test
    public void listGamesNegative(){
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
            RegisterResponse registerRes = facade.register(registerReq);
            CreateGameRequest gameReq = new CreateGameRequest("test_game");
            facade.createGame(gameReq, registerRes.authToken());
            facade.createGame(gameReq, registerRes.authToken());
            facade.createGame(gameReq, registerRes.authToken());
            ListGamesResponse listRes = facade.list("badAuthToken");
        });
    }

    @Test
    void joinPositive() throws Exception {
        RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
        RegisterResponse registerRes = facade.register(registerReq);
        CreateGameRequest gameReq = new CreateGameRequest("test_game");
        CreateGameResponse gameRes = facade.createGame(gameReq, registerRes.authToken());
        JoinGameRequest joinReq = new JoinGameRequest("WHITE", gameRes.gameID());
        facade.join(joinReq, registerRes.authToken());
        ListGamesResponse listRes = facade.list(registerRes.authToken());
        assertEquals("username", listRes.games().get(0).whiteUsername());
    }

    @Test
    public void joinNegative(){
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            RegisterRequest registerReq = new RegisterRequest("username", "password", "email");
            RegisterResponse registerRes = facade.register(registerReq);
            CreateGameRequest gameReq = new CreateGameRequest("test_game");
            CreateGameResponse gameRes = facade.createGame(gameReq, registerRes.authToken());
            JoinGameRequest joinReq = new JoinGameRequest("WHITE", gameRes.gameID());
            facade.join(joinReq, registerRes.authToken());
            facade.join(joinReq, registerRes.authToken());
        });
    }
}
