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

import org.wso2.carbon.utils.CarbonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Util class for business user userstore.
 */
public class Util {

    private Util() {

    }

    /**
     * Get business user userstore configuration path.
     *
     * @return business user userstore configuration path.
     */
    public static Path getAgentUSConfigPath() {

        return Paths.get(CarbonUtils.getCarbonConfigDirPath(), "identity", "agent" ,
                Constants.USERSTORE_CONFIG_FILE);
    }
}
