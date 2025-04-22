package org.wso2.carbon.identity.agent.core.util;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Utility class for handling agent-related operations.
 */
public class AgentUtil {

    /**
     * Decodes the Authorization header.
     *
     * @param authorizationHeader The Authorization header value
     * @return A string array with [0]=agentId and [1]=agentSecret, or null if invalid
     */
    public static String[] decodeAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            return null;
        }
        try {
            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            return credentials.split(":", 2);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Decodes space-separated scopes into a Set.
     *
     * @param scopes Space-separated scope string
     * @return A Set of scope strings
     */
    public static Set<String> decodeScopes(String scopes) {
        if (scopes == null || scopes.isEmpty()) {
            return java.util.Collections.emptySet();
        }

        String[] scopesArray = scopes.split("\\s+");
        return new java.util.HashSet<>(java.util.Arrays.asList(scopesArray));
    }

}
