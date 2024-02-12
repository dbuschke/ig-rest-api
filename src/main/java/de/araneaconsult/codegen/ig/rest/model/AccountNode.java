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
import de.araneaconsult.codegen.ig.rest.model.PermOwner;
import de.araneaconsult.codegen.ig.rest.model.Permission;
import de.araneaconsult.codegen.ig.rest.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AccountNode
 */



public class AccountNode {
  @SerializedName("accountId")
  private Long accountId = null;

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

  @SerializedName("attributes")
  private List<AttributeValueNode> attributes = null;

  @SerializedName("authorizedBy")
  private List<Authorizedby> authorizedBy = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("editable")
  private Boolean editable = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkAccounts")
  private String linkAccounts = null;

  @SerializedName("linkApplication")
  private String linkApplication = null;

  @SerializedName("mappings")
  private List<AttributeValueNode> mappings = null;

  @SerializedName("owners")
  private List<PermOwner> owners = null;

  @SerializedName("permissions")
  private List<Permission> permissions = null;

  @SerializedName("permsCount")
  private Long permsCount = null;

  @SerializedName("uniqueAcctId")
  private String uniqueAcctId = null;

  @SerializedName("users")
  private List<User> users = null;

  @SerializedName("usersCount")
  private Long usersCount = null;

  public AccountNode accountId(Long accountId) {
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

  public AccountNode appDisplayName(String appDisplayName) {
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

  public AccountNode appId(Long appId) {
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

  public AccountNode appType(AppTypeEnum appType) {
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

  public AccountNode attributes(List<AttributeValueNode> attributes) {
    this.attributes = attributes;
    return this;
  }

  public AccountNode addAttributesItem(AttributeValueNode attributesItem) {
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

  public AccountNode authorizedBy(List<Authorizedby> authorizedBy) {
    this.authorizedBy = authorizedBy;
    return this;
  }

  public AccountNode addAuthorizedByItem(Authorizedby authorizedByItem) {
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

  public AccountNode deleted(Boolean deleted) {
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

  public AccountNode editable(Boolean editable) {
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

  public AccountNode link(String link) {
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

  public AccountNode linkAccounts(String linkAccounts) {
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

  public AccountNode linkApplication(String linkApplication) {
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

  public AccountNode mappings(List<AttributeValueNode> mappings) {
    this.mappings = mappings;
    return this;
  }

  public AccountNode addMappingsItem(AttributeValueNode mappingsItem) {
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

  public AccountNode owners(List<PermOwner> owners) {
    this.owners = owners;
    return this;
  }

  public AccountNode addOwnersItem(PermOwner ownersItem) {
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

  public AccountNode permissions(List<Permission> permissions) {
    this.permissions = permissions;
    return this;
  }

  public AccountNode addPermissionsItem(Permission permissionsItem) {
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

  public AccountNode permsCount(Long permsCount) {
    this.permsCount = permsCount;
    return this;
  }

   /**
   * Get permsCount
   * @return permsCount
  **/
  @ApiModelProperty(value = "")
  public Long getPermsCount() {
    return permsCount;
  }

  public void setPermsCount(Long permsCount) {
    this.permsCount = permsCount;
  }

  public AccountNode uniqueAcctId(String uniqueAcctId) {
    this.uniqueAcctId = uniqueAcctId;
    return this;
  }

   /**
   * Get uniqueAcctId
   * @return uniqueAcctId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueAcctId() {
    return uniqueAcctId;
  }

  public void setUniqueAcctId(String uniqueAcctId) {
    this.uniqueAcctId = uniqueAcctId;
  }

  public AccountNode users(List<User> users) {
    this.users = users;
    return this;
  }

  public AccountNode addUsersItem(User usersItem) {
    if (this.users == null) {
      this.users = new ArrayList<User>();
    }
    this.users.add(usersItem);
    return this;
  }

   /**
   * Get users
   * @return users
  **/
  @ApiModelProperty(value = "")
  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public AccountNode usersCount(Long usersCount) {
    this.usersCount = usersCount;
    return this;
  }

   /**
   * Get usersCount
   * @return usersCount
  **/
  @ApiModelProperty(value = "")
  public Long getUsersCount() {
    return usersCount;
  }

  public void setUsersCount(Long usersCount) {
    this.usersCount = usersCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountNode accountNode = (AccountNode) o;
    return Objects.equals(this.accountId, accountNode.accountId) &&
        Objects.equals(this.appDisplayName, accountNode.appDisplayName) &&
        Objects.equals(this.appId, accountNode.appId) &&
        Objects.equals(this.appType, accountNode.appType) &&
        Objects.equals(this.attributes, accountNode.attributes) &&
        Objects.equals(this.authorizedBy, accountNode.authorizedBy) &&
        Objects.equals(this.deleted, accountNode.deleted) &&
        Objects.equals(this.editable, accountNode.editable) &&
        Objects.equals(this.link, accountNode.link) &&
        Objects.equals(this.linkAccounts, accountNode.linkAccounts) &&
        Objects.equals(this.linkApplication, accountNode.linkApplication) &&
        Objects.equals(this.mappings, accountNode.mappings) &&
        Objects.equals(this.owners, accountNode.owners) &&
        Objects.equals(this.permissions, accountNode.permissions) &&
        Objects.equals(this.permsCount, accountNode.permsCount) &&
        Objects.equals(this.uniqueAcctId, accountNode.uniqueAcctId) &&
        Objects.equals(this.users, accountNode.users) &&
        Objects.equals(this.usersCount, accountNode.usersCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, appDisplayName, appId, appType, attributes, authorizedBy, deleted, editable, link, linkAccounts, linkApplication, mappings, owners, permissions, permsCount, uniqueAcctId, users, usersCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountNode {\n");
    
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    appDisplayName: ").append(toIndentedString(appDisplayName)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    appType: ").append(toIndentedString(appType)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    authorizedBy: ").append(toIndentedString(authorizedBy)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkAccounts: ").append(toIndentedString(linkAccounts)).append("\n");
    sb.append("    linkApplication: ").append(toIndentedString(linkApplication)).append("\n");
    sb.append("    mappings: ").append(toIndentedString(mappings)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    permsCount: ").append(toIndentedString(permsCount)).append("\n");
    sb.append("    uniqueAcctId: ").append(toIndentedString(uniqueAcctId)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("    usersCount: ").append(toIndentedString(usersCount)).append("\n");
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
