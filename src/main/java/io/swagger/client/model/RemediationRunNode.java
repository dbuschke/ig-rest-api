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
import io.swagger.client.model.ReviewInst;
import io.swagger.client.model.TypeCount;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * RemediationRunNode
 */



public class RemediationRunNode {
  @SerializedName("changeSetId")
  private Long changeSetId = null;

  @SerializedName("emailTemplateId")
  private String emailTemplateId = null;

  @SerializedName("emailTo")
  private List<Entity> emailTo = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("itemCount")
  private Long itemCount = null;

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

  @SerializedName("reviewInstance")
  private ReviewInst reviewInstance = null;

  @SerializedName("reviewInstanceId")
  private Long reviewInstanceId = null;

  @SerializedName("runDate")
  private Long runDate = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("violationId")
  private Long violationId = null;

  @SerializedName("violationType")
  private List<TypeCount> violationType = null;

  @SerializedName("workflowId")
  private String workflowId = null;

  @SerializedName("workflowRequestId")
  private String workflowRequestId = null;

  public RemediationRunNode changeSetId(Long changeSetId) {
    this.changeSetId = changeSetId;
    return this;
  }

   /**
   * Get changeSetId
   * @return changeSetId
  **/
  @ApiModelProperty(value = "")
  public Long getChangeSetId() {
    return changeSetId;
  }

  public void setChangeSetId(Long changeSetId) {
    this.changeSetId = changeSetId;
  }

  public RemediationRunNode emailTemplateId(String emailTemplateId) {
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

  public RemediationRunNode emailTo(List<Entity> emailTo) {
    this.emailTo = emailTo;
    return this;
  }

  public RemediationRunNode addEmailToItem(Entity emailToItem) {
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

  public RemediationRunNode id(Long id) {
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

  public RemediationRunNode itemCount(Long itemCount) {
    this.itemCount = itemCount;
    return this;
  }

   /**
   * Get itemCount
   * @return itemCount
  **/
  @ApiModelProperty(value = "")
  public Long getItemCount() {
    return itemCount;
  }

  public void setItemCount(Long itemCount) {
    this.itemCount = itemCount;
  }

  public RemediationRunNode policyCriteria(String policyCriteria) {
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

  public RemediationRunNode policyDescription(String policyDescription) {
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

  public RemediationRunNode policyId(Long policyId) {
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

  public RemediationRunNode policyName(String policyName) {
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

  public RemediationRunNode policySecondCriteria(String policySecondCriteria) {
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

  public RemediationRunNode policyType(PolicyTypeEnum policyType) {
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

  public RemediationRunNode policyUniqueId(String policyUniqueId) {
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

  public RemediationRunNode remediationId(Long remediationId) {
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

  public RemediationRunNode remediationType(RemediationTypeEnum remediationType) {
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

  public RemediationRunNode reviewInstance(ReviewInst reviewInstance) {
    this.reviewInstance = reviewInstance;
    return this;
  }

   /**
   * Get reviewInstance
   * @return reviewInstance
  **/
  @ApiModelProperty(value = "")
  public ReviewInst getReviewInstance() {
    return reviewInstance;
  }

  public void setReviewInstance(ReviewInst reviewInstance) {
    this.reviewInstance = reviewInstance;
  }

  public RemediationRunNode reviewInstanceId(Long reviewInstanceId) {
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

  public RemediationRunNode runDate(Long runDate) {
    this.runDate = runDate;
    return this;
  }

   /**
   * Get runDate
   * @return runDate
  **/
  @ApiModelProperty(value = "")
  public Long getRunDate() {
    return runDate;
  }

  public void setRunDate(Long runDate) {
    this.runDate = runDate;
  }

  public RemediationRunNode status(String status) {
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

  public RemediationRunNode violationId(Long violationId) {
    this.violationId = violationId;
    return this;
  }

   /**
   * Get violationId
   * @return violationId
  **/
  @ApiModelProperty(value = "")
  public Long getViolationId() {
    return violationId;
  }

  public void setViolationId(Long violationId) {
    this.violationId = violationId;
  }

  public RemediationRunNode violationType(List<TypeCount> violationType) {
    this.violationType = violationType;
    return this;
  }

  public RemediationRunNode addViolationTypeItem(TypeCount violationTypeItem) {
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

  public RemediationRunNode workflowId(String workflowId) {
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

  public RemediationRunNode workflowRequestId(String workflowRequestId) {
    this.workflowRequestId = workflowRequestId;
    return this;
  }

   /**
   * Get workflowRequestId
   * @return workflowRequestId
  **/
  @ApiModelProperty(value = "")
  public String getWorkflowRequestId() {
    return workflowRequestId;
  }

  public void setWorkflowRequestId(String workflowRequestId) {
    this.workflowRequestId = workflowRequestId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemediationRunNode remediationRunNode = (RemediationRunNode) o;
    return Objects.equals(this.changeSetId, remediationRunNode.changeSetId) &&
        Objects.equals(this.emailTemplateId, remediationRunNode.emailTemplateId) &&
        Objects.equals(this.emailTo, remediationRunNode.emailTo) &&
        Objects.equals(this.id, remediationRunNode.id) &&
        Objects.equals(this.itemCount, remediationRunNode.itemCount) &&
        Objects.equals(this.policyCriteria, remediationRunNode.policyCriteria) &&
        Objects.equals(this.policyDescription, remediationRunNode.policyDescription) &&
        Objects.equals(this.policyId, remediationRunNode.policyId) &&
        Objects.equals(this.policyName, remediationRunNode.policyName) &&
        Objects.equals(this.policySecondCriteria, remediationRunNode.policySecondCriteria) &&
        Objects.equals(this.policyType, remediationRunNode.policyType) &&
        Objects.equals(this.policyUniqueId, remediationRunNode.policyUniqueId) &&
        Objects.equals(this.remediationId, remediationRunNode.remediationId) &&
        Objects.equals(this.remediationType, remediationRunNode.remediationType) &&
        Objects.equals(this.reviewInstance, remediationRunNode.reviewInstance) &&
        Objects.equals(this.reviewInstanceId, remediationRunNode.reviewInstanceId) &&
        Objects.equals(this.runDate, remediationRunNode.runDate) &&
        Objects.equals(this.status, remediationRunNode.status) &&
        Objects.equals(this.violationId, remediationRunNode.violationId) &&
        Objects.equals(this.violationType, remediationRunNode.violationType) &&
        Objects.equals(this.workflowId, remediationRunNode.workflowId) &&
        Objects.equals(this.workflowRequestId, remediationRunNode.workflowRequestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(changeSetId, emailTemplateId, emailTo, id, itemCount, policyCriteria, policyDescription, policyId, policyName, policySecondCriteria, policyType, policyUniqueId, remediationId, remediationType, reviewInstance, reviewInstanceId, runDate, status, violationId, violationType, workflowId, workflowRequestId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemediationRunNode {\n");
    
    sb.append("    changeSetId: ").append(toIndentedString(changeSetId)).append("\n");
    sb.append("    emailTemplateId: ").append(toIndentedString(emailTemplateId)).append("\n");
    sb.append("    emailTo: ").append(toIndentedString(emailTo)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    itemCount: ").append(toIndentedString(itemCount)).append("\n");
    sb.append("    policyCriteria: ").append(toIndentedString(policyCriteria)).append("\n");
    sb.append("    policyDescription: ").append(toIndentedString(policyDescription)).append("\n");
    sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
    sb.append("    policyName: ").append(toIndentedString(policyName)).append("\n");
    sb.append("    policySecondCriteria: ").append(toIndentedString(policySecondCriteria)).append("\n");
    sb.append("    policyType: ").append(toIndentedString(policyType)).append("\n");
    sb.append("    policyUniqueId: ").append(toIndentedString(policyUniqueId)).append("\n");
    sb.append("    remediationId: ").append(toIndentedString(remediationId)).append("\n");
    sb.append("    remediationType: ").append(toIndentedString(remediationType)).append("\n");
    sb.append("    reviewInstance: ").append(toIndentedString(reviewInstance)).append("\n");
    sb.append("    reviewInstanceId: ").append(toIndentedString(reviewInstanceId)).append("\n");
    sb.append("    runDate: ").append(toIndentedString(runDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    violationId: ").append(toIndentedString(violationId)).append("\n");
    sb.append("    violationType: ").append(toIndentedString(violationType)).append("\n");
    sb.append("    workflowId: ").append(toIndentedString(workflowId)).append("\n");
    sb.append("    workflowRequestId: ").append(toIndentedString(workflowRequestId)).append("\n");
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
