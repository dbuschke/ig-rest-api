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
import io.swagger.client.model.AttributeValueNode;
import io.swagger.client.model.Authorizedby;
import io.swagger.client.model.EntityCategory;
import io.swagger.client.model.Owner;
import io.swagger.client.model.Permission;
import io.swagger.client.model.RequestPolicy;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Role
 */



public class Role {
  @SerializedName("assigned")
  private Boolean assigned = null;

  @SerializedName("assignedCount")
  private Long assignedCount = null;

  @SerializedName("attributes")
  private List<AttributeValueNode> attributes = null;

  @SerializedName("authorizationRole")
  private Boolean authorizationRole = null;

  @SerializedName("authorizedBy")
  private List<Authorizedby> authorizedBy = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("canManageEntity")
  private Boolean canManageEntity = null;

  @SerializedName("changedBy")
  private User changedBy = null;

  @SerializedName("cost")
  private Long cost = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("detected")
  private Boolean detected = null;

  /**
   * Gets or Sets detectionStatus
   */
  @JsonAdapter(DetectionStatusEnum.Adapter.class)
  public enum DetectionStatusEnum {
    @SerializedName("RUNNING")
    RUNNING("RUNNING"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("INVALID")
    INVALID("INVALID"),
    @SerializedName("FAILED")
    FAILED("FAILED");

    private String value;

    DetectionStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DetectionStatusEnum fromValue(String input) {
      for (DetectionStatusEnum b : DetectionStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DetectionStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DetectionStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DetectionStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DetectionStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("detectionStatus")
  private DetectionStatusEnum detectionStatus = null;

  @SerializedName("entityCategories")
  private List<EntityCategory> entityCategories = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("lastDetectionTime")
  private Long lastDetectionTime = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("owners")
  private List<Owner> owners = null;

  @SerializedName("permissions")
  private List<Permission> permissions = null;

  @SerializedName("referencedCount")
  private Long referencedCount = null;

  @SerializedName("requestApprovalPolicy")
  private RequestPolicy requestApprovalPolicy = null;

  @SerializedName("requestPolicy")
  private RequestPolicy requestPolicy = null;

  @SerializedName("risk")
  private Long risk = null;

  /**
   * Gets or Sets riskMode
   */
  @JsonAdapter(RiskModeEnum.Adapter.class)
  public enum RiskModeEnum {
    @SerializedName("MAN")
    MAN("MAN"),
    @SerializedName("MAX")
    MAX("MAX"),
    @SerializedName("AVE")
    AVE("AVE");

    private String value;

    RiskModeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RiskModeEnum fromValue(String input) {
      for (RiskModeEnum b : RiskModeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RiskModeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RiskModeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RiskModeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RiskModeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("riskMode")
  private RiskModeEnum riskMode = null;

  @SerializedName("roleLevelApproval")
  private Boolean roleLevelApproval = null;

  @SerializedName("selected")
  private Boolean selected = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("INVALID")
    INVALID("INVALID"),
    @SerializedName("MINED")
    MINED("MINED");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StateEnum fromValue(String input) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("state")
  private StateEnum state = null;

  @SerializedName("uniquePolicyId")
  private String uniquePolicyId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  @SerializedName("userCount")
  private Long userCount = null;

  public Role assigned(Boolean assigned) {
    this.assigned = assigned;
    return this;
  }

   /**
   * Get assigned
   * @return assigned
  **/
  @ApiModelProperty(value = "")
  public Boolean isAssigned() {
    return assigned;
  }

  public void setAssigned(Boolean assigned) {
    this.assigned = assigned;
  }

  public Role assignedCount(Long assignedCount) {
    this.assignedCount = assignedCount;
    return this;
  }

   /**
   * Get assignedCount
   * @return assignedCount
  **/
  @ApiModelProperty(value = "")
  public Long getAssignedCount() {
    return assignedCount;
  }

  public void setAssignedCount(Long assignedCount) {
    this.assignedCount = assignedCount;
  }

  public Role attributes(List<AttributeValueNode> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Role addAttributesItem(AttributeValueNode attributesItem) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<AttributeValueNode>();
    }
    this.attributes.add(attributesItem);
    return this;
  }

   /**
   * Get attributes
   * @return attributes
  **/
  @ApiModelProperty(value = "")
  public List<AttributeValueNode> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<AttributeValueNode> attributes) {
    this.attributes = attributes;
  }

  public Role authorizationRole(Boolean authorizationRole) {
    this.authorizationRole = authorizationRole;
    return this;
  }

   /**
   * Get authorizationRole
   * @return authorizationRole
  **/
  @ApiModelProperty(value = "")
  public Boolean isAuthorizationRole() {
    return authorizationRole;
  }

  public void setAuthorizationRole(Boolean authorizationRole) {
    this.authorizationRole = authorizationRole;
  }

  public Role authorizedBy(List<Authorizedby> authorizedBy) {
    this.authorizedBy = authorizedBy;
    return this;
  }

  public Role addAuthorizedByItem(Authorizedby authorizedByItem) {
    if (this.authorizedBy == null) {
      this.authorizedBy = new ArrayList<Authorizedby>();
    }
    this.authorizedBy.add(authorizedByItem);
    return this;
  }

   /**
   * Get authorizedBy
   * @return authorizedBy
  **/
  @ApiModelProperty(value = "")
  public List<Authorizedby> getAuthorizedBy() {
    return authorizedBy;
  }

  public void setAuthorizedBy(List<Authorizedby> authorizedBy) {
    this.authorizedBy = authorizedBy;
  }

  public Role canEditEntity(Boolean canEditEntity) {
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

  public Role canManageEntity(Boolean canManageEntity) {
    this.canManageEntity = canManageEntity;
    return this;
  }

   /**
   * Get canManageEntity
   * @return canManageEntity
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanManageEntity() {
    return canManageEntity;
  }

  public void setCanManageEntity(Boolean canManageEntity) {
    this.canManageEntity = canManageEntity;
  }

  public Role changedBy(User changedBy) {
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

  public Role cost(Long cost) {
    this.cost = cost;
    return this;
  }

   /**
   * Get cost
   * @return cost
  **/
  @ApiModelProperty(value = "")
  public Long getCost() {
    return cost;
  }

  public void setCost(Long cost) {
    this.cost = cost;
  }

  public Role createdBy(User createdBy) {
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

  public Role creationTime(Long creationTime) {
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

  public Role deleted(Boolean deleted) {
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

  public Role description(String description) {
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

  public Role detected(Boolean detected) {
    this.detected = detected;
    return this;
  }

   /**
   * Get detected
   * @return detected
  **/
  @ApiModelProperty(value = "")
  public Boolean isDetected() {
    return detected;
  }

  public void setDetected(Boolean detected) {
    this.detected = detected;
  }

  public Role detectionStatus(DetectionStatusEnum detectionStatus) {
    this.detectionStatus = detectionStatus;
    return this;
  }

   /**
   * Get detectionStatus
   * @return detectionStatus
  **/
  @ApiModelProperty(value = "")
  public DetectionStatusEnum getDetectionStatus() {
    return detectionStatus;
  }

  public void setDetectionStatus(DetectionStatusEnum detectionStatus) {
    this.detectionStatus = detectionStatus;
  }

  public Role entityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
    return this;
  }

  public Role addEntityCategoriesItem(EntityCategory entityCategoriesItem) {
    if (this.entityCategories == null) {
      this.entityCategories = new ArrayList<EntityCategory>();
    }
    this.entityCategories.add(entityCategoriesItem);
    return this;
  }

   /**
   * Get entityCategories
   * @return entityCategories
  **/
  @ApiModelProperty(value = "")
  public List<EntityCategory> getEntityCategories() {
    return entityCategories;
  }

  public void setEntityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
  }

  public Role id(Long id) {
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

  public Role lastDetectionTime(Long lastDetectionTime) {
    this.lastDetectionTime = lastDetectionTime;
    return this;
  }

   /**
   * Get lastDetectionTime
   * @return lastDetectionTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastDetectionTime() {
    return lastDetectionTime;
  }

  public void setLastDetectionTime(Long lastDetectionTime) {
    this.lastDetectionTime = lastDetectionTime;
  }

  public Role link(String link) {
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

  public Role name(String name) {
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

  public Role owners(List<Owner> owners) {
    this.owners = owners;
    return this;
  }

  public Role addOwnersItem(Owner ownersItem) {
    if (this.owners == null) {
      this.owners = new ArrayList<Owner>();
    }
    this.owners.add(ownersItem);
    return this;
  }

   /**
   * Get owners
   * @return owners
  **/
  @ApiModelProperty(value = "")
  public List<Owner> getOwners() {
    return owners;
  }

  public void setOwners(List<Owner> owners) {
    this.owners = owners;
  }

  public Role permissions(List<Permission> permissions) {
    this.permissions = permissions;
    return this;
  }

  public Role addPermissionsItem(Permission permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<Permission>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

   /**
   * Get permissions
   * @return permissions
  **/
  @ApiModelProperty(value = "")
  public List<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<Permission> permissions) {
    this.permissions = permissions;
  }

  public Role referencedCount(Long referencedCount) {
    this.referencedCount = referencedCount;
    return this;
  }

   /**
   * Get referencedCount
   * @return referencedCount
  **/
  @ApiModelProperty(value = "")
  public Long getReferencedCount() {
    return referencedCount;
  }

  public void setReferencedCount(Long referencedCount) {
    this.referencedCount = referencedCount;
  }

  public Role requestApprovalPolicy(RequestPolicy requestApprovalPolicy) {
    this.requestApprovalPolicy = requestApprovalPolicy;
    return this;
  }

   /**
   * Get requestApprovalPolicy
   * @return requestApprovalPolicy
  **/
  @ApiModelProperty(value = "")
  public RequestPolicy getRequestApprovalPolicy() {
    return requestApprovalPolicy;
  }

  public void setRequestApprovalPolicy(RequestPolicy requestApprovalPolicy) {
    this.requestApprovalPolicy = requestApprovalPolicy;
  }

  public Role requestPolicy(RequestPolicy requestPolicy) {
    this.requestPolicy = requestPolicy;
    return this;
  }

   /**
   * Get requestPolicy
   * @return requestPolicy
  **/
  @ApiModelProperty(value = "")
  public RequestPolicy getRequestPolicy() {
    return requestPolicy;
  }

  public void setRequestPolicy(RequestPolicy requestPolicy) {
    this.requestPolicy = requestPolicy;
  }

  public Role risk(Long risk) {
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

  public Role riskMode(RiskModeEnum riskMode) {
    this.riskMode = riskMode;
    return this;
  }

   /**
   * Get riskMode
   * @return riskMode
  **/
  @ApiModelProperty(value = "")
  public RiskModeEnum getRiskMode() {
    return riskMode;
  }

  public void setRiskMode(RiskModeEnum riskMode) {
    this.riskMode = riskMode;
  }

  public Role roleLevelApproval(Boolean roleLevelApproval) {
    this.roleLevelApproval = roleLevelApproval;
    return this;
  }

   /**
   * Get roleLevelApproval
   * @return roleLevelApproval
  **/
  @ApiModelProperty(value = "")
  public Boolean isRoleLevelApproval() {
    return roleLevelApproval;
  }

  public void setRoleLevelApproval(Boolean roleLevelApproval) {
    this.roleLevelApproval = roleLevelApproval;
  }

  public Role selected(Boolean selected) {
    this.selected = selected;
    return this;
  }

   /**
   * Get selected
   * @return selected
  **/
  @ApiModelProperty(value = "")
  public Boolean isSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  public Role state(StateEnum state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public Role uniquePolicyId(String uniquePolicyId) {
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

  public Role updateTime(Long updateTime) {
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

  public Role userCount(Long userCount) {
    this.userCount = userCount;
    return this;
  }

   /**
   * Get userCount
   * @return userCount
  **/
  @ApiModelProperty(value = "")
  public Long getUserCount() {
    return userCount;
  }

  public void setUserCount(Long userCount) {
    this.userCount = userCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Role role = (Role) o;
    return Objects.equals(this.assigned, role.assigned) &&
        Objects.equals(this.assignedCount, role.assignedCount) &&
        Objects.equals(this.attributes, role.attributes) &&
        Objects.equals(this.authorizationRole, role.authorizationRole) &&
        Objects.equals(this.authorizedBy, role.authorizedBy) &&
        Objects.equals(this.canEditEntity, role.canEditEntity) &&
        Objects.equals(this.canManageEntity, role.canManageEntity) &&
        Objects.equals(this.changedBy, role.changedBy) &&
        Objects.equals(this.cost, role.cost) &&
        Objects.equals(this.createdBy, role.createdBy) &&
        Objects.equals(this.creationTime, role.creationTime) &&
        Objects.equals(this.deleted, role.deleted) &&
        Objects.equals(this.description, role.description) &&
        Objects.equals(this.detected, role.detected) &&
        Objects.equals(this.detectionStatus, role.detectionStatus) &&
        Objects.equals(this.entityCategories, role.entityCategories) &&
        Objects.equals(this.id, role.id) &&
        Objects.equals(this.lastDetectionTime, role.lastDetectionTime) &&
        Objects.equals(this.link, role.link) &&
        Objects.equals(this.name, role.name) &&
        Objects.equals(this.owners, role.owners) &&
        Objects.equals(this.permissions, role.permissions) &&
        Objects.equals(this.referencedCount, role.referencedCount) &&
        Objects.equals(this.requestApprovalPolicy, role.requestApprovalPolicy) &&
        Objects.equals(this.requestPolicy, role.requestPolicy) &&
        Objects.equals(this.risk, role.risk) &&
        Objects.equals(this.riskMode, role.riskMode) &&
        Objects.equals(this.roleLevelApproval, role.roleLevelApproval) &&
        Objects.equals(this.selected, role.selected) &&
        Objects.equals(this.state, role.state) &&
        Objects.equals(this.uniquePolicyId, role.uniquePolicyId) &&
        Objects.equals(this.updateTime, role.updateTime) &&
        Objects.equals(this.userCount, role.userCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assigned, assignedCount, attributes, authorizationRole, authorizedBy, canEditEntity, canManageEntity, changedBy, cost, createdBy, creationTime, deleted, description, detected, detectionStatus, entityCategories, id, lastDetectionTime, link, name, owners, permissions, referencedCount, requestApprovalPolicy, requestPolicy, risk, riskMode, roleLevelApproval, selected, state, uniquePolicyId, updateTime, userCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Role {\n");
    
    sb.append("    assigned: ").append(toIndentedString(assigned)).append("\n");
    sb.append("    assignedCount: ").append(toIndentedString(assignedCount)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    authorizationRole: ").append(toIndentedString(authorizationRole)).append("\n");
    sb.append("    authorizedBy: ").append(toIndentedString(authorizedBy)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    canManageEntity: ").append(toIndentedString(canManageEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    detected: ").append(toIndentedString(detected)).append("\n");
    sb.append("    detectionStatus: ").append(toIndentedString(detectionStatus)).append("\n");
    sb.append("    entityCategories: ").append(toIndentedString(entityCategories)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lastDetectionTime: ").append(toIndentedString(lastDetectionTime)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    referencedCount: ").append(toIndentedString(referencedCount)).append("\n");
    sb.append("    requestApprovalPolicy: ").append(toIndentedString(requestApprovalPolicy)).append("\n");
    sb.append("    requestPolicy: ").append(toIndentedString(requestPolicy)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    riskMode: ").append(toIndentedString(riskMode)).append("\n");
    sb.append("    roleLevelApproval: ").append(toIndentedString(roleLevelApproval)).append("\n");
    sb.append("    selected: ").append(toIndentedString(selected)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    uniquePolicyId: ").append(toIndentedString(uniquePolicyId)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    userCount: ").append(toIndentedString(userCount)).append("\n");
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
