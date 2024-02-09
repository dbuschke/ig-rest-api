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
import java.io.IOException;
/**
 * AuthInfo
 */



public class AuthInfo {
  @SerializedName("authEndDate")
  private Long authEndDate = null;

  @SerializedName("authStartDate")
  private Long authStartDate = null;

  @SerializedName("authUserResourceId")
  private Long authUserResourceId = null;

  @SerializedName("autoGrant")
  private Boolean autoGrant = null;

  @SerializedName("autoRevoke")
  private Boolean autoRevoke = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("inGracePeriod")
  private Boolean inGracePeriod = null;

  @SerializedName("indirectAuthorization")
  private Boolean indirectAuthorization = null;

  @SerializedName("mandatory")
  private Boolean mandatory = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("roleUniqueId")
  private String roleUniqueId = null;

  @SerializedName("userResourceId")
  private Long userResourceId = null;

  public AuthInfo authEndDate(Long authEndDate) {
    this.authEndDate = authEndDate;
    return this;
  }

   /**
   * Get authEndDate
   * @return authEndDate
  **/
  @ApiModelProperty(value = "")
  public Long getAuthEndDate() {
    return authEndDate;
  }

  public void setAuthEndDate(Long authEndDate) {
    this.authEndDate = authEndDate;
  }

  public AuthInfo authStartDate(Long authStartDate) {
    this.authStartDate = authStartDate;
    return this;
  }

   /**
   * Get authStartDate
   * @return authStartDate
  **/
  @ApiModelProperty(value = "")
  public Long getAuthStartDate() {
    return authStartDate;
  }

  public void setAuthStartDate(Long authStartDate) {
    this.authStartDate = authStartDate;
  }

  public AuthInfo authUserResourceId(Long authUserResourceId) {
    this.authUserResourceId = authUserResourceId;
    return this;
  }

   /**
   * Get authUserResourceId
   * @return authUserResourceId
  **/
  @ApiModelProperty(value = "")
  public Long getAuthUserResourceId() {
    return authUserResourceId;
  }

  public void setAuthUserResourceId(Long authUserResourceId) {
    this.authUserResourceId = authUserResourceId;
  }

  public AuthInfo autoGrant(Boolean autoGrant) {
    this.autoGrant = autoGrant;
    return this;
  }

   /**
   * Get autoGrant
   * @return autoGrant
  **/
  @ApiModelProperty(value = "")
  public Boolean isAutoGrant() {
    return autoGrant;
  }

  public void setAutoGrant(Boolean autoGrant) {
    this.autoGrant = autoGrant;
  }

  public AuthInfo autoRevoke(Boolean autoRevoke) {
    this.autoRevoke = autoRevoke;
    return this;
  }

   /**
   * Get autoRevoke
   * @return autoRevoke
  **/
  @ApiModelProperty(value = "")
  public Boolean isAutoRevoke() {
    return autoRevoke;
  }

  public void setAutoRevoke(Boolean autoRevoke) {
    this.autoRevoke = autoRevoke;
  }

  public AuthInfo description(String description) {
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

  public AuthInfo inGracePeriod(Boolean inGracePeriod) {
    this.inGracePeriod = inGracePeriod;
    return this;
  }

   /**
   * Get inGracePeriod
   * @return inGracePeriod
  **/
  @ApiModelProperty(value = "")
  public Boolean isInGracePeriod() {
    return inGracePeriod;
  }

  public void setInGracePeriod(Boolean inGracePeriod) {
    this.inGracePeriod = inGracePeriod;
  }

  public AuthInfo indirectAuthorization(Boolean indirectAuthorization) {
    this.indirectAuthorization = indirectAuthorization;
    return this;
  }

   /**
   * Get indirectAuthorization
   * @return indirectAuthorization
  **/
  @ApiModelProperty(value = "")
  public Boolean isIndirectAuthorization() {
    return indirectAuthorization;
  }

  public void setIndirectAuthorization(Boolean indirectAuthorization) {
    this.indirectAuthorization = indirectAuthorization;
  }

  public AuthInfo mandatory(Boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }

   /**
   * Get mandatory
   * @return mandatory
  **/
  @ApiModelProperty(value = "")
  public Boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(Boolean mandatory) {
    this.mandatory = mandatory;
  }

  public AuthInfo name(String name) {
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

  public AuthInfo roleUniqueId(String roleUniqueId) {
    this.roleUniqueId = roleUniqueId;
    return this;
  }

   /**
   * Get roleUniqueId
   * @return roleUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getRoleUniqueId() {
    return roleUniqueId;
  }

  public void setRoleUniqueId(String roleUniqueId) {
    this.roleUniqueId = roleUniqueId;
  }

  public AuthInfo userResourceId(Long userResourceId) {
    this.userResourceId = userResourceId;
    return this;
  }

   /**
   * Get userResourceId
   * @return userResourceId
  **/
  @ApiModelProperty(value = "")
  public Long getUserResourceId() {
    return userResourceId;
  }

  public void setUserResourceId(Long userResourceId) {
    this.userResourceId = userResourceId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthInfo authInfo = (AuthInfo) o;
    return Objects.equals(this.authEndDate, authInfo.authEndDate) &&
        Objects.equals(this.authStartDate, authInfo.authStartDate) &&
        Objects.equals(this.authUserResourceId, authInfo.authUserResourceId) &&
        Objects.equals(this.autoGrant, authInfo.autoGrant) &&
        Objects.equals(this.autoRevoke, authInfo.autoRevoke) &&
        Objects.equals(this.description, authInfo.description) &&
        Objects.equals(this.inGracePeriod, authInfo.inGracePeriod) &&
        Objects.equals(this.indirectAuthorization, authInfo.indirectAuthorization) &&
        Objects.equals(this.mandatory, authInfo.mandatory) &&
        Objects.equals(this.name, authInfo.name) &&
        Objects.equals(this.roleUniqueId, authInfo.roleUniqueId) &&
        Objects.equals(this.userResourceId, authInfo.userResourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authEndDate, authStartDate, authUserResourceId, autoGrant, autoRevoke, description, inGracePeriod, indirectAuthorization, mandatory, name, roleUniqueId, userResourceId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthInfo {\n");
    
    sb.append("    authEndDate: ").append(toIndentedString(authEndDate)).append("\n");
    sb.append("    authStartDate: ").append(toIndentedString(authStartDate)).append("\n");
    sb.append("    authUserResourceId: ").append(toIndentedString(authUserResourceId)).append("\n");
    sb.append("    autoGrant: ").append(toIndentedString(autoGrant)).append("\n");
    sb.append("    autoRevoke: ").append(toIndentedString(autoRevoke)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    inGracePeriod: ").append(toIndentedString(inGracePeriod)).append("\n");
    sb.append("    indirectAuthorization: ").append(toIndentedString(indirectAuthorization)).append("\n");
    sb.append("    mandatory: ").append(toIndentedString(mandatory)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    roleUniqueId: ").append(toIndentedString(roleUniqueId)).append("\n");
    sb.append("    userResourceId: ").append(toIndentedString(userResourceId)).append("\n");
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
