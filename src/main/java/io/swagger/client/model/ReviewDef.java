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
import io.swagger.client.model.Attribute;
import io.swagger.client.model.Notifications;
import io.swagger.client.model.ReviewDataTargets;
import io.swagger.client.model.ReviewInst;
import io.swagger.client.model.Reviewer;
import io.swagger.client.model.ReviewingTargets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ReviewDef
 */



public class ReviewDef {
  @SerializedName("active")
  private Boolean active = null;

  @SerializedName("activeFromDate")
  private Long activeFromDate = null;

  @SerializedName("activeMicroCertCt")
  private Integer activeMicroCertCt = null;

  @SerializedName("activeToDate")
  private Long activeToDate = null;

  @SerializedName("allowModifyAccess")
  private Boolean allowModifyAccess = null;

  @SerializedName("allowOverride")
  private Boolean allowOverride = null;

  @SerializedName("approvedBy")
  private Reviewer approvedBy = null;

  @SerializedName("approvedByPolicy")
  private Boolean approvedByPolicy = null;

  @SerializedName("attributes")
  private List<Attribute> attributes = null;

  @SerializedName("auditor")
  private Reviewer auditor = null;

  @SerializedName("auditorComment")
  private String auditorComment = null;

  @SerializedName("canceledBy")
  private Reviewer canceledBy = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("editable")
  private Boolean editable = null;

  @SerializedName("escalation")
  private Integer escalation = null;

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

  @SerializedName("expirationExtension")
  private Integer expirationExtension = null;

  /**
   * Gets or Sets expirationExtensionUnit
   */
  @JsonAdapter(ExpirationExtensionUnitEnum.Adapter.class)
  public enum ExpirationExtensionUnitEnum {
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

    ExpirationExtensionUnitEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ExpirationExtensionUnitEnum fromValue(String input) {
      for (ExpirationExtensionUnitEnum b : ExpirationExtensionUnitEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ExpirationExtensionUnitEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ExpirationExtensionUnitEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ExpirationExtensionUnitEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ExpirationExtensionUnitEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("expirationExtensionUnit")
  private ExpirationExtensionUnitEnum expirationExtensionUnit = null;

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

  @SerializedName("instanceOwner")
  private Reviewer instanceOwner = null;

  @SerializedName("instructions")
  private String instructions = null;

  @SerializedName("itemApproveCount")
  private Integer itemApproveCount = null;

  @SerializedName("itemCompleteCount")
  private Integer itemCompleteCount = null;

  @SerializedName("itemCount")
  private Integer itemCount = null;

  @SerializedName("latestValidToTime")
  private Long latestValidToTime = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkInstance")
  private String linkInstance = null;

  @SerializedName("linkInstances")
  private String linkInstances = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("notifications")
  private List<Notifications> notifications = null;

  @SerializedName("overlayDefId")
  private Long overlayDefId = null;

  /**
   * Gets or Sets overlayDefType
   */
  @JsonAdapter(OverlayDefTypeEnum.Adapter.class)
  public enum OverlayDefTypeEnum {
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

    OverlayDefTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static OverlayDefTypeEnum fromValue(String input) {
      for (OverlayDefTypeEnum b : OverlayDefTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<OverlayDefTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final OverlayDefTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public OverlayDefTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return OverlayDefTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("overlayDefType")
  private OverlayDefTypeEnum overlayDefType = null;

  @SerializedName("owner")
  private Reviewer owner = null;

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

  @SerializedName("prd")
  private String prd = null;

  @SerializedName("previewDefId")
  private Long previewDefId = null;

  @SerializedName("reassignOnEscalation")
  private Boolean reassignOnEscalation = null;

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

  @SerializedName("reviewers")
  private List<Reviewer> reviewers = null;

  @SerializedName("reviewingTargetData")
  private ReviewDataTargets reviewingTargetData = null;

  @SerializedName("reviewingTargets")
  private ReviewingTargets reviewingTargets = null;

  @SerializedName("reviews")
  private List<ReviewInst> reviews = null;

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

  @SerializedName("startable")
  private Boolean startable = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("statusComment")
  private String statusComment = null;

  @SerializedName("taskGroupAttrKey")
  private String taskGroupAttrKey = null;

  @SerializedName("taskSortAttrKey")
  private String taskSortAttrKey = null;

  @SerializedName("uniqueReviewDefId")
  private String uniqueReviewDefId = null;

  @SerializedName("useMaterializedView")
  private Boolean useMaterializedView = null;

  @SerializedName("valid")
  private Integer valid = null;

  /**
   * Gets or Sets validUnit
   */
  @JsonAdapter(ValidUnitEnum.Adapter.class)
  public enum ValidUnitEnum {
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

    ValidUnitEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ValidUnitEnum fromValue(String input) {
      for (ValidUnitEnum b : ValidUnitEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ValidUnitEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ValidUnitEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ValidUnitEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ValidUnitEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("validUnit")
  private ValidUnitEnum validUnit = null;

  @SerializedName("version")
  private Long version = null;

  public ReviewDef active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @ApiModelProperty(value = "")
  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public ReviewDef activeFromDate(Long activeFromDate) {
    this.activeFromDate = activeFromDate;
    return this;
  }

   /**
   * Get activeFromDate
   * @return activeFromDate
  **/
  @ApiModelProperty(value = "")
  public Long getActiveFromDate() {
    return activeFromDate;
  }

  public void setActiveFromDate(Long activeFromDate) {
    this.activeFromDate = activeFromDate;
  }

  public ReviewDef activeMicroCertCt(Integer activeMicroCertCt) {
    this.activeMicroCertCt = activeMicroCertCt;
    return this;
  }

   /**
   * Get activeMicroCertCt
   * @return activeMicroCertCt
  **/
  @ApiModelProperty(value = "")
  public Integer getActiveMicroCertCt() {
    return activeMicroCertCt;
  }

  public void setActiveMicroCertCt(Integer activeMicroCertCt) {
    this.activeMicroCertCt = activeMicroCertCt;
  }

  public ReviewDef activeToDate(Long activeToDate) {
    this.activeToDate = activeToDate;
    return this;
  }

   /**
   * Get activeToDate
   * @return activeToDate
  **/
  @ApiModelProperty(value = "")
  public Long getActiveToDate() {
    return activeToDate;
  }

  public void setActiveToDate(Long activeToDate) {
    this.activeToDate = activeToDate;
  }

  public ReviewDef allowModifyAccess(Boolean allowModifyAccess) {
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

  public ReviewDef allowOverride(Boolean allowOverride) {
    this.allowOverride = allowOverride;
    return this;
  }

   /**
   * Get allowOverride
   * @return allowOverride
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowOverride() {
    return allowOverride;
  }

  public void setAllowOverride(Boolean allowOverride) {
    this.allowOverride = allowOverride;
  }

  public ReviewDef approvedBy(Reviewer approvedBy) {
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

  public ReviewDef approvedByPolicy(Boolean approvedByPolicy) {
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

  public ReviewDef attributes(List<Attribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public ReviewDef addAttributesItem(Attribute attributesItem) {
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

  public ReviewDef auditor(Reviewer auditor) {
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

  public ReviewDef auditorComment(String auditorComment) {
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

  public ReviewDef canceledBy(Reviewer canceledBy) {
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

  public ReviewDef deleted(Boolean deleted) {
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

  public ReviewDef description(String description) {
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

  public ReviewDef editable(Boolean editable) {
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

  public ReviewDef escalation(Integer escalation) {
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

  public ReviewDef escalationOwners(List<Reviewer> escalationOwners) {
    this.escalationOwners = escalationOwners;
    return this;
  }

  public ReviewDef addEscalationOwnersItem(Reviewer escalationOwnersItem) {
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

  public ReviewDef escalationUnit(EscalationUnitEnum escalationUnit) {
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

  public ReviewDef excludeAuthorized(Boolean excludeAuthorized) {
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

  public ReviewDef excludeNoDirectReports(Boolean excludeNoDirectReports) {
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

  public ReviewDef excludeNotAuthorized(Boolean excludeNotAuthorized) {
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

  public ReviewDef expirationExtension(Integer expirationExtension) {
    this.expirationExtension = expirationExtension;
    return this;
  }

   /**
   * Get expirationExtension
   * @return expirationExtension
  **/
  @ApiModelProperty(value = "")
  public Integer getExpirationExtension() {
    return expirationExtension;
  }

  public void setExpirationExtension(Integer expirationExtension) {
    this.expirationExtension = expirationExtension;
  }

  public ReviewDef expirationExtensionUnit(ExpirationExtensionUnitEnum expirationExtensionUnit) {
    this.expirationExtensionUnit = expirationExtensionUnit;
    return this;
  }

   /**
   * Get expirationExtensionUnit
   * @return expirationExtensionUnit
  **/
  @ApiModelProperty(value = "")
  public ExpirationExtensionUnitEnum getExpirationExtensionUnit() {
    return expirationExtensionUnit;
  }

  public void setExpirationExtensionUnit(ExpirationExtensionUnitEnum expirationExtensionUnit) {
    this.expirationExtensionUnit = expirationExtensionUnit;
  }

  public ReviewDef expirationPolicy(ExpirationPolicyEnum expirationPolicy) {
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

  public ReviewDef groupInIDMRoles(Boolean groupInIDMRoles) {
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

  public ReviewDef hideBulkActions(List<HideBulkActionsEnum> hideBulkActions) {
    this.hideBulkActions = hideBulkActions;
    return this;
  }

  public ReviewDef addHideBulkActionsItem(HideBulkActionsEnum hideBulkActionsItem) {
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

  public ReviewDef hideReassign(Boolean hideReassign) {
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

  public ReviewDef instanceOwner(Reviewer instanceOwner) {
    this.instanceOwner = instanceOwner;
    return this;
  }

   /**
   * Get instanceOwner
   * @return instanceOwner
  **/
  @ApiModelProperty(value = "")
  public Reviewer getInstanceOwner() {
    return instanceOwner;
  }

  public void setInstanceOwner(Reviewer instanceOwner) {
    this.instanceOwner = instanceOwner;
  }

  public ReviewDef instructions(String instructions) {
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

  public ReviewDef itemApproveCount(Integer itemApproveCount) {
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

  public ReviewDef itemCompleteCount(Integer itemCompleteCount) {
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

  public ReviewDef itemCount(Integer itemCount) {
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

  public ReviewDef latestValidToTime(Long latestValidToTime) {
    this.latestValidToTime = latestValidToTime;
    return this;
  }

   /**
   * Get latestValidToTime
   * @return latestValidToTime
  **/
  @ApiModelProperty(value = "")
  public Long getLatestValidToTime() {
    return latestValidToTime;
  }

  public void setLatestValidToTime(Long latestValidToTime) {
    this.latestValidToTime = latestValidToTime;
  }

  public ReviewDef link(String link) {
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

  public ReviewDef linkInstance(String linkInstance) {
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

  public ReviewDef linkInstances(String linkInstances) {
    this.linkInstances = linkInstances;
    return this;
  }

   /**
   * Get linkInstances
   * @return linkInstances
  **/
  @ApiModelProperty(value = "")
  public String getLinkInstances() {
    return linkInstances;
  }

  public void setLinkInstances(String linkInstances) {
    this.linkInstances = linkInstances;
  }

  public ReviewDef name(String name) {
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

  public ReviewDef notifications(List<Notifications> notifications) {
    this.notifications = notifications;
    return this;
  }

  public ReviewDef addNotificationsItem(Notifications notificationsItem) {
    if (this.notifications == null) {
      this.notifications = new ArrayList<Notifications>();
    }
    this.notifications.add(notificationsItem);
    return this;
  }

   /**
   * Get notifications
   * @return notifications
  **/
  @ApiModelProperty(value = "")
  public List<Notifications> getNotifications() {
    return notifications;
  }

  public void setNotifications(List<Notifications> notifications) {
    this.notifications = notifications;
  }

  public ReviewDef overlayDefId(Long overlayDefId) {
    this.overlayDefId = overlayDefId;
    return this;
  }

   /**
   * Get overlayDefId
   * @return overlayDefId
  **/
  @ApiModelProperty(value = "")
  public Long getOverlayDefId() {
    return overlayDefId;
  }

  public void setOverlayDefId(Long overlayDefId) {
    this.overlayDefId = overlayDefId;
  }

  public ReviewDef overlayDefType(OverlayDefTypeEnum overlayDefType) {
    this.overlayDefType = overlayDefType;
    return this;
  }

   /**
   * Get overlayDefType
   * @return overlayDefType
  **/
  @ApiModelProperty(value = "")
  public OverlayDefTypeEnum getOverlayDefType() {
    return overlayDefType;
  }

  public void setOverlayDefType(OverlayDefTypeEnum overlayDefType) {
    this.overlayDefType = overlayDefType;
  }

  public ReviewDef owner(Reviewer owner) {
    this.owner = owner;
    return this;
  }

   /**
   * Get owner
   * @return owner
  **/
  @ApiModelProperty(value = "")
  public Reviewer getOwner() {
    return owner;
  }

  public void setOwner(Reviewer owner) {
    this.owner = owner;
  }

  public ReviewDef owners(List<Reviewer> owners) {
    this.owners = owners;
    return this;
  }

  public ReviewDef addOwnersItem(Reviewer ownersItem) {
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

  public ReviewDef partialApprovalPolicy(PartialApprovalPolicyEnum partialApprovalPolicy) {
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

  public ReviewDef prd(String prd) {
    this.prd = prd;
    return this;
  }

   /**
   * Get prd
   * @return prd
  **/
  @ApiModelProperty(value = "")
  public String getPrd() {
    return prd;
  }

  public void setPrd(String prd) {
    this.prd = prd;
  }

  public ReviewDef previewDefId(Long previewDefId) {
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

  public ReviewDef reassignOnEscalation(Boolean reassignOnEscalation) {
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

  public ReviewDef requiredComments(List<RequiredCommentsEnum> requiredComments) {
    this.requiredComments = requiredComments;
    return this;
  }

  public ReviewDef addRequiredCommentsItem(RequiredCommentsEnum requiredCommentsItem) {
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

  public ReviewDef reviewDefId(Long reviewDefId) {
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

  public ReviewDef reviewInstId(Long reviewInstId) {
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

  public ReviewDef reviewType(ReviewTypeEnum reviewType) {
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

  public ReviewDef reviewers(List<Reviewer> reviewers) {
    this.reviewers = reviewers;
    return this;
  }

  public ReviewDef addReviewersItem(Reviewer reviewersItem) {
    if (this.reviewers == null) {
      this.reviewers = new ArrayList<Reviewer>();
    }
    this.reviewers.add(reviewersItem);
    return this;
  }

   /**
   * Get reviewers
   * @return reviewers
  **/
  @ApiModelProperty(value = "")
  public List<Reviewer> getReviewers() {
    return reviewers;
  }

  public void setReviewers(List<Reviewer> reviewers) {
    this.reviewers = reviewers;
  }

  public ReviewDef reviewingTargetData(ReviewDataTargets reviewingTargetData) {
    this.reviewingTargetData = reviewingTargetData;
    return this;
  }

   /**
   * Get reviewingTargetData
   * @return reviewingTargetData
  **/
  @ApiModelProperty(value = "")
  public ReviewDataTargets getReviewingTargetData() {
    return reviewingTargetData;
  }

  public void setReviewingTargetData(ReviewDataTargets reviewingTargetData) {
    this.reviewingTargetData = reviewingTargetData;
  }

  public ReviewDef reviewingTargets(ReviewingTargets reviewingTargets) {
    this.reviewingTargets = reviewingTargets;
    return this;
  }

   /**
   * Get reviewingTargets
   * @return reviewingTargets
  **/
  @ApiModelProperty(value = "")
  public ReviewingTargets getReviewingTargets() {
    return reviewingTargets;
  }

  public void setReviewingTargets(ReviewingTargets reviewingTargets) {
    this.reviewingTargets = reviewingTargets;
  }

  public ReviewDef reviews(List<ReviewInst> reviews) {
    this.reviews = reviews;
    return this;
  }

  public ReviewDef addReviewsItem(ReviewInst reviewsItem) {
    if (this.reviews == null) {
      this.reviews = new ArrayList<ReviewInst>();
    }
    this.reviews.add(reviewsItem);
    return this;
  }

   /**
   * Get reviews
   * @return reviews
  **/
  @ApiModelProperty(value = "")
  public List<ReviewInst> getReviews() {
    return reviews;
  }

  public void setReviews(List<ReviewInst> reviews) {
    this.reviews = reviews;
  }

  public ReviewDef schedule(Reviewer schedule) {
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

  public ReviewDef selfReviewPolicy(SelfReviewPolicyEnum selfReviewPolicy) {
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

  public ReviewDef showAcctPermissions(Boolean showAcctPermissions) {
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

  public ReviewDef showBRoleMembers(Boolean showBRoleMembers) {
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

  public ReviewDef showBRolePolicies(Boolean showBRolePolicies) {
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

  public ReviewDef showRolePermissions(Boolean showRolePermissions) {
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

  public ReviewDef startMessage(String startMessage) {
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

  public ReviewDef startState(StartStateEnum startState) {
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

  public ReviewDef startStatus(StartStatusEnum startStatus) {
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

  public ReviewDef startable(Boolean startable) {
    this.startable = startable;
    return this;
  }

   /**
   * Get startable
   * @return startable
  **/
  @ApiModelProperty(value = "")
  public Boolean isStartable() {
    return startable;
  }

  public void setStartable(Boolean startable) {
    this.startable = startable;
  }

  public ReviewDef status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ReviewDef statusComment(String statusComment) {
    this.statusComment = statusComment;
    return this;
  }

   /**
   * Get statusComment
   * @return statusComment
  **/
  @ApiModelProperty(value = "")
  public String getStatusComment() {
    return statusComment;
  }

  public void setStatusComment(String statusComment) {
    this.statusComment = statusComment;
  }

  public ReviewDef taskGroupAttrKey(String taskGroupAttrKey) {
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

  public ReviewDef taskSortAttrKey(String taskSortAttrKey) {
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

  public ReviewDef uniqueReviewDefId(String uniqueReviewDefId) {
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

  public ReviewDef useMaterializedView(Boolean useMaterializedView) {
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

  public ReviewDef valid(Integer valid) {
    this.valid = valid;
    return this;
  }

   /**
   * Get valid
   * @return valid
  **/
  @ApiModelProperty(value = "")
  public Integer getValid() {
    return valid;
  }

  public void setValid(Integer valid) {
    this.valid = valid;
  }

  public ReviewDef validUnit(ValidUnitEnum validUnit) {
    this.validUnit = validUnit;
    return this;
  }

   /**
   * Get validUnit
   * @return validUnit
  **/
  @ApiModelProperty(value = "")
  public ValidUnitEnum getValidUnit() {
    return validUnit;
  }

  public void setValidUnit(ValidUnitEnum validUnit) {
    this.validUnit = validUnit;
  }

  public ReviewDef version(Long version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(value = "")
  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewDef reviewDef = (ReviewDef) o;
    return Objects.equals(this.active, reviewDef.active) &&
        Objects.equals(this.activeFromDate, reviewDef.activeFromDate) &&
        Objects.equals(this.activeMicroCertCt, reviewDef.activeMicroCertCt) &&
        Objects.equals(this.activeToDate, reviewDef.activeToDate) &&
        Objects.equals(this.allowModifyAccess, reviewDef.allowModifyAccess) &&
        Objects.equals(this.allowOverride, reviewDef.allowOverride) &&
        Objects.equals(this.approvedBy, reviewDef.approvedBy) &&
        Objects.equals(this.approvedByPolicy, reviewDef.approvedByPolicy) &&
        Objects.equals(this.attributes, reviewDef.attributes) &&
        Objects.equals(this.auditor, reviewDef.auditor) &&
        Objects.equals(this.auditorComment, reviewDef.auditorComment) &&
        Objects.equals(this.canceledBy, reviewDef.canceledBy) &&
        Objects.equals(this.deleted, reviewDef.deleted) &&
        Objects.equals(this.description, reviewDef.description) &&
        Objects.equals(this.editable, reviewDef.editable) &&
        Objects.equals(this.escalation, reviewDef.escalation) &&
        Objects.equals(this.escalationOwners, reviewDef.escalationOwners) &&
        Objects.equals(this.escalationUnit, reviewDef.escalationUnit) &&
        Objects.equals(this.excludeAuthorized, reviewDef.excludeAuthorized) &&
        Objects.equals(this.excludeNoDirectReports, reviewDef.excludeNoDirectReports) &&
        Objects.equals(this.excludeNotAuthorized, reviewDef.excludeNotAuthorized) &&
        Objects.equals(this.expirationExtension, reviewDef.expirationExtension) &&
        Objects.equals(this.expirationExtensionUnit, reviewDef.expirationExtensionUnit) &&
        Objects.equals(this.expirationPolicy, reviewDef.expirationPolicy) &&
        Objects.equals(this.groupInIDMRoles, reviewDef.groupInIDMRoles) &&
        Objects.equals(this.hideBulkActions, reviewDef.hideBulkActions) &&
        Objects.equals(this.hideReassign, reviewDef.hideReassign) &&
        Objects.equals(this.instanceOwner, reviewDef.instanceOwner) &&
        Objects.equals(this.instructions, reviewDef.instructions) &&
        Objects.equals(this.itemApproveCount, reviewDef.itemApproveCount) &&
        Objects.equals(this.itemCompleteCount, reviewDef.itemCompleteCount) &&
        Objects.equals(this.itemCount, reviewDef.itemCount) &&
        Objects.equals(this.latestValidToTime, reviewDef.latestValidToTime) &&
        Objects.equals(this.link, reviewDef.link) &&
        Objects.equals(this.linkInstance, reviewDef.linkInstance) &&
        Objects.equals(this.linkInstances, reviewDef.linkInstances) &&
        Objects.equals(this.name, reviewDef.name) &&
        Objects.equals(this.notifications, reviewDef.notifications) &&
        Objects.equals(this.overlayDefId, reviewDef.overlayDefId) &&
        Objects.equals(this.overlayDefType, reviewDef.overlayDefType) &&
        Objects.equals(this.owner, reviewDef.owner) &&
        Objects.equals(this.owners, reviewDef.owners) &&
        Objects.equals(this.partialApprovalPolicy, reviewDef.partialApprovalPolicy) &&
        Objects.equals(this.prd, reviewDef.prd) &&
        Objects.equals(this.previewDefId, reviewDef.previewDefId) &&
        Objects.equals(this.reassignOnEscalation, reviewDef.reassignOnEscalation) &&
        Objects.equals(this.requiredComments, reviewDef.requiredComments) &&
        Objects.equals(this.reviewDefId, reviewDef.reviewDefId) &&
        Objects.equals(this.reviewInstId, reviewDef.reviewInstId) &&
        Objects.equals(this.reviewType, reviewDef.reviewType) &&
        Objects.equals(this.reviewers, reviewDef.reviewers) &&
        Objects.equals(this.reviewingTargetData, reviewDef.reviewingTargetData) &&
        Objects.equals(this.reviewingTargets, reviewDef.reviewingTargets) &&
        Objects.equals(this.reviews, reviewDef.reviews) &&
        Objects.equals(this.schedule, reviewDef.schedule) &&
        Objects.equals(this.selfReviewPolicy, reviewDef.selfReviewPolicy) &&
        Objects.equals(this.showAcctPermissions, reviewDef.showAcctPermissions) &&
        Objects.equals(this.showBRoleMembers, reviewDef.showBRoleMembers) &&
        Objects.equals(this.showBRolePolicies, reviewDef.showBRolePolicies) &&
        Objects.equals(this.showRolePermissions, reviewDef.showRolePermissions) &&
        Objects.equals(this.startMessage, reviewDef.startMessage) &&
        Objects.equals(this.startState, reviewDef.startState) &&
        Objects.equals(this.startStatus, reviewDef.startStatus) &&
        Objects.equals(this.startable, reviewDef.startable) &&
        Objects.equals(this.status, reviewDef.status) &&
        Objects.equals(this.statusComment, reviewDef.statusComment) &&
        Objects.equals(this.taskGroupAttrKey, reviewDef.taskGroupAttrKey) &&
        Objects.equals(this.taskSortAttrKey, reviewDef.taskSortAttrKey) &&
        Objects.equals(this.uniqueReviewDefId, reviewDef.uniqueReviewDefId) &&
        Objects.equals(this.useMaterializedView, reviewDef.useMaterializedView) &&
        Objects.equals(this.valid, reviewDef.valid) &&
        Objects.equals(this.validUnit, reviewDef.validUnit) &&
        Objects.equals(this.version, reviewDef.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, activeFromDate, activeMicroCertCt, activeToDate, allowModifyAccess, allowOverride, approvedBy, approvedByPolicy, attributes, auditor, auditorComment, canceledBy, deleted, description, editable, escalation, escalationOwners, escalationUnit, excludeAuthorized, excludeNoDirectReports, excludeNotAuthorized, expirationExtension, expirationExtensionUnit, expirationPolicy, groupInIDMRoles, hideBulkActions, hideReassign, instanceOwner, instructions, itemApproveCount, itemCompleteCount, itemCount, latestValidToTime, link, linkInstance, linkInstances, name, notifications, overlayDefId, overlayDefType, owner, owners, partialApprovalPolicy, prd, previewDefId, reassignOnEscalation, requiredComments, reviewDefId, reviewInstId, reviewType, reviewers, reviewingTargetData, reviewingTargets, reviews, schedule, selfReviewPolicy, showAcctPermissions, showBRoleMembers, showBRolePolicies, showRolePermissions, startMessage, startState, startStatus, startable, status, statusComment, taskGroupAttrKey, taskSortAttrKey, uniqueReviewDefId, useMaterializedView, valid, validUnit, version);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewDef {\n");
    
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    activeFromDate: ").append(toIndentedString(activeFromDate)).append("\n");
    sb.append("    activeMicroCertCt: ").append(toIndentedString(activeMicroCertCt)).append("\n");
    sb.append("    activeToDate: ").append(toIndentedString(activeToDate)).append("\n");
    sb.append("    allowModifyAccess: ").append(toIndentedString(allowModifyAccess)).append("\n");
    sb.append("    allowOverride: ").append(toIndentedString(allowOverride)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    approvedByPolicy: ").append(toIndentedString(approvedByPolicy)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    auditor: ").append(toIndentedString(auditor)).append("\n");
    sb.append("    auditorComment: ").append(toIndentedString(auditorComment)).append("\n");
    sb.append("    canceledBy: ").append(toIndentedString(canceledBy)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    escalation: ").append(toIndentedString(escalation)).append("\n");
    sb.append("    escalationOwners: ").append(toIndentedString(escalationOwners)).append("\n");
    sb.append("    escalationUnit: ").append(toIndentedString(escalationUnit)).append("\n");
    sb.append("    excludeAuthorized: ").append(toIndentedString(excludeAuthorized)).append("\n");
    sb.append("    excludeNoDirectReports: ").append(toIndentedString(excludeNoDirectReports)).append("\n");
    sb.append("    excludeNotAuthorized: ").append(toIndentedString(excludeNotAuthorized)).append("\n");
    sb.append("    expirationExtension: ").append(toIndentedString(expirationExtension)).append("\n");
    sb.append("    expirationExtensionUnit: ").append(toIndentedString(expirationExtensionUnit)).append("\n");
    sb.append("    expirationPolicy: ").append(toIndentedString(expirationPolicy)).append("\n");
    sb.append("    groupInIDMRoles: ").append(toIndentedString(groupInIDMRoles)).append("\n");
    sb.append("    hideBulkActions: ").append(toIndentedString(hideBulkActions)).append("\n");
    sb.append("    hideReassign: ").append(toIndentedString(hideReassign)).append("\n");
    sb.append("    instanceOwner: ").append(toIndentedString(instanceOwner)).append("\n");
    sb.append("    instructions: ").append(toIndentedString(instructions)).append("\n");
    sb.append("    itemApproveCount: ").append(toIndentedString(itemApproveCount)).append("\n");
    sb.append("    itemCompleteCount: ").append(toIndentedString(itemCompleteCount)).append("\n");
    sb.append("    itemCount: ").append(toIndentedString(itemCount)).append("\n");
    sb.append("    latestValidToTime: ").append(toIndentedString(latestValidToTime)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkInstance: ").append(toIndentedString(linkInstance)).append("\n");
    sb.append("    linkInstances: ").append(toIndentedString(linkInstances)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    notifications: ").append(toIndentedString(notifications)).append("\n");
    sb.append("    overlayDefId: ").append(toIndentedString(overlayDefId)).append("\n");
    sb.append("    overlayDefType: ").append(toIndentedString(overlayDefType)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    partialApprovalPolicy: ").append(toIndentedString(partialApprovalPolicy)).append("\n");
    sb.append("    prd: ").append(toIndentedString(prd)).append("\n");
    sb.append("    previewDefId: ").append(toIndentedString(previewDefId)).append("\n");
    sb.append("    reassignOnEscalation: ").append(toIndentedString(reassignOnEscalation)).append("\n");
    sb.append("    requiredComments: ").append(toIndentedString(requiredComments)).append("\n");
    sb.append("    reviewDefId: ").append(toIndentedString(reviewDefId)).append("\n");
    sb.append("    reviewInstId: ").append(toIndentedString(reviewInstId)).append("\n");
    sb.append("    reviewType: ").append(toIndentedString(reviewType)).append("\n");
    sb.append("    reviewers: ").append(toIndentedString(reviewers)).append("\n");
    sb.append("    reviewingTargetData: ").append(toIndentedString(reviewingTargetData)).append("\n");
    sb.append("    reviewingTargets: ").append(toIndentedString(reviewingTargets)).append("\n");
    sb.append("    reviews: ").append(toIndentedString(reviews)).append("\n");
    sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
    sb.append("    selfReviewPolicy: ").append(toIndentedString(selfReviewPolicy)).append("\n");
    sb.append("    showAcctPermissions: ").append(toIndentedString(showAcctPermissions)).append("\n");
    sb.append("    showBRoleMembers: ").append(toIndentedString(showBRoleMembers)).append("\n");
    sb.append("    showBRolePolicies: ").append(toIndentedString(showBRolePolicies)).append("\n");
    sb.append("    showRolePermissions: ").append(toIndentedString(showRolePermissions)).append("\n");
    sb.append("    startMessage: ").append(toIndentedString(startMessage)).append("\n");
    sb.append("    startState: ").append(toIndentedString(startState)).append("\n");
    sb.append("    startStatus: ").append(toIndentedString(startStatus)).append("\n");
    sb.append("    startable: ").append(toIndentedString(startable)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusComment: ").append(toIndentedString(statusComment)).append("\n");
    sb.append("    taskGroupAttrKey: ").append(toIndentedString(taskGroupAttrKey)).append("\n");
    sb.append("    taskSortAttrKey: ").append(toIndentedString(taskSortAttrKey)).append("\n");
    sb.append("    uniqueReviewDefId: ").append(toIndentedString(uniqueReviewDefId)).append("\n");
    sb.append("    useMaterializedView: ").append(toIndentedString(useMaterializedView)).append("\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    validUnit: ").append(toIndentedString(validUnit)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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
