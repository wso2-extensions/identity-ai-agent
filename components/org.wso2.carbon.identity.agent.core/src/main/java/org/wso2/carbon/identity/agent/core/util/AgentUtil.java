/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
