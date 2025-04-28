/*
 * Copyright (c) 2025, WSO2 Inc. (http://www.wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.agent.v1.common;

/**
 * Constant class for Agent endpoint.
 */
public class AgentConstants {

    public static final String AGENT_PREFIX = "AGENT-";
    public static final String AGENT_STORE = "AGENT_STORE";

    public static final String AUTHENTICATED_WITH_AGENT = "AuthenticatedWithAgent";


    /**
     * Enum for Agent related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        SERVER_ERROR_RETRIEVING_AGENT_DETAILS("25001",
                "Unable to retrieve agent details.",
                "Failed to retrieve agent details for the given identifier."),
        SERVER_ERROR_UPDATING_AGENT_DETAILS("25002",
                "Unable to update agent details.",
                "Failed to update agent details for the given identifier."),
        SERVER_ERROR_DELETING_AGENT("25003",
                "Unable to delete agent.",
                "Failed to delete the agent for the given identifier."),
        SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER("15004",
                "Unable to retrieve userstore manager",
                "AgentService failed while trying to get the userstore manager from user realm of the user : %s"),


        USER_ERROR_UNAUTHORIZED_ACCESS("20001",
                "Access denied.",
                "Unauthorized access to the agent resource."),
        USER_ERROR_INVALID_AGENT_TYPE("20002",
                "Invalid agent type provided.",
                "The provided agent type is not valid."),
        USER_ERROR_AGENT_NOT_FOUND("20003",
                "Agent not found.",
                "No agent found for the given identifier.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return AGENT_PREFIX + code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return code + " | " + message;
        }
    }
}
