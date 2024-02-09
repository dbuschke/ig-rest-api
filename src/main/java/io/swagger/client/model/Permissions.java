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

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.Permission;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Permissions
 */



public class Permissions {
  @SerializedName("arraySize")
  private Integer arraySize = null;

  @SerializedName("canManageAccessApproval")
  private Boolean canManageAccessApproval = null;

  @SerializedName("canManageAccessRequest")
  private Boolean canManageAccessRequest = null;

  @SerializedName("editable")
  private Boolean editable = null;

  @SerializedName("indexFrom")
  private Integer indexFrom = null;

  @SerializedName("linkApplication")
  private String linkApplication = null;

  @SerializedName("listAttrs")
  private List<String> listAttrs = null;

  @SerializedName("maxPermHolders")
  private Long maxPermHolders = null;

  @SerializedName("nameApplication")
  private String nameApplication = null;

  @SerializedName("permissions")
  private List<Permission> permissions = null;

  @SerializedName("q")
  private String q = null;

  @SerializedName("qMatch")
  private String qMatch = null;

  @SerializedName("showCt")
  private Boolean showCt = null;

  @SerializedName("size")
  private Integer size = null;

  @SerializedName("sortBy")
  private String sortBy = null;

  @SerializedName("sortOrder")
  private String sortOrder = null;

  @SerializedName("totalSearch")
  private Long totalSearch = null;

  @SerializedName("unfilteredCount")
  private Long unfilteredCount = null;

  public Permissions arraySize(Integer arraySize) {
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

  public Permissions canManageAccessApproval(Boolean canManageAccessApproval) {
    this.canManageAccessApproval = canManageAccessApproval;
    return this;
  }

   /**
   * Get canManageAccessApproval
   * @return canManageAccessApproval
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanManageAccessApproval() {
    return canManageAccessApproval;
  }

  public void setCanManageAccessApproval(Boolean canManageAccessApproval) {
    this.canManageAccessApproval = canManageAccessApproval;
  }

  public Permissions canManageAccessRequest(Boolean canManageAccessRequest) {
    this.canManageAccessRequest = canManageAccessRequest;
    return this;
  }

   /**
   * Get canManageAccessRequest
   * @return canManageAccessRequest
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanManageAccessRequest() {
    return canManageAccessRequest;
  }

  public void setCanManageAccessRequest(Boolean canManageAccessRequest) {
    this.canManageAccessRequest = canManageAccessRequest;
  }

  public Permissions editable(Boolean editable) {
    this.editable = editable;
    return this;
  }

   /**
   * Get editable
   * @return editable
  **/
  @ApiModelProperty(value = "")
  public Boolean isEditable() {
    return editable;
  }

  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  public Permissions indexFrom(Integer indexFrom) {
    this.indexFrom = indexFrom;
    return this;
  }

   /**
   * Get indexFrom
   * @return indexFrom
  **/
  @ApiModelProperty(value = "")
  public Integer getIndexFrom() {
    return indexFrom;
  }

  public void setIndexFrom(Integer indexFrom) {
    this.indexFrom = indexFrom;
  }

  public Permissions linkApplication(String linkApplication) {
    this.linkApplication = linkApplication;
    return this;
  }

   /**
   * Get linkApplication
   * @return linkApplication
  **/
  @ApiModelProperty(value = "")
  public String getLinkApplication() {
    return linkApplication;
  }

  public void setLinkApplication(String linkApplication) {
    this.linkApplication = linkApplication;
  }

  public Permissions listAttrs(List<String> listAttrs) {
    this.listAttrs = listAttrs;
    return this;
  }

  public Permissions addListAttrsItem(String listAttrsItem) {
    if (this.listAttrs == null) {
      this.listAttrs = new ArrayList<String>();
    }
    this.listAttrs.add(listAttrsItem);
    return this;
  }

   /**
   * Get listAttrs
   * @return listAttrs
  **/
  @ApiModelProperty(value = "")
  public List<String> getListAttrs() {
    return listAttrs;
  }

  public void setListAttrs(List<String> listAttrs) {
    this.listAttrs = listAttrs;
  }

  public Permissions maxPermHolders(Long maxPermHolders) {
    this.maxPermHolders = maxPermHolders;
    return this;
  }

   /**
   * Get maxPermHolders
   * @return maxPermHolders
  **/
  @ApiModelProperty(value = "")
  public Long getMaxPermHolders() {
    return maxPermHolders;
  }

  public void setMaxPermHolders(Long maxPermHolders) {
    this.maxPermHolders = maxPermHolders;
  }

  public Permissions nameApplication(String nameApplication) {
    this.nameApplication = nameApplication;
    return this;
  }

   /**
   * Get nameApplication
   * @return nameApplication
  **/
  @ApiModelProperty(value = "")
  public String getNameApplication() {
    return nameApplication;
  }

  public void setNameApplication(String nameApplication) {
    this.nameApplication = nameApplication;
  }

  public Permissions permissions(List<Permission> permissions) {
    this.permissions = permissions;
    return this;
  }

  public Permissions addPermissionsItem(Permission permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<Permission>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

   /**
   * Get permissions
   * @return permissions
  **/
  @ApiModelProperty(value = "")
  public List<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<Permission> permissions) {
    this.permissions = permissions;
  }

  public Permissions q(String q) {
    this.q = q;
    return this;
  }

   /**
   * Get q
   * @return q
  **/
  @ApiModelProperty(value = "")
  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public Permissions qMatch(String qMatch) {
    this.qMatch = qMatch;
    return this;
  }

   /**
   * Get qMatch
   * @return qMatch
  **/
  @ApiModelProperty(value = "")
  public String getQMatch() {
    return qMatch;
  }

  public void setQMatch(String qMatch) {
    this.qMatch = qMatch;
  }

  public Permissions showCt(Boolean showCt) {
    this.showCt = showCt;
    return this;
  }

   /**
   * Get showCt
   * @return showCt
  **/
  @ApiModelProperty(value = "")
  public Boolean isShowCt() {
    return showCt;
  }

  public void setShowCt(Boolean showCt) {
    this.showCt = showCt;
  }

  public Permissions size(Integer size) {
    this.size = size;
    return this;
  }

   /**
   * Get size
   * @return size
  **/
  @ApiModelProperty(value = "")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Permissions sortBy(String sortBy) {
    this.sortBy = sortBy;
    return this;
  }

   /**
   * Get sortBy
   * @return sortBy
  **/
  @ApiModelProperty(value = "")
  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public Permissions sortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
    return this;
  }

   /**
   * Get sortOrder
   * @return sortOrder
  **/
  @ApiModelProperty(value = "")
  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }

  public Permissions totalSearch(Long totalSearch) {
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

  public Permissions unfilteredCount(Long unfilteredCount) {
    this.unfilteredCount = unfilteredCount;
    return this;
  }

   /**
   * Get unfilteredCount
   * @return unfilteredCount
  **/
  @ApiModelProperty(value = "")
  public Long getUnfilteredCount() {
    return unfilteredCount;
  }

  public void setUnfilteredCount(Long unfilteredCount) {
    this.unfilteredCount = unfilteredCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Permissions permissions = (Permissions) o;
    return Objects.equals(this.arraySize, permissions.arraySize) &&
        Objects.equals(this.canManageAccessApproval, permissions.canManageAccessApproval) &&
        Objects.equals(this.canManageAccessRequest, permissions.canManageAccessRequest) &&
        Objects.equals(this.editable, permissions.editable) &&
        Objects.equals(this.indexFrom, permissions.indexFrom) &&
        Objects.equals(this.linkApplication, permissions.linkApplication) &&
        Objects.equals(this.listAttrs, permissions.listAttrs) &&
        Objects.equals(this.maxPermHolders, permissions.maxPermHolders) &&
        Objects.equals(this.nameApplication, permissions.nameApplication) &&
        Objects.equals(this.permissions, permissions.permissions) &&
        Objects.equals(this.q, permissions.q) &&
        Objects.equals(this.qMatch, permissions.qMatch) &&
        Objects.equals(this.showCt, permissions.showCt) &&
        Objects.equals(this.size, permissions.size) &&
        Objects.equals(this.sortBy, permissions.sortBy) &&
        Objects.equals(this.sortOrder, permissions.sortOrder) &&
        Objects.equals(this.totalSearch, permissions.totalSearch) &&
        Objects.equals(this.unfilteredCount, permissions.unfilteredCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(arraySize, canManageAccessApproval, canManageAccessRequest, editable, indexFrom, linkApplication, listAttrs, maxPermHolders, nameApplication, permissions, q, qMatch, showCt, size, sortBy, sortOrder, totalSearch, unfilteredCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Permissions {\n");
    
    sb.append("    arraySize: ").append(toIndentedString(arraySize)).append("\n");
    sb.append("    canManageAccessApproval: ").append(toIndentedString(canManageAccessApproval)).append("\n");
    sb.append("    canManageAccessRequest: ").append(toIndentedString(canManageAccessRequest)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    indexFrom: ").append(toIndentedString(indexFrom)).append("\n");
    sb.append("    linkApplication: ").append(toIndentedString(linkApplication)).append("\n");
    sb.append("    listAttrs: ").append(toIndentedString(listAttrs)).append("\n");
    sb.append("    maxPermHolders: ").append(toIndentedString(maxPermHolders)).append("\n");
    sb.append("    nameApplication: ").append(toIndentedString(nameApplication)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    q: ").append(toIndentedString(q)).append("\n");
    sb.append("    qMatch: ").append(toIndentedString(qMatch)).append("\n");
    sb.append("    showCt: ").append(toIndentedString(showCt)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    sortBy: ").append(toIndentedString(sortBy)).append("\n");
    sb.append("    sortOrder: ").append(toIndentedString(sortOrder)).append("\n");
    sb.append("    totalSearch: ").append(toIndentedString(totalSearch)).append("\n");
    sb.append("    unfilteredCount: ").append(toIndentedString(unfilteredCount)).append("\n");
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
