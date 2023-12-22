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
import io.swagger.client.model.Britem;
import io.swagger.client.model.SearchCriteria;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Sodcrit
 */



public class Sodcrit {
  @SerializedName("broles")
  private List<String> broles = null;

  @SerializedName("excludedGroups")
  private List<Britem> excludedGroups = null;

  @SerializedName("excludedUsers")
  private List<Britem> excludedUsers = null;

  @SerializedName("gracePeriod")
  private Long gracePeriod = null;

  @SerializedName("includedGroups")
  private List<Britem> includedGroups = null;

  @SerializedName("includedUsers")
  private List<Britem> includedUsers = null;

  @SerializedName("membCrits")
  private List<Britem> membCrits = null;

  @SerializedName("perms")
  private List<Britem> perms = null;

  @SerializedName("roles")
  private List<Britem> roles = null;

  @SerializedName("serverSearchCriteria")
  private SearchCriteria serverSearchCriteria = null;

  public Sodcrit broles(List<String> broles) {
    this.broles = broles;
    return this;
  }

  public Sodcrit addBrolesItem(String brolesItem) {
    if (this.broles == null) {
      this.broles = new ArrayList<String>();
    }
    this.broles.add(brolesItem);
    return this;
  }

   /**
   * Get broles
   * @return broles
  **/
  @ApiModelProperty(value = "")
  public List<String> getBroles() {
    return broles;
  }

  public void setBroles(List<String> broles) {
    this.broles = broles;
  }

  public Sodcrit excludedGroups(List<Britem> excludedGroups) {
    this.excludedGroups = excludedGroups;
    return this;
  }

  public Sodcrit addExcludedGroupsItem(Britem excludedGroupsItem) {
    if (this.excludedGroups == null) {
      this.excludedGroups = new ArrayList<Britem>();
    }
    this.excludedGroups.add(excludedGroupsItem);
    return this;
  }

   /**
   * Get excludedGroups
   * @return excludedGroups
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getExcludedGroups() {
    return excludedGroups;
  }

  public void setExcludedGroups(List<Britem> excludedGroups) {
    this.excludedGroups = excludedGroups;
  }

  public Sodcrit excludedUsers(List<Britem> excludedUsers) {
    this.excludedUsers = excludedUsers;
    return this;
  }

  public Sodcrit addExcludedUsersItem(Britem excludedUsersItem) {
    if (this.excludedUsers == null) {
      this.excludedUsers = new ArrayList<Britem>();
    }
    this.excludedUsers.add(excludedUsersItem);
    return this;
  }

   /**
   * Get excludedUsers
   * @return excludedUsers
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getExcludedUsers() {
    return excludedUsers;
  }

  public void setExcludedUsers(List<Britem> excludedUsers) {
    this.excludedUsers = excludedUsers;
  }

  public Sodcrit gracePeriod(Long gracePeriod) {
    this.gracePeriod = gracePeriod;
    return this;
  }

   /**
   * Get gracePeriod
   * @return gracePeriod
  **/
  @ApiModelProperty(value = "")
  public Long getGracePeriod() {
    return gracePeriod;
  }

  public void setGracePeriod(Long gracePeriod) {
    this.gracePeriod = gracePeriod;
  }

  public Sodcrit includedGroups(List<Britem> includedGroups) {
    this.includedGroups = includedGroups;
    return this;
  }

  public Sodcrit addIncludedGroupsItem(Britem includedGroupsItem) {
    if (this.includedGroups == null) {
      this.includedGroups = new ArrayList<Britem>();
    }
    this.includedGroups.add(includedGroupsItem);
    return this;
  }

   /**
   * Get includedGroups
   * @return includedGroups
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getIncludedGroups() {
    return includedGroups;
  }

  public void setIncludedGroups(List<Britem> includedGroups) {
    this.includedGroups = includedGroups;
  }

  public Sodcrit includedUsers(List<Britem> includedUsers) {
    this.includedUsers = includedUsers;
    return this;
  }

  public Sodcrit addIncludedUsersItem(Britem includedUsersItem) {
    if (this.includedUsers == null) {
      this.includedUsers = new ArrayList<Britem>();
    }
    this.includedUsers.add(includedUsersItem);
    return this;
  }

   /**
   * Get includedUsers
   * @return includedUsers
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getIncludedUsers() {
    return includedUsers;
  }

  public void setIncludedUsers(List<Britem> includedUsers) {
    this.includedUsers = includedUsers;
  }

  public Sodcrit membCrits(List<Britem> membCrits) {
    this.membCrits = membCrits;
    return this;
  }

  public Sodcrit addMembCritsItem(Britem membCritsItem) {
    if (this.membCrits == null) {
      this.membCrits = new ArrayList<Britem>();
    }
    this.membCrits.add(membCritsItem);
    return this;
  }

   /**
   * Get membCrits
   * @return membCrits
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getMembCrits() {
    return membCrits;
  }

  public void setMembCrits(List<Britem> membCrits) {
    this.membCrits = membCrits;
  }

  public Sodcrit perms(List<Britem> perms) {
    this.perms = perms;
    return this;
  }

  public Sodcrit addPermsItem(Britem permsItem) {
    if (this.perms == null) {
      this.perms = new ArrayList<Britem>();
    }
    this.perms.add(permsItem);
    return this;
  }

   /**
   * Get perms
   * @return perms
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getPerms() {
    return perms;
  }

  public void setPerms(List<Britem> perms) {
    this.perms = perms;
  }

  public Sodcrit roles(List<Britem> roles) {
    this.roles = roles;
    return this;
  }

  public Sodcrit addRolesItem(Britem rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<Britem>();
    }
    this.roles.add(rolesItem);
    return this;
  }

   /**
   * Get roles
   * @return roles
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getRoles() {
    return roles;
  }

  public void setRoles(List<Britem> roles) {
    this.roles = roles;
  }

  public Sodcrit serverSearchCriteria(SearchCriteria serverSearchCriteria) {
    this.serverSearchCriteria = serverSearchCriteria;
    return this;
  }

   /**
   * Get serverSearchCriteria
   * @return serverSearchCriteria
  **/
  @ApiModelProperty(value = "")
  public SearchCriteria getServerSearchCriteria() {
    return serverSearchCriteria;
  }

  public void setServerSearchCriteria(SearchCriteria serverSearchCriteria) {
    this.serverSearchCriteria = serverSearchCriteria;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sodcrit sodcrit = (Sodcrit) o;
    return Objects.equals(this.broles, sodcrit.broles) &&
        Objects.equals(this.excludedGroups, sodcrit.excludedGroups) &&
        Objects.equals(this.excludedUsers, sodcrit.excludedUsers) &&
        Objects.equals(this.gracePeriod, sodcrit.gracePeriod) &&
        Objects.equals(this.includedGroups, sodcrit.includedGroups) &&
        Objects.equals(this.includedUsers, sodcrit.includedUsers) &&
        Objects.equals(this.membCrits, sodcrit.membCrits) &&
        Objects.equals(this.perms, sodcrit.perms) &&
        Objects.equals(this.roles, sodcrit.roles) &&
        Objects.equals(this.serverSearchCriteria, sodcrit.serverSearchCriteria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(broles, excludedGroups, excludedUsers, gracePeriod, includedGroups, includedUsers, membCrits, perms, roles, serverSearchCriteria);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Sodcrit {\n");
    
    sb.append("    broles: ").append(toIndentedString(broles)).append("\n");
    sb.append("    excludedGroups: ").append(toIndentedString(excludedGroups)).append("\n");
    sb.append("    excludedUsers: ").append(toIndentedString(excludedUsers)).append("\n");
    sb.append("    gracePeriod: ").append(toIndentedString(gracePeriod)).append("\n");
    sb.append("    includedGroups: ").append(toIndentedString(includedGroups)).append("\n");
    sb.append("    includedUsers: ").append(toIndentedString(includedUsers)).append("\n");
    sb.append("    membCrits: ").append(toIndentedString(membCrits)).append("\n");
    sb.append("    perms: ").append(toIndentedString(perms)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    serverSearchCriteria: ").append(toIndentedString(serverSearchCriteria)).append("\n");
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