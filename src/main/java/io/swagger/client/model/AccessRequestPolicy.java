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
import io.swagger.client.model.AllowedItemRequester;
import io.swagger.client.model.DisallowedItemRequester;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AccessRequestPolicy
 */



public class AccessRequestPolicy {
  @SerializedName("allApps")
  private Boolean allApps = null;

  @SerializedName("allPerms")
  private Boolean allPerms = null;

  @SerializedName("allRoles")
  private Boolean allRoles = null;

  @SerializedName("allowAllUsers")
  private AllowedItemRequester allowAllUsers = null;

  @SerializedName("allowedBusinessRoleCount")
  private Long allowedBusinessRoleCount = null;

  @SerializedName("allowedBusinessRoles")
  private List<AllowedItemRequester> allowedBusinessRoles = null;

  @SerializedName("allowedGroupCount")
  private Long allowedGroupCount = null;

  @SerializedName("allowedGroups")
  private List<AllowedItemRequester> allowedGroups = null;

  @SerializedName("allowedUserCount")
  private Long allowedUserCount = null;

  @SerializedName("allowedUsers")
  private List<AllowedItemRequester> allowedUsers = null;

  @SerializedName("applicationsCount")
  private Long applicationsCount = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("changedBy")
  private User changedBy = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("defaultPolicy")
  private Boolean defaultPolicy = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("disallowedGroupCount")
  private Long disallowedGroupCount = null;

  @SerializedName("disallowedGroups")
  private List<DisallowedItemRequester> disallowedGroups = null;

  @SerializedName("disallowedUserCount")
  private Long disallowedUserCount = null;

  @SerializedName("disallowedUsers")
  private List<DisallowedItemRequester> disallowedUsers = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("permissionsCount")
  private Long permissionsCount = null;

  @SerializedName("techRolesCount")
  private Long techRolesCount = null;

  @SerializedName("uniquePolicyId")
  private String uniquePolicyId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  public AccessRequestPolicy allApps(Boolean allApps) {
    this.allApps = allApps;
    return this;
  }

   /**
   * Get allApps
   * @return allApps
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllApps() {
    return allApps;
  }

  public void setAllApps(Boolean allApps) {
    this.allApps = allApps;
  }

  public AccessRequestPolicy allPerms(Boolean allPerms) {
    this.allPerms = allPerms;
    return this;
  }

   /**
   * Get allPerms
   * @return allPerms
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllPerms() {
    return allPerms;
  }

  public void setAllPerms(Boolean allPerms) {
    this.allPerms = allPerms;
  }

  public AccessRequestPolicy allRoles(Boolean allRoles) {
    this.allRoles = allRoles;
    return this;
  }

   /**
   * Get allRoles
   * @return allRoles
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllRoles() {
    return allRoles;
  }

  public void setAllRoles(Boolean allRoles) {
    this.allRoles = allRoles;
  }

  public AccessRequestPolicy allowAllUsers(AllowedItemRequester allowAllUsers) {
    this.allowAllUsers = allowAllUsers;
    return this;
  }

   /**
   * Get allowAllUsers
   * @return allowAllUsers
  **/
  @ApiModelProperty(value = "")
  public AllowedItemRequester getAllowAllUsers() {
    return allowAllUsers;
  }

  public void setAllowAllUsers(AllowedItemRequester allowAllUsers) {
    this.allowAllUsers = allowAllUsers;
  }

  public AccessRequestPolicy allowedBusinessRoleCount(Long allowedBusinessRoleCount) {
    this.allowedBusinessRoleCount = allowedBusinessRoleCount;
    return this;
  }

   /**
   * Get allowedBusinessRoleCount
   * @return allowedBusinessRoleCount
  **/
  @ApiModelProperty(value = "")
  public Long getAllowedBusinessRoleCount() {
    return allowedBusinessRoleCount;
  }

  public void setAllowedBusinessRoleCount(Long allowedBusinessRoleCount) {
    this.allowedBusinessRoleCount = allowedBusinessRoleCount;
  }

  public AccessRequestPolicy allowedBusinessRoles(List<AllowedItemRequester> allowedBusinessRoles) {
    this.allowedBusinessRoles = allowedBusinessRoles;
    return this;
  }

  public AccessRequestPolicy addAllowedBusinessRolesItem(AllowedItemRequester allowedBusinessRolesItem) {
    if (this.allowedBusinessRoles == null) {
      this.allowedBusinessRoles = new ArrayList<AllowedItemRequester>();
    }
    this.allowedBusinessRoles.add(allowedBusinessRolesItem);
    return this;
  }

   /**
   * Get allowedBusinessRoles
   * @return allowedBusinessRoles
  **/
  @ApiModelProperty(value = "")
  public List<AllowedItemRequester> getAllowedBusinessRoles() {
    return allowedBusinessRoles;
  }

  public void setAllowedBusinessRoles(List<AllowedItemRequester> allowedBusinessRoles) {
    this.allowedBusinessRoles = allowedBusinessRoles;
  }

  public AccessRequestPolicy allowedGroupCount(Long allowedGroupCount) {
    this.allowedGroupCount = allowedGroupCount;
    return this;
  }

   /**
   * Get allowedGroupCount
   * @return allowedGroupCount
  **/
  @ApiModelProperty(value = "")
  public Long getAllowedGroupCount() {
    return allowedGroupCount;
  }

  public void setAllowedGroupCount(Long allowedGroupCount) {
    this.allowedGroupCount = allowedGroupCount;
  }

  public AccessRequestPolicy allowedGroups(List<AllowedItemRequester> allowedGroups) {
    this.allowedGroups = allowedGroups;
    return this;
  }

  public AccessRequestPolicy addAllowedGroupsItem(AllowedItemRequester allowedGroupsItem) {
    if (this.allowedGroups == null) {
      this.allowedGroups = new ArrayList<AllowedItemRequester>();
    }
    this.allowedGroups.add(allowedGroupsItem);
    return this;
  }

   /**
   * Get allowedGroups
   * @return allowedGroups
  **/
  @ApiModelProperty(value = "")
  public List<AllowedItemRequester> getAllowedGroups() {
    return allowedGroups;
  }

  public void setAllowedGroups(List<AllowedItemRequester> allowedGroups) {
    this.allowedGroups = allowedGroups;
  }

  public AccessRequestPolicy allowedUserCount(Long allowedUserCount) {
    this.allowedUserCount = allowedUserCount;
    return this;
  }

   /**
   * Get allowedUserCount
   * @return allowedUserCount
  **/
  @ApiModelProperty(value = "")
  public Long getAllowedUserCount() {
    return allowedUserCount;
  }

  public void setAllowedUserCount(Long allowedUserCount) {
    this.allowedUserCount = allowedUserCount;
  }

  public AccessRequestPolicy allowedUsers(List<AllowedItemRequester> allowedUsers) {
    this.allowedUsers = allowedUsers;
    return this;
  }

  public AccessRequestPolicy addAllowedUsersItem(AllowedItemRequester allowedUsersItem) {
    if (this.allowedUsers == null) {
      this.allowedUsers = new ArrayList<AllowedItemRequester>();
    }
    this.allowedUsers.add(allowedUsersItem);
    return this;
  }

   /**
   * Get allowedUsers
   * @return allowedUsers
  **/
  @ApiModelProperty(value = "")
  public List<AllowedItemRequester> getAllowedUsers() {
    return allowedUsers;
  }

  public void setAllowedUsers(List<AllowedItemRequester> allowedUsers) {
    this.allowedUsers = allowedUsers;
  }

  public AccessRequestPolicy applicationsCount(Long applicationsCount) {
    this.applicationsCount = applicationsCount;
    return this;
  }

   /**
   * Get applicationsCount
   * @return applicationsCount
  **/
  @ApiModelProperty(value = "")
  public Long getApplicationsCount() {
    return applicationsCount;
  }

  public void setApplicationsCount(Long applicationsCount) {
    this.applicationsCount = applicationsCount;
  }

  public AccessRequestPolicy canEditEntity(Boolean canEditEntity) {
    this.canEditEntity = canEditEntity;
    return this;
  }

   /**
   * Get canEditEntity
   * @return canEditEntity
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanEditEntity() {
    return canEditEntity;
  }

  public void setCanEditEntity(Boolean canEditEntity) {
    this.canEditEntity = canEditEntity;
  }

  public AccessRequestPolicy changedBy(User changedBy) {
    this.changedBy = changedBy;
    return this;
  }

   /**
   * Get changedBy
   * @return changedBy
  **/
  @ApiModelProperty(value = "")
  public User getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(User changedBy) {
    this.changedBy = changedBy;
  }

  public AccessRequestPolicy createdBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Get createdBy
   * @return createdBy
  **/
  @ApiModelProperty(value = "")
  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public AccessRequestPolicy creationTime(Long creationTime) {
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

  public AccessRequestPolicy defaultPolicy(Boolean defaultPolicy) {
    this.defaultPolicy = defaultPolicy;
    return this;
  }

   /**
   * Get defaultPolicy
   * @return defaultPolicy
  **/
  @ApiModelProperty(value = "")
  public Boolean isDefaultPolicy() {
    return defaultPolicy;
  }

  public void setDefaultPolicy(Boolean defaultPolicy) {
    this.defaultPolicy = defaultPolicy;
  }

  public AccessRequestPolicy description(String description) {
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

  public AccessRequestPolicy disallowedGroupCount(Long disallowedGroupCount) {
    this.disallowedGroupCount = disallowedGroupCount;
    return this;
  }

   /**
   * Get disallowedGroupCount
   * @return disallowedGroupCount
  **/
  @ApiModelProperty(value = "")
  public Long getDisallowedGroupCount() {
    return disallowedGroupCount;
  }

  public void setDisallowedGroupCount(Long disallowedGroupCount) {
    this.disallowedGroupCount = disallowedGroupCount;
  }

  public AccessRequestPolicy disallowedGroups(List<DisallowedItemRequester> disallowedGroups) {
    this.disallowedGroups = disallowedGroups;
    return this;
  }

  public AccessRequestPolicy addDisallowedGroupsItem(DisallowedItemRequester disallowedGroupsItem) {
    if (this.disallowedGroups == null) {
      this.disallowedGroups = new ArrayList<DisallowedItemRequester>();
    }
    this.disallowedGroups.add(disallowedGroupsItem);
    return this;
  }

   /**
   * Get disallowedGroups
   * @return disallowedGroups
  **/
  @ApiModelProperty(value = "")
  public List<DisallowedItemRequester> getDisallowedGroups() {
    return disallowedGroups;
  }

  public void setDisallowedGroups(List<DisallowedItemRequester> disallowedGroups) {
    this.disallowedGroups = disallowedGroups;
  }

  public AccessRequestPolicy disallowedUserCount(Long disallowedUserCount) {
    this.disallowedUserCount = disallowedUserCount;
    return this;
  }

   /**
   * Get disallowedUserCount
   * @return disallowedUserCount
  **/
  @ApiModelProperty(value = "")
  public Long getDisallowedUserCount() {
    return disallowedUserCount;
  }

  public void setDisallowedUserCount(Long disallowedUserCount) {
    this.disallowedUserCount = disallowedUserCount;
  }

  public AccessRequestPolicy disallowedUsers(List<DisallowedItemRequester> disallowedUsers) {
    this.disallowedUsers = disallowedUsers;
    return this;
  }

  public AccessRequestPolicy addDisallowedUsersItem(DisallowedItemRequester disallowedUsersItem) {
    if (this.disallowedUsers == null) {
      this.disallowedUsers = new ArrayList<DisallowedItemRequester>();
    }
    this.disallowedUsers.add(disallowedUsersItem);
    return this;
  }

   /**
   * Get disallowedUsers
   * @return disallowedUsers
  **/
  @ApiModelProperty(value = "")
  public List<DisallowedItemRequester> getDisallowedUsers() {
    return disallowedUsers;
  }

  public void setDisallowedUsers(List<DisallowedItemRequester> disallowedUsers) {
    this.disallowedUsers = disallowedUsers;
  }

  public AccessRequestPolicy id(Long id) {
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

  public AccessRequestPolicy name(String name) {
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

  public AccessRequestPolicy permissionsCount(Long permissionsCount) {
    this.permissionsCount = permissionsCount;
    return this;
  }

   /**
   * Get permissionsCount
   * @return permissionsCount
  **/
  @ApiModelProperty(value = "")
  public Long getPermissionsCount() {
    return permissionsCount;
  }

  public void setPermissionsCount(Long permissionsCount) {
    this.permissionsCount = permissionsCount;
  }

  public AccessRequestPolicy techRolesCount(Long techRolesCount) {
    this.techRolesCount = techRolesCount;
    return this;
  }

   /**
   * Get techRolesCount
   * @return techRolesCount
  **/
  @ApiModelProperty(value = "")
  public Long getTechRolesCount() {
    return techRolesCount;
  }

  public void setTechRolesCount(Long techRolesCount) {
    this.techRolesCount = techRolesCount;
  }

  public AccessRequestPolicy uniquePolicyId(String uniquePolicyId) {
    this.uniquePolicyId = uniquePolicyId;
    return this;
  }

   /**
   * Get uniquePolicyId
   * @return uniquePolicyId
  **/
  @ApiModelProperty(value = "")
  public String getUniquePolicyId() {
    return uniquePolicyId;
  }

  public void setUniquePolicyId(String uniquePolicyId) {
    this.uniquePolicyId = uniquePolicyId;
  }

  public AccessRequestPolicy updateTime(Long updateTime) {
    this.updateTime = updateTime;
    return this;
  }

   /**
   * Get updateTime
   * @return updateTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccessRequestPolicy accessRequestPolicy = (AccessRequestPolicy) o;
    return Objects.equals(this.allApps, accessRequestPolicy.allApps) &&
        Objects.equals(this.allPerms, accessRequestPolicy.allPerms) &&
        Objects.equals(this.allRoles, accessRequestPolicy.allRoles) &&
        Objects.equals(this.allowAllUsers, accessRequestPolicy.allowAllUsers) &&
        Objects.equals(this.allowedBusinessRoleCount, accessRequestPolicy.allowedBusinessRoleCount) &&
        Objects.equals(this.allowedBusinessRoles, accessRequestPolicy.allowedBusinessRoles) &&
        Objects.equals(this.allowedGroupCount, accessRequestPolicy.allowedGroupCount) &&
        Objects.equals(this.allowedGroups, accessRequestPolicy.allowedGroups) &&
        Objects.equals(this.allowedUserCount, accessRequestPolicy.allowedUserCount) &&
        Objects.equals(this.allowedUsers, accessRequestPolicy.allowedUsers) &&
        Objects.equals(this.applicationsCount, accessRequestPolicy.applicationsCount) &&
        Objects.equals(this.canEditEntity, accessRequestPolicy.canEditEntity) &&
        Objects.equals(this.changedBy, accessRequestPolicy.changedBy) &&
        Objects.equals(this.createdBy, accessRequestPolicy.createdBy) &&
        Objects.equals(this.creationTime, accessRequestPolicy.creationTime) &&
        Objects.equals(this.defaultPolicy, accessRequestPolicy.defaultPolicy) &&
        Objects.equals(this.description, accessRequestPolicy.description) &&
        Objects.equals(this.disallowedGroupCount, accessRequestPolicy.disallowedGroupCount) &&
        Objects.equals(this.disallowedGroups, accessRequestPolicy.disallowedGroups) &&
        Objects.equals(this.disallowedUserCount, accessRequestPolicy.disallowedUserCount) &&
        Objects.equals(this.disallowedUsers, accessRequestPolicy.disallowedUsers) &&
        Objects.equals(this.id, accessRequestPolicy.id) &&
        Objects.equals(this.name, accessRequestPolicy.name) &&
        Objects.equals(this.permissionsCount, accessRequestPolicy.permissionsCount) &&
        Objects.equals(this.techRolesCount, accessRequestPolicy.techRolesCount) &&
        Objects.equals(this.uniquePolicyId, accessRequestPolicy.uniquePolicyId) &&
        Objects.equals(this.updateTime, accessRequestPolicy.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allApps, allPerms, allRoles, allowAllUsers, allowedBusinessRoleCount, allowedBusinessRoles, allowedGroupCount, allowedGroups, allowedUserCount, allowedUsers, applicationsCount, canEditEntity, changedBy, createdBy, creationTime, defaultPolicy, description, disallowedGroupCount, disallowedGroups, disallowedUserCount, disallowedUsers, id, name, permissionsCount, techRolesCount, uniquePolicyId, updateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccessRequestPolicy {\n");
    
    sb.append("    allApps: ").append(toIndentedString(allApps)).append("\n");
    sb.append("    allPerms: ").append(toIndentedString(allPerms)).append("\n");
    sb.append("    allRoles: ").append(toIndentedString(allRoles)).append("\n");
    sb.append("    allowAllUsers: ").append(toIndentedString(allowAllUsers)).append("\n");
    sb.append("    allowedBusinessRoleCount: ").append(toIndentedString(allowedBusinessRoleCount)).append("\n");
    sb.append("    allowedBusinessRoles: ").append(toIndentedString(allowedBusinessRoles)).append("\n");
    sb.append("    allowedGroupCount: ").append(toIndentedString(allowedGroupCount)).append("\n");
    sb.append("    allowedGroups: ").append(toIndentedString(allowedGroups)).append("\n");
    sb.append("    allowedUserCount: ").append(toIndentedString(allowedUserCount)).append("\n");
    sb.append("    allowedUsers: ").append(toIndentedString(allowedUsers)).append("\n");
    sb.append("    applicationsCount: ").append(toIndentedString(applicationsCount)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    defaultPolicy: ").append(toIndentedString(defaultPolicy)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    disallowedGroupCount: ").append(toIndentedString(disallowedGroupCount)).append("\n");
    sb.append("    disallowedGroups: ").append(toIndentedString(disallowedGroups)).append("\n");
    sb.append("    disallowedUserCount: ").append(toIndentedString(disallowedUserCount)).append("\n");
    sb.append("    disallowedUsers: ").append(toIndentedString(disallowedUsers)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    permissionsCount: ").append(toIndentedString(permissionsCount)).append("\n");
    sb.append("    techRolesCount: ").append(toIndentedString(techRolesCount)).append("\n");
    sb.append("    uniquePolicyId: ").append(toIndentedString(uniquePolicyId)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
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
