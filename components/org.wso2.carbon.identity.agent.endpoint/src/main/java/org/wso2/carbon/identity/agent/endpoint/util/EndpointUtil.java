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

package org.wso2.carbon.identity.agent.endpoint.util;

import org.wso2.carbon.identity.agent.endpoint.exception.AgentAuthException;
import org.wso2.carbon.identity.oauth.common.exception.InvalidOAuthClientException;
import org.wso2.carbon.identity.oauth.dao.OAuthAppDO;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;

/**
 * This class provides utility methods for the agent endpoint.
 */
public class EndpointUtil {

    private static final String AGENT_DEFAULT_CLIENT = "r93jv2sDSbz0g_C4kfc0hUe11fca";

    public static OAuthAppDO getAgentDefaultOAuthAppDO(String tenantDomain) throws AgentAuthException {

        try {
            return OAuth2Util.getAppInformationByClientId(AGENT_DEFAULT_CLIENT, tenantDomain);
        } catch (IdentityOAuth2Exception | InvalidOAuthClientException e) {
            throw new AgentAuthException("Error while retrieving the default OAuth app for agent.", e);
        }
    }
}
