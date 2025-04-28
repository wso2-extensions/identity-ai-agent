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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.agent.v1.CredentialBase;
import javax.validation.constraints.*;

/**
 * Represents a credential associated with an agent, returned in responses after creation or update. Sensitive fields are omitted or masked.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represents a credential associated with an agent, returned in responses after creation or update. Sensitive fields are omitted or masked.")
public class AgentCredentialResponse  {
  
    private String credentialId;
    private CredentialBase credentialDetails;

    /**
    * The unique server-assigned identifier for this credential.
    **/
    public AgentCredentialResponse credentialId(String credentialId) {

        this.credentialId = credentialId;
        return this;
    }
    
    @ApiModelProperty(example = "cred-e5d8a0b7", required = true, value = "The unique server-assigned identifier for this credential.")
    @JsonProperty("credentialId")
    @Valid
    @NotNull(message = "Property credentialId cannot be null.")

    public String getCredentialId() {
        return credentialId;
    }
    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    /**
    **/
    public AgentCredentialResponse credentialDetails(CredentialBase credentialDetails) {

        this.credentialDetails = credentialDetails;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("credentialDetails")
    @Valid
    public CredentialBase getCredentialDetails() {
        return credentialDetails;
    }
    public void setCredentialDetails(CredentialBase credentialDetails) {
        this.credentialDetails = credentialDetails;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentCredentialResponse agentCredentialResponse = (AgentCredentialResponse) o;
        return Objects.equals(this.credentialId, agentCredentialResponse.credentialId) &&
            Objects.equals(this.credentialDetails, agentCredentialResponse.credentialDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialId, credentialDetails);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentCredentialResponse {\n");
        
        sb.append("    credentialId: ").append(toIndentedString(credentialId)).append("\n");
        sb.append("    credentialDetails: ").append(toIndentedString(credentialDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

