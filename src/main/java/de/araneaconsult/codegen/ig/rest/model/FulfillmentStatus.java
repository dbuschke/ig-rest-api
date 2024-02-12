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
import de.araneaconsult.codegen.ig.rest.model.FulfillmentStatus;
import de.araneaconsult.codegen.ig.rest.model.ItemValue;
import de.araneaconsult.codegen.ig.rest.model.Reviewer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * FulfillmentStatus
 */



public class FulfillmentStatus {
  @SerializedName("accountDeleted")
  private Boolean accountDeleted = null;

  @SerializedName("accountId")
  private Long accountId = null;

  @SerializedName("accountName")
  private String accountName = null;

  @SerializedName("actualFulfillerId")
  private Long actualFulfillerId = null;

  @SerializedName("actualFulfillerLink")
  private String actualFulfillerLink = null;

  @SerializedName("actualFulfillerName")
  private String actualFulfillerName = null;

  @SerializedName("appId")
  private Long appId = null;

  @SerializedName("appName")
  private String appName = null;

  @SerializedName("assignedUsers")
  private List<Reviewer> assignedUsers = null;

  @SerializedName("broleId")
  private Long broleId = null;

  @SerializedName("broleName")
  private String broleName = null;

  @SerializedName("certPolicyDescription")
  private String certPolicyDescription = null;

  @SerializedName("certPolicyId")
  private Long certPolicyId = null;

  @SerializedName("certPolicyName")
  private String certPolicyName = null;

  /**
   * Gets or Sets changeItemAction
   */
  @JsonAdapter(ChangeItemActionEnum.Adapter.class)
  public enum ChangeItemActionEnum {
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("ADD")
    ADD("ADD"),
    @SerializedName("REMOVE")
    REMOVE("REMOVE"),
    @SerializedName("REFUSED")
    REFUSED("REFUSED"),
    @SerializedName("SKIPPED")
    SKIPPED("SKIPPED"),
    @SerializedName("REASSIGNED")
    REASSIGNED("REASSIGNED"),
    @SerializedName("TIMED_OUT")
    TIMED_OUT("TIMED_OUT"),
    @SerializedName("FULFILLED")
    FULFILLED("FULFILLED");

    private String value;

    ChangeItemActionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ChangeItemActionEnum fromValue(String input) {
      for (ChangeItemActionEnum b : ChangeItemActionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ChangeItemActionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ChangeItemActionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ChangeItemActionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ChangeItemActionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("changeItemAction")
  private ChangeItemActionEnum changeItemAction = null;

  @SerializedName("changeItemId")
  private Long changeItemId = null;

  /**
   * Gets or Sets changeItemSource
   */
  @JsonAdapter(ChangeItemSourceEnum.Adapter.class)
  public enum ChangeItemSourceEnum {
    @SerializedName("REVIEW")
    REVIEW("REVIEW"),
    @SerializedName("SOD")
    SOD("SOD"),
    @SerializedName("BUSINESS_ROLE_AUTO_FULFILL")
    BUSINESS_ROLE_AUTO_FULFILL("BUSINESS_ROLE_AUTO_FULFILL"),
    @SerializedName("REQUEST")
    REQUEST("REQUEST"),
    @SerializedName("DATA_POLICY_REMEDIATION")
    DATA_POLICY_REMEDIATION("DATA_POLICY_REMEDIATION"),
    @SerializedName("CERTIFICATION_POLICY_REMEDIATION")
    CERTIFICATION_POLICY_REMEDIATION("CERTIFICATION_POLICY_REMEDIATION"),
    @SerializedName("SCRIPTED_CHANGESET_POLICY_MODIFICATION")
    SCRIPTED_CHANGESET_POLICY_MODIFICATION("SCRIPTED_CHANGESET_POLICY_MODIFICATION");

    private String value;

    ChangeItemSourceEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ChangeItemSourceEnum fromValue(String input) {
      for (ChangeItemSourceEnum b : ChangeItemSourceEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ChangeItemSourceEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ChangeItemSourceEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ChangeItemSourceEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ChangeItemSourceEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("changeItemSource")
  private ChangeItemSourceEnum changeItemSource = null;

  @SerializedName("changeRequestItemDate")
  private Long changeRequestItemDate = null;

  /**
   * Gets or Sets changeRequestType
   */
  @JsonAdapter(ChangeRequestTypeEnum.Adapter.class)
  public enum ChangeRequestTypeEnum {
    @SerializedName("REMOVE_BUS_ROLE_ASSIGNMENT")
    REMOVE_BUS_ROLE_ASSIGNMENT("REMOVE_BUS_ROLE_ASSIGNMENT"),
    @SerializedName("ADD_USER_TO_ACCOUNT")
    ADD_USER_TO_ACCOUNT("ADD_USER_TO_ACCOUNT"),
    @SerializedName("REMOVE_PERMISSION_ASSIGNMENT")
    REMOVE_PERMISSION_ASSIGNMENT("REMOVE_PERMISSION_ASSIGNMENT"),
    @SerializedName("REMOVE_ACCOUNT_ASSIGNMENT")
    REMOVE_ACCOUNT_ASSIGNMENT("REMOVE_ACCOUNT_ASSIGNMENT"),
    @SerializedName("MODIFY_PERMISSION_ASSIGNMENT")
    MODIFY_PERMISSION_ASSIGNMENT("MODIFY_PERMISSION_ASSIGNMENT"),
    @SerializedName("MODIFY_ACCOUNT_ASSIGNMENT")
    MODIFY_ACCOUNT_ASSIGNMENT("MODIFY_ACCOUNT_ASSIGNMENT"),
    @SerializedName("MODIFY_TECH_ROLE_ASSIGNMENT")
    MODIFY_TECH_ROLE_ASSIGNMENT("MODIFY_TECH_ROLE_ASSIGNMENT"),
    @SerializedName("REMOVE_ACCOUNT")
    REMOVE_ACCOUNT("REMOVE_ACCOUNT"),
    @SerializedName("ADD_PERMISSION_TO_USER")
    ADD_PERMISSION_TO_USER("ADD_PERMISSION_TO_USER"),
    @SerializedName("ADD_APPLICATION_TO_USER")
    ADD_APPLICATION_TO_USER("ADD_APPLICATION_TO_USER"),
    @SerializedName("REMOVE_APPLICATION_FROM_USER")
    REMOVE_APPLICATION_FROM_USER("REMOVE_APPLICATION_FROM_USER"),
    @SerializedName("ADD_TECH_ROLE_TO_USER")
    ADD_TECH_ROLE_TO_USER("ADD_TECH_ROLE_TO_USER"),
    @SerializedName("REMOVE_ACCOUNT_PERMISSION")
    REMOVE_ACCOUNT_PERMISSION("REMOVE_ACCOUNT_PERMISSION"),
    @SerializedName("MODIFY_ACCOUNT")
    MODIFY_ACCOUNT("MODIFY_ACCOUNT"),
    @SerializedName("MODIFY_USER_PROFILE")
    MODIFY_USER_PROFILE("MODIFY_USER_PROFILE"),
    @SerializedName("MODIFY_USER_SUPERVISOR")
    MODIFY_USER_SUPERVISOR("MODIFY_USER_SUPERVISOR"),
    @SerializedName("DATA_VIOLATION_USER")
    DATA_VIOLATION_USER("DATA_VIOLATION_USER"),
    @SerializedName("DATA_VIOLATION_PERMISSION")
    DATA_VIOLATION_PERMISSION("DATA_VIOLATION_PERMISSION"),
    @SerializedName("DATA_VIOLATION_ACCOUNT")
    DATA_VIOLATION_ACCOUNT("DATA_VIOLATION_ACCOUNT"),
    @SerializedName("CERTIFICATION_POLICY_VIOLATION")
    CERTIFICATION_POLICY_VIOLATION("CERTIFICATION_POLICY_VIOLATION"),
    @SerializedName("MODIFY_BUS_ROLE_DEFN")
    MODIFY_BUS_ROLE_DEFN("MODIFY_BUS_ROLE_DEFN"),
    @SerializedName("REMOVE_TECH_ROLE_ASSIGNMENT")
    REMOVE_TECH_ROLE_ASSIGNMENT("REMOVE_TECH_ROLE_ASSIGNMENT"),
    @SerializedName("REMOVE_BUS_ROLE_AUTHS")
    REMOVE_BUS_ROLE_AUTHS("REMOVE_BUS_ROLE_AUTHS"),
    @SerializedName("MODIFY_TECH_ROLE_DEFN")
    MODIFY_TECH_ROLE_DEFN("MODIFY_TECH_ROLE_DEFN"),
    @SerializedName("ADD_BUSINESS_ROLE_MEMBERSHIP")
    ADD_BUSINESS_ROLE_MEMBERSHIP("ADD_BUSINESS_ROLE_MEMBERSHIP"),
    @SerializedName("REMOVE_BUSINESS_ROLE_MEMBERSHIP")
    REMOVE_BUSINESS_ROLE_MEMBERSHIP("REMOVE_BUSINESS_ROLE_MEMBERSHIP");

    private String value;

    ChangeRequestTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ChangeRequestTypeEnum fromValue(String input) {
      for (ChangeRequestTypeEnum b : ChangeRequestTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ChangeRequestTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ChangeRequestTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ChangeRequestTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ChangeRequestTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("changeRequestType")
  private ChangeRequestTypeEnum changeRequestType = null;

  /**
   * Gets or Sets changeType
   */
  @JsonAdapter(ChangeTypeEnum.Adapter.class)
  public enum ChangeTypeEnum {
    @SerializedName("EXTERNAL_WORKFLOW")
    EXTERNAL_WORKFLOW("EXTERNAL_WORKFLOW"),
    @SerializedName("IDENTITY_MANAGER")
    IDENTITY_MANAGER("IDENTITY_MANAGER"),
    @SerializedName("MANUAL")
    MANUAL("MANUAL"),
    @SerializedName("DAAS")
    DAAS("DAAS"),
    @SerializedName("INTERNAL")
    INTERNAL("INTERNAL");

    private String value;

    ChangeTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ChangeTypeEnum fromValue(String input) {
      for (ChangeTypeEnum b : ChangeTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ChangeTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ChangeTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ChangeTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ChangeTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("changeType")
  private ChangeTypeEnum changeType = null;

  @SerializedName("changesetId")
  private Long changesetId = null;

  @SerializedName("childFulfillmentStatusItems")
  private List<FulfillmentStatus> childFulfillmentStatusItems = null;

  @SerializedName("childFulfillmentStatusItemsSkipped")
  private Integer childFulfillmentStatusItemsSkipped = null;

  @SerializedName("comment")
  private String comment = null;

  @SerializedName("dataPolicyCriteria")
  private String dataPolicyCriteria = null;

  @SerializedName("dataPolicyDescription")
  private String dataPolicyDescription = null;

  @SerializedName("dataPolicyId")
  private Long dataPolicyId = null;

  @SerializedName("dataPolicyName")
  private String dataPolicyName = null;

  @SerializedName("extraInfo")
  private String extraInfo = null;

  @SerializedName("fallBackReason")
  private Integer fallBackReason = null;

  @SerializedName("flowdata")
  private String flowdata = null;

  @SerializedName("fulfillerDeleted")
  private Boolean fulfillerDeleted = null;

  @SerializedName("fulfillerId")
  private Long fulfillerId = null;

  @SerializedName("fulfillerName")
  private String fulfillerName = null;

  @SerializedName("fulfillerType")
  private String fulfillerType = null;

  @SerializedName("fulfillmentId")
  private Long fulfillmentId = null;

  @SerializedName("fulfillmentInstructions")
  private String fulfillmentInstructions = null;

  /**
   * Gets or Sets fulfillmentStatus
   */
  @JsonAdapter(FulfillmentStatusEnum.Adapter.class)
  public enum FulfillmentStatusEnum {
    @SerializedName("INITIALIZED")
    INITIALIZED("INITIALIZED"),
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("TIMED_OUT")
    TIMED_OUT("TIMED_OUT"),
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS("IN_PROGRESS"),
    @SerializedName("REFUSED")
    REFUSED("REFUSED"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("ERROR")
    ERROR("ERROR"),
    @SerializedName("VERIFIED")
    VERIFIED("VERIFIED"),
    @SerializedName("NOT_VERIFIED")
    NOT_VERIFIED("NOT_VERIFIED"),
    @SerializedName("VERIFICATION_IGNORED")
    VERIFICATION_IGNORED("VERIFICATION_IGNORED"),
    @SerializedName("VERIFICATION_TIMEOUT")
    VERIFICATION_TIMEOUT("VERIFICATION_TIMEOUT"),
    @SerializedName("RETRY")
    RETRY("RETRY");

    private String value;

    FulfillmentStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static FulfillmentStatusEnum fromValue(String input) {
      for (FulfillmentStatusEnum b : FulfillmentStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<FulfillmentStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FulfillmentStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public FulfillmentStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return FulfillmentStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("fulfillmentStatus")
  private FulfillmentStatusEnum fulfillmentStatus = null;

  @SerializedName("linkAccount")
  private String linkAccount = null;

  @SerializedName("linkApplication")
  private String linkApplication = null;

  @SerializedName("linkFulfiller")
  private String linkFulfiller = null;

  @SerializedName("linkHistory")
  private String linkHistory = null;

  @SerializedName("linkPermission")
  private String linkPermission = null;

  @SerializedName("linkUser")
  private String linkUser = null;

  /**
   * Gets or Sets objectType
   */
  @JsonAdapter(ObjectTypeEnum.Adapter.class)
  public enum ObjectTypeEnum {
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("ROLE")
    ROLE("ROLE"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("DIRECT_REPORT")
    DIRECT_REPORT("DIRECT_REPORT"),
    @SerializedName("DATA_POLICY_REMEDIATION")
    DATA_POLICY_REMEDIATION("DATA_POLICY_REMEDIATION"),
    @SerializedName("CERT_POLICY_REMEDIATION")
    CERT_POLICY_REMEDIATION("CERT_POLICY_REMEDIATION"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION");

    private String value;

    ObjectTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ObjectTypeEnum fromValue(String input) {
      for (ObjectTypeEnum b : ObjectTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ObjectTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ObjectTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ObjectTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ObjectTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("objectType")
  private ObjectTypeEnum objectType = null;

  @SerializedName("permId")
  private Long permId = null;

  @SerializedName("permName")
  private String permName = null;

  @SerializedName("permissionDeleted")
  private Boolean permissionDeleted = null;

  @SerializedName("processId")
  private String processId = null;

  @SerializedName("reason")
  private String reason = null;

  @SerializedName("reasonId")
  private Long reasonId = null;

  @SerializedName("reasonName")
  private String reasonName = null;

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

  @SerializedName("remediationRunId")
  private Long remediationRunId = null;

  @SerializedName("requesterName")
  private String requesterName = null;

  @SerializedName("reviewName")
  private String reviewName = null;

  @SerializedName("scriptId")
  private Long scriptId = null;

  @SerializedName("scriptName")
  private String scriptName = null;

  @SerializedName("sourceName")
  private String sourceName = null;

  @SerializedName("statusDate")
  private Long statusDate = null;

  @SerializedName("troleActive")
  private Boolean troleActive = null;

  @SerializedName("troleDeleted")
  private Boolean troleDeleted = null;

  @SerializedName("troleId")
  private Long troleId = null;

  @SerializedName("troleName")
  private String troleName = null;

  @SerializedName("userDeleted")
  private Boolean userDeleted = null;

  @SerializedName("userId")
  private Long userId = null;

  @SerializedName("userName")
  private String userName = null;

  @SerializedName("userUniqueId")
  private String userUniqueId = null;

  @SerializedName("values")
  private List<ItemValue> values = null;

  public FulfillmentStatus accountDeleted(Boolean accountDeleted) {
    this.accountDeleted = accountDeleted;
    return this;
  }

   /**
   * Get accountDeleted
   * @return accountDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isAccountDeleted() {
    return accountDeleted;
  }

  public void setAccountDeleted(Boolean accountDeleted) {
    this.accountDeleted = accountDeleted;
  }

  public FulfillmentStatus accountId(Long accountId) {
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

  public FulfillmentStatus accountName(String accountName) {
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

  public FulfillmentStatus actualFulfillerId(Long actualFulfillerId) {
    this.actualFulfillerId = actualFulfillerId;
    return this;
  }

   /**
   * Get actualFulfillerId
   * @return actualFulfillerId
  **/
  @ApiModelProperty(value = "")
  public Long getActualFulfillerId() {
    return actualFulfillerId;
  }

  public void setActualFulfillerId(Long actualFulfillerId) {
    this.actualFulfillerId = actualFulfillerId;
  }

  public FulfillmentStatus actualFulfillerLink(String actualFulfillerLink) {
    this.actualFulfillerLink = actualFulfillerLink;
    return this;
  }

   /**
   * Get actualFulfillerLink
   * @return actualFulfillerLink
  **/
  @ApiModelProperty(value = "")
  public String getActualFulfillerLink() {
    return actualFulfillerLink;
  }

  public void setActualFulfillerLink(String actualFulfillerLink) {
    this.actualFulfillerLink = actualFulfillerLink;
  }

  public FulfillmentStatus actualFulfillerName(String actualFulfillerName) {
    this.actualFulfillerName = actualFulfillerName;
    return this;
  }

   /**
   * Get actualFulfillerName
   * @return actualFulfillerName
  **/
  @ApiModelProperty(value = "")
  public String getActualFulfillerName() {
    return actualFulfillerName;
  }

  public void setActualFulfillerName(String actualFulfillerName) {
    this.actualFulfillerName = actualFulfillerName;
  }

  public FulfillmentStatus appId(Long appId) {
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

  public FulfillmentStatus appName(String appName) {
    this.appName = appName;
    return this;
  }

   /**
   * Get appName
   * @return appName
  **/
  @ApiModelProperty(value = "")
  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public FulfillmentStatus assignedUsers(List<Reviewer> assignedUsers) {
    this.assignedUsers = assignedUsers;
    return this;
  }

  public FulfillmentStatus addAssignedUsersItem(Reviewer assignedUsersItem) {
    if (this.assignedUsers == null) {
      this.assignedUsers = new ArrayList<Reviewer>();
    }
    this.assignedUsers.add(assignedUsersItem);
    return this;
  }

   /**
   * Get assignedUsers
   * @return assignedUsers
  **/
  @ApiModelProperty(value = "")
  public List<Reviewer> getAssignedUsers() {
    return assignedUsers;
  }

  public void setAssignedUsers(List<Reviewer> assignedUsers) {
    this.assignedUsers = assignedUsers;
  }

  public FulfillmentStatus broleId(Long broleId) {
    this.broleId = broleId;
    return this;
  }

   /**
   * Get broleId
   * @return broleId
  **/
  @ApiModelProperty(value = "")
  public Long getBroleId() {
    return broleId;
  }

  public void setBroleId(Long broleId) {
    this.broleId = broleId;
  }

  public FulfillmentStatus broleName(String broleName) {
    this.broleName = broleName;
    return this;
  }

   /**
   * Get broleName
   * @return broleName
  **/
  @ApiModelProperty(value = "")
  public String getBroleName() {
    return broleName;
  }

  public void setBroleName(String broleName) {
    this.broleName = broleName;
  }

  public FulfillmentStatus certPolicyDescription(String certPolicyDescription) {
    this.certPolicyDescription = certPolicyDescription;
    return this;
  }

   /**
   * Get certPolicyDescription
   * @return certPolicyDescription
  **/
  @ApiModelProperty(value = "")
  public String getCertPolicyDescription() {
    return certPolicyDescription;
  }

  public void setCertPolicyDescription(String certPolicyDescription) {
    this.certPolicyDescription = certPolicyDescription;
  }

  public FulfillmentStatus certPolicyId(Long certPolicyId) {
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

  public FulfillmentStatus certPolicyName(String certPolicyName) {
    this.certPolicyName = certPolicyName;
    return this;
  }

   /**
   * Get certPolicyName
   * @return certPolicyName
  **/
  @ApiModelProperty(value = "")
  public String getCertPolicyName() {
    return certPolicyName;
  }

  public void setCertPolicyName(String certPolicyName) {
    this.certPolicyName = certPolicyName;
  }

  public FulfillmentStatus changeItemAction(ChangeItemActionEnum changeItemAction) {
    this.changeItemAction = changeItemAction;
    return this;
  }

   /**
   * Get changeItemAction
   * @return changeItemAction
  **/
  @ApiModelProperty(value = "")
  public ChangeItemActionEnum getChangeItemAction() {
    return changeItemAction;
  }

  public void setChangeItemAction(ChangeItemActionEnum changeItemAction) {
    this.changeItemAction = changeItemAction;
  }

  public FulfillmentStatus changeItemId(Long changeItemId) {
    this.changeItemId = changeItemId;
    return this;
  }

   /**
   * Get changeItemId
   * @return changeItemId
  **/
  @ApiModelProperty(value = "")
  public Long getChangeItemId() {
    return changeItemId;
  }

  public void setChangeItemId(Long changeItemId) {
    this.changeItemId = changeItemId;
  }

  public FulfillmentStatus changeItemSource(ChangeItemSourceEnum changeItemSource) {
    this.changeItemSource = changeItemSource;
    return this;
  }

   /**
   * Get changeItemSource
   * @return changeItemSource
  **/
  @ApiModelProperty(value = "")
  public ChangeItemSourceEnum getChangeItemSource() {
    return changeItemSource;
  }

  public void setChangeItemSource(ChangeItemSourceEnum changeItemSource) {
    this.changeItemSource = changeItemSource;
  }

  public FulfillmentStatus changeRequestItemDate(Long changeRequestItemDate) {
    this.changeRequestItemDate = changeRequestItemDate;
    return this;
  }

   /**
   * Get changeRequestItemDate
   * @return changeRequestItemDate
  **/
  @ApiModelProperty(value = "")
  public Long getChangeRequestItemDate() {
    return changeRequestItemDate;
  }

  public void setChangeRequestItemDate(Long changeRequestItemDate) {
    this.changeRequestItemDate = changeRequestItemDate;
  }

  public FulfillmentStatus changeRequestType(ChangeRequestTypeEnum changeRequestType) {
    this.changeRequestType = changeRequestType;
    return this;
  }

   /**
   * Get changeRequestType
   * @return changeRequestType
  **/
  @ApiModelProperty(value = "")
  public ChangeRequestTypeEnum getChangeRequestType() {
    return changeRequestType;
  }

  public void setChangeRequestType(ChangeRequestTypeEnum changeRequestType) {
    this.changeRequestType = changeRequestType;
  }

  public FulfillmentStatus changeType(ChangeTypeEnum changeType) {
    this.changeType = changeType;
    return this;
  }

   /**
   * Get changeType
   * @return changeType
  **/
  @ApiModelProperty(value = "")
  public ChangeTypeEnum getChangeType() {
    return changeType;
  }

  public void setChangeType(ChangeTypeEnum changeType) {
    this.changeType = changeType;
  }

  public FulfillmentStatus changesetId(Long changesetId) {
    this.changesetId = changesetId;
    return this;
  }

   /**
   * Get changesetId
   * @return changesetId
  **/
  @ApiModelProperty(value = "")
  public Long getChangesetId() {
    return changesetId;
  }

  public void setChangesetId(Long changesetId) {
    this.changesetId = changesetId;
  }

  public FulfillmentStatus childFulfillmentStatusItems(List<FulfillmentStatus> childFulfillmentStatusItems) {
    this.childFulfillmentStatusItems = childFulfillmentStatusItems;
    return this;
  }

  public FulfillmentStatus addChildFulfillmentStatusItemsItem(FulfillmentStatus childFulfillmentStatusItemsItem) {
    if (this.childFulfillmentStatusItems == null) {
      this.childFulfillmentStatusItems = new ArrayList<FulfillmentStatus>();
    }
    this.childFulfillmentStatusItems.add(childFulfillmentStatusItemsItem);
    return this;
  }

   /**
   * Get childFulfillmentStatusItems
   * @return childFulfillmentStatusItems
  **/
  @ApiModelProperty(value = "")
  public List<FulfillmentStatus> getChildFulfillmentStatusItems() {
    return childFulfillmentStatusItems;
  }

  public void setChildFulfillmentStatusItems(List<FulfillmentStatus> childFulfillmentStatusItems) {
    this.childFulfillmentStatusItems = childFulfillmentStatusItems;
  }

  public FulfillmentStatus childFulfillmentStatusItemsSkipped(Integer childFulfillmentStatusItemsSkipped) {
    this.childFulfillmentStatusItemsSkipped = childFulfillmentStatusItemsSkipped;
    return this;
  }

   /**
   * Get childFulfillmentStatusItemsSkipped
   * @return childFulfillmentStatusItemsSkipped
  **/
  @ApiModelProperty(value = "")
  public Integer getChildFulfillmentStatusItemsSkipped() {
    return childFulfillmentStatusItemsSkipped;
  }

  public void setChildFulfillmentStatusItemsSkipped(Integer childFulfillmentStatusItemsSkipped) {
    this.childFulfillmentStatusItemsSkipped = childFulfillmentStatusItemsSkipped;
  }

  public FulfillmentStatus comment(String comment) {
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

  public FulfillmentStatus dataPolicyCriteria(String dataPolicyCriteria) {
    this.dataPolicyCriteria = dataPolicyCriteria;
    return this;
  }

   /**
   * Get dataPolicyCriteria
   * @return dataPolicyCriteria
  **/
  @ApiModelProperty(value = "")
  public String getDataPolicyCriteria() {
    return dataPolicyCriteria;
  }

  public void setDataPolicyCriteria(String dataPolicyCriteria) {
    this.dataPolicyCriteria = dataPolicyCriteria;
  }

  public FulfillmentStatus dataPolicyDescription(String dataPolicyDescription) {
    this.dataPolicyDescription = dataPolicyDescription;
    return this;
  }

   /**
   * Get dataPolicyDescription
   * @return dataPolicyDescription
  **/
  @ApiModelProperty(value = "")
  public String getDataPolicyDescription() {
    return dataPolicyDescription;
  }

  public void setDataPolicyDescription(String dataPolicyDescription) {
    this.dataPolicyDescription = dataPolicyDescription;
  }

  public FulfillmentStatus dataPolicyId(Long dataPolicyId) {
    this.dataPolicyId = dataPolicyId;
    return this;
  }

   /**
   * Get dataPolicyId
   * @return dataPolicyId
  **/
  @ApiModelProperty(value = "")
  public Long getDataPolicyId() {
    return dataPolicyId;
  }

  public void setDataPolicyId(Long dataPolicyId) {
    this.dataPolicyId = dataPolicyId;
  }

  public FulfillmentStatus dataPolicyName(String dataPolicyName) {
    this.dataPolicyName = dataPolicyName;
    return this;
  }

   /**
   * Get dataPolicyName
   * @return dataPolicyName
  **/
  @ApiModelProperty(value = "")
  public String getDataPolicyName() {
    return dataPolicyName;
  }

  public void setDataPolicyName(String dataPolicyName) {
    this.dataPolicyName = dataPolicyName;
  }

  public FulfillmentStatus extraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
    return this;
  }

   /**
   * Get extraInfo
   * @return extraInfo
  **/
  @ApiModelProperty(value = "")
  public String getExtraInfo() {
    return extraInfo;
  }

  public void setExtraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
  }

  public FulfillmentStatus fallBackReason(Integer fallBackReason) {
    this.fallBackReason = fallBackReason;
    return this;
  }

   /**
   * Get fallBackReason
   * @return fallBackReason
  **/
  @ApiModelProperty(value = "")
  public Integer getFallBackReason() {
    return fallBackReason;
  }

  public void setFallBackReason(Integer fallBackReason) {
    this.fallBackReason = fallBackReason;
  }

  public FulfillmentStatus flowdata(String flowdata) {
    this.flowdata = flowdata;
    return this;
  }

   /**
   * Get flowdata
   * @return flowdata
  **/
  @ApiModelProperty(value = "")
  public String getFlowdata() {
    return flowdata;
  }

  public void setFlowdata(String flowdata) {
    this.flowdata = flowdata;
  }

  public FulfillmentStatus fulfillerDeleted(Boolean fulfillerDeleted) {
    this.fulfillerDeleted = fulfillerDeleted;
    return this;
  }

   /**
   * Get fulfillerDeleted
   * @return fulfillerDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isFulfillerDeleted() {
    return fulfillerDeleted;
  }

  public void setFulfillerDeleted(Boolean fulfillerDeleted) {
    this.fulfillerDeleted = fulfillerDeleted;
  }

  public FulfillmentStatus fulfillerId(Long fulfillerId) {
    this.fulfillerId = fulfillerId;
    return this;
  }

   /**
   * Get fulfillerId
   * @return fulfillerId
  **/
  @ApiModelProperty(value = "")
  public Long getFulfillerId() {
    return fulfillerId;
  }

  public void setFulfillerId(Long fulfillerId) {
    this.fulfillerId = fulfillerId;
  }

  public FulfillmentStatus fulfillerName(String fulfillerName) {
    this.fulfillerName = fulfillerName;
    return this;
  }

   /**
   * Get fulfillerName
   * @return fulfillerName
  **/
  @ApiModelProperty(value = "")
  public String getFulfillerName() {
    return fulfillerName;
  }

  public void setFulfillerName(String fulfillerName) {
    this.fulfillerName = fulfillerName;
  }

  public FulfillmentStatus fulfillerType(String fulfillerType) {
    this.fulfillerType = fulfillerType;
    return this;
  }

   /**
   * Get fulfillerType
   * @return fulfillerType
  **/
  @ApiModelProperty(value = "")
  public String getFulfillerType() {
    return fulfillerType;
  }

  public void setFulfillerType(String fulfillerType) {
    this.fulfillerType = fulfillerType;
  }

  public FulfillmentStatus fulfillmentId(Long fulfillmentId) {
    this.fulfillmentId = fulfillmentId;
    return this;
  }

   /**
   * Get fulfillmentId
   * @return fulfillmentId
  **/
  @ApiModelProperty(value = "")
  public Long getFulfillmentId() {
    return fulfillmentId;
  }

  public void setFulfillmentId(Long fulfillmentId) {
    this.fulfillmentId = fulfillmentId;
  }

  public FulfillmentStatus fulfillmentInstructions(String fulfillmentInstructions) {
    this.fulfillmentInstructions = fulfillmentInstructions;
    return this;
  }

   /**
   * Get fulfillmentInstructions
   * @return fulfillmentInstructions
  **/
  @ApiModelProperty(value = "")
  public String getFulfillmentInstructions() {
    return fulfillmentInstructions;
  }

  public void setFulfillmentInstructions(String fulfillmentInstructions) {
    this.fulfillmentInstructions = fulfillmentInstructions;
  }

  public FulfillmentStatus fulfillmentStatus(FulfillmentStatusEnum fulfillmentStatus) {
    this.fulfillmentStatus = fulfillmentStatus;
    return this;
  }

   /**
   * Get fulfillmentStatus
   * @return fulfillmentStatus
  **/
  @ApiModelProperty(value = "")
  public FulfillmentStatusEnum getFulfillmentStatus() {
    return fulfillmentStatus;
  }

  public void setFulfillmentStatus(FulfillmentStatusEnum fulfillmentStatus) {
    this.fulfillmentStatus = fulfillmentStatus;
  }

  public FulfillmentStatus linkAccount(String linkAccount) {
    this.linkAccount = linkAccount;
    return this;
  }

   /**
   * Get linkAccount
   * @return linkAccount
  **/
  @ApiModelProperty(value = "")
  public String getLinkAccount() {
    return linkAccount;
  }

  public void setLinkAccount(String linkAccount) {
    this.linkAccount = linkAccount;
  }

  public FulfillmentStatus linkApplication(String linkApplication) {
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

  public FulfillmentStatus linkFulfiller(String linkFulfiller) {
    this.linkFulfiller = linkFulfiller;
    return this;
  }

   /**
   * Get linkFulfiller
   * @return linkFulfiller
  **/
  @ApiModelProperty(value = "")
  public String getLinkFulfiller() {
    return linkFulfiller;
  }

  public void setLinkFulfiller(String linkFulfiller) {
    this.linkFulfiller = linkFulfiller;
  }

  public FulfillmentStatus linkHistory(String linkHistory) {
    this.linkHistory = linkHistory;
    return this;
  }

   /**
   * Get linkHistory
   * @return linkHistory
  **/
  @ApiModelProperty(value = "")
  public String getLinkHistory() {
    return linkHistory;
  }

  public void setLinkHistory(String linkHistory) {
    this.linkHistory = linkHistory;
  }

  public FulfillmentStatus linkPermission(String linkPermission) {
    this.linkPermission = linkPermission;
    return this;
  }

   /**
   * Get linkPermission
   * @return linkPermission
  **/
  @ApiModelProperty(value = "")
  public String getLinkPermission() {
    return linkPermission;
  }

  public void setLinkPermission(String linkPermission) {
    this.linkPermission = linkPermission;
  }

  public FulfillmentStatus linkUser(String linkUser) {
    this.linkUser = linkUser;
    return this;
  }

   /**
   * Get linkUser
   * @return linkUser
  **/
  @ApiModelProperty(value = "")
  public String getLinkUser() {
    return linkUser;
  }

  public void setLinkUser(String linkUser) {
    this.linkUser = linkUser;
  }

  public FulfillmentStatus objectType(ObjectTypeEnum objectType) {
    this.objectType = objectType;
    return this;
  }

   /**
   * Get objectType
   * @return objectType
  **/
  @ApiModelProperty(value = "")
  public ObjectTypeEnum getObjectType() {
    return objectType;
  }

  public void setObjectType(ObjectTypeEnum objectType) {
    this.objectType = objectType;
  }

  public FulfillmentStatus permId(Long permId) {
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

  public FulfillmentStatus permName(String permName) {
    this.permName = permName;
    return this;
  }

   /**
   * Get permName
   * @return permName
  **/
  @ApiModelProperty(value = "")
  public String getPermName() {
    return permName;
  }

  public void setPermName(String permName) {
    this.permName = permName;
  }

  public FulfillmentStatus permissionDeleted(Boolean permissionDeleted) {
    this.permissionDeleted = permissionDeleted;
    return this;
  }

   /**
   * Get permissionDeleted
   * @return permissionDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isPermissionDeleted() {
    return permissionDeleted;
  }

  public void setPermissionDeleted(Boolean permissionDeleted) {
    this.permissionDeleted = permissionDeleted;
  }

  public FulfillmentStatus processId(String processId) {
    this.processId = processId;
    return this;
  }

   /**
   * Get processId
   * @return processId
  **/
  @ApiModelProperty(value = "")
  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
  }

  public FulfillmentStatus reason(String reason) {
    this.reason = reason;
    return this;
  }

   /**
   * Get reason
   * @return reason
  **/
  @ApiModelProperty(value = "")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public FulfillmentStatus reasonId(Long reasonId) {
    this.reasonId = reasonId;
    return this;
  }

   /**
   * Get reasonId
   * @return reasonId
  **/
  @ApiModelProperty(value = "")
  public Long getReasonId() {
    return reasonId;
  }

  public void setReasonId(Long reasonId) {
    this.reasonId = reasonId;
  }

  public FulfillmentStatus reasonName(String reasonName) {
    this.reasonName = reasonName;
    return this;
  }

   /**
   * Get reasonName
   * @return reasonName
  **/
  @ApiModelProperty(value = "")
  public String getReasonName() {
    return reasonName;
  }

  public void setReasonName(String reasonName) {
    this.reasonName = reasonName;
  }

  public FulfillmentStatus relationToUserType(RelationToUserTypeEnum relationToUserType) {
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

  public FulfillmentStatus remediationRunId(Long remediationRunId) {
    this.remediationRunId = remediationRunId;
    return this;
  }

   /**
   * Get remediationRunId
   * @return remediationRunId
  **/
  @ApiModelProperty(value = "")
  public Long getRemediationRunId() {
    return remediationRunId;
  }

  public void setRemediationRunId(Long remediationRunId) {
    this.remediationRunId = remediationRunId;
  }

  public FulfillmentStatus requesterName(String requesterName) {
    this.requesterName = requesterName;
    return this;
  }

   /**
   * Get requesterName
   * @return requesterName
  **/
  @ApiModelProperty(value = "")
  public String getRequesterName() {
    return requesterName;
  }

  public void setRequesterName(String requesterName) {
    this.requesterName = requesterName;
  }

  public FulfillmentStatus reviewName(String reviewName) {
    this.reviewName = reviewName;
    return this;
  }

   /**
   * Get reviewName
   * @return reviewName
  **/
  @ApiModelProperty(value = "")
  public String getReviewName() {
    return reviewName;
  }

  public void setReviewName(String reviewName) {
    this.reviewName = reviewName;
  }

  public FulfillmentStatus scriptId(Long scriptId) {
    this.scriptId = scriptId;
    return this;
  }

   /**
   * Get scriptId
   * @return scriptId
  **/
  @ApiModelProperty(value = "")
  public Long getScriptId() {
    return scriptId;
  }

  public void setScriptId(Long scriptId) {
    this.scriptId = scriptId;
  }

  public FulfillmentStatus scriptName(String scriptName) {
    this.scriptName = scriptName;
    return this;
  }

   /**
   * Get scriptName
   * @return scriptName
  **/
  @ApiModelProperty(value = "")
  public String getScriptName() {
    return scriptName;
  }

  public void setScriptName(String scriptName) {
    this.scriptName = scriptName;
  }

  public FulfillmentStatus sourceName(String sourceName) {
    this.sourceName = sourceName;
    return this;
  }

   /**
   * Get sourceName
   * @return sourceName
  **/
  @ApiModelProperty(value = "")
  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public FulfillmentStatus statusDate(Long statusDate) {
    this.statusDate = statusDate;
    return this;
  }

   /**
   * Get statusDate
   * @return statusDate
  **/
  @ApiModelProperty(value = "")
  public Long getStatusDate() {
    return statusDate;
  }

  public void setStatusDate(Long statusDate) {
    this.statusDate = statusDate;
  }

  public FulfillmentStatus troleActive(Boolean troleActive) {
    this.troleActive = troleActive;
    return this;
  }

   /**
   * Get troleActive
   * @return troleActive
  **/
  @ApiModelProperty(value = "")
  public Boolean isTroleActive() {
    return troleActive;
  }

  public void setTroleActive(Boolean troleActive) {
    this.troleActive = troleActive;
  }

  public FulfillmentStatus troleDeleted(Boolean troleDeleted) {
    this.troleDeleted = troleDeleted;
    return this;
  }

   /**
   * Get troleDeleted
   * @return troleDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isTroleDeleted() {
    return troleDeleted;
  }

  public void setTroleDeleted(Boolean troleDeleted) {
    this.troleDeleted = troleDeleted;
  }

  public FulfillmentStatus troleId(Long troleId) {
    this.troleId = troleId;
    return this;
  }

   /**
   * Get troleId
   * @return troleId
  **/
  @ApiModelProperty(value = "")
  public Long getTroleId() {
    return troleId;
  }

  public void setTroleId(Long troleId) {
    this.troleId = troleId;
  }

  public FulfillmentStatus troleName(String troleName) {
    this.troleName = troleName;
    return this;
  }

   /**
   * Get troleName
   * @return troleName
  **/
  @ApiModelProperty(value = "")
  public String getTroleName() {
    return troleName;
  }

  public void setTroleName(String troleName) {
    this.troleName = troleName;
  }

  public FulfillmentStatus userDeleted(Boolean userDeleted) {
    this.userDeleted = userDeleted;
    return this;
  }

   /**
   * Get userDeleted
   * @return userDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isUserDeleted() {
    return userDeleted;
  }

  public void setUserDeleted(Boolean userDeleted) {
    this.userDeleted = userDeleted;
  }

  public FulfillmentStatus userId(Long userId) {
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

  public FulfillmentStatus userName(String userName) {
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

  public FulfillmentStatus userUniqueId(String userUniqueId) {
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

  public FulfillmentStatus values(List<ItemValue> values) {
    this.values = values;
    return this;
  }

  public FulfillmentStatus addValuesItem(ItemValue valuesItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FulfillmentStatus fulfillmentStatus = (FulfillmentStatus) o;
    return Objects.equals(this.accountDeleted, fulfillmentStatus.accountDeleted) &&
        Objects.equals(this.accountId, fulfillmentStatus.accountId) &&
        Objects.equals(this.accountName, fulfillmentStatus.accountName) &&
        Objects.equals(this.actualFulfillerId, fulfillmentStatus.actualFulfillerId) &&
        Objects.equals(this.actualFulfillerLink, fulfillmentStatus.actualFulfillerLink) &&
        Objects.equals(this.actualFulfillerName, fulfillmentStatus.actualFulfillerName) &&
        Objects.equals(this.appId, fulfillmentStatus.appId) &&
        Objects.equals(this.appName, fulfillmentStatus.appName) &&
        Objects.equals(this.assignedUsers, fulfillmentStatus.assignedUsers) &&
        Objects.equals(this.broleId, fulfillmentStatus.broleId) &&
        Objects.equals(this.broleName, fulfillmentStatus.broleName) &&
        Objects.equals(this.certPolicyDescription, fulfillmentStatus.certPolicyDescription) &&
        Objects.equals(this.certPolicyId, fulfillmentStatus.certPolicyId) &&
        Objects.equals(this.certPolicyName, fulfillmentStatus.certPolicyName) &&
        Objects.equals(this.changeItemAction, fulfillmentStatus.changeItemAction) &&
        Objects.equals(this.changeItemId, fulfillmentStatus.changeItemId) &&
        Objects.equals(this.changeItemSource, fulfillmentStatus.changeItemSource) &&
        Objects.equals(this.changeRequestItemDate, fulfillmentStatus.changeRequestItemDate) &&
        Objects.equals(this.changeRequestType, fulfillmentStatus.changeRequestType) &&
        Objects.equals(this.changeType, fulfillmentStatus.changeType) &&
        Objects.equals(this.changesetId, fulfillmentStatus.changesetId) &&
        Objects.equals(this.childFulfillmentStatusItems, fulfillmentStatus.childFulfillmentStatusItems) &&
        Objects.equals(this.childFulfillmentStatusItemsSkipped, fulfillmentStatus.childFulfillmentStatusItemsSkipped) &&
        Objects.equals(this.comment, fulfillmentStatus.comment) &&
        Objects.equals(this.dataPolicyCriteria, fulfillmentStatus.dataPolicyCriteria) &&
        Objects.equals(this.dataPolicyDescription, fulfillmentStatus.dataPolicyDescription) &&
        Objects.equals(this.dataPolicyId, fulfillmentStatus.dataPolicyId) &&
        Objects.equals(this.dataPolicyName, fulfillmentStatus.dataPolicyName) &&
        Objects.equals(this.extraInfo, fulfillmentStatus.extraInfo) &&
        Objects.equals(this.fallBackReason, fulfillmentStatus.fallBackReason) &&
        Objects.equals(this.flowdata, fulfillmentStatus.flowdata) &&
        Objects.equals(this.fulfillerDeleted, fulfillmentStatus.fulfillerDeleted) &&
        Objects.equals(this.fulfillerId, fulfillmentStatus.fulfillerId) &&
        Objects.equals(this.fulfillerName, fulfillmentStatus.fulfillerName) &&
        Objects.equals(this.fulfillerType, fulfillmentStatus.fulfillerType) &&
        Objects.equals(this.fulfillmentId, fulfillmentStatus.fulfillmentId) &&
        Objects.equals(this.fulfillmentInstructions, fulfillmentStatus.fulfillmentInstructions) &&
        Objects.equals(this.fulfillmentStatus, fulfillmentStatus.fulfillmentStatus) &&
        Objects.equals(this.linkAccount, fulfillmentStatus.linkAccount) &&
        Objects.equals(this.linkApplication, fulfillmentStatus.linkApplication) &&
        Objects.equals(this.linkFulfiller, fulfillmentStatus.linkFulfiller) &&
        Objects.equals(this.linkHistory, fulfillmentStatus.linkHistory) &&
        Objects.equals(this.linkPermission, fulfillmentStatus.linkPermission) &&
        Objects.equals(this.linkUser, fulfillmentStatus.linkUser) &&
        Objects.equals(this.objectType, fulfillmentStatus.objectType) &&
        Objects.equals(this.permId, fulfillmentStatus.permId) &&
        Objects.equals(this.permName, fulfillmentStatus.permName) &&
        Objects.equals(this.permissionDeleted, fulfillmentStatus.permissionDeleted) &&
        Objects.equals(this.processId, fulfillmentStatus.processId) &&
        Objects.equals(this.reason, fulfillmentStatus.reason) &&
        Objects.equals(this.reasonId, fulfillmentStatus.reasonId) &&
        Objects.equals(this.reasonName, fulfillmentStatus.reasonName) &&
        Objects.equals(this.relationToUserType, fulfillmentStatus.relationToUserType) &&
        Objects.equals(this.remediationRunId, fulfillmentStatus.remediationRunId) &&
        Objects.equals(this.requesterName, fulfillmentStatus.requesterName) &&
        Objects.equals(this.reviewName, fulfillmentStatus.reviewName) &&
        Objects.equals(this.scriptId, fulfillmentStatus.scriptId) &&
        Objects.equals(this.scriptName, fulfillmentStatus.scriptName) &&
        Objects.equals(this.sourceName, fulfillmentStatus.sourceName) &&
        Objects.equals(this.statusDate, fulfillmentStatus.statusDate) &&
        Objects.equals(this.troleActive, fulfillmentStatus.troleActive) &&
        Objects.equals(this.troleDeleted, fulfillmentStatus.troleDeleted) &&
        Objects.equals(this.troleId, fulfillmentStatus.troleId) &&
        Objects.equals(this.troleName, fulfillmentStatus.troleName) &&
        Objects.equals(this.userDeleted, fulfillmentStatus.userDeleted) &&
        Objects.equals(this.userId, fulfillmentStatus.userId) &&
        Objects.equals(this.userName, fulfillmentStatus.userName) &&
        Objects.equals(this.userUniqueId, fulfillmentStatus.userUniqueId) &&
        Objects.equals(this.values, fulfillmentStatus.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountDeleted, accountId, accountName, actualFulfillerId, actualFulfillerLink, actualFulfillerName, appId, appName, assignedUsers, broleId, broleName, certPolicyDescription, certPolicyId, certPolicyName, changeItemAction, changeItemId, changeItemSource, changeRequestItemDate, changeRequestType, changeType, changesetId, childFulfillmentStatusItems, childFulfillmentStatusItemsSkipped, comment, dataPolicyCriteria, dataPolicyDescription, dataPolicyId, dataPolicyName, extraInfo, fallBackReason, flowdata, fulfillerDeleted, fulfillerId, fulfillerName, fulfillerType, fulfillmentId, fulfillmentInstructions, fulfillmentStatus, linkAccount, linkApplication, linkFulfiller, linkHistory, linkPermission, linkUser, objectType, permId, permName, permissionDeleted, processId, reason, reasonId, reasonName, relationToUserType, remediationRunId, requesterName, reviewName, scriptId, scriptName, sourceName, statusDate, troleActive, troleDeleted, troleId, troleName, userDeleted, userId, userName, userUniqueId, values);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FulfillmentStatus {\n");
    
    sb.append("    accountDeleted: ").append(toIndentedString(accountDeleted)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    actualFulfillerId: ").append(toIndentedString(actualFulfillerId)).append("\n");
    sb.append("    actualFulfillerLink: ").append(toIndentedString(actualFulfillerLink)).append("\n");
    sb.append("    actualFulfillerName: ").append(toIndentedString(actualFulfillerName)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    appName: ").append(toIndentedString(appName)).append("\n");
    sb.append("    assignedUsers: ").append(toIndentedString(assignedUsers)).append("\n");
    sb.append("    broleId: ").append(toIndentedString(broleId)).append("\n");
    sb.append("    broleName: ").append(toIndentedString(broleName)).append("\n");
    sb.append("    certPolicyDescription: ").append(toIndentedString(certPolicyDescription)).append("\n");
    sb.append("    certPolicyId: ").append(toIndentedString(certPolicyId)).append("\n");
    sb.append("    certPolicyName: ").append(toIndentedString(certPolicyName)).append("\n");
    sb.append("    changeItemAction: ").append(toIndentedString(changeItemAction)).append("\n");
    sb.append("    changeItemId: ").append(toIndentedString(changeItemId)).append("\n");
    sb.append("    changeItemSource: ").append(toIndentedString(changeItemSource)).append("\n");
    sb.append("    changeRequestItemDate: ").append(toIndentedString(changeRequestItemDate)).append("\n");
    sb.append("    changeRequestType: ").append(toIndentedString(changeRequestType)).append("\n");
    sb.append("    changeType: ").append(toIndentedString(changeType)).append("\n");
    sb.append("    changesetId: ").append(toIndentedString(changesetId)).append("\n");
    sb.append("    childFulfillmentStatusItems: ").append(toIndentedString(childFulfillmentStatusItems)).append("\n");
    sb.append("    childFulfillmentStatusItemsSkipped: ").append(toIndentedString(childFulfillmentStatusItemsSkipped)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    dataPolicyCriteria: ").append(toIndentedString(dataPolicyCriteria)).append("\n");
    sb.append("    dataPolicyDescription: ").append(toIndentedString(dataPolicyDescription)).append("\n");
    sb.append("    dataPolicyId: ").append(toIndentedString(dataPolicyId)).append("\n");
    sb.append("    dataPolicyName: ").append(toIndentedString(dataPolicyName)).append("\n");
    sb.append("    extraInfo: ").append(toIndentedString(extraInfo)).append("\n");
    sb.append("    fallBackReason: ").append(toIndentedString(fallBackReason)).append("\n");
    sb.append("    flowdata: ").append(toIndentedString(flowdata)).append("\n");
    sb.append("    fulfillerDeleted: ").append(toIndentedString(fulfillerDeleted)).append("\n");
    sb.append("    fulfillerId: ").append(toIndentedString(fulfillerId)).append("\n");
    sb.append("    fulfillerName: ").append(toIndentedString(fulfillerName)).append("\n");
    sb.append("    fulfillerType: ").append(toIndentedString(fulfillerType)).append("\n");
    sb.append("    fulfillmentId: ").append(toIndentedString(fulfillmentId)).append("\n");
    sb.append("    fulfillmentInstructions: ").append(toIndentedString(fulfillmentInstructions)).append("\n");
    sb.append("    fulfillmentStatus: ").append(toIndentedString(fulfillmentStatus)).append("\n");
    sb.append("    linkAccount: ").append(toIndentedString(linkAccount)).append("\n");
    sb.append("    linkApplication: ").append(toIndentedString(linkApplication)).append("\n");
    sb.append("    linkFulfiller: ").append(toIndentedString(linkFulfiller)).append("\n");
    sb.append("    linkHistory: ").append(toIndentedString(linkHistory)).append("\n");
    sb.append("    linkPermission: ").append(toIndentedString(linkPermission)).append("\n");
    sb.append("    linkUser: ").append(toIndentedString(linkUser)).append("\n");
    sb.append("    objectType: ").append(toIndentedString(objectType)).append("\n");
    sb.append("    permId: ").append(toIndentedString(permId)).append("\n");
    sb.append("    permName: ").append(toIndentedString(permName)).append("\n");
    sb.append("    permissionDeleted: ").append(toIndentedString(permissionDeleted)).append("\n");
    sb.append("    processId: ").append(toIndentedString(processId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    reasonId: ").append(toIndentedString(reasonId)).append("\n");
    sb.append("    reasonName: ").append(toIndentedString(reasonName)).append("\n");
    sb.append("    relationToUserType: ").append(toIndentedString(relationToUserType)).append("\n");
    sb.append("    remediationRunId: ").append(toIndentedString(remediationRunId)).append("\n");
    sb.append("    requesterName: ").append(toIndentedString(requesterName)).append("\n");
    sb.append("    reviewName: ").append(toIndentedString(reviewName)).append("\n");
    sb.append("    scriptId: ").append(toIndentedString(scriptId)).append("\n");
    sb.append("    scriptName: ").append(toIndentedString(scriptName)).append("\n");
    sb.append("    sourceName: ").append(toIndentedString(sourceName)).append("\n");
    sb.append("    statusDate: ").append(toIndentedString(statusDate)).append("\n");
    sb.append("    troleActive: ").append(toIndentedString(troleActive)).append("\n");
    sb.append("    troleDeleted: ").append(toIndentedString(troleDeleted)).append("\n");
    sb.append("    troleId: ").append(toIndentedString(troleId)).append("\n");
    sb.append("    troleName: ").append(toIndentedString(troleName)).append("\n");
    sb.append("    userDeleted: ").append(toIndentedString(userDeleted)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    userUniqueId: ").append(toIndentedString(userUniqueId)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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
