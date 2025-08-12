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

package org.wso2.carbon.identity.agent.manager.utils;

/**
 * Constants class for agent userstore.
 */
public class Constants {

    private Constants() {

    }

    public static final String USER_DOMAIN = "AGENT";
    public static final String USERSTORE_CONFIG_FILE = USER_DOMAIN + ".xml";
    public static final String DOMAIN_NAME_KEY = "DomainName";
    public static final String DESCRIPTION_KEY = "Description";
    public static final String TENANT_PROPERTIES_KEY = "TenantProperties";
    public static final String TENANT_EDITABLE_PROPERTIES_KEY = "TenantEditableProperties";
    public static final String AGENT_IDENTITY_USERSTORE_MANAGER_CLASS = "AgentIdentity.UserstoreManagerClass";
    public static final String AGENT_IDENTITY_DATASOURCE = "AgentIdentity.Datasource";
}
