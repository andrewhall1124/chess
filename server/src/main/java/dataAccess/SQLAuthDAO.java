package dataAccess;
import model.AuthData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public String createAuth(String username) throws DataAccessException {
        UUID uuid = UUID.randomUUID();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO auth (username,authToken) VALUES (?, ?)";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
                return uuid.toString();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM auth WHERE authToken = ?";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1,authToken);
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                    return new AuthData(
                            rs.getString("username"),
                            rs.getString("authToken")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM auth WHERE authToken = ?";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1,authToken);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAllAuth() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM auth";
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
