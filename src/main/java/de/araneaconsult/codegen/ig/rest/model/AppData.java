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
import de.araneaconsult.codegen.ig.rest.model.AppOwner;
import de.araneaconsult.codegen.ig.rest.model.AttributeValueNode;
import de.araneaconsult.codegen.ig.rest.model.Authorizedby;
import de.araneaconsult.codegen.ig.rest.model.EntityCategory;
import de.araneaconsult.codegen.ig.rest.model.FulfillmentInfo;
import de.araneaconsult.codegen.ig.rest.model.PermissionSyncInfo;
import de.araneaconsult.codegen.ig.rest.model.Permissions;
import de.araneaconsult.codegen.ig.rest.model.RequestPolicy;
import de.araneaconsult.codegen.ig.rest.model.Tag;
import de.araneaconsult.codegen.ig.rest.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AppData
 */



public class AppData {
  @SerializedName("tags")
  private List<Tag> tags = null;

  @SerializedName("administrators")
  private List<User> administrators = null;

  @SerializedName("affiliatedAppLink")
  private String affiliatedAppLink = null;

  @SerializedName("affiliatedAppName")
  private String affiliatedAppName = null;

  @SerializedName("appId")
  private Long appId = null;

  @SerializedName("appParentName")
  private String appParentName = null;

  @SerializedName("appType")
  private String appType = null;

  @SerializedName("attributes")
  private List<AttributeValueNode> attributes = null;

  @SerializedName("authorizedBy")
  private List<Authorizedby> authorizedBy = null;

  @SerializedName("canViewAnyApp")
  private Boolean canViewAnyApp = null;

  @SerializedName("canViewApp")
  private Boolean canViewApp = null;

  @SerializedName("collectorCount")
  private Long collectorCount = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("editable")
  private Boolean editable = null;

  @SerializedName("entitlements")
  private Permissions entitlements = null;

  @SerializedName("entityCategories")
  private List<EntityCategory> entityCategories = null;

  @SerializedName("fulfillment")
  private String fulfillment = null;

  @SerializedName("fulfillmentInfo")
  private FulfillmentInfo fulfillmentInfo = null;

  @SerializedName("fulfillmentInfos")
  private List<FulfillmentInfo> fulfillmentInfos = null;

  @SerializedName("fulfillmentOptions")
  private String fulfillmentOptions = null;

  @SerializedName("hasAccountCollector")
  private Boolean hasAccountCollector = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkAccounts")
  private String linkAccounts = null;

  @SerializedName("linkOwner")
  private String linkOwner = null;

  @SerializedName("linkParent")
  private String linkParent = null;

  @SerializedName("linkPermissions")
  private String linkPermissions = null;

  @SerializedName("multiAppCollector")
  private String multiAppCollector = null;

  @SerializedName("multiAppFlag")
  private Boolean multiAppFlag = null;

  @SerializedName("multiAppTargetAllowable")
  private Boolean multiAppTargetAllowable = null;

  @SerializedName("ownerId")
  private Long ownerId = null;

  @SerializedName("ownerName")
  private String ownerName = null;

  @SerializedName("ownerPermissionId")
  private Long ownerPermissionId = null;

  @SerializedName("ownerPermissionLink")
  private String ownerPermissionLink = null;

  @SerializedName("ownerPermissionName")
  private String ownerPermissionName = null;

  @SerializedName("owners")
  private List<AppOwner> owners = null;

  @SerializedName("permCount")
  private Long permCount = null;

  @SerializedName("permissionSyncInfo")
  private PermissionSyncInfo permissionSyncInfo = null;

  @SerializedName("qid")
  private String qid = null;

  @SerializedName("requestApprovalPolicy")
  private RequestPolicy requestApprovalPolicy = null;

  @SerializedName("requestPolicy")
  private RequestPolicy requestPolicy = null;

  @SerializedName("selected")
  private Boolean selected = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("updatedBy")
  private User updatedBy = null;

  @SerializedName("userCount")
  private Long userCount = null;

  public AppData tags(List<Tag> tags) {
    this.tags = tags;
    return this;
  }

  public AppData addTagsItem(Tag tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<Tag>();
    }
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * Get tags
   * @return tags
  **/
  @ApiModelProperty(value = "")
  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public AppData administrators(List<User> administrators) {
    this.administrators = administrators;
    return this;
  }

  public AppData addAdministratorsItem(User administratorsItem) {
    if (this.administrators == null) {
      this.administrators = new ArrayList<User>();
    }
    this.administrators.add(administratorsItem);
    return this;
  }

   /**
   * Get administrators
   * @return administrators
  **/
  @ApiModelProperty(value = "")
  public List<User> getAdministrators() {
    return administrators;
  }

  public void setAdministrators(List<User> administrators) {
    this.administrators = administrators;
  }

  public AppData affiliatedAppLink(String affiliatedAppLink) {
    this.affiliatedAppLink = affiliatedAppLink;
    return this;
  }

   /**
   * Get affiliatedAppLink
   * @return affiliatedAppLink
  **/
  @ApiModelProperty(value = "")
  public String getAffiliatedAppLink() {
    return affiliatedAppLink;
  }

  public void setAffiliatedAppLink(String affiliatedAppLink) {
    this.affiliatedAppLink = affiliatedAppLink;
  }

  public AppData affiliatedAppName(String affiliatedAppName) {
    this.affiliatedAppName = affiliatedAppName;
    return this;
  }

   /**
   * Get affiliatedAppName
   * @return affiliatedAppName
  **/
  @ApiModelProperty(value = "")
  public String getAffiliatedAppName() {
    return affiliatedAppName;
  }

  public void setAffiliatedAppName(String affiliatedAppName) {
    this.affiliatedAppName = affiliatedAppName;
  }

  public AppData appId(Long appId) {
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

  public AppData appParentName(String appParentName) {
    this.appParentName = appParentName;
    return this;
  }

   /**
   * Get appParentName
   * @return appParentName
  **/
  @ApiModelProperty(value = "")
  public String getAppParentName() {
    return appParentName;
  }

  public void setAppParentName(String appParentName) {
    this.appParentName = appParentName;
  }

  public AppData appType(String appType) {
    this.appType = appType;
    return this;
  }

   /**
   * Get appType
   * @return appType
  **/
  @ApiModelProperty(value = "")
  public String getAppType() {
    return appType;
  }

  public void setAppType(String appType) {
    this.appType = appType;
  }

  public AppData attributes(List<AttributeValueNode> attributes) {
    this.attributes = attributes;
    return this;
  }

  public AppData addAttributesItem(AttributeValueNode attributesItem) {
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

  public AppData authorizedBy(List<Authorizedby> authorizedBy) {
    this.authorizedBy = authorizedBy;
    return this;
  }

  public AppData addAuthorizedByItem(Authorizedby authorizedByItem) {
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

  public AppData canViewAnyApp(Boolean canViewAnyApp) {
    this.canViewAnyApp = canViewAnyApp;
    return this;
  }

   /**
   * Get canViewAnyApp
   * @return canViewAnyApp
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanViewAnyApp() {
    return canViewAnyApp;
  }

  public void setCanViewAnyApp(Boolean canViewAnyApp) {
    this.canViewAnyApp = canViewAnyApp;
  }

  public AppData canViewApp(Boolean canViewApp) {
    this.canViewApp = canViewApp;
    return this;
  }

   /**
   * Get canViewApp
   * @return canViewApp
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanViewApp() {
    return canViewApp;
  }

  public void setCanViewApp(Boolean canViewApp) {
    this.canViewApp = canViewApp;
  }

  public AppData collectorCount(Long collectorCount) {
    this.collectorCount = collectorCount;
    return this;
  }

   /**
   * Get collectorCount
   * @return collectorCount
  **/
  @ApiModelProperty(value = "")
  public Long getCollectorCount() {
    return collectorCount;
  }

  public void setCollectorCount(Long collectorCount) {
    this.collectorCount = collectorCount;
  }

  public AppData createdBy(User createdBy) {
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

  public AppData deleted(Boolean deleted) {
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

  public AppData editable(Boolean editable) {
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

  public AppData entitlements(Permissions entitlements) {
    this.entitlements = entitlements;
    return this;
  }

   /**
   * Get entitlements
   * @return entitlements
  **/
  @ApiModelProperty(value = "")
  public Permissions getEntitlements() {
    return entitlements;
  }

  public void setEntitlements(Permissions entitlements) {
    this.entitlements = entitlements;
  }

  public AppData entityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
    return this;
  }

  public AppData addEntityCategoriesItem(EntityCategory entityCategoriesItem) {
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

  public AppData fulfillment(String fulfillment) {
    this.fulfillment = fulfillment;
    return this;
  }

   /**
   * Get fulfillment
   * @return fulfillment
  **/
  @ApiModelProperty(value = "")
  public String getFulfillment() {
    return fulfillment;
  }

  public void setFulfillment(String fulfillment) {
    this.fulfillment = fulfillment;
  }

  public AppData fulfillmentInfo(FulfillmentInfo fulfillmentInfo) {
    this.fulfillmentInfo = fulfillmentInfo;
    return this;
  }

   /**
   * Get fulfillmentInfo
   * @return fulfillmentInfo
  **/
  @ApiModelProperty(value = "")
  public FulfillmentInfo getFulfillmentInfo() {
    return fulfillmentInfo;
  }

  public void setFulfillmentInfo(FulfillmentInfo fulfillmentInfo) {
    this.fulfillmentInfo = fulfillmentInfo;
  }

  public AppData fulfillmentInfos(List<FulfillmentInfo> fulfillmentInfos) {
    this.fulfillmentInfos = fulfillmentInfos;
    return this;
  }

  public AppData addFulfillmentInfosItem(FulfillmentInfo fulfillmentInfosItem) {
    if (this.fulfillmentInfos == null) {
      this.fulfillmentInfos = new ArrayList<FulfillmentInfo>();
    }
    this.fulfillmentInfos.add(fulfillmentInfosItem);
    return this;
  }

   /**
   * Get fulfillmentInfos
   * @return fulfillmentInfos
  **/
  @ApiModelProperty(value = "")
  public List<FulfillmentInfo> getFulfillmentInfos() {
    return fulfillmentInfos;
  }

  public void setFulfillmentInfos(List<FulfillmentInfo> fulfillmentInfos) {
    this.fulfillmentInfos = fulfillmentInfos;
  }

  public AppData fulfillmentOptions(String fulfillmentOptions) {
    this.fulfillmentOptions = fulfillmentOptions;
    return this;
  }

   /**
   * Get fulfillmentOptions
   * @return fulfillmentOptions
  **/
  @ApiModelProperty(value = "")
  public String getFulfillmentOptions() {
    return fulfillmentOptions;
  }

  public void setFulfillmentOptions(String fulfillmentOptions) {
    this.fulfillmentOptions = fulfillmentOptions;
  }

  public AppData hasAccountCollector(Boolean hasAccountCollector) {
    this.hasAccountCollector = hasAccountCollector;
    return this;
  }

   /**
   * Get hasAccountCollector
   * @return hasAccountCollector
  **/
  @ApiModelProperty(value = "")
  public Boolean isHasAccountCollector() {
    return hasAccountCollector;
  }

  public void setHasAccountCollector(Boolean hasAccountCollector) {
    this.hasAccountCollector = hasAccountCollector;
  }

  public AppData link(String link) {
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

  public AppData linkAccounts(String linkAccounts) {
    this.linkAccounts = linkAccounts;
    return this;
  }

   /**
   * Get linkAccounts
   * @return linkAccounts
  **/
  @ApiModelProperty(value = "")
  public String getLinkAccounts() {
    return linkAccounts;
  }

  public void setLinkAccounts(String linkAccounts) {
    this.linkAccounts = linkAccounts;
  }

  public AppData linkOwner(String linkOwner) {
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

  public AppData linkParent(String linkParent) {
    this.linkParent = linkParent;
    return this;
  }

   /**
   * Get linkParent
   * @return linkParent
  **/
  @ApiModelProperty(value = "")
  public String getLinkParent() {
    return linkParent;
  }

  public void setLinkParent(String linkParent) {
    this.linkParent = linkParent;
  }

  public AppData linkPermissions(String linkPermissions) {
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

  public AppData multiAppCollector(String multiAppCollector) {
    this.multiAppCollector = multiAppCollector;
    return this;
  }

   /**
   * Get multiAppCollector
   * @return multiAppCollector
  **/
  @ApiModelProperty(value = "")
  public String getMultiAppCollector() {
    return multiAppCollector;
  }

  public void setMultiAppCollector(String multiAppCollector) {
    this.multiAppCollector = multiAppCollector;
  }

  public AppData multiAppFlag(Boolean multiAppFlag) {
    this.multiAppFlag = multiAppFlag;
    return this;
  }

   /**
   * Get multiAppFlag
   * @return multiAppFlag
  **/
  @ApiModelProperty(value = "")
  public Boolean isMultiAppFlag() {
    return multiAppFlag;
  }

  public void setMultiAppFlag(Boolean multiAppFlag) {
    this.multiAppFlag = multiAppFlag;
  }

  public AppData multiAppTargetAllowable(Boolean multiAppTargetAllowable) {
    this.multiAppTargetAllowable = multiAppTargetAllowable;
    return this;
  }

   /**
   * Get multiAppTargetAllowable
   * @return multiAppTargetAllowable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMultiAppTargetAllowable() {
    return multiAppTargetAllowable;
  }

  public void setMultiAppTargetAllowable(Boolean multiAppTargetAllowable) {
    this.multiAppTargetAllowable = multiAppTargetAllowable;
  }

  public AppData ownerId(Long ownerId) {
    this.ownerId = ownerId;
    return this;
  }

   /**
   * Get ownerId
   * @return ownerId
  **/
  @ApiModelProperty(value = "")
  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public AppData ownerName(String ownerName) {
    this.ownerName = ownerName;
    return this;
  }

   /**
   * Get ownerName
   * @return ownerName
  **/
  @ApiModelProperty(value = "")
  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public AppData ownerPermissionId(Long ownerPermissionId) {
    this.ownerPermissionId = ownerPermissionId;
    return this;
  }

   /**
   * Get ownerPermissionId
   * @return ownerPermissionId
  **/
  @ApiModelProperty(value = "")
  public Long getOwnerPermissionId() {
    return ownerPermissionId;
  }

  public void setOwnerPermissionId(Long ownerPermissionId) {
    this.ownerPermissionId = ownerPermissionId;
  }

  public AppData ownerPermissionLink(String ownerPermissionLink) {
    this.ownerPermissionLink = ownerPermissionLink;
    return this;
  }

   /**
   * Get ownerPermissionLink
   * @return ownerPermissionLink
  **/
  @ApiModelProperty(value = "")
  public String getOwnerPermissionLink() {
    return ownerPermissionLink;
  }

  public void setOwnerPermissionLink(String ownerPermissionLink) {
    this.ownerPermissionLink = ownerPermissionLink;
  }

  public AppData ownerPermissionName(String ownerPermissionName) {
    this.ownerPermissionName = ownerPermissionName;
    return this;
  }

   /**
   * Get ownerPermissionName
   * @return ownerPermissionName
  **/
  @ApiModelProperty(value = "")
  public String getOwnerPermissionName() {
    return ownerPermissionName;
  }

  public void setOwnerPermissionName(String ownerPermissionName) {
    this.ownerPermissionName = ownerPermissionName;
  }

  public AppData owners(List<AppOwner> owners) {
    this.owners = owners;
    return this;
  }

  public AppData addOwnersItem(AppOwner ownersItem) {
    if (this.owners == null) {
      this.owners = new ArrayList<AppOwner>();
    }
    this.owners.add(ownersItem);
    return this;
  }

   /**
   * Get owners
   * @return owners
  **/
  @ApiModelProperty(value = "")
  public List<AppOwner> getOwners() {
    return owners;
  }

  public void setOwners(List<AppOwner> owners) {
    this.owners = owners;
  }

  public AppData permCount(Long permCount) {
    this.permCount = permCount;
    return this;
  }

   /**
   * Get permCount
   * @return permCount
  **/
  @ApiModelProperty(value = "")
  public Long getPermCount() {
    return permCount;
  }

  public void setPermCount(Long permCount) {
    this.permCount = permCount;
  }

  public AppData permissionSyncInfo(PermissionSyncInfo permissionSyncInfo) {
    this.permissionSyncInfo = permissionSyncInfo;
    return this;
  }

   /**
   * Get permissionSyncInfo
   * @return permissionSyncInfo
  **/
  @ApiModelProperty(value = "")
  public PermissionSyncInfo getPermissionSyncInfo() {
    return permissionSyncInfo;
  }

  public void setPermissionSyncInfo(PermissionSyncInfo permissionSyncInfo) {
    this.permissionSyncInfo = permissionSyncInfo;
  }

  public AppData qid(String qid) {
    this.qid = qid;
    return this;
  }

   /**
   * Get qid
   * @return qid
  **/
  @ApiModelProperty(value = "")
  public String getQid() {
    return qid;
  }

  public void setQid(String qid) {
    this.qid = qid;
  }

  public AppData requestApprovalPolicy(RequestPolicy requestApprovalPolicy) {
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

  public AppData requestPolicy(RequestPolicy requestPolicy) {
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

  public AppData selected(Boolean selected) {
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

  public AppData uniqueId(String uniqueId) {
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

  public AppData updatedBy(User updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

   /**
   * Get updatedBy
   * @return updatedBy
  **/
  @ApiModelProperty(value = "")
  public User getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(User updatedBy) {
    this.updatedBy = updatedBy;
  }

  public AppData userCount(Long userCount) {
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
    AppData appData = (AppData) o;
    return Objects.equals(this.tags, appData.tags) &&
        Objects.equals(this.administrators, appData.administrators) &&
        Objects.equals(this.affiliatedAppLink, appData.affiliatedAppLink) &&
        Objects.equals(this.affiliatedAppName, appData.affiliatedAppName) &&
        Objects.equals(this.appId, appData.appId) &&
        Objects.equals(this.appParentName, appData.appParentName) &&
        Objects.equals(this.appType, appData.appType) &&
        Objects.equals(this.attributes, appData.attributes) &&
        Objects.equals(this.authorizedBy, appData.authorizedBy) &&
        Objects.equals(this.canViewAnyApp, appData.canViewAnyApp) &&
        Objects.equals(this.canViewApp, appData.canViewApp) &&
        Objects.equals(this.collectorCount, appData.collectorCount) &&
        Objects.equals(this.createdBy, appData.createdBy) &&
        Objects.equals(this.deleted, appData.deleted) &&
        Objects.equals(this.editable, appData.editable) &&
        Objects.equals(this.entitlements, appData.entitlements) &&
        Objects.equals(this.entityCategories, appData.entityCategories) &&
        Objects.equals(this.fulfillment, appData.fulfillment) &&
        Objects.equals(this.fulfillmentInfo, appData.fulfillmentInfo) &&
        Objects.equals(this.fulfillmentInfos, appData.fulfillmentInfos) &&
        Objects.equals(this.fulfillmentOptions, appData.fulfillmentOptions) &&
        Objects.equals(this.hasAccountCollector, appData.hasAccountCollector) &&
        Objects.equals(this.link, appData.link) &&
        Objects.equals(this.linkAccounts, appData.linkAccounts) &&
        Objects.equals(this.linkOwner, appData.linkOwner) &&
        Objects.equals(this.linkParent, appData.linkParent) &&
        Objects.equals(this.linkPermissions, appData.linkPermissions) &&
        Objects.equals(this.multiAppCollector, appData.multiAppCollector) &&
        Objects.equals(this.multiAppFlag, appData.multiAppFlag) &&
        Objects.equals(this.multiAppTargetAllowable, appData.multiAppTargetAllowable) &&
        Objects.equals(this.ownerId, appData.ownerId) &&
        Objects.equals(this.ownerName, appData.ownerName) &&
        Objects.equals(this.ownerPermissionId, appData.ownerPermissionId) &&
        Objects.equals(this.ownerPermissionLink, appData.ownerPermissionLink) &&
        Objects.equals(this.ownerPermissionName, appData.ownerPermissionName) &&
        Objects.equals(this.owners, appData.owners) &&
        Objects.equals(this.permCount, appData.permCount) &&
        Objects.equals(this.permissionSyncInfo, appData.permissionSyncInfo) &&
        Objects.equals(this.qid, appData.qid) &&
        Objects.equals(this.requestApprovalPolicy, appData.requestApprovalPolicy) &&
        Objects.equals(this.requestPolicy, appData.requestPolicy) &&
        Objects.equals(this.selected, appData.selected) &&
        Objects.equals(this.uniqueId, appData.uniqueId) &&
        Objects.equals(this.updatedBy, appData.updatedBy) &&
        Objects.equals(this.userCount, appData.userCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tags, administrators, affiliatedAppLink, affiliatedAppName, appId, appParentName, appType, attributes, authorizedBy, canViewAnyApp, canViewApp, collectorCount, createdBy, deleted, editable, entitlements, entityCategories, fulfillment, fulfillmentInfo, fulfillmentInfos, fulfillmentOptions, hasAccountCollector, link, linkAccounts, linkOwner, linkParent, linkPermissions, multiAppCollector, multiAppFlag, multiAppTargetAllowable, ownerId, ownerName, ownerPermissionId, ownerPermissionLink, ownerPermissionName, owners, permCount, permissionSyncInfo, qid, requestApprovalPolicy, requestPolicy, selected, uniqueId, updatedBy, userCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppData {\n");
    
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    administrators: ").append(toIndentedString(administrators)).append("\n");
    sb.append("    affiliatedAppLink: ").append(toIndentedString(affiliatedAppLink)).append("\n");
    sb.append("    affiliatedAppName: ").append(toIndentedString(affiliatedAppName)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    appParentName: ").append(toIndentedString(appParentName)).append("\n");
    sb.append("    appType: ").append(toIndentedString(appType)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    authorizedBy: ").append(toIndentedString(authorizedBy)).append("\n");
    sb.append("    canViewAnyApp: ").append(toIndentedString(canViewAnyApp)).append("\n");
    sb.append("    canViewApp: ").append(toIndentedString(canViewApp)).append("\n");
    sb.append("    collectorCount: ").append(toIndentedString(collectorCount)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    entitlements: ").append(toIndentedString(entitlements)).append("\n");
    sb.append("    entityCategories: ").append(toIndentedString(entityCategories)).append("\n");
    sb.append("    fulfillment: ").append(toIndentedString(fulfillment)).append("\n");
    sb.append("    fulfillmentInfo: ").append(toIndentedString(fulfillmentInfo)).append("\n");
    sb.append("    fulfillmentInfos: ").append(toIndentedString(fulfillmentInfos)).append("\n");
    sb.append("    fulfillmentOptions: ").append(toIndentedString(fulfillmentOptions)).append("\n");
    sb.append("    hasAccountCollector: ").append(toIndentedString(hasAccountCollector)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkAccounts: ").append(toIndentedString(linkAccounts)).append("\n");
    sb.append("    linkOwner: ").append(toIndentedString(linkOwner)).append("\n");
    sb.append("    linkParent: ").append(toIndentedString(linkParent)).append("\n");
    sb.append("    linkPermissions: ").append(toIndentedString(linkPermissions)).append("\n");
    sb.append("    multiAppCollector: ").append(toIndentedString(multiAppCollector)).append("\n");
    sb.append("    multiAppFlag: ").append(toIndentedString(multiAppFlag)).append("\n");
    sb.append("    multiAppTargetAllowable: ").append(toIndentedString(multiAppTargetAllowable)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    ownerName: ").append(toIndentedString(ownerName)).append("\n");
    sb.append("    ownerPermissionId: ").append(toIndentedString(ownerPermissionId)).append("\n");
    sb.append("    ownerPermissionLink: ").append(toIndentedString(ownerPermissionLink)).append("\n");
    sb.append("    ownerPermissionName: ").append(toIndentedString(ownerPermissionName)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    permCount: ").append(toIndentedString(permCount)).append("\n");
    sb.append("    permissionSyncInfo: ").append(toIndentedString(permissionSyncInfo)).append("\n");
    sb.append("    qid: ").append(toIndentedString(qid)).append("\n");
    sb.append("    requestApprovalPolicy: ").append(toIndentedString(requestApprovalPolicy)).append("\n");
    sb.append("    requestPolicy: ").append(toIndentedString(requestPolicy)).append("\n");
    sb.append("    selected: ").append(toIndentedString(selected)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
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
