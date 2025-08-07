# Agent UserStore Manager

## Build
```
mvn clean install
```

## Deployment
### DB schema

#### MySQL
```sql
CREATE TABLE UM_ROLE (
             UM_ID INTEGER NOT NULL AUTO_INCREMENT,
             UM_ROLE_NAME VARCHAR(255) NOT NULL,
             UM_TENANT_ID INTEGER DEFAULT 0,
		UM_SHARED_ROLE BOOLEAN DEFAULT FALSE,
             PRIMARY KEY (UM_ID, UM_TENANT_ID),
             UNIQUE(UM_ROLE_NAME, UM_TENANT_ID)
)ENGINE INNODB;

CREATE TABLE UM_USER (
             UM_ID INTEGER NOT NULL AUTO_INCREMENT,
             UM_USER_ID VARCHAR(255) NOT NULL,
             UM_USER_NAME VARCHAR(255) NOT NULL,
             UM_USER_PASSWORD VARCHAR(255) NOT NULL,
             UM_SALT_VALUE VARCHAR(31),
             UM_REQUIRE_CHANGE BOOLEAN DEFAULT FALSE,
             UM_CHANGED_TIME TIMESTAMP NOT NULL,
             UM_TENANT_ID INTEGER DEFAULT 0,
             PRIMARY KEY (UM_ID, UM_TENANT_ID),
             UNIQUE(UM_USER_ID, UM_TENANT_ID)
)ENGINE INNODB;

CREATE TABLE UM_USER_ATTRIBUTE (
            UM_ID INTEGER NOT NULL AUTO_INCREMENT,
            UM_ATTR_NAME VARCHAR(255) NOT NULL,
            UM_ATTR_VALUE VARCHAR(1024),
            UM_PROFILE_ID VARCHAR(255),
            UM_USER_ID INTEGER,
            UM_TENANT_ID INTEGER DEFAULT 0,
            FOREIGN KEY (UM_USER_ID, UM_TENANT_ID) REFERENCES UM_USER(UM_ID, UM_TENANT_ID),
            PRIMARY KEY (UM_ID, UM_TENANT_ID)
)ENGINE INNODB;


CREATE INDEX UM_USER_ID_INDEX ON UM_USER_ATTRIBUTE(UM_USER_ID);
CREATE INDEX UM_ATTR_NAME_VALUE_INDEX ON UM_USER_ATTRIBUTE(UM_ATTR_NAME, UM_ATTR_VALUE);

CREATE TABLE UM_USER_ROLE (
             UM_ID INTEGER NOT NULL AUTO_INCREMENT,
             UM_ROLE_ID INTEGER NOT NULL,
             UM_USER_ID INTEGER NOT NULL,
             UM_TENANT_ID INTEGER DEFAULT 0,
             UNIQUE (UM_USER_ID, UM_ROLE_ID, UM_TENANT_ID),
             FOREIGN KEY (UM_ROLE_ID, UM_TENANT_ID) REFERENCES UM_ROLE(UM_ID, UM_TENANT_ID),
             FOREIGN KEY (UM_USER_ID, UM_TENANT_ID) REFERENCES UM_USER(UM_ID, UM_TENANT_ID),
             PRIMARY KEY (UM_ID, UM_TENANT_ID)
)ENGINE INNODB;
```

#### MSSQL
```sql
IF NOT  EXISTS (SELECT * FROM SYS.OBJECTS WHERE OBJECT_ID = OBJECT_ID(N'[dbo].[UM_ROLE]') AND TYPE IN (N'U'))
CREATE TABLE UM_ROLE (
             UM_ID INTEGER IDENTITY(1,1) NOT NULL,
             UM_ROLE_NAME VARCHAR(255) NOT NULL,
             UM_TENANT_ID INTEGER DEFAULT 0,
		     UM_SHARED_ROLE BIT DEFAULT 0,
             PRIMARY KEY (UM_ID, UM_TENANT_ID),
             UNIQUE(UM_ROLE_NAME, UM_TENANT_ID)
);

IF NOT EXISTS (SELECT * FROM SYS.OBJECTS WHERE OBJECT_ID = OBJECT_ID(N'[dbo].[UM_USER]') AND TYPE IN (N'U'))
CREATE TABLE  UM_USER (
             UM_ID INTEGER IDENTITY(1,1) NOT NULL,
             UM_USER_ID VARCHAR(255) NOT NULL,
             UM_USER_NAME VARCHAR(255) NOT NULL,
             UM_USER_PASSWORD VARCHAR(255) NOT NULL,
             UM_SALT_VALUE VARCHAR(31),
             UM_REQUIRE_CHANGE BIT DEFAULT 0,
             UM_CHANGED_TIME DATETIME NOT NULL,
             UM_TENANT_ID INTEGER DEFAULT 0,
             PRIMARY KEY (UM_ID, UM_TENANT_ID),
             UNIQUE(UM_USER_ID, UM_TENANT_ID)
);

IF NOT EXISTS (SELECT * FROM SYS.OBJECTS WHERE OBJECT_ID = OBJECT_ID(N'[dbo].[UM_USER_ATTRIBUTE]') AND TYPE IN (N'U'))
CREATE TABLE  UM_USER_ATTRIBUTE (
             UM_ID INTEGER IDENTITY(1,1) NOT NULL,
			UM_ATTR_NAME VARCHAR(255) NOT NULL,
			UM_ATTR_VALUE VARCHAR(1024),
			UM_PROFILE_ID VARCHAR(255),
			UM_USER_ID INTEGER,
            UM_TENANT_ID INTEGER DEFAULT 0,
			FOREIGN KEY (UM_USER_ID, UM_TENANT_ID) REFERENCES UM_USER(UM_ID, UM_TENANT_ID),
			PRIMARY KEY (UM_ID, UM_TENANT_ID));

IF EXISTS (SELECT NAME FROM SYSINDEXES WHERE NAME = 'UM_USER_ID_INDEX')
DROP INDEX UM_USER_ATTRIBUTE.UM_USER_ID_INDEX
CREATE INDEX UM_USER_ID_INDEX ON UM_USER_ATTRIBUTE(UM_USER_ID);

CREATE INDEX UM_ATTR_NAME_VALUE_INDEX ON UM_USER_ATTRIBUTE(UM_ATTR_NAME, UM_ATTR_VALUE);

IF NOT EXISTS (SELECT * FROM SYS.OBJECTS WHERE OBJECT_ID = OBJECT_ID(N'[dbo].[UM_USER_ROLE]') AND TYPE IN (N'U'))
CREATE TABLE  UM_USER_ROLE (
             UM_ID INTEGER IDENTITY(1,1) NOT NULL,
             UM_ROLE_ID INTEGER NOT NULL,
             UM_USER_ID INTEGER NOT NULL,
             UM_TENANT_ID INTEGER DEFAULT 0,
             UNIQUE (UM_USER_ID, UM_ROLE_ID, UM_TENANT_ID),
             FOREIGN KEY (UM_ROLE_ID, UM_TENANT_ID) REFERENCES UM_ROLE(UM_ID, UM_TENANT_ID),
             FOREIGN KEY (UM_USER_ID, UM_TENANT_ID) REFERENCES UM_USER(UM_ID, UM_TENANT_ID),
             PRIMARY KEY (UM_ID, UM_TENANT_ID)
);
```

### Identity Server configurations

#### DEFAULT.xml.j2
Create a file named `DEFAULT.xml.j2` with the below content and add it to <HOME>/repository/resources/conf/templates/repository/conf/DEFAULT.xml.j2

```xml
<?xml version="1.0" encoding="UTF-8"?>
<UserStoreManager xmlns:svns="http://org.wso2.securevault/configuration"
                  class="org.wso2.carbon.identity.agent.manager.AgentUserStoreManager">
    <Property name="dataSource">{{agent_userstore.data_source}}</Property>
    <Property name="Disabled">false</Property>
    <Property name="ReadOnly">false</Property>
    <Property name="ReadGroups">true</Property>
    <Property name="isConsumerUserstore">true</Property>
    <Property name="TenantProperties">{{agent_userstore.tenant_properties}}</Property>
    <Property name="TenantEditableProperties">{{agent_userstore.tenant_editable_properties}}</Property>
    <Property name="WriteGroups">true</Property>
    <Property name="UsernameJavaRegEx">
        ([a-zA-Z0-9._\-|]{3,50}$|(?=^.{3,50}$)^[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4})
    </Property>
    <Property name="UsernameJavaScriptRegEx">^[\S]{3,30}$</Property>
    <Property name="UsernameJavaRegExViolationErrorMsg">Username pattern policy violated.</Property>
    <Property name="PasswordJavaRegEx">^[\S]{5,30}$</Property>
    <Property name="PasswordJavaScriptRegEx">^[\S]{5,30}$</Property>
    <Property name="PasswordJavaRegExViolationErrorMsg">Password pattern policy violated</Property>
    <Property name="RolenameJavaRegEx">^[\S]{3,30}$</Property>
    <Property name="RolenameJavaScriptRegEx">^[\S]{3,30}$</Property>
    <Property name="CaseInsensitiveUsername">false</Property>
    <Property name="IsBulkImportSupported">false</Property>
    <Property name="PasswordDigest">SHA-256</Property>
    <Property name="MultiAttributeSeparator">,</Property>
    <Property name="StoreSaltedPassword">true</Property>
    <Property name="MaxUserNameListLength">100</Property>
    <Property name="MaxRoleNameListLength">100</Property>
    <Property name="UserRolesCacheEnabled">true</Property>
    <Property name="UserNameUniqueAcrossTenants">false</Property>
    <Property name="validationQuery"/>
    <Property name="validationInterval"/>
    <Property name="defaultAutoCommit"/>
    <Property name="defaultReadOnly"/>
    <Property name="defaultTransactionIsolation"/>
    <Property name="defaultCatalog"/>
    <Property name="initialSize"/>
    <Property name="testOnReturn">false</Property>
    <Property name="testOnBorrow">false</Property>
    <Property name="validatorClassName"/>
    <Property name="numTestsPerEvictionRun"/>
    <Property name="accessToUnderlyingConnectionAllowed"/>
    <Property name="removeAbandoned">false</Property>
    <Property name="removeAbandonedTimeout"/>
    <Property name="logAbandoned">false</Property>
    <Property name="connectionProperties"/>
    <Property name="initSQL"/>
    <Property name="jdbcInterceptors"/>
    <Property name="jmxEnabled">true</Property>
    <Property name="fairQueue">true</Property>
    <Property name="abandonWhenPercentageFull"/>
    <Property name="maxAge"/>
    <Property name="useEquals">true</Property>
    <Property name="suspectTimeout"/>
    <Property name="validationQueryTimeout"/>
    <Property name="alternateUsernameAllowed">false</Property>
    <Property name="commitOnReturn">false</Property>
    <Property name="rollbackOnReturn">false</Property>
    <Property name="CountRetrieverClass">org.wso2.carbon.identity.user.store.count.jdbc.JDBCUserStoreCountRetriever
    </Property>
    <Property name="ClaimOperationsSupported">true</Property>
    <Property name="DomainName">DEFAULT</Property>
    <Property name="Description"/>
</UserStoreManager>
```

#### deployment.toml
Add the following configs to the `deployment.toml` file.
```

[datasource.AgentIdentity]
id = "AgentIdentity"
url = "<connection url>"
username = "<connection username>"
password = "<connection password>"
driver = "<connection driver>"
# Add required pool options
# Ex:
# [datasource.DefaultUser.pool_options]
# validationQuery="SELECT 1"
# testOnBorrow = true
# maxActive=200
# maxWait = 60000 # wait in milliseconds
# minIdle = 5 # wait in milliseconds
# jmxEnabled = false
# validationInterval="30000"
# defaultAutoCommit=false

[agent_userstore]
data_source="jdbc/AgentIdentity" # jdbc/<datasource id>
tenant_properties="UsernameJavaRegEx,UsernameJavaScriptRegEx,UsernameJavaRegExViolationErrorMsg,PasswordJavaRegEx,PasswordJavaScriptRegEx,RolenameJavaRegEx,RolenameJavaScriptRegEx"
tenant_editable_properties="UsernameJavaRegEx,UsernameJavaScriptRegEx,UsernameJavaRegExViolationErrorMsg,PasswordJavaRegEx,PasswordJavaScriptRegEx,RolenameJavaRegEx,RolenameJavaScriptRegEx"
```

- Note: Above parameters support secure vault. 
- Note: Same pool options as the primary userstore db can be used. All tenants will be sharing connections from this datasource for agent userstore.
