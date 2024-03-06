package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MyDataAccessTest {
    private final SQLUserDAO userDAO = new SQLUserDAO();
    private final SQLGameDAO gameDAO = new SQLGameDAO();
    private final SQLAuthDAO authDAO = new SQLAuthDAO();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    UserData userData = new UserData("andrew", "password", "andrew@me.com");
    UserData hashedUserData = new UserData("andrew", encoder.encode("password"),"andrew@me.com");
    GameData gameOne = new GameData(1,"white","black","chess game", new ChessGame());
    GameData gameTwo = new GameData(2,"white","black","chess game", new ChessGame());
    GameData gameThree = new GameData(3,"white","black","chess game", new ChessGame());


    @BeforeEach
    public void setup() throws DataAccessException{
        userDAO.deleteAllUsers();
        gameDAO.deleteAllGames();
        authDAO.deleteAllAuth();
    }

    @Test
    public void positiveCreateUser() throws DataAccessException{
        userDAO.createUser(userData);
    }

    @Test
    public void positiveReadUser() throws DataAccessException{
        userDAO.createUser(userData);
        UserData resUser = userDAO.readUser(userData.username());
    }

    @Test
    public void positiveDeleteAllUsers() throws DataAccessException{
        userDAO.createUser(userData);
        userDAO.deleteAllUsers();
    }

    @Test
    public void positiveCreateGame() throws DataAccessException{
        gameDAO.createGame(gameOne);
    }

    @Test
    public void positiveReadGame() throws DataAccessException{
        gameDAO.createGame(gameOne);
        GameData resGame = gameDAO.readGame(gameOne.gameID());
        assertEquals(gameOne, resGame);
    }

    @Test
    public void positiveDeleteGame() throws DataAccessException{
        gameDAO.createGame(gameOne);
        gameDAO.deleteGame(gameOne.gameID());
    }

    @Test
    public void positiveReadAllGames() throws DataAccessException{
        gameDAO.createGame(gameOne);
        gameDAO.createGame(gameTwo);
        gameDAO.createGame(gameThree);
        ArrayList<GameData> resGameList = gameDAO.readAllGames();
        ArrayList<GameData> gameList = new ArrayList<>();
        gameList.add(gameOne);
        gameList.add(gameTwo);
        gameList.add(gameThree);
        assertEquals(gameList,resGameList);
    }

    @Test
    public void positiveDeleteAllGames() throws DataAccessException{
        gameDAO.createGame(gameOne);
        gameDAO.deleteAllGames();
    }

    @Test
    public void positiveCreateAuth() throws DataAccessException{
        authDAO.createAuth(userData.username());
    }

    @Test
    public void positiveReadAuth() throws DataAccessException{
        String authToken = authDAO.createAuth(userData.username());
        AuthData authData = authDAO.readAuth(authToken);
        assertEquals(userData.username(), authData.username());
    }

    @Test
    public void positiveDeleteAuth() throws DataAccessException{
        String authToken = authDAO.createAuth(userData.username());
        authDAO.deleteAuth(authToken);
    }

    @Test
    public void positiveDeleteAllAuth() throws DataAccessException{
        authDAO.createAuth(userData.username());
        authDAO.deleteAllAuth();
    }

    @Test
    public void badCreateUser() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(userData);
            userDAO.createUser(userData);
        });
    }

    @Test
    public void badReadUser() throws DataAccessException{
        UserData userData = userDAO.readUser("bad username");
        assertNull(userData);
    }

    @Test
    public void badCreateAuth() throws DataAccessException{
        authDAO.createAuth(userData.username());
    }

    @Test
    public void badReadAuth() throws DataAccessException{
        AuthData authData = authDAO.readAuth("bad token");
        assertNull(authData);
    }

    @Test
    public void badCreateGame() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(gameOne);
            gameDAO.createGame(gameOne);
        });
    }
    @Test
    public void badReadGame() throws DataAccessException{
        GameData gameData = gameDAO.readGame(1);
        assertNull(gameData);
    }

    @Test
    public void badReadAllGames() throws DataAccessException {
        ArrayList<GameData> resGameList = gameDAO.readAllGames();
        ArrayList<GameData> gameList = new ArrayList<>();
        assertEquals(gameList,resGameList);
    }

}
