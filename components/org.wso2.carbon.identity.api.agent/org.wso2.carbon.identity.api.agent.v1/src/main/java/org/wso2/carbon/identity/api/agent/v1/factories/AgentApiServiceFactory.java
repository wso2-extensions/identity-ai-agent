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

package org.wso2.carbon.identity.api.agent.v1.factories;

import org.wso2.carbon.identity.api.agent.v1.core.AgentService;
import org.wso2.carbon.identity.api.agent.v1.util.AgentServiceHolder;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for Agent API service
 */
public class AgentApiServiceFactory {

  private AgentApiServiceFactory() {

  }

  private static class AgentApiServicesHolder {

      private static final AgentService SERVICE = createServiceInstance();
  }

  private static AgentService createServiceInstance() {

      RealmService realmService = getRealmService();
      return new AgentService(realmService);
  }

  /**
   * Get Agent service.
   *
   * @return AgentService.
   */
  public static AgentService getAgentService() {

      return AgentApiServicesHolder.SERVICE;
  }

  /**
   *  Get agent realmservice
   * @return
   */
  private static RealmService getRealmService() {

      RealmService service = AgentServiceHolder.getRealmService();
      if (service == null) {
          throw new IllegalStateException("RealmService is not available from OSGi context.");
      }
      return service;
  }
}
