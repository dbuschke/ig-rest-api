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
import io.swagger.client.model.ApprovalStep;
import io.swagger.client.model.ChangeItemAction;
import io.swagger.client.model.PsodvItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ReqItemInfo
 */



public class ReqItemInfo {
  @SerializedName("approvalStartTime")
  private Long approvalStartTime = null;

  @SerializedName("approvalSteps")
  private List<ApprovalStep> approvalSteps = null;

  @SerializedName("autoApprovedByBr")
  private Boolean autoApprovedByBr = null;

  @SerializedName("autoApprovedByCond")
  private Boolean autoApprovedByCond = null;

  @SerializedName("autoApprovedByValue")
  private String autoApprovedByValue = null;

  @SerializedName("changeItemActions")
  private List<ChangeItemAction> changeItemActions = null;

  @SerializedName("changeRequestTime")
  private Long changeRequestTime = null;

  @SerializedName("itemApprovalStatus")
  private String itemApprovalStatus = null;

  @SerializedName("lastStatusUpdateTime")
  private Long lastStatusUpdateTime = null;

  @SerializedName("lastSubmit")
  private Boolean lastSubmit = null;

  @SerializedName("lastUpdateTime")
  private Long lastUpdateTime = null;

  @SerializedName("originalApproverUserDisplayName")
  private String originalApproverUserDisplayName = null;

  @SerializedName("originalApproverUserId")
  private String originalApproverUserId = null;

  @SerializedName("psodvApprovalStartTime")
  private Long psodvApprovalStartTime = null;

  @SerializedName("psodvItems")
  private List<PsodvItem> psodvItems = null;

  @SerializedName("requestItemReason")
  private String requestItemReason = null;

  @SerializedName("requestReason")
  private String requestReason = null;

  @SerializedName("requestStatus")
  private String requestStatus = null;

  @SerializedName("requestTime")
  private Long requestTime = null;

  @SerializedName("requestUserDisplayName")
  private String requestUserDisplayName = null;

  @SerializedName("requestUserId")
  private String requestUserId = null;

  @SerializedName("requesterFeedback")
  private String requesterFeedback = null;

  @SerializedName("statusChangedByUserDisplayName")
  private String statusChangedByUserDisplayName = null;

  @SerializedName("statusChangedByUserId")
  private String statusChangedByUserId = null;

  @SerializedName("submitStatus")
  private String submitStatus = null;

  @SerializedName("submittingToWf")
  private Boolean submittingToWf = null;

  public ReqItemInfo approvalStartTime(Long approvalStartTime) {
    this.approvalStartTime = approvalStartTime;
    return this;
  }

   /**
   * Get approvalStartTime
   * @return approvalStartTime
  **/
  @ApiModelProperty(value = "")
  public Long getApprovalStartTime() {
    return approvalStartTime;
  }

  public void setApprovalStartTime(Long approvalStartTime) {
    this.approvalStartTime = approvalStartTime;
  }

  public ReqItemInfo approvalSteps(List<ApprovalStep> approvalSteps) {
    this.approvalSteps = approvalSteps;
    return this;
  }

  public ReqItemInfo addApprovalStepsItem(ApprovalStep approvalStepsItem) {
    if (this.approvalSteps == null) {
      this.approvalSteps = new ArrayList<ApprovalStep>();
    }
    this.approvalSteps.add(approvalStepsItem);
    return this;
  }

   /**
   * Get approvalSteps
   * @return approvalSteps
  **/
  @ApiModelProperty(value = "")
  public List<ApprovalStep> getApprovalSteps() {
    return approvalSteps;
  }

  public void setApprovalSteps(List<ApprovalStep> approvalSteps) {
    this.approvalSteps = approvalSteps;
  }

  public ReqItemInfo autoApprovedByBr(Boolean autoApprovedByBr) {
    this.autoApprovedByBr = autoApprovedByBr;
    return this;
  }

   /**
   * Get autoApprovedByBr
   * @return autoApprovedByBr
  **/
  @ApiModelProperty(value = "")
  public Boolean isAutoApprovedByBr() {
    return autoApprovedByBr;
  }

  public void setAutoApprovedByBr(Boolean autoApprovedByBr) {
    this.autoApprovedByBr = autoApprovedByBr;
  }

  public ReqItemInfo autoApprovedByCond(Boolean autoApprovedByCond) {
    this.autoApprovedByCond = autoApprovedByCond;
    return this;
  }

   /**
   * Get autoApprovedByCond
   * @return autoApprovedByCond
  **/
  @ApiModelProperty(value = "")
  public Boolean isAutoApprovedByCond() {
    return autoApprovedByCond;
  }

  public void setAutoApprovedByCond(Boolean autoApprovedByCond) {
    this.autoApprovedByCond = autoApprovedByCond;
  }

  public ReqItemInfo autoApprovedByValue(String autoApprovedByValue) {
    this.autoApprovedByValue = autoApprovedByValue;
    return this;
  }

   /**
   * Get autoApprovedByValue
   * @return autoApprovedByValue
  **/
  @ApiModelProperty(value = "")
  public String getAutoApprovedByValue() {
    return autoApprovedByValue;
  }

  public void setAutoApprovedByValue(String autoApprovedByValue) {
    this.autoApprovedByValue = autoApprovedByValue;
  }

  public ReqItemInfo changeItemActions(List<ChangeItemAction> changeItemActions) {
    this.changeItemActions = changeItemActions;
    return this;
  }

  public ReqItemInfo addChangeItemActionsItem(ChangeItemAction changeItemActionsItem) {
    if (this.changeItemActions == null) {
      this.changeItemActions = new ArrayList<ChangeItemAction>();
    }
    this.changeItemActions.add(changeItemActionsItem);
    return this;
  }

   /**
   * Get changeItemActions
   * @return changeItemActions
  **/
  @ApiModelProperty(value = "")
  public List<ChangeItemAction> getChangeItemActions() {
    return changeItemActions;
  }

  public void setChangeItemActions(List<ChangeItemAction> changeItemActions) {
    this.changeItemActions = changeItemActions;
  }

  public ReqItemInfo changeRequestTime(Long changeRequestTime) {
    this.changeRequestTime = changeRequestTime;
    return this;
  }

   /**
   * Get changeRequestTime
   * @return changeRequestTime
  **/
  @ApiModelProperty(value = "")
  public Long getChangeRequestTime() {
    return changeRequestTime;
  }

  public void setChangeRequestTime(Long changeRequestTime) {
    this.changeRequestTime = changeRequestTime;
  }

  public ReqItemInfo itemApprovalStatus(String itemApprovalStatus) {
    this.itemApprovalStatus = itemApprovalStatus;
    return this;
  }

   /**
   * Get itemApprovalStatus
   * @return itemApprovalStatus
  **/
  @ApiModelProperty(value = "")
  public String getItemApprovalStatus() {
    return itemApprovalStatus;
  }

  public void setItemApprovalStatus(String itemApprovalStatus) {
    this.itemApprovalStatus = itemApprovalStatus;
  }

  public ReqItemInfo lastStatusUpdateTime(Long lastStatusUpdateTime) {
    this.lastStatusUpdateTime = lastStatusUpdateTime;
    return this;
  }

   /**
   * Get lastStatusUpdateTime
   * @return lastStatusUpdateTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastStatusUpdateTime() {
    return lastStatusUpdateTime;
  }

  public void setLastStatusUpdateTime(Long lastStatusUpdateTime) {
    this.lastStatusUpdateTime = lastStatusUpdateTime;
  }

  public ReqItemInfo lastSubmit(Boolean lastSubmit) {
    this.lastSubmit = lastSubmit;
    return this;
  }

   /**
   * Get lastSubmit
   * @return lastSubmit
  **/
  @ApiModelProperty(value = "")
  public Boolean isLastSubmit() {
    return lastSubmit;
  }

  public void setLastSubmit(Boolean lastSubmit) {
    this.lastSubmit = lastSubmit;
  }

  public ReqItemInfo lastUpdateTime(Long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
    return this;
  }

   /**
   * Get lastUpdateTime
   * @return lastUpdateTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public ReqItemInfo originalApproverUserDisplayName(String originalApproverUserDisplayName) {
    this.originalApproverUserDisplayName = originalApproverUserDisplayName;
    return this;
  }

   /**
   * Get originalApproverUserDisplayName
   * @return originalApproverUserDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getOriginalApproverUserDisplayName() {
    return originalApproverUserDisplayName;
  }

  public void setOriginalApproverUserDisplayName(String originalApproverUserDisplayName) {
    this.originalApproverUserDisplayName = originalApproverUserDisplayName;
  }

  public ReqItemInfo originalApproverUserId(String originalApproverUserId) {
    this.originalApproverUserId = originalApproverUserId;
    return this;
  }

   /**
   * Get originalApproverUserId
   * @return originalApproverUserId
  **/
  @ApiModelProperty(value = "")
  public String getOriginalApproverUserId() {
    return originalApproverUserId;
  }

  public void setOriginalApproverUserId(String originalApproverUserId) {
    this.originalApproverUserId = originalApproverUserId;
  }

  public ReqItemInfo psodvApprovalStartTime(Long psodvApprovalStartTime) {
    this.psodvApprovalStartTime = psodvApprovalStartTime;
    return this;
  }

   /**
   * Get psodvApprovalStartTime
   * @return psodvApprovalStartTime
  **/
  @ApiModelProperty(value = "")
  public Long getPsodvApprovalStartTime() {
    return psodvApprovalStartTime;
  }

  public void setPsodvApprovalStartTime(Long psodvApprovalStartTime) {
    this.psodvApprovalStartTime = psodvApprovalStartTime;
  }

  public ReqItemInfo psodvItems(List<PsodvItem> psodvItems) {
    this.psodvItems = psodvItems;
    return this;
  }

  public ReqItemInfo addPsodvItemsItem(PsodvItem psodvItemsItem) {
    if (this.psodvItems == null) {
      this.psodvItems = new ArrayList<PsodvItem>();
    }
    this.psodvItems.add(psodvItemsItem);
    return this;
  }

   /**
   * Get psodvItems
   * @return psodvItems
  **/
  @ApiModelProperty(value = "")
  public List<PsodvItem> getPsodvItems() {
    return psodvItems;
  }

  public void setPsodvItems(List<PsodvItem> psodvItems) {
    this.psodvItems = psodvItems;
  }

  public ReqItemInfo requestItemReason(String requestItemReason) {
    this.requestItemReason = requestItemReason;
    return this;
  }

   /**
   * Get requestItemReason
   * @return requestItemReason
  **/
  @ApiModelProperty(value = "")
  public String getRequestItemReason() {
    return requestItemReason;
  }

  public void setRequestItemReason(String requestItemReason) {
    this.requestItemReason = requestItemReason;
  }

  public ReqItemInfo requestReason(String requestReason) {
    this.requestReason = requestReason;
    return this;
  }

   /**
   * Get requestReason
   * @return requestReason
  **/
  @ApiModelProperty(value = "")
  public String getRequestReason() {
    return requestReason;
  }

  public void setRequestReason(String requestReason) {
    this.requestReason = requestReason;
  }

  public ReqItemInfo requestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
    return this;
  }

   /**
   * Get requestStatus
   * @return requestStatus
  **/
  @ApiModelProperty(value = "")
  public String getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
  }

  public ReqItemInfo requestTime(Long requestTime) {
    this.requestTime = requestTime;
    return this;
  }

   /**
   * Get requestTime
   * @return requestTime
  **/
  @ApiModelProperty(value = "")
  public Long getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(Long requestTime) {
    this.requestTime = requestTime;
  }

  public ReqItemInfo requestUserDisplayName(String requestUserDisplayName) {
    this.requestUserDisplayName = requestUserDisplayName;
    return this;
  }

   /**
   * Get requestUserDisplayName
   * @return requestUserDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getRequestUserDisplayName() {
    return requestUserDisplayName;
  }

  public void setRequestUserDisplayName(String requestUserDisplayName) {
    this.requestUserDisplayName = requestUserDisplayName;
  }

  public ReqItemInfo requestUserId(String requestUserId) {
    this.requestUserId = requestUserId;
    return this;
  }

   /**
   * Get requestUserId
   * @return requestUserId
  **/
  @ApiModelProperty(value = "")
  public String getRequestUserId() {
    return requestUserId;
  }

  public void setRequestUserId(String requestUserId) {
    this.requestUserId = requestUserId;
  }

  public ReqItemInfo requesterFeedback(String requesterFeedback) {
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

  public ReqItemInfo statusChangedByUserDisplayName(String statusChangedByUserDisplayName) {
    this.statusChangedByUserDisplayName = statusChangedByUserDisplayName;
    return this;
  }

   /**
   * Get statusChangedByUserDisplayName
   * @return statusChangedByUserDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getStatusChangedByUserDisplayName() {
    return statusChangedByUserDisplayName;
  }

  public void setStatusChangedByUserDisplayName(String statusChangedByUserDisplayName) {
    this.statusChangedByUserDisplayName = statusChangedByUserDisplayName;
  }

  public ReqItemInfo statusChangedByUserId(String statusChangedByUserId) {
    this.statusChangedByUserId = statusChangedByUserId;
    return this;
  }

   /**
   * Get statusChangedByUserId
   * @return statusChangedByUserId
  **/
  @ApiModelProperty(value = "")
  public String getStatusChangedByUserId() {
    return statusChangedByUserId;
  }

  public void setStatusChangedByUserId(String statusChangedByUserId) {
    this.statusChangedByUserId = statusChangedByUserId;
  }

  public ReqItemInfo submitStatus(String submitStatus) {
    this.submitStatus = submitStatus;
    return this;
  }

   /**
   * Get submitStatus
   * @return submitStatus
  **/
  @ApiModelProperty(value = "")
  public String getSubmitStatus() {
    return submitStatus;
  }

  public void setSubmitStatus(String submitStatus) {
    this.submitStatus = submitStatus;
  }

  public ReqItemInfo submittingToWf(Boolean submittingToWf) {
    this.submittingToWf = submittingToWf;
    return this;
  }

   /**
   * Get submittingToWf
   * @return submittingToWf
  **/
  @ApiModelProperty(value = "")
  public Boolean isSubmittingToWf() {
    return submittingToWf;
  }

  public void setSubmittingToWf(Boolean submittingToWf) {
    this.submittingToWf = submittingToWf;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReqItemInfo reqItemInfo = (ReqItemInfo) o;
    return Objects.equals(this.approvalStartTime, reqItemInfo.approvalStartTime) &&
        Objects.equals(this.approvalSteps, reqItemInfo.approvalSteps) &&
        Objects.equals(this.autoApprovedByBr, reqItemInfo.autoApprovedByBr) &&
        Objects.equals(this.autoApprovedByCond, reqItemInfo.autoApprovedByCond) &&
        Objects.equals(this.autoApprovedByValue, reqItemInfo.autoApprovedByValue) &&
        Objects.equals(this.changeItemActions, reqItemInfo.changeItemActions) &&
        Objects.equals(this.changeRequestTime, reqItemInfo.changeRequestTime) &&
        Objects.equals(this.itemApprovalStatus, reqItemInfo.itemApprovalStatus) &&
        Objects.equals(this.lastStatusUpdateTime, reqItemInfo.lastStatusUpdateTime) &&
        Objects.equals(this.lastSubmit, reqItemInfo.lastSubmit) &&
        Objects.equals(this.lastUpdateTime, reqItemInfo.lastUpdateTime) &&
        Objects.equals(this.originalApproverUserDisplayName, reqItemInfo.originalApproverUserDisplayName) &&
        Objects.equals(this.originalApproverUserId, reqItemInfo.originalApproverUserId) &&
        Objects.equals(this.psodvApprovalStartTime, reqItemInfo.psodvApprovalStartTime) &&
        Objects.equals(this.psodvItems, reqItemInfo.psodvItems) &&
        Objects.equals(this.requestItemReason, reqItemInfo.requestItemReason) &&
        Objects.equals(this.requestReason, reqItemInfo.requestReason) &&
        Objects.equals(this.requestStatus, reqItemInfo.requestStatus) &&
        Objects.equals(this.requestTime, reqItemInfo.requestTime) &&
        Objects.equals(this.requestUserDisplayName, reqItemInfo.requestUserDisplayName) &&
        Objects.equals(this.requestUserId, reqItemInfo.requestUserId) &&
        Objects.equals(this.requesterFeedback, reqItemInfo.requesterFeedback) &&
        Objects.equals(this.statusChangedByUserDisplayName, reqItemInfo.statusChangedByUserDisplayName) &&
        Objects.equals(this.statusChangedByUserId, reqItemInfo.statusChangedByUserId) &&
        Objects.equals(this.submitStatus, reqItemInfo.submitStatus) &&
        Objects.equals(this.submittingToWf, reqItemInfo.submittingToWf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvalStartTime, approvalSteps, autoApprovedByBr, autoApprovedByCond, autoApprovedByValue, changeItemActions, changeRequestTime, itemApprovalStatus, lastStatusUpdateTime, lastSubmit, lastUpdateTime, originalApproverUserDisplayName, originalApproverUserId, psodvApprovalStartTime, psodvItems, requestItemReason, requestReason, requestStatus, requestTime, requestUserDisplayName, requestUserId, requesterFeedback, statusChangedByUserDisplayName, statusChangedByUserId, submitStatus, submittingToWf);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReqItemInfo {\n");
    
    sb.append("    approvalStartTime: ").append(toIndentedString(approvalStartTime)).append("\n");
    sb.append("    approvalSteps: ").append(toIndentedString(approvalSteps)).append("\n");
    sb.append("    autoApprovedByBr: ").append(toIndentedString(autoApprovedByBr)).append("\n");
    sb.append("    autoApprovedByCond: ").append(toIndentedString(autoApprovedByCond)).append("\n");
    sb.append("    autoApprovedByValue: ").append(toIndentedString(autoApprovedByValue)).append("\n");
    sb.append("    changeItemActions: ").append(toIndentedString(changeItemActions)).append("\n");
    sb.append("    changeRequestTime: ").append(toIndentedString(changeRequestTime)).append("\n");
    sb.append("    itemApprovalStatus: ").append(toIndentedString(itemApprovalStatus)).append("\n");
    sb.append("    lastStatusUpdateTime: ").append(toIndentedString(lastStatusUpdateTime)).append("\n");
    sb.append("    lastSubmit: ").append(toIndentedString(lastSubmit)).append("\n");
    sb.append("    lastUpdateTime: ").append(toIndentedString(lastUpdateTime)).append("\n");
    sb.append("    originalApproverUserDisplayName: ").append(toIndentedString(originalApproverUserDisplayName)).append("\n");
    sb.append("    originalApproverUserId: ").append(toIndentedString(originalApproverUserId)).append("\n");
    sb.append("    psodvApprovalStartTime: ").append(toIndentedString(psodvApprovalStartTime)).append("\n");
    sb.append("    psodvItems: ").append(toIndentedString(psodvItems)).append("\n");
    sb.append("    requestItemReason: ").append(toIndentedString(requestItemReason)).append("\n");
    sb.append("    requestReason: ").append(toIndentedString(requestReason)).append("\n");
    sb.append("    requestStatus: ").append(toIndentedString(requestStatus)).append("\n");
    sb.append("    requestTime: ").append(toIndentedString(requestTime)).append("\n");
    sb.append("    requestUserDisplayName: ").append(toIndentedString(requestUserDisplayName)).append("\n");
    sb.append("    requestUserId: ").append(toIndentedString(requestUserId)).append("\n");
    sb.append("    requesterFeedback: ").append(toIndentedString(requesterFeedback)).append("\n");
    sb.append("    statusChangedByUserDisplayName: ").append(toIndentedString(statusChangedByUserDisplayName)).append("\n");
    sb.append("    statusChangedByUserId: ").append(toIndentedString(statusChangedByUserId)).append("\n");
    sb.append("    submitStatus: ").append(toIndentedString(submitStatus)).append("\n");
    sb.append("    submittingToWf: ").append(toIndentedString(submittingToWf)).append("\n");
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
