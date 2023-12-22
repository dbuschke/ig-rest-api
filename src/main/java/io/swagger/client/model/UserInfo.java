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
 * UserInfo
 */



public class UserInfo {
  @SerializedName("attrValue")
  private Object attrValue = null;

  @SerializedName("attrValues")
  private Object attrValues = null;

  @SerializedName("authTrackingId")
  private String authTrackingId = null;

  @SerializedName("clientId")
  private String clientId = null;

  @SerializedName("dN")
  private String dN = null;

  @SerializedName("isService")
  private Object isService = null;

  @SerializedName("loggableName")
  private String loggableName = null;

  @SerializedName("m_authTrackingId")
  private String mAuthTrackingId = null;

  @SerializedName("m_service")
  private Boolean mService = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("service")
  private Boolean service = null;

  public UserInfo attrValue(Object attrValue) {
    this.attrValue = attrValue;
    return this;
  }

   /**
   * Get attrValue
   * @return attrValue
  **/
  @ApiModelProperty(value = "")
  public Object getAttrValue() {
    return attrValue;
  }

  public void setAttrValue(Object attrValue) {
    this.attrValue = attrValue;
  }

  public UserInfo attrValues(Object attrValues) {
    this.attrValues = attrValues;
    return this;
  }

   /**
   * Get attrValues
   * @return attrValues
  **/
  @ApiModelProperty(value = "")
  public Object getAttrValues() {
    return attrValues;
  }

  public void setAttrValues(Object attrValues) {
    this.attrValues = attrValues;
  }

  public UserInfo authTrackingId(String authTrackingId) {
    this.authTrackingId = authTrackingId;
    return this;
  }

   /**
   * Get authTrackingId
   * @return authTrackingId
  **/
  @ApiModelProperty(value = "")
  public String getAuthTrackingId() {
    return authTrackingId;
  }

  public void setAuthTrackingId(String authTrackingId) {
    this.authTrackingId = authTrackingId;
  }

  public UserInfo clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

   /**
   * Get clientId
   * @return clientId
  **/
  @ApiModelProperty(value = "")
  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public UserInfo dN(String dN) {
    this.dN = dN;
    return this;
  }

   /**
   * Get dN
   * @return dN
  **/
  @ApiModelProperty(value = "")
  public String getDN() {
    return dN;
  }

  public void setDN(String dN) {
    this.dN = dN;
  }

  public UserInfo isService(Object isService) {
    this.isService = isService;
    return this;
  }

   /**
   * Get isService
   * @return isService
  **/
  @ApiModelProperty(value = "")
  public Object getIsService() {
    return isService;
  }

  public void setIsService(Object isService) {
    this.isService = isService;
  }

  public UserInfo loggableName(String loggableName) {
    this.loggableName = loggableName;
    return this;
  }

   /**
   * Get loggableName
   * @return loggableName
  **/
  @ApiModelProperty(value = "")
  public String getLoggableName() {
    return loggableName;
  }

  public void setLoggableName(String loggableName) {
    this.loggableName = loggableName;
  }

  public UserInfo mAuthTrackingId(String mAuthTrackingId) {
    this.mAuthTrackingId = mAuthTrackingId;
    return this;
  }

   /**
   * Get mAuthTrackingId
   * @return mAuthTrackingId
  **/
  @ApiModelProperty(value = "")
  public String getMAuthTrackingId() {
    return mAuthTrackingId;
  }

  public void setMAuthTrackingId(String mAuthTrackingId) {
    this.mAuthTrackingId = mAuthTrackingId;
  }

  public UserInfo mService(Boolean mService) {
    this.mService = mService;
    return this;
  }

   /**
   * Get mService
   * @return mService
  **/
  @ApiModelProperty(value = "")
  public Boolean isMService() {
    return mService;
  }

  public void setMService(Boolean mService) {
    this.mService = mService;
  }

  public UserInfo name(String name) {
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

  public UserInfo service(Boolean service) {
    this.service = service;
    return this;
  }

   /**
   * Get service
   * @return service
  **/
  @ApiModelProperty(value = "")
  public Boolean isService() {
    return service;
  }

  public void setService(Boolean service) {
    this.service = service;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(this.attrValue, userInfo.attrValue) &&
        Objects.equals(this.attrValues, userInfo.attrValues) &&
        Objects.equals(this.authTrackingId, userInfo.authTrackingId) &&
        Objects.equals(this.clientId, userInfo.clientId) &&
        Objects.equals(this.dN, userInfo.dN) &&
        Objects.equals(this.isService, userInfo.isService) &&
        Objects.equals(this.loggableName, userInfo.loggableName) &&
        Objects.equals(this.mAuthTrackingId, userInfo.mAuthTrackingId) &&
        Objects.equals(this.mService, userInfo.mService) &&
        Objects.equals(this.name, userInfo.name) &&
        Objects.equals(this.service, userInfo.service);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attrValue, attrValues, authTrackingId, clientId, dN, isService, loggableName, mAuthTrackingId, mService, name, service);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("    attrValue: ").append(toIndentedString(attrValue)).append("\n");
    sb.append("    attrValues: ").append(toIndentedString(attrValues)).append("\n");
    sb.append("    authTrackingId: ").append(toIndentedString(authTrackingId)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    dN: ").append(toIndentedString(dN)).append("\n");
    sb.append("    isService: ").append(toIndentedString(isService)).append("\n");
    sb.append("    loggableName: ").append(toIndentedString(loggableName)).append("\n");
    sb.append("    mAuthTrackingId: ").append(toIndentedString(mAuthTrackingId)).append("\n");
    sb.append("    mService: ").append(toIndentedString(mService)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
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
