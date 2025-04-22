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

package org.wso2.carbon.identity.agent.endpoint.exception;

import org.wso2.carbon.identity.base.IdentityException;

/**
 * This class represents the Agent Auth Exception.
 */
public class AgentAuthException extends IdentityException {

    /**
     * Constructor with error message.
     *
     * @param message Error message.
     */
    public AgentAuthException(String message) {

        super(message);
    }

    /**
     * Constructor with error code and error message.
     *
     * @param errorCode Error code.
     * @param message Error message.
     */
    public AgentAuthException(String errorCode, String message) {

        super(errorCode, message);
    }

    /**
     * Constructor with error message and throwable.
     *
     * @param message Error message.
     * @param cause Throwable.
     */
    public AgentAuthException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Constructor with error code, error message and throwable.
     *
     * @param errorCode Error code.
     * @param message Error message.
     * @param cause Throwable.
     */
    public AgentAuthException(String errorCode, String message, Throwable cause) {

        super(errorCode, message, cause);
    }
}
