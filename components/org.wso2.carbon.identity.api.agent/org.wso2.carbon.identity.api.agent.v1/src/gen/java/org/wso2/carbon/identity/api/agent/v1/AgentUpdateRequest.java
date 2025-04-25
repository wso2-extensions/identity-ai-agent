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
import javax.validation.constraints.*;

/**
 * Properties to update for an existing agent. All fields are optional.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Properties to update for an existing agent. All fields are optional.")
public class AgentUpdateRequest  {
  
    private String name;
    private String description;
    private String version;
    private String url;
    private String owner;

    /**
    * New human-readable name of the agent.
    **/
    public AgentUpdateRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Gardio Hotel Booking Assistant V2", value = "New human-readable name of the agent.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Updated human-readable description.
    **/
    public AgentUpdateRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Enhanced assistant for Gardio Hotels booking system, now with flight integration.", value = "Updated human-readable description.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Updated version of the agent-format.
    **/
    public AgentUpdateRequest version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "1.3.0", value = "Updated version of the agent-format.")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    * Updated URL to the agent&#39;s endpoint.
    **/
    public AgentUpdateRequest url(String url) {

        this.url = url;
        return this;
    }
    
    @ApiModelProperty(example = "https://api.gardiohotels.com/booking/v2", value = "Updated URL to the agent's endpoint.")
    @JsonProperty("url")
    @Valid
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    /**
    * Change the owner/provider of the agent.
    **/
    public AgentUpdateRequest owner(String owner) {

        this.owner = owner;
        return this;
    }
    
    @ApiModelProperty(example = "98765432-10fe-dcba-0987-654321fedcba", value = "Change the owner/provider of the agent.")
    @JsonProperty("owner")
    @Valid
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentUpdateRequest agentUpdateRequest = (AgentUpdateRequest) o;
        return Objects.equals(this.name, agentUpdateRequest.name) &&
            Objects.equals(this.description, agentUpdateRequest.description) &&
            Objects.equals(this.version, agentUpdateRequest.version) &&
            Objects.equals(this.url, agentUpdateRequest.url) &&
            Objects.equals(this.owner, agentUpdateRequest.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, version, url, owner);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentUpdateRequest {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
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

