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
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * DelegateMapping
 */



public class DelegateMapping {
  @SerializedName("active")
  private Boolean active = null;

  @SerializedName("activeCount")
  private Integer activeCount = null;

  @SerializedName("delegate")
  private User delegate = null;

  @SerializedName("endTime")
  private Long endTime = null;

  @SerializedName("lastModifyBy")
  private User lastModifyBy = null;

  @SerializedName("lastModifyTime")
  private Long lastModifyTime = null;

  @SerializedName("mappingId")
  private Long mappingId = null;

  @SerializedName("mappingIds")
  private List<Long> mappingIds = null;

  @SerializedName("reason")
  private String reason = null;

  @SerializedName("startTime")
  private Long startTime = null;

  @SerializedName("totalCount")
  private Integer totalCount = null;

  @SerializedName("user")
  private User user = null;

  @SerializedName("validForRequests")
  private Boolean validForRequests = null;

  @SerializedName("validForReviews")
  private Boolean validForReviews = null;

  public DelegateMapping active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @ApiModelProperty(value = "")
  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public DelegateMapping activeCount(Integer activeCount) {
    this.activeCount = activeCount;
    return this;
  }

   /**
   * Get activeCount
   * @return activeCount
  **/
  @ApiModelProperty(value = "")
  public Integer getActiveCount() {
    return activeCount;
  }

  public void setActiveCount(Integer activeCount) {
    this.activeCount = activeCount;
  }

  public DelegateMapping delegate(User delegate) {
    this.delegate = delegate;
    return this;
  }

   /**
   * Get delegate
   * @return delegate
  **/
  @ApiModelProperty(value = "")
  public User getDelegate() {
    return delegate;
  }

  public void setDelegate(User delegate) {
    this.delegate = delegate;
  }

  public DelegateMapping endTime(Long endTime) {
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

  public DelegateMapping lastModifyBy(User lastModifyBy) {
    this.lastModifyBy = lastModifyBy;
    return this;
  }

   /**
   * Get lastModifyBy
   * @return lastModifyBy
  **/
  @ApiModelProperty(value = "")
  public User getLastModifyBy() {
    return lastModifyBy;
  }

  public void setLastModifyBy(User lastModifyBy) {
    this.lastModifyBy = lastModifyBy;
  }

  public DelegateMapping lastModifyTime(Long lastModifyTime) {
    this.lastModifyTime = lastModifyTime;
    return this;
  }

   /**
   * Get lastModifyTime
   * @return lastModifyTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastModifyTime() {
    return lastModifyTime;
  }

  public void setLastModifyTime(Long lastModifyTime) {
    this.lastModifyTime = lastModifyTime;
  }

  public DelegateMapping mappingId(Long mappingId) {
    this.mappingId = mappingId;
    return this;
  }

   /**
   * Get mappingId
   * @return mappingId
  **/
  @ApiModelProperty(value = "")
  public Long getMappingId() {
    return mappingId;
  }

  public void setMappingId(Long mappingId) {
    this.mappingId = mappingId;
  }

  public DelegateMapping mappingIds(List<Long> mappingIds) {
    this.mappingIds = mappingIds;
    return this;
  }

  public DelegateMapping addMappingIdsItem(Long mappingIdsItem) {
    if (this.mappingIds == null) {
      this.mappingIds = new ArrayList<Long>();
    }
    this.mappingIds.add(mappingIdsItem);
    return this;
  }

   /**
   * Get mappingIds
   * @return mappingIds
  **/
  @ApiModelProperty(value = "")
  public List<Long> getMappingIds() {
    return mappingIds;
  }

  public void setMappingIds(List<Long> mappingIds) {
    this.mappingIds = mappingIds;
  }

  public DelegateMapping reason(String reason) {
    this.reason = reason;
    return this;
  }

   /**
   * Get reason
   * @return reason
  **/
  @ApiModelProperty(value = "")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public DelegateMapping startTime(Long startTime) {
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

  public DelegateMapping totalCount(Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }

   /**
   * Get totalCount
   * @return totalCount
  **/
  @ApiModelProperty(value = "")
  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public DelegateMapping user(User user) {
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @ApiModelProperty(value = "")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public DelegateMapping validForRequests(Boolean validForRequests) {
    this.validForRequests = validForRequests;
    return this;
  }

   /**
   * Get validForRequests
   * @return validForRequests
  **/
  @ApiModelProperty(value = "")
  public Boolean isValidForRequests() {
    return validForRequests;
  }

  public void setValidForRequests(Boolean validForRequests) {
    this.validForRequests = validForRequests;
  }

  public DelegateMapping validForReviews(Boolean validForReviews) {
    this.validForReviews = validForReviews;
    return this;
  }

   /**
   * Get validForReviews
   * @return validForReviews
  **/
  @ApiModelProperty(value = "")
  public Boolean isValidForReviews() {
    return validForReviews;
  }

  public void setValidForReviews(Boolean validForReviews) {
    this.validForReviews = validForReviews;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegateMapping delegateMapping = (DelegateMapping) o;
    return Objects.equals(this.active, delegateMapping.active) &&
        Objects.equals(this.activeCount, delegateMapping.activeCount) &&
        Objects.equals(this.delegate, delegateMapping.delegate) &&
        Objects.equals(this.endTime, delegateMapping.endTime) &&
        Objects.equals(this.lastModifyBy, delegateMapping.lastModifyBy) &&
        Objects.equals(this.lastModifyTime, delegateMapping.lastModifyTime) &&
        Objects.equals(this.mappingId, delegateMapping.mappingId) &&
        Objects.equals(this.mappingIds, delegateMapping.mappingIds) &&
        Objects.equals(this.reason, delegateMapping.reason) &&
        Objects.equals(this.startTime, delegateMapping.startTime) &&
        Objects.equals(this.totalCount, delegateMapping.totalCount) &&
        Objects.equals(this.user, delegateMapping.user) &&
        Objects.equals(this.validForRequests, delegateMapping.validForRequests) &&
        Objects.equals(this.validForReviews, delegateMapping.validForReviews);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, activeCount, delegate, endTime, lastModifyBy, lastModifyTime, mappingId, mappingIds, reason, startTime, totalCount, user, validForRequests, validForReviews);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegateMapping {\n");
    
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    activeCount: ").append(toIndentedString(activeCount)).append("\n");
    sb.append("    delegate: ").append(toIndentedString(delegate)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    lastModifyBy: ").append(toIndentedString(lastModifyBy)).append("\n");
    sb.append("    lastModifyTime: ").append(toIndentedString(lastModifyTime)).append("\n");
    sb.append("    mappingId: ").append(toIndentedString(mappingId)).append("\n");
    sb.append("    mappingIds: ").append(toIndentedString(mappingIds)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    validForRequests: ").append(toIndentedString(validForRequests)).append("\n");
    sb.append("    validForReviews: ").append(toIndentedString(validForReviews)).append("\n");
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
