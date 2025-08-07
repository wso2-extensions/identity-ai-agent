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

package org.wso2.carbon.identity.agent.manager.listeners;

import org.wso2.carbon.identity.agent.manager.internal.AgentUserStoreManagerHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.core.AbstractIdentityTenantMgtListener;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.user.store.configuration.dto.UserStoreDTO;
import org.wso2.carbon.identity.user.store.configuration.utils.IdentityUserStoreMgtException;

/**
 * Tenant mgt listener for agent userstore onboarding.
 */
public class TenantCreationListener extends AbstractIdentityTenantMgtListener {

    private static final Log LOG = LogFactory.getLog(TenantCreationListener.class);
    private final UserStoreDTO agentTenantUserStoreConfig;

    /**
     * Constructor used to register TenantCreationListener as OSGi service.
     *
     * @param agentTenantUserStoreConfig  agent user store configuration.
     */
    public TenantCreationListener(UserStoreDTO agentTenantUserStoreConfig) {

         this.agentTenantUserStoreConfig = agentTenantUserStoreConfig;
    }

    @Override
    public void onTenantInitialActivation(int tenantId) {

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext()
                    .setTenantDomain(IdentityTenantUtil.getTenantDomain(tenantId));
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantId);

            agentTenantUserStoreConfig.setDomainId(IdentityUtil.getAgentIdentityUserstoreName());

            AgentUserStoreManagerHolder.getUserStoreConfigService().addUserStore(agentTenantUserStoreConfig);
        } catch (IdentityUserStoreMgtException e) {
            LOG.error("Error occurred while adding the agent user-store at tenant creation for tenant id "
                    + tenantId, e);
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }
}
