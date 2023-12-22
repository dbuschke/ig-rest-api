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
import io.swagger.client.model.Param;
import io.swagger.client.model.RequestItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
/**
 * RequestItem
 */



public class RequestItem {
  @SerializedName("appDisplayName")
  private String appDisplayName = null;

  @SerializedName("approvalFormId")
  private Long approvalFormId = null;

  @SerializedName("canRequestThisForOthers")
  private Boolean canRequestThisForOthers = null;

  @SerializedName("currentApprovalStep")
  private Long currentApprovalStep = null;

  /**
   * Gets or Sets decision
   */
  @JsonAdapter(DecisionEnum.Adapter.class)
  public enum DecisionEnum {
    @SerializedName("ERROR_STARTING_PSODV_APPROVAL")
    ERROR_STARTING_PSODV_APPROVAL("ERROR_STARTING_PSODV_APPROVAL"),
    @SerializedName("WAITING_PSODV_APPROVAL")
    WAITING_PSODV_APPROVAL("WAITING_PSODV_APPROVAL"),
    @SerializedName("ERROR_STARTING_APPROVAL")
    ERROR_STARTING_APPROVAL("ERROR_STARTING_APPROVAL"),
    @SerializedName("ERROR_STARTING_REQUEST")
    ERROR_STARTING_REQUEST("ERROR_STARTING_REQUEST"),
    @SerializedName("NO_DECISION")
    NO_DECISION("NO_DECISION"),
    @SerializedName("TENTATIVE_APPROVED")
    TENTATIVE_APPROVED("TENTATIVE_APPROVED"),
    @SerializedName("TENTATIVE_DENIED")
    TENTATIVE_DENIED("TENTATIVE_DENIED"),
    @SerializedName("TENTATIVE_RETRACTED")
    TENTATIVE_RETRACTED("TENTATIVE_RETRACTED"),
    @SerializedName("APPROVED")
    APPROVED("APPROVED"),
    @SerializedName("DENIED")
    DENIED("DENIED"),
    @SerializedName("RETRACTED")
    RETRACTED("RETRACTED"),
    @SerializedName("CLEARED")
    CLEARED("CLEARED");

    private String value;

    DecisionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DecisionEnum fromValue(String input) {
      for (DecisionEnum b : DecisionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DecisionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DecisionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DecisionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DecisionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("decision")
  private DecisionEnum decision = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("effectiveDate")
  private OffsetDateTime effectiveDate = null;

  @SerializedName("expirationDate")
  private OffsetDateTime expirationDate = null;

  @SerializedName("flowdata")
  private String flowdata = null;

  @SerializedName("fulfillmentInstructions")
  private String fulfillmentInstructions = null;

  @SerializedName("hasRequiredField")
  private Boolean hasRequiredField = null;

  @SerializedName("hideResourceImage")
  private Boolean hideResourceImage = null;

  @SerializedName("imageUrl")
  private String imageUrl = null;

  @SerializedName("instanceGuid")
  private String instanceGuid = null;

  @SerializedName("matchesFilter")
  private Boolean matchesFilter = null;

  @SerializedName("originalItemDisplayName")
  private String originalItemDisplayName = null;

  @SerializedName("originalRequestItem")
  private String originalRequestItem = null;

  @SerializedName("params")
  private List<Param> params = null;

  @SerializedName("permType")
  private String permType = null;

  @SerializedName("reason")
  private String reason = null;

  @SerializedName("recipient")
  private String recipient = null;

  @SerializedName("recipientDeleted")
  private Boolean recipientDeleted = null;

  @SerializedName("recipientDisplayName")
  private String recipientDisplayName = null;

  @SerializedName("requestFormId")
  private Long requestFormId = null;

  @SerializedName("requestItem")
  private String requestItem = null;

  /**
   * Gets or Sets requestType
   */
  @JsonAdapter(RequestTypeEnum.Adapter.class)
  public enum RequestTypeEnum {
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

    RequestTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RequestTypeEnum fromValue(String input) {
      for (RequestTypeEnum b : RequestTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RequestTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RequestTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RequestTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RequestTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("requestType")
  private RequestTypeEnum requestType = null;

  @SerializedName("requesterFeedback")
  private String requesterFeedback = null;

  @SerializedName("resourceRequestItemId")
  private Long resourceRequestItemId = null;

  @SerializedName("roleLevelApproval")
  private Boolean roleLevelApproval = null;

  @SerializedName("roleLevelApprovalCount")
  private Long roleLevelApprovalCount = null;

  @SerializedName("roleLevelApprovalItems")
  private List<RequestItem> roleLevelApprovalItems = null;

  @SerializedName("showDefaultResourceImage")
  private Boolean showDefaultResourceImage = null;

  @SerializedName("type")
  private String type = null;

  public RequestItem appDisplayName(String appDisplayName) {
    this.appDisplayName = appDisplayName;
    return this;
  }

   /**
   * Get appDisplayName
   * @return appDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getAppDisplayName() {
    return appDisplayName;
  }

  public void setAppDisplayName(String appDisplayName) {
    this.appDisplayName = appDisplayName;
  }

  public RequestItem approvalFormId(Long approvalFormId) {
    this.approvalFormId = approvalFormId;
    return this;
  }

   /**
   * Get approvalFormId
   * @return approvalFormId
  **/
  @ApiModelProperty(value = "")
  public Long getApprovalFormId() {
    return approvalFormId;
  }

  public void setApprovalFormId(Long approvalFormId) {
    this.approvalFormId = approvalFormId;
  }

  public RequestItem canRequestThisForOthers(Boolean canRequestThisForOthers) {
    this.canRequestThisForOthers = canRequestThisForOthers;
    return this;
  }

   /**
   * Get canRequestThisForOthers
   * @return canRequestThisForOthers
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanRequestThisForOthers() {
    return canRequestThisForOthers;
  }

  public void setCanRequestThisForOthers(Boolean canRequestThisForOthers) {
    this.canRequestThisForOthers = canRequestThisForOthers;
  }

  public RequestItem currentApprovalStep(Long currentApprovalStep) {
    this.currentApprovalStep = currentApprovalStep;
    return this;
  }

   /**
   * Get currentApprovalStep
   * @return currentApprovalStep
  **/
  @ApiModelProperty(value = "")
  public Long getCurrentApprovalStep() {
    return currentApprovalStep;
  }

  public void setCurrentApprovalStep(Long currentApprovalStep) {
    this.currentApprovalStep = currentApprovalStep;
  }

  public RequestItem decision(DecisionEnum decision) {
    this.decision = decision;
    return this;
  }

   /**
   * Get decision
   * @return decision
  **/
  @ApiModelProperty(value = "")
  public DecisionEnum getDecision() {
    return decision;
  }

  public void setDecision(DecisionEnum decision) {
    this.decision = decision;
  }

  public RequestItem deleted(Boolean deleted) {
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

  public RequestItem description(String description) {
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

  public RequestItem displayName(String displayName) {
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

  public RequestItem effectiveDate(OffsetDateTime effectiveDate) {
    this.effectiveDate = effectiveDate;
    return this;
  }

   /**
   * Get effectiveDate
   * @return effectiveDate
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(OffsetDateTime effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public RequestItem expirationDate(OffsetDateTime expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

   /**
   * Get expirationDate
   * @return expirationDate
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(OffsetDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public RequestItem flowdata(String flowdata) {
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

  public RequestItem fulfillmentInstructions(String fulfillmentInstructions) {
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

  public RequestItem hasRequiredField(Boolean hasRequiredField) {
    this.hasRequiredField = hasRequiredField;
    return this;
  }

   /**
   * Get hasRequiredField
   * @return hasRequiredField
  **/
  @ApiModelProperty(value = "")
  public Boolean isHasRequiredField() {
    return hasRequiredField;
  }

  public void setHasRequiredField(Boolean hasRequiredField) {
    this.hasRequiredField = hasRequiredField;
  }

  public RequestItem hideResourceImage(Boolean hideResourceImage) {
    this.hideResourceImage = hideResourceImage;
    return this;
  }

   /**
   * Get hideResourceImage
   * @return hideResourceImage
  **/
  @ApiModelProperty(value = "")
  public Boolean isHideResourceImage() {
    return hideResourceImage;
  }

  public void setHideResourceImage(Boolean hideResourceImage) {
    this.hideResourceImage = hideResourceImage;
  }

  public RequestItem imageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

   /**
   * Get imageUrl
   * @return imageUrl
  **/
  @ApiModelProperty(value = "")
  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public RequestItem instanceGuid(String instanceGuid) {
    this.instanceGuid = instanceGuid;
    return this;
  }

   /**
   * Get instanceGuid
   * @return instanceGuid
  **/
  @ApiModelProperty(value = "")
  public String getInstanceGuid() {
    return instanceGuid;
  }

  public void setInstanceGuid(String instanceGuid) {
    this.instanceGuid = instanceGuid;
  }

  public RequestItem matchesFilter(Boolean matchesFilter) {
    this.matchesFilter = matchesFilter;
    return this;
  }

   /**
   * Get matchesFilter
   * @return matchesFilter
  **/
  @ApiModelProperty(value = "")
  public Boolean isMatchesFilter() {
    return matchesFilter;
  }

  public void setMatchesFilter(Boolean matchesFilter) {
    this.matchesFilter = matchesFilter;
  }

  public RequestItem originalItemDisplayName(String originalItemDisplayName) {
    this.originalItemDisplayName = originalItemDisplayName;
    return this;
  }

   /**
   * Get originalItemDisplayName
   * @return originalItemDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getOriginalItemDisplayName() {
    return originalItemDisplayName;
  }

  public void setOriginalItemDisplayName(String originalItemDisplayName) {
    this.originalItemDisplayName = originalItemDisplayName;
  }

  public RequestItem originalRequestItem(String originalRequestItem) {
    this.originalRequestItem = originalRequestItem;
    return this;
  }

   /**
   * Get originalRequestItem
   * @return originalRequestItem
  **/
  @ApiModelProperty(value = "")
  public String getOriginalRequestItem() {
    return originalRequestItem;
  }

  public void setOriginalRequestItem(String originalRequestItem) {
    this.originalRequestItem = originalRequestItem;
  }

  public RequestItem params(List<Param> params) {
    this.params = params;
    return this;
  }

  public RequestItem addParamsItem(Param paramsItem) {
    if (this.params == null) {
      this.params = new ArrayList<Param>();
    }
    this.params.add(paramsItem);
    return this;
  }

   /**
   * Get params
   * @return params
  **/
  @ApiModelProperty(value = "")
  public List<Param> getParams() {
    return params;
  }

  public void setParams(List<Param> params) {
    this.params = params;
  }

  public RequestItem permType(String permType) {
    this.permType = permType;
    return this;
  }

   /**
   * Get permType
   * @return permType
  **/
  @ApiModelProperty(value = "")
  public String getPermType() {
    return permType;
  }

  public void setPermType(String permType) {
    this.permType = permType;
  }

  public RequestItem reason(String reason) {
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

  public RequestItem recipient(String recipient) {
    this.recipient = recipient;
    return this;
  }

   /**
   * Get recipient
   * @return recipient
  **/
  @ApiModelProperty(value = "")
  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public RequestItem recipientDeleted(Boolean recipientDeleted) {
    this.recipientDeleted = recipientDeleted;
    return this;
  }

   /**
   * Get recipientDeleted
   * @return recipientDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isRecipientDeleted() {
    return recipientDeleted;
  }

  public void setRecipientDeleted(Boolean recipientDeleted) {
    this.recipientDeleted = recipientDeleted;
  }

  public RequestItem recipientDisplayName(String recipientDisplayName) {
    this.recipientDisplayName = recipientDisplayName;
    return this;
  }

   /**
   * Get recipientDisplayName
   * @return recipientDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getRecipientDisplayName() {
    return recipientDisplayName;
  }

  public void setRecipientDisplayName(String recipientDisplayName) {
    this.recipientDisplayName = recipientDisplayName;
  }

  public RequestItem requestFormId(Long requestFormId) {
    this.requestFormId = requestFormId;
    return this;
  }

   /**
   * Get requestFormId
   * @return requestFormId
  **/
  @ApiModelProperty(value = "")
  public Long getRequestFormId() {
    return requestFormId;
  }

  public void setRequestFormId(Long requestFormId) {
    this.requestFormId = requestFormId;
  }

  public RequestItem requestItem(String requestItem) {
    this.requestItem = requestItem;
    return this;
  }

   /**
   * Get requestItem
   * @return requestItem
  **/
  @ApiModelProperty(value = "")
  public String getRequestItem() {
    return requestItem;
  }

  public void setRequestItem(String requestItem) {
    this.requestItem = requestItem;
  }

  public RequestItem requestType(RequestTypeEnum requestType) {
    this.requestType = requestType;
    return this;
  }

   /**
   * Get requestType
   * @return requestType
  **/
  @ApiModelProperty(value = "")
  public RequestTypeEnum getRequestType() {
    return requestType;
  }

  public void setRequestType(RequestTypeEnum requestType) {
    this.requestType = requestType;
  }

  public RequestItem requesterFeedback(String requesterFeedback) {
    this.requesterFeedback = requesterFeedback;
    return this;
  }

   /**
   * Get requesterFeedback
   * @return requesterFeedback
  **/
  @ApiModelProperty(value = "")
  public String getRequesterFeedback() {
    return requesterFeedback;
  }

  public void setRequesterFeedback(String requesterFeedback) {
    this.requesterFeedback = requesterFeedback;
  }

  public RequestItem resourceRequestItemId(Long resourceRequestItemId) {
    this.resourceRequestItemId = resourceRequestItemId;
    return this;
  }

   /**
   * Get resourceRequestItemId
   * @return resourceRequestItemId
  **/
  @ApiModelProperty(value = "")
  public Long getResourceRequestItemId() {
    return resourceRequestItemId;
  }

  public void setResourceRequestItemId(Long resourceRequestItemId) {
    this.resourceRequestItemId = resourceRequestItemId;
  }

  public RequestItem roleLevelApproval(Boolean roleLevelApproval) {
    this.roleLevelApproval = roleLevelApproval;
    return this;
  }

   /**
   * Get roleLevelApproval
   * @return roleLevelApproval
  **/
  @ApiModelProperty(value = "")
  public Boolean isRoleLevelApproval() {
    return roleLevelApproval;
  }

  public void setRoleLevelApproval(Boolean roleLevelApproval) {
    this.roleLevelApproval = roleLevelApproval;
  }

  public RequestItem roleLevelApprovalCount(Long roleLevelApprovalCount) {
    this.roleLevelApprovalCount = roleLevelApprovalCount;
    return this;
  }

   /**
   * Get roleLevelApprovalCount
   * @return roleLevelApprovalCount
  **/
  @ApiModelProperty(value = "")
  public Long getRoleLevelApprovalCount() {
    return roleLevelApprovalCount;
  }

  public void setRoleLevelApprovalCount(Long roleLevelApprovalCount) {
    this.roleLevelApprovalCount = roleLevelApprovalCount;
  }

  public RequestItem roleLevelApprovalItems(List<RequestItem> roleLevelApprovalItems) {
    this.roleLevelApprovalItems = roleLevelApprovalItems;
    return this;
  }

  public RequestItem addRoleLevelApprovalItemsItem(RequestItem roleLevelApprovalItemsItem) {
    if (this.roleLevelApprovalItems == null) {
      this.roleLevelApprovalItems = new ArrayList<RequestItem>();
    }
    this.roleLevelApprovalItems.add(roleLevelApprovalItemsItem);
    return this;
  }

   /**
   * Get roleLevelApprovalItems
   * @return roleLevelApprovalItems
  **/
  @ApiModelProperty(value = "")
  public List<RequestItem> getRoleLevelApprovalItems() {
    return roleLevelApprovalItems;
  }

  public void setRoleLevelApprovalItems(List<RequestItem> roleLevelApprovalItems) {
    this.roleLevelApprovalItems = roleLevelApprovalItems;
  }

  public RequestItem showDefaultResourceImage(Boolean showDefaultResourceImage) {
    this.showDefaultResourceImage = showDefaultResourceImage;
    return this;
  }

   /**
   * Get showDefaultResourceImage
   * @return showDefaultResourceImage
  **/
  @ApiModelProperty(value = "")
  public Boolean isShowDefaultResourceImage() {
    return showDefaultResourceImage;
  }

  public void setShowDefaultResourceImage(Boolean showDefaultResourceImage) {
    this.showDefaultResourceImage = showDefaultResourceImage;
  }

  public RequestItem type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestItem requestItem = (RequestItem) o;
    return Objects.equals(this.appDisplayName, requestItem.appDisplayName) &&
        Objects.equals(this.approvalFormId, requestItem.approvalFormId) &&
        Objects.equals(this.canRequestThisForOthers, requestItem.canRequestThisForOthers) &&
        Objects.equals(this.currentApprovalStep, requestItem.currentApprovalStep) &&
        Objects.equals(this.decision, requestItem.decision) &&
        Objects.equals(this.deleted, requestItem.deleted) &&
        Objects.equals(this.description, requestItem.description) &&
        Objects.equals(this.displayName, requestItem.displayName) &&
        Objects.equals(this.effectiveDate, requestItem.effectiveDate) &&
        Objects.equals(this.expirationDate, requestItem.expirationDate) &&
        Objects.equals(this.flowdata, requestItem.flowdata) &&
        Objects.equals(this.fulfillmentInstructions, requestItem.fulfillmentInstructions) &&
        Objects.equals(this.hasRequiredField, requestItem.hasRequiredField) &&
        Objects.equals(this.hideResourceImage, requestItem.hideResourceImage) &&
        Objects.equals(this.imageUrl, requestItem.imageUrl) &&
        Objects.equals(this.instanceGuid, requestItem.instanceGuid) &&
        Objects.equals(this.matchesFilter, requestItem.matchesFilter) &&
        Objects.equals(this.originalItemDisplayName, requestItem.originalItemDisplayName) &&
        Objects.equals(this.originalRequestItem, requestItem.originalRequestItem) &&
        Objects.equals(this.params, requestItem.params) &&
        Objects.equals(this.permType, requestItem.permType) &&
        Objects.equals(this.reason, requestItem.reason) &&
        Objects.equals(this.recipient, requestItem.recipient) &&
        Objects.equals(this.recipientDeleted, requestItem.recipientDeleted) &&
        Objects.equals(this.recipientDisplayName, requestItem.recipientDisplayName) &&
        Objects.equals(this.requestFormId, requestItem.requestFormId) &&
        Objects.equals(this.requestItem, requestItem.requestItem) &&
        Objects.equals(this.requestType, requestItem.requestType) &&
        Objects.equals(this.requesterFeedback, requestItem.requesterFeedback) &&
        Objects.equals(this.resourceRequestItemId, requestItem.resourceRequestItemId) &&
        Objects.equals(this.roleLevelApproval, requestItem.roleLevelApproval) &&
        Objects.equals(this.roleLevelApprovalCount, requestItem.roleLevelApprovalCount) &&
        Objects.equals(this.roleLevelApprovalItems, requestItem.roleLevelApprovalItems) &&
        Objects.equals(this.showDefaultResourceImage, requestItem.showDefaultResourceImage) &&
        Objects.equals(this.type, requestItem.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appDisplayName, approvalFormId, canRequestThisForOthers, currentApprovalStep, decision, deleted, description, displayName, effectiveDate, expirationDate, flowdata, fulfillmentInstructions, hasRequiredField, hideResourceImage, imageUrl, instanceGuid, matchesFilter, originalItemDisplayName, originalRequestItem, params, permType, reason, recipient, recipientDeleted, recipientDisplayName, requestFormId, requestItem, requestType, requesterFeedback, resourceRequestItemId, roleLevelApproval, roleLevelApprovalCount, roleLevelApprovalItems, showDefaultResourceImage, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestItem {\n");
    
    sb.append("    appDisplayName: ").append(toIndentedString(appDisplayName)).append("\n");
    sb.append("    approvalFormId: ").append(toIndentedString(approvalFormId)).append("\n");
    sb.append("    canRequestThisForOthers: ").append(toIndentedString(canRequestThisForOthers)).append("\n");
    sb.append("    currentApprovalStep: ").append(toIndentedString(currentApprovalStep)).append("\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    effectiveDate: ").append(toIndentedString(effectiveDate)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    flowdata: ").append(toIndentedString(flowdata)).append("\n");
    sb.append("    fulfillmentInstructions: ").append(toIndentedString(fulfillmentInstructions)).append("\n");
    sb.append("    hasRequiredField: ").append(toIndentedString(hasRequiredField)).append("\n");
    sb.append("    hideResourceImage: ").append(toIndentedString(hideResourceImage)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("    instanceGuid: ").append(toIndentedString(instanceGuid)).append("\n");
    sb.append("    matchesFilter: ").append(toIndentedString(matchesFilter)).append("\n");
    sb.append("    originalItemDisplayName: ").append(toIndentedString(originalItemDisplayName)).append("\n");
    sb.append("    originalRequestItem: ").append(toIndentedString(originalRequestItem)).append("\n");
    sb.append("    params: ").append(toIndentedString(params)).append("\n");
    sb.append("    permType: ").append(toIndentedString(permType)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("    recipientDeleted: ").append(toIndentedString(recipientDeleted)).append("\n");
    sb.append("    recipientDisplayName: ").append(toIndentedString(recipientDisplayName)).append("\n");
    sb.append("    requestFormId: ").append(toIndentedString(requestFormId)).append("\n");
    sb.append("    requestItem: ").append(toIndentedString(requestItem)).append("\n");
    sb.append("    requestType: ").append(toIndentedString(requestType)).append("\n");
    sb.append("    requesterFeedback: ").append(toIndentedString(requesterFeedback)).append("\n");
    sb.append("    resourceRequestItemId: ").append(toIndentedString(resourceRequestItemId)).append("\n");
    sb.append("    roleLevelApproval: ").append(toIndentedString(roleLevelApproval)).append("\n");
    sb.append("    roleLevelApprovalCount: ").append(toIndentedString(roleLevelApprovalCount)).append("\n");
    sb.append("    roleLevelApprovalItems: ").append(toIndentedString(roleLevelApprovalItems)).append("\n");
    sb.append("    showDefaultResourceImage: ").append(toIndentedString(showDefaultResourceImage)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
