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
import de.araneaconsult.codegen.ig.rest.model.RawStmt;
import java.io.IOException;
/**
 * Statement
 */



public class Statement {
  @SerializedName("accountCriteria")
  private String accountCriteria = null;

  @SerializedName("applicationCriteria")
  private String applicationCriteria = null;

  @SerializedName("businessRoleCriteria")
  private String businessRoleCriteria = null;

  @SerializedName("coverageMapId")
  private String coverageMapId = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("permissionCriteria")
  private String permissionCriteria = null;

  @SerializedName("rawReviewerGroupCriteria")
  private RawStmt rawReviewerGroupCriteria = null;

  @SerializedName("rawReviewerUserCriteria")
  private RawStmt rawReviewerUserCriteria = null;

  @SerializedName("reviewerGroupCriteria")
  private String reviewerGroupCriteria = null;

  @SerializedName("reviewerUserCriteria")
  private String reviewerUserCriteria = null;

  @SerializedName("roleCriteria")
  private String roleCriteria = null;

  @SerializedName("userCriteria")
  private String userCriteria = null;

  public Statement accountCriteria(String accountCriteria) {
    this.accountCriteria = accountCriteria;
    return this;
  }

   /**
   * Get accountCriteria
   * @return accountCriteria
  **/
  @ApiModelProperty(value = "")
  public String getAccountCriteria() {
    return accountCriteria;
  }

  public void setAccountCriteria(String accountCriteria) {
    this.accountCriteria = accountCriteria;
  }

  public Statement applicationCriteria(String applicationCriteria) {
    this.applicationCriteria = applicationCriteria;
    return this;
  }

   /**
   * Get applicationCriteria
   * @return applicationCriteria
  **/
  @ApiModelProperty(value = "")
  public String getApplicationCriteria() {
    return applicationCriteria;
  }

  public void setApplicationCriteria(String applicationCriteria) {
    this.applicationCriteria = applicationCriteria;
  }

  public Statement businessRoleCriteria(String businessRoleCriteria) {
    this.businessRoleCriteria = businessRoleCriteria;
    return this;
  }

   /**
   * Get businessRoleCriteria
   * @return businessRoleCriteria
  **/
  @ApiModelProperty(value = "")
  public String getBusinessRoleCriteria() {
    return businessRoleCriteria;
  }

  public void setBusinessRoleCriteria(String businessRoleCriteria) {
    this.businessRoleCriteria = businessRoleCriteria;
  }

  public Statement coverageMapId(String coverageMapId) {
    this.coverageMapId = coverageMapId;
    return this;
  }

   /**
   * Get coverageMapId
   * @return coverageMapId
  **/
  @ApiModelProperty(value = "")
  public String getCoverageMapId() {
    return coverageMapId;
  }

  public void setCoverageMapId(String coverageMapId) {
    this.coverageMapId = coverageMapId;
  }

  public Statement id(Long id) {
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

  public Statement permissionCriteria(String permissionCriteria) {
    this.permissionCriteria = permissionCriteria;
    return this;
  }

   /**
   * Get permissionCriteria
   * @return permissionCriteria
  **/
  @ApiModelProperty(value = "")
  public String getPermissionCriteria() {
    return permissionCriteria;
  }

  public void setPermissionCriteria(String permissionCriteria) {
    this.permissionCriteria = permissionCriteria;
  }

  public Statement rawReviewerGroupCriteria(RawStmt rawReviewerGroupCriteria) {
    this.rawReviewerGroupCriteria = rawReviewerGroupCriteria;
    return this;
  }

   /**
   * Get rawReviewerGroupCriteria
   * @return rawReviewerGroupCriteria
  **/
  @ApiModelProperty(value = "")
  public RawStmt getRawReviewerGroupCriteria() {
    return rawReviewerGroupCriteria;
  }

  public void setRawReviewerGroupCriteria(RawStmt rawReviewerGroupCriteria) {
    this.rawReviewerGroupCriteria = rawReviewerGroupCriteria;
  }

  public Statement rawReviewerUserCriteria(RawStmt rawReviewerUserCriteria) {
    this.rawReviewerUserCriteria = rawReviewerUserCriteria;
    return this;
  }

   /**
   * Get rawReviewerUserCriteria
   * @return rawReviewerUserCriteria
  **/
  @ApiModelProperty(value = "")
  public RawStmt getRawReviewerUserCriteria() {
    return rawReviewerUserCriteria;
  }

  public void setRawReviewerUserCriteria(RawStmt rawReviewerUserCriteria) {
    this.rawReviewerUserCriteria = rawReviewerUserCriteria;
  }

  public Statement reviewerGroupCriteria(String reviewerGroupCriteria) {
    this.reviewerGroupCriteria = reviewerGroupCriteria;
    return this;
  }

   /**
   * Get reviewerGroupCriteria
   * @return reviewerGroupCriteria
  **/
  @ApiModelProperty(value = "")
  public String getReviewerGroupCriteria() {
    return reviewerGroupCriteria;
  }

  public void setReviewerGroupCriteria(String reviewerGroupCriteria) {
    this.reviewerGroupCriteria = reviewerGroupCriteria;
  }

  public Statement reviewerUserCriteria(String reviewerUserCriteria) {
    this.reviewerUserCriteria = reviewerUserCriteria;
    return this;
  }

   /**
   * Get reviewerUserCriteria
   * @return reviewerUserCriteria
  **/
  @ApiModelProperty(value = "")
  public String getReviewerUserCriteria() {
    return reviewerUserCriteria;
  }

  public void setReviewerUserCriteria(String reviewerUserCriteria) {
    this.reviewerUserCriteria = reviewerUserCriteria;
  }

  public Statement roleCriteria(String roleCriteria) {
    this.roleCriteria = roleCriteria;
    return this;
  }

   /**
   * Get roleCriteria
   * @return roleCriteria
  **/
  @ApiModelProperty(value = "")
  public String getRoleCriteria() {
    return roleCriteria;
  }

  public void setRoleCriteria(String roleCriteria) {
    this.roleCriteria = roleCriteria;
  }

  public Statement userCriteria(String userCriteria) {
    this.userCriteria = userCriteria;
    return this;
  }

   /**
   * Get userCriteria
   * @return userCriteria
  **/
  @ApiModelProperty(value = "")
  public String getUserCriteria() {
    return userCriteria;
  }

  public void setUserCriteria(String userCriteria) {
    this.userCriteria = userCriteria;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Statement statement = (Statement) o;
    return Objects.equals(this.accountCriteria, statement.accountCriteria) &&
        Objects.equals(this.applicationCriteria, statement.applicationCriteria) &&
        Objects.equals(this.businessRoleCriteria, statement.businessRoleCriteria) &&
        Objects.equals(this.coverageMapId, statement.coverageMapId) &&
        Objects.equals(this.id, statement.id) &&
        Objects.equals(this.permissionCriteria, statement.permissionCriteria) &&
        Objects.equals(this.rawReviewerGroupCriteria, statement.rawReviewerGroupCriteria) &&
        Objects.equals(this.rawReviewerUserCriteria, statement.rawReviewerUserCriteria) &&
        Objects.equals(this.reviewerGroupCriteria, statement.reviewerGroupCriteria) &&
        Objects.equals(this.reviewerUserCriteria, statement.reviewerUserCriteria) &&
        Objects.equals(this.roleCriteria, statement.roleCriteria) &&
        Objects.equals(this.userCriteria, statement.userCriteria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountCriteria, applicationCriteria, businessRoleCriteria, coverageMapId, id, permissionCriteria, rawReviewerGroupCriteria, rawReviewerUserCriteria, reviewerGroupCriteria, reviewerUserCriteria, roleCriteria, userCriteria);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Statement {\n");
    
    sb.append("    accountCriteria: ").append(toIndentedString(accountCriteria)).append("\n");
    sb.append("    applicationCriteria: ").append(toIndentedString(applicationCriteria)).append("\n");
    sb.append("    businessRoleCriteria: ").append(toIndentedString(businessRoleCriteria)).append("\n");
    sb.append("    coverageMapId: ").append(toIndentedString(coverageMapId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    permissionCriteria: ").append(toIndentedString(permissionCriteria)).append("\n");
    sb.append("    rawReviewerGroupCriteria: ").append(toIndentedString(rawReviewerGroupCriteria)).append("\n");
    sb.append("    rawReviewerUserCriteria: ").append(toIndentedString(rawReviewerUserCriteria)).append("\n");
    sb.append("    reviewerGroupCriteria: ").append(toIndentedString(reviewerGroupCriteria)).append("\n");
    sb.append("    reviewerUserCriteria: ").append(toIndentedString(reviewerUserCriteria)).append("\n");
    sb.append("    roleCriteria: ").append(toIndentedString(roleCriteria)).append("\n");
    sb.append("    userCriteria: ").append(toIndentedString(userCriteria)).append("\n");
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
