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
 * Dsnode
 */



public class Dsnode {
  @SerializedName("assignedEntityPct")
  private Double assignedEntityPct = null;

  @SerializedName("lastReviewedDate")
  private Long lastReviewedDate = null;

  @SerializedName("lastReviewedDecision")
  private String lastReviewedDecision = null;

  @SerializedName("likeAssignedEntityPct")
  private Double likeAssignedEntityPct = null;

  @SerializedName("likeUserEntityPct")
  private Double likeUserEntityPct = null;

  @SerializedName("uniqueEntityId")
  private String uniqueEntityId = null;

  @SerializedName("uniqueUserId")
  private String uniqueUserId = null;

  @SerializedName("userEntityPct")
  private Double userEntityPct = null;

  public Dsnode assignedEntityPct(Double assignedEntityPct) {
    this.assignedEntityPct = assignedEntityPct;
    return this;
  }

   /**
   * Get assignedEntityPct
   * @return assignedEntityPct
  **/
  @ApiModelProperty(value = "")
  public Double getAssignedEntityPct() {
    return assignedEntityPct;
  }

  public void setAssignedEntityPct(Double assignedEntityPct) {
    this.assignedEntityPct = assignedEntityPct;
  }

  public Dsnode lastReviewedDate(Long lastReviewedDate) {
    this.lastReviewedDate = lastReviewedDate;
    return this;
  }

   /**
   * Get lastReviewedDate
   * @return lastReviewedDate
  **/
  @ApiModelProperty(value = "")
  public Long getLastReviewedDate() {
    return lastReviewedDate;
  }

  public void setLastReviewedDate(Long lastReviewedDate) {
    this.lastReviewedDate = lastReviewedDate;
  }

  public Dsnode lastReviewedDecision(String lastReviewedDecision) {
    this.lastReviewedDecision = lastReviewedDecision;
    return this;
  }

   /**
   * Get lastReviewedDecision
   * @return lastReviewedDecision
  **/
  @ApiModelProperty(value = "")
  public String getLastReviewedDecision() {
    return lastReviewedDecision;
  }

  public void setLastReviewedDecision(String lastReviewedDecision) {
    this.lastReviewedDecision = lastReviewedDecision;
  }

  public Dsnode likeAssignedEntityPct(Double likeAssignedEntityPct) {
    this.likeAssignedEntityPct = likeAssignedEntityPct;
    return this;
  }

   /**
   * Get likeAssignedEntityPct
   * @return likeAssignedEntityPct
  **/
  @ApiModelProperty(value = "")
  public Double getLikeAssignedEntityPct() {
    return likeAssignedEntityPct;
  }

  public void setLikeAssignedEntityPct(Double likeAssignedEntityPct) {
    this.likeAssignedEntityPct = likeAssignedEntityPct;
  }

  public Dsnode likeUserEntityPct(Double likeUserEntityPct) {
    this.likeUserEntityPct = likeUserEntityPct;
    return this;
  }

   /**
   * Get likeUserEntityPct
   * @return likeUserEntityPct
  **/
  @ApiModelProperty(value = "")
  public Double getLikeUserEntityPct() {
    return likeUserEntityPct;
  }

  public void setLikeUserEntityPct(Double likeUserEntityPct) {
    this.likeUserEntityPct = likeUserEntityPct;
  }

  public Dsnode uniqueEntityId(String uniqueEntityId) {
    this.uniqueEntityId = uniqueEntityId;
    return this;
  }

   /**
   * Get uniqueEntityId
   * @return uniqueEntityId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueEntityId() {
    return uniqueEntityId;
  }

  public void setUniqueEntityId(String uniqueEntityId) {
    this.uniqueEntityId = uniqueEntityId;
  }

  public Dsnode uniqueUserId(String uniqueUserId) {
    this.uniqueUserId = uniqueUserId;
    return this;
  }

   /**
   * Get uniqueUserId
   * @return uniqueUserId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueUserId() {
    return uniqueUserId;
  }

  public void setUniqueUserId(String uniqueUserId) {
    this.uniqueUserId = uniqueUserId;
  }

  public Dsnode userEntityPct(Double userEntityPct) {
    this.userEntityPct = userEntityPct;
    return this;
  }

   /**
   * Get userEntityPct
   * @return userEntityPct
  **/
  @ApiModelProperty(value = "")
  public Double getUserEntityPct() {
    return userEntityPct;
  }

  public void setUserEntityPct(Double userEntityPct) {
    this.userEntityPct = userEntityPct;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Dsnode dsnode = (Dsnode) o;
    return Objects.equals(this.assignedEntityPct, dsnode.assignedEntityPct) &&
        Objects.equals(this.lastReviewedDate, dsnode.lastReviewedDate) &&
        Objects.equals(this.lastReviewedDecision, dsnode.lastReviewedDecision) &&
        Objects.equals(this.likeAssignedEntityPct, dsnode.likeAssignedEntityPct) &&
        Objects.equals(this.likeUserEntityPct, dsnode.likeUserEntityPct) &&
        Objects.equals(this.uniqueEntityId, dsnode.uniqueEntityId) &&
        Objects.equals(this.uniqueUserId, dsnode.uniqueUserId) &&
        Objects.equals(this.userEntityPct, dsnode.userEntityPct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assignedEntityPct, lastReviewedDate, lastReviewedDecision, likeAssignedEntityPct, likeUserEntityPct, uniqueEntityId, uniqueUserId, userEntityPct);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dsnode {\n");
    
    sb.append("    assignedEntityPct: ").append(toIndentedString(assignedEntityPct)).append("\n");
    sb.append("    lastReviewedDate: ").append(toIndentedString(lastReviewedDate)).append("\n");
    sb.append("    lastReviewedDecision: ").append(toIndentedString(lastReviewedDecision)).append("\n");
    sb.append("    likeAssignedEntityPct: ").append(toIndentedString(likeAssignedEntityPct)).append("\n");
    sb.append("    likeUserEntityPct: ").append(toIndentedString(likeUserEntityPct)).append("\n");
    sb.append("    uniqueEntityId: ").append(toIndentedString(uniqueEntityId)).append("\n");
    sb.append("    uniqueUserId: ").append(toIndentedString(uniqueUserId)).append("\n");
    sb.append("    userEntityPct: ").append(toIndentedString(userEntityPct)).append("\n");
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
