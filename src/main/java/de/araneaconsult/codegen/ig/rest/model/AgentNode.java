/*
 * Identity Governance REST APIs
 * Welcome to the NetIQ Identity Governance API Documentation page. This is the API reference for the NetIQ Identity Governance REST API.  Below you will see the main sections of the API. Click each section in order to see the endpoints that are available in that category as well as information related to which Identity Governance roles have access. Global Administrators are not included in the accepted roles list for each API however they have access to all APIs. Those APIs that do not display a list of accepted roles are accessible for any role. An authenticated user is one that has the RT_ROLE_USER permission. The Operations Administrator SaaS is an example of a user that does not have the RT_ROLE_USER permission.  The NetIQ Identity Governance REST API uses the OAuth2 protocol for authentication. Therefore, in order to make any of these calls, you must obtain a token, and include that token in the Authentication header.  OSP = One SSO Provider   NAM = NetIQ Access Manager  **Note:** The various OAuth 2.0 endpoints described below can also be obtained from the OAuth/OpenID Connect provider 'metadata' found at the following location relative to the 'issuer URI':`[issuer-uri]/.well-known/openid-configuration`  Issuer URIs:  *   OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2 *   NAM: https://server/nidp/oauth/nam  See [Open ID Connect Discovery 1.0](\"https://openid.net/specs/openid-connect-discovery-1_0.html\") for more information.  Obtaining the Initial Access Token ==================================  ### OAuth 2.0 Resource Owner Password Credentials Grant Request  1.  Determine the OAuth 2.0 token endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/token     2.  NAM: https://server/nidp/oauth/nam/token 2.  Obtain the Identity Governance 'iac' client identifier and client secret.     1.  OSP         1.  The identifier is usually _iac_ but can be changed with the configutil or configupdate utilities.         2.  The client secret is the 'service password' specified during installation but can be changed with the configutil or configupdate utilities.     2.  NAM         1.  Open the Access Manager administrator console in a browser and navigate to the _OAuth & OpenID Connect_ tab on the _IDPCluster_ page. Select the _Client Applications_ heading.         2.  Click on the 'View' icon under the 'Actions' heading for the _Client Application_ named _iac_.         3.  Click on _Click to reveal_.         4.  Copy the _Client ID_ value and the _Client Secret_ value.         5.  Ensure that _Resource Owner Credentials_ appears in the _Grants Required_ list. If not, edit the client definition and check the _Resource Owner Credentials_ box, save the client definition, and update the IDP. 3.  Obtain the user identifier and password of a user with the required privilege for the desired API endpoint. 4.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 4.3.1](\"https://tools.ietf.org/html/rfc6749#section-4.3.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=password&username=<user-id>&password=[user-password]&client_id=[iac-client-id]&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the client and user values obtained in the steps above.  Also note the '**& amp;**' shown in this and other POST payload examples should actually be '**&**'. 5.  Issue the request to the OAuth 2.0 token endpoint. 6.  The JSON response will be similar to the following (see [RFC 6749 section 4.3.3](\"https://tools.ietf.org/html/rfc6749#section-4.3.3\")):`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119, \"refresh_token\": \"eyJhbGciOiJ...\" }` 7.  When issuing a REST request to an Identity Governance endpoint pass the access token value using an HTTP _Bearer_ authorization header (see [RFC 6750 section 2.1](\"https://tools.ietf.org/html/rfc6750#section-2.1\")):`Authorization: Bearer [access-token]`  Refresh Tokens ==============  If the authorization server is configured to return an OAuth 2.0 refresh token in the JSON result of the Resource Owner Password Credential Grant request then the refresh token should be used to obtain additional access tokens after the currently-valid access token expires.  In addition, each refresh token issued on behalf of a user causes a 'revocation entry' to be stored in an attribute on the user's LDAP object. Obtaining many refresh tokens without revoking previously obtained, unexpired refresh tokens will eventually exceed the capacity of the attribute on the user's LDAP object and will result in errors.  Therefore, if a refresh token is obtained it must be revoked after it is no longer needed.  ### Access Token Request  1.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 6](\"https://tools.ietf.org/html/rfc6749#section-6\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=refresh_token&refresh_token=<refresh-token>&client_id=<iac-clientid>&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the obvious values. 2.  Issue the request to the OAuth 2.0 token endpoint. 3.  The JSON result will be similar to`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119 }` 4.  Use the new access token value in requests to Identity Governance REST endpoints as described above.  ### Refresh Token Revocation Request  1.  Determine the OAuth 2.0 token revocation endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/revoke     2.  NAM: https://server/nidp/oauth/nam/revoke 2.  Create an HTTP POST request with the following characteristics (see [RFC 7009 section 2.1](\"https://tools.ietf.org/html/rfc7009#section-2.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body:`token=[refresh-token]&client_id=[iac-client-id]&client_secret=[iac-client-secret]` 3.  Issue the request to the OAuth 2.0 revocation endpoint.      As a shortcut to learning the API, run Identity Governance in a browser with developer tools enabled and watch the network traffic between the browser and the server.  REST API and Data/Service Access Rights =======================================  The authenticated user may need two kinds of rights to invoke a REST API:  1. Authorization to call the API. 2. Permission(s) to access data returned by the API or to access services the API uses.  The user's roles are checked to see if they have the required rights.  As an example, suppose that John Jones is the user, and the REST API he wants to call is **GET /data/perms/authorizedby/2/causes**, and it requires a permission named **ViewAuthResourceInfo**.  If John Jones does not have the authorization to call the API he will get an error that looks like this:  `{     \"Fault\": {        \"Code\": {           \"Value\": \"Sender\",           \"Subcode\": {              \"Value\": \"RestApiUnAuthorized\"           }        },        \"Reason\": {           \"Text\": \"Denying access to /data/perms/authorizedby/2/causes for user John Jones.\"        }     } }`  If John Jones is allowed to call the REST API, but does not have the **ViewAuthResourceInfo** permission, he would get an error that looks like this:  `{     \"Fault\": {        \"Code\": {           \"Value\": \"Sender\",           \"Subcode\": {              \"Value\": \"UnauthorizedDataAccess\"           }        },        \"Reason\": {           \"Text\": \"Unauthorized data access attempt: User [John Jones] has no right [ViewAuthResourceInfo] for requested data.\",           \"Stack\": null        }     } }`  To determine what permissions a particular role has, you can query the OPS database.  For example, the query below returns all permissions for the **Fulfillment Administrator** role.  To get permissions for a different role, just change **'Fulfillment Administrator'** value to the name of the role you want:  ` SELECT distinct     ar.role_name as role_short_name,     ar.role_display_name as role_display_name,     ap.permission_name as permission_name FROM auth_role_mapping arm JOIN auth_role ar on ar.id = arm.auth_role JOIN auth_scope asp on asp.id = arm.auth_scope JOIN auth_permission ap on ap.id = arm.auth_perm WHERE     asp.rest_api_uri IS NULL     and ar.role_display_name = 'Fulfillment Administrator' order by ar.role_display_name, ap.permission_name `  * * *
 *
 * OpenAPI spec version: 4.2.0-644
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package de.araneaconsult.codegen.ig.rest.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import de.araneaconsult.codegen.ig.rest.model.DataSourceConnectionList;
import de.araneaconsult.codegen.ig.rest.model.KafkaPropertyList;
import de.araneaconsult.codegen.ig.rest.model.KafkaPropertyNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AgentNode
 */



public class AgentNode {
  @SerializedName("commandTopic")
  private String commandTopic = null;

  @SerializedName("dataSourceConnectionList")
  private DataSourceConnectionList dataSourceConnectionList = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("environment")
  private String environment = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("kafkaPropertyList")
  private KafkaPropertyList kafkaPropertyList = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("m_commandTopic")
  private String mCommandTopic = null;

  @SerializedName("m_dataSourceConnectionList")
  private DataSourceConnectionList mDataSourceConnectionList = null;

  @SerializedName("m_deleted")
  private Boolean mDeleted = null;

  @SerializedName("m_description")
  private String mDescription = null;

  @SerializedName("m_environment")
  private String mEnvironment = null;

  @SerializedName("m_id")
  private Long mId = null;

  @SerializedName("m_kafkaPropertyList")
  private KafkaPropertyList mKafkaPropertyList = null;

  @SerializedName("m_name")
  private String mName = null;

  @SerializedName("m_propertyList")
  private List<KafkaPropertyNode> mPropertyList = null;

  @SerializedName("m_region")
  private String mRegion = null;

  @SerializedName("m_responseTopic")
  private String mResponseTopic = null;

  @SerializedName("m_tenantId")
  private String mTenantId = null;

  @SerializedName("m_uniqueId")
  private String mUniqueId = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("propertyList")
  private List<KafkaPropertyNode> propertyList = null;

  @SerializedName("region")
  private String region = null;

  @SerializedName("responseTopic")
  private String responseTopic = null;

  @SerializedName("tenantId")
  private String tenantId = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  public AgentNode commandTopic(String commandTopic) {
    this.commandTopic = commandTopic;
    return this;
  }

   /**
   * the command topic
   * @return commandTopic
  **/
  @ApiModelProperty(value = "the command topic")
  public String getCommandTopic() {
    return commandTopic;
  }

  public void setCommandTopic(String commandTopic) {
    this.commandTopic = commandTopic;
  }

  public AgentNode dataSourceConnectionList(DataSourceConnectionList dataSourceConnectionList) {
    this.dataSourceConnectionList = dataSourceConnectionList;
    return this;
  }

   /**
   * Get dataSourceConnectionList
   * @return dataSourceConnectionList
  **/
  @ApiModelProperty(value = "")
  public DataSourceConnectionList getDataSourceConnectionList() {
    return dataSourceConnectionList;
  }

  public void setDataSourceConnectionList(DataSourceConnectionList dataSourceConnectionList) {
    this.dataSourceConnectionList = dataSourceConnectionList;
  }

  public AgentNode deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * the deleted
   * @return deleted
  **/
  @ApiModelProperty(value = "the deleted")
  public Boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public AgentNode description(String description) {
    this.description = description;
    return this;
  }

   /**
   * the description
   * @return description
  **/
  @ApiModelProperty(value = "the description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AgentNode environment(String environment) {
    this.environment = environment;
    return this;
  }

   /**
   * the environment
   * @return environment
  **/
  @ApiModelProperty(value = "the environment")
  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public AgentNode id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * the id
   * @return id
  **/
  @ApiModelProperty(value = "the id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AgentNode kafkaPropertyList(KafkaPropertyList kafkaPropertyList) {
    this.kafkaPropertyList = kafkaPropertyList;
    return this;
  }

   /**
   * Get kafkaPropertyList
   * @return kafkaPropertyList
  **/
  @ApiModelProperty(value = "")
  public KafkaPropertyList getKafkaPropertyList() {
    return kafkaPropertyList;
  }

  public void setKafkaPropertyList(KafkaPropertyList kafkaPropertyList) {
    this.kafkaPropertyList = kafkaPropertyList;
  }

  public AgentNode link(String link) {
    this.link = link;
    return this;
  }

   /**
   * Get link
   * @return link
  **/
  @ApiModelProperty(value = "")
  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public AgentNode mCommandTopic(String mCommandTopic) {
    this.mCommandTopic = mCommandTopic;
    return this;
  }

   /**
   * Get mCommandTopic
   * @return mCommandTopic
  **/
  @ApiModelProperty(value = "")
  public String getMCommandTopic() {
    return mCommandTopic;
  }

  public void setMCommandTopic(String mCommandTopic) {
    this.mCommandTopic = mCommandTopic;
  }

  public AgentNode mDataSourceConnectionList(DataSourceConnectionList mDataSourceConnectionList) {
    this.mDataSourceConnectionList = mDataSourceConnectionList;
    return this;
  }

   /**
   * Get mDataSourceConnectionList
   * @return mDataSourceConnectionList
  **/
  @ApiModelProperty(value = "")
  public DataSourceConnectionList getMDataSourceConnectionList() {
    return mDataSourceConnectionList;
  }

  public void setMDataSourceConnectionList(DataSourceConnectionList mDataSourceConnectionList) {
    this.mDataSourceConnectionList = mDataSourceConnectionList;
  }

  public AgentNode mDeleted(Boolean mDeleted) {
    this.mDeleted = mDeleted;
    return this;
  }

   /**
   * Get mDeleted
   * @return mDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isMDeleted() {
    return mDeleted;
  }

  public void setMDeleted(Boolean mDeleted) {
    this.mDeleted = mDeleted;
  }

  public AgentNode mDescription(String mDescription) {
    this.mDescription = mDescription;
    return this;
  }

   /**
   * Get mDescription
   * @return mDescription
  **/
  @ApiModelProperty(value = "")
  public String getMDescription() {
    return mDescription;
  }

  public void setMDescription(String mDescription) {
    this.mDescription = mDescription;
  }

  public AgentNode mEnvironment(String mEnvironment) {
    this.mEnvironment = mEnvironment;
    return this;
  }

   /**
   * Get mEnvironment
   * @return mEnvironment
  **/
  @ApiModelProperty(value = "")
  public String getMEnvironment() {
    return mEnvironment;
  }

  public void setMEnvironment(String mEnvironment) {
    this.mEnvironment = mEnvironment;
  }

  public AgentNode mId(Long mId) {
    this.mId = mId;
    return this;
  }

   /**
   * Get mId
   * @return mId
  **/
  @ApiModelProperty(value = "")
  public Long getMId() {
    return mId;
  }

  public void setMId(Long mId) {
    this.mId = mId;
  }

  public AgentNode mKafkaPropertyList(KafkaPropertyList mKafkaPropertyList) {
    this.mKafkaPropertyList = mKafkaPropertyList;
    return this;
  }

   /**
   * Get mKafkaPropertyList
   * @return mKafkaPropertyList
  **/
  @ApiModelProperty(value = "")
  public KafkaPropertyList getMKafkaPropertyList() {
    return mKafkaPropertyList;
  }

  public void setMKafkaPropertyList(KafkaPropertyList mKafkaPropertyList) {
    this.mKafkaPropertyList = mKafkaPropertyList;
  }

  public AgentNode mName(String mName) {
    this.mName = mName;
    return this;
  }

   /**
   * Get mName
   * @return mName
  **/
  @ApiModelProperty(value = "")
  public String getMName() {
    return mName;
  }

  public void setMName(String mName) {
    this.mName = mName;
  }

  public AgentNode mPropertyList(List<KafkaPropertyNode> mPropertyList) {
    this.mPropertyList = mPropertyList;
    return this;
  }

  public AgentNode addMPropertyListItem(KafkaPropertyNode mPropertyListItem) {
    if (this.mPropertyList == null) {
      this.mPropertyList = new ArrayList<KafkaPropertyNode>();
    }
    this.mPropertyList.add(mPropertyListItem);
    return this;
  }

   /**
   * Get mPropertyList
   * @return mPropertyList
  **/
  @ApiModelProperty(value = "")
  public List<KafkaPropertyNode> getMPropertyList() {
    return mPropertyList;
  }

  public void setMPropertyList(List<KafkaPropertyNode> mPropertyList) {
    this.mPropertyList = mPropertyList;
  }

  public AgentNode mRegion(String mRegion) {
    this.mRegion = mRegion;
    return this;
  }

   /**
   * Get mRegion
   * @return mRegion
  **/
  @ApiModelProperty(value = "")
  public String getMRegion() {
    return mRegion;
  }

  public void setMRegion(String mRegion) {
    this.mRegion = mRegion;
  }

  public AgentNode mResponseTopic(String mResponseTopic) {
    this.mResponseTopic = mResponseTopic;
    return this;
  }

   /**
   * Get mResponseTopic
   * @return mResponseTopic
  **/
  @ApiModelProperty(value = "")
  public String getMResponseTopic() {
    return mResponseTopic;
  }

  public void setMResponseTopic(String mResponseTopic) {
    this.mResponseTopic = mResponseTopic;
  }

  public AgentNode mTenantId(String mTenantId) {
    this.mTenantId = mTenantId;
    return this;
  }

   /**
   * Get mTenantId
   * @return mTenantId
  **/
  @ApiModelProperty(value = "")
  public String getMTenantId() {
    return mTenantId;
  }

  public void setMTenantId(String mTenantId) {
    this.mTenantId = mTenantId;
  }

  public AgentNode mUniqueId(String mUniqueId) {
    this.mUniqueId = mUniqueId;
    return this;
  }

   /**
   * Get mUniqueId
   * @return mUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getMUniqueId() {
    return mUniqueId;
  }

  public void setMUniqueId(String mUniqueId) {
    this.mUniqueId = mUniqueId;
  }

  public AgentNode name(String name) {
    this.name = name;
    return this;
  }

   /**
   * the name
   * @return name
  **/
  @ApiModelProperty(value = "the name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AgentNode propertyList(List<KafkaPropertyNode> propertyList) {
    this.propertyList = propertyList;
    return this;
  }

  public AgentNode addPropertyListItem(KafkaPropertyNode propertyListItem) {
    if (this.propertyList == null) {
      this.propertyList = new ArrayList<KafkaPropertyNode>();
    }
    this.propertyList.add(propertyListItem);
    return this;
  }

   /**
   * the property list
   * @return propertyList
  **/
  @ApiModelProperty(value = "the property list")
  public List<KafkaPropertyNode> getPropertyList() {
    return propertyList;
  }

  public void setPropertyList(List<KafkaPropertyNode> propertyList) {
    this.propertyList = propertyList;
  }

  public AgentNode region(String region) {
    this.region = region;
    return this;
  }

   /**
   * the data center region
   * @return region
  **/
  @ApiModelProperty(value = "the data center region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public AgentNode responseTopic(String responseTopic) {
    this.responseTopic = responseTopic;
    return this;
  }

   /**
   * the response topic
   * @return responseTopic
  **/
  @ApiModelProperty(value = "the response topic")
  public String getResponseTopic() {
    return responseTopic;
  }

  public void setResponseTopic(String responseTopic) {
    this.responseTopic = responseTopic;
  }

  public AgentNode tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * the name
   * @return tenantId
  **/
  @ApiModelProperty(value = "the name")
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AgentNode uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

   /**
   * the unique id
   * @return uniqueId
  **/
  @ApiModelProperty(value = "the unique id")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AgentNode agentNode = (AgentNode) o;
    return Objects.equals(this.commandTopic, agentNode.commandTopic) &&
        Objects.equals(this.dataSourceConnectionList, agentNode.dataSourceConnectionList) &&
        Objects.equals(this.deleted, agentNode.deleted) &&
        Objects.equals(this.description, agentNode.description) &&
        Objects.equals(this.environment, agentNode.environment) &&
        Objects.equals(this.id, agentNode.id) &&
        Objects.equals(this.kafkaPropertyList, agentNode.kafkaPropertyList) &&
        Objects.equals(this.link, agentNode.link) &&
        Objects.equals(this.mCommandTopic, agentNode.mCommandTopic) &&
        Objects.equals(this.mDataSourceConnectionList, agentNode.mDataSourceConnectionList) &&
        Objects.equals(this.mDeleted, agentNode.mDeleted) &&
        Objects.equals(this.mDescription, agentNode.mDescription) &&
        Objects.equals(this.mEnvironment, agentNode.mEnvironment) &&
        Objects.equals(this.mId, agentNode.mId) &&
        Objects.equals(this.mKafkaPropertyList, agentNode.mKafkaPropertyList) &&
        Objects.equals(this.mName, agentNode.mName) &&
        Objects.equals(this.mPropertyList, agentNode.mPropertyList) &&
        Objects.equals(this.mRegion, agentNode.mRegion) &&
        Objects.equals(this.mResponseTopic, agentNode.mResponseTopic) &&
        Objects.equals(this.mTenantId, agentNode.mTenantId) &&
        Objects.equals(this.mUniqueId, agentNode.mUniqueId) &&
        Objects.equals(this.name, agentNode.name) &&
        Objects.equals(this.propertyList, agentNode.propertyList) &&
        Objects.equals(this.region, agentNode.region) &&
        Objects.equals(this.responseTopic, agentNode.responseTopic) &&
        Objects.equals(this.tenantId, agentNode.tenantId) &&
        Objects.equals(this.uniqueId, agentNode.uniqueId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commandTopic, dataSourceConnectionList, deleted, description, environment, id, kafkaPropertyList, link, mCommandTopic, mDataSourceConnectionList, mDeleted, mDescription, mEnvironment, mId, mKafkaPropertyList, mName, mPropertyList, mRegion, mResponseTopic, mTenantId, mUniqueId, name, propertyList, region, responseTopic, tenantId, uniqueId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AgentNode {\n");
    
    sb.append("    commandTopic: ").append(toIndentedString(commandTopic)).append("\n");
    sb.append("    dataSourceConnectionList: ").append(toIndentedString(dataSourceConnectionList)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    kafkaPropertyList: ").append(toIndentedString(kafkaPropertyList)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    mCommandTopic: ").append(toIndentedString(mCommandTopic)).append("\n");
    sb.append("    mDataSourceConnectionList: ").append(toIndentedString(mDataSourceConnectionList)).append("\n");
    sb.append("    mDeleted: ").append(toIndentedString(mDeleted)).append("\n");
    sb.append("    mDescription: ").append(toIndentedString(mDescription)).append("\n");
    sb.append("    mEnvironment: ").append(toIndentedString(mEnvironment)).append("\n");
    sb.append("    mId: ").append(toIndentedString(mId)).append("\n");
    sb.append("    mKafkaPropertyList: ").append(toIndentedString(mKafkaPropertyList)).append("\n");
    sb.append("    mName: ").append(toIndentedString(mName)).append("\n");
    sb.append("    mPropertyList: ").append(toIndentedString(mPropertyList)).append("\n");
    sb.append("    mRegion: ").append(toIndentedString(mRegion)).append("\n");
    sb.append("    mResponseTopic: ").append(toIndentedString(mResponseTopic)).append("\n");
    sb.append("    mTenantId: ").append(toIndentedString(mTenantId)).append("\n");
    sb.append("    mUniqueId: ").append(toIndentedString(mUniqueId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    propertyList: ").append(toIndentedString(propertyList)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    responseTopic: ").append(toIndentedString(responseTopic)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
