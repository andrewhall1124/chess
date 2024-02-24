package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class MemoryAuthDAO{
    private final ArrayList<AuthData> tokensList = new ArrayList<>();

    public void clearTokens() throws DataAccessException{
        tokensList.clear();
//        throw new DataAccessException("Failed to clear database");
    }

    public String addToken(String username){
        UUID uuid = UUID.randomUUID();
        AuthData newToken = new AuthData(username, uuid.toString());
        tokensList.add(newToken);
        return uuid.toString();
    }

    public String getToken(String username){
        for(AuthData token : tokensList){
            if(token.getUsername().equals(username)){
                return token.getAuthToken();
            }
        }
        return null;
    }

    public AuthData verifyToken(String authToken){
        for(AuthData token : tokensList){
            if(token.getAuthToken().equals(authToken)){
                return token;
            }
        }
        return null;
    }

    public String deleteToken(String authToken) {
        Iterator<AuthData> iterator = tokensList.iterator();
        while (iterator.hasNext()) {
            AuthData token = iterator.next();
            if (token.getAuthToken().equals(authToken)) {
                iterator.remove();
                return token.getUsername();
            }
        }
        return null;
    }
}
