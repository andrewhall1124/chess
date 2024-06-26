package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    public void createGame(GameData game) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO game (gameID,whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
            try (var statement = conn.prepareStatement(sql)) {
                String json = new Gson().toJson(game.game());
                statement.setString(1, game.gameID().toString());
                statement.setString(2, game.whiteUsername());
                statement.setString(3, game.blackUsername());
                statement.setString(4, game.gameName());
                statement.setString(5, json);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData readGame(Integer gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM game WHERE gameID = ?";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1,gameID.toString());
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                    ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    return new GameData(
                            Integer.parseInt(rs.getString("gameID")),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"),
                           chessGame
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteGame(Integer gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM game WHERE gameID = ?";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1,gameID.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ArrayList<GameData> readAllGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM game";
            try (var statement = conn.prepareStatement(sql)) {
                ResultSet rs = statement.executeQuery();
                ArrayList<GameData> gameList = new ArrayList<>();
                while(rs.next()){
                    ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    gameList.add(new GameData(
                            Integer.parseInt(rs.getString("gameID")),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"),
                            chessGame
                    ));
                }
                if(!gameList.isEmpty()){
                    return gameList;
                }
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAllGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM game";
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE game SET game = ? WHERE gameID = ?";
            try (var statement = conn.prepareStatement(sql)) {
                String json = new Gson().toJson(game);
                statement.setString(1, json);
                statement.setInt(2, gameID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteWhiteUsername(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE game SET whiteUsername = NULL WHERE gameID = ?";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setInt(1, gameID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteBlackUsername(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE game SET blackUsername = NULL WHERE gameID = ?";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setInt(1, gameID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
