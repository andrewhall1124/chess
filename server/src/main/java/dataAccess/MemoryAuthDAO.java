package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO{
    private final HashMap<String, AuthData> authMap = new HashMap<>();

    public String createAuth(String username){
        UUID uuid = UUID.randomUUID();
        AuthData auth = new AuthData(username, uuid.toString());
        authMap.put(uuid.toString(), auth);
        return uuid.toString();
    }

    public AuthData readAuth(String authToken){
        return authMap.get(authToken);
    }

    public void updateAuth(AuthData auth){
        authMap.remove(auth.authToken());
        authMap.put(auth.username(),auth);
    }

    public void deleteAuth(String authToken){
        authMap.remove(authToken);
    }
}
