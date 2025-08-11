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
package org.wso2.carbon.identity.agent.manager.tests;

import org.wso2.carbon.identity.agent.manager.tests.utils.Util;
import org.wso2.carbon.identity.agent.manager.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.powermock.modules.testng.PowerMockTestCase;
import org.wso2.carbon.identity.user.store.configuration.dto.UserStoreDTO;
import org.wso2.carbon.user.api.RealmConfiguration;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Base test class for agent manager related tests.
 */
public class AgentManagerTestBase extends PowerMockTestCase {

    private static final String TENANT_REALM_CONFIG_PATH = "src/test/resources/realmconfigs/USER_TENANT.xml";
    private static final String GLOBAL_REALM_CONFIG_PATH = "src/test/resources/realmconfigs/AGENT.xml";
    private static final String DUMMY_REALM_CONFIG_PATH = "src/test/resources/realmconfigs/DUMMY.xml";
    private static RealmConfiguration agentRealmConfig = null;
    private static RealmConfiguration tenantRealmConfig = null;
    private static RealmConfiguration dummyRealmConfig = null;

    /**
     * Build a agent realm config.
     *
     * @throws Exception exception.
     */
    protected void buildAgentRealmConfig() throws Exception {

        agentRealmConfig = Util.getRealmConfig(Paths.get(GLOBAL_REALM_CONFIG_PATH).toString());
    }

    /**
     * Retrieve a agent realm config.
     *
     * @throws Exception exception.
     */
    protected RealmConfiguration getAgentRealmConfig() throws Exception {

        return agentRealmConfig.cloneRealmConfigurationWithoutSecondary();
    }

    /**
     * Build a tenant agent realm config.
     *
     * @throws Exception exception.
     */
    protected void buildTenantRealmConfig() throws Exception {

        tenantRealmConfig = Util.getRealmConfig(Paths.get(TENANT_REALM_CONFIG_PATH).toString());
    }

    /**
     * Retrieve a tenant agent realm config.
     *
     * @throws Exception exception.
     */
    protected RealmConfiguration getTenantRealmConfig() throws Exception {

        return tenantRealmConfig.cloneRealmConfigurationWithoutSecondary();
    }

    /**
     * Build a dummy realm config.
     *
     * @throws Exception exception.
     */
    protected void buildDummyRealmConfig() throws Exception {

        dummyRealmConfig = Util.getRealmConfig(Paths.get(DUMMY_REALM_CONFIG_PATH).toString());
    }

    /**
     * Retrieve a dummy realm config.
     *
     * @throws Exception exception.
     */
    protected RealmConfiguration getDummyRealmConfig() throws Exception {

        return dummyRealmConfig.cloneRealmConfigurationWithoutSecondary();
    }

    /**
     * Get TenantProperties prop value defined
     * in the agent realm config.
     *
     * @return TenantProperties as list.
     * @throws Exception exception.
     */
    protected List<String> getTenantPropertyKeys() throws Exception {

        String tenantProp = agentRealmConfig.getUserStoreProperties().get(Constants.TENANT_PROPERTIES_KEY);
        String[] tenantProps = StringUtils.split(StringUtils.deleteWhitespace(tenantProp), ',');
        return Arrays.asList(tenantProps);
    }

    /**
     * Get TenantEditableProperties prop value
     * defined in the agent realm config.
     *
     * @return TenantEditableProperties as list.
     * @throws Exception exception.
     */
    protected List<String> getTenantEditablePropertyKeys() throws Exception {

        String tenantProp = agentRealmConfig.getUserStoreProperties()
                .get(Constants.TENANT_EDITABLE_PROPERTIES_KEY);
        String[] tenantProps = StringUtils.split(StringUtils.deleteWhitespace(tenantProp), ',');
        return Arrays.asList(tenantProps);
    }

    /**
     * Get a UserStoreDTO from a realm config with
     * agent related properties filtered.
     *
     * @param realmConfiguration Realm configuration.
     * @return UserStoreDTO.
     * @throws Exception exception.
     */
    protected UserStoreDTO getAgentStoreDTO(RealmConfiguration realmConfiguration) throws Exception {

        return Util.getUserStoreConfig(realmConfiguration, true);
    }

    /**
     * Get a UserStoreDTO from a realm config.
     *
     * @param realmConfiguration Realm configuration.
     * @return UserStoreDTO.
     * @throws Exception exception.
     */
    protected UserStoreDTO getUserStoreDTO(RealmConfiguration realmConfiguration) throws Exception {

        return Util.getUserStoreConfig(realmConfiguration, false);
    }
}
