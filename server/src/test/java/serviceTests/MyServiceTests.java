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

import static org.junit.jupiter.api.Assertions.*;

public class MyServiceTests {
    private final GameService gameService = new GameService(new MemoryGameDAO());
    private final UserService userService = new UserService(new MemoryUserDAO());
    private final AuthService authService = new AuthService(new MemoryAuthDAO());

    @Test
    public void clearTest(){
        ChessGame chessGame = new ChessGame();
        GameData newGame = new GameData(7,"jack", "jill", "the hill",chessGame);
        gameService.add(newGame);
        gameService.clear();
        assertEquals(new ArrayList<>(), gameService.get());
    }
}
