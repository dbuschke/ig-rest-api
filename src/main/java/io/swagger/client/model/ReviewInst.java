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
import io.swagger.client.model.ApprovalTask;
import io.swagger.client.model.Attribute;
import io.swagger.client.model.FulfillmentStatus;
import io.swagger.client.model.ReviewDef;
import io.swagger.client.model.ReviewItem;
import io.swagger.client.model.Reviewer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ReviewInst
 */



public class ReviewInst {
  @SerializedName("allowModifyAccess")
  private Boolean allowModifyAccess = null;

  @SerializedName("allowOverrides")
  private Boolean allowOverrides = null;

  @SerializedName("approvalTasks")
  private List<ApprovalTask> approvalTasks = null;

  @SerializedName("approvedBy")
  private Reviewer approvedBy = null;

  @SerializedName("approvedByPolicy")
  private Boolean approvedByPolicy = null;

  @SerializedName("assignedRolesOnly")
  private Boolean assignedRolesOnly = null;

  @SerializedName("attributes")
  private List<Attribute> attributes = null;

  @SerializedName("auditor")
  private Reviewer auditor = null;

  @SerializedName("auditorComment")
  private String auditorComment = null;

  @SerializedName("canceledBy")
  private Reviewer canceledBy = null;

  @SerializedName("certifiedCount")
  private Integer certifiedCount = null;

  @SerializedName("comment")
  private String comment = null;

  @SerializedName("count")
  private Integer count = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("editable")
  private Boolean editable = null;

  @SerializedName("endTime")
  private Long endTime = null;

  @SerializedName("escalation")
  private Integer escalation = null;

  @SerializedName("escalationMs")
  private Long escalationMs = null;

  @SerializedName("escalationOwners")
  private List<Reviewer> escalationOwners = null;

  /**
   * Gets or Sets escalationUnit
   */
  @JsonAdapter(EscalationUnitEnum.Adapter.class)
  public enum EscalationUnitEnum {
    @SerializedName("MILLIS")
    MILLIS("MILLIS"),
    @SerializedName("SECONDS")
    SECONDS("SECONDS"),
    @SerializedName("MINUTES")
    MINUTES("MINUTES"),
    @SerializedName("HOURS")
    HOURS("HOURS"),
    @SerializedName("DAYS")
    DAYS("DAYS"),
    @SerializedName("WEEKS")
    WEEKS("WEEKS"),
    @SerializedName("MONTHS")
    MONTHS("MONTHS"),
    @SerializedName("YEARS")
    YEARS("YEARS");

    private String value;

    EscalationUnitEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static EscalationUnitEnum fromValue(String input) {
      for (EscalationUnitEnum b : EscalationUnitEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<EscalationUnitEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EscalationUnitEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public EscalationUnitEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return EscalationUnitEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("escalationUnit")
  private EscalationUnitEnum escalationUnit = null;

  @SerializedName("excludeAuthorized")
  private Boolean excludeAuthorized = null;

  @SerializedName("excludeNoDirectReports")
  private Boolean excludeNoDirectReports = null;

  @SerializedName("excludeNotAuthorized")
  private Boolean excludeNotAuthorized = null;

  @SerializedName("expectedEndTime")
  private Long expectedEndTime = null;

  /**
   * Gets or Sets expirationPolicy
   */
  @JsonAdapter(ExpirationPolicyEnum.Adapter.class)
  public enum ExpirationPolicyEnum {
    @SerializedName("TERMINATE")
    TERMINATE("TERMINATE"),
    @SerializedName("EXTEND")
    EXTEND("EXTEND"),
    @SerializedName("COMPLETE")
    COMPLETE("COMPLETE"),
    @SerializedName("COMPLETE_KEEP")
    COMPLETE_KEEP("COMPLETE_KEEP"),
    @SerializedName("COMPLETE_REMOVE")
    COMPLETE_REMOVE("COMPLETE_REMOVE");

    private String value;

    ExpirationPolicyEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ExpirationPolicyEnum fromValue(String input) {
      for (ExpirationPolicyEnum b : ExpirationPolicyEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ExpirationPolicyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ExpirationPolicyEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ExpirationPolicyEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ExpirationPolicyEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("expirationPolicy")
  private ExpirationPolicyEnum expirationPolicy = null;

  @SerializedName("fulfillmentItems")
  private List<FulfillmentStatus> fulfillmentItems = null;

  @SerializedName("groupInIDMRoles")
  private Boolean groupInIDMRoles = null;

  /**
   * Gets or Sets hideBulkActions
   */
  @JsonAdapter(HideBulkActionsEnum.Adapter.class)
  public enum HideBulkActionsEnum {
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("KEEP")
    KEEP("KEEP"),
    @SerializedName("REMOVE")
    REMOVE("REMOVE"),
    @SerializedName("ASSIGN_USER")
    ASSIGN_USER("ASSIGN_USER"),
    @SerializedName("CONFIRM_KEEP")
    CONFIRM_KEEP("CONFIRM_KEEP"),
    @SerializedName("CONFIRM_REMOVE")
    CONFIRM_REMOVE("CONFIRM_REMOVE"),
    @SerializedName("CONFIRM_ASSIGN_USER")
    CONFIRM_ASSIGN_USER("CONFIRM_ASSIGN_USER"),
    @SerializedName("SKIPPED")
    SKIPPED("SKIPPED"),
    @SerializedName("REASSIGNED")
    REASSIGNED("REASSIGNED"),
    @SerializedName("TIMED_OUT")
    TIMED_OUT("TIMED_OUT"),
    @SerializedName("ESCALATE")
    ESCALATE("ESCALATE"),
    @SerializedName("MODIFY_ACCESS")
    MODIFY_ACCESS("MODIFY_ACCESS"),
    @SerializedName("CONFIRM_MODIFY_ACCESS")
    CONFIRM_MODIFY_ACCESS("CONFIRM_MODIFY_ACCESS"),
    @SerializedName("MODIFY_ENTITY")
    MODIFY_ENTITY("MODIFY_ENTITY"),
    @SerializedName("CONFIRM_MODIFY_ENTITY")
    CONFIRM_MODIFY_ENTITY("CONFIRM_MODIFY_ENTITY"),
    @SerializedName("MODIFY_SUPERVISOR")
    MODIFY_SUPERVISOR("MODIFY_SUPERVISOR"),
    @SerializedName("CONFIRM_MODIFY_SUPERVISOR")
    CONFIRM_MODIFY_SUPERVISOR("CONFIRM_MODIFY_SUPERVISOR"),
    @SerializedName("SKIP_KEEP")
    SKIP_KEEP("SKIP_KEEP"),
    @SerializedName("SKIP_REMOVE")
    SKIP_REMOVE("SKIP_REMOVE"),
    @SerializedName("COMMENT_OVERRIDE")
    COMMENT_OVERRIDE("COMMENT_OVERRIDE");

    private String value;

    HideBulkActionsEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static HideBulkActionsEnum fromValue(String input) {
      for (HideBulkActionsEnum b : HideBulkActionsEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<HideBulkActionsEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final HideBulkActionsEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public HideBulkActionsEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return HideBulkActionsEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("hideBulkActions")
  private List<HideBulkActionsEnum> hideBulkActions = null;

  @SerializedName("hideReassign")
  private Boolean hideReassign = null;

  @SerializedName("inProgressCount")
  private Integer inProgressCount = null;

  @SerializedName("inProgressPendingCount")
  private Integer inProgressPendingCount = null;

  @SerializedName("includeAccounts")
  private Boolean includeAccounts = null;

  @SerializedName("includePermissions")
  private Boolean includePermissions = null;

  @SerializedName("instReviewers")
  private List<ReviewItem> instReviewers = null;

  @SerializedName("instructions")
  private String instructions = null;

  @SerializedName("itemApproveCount")
  private Integer itemApproveCount = null;

  @SerializedName("itemCompleteCount")
  private Integer itemCompleteCount = null;

  @SerializedName("itemCount")
  private Integer itemCount = null;

  @SerializedName("items")
  private List<ReviewItem> items = null;

  @SerializedName("lastStatusChange")
  private Long lastStatusChange = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkDefinition")
  private String linkDefinition = null;

  @SerializedName("linkInstance")
  private String linkInstance = null;

  @SerializedName("linkOwner")
  private String linkOwner = null;

  @SerializedName("linkReassign")
  private String linkReassign = null;

  @SerializedName("materializedViewCreated")
  private Boolean materializedViewCreated = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("ownerName")
  private String ownerName = null;

  @SerializedName("owners")
  private List<Reviewer> owners = null;

  /**
   * Gets or Sets partialApprovalPolicy
   */
  @JsonAdapter(PartialApprovalPolicyEnum.Adapter.class)
  public enum PartialApprovalPolicyEnum {
    @SerializedName("NONE")
    NONE("NONE"),
    @SerializedName("ON_DEMAND")
    ON_DEMAND("ON_DEMAND"),
    @SerializedName("AUTO")
    AUTO("AUTO");

    private String value;

    PartialApprovalPolicyEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static PartialApprovalPolicyEnum fromValue(String input) {
      for (PartialApprovalPolicyEnum b : PartialApprovalPolicyEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<PartialApprovalPolicyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PartialApprovalPolicyEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public PartialApprovalPolicyEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return PartialApprovalPolicyEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("partialApprovalPolicy")
  private PartialApprovalPolicyEnum partialApprovalPolicy = null;

  @SerializedName("pendingCount")
  private Integer pendingCount = null;

  @SerializedName("percentComplete")
  private Integer percentComplete = null;

  @SerializedName("previewDef")
  private ReviewDef previewDef = null;

  @SerializedName("previewDefId")
  private Long previewDefId = null;

  @SerializedName("reassignOnEscalation")
  private Boolean reassignOnEscalation = null;

  @SerializedName("removeAuditor")
  private Boolean removeAuditor = null;

  /**
   * Gets or Sets requiredComments
   */
  @JsonAdapter(RequiredCommentsEnum.Adapter.class)
  public enum RequiredCommentsEnum {
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("KEEP")
    KEEP("KEEP"),
    @SerializedName("REMOVE")
    REMOVE("REMOVE"),
    @SerializedName("ASSIGN_USER")
    ASSIGN_USER("ASSIGN_USER"),
    @SerializedName("CONFIRM_KEEP")
    CONFIRM_KEEP("CONFIRM_KEEP"),
    @SerializedName("CONFIRM_REMOVE")
    CONFIRM_REMOVE("CONFIRM_REMOVE"),
    @SerializedName("CONFIRM_ASSIGN_USER")
    CONFIRM_ASSIGN_USER("CONFIRM_ASSIGN_USER"),
    @SerializedName("SKIPPED")
    SKIPPED("SKIPPED"),
    @SerializedName("REASSIGNED")
    REASSIGNED("REASSIGNED"),
    @SerializedName("TIMED_OUT")
    TIMED_OUT("TIMED_OUT"),
    @SerializedName("ESCALATE")
    ESCALATE("ESCALATE"),
    @SerializedName("MODIFY_ACCESS")
    MODIFY_ACCESS("MODIFY_ACCESS"),
    @SerializedName("CONFIRM_MODIFY_ACCESS")
    CONFIRM_MODIFY_ACCESS("CONFIRM_MODIFY_ACCESS"),
    @SerializedName("MODIFY_ENTITY")
    MODIFY_ENTITY("MODIFY_ENTITY"),
    @SerializedName("CONFIRM_MODIFY_ENTITY")
    CONFIRM_MODIFY_ENTITY("CONFIRM_MODIFY_ENTITY"),
    @SerializedName("MODIFY_SUPERVISOR")
    MODIFY_SUPERVISOR("MODIFY_SUPERVISOR"),
    @SerializedName("CONFIRM_MODIFY_SUPERVISOR")
    CONFIRM_MODIFY_SUPERVISOR("CONFIRM_MODIFY_SUPERVISOR"),
    @SerializedName("SKIP_KEEP")
    SKIP_KEEP("SKIP_KEEP"),
    @SerializedName("SKIP_REMOVE")
    SKIP_REMOVE("SKIP_REMOVE"),
    @SerializedName("COMMENT_OVERRIDE")
    COMMENT_OVERRIDE("COMMENT_OVERRIDE");

    private String value;

    RequiredCommentsEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RequiredCommentsEnum fromValue(String input) {
      for (RequiredCommentsEnum b : RequiredCommentsEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RequiredCommentsEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RequiredCommentsEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RequiredCommentsEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RequiredCommentsEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("requiredComments")
  private List<RequiredCommentsEnum> requiredComments = null;

  @SerializedName("reviewDefId")
  private Long reviewDefId = null;

  @SerializedName("reviewInstId")
  private Long reviewInstId = null;

  /**
   * Gets or Sets reviewType
   */
  @JsonAdapter(ReviewTypeEnum.Adapter.class)
  public enum ReviewTypeEnum {
    @SerializedName("USER_ACCESS")
    USER_ACCESS("USER_ACCESS"),
    @SerializedName("ORPHAN")
    ORPHAN("ORPHAN"),
    @SerializedName("BROLE_MEMB")
    BROLE_MEMB("BROLE_MEMB"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("STAGED")
    STAGED("STAGED"),
    @SerializedName("USER_PROFILE")
    USER_PROFILE("USER_PROFILE"),
    @SerializedName("DIRECT_REPORT")
    DIRECT_REPORT("DIRECT_REPORT"),
    @SerializedName("MICRO_CERT")
    MICRO_CERT("MICRO_CERT"),
    @SerializedName("MICRO_CERT_DATA")
    MICRO_CERT_DATA("MICRO_CERT_DATA"),
    @SerializedName("BROLE_DEFN")
    BROLE_DEFN("BROLE_DEFN"),
    @SerializedName("ACCOUNT_ACCESS")
    ACCOUNT_ACCESS("ACCOUNT_ACCESS"),
    @SerializedName("BROLE_AUTH")
    BROLE_AUTH("BROLE_AUTH"),
    @SerializedName("TROLE_DEFN")
    TROLE_DEFN("TROLE_DEFN");

    private String value;

    ReviewTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ReviewTypeEnum fromValue(String input) {
      for (ReviewTypeEnum b : ReviewTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ReviewTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReviewTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ReviewTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ReviewTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("reviewType")
  private ReviewTypeEnum reviewType = null;

  @SerializedName("reviewer")
  private ReviewItem reviewer = null;

  @SerializedName("reviewerCertifyReadyCount")
  private Integer reviewerCertifyReadyCount = null;

  @SerializedName("reviewerCompleteCount")
  private Integer reviewerCompleteCount = null;

  @SerializedName("reviewerTaskCount")
  private Integer reviewerTaskCount = null;

  @SerializedName("schedule")
  private Reviewer schedule = null;

  /**
   * Gets or Sets selfReviewPolicy
   */
  @JsonAdapter(SelfReviewPolicyEnum.Adapter.class)
  public enum SelfReviewPolicyEnum {
    @SerializedName("NOT_ALLOWED")
    NOT_ALLOWED("NOT_ALLOWED"),
    @SerializedName("ALLOWED_ALL")
    ALLOWED_ALL("ALLOWED_ALL"),
    @SerializedName("LIMITED_MULTI")
    LIMITED_MULTI("LIMITED_MULTI");

    private String value;

    SelfReviewPolicyEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static SelfReviewPolicyEnum fromValue(String input) {
      for (SelfReviewPolicyEnum b : SelfReviewPolicyEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<SelfReviewPolicyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final SelfReviewPolicyEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public SelfReviewPolicyEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return SelfReviewPolicyEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("selfReviewPolicy")
  private SelfReviewPolicyEnum selfReviewPolicy = null;

  @SerializedName("showAcctPermissions")
  private Boolean showAcctPermissions = null;

  @SerializedName("showBRoleMembers")
  private Boolean showBRoleMembers = null;

  @SerializedName("showBRolePolicies")
  private Boolean showBRolePolicies = null;

  @SerializedName("showRolePermissions")
  private Boolean showRolePermissions = null;

  @SerializedName("startMessage")
  private String startMessage = null;

  /**
   * Gets or Sets startState
   */
  @JsonAdapter(StartStateEnum.Adapter.class)
  public enum StartStateEnum {
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("READY_PHASE_1")
    READY_PHASE_1("READY_PHASE_1"),
    @SerializedName("RUN_PHASE_1")
    RUN_PHASE_1("RUN_PHASE_1"),
    @SerializedName("RUNNING_PHASE_1")
    RUNNING_PHASE_1("RUNNING_PHASE_1"),
    @SerializedName("ERR_PHASE_1")
    ERR_PHASE_1("ERR_PHASE_1"),
    @SerializedName("DONE_PHASE_1")
    DONE_PHASE_1("DONE_PHASE_1"),
    @SerializedName("RUN_PHASE_2")
    RUN_PHASE_2("RUN_PHASE_2"),
    @SerializedName("RUNNING_PHASE_2")
    RUNNING_PHASE_2("RUNNING_PHASE_2"),
    @SerializedName("ERR_PHASE_2")
    ERR_PHASE_2("ERR_PHASE_2"),
    @SerializedName("DONE_PHASE_2")
    DONE_PHASE_2("DONE_PHASE_2"),
    @SerializedName("CANCEL_EXECUTING")
    CANCEL_EXECUTING("CANCEL_EXECUTING"),
    @SerializedName("CANCEL_PHASE_1")
    CANCEL_PHASE_1("CANCEL_PHASE_1"),
    @SerializedName("CANCELLING_PHASE_1")
    CANCELLING_PHASE_1("CANCELLING_PHASE_1"),
    @SerializedName("CANCEL_PHASE_2")
    CANCEL_PHASE_2("CANCEL_PHASE_2"),
    @SerializedName("CANCELLING_PHASE_2")
    CANCELLING_PHASE_2("CANCELLING_PHASE_2"),
    @SerializedName("COMPLETE")
    COMPLETE("COMPLETE"),
    @SerializedName("STOPPED")
    STOPPED("STOPPED"),
    @SerializedName("CANCEL_EXECUTION_FAILED")
    CANCEL_EXECUTION_FAILED("CANCEL_EXECUTION_FAILED"),
    @SerializedName("TERMINATE_EXECUTING")
    TERMINATE_EXECUTING("TERMINATE_EXECUTING"),
    @SerializedName("TERMINATED")
    TERMINATED("TERMINATED"),
    @SerializedName("TERMINATE_FAILED")
    TERMINATE_FAILED("TERMINATE_FAILED");

    private String value;

    StartStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StartStateEnum fromValue(String input) {
      for (StartStateEnum b : StartStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StartStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StartStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StartStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StartStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("startState")
  private StartStateEnum startState = null;

  /**
   * Gets or Sets startStatus
   */
  @JsonAdapter(StartStatusEnum.Adapter.class)
  public enum StartStatusEnum {
    @SerializedName("RUNNING")
    RUNNING("RUNNING"),
    @SerializedName("CANCEL_REQUESTED")
    CANCEL_REQUESTED("CANCEL_REQUESTED"),
    @SerializedName("CANCEL_PENDING")
    CANCEL_PENDING("CANCEL_PENDING"),
    @SerializedName("TERMINATE_PENDING")
    TERMINATE_PENDING("TERMINATE_PENDING"),
    @SerializedName("CANCELLED")
    CANCELLED("CANCELLED"),
    @SerializedName("FAILED")
    FAILED("FAILED"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("TERMINATED")
    TERMINATED("TERMINATED"),
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("BULK_DETECTION")
    BULK_DETECTION("BULK_DETECTION");

    private String value;

    StartStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StartStatusEnum fromValue(String input) {
      for (StartStatusEnum b : StartStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StartStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StartStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StartStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StartStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("startStatus")
  private StartStatusEnum startStatus = null;

  @SerializedName("startTime")
  private Long startTime = null;

  /**
   * Gets or Sets startedBy
   */
  @JsonAdapter(StartedByEnum.Adapter.class)
  public enum StartedByEnum {
    @SerializedName("ON_DEMAND")
    ON_DEMAND("ON_DEMAND"),
    @SerializedName("SCHEDULE")
    SCHEDULE("SCHEDULE"),
    @SerializedName("EXTERNAL")
    EXTERNAL("EXTERNAL");

    private String value;

    StartedByEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StartedByEnum fromValue(String input) {
      for (StartedByEnum b : StartedByEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StartedByEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StartedByEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StartedByEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StartedByEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("startedBy")
  private StartedByEnum startedBy = null;

  @SerializedName("startedByUser")
  private Reviewer startedByUser = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("STARTING")
    STARTING("STARTING"),
    @SerializedName("PREVIEW")
    PREVIEW("PREVIEW"),
    @SerializedName("PREVIEW_STARTING")
    PREVIEW_STARTING("PREVIEW_STARTING"),
    @SerializedName("RUNNING")
    RUNNING("RUNNING"),
    @SerializedName("CERTIFYING")
    CERTIFYING("CERTIFYING"),
    @SerializedName("COMPLETE")
    COMPLETE("COMPLETE"),
    @SerializedName("PARTIAL_APPROVE")
    PARTIAL_APPROVE("PARTIAL_APPROVE"),
    @SerializedName("AUDIT_APPROVAL")
    AUDIT_APPROVAL("AUDIT_APPROVAL"),
    @SerializedName("NOT_CERTIFIED")
    NOT_CERTIFIED("NOT_CERTIFIED"),
    @SerializedName("CERTIFIED")
    CERTIFIED("CERTIFIED"),
    @SerializedName("CANCELED")
    CANCELED("CANCELED"),
    @SerializedName("PREVIEW_CANCELED")
    PREVIEW_CANCELED("PREVIEW_CANCELED"),
    @SerializedName("TIMED_OUT")
    TIMED_OUT("TIMED_OUT"),
    @SerializedName("ERROR")
    ERROR("ERROR");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StatusEnum fromValue(String input) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("status")
  private StatusEnum status = null;

  @SerializedName("stoppable")
  private Boolean stoppable = null;

  @SerializedName("taskCompleteCount")
  private Integer taskCompleteCount = null;

  @SerializedName("taskCount")
  private Integer taskCount = null;

  @SerializedName("taskGroupAttrKey")
  private String taskGroupAttrKey = null;

  @SerializedName("taskSortAttrKey")
  private String taskSortAttrKey = null;

  @SerializedName("taskType")
  private String taskType = null;

  @SerializedName("totalSearch")
  private Integer totalSearch = null;

  @SerializedName("uniqueReviewDefId")
  private String uniqueReviewDefId = null;

  @SerializedName("useMaterializedView")
  private Boolean useMaterializedView = null;

  @SerializedName("validToTime")
  private Long validToTime = null;

  @SerializedName("wfSyncFailed")
  private Boolean wfSyncFailed = null;

  public ReviewInst allowModifyAccess(Boolean allowModifyAccess) {
    this.allowModifyAccess = allowModifyAccess;
    return this;
  }

   /**
   * Get allowModifyAccess
   * @return allowModifyAccess
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowModifyAccess() {
    return allowModifyAccess;
  }

  public void setAllowModifyAccess(Boolean allowModifyAccess) {
    this.allowModifyAccess = allowModifyAccess;
  }

  public ReviewInst allowOverrides(Boolean allowOverrides) {
    this.allowOverrides = allowOverrides;
    return this;
  }

   /**
   * Get allowOverrides
   * @return allowOverrides
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowOverrides() {
    return allowOverrides;
  }

  public void setAllowOverrides(Boolean allowOverrides) {
    this.allowOverrides = allowOverrides;
  }

  public ReviewInst approvalTasks(List<ApprovalTask> approvalTasks) {
    this.approvalTasks = approvalTasks;
    return this;
  }

  public ReviewInst addApprovalTasksItem(ApprovalTask approvalTasksItem) {
    if (this.approvalTasks == null) {
      this.approvalTasks = new ArrayList<ApprovalTask>();
    }
    this.approvalTasks.add(approvalTasksItem);
    return this;
  }

   /**
   * Get approvalTasks
   * @return approvalTasks
  **/
  @ApiModelProperty(value = "")
  public List<ApprovalTask> getApprovalTasks() {
    return approvalTasks;
  }

  public void setApprovalTasks(List<ApprovalTask> approvalTasks) {
    this.approvalTasks = approvalTasks;
  }

  public ReviewInst approvedBy(Reviewer approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

   /**
   * Get approvedBy
   * @return approvedBy
  **/
  @ApiModelProperty(value = "")
  public Reviewer getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(Reviewer approvedBy) {
    this.approvedBy = approvedBy;
  }

  public ReviewInst approvedByPolicy(Boolean approvedByPolicy) {
    this.approvedByPolicy = approvedByPolicy;
    return this;
  }

   /**
   * Get approvedByPolicy
   * @return approvedByPolicy
  **/
  @ApiModelProperty(value = "")
  public Boolean isApprovedByPolicy() {
    return approvedByPolicy;
  }

  public void setApprovedByPolicy(Boolean approvedByPolicy) {
    this.approvedByPolicy = approvedByPolicy;
  }

  public ReviewInst assignedRolesOnly(Boolean assignedRolesOnly) {
    this.assignedRolesOnly = assignedRolesOnly;
    return this;
  }

   /**
   * Get assignedRolesOnly
   * @return assignedRolesOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean isAssignedRolesOnly() {
    return assignedRolesOnly;
  }

  public void setAssignedRolesOnly(Boolean assignedRolesOnly) {
    this.assignedRolesOnly = assignedRolesOnly;
  }

  public ReviewInst attributes(List<Attribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public ReviewInst addAttributesItem(Attribute attributesItem) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<Attribute>();
    }
    this.attributes.add(attributesItem);
    return this;
  }

   /**
   * Get attributes
   * @return attributes
  **/
  @ApiModelProperty(value = "")
  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  public ReviewInst auditor(Reviewer auditor) {
    this.auditor = auditor;
    return this;
  }

   /**
   * Get auditor
   * @return auditor
  **/
  @ApiModelProperty(value = "")
  public Reviewer getAuditor() {
    return auditor;
  }

  public void setAuditor(Reviewer auditor) {
    this.auditor = auditor;
  }

  public ReviewInst auditorComment(String auditorComment) {
    this.auditorComment = auditorComment;
    return this;
  }

   /**
   * Get auditorComment
   * @return auditorComment
  **/
  @ApiModelProperty(value = "")
  public String getAuditorComment() {
    return auditorComment;
  }

  public void setAuditorComment(String auditorComment) {
    this.auditorComment = auditorComment;
  }

  public ReviewInst canceledBy(Reviewer canceledBy) {
    this.canceledBy = canceledBy;
    return this;
  }

   /**
   * Get canceledBy
   * @return canceledBy
  **/
  @ApiModelProperty(value = "")
  public Reviewer getCanceledBy() {
    return canceledBy;
  }

  public void setCanceledBy(Reviewer canceledBy) {
    this.canceledBy = canceledBy;
  }

  public ReviewInst certifiedCount(Integer certifiedCount) {
    this.certifiedCount = certifiedCount;
    return this;
  }

   /**
   * Get certifiedCount
   * @return certifiedCount
  **/
  @ApiModelProperty(value = "")
  public Integer getCertifiedCount() {
    return certifiedCount;
  }

  public void setCertifiedCount(Integer certifiedCount) {
    this.certifiedCount = certifiedCount;
  }

  public ReviewInst comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @ApiModelProperty(value = "")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public ReviewInst count(Integer count) {
    this.count = count;
    return this;
  }

   /**
   * Get count
   * @return count
  **/
  @ApiModelProperty(value = "")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public ReviewInst description(String description) {
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

  public ReviewInst editable(Boolean editable) {
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

  public ReviewInst endTime(Long endTime) {
    this.endTime = endTime;
    return this;
  }

   /**
   * Get endTime
   * @return endTime
  **/
  @ApiModelProperty(value = "")
  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  public ReviewInst escalation(Integer escalation) {
    this.escalation = escalation;
    return this;
  }

   /**
   * Get escalation
   * @return escalation
  **/
  @ApiModelProperty(value = "")
  public Integer getEscalation() {
    return escalation;
  }

  public void setEscalation(Integer escalation) {
    this.escalation = escalation;
  }

  public ReviewInst escalationMs(Long escalationMs) {
    this.escalationMs = escalationMs;
    return this;
  }

   /**
   * Get escalationMs
   * @return escalationMs
  **/
  @ApiModelProperty(value = "")
  public Long getEscalationMs() {
    return escalationMs;
  }

  public void setEscalationMs(Long escalationMs) {
    this.escalationMs = escalationMs;
  }

  public ReviewInst escalationOwners(List<Reviewer> escalationOwners) {
    this.escalationOwners = escalationOwners;
    return this;
  }

  public ReviewInst addEscalationOwnersItem(Reviewer escalationOwnersItem) {
    if (this.escalationOwners == null) {
      this.escalationOwners = new ArrayList<Reviewer>();
    }
    this.escalationOwners.add(escalationOwnersItem);
    return this;
  }

   /**
   * Get escalationOwners
   * @return escalationOwners
  **/
  @ApiModelProperty(value = "")
  public List<Reviewer> getEscalationOwners() {
    return escalationOwners;
  }

  public void setEscalationOwners(List<Reviewer> escalationOwners) {
    this.escalationOwners = escalationOwners;
  }

  public ReviewInst escalationUnit(EscalationUnitEnum escalationUnit) {
    this.escalationUnit = escalationUnit;
    return this;
  }

   /**
   * Get escalationUnit
   * @return escalationUnit
  **/
  @ApiModelProperty(value = "")
  public EscalationUnitEnum getEscalationUnit() {
    return escalationUnit;
  }

  public void setEscalationUnit(EscalationUnitEnum escalationUnit) {
    this.escalationUnit = escalationUnit;
  }

  public ReviewInst excludeAuthorized(Boolean excludeAuthorized) {
    this.excludeAuthorized = excludeAuthorized;
    return this;
  }

   /**
   * Get excludeAuthorized
   * @return excludeAuthorized
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludeAuthorized() {
    return excludeAuthorized;
  }

  public void setExcludeAuthorized(Boolean excludeAuthorized) {
    this.excludeAuthorized = excludeAuthorized;
  }

  public ReviewInst excludeNoDirectReports(Boolean excludeNoDirectReports) {
    this.excludeNoDirectReports = excludeNoDirectReports;
    return this;
  }

   /**
   * Get excludeNoDirectReports
   * @return excludeNoDirectReports
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludeNoDirectReports() {
    return excludeNoDirectReports;
  }

  public void setExcludeNoDirectReports(Boolean excludeNoDirectReports) {
    this.excludeNoDirectReports = excludeNoDirectReports;
  }

  public ReviewInst excludeNotAuthorized(Boolean excludeNotAuthorized) {
    this.excludeNotAuthorized = excludeNotAuthorized;
    return this;
  }

   /**
   * Get excludeNotAuthorized
   * @return excludeNotAuthorized
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludeNotAuthorized() {
    return excludeNotAuthorized;
  }

  public void setExcludeNotAuthorized(Boolean excludeNotAuthorized) {
    this.excludeNotAuthorized = excludeNotAuthorized;
  }

  public ReviewInst expectedEndTime(Long expectedEndTime) {
    this.expectedEndTime = expectedEndTime;
    return this;
  }

   /**
   * Get expectedEndTime
   * @return expectedEndTime
  **/
  @ApiModelProperty(value = "")
  public Long getExpectedEndTime() {
    return expectedEndTime;
  }

  public void setExpectedEndTime(Long expectedEndTime) {
    this.expectedEndTime = expectedEndTime;
  }

  public ReviewInst expirationPolicy(ExpirationPolicyEnum expirationPolicy) {
    this.expirationPolicy = expirationPolicy;
    return this;
  }

   /**
   * Get expirationPolicy
   * @return expirationPolicy
  **/
  @ApiModelProperty(value = "")
  public ExpirationPolicyEnum getExpirationPolicy() {
    return expirationPolicy;
  }

  public void setExpirationPolicy(ExpirationPolicyEnum expirationPolicy) {
    this.expirationPolicy = expirationPolicy;
  }

  public ReviewInst fulfillmentItems(List<FulfillmentStatus> fulfillmentItems) {
    this.fulfillmentItems = fulfillmentItems;
    return this;
  }

  public ReviewInst addFulfillmentItemsItem(FulfillmentStatus fulfillmentItemsItem) {
    if (this.fulfillmentItems == null) {
      this.fulfillmentItems = new ArrayList<FulfillmentStatus>();
    }
    this.fulfillmentItems.add(fulfillmentItemsItem);
    return this;
  }

   /**
   * Get fulfillmentItems
   * @return fulfillmentItems
  **/
  @ApiModelProperty(value = "")
  public List<FulfillmentStatus> getFulfillmentItems() {
    return fulfillmentItems;
  }

  public void setFulfillmentItems(List<FulfillmentStatus> fulfillmentItems) {
    this.fulfillmentItems = fulfillmentItems;
  }

  public ReviewInst groupInIDMRoles(Boolean groupInIDMRoles) {
    this.groupInIDMRoles = groupInIDMRoles;
    return this;
  }

   /**
   * Get groupInIDMRoles
   * @return groupInIDMRoles
  **/
  @ApiModelProperty(value = "")
  public Boolean isGroupInIDMRoles() {
    return groupInIDMRoles;
  }

  public void setGroupInIDMRoles(Boolean groupInIDMRoles) {
    this.groupInIDMRoles = groupInIDMRoles;
  }

  public ReviewInst hideBulkActions(List<HideBulkActionsEnum> hideBulkActions) {
    this.hideBulkActions = hideBulkActions;
    return this;
  }

  public ReviewInst addHideBulkActionsItem(HideBulkActionsEnum hideBulkActionsItem) {
    if (this.hideBulkActions == null) {
      this.hideBulkActions = new ArrayList<HideBulkActionsEnum>();
    }
    this.hideBulkActions.add(hideBulkActionsItem);
    return this;
  }

   /**
   * Get hideBulkActions
   * @return hideBulkActions
  **/
  @ApiModelProperty(value = "")
  public List<HideBulkActionsEnum> getHideBulkActions() {
    return hideBulkActions;
  }

  public void setHideBulkActions(List<HideBulkActionsEnum> hideBulkActions) {
    this.hideBulkActions = hideBulkActions;
  }

  public ReviewInst hideReassign(Boolean hideReassign) {
    this.hideReassign = hideReassign;
    return this;
  }

   /**
   * Get hideReassign
   * @return hideReassign
  **/
  @ApiModelProperty(value = "")
  public Boolean isHideReassign() {
    return hideReassign;
  }

  public void setHideReassign(Boolean hideReassign) {
    this.hideReassign = hideReassign;
  }

  public ReviewInst inProgressCount(Integer inProgressCount) {
    this.inProgressCount = inProgressCount;
    return this;
  }

   /**
   * Get inProgressCount
   * @return inProgressCount
  **/
  @ApiModelProperty(value = "")
  public Integer getInProgressCount() {
    return inProgressCount;
  }

  public void setInProgressCount(Integer inProgressCount) {
    this.inProgressCount = inProgressCount;
  }

  public ReviewInst inProgressPendingCount(Integer inProgressPendingCount) {
    this.inProgressPendingCount = inProgressPendingCount;
    return this;
  }

   /**
   * Get inProgressPendingCount
   * @return inProgressPendingCount
  **/
  @ApiModelProperty(value = "")
  public Integer getInProgressPendingCount() {
    return inProgressPendingCount;
  }

  public void setInProgressPendingCount(Integer inProgressPendingCount) {
    this.inProgressPendingCount = inProgressPendingCount;
  }

  public ReviewInst includeAccounts(Boolean includeAccounts) {
    this.includeAccounts = includeAccounts;
    return this;
  }

   /**
   * Get includeAccounts
   * @return includeAccounts
  **/
  @ApiModelProperty(value = "")
  public Boolean isIncludeAccounts() {
    return includeAccounts;
  }

  public void setIncludeAccounts(Boolean includeAccounts) {
    this.includeAccounts = includeAccounts;
  }

  public ReviewInst includePermissions(Boolean includePermissions) {
    this.includePermissions = includePermissions;
    return this;
  }

   /**
   * Get includePermissions
   * @return includePermissions
  **/
  @ApiModelProperty(value = "")
  public Boolean isIncludePermissions() {
    return includePermissions;
  }

  public void setIncludePermissions(Boolean includePermissions) {
    this.includePermissions = includePermissions;
  }

  public ReviewInst instReviewers(List<ReviewItem> instReviewers) {
    this.instReviewers = instReviewers;
    return this;
  }

  public ReviewInst addInstReviewersItem(ReviewItem instReviewersItem) {
    if (this.instReviewers == null) {
      this.instReviewers = new ArrayList<ReviewItem>();
    }
    this.instReviewers.add(instReviewersItem);
    return this;
  }

   /**
   * Get instReviewers
   * @return instReviewers
  **/
  @ApiModelProperty(value = "")
  public List<ReviewItem> getInstReviewers() {
    return instReviewers;
  }

  public void setInstReviewers(List<ReviewItem> instReviewers) {
    this.instReviewers = instReviewers;
  }

  public ReviewInst instructions(String instructions) {
    this.instructions = instructions;
    return this;
  }

   /**
   * Get instructions
   * @return instructions
  **/
  @ApiModelProperty(value = "")
  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public ReviewInst itemApproveCount(Integer itemApproveCount) {
    this.itemApproveCount = itemApproveCount;
    return this;
  }

   /**
   * Get itemApproveCount
   * @return itemApproveCount
  **/
  @ApiModelProperty(value = "")
  public Integer getItemApproveCount() {
    return itemApproveCount;
  }

  public void setItemApproveCount(Integer itemApproveCount) {
    this.itemApproveCount = itemApproveCount;
  }

  public ReviewInst itemCompleteCount(Integer itemCompleteCount) {
    this.itemCompleteCount = itemCompleteCount;
    return this;
  }

   /**
   * Get itemCompleteCount
   * @return itemCompleteCount
  **/
  @ApiModelProperty(value = "")
  public Integer getItemCompleteCount() {
    return itemCompleteCount;
  }

  public void setItemCompleteCount(Integer itemCompleteCount) {
    this.itemCompleteCount = itemCompleteCount;
  }

  public ReviewInst itemCount(Integer itemCount) {
    this.itemCount = itemCount;
    return this;
  }

   /**
   * Get itemCount
   * @return itemCount
  **/
  @ApiModelProperty(value = "")
  public Integer getItemCount() {
    return itemCount;
  }

  public void setItemCount(Integer itemCount) {
    this.itemCount = itemCount;
  }

  public ReviewInst items(List<ReviewItem> items) {
    this.items = items;
    return this;
  }

  public ReviewInst addItemsItem(ReviewItem itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<ReviewItem>();
    }
    this.items.add(itemsItem);
    return this;
  }

   /**
   * Get items
   * @return items
  **/
  @ApiModelProperty(value = "")
  public List<ReviewItem> getItems() {
    return items;
  }

  public void setItems(List<ReviewItem> items) {
    this.items = items;
  }

  public ReviewInst lastStatusChange(Long lastStatusChange) {
    this.lastStatusChange = lastStatusChange;
    return this;
  }

   /**
   * Get lastStatusChange
   * @return lastStatusChange
  **/
  @ApiModelProperty(value = "")
  public Long getLastStatusChange() {
    return lastStatusChange;
  }

  public void setLastStatusChange(Long lastStatusChange) {
    this.lastStatusChange = lastStatusChange;
  }

  public ReviewInst link(String link) {
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

  public ReviewInst linkDefinition(String linkDefinition) {
    this.linkDefinition = linkDefinition;
    return this;
  }

   /**
   * Get linkDefinition
   * @return linkDefinition
  **/
  @ApiModelProperty(value = "")
  public String getLinkDefinition() {
    return linkDefinition;
  }

  public void setLinkDefinition(String linkDefinition) {
    this.linkDefinition = linkDefinition;
  }

  public ReviewInst linkInstance(String linkInstance) {
    this.linkInstance = linkInstance;
    return this;
  }

   /**
   * Get linkInstance
   * @return linkInstance
  **/
  @ApiModelProperty(value = "")
  public String getLinkInstance() {
    return linkInstance;
  }

  public void setLinkInstance(String linkInstance) {
    this.linkInstance = linkInstance;
  }

  public ReviewInst linkOwner(String linkOwner) {
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

  public ReviewInst linkReassign(String linkReassign) {
    this.linkReassign = linkReassign;
    return this;
  }

   /**
   * Get linkReassign
   * @return linkReassign
  **/
  @ApiModelProperty(value = "")
  public String getLinkReassign() {
    return linkReassign;
  }

  public void setLinkReassign(String linkReassign) {
    this.linkReassign = linkReassign;
  }

  public ReviewInst materializedViewCreated(Boolean materializedViewCreated) {
    this.materializedViewCreated = materializedViewCreated;
    return this;
  }

   /**
   * Get materializedViewCreated
   * @return materializedViewCreated
  **/
  @ApiModelProperty(value = "")
  public Boolean isMaterializedViewCreated() {
    return materializedViewCreated;
  }

  public void setMaterializedViewCreated(Boolean materializedViewCreated) {
    this.materializedViewCreated = materializedViewCreated;
  }

  public ReviewInst name(String name) {
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

  public ReviewInst ownerName(String ownerName) {
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

  public ReviewInst owners(List<Reviewer> owners) {
    this.owners = owners;
    return this;
  }

  public ReviewInst addOwnersItem(Reviewer ownersItem) {
    if (this.owners == null) {
      this.owners = new ArrayList<Reviewer>();
    }
    this.owners.add(ownersItem);
    return this;
  }

   /**
   * Get owners
   * @return owners
  **/
  @ApiModelProperty(value = "")
  public List<Reviewer> getOwners() {
    return owners;
  }

  public void setOwners(List<Reviewer> owners) {
    this.owners = owners;
  }

  public ReviewInst partialApprovalPolicy(PartialApprovalPolicyEnum partialApprovalPolicy) {
    this.partialApprovalPolicy = partialApprovalPolicy;
    return this;
  }

   /**
   * Get partialApprovalPolicy
   * @return partialApprovalPolicy
  **/
  @ApiModelProperty(value = "")
  public PartialApprovalPolicyEnum getPartialApprovalPolicy() {
    return partialApprovalPolicy;
  }

  public void setPartialApprovalPolicy(PartialApprovalPolicyEnum partialApprovalPolicy) {
    this.partialApprovalPolicy = partialApprovalPolicy;
  }

  public ReviewInst pendingCount(Integer pendingCount) {
    this.pendingCount = pendingCount;
    return this;
  }

   /**
   * Get pendingCount
   * @return pendingCount
  **/
  @ApiModelProperty(value = "")
  public Integer getPendingCount() {
    return pendingCount;
  }

  public void setPendingCount(Integer pendingCount) {
    this.pendingCount = pendingCount;
  }

  public ReviewInst percentComplete(Integer percentComplete) {
    this.percentComplete = percentComplete;
    return this;
  }

   /**
   * Get percentComplete
   * @return percentComplete
  **/
  @ApiModelProperty(value = "")
  public Integer getPercentComplete() {
    return percentComplete;
  }

  public void setPercentComplete(Integer percentComplete) {
    this.percentComplete = percentComplete;
  }

  public ReviewInst previewDef(ReviewDef previewDef) {
    this.previewDef = previewDef;
    return this;
  }

   /**
   * Get previewDef
   * @return previewDef
  **/
  @ApiModelProperty(value = "")
  public ReviewDef getPreviewDef() {
    return previewDef;
  }

  public void setPreviewDef(ReviewDef previewDef) {
    this.previewDef = previewDef;
  }

  public ReviewInst previewDefId(Long previewDefId) {
    this.previewDefId = previewDefId;
    return this;
  }

   /**
   * Get previewDefId
   * @return previewDefId
  **/
  @ApiModelProperty(value = "")
  public Long getPreviewDefId() {
    return previewDefId;
  }

  public void setPreviewDefId(Long previewDefId) {
    this.previewDefId = previewDefId;
  }

  public ReviewInst reassignOnEscalation(Boolean reassignOnEscalation) {
    this.reassignOnEscalation = reassignOnEscalation;
    return this;
  }

   /**
   * Get reassignOnEscalation
   * @return reassignOnEscalation
  **/
  @ApiModelProperty(value = "")
  public Boolean isReassignOnEscalation() {
    return reassignOnEscalation;
  }

  public void setReassignOnEscalation(Boolean reassignOnEscalation) {
    this.reassignOnEscalation = reassignOnEscalation;
  }

  public ReviewInst removeAuditor(Boolean removeAuditor) {
    this.removeAuditor = removeAuditor;
    return this;
  }

   /**
   * Get removeAuditor
   * @return removeAuditor
  **/
  @ApiModelProperty(value = "")
  public Boolean isRemoveAuditor() {
    return removeAuditor;
  }

  public void setRemoveAuditor(Boolean removeAuditor) {
    this.removeAuditor = removeAuditor;
  }

  public ReviewInst requiredComments(List<RequiredCommentsEnum> requiredComments) {
    this.requiredComments = requiredComments;
    return this;
  }

  public ReviewInst addRequiredCommentsItem(RequiredCommentsEnum requiredCommentsItem) {
    if (this.requiredComments == null) {
      this.requiredComments = new ArrayList<RequiredCommentsEnum>();
    }
    this.requiredComments.add(requiredCommentsItem);
    return this;
  }

   /**
   * Get requiredComments
   * @return requiredComments
  **/
  @ApiModelProperty(value = "")
  public List<RequiredCommentsEnum> getRequiredComments() {
    return requiredComments;
  }

  public void setRequiredComments(List<RequiredCommentsEnum> requiredComments) {
    this.requiredComments = requiredComments;
  }

  public ReviewInst reviewDefId(Long reviewDefId) {
    this.reviewDefId = reviewDefId;
    return this;
  }

   /**
   * Get reviewDefId
   * @return reviewDefId
  **/
  @ApiModelProperty(value = "")
  public Long getReviewDefId() {
    return reviewDefId;
  }

  public void setReviewDefId(Long reviewDefId) {
    this.reviewDefId = reviewDefId;
  }

  public ReviewInst reviewInstId(Long reviewInstId) {
    this.reviewInstId = reviewInstId;
    return this;
  }

   /**
   * Get reviewInstId
   * @return reviewInstId
  **/
  @ApiModelProperty(value = "")
  public Long getReviewInstId() {
    return reviewInstId;
  }

  public void setReviewInstId(Long reviewInstId) {
    this.reviewInstId = reviewInstId;
  }

  public ReviewInst reviewType(ReviewTypeEnum reviewType) {
    this.reviewType = reviewType;
    return this;
  }

   /**
   * Get reviewType
   * @return reviewType
  **/
  @ApiModelProperty(value = "")
  public ReviewTypeEnum getReviewType() {
    return reviewType;
  }

  public void setReviewType(ReviewTypeEnum reviewType) {
    this.reviewType = reviewType;
  }

  public ReviewInst reviewer(ReviewItem reviewer) {
    this.reviewer = reviewer;
    return this;
  }

   /**
   * Get reviewer
   * @return reviewer
  **/
  @ApiModelProperty(value = "")
  public ReviewItem getReviewer() {
    return reviewer;
  }

  public void setReviewer(ReviewItem reviewer) {
    this.reviewer = reviewer;
  }

  public ReviewInst reviewerCertifyReadyCount(Integer reviewerCertifyReadyCount) {
    this.reviewerCertifyReadyCount = reviewerCertifyReadyCount;
    return this;
  }

   /**
   * Get reviewerCertifyReadyCount
   * @return reviewerCertifyReadyCount
  **/
  @ApiModelProperty(value = "")
  public Integer getReviewerCertifyReadyCount() {
    return reviewerCertifyReadyCount;
  }

  public void setReviewerCertifyReadyCount(Integer reviewerCertifyReadyCount) {
    this.reviewerCertifyReadyCount = reviewerCertifyReadyCount;
  }

  public ReviewInst reviewerCompleteCount(Integer reviewerCompleteCount) {
    this.reviewerCompleteCount = reviewerCompleteCount;
    return this;
  }

   /**
   * Get reviewerCompleteCount
   * @return reviewerCompleteCount
  **/
  @ApiModelProperty(value = "")
  public Integer getReviewerCompleteCount() {
    return reviewerCompleteCount;
  }

  public void setReviewerCompleteCount(Integer reviewerCompleteCount) {
    this.reviewerCompleteCount = reviewerCompleteCount;
  }

  public ReviewInst reviewerTaskCount(Integer reviewerTaskCount) {
    this.reviewerTaskCount = reviewerTaskCount;
    return this;
  }

   /**
   * Get reviewerTaskCount
   * @return reviewerTaskCount
  **/
  @ApiModelProperty(value = "")
  public Integer getReviewerTaskCount() {
    return reviewerTaskCount;
  }

  public void setReviewerTaskCount(Integer reviewerTaskCount) {
    this.reviewerTaskCount = reviewerTaskCount;
  }

  public ReviewInst schedule(Reviewer schedule) {
    this.schedule = schedule;
    return this;
  }

   /**
   * Get schedule
   * @return schedule
  **/
  @ApiModelProperty(value = "")
  public Reviewer getSchedule() {
    return schedule;
  }

  public void setSchedule(Reviewer schedule) {
    this.schedule = schedule;
  }

  public ReviewInst selfReviewPolicy(SelfReviewPolicyEnum selfReviewPolicy) {
    this.selfReviewPolicy = selfReviewPolicy;
    return this;
  }

   /**
   * Get selfReviewPolicy
   * @return selfReviewPolicy
  **/
  @ApiModelProperty(value = "")
  public SelfReviewPolicyEnum getSelfReviewPolicy() {
    return selfReviewPolicy;
  }

  public void setSelfReviewPolicy(SelfReviewPolicyEnum selfReviewPolicy) {
    this.selfReviewPolicy = selfReviewPolicy;
  }

  public ReviewInst showAcctPermissions(Boolean showAcctPermissions) {
    this.showAcctPermissions = showAcctPermissions;
    return this;
  }

   /**
   * Get showAcctPermissions
   * @return showAcctPermissions
  **/
  @ApiModelProperty(value = "")
  public Boolean isShowAcctPermissions() {
    return showAcctPermissions;
  }

  public void setShowAcctPermissions(Boolean showAcctPermissions) {
    this.showAcctPermissions = showAcctPermissions;
  }

  public ReviewInst showBRoleMembers(Boolean showBRoleMembers) {
    this.showBRoleMembers = showBRoleMembers;
    return this;
  }

   /**
   * Get showBRoleMembers
   * @return showBRoleMembers
  **/
  @ApiModelProperty(value = "")
  public Boolean isShowBRoleMembers() {
    return showBRoleMembers;
  }

  public void setShowBRoleMembers(Boolean showBRoleMembers) {
    this.showBRoleMembers = showBRoleMembers;
  }

  public ReviewInst showBRolePolicies(Boolean showBRolePolicies) {
    this.showBRolePolicies = showBRolePolicies;
    return this;
  }

   /**
   * Get showBRolePolicies
   * @return showBRolePolicies
  **/
  @ApiModelProperty(value = "")
  public Boolean isShowBRolePolicies() {
    return showBRolePolicies;
  }

  public void setShowBRolePolicies(Boolean showBRolePolicies) {
    this.showBRolePolicies = showBRolePolicies;
  }

  public ReviewInst showRolePermissions(Boolean showRolePermissions) {
    this.showRolePermissions = showRolePermissions;
    return this;
  }

   /**
   * Get showRolePermissions
   * @return showRolePermissions
  **/
  @ApiModelProperty(value = "")
  public Boolean isShowRolePermissions() {
    return showRolePermissions;
  }

  public void setShowRolePermissions(Boolean showRolePermissions) {
    this.showRolePermissions = showRolePermissions;
  }

  public ReviewInst startMessage(String startMessage) {
    this.startMessage = startMessage;
    return this;
  }

   /**
   * Get startMessage
   * @return startMessage
  **/
  @ApiModelProperty(value = "")
  public String getStartMessage() {
    return startMessage;
  }

  public void setStartMessage(String startMessage) {
    this.startMessage = startMessage;
  }

  public ReviewInst startState(StartStateEnum startState) {
    this.startState = startState;
    return this;
  }

   /**
   * Get startState
   * @return startState
  **/
  @ApiModelProperty(value = "")
  public StartStateEnum getStartState() {
    return startState;
  }

  public void setStartState(StartStateEnum startState) {
    this.startState = startState;
  }

  public ReviewInst startStatus(StartStatusEnum startStatus) {
    this.startStatus = startStatus;
    return this;
  }

   /**
   * Get startStatus
   * @return startStatus
  **/
  @ApiModelProperty(value = "")
  public StartStatusEnum getStartStatus() {
    return startStatus;
  }

  public void setStartStatus(StartStatusEnum startStatus) {
    this.startStatus = startStatus;
  }

  public ReviewInst startTime(Long startTime) {
    this.startTime = startTime;
    return this;
  }

   /**
   * Get startTime
   * @return startTime
  **/
  @ApiModelProperty(value = "")
  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public ReviewInst startedBy(StartedByEnum startedBy) {
    this.startedBy = startedBy;
    return this;
  }

   /**
   * Get startedBy
   * @return startedBy
  **/
  @ApiModelProperty(value = "")
  public StartedByEnum getStartedBy() {
    return startedBy;
  }

  public void setStartedBy(StartedByEnum startedBy) {
    this.startedBy = startedBy;
  }

  public ReviewInst startedByUser(Reviewer startedByUser) {
    this.startedByUser = startedByUser;
    return this;
  }

   /**
   * Get startedByUser
   * @return startedByUser
  **/
  @ApiModelProperty(value = "")
  public Reviewer getStartedByUser() {
    return startedByUser;
  }

  public void setStartedByUser(Reviewer startedByUser) {
    this.startedByUser = startedByUser;
  }

  public ReviewInst status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ReviewInst stoppable(Boolean stoppable) {
    this.stoppable = stoppable;
    return this;
  }

   /**
   * Get stoppable
   * @return stoppable
  **/
  @ApiModelProperty(value = "")
  public Boolean isStoppable() {
    return stoppable;
  }

  public void setStoppable(Boolean stoppable) {
    this.stoppable = stoppable;
  }

  public ReviewInst taskCompleteCount(Integer taskCompleteCount) {
    this.taskCompleteCount = taskCompleteCount;
    return this;
  }

   /**
   * Get taskCompleteCount
   * @return taskCompleteCount
  **/
  @ApiModelProperty(value = "")
  public Integer getTaskCompleteCount() {
    return taskCompleteCount;
  }

  public void setTaskCompleteCount(Integer taskCompleteCount) {
    this.taskCompleteCount = taskCompleteCount;
  }

  public ReviewInst taskCount(Integer taskCount) {
    this.taskCount = taskCount;
    return this;
  }

   /**
   * Get taskCount
   * @return taskCount
  **/
  @ApiModelProperty(value = "")
  public Integer getTaskCount() {
    return taskCount;
  }

  public void setTaskCount(Integer taskCount) {
    this.taskCount = taskCount;
  }

  public ReviewInst taskGroupAttrKey(String taskGroupAttrKey) {
    this.taskGroupAttrKey = taskGroupAttrKey;
    return this;
  }

   /**
   * Get taskGroupAttrKey
   * @return taskGroupAttrKey
  **/
  @ApiModelProperty(value = "")
  public String getTaskGroupAttrKey() {
    return taskGroupAttrKey;
  }

  public void setTaskGroupAttrKey(String taskGroupAttrKey) {
    this.taskGroupAttrKey = taskGroupAttrKey;
  }

  public ReviewInst taskSortAttrKey(String taskSortAttrKey) {
    this.taskSortAttrKey = taskSortAttrKey;
    return this;
  }

   /**
   * Get taskSortAttrKey
   * @return taskSortAttrKey
  **/
  @ApiModelProperty(value = "")
  public String getTaskSortAttrKey() {
    return taskSortAttrKey;
  }

  public void setTaskSortAttrKey(String taskSortAttrKey) {
    this.taskSortAttrKey = taskSortAttrKey;
  }

  public ReviewInst taskType(String taskType) {
    this.taskType = taskType;
    return this;
  }

   /**
   * Get taskType
   * @return taskType
  **/
  @ApiModelProperty(value = "")
  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  public ReviewInst totalSearch(Integer totalSearch) {
    this.totalSearch = totalSearch;
    return this;
  }

   /**
   * Get totalSearch
   * @return totalSearch
  **/
  @ApiModelProperty(value = "")
  public Integer getTotalSearch() {
    return totalSearch;
  }

  public void setTotalSearch(Integer totalSearch) {
    this.totalSearch = totalSearch;
  }

  public ReviewInst uniqueReviewDefId(String uniqueReviewDefId) {
    this.uniqueReviewDefId = uniqueReviewDefId;
    return this;
  }

   /**
   * Get uniqueReviewDefId
   * @return uniqueReviewDefId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueReviewDefId() {
    return uniqueReviewDefId;
  }

  public void setUniqueReviewDefId(String uniqueReviewDefId) {
    this.uniqueReviewDefId = uniqueReviewDefId;
  }

  public ReviewInst useMaterializedView(Boolean useMaterializedView) {
    this.useMaterializedView = useMaterializedView;
    return this;
  }

   /**
   * Get useMaterializedView
   * @return useMaterializedView
  **/
  @ApiModelProperty(value = "")
  public Boolean isUseMaterializedView() {
    return useMaterializedView;
  }

  public void setUseMaterializedView(Boolean useMaterializedView) {
    this.useMaterializedView = useMaterializedView;
  }

  public ReviewInst validToTime(Long validToTime) {
    this.validToTime = validToTime;
    return this;
  }

   /**
   * Get validToTime
   * @return validToTime
  **/
  @ApiModelProperty(value = "")
  public Long getValidToTime() {
    return validToTime;
  }

  public void setValidToTime(Long validToTime) {
    this.validToTime = validToTime;
  }

  public ReviewInst wfSyncFailed(Boolean wfSyncFailed) {
    this.wfSyncFailed = wfSyncFailed;
    return this;
  }

   /**
   * Get wfSyncFailed
   * @return wfSyncFailed
  **/
  @ApiModelProperty(value = "")
  public Boolean isWfSyncFailed() {
    return wfSyncFailed;
  }

  public void setWfSyncFailed(Boolean wfSyncFailed) {
    this.wfSyncFailed = wfSyncFailed;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewInst reviewInst = (ReviewInst) o;
    return Objects.equals(this.allowModifyAccess, reviewInst.allowModifyAccess) &&
        Objects.equals(this.allowOverrides, reviewInst.allowOverrides) &&
        Objects.equals(this.approvalTasks, reviewInst.approvalTasks) &&
        Objects.equals(this.approvedBy, reviewInst.approvedBy) &&
        Objects.equals(this.approvedByPolicy, reviewInst.approvedByPolicy) &&
        Objects.equals(this.assignedRolesOnly, reviewInst.assignedRolesOnly) &&
        Objects.equals(this.attributes, reviewInst.attributes) &&
        Objects.equals(this.auditor, reviewInst.auditor) &&
        Objects.equals(this.auditorComment, reviewInst.auditorComment) &&
        Objects.equals(this.canceledBy, reviewInst.canceledBy) &&
        Objects.equals(this.certifiedCount, reviewInst.certifiedCount) &&
        Objects.equals(this.comment, reviewInst.comment) &&
        Objects.equals(this.count, reviewInst.count) &&
        Objects.equals(this.description, reviewInst.description) &&
        Objects.equals(this.editable, reviewInst.editable) &&
        Objects.equals(this.endTime, reviewInst.endTime) &&
        Objects.equals(this.escalation, reviewInst.escalation) &&
        Objects.equals(this.escalationMs, reviewInst.escalationMs) &&
        Objects.equals(this.escalationOwners, reviewInst.escalationOwners) &&
        Objects.equals(this.escalationUnit, reviewInst.escalationUnit) &&
        Objects.equals(this.excludeAuthorized, reviewInst.excludeAuthorized) &&
        Objects.equals(this.excludeNoDirectReports, reviewInst.excludeNoDirectReports) &&
        Objects.equals(this.excludeNotAuthorized, reviewInst.excludeNotAuthorized) &&
        Objects.equals(this.expectedEndTime, reviewInst.expectedEndTime) &&
        Objects.equals(this.expirationPolicy, reviewInst.expirationPolicy) &&
        Objects.equals(this.fulfillmentItems, reviewInst.fulfillmentItems) &&
        Objects.equals(this.groupInIDMRoles, reviewInst.groupInIDMRoles) &&
        Objects.equals(this.hideBulkActions, reviewInst.hideBulkActions) &&
        Objects.equals(this.hideReassign, reviewInst.hideReassign) &&
        Objects.equals(this.inProgressCount, reviewInst.inProgressCount) &&
        Objects.equals(this.inProgressPendingCount, reviewInst.inProgressPendingCount) &&
        Objects.equals(this.includeAccounts, reviewInst.includeAccounts) &&
        Objects.equals(this.includePermissions, reviewInst.includePermissions) &&
        Objects.equals(this.instReviewers, reviewInst.instReviewers) &&
        Objects.equals(this.instructions, reviewInst.instructions) &&
        Objects.equals(this.itemApproveCount, reviewInst.itemApproveCount) &&
        Objects.equals(this.itemCompleteCount, reviewInst.itemCompleteCount) &&
        Objects.equals(this.itemCount, reviewInst.itemCount) &&
        Objects.equals(this.items, reviewInst.items) &&
        Objects.equals(this.lastStatusChange, reviewInst.lastStatusChange) &&
        Objects.equals(this.link, reviewInst.link) &&
        Objects.equals(this.linkDefinition, reviewInst.linkDefinition) &&
        Objects.equals(this.linkInstance, reviewInst.linkInstance) &&
        Objects.equals(this.linkOwner, reviewInst.linkOwner) &&
        Objects.equals(this.linkReassign, reviewInst.linkReassign) &&
        Objects.equals(this.materializedViewCreated, reviewInst.materializedViewCreated) &&
        Objects.equals(this.name, reviewInst.name) &&
        Objects.equals(this.ownerName, reviewInst.ownerName) &&
        Objects.equals(this.owners, reviewInst.owners) &&
        Objects.equals(this.partialApprovalPolicy, reviewInst.partialApprovalPolicy) &&
        Objects.equals(this.pendingCount, reviewInst.pendingCount) &&
        Objects.equals(this.percentComplete, reviewInst.percentComplete) &&
        Objects.equals(this.previewDef, reviewInst.previewDef) &&
        Objects.equals(this.previewDefId, reviewInst.previewDefId) &&
        Objects.equals(this.reassignOnEscalation, reviewInst.reassignOnEscalation) &&
        Objects.equals(this.removeAuditor, reviewInst.removeAuditor) &&
        Objects.equals(this.requiredComments, reviewInst.requiredComments) &&
        Objects.equals(this.reviewDefId, reviewInst.reviewDefId) &&
        Objects.equals(this.reviewInstId, reviewInst.reviewInstId) &&
        Objects.equals(this.reviewType, reviewInst.reviewType) &&
        Objects.equals(this.reviewer, reviewInst.reviewer) &&
        Objects.equals(this.reviewerCertifyReadyCount, reviewInst.reviewerCertifyReadyCount) &&
        Objects.equals(this.reviewerCompleteCount, reviewInst.reviewerCompleteCount) &&
        Objects.equals(this.reviewerTaskCount, reviewInst.reviewerTaskCount) &&
        Objects.equals(this.schedule, reviewInst.schedule) &&
        Objects.equals(this.selfReviewPolicy, reviewInst.selfReviewPolicy) &&
        Objects.equals(this.showAcctPermissions, reviewInst.showAcctPermissions) &&
        Objects.equals(this.showBRoleMembers, reviewInst.showBRoleMembers) &&
        Objects.equals(this.showBRolePolicies, reviewInst.showBRolePolicies) &&
        Objects.equals(this.showRolePermissions, reviewInst.showRolePermissions) &&
        Objects.equals(this.startMessage, reviewInst.startMessage) &&
        Objects.equals(this.startState, reviewInst.startState) &&
        Objects.equals(this.startStatus, reviewInst.startStatus) &&
        Objects.equals(this.startTime, reviewInst.startTime) &&
        Objects.equals(this.startedBy, reviewInst.startedBy) &&
        Objects.equals(this.startedByUser, reviewInst.startedByUser) &&
        Objects.equals(this.status, reviewInst.status) &&
        Objects.equals(this.stoppable, reviewInst.stoppable) &&
        Objects.equals(this.taskCompleteCount, reviewInst.taskCompleteCount) &&
        Objects.equals(this.taskCount, reviewInst.taskCount) &&
        Objects.equals(this.taskGroupAttrKey, reviewInst.taskGroupAttrKey) &&
        Objects.equals(this.taskSortAttrKey, reviewInst.taskSortAttrKey) &&
        Objects.equals(this.taskType, reviewInst.taskType) &&
        Objects.equals(this.totalSearch, reviewInst.totalSearch) &&
        Objects.equals(this.uniqueReviewDefId, reviewInst.uniqueReviewDefId) &&
        Objects.equals(this.useMaterializedView, reviewInst.useMaterializedView) &&
        Objects.equals(this.validToTime, reviewInst.validToTime) &&
        Objects.equals(this.wfSyncFailed, reviewInst.wfSyncFailed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowModifyAccess, allowOverrides, approvalTasks, approvedBy, approvedByPolicy, assignedRolesOnly, attributes, auditor, auditorComment, canceledBy, certifiedCount, comment, count, description, editable, endTime, escalation, escalationMs, escalationOwners, escalationUnit, excludeAuthorized, excludeNoDirectReports, excludeNotAuthorized, expectedEndTime, expirationPolicy, fulfillmentItems, groupInIDMRoles, hideBulkActions, hideReassign, inProgressCount, inProgressPendingCount, includeAccounts, includePermissions, instReviewers, instructions, itemApproveCount, itemCompleteCount, itemCount, items, lastStatusChange, link, linkDefinition, linkInstance, linkOwner, linkReassign, materializedViewCreated, name, ownerName, owners, partialApprovalPolicy, pendingCount, percentComplete, previewDef, previewDefId, reassignOnEscalation, removeAuditor, requiredComments, reviewDefId, reviewInstId, reviewType, reviewer, reviewerCertifyReadyCount, reviewerCompleteCount, reviewerTaskCount, schedule, selfReviewPolicy, showAcctPermissions, showBRoleMembers, showBRolePolicies, showRolePermissions, startMessage, startState, startStatus, startTime, startedBy, startedByUser, status, stoppable, taskCompleteCount, taskCount, taskGroupAttrKey, taskSortAttrKey, taskType, totalSearch, uniqueReviewDefId, useMaterializedView, validToTime, wfSyncFailed);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewInst {\n");
    
    sb.append("    allowModifyAccess: ").append(toIndentedString(allowModifyAccess)).append("\n");
    sb.append("    allowOverrides: ").append(toIndentedString(allowOverrides)).append("\n");
    sb.append("    approvalTasks: ").append(toIndentedString(approvalTasks)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    approvedByPolicy: ").append(toIndentedString(approvedByPolicy)).append("\n");
    sb.append("    assignedRolesOnly: ").append(toIndentedString(assignedRolesOnly)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    auditor: ").append(toIndentedString(auditor)).append("\n");
    sb.append("    auditorComment: ").append(toIndentedString(auditorComment)).append("\n");
    sb.append("    canceledBy: ").append(toIndentedString(canceledBy)).append("\n");
    sb.append("    certifiedCount: ").append(toIndentedString(certifiedCount)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    escalation: ").append(toIndentedString(escalation)).append("\n");
    sb.append("    escalationMs: ").append(toIndentedString(escalationMs)).append("\n");
    sb.append("    escalationOwners: ").append(toIndentedString(escalationOwners)).append("\n");
    sb.append("    escalationUnit: ").append(toIndentedString(escalationUnit)).append("\n");
    sb.append("    excludeAuthorized: ").append(toIndentedString(excludeAuthorized)).append("\n");
    sb.append("    excludeNoDirectReports: ").append(toIndentedString(excludeNoDirectReports)).append("\n");
    sb.append("    excludeNotAuthorized: ").append(toIndentedString(excludeNotAuthorized)).append("\n");
    sb.append("    expectedEndTime: ").append(toIndentedString(expectedEndTime)).append("\n");
    sb.append("    expirationPolicy: ").append(toIndentedString(expirationPolicy)).append("\n");
    sb.append("    fulfillmentItems: ").append(toIndentedString(fulfillmentItems)).append("\n");
    sb.append("    groupInIDMRoles: ").append(toIndentedString(groupInIDMRoles)).append("\n");
    sb.append("    hideBulkActions: ").append(toIndentedString(hideBulkActions)).append("\n");
    sb.append("    hideReassign: ").append(toIndentedString(hideReassign)).append("\n");
    sb.append("    inProgressCount: ").append(toIndentedString(inProgressCount)).append("\n");
    sb.append("    inProgressPendingCount: ").append(toIndentedString(inProgressPendingCount)).append("\n");
    sb.append("    includeAccounts: ").append(toIndentedString(includeAccounts)).append("\n");
    sb.append("    includePermissions: ").append(toIndentedString(includePermissions)).append("\n");
    sb.append("    instReviewers: ").append(toIndentedString(instReviewers)).append("\n");
    sb.append("    instructions: ").append(toIndentedString(instructions)).append("\n");
    sb.append("    itemApproveCount: ").append(toIndentedString(itemApproveCount)).append("\n");
    sb.append("    itemCompleteCount: ").append(toIndentedString(itemCompleteCount)).append("\n");
    sb.append("    itemCount: ").append(toIndentedString(itemCount)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    lastStatusChange: ").append(toIndentedString(lastStatusChange)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkDefinition: ").append(toIndentedString(linkDefinition)).append("\n");
    sb.append("    linkInstance: ").append(toIndentedString(linkInstance)).append("\n");
    sb.append("    linkOwner: ").append(toIndentedString(linkOwner)).append("\n");
    sb.append("    linkReassign: ").append(toIndentedString(linkReassign)).append("\n");
    sb.append("    materializedViewCreated: ").append(toIndentedString(materializedViewCreated)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    ownerName: ").append(toIndentedString(ownerName)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    partialApprovalPolicy: ").append(toIndentedString(partialApprovalPolicy)).append("\n");
    sb.append("    pendingCount: ").append(toIndentedString(pendingCount)).append("\n");
    sb.append("    percentComplete: ").append(toIndentedString(percentComplete)).append("\n");
    sb.append("    previewDef: ").append(toIndentedString(previewDef)).append("\n");
    sb.append("    previewDefId: ").append(toIndentedString(previewDefId)).append("\n");
    sb.append("    reassignOnEscalation: ").append(toIndentedString(reassignOnEscalation)).append("\n");
    sb.append("    removeAuditor: ").append(toIndentedString(removeAuditor)).append("\n");
    sb.append("    requiredComments: ").append(toIndentedString(requiredComments)).append("\n");
    sb.append("    reviewDefId: ").append(toIndentedString(reviewDefId)).append("\n");
    sb.append("    reviewInstId: ").append(toIndentedString(reviewInstId)).append("\n");
    sb.append("    reviewType: ").append(toIndentedString(reviewType)).append("\n");
    sb.append("    reviewer: ").append(toIndentedString(reviewer)).append("\n");
    sb.append("    reviewerCertifyReadyCount: ").append(toIndentedString(reviewerCertifyReadyCount)).append("\n");
    sb.append("    reviewerCompleteCount: ").append(toIndentedString(reviewerCompleteCount)).append("\n");
    sb.append("    reviewerTaskCount: ").append(toIndentedString(reviewerTaskCount)).append("\n");
    sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
    sb.append("    selfReviewPolicy: ").append(toIndentedString(selfReviewPolicy)).append("\n");
    sb.append("    showAcctPermissions: ").append(toIndentedString(showAcctPermissions)).append("\n");
    sb.append("    showBRoleMembers: ").append(toIndentedString(showBRoleMembers)).append("\n");
    sb.append("    showBRolePolicies: ").append(toIndentedString(showBRolePolicies)).append("\n");
    sb.append("    showRolePermissions: ").append(toIndentedString(showRolePermissions)).append("\n");
    sb.append("    startMessage: ").append(toIndentedString(startMessage)).append("\n");
    sb.append("    startState: ").append(toIndentedString(startState)).append("\n");
    sb.append("    startStatus: ").append(toIndentedString(startStatus)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    startedBy: ").append(toIndentedString(startedBy)).append("\n");
    sb.append("    startedByUser: ").append(toIndentedString(startedByUser)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    stoppable: ").append(toIndentedString(stoppable)).append("\n");
    sb.append("    taskCompleteCount: ").append(toIndentedString(taskCompleteCount)).append("\n");
    sb.append("    taskCount: ").append(toIndentedString(taskCount)).append("\n");
    sb.append("    taskGroupAttrKey: ").append(toIndentedString(taskGroupAttrKey)).append("\n");
    sb.append("    taskSortAttrKey: ").append(toIndentedString(taskSortAttrKey)).append("\n");
    sb.append("    taskType: ").append(toIndentedString(taskType)).append("\n");
    sb.append("    totalSearch: ").append(toIndentedString(totalSearch)).append("\n");
    sb.append("    uniqueReviewDefId: ").append(toIndentedString(uniqueReviewDefId)).append("\n");
    sb.append("    useMaterializedView: ").append(toIndentedString(useMaterializedView)).append("\n");
    sb.append("    validToTime: ").append(toIndentedString(validToTime)).append("\n");
    sb.append("    wfSyncFailed: ").append(toIndentedString(wfSyncFailed)).append("\n");
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
