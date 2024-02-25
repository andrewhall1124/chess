package serviceTests;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MyServiceTests {
    private final MemoryUserDAO userDao = new MemoryUserDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final GameService gameService = new GameService(gameDAO, authDao);
    private final UserService userService = new UserService(userDao, authDao);
    private final AuthService authService = new AuthService(authDao);

    @Test
    public void clearTest() throws DataAccessException {
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String gameId = gameService.createGame(authToken, "My Chess Game");
        userService.clear();
        gameService.clear();
        authService.clear();
        assertNull(userDao.getUser("Andrew"));
        assertNull(gameDAO.getGame(gameId));
        assertNull(authDao.verifyToken(authToken));
    }

    @Test
    public void registerSuccess() throws DataAccessException{
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String testToken = authDao.getToken("Andrew");
        assertEquals(testToken, authToken);
    }

    @Test
    public void registerFailure() throws DataAccessException{

    }

    @Test
    public void loginSuccess() throws DataAccessException{
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        userService.logout(authToken);
        HashMap<String, String> result = userService.login("Andrew", "Password");
        String testToken = authDao.getToken("Andrew");
        assertEquals(testToken,result.get("authToken"));
    }

    @Test
    public void loginFailure() {
        // Attempt to login with incorrect credentials
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userService.login("NonExistentUser", "IncorrectPassword");
        });
        String expectedMessage = "Unauthorized";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void logoutSuccess() throws DataAccessException{
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        userService.logout(authToken);
        String testToken = authDao.getToken("Andrew");
        assertNull(testToken);
    }

    @Test
    public void logoutFailure() throws DataAccessException{
        // Attempt to logout with bad authToken
        userService.register("Andrew","Password","andrew@me.com");
        String randomToken = UUID.randomUUID().toString();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userService.logout(randomToken);
        });
        String expectedMessage = "Unauthorized";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void createGameSuccess() throws DataAccessException{
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String gameId = gameService.createGame(authToken, "My Chess Game");
        GameData testGame = gameDAO.getGame(gameId);
        assertEquals("My Chess Game", testGame.getName());
    }

    @Test
    public void createGameFailure() throws DataAccessException{
        // Attempt to create a game that has already been made
        String authToken = userService.register("Andrew","Password","andrew@me.com");
        gameService.createGame(authToken, "New Game");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame(authToken, "New Game");
        });
        String expectedMessage = "Bad request";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void joinGameSuccess() throws DataAccessException{
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String gameId = gameService.createGame(authToken, "My Chess Game");
        gameService.joinGame("WHITE", gameId, authToken);
        GameData testGame = gameDAO.getGame(gameId);
        assertEquals("Andrew", testGame.getWhiteUserName());
    }

    @Test
    public void joinGameFailure() throws DataAccessException{
        // Attempt to join a game that already has a white user
        String authToken1 = userService.register("Andrew","Password","andrew@me.com");
        String gameId = gameService.createGame(authToken1, "New Game");
        gameService.joinGame("WHITE", gameId, authToken1);
        String authToken2 = userService.register("Wannabe", "12341234", "noob@me.com");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame("WHITE", gameId, authToken2);
        });
        String expectedMessage = "Already taken";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
