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
import io.swagger.client.model.Attribute;
import io.swagger.client.model.Reviewentity;
import io.swagger.client.model.Reviewer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ReviewItemAction
 */



public class ReviewItemAction {
  @SerializedName("accountCategoryId")
  private Long accountCategoryId = null;

  @SerializedName("accountCategoryName")
  private String accountCategoryName = null;

  @SerializedName("actedByPolicy")
  private Boolean actedByPolicy = null;

  @SerializedName("actedOnByUser")
  private Reviewer actedOnByUser = null;

  /**
   * Gets or Sets action
   */
  @JsonAdapter(ActionEnum.Adapter.class)
  public enum ActionEnum {
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

    ActionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ActionEnum fromValue(String input) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ActionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ActionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ActionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ActionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("action")
  private ActionEnum action = null;

  @SerializedName("actionId")
  private Long actionId = null;

  @SerializedName("actionIds")
  private List<Long> actionIds = null;

  @SerializedName("assignedUsers")
  private List<Reviewer> assignedUsers = null;

  @SerializedName("assignmentTime")
  private Long assignmentTime = null;

  @SerializedName("attributes")
  private List<Attribute> attributes = null;

  @SerializedName("causedByException")
  private Boolean causedByException = null;

  @SerializedName("causedByExceptionMsg")
  private String causedByExceptionMsg = null;

  @SerializedName("comment")
  private String comment = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("dueTime")
  private Long dueTime = null;

  @SerializedName("entities")
  private List<Reviewentity> entities = null;

  @SerializedName("fulfillmentInstructions")
  private String fulfillmentInstructions = null;

  @SerializedName("inProgressPendingOnly")
  private Boolean inProgressPendingOnly = null;

  @SerializedName("itemId")
  private Long itemId = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("permId")
  private Long permId = null;

  @SerializedName("q")
  private String q = null;

  @SerializedName("qCols")
  private List<String> qCols = null;

  @SerializedName("qMatch")
  private String qMatch = null;

  @SerializedName("readyToSubmit")
  private Boolean readyToSubmit = null;

  @SerializedName("reasonId")
  private Long reasonId = null;

  @SerializedName("reasonName")
  private String reasonName = null;

  @SerializedName("reassignedBy")
  private Reviewer reassignedBy = null;

  @SerializedName("reassignedTo")
  private Reviewer reassignedTo = null;

  @SerializedName("removedEntities")
  private List<Reviewentity> removedEntities = null;

  @SerializedName("removedUsers")
  private List<Reviewer> removedUsers = null;

  @SerializedName("reviewOrder")
  private Integer reviewOrder = null;

  @SerializedName("reviewer")
  private Reviewer reviewer = null;

  @SerializedName("reviewerId")
  private Long reviewerId = null;

  /**
   * Gets or Sets reviewerType
   */
  @JsonAdapter(ReviewerTypeEnum.Adapter.class)
  public enum ReviewerTypeEnum {
    @SerializedName("BUSINESS_ROLE_OWNER")
    BUSINESS_ROLE_OWNER("BUSINESS_ROLE_OWNER"),
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("GROUP")
    GROUP("GROUP"),
    @SerializedName("SUPERVISOR")
    SUPERVISOR("SUPERVISOR"),
    @SerializedName("APPLICATION_OWNER")
    APPLICATION_OWNER("APPLICATION_OWNER"),
    @SerializedName("PERMISSION_OWNER")
    PERMISSION_OWNER("PERMISSION_OWNER"),
    @SerializedName("SELF")
    SELF("SELF"),
    @SerializedName("EO")
    EO("EO"),
    @SerializedName("RO")
    RO("RO"),
    @SerializedName("EXCEPTION_REVIEWER")
    EXCEPTION_REVIEWER("EXCEPTION_REVIEWER"),
    @SerializedName("AUDITOR")
    AUDITOR("AUDITOR"),
    @SerializedName("V11")
    V11("V11"),
    @SerializedName("COVERAGE_MAP")
    COVERAGE_MAP("COVERAGE_MAP"),
    @SerializedName("ACCOUNT_OWNER")
    ACCOUNT_OWNER("ACCOUNT_OWNER"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("TECHNICAL_ROLE_OWNER")
    TECHNICAL_ROLE_OWNER("TECHNICAL_ROLE_OWNER");

    private String value;

    ReviewerTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ReviewerTypeEnum fromValue(String input) {
      for (ReviewerTypeEnum b : ReviewerTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ReviewerTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReviewerTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ReviewerTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ReviewerTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("reviewerType")
  private ReviewerTypeEnum reviewerType = null;

  @SerializedName("revrInstId")
  private Long revrInstId = null;

  @SerializedName("sharedQueue")
  private Boolean sharedQueue = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS("IN_PROGRESS"),
    @SerializedName("OVERRIDDEN")
    OVERRIDDEN("OVERRIDDEN"),
    @SerializedName("ESCALATED")
    ESCALATED("ESCALATED"),
    @SerializedName("CERTIFIED")
    CERTIFIED("CERTIFIED"),
    @SerializedName("REASSIGNED")
    REASSIGNED("REASSIGNED"),
    @SerializedName("CANCELED")
    CANCELED("CANCELED");

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

  @SerializedName("targetUsers")
  private List<Reviewer> targetUsers = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  public ReviewItemAction accountCategoryId(Long accountCategoryId) {
    this.accountCategoryId = accountCategoryId;
    return this;
  }

   /**
   * Get accountCategoryId
   * @return accountCategoryId
  **/
  @ApiModelProperty(value = "")
  public Long getAccountCategoryId() {
    return accountCategoryId;
  }

  public void setAccountCategoryId(Long accountCategoryId) {
    this.accountCategoryId = accountCategoryId;
  }

  public ReviewItemAction accountCategoryName(String accountCategoryName) {
    this.accountCategoryName = accountCategoryName;
    return this;
  }

   /**
   * Get accountCategoryName
   * @return accountCategoryName
  **/
  @ApiModelProperty(value = "")
  public String getAccountCategoryName() {
    return accountCategoryName;
  }

  public void setAccountCategoryName(String accountCategoryName) {
    this.accountCategoryName = accountCategoryName;
  }

  public ReviewItemAction actedByPolicy(Boolean actedByPolicy) {
    this.actedByPolicy = actedByPolicy;
    return this;
  }

   /**
   * Get actedByPolicy
   * @return actedByPolicy
  **/
  @ApiModelProperty(value = "")
  public Boolean isActedByPolicy() {
    return actedByPolicy;
  }

  public void setActedByPolicy(Boolean actedByPolicy) {
    this.actedByPolicy = actedByPolicy;
  }

  public ReviewItemAction actedOnByUser(Reviewer actedOnByUser) {
    this.actedOnByUser = actedOnByUser;
    return this;
  }

   /**
   * Get actedOnByUser
   * @return actedOnByUser
  **/
  @ApiModelProperty(value = "")
  public Reviewer getActedOnByUser() {
    return actedOnByUser;
  }

  public void setActedOnByUser(Reviewer actedOnByUser) {
    this.actedOnByUser = actedOnByUser;
  }

  public ReviewItemAction action(ActionEnum action) {
    this.action = action;
    return this;
  }

   /**
   * Get action
   * @return action
  **/
  @ApiModelProperty(value = "")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public ReviewItemAction actionId(Long actionId) {
    this.actionId = actionId;
    return this;
  }

   /**
   * Get actionId
   * @return actionId
  **/
  @ApiModelProperty(value = "")
  public Long getActionId() {
    return actionId;
  }

  public void setActionId(Long actionId) {
    this.actionId = actionId;
  }

  public ReviewItemAction actionIds(List<Long> actionIds) {
    this.actionIds = actionIds;
    return this;
  }

  public ReviewItemAction addActionIdsItem(Long actionIdsItem) {
    if (this.actionIds == null) {
      this.actionIds = new ArrayList<Long>();
    }
    this.actionIds.add(actionIdsItem);
    return this;
  }

   /**
   * Get actionIds
   * @return actionIds
  **/
  @ApiModelProperty(value = "")
  public List<Long> getActionIds() {
    return actionIds;
  }

  public void setActionIds(List<Long> actionIds) {
    this.actionIds = actionIds;
  }

  public ReviewItemAction assignedUsers(List<Reviewer> assignedUsers) {
    this.assignedUsers = assignedUsers;
    return this;
  }

  public ReviewItemAction addAssignedUsersItem(Reviewer assignedUsersItem) {
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

  public ReviewItemAction assignmentTime(Long assignmentTime) {
    this.assignmentTime = assignmentTime;
    return this;
  }

   /**
   * Get assignmentTime
   * @return assignmentTime
  **/
  @ApiModelProperty(value = "")
  public Long getAssignmentTime() {
    return assignmentTime;
  }

  public void setAssignmentTime(Long assignmentTime) {
    this.assignmentTime = assignmentTime;
  }

  public ReviewItemAction attributes(List<Attribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public ReviewItemAction addAttributesItem(Attribute attributesItem) {
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

  public ReviewItemAction causedByException(Boolean causedByException) {
    this.causedByException = causedByException;
    return this;
  }

   /**
   * Get causedByException
   * @return causedByException
  **/
  @ApiModelProperty(value = "")
  public Boolean isCausedByException() {
    return causedByException;
  }

  public void setCausedByException(Boolean causedByException) {
    this.causedByException = causedByException;
  }

  public ReviewItemAction causedByExceptionMsg(String causedByExceptionMsg) {
    this.causedByExceptionMsg = causedByExceptionMsg;
    return this;
  }

   /**
   * Get causedByExceptionMsg
   * @return causedByExceptionMsg
  **/
  @ApiModelProperty(value = "")
  public String getCausedByExceptionMsg() {
    return causedByExceptionMsg;
  }

  public void setCausedByExceptionMsg(String causedByExceptionMsg) {
    this.causedByExceptionMsg = causedByExceptionMsg;
  }

  public ReviewItemAction comment(String comment) {
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

  public ReviewItemAction displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @ApiModelProperty(value = "")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public ReviewItemAction dueTime(Long dueTime) {
    this.dueTime = dueTime;
    return this;
  }

   /**
   * Get dueTime
   * @return dueTime
  **/
  @ApiModelProperty(value = "")
  public Long getDueTime() {
    return dueTime;
  }

  public void setDueTime(Long dueTime) {
    this.dueTime = dueTime;
  }

  public ReviewItemAction entities(List<Reviewentity> entities) {
    this.entities = entities;
    return this;
  }

  public ReviewItemAction addEntitiesItem(Reviewentity entitiesItem) {
    if (this.entities == null) {
      this.entities = new ArrayList<Reviewentity>();
    }
    this.entities.add(entitiesItem);
    return this;
  }

   /**
   * Get entities
   * @return entities
  **/
  @ApiModelProperty(value = "")
  public List<Reviewentity> getEntities() {
    return entities;
  }

  public void setEntities(List<Reviewentity> entities) {
    this.entities = entities;
  }

  public ReviewItemAction fulfillmentInstructions(String fulfillmentInstructions) {
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

  public ReviewItemAction inProgressPendingOnly(Boolean inProgressPendingOnly) {
    this.inProgressPendingOnly = inProgressPendingOnly;
    return this;
  }

   /**
   * Get inProgressPendingOnly
   * @return inProgressPendingOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean isInProgressPendingOnly() {
    return inProgressPendingOnly;
  }

  public void setInProgressPendingOnly(Boolean inProgressPendingOnly) {
    this.inProgressPendingOnly = inProgressPendingOnly;
  }

  public ReviewItemAction itemId(Long itemId) {
    this.itemId = itemId;
    return this;
  }

   /**
   * Get itemId
   * @return itemId
  **/
  @ApiModelProperty(value = "")
  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public ReviewItemAction link(String link) {
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

  public ReviewItemAction permId(Long permId) {
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

  public ReviewItemAction q(String q) {
    this.q = q;
    return this;
  }

   /**
   * Get q
   * @return q
  **/
  @ApiModelProperty(value = "")
  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public ReviewItemAction qCols(List<String> qCols) {
    this.qCols = qCols;
    return this;
  }

  public ReviewItemAction addQColsItem(String qColsItem) {
    if (this.qCols == null) {
      this.qCols = new ArrayList<String>();
    }
    this.qCols.add(qColsItem);
    return this;
  }

   /**
   * Get qCols
   * @return qCols
  **/
  @ApiModelProperty(value = "")
  public List<String> getQCols() {
    return qCols;
  }

  public void setQCols(List<String> qCols) {
    this.qCols = qCols;
  }

  public ReviewItemAction qMatch(String qMatch) {
    this.qMatch = qMatch;
    return this;
  }

   /**
   * Get qMatch
   * @return qMatch
  **/
  @ApiModelProperty(value = "")
  public String getQMatch() {
    return qMatch;
  }

  public void setQMatch(String qMatch) {
    this.qMatch = qMatch;
  }

  public ReviewItemAction readyToSubmit(Boolean readyToSubmit) {
    this.readyToSubmit = readyToSubmit;
    return this;
  }

   /**
   * Get readyToSubmit
   * @return readyToSubmit
  **/
  @ApiModelProperty(value = "")
  public Boolean isReadyToSubmit() {
    return readyToSubmit;
  }

  public void setReadyToSubmit(Boolean readyToSubmit) {
    this.readyToSubmit = readyToSubmit;
  }

  public ReviewItemAction reasonId(Long reasonId) {
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

  public ReviewItemAction reasonName(String reasonName) {
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

  public ReviewItemAction reassignedBy(Reviewer reassignedBy) {
    this.reassignedBy = reassignedBy;
    return this;
  }

   /**
   * Get reassignedBy
   * @return reassignedBy
  **/
  @ApiModelProperty(value = "")
  public Reviewer getReassignedBy() {
    return reassignedBy;
  }

  public void setReassignedBy(Reviewer reassignedBy) {
    this.reassignedBy = reassignedBy;
  }

  public ReviewItemAction reassignedTo(Reviewer reassignedTo) {
    this.reassignedTo = reassignedTo;
    return this;
  }

   /**
   * Get reassignedTo
   * @return reassignedTo
  **/
  @ApiModelProperty(value = "")
  public Reviewer getReassignedTo() {
    return reassignedTo;
  }

  public void setReassignedTo(Reviewer reassignedTo) {
    this.reassignedTo = reassignedTo;
  }

  public ReviewItemAction removedEntities(List<Reviewentity> removedEntities) {
    this.removedEntities = removedEntities;
    return this;
  }

  public ReviewItemAction addRemovedEntitiesItem(Reviewentity removedEntitiesItem) {
    if (this.removedEntities == null) {
      this.removedEntities = new ArrayList<Reviewentity>();
    }
    this.removedEntities.add(removedEntitiesItem);
    return this;
  }

   /**
   * Get removedEntities
   * @return removedEntities
  **/
  @ApiModelProperty(value = "")
  public List<Reviewentity> getRemovedEntities() {
    return removedEntities;
  }

  public void setRemovedEntities(List<Reviewentity> removedEntities) {
    this.removedEntities = removedEntities;
  }

  public ReviewItemAction removedUsers(List<Reviewer> removedUsers) {
    this.removedUsers = removedUsers;
    return this;
  }

  public ReviewItemAction addRemovedUsersItem(Reviewer removedUsersItem) {
    if (this.removedUsers == null) {
      this.removedUsers = new ArrayList<Reviewer>();
    }
    this.removedUsers.add(removedUsersItem);
    return this;
  }

   /**
   * Get removedUsers
   * @return removedUsers
  **/
  @ApiModelProperty(value = "")
  public List<Reviewer> getRemovedUsers() {
    return removedUsers;
  }

  public void setRemovedUsers(List<Reviewer> removedUsers) {
    this.removedUsers = removedUsers;
  }

  public ReviewItemAction reviewOrder(Integer reviewOrder) {
    this.reviewOrder = reviewOrder;
    return this;
  }

   /**
   * Get reviewOrder
   * @return reviewOrder
  **/
  @ApiModelProperty(value = "")
  public Integer getReviewOrder() {
    return reviewOrder;
  }

  public void setReviewOrder(Integer reviewOrder) {
    this.reviewOrder = reviewOrder;
  }

  public ReviewItemAction reviewer(Reviewer reviewer) {
    this.reviewer = reviewer;
    return this;
  }

   /**
   * Get reviewer
   * @return reviewer
  **/
  @ApiModelProperty(value = "")
  public Reviewer getReviewer() {
    return reviewer;
  }

  public void setReviewer(Reviewer reviewer) {
    this.reviewer = reviewer;
  }

  public ReviewItemAction reviewerId(Long reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

   /**
   * Get reviewerId
   * @return reviewerId
  **/
  @ApiModelProperty(value = "")
  public Long getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(Long reviewerId) {
    this.reviewerId = reviewerId;
  }

  public ReviewItemAction reviewerType(ReviewerTypeEnum reviewerType) {
    this.reviewerType = reviewerType;
    return this;
  }

   /**
   * Get reviewerType
   * @return reviewerType
  **/
  @ApiModelProperty(value = "")
  public ReviewerTypeEnum getReviewerType() {
    return reviewerType;
  }

  public void setReviewerType(ReviewerTypeEnum reviewerType) {
    this.reviewerType = reviewerType;
  }

  public ReviewItemAction revrInstId(Long revrInstId) {
    this.revrInstId = revrInstId;
    return this;
  }

   /**
   * Get revrInstId
   * @return revrInstId
  **/
  @ApiModelProperty(value = "")
  public Long getRevrInstId() {
    return revrInstId;
  }

  public void setRevrInstId(Long revrInstId) {
    this.revrInstId = revrInstId;
  }

  public ReviewItemAction sharedQueue(Boolean sharedQueue) {
    this.sharedQueue = sharedQueue;
    return this;
  }

   /**
   * Get sharedQueue
   * @return sharedQueue
  **/
  @ApiModelProperty(value = "")
  public Boolean isSharedQueue() {
    return sharedQueue;
  }

  public void setSharedQueue(Boolean sharedQueue) {
    this.sharedQueue = sharedQueue;
  }

  public ReviewItemAction status(StatusEnum status) {
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

  public ReviewItemAction targetUsers(List<Reviewer> targetUsers) {
    this.targetUsers = targetUsers;
    return this;
  }

  public ReviewItemAction addTargetUsersItem(Reviewer targetUsersItem) {
    if (this.targetUsers == null) {
      this.targetUsers = new ArrayList<Reviewer>();
    }
    this.targetUsers.add(targetUsersItem);
    return this;
  }

   /**
   * Get targetUsers
   * @return targetUsers
  **/
  @ApiModelProperty(value = "")
  public List<Reviewer> getTargetUsers() {
    return targetUsers;
  }

  public void setTargetUsers(List<Reviewer> targetUsers) {
    this.targetUsers = targetUsers;
  }

  public ReviewItemAction updateTime(Long updateTime) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewItemAction reviewItemAction = (ReviewItemAction) o;
    return Objects.equals(this.accountCategoryId, reviewItemAction.accountCategoryId) &&
        Objects.equals(this.accountCategoryName, reviewItemAction.accountCategoryName) &&
        Objects.equals(this.actedByPolicy, reviewItemAction.actedByPolicy) &&
        Objects.equals(this.actedOnByUser, reviewItemAction.actedOnByUser) &&
        Objects.equals(this.action, reviewItemAction.action) &&
        Objects.equals(this.actionId, reviewItemAction.actionId) &&
        Objects.equals(this.actionIds, reviewItemAction.actionIds) &&
        Objects.equals(this.assignedUsers, reviewItemAction.assignedUsers) &&
        Objects.equals(this.assignmentTime, reviewItemAction.assignmentTime) &&
        Objects.equals(this.attributes, reviewItemAction.attributes) &&
        Objects.equals(this.causedByException, reviewItemAction.causedByException) &&
        Objects.equals(this.causedByExceptionMsg, reviewItemAction.causedByExceptionMsg) &&
        Objects.equals(this.comment, reviewItemAction.comment) &&
        Objects.equals(this.displayName, reviewItemAction.displayName) &&
        Objects.equals(this.dueTime, reviewItemAction.dueTime) &&
        Objects.equals(this.entities, reviewItemAction.entities) &&
        Objects.equals(this.fulfillmentInstructions, reviewItemAction.fulfillmentInstructions) &&
        Objects.equals(this.inProgressPendingOnly, reviewItemAction.inProgressPendingOnly) &&
        Objects.equals(this.itemId, reviewItemAction.itemId) &&
        Objects.equals(this.link, reviewItemAction.link) &&
        Objects.equals(this.permId, reviewItemAction.permId) &&
        Objects.equals(this.q, reviewItemAction.q) &&
        Objects.equals(this.qCols, reviewItemAction.qCols) &&
        Objects.equals(this.qMatch, reviewItemAction.qMatch) &&
        Objects.equals(this.readyToSubmit, reviewItemAction.readyToSubmit) &&
        Objects.equals(this.reasonId, reviewItemAction.reasonId) &&
        Objects.equals(this.reasonName, reviewItemAction.reasonName) &&
        Objects.equals(this.reassignedBy, reviewItemAction.reassignedBy) &&
        Objects.equals(this.reassignedTo, reviewItemAction.reassignedTo) &&
        Objects.equals(this.removedEntities, reviewItemAction.removedEntities) &&
        Objects.equals(this.removedUsers, reviewItemAction.removedUsers) &&
        Objects.equals(this.reviewOrder, reviewItemAction.reviewOrder) &&
        Objects.equals(this.reviewer, reviewItemAction.reviewer) &&
        Objects.equals(this.reviewerId, reviewItemAction.reviewerId) &&
        Objects.equals(this.reviewerType, reviewItemAction.reviewerType) &&
        Objects.equals(this.revrInstId, reviewItemAction.revrInstId) &&
        Objects.equals(this.sharedQueue, reviewItemAction.sharedQueue) &&
        Objects.equals(this.status, reviewItemAction.status) &&
        Objects.equals(this.targetUsers, reviewItemAction.targetUsers) &&
        Objects.equals(this.updateTime, reviewItemAction.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountCategoryId, accountCategoryName, actedByPolicy, actedOnByUser, action, actionId, actionIds, assignedUsers, assignmentTime, attributes, causedByException, causedByExceptionMsg, comment, displayName, dueTime, entities, fulfillmentInstructions, inProgressPendingOnly, itemId, link, permId, q, qCols, qMatch, readyToSubmit, reasonId, reasonName, reassignedBy, reassignedTo, removedEntities, removedUsers, reviewOrder, reviewer, reviewerId, reviewerType, revrInstId, sharedQueue, status, targetUsers, updateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewItemAction {\n");
    
    sb.append("    accountCategoryId: ").append(toIndentedString(accountCategoryId)).append("\n");
    sb.append("    accountCategoryName: ").append(toIndentedString(accountCategoryName)).append("\n");
    sb.append("    actedByPolicy: ").append(toIndentedString(actedByPolicy)).append("\n");
    sb.append("    actedOnByUser: ").append(toIndentedString(actedOnByUser)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    actionIds: ").append(toIndentedString(actionIds)).append("\n");
    sb.append("    assignedUsers: ").append(toIndentedString(assignedUsers)).append("\n");
    sb.append("    assignmentTime: ").append(toIndentedString(assignmentTime)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    causedByException: ").append(toIndentedString(causedByException)).append("\n");
    sb.append("    causedByExceptionMsg: ").append(toIndentedString(causedByExceptionMsg)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    dueTime: ").append(toIndentedString(dueTime)).append("\n");
    sb.append("    entities: ").append(toIndentedString(entities)).append("\n");
    sb.append("    fulfillmentInstructions: ").append(toIndentedString(fulfillmentInstructions)).append("\n");
    sb.append("    inProgressPendingOnly: ").append(toIndentedString(inProgressPendingOnly)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    permId: ").append(toIndentedString(permId)).append("\n");
    sb.append("    q: ").append(toIndentedString(q)).append("\n");
    sb.append("    qCols: ").append(toIndentedString(qCols)).append("\n");
    sb.append("    qMatch: ").append(toIndentedString(qMatch)).append("\n");
    sb.append("    readyToSubmit: ").append(toIndentedString(readyToSubmit)).append("\n");
    sb.append("    reasonId: ").append(toIndentedString(reasonId)).append("\n");
    sb.append("    reasonName: ").append(toIndentedString(reasonName)).append("\n");
    sb.append("    reassignedBy: ").append(toIndentedString(reassignedBy)).append("\n");
    sb.append("    reassignedTo: ").append(toIndentedString(reassignedTo)).append("\n");
    sb.append("    removedEntities: ").append(toIndentedString(removedEntities)).append("\n");
    sb.append("    removedUsers: ").append(toIndentedString(removedUsers)).append("\n");
    sb.append("    reviewOrder: ").append(toIndentedString(reviewOrder)).append("\n");
    sb.append("    reviewer: ").append(toIndentedString(reviewer)).append("\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    reviewerType: ").append(toIndentedString(reviewerType)).append("\n");
    sb.append("    revrInstId: ").append(toIndentedString(revrInstId)).append("\n");
    sb.append("    sharedQueue: ").append(toIndentedString(sharedQueue)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    targetUsers: ").append(toIndentedString(targetUsers)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
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
