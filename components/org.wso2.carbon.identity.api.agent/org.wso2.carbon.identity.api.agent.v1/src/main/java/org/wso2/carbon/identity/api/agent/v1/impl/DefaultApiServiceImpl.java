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

package org.wso2.carbon.identity.api.agent.v1.impl;

import org.wso2.carbon.identity.api.agent.v1.Agent;
import org.wso2.carbon.identity.api.agent.v1.AgentCreateRequest;
import org.wso2.carbon.identity.api.agent.v1.AgentUpdateRequest;
import org.wso2.carbon.identity.api.agent.v1.CredentialBase;
import org.wso2.carbon.identity.api.agent.v1.DefaultApiService;
import org.wso2.carbon.identity.api.agent.v1.core.AgentService;
import org.wso2.carbon.identity.api.agent.v1.factories.AgentApiServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the DefaultApiService interface, providing methods to manage agents and their credentials.
 */
public class DefaultApiServiceImpl implements DefaultApiService {


    private final AgentService agentService;

    public DefaultApiServiceImpl() {

        try {
            this.agentService = AgentApiServiceFactory.getAgentService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating UserAssociationService.", e);
        }
    }

    /**
     * Adds a credential for the specified agent.
     *
     * @param agentId        The unique identifier of the agent.
     * @param credentialBase The credential details to be added.
     * @return A Response indicating the result of the operation.
     */
    @Override
    public Response addAgentCredential(String agentId, CredentialBase credentialBase) {
        // Implementation here
        return Response.ok().entity("magic!").build();
    }

    /**
     * Creates a new agent with the provided details.
     *
     * @param agentCreateRequest The request containing details for creating the agent.
     * @return A Response indicating the result of the operation.
     */
    @Override
    public Response createAgent(AgentCreateRequest agentCreateRequest) {
        // Call AgentService to create the agent and return the created Agent object
        Agent createdAgent = agentService.createAgent(agentCreateRequest);
        return Response.status(Response.Status.CREATED).entity(createdAgent).build();
    }

    /**
     * Deletes the agent with the specified ID.
     *
     * @param agentId The unique identifier of the agent to be deleted.
     * @return A Response indicating the result of the operation.
     */
    @Override
    public Response deleteAgent(String agentId) {
        // Implementation here
        return Response.ok().entity("magic!").build();
    }

    /**
     * Retrieves the details of an agent by their unique identifier.
     *
     * @param agentId The unique identifier of the agent.
     * @return A Response containing the agent details.
     */
    @Override
    public Response getAgentById(String agentId) {
        // Implementation here
        return Response.ok().entity("magic!").build();
    }

    /**
     * Lists all agents with optional pagination.
     *
     * @param limit  The maximum number of agents to return.
     * @param offset The starting point for the list of agents.
     * @return A Response containing the list of agents.
     */
    @Override
    public Response listAgents(Integer limit, Integer offset) {
        // Implementation here
        return Response.ok().entity(agentService.getAgents(limit, offset)).build();
    }

    /**
     * Updates the details of an existing agent.
     *
     * @param agentId           The unique identifier of the agent to be updated.
     * @param agentUpdateRequest The request containing updated agent details.
     * @return A Response indicating the result of the operation.
     */
    @Override
    public Response updateAgent(String agentId, AgentUpdateRequest agentUpdateRequest) {
        // Implementation here
        return Response.ok().entity("magic!").build();
    }

    /**
     * Updates a specific credential for the specified agent.
     *
     * @param agentId        The unique identifier of the agent.
     * @param credentialId   The unique identifier of the credential to be updated.
     * @param credentialBase The updated credential details.
     * @return A Response indicating the result of the operation.
     */
    @Override
    public Response updateAgentCredential(String agentId, String credentialId, CredentialBase credentialBase) {
        // Implementation here
        return Response.ok().entity("magic!").build();
    }
}
