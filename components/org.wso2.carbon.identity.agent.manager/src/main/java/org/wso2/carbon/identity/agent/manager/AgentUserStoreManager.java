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

package org.wso2.carbon.identity.agent.manager;

import org.wso2.carbon.identity.agent.manager.internal.AgentUserStoreManagerHolder;
import org.wso2.carbon.identity.agent.manager.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.user.api.Properties;
import org.wso2.carbon.user.api.Property;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.claim.ClaimManager;
import org.wso2.carbon.user.core.common.User;
import org.wso2.carbon.user.core.jdbc.JDBCUserStoreConstants;
import org.wso2.carbon.user.core.jdbc.UniqueIDJDBCUserStoreManager;
import org.wso2.carbon.user.core.profile.ProfileConfigurationManager;
import org.wso2.carbon.user.core.util.DatabaseUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.Arrays;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Userstore manager to manage agents.
 */
public class AgentUserStoreManager extends UniqueIDJDBCUserStoreManager {

    private static final Log LOG = LogFactory.getLog(AgentUserStoreManager.class);

    /**
     * Default Constructor.
     */
    public AgentUserStoreManager() {
        super();
    }

    /**
     * Constructor with realm config and tenant id.
     *
     * @param realmConfig User store realm configuration.
     * @param tenantId    Tenant id.
     * @throws UserStoreException if any error occurs while access user store.
     */
    public AgentUserStoreManager(RealmConfiguration realmConfig, int tenantId) throws UserStoreException {

        super(buildRealmConfig(realmConfig), tenantId);
        setDatasource();
    }

    /**
     * Constructor with realm config, data source, initial data and tenant id.
     *
     * @param dataSource  Data source.
     * @param realmConfig User store realm configuration.
     * @param tenantId    Tenant id.
     * @param addInitData Flag for adding initial data.
     * @throws UserStoreException if any error occurs while access user store.
     */
    public AgentUserStoreManager(DataSource dataSource, RealmConfiguration realmConfig, int tenantId,
                                         boolean addInitData) throws UserStoreException {

        super(dataSource, buildRealmConfig(realmConfig), tenantId, addInitData);
        setDatasource();
    }

    /**
     * Constructor with realm config and data source.
     *
     * @param dataSource  Data source.
     * @param realmConfig User store realm configuration.
     * @throws UserStoreException if any error occurs while access user store.
     */
    public AgentUserStoreManager(DataSource dataSource, RealmConfiguration realmConfig)
            throws UserStoreException {

        super(dataSource, buildRealmConfig(realmConfig));
        setDatasource();
    }

    /**
     * Constructor with claim manager and profile manager.
     *
     * @param realmConfig    User store realm configuration.
     * @param properties     Additional properties to be configured.
     * @param claimManager   Claim manager.
     * @param profileManager User Profile manager.
     * @param realm          User realm.
     * @param tenantId       Tenant Id.
     * @throws UserStoreException if any error occurs while access user store.
     */
    public AgentUserStoreManager(RealmConfiguration realmConfig, Map<String, Object> properties,
                                         ClaimManager claimManager, ProfileConfigurationManager profileManager,
                                         UserRealm realm, Integer tenantId)
            throws UserStoreException {

        super(buildRealmConfig(realmConfig), properties, claimManager, profileManager, realm, tenantId);
        setDatasource();
    }

    /**
     * Constructor with claim manager, skip init data flag and profile manager.
     *
     * @param realmConfig    User store realm configuration.
     * @param properties     Additional properties to be configured.
     * @param claimManager   Claim manager.
     * @param profileManager User Profile manager.
     * @param realm          User realm.
     * @param tenantId       Tenant Id.
     * @param skipInitData   Flag to skip setting initial data.
     * @throws UserStoreException if any error occurs while access user store.
     */
    public AgentUserStoreManager(RealmConfiguration realmConfig, Map<String, Object> properties,
                                         ClaimManager claimManager, ProfileConfigurationManager profileManager,
                                         UserRealm realm, Integer tenantId,
                                         boolean skipInitData) throws UserStoreException {

        super(buildRealmConfig(realmConfig), properties, claimManager, profileManager, realm, tenantId, skipInitData);
        setDatasource();
    }

    @Override
    public User doAddUserWithID(String userName, Object credential, String[] roleList, Map<String, String> claims,
                                String profileName, boolean requirePasswordChange) throws UserStoreException {

        String userID = this.getUniqueUserID();
        this.updateLocationClaimWithUserId(userID, claims);
        claims = this.addUserNameAttribute(userID, claims);
        claims = this.addUserIDAttribute(userID, claims);
        this.persistUser(userID, userID, credential, roleList, claims, profileName, requirePasswordChange);
        return this.getUser(userID, userID);
    }

    @Override
    public Properties getDefaultUserStoreProperties() {

        Properties properties = new Properties();
        properties.setMandatoryProperties(new Property[0]);
        properties.setOptionalProperties(JDBCUserStoreConstants.JDBC_UM_OPTIONAL_PROPERTIES.toArray(new Property[0]));
        properties.setAdvancedProperties(JDBCUserStoreConstants.JDBC_UM_ADVANCED_PROPERTIES.toArray(new Property[0]));
        return properties;
    }

    /**
     * Build the realm configuration for the tenant using both tenant and common userstore configurations.
     *
     * @param tenantRealmConfig Tenant realm configuration.
     * @return Complete realm configuration for tenant.
     * @throws UserStoreException Thrown when there is an exception.
     */
    protected static RealmConfiguration buildRealmConfig(RealmConfiguration tenantRealmConfig)
            throws UserStoreException {

        RealmConfiguration userRealmConfig;
        try {
            userRealmConfig = AgentUserStoreManagerHolder.getUserRealmConfig();
        } catch (Exception e) {
            throw new UserStoreException("Error while retrieving common user realm configuration.", e);
        }

        // Userstore properties.
        buildTenantProperties(userRealmConfig.getUserStoreProperties(), tenantRealmConfig.getUserStoreProperties(),
                tenantRealmConfig.getTenantId());

        // Realm properties.
        buildTenantProperties(userRealmConfig.getRealmProperties(), tenantRealmConfig.getRealmProperties(),
                tenantRealmConfig.getTenantId());

        // Add common config to tenant realm.
        tenantRealmConfig.setUserStoreProperties(userRealmConfig.getUserStoreProperties());
        tenantRealmConfig.setAuthorizationManagerClass(userRealmConfig.getAuthorizationManagerClass());
        tenantRealmConfig.setAuthzProperties(userRealmConfig.getAuthzProperties());
        tenantRealmConfig.setRealmProperties(userRealmConfig.getRealmProperties());
        tenantRealmConfig.setPasswordsExternallyManaged(userRealmConfig.isPasswordsExternallyManaged());
        tenantRealmConfig.addMultipleCredentialProperties(userRealmConfig.getUserStoreClass(),
                userRealmConfig.getMultipleCredentialProps().get(userRealmConfig.getUserStoreClass()));

        return tenantRealmConfig;
    }

    /**
     * Combine common properties with tenant specific properties.
     *
     * @param commonProperties Common properties.
     * @param tenantProperties Tenant specific properties.
     * @param tenantId         Tenant id of the tenant.
     */
    protected static void buildTenantProperties(Map<String, String> commonProperties,
                                                Map<String, String> tenantProperties, int tenantId) {

        if (!commonProperties.containsKey(Constants.TENANT_PROPERTIES_KEY)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Tenant properties not defined in common agent config.");
            }
            return;
        }

        String tenantProps = commonProperties.get(Constants.TENANT_PROPERTIES_KEY);
        if (StringUtils.isBlank(tenantProps)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Tenant properties empty in common agent config.");
            }
            return;
        }

        tenantProps = StringUtils.deleteWhitespace(tenantProps);
        String[] props = StringUtils.split(tenantProps, ',');

        Arrays.stream(StringUtils.split(tenantProps, ','))
                .map(StringUtils::deleteWhitespace)
                .filter(prop -> StringUtils.isNotBlank(tenantProperties.get(prop)))
                .forEach(prop -> commonProperties.put(prop, tenantProperties.get(prop)));

        for (String prop : props) {
            String tenantPropValue = tenantProperties.get(prop);
            if (StringUtils.isNotBlank(tenantPropValue)) {
                commonProperties.put(prop, tenantPropValue);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Property %s not available in tenant %s agent user-store config. " +
                                    "Proceeding with the property value '%s' from the common config.", prop,
                            tenantId, commonProperties.get(prop)));
                }
            }
        }
    }

    /**
     * Set the datasource to be used in the agent store manager.
     *
     * @throws UserStoreException Thrown when there is an exception.
     */
    protected void setDatasource() throws UserStoreException {

        startSuperTenantFlow();
        DataSource commonDataSource;
        try {
            commonDataSource = DatabaseUtil.createUserStoreDataSource(realmConfig);
        } catch (Exception e) {
            throw new UserStoreException("Error while retrieving agent user-store datasource.", e);
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

        if (commonDataSource == null) {
            // Breaking the flow as cannot proceed without the datasource.
            throw new UserStoreException("Error while retrieving agent user-store datasource.");
        }

        // Overriding already set datasource details.
        jdbcds = commonDataSource;
    }

    private void startSuperTenantFlow() {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(MultitenantConstants.SUPER_TENANT_ID);
    }
}
