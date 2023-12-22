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
import io.swagger.client.model.Sod;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Policies
 */



public class Policies {
  @SerializedName("active")
  private Long active = null;

  @SerializedName("arraySize")
  private Integer arraySize = null;

  @SerializedName("canManageSods")
  private Boolean canManageSods = null;

  @SerializedName("canViewSodApprovals")
  private Boolean canViewSodApprovals = null;

  @SerializedName("disabled")
  private Long disabled = null;

  @SerializedName("inactive")
  private Long inactive = null;

  @SerializedName("policies")
  private List<Sod> policies = null;

  @SerializedName("totalSearch")
  private Long totalSearch = null;

  @SerializedName("unfilteredCount")
  private Long unfilteredCount = null;

  public Policies active(Long active) {
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @ApiModelProperty(value = "")
  public Long getActive() {
    return active;
  }

  public void setActive(Long active) {
    this.active = active;
  }

  public Policies arraySize(Integer arraySize) {
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

  public Policies canManageSods(Boolean canManageSods) {
    this.canManageSods = canManageSods;
    return this;
  }

   /**
   * Get canManageSods
   * @return canManageSods
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanManageSods() {
    return canManageSods;
  }

  public void setCanManageSods(Boolean canManageSods) {
    this.canManageSods = canManageSods;
  }

  public Policies canViewSodApprovals(Boolean canViewSodApprovals) {
    this.canViewSodApprovals = canViewSodApprovals;
    return this;
  }

   /**
   * Get canViewSodApprovals
   * @return canViewSodApprovals
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanViewSodApprovals() {
    return canViewSodApprovals;
  }

  public void setCanViewSodApprovals(Boolean canViewSodApprovals) {
    this.canViewSodApprovals = canViewSodApprovals;
  }

  public Policies disabled(Long disabled) {
    this.disabled = disabled;
    return this;
  }

   /**
   * Get disabled
   * @return disabled
  **/
  @ApiModelProperty(value = "")
  public Long getDisabled() {
    return disabled;
  }

  public void setDisabled(Long disabled) {
    this.disabled = disabled;
  }

  public Policies inactive(Long inactive) {
    this.inactive = inactive;
    return this;
  }

   /**
   * Get inactive
   * @return inactive
  **/
  @ApiModelProperty(value = "")
  public Long getInactive() {
    return inactive;
  }

  public void setInactive(Long inactive) {
    this.inactive = inactive;
  }

  public Policies policies(List<Sod> policies) {
    this.policies = policies;
    return this;
  }

  public Policies addPoliciesItem(Sod policiesItem) {
    if (this.policies == null) {
      this.policies = new ArrayList<Sod>();
    }
    this.policies.add(policiesItem);
    return this;
  }

   /**
   * Get policies
   * @return policies
  **/
  @ApiModelProperty(value = "")
  public List<Sod> getPolicies() {
    return policies;
  }

  public void setPolicies(List<Sod> policies) {
    this.policies = policies;
  }

  public Policies totalSearch(Long totalSearch) {
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

  public Policies unfilteredCount(Long unfilteredCount) {
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
    Policies policies = (Policies) o;
    return Objects.equals(this.active, policies.active) &&
        Objects.equals(this.arraySize, policies.arraySize) &&
        Objects.equals(this.canManageSods, policies.canManageSods) &&
        Objects.equals(this.canViewSodApprovals, policies.canViewSodApprovals) &&
        Objects.equals(this.disabled, policies.disabled) &&
        Objects.equals(this.inactive, policies.inactive) &&
        Objects.equals(this.policies, policies.policies) &&
        Objects.equals(this.totalSearch, policies.totalSearch) &&
        Objects.equals(this.unfilteredCount, policies.unfilteredCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, arraySize, canManageSods, canViewSodApprovals, disabled, inactive, policies, totalSearch, unfilteredCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Policies {\n");
    
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    arraySize: ").append(toIndentedString(arraySize)).append("\n");
    sb.append("    canManageSods: ").append(toIndentedString(canManageSods)).append("\n");
    sb.append("    canViewSodApprovals: ").append(toIndentedString(canViewSodApprovals)).append("\n");
    sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
    sb.append("    inactive: ").append(toIndentedString(inactive)).append("\n");
    sb.append("    policies: ").append(toIndentedString(policies)).append("\n");
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
