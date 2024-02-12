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
import de.araneaconsult.codegen.ig.rest.model.Authorizedby;
import de.araneaconsult.codegen.ig.rest.model.EntityCategory;
import de.araneaconsult.codegen.ig.rest.model.PermOwner;
import de.araneaconsult.codegen.ig.rest.model.PermissionAssignmentInfo;
import de.araneaconsult.codegen.ig.rest.model.RequestPolicy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * MinimalPerm
 */



public class MinimalPerm {
  @SerializedName("authorizedBy")
  private List<Authorizedby> authorizedBy = null;

  @SerializedName("boundPerms")
  private List<String> boundPerms = null;

  @SerializedName("boundPermsType")
  private String boundPermsType = null;

  @SerializedName("cost")
  private Long cost = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("dynamic")
  private Boolean dynamic = null;

  @SerializedName("entityCategories")
  private List<EntityCategory> entityCategories = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("instanceGuid")
  private String instanceGuid = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("owners")
  private List<PermOwner> owners = null;

  @SerializedName("permissionAssignmentInfo")
  private PermissionAssignmentInfo permissionAssignmentInfo = null;

  @SerializedName("permissionAssignmentInfoList")
  private List<PermissionAssignmentInfo> permissionAssignmentInfoList = null;

  @SerializedName("requestApprovalPolicy")
  private RequestPolicy requestApprovalPolicy = null;

  @SerializedName("requestPolicy")
  private RequestPolicy requestPolicy = null;

  @SerializedName("risk")
  private Long risk = null;

  @SerializedName("type")
  private String type = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("userCount")
  private Long userCount = null;

  public MinimalPerm authorizedBy(List<Authorizedby> authorizedBy) {
    this.authorizedBy = authorizedBy;
    return this;
  }

  public MinimalPerm addAuthorizedByItem(Authorizedby authorizedByItem) {
    if (this.authorizedBy == null) {
      this.authorizedBy = new ArrayList<Authorizedby>();
    }
    this.authorizedBy.add(authorizedByItem);
    return this;
  }

   /**
   * Get authorizedBy
   * @return authorizedBy
  **/
  @ApiModelProperty(value = "")
  public List<Authorizedby> getAuthorizedBy() {
    return authorizedBy;
  }

  public void setAuthorizedBy(List<Authorizedby> authorizedBy) {
    this.authorizedBy = authorizedBy;
  }

  public MinimalPerm boundPerms(List<String> boundPerms) {
    this.boundPerms = boundPerms;
    return this;
  }

  public MinimalPerm addBoundPermsItem(String boundPermsItem) {
    if (this.boundPerms == null) {
      this.boundPerms = new ArrayList<String>();
    }
    this.boundPerms.add(boundPermsItem);
    return this;
  }

   /**
   * Get boundPerms
   * @return boundPerms
  **/
  @ApiModelProperty(value = "")
  public List<String> getBoundPerms() {
    return boundPerms;
  }

  public void setBoundPerms(List<String> boundPerms) {
    this.boundPerms = boundPerms;
  }

  public MinimalPerm boundPermsType(String boundPermsType) {
    this.boundPermsType = boundPermsType;
    return this;
  }

   /**
   * Get boundPermsType
   * @return boundPermsType
  **/
  @ApiModelProperty(value = "")
  public String getBoundPermsType() {
    return boundPermsType;
  }

  public void setBoundPermsType(String boundPermsType) {
    this.boundPermsType = boundPermsType;
  }

  public MinimalPerm cost(Long cost) {
    this.cost = cost;
    return this;
  }

   /**
   * Get cost
   * @return cost
  **/
  @ApiModelProperty(value = "")
  public Long getCost() {
    return cost;
  }

  public void setCost(Long cost) {
    this.cost = cost;
  }

  public MinimalPerm description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MinimalPerm dynamic(Boolean dynamic) {
    this.dynamic = dynamic;
    return this;
  }

   /**
   * Get dynamic
   * @return dynamic
  **/
  @ApiModelProperty(value = "")
  public Boolean isDynamic() {
    return dynamic;
  }

  public void setDynamic(Boolean dynamic) {
    this.dynamic = dynamic;
  }

  public MinimalPerm entityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
    return this;
  }

  public MinimalPerm addEntityCategoriesItem(EntityCategory entityCategoriesItem) {
    if (this.entityCategories == null) {
      this.entityCategories = new ArrayList<EntityCategory>();
    }
    this.entityCategories.add(entityCategoriesItem);
    return this;
  }

   /**
   * Get entityCategories
   * @return entityCategories
  **/
  @ApiModelProperty(value = "")
  public List<EntityCategory> getEntityCategories() {
    return entityCategories;
  }

  public void setEntityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
  }

  public MinimalPerm id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MinimalPerm instanceGuid(String instanceGuid) {
    this.instanceGuid = instanceGuid;
    return this;
  }

   /**
   * Get instanceGuid
   * @return instanceGuid
  **/
  @ApiModelProperty(value = "")
  public String getInstanceGuid() {
    return instanceGuid;
  }

  public void setInstanceGuid(String instanceGuid) {
    this.instanceGuid = instanceGuid;
  }

  public MinimalPerm name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MinimalPerm owners(List<PermOwner> owners) {
    this.owners = owners;
    return this;
  }

  public MinimalPerm addOwnersItem(PermOwner ownersItem) {
    if (this.owners == null) {
      this.owners = new ArrayList<PermOwner>();
    }
    this.owners.add(ownersItem);
    return this;
  }

   /**
   * Get owners
   * @return owners
  **/
  @ApiModelProperty(value = "")
  public List<PermOwner> getOwners() {
    return owners;
  }

  public void setOwners(List<PermOwner> owners) {
    this.owners = owners;
  }

  public MinimalPerm permissionAssignmentInfo(PermissionAssignmentInfo permissionAssignmentInfo) {
    this.permissionAssignmentInfo = permissionAssignmentInfo;
    return this;
  }

   /**
   * Get permissionAssignmentInfo
   * @return permissionAssignmentInfo
  **/
  @ApiModelProperty(value = "")
  public PermissionAssignmentInfo getPermissionAssignmentInfo() {
    return permissionAssignmentInfo;
  }

  public void setPermissionAssignmentInfo(PermissionAssignmentInfo permissionAssignmentInfo) {
    this.permissionAssignmentInfo = permissionAssignmentInfo;
  }

  public MinimalPerm permissionAssignmentInfoList(List<PermissionAssignmentInfo> permissionAssignmentInfoList) {
    this.permissionAssignmentInfoList = permissionAssignmentInfoList;
    return this;
  }

  public MinimalPerm addPermissionAssignmentInfoListItem(PermissionAssignmentInfo permissionAssignmentInfoListItem) {
    if (this.permissionAssignmentInfoList == null) {
      this.permissionAssignmentInfoList = new ArrayList<PermissionAssignmentInfo>();
    }
    this.permissionAssignmentInfoList.add(permissionAssignmentInfoListItem);
    return this;
  }

   /**
   * Get permissionAssignmentInfoList
   * @return permissionAssignmentInfoList
  **/
  @ApiModelProperty(value = "")
  public List<PermissionAssignmentInfo> getPermissionAssignmentInfoList() {
    return permissionAssignmentInfoList;
  }

  public void setPermissionAssignmentInfoList(List<PermissionAssignmentInfo> permissionAssignmentInfoList) {
    this.permissionAssignmentInfoList = permissionAssignmentInfoList;
  }

  public MinimalPerm requestApprovalPolicy(RequestPolicy requestApprovalPolicy) {
    this.requestApprovalPolicy = requestApprovalPolicy;
    return this;
  }

   /**
   * Get requestApprovalPolicy
   * @return requestApprovalPolicy
  **/
  @ApiModelProperty(value = "")
  public RequestPolicy getRequestApprovalPolicy() {
    return requestApprovalPolicy;
  }

  public void setRequestApprovalPolicy(RequestPolicy requestApprovalPolicy) {
    this.requestApprovalPolicy = requestApprovalPolicy;
  }

  public MinimalPerm requestPolicy(RequestPolicy requestPolicy) {
    this.requestPolicy = requestPolicy;
    return this;
  }

   /**
   * Get requestPolicy
   * @return requestPolicy
  **/
  @ApiModelProperty(value = "")
  public RequestPolicy getRequestPolicy() {
    return requestPolicy;
  }

  public void setRequestPolicy(RequestPolicy requestPolicy) {
    this.requestPolicy = requestPolicy;
  }

  public MinimalPerm risk(Long risk) {
    this.risk = risk;
    return this;
  }

   /**
   * Get risk
   * @return risk
  **/
  @ApiModelProperty(value = "")
  public Long getRisk() {
    return risk;
  }

  public void setRisk(Long risk) {
    this.risk = risk;
  }

  public MinimalPerm type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public MinimalPerm uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

   /**
   * Get uniqueId
   * @return uniqueId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public MinimalPerm userCount(Long userCount) {
    this.userCount = userCount;
    return this;
  }

   /**
   * Get userCount
   * @return userCount
  **/
  @ApiModelProperty(value = "")
  public Long getUserCount() {
    return userCount;
  }

  public void setUserCount(Long userCount) {
    this.userCount = userCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MinimalPerm minimalPerm = (MinimalPerm) o;
    return Objects.equals(this.authorizedBy, minimalPerm.authorizedBy) &&
        Objects.equals(this.boundPerms, minimalPerm.boundPerms) &&
        Objects.equals(this.boundPermsType, minimalPerm.boundPermsType) &&
        Objects.equals(this.cost, minimalPerm.cost) &&
        Objects.equals(this.description, minimalPerm.description) &&
        Objects.equals(this.dynamic, minimalPerm.dynamic) &&
        Objects.equals(this.entityCategories, minimalPerm.entityCategories) &&
        Objects.equals(this.id, minimalPerm.id) &&
        Objects.equals(this.instanceGuid, minimalPerm.instanceGuid) &&
        Objects.equals(this.name, minimalPerm.name) &&
        Objects.equals(this.owners, minimalPerm.owners) &&
        Objects.equals(this.permissionAssignmentInfo, minimalPerm.permissionAssignmentInfo) &&
        Objects.equals(this.permissionAssignmentInfoList, minimalPerm.permissionAssignmentInfoList) &&
        Objects.equals(this.requestApprovalPolicy, minimalPerm.requestApprovalPolicy) &&
        Objects.equals(this.requestPolicy, minimalPerm.requestPolicy) &&
        Objects.equals(this.risk, minimalPerm.risk) &&
        Objects.equals(this.type, minimalPerm.type) &&
        Objects.equals(this.uniqueId, minimalPerm.uniqueId) &&
        Objects.equals(this.userCount, minimalPerm.userCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorizedBy, boundPerms, boundPermsType, cost, description, dynamic, entityCategories, id, instanceGuid, name, owners, permissionAssignmentInfo, permissionAssignmentInfoList, requestApprovalPolicy, requestPolicy, risk, type, uniqueId, userCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MinimalPerm {\n");
    
    sb.append("    authorizedBy: ").append(toIndentedString(authorizedBy)).append("\n");
    sb.append("    boundPerms: ").append(toIndentedString(boundPerms)).append("\n");
    sb.append("    boundPermsType: ").append(toIndentedString(boundPermsType)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dynamic: ").append(toIndentedString(dynamic)).append("\n");
    sb.append("    entityCategories: ").append(toIndentedString(entityCategories)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    instanceGuid: ").append(toIndentedString(instanceGuid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    permissionAssignmentInfo: ").append(toIndentedString(permissionAssignmentInfo)).append("\n");
    sb.append("    permissionAssignmentInfoList: ").append(toIndentedString(permissionAssignmentInfoList)).append("\n");
    sb.append("    requestApprovalPolicy: ").append(toIndentedString(requestApprovalPolicy)).append("\n");
    sb.append("    requestPolicy: ").append(toIndentedString(requestPolicy)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    userCount: ").append(toIndentedString(userCount)).append("\n");
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
