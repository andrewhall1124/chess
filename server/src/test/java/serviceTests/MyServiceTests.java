package serviceTests;
import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MyServiceTests {
    private final MemoryUserDAO userDao = new MemoryUserDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final GameService gameService = new GameService(gameDAO);
    private final UserService userService = new UserService(userDao, authDao);
    private final AuthService authService = new AuthService(authDao);

    @Test
    public void clearTest(){
        GameData newGame = new GameData("the hill");
        gameService.add(newGame);
        gameService.clear();
        assertEquals(new ArrayList<>(), gameService.get());
    }

    @Test
    public void registerSuccess(){
        String authToken = userService.register("Andrew", "Password", "andrew@me.com");
        String testToken = authDao.getToken("Andrew");
        assertEquals(testToken, authToken);
    }
}
