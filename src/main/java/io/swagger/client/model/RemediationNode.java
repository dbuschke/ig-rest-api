/*
 * Identity Governance REST APIs
 * Welcome to the NetIQ Identity Governance API Documentation page. This is the API reference for the NetIQ Identity Governance REST API.  Below you will see the main sections of the API. Click each section in order to see the endpoints that are available in that category as well as information related to which Identity Governance roles have access. Global Administrators are not included in the accepted roles list for each API however they have access to all APIs. Those APIs that do not display a list of accepted roles are accessible for any role.  The NetIQ Identity Governance REST API uses the OAuth2 protocol for authentication. Therefore, in order to make any of these calls, you must obtain a token, and include that token in the Authentication header.  OSP = One SSO Provider   NAM = NetIQ Access Manager  **Note:** The various OAuth 2.0 endpoints described below can also be obtained from the OAuth/OpenID Connect provider 'metadata' found at the following location relative to the 'issuer URI':`[issuer-uri]/.well-known/openid-configuration`  Issuer URIs:  *   OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2 *   NAM: https://server/nidp/oauth/nam  See [Open ID Connect Discovery 1.0](\"https://openid.net/specs/openid-connect-discovery-1_0.html\") for more information.  Obtaining the Initial Access Token ==================================  ### OAuth 2.0 Resource Owner Password Credentials Grant Request  1.  Determine the OAuth 2.0 token endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/token     2.  NAM: https://server/nidp/oauth/nam/token 2.  Obtain the Identity Governance 'iac' client identifier and client secret.     1.  OSP         1.  The identifier is usually _iac_ but can be changed with the configutil or configupdate utilities.         2.  The client secret is the 'service password' specified during installation but can be changed with the configutil or configupdate utilities.     2.  NAM         1.  Open the Access Manager administrator console in a browser and navigate to the _OAuth & OpenID Connect_ tab on the _IDPCluster_ page. Select the _Client Applications_ heading.         2.  Click on the 'View' icon under the 'Actions' heading for the _Client Application_ named _iac_.         3.  Click on _Click to reveal_.         4.  Copy the _Client ID_ value and the _Client Secret_ value.         5.  Ensure that _Resource Owner Credentials_ appears in the _Grants Required_ list. If not, edit the client definition and check the _Resource Owner Credentials_ box, save the client definition, and update the IDP. 3.  Obtain the user identifier and password of a user with the required privilege for the desired API endpoint. 4.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 4.3.1](\"https://tools.ietf.org/html/rfc6749#section-4.3.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=password&username=<user-id>&password=[user-password]&client_id=[iac-client-id]&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the client and user values obtained in the steps above.  Also note the '**& amp;**' shown in this and other POST payload examples should actually be '**&**'. 5.  Issue the request to the OAuth 2.0 token endpoint. 6.  The JSON response will be similar to the following (see [RFC 6749 section 4.3.3](\"https://tools.ietf.org/html/rfc6749#section-4.3.3\")):`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119, \"refresh_token\": \"eyJhbGciOiJ...\" }` 7.  When issuing a REST request to an Identity Governance endpoint pass the access token value using an HTTP _Bearer_ authorization header (see [RFC 6750 section 2.1](\"https://tools.ietf.org/html/rfc6750#section-2.1\")):`Authorization: Bearer [access-token]`  Refresh Tokens ==============  If the authorization server is configured to return an OAuth 2.0 refresh token in the JSON result of the Resource Owner Password Credential Grant request then the refresh token should be used to obtain additional access tokens after the currently-valid access token expires.  In addition, each refresh token issued on behalf of a user causes a 'revocation entry' to be stored in an attribute on the user's LDAP object. Obtaining many refresh tokens without revoking previously obtained, unexpired refresh tokens will eventually exceed the capacity of the attribute on the user's LDAP object and will result in errors.  Therefore, if a refresh token is obtained it must be revoked after it is no longer needed.  ### Access Token Request  1.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 6](\"https://tools.ietf.org/html/rfc6749#section-6\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=refresh_token&refresh_token=<refresh-token>&client_id=<iac-clientid>&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the obvious values. 2.  Issue the request to the OAuth 2.0 token endpoint. 3.  The JSON result will be similar to`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119 }` 4.  Use the new access token value in requests to Identity Governance REST endpoints as described above.  ### Refresh Token Revocation Request  1.  Determine the OAuth 2.0 token revocation endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/revoke     2.  NAM: https://server/nidp/oauth/nam/revoke 2.  Create an HTTP POST request with the following characteristics (see [RFC 7009 section 2.1](\"https://tools.ietf.org/html/rfc7009#section-2.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body:`token=[refresh-token]&client_id=[iac-client-id]&client_secret=[iac-client-secret]` 3.  Issue the request to the OAuth 2.0 revocation endpoint.      As a shortcut to learning the API, run Identity Governance in a browser with developer tools enabled and watch the network traffic between the browser and the server.  * * *
 *
 * OpenAPI spec version: 3.7.3-202
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
import io.swagger.client.model.Entity;
import io.swagger.client.model.RemediationRunNode;
import io.swagger.client.model.ReviewDef;
import io.swagger.client.model.TypeCount;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * RemediationNode
 */



public class RemediationNode {
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
    MODIFY_TECH_ROLE_DEFN("MODIFY_TECH_ROLE_DEFN");

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

  @SerializedName("createdDate")
  private Long createdDate = null;

  @SerializedName("details")
  private String details = null;

  @SerializedName("emailTemplateId")
  private String emailTemplateId = null;

  @SerializedName("emailTo")
  private List<Entity> emailTo = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("isActive")
  private Boolean isActive = null;

  @SerializedName("lastActionDate")
  private Long lastActionDate = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("policyCriteria")
  private String policyCriteria = null;

  @SerializedName("policyDescription")
  private String policyDescription = null;

  @SerializedName("policyId")
  private Long policyId = null;

  @SerializedName("policyName")
  private String policyName = null;

  @SerializedName("policySecondCriteria")
  private String policySecondCriteria = null;

  /**
   * Gets or Sets policyType
   */
  @JsonAdapter(PolicyTypeEnum.Adapter.class)
  public enum PolicyTypeEnum {
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("GROUP")
    GROUP("GROUP"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("ACCOUNT_USER")
    ACCOUNT_USER("ACCOUNT_USER"),
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("PERMISSION_TO_HOLDERS")
    PERMISSION_TO_HOLDERS("PERMISSION_TO_HOLDERS"),
    @SerializedName("PERMISSIONS_FROM_HOLDER")
    PERMISSIONS_FROM_HOLDER("PERMISSIONS_FROM_HOLDER"),
    @SerializedName("PERMISSION_HIERARCHY_TO_CHILD")
    PERMISSION_HIERARCHY_TO_CHILD("PERMISSION_HIERARCHY_TO_CHILD"),
    @SerializedName("PERMISSION_HIERARCHY_TO_PARENT")
    PERMISSION_HIERARCHY_TO_PARENT("PERMISSION_HIERARCHY_TO_PARENT"),
    @SerializedName("GROUPMEMBERSHIP")
    GROUPMEMBERSHIP("GROUPMEMBERSHIP"),
    @SerializedName("GROUPTOGROUP")
    GROUPTOGROUP("GROUPTOGROUP"),
    @SerializedName("REVIEW_DEFINITION")
    REVIEW_DEFINITION("REVIEW_DEFINITION"),
    @SerializedName("REVIEWINSTANCE")
    REVIEWINSTANCE("REVIEWINSTANCE"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION"),
    @SerializedName("IDENTITY_SOURCE")
    IDENTITY_SOURCE("IDENTITY_SOURCE"),
    @SerializedName("IDMDRIVER")
    IDMDRIVER("IDMDRIVER"),
    @SerializedName("NAV_ITEM")
    NAV_ITEM("NAV_ITEM"),
    @SerializedName("REVIEW_ACT_ITEM")
    REVIEW_ACT_ITEM("REVIEW_ACT_ITEM"),
    @SerializedName("ATTRIBUTE_DEFINITION")
    ATTRIBUTE_DEFINITION("ATTRIBUTE_DEFINITION"),
    @SerializedName("DATA_SOURCE")
    DATA_SOURCE("DATA_SOURCE"),
    @SerializedName("ENTITY_SCHEMA")
    ENTITY_SCHEMA("ENTITY_SCHEMA"),
    @SerializedName("CHANGE_REQUEST_ITEM")
    CHANGE_REQUEST_ITEM("CHANGE_REQUEST_ITEM"),
    @SerializedName("TAG")
    TAG("TAG"),
    @SerializedName("AUTH_ROLE")
    AUTH_ROLE("AUTH_ROLE"),
    @SerializedName("SCHEDULE")
    SCHEDULE("SCHEDULE"),
    @SerializedName("ROLE_POLICY")
    ROLE_POLICY("ROLE_POLICY"),
    @SerializedName("SOD_POLICY")
    SOD_POLICY("SOD_POLICY"),
    @SerializedName("ACCOUNT_CATEGORY")
    ACCOUNT_CATEGORY("ACCOUNT_CATEGORY"),
    @SerializedName("SOD_CASE")
    SOD_CASE("SOD_CASE"),
    @SerializedName("ADVISOR_FEED")
    ADVISOR_FEED("ADVISOR_FEED"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("APPROVAL_POLICY")
    APPROVAL_POLICY("APPROVAL_POLICY"),
    @SerializedName("GOVERNANCE")
    GOVERNANCE("GOVERNANCE"),
    @SerializedName("COVERAGE_MAP")
    COVERAGE_MAP("COVERAGE_MAP"),
    @SerializedName("RISK_SCORE_CFG")
    RISK_SCORE_CFG("RISK_SCORE_CFG"),
    @SerializedName("ANALYTICS")
    ANALYTICS("ANALYTICS"),
    @SerializedName("ENTITY_CATEGORY")
    ENTITY_CATEGORY("ENTITY_CATEGORY"),
    @SerializedName("AFFILIATED_USER")
    AFFILIATED_USER("AFFILIATED_USER"),
    @SerializedName("SUPERVISING_USER")
    SUPERVISING_USER("SUPERVISING_USER"),
    @SerializedName("HOLDER_USER")
    HOLDER_USER("HOLDER_USER"),
    @SerializedName("PERMISSION_BOUND")
    PERMISSION_BOUND("PERMISSION_BOUND"),
    @SerializedName("SUB_PERMISSION")
    SUB_PERMISSION("SUB_PERMISSION"),
    @SerializedName("SUBORDINATE_APP")
    SUBORDINATE_APP("SUBORDINATE_APP"),
    @SerializedName("SUB_GROUP")
    SUB_GROUP("SUB_GROUP"),
    @SerializedName("NOTIFICATION_TEMPLATE")
    NOTIFICATION_TEMPLATE("NOTIFICATION_TEMPLATE"),
    @SerializedName("ACCESS_REQUEST_POLICY")
    ACCESS_REQUEST_POLICY("ACCESS_REQUEST_POLICY"),
    @SerializedName("ACCESS_REQUEST_APPROVAL_POLICY")
    ACCESS_REQUEST_APPROVAL_POLICY("ACCESS_REQUEST_APPROVAL_POLICY"),
    @SerializedName("LICENSE")
    LICENSE("LICENSE"),
    @SerializedName("DATA_POLICY")
    DATA_POLICY("DATA_POLICY"),
    @SerializedName("CERTIFICATION_POLICY")
    CERTIFICATION_POLICY("CERTIFICATION_POLICY"),
    @SerializedName("ANALYTICS_FACT")
    ANALYTICS_FACT("ANALYTICS_FACT"),
    @SerializedName("ANALYTICS_DASHBOARD")
    ANALYTICS_DASHBOARD("ANALYTICS_DASHBOARD"),
    @SerializedName("DECISION_SUPPORT_POLICY")
    DECISION_SUPPORT_POLICY("DECISION_SUPPORT_POLICY"),
    @SerializedName("TROUBLE_SHOOT_INFO")
    TROUBLE_SHOOT_INFO("TROUBLE_SHOOT_INFO"),
    @SerializedName("ARCHIVAL")
    ARCHIVAL("ARCHIVAL"),
    @SerializedName("MAINTENANCE")
    MAINTENANCE("MAINTENANCE"),
    @SerializedName("DATA_QUERY")
    DATA_QUERY("DATA_QUERY"),
    @SerializedName("ACCESS_REQUEST_ITEM")
    ACCESS_REQUEST_ITEM("ACCESS_REQUEST_ITEM"),
    @SerializedName("REVIEW_ITEM")
    REVIEW_ITEM("REVIEW_ITEM"),
    @SerializedName("FULFILLMENT_ITEM")
    FULFILLMENT_ITEM("FULFILLMENT_ITEM"),
    @SerializedName("PERMISSION_ASSIGNMENT")
    PERMISSION_ASSIGNMENT("PERMISSION_ASSIGNMENT"),
    @SerializedName("AUTHORIZATION")
    AUTHORIZATION("AUTHORIZATION"),
    @SerializedName("CERTIFICATION_POLICY_VIOLATION")
    CERTIFICATION_POLICY_VIOLATION("CERTIFICATION_POLICY_VIOLATION"),
    @SerializedName("REMEDIATION")
    REMEDIATION("REMEDIATION"),
    @SerializedName("SOD_VIOLATION")
    SOD_VIOLATION("SOD_VIOLATION"),
    @SerializedName("CAPPLICATION")
    CAPPLICATION("CAPPLICATION"),
    @SerializedName("FULFILLMENT_TARGET")
    FULFILLMENT_TARGET("FULFILLMENT_TARGET"),
    @SerializedName("FULFILLMENT_SAMPLE")
    FULFILLMENT_SAMPLE("FULFILLMENT_SAMPLE"),
    @SerializedName("USER_SOURCE")
    USER_SOURCE("USER_SOURCE"),
    @SerializedName("DATA_CENTER")
    DATA_CENTER("DATA_CENTER"),
    @SerializedName("DATA_CONN_SOURCE")
    DATA_CONN_SOURCE("DATA_CONN_SOURCE"),
    @SerializedName("ACCESS_REQUEST")
    ACCESS_REQUEST("ACCESS_REQUEST"),
    @SerializedName("ACCESS_REQUEST_APPROVAL")
    ACCESS_REQUEST_APPROVAL("ACCESS_REQUEST_APPROVAL"),
    @SerializedName("PSODV_APPROVAL")
    PSODV_APPROVAL("PSODV_APPROVAL"),
    @SerializedName("FORM")
    FORM("FORM"),
    @SerializedName("CUSER")
    CUSER("CUSER"),
    @SerializedName("REVIEW_ITEM_ACTION")
    REVIEW_ITEM_ACTION("REVIEW_ITEM_ACTION"),
    @SerializedName("USERS_AND_GROUPS")
    USERS_AND_GROUPS("USERS_AND_GROUPS"),
    @SerializedName("DASHBOARD")
    DASHBOARD("DASHBOARD"),
    @SerializedName("SOD_APPROVAL_POLICY")
    SOD_APPROVAL_POLICY("SOD_APPROVAL_POLICY"),
    @SerializedName("SOD_VIOLATION_CASE")
    SOD_VIOLATION_CASE("SOD_VIOLATION_CASE");

    private String value;

    PolicyTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static PolicyTypeEnum fromValue(String input) {
      for (PolicyTypeEnum b : PolicyTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<PolicyTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PolicyTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public PolicyTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return PolicyTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("policyType")
  private PolicyTypeEnum policyType = null;

  @SerializedName("policyUniqueId")
  private String policyUniqueId = null;

  @SerializedName("remediationRun")
  private RemediationRunNode remediationRun = null;

  @SerializedName("remediationRunAuto")
  private Boolean remediationRunAuto = null;

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

  @SerializedName("reviewDefUniqueId")
  private String reviewDefUniqueId = null;

  @SerializedName("reviewDefinition")
  private ReviewDef reviewDefinition = null;

  @SerializedName("reviewDuration")
  private Integer reviewDuration = null;

  /**
   * Gets or Sets reviewDurationUnit
   */
  @JsonAdapter(ReviewDurationUnitEnum.Adapter.class)
  public enum ReviewDurationUnitEnum {
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

    ReviewDurationUnitEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ReviewDurationUnitEnum fromValue(String input) {
      for (ReviewDurationUnitEnum b : ReviewDurationUnitEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ReviewDurationUnitEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReviewDurationUnitEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ReviewDurationUnitEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ReviewDurationUnitEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("reviewDurationUnit")
  private ReviewDurationUnitEnum reviewDurationUnit = null;

  @SerializedName("reviewName")
  private String reviewName = null;

  @SerializedName("runOnlyNewVio")
  private Boolean runOnlyNewVio = null;

  @SerializedName("startMessage")
  private String startMessage = null;

  @SerializedName("violationType")
  private List<TypeCount> violationType = null;

  @SerializedName("workflowId")
  private String workflowId = null;

  public RemediationNode changeRequestType(ChangeRequestTypeEnum changeRequestType) {
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

  public RemediationNode createdDate(Long createdDate) {
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

  public RemediationNode details(String details) {
    this.details = details;
    return this;
  }

   /**
   * Get details
   * @return details
  **/
  @ApiModelProperty(value = "")
  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public RemediationNode emailTemplateId(String emailTemplateId) {
    this.emailTemplateId = emailTemplateId;
    return this;
  }

   /**
   * Get emailTemplateId
   * @return emailTemplateId
  **/
  @ApiModelProperty(value = "")
  public String getEmailTemplateId() {
    return emailTemplateId;
  }

  public void setEmailTemplateId(String emailTemplateId) {
    this.emailTemplateId = emailTemplateId;
  }

  public RemediationNode emailTo(List<Entity> emailTo) {
    this.emailTo = emailTo;
    return this;
  }

  public RemediationNode addEmailToItem(Entity emailToItem) {
    if (this.emailTo == null) {
      this.emailTo = new ArrayList<Entity>();
    }
    this.emailTo.add(emailToItem);
    return this;
  }

   /**
   * Get emailTo
   * @return emailTo
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getEmailTo() {
    return emailTo;
  }

  public void setEmailTo(List<Entity> emailTo) {
    this.emailTo = emailTo;
  }

  public RemediationNode id(Long id) {
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

  public RemediationNode isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

   /**
   * Get isActive
   * @return isActive
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public RemediationNode lastActionDate(Long lastActionDate) {
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

  public RemediationNode name(String name) {
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

  public RemediationNode policyCriteria(String policyCriteria) {
    this.policyCriteria = policyCriteria;
    return this;
  }

   /**
   * Get policyCriteria
   * @return policyCriteria
  **/
  @ApiModelProperty(value = "")
  public String getPolicyCriteria() {
    return policyCriteria;
  }

  public void setPolicyCriteria(String policyCriteria) {
    this.policyCriteria = policyCriteria;
  }

  public RemediationNode policyDescription(String policyDescription) {
    this.policyDescription = policyDescription;
    return this;
  }

   /**
   * Get policyDescription
   * @return policyDescription
  **/
  @ApiModelProperty(value = "")
  public String getPolicyDescription() {
    return policyDescription;
  }

  public void setPolicyDescription(String policyDescription) {
    this.policyDescription = policyDescription;
  }

  public RemediationNode policyId(Long policyId) {
    this.policyId = policyId;
    return this;
  }

   /**
   * Get policyId
   * @return policyId
  **/
  @ApiModelProperty(value = "")
  public Long getPolicyId() {
    return policyId;
  }

  public void setPolicyId(Long policyId) {
    this.policyId = policyId;
  }

  public RemediationNode policyName(String policyName) {
    this.policyName = policyName;
    return this;
  }

   /**
   * Get policyName
   * @return policyName
  **/
  @ApiModelProperty(value = "")
  public String getPolicyName() {
    return policyName;
  }

  public void setPolicyName(String policyName) {
    this.policyName = policyName;
  }

  public RemediationNode policySecondCriteria(String policySecondCriteria) {
    this.policySecondCriteria = policySecondCriteria;
    return this;
  }

   /**
   * Get policySecondCriteria
   * @return policySecondCriteria
  **/
  @ApiModelProperty(value = "")
  public String getPolicySecondCriteria() {
    return policySecondCriteria;
  }

  public void setPolicySecondCriteria(String policySecondCriteria) {
    this.policySecondCriteria = policySecondCriteria;
  }

  public RemediationNode policyType(PolicyTypeEnum policyType) {
    this.policyType = policyType;
    return this;
  }

   /**
   * Get policyType
   * @return policyType
  **/
  @ApiModelProperty(value = "")
  public PolicyTypeEnum getPolicyType() {
    return policyType;
  }

  public void setPolicyType(PolicyTypeEnum policyType) {
    this.policyType = policyType;
  }

  public RemediationNode policyUniqueId(String policyUniqueId) {
    this.policyUniqueId = policyUniqueId;
    return this;
  }

   /**
   * Get policyUniqueId
   * @return policyUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getPolicyUniqueId() {
    return policyUniqueId;
  }

  public void setPolicyUniqueId(String policyUniqueId) {
    this.policyUniqueId = policyUniqueId;
  }

  public RemediationNode remediationRun(RemediationRunNode remediationRun) {
    this.remediationRun = remediationRun;
    return this;
  }

   /**
   * Get remediationRun
   * @return remediationRun
  **/
  @ApiModelProperty(value = "")
  public RemediationRunNode getRemediationRun() {
    return remediationRun;
  }

  public void setRemediationRun(RemediationRunNode remediationRun) {
    this.remediationRun = remediationRun;
  }

  public RemediationNode remediationRunAuto(Boolean remediationRunAuto) {
    this.remediationRunAuto = remediationRunAuto;
    return this;
  }

   /**
   * Get remediationRunAuto
   * @return remediationRunAuto
  **/
  @ApiModelProperty(value = "")
  public Boolean isRemediationRunAuto() {
    return remediationRunAuto;
  }

  public void setRemediationRunAuto(Boolean remediationRunAuto) {
    this.remediationRunAuto = remediationRunAuto;
  }

  public RemediationNode remediationType(RemediationTypeEnum remediationType) {
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

  public RemediationNode reviewDefUniqueId(String reviewDefUniqueId) {
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

  public RemediationNode reviewDefinition(ReviewDef reviewDefinition) {
    this.reviewDefinition = reviewDefinition;
    return this;
  }

   /**
   * Get reviewDefinition
   * @return reviewDefinition
  **/
  @ApiModelProperty(value = "")
  public ReviewDef getReviewDefinition() {
    return reviewDefinition;
  }

  public void setReviewDefinition(ReviewDef reviewDefinition) {
    this.reviewDefinition = reviewDefinition;
  }

  public RemediationNode reviewDuration(Integer reviewDuration) {
    this.reviewDuration = reviewDuration;
    return this;
  }

   /**
   * Get reviewDuration
   * @return reviewDuration
  **/
  @ApiModelProperty(value = "")
  public Integer getReviewDuration() {
    return reviewDuration;
  }

  public void setReviewDuration(Integer reviewDuration) {
    this.reviewDuration = reviewDuration;
  }

  public RemediationNode reviewDurationUnit(ReviewDurationUnitEnum reviewDurationUnit) {
    this.reviewDurationUnit = reviewDurationUnit;
    return this;
  }

   /**
   * Get reviewDurationUnit
   * @return reviewDurationUnit
  **/
  @ApiModelProperty(value = "")
  public ReviewDurationUnitEnum getReviewDurationUnit() {
    return reviewDurationUnit;
  }

  public void setReviewDurationUnit(ReviewDurationUnitEnum reviewDurationUnit) {
    this.reviewDurationUnit = reviewDurationUnit;
  }

  public RemediationNode reviewName(String reviewName) {
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

  public RemediationNode runOnlyNewVio(Boolean runOnlyNewVio) {
    this.runOnlyNewVio = runOnlyNewVio;
    return this;
  }

   /**
   * Get runOnlyNewVio
   * @return runOnlyNewVio
  **/
  @ApiModelProperty(value = "")
  public Boolean isRunOnlyNewVio() {
    return runOnlyNewVio;
  }

  public void setRunOnlyNewVio(Boolean runOnlyNewVio) {
    this.runOnlyNewVio = runOnlyNewVio;
  }

  public RemediationNode startMessage(String startMessage) {
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

  public RemediationNode violationType(List<TypeCount> violationType) {
    this.violationType = violationType;
    return this;
  }

  public RemediationNode addViolationTypeItem(TypeCount violationTypeItem) {
    if (this.violationType == null) {
      this.violationType = new ArrayList<TypeCount>();
    }
    this.violationType.add(violationTypeItem);
    return this;
  }

   /**
   * Get violationType
   * @return violationType
  **/
  @ApiModelProperty(value = "")
  public List<TypeCount> getViolationType() {
    return violationType;
  }

  public void setViolationType(List<TypeCount> violationType) {
    this.violationType = violationType;
  }

  public RemediationNode workflowId(String workflowId) {
    this.workflowId = workflowId;
    return this;
  }

   /**
   * Get workflowId
   * @return workflowId
  **/
  @ApiModelProperty(value = "")
  public String getWorkflowId() {
    return workflowId;
  }

  public void setWorkflowId(String workflowId) {
    this.workflowId = workflowId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemediationNode remediationNode = (RemediationNode) o;
    return Objects.equals(this.changeRequestType, remediationNode.changeRequestType) &&
        Objects.equals(this.createdDate, remediationNode.createdDate) &&
        Objects.equals(this.details, remediationNode.details) &&
        Objects.equals(this.emailTemplateId, remediationNode.emailTemplateId) &&
        Objects.equals(this.emailTo, remediationNode.emailTo) &&
        Objects.equals(this.id, remediationNode.id) &&
        Objects.equals(this.isActive, remediationNode.isActive) &&
        Objects.equals(this.lastActionDate, remediationNode.lastActionDate) &&
        Objects.equals(this.name, remediationNode.name) &&
        Objects.equals(this.policyCriteria, remediationNode.policyCriteria) &&
        Objects.equals(this.policyDescription, remediationNode.policyDescription) &&
        Objects.equals(this.policyId, remediationNode.policyId) &&
        Objects.equals(this.policyName, remediationNode.policyName) &&
        Objects.equals(this.policySecondCriteria, remediationNode.policySecondCriteria) &&
        Objects.equals(this.policyType, remediationNode.policyType) &&
        Objects.equals(this.policyUniqueId, remediationNode.policyUniqueId) &&
        Objects.equals(this.remediationRun, remediationNode.remediationRun) &&
        Objects.equals(this.remediationRunAuto, remediationNode.remediationRunAuto) &&
        Objects.equals(this.remediationType, remediationNode.remediationType) &&
        Objects.equals(this.reviewDefUniqueId, remediationNode.reviewDefUniqueId) &&
        Objects.equals(this.reviewDefinition, remediationNode.reviewDefinition) &&
        Objects.equals(this.reviewDuration, remediationNode.reviewDuration) &&
        Objects.equals(this.reviewDurationUnit, remediationNode.reviewDurationUnit) &&
        Objects.equals(this.reviewName, remediationNode.reviewName) &&
        Objects.equals(this.runOnlyNewVio, remediationNode.runOnlyNewVio) &&
        Objects.equals(this.startMessage, remediationNode.startMessage) &&
        Objects.equals(this.violationType, remediationNode.violationType) &&
        Objects.equals(this.workflowId, remediationNode.workflowId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(changeRequestType, createdDate, details, emailTemplateId, emailTo, id, isActive, lastActionDate, name, policyCriteria, policyDescription, policyId, policyName, policySecondCriteria, policyType, policyUniqueId, remediationRun, remediationRunAuto, remediationType, reviewDefUniqueId, reviewDefinition, reviewDuration, reviewDurationUnit, reviewName, runOnlyNewVio, startMessage, violationType, workflowId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemediationNode {\n");
    
    sb.append("    changeRequestType: ").append(toIndentedString(changeRequestType)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("    emailTemplateId: ").append(toIndentedString(emailTemplateId)).append("\n");
    sb.append("    emailTo: ").append(toIndentedString(emailTo)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    lastActionDate: ").append(toIndentedString(lastActionDate)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    policyCriteria: ").append(toIndentedString(policyCriteria)).append("\n");
    sb.append("    policyDescription: ").append(toIndentedString(policyDescription)).append("\n");
    sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
    sb.append("    policyName: ").append(toIndentedString(policyName)).append("\n");
    sb.append("    policySecondCriteria: ").append(toIndentedString(policySecondCriteria)).append("\n");
    sb.append("    policyType: ").append(toIndentedString(policyType)).append("\n");
    sb.append("    policyUniqueId: ").append(toIndentedString(policyUniqueId)).append("\n");
    sb.append("    remediationRun: ").append(toIndentedString(remediationRun)).append("\n");
    sb.append("    remediationRunAuto: ").append(toIndentedString(remediationRunAuto)).append("\n");
    sb.append("    remediationType: ").append(toIndentedString(remediationType)).append("\n");
    sb.append("    reviewDefUniqueId: ").append(toIndentedString(reviewDefUniqueId)).append("\n");
    sb.append("    reviewDefinition: ").append(toIndentedString(reviewDefinition)).append("\n");
    sb.append("    reviewDuration: ").append(toIndentedString(reviewDuration)).append("\n");
    sb.append("    reviewDurationUnit: ").append(toIndentedString(reviewDurationUnit)).append("\n");
    sb.append("    reviewName: ").append(toIndentedString(reviewName)).append("\n");
    sb.append("    runOnlyNewVio: ").append(toIndentedString(runOnlyNewVio)).append("\n");
    sb.append("    startMessage: ").append(toIndentedString(startMessage)).append("\n");
    sb.append("    violationType: ").append(toIndentedString(violationType)).append("\n");
    sb.append("    workflowId: ").append(toIndentedString(workflowId)).append("\n");
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
