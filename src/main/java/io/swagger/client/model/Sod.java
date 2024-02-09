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
import io.swagger.client.model.CompControl;
import io.swagger.client.model.Condition;
import io.swagger.client.model.Owner;
import io.swagger.client.model.SodApprovalPolicy;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Sod
 */



public class Sod {
  @SerializedName("accountCount")
  private Long accountCount = null;

  @SerializedName("accountCriteria")
  private String accountCriteria = null;

  @SerializedName("accountCriteriaCount")
  private Long accountCriteriaCount = null;

  @SerializedName("approvalRequired")
  private Boolean approvalRequired = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("canManageEntity")
  private Boolean canManageEntity = null;

  @SerializedName("changedBy")
  private User changedBy = null;

  @SerializedName("compControls")
  private List<CompControl> compControls = null;

  @SerializedName("conditions")
  private List<Condition> conditions = null;

  @SerializedName("conditionsWarnMsg")
  private String conditionsWarnMsg = null;

  @SerializedName("controlPeriod")
  private Long controlPeriod = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  /**
   * Gets or Sets detectionStatus
   */
  @JsonAdapter(DetectionStatusEnum.Adapter.class)
  public enum DetectionStatusEnum {
    @SerializedName("RUNNING")
    RUNNING("RUNNING"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("INVALID")
    INVALID("INVALID"),
    @SerializedName("FAILED")
    FAILED("FAILED");

    private String value;

    DetectionStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DetectionStatusEnum fromValue(String input) {
      for (DetectionStatusEnum b : DetectionStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DetectionStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DetectionStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DetectionStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DetectionStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("detectionStatus")
  private DetectionStatusEnum detectionStatus = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("lastDetectionTime")
  private Long lastDetectionTime = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("owners")
  private List<Owner> owners = null;

  @SerializedName("resolve")
  private String resolve = null;

  @SerializedName("risk")
  private Long risk = null;

  @SerializedName("singleItemWarning")
  private Boolean singleItemWarning = null;

  @SerializedName("sodApprovalPolicy")
  private SodApprovalPolicy sodApprovalPolicy = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("INVALID")
    INVALID("INVALID"),
    @SerializedName("MINED")
    MINED("MINED");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StateEnum fromValue(String input) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("state")
  private StateEnum state = null;

  @SerializedName("totalViolation")
  private Long totalViolation = null;

  @SerializedName("uniquePolicyId")
  private String uniquePolicyId = null;

  @SerializedName("unnecessaryConditionWarning")
  private Boolean unnecessaryConditionWarning = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  @SerializedName("userCount")
  private Long userCount = null;

  @SerializedName("userCriteria")
  private String userCriteria = null;

  @SerializedName("userCriteriaCount")
  private Long userCriteriaCount = null;

  public Sod accountCount(Long accountCount) {
    this.accountCount = accountCount;
    return this;
  }

   /**
   * Get accountCount
   * @return accountCount
  **/
  @ApiModelProperty(value = "")
  public Long getAccountCount() {
    return accountCount;
  }

  public void setAccountCount(Long accountCount) {
    this.accountCount = accountCount;
  }

  public Sod accountCriteria(String accountCriteria) {
    this.accountCriteria = accountCriteria;
    return this;
  }

   /**
   * Get accountCriteria
   * @return accountCriteria
  **/
  @ApiModelProperty(value = "")
  public String getAccountCriteria() {
    return accountCriteria;
  }

  public void setAccountCriteria(String accountCriteria) {
    this.accountCriteria = accountCriteria;
  }

  public Sod accountCriteriaCount(Long accountCriteriaCount) {
    this.accountCriteriaCount = accountCriteriaCount;
    return this;
  }

   /**
   * Get accountCriteriaCount
   * @return accountCriteriaCount
  **/
  @ApiModelProperty(value = "")
  public Long getAccountCriteriaCount() {
    return accountCriteriaCount;
  }

  public void setAccountCriteriaCount(Long accountCriteriaCount) {
    this.accountCriteriaCount = accountCriteriaCount;
  }

  public Sod approvalRequired(Boolean approvalRequired) {
    this.approvalRequired = approvalRequired;
    return this;
  }

   /**
   * Get approvalRequired
   * @return approvalRequired
  **/
  @ApiModelProperty(value = "")
  public Boolean isApprovalRequired() {
    return approvalRequired;
  }

  public void setApprovalRequired(Boolean approvalRequired) {
    this.approvalRequired = approvalRequired;
  }

  public Sod canEditEntity(Boolean canEditEntity) {
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

  public Sod canManageEntity(Boolean canManageEntity) {
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

  public Sod changedBy(User changedBy) {
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

  public Sod compControls(List<CompControl> compControls) {
    this.compControls = compControls;
    return this;
  }

  public Sod addCompControlsItem(CompControl compControlsItem) {
    if (this.compControls == null) {
      this.compControls = new ArrayList<CompControl>();
    }
    this.compControls.add(compControlsItem);
    return this;
  }

   /**
   * Get compControls
   * @return compControls
  **/
  @ApiModelProperty(value = "")
  public List<CompControl> getCompControls() {
    return compControls;
  }

  public void setCompControls(List<CompControl> compControls) {
    this.compControls = compControls;
  }

  public Sod conditions(List<Condition> conditions) {
    this.conditions = conditions;
    return this;
  }

  public Sod addConditionsItem(Condition conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<Condition>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

   /**
   * Get conditions
   * @return conditions
  **/
  @ApiModelProperty(value = "")
  public List<Condition> getConditions() {
    return conditions;
  }

  public void setConditions(List<Condition> conditions) {
    this.conditions = conditions;
  }

  public Sod conditionsWarnMsg(String conditionsWarnMsg) {
    this.conditionsWarnMsg = conditionsWarnMsg;
    return this;
  }

   /**
   * Get conditionsWarnMsg
   * @return conditionsWarnMsg
  **/
  @ApiModelProperty(value = "")
  public String getConditionsWarnMsg() {
    return conditionsWarnMsg;
  }

  public void setConditionsWarnMsg(String conditionsWarnMsg) {
    this.conditionsWarnMsg = conditionsWarnMsg;
  }

  public Sod controlPeriod(Long controlPeriod) {
    this.controlPeriod = controlPeriod;
    return this;
  }

   /**
   * Get controlPeriod
   * @return controlPeriod
  **/
  @ApiModelProperty(value = "")
  public Long getControlPeriod() {
    return controlPeriod;
  }

  public void setControlPeriod(Long controlPeriod) {
    this.controlPeriod = controlPeriod;
  }

  public Sod createdBy(User createdBy) {
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

  public Sod creationTime(Long creationTime) {
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

  public Sod deleted(Boolean deleted) {
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

  public Sod description(String description) {
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

  public Sod detectionStatus(DetectionStatusEnum detectionStatus) {
    this.detectionStatus = detectionStatus;
    return this;
  }

   /**
   * Get detectionStatus
   * @return detectionStatus
  **/
  @ApiModelProperty(value = "")
  public DetectionStatusEnum getDetectionStatus() {
    return detectionStatus;
  }

  public void setDetectionStatus(DetectionStatusEnum detectionStatus) {
    this.detectionStatus = detectionStatus;
  }

  public Sod id(Long id) {
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

  public Sod lastDetectionTime(Long lastDetectionTime) {
    this.lastDetectionTime = lastDetectionTime;
    return this;
  }

   /**
   * Get lastDetectionTime
   * @return lastDetectionTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastDetectionTime() {
    return lastDetectionTime;
  }

  public void setLastDetectionTime(Long lastDetectionTime) {
    this.lastDetectionTime = lastDetectionTime;
  }

  public Sod link(String link) {
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

  public Sod name(String name) {
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

  public Sod owners(List<Owner> owners) {
    this.owners = owners;
    return this;
  }

  public Sod addOwnersItem(Owner ownersItem) {
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

  public Sod resolve(String resolve) {
    this.resolve = resolve;
    return this;
  }

   /**
   * Get resolve
   * @return resolve
  **/
  @ApiModelProperty(value = "")
  public String getResolve() {
    return resolve;
  }

  public void setResolve(String resolve) {
    this.resolve = resolve;
  }

  public Sod risk(Long risk) {
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

  public Sod singleItemWarning(Boolean singleItemWarning) {
    this.singleItemWarning = singleItemWarning;
    return this;
  }

   /**
   * Get singleItemWarning
   * @return singleItemWarning
  **/
  @ApiModelProperty(value = "")
  public Boolean isSingleItemWarning() {
    return singleItemWarning;
  }

  public void setSingleItemWarning(Boolean singleItemWarning) {
    this.singleItemWarning = singleItemWarning;
  }

  public Sod sodApprovalPolicy(SodApprovalPolicy sodApprovalPolicy) {
    this.sodApprovalPolicy = sodApprovalPolicy;
    return this;
  }

   /**
   * Get sodApprovalPolicy
   * @return sodApprovalPolicy
  **/
  @ApiModelProperty(value = "")
  public SodApprovalPolicy getSodApprovalPolicy() {
    return sodApprovalPolicy;
  }

  public void setSodApprovalPolicy(SodApprovalPolicy sodApprovalPolicy) {
    this.sodApprovalPolicy = sodApprovalPolicy;
  }

  public Sod state(StateEnum state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public Sod totalViolation(Long totalViolation) {
    this.totalViolation = totalViolation;
    return this;
  }

   /**
   * Get totalViolation
   * @return totalViolation
  **/
  @ApiModelProperty(value = "")
  public Long getTotalViolation() {
    return totalViolation;
  }

  public void setTotalViolation(Long totalViolation) {
    this.totalViolation = totalViolation;
  }

  public Sod uniquePolicyId(String uniquePolicyId) {
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

  public Sod unnecessaryConditionWarning(Boolean unnecessaryConditionWarning) {
    this.unnecessaryConditionWarning = unnecessaryConditionWarning;
    return this;
  }

   /**
   * Get unnecessaryConditionWarning
   * @return unnecessaryConditionWarning
  **/
  @ApiModelProperty(value = "")
  public Boolean isUnnecessaryConditionWarning() {
    return unnecessaryConditionWarning;
  }

  public void setUnnecessaryConditionWarning(Boolean unnecessaryConditionWarning) {
    this.unnecessaryConditionWarning = unnecessaryConditionWarning;
  }

  public Sod updateTime(Long updateTime) {
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

  public Sod userCount(Long userCount) {
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

  public Sod userCriteria(String userCriteria) {
    this.userCriteria = userCriteria;
    return this;
  }

   /**
   * Get userCriteria
   * @return userCriteria
  **/
  @ApiModelProperty(value = "")
  public String getUserCriteria() {
    return userCriteria;
  }

  public void setUserCriteria(String userCriteria) {
    this.userCriteria = userCriteria;
  }

  public Sod userCriteriaCount(Long userCriteriaCount) {
    this.userCriteriaCount = userCriteriaCount;
    return this;
  }

   /**
   * Get userCriteriaCount
   * @return userCriteriaCount
  **/
  @ApiModelProperty(value = "")
  public Long getUserCriteriaCount() {
    return userCriteriaCount;
  }

  public void setUserCriteriaCount(Long userCriteriaCount) {
    this.userCriteriaCount = userCriteriaCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sod sod = (Sod) o;
    return Objects.equals(this.accountCount, sod.accountCount) &&
        Objects.equals(this.accountCriteria, sod.accountCriteria) &&
        Objects.equals(this.accountCriteriaCount, sod.accountCriteriaCount) &&
        Objects.equals(this.approvalRequired, sod.approvalRequired) &&
        Objects.equals(this.canEditEntity, sod.canEditEntity) &&
        Objects.equals(this.canManageEntity, sod.canManageEntity) &&
        Objects.equals(this.changedBy, sod.changedBy) &&
        Objects.equals(this.compControls, sod.compControls) &&
        Objects.equals(this.conditions, sod.conditions) &&
        Objects.equals(this.conditionsWarnMsg, sod.conditionsWarnMsg) &&
        Objects.equals(this.controlPeriod, sod.controlPeriod) &&
        Objects.equals(this.createdBy, sod.createdBy) &&
        Objects.equals(this.creationTime, sod.creationTime) &&
        Objects.equals(this.deleted, sod.deleted) &&
        Objects.equals(this.description, sod.description) &&
        Objects.equals(this.detectionStatus, sod.detectionStatus) &&
        Objects.equals(this.id, sod.id) &&
        Objects.equals(this.lastDetectionTime, sod.lastDetectionTime) &&
        Objects.equals(this.link, sod.link) &&
        Objects.equals(this.name, sod.name) &&
        Objects.equals(this.owners, sod.owners) &&
        Objects.equals(this.resolve, sod.resolve) &&
        Objects.equals(this.risk, sod.risk) &&
        Objects.equals(this.singleItemWarning, sod.singleItemWarning) &&
        Objects.equals(this.sodApprovalPolicy, sod.sodApprovalPolicy) &&
        Objects.equals(this.state, sod.state) &&
        Objects.equals(this.totalViolation, sod.totalViolation) &&
        Objects.equals(this.uniquePolicyId, sod.uniquePolicyId) &&
        Objects.equals(this.unnecessaryConditionWarning, sod.unnecessaryConditionWarning) &&
        Objects.equals(this.updateTime, sod.updateTime) &&
        Objects.equals(this.userCount, sod.userCount) &&
        Objects.equals(this.userCriteria, sod.userCriteria) &&
        Objects.equals(this.userCriteriaCount, sod.userCriteriaCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountCount, accountCriteria, accountCriteriaCount, approvalRequired, canEditEntity, canManageEntity, changedBy, compControls, conditions, conditionsWarnMsg, controlPeriod, createdBy, creationTime, deleted, description, detectionStatus, id, lastDetectionTime, link, name, owners, resolve, risk, singleItemWarning, sodApprovalPolicy, state, totalViolation, uniquePolicyId, unnecessaryConditionWarning, updateTime, userCount, userCriteria, userCriteriaCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Sod {\n");
    
    sb.append("    accountCount: ").append(toIndentedString(accountCount)).append("\n");
    sb.append("    accountCriteria: ").append(toIndentedString(accountCriteria)).append("\n");
    sb.append("    accountCriteriaCount: ").append(toIndentedString(accountCriteriaCount)).append("\n");
    sb.append("    approvalRequired: ").append(toIndentedString(approvalRequired)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    canManageEntity: ").append(toIndentedString(canManageEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    compControls: ").append(toIndentedString(compControls)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
    sb.append("    conditionsWarnMsg: ").append(toIndentedString(conditionsWarnMsg)).append("\n");
    sb.append("    controlPeriod: ").append(toIndentedString(controlPeriod)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    detectionStatus: ").append(toIndentedString(detectionStatus)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lastDetectionTime: ").append(toIndentedString(lastDetectionTime)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    resolve: ").append(toIndentedString(resolve)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    singleItemWarning: ").append(toIndentedString(singleItemWarning)).append("\n");
    sb.append("    sodApprovalPolicy: ").append(toIndentedString(sodApprovalPolicy)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    totalViolation: ").append(toIndentedString(totalViolation)).append("\n");
    sb.append("    uniquePolicyId: ").append(toIndentedString(uniquePolicyId)).append("\n");
    sb.append("    unnecessaryConditionWarning: ").append(toIndentedString(unnecessaryConditionWarning)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    userCount: ").append(toIndentedString(userCount)).append("\n");
    sb.append("    userCriteria: ").append(toIndentedString(userCriteria)).append("\n");
    sb.append("    userCriteriaCount: ").append(toIndentedString(userCriteriaCount)).append("\n");
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
