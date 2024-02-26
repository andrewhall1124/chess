package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO{
    private final HashMap<String, AuthData> authMap = new HashMap<>();

    public void createAuth(String username){
        UUID uuid = UUID.randomUUID();
        AuthData newAuth = new AuthData(username, uuid.toString());
        authMap.put(uuid.toString(), newAuth);
    }

    public AuthData readAuth(String authToken){
        return authMap.get(authToken);
    }

    public void deleteAuth(String authToken){
        authMap.remove(authToken);
    }
}
