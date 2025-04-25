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

package org.wso2.carbon.identity.api.agent.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.agent.v1.Agent;
import org.wso2.carbon.identity.api.agent.v1.AgentCreateRequest;
import org.wso2.carbon.identity.api.agent.v1.AgentCredentialResponse;
import org.wso2.carbon.identity.api.agent.v1.AgentUpdateRequest;
import org.wso2.carbon.identity.api.agent.v1.CredentialBase;
import org.wso2.carbon.identity.api.agent.v1.ErrorResponse;
import org.wso2.carbon.identity.api.agent.v1.DefaultApiService;
import org.wso2.carbon.identity.api.agent.v1.factories.DefaultApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/")
@Api(description = "The  API")

public class DefaultApi  {

    private final DefaultApiService delegate;

    public DefaultApi() {

        this.delegate = DefaultApiServiceFactory.getDefaultApi();
    }

    @Valid
    @POST
    @Path("/{agent-id}/credentials")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a new credential for an agent", notes = "Creates and associates a new credential with the specified AI Agent. The specific type and details are determined by the request body based on `credentialType`. <b>Scope required:</b> `internal_org_agent_mgt_update`", response = AgentCredentialResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Credential successfully created.", response = AgentCredentialResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. The request could not be understood due to malformed syntax or invalid data.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized. Authentication credentials required or provided credentials are invalid.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden. The authenticated client does not have sufficient permissions (scopes).", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not Found. The requested resource could not be found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An unexpected condition was encountered on the server.", response = ErrorResponse.class)
    })
    public Response addAgentCredential(@ApiParam(value = "The unique identifier of the AI Agent.",required=true) @PathParam("agent-id") String agentId, @ApiParam(value = "The credential object to add. The `credentialType` determines the required fields." ,required=true) @Valid CredentialBase credentialBase) {

        return delegate.addAgentCredential(agentId,  credentialBase );
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a new Agent", notes = "Creates a new agent. Allows requesting server-side credential generation. <b>Scope required:</b> `internal_org_agent_mgt_create`", response = Agent.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Agent created successfully. The response includes the full Agent object, including generated credentials if requested.", response = Agent.class),
        @ApiResponse(code = 400, message = "Bad Request. The request could not be understood due to malformed syntax or invalid data.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized. Authentication credentials required or provided credentials are invalid.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden. The authenticated client does not have sufficient permissions (scopes).", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An unexpected condition was encountered on the server.", response = ErrorResponse.class)
    })
    public Response createAgent(@ApiParam(value = "Agent object to be created." ,required=true) @Valid AgentCreateRequest agentCreateRequest) {

        return delegate.createAgent(agentCreateRequest );
    }

    @Valid
    @DELETE
    @Path("/{agentId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an Agent", notes = "Deletes a specific agent by ID. <b>Scope required:</b> `internal_org_agent_mgt_delete`", response = Void.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Agent deleted successfully. No content.", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized. Authentication credentials required or provided credentials are invalid.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden. The authenticated client does not have sufficient permissions (scopes).", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not Found. The requested resource could not be found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An unexpected condition was encountered on the server.", response = ErrorResponse.class)
    })
    public Response deleteAgent(@ApiParam(value = "ID of the agent to operate on.",required=true) @PathParam("agentId") String agentId) {

        return delegate.deleteAgent(agentId );
    }

    @Valid
    @GET
    @Path("/{agentId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a specific Agent by ID", notes = "Returns the details of a specific agent. <b>Scope required:</b> `internal_org_agent_mgt_read`", response = Agent.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Agent details.", response = Agent.class),
        @ApiResponse(code = 401, message = "Unauthorized. Authentication credentials required or provided credentials are invalid.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden. The authenticated client does not have sufficient permissions (scopes).", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not Found. The requested resource could not be found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An unexpected condition was encountered on the server.", response = ErrorResponse.class)
    })
    public Response getAgentById(@ApiParam(value = "ID of the agent to operate on.",required=true) @PathParam("agentId") String agentId) {

        return delegate.getAgentById(agentId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all Agents", notes = "Returns a list of all agents with pagination support. <b>Scope required:</b> `internal_org_agent_mgt_read`", response = Agent.class, responseContainer = "List", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A list of agents.", response = Agent.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized. Authentication credentials required or provided credentials are invalid.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden. The authenticated client does not have sufficient permissions (scopes).", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An unexpected condition was encountered on the server.", response = ErrorResponse.class)
    })
    public Response listAgents(    @Valid@ApiParam(value = "The maximum number of agents to return.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "The starting point for the list of agents to return.")  @QueryParam("offset") Integer offset) {

        return delegate.listAgents(limit,  offset );
    }

    @Valid
    @PUT
    @Path("/{agentId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an existing Agent", notes = "Updates the properties of an existing agent. Only include fields to be changed. <b>Scope required:</b> `internal_org_agent_mgt_update`", response = Agent.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Agent updated successfully. Returns the updated agent object.", response = Agent.class),
        @ApiResponse(code = 400, message = "Bad Request. The request could not be understood due to malformed syntax or invalid data.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized. Authentication credentials required or provided credentials are invalid.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden. The authenticated client does not have sufficient permissions (scopes).", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not Found. The requested resource could not be found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An unexpected condition was encountered on the server.", response = ErrorResponse.class)
    })
    public Response updateAgent(@ApiParam(value = "ID of the agent to operate on.",required=true) @PathParam("agentId") String agentId, @ApiParam(value = "Agent properties to update. Only include fields to be changed." ,required=true) @Valid AgentUpdateRequest agentUpdateRequest) {

        return delegate.updateAgent(agentId,  agentUpdateRequest );
    }

    @Valid
    @PUT
    @Path("/{agent-id}/credentials/{credential-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a specific credential for an agent", notes = "Updates an existing credential associated with the AI Agent. The entire credential object (matching the `credential-id`) should be provided. The `credentialType` in the request body *must* match the existing credential's type. You cannot change the type using PUT. <b>Scope required:</b> `internal_org_agent_mgt_update` ", response = AgentCredentialResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Credential updated successfully.", response = AgentCredentialResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. The request could not be understood due to malformed syntax or invalid data.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized. Authentication credentials required or provided credentials are invalid.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden. The authenticated client does not have sufficient permissions (scopes).", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not Found. The requested resource could not be found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An unexpected condition was encountered on the server.", response = ErrorResponse.class)
    })
    public Response updateAgentCredential(@ApiParam(value = "The unique identifier of the AI Agent.",required=true) @PathParam("agent-id") String agentId, @ApiParam(value = "The unique identifier of the credential to update.",required=true) @PathParam("credential-id") String credentialId, @ApiParam(value = "The updated credential object. `credentialType` must match the existing credential." ,required=true) @Valid CredentialBase credentialBase) {

        return delegate.updateAgentCredential(agentId,  credentialId,  credentialBase );
    }

}
