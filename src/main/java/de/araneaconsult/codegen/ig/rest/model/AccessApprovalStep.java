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
import de.araneaconsult.codegen.ig.rest.model.AccessApprovalStepApprover;
import de.araneaconsult.codegen.ig.rest.model.Prd;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AccessApprovalStep
 */



public class AccessApprovalStep {
  @SerializedName("approvalCondition")
  private String approvalCondition = null;

  /**
   * Gets or Sets approvalConditionReq
   */
  @JsonAdapter(ApprovalConditionReqEnum.Adapter.class)
  public enum ApprovalConditionReqEnum {
    @SerializedName("NO_REQUESTS")
    NO_REQUESTS("NO_REQUESTS"),
    @SerializedName("GRANT_REQUESTS")
    GRANT_REQUESTS("GRANT_REQUESTS"),
    @SerializedName("REVOKE_REQUESTS")
    REVOKE_REQUESTS("REVOKE_REQUESTS"),
    @SerializedName("ALL_REQUESTS")
    ALL_REQUESTS("ALL_REQUESTS");

    private String value;

    ApprovalConditionReqEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ApprovalConditionReqEnum fromValue(String input) {
      for (ApprovalConditionReqEnum b : ApprovalConditionReqEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ApprovalConditionReqEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ApprovalConditionReqEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ApprovalConditionReqEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ApprovalConditionReqEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("approvalConditionReq")
  private ApprovalConditionReqEnum approvalConditionReq = null;

  /**
   * Gets or Sets approverType
   */
  @JsonAdapter(ApproverTypeEnum.Adapter.class)
  public enum ApproverTypeEnum {
    @SerializedName("SELF")
    SELF("SELF"),
    @SerializedName("SUPERVISOR")
    SUPERVISOR("SUPERVISOR"),
    @SerializedName("ITEM_OWNER")
    ITEM_OWNER("ITEM_OWNER"),
    @SerializedName("USERS_AND_GROUPS")
    USERS_AND_GROUPS("USERS_AND_GROUPS"),
    @SerializedName("COVERAGE_MAP")
    COVERAGE_MAP("COVERAGE_MAP"),
    @SerializedName("IDM_WORKFLOW")
    IDM_WORKFLOW("IDM_WORKFLOW"),
    @SerializedName("PROCESS")
    PROCESS("PROCESS");

    private String value;

    ApproverTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ApproverTypeEnum fromValue(String input) {
      for (ApproverTypeEnum b : ApproverTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ApproverTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ApproverTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ApproverTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ApproverTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("approverType")
  private ApproverTypeEnum approverType = null;

  @SerializedName("approvers")
  private List<AccessApprovalStepApprover> approvers = null;

  @SerializedName("autoApproveAuthItems")
  private Boolean autoApproveAuthItems = null;

  /**
   * Gets or Sets autoApproveAuthItemsReq
   */
  @JsonAdapter(AutoApproveAuthItemsReqEnum.Adapter.class)
  public enum AutoApproveAuthItemsReqEnum {
    @SerializedName("NO_REQUESTS")
    NO_REQUESTS("NO_REQUESTS"),
    @SerializedName("GRANT_REQUESTS")
    GRANT_REQUESTS("GRANT_REQUESTS"),
    @SerializedName("REVOKE_REQUESTS")
    REVOKE_REQUESTS("REVOKE_REQUESTS"),
    @SerializedName("ALL_REQUESTS")
    ALL_REQUESTS("ALL_REQUESTS");

    private String value;

    AutoApproveAuthItemsReqEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AutoApproveAuthItemsReqEnum fromValue(String input) {
      for (AutoApproveAuthItemsReqEnum b : AutoApproveAuthItemsReqEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AutoApproveAuthItemsReqEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AutoApproveAuthItemsReqEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AutoApproveAuthItemsReqEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AutoApproveAuthItemsReqEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("autoApproveAuthItemsReq")
  private AutoApproveAuthItemsReqEnum autoApproveAuthItemsReq = null;

  @SerializedName("escalationPeriod")
  private Long escalationPeriod = null;

  /**
   * Gets or Sets escalatorType
   */
  @JsonAdapter(EscalatorTypeEnum.Adapter.class)
  public enum EscalatorTypeEnum {
    @SerializedName("SELF")
    SELF("SELF"),
    @SerializedName("SUPERVISOR")
    SUPERVISOR("SUPERVISOR"),
    @SerializedName("ITEM_OWNER")
    ITEM_OWNER("ITEM_OWNER"),
    @SerializedName("USERS_AND_GROUPS")
    USERS_AND_GROUPS("USERS_AND_GROUPS"),
    @SerializedName("COVERAGE_MAP")
    COVERAGE_MAP("COVERAGE_MAP"),
    @SerializedName("IDM_WORKFLOW")
    IDM_WORKFLOW("IDM_WORKFLOW"),
    @SerializedName("PROCESS")
    PROCESS("PROCESS");

    private String value;

    EscalatorTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static EscalatorTypeEnum fromValue(String input) {
      for (EscalatorTypeEnum b : EscalatorTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<EscalatorTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EscalatorTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public EscalatorTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return EscalatorTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("escalatorType")
  private EscalatorTypeEnum escalatorType = null;

  @SerializedName("escalators")
  private List<AccessApprovalStepApprover> escalators = null;

  @SerializedName("executeOffset")
  private Long executeOffset = null;

  @SerializedName("idmWorkflow")
  private List<Prd> idmWorkflow = null;

  /**
   * Gets or Sets idmWorkflowFailureAction
   */
  @JsonAdapter(IdmWorkflowFailureActionEnum.Adapter.class)
  public enum IdmWorkflowFailureActionEnum {
    @SerializedName("APPROVE")
    APPROVE("APPROVE"),
    @SerializedName("DENY")
    DENY("DENY");

    private String value;

    IdmWorkflowFailureActionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static IdmWorkflowFailureActionEnum fromValue(String input) {
      for (IdmWorkflowFailureActionEnum b : IdmWorkflowFailureActionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<IdmWorkflowFailureActionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final IdmWorkflowFailureActionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public IdmWorkflowFailureActionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return IdmWorkflowFailureActionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("idmWorkflowFailureAction")
  private IdmWorkflowFailureActionEnum idmWorkflowFailureAction = null;

  /**
   * Gets or Sets reminderBccType
   */
  @JsonAdapter(ReminderBccTypeEnum.Adapter.class)
  public enum ReminderBccTypeEnum {
    @SerializedName("SELF")
    SELF("SELF"),
    @SerializedName("SUPERVISOR")
    SUPERVISOR("SUPERVISOR"),
    @SerializedName("ITEM_OWNER")
    ITEM_OWNER("ITEM_OWNER"),
    @SerializedName("USERS_AND_GROUPS")
    USERS_AND_GROUPS("USERS_AND_GROUPS"),
    @SerializedName("COVERAGE_MAP")
    COVERAGE_MAP("COVERAGE_MAP"),
    @SerializedName("IDM_WORKFLOW")
    IDM_WORKFLOW("IDM_WORKFLOW"),
    @SerializedName("PROCESS")
    PROCESS("PROCESS");

    private String value;

    ReminderBccTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ReminderBccTypeEnum fromValue(String input) {
      for (ReminderBccTypeEnum b : ReminderBccTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ReminderBccTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReminderBccTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ReminderBccTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ReminderBccTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("reminderBccType")
  private ReminderBccTypeEnum reminderBccType = null;

  @SerializedName("reminderBccs")
  private List<AccessApprovalStepApprover> reminderBccs = null;

  /**
   * Gets or Sets reminderCcType
   */
  @JsonAdapter(ReminderCcTypeEnum.Adapter.class)
  public enum ReminderCcTypeEnum {
    @SerializedName("SELF")
    SELF("SELF"),
    @SerializedName("SUPERVISOR")
    SUPERVISOR("SUPERVISOR"),
    @SerializedName("ITEM_OWNER")
    ITEM_OWNER("ITEM_OWNER"),
    @SerializedName("USERS_AND_GROUPS")
    USERS_AND_GROUPS("USERS_AND_GROUPS"),
    @SerializedName("COVERAGE_MAP")
    COVERAGE_MAP("COVERAGE_MAP"),
    @SerializedName("IDM_WORKFLOW")
    IDM_WORKFLOW("IDM_WORKFLOW"),
    @SerializedName("PROCESS")
    PROCESS("PROCESS");

    private String value;

    ReminderCcTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ReminderCcTypeEnum fromValue(String input) {
      for (ReminderCcTypeEnum b : ReminderCcTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ReminderCcTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReminderCcTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ReminderCcTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ReminderCcTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("reminderCcType")
  private ReminderCcTypeEnum reminderCcType = null;

  @SerializedName("reminderCcs")
  private List<AccessApprovalStepApprover> reminderCcs = null;

  @SerializedName("reminderFrequency")
  private Long reminderFrequency = null;

  @SerializedName("stepSequence")
  private Long stepSequence = null;

  /**
   * Gets or Sets stepType
   */
  @JsonAdapter(StepTypeEnum.Adapter.class)
  public enum StepTypeEnum {
    @SerializedName("APPROVAL")
    APPROVAL("APPROVAL"),
    @SerializedName("PSODV_CHECK")
    PSODV_CHECK("PSODV_CHECK");

    private String value;

    StepTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StepTypeEnum fromValue(String input) {
      for (StepTypeEnum b : StepTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StepTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StepTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StepTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StepTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("stepType")
  private StepTypeEnum stepType = null;

  /**
   * Gets or Sets timeoutAction
   */
  @JsonAdapter(TimeoutActionEnum.Adapter.class)
  public enum TimeoutActionEnum {
    @SerializedName("APPROVE")
    APPROVE("APPROVE"),
    @SerializedName("DENY")
    DENY("DENY");

    private String value;

    TimeoutActionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static TimeoutActionEnum fromValue(String input) {
      for (TimeoutActionEnum b : TimeoutActionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<TimeoutActionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TimeoutActionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public TimeoutActionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return TimeoutActionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("timeoutAction")
  private TimeoutActionEnum timeoutAction = null;

  @SerializedName("timeoutPeriod")
  private Long timeoutPeriod = null;

  /**
   * Gets or Sets whenToExecute
   */
  @JsonAdapter(WhenToExecuteEnum.Adapter.class)
  public enum WhenToExecuteEnum {
    @SerializedName("RELATIVE_TO_EFFECTIVE")
    RELATIVE_TO_EFFECTIVE("RELATIVE_TO_EFFECTIVE"),
    @SerializedName("IMMEDIATELY")
    IMMEDIATELY("IMMEDIATELY");

    private String value;

    WhenToExecuteEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static WhenToExecuteEnum fromValue(String input) {
      for (WhenToExecuteEnum b : WhenToExecuteEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<WhenToExecuteEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final WhenToExecuteEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public WhenToExecuteEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return WhenToExecuteEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("whenToExecute")
  private WhenToExecuteEnum whenToExecute = null;

  public AccessApprovalStep approvalCondition(String approvalCondition) {
    this.approvalCondition = approvalCondition;
    return this;
  }

   /**
   * Get approvalCondition
   * @return approvalCondition
  **/
  @ApiModelProperty(value = "")
  public String getApprovalCondition() {
    return approvalCondition;
  }

  public void setApprovalCondition(String approvalCondition) {
    this.approvalCondition = approvalCondition;
  }

  public AccessApprovalStep approvalConditionReq(ApprovalConditionReqEnum approvalConditionReq) {
    this.approvalConditionReq = approvalConditionReq;
    return this;
  }

   /**
   * Get approvalConditionReq
   * @return approvalConditionReq
  **/
  @ApiModelProperty(value = "")
  public ApprovalConditionReqEnum getApprovalConditionReq() {
    return approvalConditionReq;
  }

  public void setApprovalConditionReq(ApprovalConditionReqEnum approvalConditionReq) {
    this.approvalConditionReq = approvalConditionReq;
  }

  public AccessApprovalStep approverType(ApproverTypeEnum approverType) {
    this.approverType = approverType;
    return this;
  }

   /**
   * Get approverType
   * @return approverType
  **/
  @ApiModelProperty(value = "")
  public ApproverTypeEnum getApproverType() {
    return approverType;
  }

  public void setApproverType(ApproverTypeEnum approverType) {
    this.approverType = approverType;
  }

  public AccessApprovalStep approvers(List<AccessApprovalStepApprover> approvers) {
    this.approvers = approvers;
    return this;
  }

  public AccessApprovalStep addApproversItem(AccessApprovalStepApprover approversItem) {
    if (this.approvers == null) {
      this.approvers = new ArrayList<AccessApprovalStepApprover>();
    }
    this.approvers.add(approversItem);
    return this;
  }

   /**
   * Get approvers
   * @return approvers
  **/
  @ApiModelProperty(value = "")
  public List<AccessApprovalStepApprover> getApprovers() {
    return approvers;
  }

  public void setApprovers(List<AccessApprovalStepApprover> approvers) {
    this.approvers = approvers;
  }

  public AccessApprovalStep autoApproveAuthItems(Boolean autoApproveAuthItems) {
    this.autoApproveAuthItems = autoApproveAuthItems;
    return this;
  }

   /**
   * Get autoApproveAuthItems
   * @return autoApproveAuthItems
  **/
  @ApiModelProperty(value = "")
  public Boolean isAutoApproveAuthItems() {
    return autoApproveAuthItems;
  }

  public void setAutoApproveAuthItems(Boolean autoApproveAuthItems) {
    this.autoApproveAuthItems = autoApproveAuthItems;
  }

  public AccessApprovalStep autoApproveAuthItemsReq(AutoApproveAuthItemsReqEnum autoApproveAuthItemsReq) {
    this.autoApproveAuthItemsReq = autoApproveAuthItemsReq;
    return this;
  }

   /**
   * Get autoApproveAuthItemsReq
   * @return autoApproveAuthItemsReq
  **/
  @ApiModelProperty(value = "")
  public AutoApproveAuthItemsReqEnum getAutoApproveAuthItemsReq() {
    return autoApproveAuthItemsReq;
  }

  public void setAutoApproveAuthItemsReq(AutoApproveAuthItemsReqEnum autoApproveAuthItemsReq) {
    this.autoApproveAuthItemsReq = autoApproveAuthItemsReq;
  }

  public AccessApprovalStep escalationPeriod(Long escalationPeriod) {
    this.escalationPeriod = escalationPeriod;
    return this;
  }

   /**
   * Get escalationPeriod
   * @return escalationPeriod
  **/
  @ApiModelProperty(value = "")
  public Long getEscalationPeriod() {
    return escalationPeriod;
  }

  public void setEscalationPeriod(Long escalationPeriod) {
    this.escalationPeriod = escalationPeriod;
  }

  public AccessApprovalStep escalatorType(EscalatorTypeEnum escalatorType) {
    this.escalatorType = escalatorType;
    return this;
  }

   /**
   * Get escalatorType
   * @return escalatorType
  **/
  @ApiModelProperty(value = "")
  public EscalatorTypeEnum getEscalatorType() {
    return escalatorType;
  }

  public void setEscalatorType(EscalatorTypeEnum escalatorType) {
    this.escalatorType = escalatorType;
  }

  public AccessApprovalStep escalators(List<AccessApprovalStepApprover> escalators) {
    this.escalators = escalators;
    return this;
  }

  public AccessApprovalStep addEscalatorsItem(AccessApprovalStepApprover escalatorsItem) {
    if (this.escalators == null) {
      this.escalators = new ArrayList<AccessApprovalStepApprover>();
    }
    this.escalators.add(escalatorsItem);
    return this;
  }

   /**
   * Get escalators
   * @return escalators
  **/
  @ApiModelProperty(value = "")
  public List<AccessApprovalStepApprover> getEscalators() {
    return escalators;
  }

  public void setEscalators(List<AccessApprovalStepApprover> escalators) {
    this.escalators = escalators;
  }

  public AccessApprovalStep executeOffset(Long executeOffset) {
    this.executeOffset = executeOffset;
    return this;
  }

   /**
   * Get executeOffset
   * @return executeOffset
  **/
  @ApiModelProperty(value = "")
  public Long getExecuteOffset() {
    return executeOffset;
  }

  public void setExecuteOffset(Long executeOffset) {
    this.executeOffset = executeOffset;
  }

  public AccessApprovalStep idmWorkflow(List<Prd> idmWorkflow) {
    this.idmWorkflow = idmWorkflow;
    return this;
  }

  public AccessApprovalStep addIdmWorkflowItem(Prd idmWorkflowItem) {
    if (this.idmWorkflow == null) {
      this.idmWorkflow = new ArrayList<Prd>();
    }
    this.idmWorkflow.add(idmWorkflowItem);
    return this;
  }

   /**
   * Get idmWorkflow
   * @return idmWorkflow
  **/
  @ApiModelProperty(value = "")
  public List<Prd> getIdmWorkflow() {
    return idmWorkflow;
  }

  public void setIdmWorkflow(List<Prd> idmWorkflow) {
    this.idmWorkflow = idmWorkflow;
  }

  public AccessApprovalStep idmWorkflowFailureAction(IdmWorkflowFailureActionEnum idmWorkflowFailureAction) {
    this.idmWorkflowFailureAction = idmWorkflowFailureAction;
    return this;
  }

   /**
   * Get idmWorkflowFailureAction
   * @return idmWorkflowFailureAction
  **/
  @ApiModelProperty(value = "")
  public IdmWorkflowFailureActionEnum getIdmWorkflowFailureAction() {
    return idmWorkflowFailureAction;
  }

  public void setIdmWorkflowFailureAction(IdmWorkflowFailureActionEnum idmWorkflowFailureAction) {
    this.idmWorkflowFailureAction = idmWorkflowFailureAction;
  }

  public AccessApprovalStep reminderBccType(ReminderBccTypeEnum reminderBccType) {
    this.reminderBccType = reminderBccType;
    return this;
  }

   /**
   * Get reminderBccType
   * @return reminderBccType
  **/
  @ApiModelProperty(value = "")
  public ReminderBccTypeEnum getReminderBccType() {
    return reminderBccType;
  }

  public void setReminderBccType(ReminderBccTypeEnum reminderBccType) {
    this.reminderBccType = reminderBccType;
  }

  public AccessApprovalStep reminderBccs(List<AccessApprovalStepApprover> reminderBccs) {
    this.reminderBccs = reminderBccs;
    return this;
  }

  public AccessApprovalStep addReminderBccsItem(AccessApprovalStepApprover reminderBccsItem) {
    if (this.reminderBccs == null) {
      this.reminderBccs = new ArrayList<AccessApprovalStepApprover>();
    }
    this.reminderBccs.add(reminderBccsItem);
    return this;
  }

   /**
   * Get reminderBccs
   * @return reminderBccs
  **/
  @ApiModelProperty(value = "")
  public List<AccessApprovalStepApprover> getReminderBccs() {
    return reminderBccs;
  }

  public void setReminderBccs(List<AccessApprovalStepApprover> reminderBccs) {
    this.reminderBccs = reminderBccs;
  }

  public AccessApprovalStep reminderCcType(ReminderCcTypeEnum reminderCcType) {
    this.reminderCcType = reminderCcType;
    return this;
  }

   /**
   * Get reminderCcType
   * @return reminderCcType
  **/
  @ApiModelProperty(value = "")
  public ReminderCcTypeEnum getReminderCcType() {
    return reminderCcType;
  }

  public void setReminderCcType(ReminderCcTypeEnum reminderCcType) {
    this.reminderCcType = reminderCcType;
  }

  public AccessApprovalStep reminderCcs(List<AccessApprovalStepApprover> reminderCcs) {
    this.reminderCcs = reminderCcs;
    return this;
  }

  public AccessApprovalStep addReminderCcsItem(AccessApprovalStepApprover reminderCcsItem) {
    if (this.reminderCcs == null) {
      this.reminderCcs = new ArrayList<AccessApprovalStepApprover>();
    }
    this.reminderCcs.add(reminderCcsItem);
    return this;
  }

   /**
   * Get reminderCcs
   * @return reminderCcs
  **/
  @ApiModelProperty(value = "")
  public List<AccessApprovalStepApprover> getReminderCcs() {
    return reminderCcs;
  }

  public void setReminderCcs(List<AccessApprovalStepApprover> reminderCcs) {
    this.reminderCcs = reminderCcs;
  }

  public AccessApprovalStep reminderFrequency(Long reminderFrequency) {
    this.reminderFrequency = reminderFrequency;
    return this;
  }

   /**
   * Get reminderFrequency
   * @return reminderFrequency
  **/
  @ApiModelProperty(value = "")
  public Long getReminderFrequency() {
    return reminderFrequency;
  }

  public void setReminderFrequency(Long reminderFrequency) {
    this.reminderFrequency = reminderFrequency;
  }

  public AccessApprovalStep stepSequence(Long stepSequence) {
    this.stepSequence = stepSequence;
    return this;
  }

   /**
   * Get stepSequence
   * @return stepSequence
  **/
  @ApiModelProperty(value = "")
  public Long getStepSequence() {
    return stepSequence;
  }

  public void setStepSequence(Long stepSequence) {
    this.stepSequence = stepSequence;
  }

  public AccessApprovalStep stepType(StepTypeEnum stepType) {
    this.stepType = stepType;
    return this;
  }

   /**
   * Get stepType
   * @return stepType
  **/
  @ApiModelProperty(value = "")
  public StepTypeEnum getStepType() {
    return stepType;
  }

  public void setStepType(StepTypeEnum stepType) {
    this.stepType = stepType;
  }

  public AccessApprovalStep timeoutAction(TimeoutActionEnum timeoutAction) {
    this.timeoutAction = timeoutAction;
    return this;
  }

   /**
   * Get timeoutAction
   * @return timeoutAction
  **/
  @ApiModelProperty(value = "")
  public TimeoutActionEnum getTimeoutAction() {
    return timeoutAction;
  }

  public void setTimeoutAction(TimeoutActionEnum timeoutAction) {
    this.timeoutAction = timeoutAction;
  }

  public AccessApprovalStep timeoutPeriod(Long timeoutPeriod) {
    this.timeoutPeriod = timeoutPeriod;
    return this;
  }

   /**
   * Get timeoutPeriod
   * @return timeoutPeriod
  **/
  @ApiModelProperty(value = "")
  public Long getTimeoutPeriod() {
    return timeoutPeriod;
  }

  public void setTimeoutPeriod(Long timeoutPeriod) {
    this.timeoutPeriod = timeoutPeriod;
  }

  public AccessApprovalStep whenToExecute(WhenToExecuteEnum whenToExecute) {
    this.whenToExecute = whenToExecute;
    return this;
  }

   /**
   * Get whenToExecute
   * @return whenToExecute
  **/
  @ApiModelProperty(value = "")
  public WhenToExecuteEnum getWhenToExecute() {
    return whenToExecute;
  }

  public void setWhenToExecute(WhenToExecuteEnum whenToExecute) {
    this.whenToExecute = whenToExecute;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccessApprovalStep accessApprovalStep = (AccessApprovalStep) o;
    return Objects.equals(this.approvalCondition, accessApprovalStep.approvalCondition) &&
        Objects.equals(this.approvalConditionReq, accessApprovalStep.approvalConditionReq) &&
        Objects.equals(this.approverType, accessApprovalStep.approverType) &&
        Objects.equals(this.approvers, accessApprovalStep.approvers) &&
        Objects.equals(this.autoApproveAuthItems, accessApprovalStep.autoApproveAuthItems) &&
        Objects.equals(this.autoApproveAuthItemsReq, accessApprovalStep.autoApproveAuthItemsReq) &&
        Objects.equals(this.escalationPeriod, accessApprovalStep.escalationPeriod) &&
        Objects.equals(this.escalatorType, accessApprovalStep.escalatorType) &&
        Objects.equals(this.escalators, accessApprovalStep.escalators) &&
        Objects.equals(this.executeOffset, accessApprovalStep.executeOffset) &&
        Objects.equals(this.idmWorkflow, accessApprovalStep.idmWorkflow) &&
        Objects.equals(this.idmWorkflowFailureAction, accessApprovalStep.idmWorkflowFailureAction) &&
        Objects.equals(this.reminderBccType, accessApprovalStep.reminderBccType) &&
        Objects.equals(this.reminderBccs, accessApprovalStep.reminderBccs) &&
        Objects.equals(this.reminderCcType, accessApprovalStep.reminderCcType) &&
        Objects.equals(this.reminderCcs, accessApprovalStep.reminderCcs) &&
        Objects.equals(this.reminderFrequency, accessApprovalStep.reminderFrequency) &&
        Objects.equals(this.stepSequence, accessApprovalStep.stepSequence) &&
        Objects.equals(this.stepType, accessApprovalStep.stepType) &&
        Objects.equals(this.timeoutAction, accessApprovalStep.timeoutAction) &&
        Objects.equals(this.timeoutPeriod, accessApprovalStep.timeoutPeriod) &&
        Objects.equals(this.whenToExecute, accessApprovalStep.whenToExecute);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvalCondition, approvalConditionReq, approverType, approvers, autoApproveAuthItems, autoApproveAuthItemsReq, escalationPeriod, escalatorType, escalators, executeOffset, idmWorkflow, idmWorkflowFailureAction, reminderBccType, reminderBccs, reminderCcType, reminderCcs, reminderFrequency, stepSequence, stepType, timeoutAction, timeoutPeriod, whenToExecute);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccessApprovalStep {\n");
    
    sb.append("    approvalCondition: ").append(toIndentedString(approvalCondition)).append("\n");
    sb.append("    approvalConditionReq: ").append(toIndentedString(approvalConditionReq)).append("\n");
    sb.append("    approverType: ").append(toIndentedString(approverType)).append("\n");
    sb.append("    approvers: ").append(toIndentedString(approvers)).append("\n");
    sb.append("    autoApproveAuthItems: ").append(toIndentedString(autoApproveAuthItems)).append("\n");
    sb.append("    autoApproveAuthItemsReq: ").append(toIndentedString(autoApproveAuthItemsReq)).append("\n");
    sb.append("    escalationPeriod: ").append(toIndentedString(escalationPeriod)).append("\n");
    sb.append("    escalatorType: ").append(toIndentedString(escalatorType)).append("\n");
    sb.append("    escalators: ").append(toIndentedString(escalators)).append("\n");
    sb.append("    executeOffset: ").append(toIndentedString(executeOffset)).append("\n");
    sb.append("    idmWorkflow: ").append(toIndentedString(idmWorkflow)).append("\n");
    sb.append("    idmWorkflowFailureAction: ").append(toIndentedString(idmWorkflowFailureAction)).append("\n");
    sb.append("    reminderBccType: ").append(toIndentedString(reminderBccType)).append("\n");
    sb.append("    reminderBccs: ").append(toIndentedString(reminderBccs)).append("\n");
    sb.append("    reminderCcType: ").append(toIndentedString(reminderCcType)).append("\n");
    sb.append("    reminderCcs: ").append(toIndentedString(reminderCcs)).append("\n");
    sb.append("    reminderFrequency: ").append(toIndentedString(reminderFrequency)).append("\n");
    sb.append("    stepSequence: ").append(toIndentedString(stepSequence)).append("\n");
    sb.append("    stepType: ").append(toIndentedString(stepType)).append("\n");
    sb.append("    timeoutAction: ").append(toIndentedString(timeoutAction)).append("\n");
    sb.append("    timeoutPeriod: ").append(toIndentedString(timeoutPeriod)).append("\n");
    sb.append("    whenToExecute: ").append(toIndentedString(whenToExecute)).append("\n");
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
