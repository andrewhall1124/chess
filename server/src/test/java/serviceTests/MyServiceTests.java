package serviceTests;
import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class MyServiceTests {
    private final MemoryUserDAO userDao = new MemoryUserDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final GameService gameService = new GameService(gameDAO, authDao);
    private final UserService userService = new UserService(userDao, authDao);
    private final AuthService authService = new AuthService(authDao);

    @Test
    public void clearTest(){
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
    public void registerSuccess(){
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String testToken = authDao.getToken("Andrew");
        assertEquals(testToken, authToken);
    }

    @Test
    public void loginSuccess(){
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        userService.logout(authToken);
        HashMap<String, String> result = userService.login("Andrew", "Password");
        String testToken = authDao.getToken("Andrew");
        assertEquals(testToken,result.get("authToken"));
    }

    @Test
    public void logoutSuccess(){
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        userService.logout(authToken);
        String testToken = authDao.getToken("Andrew");
        assertNull(testToken);
    }

    @Test
    public void createGameSuccess(){
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String gameId = gameService.createGame(authToken, "My Chess Game");
        GameData testGame = gameDAO.getGame(gameId);
        assertEquals("My Chess Game", testGame.getName());
    }

    @Test
    public void joinGameSuccess(){
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String gameId = gameService.createGame(authToken, "My Chess Game");
        gameService.joinGame(ChessGame.TeamColor.WHITE, gameId, authToken);
        GameData testGame = gameDAO.getGame(gameId);
        assertEquals("Andrew", testGame.getWhiteUserName());
    }
}
