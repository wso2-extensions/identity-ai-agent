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

package org.wso2.carbon.identity.agent.core.model;

import org.wso2.carbon.identity.agent.core.util.AgentUtil;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import static org.wso2.carbon.identity.agent.core.constant.AgentConstant.AGENT_ID;
import static org.wso2.carbon.identity.agent.core.constant.AgentConstant.AGENT_SECRET;

/**
 * AgentTokenRequest holds all agent auth token request parameters.
 */
public class AgentTokenRequest {

    private final HttpServletRequest request;

    public AgentTokenRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getParam(String name) {

        return this.request.getParameter(name);
    }

    public String getAgentId() {

        String[] credentials = AgentUtil.decodeAuthorizationHeader(this.request.getHeader("Authorization"));
        return credentials != null ? credentials[0] : this.getParam(AGENT_ID);
    }

    public String getAgentSecret() {

        String[] credentials = AgentUtil.decodeAuthorizationHeader(this.request.getHeader("Authorization"));
        return credentials != null ? credentials[1] : this.getParam(AGENT_SECRET);
    }

    public Set<String> getScopes() {

        String scopes = this.getParam("scope");
        return AgentUtil.decodeScopes(scopes);
    }

}
