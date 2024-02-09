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
import io.swagger.client.model.AccessApprovalStepApprover;
import io.swagger.client.model.Reassignment;
import io.swagger.client.model.StandaloneWorkflowApprovals;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ApprovalStep
 */



public class ApprovalStep {
  @SerializedName("approverType")
  private String approverType = null;

  @SerializedName("approvers")
  private List<AccessApprovalStepApprover> approvers = null;

  @SerializedName("autoApprovedByBr")
  private Boolean autoApprovedByBr = null;

  @SerializedName("autoApprovedByCond")
  private Boolean autoApprovedByCond = null;

  @SerializedName("autoApprovedByValue")
  private String autoApprovedByValue = null;

  @SerializedName("delayed")
  private Boolean delayed = null;

  @SerializedName("delayedToTime")
  private Long delayedToTime = null;

  @SerializedName("originalApproverUserDisplayName")
  private String originalApproverUserDisplayName = null;

  @SerializedName("originalApproverUserId")
  private String originalApproverUserId = null;

  @SerializedName("reassignments")
  private List<Reassignment> reassignments = null;

  @SerializedName("standaloneWorkflowApprovals")
  private List<StandaloneWorkflowApprovals> standaloneWorkflowApprovals = null;

  @SerializedName("statusSetTime")
  private Long statusSetTime = null;

  @SerializedName("statusSetterDisplayName")
  private String statusSetterDisplayName = null;

  @SerializedName("statusSetterUserId")
  private String statusSetterUserId = null;

  @SerializedName("stepApproveOrDenyStatus")
  private String stepApproveOrDenyStatus = null;

  @SerializedName("stepCompleterDisplayName")
  private String stepCompleterDisplayName = null;

  @SerializedName("stepCompleterId")
  private String stepCompleterId = null;

  @SerializedName("stepCompletionComment")
  private String stepCompletionComment = null;

  @SerializedName("stepCompletionTime")
  private Long stepCompletionTime = null;

  @SerializedName("stepNumber")
  private Long stepNumber = null;

  @SerializedName("stepType")
  private String stepType = null;

  public ApprovalStep approverType(String approverType) {
    this.approverType = approverType;
    return this;
  }

   /**
   * Get approverType
   * @return approverType
  **/
  @ApiModelProperty(value = "")
  public String getApproverType() {
    return approverType;
  }

  public void setApproverType(String approverType) {
    this.approverType = approverType;
  }

  public ApprovalStep approvers(List<AccessApprovalStepApprover> approvers) {
    this.approvers = approvers;
    return this;
  }

  public ApprovalStep addApproversItem(AccessApprovalStepApprover approversItem) {
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

  public ApprovalStep autoApprovedByBr(Boolean autoApprovedByBr) {
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

  public ApprovalStep autoApprovedByCond(Boolean autoApprovedByCond) {
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

  public ApprovalStep autoApprovedByValue(String autoApprovedByValue) {
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

  public ApprovalStep delayed(Boolean delayed) {
    this.delayed = delayed;
    return this;
  }

   /**
   * Get delayed
   * @return delayed
  **/
  @ApiModelProperty(value = "")
  public Boolean isDelayed() {
    return delayed;
  }

  public void setDelayed(Boolean delayed) {
    this.delayed = delayed;
  }

  public ApprovalStep delayedToTime(Long delayedToTime) {
    this.delayedToTime = delayedToTime;
    return this;
  }

   /**
   * Get delayedToTime
   * @return delayedToTime
  **/
  @ApiModelProperty(value = "")
  public Long getDelayedToTime() {
    return delayedToTime;
  }

  public void setDelayedToTime(Long delayedToTime) {
    this.delayedToTime = delayedToTime;
  }

  public ApprovalStep originalApproverUserDisplayName(String originalApproverUserDisplayName) {
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

  public ApprovalStep originalApproverUserId(String originalApproverUserId) {
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

  public ApprovalStep reassignments(List<Reassignment> reassignments) {
    this.reassignments = reassignments;
    return this;
  }

  public ApprovalStep addReassignmentsItem(Reassignment reassignmentsItem) {
    if (this.reassignments == null) {
      this.reassignments = new ArrayList<Reassignment>();
    }
    this.reassignments.add(reassignmentsItem);
    return this;
  }

   /**
   * Get reassignments
   * @return reassignments
  **/
  @ApiModelProperty(value = "")
  public List<Reassignment> getReassignments() {
    return reassignments;
  }

  public void setReassignments(List<Reassignment> reassignments) {
    this.reassignments = reassignments;
  }

  public ApprovalStep standaloneWorkflowApprovals(List<StandaloneWorkflowApprovals> standaloneWorkflowApprovals) {
    this.standaloneWorkflowApprovals = standaloneWorkflowApprovals;
    return this;
  }

  public ApprovalStep addStandaloneWorkflowApprovalsItem(StandaloneWorkflowApprovals standaloneWorkflowApprovalsItem) {
    if (this.standaloneWorkflowApprovals == null) {
      this.standaloneWorkflowApprovals = new ArrayList<StandaloneWorkflowApprovals>();
    }
    this.standaloneWorkflowApprovals.add(standaloneWorkflowApprovalsItem);
    return this;
  }

   /**
   * Get standaloneWorkflowApprovals
   * @return standaloneWorkflowApprovals
  **/
  @ApiModelProperty(value = "")
  public List<StandaloneWorkflowApprovals> getStandaloneWorkflowApprovals() {
    return standaloneWorkflowApprovals;
  }

  public void setStandaloneWorkflowApprovals(List<StandaloneWorkflowApprovals> standaloneWorkflowApprovals) {
    this.standaloneWorkflowApprovals = standaloneWorkflowApprovals;
  }

  public ApprovalStep statusSetTime(Long statusSetTime) {
    this.statusSetTime = statusSetTime;
    return this;
  }

   /**
   * Get statusSetTime
   * @return statusSetTime
  **/
  @ApiModelProperty(value = "")
  public Long getStatusSetTime() {
    return statusSetTime;
  }

  public void setStatusSetTime(Long statusSetTime) {
    this.statusSetTime = statusSetTime;
  }

  public ApprovalStep statusSetterDisplayName(String statusSetterDisplayName) {
    this.statusSetterDisplayName = statusSetterDisplayName;
    return this;
  }

   /**
   * Get statusSetterDisplayName
   * @return statusSetterDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getStatusSetterDisplayName() {
    return statusSetterDisplayName;
  }

  public void setStatusSetterDisplayName(String statusSetterDisplayName) {
    this.statusSetterDisplayName = statusSetterDisplayName;
  }

  public ApprovalStep statusSetterUserId(String statusSetterUserId) {
    this.statusSetterUserId = statusSetterUserId;
    return this;
  }

   /**
   * Get statusSetterUserId
   * @return statusSetterUserId
  **/
  @ApiModelProperty(value = "")
  public String getStatusSetterUserId() {
    return statusSetterUserId;
  }

  public void setStatusSetterUserId(String statusSetterUserId) {
    this.statusSetterUserId = statusSetterUserId;
  }

  public ApprovalStep stepApproveOrDenyStatus(String stepApproveOrDenyStatus) {
    this.stepApproveOrDenyStatus = stepApproveOrDenyStatus;
    return this;
  }

   /**
   * Get stepApproveOrDenyStatus
   * @return stepApproveOrDenyStatus
  **/
  @ApiModelProperty(value = "")
  public String getStepApproveOrDenyStatus() {
    return stepApproveOrDenyStatus;
  }

  public void setStepApproveOrDenyStatus(String stepApproveOrDenyStatus) {
    this.stepApproveOrDenyStatus = stepApproveOrDenyStatus;
  }

  public ApprovalStep stepCompleterDisplayName(String stepCompleterDisplayName) {
    this.stepCompleterDisplayName = stepCompleterDisplayName;
    return this;
  }

   /**
   * Get stepCompleterDisplayName
   * @return stepCompleterDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getStepCompleterDisplayName() {
    return stepCompleterDisplayName;
  }

  public void setStepCompleterDisplayName(String stepCompleterDisplayName) {
    this.stepCompleterDisplayName = stepCompleterDisplayName;
  }

  public ApprovalStep stepCompleterId(String stepCompleterId) {
    this.stepCompleterId = stepCompleterId;
    return this;
  }

   /**
   * Get stepCompleterId
   * @return stepCompleterId
  **/
  @ApiModelProperty(value = "")
  public String getStepCompleterId() {
    return stepCompleterId;
  }

  public void setStepCompleterId(String stepCompleterId) {
    this.stepCompleterId = stepCompleterId;
  }

  public ApprovalStep stepCompletionComment(String stepCompletionComment) {
    this.stepCompletionComment = stepCompletionComment;
    return this;
  }

   /**
   * Get stepCompletionComment
   * @return stepCompletionComment
  **/
  @ApiModelProperty(value = "")
  public String getStepCompletionComment() {
    return stepCompletionComment;
  }

  public void setStepCompletionComment(String stepCompletionComment) {
    this.stepCompletionComment = stepCompletionComment;
  }

  public ApprovalStep stepCompletionTime(Long stepCompletionTime) {
    this.stepCompletionTime = stepCompletionTime;
    return this;
  }

   /**
   * Get stepCompletionTime
   * @return stepCompletionTime
  **/
  @ApiModelProperty(value = "")
  public Long getStepCompletionTime() {
    return stepCompletionTime;
  }

  public void setStepCompletionTime(Long stepCompletionTime) {
    this.stepCompletionTime = stepCompletionTime;
  }

  public ApprovalStep stepNumber(Long stepNumber) {
    this.stepNumber = stepNumber;
    return this;
  }

   /**
   * Get stepNumber
   * @return stepNumber
  **/
  @ApiModelProperty(value = "")
  public Long getStepNumber() {
    return stepNumber;
  }

  public void setStepNumber(Long stepNumber) {
    this.stepNumber = stepNumber;
  }

  public ApprovalStep stepType(String stepType) {
    this.stepType = stepType;
    return this;
  }

   /**
   * Get stepType
   * @return stepType
  **/
  @ApiModelProperty(value = "")
  public String getStepType() {
    return stepType;
  }

  public void setStepType(String stepType) {
    this.stepType = stepType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApprovalStep approvalStep = (ApprovalStep) o;
    return Objects.equals(this.approverType, approvalStep.approverType) &&
        Objects.equals(this.approvers, approvalStep.approvers) &&
        Objects.equals(this.autoApprovedByBr, approvalStep.autoApprovedByBr) &&
        Objects.equals(this.autoApprovedByCond, approvalStep.autoApprovedByCond) &&
        Objects.equals(this.autoApprovedByValue, approvalStep.autoApprovedByValue) &&
        Objects.equals(this.delayed, approvalStep.delayed) &&
        Objects.equals(this.delayedToTime, approvalStep.delayedToTime) &&
        Objects.equals(this.originalApproverUserDisplayName, approvalStep.originalApproverUserDisplayName) &&
        Objects.equals(this.originalApproverUserId, approvalStep.originalApproverUserId) &&
        Objects.equals(this.reassignments, approvalStep.reassignments) &&
        Objects.equals(this.standaloneWorkflowApprovals, approvalStep.standaloneWorkflowApprovals) &&
        Objects.equals(this.statusSetTime, approvalStep.statusSetTime) &&
        Objects.equals(this.statusSetterDisplayName, approvalStep.statusSetterDisplayName) &&
        Objects.equals(this.statusSetterUserId, approvalStep.statusSetterUserId) &&
        Objects.equals(this.stepApproveOrDenyStatus, approvalStep.stepApproveOrDenyStatus) &&
        Objects.equals(this.stepCompleterDisplayName, approvalStep.stepCompleterDisplayName) &&
        Objects.equals(this.stepCompleterId, approvalStep.stepCompleterId) &&
        Objects.equals(this.stepCompletionComment, approvalStep.stepCompletionComment) &&
        Objects.equals(this.stepCompletionTime, approvalStep.stepCompletionTime) &&
        Objects.equals(this.stepNumber, approvalStep.stepNumber) &&
        Objects.equals(this.stepType, approvalStep.stepType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approverType, approvers, autoApprovedByBr, autoApprovedByCond, autoApprovedByValue, delayed, delayedToTime, originalApproverUserDisplayName, originalApproverUserId, reassignments, standaloneWorkflowApprovals, statusSetTime, statusSetterDisplayName, statusSetterUserId, stepApproveOrDenyStatus, stepCompleterDisplayName, stepCompleterId, stepCompletionComment, stepCompletionTime, stepNumber, stepType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApprovalStep {\n");
    
    sb.append("    approverType: ").append(toIndentedString(approverType)).append("\n");
    sb.append("    approvers: ").append(toIndentedString(approvers)).append("\n");
    sb.append("    autoApprovedByBr: ").append(toIndentedString(autoApprovedByBr)).append("\n");
    sb.append("    autoApprovedByCond: ").append(toIndentedString(autoApprovedByCond)).append("\n");
    sb.append("    autoApprovedByValue: ").append(toIndentedString(autoApprovedByValue)).append("\n");
    sb.append("    delayed: ").append(toIndentedString(delayed)).append("\n");
    sb.append("    delayedToTime: ").append(toIndentedString(delayedToTime)).append("\n");
    sb.append("    originalApproverUserDisplayName: ").append(toIndentedString(originalApproverUserDisplayName)).append("\n");
    sb.append("    originalApproverUserId: ").append(toIndentedString(originalApproverUserId)).append("\n");
    sb.append("    reassignments: ").append(toIndentedString(reassignments)).append("\n");
    sb.append("    standaloneWorkflowApprovals: ").append(toIndentedString(standaloneWorkflowApprovals)).append("\n");
    sb.append("    statusSetTime: ").append(toIndentedString(statusSetTime)).append("\n");
    sb.append("    statusSetterDisplayName: ").append(toIndentedString(statusSetterDisplayName)).append("\n");
    sb.append("    statusSetterUserId: ").append(toIndentedString(statusSetterUserId)).append("\n");
    sb.append("    stepApproveOrDenyStatus: ").append(toIndentedString(stepApproveOrDenyStatus)).append("\n");
    sb.append("    stepCompleterDisplayName: ").append(toIndentedString(stepCompleterDisplayName)).append("\n");
    sb.append("    stepCompleterId: ").append(toIndentedString(stepCompleterId)).append("\n");
    sb.append("    stepCompletionComment: ").append(toIndentedString(stepCompletionComment)).append("\n");
    sb.append("    stepCompletionTime: ").append(toIndentedString(stepCompletionTime)).append("\n");
    sb.append("    stepNumber: ").append(toIndentedString(stepNumber)).append("\n");
    sb.append("    stepType: ").append(toIndentedString(stepType)).append("\n");
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
