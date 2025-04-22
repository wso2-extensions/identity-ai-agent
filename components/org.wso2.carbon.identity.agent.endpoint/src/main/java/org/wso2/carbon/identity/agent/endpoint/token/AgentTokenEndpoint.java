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

package org.wso2.carbon.identity.agent.endpoint.token;

import com.google.gson.JsonObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.agent.core.model.AgentTokenRequest;
import org.wso2.carbon.identity.agent.endpoint.exception.AgentAuthException;
import org.wso2.carbon.identity.agent.endpoint.util.EndpointUtil;
import org.wso2.carbon.identity.agent.endpoint.util.factory.OAuth2ServiceFactory;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth.dao.OAuthAppDO;
import org.wso2.carbon.identity.oauth2.ResponseHeader;
import org.wso2.carbon.identity.oauth2.bean.OAuthClientAuthnContext;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenReqDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenRespDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * REST implementation of the Agent Token Endpoint.
 */
@Path("/auth/token")
public class AgentTokenEndpoint {

    private static final String BEARER = "Bearer";
    private static final String PASSWORD_GRANT = "password";

    @POST
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response issueAccessToken(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                     MultivaluedMap<String, String> paramMap)
            throws AgentAuthException {

        AgentTokenRequest agentTokenRequest = new AgentTokenRequest(request);
        OAuth2AccessTokenReqDTO tokenReqDTO = buildAccessTokenReqDTO(agentTokenRequest);
        OAuth2AccessTokenRespDTO oauth2AccessTokenResp = OAuth2ServiceFactory.getOAuth2Service()
                .issueAccessToken(tokenReqDTO);

        if (oauth2AccessTokenResp.getErrorMsg() != null) {
            return handleErrorResponse(oauth2AccessTokenResp);
        } else {
            return buildTokenResponse(oauth2AccessTokenResp);
        }
    }

    /**
     * Build access token request.
     *
     * @param request AgentTokenRequest
     * @return OAuth2AccessTokenReqDTO
     * @throws AgentAuthException if error at request building
     */
    private OAuth2AccessTokenReqDTO buildAccessTokenReqDTO(AgentTokenRequest request) throws
            AgentAuthException {

        OAuth2AccessTokenReqDTO tokenReqDTO = new OAuth2AccessTokenReqDTO();
        OAuthClientAuthnContext oauthClientAuthnContext = new OAuthClientAuthnContext();
        oauthClientAuthnContext.setAuthenticated(true);
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();;
        OAuthAppDO oAuthAppDO = EndpointUtil.getAgentDefaultOAuthAppDO(tenantDomain);
        tokenReqDTO.setoAuthClientAuthnContext(oauthClientAuthnContext);
        tokenReqDTO.setGrantType(PASSWORD_GRANT);
        tokenReqDTO.setClientId(oAuthAppDO.getOauthConsumerKey());
        tokenReqDTO.setClientSecret(oAuthAppDO.getOauthConsumerSecret());
        tokenReqDTO.setScope(request.getScopes().toArray(new String[0]));

        tokenReqDTO.setResourceOwnerUsername(request.getAgentId());
        tokenReqDTO.setResourceOwnerPassword(request.getAgentSecret());

        tokenReqDTO.addAuthenticationMethodReference(PASSWORD_GRANT);
        return tokenReqDTO;
    }

    /**
     * Build token response.
     *
     * @param oauth2AccessTokenResp OAuth2AccessTokenRespDTO
     * @return Token Response
     */
    private Response buildTokenResponse(OAuth2AccessTokenRespDTO oauth2AccessTokenResp) {

        if (StringUtils.isBlank(oauth2AccessTokenResp.getTokenType())) {
            oauth2AccessTokenResp.setTokenType(BEARER);
        }

        JsonObject jsonBuilder = new JsonObject();
        jsonBuilder.addProperty("access_token", oauth2AccessTokenResp.getAccessToken());
        jsonBuilder.addProperty("expires_in", oauth2AccessTokenResp.getExpiresIn());
        jsonBuilder.addProperty("token_type", oauth2AccessTokenResp.getTokenType());

        if (StringUtils.isNotBlank(oauth2AccessTokenResp.getAuthorizedScopes())) {
            jsonBuilder.addProperty("scope", oauth2AccessTokenResp.getAuthorizedScopes());
        }

        if (oauth2AccessTokenResp.getIDToken() != null) {
            jsonBuilder.addProperty(OAuthConstants.ID_TOKEN, oauth2AccessTokenResp.getIDToken());
        }

        if (MapUtils.isNotEmpty(oauth2AccessTokenResp.getParameterObjects())) {
            oauth2AccessTokenResp.getParameterObjects().forEach((key, value) -> {
                if (value instanceof String) {
                    jsonBuilder.addProperty(key, (String) value);
                }
            });
        }

        ResponseHeader[] headers = oauth2AccessTokenResp.getResponseHeaders();
        Response.ResponseBuilder respBuilder = Response
                .status(HttpServletResponse.SC_OK)
                .header(OAuthConstants.HTTP_RESP_HEADER_CACHE_CONTROL,
                        OAuthConstants.HTTP_RESP_HEADER_VAL_CACHE_CONTROL_NO_STORE)
                .header(OAuthConstants.HTTP_RESP_HEADER_PRAGMA,
                        OAuthConstants.HTTP_RESP_HEADER_VAL_PRAGMA_NO_CACHE);

        if (headers != null) {
            for (ResponseHeader header : headers) {
                if (header != null) {
                    respBuilder.header(header.getKey(), header.getValue());
                }
            }
        }

        return respBuilder.entity(jsonBuilder.toString()).build();
    }

    private Response handleErrorResponse(OAuth2AccessTokenRespDTO oauth2AccessTokenResp) {

        JsonObject jsonBuilder = new JsonObject();
        jsonBuilder.addProperty("error", oauth2AccessTokenResp.getErrorCode());
        jsonBuilder.addProperty("error_description", oauth2AccessTokenResp.getErrorMsg());

        ResponseHeader[] headers = oauth2AccessTokenResp.getResponseHeaders();
        Response.ResponseBuilder respBuilder = Response.status(HttpServletResponse.SC_BAD_REQUEST);

        if (headers != null) {
            for (ResponseHeader header : headers) {
                if (header != null) {
                    respBuilder.header(header.getKey(), header.getValue());
                }
            }
        }

        return respBuilder.entity(jsonBuilder.toString()).build();
    }
}
