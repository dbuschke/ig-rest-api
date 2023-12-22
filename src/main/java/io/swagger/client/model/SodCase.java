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
import io.swagger.client.model.ContribItem;
import io.swagger.client.model.SodCaseAction;
import io.swagger.client.model.UniquePermNode;
import io.swagger.client.model.UniqueRoleNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * SodCase
 */



public class SodCase {
  @SerializedName("allowApprove")
  private Boolean allowApprove = null;

  @SerializedName("approvalExpireTime")
  private Long approvalExpireTime = null;

  @SerializedName("approvalExpired")
  private Boolean approvalExpired = null;

  @SerializedName("approverCount")
  private Long approverCount = null;

  @SerializedName("businessRoleUniqueIds")
  private List<UniqueRoleNode> businessRoleUniqueIds = null;

  @SerializedName("caseActions")
  private List<SodCaseAction> caseActions = null;

  @SerializedName("caseNumber")
  private String caseNumber = null;

  @SerializedName("contributingItem")
  private ContribItem contributingItem = null;

  @SerializedName("currViolationId")
  private Long currViolationId = null;

  @SerializedName("currentStep")
  private Long currentStep = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("indirectContributingItems")
  private List<ContribItem> indirectContributingItems = null;

  @SerializedName("initialDetectionTime")
  private Long initialDetectionTime = null;

  @SerializedName("latestDetectionTime")
  private Long latestDetectionTime = null;

  @SerializedName("permUniqueIds")
  private List<UniquePermNode> permUniqueIds = null;

  @SerializedName("policyId")
  private Long policyId = null;

  @SerializedName("policyName")
  private String policyName = null;

  /**
   * Gets or Sets policyState
   */
  @JsonAdapter(PolicyStateEnum.Adapter.class)
  public enum PolicyStateEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("INVALID")
    INVALID("INVALID"),
    @SerializedName("MINED")
    MINED("MINED");

    private String value;

    PolicyStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static PolicyStateEnum fromValue(String input) {
      for (PolicyStateEnum b : PolicyStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<PolicyStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PolicyStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public PolicyStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return PolicyStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("policyState")
  private PolicyStateEnum policyState = null;

  /**
   * Gets or Sets preHoldState
   */
  @JsonAdapter(PreHoldStateEnum.Adapter.class)
  public enum PreHoldStateEnum {
    @SerializedName("NOT_REVIEWED")
    NOT_REVIEWED("NOT_REVIEWED"),
    @SerializedName("APPROVED")
    APPROVED("APPROVED"),
    @SerializedName("APPROVAL_EXPIRED")
    APPROVAL_EXPIRED("APPROVAL_EXPIRED"),
    @SerializedName("RESOLVING")
    RESOLVING("RESOLVING"),
    @SerializedName("ON_HOLD")
    ON_HOLD("ON_HOLD"),
    @SerializedName("CLOSED")
    CLOSED("CLOSED");

    private String value;

    PreHoldStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static PreHoldStateEnum fromValue(String input) {
      for (PreHoldStateEnum b : PreHoldStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<PreHoldStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PreHoldStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public PreHoldStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return PreHoldStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("preHoldState")
  private PreHoldStateEnum preHoldState = null;

  @SerializedName("roleUniqueIds")
  private List<UniqueRoleNode> roleUniqueIds = null;

  /**
   * Gets or Sets sodApprovalConstraint
   */
  @JsonAdapter(SodApprovalConstraintEnum.Adapter.class)
  public enum SodApprovalConstraintEnum {
    @SerializedName("NONE")
    NONE("NONE"),
    @SerializedName("SOD_ADMIN_OR_GLOBAL_ADMIN")
    SOD_ADMIN_OR_GLOBAL_ADMIN("SOD_ADMIN_OR_GLOBAL_ADMIN"),
    @SerializedName("ANOTHER_SOD_ADMIN_OR_GLOBAL_ADMIN")
    ANOTHER_SOD_ADMIN_OR_GLOBAL_ADMIN("ANOTHER_SOD_ADMIN_OR_GLOBAL_ADMIN");

    private String value;

    SodApprovalConstraintEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static SodApprovalConstraintEnum fromValue(String input) {
      for (SodApprovalConstraintEnum b : SodApprovalConstraintEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<SodApprovalConstraintEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final SodApprovalConstraintEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public SodApprovalConstraintEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return SodApprovalConstraintEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("sodApprovalConstraint")
  private SodApprovalConstraintEnum sodApprovalConstraint = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    @SerializedName("NOT_REVIEWED")
    NOT_REVIEWED("NOT_REVIEWED"),
    @SerializedName("APPROVED")
    APPROVED("APPROVED"),
    @SerializedName("APPROVAL_EXPIRED")
    APPROVAL_EXPIRED("APPROVAL_EXPIRED"),
    @SerializedName("RESOLVING")
    RESOLVING("RESOLVING"),
    @SerializedName("ON_HOLD")
    ON_HOLD("ON_HOLD"),
    @SerializedName("CLOSED")
    CLOSED("CLOSED");

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

  @SerializedName("timesDetected")
  private Long timesDetected = null;

  @SerializedName("totalSteps")
  private Long totalSteps = null;

  @SerializedName("toxic")
  private Boolean toxic = null;

  @SerializedName("violatorAppId")
  private Long violatorAppId = null;

  @SerializedName("violatorId")
  private Long violatorId = null;

  @SerializedName("violatorIsDeleted")
  private Boolean violatorIsDeleted = null;

  @SerializedName("violatorIsUser")
  private Boolean violatorIsUser = null;

  @SerializedName("violatorName")
  private String violatorName = null;

  @SerializedName("violatorTitle")
  private String violatorTitle = null;

  public SodCase allowApprove(Boolean allowApprove) {
    this.allowApprove = allowApprove;
    return this;
  }

   /**
   * Get allowApprove
   * @return allowApprove
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowApprove() {
    return allowApprove;
  }

  public void setAllowApprove(Boolean allowApprove) {
    this.allowApprove = allowApprove;
  }

  public SodCase approvalExpireTime(Long approvalExpireTime) {
    this.approvalExpireTime = approvalExpireTime;
    return this;
  }

   /**
   * Get approvalExpireTime
   * @return approvalExpireTime
  **/
  @ApiModelProperty(value = "")
  public Long getApprovalExpireTime() {
    return approvalExpireTime;
  }

  public void setApprovalExpireTime(Long approvalExpireTime) {
    this.approvalExpireTime = approvalExpireTime;
  }

  public SodCase approvalExpired(Boolean approvalExpired) {
    this.approvalExpired = approvalExpired;
    return this;
  }

   /**
   * Get approvalExpired
   * @return approvalExpired
  **/
  @ApiModelProperty(value = "")
  public Boolean isApprovalExpired() {
    return approvalExpired;
  }

  public void setApprovalExpired(Boolean approvalExpired) {
    this.approvalExpired = approvalExpired;
  }

  public SodCase approverCount(Long approverCount) {
    this.approverCount = approverCount;
    return this;
  }

   /**
   * Get approverCount
   * @return approverCount
  **/
  @ApiModelProperty(value = "")
  public Long getApproverCount() {
    return approverCount;
  }

  public void setApproverCount(Long approverCount) {
    this.approverCount = approverCount;
  }

  public SodCase businessRoleUniqueIds(List<UniqueRoleNode> businessRoleUniqueIds) {
    this.businessRoleUniqueIds = businessRoleUniqueIds;
    return this;
  }

  public SodCase addBusinessRoleUniqueIdsItem(UniqueRoleNode businessRoleUniqueIdsItem) {
    if (this.businessRoleUniqueIds == null) {
      this.businessRoleUniqueIds = new ArrayList<UniqueRoleNode>();
    }
    this.businessRoleUniqueIds.add(businessRoleUniqueIdsItem);
    return this;
  }

   /**
   * Get businessRoleUniqueIds
   * @return businessRoleUniqueIds
  **/
  @ApiModelProperty(value = "")
  public List<UniqueRoleNode> getBusinessRoleUniqueIds() {
    return businessRoleUniqueIds;
  }

  public void setBusinessRoleUniqueIds(List<UniqueRoleNode> businessRoleUniqueIds) {
    this.businessRoleUniqueIds = businessRoleUniqueIds;
  }

  public SodCase caseActions(List<SodCaseAction> caseActions) {
    this.caseActions = caseActions;
    return this;
  }

  public SodCase addCaseActionsItem(SodCaseAction caseActionsItem) {
    if (this.caseActions == null) {
      this.caseActions = new ArrayList<SodCaseAction>();
    }
    this.caseActions.add(caseActionsItem);
    return this;
  }

   /**
   * Get caseActions
   * @return caseActions
  **/
  @ApiModelProperty(value = "")
  public List<SodCaseAction> getCaseActions() {
    return caseActions;
  }

  public void setCaseActions(List<SodCaseAction> caseActions) {
    this.caseActions = caseActions;
  }

  public SodCase caseNumber(String caseNumber) {
    this.caseNumber = caseNumber;
    return this;
  }

   /**
   * Get caseNumber
   * @return caseNumber
  **/
  @ApiModelProperty(value = "")
  public String getCaseNumber() {
    return caseNumber;
  }

  public void setCaseNumber(String caseNumber) {
    this.caseNumber = caseNumber;
  }

  public SodCase contributingItem(ContribItem contributingItem) {
    this.contributingItem = contributingItem;
    return this;
  }

   /**
   * Get contributingItem
   * @return contributingItem
  **/
  @ApiModelProperty(value = "")
  public ContribItem getContributingItem() {
    return contributingItem;
  }

  public void setContributingItem(ContribItem contributingItem) {
    this.contributingItem = contributingItem;
  }

  public SodCase currViolationId(Long currViolationId) {
    this.currViolationId = currViolationId;
    return this;
  }

   /**
   * Get currViolationId
   * @return currViolationId
  **/
  @ApiModelProperty(value = "")
  public Long getCurrViolationId() {
    return currViolationId;
  }

  public void setCurrViolationId(Long currViolationId) {
    this.currViolationId = currViolationId;
  }

  public SodCase currentStep(Long currentStep) {
    this.currentStep = currentStep;
    return this;
  }

   /**
   * Get currentStep
   * @return currentStep
  **/
  @ApiModelProperty(value = "")
  public Long getCurrentStep() {
    return currentStep;
  }

  public void setCurrentStep(Long currentStep) {
    this.currentStep = currentStep;
  }

  public SodCase id(Long id) {
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

  public SodCase indirectContributingItems(List<ContribItem> indirectContributingItems) {
    this.indirectContributingItems = indirectContributingItems;
    return this;
  }

  public SodCase addIndirectContributingItemsItem(ContribItem indirectContributingItemsItem) {
    if (this.indirectContributingItems == null) {
      this.indirectContributingItems = new ArrayList<ContribItem>();
    }
    this.indirectContributingItems.add(indirectContributingItemsItem);
    return this;
  }

   /**
   * Get indirectContributingItems
   * @return indirectContributingItems
  **/
  @ApiModelProperty(value = "")
  public List<ContribItem> getIndirectContributingItems() {
    return indirectContributingItems;
  }

  public void setIndirectContributingItems(List<ContribItem> indirectContributingItems) {
    this.indirectContributingItems = indirectContributingItems;
  }

  public SodCase initialDetectionTime(Long initialDetectionTime) {
    this.initialDetectionTime = initialDetectionTime;
    return this;
  }

   /**
   * Get initialDetectionTime
   * @return initialDetectionTime
  **/
  @ApiModelProperty(value = "")
  public Long getInitialDetectionTime() {
    return initialDetectionTime;
  }

  public void setInitialDetectionTime(Long initialDetectionTime) {
    this.initialDetectionTime = initialDetectionTime;
  }

  public SodCase latestDetectionTime(Long latestDetectionTime) {
    this.latestDetectionTime = latestDetectionTime;
    return this;
  }

   /**
   * Get latestDetectionTime
   * @return latestDetectionTime
  **/
  @ApiModelProperty(value = "")
  public Long getLatestDetectionTime() {
    return latestDetectionTime;
  }

  public void setLatestDetectionTime(Long latestDetectionTime) {
    this.latestDetectionTime = latestDetectionTime;
  }

  public SodCase permUniqueIds(List<UniquePermNode> permUniqueIds) {
    this.permUniqueIds = permUniqueIds;
    return this;
  }

  public SodCase addPermUniqueIdsItem(UniquePermNode permUniqueIdsItem) {
    if (this.permUniqueIds == null) {
      this.permUniqueIds = new ArrayList<UniquePermNode>();
    }
    this.permUniqueIds.add(permUniqueIdsItem);
    return this;
  }

   /**
   * Get permUniqueIds
   * @return permUniqueIds
  **/
  @ApiModelProperty(value = "")
  public List<UniquePermNode> getPermUniqueIds() {
    return permUniqueIds;
  }

  public void setPermUniqueIds(List<UniquePermNode> permUniqueIds) {
    this.permUniqueIds = permUniqueIds;
  }

  public SodCase policyId(Long policyId) {
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

  public SodCase policyName(String policyName) {
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

  public SodCase policyState(PolicyStateEnum policyState) {
    this.policyState = policyState;
    return this;
  }

   /**
   * Get policyState
   * @return policyState
  **/
  @ApiModelProperty(value = "")
  public PolicyStateEnum getPolicyState() {
    return policyState;
  }

  public void setPolicyState(PolicyStateEnum policyState) {
    this.policyState = policyState;
  }

  public SodCase preHoldState(PreHoldStateEnum preHoldState) {
    this.preHoldState = preHoldState;
    return this;
  }

   /**
   * Get preHoldState
   * @return preHoldState
  **/
  @ApiModelProperty(value = "")
  public PreHoldStateEnum getPreHoldState() {
    return preHoldState;
  }

  public void setPreHoldState(PreHoldStateEnum preHoldState) {
    this.preHoldState = preHoldState;
  }

  public SodCase roleUniqueIds(List<UniqueRoleNode> roleUniqueIds) {
    this.roleUniqueIds = roleUniqueIds;
    return this;
  }

  public SodCase addRoleUniqueIdsItem(UniqueRoleNode roleUniqueIdsItem) {
    if (this.roleUniqueIds == null) {
      this.roleUniqueIds = new ArrayList<UniqueRoleNode>();
    }
    this.roleUniqueIds.add(roleUniqueIdsItem);
    return this;
  }

   /**
   * Get roleUniqueIds
   * @return roleUniqueIds
  **/
  @ApiModelProperty(value = "")
  public List<UniqueRoleNode> getRoleUniqueIds() {
    return roleUniqueIds;
  }

  public void setRoleUniqueIds(List<UniqueRoleNode> roleUniqueIds) {
    this.roleUniqueIds = roleUniqueIds;
  }

  public SodCase sodApprovalConstraint(SodApprovalConstraintEnum sodApprovalConstraint) {
    this.sodApprovalConstraint = sodApprovalConstraint;
    return this;
  }

   /**
   * Get sodApprovalConstraint
   * @return sodApprovalConstraint
  **/
  @ApiModelProperty(value = "")
  public SodApprovalConstraintEnum getSodApprovalConstraint() {
    return sodApprovalConstraint;
  }

  public void setSodApprovalConstraint(SodApprovalConstraintEnum sodApprovalConstraint) {
    this.sodApprovalConstraint = sodApprovalConstraint;
  }

  public SodCase state(StateEnum state) {
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

  public SodCase timesDetected(Long timesDetected) {
    this.timesDetected = timesDetected;
    return this;
  }

   /**
   * Get timesDetected
   * @return timesDetected
  **/
  @ApiModelProperty(value = "")
  public Long getTimesDetected() {
    return timesDetected;
  }

  public void setTimesDetected(Long timesDetected) {
    this.timesDetected = timesDetected;
  }

  public SodCase totalSteps(Long totalSteps) {
    this.totalSteps = totalSteps;
    return this;
  }

   /**
   * Get totalSteps
   * @return totalSteps
  **/
  @ApiModelProperty(value = "")
  public Long getTotalSteps() {
    return totalSteps;
  }

  public void setTotalSteps(Long totalSteps) {
    this.totalSteps = totalSteps;
  }

  public SodCase toxic(Boolean toxic) {
    this.toxic = toxic;
    return this;
  }

   /**
   * Get toxic
   * @return toxic
  **/
  @ApiModelProperty(value = "")
  public Boolean isToxic() {
    return toxic;
  }

  public void setToxic(Boolean toxic) {
    this.toxic = toxic;
  }

  public SodCase violatorAppId(Long violatorAppId) {
    this.violatorAppId = violatorAppId;
    return this;
  }

   /**
   * Get violatorAppId
   * @return violatorAppId
  **/
  @ApiModelProperty(value = "")
  public Long getViolatorAppId() {
    return violatorAppId;
  }

  public void setViolatorAppId(Long violatorAppId) {
    this.violatorAppId = violatorAppId;
  }

  public SodCase violatorId(Long violatorId) {
    this.violatorId = violatorId;
    return this;
  }

   /**
   * Get violatorId
   * @return violatorId
  **/
  @ApiModelProperty(value = "")
  public Long getViolatorId() {
    return violatorId;
  }

  public void setViolatorId(Long violatorId) {
    this.violatorId = violatorId;
  }

  public SodCase violatorIsDeleted(Boolean violatorIsDeleted) {
    this.violatorIsDeleted = violatorIsDeleted;
    return this;
  }

   /**
   * Get violatorIsDeleted
   * @return violatorIsDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isViolatorIsDeleted() {
    return violatorIsDeleted;
  }

  public void setViolatorIsDeleted(Boolean violatorIsDeleted) {
    this.violatorIsDeleted = violatorIsDeleted;
  }

  public SodCase violatorIsUser(Boolean violatorIsUser) {
    this.violatorIsUser = violatorIsUser;
    return this;
  }

   /**
   * Get violatorIsUser
   * @return violatorIsUser
  **/
  @ApiModelProperty(value = "")
  public Boolean isViolatorIsUser() {
    return violatorIsUser;
  }

  public void setViolatorIsUser(Boolean violatorIsUser) {
    this.violatorIsUser = violatorIsUser;
  }

  public SodCase violatorName(String violatorName) {
    this.violatorName = violatorName;
    return this;
  }

   /**
   * Get violatorName
   * @return violatorName
  **/
  @ApiModelProperty(value = "")
  public String getViolatorName() {
    return violatorName;
  }

  public void setViolatorName(String violatorName) {
    this.violatorName = violatorName;
  }

  public SodCase violatorTitle(String violatorTitle) {
    this.violatorTitle = violatorTitle;
    return this;
  }

   /**
   * Get violatorTitle
   * @return violatorTitle
  **/
  @ApiModelProperty(value = "")
  public String getViolatorTitle() {
    return violatorTitle;
  }

  public void setViolatorTitle(String violatorTitle) {
    this.violatorTitle = violatorTitle;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SodCase sodCase = (SodCase) o;
    return Objects.equals(this.allowApprove, sodCase.allowApprove) &&
        Objects.equals(this.approvalExpireTime, sodCase.approvalExpireTime) &&
        Objects.equals(this.approvalExpired, sodCase.approvalExpired) &&
        Objects.equals(this.approverCount, sodCase.approverCount) &&
        Objects.equals(this.businessRoleUniqueIds, sodCase.businessRoleUniqueIds) &&
        Objects.equals(this.caseActions, sodCase.caseActions) &&
        Objects.equals(this.caseNumber, sodCase.caseNumber) &&
        Objects.equals(this.contributingItem, sodCase.contributingItem) &&
        Objects.equals(this.currViolationId, sodCase.currViolationId) &&
        Objects.equals(this.currentStep, sodCase.currentStep) &&
        Objects.equals(this.id, sodCase.id) &&
        Objects.equals(this.indirectContributingItems, sodCase.indirectContributingItems) &&
        Objects.equals(this.initialDetectionTime, sodCase.initialDetectionTime) &&
        Objects.equals(this.latestDetectionTime, sodCase.latestDetectionTime) &&
        Objects.equals(this.permUniqueIds, sodCase.permUniqueIds) &&
        Objects.equals(this.policyId, sodCase.policyId) &&
        Objects.equals(this.policyName, sodCase.policyName) &&
        Objects.equals(this.policyState, sodCase.policyState) &&
        Objects.equals(this.preHoldState, sodCase.preHoldState) &&
        Objects.equals(this.roleUniqueIds, sodCase.roleUniqueIds) &&
        Objects.equals(this.sodApprovalConstraint, sodCase.sodApprovalConstraint) &&
        Objects.equals(this.state, sodCase.state) &&
        Objects.equals(this.timesDetected, sodCase.timesDetected) &&
        Objects.equals(this.totalSteps, sodCase.totalSteps) &&
        Objects.equals(this.toxic, sodCase.toxic) &&
        Objects.equals(this.violatorAppId, sodCase.violatorAppId) &&
        Objects.equals(this.violatorId, sodCase.violatorId) &&
        Objects.equals(this.violatorIsDeleted, sodCase.violatorIsDeleted) &&
        Objects.equals(this.violatorIsUser, sodCase.violatorIsUser) &&
        Objects.equals(this.violatorName, sodCase.violatorName) &&
        Objects.equals(this.violatorTitle, sodCase.violatorTitle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowApprove, approvalExpireTime, approvalExpired, approverCount, businessRoleUniqueIds, caseActions, caseNumber, contributingItem, currViolationId, currentStep, id, indirectContributingItems, initialDetectionTime, latestDetectionTime, permUniqueIds, policyId, policyName, policyState, preHoldState, roleUniqueIds, sodApprovalConstraint, state, timesDetected, totalSteps, toxic, violatorAppId, violatorId, violatorIsDeleted, violatorIsUser, violatorName, violatorTitle);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SodCase {\n");
    
    sb.append("    allowApprove: ").append(toIndentedString(allowApprove)).append("\n");
    sb.append("    approvalExpireTime: ").append(toIndentedString(approvalExpireTime)).append("\n");
    sb.append("    approvalExpired: ").append(toIndentedString(approvalExpired)).append("\n");
    sb.append("    approverCount: ").append(toIndentedString(approverCount)).append("\n");
    sb.append("    businessRoleUniqueIds: ").append(toIndentedString(businessRoleUniqueIds)).append("\n");
    sb.append("    caseActions: ").append(toIndentedString(caseActions)).append("\n");
    sb.append("    caseNumber: ").append(toIndentedString(caseNumber)).append("\n");
    sb.append("    contributingItem: ").append(toIndentedString(contributingItem)).append("\n");
    sb.append("    currViolationId: ").append(toIndentedString(currViolationId)).append("\n");
    sb.append("    currentStep: ").append(toIndentedString(currentStep)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    indirectContributingItems: ").append(toIndentedString(indirectContributingItems)).append("\n");
    sb.append("    initialDetectionTime: ").append(toIndentedString(initialDetectionTime)).append("\n");
    sb.append("    latestDetectionTime: ").append(toIndentedString(latestDetectionTime)).append("\n");
    sb.append("    permUniqueIds: ").append(toIndentedString(permUniqueIds)).append("\n");
    sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
    sb.append("    policyName: ").append(toIndentedString(policyName)).append("\n");
    sb.append("    policyState: ").append(toIndentedString(policyState)).append("\n");
    sb.append("    preHoldState: ").append(toIndentedString(preHoldState)).append("\n");
    sb.append("    roleUniqueIds: ").append(toIndentedString(roleUniqueIds)).append("\n");
    sb.append("    sodApprovalConstraint: ").append(toIndentedString(sodApprovalConstraint)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    timesDetected: ").append(toIndentedString(timesDetected)).append("\n");
    sb.append("    totalSteps: ").append(toIndentedString(totalSteps)).append("\n");
    sb.append("    toxic: ").append(toIndentedString(toxic)).append("\n");
    sb.append("    violatorAppId: ").append(toIndentedString(violatorAppId)).append("\n");
    sb.append("    violatorId: ").append(toIndentedString(violatorId)).append("\n");
    sb.append("    violatorIsDeleted: ").append(toIndentedString(violatorIsDeleted)).append("\n");
    sb.append("    violatorIsUser: ").append(toIndentedString(violatorIsUser)).append("\n");
    sb.append("    violatorName: ").append(toIndentedString(violatorName)).append("\n");
    sb.append("    violatorTitle: ").append(toIndentedString(violatorTitle)).append("\n");
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
