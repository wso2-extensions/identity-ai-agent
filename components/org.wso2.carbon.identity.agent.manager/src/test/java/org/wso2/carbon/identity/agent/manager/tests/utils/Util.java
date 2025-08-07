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

package org.wso2.carbon.identity.agent.manager.tests.utils;

import org.wso2.carbon.identity.agent.manager.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.user.store.configuration.dto.PropertyDTO;
import org.wso2.carbon.identity.user.store.configuration.dto.UserStoreDTO;
import org.wso2.carbon.user.api.RealmConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Util class for business user manager tests.
 */
public class Util {

    /**
     * Build a realm configuraiton from file
     *
     * @param path Path to configuration file.
     * @return Realm configuration.
     * @throws Exception exception.
     */
    public static RealmConfiguration getRealmConfig(String path) throws Exception {

        XMLProcessorUtil xmlProcessorUtil = new XMLProcessorUtil(path);
        return xmlProcessorUtil.buildUserStoreConfigurationFromFile();
    }

    /**
     * Get UserStoreDTO from realm config.
     *
     * @param realmConfig       Realm config.
     * @param filterForConsumer Denote to filter properties
     *                          for a consumer user store.
     * @return UserStoreDTO.
     * @throws Exception exception.
     */
    public static UserStoreDTO getUserStoreConfig(RealmConfiguration realmConfig,
                                                  boolean filterForConsumer) throws Exception {

        UserStoreDTO userStoreConfig = new UserStoreDTO();
        userStoreConfig.setClassName(realmConfig.getUserStoreClass());
        userStoreConfig.setDomainId(realmConfig.getUserStoreProperties().get(Constants.DOMAIN_NAME_KEY));
        if (realmConfig.getUserStoreProperties().containsKey(Constants.DESCRIPTION_KEY)) {
            userStoreConfig.setDescription(realmConfig.getUserStoreProperties().get(Constants.DESCRIPTION_KEY));
        }
        userStoreConfig.setProperties(getUserStoreProperties(realmConfig.getUserStoreProperties(),
                filterForConsumer));

        return userStoreConfig;
    }

    private static PropertyDTO[] getUserStoreProperties(Map<String, String> commonProperties, boolean filterForConsumer)
            throws Exception {

        if (filterForConsumer) {
            if (!commonProperties.containsKey(Constants.TENANT_PROPERTIES_KEY)) {
                throw new Exception("Tenant allowed property not defined in the common consumer userstore config. " +
                        "Proceeding with adding the coded configs.");
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
                    throw new Exception("Tenant allowed property " + propKey
                            + " not available in consumer userstore config.");
                }
            }

            return propertyDTOS;
        } else {
            List<PropertyDTO> propertyDTOS = new ArrayList<>();
            commonProperties.forEach((k, v) -> propertyDTOS.add(new PropertyDTO(k, v)));
            return propertyDTOS.toArray(new PropertyDTO[0]);
        }
    }
}
