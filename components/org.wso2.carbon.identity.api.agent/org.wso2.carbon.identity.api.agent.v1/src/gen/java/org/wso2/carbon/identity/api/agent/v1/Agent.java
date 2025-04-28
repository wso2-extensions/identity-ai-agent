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

public class Agent  {
  
    private String id;
    private String name;
    private String description;
    private String version;
    private String url;
    private String owner;

    private String createdAt;
    private String updatedAt;


    /**
    * Unique identifier for the agent (usually assigned by the server).
    **/
    public Agent id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "a1b2c3d4-e5f6-7890-1234-567890abcdef", required = true, value = "Unique identifier for the agent (usually assigned by the server).")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Human-readable name of the agent.
    **/
    public Agent name(String name) {

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
    public Agent description(String description) {

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
    * The version of the agent-format is up to the provider.
    **/
    public Agent version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "1.2.0", value = "The version of the agent-format is up to the provider.")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    * A URL to the address the agent is hosted at (e.g., its API endpoint).
    **/
    public Agent url(String url) {

        this.url = url;
        return this;
    }
    
    @ApiModelProperty(example = "https://api.gardiohotels.com/booking/v1", value = "A URL to the address the agent is hosted at (e.g., its API endpoint).")
    @JsonProperty("url")
    @Valid
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    /**
    * The service provider or organization behind the agent (referencing a Provider ID).
    **/
    public Agent owner(String owner) {

        this.owner = owner;
        return this;
    }
    
    @ApiModelProperty(example = "f0e9d8c7-b6a5-4321-fedc-ba9876543210", value = "The service provider or organization behind the agent (referencing a Provider ID).")
    @JsonProperty("owner")
    @Valid
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
    * Timestamp when the agent was created.
    **/
    public Agent createdAt(String createdAt) {

        this.createdAt = createdAt;
        return this;
    }
    
    @ApiModelProperty(value = "Timestamp when the agent was created.")
    @JsonProperty("createdAt")
    @Valid
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
    * Timestamp when the agent was last updated.
    **/
    public Agent updatedAt(String updatedAt) {

        this.updatedAt = updatedAt;
        return this;
    }
    
    @ApiModelProperty(value = "Timestamp when the agent was last updated.")
    @JsonProperty("updatedAt")
    @Valid
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Agent agent = (Agent) o;
        return Objects.equals(this.id, agent.id) &&
            Objects.equals(this.name, agent.name) &&
            Objects.equals(this.description, agent.description) &&
            Objects.equals(this.version, agent.version) &&
            Objects.equals(this.url, agent.url) &&
            Objects.equals(this.owner, agent.owner) &&
            Objects.equals(this.createdAt, agent.createdAt) &&
            Objects.equals(this.updatedAt, agent.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, version, url, owner, createdAt, updatedAt);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Agent {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

