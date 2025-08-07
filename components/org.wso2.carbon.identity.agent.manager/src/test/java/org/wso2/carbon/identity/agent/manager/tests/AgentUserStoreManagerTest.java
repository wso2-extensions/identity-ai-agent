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

import org.wso2.carbon.identity.agent.manager.AgentUserStoreManager;
import org.wso2.carbon.identity.agent.manager.internal.AgentUserStoreManagerHolder;
import org.wso2.carbon.identity.agent.manager.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.user.api.Property;
import org.wso2.carbon.user.api.RealmConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Test class for AgentStoreManager.
 */
public class AgentUserStoreManagerTest extends AgentManagerTestBase {

    private static final Log LOG = LogFactory.getLog(AgentUserStoreManagerTest.class);
    private static final int TENANT_ID = 1;

    private static final String ALLOWED_TENANT_PROPS = "UsernameJavaRegEx,UsernameJavaScriptRegEx," +
            "UsernameJavaRegExViolationErrorMsg,PasswordJavaRegEx,PasswordJavaScriptRegEx,RolenameJavaRegEx," +
            "RolenameJavaScriptRegEx";

    @BeforeClass
    public void init() throws Exception {

        buildAgentRealmConfig();
        buildTenantRealmConfig();
    }

    @Test
    public void testMandatoryDefaultProperties() {

        AgentUserStoreManager AgentUserStoreManager = new AgentUserStoreManager();

        Property[] mandatoryProperties = AgentUserStoreManager
                .getDefaultUserStoreProperties()
                .getMandatoryProperties();
        StringJoiner sj = new StringJoiner(",");
        Arrays.stream(mandatoryProperties).forEach(property -> sj.add(property.getName()));

        Assert.assertEquals(AgentUserStoreManager.getDefaultUserStoreProperties().getMandatoryProperties()
                        .length, 0, "No mandatory properties should be defined. " + sj
                + ". If the defined mandatory properties are " + "tenant configurable update the test case.");
    }

    @Test
    public void testBuildRealmConfig() throws Exception {

        RealmConfiguration agentRealmConfig = getAgentRealmConfig();
        RealmConfiguration tenantRealmConfig = getTenantRealmConfig();

        setTenantAllowedProps(agentRealmConfig);
        AgentUserStoreManagerHolder.setUserRealmConfig(agentRealmConfig);

        injectNonAllowedTenantProps(tenantRealmConfig);
        removeAllowedTenantProp(tenantRealmConfig);

        AgentUserStoreManager AgentUserStoreManager = new AgentUserStoreManager();
        RealmConfiguration mergedTenantRealmConfig = Whitebox.invokeMethod(AgentUserStoreManager,
                "buildRealmConfig", tenantRealmConfig.cloneRealmConfiguration());

        /* Currently the test util XMLProcessorUtil only sets the userstore related realm
         * properties. Most of the other realm properties are added from the primary realm
         * when building the secondary realm using the UserStoreConfigXMLProcessor. As the
         * primary realm is unavailable for unit tests this test will only check for
         * properties related to the userstore.
         */
        List<String> tenantProps = getTenantPropertyKeys();

        // Has allowed tenant properties replaced.
        Map<String, String> allowedTenantProps = getAllowedTenantProperties(
                tenantRealmConfig.getUserStoreProperties(), tenantProps);

        allowedTenantProps.forEach((k, v) -> Assert.assertTrue(
                StringUtils.equals(mergedTenantRealmConfig.getUserStoreProperty(k), v),
                String.format("Allowed property %s value not merged. Actual: %s Expected: %s",
                        k, mergedTenantRealmConfig.getUserStoreProperty(k), v)));

        // Has non allowed tenant properties not replaced.
        Map<String, String> nonAllowedTenantProps = getNonAllowedTenantProperties(
                tenantRealmConfig.getUserStoreProperties(), tenantProps);

        nonAllowedTenantProps.forEach((k, v) -> Assert.assertFalse(
                StringUtils.equals(mergedTenantRealmConfig.getUserStoreProperty(k), v) &&
                        !StringUtils.equals(agentRealmConfig.getUserStoreProperty(k), v),
                String.format("Non allowed property %s is available in merged config. Allowed properties %s",
                        k, StringUtils.join(tenantProps, ","))));
    }

    @Test
    public void testBuildTenantPropertiesWithEmptyTenantAllowedProps() throws Exception {

        RealmConfiguration agentRealmConfig = getAgentRealmConfig();
        removeAllowedTenantProps(agentRealmConfig);
        AgentUserStoreManagerHolder.setUserRealmConfig(agentRealmConfig);
        Map<String, String> userPropsOriginal = agentRealmConfig.cloneRealmConfiguration()
                .getUserStoreProperties();

        RealmConfiguration tenantRealmConfig = getTenantRealmConfig();
        injectNonAllowedTenantProps(agentRealmConfig);

        AgentUserStoreManager AgentUserStoreManager = new AgentUserStoreManager();
        Whitebox.invokeMethod(AgentUserStoreManager, "buildTenantProperties",
                agentRealmConfig.getUserStoreProperties(),
                tenantRealmConfig.getUserStoreProperties(), TENANT_ID);

        Map<String, String> tenantProps = tenantRealmConfig.getUserStoreProperties();
        Map<String, String> userStoreProperties = agentRealmConfig.getUserStoreProperties();
        final String failMsg = "No tenant allowed properties defined, but tenant property %s " +
                "with value %s is available in merged config.";
        tenantProps.forEach((k, v) -> {
            if (userStoreProperties.containsKey(k) && !userPropsOriginal.containsKey(k)) {
                Assert.fail(String.format(failMsg, k, v));
            }

            if (StringUtils.equals(userStoreProperties.get(k), v) &&
                    !StringUtils.equals(userPropsOriginal.get(k), v)) {
                Assert.fail(String.format(failMsg, k, v));
            }
         });
    }

    private Map<String, String> getAllowedTenantProperties(Map<String, String> props, List<String> tenantPropKeys) {

        return props.entrySet().stream()
                .filter(e -> tenantPropKeys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, String> getNonAllowedTenantProperties(Map<String, String> props, List<String> tenantPropKeys) {

        return props.entrySet().stream()
                .filter(e -> !tenantPropKeys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void injectNonAllowedTenantProps(RealmConfiguration realmConfiguration) {

        Map<String, String> userStoreProperties = realmConfiguration.getUserStoreProperties();
        userStoreProperties.put("dummy1", "dummy1");
        userStoreProperties.put("dummy2", "dummy2");
    }

    private void removeAllowedTenantProp(RealmConfiguration realmConfiguration) throws Exception {

        Map<String, String> userStoreProperties = realmConfiguration.getUserStoreProperties();
        List<String> tenantPropKeys = getTenantPropertyKeys();
        for (String key : tenantPropKeys) {
            if (userStoreProperties.containsKey(key)) {
                userStoreProperties.remove(key);
                LOG.info("Remove allowed property " + key + " from the realm config.");
                break;
            }
        }
    }

    private void removeAllowedTenantProps(RealmConfiguration realmConfiguration) throws Exception {

        Map<String, String> userStoreProperties = realmConfiguration.getUserStoreProperties();
        userStoreProperties.put(Constants.TENANT_PROPERTIES_KEY, StringUtils.EMPTY);
    }

    private void setTenantAllowedProps(RealmConfiguration realmConfiguration) {

        Map<String, String> userStoreProperties = realmConfiguration.getUserStoreProperties();
        userStoreProperties.put(Constants.TENANT_PROPERTIES_KEY, ALLOWED_TENANT_PROPS);
    }

    @Override
    protected List<String> getTenantPropertyKeys() throws Exception {

        return Arrays.asList(StringUtils.deleteWhitespace(ALLOWED_TENANT_PROPS).split(","));
    }
}
