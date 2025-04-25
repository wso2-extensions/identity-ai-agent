package org.wso2.carbon.identity.api.agent.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.agent.v1.Agent;
import org.wso2.carbon.identity.api.agent.v1.AgentCreateRequest;
import org.wso2.carbon.identity.api.agent.v1.common.AgentConstants;
import org.wso2.carbon.identity.api.agent.v1.error.APIError;
import org.wso2.carbon.identity.api.agent.v1.error.ErrorResponse;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.model.UniqueIDUserClaimSearchEntry;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.agent.v1.common.AgentConstants.ErrorMessage.SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER;

/**
 * Service class to interact with the UserStoreManager for agent-related
 * operations.
 */
public class AgentService {

    private final RealmService realmService;

    private static final Log log = LogFactory.getLog(AgentService.class);

    private static final Integer DEFAULT_LIMIT = 20;

    private static final Integer DEFAULT_OFFSET = 0;

    public AgentService(RealmService realmService) {

        this.realmService = realmService;
    }

    /**
     * Retrieve a list of agents from the AGENT secondary user store.
     *
     * @param limit  The maximum number of agents to return.
     * @param offset The starting point for the list of agents to return.
     * @return List of agents.
     */
    public List<Agent> getAgents(Integer limit, Integer offset) {
        int effectiveLimit = (limit == null || limit <= 0) ? DEFAULT_LIMIT : limit;
        int effectiveOffset = (offset == null || offset < 0) ? DEFAULT_OFFSET : offset;
        List<Agent> agents = new ArrayList<>();
        List<String> claimURIList = List.of(
                "http://wso2.org/claims/displayName",
                "http://wso2.org/claims/created",
                "http://wso2.org/claims/metadata.version");
        try {
            AbstractUserStoreManager agentStoreManager = getAgentStoreManager();
            List<org.wso2.carbon.user.core.common.User> users = agentStoreManager.listUsersWithID("*", effectiveLimit,
                    effectiveOffset);
            List<String> userIDs = new ArrayList<>();
            for (org.wso2.carbon.user.core.common.User user : users) {
                userIDs.add(user.getUserID());
            }
            List<UniqueIDUserClaimSearchEntry> searchEntries = agentStoreManager.getUsersClaimValuesWithID(userIDs,
                    claimURIList, null);
            for (org.wso2.carbon.user.core.common.User user : users) {
                Map<String, String> userClaimValues = new HashMap<>();
                for (UniqueIDUserClaimSearchEntry entry : searchEntries) {
                    if (entry.getUser() != null && StringUtils.isNotBlank(entry.getUser().getUserID())
                            && entry.getUser().getUserID().equals(user.getUserID())) {
                        userClaimValues = entry.getClaims();
                        break;
                    }
                }
                Agent agent = new Agent();
                agent.setId(user.getUserID());
                agent.setName(userClaimValues.get("http://wso2.org/claims/displayName"));
                agent.setCreatedAt(userClaimValues.get("http://wso2.org/claims/created"));
                agent.setVersion(userClaimValues.get("http://wso2.org/claims/metadata.version"));
                agents.add(agent);
            }
        } catch (UserStoreException e) {
            throw handleException(e, SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER);
        }
        return agents;
    }

    /**
     * Create a new agent in the AGENT secondary user store with a generated
     * credential.
     *
     * @param agentCreateRequest The request containing agent details.
     * @return The created Agent object.
     */
    public Agent createAgent(AgentCreateRequest agentCreateRequest) {
        String agentId = java.util.UUID.randomUUID().toString();
        String generatedPassword = java.util.UUID.randomUUID().toString();
        try {
            AbstractUserStoreManager agentStoreManager = getAgentStoreManager();
            Map<String, String> claims = new HashMap<>();
            claims.put("http://wso2.org/claims/displayName", agentCreateRequest.getName());
            claims.put("http://wso2.org/claims/description", agentCreateRequest.getDescription());
            claims.put("http://wso2.org/claims/metadata.version", agentCreateRequest.getVersion());
            if (agentCreateRequest.getUrl() != null) {
                claims.put("http://wso2.org/claims/url", agentCreateRequest.getUrl());
            }
            if (agentCreateRequest.getOwner() != null) {
                claims.put("http://wso2.org/claims/owner", agentCreateRequest.getOwner());
            }
            String createdAt = java.time.Instant.now().toString();
            claims.put("http://wso2.org/claims/created", createdAt);
            agentStoreManager.addUserWithID(agentId, generatedPassword, null, claims, null);
            Agent agent = new Agent();
            agent.setId(agentId);
            agent.setName(agentCreateRequest.getName());
            agent.setDescription(agentCreateRequest.getDescription());
            agent.setVersion(agentCreateRequest.getVersion());
            agent.setUrl(agentCreateRequest.getUrl());
            agent.setOwner(agentCreateRequest.getOwner());
            agent.setCreatedAt(createdAt);
            return agent;
        } catch (UserStoreException e) {
            throw handleException(e, SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER);
        }
    }

    /**
     * Get the AGENT secondary user store manager.
     */
    private AbstractUserStoreManager getAgentStoreManager() throws UserStoreException {
        AbstractUserStoreManager userStoreManager = (AbstractUserStoreManager) realmService
                .getTenantUserRealm(IdentityTenantUtil.getTenantId(getTenantDomain()))
                .getUserStoreManager();
        if (userStoreManager == null) {
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR, SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER);
        }
        AbstractUserStoreManager agentStoreManager = (AbstractUserStoreManager) userStoreManager
                .getSecondaryUserStoreManager("AGENT");
        if (agentStoreManager == null) {
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR, SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER);
        }
        return agentStoreManager;
    }

    private String getTenantDomain() {

        return IdentityTenantUtil.resolveTenantDomain();
    }

    /**
     * Handle Exceptions.
     *
     * @param e         Exception
     * @param errorEnum Error message enum.
     * @return An APIError.
     */
    private APIError handleException(Exception e, AgentConstants.ErrorMessage errorEnum, String... data) {

        ErrorResponse errorResponse;
        if (data != null) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, String.format(errorEnum.getDescription(),
                    (Object[]) data));
        } else {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
        }

        if (e instanceof UserStoreException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else {
            return new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
    }

    /**
     * Handle User errors.
     *
     * @param status Http status.
     * @param error  Error .
     * @return An APIError.
     */
    private APIError handleError(Response.Status status, AgentConstants.ErrorMessage error) {

        return new APIError(status, getErrorBuilder(error).build());
    }

    /**
     * Get ErrorResponse Builder for Error enum.
     *
     * @param errorEnum Error message enum.
     * @return Error response for the given errorEnum.
     */
    private ErrorResponse.Builder getErrorBuilder(AgentConstants.ErrorMessage errorEnum) {

        return new ErrorResponse.Builder().withCode(errorEnum.getCode()).withMessage(errorEnum.getMessage())
                .withDescription(errorEnum.getDescription());
    }

}
