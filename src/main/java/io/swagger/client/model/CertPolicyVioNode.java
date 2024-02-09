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
import io.swagger.client.model.ItemValue;
import io.swagger.client.model.TypeCount;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * CertPolicyVioNode
 */



public class CertPolicyVioNode {
  @SerializedName("accountId")
  private Long accountId = null;

  @SerializedName("accountName")
  private String accountName = null;

  @SerializedName("accountUniqueId")
  private String accountUniqueId = null;

  @SerializedName("applicationName")
  private String applicationName = null;

  @SerializedName("applicationUniqueId")
  private String applicationUniqueId = null;

  @SerializedName("businessRoleId")
  private Long businessRoleId = null;

  @SerializedName("businessRoleName")
  private String businessRoleName = null;

  @SerializedName("certPolicyId")
  private Long certPolicyId = null;

  @SerializedName("createdDate")
  private Long createdDate = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("isAccountDeleted")
  private Boolean isAccountDeleted = null;

  @SerializedName("isPermissionDeleted")
  private Boolean isPermissionDeleted = null;

  @SerializedName("isReviewDeleted")
  private Boolean isReviewDeleted = null;

  @SerializedName("isRoleDeleted")
  private Boolean isRoleDeleted = null;

  @SerializedName("isUserDeleted")
  private Boolean isUserDeleted = null;

  @SerializedName("lastActionDate")
  private Long lastActionDate = null;

  @SerializedName("lastDetectedDate")
  private Long lastDetectedDate = null;

  @SerializedName("permissionId")
  private Long permissionId = null;

  @SerializedName("permissionName")
  private String permissionName = null;

  @SerializedName("permissionUniqueId")
  private String permissionUniqueId = null;

  /**
   * Gets or Sets relationToUserType
   */
  @JsonAdapter(RelationToUserTypeEnum.Adapter.class)
  public enum RelationToUserTypeEnum {
    @SerializedName("UNMAPPED")
    UNMAPPED("UNMAPPED"),
    @SerializedName("SINGULAR")
    SINGULAR("SINGULAR"),
    @SerializedName("SHARED")
    SHARED("SHARED");

    private String value;

    RelationToUserTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RelationToUserTypeEnum fromValue(String input) {
      for (RelationToUserTypeEnum b : RelationToUserTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RelationToUserTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RelationToUserTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RelationToUserTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RelationToUserTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("relationToUserType")
  private RelationToUserTypeEnum relationToUserType = null;

  @SerializedName("remediationId")
  private Long remediationId = null;

  /**
   * Gets or Sets remediationType
   */
  @JsonAdapter(RemediationTypeEnum.Adapter.class)
  public enum RemediationTypeEnum {
    @SerializedName("EMAIL_NOTIFICATION")
    EMAIL_NOTIFICATION("EMAIL_NOTIFICATION"),
    @SerializedName("CHANGE_REQUEST")
    CHANGE_REQUEST("CHANGE_REQUEST"),
    @SerializedName("MICRO_CERTIFICATION")
    MICRO_CERTIFICATION("MICRO_CERTIFICATION"),
    @SerializedName("WORKFLOW")
    WORKFLOW("WORKFLOW");

    private String value;

    RemediationTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RemediationTypeEnum fromValue(String input) {
      for (RemediationTypeEnum b : RemediationTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RemediationTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RemediationTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RemediationTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RemediationTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("remediationType")
  private RemediationTypeEnum remediationType = null;

  @SerializedName("resolvedDate")
  private Long resolvedDate = null;

  @SerializedName("reviewDefName")
  private String reviewDefName = null;

  @SerializedName("reviewDefUniqueId")
  private String reviewDefUniqueId = null;

  @SerializedName("reviewEndDate")
  private Long reviewEndDate = null;

  @SerializedName("reviewInstanceId")
  private Long reviewInstanceId = null;

  @SerializedName("reviewItemId")
  private Long reviewItemId = null;

  @SerializedName("roleId")
  private Long roleId = null;

  @SerializedName("roleName")
  private String roleName = null;

  @SerializedName("typeCounts")
  private List<TypeCount> typeCounts = null;

  @SerializedName("userId")
  private Long userId = null;

  @SerializedName("userName")
  private String userName = null;

  @SerializedName("userUniqueId")
  private String userUniqueId = null;

  @SerializedName("values")
  private List<ItemValue> values = null;

  @SerializedName("violationCount")
  private Long violationCount = null;

  /**
   * Gets or Sets violationStatus
   */
  @JsonAdapter(ViolationStatusEnum.Adapter.class)
  public enum ViolationStatusEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("RESOLVED")
    RESOLVED("RESOLVED");

    private String value;

    ViolationStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ViolationStatusEnum fromValue(String input) {
      for (ViolationStatusEnum b : ViolationStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ViolationStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ViolationStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ViolationStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ViolationStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("violationStatus")
  private ViolationStatusEnum violationStatus = null;

  /**
   * Gets or Sets violationType
   */
  @JsonAdapter(ViolationTypeEnum.Adapter.class)
  public enum ViolationTypeEnum {
    @SerializedName("NO_DECISION")
    NO_DECISION("NO_DECISION"),
    @SerializedName("NOT_REVIEWED")
    NOT_REVIEWED("NOT_REVIEWED"),
    @SerializedName("REVIEW_IN_PROGRESS")
    REVIEW_IN_PROGRESS("REVIEW_IN_PROGRESS"),
    @SerializedName("OVERDUE")
    OVERDUE("OVERDUE"),
    @SerializedName("OVERDUE_NO_DECISION")
    OVERDUE_NO_DECISION("OVERDUE_NO_DECISION"),
    @SerializedName("ANY_DECISION")
    ANY_DECISION("ANY_DECISION"),
    @SerializedName("NO_DECISION_REVIEW_IN_PROGRESS")
    NO_DECISION_REVIEW_IN_PROGRESS("NO_DECISION_REVIEW_IN_PROGRESS"),
    @SerializedName("OVERDUE_REVIEW_IN_PROGRESS")
    OVERDUE_REVIEW_IN_PROGRESS("OVERDUE_REVIEW_IN_PROGRESS"),
    @SerializedName("OVERDUE_NO_DECISION_REVIEW_IN_PROGRESS")
    OVERDUE_NO_DECISION_REVIEW_IN_PROGRESS("OVERDUE_NO_DECISION_REVIEW_IN_PROGRESS"),
    @SerializedName("PARTIAL_APPROVED")
    PARTIAL_APPROVED("PARTIAL_APPROVED");

    private String value;

    ViolationTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ViolationTypeEnum fromValue(String input) {
      for (ViolationTypeEnum b : ViolationTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ViolationTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ViolationTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ViolationTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ViolationTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("violationType")
  private ViolationTypeEnum violationType = null;

  public CertPolicyVioNode accountId(Long accountId) {
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

  public CertPolicyVioNode accountName(String accountName) {
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

  public CertPolicyVioNode accountUniqueId(String accountUniqueId) {
    this.accountUniqueId = accountUniqueId;
    return this;
  }

   /**
   * Get accountUniqueId
   * @return accountUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getAccountUniqueId() {
    return accountUniqueId;
  }

  public void setAccountUniqueId(String accountUniqueId) {
    this.accountUniqueId = accountUniqueId;
  }

  public CertPolicyVioNode applicationName(String applicationName) {
    this.applicationName = applicationName;
    return this;
  }

   /**
   * Get applicationName
   * @return applicationName
  **/
  @ApiModelProperty(value = "")
  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public CertPolicyVioNode applicationUniqueId(String applicationUniqueId) {
    this.applicationUniqueId = applicationUniqueId;
    return this;
  }

   /**
   * Get applicationUniqueId
   * @return applicationUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getApplicationUniqueId() {
    return applicationUniqueId;
  }

  public void setApplicationUniqueId(String applicationUniqueId) {
    this.applicationUniqueId = applicationUniqueId;
  }

  public CertPolicyVioNode businessRoleId(Long businessRoleId) {
    this.businessRoleId = businessRoleId;
    return this;
  }

   /**
   * Get businessRoleId
   * @return businessRoleId
  **/
  @ApiModelProperty(value = "")
  public Long getBusinessRoleId() {
    return businessRoleId;
  }

  public void setBusinessRoleId(Long businessRoleId) {
    this.businessRoleId = businessRoleId;
  }

  public CertPolicyVioNode businessRoleName(String businessRoleName) {
    this.businessRoleName = businessRoleName;
    return this;
  }

   /**
   * Get businessRoleName
   * @return businessRoleName
  **/
  @ApiModelProperty(value = "")
  public String getBusinessRoleName() {
    return businessRoleName;
  }

  public void setBusinessRoleName(String businessRoleName) {
    this.businessRoleName = businessRoleName;
  }

  public CertPolicyVioNode certPolicyId(Long certPolicyId) {
    this.certPolicyId = certPolicyId;
    return this;
  }

   /**
   * Get certPolicyId
   * @return certPolicyId
  **/
  @ApiModelProperty(value = "")
  public Long getCertPolicyId() {
    return certPolicyId;
  }

  public void setCertPolicyId(Long certPolicyId) {
    this.certPolicyId = certPolicyId;
  }

  public CertPolicyVioNode createdDate(Long createdDate) {
    this.createdDate = createdDate;
    return this;
  }

   /**
   * Get createdDate
   * @return createdDate
  **/
  @ApiModelProperty(value = "")
  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public CertPolicyVioNode id(Long id) {
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

  public CertPolicyVioNode isAccountDeleted(Boolean isAccountDeleted) {
    this.isAccountDeleted = isAccountDeleted;
    return this;
  }

   /**
   * Get isAccountDeleted
   * @return isAccountDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsAccountDeleted() {
    return isAccountDeleted;
  }

  public void setIsAccountDeleted(Boolean isAccountDeleted) {
    this.isAccountDeleted = isAccountDeleted;
  }

  public CertPolicyVioNode isPermissionDeleted(Boolean isPermissionDeleted) {
    this.isPermissionDeleted = isPermissionDeleted;
    return this;
  }

   /**
   * Get isPermissionDeleted
   * @return isPermissionDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsPermissionDeleted() {
    return isPermissionDeleted;
  }

  public void setIsPermissionDeleted(Boolean isPermissionDeleted) {
    this.isPermissionDeleted = isPermissionDeleted;
  }

  public CertPolicyVioNode isReviewDeleted(Boolean isReviewDeleted) {
    this.isReviewDeleted = isReviewDeleted;
    return this;
  }

   /**
   * Get isReviewDeleted
   * @return isReviewDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsReviewDeleted() {
    return isReviewDeleted;
  }

  public void setIsReviewDeleted(Boolean isReviewDeleted) {
    this.isReviewDeleted = isReviewDeleted;
  }

  public CertPolicyVioNode isRoleDeleted(Boolean isRoleDeleted) {
    this.isRoleDeleted = isRoleDeleted;
    return this;
  }

   /**
   * Get isRoleDeleted
   * @return isRoleDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsRoleDeleted() {
    return isRoleDeleted;
  }

  public void setIsRoleDeleted(Boolean isRoleDeleted) {
    this.isRoleDeleted = isRoleDeleted;
  }

  public CertPolicyVioNode isUserDeleted(Boolean isUserDeleted) {
    this.isUserDeleted = isUserDeleted;
    return this;
  }

   /**
   * Get isUserDeleted
   * @return isUserDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsUserDeleted() {
    return isUserDeleted;
  }

  public void setIsUserDeleted(Boolean isUserDeleted) {
    this.isUserDeleted = isUserDeleted;
  }

  public CertPolicyVioNode lastActionDate(Long lastActionDate) {
    this.lastActionDate = lastActionDate;
    return this;
  }

   /**
   * Get lastActionDate
   * @return lastActionDate
  **/
  @ApiModelProperty(value = "")
  public Long getLastActionDate() {
    return lastActionDate;
  }

  public void setLastActionDate(Long lastActionDate) {
    this.lastActionDate = lastActionDate;
  }

  public CertPolicyVioNode lastDetectedDate(Long lastDetectedDate) {
    this.lastDetectedDate = lastDetectedDate;
    return this;
  }

   /**
   * Get lastDetectedDate
   * @return lastDetectedDate
  **/
  @ApiModelProperty(value = "")
  public Long getLastDetectedDate() {
    return lastDetectedDate;
  }

  public void setLastDetectedDate(Long lastDetectedDate) {
    this.lastDetectedDate = lastDetectedDate;
  }

  public CertPolicyVioNode permissionId(Long permissionId) {
    this.permissionId = permissionId;
    return this;
  }

   /**
   * Get permissionId
   * @return permissionId
  **/
  @ApiModelProperty(value = "")
  public Long getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Long permissionId) {
    this.permissionId = permissionId;
  }

  public CertPolicyVioNode permissionName(String permissionName) {
    this.permissionName = permissionName;
    return this;
  }

   /**
   * Get permissionName
   * @return permissionName
  **/
  @ApiModelProperty(value = "")
  public String getPermissionName() {
    return permissionName;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public CertPolicyVioNode permissionUniqueId(String permissionUniqueId) {
    this.permissionUniqueId = permissionUniqueId;
    return this;
  }

   /**
   * Get permissionUniqueId
   * @return permissionUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getPermissionUniqueId() {
    return permissionUniqueId;
  }

  public void setPermissionUniqueId(String permissionUniqueId) {
    this.permissionUniqueId = permissionUniqueId;
  }

  public CertPolicyVioNode relationToUserType(RelationToUserTypeEnum relationToUserType) {
    this.relationToUserType = relationToUserType;
    return this;
  }

   /**
   * Get relationToUserType
   * @return relationToUserType
  **/
  @ApiModelProperty(value = "")
  public RelationToUserTypeEnum getRelationToUserType() {
    return relationToUserType;
  }

  public void setRelationToUserType(RelationToUserTypeEnum relationToUserType) {
    this.relationToUserType = relationToUserType;
  }

  public CertPolicyVioNode remediationId(Long remediationId) {
    this.remediationId = remediationId;
    return this;
  }

   /**
   * Get remediationId
   * @return remediationId
  **/
  @ApiModelProperty(value = "")
  public Long getRemediationId() {
    return remediationId;
  }

  public void setRemediationId(Long remediationId) {
    this.remediationId = remediationId;
  }

  public CertPolicyVioNode remediationType(RemediationTypeEnum remediationType) {
    this.remediationType = remediationType;
    return this;
  }

   /**
   * Get remediationType
   * @return remediationType
  **/
  @ApiModelProperty(value = "")
  public RemediationTypeEnum getRemediationType() {
    return remediationType;
  }

  public void setRemediationType(RemediationTypeEnum remediationType) {
    this.remediationType = remediationType;
  }

  public CertPolicyVioNode resolvedDate(Long resolvedDate) {
    this.resolvedDate = resolvedDate;
    return this;
  }

   /**
   * Get resolvedDate
   * @return resolvedDate
  **/
  @ApiModelProperty(value = "")
  public Long getResolvedDate() {
    return resolvedDate;
  }

  public void setResolvedDate(Long resolvedDate) {
    this.resolvedDate = resolvedDate;
  }

  public CertPolicyVioNode reviewDefName(String reviewDefName) {
    this.reviewDefName = reviewDefName;
    return this;
  }

   /**
   * Get reviewDefName
   * @return reviewDefName
  **/
  @ApiModelProperty(value = "")
  public String getReviewDefName() {
    return reviewDefName;
  }

  public void setReviewDefName(String reviewDefName) {
    this.reviewDefName = reviewDefName;
  }

  public CertPolicyVioNode reviewDefUniqueId(String reviewDefUniqueId) {
    this.reviewDefUniqueId = reviewDefUniqueId;
    return this;
  }

   /**
   * Get reviewDefUniqueId
   * @return reviewDefUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getReviewDefUniqueId() {
    return reviewDefUniqueId;
  }

  public void setReviewDefUniqueId(String reviewDefUniqueId) {
    this.reviewDefUniqueId = reviewDefUniqueId;
  }

  public CertPolicyVioNode reviewEndDate(Long reviewEndDate) {
    this.reviewEndDate = reviewEndDate;
    return this;
  }

   /**
   * Get reviewEndDate
   * @return reviewEndDate
  **/
  @ApiModelProperty(value = "")
  public Long getReviewEndDate() {
    return reviewEndDate;
  }

  public void setReviewEndDate(Long reviewEndDate) {
    this.reviewEndDate = reviewEndDate;
  }

  public CertPolicyVioNode reviewInstanceId(Long reviewInstanceId) {
    this.reviewInstanceId = reviewInstanceId;
    return this;
  }

   /**
   * Get reviewInstanceId
   * @return reviewInstanceId
  **/
  @ApiModelProperty(value = "")
  public Long getReviewInstanceId() {
    return reviewInstanceId;
  }

  public void setReviewInstanceId(Long reviewInstanceId) {
    this.reviewInstanceId = reviewInstanceId;
  }

  public CertPolicyVioNode reviewItemId(Long reviewItemId) {
    this.reviewItemId = reviewItemId;
    return this;
  }

   /**
   * Get reviewItemId
   * @return reviewItemId
  **/
  @ApiModelProperty(value = "")
  public Long getReviewItemId() {
    return reviewItemId;
  }

  public void setReviewItemId(Long reviewItemId) {
    this.reviewItemId = reviewItemId;
  }

  public CertPolicyVioNode roleId(Long roleId) {
    this.roleId = roleId;
    return this;
  }

   /**
   * Get roleId
   * @return roleId
  **/
  @ApiModelProperty(value = "")
  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public CertPolicyVioNode roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

   /**
   * Get roleName
   * @return roleName
  **/
  @ApiModelProperty(value = "")
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public CertPolicyVioNode typeCounts(List<TypeCount> typeCounts) {
    this.typeCounts = typeCounts;
    return this;
  }

  public CertPolicyVioNode addTypeCountsItem(TypeCount typeCountsItem) {
    if (this.typeCounts == null) {
      this.typeCounts = new ArrayList<TypeCount>();
    }
    this.typeCounts.add(typeCountsItem);
    return this;
  }

   /**
   * Get typeCounts
   * @return typeCounts
  **/
  @ApiModelProperty(value = "")
  public List<TypeCount> getTypeCounts() {
    return typeCounts;
  }

  public void setTypeCounts(List<TypeCount> typeCounts) {
    this.typeCounts = typeCounts;
  }

  public CertPolicyVioNode userId(Long userId) {
    this.userId = userId;
    return this;
  }

   /**
   * Get userId
   * @return userId
  **/
  @ApiModelProperty(value = "")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public CertPolicyVioNode userName(String userName) {
    this.userName = userName;
    return this;
  }

   /**
   * Get userName
   * @return userName
  **/
  @ApiModelProperty(value = "")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public CertPolicyVioNode userUniqueId(String userUniqueId) {
    this.userUniqueId = userUniqueId;
    return this;
  }

   /**
   * Get userUniqueId
   * @return userUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getUserUniqueId() {
    return userUniqueId;
  }

  public void setUserUniqueId(String userUniqueId) {
    this.userUniqueId = userUniqueId;
  }

  public CertPolicyVioNode values(List<ItemValue> values) {
    this.values = values;
    return this;
  }

  public CertPolicyVioNode addValuesItem(ItemValue valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<ItemValue>();
    }
    this.values.add(valuesItem);
    return this;
  }

   /**
   * Get values
   * @return values
  **/
  @ApiModelProperty(value = "")
  public List<ItemValue> getValues() {
    return values;
  }

  public void setValues(List<ItemValue> values) {
    this.values = values;
  }

  public CertPolicyVioNode violationCount(Long violationCount) {
    this.violationCount = violationCount;
    return this;
  }

   /**
   * Get violationCount
   * @return violationCount
  **/
  @ApiModelProperty(value = "")
  public Long getViolationCount() {
    return violationCount;
  }

  public void setViolationCount(Long violationCount) {
    this.violationCount = violationCount;
  }

  public CertPolicyVioNode violationStatus(ViolationStatusEnum violationStatus) {
    this.violationStatus = violationStatus;
    return this;
  }

   /**
   * Get violationStatus
   * @return violationStatus
  **/
  @ApiModelProperty(value = "")
  public ViolationStatusEnum getViolationStatus() {
    return violationStatus;
  }

  public void setViolationStatus(ViolationStatusEnum violationStatus) {
    this.violationStatus = violationStatus;
  }

  public CertPolicyVioNode violationType(ViolationTypeEnum violationType) {
    this.violationType = violationType;
    return this;
  }

   /**
   * Get violationType
   * @return violationType
  **/
  @ApiModelProperty(value = "")
  public ViolationTypeEnum getViolationType() {
    return violationType;
  }

  public void setViolationType(ViolationTypeEnum violationType) {
    this.violationType = violationType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CertPolicyVioNode certPolicyVioNode = (CertPolicyVioNode) o;
    return Objects.equals(this.accountId, certPolicyVioNode.accountId) &&
        Objects.equals(this.accountName, certPolicyVioNode.accountName) &&
        Objects.equals(this.accountUniqueId, certPolicyVioNode.accountUniqueId) &&
        Objects.equals(this.applicationName, certPolicyVioNode.applicationName) &&
        Objects.equals(this.applicationUniqueId, certPolicyVioNode.applicationUniqueId) &&
        Objects.equals(this.businessRoleId, certPolicyVioNode.businessRoleId) &&
        Objects.equals(this.businessRoleName, certPolicyVioNode.businessRoleName) &&
        Objects.equals(this.certPolicyId, certPolicyVioNode.certPolicyId) &&
        Objects.equals(this.createdDate, certPolicyVioNode.createdDate) &&
        Objects.equals(this.id, certPolicyVioNode.id) &&
        Objects.equals(this.isAccountDeleted, certPolicyVioNode.isAccountDeleted) &&
        Objects.equals(this.isPermissionDeleted, certPolicyVioNode.isPermissionDeleted) &&
        Objects.equals(this.isReviewDeleted, certPolicyVioNode.isReviewDeleted) &&
        Objects.equals(this.isRoleDeleted, certPolicyVioNode.isRoleDeleted) &&
        Objects.equals(this.isUserDeleted, certPolicyVioNode.isUserDeleted) &&
        Objects.equals(this.lastActionDate, certPolicyVioNode.lastActionDate) &&
        Objects.equals(this.lastDetectedDate, certPolicyVioNode.lastDetectedDate) &&
        Objects.equals(this.permissionId, certPolicyVioNode.permissionId) &&
        Objects.equals(this.permissionName, certPolicyVioNode.permissionName) &&
        Objects.equals(this.permissionUniqueId, certPolicyVioNode.permissionUniqueId) &&
        Objects.equals(this.relationToUserType, certPolicyVioNode.relationToUserType) &&
        Objects.equals(this.remediationId, certPolicyVioNode.remediationId) &&
        Objects.equals(this.remediationType, certPolicyVioNode.remediationType) &&
        Objects.equals(this.resolvedDate, certPolicyVioNode.resolvedDate) &&
        Objects.equals(this.reviewDefName, certPolicyVioNode.reviewDefName) &&
        Objects.equals(this.reviewDefUniqueId, certPolicyVioNode.reviewDefUniqueId) &&
        Objects.equals(this.reviewEndDate, certPolicyVioNode.reviewEndDate) &&
        Objects.equals(this.reviewInstanceId, certPolicyVioNode.reviewInstanceId) &&
        Objects.equals(this.reviewItemId, certPolicyVioNode.reviewItemId) &&
        Objects.equals(this.roleId, certPolicyVioNode.roleId) &&
        Objects.equals(this.roleName, certPolicyVioNode.roleName) &&
        Objects.equals(this.typeCounts, certPolicyVioNode.typeCounts) &&
        Objects.equals(this.userId, certPolicyVioNode.userId) &&
        Objects.equals(this.userName, certPolicyVioNode.userName) &&
        Objects.equals(this.userUniqueId, certPolicyVioNode.userUniqueId) &&
        Objects.equals(this.values, certPolicyVioNode.values) &&
        Objects.equals(this.violationCount, certPolicyVioNode.violationCount) &&
        Objects.equals(this.violationStatus, certPolicyVioNode.violationStatus) &&
        Objects.equals(this.violationType, certPolicyVioNode.violationType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, accountName, accountUniqueId, applicationName, applicationUniqueId, businessRoleId, businessRoleName, certPolicyId, createdDate, id, isAccountDeleted, isPermissionDeleted, isReviewDeleted, isRoleDeleted, isUserDeleted, lastActionDate, lastDetectedDate, permissionId, permissionName, permissionUniqueId, relationToUserType, remediationId, remediationType, resolvedDate, reviewDefName, reviewDefUniqueId, reviewEndDate, reviewInstanceId, reviewItemId, roleId, roleName, typeCounts, userId, userName, userUniqueId, values, violationCount, violationStatus, violationType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CertPolicyVioNode {\n");
    
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    accountUniqueId: ").append(toIndentedString(accountUniqueId)).append("\n");
    sb.append("    applicationName: ").append(toIndentedString(applicationName)).append("\n");
    sb.append("    applicationUniqueId: ").append(toIndentedString(applicationUniqueId)).append("\n");
    sb.append("    businessRoleId: ").append(toIndentedString(businessRoleId)).append("\n");
    sb.append("    businessRoleName: ").append(toIndentedString(businessRoleName)).append("\n");
    sb.append("    certPolicyId: ").append(toIndentedString(certPolicyId)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isAccountDeleted: ").append(toIndentedString(isAccountDeleted)).append("\n");
    sb.append("    isPermissionDeleted: ").append(toIndentedString(isPermissionDeleted)).append("\n");
    sb.append("    isReviewDeleted: ").append(toIndentedString(isReviewDeleted)).append("\n");
    sb.append("    isRoleDeleted: ").append(toIndentedString(isRoleDeleted)).append("\n");
    sb.append("    isUserDeleted: ").append(toIndentedString(isUserDeleted)).append("\n");
    sb.append("    lastActionDate: ").append(toIndentedString(lastActionDate)).append("\n");
    sb.append("    lastDetectedDate: ").append(toIndentedString(lastDetectedDate)).append("\n");
    sb.append("    permissionId: ").append(toIndentedString(permissionId)).append("\n");
    sb.append("    permissionName: ").append(toIndentedString(permissionName)).append("\n");
    sb.append("    permissionUniqueId: ").append(toIndentedString(permissionUniqueId)).append("\n");
    sb.append("    relationToUserType: ").append(toIndentedString(relationToUserType)).append("\n");
    sb.append("    remediationId: ").append(toIndentedString(remediationId)).append("\n");
    sb.append("    remediationType: ").append(toIndentedString(remediationType)).append("\n");
    sb.append("    resolvedDate: ").append(toIndentedString(resolvedDate)).append("\n");
    sb.append("    reviewDefName: ").append(toIndentedString(reviewDefName)).append("\n");
    sb.append("    reviewDefUniqueId: ").append(toIndentedString(reviewDefUniqueId)).append("\n");
    sb.append("    reviewEndDate: ").append(toIndentedString(reviewEndDate)).append("\n");
    sb.append("    reviewInstanceId: ").append(toIndentedString(reviewInstanceId)).append("\n");
    sb.append("    reviewItemId: ").append(toIndentedString(reviewItemId)).append("\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
    sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
    sb.append("    typeCounts: ").append(toIndentedString(typeCounts)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    userUniqueId: ").append(toIndentedString(userUniqueId)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
    sb.append("    violationCount: ").append(toIndentedString(violationCount)).append("\n");
    sb.append("    violationStatus: ").append(toIndentedString(violationStatus)).append("\n");
    sb.append("    violationType: ").append(toIndentedString(violationType)).append("\n");
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
