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

package org.wso2.carbon.identity.agent.manager.internal;

import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.agent.manager.AgentUserStoreManager;
import org.wso2.carbon.identity.agent.manager.listeners.TenantCreationListener;
import org.wso2.carbon.identity.agent.manager.utils.Constants;
import org.wso2.carbon.identity.agent.manager.utils.Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.user.store.configuration.UserStoreConfigService;
import org.wso2.carbon.identity.user.store.configuration.dto.PropertyDTO;
import org.wso2.carbon.identity.user.store.configuration.dto.UserStoreDTO;
import org.wso2.carbon.identity.user.store.configuration.utils.IdentityUserStoreMgtException;
import org.wso2.carbon.stratos.common.listeners.TenantMgtListener;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.config.UserStoreConfigXMLProcessor;
import org.wso2.carbon.user.core.util.JDBCRealmUtil;
import org.wso2.carbon.utils.CarbonUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.wso2.carbon.identity.agent.manager.utils.Constants.AGENT_IDENTITY_DATASOURCE;
import static org.wso2.carbon.identity.agent.manager.utils.Constants.AGENT_IDENTITY_USERSTORE_MANAGER_CLASS;
import static org.wso2.carbon.identity.core.util.IdentityCoreConstants.AGENT_IDENTITY_USERSTORE_NAME;
import static org.wso2.carbon.identity.core.util.IdentityCoreConstants.DEFAULT_AGENT_IDENTITY_USERSTORE_NAME;

/**
 * This class contains the service component of the end users store manager.
 */
@Component(
        name = "org.wso2.carbon.identity.agent.manager.component",
        immediate = true
)
public class AgentUserStoreManagerServiceComponent {

    private static final Log LOG = LogFactory.getLog(AgentUserStoreManagerServiceComponent.class);

    @Activate
    protected void activate(ComponentContext componentContext) {

        try {
            if (IdentityUtil.isAgentIdentityEnabled() && isAgentIdentityUserstoreManagerClassDefined()
                && isAgentIdentityDatasourceDefined()) {
                componentContext.getBundleContext().registerService(UserStoreManager.class.getName(),
                        new AgentUserStoreManager(), null);

                RealmConfiguration userRealmConfig = buildUserRealmConfig();
                AgentUserStoreManagerHolder.setUserRealmConfig(userRealmConfig);
                UserStoreDTO tenantUserStoreConfig = buildTenantUserStoreConfig(userRealmConfig);
                // Add agent userstore to super tenant as a secondary userstore.
                addAgentUserStoreToSuperTenant(tenantUserStoreConfig);
                componentContext.getBundleContext().registerService(TenantMgtListener.class.getName(),
                        new TenantCreationListener(tenantUserStoreConfig), null);
                LOG.debug("Agent UserStore Manager service component activated successfully.");
            }
        } catch (Exception e) {
            LOG.error("Failed to activate Agent UserStore Manager service component.", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Agent UserStore Manager service component deactivated.");
        }
    }

    @Reference(
            name = "userStoreConfigService",
            service = UserStoreConfigService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetUserStoreConfigService")
    protected void setUserStoreConfigService(UserStoreConfigService userStoreConfigService) {

        AgentUserStoreManagerHolder.setUserStoreConfigService(userStoreConfigService);
    }

    protected void unsetUserStoreConfigService(UserStoreConfigService userStoreConfigService) {

        AgentUserStoreManagerHolder.setUserStoreConfigService(null);
    }

    /**
     * Add agent userstore to super tenant as secondary userstore.
     *
     * @param agentTenantUserStoreConfig UserStoreDTO
     */
    public void addAgentUserStoreToSuperTenant(UserStoreDTO agentTenantUserStoreConfig) {

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext()
                    .setTenantDomain(IdentityTenantUtil.getTenantDomain(MultitenantConstants.SUPER_TENANT_ID));
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(MultitenantConstants.SUPER_TENANT_ID);

            agentTenantUserStoreConfig.setDomainId(IdentityUtil.getAgentIdentityUserstoreName());

            // Check agent userstore is already added
            if (Files.exists(Util.getSuperTenantAgentUSPath(IdentityUtil.getAgentIdentityUserstoreName()))) {
                return;
            }
            AgentUserStoreManagerHolder.getUserStoreConfigService().addUserStore(agentTenantUserStoreConfig);
        } catch (IdentityUserStoreMgtException e) {
            LOG.error("Error occurred while adding the agent user-store at carbon.super.", e);
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    private RealmConfiguration buildUserRealmConfig() throws Exception {

        UserStoreConfigXMLProcessor configXMLProcessor =
                new UserStoreConfigXMLProcessor(Util.getAgentUSConfigPath().toString());
        RealmConfiguration userRealmConfig = configXMLProcessor.buildUserStoreConfigurationFromFile();
        userRealmConfig.setUserStoreProperties(JDBCRealmUtil.getSQL(userRealmConfig.getUserStoreProperties()));
        return userRealmConfig;
    }

    private UserStoreDTO buildTenantUserStoreConfig(RealmConfiguration userRealmConfig) throws Exception {

        UserStoreDTO userStoreConfig = new UserStoreDTO();
        userStoreConfig.setClassName(userRealmConfig.getUserStoreClass());
        userStoreConfig.setDomainId(userRealmConfig.getUserStoreProperties().get(Constants.DOMAIN_NAME_KEY));
        if (userRealmConfig.getUserStoreProperties().containsKey(Constants.DESCRIPTION_KEY)) {
            userStoreConfig.setDescription(userRealmConfig.getUserStoreProperties().get(Constants.DESCRIPTION_KEY));
        }
        userStoreConfig.setProperties(getUserStoreProperties(userRealmConfig.getUserStoreProperties()));

        return userStoreConfig;
    }

    private PropertyDTO[] getUserStoreProperties(Map<String, String> commonProperties) throws Exception {

        if (!commonProperties.containsKey(Constants.TENANT_PROPERTIES_KEY)) {
            throw new Exception("Tenant allowed property(" + Constants.TENANT_PROPERTIES_KEY + ") not defined in the " +
                    "common agent user-store config.");
        }

        String tenantProps = StringUtils.deleteWhitespace(commonProperties.get(Constants.TENANT_PROPERTIES_KEY));
        String[] props = StringUtils.split(tenantProps, ',');
        PropertyDTO[] propertyDTOS = new PropertyDTO[props.length];

        for (int i = 0; i < props.length; i++) {
            String propKey = props[i];
            String propValue = commonProperties.get(propKey);
            if (StringUtils.isNotBlank(propValue)) {
                propertyDTOS[i] = new PropertyDTO(propKey, propValue);
            } else {
                throw new Exception("Tenant allowed property " + propKey +
                        " not available in agent user-store config.");
            }
        }

        return propertyDTOS;
    }

    /**
     * Checks whether the agent identity userstore manager class is explicitly defined.
     *
     * @return return true, if the agent identity userstore manager class is defined.
     */
    public static boolean isAgentIdentityUserstoreManagerClassDefined() {

        String userstoreManagerClass = IdentityUtil.getProperty(AGENT_IDENTITY_USERSTORE_MANAGER_CLASS);
        if (StringUtils.isBlank(userstoreManagerClass)) {
            return false;
        }
        return AgentUserStoreManager.class.getName().equals(userstoreManagerClass);
    }

    /**
     * Checks whether the agent identity datasource is defined.
     *
     * @return return true, if the agent identity datasource is defined.
     */
    public static boolean isAgentIdentityDatasourceDefined() {

        String userstoreManagerClass = IdentityUtil.getProperty(AGENT_IDENTITY_DATASOURCE);
        if (StringUtils.isBlank(userstoreManagerClass)) {
            return false;
        }
        return true;
    }
}
