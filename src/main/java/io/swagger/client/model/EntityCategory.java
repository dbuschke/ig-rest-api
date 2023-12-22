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
import io.swagger.client.model.CatetoryItem;
import io.swagger.client.model.EntityTypeAndIdNode;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * EntityCategory
 */



public class EntityCategory {
  @SerializedName("addEntities")
  private List<EntityTypeAndIdNode> addEntities = null;

  @SerializedName("assignedAppCount")
  private Long assignedAppCount = null;

  @SerializedName("assignedBroleCount")
  private Long assignedBroleCount = null;

  @SerializedName("assignedEntities")
  private List<CatetoryItem> assignedEntities = null;

  @SerializedName("assignedEntityCount")
  private Long assignedEntityCount = null;

  @SerializedName("assignedPermCount")
  private Long assignedPermCount = null;

  @SerializedName("assignedTroleCount")
  private Long assignedTroleCount = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("changedBy")
  private User changedBy = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("removeEntities")
  private List<EntityTypeAndIdNode> removeEntities = null;

  @SerializedName("type")
  private String type = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  public EntityCategory addEntities(List<EntityTypeAndIdNode> addEntities) {
    this.addEntities = addEntities;
    return this;
  }

  public EntityCategory addAddEntitiesItem(EntityTypeAndIdNode addEntitiesItem) {
    if (this.addEntities == null) {
      this.addEntities = new ArrayList<EntityTypeAndIdNode>();
    }
    this.addEntities.add(addEntitiesItem);
    return this;
  }

   /**
   * Get addEntities
   * @return addEntities
  **/
  @ApiModelProperty(value = "")
  public List<EntityTypeAndIdNode> getAddEntities() {
    return addEntities;
  }

  public void setAddEntities(List<EntityTypeAndIdNode> addEntities) {
    this.addEntities = addEntities;
  }

  public EntityCategory assignedAppCount(Long assignedAppCount) {
    this.assignedAppCount = assignedAppCount;
    return this;
  }

   /**
   * Get assignedAppCount
   * @return assignedAppCount
  **/
  @ApiModelProperty(value = "")
  public Long getAssignedAppCount() {
    return assignedAppCount;
  }

  public void setAssignedAppCount(Long assignedAppCount) {
    this.assignedAppCount = assignedAppCount;
  }

  public EntityCategory assignedBroleCount(Long assignedBroleCount) {
    this.assignedBroleCount = assignedBroleCount;
    return this;
  }

   /**
   * Get assignedBroleCount
   * @return assignedBroleCount
  **/
  @ApiModelProperty(value = "")
  public Long getAssignedBroleCount() {
    return assignedBroleCount;
  }

  public void setAssignedBroleCount(Long assignedBroleCount) {
    this.assignedBroleCount = assignedBroleCount;
  }

  public EntityCategory assignedEntities(List<CatetoryItem> assignedEntities) {
    this.assignedEntities = assignedEntities;
    return this;
  }

  public EntityCategory addAssignedEntitiesItem(CatetoryItem assignedEntitiesItem) {
    if (this.assignedEntities == null) {
      this.assignedEntities = new ArrayList<CatetoryItem>();
    }
    this.assignedEntities.add(assignedEntitiesItem);
    return this;
  }

   /**
   * Get assignedEntities
   * @return assignedEntities
  **/
  @ApiModelProperty(value = "")
  public List<CatetoryItem> getAssignedEntities() {
    return assignedEntities;
  }

  public void setAssignedEntities(List<CatetoryItem> assignedEntities) {
    this.assignedEntities = assignedEntities;
  }

  public EntityCategory assignedEntityCount(Long assignedEntityCount) {
    this.assignedEntityCount = assignedEntityCount;
    return this;
  }

   /**
   * Get assignedEntityCount
   * @return assignedEntityCount
  **/
  @ApiModelProperty(value = "")
  public Long getAssignedEntityCount() {
    return assignedEntityCount;
  }

  public void setAssignedEntityCount(Long assignedEntityCount) {
    this.assignedEntityCount = assignedEntityCount;
  }

  public EntityCategory assignedPermCount(Long assignedPermCount) {
    this.assignedPermCount = assignedPermCount;
    return this;
  }

   /**
   * Get assignedPermCount
   * @return assignedPermCount
  **/
  @ApiModelProperty(value = "")
  public Long getAssignedPermCount() {
    return assignedPermCount;
  }

  public void setAssignedPermCount(Long assignedPermCount) {
    this.assignedPermCount = assignedPermCount;
  }

  public EntityCategory assignedTroleCount(Long assignedTroleCount) {
    this.assignedTroleCount = assignedTroleCount;
    return this;
  }

   /**
   * Get assignedTroleCount
   * @return assignedTroleCount
  **/
  @ApiModelProperty(value = "")
  public Long getAssignedTroleCount() {
    return assignedTroleCount;
  }

  public void setAssignedTroleCount(Long assignedTroleCount) {
    this.assignedTroleCount = assignedTroleCount;
  }

  public EntityCategory canEditEntity(Boolean canEditEntity) {
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

  public EntityCategory changedBy(User changedBy) {
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

  public EntityCategory createdBy(User createdBy) {
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

  public EntityCategory creationTime(Long creationTime) {
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

  public EntityCategory deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Get deleted
   * @return deleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public EntityCategory description(String description) {
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

  public EntityCategory id(Long id) {
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

  public EntityCategory link(String link) {
    this.link = link;
    return this;
  }

   /**
   * Get link
   * @return link
  **/
  @ApiModelProperty(value = "")
  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public EntityCategory name(String name) {
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

  public EntityCategory removeEntities(List<EntityTypeAndIdNode> removeEntities) {
    this.removeEntities = removeEntities;
    return this;
  }

  public EntityCategory addRemoveEntitiesItem(EntityTypeAndIdNode removeEntitiesItem) {
    if (this.removeEntities == null) {
      this.removeEntities = new ArrayList<EntityTypeAndIdNode>();
    }
    this.removeEntities.add(removeEntitiesItem);
    return this;
  }

   /**
   * Get removeEntities
   * @return removeEntities
  **/
  @ApiModelProperty(value = "")
  public List<EntityTypeAndIdNode> getRemoveEntities() {
    return removeEntities;
  }

  public void setRemoveEntities(List<EntityTypeAndIdNode> removeEntities) {
    this.removeEntities = removeEntities;
  }

  public EntityCategory type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public EntityCategory uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

   /**
   * Get uniqueId
   * @return uniqueId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public EntityCategory updateTime(Long updateTime) {
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
    EntityCategory entityCategory = (EntityCategory) o;
    return Objects.equals(this.addEntities, entityCategory.addEntities) &&
        Objects.equals(this.assignedAppCount, entityCategory.assignedAppCount) &&
        Objects.equals(this.assignedBroleCount, entityCategory.assignedBroleCount) &&
        Objects.equals(this.assignedEntities, entityCategory.assignedEntities) &&
        Objects.equals(this.assignedEntityCount, entityCategory.assignedEntityCount) &&
        Objects.equals(this.assignedPermCount, entityCategory.assignedPermCount) &&
        Objects.equals(this.assignedTroleCount, entityCategory.assignedTroleCount) &&
        Objects.equals(this.canEditEntity, entityCategory.canEditEntity) &&
        Objects.equals(this.changedBy, entityCategory.changedBy) &&
        Objects.equals(this.createdBy, entityCategory.createdBy) &&
        Objects.equals(this.creationTime, entityCategory.creationTime) &&
        Objects.equals(this.deleted, entityCategory.deleted) &&
        Objects.equals(this.description, entityCategory.description) &&
        Objects.equals(this.id, entityCategory.id) &&
        Objects.equals(this.link, entityCategory.link) &&
        Objects.equals(this.name, entityCategory.name) &&
        Objects.equals(this.removeEntities, entityCategory.removeEntities) &&
        Objects.equals(this.type, entityCategory.type) &&
        Objects.equals(this.uniqueId, entityCategory.uniqueId) &&
        Objects.equals(this.updateTime, entityCategory.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addEntities, assignedAppCount, assignedBroleCount, assignedEntities, assignedEntityCount, assignedPermCount, assignedTroleCount, canEditEntity, changedBy, createdBy, creationTime, deleted, description, id, link, name, removeEntities, type, uniqueId, updateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntityCategory {\n");
    
    sb.append("    addEntities: ").append(toIndentedString(addEntities)).append("\n");
    sb.append("    assignedAppCount: ").append(toIndentedString(assignedAppCount)).append("\n");
    sb.append("    assignedBroleCount: ").append(toIndentedString(assignedBroleCount)).append("\n");
    sb.append("    assignedEntities: ").append(toIndentedString(assignedEntities)).append("\n");
    sb.append("    assignedEntityCount: ").append(toIndentedString(assignedEntityCount)).append("\n");
    sb.append("    assignedPermCount: ").append(toIndentedString(assignedPermCount)).append("\n");
    sb.append("    assignedTroleCount: ").append(toIndentedString(assignedTroleCount)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    removeEntities: ").append(toIndentedString(removeEntities)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
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
