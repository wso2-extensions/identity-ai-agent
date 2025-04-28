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
 * Base definition for a credential which can take various forms. Use the &#x60;credentialType&#x60; property to determine the specific structure. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Base definition for a credential which can take various forms. Use the `credentialType` property to determine the specific structure. ")
public class CredentialBase  {
  

@XmlType(name="CredentialTypeEnum")
@XmlEnum(String.class)
public enum CredentialTypeEnum {

    @XmlEnumValue("SECRET") SECRET(String.valueOf("SECRET")), @XmlEnumValue("MTLS") MTLS(String.valueOf("MTLS")), @XmlEnumValue("PRIVATE_KEY_JWT") PRIVATE_KEY_JWT(String.valueOf("PRIVATE_KEY_JWT")), @XmlEnumValue("OAUTH2_CLIENT_CREDENTIALS") OAUTH2_CLIENT_CREDENTIALS(String.valueOf("OAUTH2_CLIENT_CREDENTIALS"));


    private String value;

    CredentialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static CredentialTypeEnum fromValue(String value) {
        for (CredentialTypeEnum b : CredentialTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private CredentialTypeEnum credentialType;
    private Object credentialDetails;

    /**
    * The type of credential being represented.
    **/
    public CredentialBase credentialType(CredentialTypeEnum credentialType) {

        this.credentialType = credentialType;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The type of credential being represented.")
    @JsonProperty("credentialType")
    @Valid
    @NotNull(message = "Property credentialType cannot be null.")

    public CredentialTypeEnum getCredentialType() {
        return credentialType;
    }
    public void setCredentialType(CredentialTypeEnum credentialType) {
        this.credentialType = credentialType;
    }

    /**
    * The actual credential value. The format depends on the &#x60;credentialType&#x60;.
    **/
    public CredentialBase credentialDetails(Object credentialDetails) {

        this.credentialDetails = credentialDetails;
        return this;
    }
    
    @ApiModelProperty(value = "The actual credential value. The format depends on the `credentialType`.")
    @JsonProperty("credentialDetails")
    @Valid
    public Object getCredentialDetails() {
        return credentialDetails;
    }
    public void setCredentialDetails(Object credentialDetails) {
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
        CredentialBase credentialBase = (CredentialBase) o;
        return Objects.equals(this.credentialType, credentialBase.credentialType) &&
            Objects.equals(this.credentialDetails, credentialBase.credentialDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialType, credentialDetails);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CredentialBase {\n");
        
        sb.append("    credentialType: ").append(toIndentedString(credentialType)).append("\n");
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

