package com.omnet.cnt.classes;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenManager {

    private final Map<String, PasswordTokenInfo> tokenMap = new ConcurrentHashMap<>();

    public String storePasswordToken(String userId, String token) {
        tokenMap.put(userId, new PasswordTokenInfo(token, System.currentTimeMillis()));
        System.out.println("Token stored for username: " + userId + " token: " + token); // Debug log
        return token;
    }

    public String getuserIdForPasswordToken(String token) {
        int totalTokens = tokenMap.size();
        System.out.println("Total tokens present: " + totalTokens);
        for (Map.Entry<String, PasswordTokenInfo> entry : tokenMap.entrySet()) {
            String userId = entry.getKey();
            String Token = entry.getValue().getToken();
            System.out.println("Usernameetet: " + userId + ", Tokenerete: " + Token);
        }
        for (Map.Entry<String, PasswordTokenInfo> entry : tokenMap.entrySet()) {
            if (entry.getValue().getToken().equals(token)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean isValidPasswordToken(String token) {
        for (Map.Entry<String, PasswordTokenInfo> entry : tokenMap.entrySet()) {
            PasswordTokenInfo tokenInfo = entry.getValue();
            if (tokenInfo.getToken().equals(token)) {
                if (System.currentTimeMillis() - tokenInfo.getCreationTime() > 300000) {
                    tokenMap.remove(entry.getKey());
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public void invalidatePasswordToken(String token) {
        for (Iterator<Map.Entry<String, PasswordTokenInfo>> iterator = tokenMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, PasswordTokenInfo> entry = iterator.next();
            if (entry.getValue().getToken().equals(token)) {
                iterator.remove();
                break;
            }
        }
    }

    private static class PasswordTokenInfo {
        private String token;
        private long creationTime;

        public PasswordTokenInfo(String token, long creationTime) {
            this.token = token;
            this.creationTime = creationTime;
        }

        public String getToken() {
            return token;
        }

        public long getCreationTime() {
            return creationTime;
        }
    }
}
