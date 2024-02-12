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
import de.araneaconsult.codegen.ig.rest.model.DataProductionNode;
import de.araneaconsult.codegen.ig.rest.model.Owner;
import de.araneaconsult.codegen.ig.rest.model.RemediationRunNode;
import de.araneaconsult.codegen.ig.rest.model.ReviewDef;
import de.araneaconsult.codegen.ig.rest.model.TypeCount;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * CertPolicyNode
 */



public class CertPolicyNode {
  @SerializedName("activeFromDate")
  private Long activeFromDate = null;

  @SerializedName("activeToDate")
  private Long activeToDate = null;

  @SerializedName("activity")
  private List<DataProductionNode> activity = null;

  @SerializedName("admins")
  private List<Owner> admins = null;

  @SerializedName("calcRunningId")
  private Long calcRunningId = null;

  @SerializedName("calculationTime")
  private Long calculationTime = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("changedBy")
  private String changedBy = null;

  @SerializedName("cpTotalViolation")
  private Long cpTotalViolation = null;

  @SerializedName("cpVersion")
  private Long cpVersion = null;

  @SerializedName("createdBy")
  private String createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("entityCounts")
  private List<TypeCount> entityCounts = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("isActive")
  private Boolean isActive = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("remediationId")
  private Long remediationId = null;

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

  @SerializedName("reviewDefinitions")
  private List<ReviewDef> reviewDefinitions = null;

  @SerializedName("risk")
  private Long risk = null;

  @SerializedName("runOnSave")
  private Boolean runOnSave = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("NOT_CERTIFIED")
    NOT_CERTIFIED("NOT_CERTIFIED"),
    @SerializedName("CERTIFIED")
    CERTIFIED("CERTIFIED");

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

  @SerializedName("triggerByEvent")
  private Boolean triggerByEvent = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  @SerializedName("validityPeriod")
  private Long validityPeriod = null;

  @SerializedName("violationCounts")
  private List<TypeCount> violationCounts = null;

  public CertPolicyNode activeFromDate(Long activeFromDate) {
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

  public CertPolicyNode activeToDate(Long activeToDate) {
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

  public CertPolicyNode activity(List<DataProductionNode> activity) {
    this.activity = activity;
    return this;
  }

  public CertPolicyNode addActivityItem(DataProductionNode activityItem) {
    if (this.activity == null) {
      this.activity = new ArrayList<DataProductionNode>();
    }
    this.activity.add(activityItem);
    return this;
  }

   /**
   * Get activity
   * @return activity
  **/
  @ApiModelProperty(value = "")
  public List<DataProductionNode> getActivity() {
    return activity;
  }

  public void setActivity(List<DataProductionNode> activity) {
    this.activity = activity;
  }

  public CertPolicyNode admins(List<Owner> admins) {
    this.admins = admins;
    return this;
  }

  public CertPolicyNode addAdminsItem(Owner adminsItem) {
    if (this.admins == null) {
      this.admins = new ArrayList<Owner>();
    }
    this.admins.add(adminsItem);
    return this;
  }

   /**
   * Get admins
   * @return admins
  **/
  @ApiModelProperty(value = "")
  public List<Owner> getAdmins() {
    return admins;
  }

  public void setAdmins(List<Owner> admins) {
    this.admins = admins;
  }

  public CertPolicyNode calcRunningId(Long calcRunningId) {
    this.calcRunningId = calcRunningId;
    return this;
  }

   /**
   * Get calcRunningId
   * @return calcRunningId
  **/
  @ApiModelProperty(value = "")
  public Long getCalcRunningId() {
    return calcRunningId;
  }

  public void setCalcRunningId(Long calcRunningId) {
    this.calcRunningId = calcRunningId;
  }

  public CertPolicyNode calculationTime(Long calculationTime) {
    this.calculationTime = calculationTime;
    return this;
  }

   /**
   * Get calculationTime
   * @return calculationTime
  **/
  @ApiModelProperty(value = "")
  public Long getCalculationTime() {
    return calculationTime;
  }

  public void setCalculationTime(Long calculationTime) {
    this.calculationTime = calculationTime;
  }

  public CertPolicyNode canEditEntity(Boolean canEditEntity) {
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

  public CertPolicyNode changedBy(String changedBy) {
    this.changedBy = changedBy;
    return this;
  }

   /**
   * Get changedBy
   * @return changedBy
  **/
  @ApiModelProperty(value = "")
  public String getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(String changedBy) {
    this.changedBy = changedBy;
  }

  public CertPolicyNode cpTotalViolation(Long cpTotalViolation) {
    this.cpTotalViolation = cpTotalViolation;
    return this;
  }

   /**
   * Get cpTotalViolation
   * @return cpTotalViolation
  **/
  @ApiModelProperty(value = "")
  public Long getCpTotalViolation() {
    return cpTotalViolation;
  }

  public void setCpTotalViolation(Long cpTotalViolation) {
    this.cpTotalViolation = cpTotalViolation;
  }

  public CertPolicyNode cpVersion(Long cpVersion) {
    this.cpVersion = cpVersion;
    return this;
  }

   /**
   * Get cpVersion
   * @return cpVersion
  **/
  @ApiModelProperty(value = "")
  public Long getCpVersion() {
    return cpVersion;
  }

  public void setCpVersion(Long cpVersion) {
    this.cpVersion = cpVersion;
  }

  public CertPolicyNode createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Get createdBy
   * @return createdBy
  **/
  @ApiModelProperty(value = "")
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public CertPolicyNode creationTime(Long creationTime) {
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

  public CertPolicyNode deleted(Boolean deleted) {
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

  public CertPolicyNode description(String description) {
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

  public CertPolicyNode entityCounts(List<TypeCount> entityCounts) {
    this.entityCounts = entityCounts;
    return this;
  }

  public CertPolicyNode addEntityCountsItem(TypeCount entityCountsItem) {
    if (this.entityCounts == null) {
      this.entityCounts = new ArrayList<TypeCount>();
    }
    this.entityCounts.add(entityCountsItem);
    return this;
  }

   /**
   * Get entityCounts
   * @return entityCounts
  **/
  @ApiModelProperty(value = "")
  public List<TypeCount> getEntityCounts() {
    return entityCounts;
  }

  public void setEntityCounts(List<TypeCount> entityCounts) {
    this.entityCounts = entityCounts;
  }

  public CertPolicyNode id(Long id) {
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

  public CertPolicyNode isActive(Boolean isActive) {
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

  public CertPolicyNode name(String name) {
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

  public CertPolicyNode remediationId(Long remediationId) {
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

  public CertPolicyNode remediationRun(RemediationRunNode remediationRun) {
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

  public CertPolicyNode remediationRunAuto(Boolean remediationRunAuto) {
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

  public CertPolicyNode remediationType(RemediationTypeEnum remediationType) {
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

  public CertPolicyNode reviewDefinitions(List<ReviewDef> reviewDefinitions) {
    this.reviewDefinitions = reviewDefinitions;
    return this;
  }

  public CertPolicyNode addReviewDefinitionsItem(ReviewDef reviewDefinitionsItem) {
    if (this.reviewDefinitions == null) {
      this.reviewDefinitions = new ArrayList<ReviewDef>();
    }
    this.reviewDefinitions.add(reviewDefinitionsItem);
    return this;
  }

   /**
   * Get reviewDefinitions
   * @return reviewDefinitions
  **/
  @ApiModelProperty(value = "")
  public List<ReviewDef> getReviewDefinitions() {
    return reviewDefinitions;
  }

  public void setReviewDefinitions(List<ReviewDef> reviewDefinitions) {
    this.reviewDefinitions = reviewDefinitions;
  }

  public CertPolicyNode risk(Long risk) {
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

  public CertPolicyNode runOnSave(Boolean runOnSave) {
    this.runOnSave = runOnSave;
    return this;
  }

   /**
   * Get runOnSave
   * @return runOnSave
  **/
  @ApiModelProperty(value = "")
  public Boolean isRunOnSave() {
    return runOnSave;
  }

  public void setRunOnSave(Boolean runOnSave) {
    this.runOnSave = runOnSave;
  }

  public CertPolicyNode status(StatusEnum status) {
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

  public CertPolicyNode triggerByEvent(Boolean triggerByEvent) {
    this.triggerByEvent = triggerByEvent;
    return this;
  }

   /**
   * Get triggerByEvent
   * @return triggerByEvent
  **/
  @ApiModelProperty(value = "")
  public Boolean isTriggerByEvent() {
    return triggerByEvent;
  }

  public void setTriggerByEvent(Boolean triggerByEvent) {
    this.triggerByEvent = triggerByEvent;
  }

  public CertPolicyNode uniqueId(String uniqueId) {
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

  public CertPolicyNode updateTime(Long updateTime) {
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

  public CertPolicyNode validityPeriod(Long validityPeriod) {
    this.validityPeriod = validityPeriod;
    return this;
  }

   /**
   * Get validityPeriod
   * @return validityPeriod
  **/
  @ApiModelProperty(value = "")
  public Long getValidityPeriod() {
    return validityPeriod;
  }

  public void setValidityPeriod(Long validityPeriod) {
    this.validityPeriod = validityPeriod;
  }

  public CertPolicyNode violationCounts(List<TypeCount> violationCounts) {
    this.violationCounts = violationCounts;
    return this;
  }

  public CertPolicyNode addViolationCountsItem(TypeCount violationCountsItem) {
    if (this.violationCounts == null) {
      this.violationCounts = new ArrayList<TypeCount>();
    }
    this.violationCounts.add(violationCountsItem);
    return this;
  }

   /**
   * Get violationCounts
   * @return violationCounts
  **/
  @ApiModelProperty(value = "")
  public List<TypeCount> getViolationCounts() {
    return violationCounts;
  }

  public void setViolationCounts(List<TypeCount> violationCounts) {
    this.violationCounts = violationCounts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CertPolicyNode certPolicyNode = (CertPolicyNode) o;
    return Objects.equals(this.activeFromDate, certPolicyNode.activeFromDate) &&
        Objects.equals(this.activeToDate, certPolicyNode.activeToDate) &&
        Objects.equals(this.activity, certPolicyNode.activity) &&
        Objects.equals(this.admins, certPolicyNode.admins) &&
        Objects.equals(this.calcRunningId, certPolicyNode.calcRunningId) &&
        Objects.equals(this.calculationTime, certPolicyNode.calculationTime) &&
        Objects.equals(this.canEditEntity, certPolicyNode.canEditEntity) &&
        Objects.equals(this.changedBy, certPolicyNode.changedBy) &&
        Objects.equals(this.cpTotalViolation, certPolicyNode.cpTotalViolation) &&
        Objects.equals(this.cpVersion, certPolicyNode.cpVersion) &&
        Objects.equals(this.createdBy, certPolicyNode.createdBy) &&
        Objects.equals(this.creationTime, certPolicyNode.creationTime) &&
        Objects.equals(this.deleted, certPolicyNode.deleted) &&
        Objects.equals(this.description, certPolicyNode.description) &&
        Objects.equals(this.entityCounts, certPolicyNode.entityCounts) &&
        Objects.equals(this.id, certPolicyNode.id) &&
        Objects.equals(this.isActive, certPolicyNode.isActive) &&
        Objects.equals(this.name, certPolicyNode.name) &&
        Objects.equals(this.remediationId, certPolicyNode.remediationId) &&
        Objects.equals(this.remediationRun, certPolicyNode.remediationRun) &&
        Objects.equals(this.remediationRunAuto, certPolicyNode.remediationRunAuto) &&
        Objects.equals(this.remediationType, certPolicyNode.remediationType) &&
        Objects.equals(this.reviewDefinitions, certPolicyNode.reviewDefinitions) &&
        Objects.equals(this.risk, certPolicyNode.risk) &&
        Objects.equals(this.runOnSave, certPolicyNode.runOnSave) &&
        Objects.equals(this.status, certPolicyNode.status) &&
        Objects.equals(this.triggerByEvent, certPolicyNode.triggerByEvent) &&
        Objects.equals(this.uniqueId, certPolicyNode.uniqueId) &&
        Objects.equals(this.updateTime, certPolicyNode.updateTime) &&
        Objects.equals(this.validityPeriod, certPolicyNode.validityPeriod) &&
        Objects.equals(this.violationCounts, certPolicyNode.violationCounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeFromDate, activeToDate, activity, admins, calcRunningId, calculationTime, canEditEntity, changedBy, cpTotalViolation, cpVersion, createdBy, creationTime, deleted, description, entityCounts, id, isActive, name, remediationId, remediationRun, remediationRunAuto, remediationType, reviewDefinitions, risk, runOnSave, status, triggerByEvent, uniqueId, updateTime, validityPeriod, violationCounts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CertPolicyNode {\n");
    
    sb.append("    activeFromDate: ").append(toIndentedString(activeFromDate)).append("\n");
    sb.append("    activeToDate: ").append(toIndentedString(activeToDate)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    admins: ").append(toIndentedString(admins)).append("\n");
    sb.append("    calcRunningId: ").append(toIndentedString(calcRunningId)).append("\n");
    sb.append("    calculationTime: ").append(toIndentedString(calculationTime)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    cpTotalViolation: ").append(toIndentedString(cpTotalViolation)).append("\n");
    sb.append("    cpVersion: ").append(toIndentedString(cpVersion)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    entityCounts: ").append(toIndentedString(entityCounts)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    remediationId: ").append(toIndentedString(remediationId)).append("\n");
    sb.append("    remediationRun: ").append(toIndentedString(remediationRun)).append("\n");
    sb.append("    remediationRunAuto: ").append(toIndentedString(remediationRunAuto)).append("\n");
    sb.append("    remediationType: ").append(toIndentedString(remediationType)).append("\n");
    sb.append("    reviewDefinitions: ").append(toIndentedString(reviewDefinitions)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    runOnSave: ").append(toIndentedString(runOnSave)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    triggerByEvent: ").append(toIndentedString(triggerByEvent)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    validityPeriod: ").append(toIndentedString(validityPeriod)).append("\n");
    sb.append("    violationCounts: ").append(toIndentedString(violationCounts)).append("\n");
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
