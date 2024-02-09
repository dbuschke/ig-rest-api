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
import io.swagger.client.model.ApprovalPolicy;
import io.swagger.client.model.AttributeValueNode;
import io.swagger.client.model.Britem;
import io.swagger.client.model.Burole;
import io.swagger.client.model.EntityCategory;
import io.swagger.client.model.Owner;
import io.swagger.client.model.RequestPolicy;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Burole
 */



public class Burole {
  @SerializedName("approvalNote")
  private String approvalNote = null;

  @SerializedName("approvalPolicy")
  private ApprovalPolicy approvalPolicy = null;

  /**
   * Gets or Sets approvalState
   */
  @JsonAdapter(ApprovalStateEnum.Adapter.class)
  public enum ApprovalStateEnum {
    @SerializedName("DRAFT")
    DRAFT("DRAFT"),
    @SerializedName("REJECTED")
    REJECTED("REJECTED"),
    @SerializedName("PENDING_APPROVAL")
    PENDING_APPROVAL("PENDING_APPROVAL"),
    @SerializedName("APPROVED")
    APPROVED("APPROVED"),
    @SerializedName("PUBLISHED")
    PUBLISHED("PUBLISHED"),
    @SerializedName("ARCHIVE")
    ARCHIVE("ARCHIVE"),
    @SerializedName("MINED")
    MINED("MINED");

    private String value;

    ApprovalStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ApprovalStateEnum fromValue(String input) {
      for (ApprovalStateEnum b : ApprovalStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ApprovalStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ApprovalStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ApprovalStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ApprovalStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("approvalState")
  private ApprovalStateEnum approvalState = null;

  @SerializedName("approvalTime")
  private Long approvalTime = null;

  @SerializedName("approvedBy")
  private User approvedBy = null;

  @SerializedName("attributes")
  private List<AttributeValueNode> attributes = null;

  @SerializedName("authAppCount")
  private Long authAppCount = null;

  @SerializedName("authPermCount")
  private Long authPermCount = null;

  @SerializedName("authRoleCount")
  private Long authRoleCount = null;

  @SerializedName("authorizationRole")
  private Boolean authorizationRole = null;

  @SerializedName("authorizedApps")
  private List<Britem> authorizedApps = null;

  @SerializedName("authorizedPerms")
  private List<Britem> authorizedPerms = null;

  @SerializedName("authorizedRoles")
  private List<Britem> authorizedRoles = null;

  @SerializedName("authsUpdatedBy")
  private User authsUpdatedBy = null;

  @SerializedName("authsUpdatedDate")
  private Long authsUpdatedDate = null;

  /**
   * Gets or Sets autoGrantPsodvCheck
   */
  @JsonAdapter(AutoGrantPsodvCheckEnum.Adapter.class)
  public enum AutoGrantPsodvCheckEnum {
    @SerializedName("ON")
    ON("ON"),
    @SerializedName("OFF")
    OFF("OFF"),
    @SerializedName("USE_GLOBAL")
    USE_GLOBAL("USE_GLOBAL");

    private String value;

    AutoGrantPsodvCheckEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AutoGrantPsodvCheckEnum fromValue(String input) {
      for (AutoGrantPsodvCheckEnum b : AutoGrantPsodvCheckEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AutoGrantPsodvCheckEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AutoGrantPsodvCheckEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AutoGrantPsodvCheckEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AutoGrantPsodvCheckEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("autoGrantPsodvCheck")
  private AutoGrantPsodvCheckEnum autoGrantPsodvCheck = null;

  @SerializedName("canApproveEntity")
  private Boolean canApproveEntity = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("canManageEntity")
  private Boolean canManageEntity = null;

  @SerializedName("changedBy")
  private User changedBy = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("description")
  private String description = null;

  /**
   * Gets or Sets detectStatus
   */
  @JsonAdapter(DetectStatusEnum.Adapter.class)
  public enum DetectStatusEnum {
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS("IN_PROGRESS"),
    @SerializedName("ERROR")
    ERROR("ERROR"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("CANCELED")
    CANCELED("CANCELED");

    private String value;

    DetectStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DetectStatusEnum fromValue(String input) {
      for (DetectStatusEnum b : DetectStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DetectStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DetectStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DetectStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DetectStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("detectStatus")
  private DetectStatusEnum detectStatus = null;

  @SerializedName("editableRoleId")
  private Long editableRoleId = null;

  /**
   * Gets or Sets editableRoleState
   */
  @JsonAdapter(EditableRoleStateEnum.Adapter.class)
  public enum EditableRoleStateEnum {
    @SerializedName("DRAFT")
    DRAFT("DRAFT"),
    @SerializedName("REJECTED")
    REJECTED("REJECTED"),
    @SerializedName("PENDING_APPROVAL")
    PENDING_APPROVAL("PENDING_APPROVAL"),
    @SerializedName("APPROVED")
    APPROVED("APPROVED"),
    @SerializedName("PUBLISHED")
    PUBLISHED("PUBLISHED"),
    @SerializedName("ARCHIVE")
    ARCHIVE("ARCHIVE"),
    @SerializedName("MINED")
    MINED("MINED");

    private String value;

    EditableRoleStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static EditableRoleStateEnum fromValue(String input) {
      for (EditableRoleStateEnum b : EditableRoleStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<EditableRoleStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EditableRoleStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public EditableRoleStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return EditableRoleStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("editableRoleState")
  private EditableRoleStateEnum editableRoleState = null;

  @SerializedName("entityCategories")
  private List<EntityCategory> entityCategories = null;

  @SerializedName("excludedGroupCount")
  private Long excludedGroupCount = null;

  @SerializedName("excludedGroups")
  private List<Britem> excludedGroups = null;

  @SerializedName("excludedUserCount")
  private Long excludedUserCount = null;

  @SerializedName("excludedUsers")
  private List<Britem> excludedUsers = null;

  @SerializedName("fulfiller")
  private Owner fulfiller = null;

  @SerializedName("gracePeriod")
  private Long gracePeriod = null;

  @SerializedName("haveInactiveItems")
  private Boolean haveInactiveItems = null;

  @SerializedName("haveInvalidItems")
  private Boolean haveInvalidItems = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("includedBRoleCount")
  private Long includedBRoleCount = null;

  @SerializedName("includedBRoles")
  private List<Britem> includedBRoles = null;

  @SerializedName("includedBy")
  private List<Britem> includedBy = null;

  @SerializedName("includedByCount")
  private Long includedByCount = null;

  @SerializedName("includedGroupCount")
  private Long includedGroupCount = null;

  @SerializedName("includedGroups")
  private List<Britem> includedGroups = null;

  @SerializedName("includedUserCount")
  private Long includedUserCount = null;

  @SerializedName("includedUsers")
  private List<Britem> includedUsers = null;

  @SerializedName("invalidCriteria")
  private Boolean invalidCriteria = null;

  @SerializedName("lastDetectTime")
  private Long lastDetectTime = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("managers")
  private List<Owner> managers = null;

  @SerializedName("membCriterias")
  private List<Britem> membCriterias = null;

  @SerializedName("membershipUpdatedBy")
  private User membershipUpdatedBy = null;

  @SerializedName("membershipUpdatedDate")
  private Long membershipUpdatedDate = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("owners")
  private List<Owner> owners = null;

  @SerializedName("publishTime")
  private Long publishTime = null;

  @SerializedName("publishedBy")
  private User publishedBy = null;

  @SerializedName("publishedNode")
  private Burole publishedNode = null;

  @SerializedName("referencedCount")
  private Long referencedCount = null;

  @SerializedName("requestApprovalPolicy")
  private RequestPolicy requestApprovalPolicy = null;

  @SerializedName("requestPolicy")
  private RequestPolicy requestPolicy = null;

  @SerializedName("requestable")
  private Boolean requestable = null;

  @SerializedName("resolved")
  private Boolean resolved = null;

  @SerializedName("risk")
  private Long risk = null;

  @SerializedName("uniquePolicyId")
  private String uniquePolicyId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  @SerializedName("userCount")
  private Long userCount = null;

  public Burole approvalNote(String approvalNote) {
    this.approvalNote = approvalNote;
    return this;
  }

   /**
   * Get approvalNote
   * @return approvalNote
  **/
  @ApiModelProperty(value = "")
  public String getApprovalNote() {
    return approvalNote;
  }

  public void setApprovalNote(String approvalNote) {
    this.approvalNote = approvalNote;
  }

  public Burole approvalPolicy(ApprovalPolicy approvalPolicy) {
    this.approvalPolicy = approvalPolicy;
    return this;
  }

   /**
   * Get approvalPolicy
   * @return approvalPolicy
  **/
  @ApiModelProperty(value = "")
  public ApprovalPolicy getApprovalPolicy() {
    return approvalPolicy;
  }

  public void setApprovalPolicy(ApprovalPolicy approvalPolicy) {
    this.approvalPolicy = approvalPolicy;
  }

  public Burole approvalState(ApprovalStateEnum approvalState) {
    this.approvalState = approvalState;
    return this;
  }

   /**
   * Get approvalState
   * @return approvalState
  **/
  @ApiModelProperty(value = "")
  public ApprovalStateEnum getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(ApprovalStateEnum approvalState) {
    this.approvalState = approvalState;
  }

  public Burole approvalTime(Long approvalTime) {
    this.approvalTime = approvalTime;
    return this;
  }

   /**
   * Get approvalTime
   * @return approvalTime
  **/
  @ApiModelProperty(value = "")
  public Long getApprovalTime() {
    return approvalTime;
  }

  public void setApprovalTime(Long approvalTime) {
    this.approvalTime = approvalTime;
  }

  public Burole approvedBy(User approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

   /**
   * Get approvedBy
   * @return approvedBy
  **/
  @ApiModelProperty(value = "")
  public User getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(User approvedBy) {
    this.approvedBy = approvedBy;
  }

  public Burole attributes(List<AttributeValueNode> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Burole addAttributesItem(AttributeValueNode attributesItem) {
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

  public Burole authAppCount(Long authAppCount) {
    this.authAppCount = authAppCount;
    return this;
  }

   /**
   * Get authAppCount
   * @return authAppCount
  **/
  @ApiModelProperty(value = "")
  public Long getAuthAppCount() {
    return authAppCount;
  }

  public void setAuthAppCount(Long authAppCount) {
    this.authAppCount = authAppCount;
  }

  public Burole authPermCount(Long authPermCount) {
    this.authPermCount = authPermCount;
    return this;
  }

   /**
   * Get authPermCount
   * @return authPermCount
  **/
  @ApiModelProperty(value = "")
  public Long getAuthPermCount() {
    return authPermCount;
  }

  public void setAuthPermCount(Long authPermCount) {
    this.authPermCount = authPermCount;
  }

  public Burole authRoleCount(Long authRoleCount) {
    this.authRoleCount = authRoleCount;
    return this;
  }

   /**
   * Get authRoleCount
   * @return authRoleCount
  **/
  @ApiModelProperty(value = "")
  public Long getAuthRoleCount() {
    return authRoleCount;
  }

  public void setAuthRoleCount(Long authRoleCount) {
    this.authRoleCount = authRoleCount;
  }

  public Burole authorizationRole(Boolean authorizationRole) {
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

  public Burole authorizedApps(List<Britem> authorizedApps) {
    this.authorizedApps = authorizedApps;
    return this;
  }

  public Burole addAuthorizedAppsItem(Britem authorizedAppsItem) {
    if (this.authorizedApps == null) {
      this.authorizedApps = new ArrayList<Britem>();
    }
    this.authorizedApps.add(authorizedAppsItem);
    return this;
  }

   /**
   * Get authorizedApps
   * @return authorizedApps
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getAuthorizedApps() {
    return authorizedApps;
  }

  public void setAuthorizedApps(List<Britem> authorizedApps) {
    this.authorizedApps = authorizedApps;
  }

  public Burole authorizedPerms(List<Britem> authorizedPerms) {
    this.authorizedPerms = authorizedPerms;
    return this;
  }

  public Burole addAuthorizedPermsItem(Britem authorizedPermsItem) {
    if (this.authorizedPerms == null) {
      this.authorizedPerms = new ArrayList<Britem>();
    }
    this.authorizedPerms.add(authorizedPermsItem);
    return this;
  }

   /**
   * Get authorizedPerms
   * @return authorizedPerms
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getAuthorizedPerms() {
    return authorizedPerms;
  }

  public void setAuthorizedPerms(List<Britem> authorizedPerms) {
    this.authorizedPerms = authorizedPerms;
  }

  public Burole authorizedRoles(List<Britem> authorizedRoles) {
    this.authorizedRoles = authorizedRoles;
    return this;
  }

  public Burole addAuthorizedRolesItem(Britem authorizedRolesItem) {
    if (this.authorizedRoles == null) {
      this.authorizedRoles = new ArrayList<Britem>();
    }
    this.authorizedRoles.add(authorizedRolesItem);
    return this;
  }

   /**
   * Get authorizedRoles
   * @return authorizedRoles
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getAuthorizedRoles() {
    return authorizedRoles;
  }

  public void setAuthorizedRoles(List<Britem> authorizedRoles) {
    this.authorizedRoles = authorizedRoles;
  }

  public Burole authsUpdatedBy(User authsUpdatedBy) {
    this.authsUpdatedBy = authsUpdatedBy;
    return this;
  }

   /**
   * Get authsUpdatedBy
   * @return authsUpdatedBy
  **/
  @ApiModelProperty(value = "")
  public User getAuthsUpdatedBy() {
    return authsUpdatedBy;
  }

  public void setAuthsUpdatedBy(User authsUpdatedBy) {
    this.authsUpdatedBy = authsUpdatedBy;
  }

  public Burole authsUpdatedDate(Long authsUpdatedDate) {
    this.authsUpdatedDate = authsUpdatedDate;
    return this;
  }

   /**
   * Get authsUpdatedDate
   * @return authsUpdatedDate
  **/
  @ApiModelProperty(value = "")
  public Long getAuthsUpdatedDate() {
    return authsUpdatedDate;
  }

  public void setAuthsUpdatedDate(Long authsUpdatedDate) {
    this.authsUpdatedDate = authsUpdatedDate;
  }

  public Burole autoGrantPsodvCheck(AutoGrantPsodvCheckEnum autoGrantPsodvCheck) {
    this.autoGrantPsodvCheck = autoGrantPsodvCheck;
    return this;
  }

   /**
   * Get autoGrantPsodvCheck
   * @return autoGrantPsodvCheck
  **/
  @ApiModelProperty(value = "")
  public AutoGrantPsodvCheckEnum getAutoGrantPsodvCheck() {
    return autoGrantPsodvCheck;
  }

  public void setAutoGrantPsodvCheck(AutoGrantPsodvCheckEnum autoGrantPsodvCheck) {
    this.autoGrantPsodvCheck = autoGrantPsodvCheck;
  }

  public Burole canApproveEntity(Boolean canApproveEntity) {
    this.canApproveEntity = canApproveEntity;
    return this;
  }

   /**
   * Get canApproveEntity
   * @return canApproveEntity
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanApproveEntity() {
    return canApproveEntity;
  }

  public void setCanApproveEntity(Boolean canApproveEntity) {
    this.canApproveEntity = canApproveEntity;
  }

  public Burole canEditEntity(Boolean canEditEntity) {
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

  public Burole canManageEntity(Boolean canManageEntity) {
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

  public Burole changedBy(User changedBy) {
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

  public Burole createdBy(User createdBy) {
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

  public Burole creationTime(Long creationTime) {
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

  public Burole description(String description) {
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

  public Burole detectStatus(DetectStatusEnum detectStatus) {
    this.detectStatus = detectStatus;
    return this;
  }

   /**
   * Get detectStatus
   * @return detectStatus
  **/
  @ApiModelProperty(value = "")
  public DetectStatusEnum getDetectStatus() {
    return detectStatus;
  }

  public void setDetectStatus(DetectStatusEnum detectStatus) {
    this.detectStatus = detectStatus;
  }

  public Burole editableRoleId(Long editableRoleId) {
    this.editableRoleId = editableRoleId;
    return this;
  }

   /**
   * Get editableRoleId
   * @return editableRoleId
  **/
  @ApiModelProperty(value = "")
  public Long getEditableRoleId() {
    return editableRoleId;
  }

  public void setEditableRoleId(Long editableRoleId) {
    this.editableRoleId = editableRoleId;
  }

  public Burole editableRoleState(EditableRoleStateEnum editableRoleState) {
    this.editableRoleState = editableRoleState;
    return this;
  }

   /**
   * Get editableRoleState
   * @return editableRoleState
  **/
  @ApiModelProperty(value = "")
  public EditableRoleStateEnum getEditableRoleState() {
    return editableRoleState;
  }

  public void setEditableRoleState(EditableRoleStateEnum editableRoleState) {
    this.editableRoleState = editableRoleState;
  }

  public Burole entityCategories(List<EntityCategory> entityCategories) {
    this.entityCategories = entityCategories;
    return this;
  }

  public Burole addEntityCategoriesItem(EntityCategory entityCategoriesItem) {
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

  public Burole excludedGroupCount(Long excludedGroupCount) {
    this.excludedGroupCount = excludedGroupCount;
    return this;
  }

   /**
   * Get excludedGroupCount
   * @return excludedGroupCount
  **/
  @ApiModelProperty(value = "")
  public Long getExcludedGroupCount() {
    return excludedGroupCount;
  }

  public void setExcludedGroupCount(Long excludedGroupCount) {
    this.excludedGroupCount = excludedGroupCount;
  }

  public Burole excludedGroups(List<Britem> excludedGroups) {
    this.excludedGroups = excludedGroups;
    return this;
  }

  public Burole addExcludedGroupsItem(Britem excludedGroupsItem) {
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

  public Burole excludedUserCount(Long excludedUserCount) {
    this.excludedUserCount = excludedUserCount;
    return this;
  }

   /**
   * Get excludedUserCount
   * @return excludedUserCount
  **/
  @ApiModelProperty(value = "")
  public Long getExcludedUserCount() {
    return excludedUserCount;
  }

  public void setExcludedUserCount(Long excludedUserCount) {
    this.excludedUserCount = excludedUserCount;
  }

  public Burole excludedUsers(List<Britem> excludedUsers) {
    this.excludedUsers = excludedUsers;
    return this;
  }

  public Burole addExcludedUsersItem(Britem excludedUsersItem) {
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

  public Burole fulfiller(Owner fulfiller) {
    this.fulfiller = fulfiller;
    return this;
  }

   /**
   * Get fulfiller
   * @return fulfiller
  **/
  @ApiModelProperty(value = "")
  public Owner getFulfiller() {
    return fulfiller;
  }

  public void setFulfiller(Owner fulfiller) {
    this.fulfiller = fulfiller;
  }

  public Burole gracePeriod(Long gracePeriod) {
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

  public Burole haveInactiveItems(Boolean haveInactiveItems) {
    this.haveInactiveItems = haveInactiveItems;
    return this;
  }

   /**
   * Get haveInactiveItems
   * @return haveInactiveItems
  **/
  @ApiModelProperty(value = "")
  public Boolean isHaveInactiveItems() {
    return haveInactiveItems;
  }

  public void setHaveInactiveItems(Boolean haveInactiveItems) {
    this.haveInactiveItems = haveInactiveItems;
  }

  public Burole haveInvalidItems(Boolean haveInvalidItems) {
    this.haveInvalidItems = haveInvalidItems;
    return this;
  }

   /**
   * Get haveInvalidItems
   * @return haveInvalidItems
  **/
  @ApiModelProperty(value = "")
  public Boolean isHaveInvalidItems() {
    return haveInvalidItems;
  }

  public void setHaveInvalidItems(Boolean haveInvalidItems) {
    this.haveInvalidItems = haveInvalidItems;
  }

  public Burole id(Long id) {
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

  public Burole includedBRoleCount(Long includedBRoleCount) {
    this.includedBRoleCount = includedBRoleCount;
    return this;
  }

   /**
   * Get includedBRoleCount
   * @return includedBRoleCount
  **/
  @ApiModelProperty(value = "")
  public Long getIncludedBRoleCount() {
    return includedBRoleCount;
  }

  public void setIncludedBRoleCount(Long includedBRoleCount) {
    this.includedBRoleCount = includedBRoleCount;
  }

  public Burole includedBRoles(List<Britem> includedBRoles) {
    this.includedBRoles = includedBRoles;
    return this;
  }

  public Burole addIncludedBRolesItem(Britem includedBRolesItem) {
    if (this.includedBRoles == null) {
      this.includedBRoles = new ArrayList<Britem>();
    }
    this.includedBRoles.add(includedBRolesItem);
    return this;
  }

   /**
   * Get includedBRoles
   * @return includedBRoles
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getIncludedBRoles() {
    return includedBRoles;
  }

  public void setIncludedBRoles(List<Britem> includedBRoles) {
    this.includedBRoles = includedBRoles;
  }

  public Burole includedBy(List<Britem> includedBy) {
    this.includedBy = includedBy;
    return this;
  }

  public Burole addIncludedByItem(Britem includedByItem) {
    if (this.includedBy == null) {
      this.includedBy = new ArrayList<Britem>();
    }
    this.includedBy.add(includedByItem);
    return this;
  }

   /**
   * Get includedBy
   * @return includedBy
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getIncludedBy() {
    return includedBy;
  }

  public void setIncludedBy(List<Britem> includedBy) {
    this.includedBy = includedBy;
  }

  public Burole includedByCount(Long includedByCount) {
    this.includedByCount = includedByCount;
    return this;
  }

   /**
   * Get includedByCount
   * @return includedByCount
  **/
  @ApiModelProperty(value = "")
  public Long getIncludedByCount() {
    return includedByCount;
  }

  public void setIncludedByCount(Long includedByCount) {
    this.includedByCount = includedByCount;
  }

  public Burole includedGroupCount(Long includedGroupCount) {
    this.includedGroupCount = includedGroupCount;
    return this;
  }

   /**
   * Get includedGroupCount
   * @return includedGroupCount
  **/
  @ApiModelProperty(value = "")
  public Long getIncludedGroupCount() {
    return includedGroupCount;
  }

  public void setIncludedGroupCount(Long includedGroupCount) {
    this.includedGroupCount = includedGroupCount;
  }

  public Burole includedGroups(List<Britem> includedGroups) {
    this.includedGroups = includedGroups;
    return this;
  }

  public Burole addIncludedGroupsItem(Britem includedGroupsItem) {
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

  public Burole includedUserCount(Long includedUserCount) {
    this.includedUserCount = includedUserCount;
    return this;
  }

   /**
   * Get includedUserCount
   * @return includedUserCount
  **/
  @ApiModelProperty(value = "")
  public Long getIncludedUserCount() {
    return includedUserCount;
  }

  public void setIncludedUserCount(Long includedUserCount) {
    this.includedUserCount = includedUserCount;
  }

  public Burole includedUsers(List<Britem> includedUsers) {
    this.includedUsers = includedUsers;
    return this;
  }

  public Burole addIncludedUsersItem(Britem includedUsersItem) {
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

  public Burole invalidCriteria(Boolean invalidCriteria) {
    this.invalidCriteria = invalidCriteria;
    return this;
  }

   /**
   * Get invalidCriteria
   * @return invalidCriteria
  **/
  @ApiModelProperty(value = "")
  public Boolean isInvalidCriteria() {
    return invalidCriteria;
  }

  public void setInvalidCriteria(Boolean invalidCriteria) {
    this.invalidCriteria = invalidCriteria;
  }

  public Burole lastDetectTime(Long lastDetectTime) {
    this.lastDetectTime = lastDetectTime;
    return this;
  }

   /**
   * Get lastDetectTime
   * @return lastDetectTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastDetectTime() {
    return lastDetectTime;
  }

  public void setLastDetectTime(Long lastDetectTime) {
    this.lastDetectTime = lastDetectTime;
  }

  public Burole link(String link) {
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

  public Burole managers(List<Owner> managers) {
    this.managers = managers;
    return this;
  }

  public Burole addManagersItem(Owner managersItem) {
    if (this.managers == null) {
      this.managers = new ArrayList<Owner>();
    }
    this.managers.add(managersItem);
    return this;
  }

   /**
   * Get managers
   * @return managers
  **/
  @ApiModelProperty(value = "")
  public List<Owner> getManagers() {
    return managers;
  }

  public void setManagers(List<Owner> managers) {
    this.managers = managers;
  }

  public Burole membCriterias(List<Britem> membCriterias) {
    this.membCriterias = membCriterias;
    return this;
  }

  public Burole addMembCriteriasItem(Britem membCriteriasItem) {
    if (this.membCriterias == null) {
      this.membCriterias = new ArrayList<Britem>();
    }
    this.membCriterias.add(membCriteriasItem);
    return this;
  }

   /**
   * Get membCriterias
   * @return membCriterias
  **/
  @ApiModelProperty(value = "")
  public List<Britem> getMembCriterias() {
    return membCriterias;
  }

  public void setMembCriterias(List<Britem> membCriterias) {
    this.membCriterias = membCriterias;
  }

  public Burole membershipUpdatedBy(User membershipUpdatedBy) {
    this.membershipUpdatedBy = membershipUpdatedBy;
    return this;
  }

   /**
   * Get membershipUpdatedBy
   * @return membershipUpdatedBy
  **/
  @ApiModelProperty(value = "")
  public User getMembershipUpdatedBy() {
    return membershipUpdatedBy;
  }

  public void setMembershipUpdatedBy(User membershipUpdatedBy) {
    this.membershipUpdatedBy = membershipUpdatedBy;
  }

  public Burole membershipUpdatedDate(Long membershipUpdatedDate) {
    this.membershipUpdatedDate = membershipUpdatedDate;
    return this;
  }

   /**
   * Get membershipUpdatedDate
   * @return membershipUpdatedDate
  **/
  @ApiModelProperty(value = "")
  public Long getMembershipUpdatedDate() {
    return membershipUpdatedDate;
  }

  public void setMembershipUpdatedDate(Long membershipUpdatedDate) {
    this.membershipUpdatedDate = membershipUpdatedDate;
  }

  public Burole name(String name) {
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

  public Burole owners(List<Owner> owners) {
    this.owners = owners;
    return this;
  }

  public Burole addOwnersItem(Owner ownersItem) {
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

  public Burole publishTime(Long publishTime) {
    this.publishTime = publishTime;
    return this;
  }

   /**
   * Get publishTime
   * @return publishTime
  **/
  @ApiModelProperty(value = "")
  public Long getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Long publishTime) {
    this.publishTime = publishTime;
  }

  public Burole publishedBy(User publishedBy) {
    this.publishedBy = publishedBy;
    return this;
  }

   /**
   * Get publishedBy
   * @return publishedBy
  **/
  @ApiModelProperty(value = "")
  public User getPublishedBy() {
    return publishedBy;
  }

  public void setPublishedBy(User publishedBy) {
    this.publishedBy = publishedBy;
  }

  public Burole publishedNode(Burole publishedNode) {
    this.publishedNode = publishedNode;
    return this;
  }

   /**
   * Get publishedNode
   * @return publishedNode
  **/
  @ApiModelProperty(value = "")
  public Burole getPublishedNode() {
    return publishedNode;
  }

  public void setPublishedNode(Burole publishedNode) {
    this.publishedNode = publishedNode;
  }

  public Burole referencedCount(Long referencedCount) {
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

  public Burole requestApprovalPolicy(RequestPolicy requestApprovalPolicy) {
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

  public Burole requestPolicy(RequestPolicy requestPolicy) {
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

  public Burole requestable(Boolean requestable) {
    this.requestable = requestable;
    return this;
  }

   /**
   * Get requestable
   * @return requestable
  **/
  @ApiModelProperty(value = "")
  public Boolean isRequestable() {
    return requestable;
  }

  public void setRequestable(Boolean requestable) {
    this.requestable = requestable;
  }

  public Burole resolved(Boolean resolved) {
    this.resolved = resolved;
    return this;
  }

   /**
   * Get resolved
   * @return resolved
  **/
  @ApiModelProperty(value = "")
  public Boolean isResolved() {
    return resolved;
  }

  public void setResolved(Boolean resolved) {
    this.resolved = resolved;
  }

  public Burole risk(Long risk) {
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

  public Burole uniquePolicyId(String uniquePolicyId) {
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

  public Burole updateTime(Long updateTime) {
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

  public Burole userCount(Long userCount) {
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
    Burole burole = (Burole) o;
    return Objects.equals(this.approvalNote, burole.approvalNote) &&
        Objects.equals(this.approvalPolicy, burole.approvalPolicy) &&
        Objects.equals(this.approvalState, burole.approvalState) &&
        Objects.equals(this.approvalTime, burole.approvalTime) &&
        Objects.equals(this.approvedBy, burole.approvedBy) &&
        Objects.equals(this.attributes, burole.attributes) &&
        Objects.equals(this.authAppCount, burole.authAppCount) &&
        Objects.equals(this.authPermCount, burole.authPermCount) &&
        Objects.equals(this.authRoleCount, burole.authRoleCount) &&
        Objects.equals(this.authorizationRole, burole.authorizationRole) &&
        Objects.equals(this.authorizedApps, burole.authorizedApps) &&
        Objects.equals(this.authorizedPerms, burole.authorizedPerms) &&
        Objects.equals(this.authorizedRoles, burole.authorizedRoles) &&
        Objects.equals(this.authsUpdatedBy, burole.authsUpdatedBy) &&
        Objects.equals(this.authsUpdatedDate, burole.authsUpdatedDate) &&
        Objects.equals(this.autoGrantPsodvCheck, burole.autoGrantPsodvCheck) &&
        Objects.equals(this.canApproveEntity, burole.canApproveEntity) &&
        Objects.equals(this.canEditEntity, burole.canEditEntity) &&
        Objects.equals(this.canManageEntity, burole.canManageEntity) &&
        Objects.equals(this.changedBy, burole.changedBy) &&
        Objects.equals(this.createdBy, burole.createdBy) &&
        Objects.equals(this.creationTime, burole.creationTime) &&
        Objects.equals(this.description, burole.description) &&
        Objects.equals(this.detectStatus, burole.detectStatus) &&
        Objects.equals(this.editableRoleId, burole.editableRoleId) &&
        Objects.equals(this.editableRoleState, burole.editableRoleState) &&
        Objects.equals(this.entityCategories, burole.entityCategories) &&
        Objects.equals(this.excludedGroupCount, burole.excludedGroupCount) &&
        Objects.equals(this.excludedGroups, burole.excludedGroups) &&
        Objects.equals(this.excludedUserCount, burole.excludedUserCount) &&
        Objects.equals(this.excludedUsers, burole.excludedUsers) &&
        Objects.equals(this.fulfiller, burole.fulfiller) &&
        Objects.equals(this.gracePeriod, burole.gracePeriod) &&
        Objects.equals(this.haveInactiveItems, burole.haveInactiveItems) &&
        Objects.equals(this.haveInvalidItems, burole.haveInvalidItems) &&
        Objects.equals(this.id, burole.id) &&
        Objects.equals(this.includedBRoleCount, burole.includedBRoleCount) &&
        Objects.equals(this.includedBRoles, burole.includedBRoles) &&
        Objects.equals(this.includedBy, burole.includedBy) &&
        Objects.equals(this.includedByCount, burole.includedByCount) &&
        Objects.equals(this.includedGroupCount, burole.includedGroupCount) &&
        Objects.equals(this.includedGroups, burole.includedGroups) &&
        Objects.equals(this.includedUserCount, burole.includedUserCount) &&
        Objects.equals(this.includedUsers, burole.includedUsers) &&
        Objects.equals(this.invalidCriteria, burole.invalidCriteria) &&
        Objects.equals(this.lastDetectTime, burole.lastDetectTime) &&
        Objects.equals(this.link, burole.link) &&
        Objects.equals(this.managers, burole.managers) &&
        Objects.equals(this.membCriterias, burole.membCriterias) &&
        Objects.equals(this.membershipUpdatedBy, burole.membershipUpdatedBy) &&
        Objects.equals(this.membershipUpdatedDate, burole.membershipUpdatedDate) &&
        Objects.equals(this.name, burole.name) &&
        Objects.equals(this.owners, burole.owners) &&
        Objects.equals(this.publishTime, burole.publishTime) &&
        Objects.equals(this.publishedBy, burole.publishedBy) &&
        Objects.equals(this.publishedNode, burole.publishedNode) &&
        Objects.equals(this.referencedCount, burole.referencedCount) &&
        Objects.equals(this.requestApprovalPolicy, burole.requestApprovalPolicy) &&
        Objects.equals(this.requestPolicy, burole.requestPolicy) &&
        Objects.equals(this.requestable, burole.requestable) &&
        Objects.equals(this.resolved, burole.resolved) &&
        Objects.equals(this.risk, burole.risk) &&
        Objects.equals(this.uniquePolicyId, burole.uniquePolicyId) &&
        Objects.equals(this.updateTime, burole.updateTime) &&
        Objects.equals(this.userCount, burole.userCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvalNote, approvalPolicy, approvalState, approvalTime, approvedBy, attributes, authAppCount, authPermCount, authRoleCount, authorizationRole, authorizedApps, authorizedPerms, authorizedRoles, authsUpdatedBy, authsUpdatedDate, autoGrantPsodvCheck, canApproveEntity, canEditEntity, canManageEntity, changedBy, createdBy, creationTime, description, detectStatus, editableRoleId, editableRoleState, entityCategories, excludedGroupCount, excludedGroups, excludedUserCount, excludedUsers, fulfiller, gracePeriod, haveInactiveItems, haveInvalidItems, id, includedBRoleCount, includedBRoles, includedBy, includedByCount, includedGroupCount, includedGroups, includedUserCount, includedUsers, invalidCriteria, lastDetectTime, link, managers, membCriterias, membershipUpdatedBy, membershipUpdatedDate, name, owners, publishTime, publishedBy, publishedNode, referencedCount, requestApprovalPolicy, requestPolicy, requestable, resolved, risk, uniquePolicyId, updateTime, userCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Burole {\n");
    
    sb.append("    approvalNote: ").append(toIndentedString(approvalNote)).append("\n");
    sb.append("    approvalPolicy: ").append(toIndentedString(approvalPolicy)).append("\n");
    sb.append("    approvalState: ").append(toIndentedString(approvalState)).append("\n");
    sb.append("    approvalTime: ").append(toIndentedString(approvalTime)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    authAppCount: ").append(toIndentedString(authAppCount)).append("\n");
    sb.append("    authPermCount: ").append(toIndentedString(authPermCount)).append("\n");
    sb.append("    authRoleCount: ").append(toIndentedString(authRoleCount)).append("\n");
    sb.append("    authorizationRole: ").append(toIndentedString(authorizationRole)).append("\n");
    sb.append("    authorizedApps: ").append(toIndentedString(authorizedApps)).append("\n");
    sb.append("    authorizedPerms: ").append(toIndentedString(authorizedPerms)).append("\n");
    sb.append("    authorizedRoles: ").append(toIndentedString(authorizedRoles)).append("\n");
    sb.append("    authsUpdatedBy: ").append(toIndentedString(authsUpdatedBy)).append("\n");
    sb.append("    authsUpdatedDate: ").append(toIndentedString(authsUpdatedDate)).append("\n");
    sb.append("    autoGrantPsodvCheck: ").append(toIndentedString(autoGrantPsodvCheck)).append("\n");
    sb.append("    canApproveEntity: ").append(toIndentedString(canApproveEntity)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    canManageEntity: ").append(toIndentedString(canManageEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    detectStatus: ").append(toIndentedString(detectStatus)).append("\n");
    sb.append("    editableRoleId: ").append(toIndentedString(editableRoleId)).append("\n");
    sb.append("    editableRoleState: ").append(toIndentedString(editableRoleState)).append("\n");
    sb.append("    entityCategories: ").append(toIndentedString(entityCategories)).append("\n");
    sb.append("    excludedGroupCount: ").append(toIndentedString(excludedGroupCount)).append("\n");
    sb.append("    excludedGroups: ").append(toIndentedString(excludedGroups)).append("\n");
    sb.append("    excludedUserCount: ").append(toIndentedString(excludedUserCount)).append("\n");
    sb.append("    excludedUsers: ").append(toIndentedString(excludedUsers)).append("\n");
    sb.append("    fulfiller: ").append(toIndentedString(fulfiller)).append("\n");
    sb.append("    gracePeriod: ").append(toIndentedString(gracePeriod)).append("\n");
    sb.append("    haveInactiveItems: ").append(toIndentedString(haveInactiveItems)).append("\n");
    sb.append("    haveInvalidItems: ").append(toIndentedString(haveInvalidItems)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    includedBRoleCount: ").append(toIndentedString(includedBRoleCount)).append("\n");
    sb.append("    includedBRoles: ").append(toIndentedString(includedBRoles)).append("\n");
    sb.append("    includedBy: ").append(toIndentedString(includedBy)).append("\n");
    sb.append("    includedByCount: ").append(toIndentedString(includedByCount)).append("\n");
    sb.append("    includedGroupCount: ").append(toIndentedString(includedGroupCount)).append("\n");
    sb.append("    includedGroups: ").append(toIndentedString(includedGroups)).append("\n");
    sb.append("    includedUserCount: ").append(toIndentedString(includedUserCount)).append("\n");
    sb.append("    includedUsers: ").append(toIndentedString(includedUsers)).append("\n");
    sb.append("    invalidCriteria: ").append(toIndentedString(invalidCriteria)).append("\n");
    sb.append("    lastDetectTime: ").append(toIndentedString(lastDetectTime)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    managers: ").append(toIndentedString(managers)).append("\n");
    sb.append("    membCriterias: ").append(toIndentedString(membCriterias)).append("\n");
    sb.append("    membershipUpdatedBy: ").append(toIndentedString(membershipUpdatedBy)).append("\n");
    sb.append("    membershipUpdatedDate: ").append(toIndentedString(membershipUpdatedDate)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    publishTime: ").append(toIndentedString(publishTime)).append("\n");
    sb.append("    publishedBy: ").append(toIndentedString(publishedBy)).append("\n");
    sb.append("    publishedNode: ").append(toIndentedString(publishedNode)).append("\n");
    sb.append("    referencedCount: ").append(toIndentedString(referencedCount)).append("\n");
    sb.append("    requestApprovalPolicy: ").append(toIndentedString(requestApprovalPolicy)).append("\n");
    sb.append("    requestPolicy: ").append(toIndentedString(requestPolicy)).append("\n");
    sb.append("    requestable: ").append(toIndentedString(requestable)).append("\n");
    sb.append("    resolved: ").append(toIndentedString(resolved)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
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
