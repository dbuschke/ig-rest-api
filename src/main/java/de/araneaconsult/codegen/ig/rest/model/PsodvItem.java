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
import de.araneaconsult.codegen.ig.rest.model.AccessApprovalStepApprover;
import de.araneaconsult.codegen.ig.rest.model.TaskItemHistory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * PsodvItem
 */



public class PsodvItem {
  @SerializedName("psodvApprovers")
  private List<AccessApprovalStepApprover> psodvApprovers = null;

  @SerializedName("psodvCompletedBy")
  private String psodvCompletedBy = null;

  @SerializedName("psodvCompletedByDisplayName")
  private String psodvCompletedByDisplayName = null;

  @SerializedName("psodvCompletionComment")
  private String psodvCompletionComment = null;

  @SerializedName("psodvCompletionDate")
  private Long psodvCompletionDate = null;

  @SerializedName("psodvCurrentStep")
  private Long psodvCurrentStep = null;

  @SerializedName("psodvDeletedReason")
  private String psodvDeletedReason = null;

  @SerializedName("psodvItemDecidedBy")
  private String psodvItemDecidedBy = null;

  @SerializedName("psodvItemDecidedByDisplayName")
  private String psodvItemDecidedByDisplayName = null;

  @SerializedName("psodvItemDecision")
  private String psodvItemDecision = null;

  @SerializedName("psodvItemDecisionDate")
  private Long psodvItemDecisionDate = null;

  @SerializedName("psodvTotalSteps")
  private Long psodvTotalSteps = null;

  @SerializedName("sodPolicyName")
  private String sodPolicyName = null;

  @SerializedName("taskItemHistory")
  private List<TaskItemHistory> taskItemHistory = null;

  @SerializedName("violatorDisplayName")
  private String violatorDisplayName = null;

  @SerializedName("violatorUserId")
  private String violatorUserId = null;

  public PsodvItem psodvApprovers(List<AccessApprovalStepApprover> psodvApprovers) {
    this.psodvApprovers = psodvApprovers;
    return this;
  }

  public PsodvItem addPsodvApproversItem(AccessApprovalStepApprover psodvApproversItem) {
    if (this.psodvApprovers == null) {
      this.psodvApprovers = new ArrayList<AccessApprovalStepApprover>();
    }
    this.psodvApprovers.add(psodvApproversItem);
    return this;
  }

   /**
   * Get psodvApprovers
   * @return psodvApprovers
  **/
  @ApiModelProperty(value = "")
  public List<AccessApprovalStepApprover> getPsodvApprovers() {
    return psodvApprovers;
  }

  public void setPsodvApprovers(List<AccessApprovalStepApprover> psodvApprovers) {
    this.psodvApprovers = psodvApprovers;
  }

  public PsodvItem psodvCompletedBy(String psodvCompletedBy) {
    this.psodvCompletedBy = psodvCompletedBy;
    return this;
  }

   /**
   * Get psodvCompletedBy
   * @return psodvCompletedBy
  **/
  @ApiModelProperty(value = "")
  public String getPsodvCompletedBy() {
    return psodvCompletedBy;
  }

  public void setPsodvCompletedBy(String psodvCompletedBy) {
    this.psodvCompletedBy = psodvCompletedBy;
  }

  public PsodvItem psodvCompletedByDisplayName(String psodvCompletedByDisplayName) {
    this.psodvCompletedByDisplayName = psodvCompletedByDisplayName;
    return this;
  }

   /**
   * Get psodvCompletedByDisplayName
   * @return psodvCompletedByDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getPsodvCompletedByDisplayName() {
    return psodvCompletedByDisplayName;
  }

  public void setPsodvCompletedByDisplayName(String psodvCompletedByDisplayName) {
    this.psodvCompletedByDisplayName = psodvCompletedByDisplayName;
  }

  public PsodvItem psodvCompletionComment(String psodvCompletionComment) {
    this.psodvCompletionComment = psodvCompletionComment;
    return this;
  }

   /**
   * Get psodvCompletionComment
   * @return psodvCompletionComment
  **/
  @ApiModelProperty(value = "")
  public String getPsodvCompletionComment() {
    return psodvCompletionComment;
  }

  public void setPsodvCompletionComment(String psodvCompletionComment) {
    this.psodvCompletionComment = psodvCompletionComment;
  }

  public PsodvItem psodvCompletionDate(Long psodvCompletionDate) {
    this.psodvCompletionDate = psodvCompletionDate;
    return this;
  }

   /**
   * Get psodvCompletionDate
   * @return psodvCompletionDate
  **/
  @ApiModelProperty(value = "")
  public Long getPsodvCompletionDate() {
    return psodvCompletionDate;
  }

  public void setPsodvCompletionDate(Long psodvCompletionDate) {
    this.psodvCompletionDate = psodvCompletionDate;
  }

  public PsodvItem psodvCurrentStep(Long psodvCurrentStep) {
    this.psodvCurrentStep = psodvCurrentStep;
    return this;
  }

   /**
   * Get psodvCurrentStep
   * @return psodvCurrentStep
  **/
  @ApiModelProperty(value = "")
  public Long getPsodvCurrentStep() {
    return psodvCurrentStep;
  }

  public void setPsodvCurrentStep(Long psodvCurrentStep) {
    this.psodvCurrentStep = psodvCurrentStep;
  }

  public PsodvItem psodvDeletedReason(String psodvDeletedReason) {
    this.psodvDeletedReason = psodvDeletedReason;
    return this;
  }

   /**
   * Get psodvDeletedReason
   * @return psodvDeletedReason
  **/
  @ApiModelProperty(value = "")
  public String getPsodvDeletedReason() {
    return psodvDeletedReason;
  }

  public void setPsodvDeletedReason(String psodvDeletedReason) {
    this.psodvDeletedReason = psodvDeletedReason;
  }

  public PsodvItem psodvItemDecidedBy(String psodvItemDecidedBy) {
    this.psodvItemDecidedBy = psodvItemDecidedBy;
    return this;
  }

   /**
   * Get psodvItemDecidedBy
   * @return psodvItemDecidedBy
  **/
  @ApiModelProperty(value = "")
  public String getPsodvItemDecidedBy() {
    return psodvItemDecidedBy;
  }

  public void setPsodvItemDecidedBy(String psodvItemDecidedBy) {
    this.psodvItemDecidedBy = psodvItemDecidedBy;
  }

  public PsodvItem psodvItemDecidedByDisplayName(String psodvItemDecidedByDisplayName) {
    this.psodvItemDecidedByDisplayName = psodvItemDecidedByDisplayName;
    return this;
  }

   /**
   * Get psodvItemDecidedByDisplayName
   * @return psodvItemDecidedByDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getPsodvItemDecidedByDisplayName() {
    return psodvItemDecidedByDisplayName;
  }

  public void setPsodvItemDecidedByDisplayName(String psodvItemDecidedByDisplayName) {
    this.psodvItemDecidedByDisplayName = psodvItemDecidedByDisplayName;
  }

  public PsodvItem psodvItemDecision(String psodvItemDecision) {
    this.psodvItemDecision = psodvItemDecision;
    return this;
  }

   /**
   * Get psodvItemDecision
   * @return psodvItemDecision
  **/
  @ApiModelProperty(value = "")
  public String getPsodvItemDecision() {
    return psodvItemDecision;
  }

  public void setPsodvItemDecision(String psodvItemDecision) {
    this.psodvItemDecision = psodvItemDecision;
  }

  public PsodvItem psodvItemDecisionDate(Long psodvItemDecisionDate) {
    this.psodvItemDecisionDate = psodvItemDecisionDate;
    return this;
  }

   /**
   * Get psodvItemDecisionDate
   * @return psodvItemDecisionDate
  **/
  @ApiModelProperty(value = "")
  public Long getPsodvItemDecisionDate() {
    return psodvItemDecisionDate;
  }

  public void setPsodvItemDecisionDate(Long psodvItemDecisionDate) {
    this.psodvItemDecisionDate = psodvItemDecisionDate;
  }

  public PsodvItem psodvTotalSteps(Long psodvTotalSteps) {
    this.psodvTotalSteps = psodvTotalSteps;
    return this;
  }

   /**
   * Get psodvTotalSteps
   * @return psodvTotalSteps
  **/
  @ApiModelProperty(value = "")
  public Long getPsodvTotalSteps() {
    return psodvTotalSteps;
  }

  public void setPsodvTotalSteps(Long psodvTotalSteps) {
    this.psodvTotalSteps = psodvTotalSteps;
  }

  public PsodvItem sodPolicyName(String sodPolicyName) {
    this.sodPolicyName = sodPolicyName;
    return this;
  }

   /**
   * Get sodPolicyName
   * @return sodPolicyName
  **/
  @ApiModelProperty(value = "")
  public String getSodPolicyName() {
    return sodPolicyName;
  }

  public void setSodPolicyName(String sodPolicyName) {
    this.sodPolicyName = sodPolicyName;
  }

  public PsodvItem taskItemHistory(List<TaskItemHistory> taskItemHistory) {
    this.taskItemHistory = taskItemHistory;
    return this;
  }

  public PsodvItem addTaskItemHistoryItem(TaskItemHistory taskItemHistoryItem) {
    if (this.taskItemHistory == null) {
      this.taskItemHistory = new ArrayList<TaskItemHistory>();
    }
    this.taskItemHistory.add(taskItemHistoryItem);
    return this;
  }

   /**
   * Get taskItemHistory
   * @return taskItemHistory
  **/
  @ApiModelProperty(value = "")
  public List<TaskItemHistory> getTaskItemHistory() {
    return taskItemHistory;
  }

  public void setTaskItemHistory(List<TaskItemHistory> taskItemHistory) {
    this.taskItemHistory = taskItemHistory;
  }

  public PsodvItem violatorDisplayName(String violatorDisplayName) {
    this.violatorDisplayName = violatorDisplayName;
    return this;
  }

   /**
   * Get violatorDisplayName
   * @return violatorDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getViolatorDisplayName() {
    return violatorDisplayName;
  }

  public void setViolatorDisplayName(String violatorDisplayName) {
    this.violatorDisplayName = violatorDisplayName;
  }

  public PsodvItem violatorUserId(String violatorUserId) {
    this.violatorUserId = violatorUserId;
    return this;
  }

   /**
   * Get violatorUserId
   * @return violatorUserId
  **/
  @ApiModelProperty(value = "")
  public String getViolatorUserId() {
    return violatorUserId;
  }

  public void setViolatorUserId(String violatorUserId) {
    this.violatorUserId = violatorUserId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PsodvItem psodvItem = (PsodvItem) o;
    return Objects.equals(this.psodvApprovers, psodvItem.psodvApprovers) &&
        Objects.equals(this.psodvCompletedBy, psodvItem.psodvCompletedBy) &&
        Objects.equals(this.psodvCompletedByDisplayName, psodvItem.psodvCompletedByDisplayName) &&
        Objects.equals(this.psodvCompletionComment, psodvItem.psodvCompletionComment) &&
        Objects.equals(this.psodvCompletionDate, psodvItem.psodvCompletionDate) &&
        Objects.equals(this.psodvCurrentStep, psodvItem.psodvCurrentStep) &&
        Objects.equals(this.psodvDeletedReason, psodvItem.psodvDeletedReason) &&
        Objects.equals(this.psodvItemDecidedBy, psodvItem.psodvItemDecidedBy) &&
        Objects.equals(this.psodvItemDecidedByDisplayName, psodvItem.psodvItemDecidedByDisplayName) &&
        Objects.equals(this.psodvItemDecision, psodvItem.psodvItemDecision) &&
        Objects.equals(this.psodvItemDecisionDate, psodvItem.psodvItemDecisionDate) &&
        Objects.equals(this.psodvTotalSteps, psodvItem.psodvTotalSteps) &&
        Objects.equals(this.sodPolicyName, psodvItem.sodPolicyName) &&
        Objects.equals(this.taskItemHistory, psodvItem.taskItemHistory) &&
        Objects.equals(this.violatorDisplayName, psodvItem.violatorDisplayName) &&
        Objects.equals(this.violatorUserId, psodvItem.violatorUserId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(psodvApprovers, psodvCompletedBy, psodvCompletedByDisplayName, psodvCompletionComment, psodvCompletionDate, psodvCurrentStep, psodvDeletedReason, psodvItemDecidedBy, psodvItemDecidedByDisplayName, psodvItemDecision, psodvItemDecisionDate, psodvTotalSteps, sodPolicyName, taskItemHistory, violatorDisplayName, violatorUserId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PsodvItem {\n");
    
    sb.append("    psodvApprovers: ").append(toIndentedString(psodvApprovers)).append("\n");
    sb.append("    psodvCompletedBy: ").append(toIndentedString(psodvCompletedBy)).append("\n");
    sb.append("    psodvCompletedByDisplayName: ").append(toIndentedString(psodvCompletedByDisplayName)).append("\n");
    sb.append("    psodvCompletionComment: ").append(toIndentedString(psodvCompletionComment)).append("\n");
    sb.append("    psodvCompletionDate: ").append(toIndentedString(psodvCompletionDate)).append("\n");
    sb.append("    psodvCurrentStep: ").append(toIndentedString(psodvCurrentStep)).append("\n");
    sb.append("    psodvDeletedReason: ").append(toIndentedString(psodvDeletedReason)).append("\n");
    sb.append("    psodvItemDecidedBy: ").append(toIndentedString(psodvItemDecidedBy)).append("\n");
    sb.append("    psodvItemDecidedByDisplayName: ").append(toIndentedString(psodvItemDecidedByDisplayName)).append("\n");
    sb.append("    psodvItemDecision: ").append(toIndentedString(psodvItemDecision)).append("\n");
    sb.append("    psodvItemDecisionDate: ").append(toIndentedString(psodvItemDecisionDate)).append("\n");
    sb.append("    psodvTotalSteps: ").append(toIndentedString(psodvTotalSteps)).append("\n");
    sb.append("    sodPolicyName: ").append(toIndentedString(sodPolicyName)).append("\n");
    sb.append("    taskItemHistory: ").append(toIndentedString(taskItemHistory)).append("\n");
    sb.append("    violatorDisplayName: ").append(toIndentedString(violatorDisplayName)).append("\n");
    sb.append("    violatorUserId: ").append(toIndentedString(violatorUserId)).append("\n");
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
