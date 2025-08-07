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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.config.UserStoreConfigXMLProcessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

/**
 * Builds a stripped down version of a realm config for testing purposes.
 */
public class XMLProcessorUtil extends UserStoreConfigXMLProcessor {

    private static final String MAX_USER_NAME_LIST_LENGTH = "MaxUserNameListLength";
    private static final String PASSWORDS_EXTERNALLY_MANAGED = "PasswordsExternallyManaged";
    private static final String READ_ONLY = "ReadOnly";
    private static final String MULTIPLE_CREDENTIALS = "MultipleCredentials";
    private static final String CREDENTIAL = "Credential";
    private final String filePath;

    public XMLProcessorUtil(String path) {

        super(path);
        this.filePath = path;
    }

    @Override
    public RealmConfiguration buildUserStoreConfigurationFromFile() throws UserStoreException {

        try {
            OMElement realmElement = this.getRealmElement();
            return this.buildUserStoreConfiguration(realmElement);
        } catch (Exception e) {
            String message = "Error while building user store manager from file";
            throw new UserStoreException(message, e);
        }
    }

    @Override
    public RealmConfiguration buildUserStoreConfiguration(OMElement userStoreElement) {

        boolean passwordsExternallyManaged = false;
        RealmConfiguration realmConfig = new RealmConfiguration();
        String userStoreClass = userStoreElement.getAttributeValue(new QName("class"));
        Map<String, String> userStoreProperties = this.getChildPropertyElements(userStoreElement);
        String sIsPasswordExternallyManaged = userStoreProperties.get(PASSWORDS_EXTERNALLY_MANAGED);
        if (null != sIsPasswordExternallyManaged && !sIsPasswordExternallyManaged.trim().equals("")) {
            passwordsExternallyManaged = Boolean.parseBoolean(sIsPasswordExternallyManaged);
        }
        Map<String, String> multipleCredentialsProperties = this.getMultipleCredentialsProperties(userStoreElement);
        realmConfig.setUserStoreClass(userStoreClass);
        realmConfig.setUserStoreProperties(userStoreProperties);
        realmConfig.setPasswordsExternallyManaged(passwordsExternallyManaged);
        realmConfig.addMultipleCredentialProperties(userStoreClass, multipleCredentialsProperties);
        if (realmConfig.getUserStoreProperty(MAX_USER_NAME_LIST_LENGTH) == null) {
            realmConfig.getUserStoreProperties().put(MAX_USER_NAME_LIST_LENGTH, "100");
        }
        if (realmConfig.getUserStoreProperty(READ_ONLY) == null) {
            realmConfig.getUserStoreProperties().put(READ_ONLY, "false");
        }
        return realmConfig;
    }

    private Map<String, String> getChildPropertyElements(OMElement omElement) {

        Map<String, String> map = new HashMap<>();
        Iterator<?> ite = omElement.getChildrenWithName(new QName("Property"));

        while (ite.hasNext()) {
            OMElement propElem = (OMElement) ite.next();
            String propName = propElem.getAttributeValue(new QName("name"));
            String propValue = propElem.getText();

            if (propName != null && propValue != null) {
                map.put(propName.trim(), propValue.trim());
            }
        }
        return map;
    }

    private Map<String, String> getMultipleCredentialsProperties(OMElement omElement) {

        Map<String, String> map = new HashMap<>();
        OMElement multipleCredentialsEl = omElement.getFirstChildWithName(new QName(MULTIPLE_CREDENTIALS));
        if (multipleCredentialsEl != null) {
            Iterator ite = multipleCredentialsEl.getChildrenWithLocalName(CREDENTIAL);

            while (ite.hasNext()) {
                Object omObj = ite.next();
                if (omObj instanceof OMElement) {
                    OMElement credsElem = (OMElement) omObj;
                    String credsType = credsElem.getAttributeValue(new QName("type"));
                    String credsClassName = credsElem.getText();
                    map.put(credsType.trim(), credsClassName.trim());
                }
            }
        }

        return map;
    }

    private OMElement getRealmElement() throws XMLStreamException, IOException {

        InputStream inStream = new FileInputStream(this.filePath);
        OMElement elm;
        try {
            StAXOMBuilder builder = new StAXOMBuilder(inStream);
            elm = builder.getDocumentElement();
        } finally {
            inStream.close();
        }

        return elm;
    }
}
