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
import de.araneaconsult.codegen.ig.rest.model.Attribute;
import de.araneaconsult.codegen.ig.rest.model.Mine;
import de.araneaconsult.codegen.ig.rest.model.Tuple;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Values
 */



public class Values {
  @SerializedName("addAuthorizations")
  private Boolean addAuthorizations = null;

  @SerializedName("appPercent")
  private Long appPercent = null;

  @SerializedName("arraySize")
  private Integer arraySize = null;

  @SerializedName("createRoleHierarchy")
  private Boolean createRoleHierarchy = null;

  @SerializedName("createTechRoles")
  private Boolean createTechRoles = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("groupByApps")
  private Boolean groupByApps = null;

  @SerializedName("permPercent")
  private Long permPercent = null;

  @SerializedName("roleHierarchy")
  private List<Attribute> roleHierarchy = null;

  @SerializedName("roleMiningNode")
  private Mine roleMiningNode = null;

  @SerializedName("roleName")
  private String roleName = null;

  @SerializedName("rolePercent")
  private Long rolePercent = null;

  @SerializedName("totalSearch")
  private Long totalSearch = null;

  @SerializedName("truncated")
  private Boolean truncated = null;

  @SerializedName("tuples")
  private List<Tuple> tuples = null;

  public Values addAuthorizations(Boolean addAuthorizations) {
    this.addAuthorizations = addAuthorizations;
    return this;
  }

   /**
   * Get addAuthorizations
   * @return addAuthorizations
  **/
  @ApiModelProperty(value = "")
  public Boolean isAddAuthorizations() {
    return addAuthorizations;
  }

  public void setAddAuthorizations(Boolean addAuthorizations) {
    this.addAuthorizations = addAuthorizations;
  }

  public Values appPercent(Long appPercent) {
    this.appPercent = appPercent;
    return this;
  }

   /**
   * Get appPercent
   * @return appPercent
  **/
  @ApiModelProperty(value = "")
  public Long getAppPercent() {
    return appPercent;
  }

  public void setAppPercent(Long appPercent) {
    this.appPercent = appPercent;
  }

  public Values arraySize(Integer arraySize) {
    this.arraySize = arraySize;
    return this;
  }

   /**
   * Get arraySize
   * @return arraySize
  **/
  @ApiModelProperty(value = "")
  public Integer getArraySize() {
    return arraySize;
  }

  public void setArraySize(Integer arraySize) {
    this.arraySize = arraySize;
  }

  public Values createRoleHierarchy(Boolean createRoleHierarchy) {
    this.createRoleHierarchy = createRoleHierarchy;
    return this;
  }

   /**
   * Get createRoleHierarchy
   * @return createRoleHierarchy
  **/
  @ApiModelProperty(value = "")
  public Boolean isCreateRoleHierarchy() {
    return createRoleHierarchy;
  }

  public void setCreateRoleHierarchy(Boolean createRoleHierarchy) {
    this.createRoleHierarchy = createRoleHierarchy;
  }

  public Values createTechRoles(Boolean createTechRoles) {
    this.createTechRoles = createTechRoles;
    return this;
  }

   /**
   * Get createTechRoles
   * @return createTechRoles
  **/
  @ApiModelProperty(value = "")
  public Boolean isCreateTechRoles() {
    return createTechRoles;
  }

  public void setCreateTechRoles(Boolean createTechRoles) {
    this.createTechRoles = createTechRoles;
  }

  public Values creationTime(Long creationTime) {
    this.creationTime = creationTime;
    return this;
  }

   /**
   * Get creationTime
   * @return creationTime
  **/
  @ApiModelProperty(value = "")
  public Long getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Long creationTime) {
    this.creationTime = creationTime;
  }

  public Values description(String description) {
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

  public Values groupByApps(Boolean groupByApps) {
    this.groupByApps = groupByApps;
    return this;
  }

   /**
   * Get groupByApps
   * @return groupByApps
  **/
  @ApiModelProperty(value = "")
  public Boolean isGroupByApps() {
    return groupByApps;
  }

  public void setGroupByApps(Boolean groupByApps) {
    this.groupByApps = groupByApps;
  }

  public Values permPercent(Long permPercent) {
    this.permPercent = permPercent;
    return this;
  }

   /**
   * Get permPercent
   * @return permPercent
  **/
  @ApiModelProperty(value = "")
  public Long getPermPercent() {
    return permPercent;
  }

  public void setPermPercent(Long permPercent) {
    this.permPercent = permPercent;
  }

  public Values roleHierarchy(List<Attribute> roleHierarchy) {
    this.roleHierarchy = roleHierarchy;
    return this;
  }

  public Values addRoleHierarchyItem(Attribute roleHierarchyItem) {
    if (this.roleHierarchy == null) {
      this.roleHierarchy = new ArrayList<Attribute>();
    }
    this.roleHierarchy.add(roleHierarchyItem);
    return this;
  }

   /**
   * Get roleHierarchy
   * @return roleHierarchy
  **/
  @ApiModelProperty(value = "")
  public List<Attribute> getRoleHierarchy() {
    return roleHierarchy;
  }

  public void setRoleHierarchy(List<Attribute> roleHierarchy) {
    this.roleHierarchy = roleHierarchy;
  }

  public Values roleMiningNode(Mine roleMiningNode) {
    this.roleMiningNode = roleMiningNode;
    return this;
  }

   /**
   * Get roleMiningNode
   * @return roleMiningNode
  **/
  @ApiModelProperty(value = "")
  public Mine getRoleMiningNode() {
    return roleMiningNode;
  }

  public void setRoleMiningNode(Mine roleMiningNode) {
    this.roleMiningNode = roleMiningNode;
  }

  public Values roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

   /**
   * Get roleName
   * @return roleName
  **/
  @ApiModelProperty(value = "")
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Values rolePercent(Long rolePercent) {
    this.rolePercent = rolePercent;
    return this;
  }

   /**
   * Get rolePercent
   * @return rolePercent
  **/
  @ApiModelProperty(value = "")
  public Long getRolePercent() {
    return rolePercent;
  }

  public void setRolePercent(Long rolePercent) {
    this.rolePercent = rolePercent;
  }

  public Values totalSearch(Long totalSearch) {
    this.totalSearch = totalSearch;
    return this;
  }

   /**
   * Get totalSearch
   * @return totalSearch
  **/
  @ApiModelProperty(value = "")
  public Long getTotalSearch() {
    return totalSearch;
  }

  public void setTotalSearch(Long totalSearch) {
    this.totalSearch = totalSearch;
  }

  public Values truncated(Boolean truncated) {
    this.truncated = truncated;
    return this;
  }

   /**
   * Get truncated
   * @return truncated
  **/
  @ApiModelProperty(value = "")
  public Boolean isTruncated() {
    return truncated;
  }

  public void setTruncated(Boolean truncated) {
    this.truncated = truncated;
  }

  public Values tuples(List<Tuple> tuples) {
    this.tuples = tuples;
    return this;
  }

  public Values addTuplesItem(Tuple tuplesItem) {
    if (this.tuples == null) {
      this.tuples = new ArrayList<Tuple>();
    }
    this.tuples.add(tuplesItem);
    return this;
  }

   /**
   * Get tuples
   * @return tuples
  **/
  @ApiModelProperty(value = "")
  public List<Tuple> getTuples() {
    return tuples;
  }

  public void setTuples(List<Tuple> tuples) {
    this.tuples = tuples;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Values values = (Values) o;
    return Objects.equals(this.addAuthorizations, values.addAuthorizations) &&
        Objects.equals(this.appPercent, values.appPercent) &&
        Objects.equals(this.arraySize, values.arraySize) &&
        Objects.equals(this.createRoleHierarchy, values.createRoleHierarchy) &&
        Objects.equals(this.createTechRoles, values.createTechRoles) &&
        Objects.equals(this.creationTime, values.creationTime) &&
        Objects.equals(this.description, values.description) &&
        Objects.equals(this.groupByApps, values.groupByApps) &&
        Objects.equals(this.permPercent, values.permPercent) &&
        Objects.equals(this.roleHierarchy, values.roleHierarchy) &&
        Objects.equals(this.roleMiningNode, values.roleMiningNode) &&
        Objects.equals(this.roleName, values.roleName) &&
        Objects.equals(this.rolePercent, values.rolePercent) &&
        Objects.equals(this.totalSearch, values.totalSearch) &&
        Objects.equals(this.truncated, values.truncated) &&
        Objects.equals(this.tuples, values.tuples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addAuthorizations, appPercent, arraySize, createRoleHierarchy, createTechRoles, creationTime, description, groupByApps, permPercent, roleHierarchy, roleMiningNode, roleName, rolePercent, totalSearch, truncated, tuples);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Values {\n");
    
    sb.append("    addAuthorizations: ").append(toIndentedString(addAuthorizations)).append("\n");
    sb.append("    appPercent: ").append(toIndentedString(appPercent)).append("\n");
    sb.append("    arraySize: ").append(toIndentedString(arraySize)).append("\n");
    sb.append("    createRoleHierarchy: ").append(toIndentedString(createRoleHierarchy)).append("\n");
    sb.append("    createTechRoles: ").append(toIndentedString(createTechRoles)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    groupByApps: ").append(toIndentedString(groupByApps)).append("\n");
    sb.append("    permPercent: ").append(toIndentedString(permPercent)).append("\n");
    sb.append("    roleHierarchy: ").append(toIndentedString(roleHierarchy)).append("\n");
    sb.append("    roleMiningNode: ").append(toIndentedString(roleMiningNode)).append("\n");
    sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
    sb.append("    rolePercent: ").append(toIndentedString(rolePercent)).append("\n");
    sb.append("    totalSearch: ").append(toIndentedString(totalSearch)).append("\n");
    sb.append("    truncated: ").append(toIndentedString(truncated)).append("\n");
    sb.append("    tuples: ").append(toIndentedString(tuples)).append("\n");
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
