/*
 * Identity Governance REST APIs
 * Welcome to the NetIQ Identity Governance API Documentation page. This is the API reference for the NetIQ Identity Governance REST API.  Below you will see the main sections of the API. Click each section in order to see the endpoints that are available in that category as well as information related to which Identity Governance roles have access. Global Administrators are not included in the accepted roles list for each API however they have access to all APIs. Those APIs that do not display a list of accepted roles are accessible for any role.  The NetIQ Identity Governance REST API uses the OAuth2 protocol for authentication. Therefore, in order to make any of these calls, you must obtain a token, and include that token in the Authentication header.  OSP = One SSO Provider   NAM = NetIQ Access Manager  **Note:** The various OAuth 2.0 endpoints described below can also be obtained from the OAuth/OpenID Connect provider 'metadata' found at the following location relative to the 'issuer URI':`[issuer-uri]/.well-known/openid-configuration`  Issuer URIs:  *   OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2 *   NAM: https://server/nidp/oauth/nam  See [Open ID Connect Discovery 1.0](\"https://openid.net/specs/openid-connect-discovery-1_0.html\") for more information.  Obtaining the Initial Access Token ==================================  ### OAuth 2.0 Resource Owner Password Credentials Grant Request  1.  Determine the OAuth 2.0 token endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/token     2.  NAM: https://server/nidp/oauth/nam/token 2.  Obtain the Identity Governance 'iac' client identifier and client secret.     1.  OSP         1.  The identifier is usually _iac_ but can be changed with the configutil or configupdate utilities.         2.  The client secret is the 'service password' specified during installation but can be changed with the configutil or configupdate utilities.     2.  NAM         1.  Open the Access Manager administrator console in a browser and navigate to the _OAuth & OpenID Connect_ tab on the _IDPCluster_ page. Select the _Client Applications_ heading.         2.  Click on the 'View' icon under the 'Actions' heading for the _Client Application_ named _iac_.         3.  Click on _Click to reveal_.         4.  Copy the _Client ID_ value and the _Client Secret_ value.         5.  Ensure that _Resource Owner Credentials_ appears in the _Grants Required_ list. If not, edit the client definition and check the _Resource Owner Credentials_ box, save the client definition, and update the IDP. 3.  Obtain the user identifier and password of a user with the required privilege for the desired API endpoint. 4.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 4.3.1](\"https://tools.ietf.org/html/rfc6749#section-4.3.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=password&username=<user-id>&password=[user-password]&client_id=[iac-client-id]&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the client and user values obtained in the steps above.  Also note the '**& amp;**' shown in this and other POST payload examples should actually be '**&**'. 5.  Issue the request to the OAuth 2.0 token endpoint. 6.  The JSON response will be similar to the following (see [RFC 6749 section 4.3.3](\"https://tools.ietf.org/html/rfc6749#section-4.3.3\")):`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119, \"refresh_token\": \"eyJhbGciOiJ...\" }` 7.  When issuing a REST request to an Identity Governance endpoint pass the access token value using an HTTP _Bearer_ authorization header (see [RFC 6750 section 2.1](\"https://tools.ietf.org/html/rfc6750#section-2.1\")):`Authorization: Bearer [access-token]`  Refresh Tokens ==============  If the authorization server is configured to return an OAuth 2.0 refresh token in the JSON result of the Resource Owner Password Credential Grant request then the refresh token should be used to obtain additional access tokens after the currently-valid access token expires.  In addition, each refresh token issued on behalf of a user causes a 'revocation entry' to be stored in an attribute on the user's LDAP object. Obtaining many refresh tokens without revoking previously obtained, unexpired refresh tokens will eventually exceed the capacity of the attribute on the user's LDAP object and will result in errors.  Therefore, if a refresh token is obtained it must be revoked after it is no longer needed.  ### Access Token Request  1.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 6](\"https://tools.ietf.org/html/rfc6749#section-6\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=refresh_token&refresh_token=<refresh-token>&client_id=<iac-clientid>&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the obvious values. 2.  Issue the request to the OAuth 2.0 token endpoint. 3.  The JSON result will be similar to`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119 }` 4.  Use the new access token value in requests to Identity Governance REST endpoints as described above.  ### Refresh Token Revocation Request  1.  Determine the OAuth 2.0 token revocation endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/revoke     2.  NAM: https://server/nidp/oauth/nam/revoke 2.  Create an HTTP POST request with the following characteristics (see [RFC 7009 section 2.1](\"https://tools.ietf.org/html/rfc7009#section-2.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body:`token=[refresh-token]&client_id=[iac-client-id]&client_secret=[iac-client-secret]` 3.  Issue the request to the OAuth 2.0 revocation endpoint.      As a shortcut to learning the API, run Identity Governance in a browser with developer tools enabled and watch the network traffic between the browser and the server.  * * *
 *
 * OpenAPI spec version: 3.7.3-202
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
 * PermissionAssignmentInfo
 */



public class PermissionAssignmentInfo {
  @SerializedName("accountId")
  private Long accountId = null;

  @SerializedName("accountName")
  private String accountName = null;

  @SerializedName("atype")
  private String atype = null;

  @SerializedName("endTime")
  private Long endTime = null;

  @SerializedName("generic1")
  private String generic1 = null;

  @SerializedName("generic2")
  private String generic2 = null;

  @SerializedName("generic3")
  private String generic3 = null;

  @SerializedName("generic4")
  private String generic4 = null;

  @SerializedName("generic5")
  private String generic5 = null;

  @SerializedName("generic6")
  private String generic6 = null;

  @SerializedName("risk")
  private Long risk = null;

  @SerializedName("startTime")
  private Long startTime = null;

  @SerializedName("usage")
  private String usage = null;

  public PermissionAssignmentInfo accountId(Long accountId) {
    this.accountId = accountId;
    return this;
  }

   /**
   * Get accountId
   * @return accountId
  **/
  @ApiModelProperty(value = "")
  public Long getAccountId() {
    return accountId;
  }

  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  public PermissionAssignmentInfo accountName(String accountName) {
    this.accountName = accountName;
    return this;
  }

   /**
   * Get accountName
   * @return accountName
  **/
  @ApiModelProperty(value = "")
  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public PermissionAssignmentInfo atype(String atype) {
    this.atype = atype;
    return this;
  }

   /**
   * Get atype
   * @return atype
  **/
  @ApiModelProperty(value = "")
  public String getAtype() {
    return atype;
  }

  public void setAtype(String atype) {
    this.atype = atype;
  }

  public PermissionAssignmentInfo endTime(Long endTime) {
    this.endTime = endTime;
    return this;
  }

   /**
   * Get endTime
   * @return endTime
  **/
  @ApiModelProperty(value = "")
  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  public PermissionAssignmentInfo generic1(String generic1) {
    this.generic1 = generic1;
    return this;
  }

   /**
   * Get generic1
   * @return generic1
  **/
  @ApiModelProperty(value = "")
  public String getGeneric1() {
    return generic1;
  }

  public void setGeneric1(String generic1) {
    this.generic1 = generic1;
  }

  public PermissionAssignmentInfo generic2(String generic2) {
    this.generic2 = generic2;
    return this;
  }

   /**
   * Get generic2
   * @return generic2
  **/
  @ApiModelProperty(value = "")
  public String getGeneric2() {
    return generic2;
  }

  public void setGeneric2(String generic2) {
    this.generic2 = generic2;
  }

  public PermissionAssignmentInfo generic3(String generic3) {
    this.generic3 = generic3;
    return this;
  }

   /**
   * Get generic3
   * @return generic3
  **/
  @ApiModelProperty(value = "")
  public String getGeneric3() {
    return generic3;
  }

  public void setGeneric3(String generic3) {
    this.generic3 = generic3;
  }

  public PermissionAssignmentInfo generic4(String generic4) {
    this.generic4 = generic4;
    return this;
  }

   /**
   * Get generic4
   * @return generic4
  **/
  @ApiModelProperty(value = "")
  public String getGeneric4() {
    return generic4;
  }

  public void setGeneric4(String generic4) {
    this.generic4 = generic4;
  }

  public PermissionAssignmentInfo generic5(String generic5) {
    this.generic5 = generic5;
    return this;
  }

   /**
   * Get generic5
   * @return generic5
  **/
  @ApiModelProperty(value = "")
  public String getGeneric5() {
    return generic5;
  }

  public void setGeneric5(String generic5) {
    this.generic5 = generic5;
  }

  public PermissionAssignmentInfo generic6(String generic6) {
    this.generic6 = generic6;
    return this;
  }

   /**
   * Get generic6
   * @return generic6
  **/
  @ApiModelProperty(value = "")
  public String getGeneric6() {
    return generic6;
  }

  public void setGeneric6(String generic6) {
    this.generic6 = generic6;
  }

  public PermissionAssignmentInfo risk(Long risk) {
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

  public PermissionAssignmentInfo startTime(Long startTime) {
    this.startTime = startTime;
    return this;
  }

   /**
   * Get startTime
   * @return startTime
  **/
  @ApiModelProperty(value = "")
  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public PermissionAssignmentInfo usage(String usage) {
    this.usage = usage;
    return this;
  }

   /**
   * Get usage
   * @return usage
  **/
  @ApiModelProperty(value = "")
  public String getUsage() {
    return usage;
  }

  public void setUsage(String usage) {
    this.usage = usage;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PermissionAssignmentInfo permissionAssignmentInfo = (PermissionAssignmentInfo) o;
    return Objects.equals(this.accountId, permissionAssignmentInfo.accountId) &&
        Objects.equals(this.accountName, permissionAssignmentInfo.accountName) &&
        Objects.equals(this.atype, permissionAssignmentInfo.atype) &&
        Objects.equals(this.endTime, permissionAssignmentInfo.endTime) &&
        Objects.equals(this.generic1, permissionAssignmentInfo.generic1) &&
        Objects.equals(this.generic2, permissionAssignmentInfo.generic2) &&
        Objects.equals(this.generic3, permissionAssignmentInfo.generic3) &&
        Objects.equals(this.generic4, permissionAssignmentInfo.generic4) &&
        Objects.equals(this.generic5, permissionAssignmentInfo.generic5) &&
        Objects.equals(this.generic6, permissionAssignmentInfo.generic6) &&
        Objects.equals(this.risk, permissionAssignmentInfo.risk) &&
        Objects.equals(this.startTime, permissionAssignmentInfo.startTime) &&
        Objects.equals(this.usage, permissionAssignmentInfo.usage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, accountName, atype, endTime, generic1, generic2, generic3, generic4, generic5, generic6, risk, startTime, usage);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PermissionAssignmentInfo {\n");
    
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    atype: ").append(toIndentedString(atype)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    generic1: ").append(toIndentedString(generic1)).append("\n");
    sb.append("    generic2: ").append(toIndentedString(generic2)).append("\n");
    sb.append("    generic3: ").append(toIndentedString(generic3)).append("\n");
    sb.append("    generic4: ").append(toIndentedString(generic4)).append("\n");
    sb.append("    generic5: ").append(toIndentedString(generic5)).append("\n");
    sb.append("    generic6: ").append(toIndentedString(generic6)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
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
