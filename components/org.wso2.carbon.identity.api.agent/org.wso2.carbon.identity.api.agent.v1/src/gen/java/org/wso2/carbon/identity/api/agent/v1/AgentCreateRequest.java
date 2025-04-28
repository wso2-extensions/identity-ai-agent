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


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AgentCreateRequest  {
  
    private String name;
    private String description;
    private String version;
    private String url;
    private String owner;

    /**
    * Human-readable name of the agent.
    **/
    public AgentCreateRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Gardio Hotel Booking Assistant", required = true, value = "Human-readable name of the agent.")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * A human-readable description of the agent.
    **/
    public AgentCreateRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Helps users interact with the Gardio Hotels booking system.", required = true, value = "A human-readable description of the agent.")
    @JsonProperty("description")
    @Valid
    @NotNull(message = "Property description cannot be null.")

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * The version of the agent-format.
    **/
    public AgentCreateRequest version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "1.2.0", required = true, value = "The version of the agent-format.")
    @JsonProperty("version")
    @Valid
    @NotNull(message = "Property version cannot be null.")

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    * A URL to the agent&#39;s endpoint (optional).
    **/
    public AgentCreateRequest url(String url) {

        this.url = url;
        return this;
    }
    
    @ApiModelProperty(example = "https://api.gardiohotels.com/booking/v1", value = "A URL to the agent's endpoint (optional).")
    @JsonProperty("url")
    @Valid
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    /**
    * The service provider or organization behind the agent (optional).
    **/
    public AgentCreateRequest owner(String owner) {

        this.owner = owner;
        return this;
    }
    
    @ApiModelProperty(example = "f0e9d8c7-b6a5-4321-fedc-ba9876543210", value = "The service provider or organization behind the agent (optional).")
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
        AgentCreateRequest agentCreateRequest = (AgentCreateRequest) o;
        return Objects.equals(this.name, agentCreateRequest.name) &&
            Objects.equals(this.description, agentCreateRequest.description) &&
            Objects.equals(this.version, agentCreateRequest.version) &&
            Objects.equals(this.url, agentCreateRequest.url) &&
            Objects.equals(this.owner, agentCreateRequest.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, version, url, owner);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentCreateRequest {\n");
        
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

