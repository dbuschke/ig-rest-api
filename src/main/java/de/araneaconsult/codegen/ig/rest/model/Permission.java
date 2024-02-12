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

package de.araneaconsult.codegen.ig.rest.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import de.araneaconsult.codegen.ig.rest.model.AttributeValueNode;
import de.araneaconsult.codegen.ig.rest.model.Authorizedby;
import de.araneaconsult.codegen.ig.rest.model.EntityCategory;
import de.araneaconsult.codegen.ig.rest.model.PermOwner;
import de.araneaconsult.codegen.ig.rest.model.Permission;
import de.araneaconsult.codegen.ig.rest.model.PermissionAssignmentInfo;
import de.araneaconsult.codegen.ig.rest.model.PermissionId;
import de.araneaconsult.codegen.ig.rest.model.RequestPolicy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Permission
 */



public class Permission {
  @SerializedName("accountGrant")
  private Boolean accountGrant = null;

  @SerializedName("accountId")
  private Long accountId = null;

  @SerializedName("accountName")
  private String accountName = null;

  @SerializedName("appDisplayName")
  private String appDisplayName = null;

  @SerializedName("appId")
  private Long appId = null;

  /**
   * Gets or Sets appType
   */
  @JsonAdapter(AppTypeEnum.Adapter.class)
  public enum AppTypeEnum {
    @SerializedName("ENTERPRISE")
    ENTERPRISE("ENTERPRISE"),
    @SerializedName("IDM")
    IDM("IDM"),
    @SerializedName("IDMPROV")
    IDMPROV("IDMPROV");

    private String value;

    AppTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AppTypeEnum fromValue(String input) {
      for (AppTypeEnum b : AppTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AppTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AppTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AppTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AppTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("appType")
  private AppTypeEnum appType = null;

  @SerializedName("arraySize")
  private Long arraySize = null;

  @SerializedName("assignData")
  private String assignData = null;

  @SerializedName("assigned")
  private Boolean assigned = null;

  @SerializedName("assocBy")
  private Permission assocBy = null;

  @SerializedName("attributes")
  private List<AttributeValueNode> attributes = null;

  @SerializedName("authorizedBy")
  private List<Authorizedby> authorizedBy = null;

  @SerializedName("boundBy")
  private Permission boundBy = null;

  @SerializedName("boundable")
  private Boolean boundable = null;

  @SerializedName("bounds")
  private List<Permission> bounds = null;

  @SerializedName("causePerms")
  private List<Permission> causePerms = null;

  @SerializedName("containedPermsAuthorizable")
  private Boolean containedPermsAuthorizable = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("dynamic")
  private Boolean dynamic = null;

  @SerializedName("editable")
  private Boolean editable = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("entityCategories")
  private List<EntityCategory> entityCategories = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkApplication")
  private String linkApplication = null;

  @SerializedName("linkBoundperms")
  private String linkBoundperms = null;

  @SerializedName("linkHolders")
  private String linkHolders = null;

  @SerializedName("linkOwner")
  private String linkOwner = null;

  @SerializedName("linkPermissions")
  private String linkPermissions = null;

  @SerializedName("linkSubperms")
  private String linkSubperms = null;

  @SerializedName("mappings")
  private List<AttributeValueNode> mappings = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("owners")
  private List<PermOwner> owners = null;

  @SerializedName("permId")
  private Long permId = null;

  @SerializedName("permissionAssignment")
  private PermissionAssignmentInfo permissionAssignment = null;

  @SerializedName("permissionAssignmentInfo")
  private PermissionAssignmentInfo permissionAssignmentInfo = null;

  @SerializedName("permissionAssignmentInfoList")
  private List<PermissionAssignmentInfo> permissionAssignmentInfoList = null;

  @SerializedName("permissionId")
  private String permissionId = null;

  @SerializedName("perms")
  private List<PermissionId> perms = null;

  @SerializedName("provByPerm")
  private Permission provByPerm = null;

  @SerializedName("provPerm")
  private Permission provPerm = null;

  @SerializedName("requestApprovalPolicy")
  private RequestPolicy requestApprovalPolicy = null;

  @SerializedName("requestPolicy")
  private RequestPolicy requestPolicy = null;

  @SerializedName("risk")
  private String risk = null;

  @SerializedName("selected")
  private Boolean selected = null;

  @SerializedName("startDate")
  private Long startDate = null;

  @SerializedName("subPerms")
  private List<Permission> subPerms = null;

  @SerializedName("totalSearch")
  private Long totalSearch = null;

  @SerializedName("type")
  private String type = null;

  @SerializedName("uniquePermId")
  private String uniquePermId = null;

  @SerializedName("userCount")
  private Long userCount = null;

  @SerializedName("value")
  private String value = null;

  public Permission accountGrant(Boolean accountGrant) {
    this.accountGrant = accountGrant;
    return this;
  }

   /**
   * Get accountGrant
   * @return accountGrant
  **/
  @ApiModelProperty(value = "")
  public Boolean isAccountGrant() {
    return accountGrant;
  }

  public void setAccountGrant(Boolean accountGrant) {
    this.accountGrant = accountGrant;
  }

  public Permission accountId(Long accountId) {
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

  public Permission accountName(String accountName) {
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

  public Permission appDisplayName(String appDisplayName) {
    this.appDisplayName = appDisplayName;
    return this;
  }

   /**
   * Get appDisplayName
   * @return appDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getAppDisplayName() {
    return appDisplayName;
  }

  public void setAppDisplayName(String appDisplayName) {
    this.appDisplayName = appDisplayName;
  }

  public Permission appId(Long appId) {
    this.appId = appId;
    return this;
  }

   /**
   * Get appId
   * @return appId
  **/
  @ApiModelProperty(value = "")
  public Long getAppId() {
    return appId;
  }

  public void setAppId(Long appId) {
    this.appId = appId;
  }

  public Permission appType(AppTypeEnum appType) {
    this.appType = appType;
    return this;
  }

   /**
   * Get appType
   * @return appType
  **/
  @ApiModelProperty(value = "")
  public AppTypeEnum getAppType() {
    return appType;
  }

  public void setAppType(AppTypeEnum appType) {
    this.appType = appType;
  }

  public Permission arraySize(Long arraySize) {
    this.arraySize = arraySize;
    return this;
  }

   /**
   * Get arraySize
   * @return arraySize
  **/
  @ApiModelProperty(value = "")
  public Long getArraySize() {
    return arraySize;
  }

  public void setArraySize(Long arraySize) {
    this.arraySize = arraySize;
  }

  public Permission assignData(String assignData) {
    this.assignData = assignData;
    return this;
  }

   /**
   * Get assignData
   * @return assignData
  **/
  @ApiModelProperty(value = "")
  public String getAssignData() {
    return assignData;
  }

  public void setAssignData(String assignData) {
    this.assignData = assignData;
  }

  public Permission assigned(Boolean assigned) {
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

  public Permission assocBy(Permission assocBy) {
    this.assocBy = assocBy;
    return this;
  }

   /**
   * Get assocBy
   * @return assocBy
  **/
  @ApiModelProperty(value = "")
  public Permission getAssocBy() {
    return assocBy;
  }

  public void setAssocBy(Permission assocBy) {
    this.assocBy = assocBy;
  }

  public Permission attributes(List<AttributeValueNode> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Permission addAttributesItem(AttributeValueNode attributesItem) {
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

  public Permission authorizedBy(List<Authorizedby> authorizedBy) {
    this.authorizedBy = authorizedBy;
    return this;
  }

  public Permission addAuthorizedByItem(Authorizedby authorizedByItem) {
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

  public Permission boundBy(Permission boundBy) {
    this.boundBy = boundBy;
    return this;
  }

   /**
   * Get boundBy
   * @return boundBy
  **/
  @ApiModelProperty(value = "")
  public Permission getBoundBy() {
    return boundBy;
  }

  public void setBoundBy(Permission boundBy) {
    this.boundBy = boundBy;
  }

  public Permission boundable(Boolean boundable) {
    this.boundable = boundable;
    return this;
  }

   /**
   * Get boundable
   * @return boundable
  **/
  @ApiModelProperty(value = "")
  public Boolean isBoundable() {
    return boundable;
  }

  public void setBoundable(Boolean boundable) {
    this.boundable = boundable;
  }

  public Permission bounds(List<Permission> bounds) {
    this.bounds = bounds;
    return this;
  }

  public Permission addBoundsItem(Permission boundsItem) {
    if (this.bounds == null) {
      this.bounds = new ArrayList<Permission>();
    }
    this.bounds.add(boundsItem);
    return this;
  }

   /**
   * Get bounds
   * @return bounds
  **/
  @ApiModelProperty(value = "")
  public List<Permission> getBounds() {
    return bounds;
  }

  public void setBounds(List<Permission> bounds) {
    this.bounds = bounds;
  }

  public Permission causePerms(List<Permission> causePerms) {
    this.causePerms = causePerms;
    return this;
  }

  public Permission addCausePermsItem(Permission causePermsItem) {
    if (this.causePerms == null) {
      this.causePerms = new ArrayList<Permission>();
    }
    this.causePerms.add(causePermsItem);
    return this;
  }

   /**
   * Get causePerms
   * @return causePerms
  **/
  @ApiModelProperty(value = "")
  public List<Permission> getCausePerms() {
    return causePerms;
  }

  public void setCausePerms(List<Permission> causePerms) {
    this.causePerms = causePerms;
  }

  public Permission containedPermsAuthorizable(Boolean containedPermsAuthorizable) {
    this.containedPermsAuthorizable = containedPermsAuthorizable;
    return this;
  }

   /**
   * Get containedPermsAuthorizable
   * @return containedPermsAuthorizable
  **/
  @ApiModelProperty(value = "")
  public Boolean isContainedPermsAuthorizable() {
    return containedPermsAuthorizable;
  }

  public void setContainedPermsAuthorizable(Boolean containedPermsAuthorizable) {
    this.containedPermsAuthorizable = containedPermsAuthorizable;
  }

  public Permission deleted(Boolean deleted) {
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

  public Permission description(String description) {
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

  public Permission displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @ApiModelProperty(value = "")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Permission dynamic(Boolean dynamic) {
    this.dynamic = dynamic;
    return this;
  }

   /**
   * Get dynamic
   * @return dynamic
  **/
  @ApiModelProperty(value = "")
  public Boolean isDynamic() {
    return dynamic;
  }

  public void setDynamic(Boolean dynamic) {
    this.dynamic = dynamic;
  }

  public Permission editable(Boolean editable) {
    this.editable = editable;
    return this;
  }

   /**
   * Get editable
   * @return editable
  **/
  @ApiModelProperty(value = "")
  public Boolean isEditable() {
    return editable;
  }

  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  public Permission endDate(Long endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * Get endDate
   * @return endDate
  **/
  @ApiModelProperty(value = "")
  public Long getEndDate() {
    return endDate;
  }

  public void setEndDate(Long endDate) {
    this.endDate = endDate;
  }

  public Permission entityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
    return this;
  }

  public Permission addEntityCategoriesItem(EntityCategory entityCategoriesItem) {
    if (this.entityCategories == null) {
      this.entityCategories = new ArrayList<EntityCategory>();
    }
    this.entityCategories.add(entityCategoriesItem);
    return this;
  }

   /**
   * The categories.
   * @return entityCategories
  **/
  @ApiModelProperty(value = "The categories.")
  public List<EntityCategory> getEntityCategories() {
    return entityCategories;
  }

  public void setEntityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
  }

  public Permission link(String link) {
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

  public Permission linkApplication(String linkApplication) {
    this.linkApplication = linkApplication;
    return this;
  }

   /**
   * Get linkApplication
   * @return linkApplication
  **/
  @ApiModelProperty(value = "")
  public String getLinkApplication() {
    return linkApplication;
  }

  public void setLinkApplication(String linkApplication) {
    this.linkApplication = linkApplication;
  }

  public Permission linkBoundperms(String linkBoundperms) {
    this.linkBoundperms = linkBoundperms;
    return this;
  }

   /**
   * Get linkBoundperms
   * @return linkBoundperms
  **/
  @ApiModelProperty(value = "")
  public String getLinkBoundperms() {
    return linkBoundperms;
  }

  public void setLinkBoundperms(String linkBoundperms) {
    this.linkBoundperms = linkBoundperms;
  }

  public Permission linkHolders(String linkHolders) {
    this.linkHolders = linkHolders;
    return this;
  }

   /**
   * Get linkHolders
   * @return linkHolders
  **/
  @ApiModelProperty(value = "")
  public String getLinkHolders() {
    return linkHolders;
  }

  public void setLinkHolders(String linkHolders) {
    this.linkHolders = linkHolders;
  }

  public Permission linkOwner(String linkOwner) {
    this.linkOwner = linkOwner;
    return this;
  }

   /**
   * Get linkOwner
   * @return linkOwner
  **/
  @ApiModelProperty(value = "")
  public String getLinkOwner() {
    return linkOwner;
  }

  public void setLinkOwner(String linkOwner) {
    this.linkOwner = linkOwner;
  }

  public Permission linkPermissions(String linkPermissions) {
    this.linkPermissions = linkPermissions;
    return this;
  }

   /**
   * Get linkPermissions
   * @return linkPermissions
  **/
  @ApiModelProperty(value = "")
  public String getLinkPermissions() {
    return linkPermissions;
  }

  public void setLinkPermissions(String linkPermissions) {
    this.linkPermissions = linkPermissions;
  }

  public Permission linkSubperms(String linkSubperms) {
    this.linkSubperms = linkSubperms;
    return this;
  }

   /**
   * Get linkSubperms
   * @return linkSubperms
  **/
  @ApiModelProperty(value = "")
  public String getLinkSubperms() {
    return linkSubperms;
  }

  public void setLinkSubperms(String linkSubperms) {
    this.linkSubperms = linkSubperms;
  }

  public Permission mappings(List<AttributeValueNode> mappings) {
    this.mappings = mappings;
    return this;
  }

  public Permission addMappingsItem(AttributeValueNode mappingsItem) {
    if (this.mappings == null) {
      this.mappings = new ArrayList<AttributeValueNode>();
    }
    this.mappings.add(mappingsItem);
    return this;
  }

   /**
   * Get mappings
   * @return mappings
  **/
  @ApiModelProperty(value = "")
  public List<AttributeValueNode> getMappings() {
    return mappings;
  }

  public void setMappings(List<AttributeValueNode> mappings) {
    this.mappings = mappings;
  }

  public Permission name(String name) {
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

  public Permission owners(List<PermOwner> owners) {
    this.owners = owners;
    return this;
  }

  public Permission addOwnersItem(PermOwner ownersItem) {
    if (this.owners == null) {
      this.owners = new ArrayList<PermOwner>();
    }
    this.owners.add(ownersItem);
    return this;
  }

   /**
   * Get owners
   * @return owners
  **/
  @ApiModelProperty(value = "")
  public List<PermOwner> getOwners() {
    return owners;
  }

  public void setOwners(List<PermOwner> owners) {
    this.owners = owners;
  }

  public Permission permId(Long permId) {
    this.permId = permId;
    return this;
  }

   /**
   * Get permId
   * @return permId
  **/
  @ApiModelProperty(value = "")
  public Long getPermId() {
    return permId;
  }

  public void setPermId(Long permId) {
    this.permId = permId;
  }

  public Permission permissionAssignment(PermissionAssignmentInfo permissionAssignment) {
    this.permissionAssignment = permissionAssignment;
    return this;
  }

   /**
   * Get permissionAssignment
   * @return permissionAssignment
  **/
  @ApiModelProperty(value = "")
  public PermissionAssignmentInfo getPermissionAssignment() {
    return permissionAssignment;
  }

  public void setPermissionAssignment(PermissionAssignmentInfo permissionAssignment) {
    this.permissionAssignment = permissionAssignment;
  }

  public Permission permissionAssignmentInfo(PermissionAssignmentInfo permissionAssignmentInfo) {
    this.permissionAssignmentInfo = permissionAssignmentInfo;
    return this;
  }

   /**
   * Get permissionAssignmentInfo
   * @return permissionAssignmentInfo
  **/
  @ApiModelProperty(value = "")
  public PermissionAssignmentInfo getPermissionAssignmentInfo() {
    return permissionAssignmentInfo;
  }

  public void setPermissionAssignmentInfo(PermissionAssignmentInfo permissionAssignmentInfo) {
    this.permissionAssignmentInfo = permissionAssignmentInfo;
  }

  public Permission permissionAssignmentInfoList(List<PermissionAssignmentInfo> permissionAssignmentInfoList) {
    this.permissionAssignmentInfoList = permissionAssignmentInfoList;
    return this;
  }

  public Permission addPermissionAssignmentInfoListItem(PermissionAssignmentInfo permissionAssignmentInfoListItem) {
    if (this.permissionAssignmentInfoList == null) {
      this.permissionAssignmentInfoList = new ArrayList<PermissionAssignmentInfo>();
    }
    this.permissionAssignmentInfoList.add(permissionAssignmentInfoListItem);
    return this;
  }

   /**
   * Get permissionAssignmentInfoList
   * @return permissionAssignmentInfoList
  **/
  @ApiModelProperty(value = "")
  public List<PermissionAssignmentInfo> getPermissionAssignmentInfoList() {
    return permissionAssignmentInfoList;
  }

  public void setPermissionAssignmentInfoList(List<PermissionAssignmentInfo> permissionAssignmentInfoList) {
    this.permissionAssignmentInfoList = permissionAssignmentInfoList;
  }

  public Permission permissionId(String permissionId) {
    this.permissionId = permissionId;
    return this;
  }

   /**
   * Get permissionId
   * @return permissionId
  **/
  @ApiModelProperty(value = "")
  public String getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }

  public Permission perms(List<PermissionId> perms) {
    this.perms = perms;
    return this;
  }

  public Permission addPermsItem(PermissionId permsItem) {
    if (this.perms == null) {
      this.perms = new ArrayList<PermissionId>();
    }
    this.perms.add(permsItem);
    return this;
  }

   /**
   * Get perms
   * @return perms
  **/
  @ApiModelProperty(value = "")
  public List<PermissionId> getPerms() {
    return perms;
  }

  public void setPerms(List<PermissionId> perms) {
    this.perms = perms;
  }

  public Permission provByPerm(Permission provByPerm) {
    this.provByPerm = provByPerm;
    return this;
  }

   /**
   * Get provByPerm
   * @return provByPerm
  **/
  @ApiModelProperty(value = "")
  public Permission getProvByPerm() {
    return provByPerm;
  }

  public void setProvByPerm(Permission provByPerm) {
    this.provByPerm = provByPerm;
  }

  public Permission provPerm(Permission provPerm) {
    this.provPerm = provPerm;
    return this;
  }

   /**
   * Get provPerm
   * @return provPerm
  **/
  @ApiModelProperty(value = "")
  public Permission getProvPerm() {
    return provPerm;
  }

  public void setProvPerm(Permission provPerm) {
    this.provPerm = provPerm;
  }

  public Permission requestApprovalPolicy(RequestPolicy requestApprovalPolicy) {
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

  public Permission requestPolicy(RequestPolicy requestPolicy) {
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

  public Permission risk(String risk) {
    this.risk = risk;
    return this;
  }

   /**
   * Get risk
   * @return risk
  **/
  @ApiModelProperty(value = "")
  public String getRisk() {
    return risk;
  }

  public void setRisk(String risk) {
    this.risk = risk;
  }

  public Permission selected(Boolean selected) {
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

  public Permission startDate(Long startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * Get startDate
   * @return startDate
  **/
  @ApiModelProperty(value = "")
  public Long getStartDate() {
    return startDate;
  }

  public void setStartDate(Long startDate) {
    this.startDate = startDate;
  }

  public Permission subPerms(List<Permission> subPerms) {
    this.subPerms = subPerms;
    return this;
  }

  public Permission addSubPermsItem(Permission subPermsItem) {
    if (this.subPerms == null) {
      this.subPerms = new ArrayList<Permission>();
    }
    this.subPerms.add(subPermsItem);
    return this;
  }

   /**
   * Get subPerms
   * @return subPerms
  **/
  @ApiModelProperty(value = "")
  public List<Permission> getSubPerms() {
    return subPerms;
  }

  public void setSubPerms(List<Permission> subPerms) {
    this.subPerms = subPerms;
  }

  public Permission totalSearch(Long totalSearch) {
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

  public Permission type(String type) {
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

  public Permission uniquePermId(String uniquePermId) {
    this.uniquePermId = uniquePermId;
    return this;
  }

   /**
   * Get uniquePermId
   * @return uniquePermId
  **/
  @ApiModelProperty(value = "")
  public String getUniquePermId() {
    return uniquePermId;
  }

  public void setUniquePermId(String uniquePermId) {
    this.uniquePermId = uniquePermId;
  }

  public Permission userCount(Long userCount) {
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

  public Permission value(String value) {
    this.value = value;
    return this;
  }

   /**
   * Get value
   * @return value
  **/
  @ApiModelProperty(value = "")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Permission permission = (Permission) o;
    return Objects.equals(this.accountGrant, permission.accountGrant) &&
        Objects.equals(this.accountId, permission.accountId) &&
        Objects.equals(this.accountName, permission.accountName) &&
        Objects.equals(this.appDisplayName, permission.appDisplayName) &&
        Objects.equals(this.appId, permission.appId) &&
        Objects.equals(this.appType, permission.appType) &&
        Objects.equals(this.arraySize, permission.arraySize) &&
        Objects.equals(this.assignData, permission.assignData) &&
        Objects.equals(this.assigned, permission.assigned) &&
        Objects.equals(this.assocBy, permission.assocBy) &&
        Objects.equals(this.attributes, permission.attributes) &&
        Objects.equals(this.authorizedBy, permission.authorizedBy) &&
        Objects.equals(this.boundBy, permission.boundBy) &&
        Objects.equals(this.boundable, permission.boundable) &&
        Objects.equals(this.bounds, permission.bounds) &&
        Objects.equals(this.causePerms, permission.causePerms) &&
        Objects.equals(this.containedPermsAuthorizable, permission.containedPermsAuthorizable) &&
        Objects.equals(this.deleted, permission.deleted) &&
        Objects.equals(this.description, permission.description) &&
        Objects.equals(this.displayName, permission.displayName) &&
        Objects.equals(this.dynamic, permission.dynamic) &&
        Objects.equals(this.editable, permission.editable) &&
        Objects.equals(this.endDate, permission.endDate) &&
        Objects.equals(this.entityCategories, permission.entityCategories) &&
        Objects.equals(this.link, permission.link) &&
        Objects.equals(this.linkApplication, permission.linkApplication) &&
        Objects.equals(this.linkBoundperms, permission.linkBoundperms) &&
        Objects.equals(this.linkHolders, permission.linkHolders) &&
        Objects.equals(this.linkOwner, permission.linkOwner) &&
        Objects.equals(this.linkPermissions, permission.linkPermissions) &&
        Objects.equals(this.linkSubperms, permission.linkSubperms) &&
        Objects.equals(this.mappings, permission.mappings) &&
        Objects.equals(this.name, permission.name) &&
        Objects.equals(this.owners, permission.owners) &&
        Objects.equals(this.permId, permission.permId) &&
        Objects.equals(this.permissionAssignment, permission.permissionAssignment) &&
        Objects.equals(this.permissionAssignmentInfo, permission.permissionAssignmentInfo) &&
        Objects.equals(this.permissionAssignmentInfoList, permission.permissionAssignmentInfoList) &&
        Objects.equals(this.permissionId, permission.permissionId) &&
        Objects.equals(this.perms, permission.perms) &&
        Objects.equals(this.provByPerm, permission.provByPerm) &&
        Objects.equals(this.provPerm, permission.provPerm) &&
        Objects.equals(this.requestApprovalPolicy, permission.requestApprovalPolicy) &&
        Objects.equals(this.requestPolicy, permission.requestPolicy) &&
        Objects.equals(this.risk, permission.risk) &&
        Objects.equals(this.selected, permission.selected) &&
        Objects.equals(this.startDate, permission.startDate) &&
        Objects.equals(this.subPerms, permission.subPerms) &&
        Objects.equals(this.totalSearch, permission.totalSearch) &&
        Objects.equals(this.type, permission.type) &&
        Objects.equals(this.uniquePermId, permission.uniquePermId) &&
        Objects.equals(this.userCount, permission.userCount) &&
        Objects.equals(this.value, permission.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountGrant, accountId, accountName, appDisplayName, appId, appType, arraySize, assignData, assigned, assocBy, attributes, authorizedBy, boundBy, boundable, bounds, causePerms, containedPermsAuthorizable, deleted, description, displayName, dynamic, editable, endDate, entityCategories, link, linkApplication, linkBoundperms, linkHolders, linkOwner, linkPermissions, linkSubperms, mappings, name, owners, permId, permissionAssignment, permissionAssignmentInfo, permissionAssignmentInfoList, permissionId, perms, provByPerm, provPerm, requestApprovalPolicy, requestPolicy, risk, selected, startDate, subPerms, totalSearch, type, uniquePermId, userCount, value);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Permission {\n");
    
    sb.append("    accountGrant: ").append(toIndentedString(accountGrant)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    appDisplayName: ").append(toIndentedString(appDisplayName)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    appType: ").append(toIndentedString(appType)).append("\n");
    sb.append("    arraySize: ").append(toIndentedString(arraySize)).append("\n");
    sb.append("    assignData: ").append(toIndentedString(assignData)).append("\n");
    sb.append("    assigned: ").append(toIndentedString(assigned)).append("\n");
    sb.append("    assocBy: ").append(toIndentedString(assocBy)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    authorizedBy: ").append(toIndentedString(authorizedBy)).append("\n");
    sb.append("    boundBy: ").append(toIndentedString(boundBy)).append("\n");
    sb.append("    boundable: ").append(toIndentedString(boundable)).append("\n");
    sb.append("    bounds: ").append(toIndentedString(bounds)).append("\n");
    sb.append("    causePerms: ").append(toIndentedString(causePerms)).append("\n");
    sb.append("    containedPermsAuthorizable: ").append(toIndentedString(containedPermsAuthorizable)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    dynamic: ").append(toIndentedString(dynamic)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    entityCategories: ").append(toIndentedString(entityCategories)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkApplication: ").append(toIndentedString(linkApplication)).append("\n");
    sb.append("    linkBoundperms: ").append(toIndentedString(linkBoundperms)).append("\n");
    sb.append("    linkHolders: ").append(toIndentedString(linkHolders)).append("\n");
    sb.append("    linkOwner: ").append(toIndentedString(linkOwner)).append("\n");
    sb.append("    linkPermissions: ").append(toIndentedString(linkPermissions)).append("\n");
    sb.append("    linkSubperms: ").append(toIndentedString(linkSubperms)).append("\n");
    sb.append("    mappings: ").append(toIndentedString(mappings)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    permId: ").append(toIndentedString(permId)).append("\n");
    sb.append("    permissionAssignment: ").append(toIndentedString(permissionAssignment)).append("\n");
    sb.append("    permissionAssignmentInfo: ").append(toIndentedString(permissionAssignmentInfo)).append("\n");
    sb.append("    permissionAssignmentInfoList: ").append(toIndentedString(permissionAssignmentInfoList)).append("\n");
    sb.append("    permissionId: ").append(toIndentedString(permissionId)).append("\n");
    sb.append("    perms: ").append(toIndentedString(perms)).append("\n");
    sb.append("    provByPerm: ").append(toIndentedString(provByPerm)).append("\n");
    sb.append("    provPerm: ").append(toIndentedString(provPerm)).append("\n");
    sb.append("    requestApprovalPolicy: ").append(toIndentedString(requestApprovalPolicy)).append("\n");
    sb.append("    requestPolicy: ").append(toIndentedString(requestPolicy)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    selected: ").append(toIndentedString(selected)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    subPerms: ").append(toIndentedString(subPerms)).append("\n");
    sb.append("    totalSearch: ").append(toIndentedString(totalSearch)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    uniquePermId: ").append(toIndentedString(uniquePermId)).append("\n");
    sb.append("    userCount: ").append(toIndentedString(userCount)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
