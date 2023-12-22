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
import io.swagger.client.model.ApprovalWorkItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Approval
 */



public class Approval {
  @SerializedName("comments")
  private Object comments = null;

  @SerializedName("correlationId")
  private String correlationId = null;

  @SerializedName("flowdata")
  private Object flowdata = null;

  @SerializedName("form")
  private Object form = null;

  @SerializedName("isRunning")
  private Boolean isRunning = null;

  @SerializedName("isSimulate")
  private Boolean isSimulate = null;

  @SerializedName("processState")
  private Long processState = null;

  @SerializedName("processStateDisplayName")
  private String processStateDisplayName = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("tasks")
  private List<ApprovalWorkItem> tasks = null;

  @SerializedName("workflowProcessId")
  private String workflowProcessId = null;

  @SerializedName("workflowStatus")
  private String workflowStatus = null;

  public Approval comments(Object comments) {
    this.comments = comments;
    return this;
  }

   /**
   * Get comments
   * @return comments
  **/
  @ApiModelProperty(value = "")
  public Object getComments() {
    return comments;
  }

  public void setComments(Object comments) {
    this.comments = comments;
  }

  public Approval correlationId(String correlationId) {
    this.correlationId = correlationId;
    return this;
  }

   /**
   * the correlation id
   * @return correlationId
  **/
  @ApiModelProperty(value = "the correlation id")
  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public Approval flowdata(Object flowdata) {
    this.flowdata = flowdata;
    return this;
  }

   /**
   * Get flowdata
   * @return flowdata
  **/
  @ApiModelProperty(value = "")
  public Object getFlowdata() {
    return flowdata;
  }

  public void setFlowdata(Object flowdata) {
    this.flowdata = flowdata;
  }

  public Approval form(Object form) {
    this.form = form;
    return this;
  }

   /**
   * Get form
   * @return form
  **/
  @ApiModelProperty(value = "")
  public Object getForm() {
    return form;
  }

  public void setForm(Object form) {
    this.form = form;
  }

  public Approval isRunning(Boolean isRunning) {
    this.isRunning = isRunning;
    return this;
  }

   /**
   * Get isRunning
   * @return isRunning
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsRunning() {
    return isRunning;
  }

  public void setIsRunning(Boolean isRunning) {
    this.isRunning = isRunning;
  }

  public Approval isSimulate(Boolean isSimulate) {
    this.isSimulate = isSimulate;
    return this;
  }

   /**
   * Get isSimulate
   * @return isSimulate
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsSimulate() {
    return isSimulate;
  }

  public void setIsSimulate(Boolean isSimulate) {
    this.isSimulate = isSimulate;
  }

  public Approval processState(Long processState) {
    this.processState = processState;
    return this;
  }

   /**
   * Get processState
   * @return processState
  **/
  @ApiModelProperty(value = "")
  public Long getProcessState() {
    return processState;
  }

  public void setProcessState(Long processState) {
    this.processState = processState;
  }

  public Approval processStateDisplayName(String processStateDisplayName) {
    this.processStateDisplayName = processStateDisplayName;
    return this;
  }

   /**
   * Get processStateDisplayName
   * @return processStateDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getProcessStateDisplayName() {
    return processStateDisplayName;
  }

  public void setProcessStateDisplayName(String processStateDisplayName) {
    this.processStateDisplayName = processStateDisplayName;
  }

  public Approval status(String status) {
    this.status = status;
    return this;
  }

   /**
   * the status
   * @return status
  **/
  @ApiModelProperty(value = "the status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Approval tasks(List<ApprovalWorkItem> tasks) {
    this.tasks = tasks;
    return this;
  }

  public Approval addTasksItem(ApprovalWorkItem tasksItem) {
    if (this.tasks == null) {
      this.tasks = new ArrayList<ApprovalWorkItem>();
    }
    this.tasks.add(tasksItem);
    return this;
  }

   /**
   * Get tasks
   * @return tasks
  **/
  @ApiModelProperty(value = "")
  public List<ApprovalWorkItem> getTasks() {
    return tasks;
  }

  public void setTasks(List<ApprovalWorkItem> tasks) {
    this.tasks = tasks;
  }

  public Approval workflowProcessId(String workflowProcessId) {
    this.workflowProcessId = workflowProcessId;
    return this;
  }

   /**
   * the workflow process id
   * @return workflowProcessId
  **/
  @ApiModelProperty(value = "the workflow process id")
  public String getWorkflowProcessId() {
    return workflowProcessId;
  }

  public void setWorkflowProcessId(String workflowProcessId) {
    this.workflowProcessId = workflowProcessId;
  }

  public Approval workflowStatus(String workflowStatus) {
    this.workflowStatus = workflowStatus;
    return this;
  }

   /**
   * The workflow status. Allowable values are &#x27;approved&#x27;, &#x27;denied&#x27;.
   * @return workflowStatus
  **/
  @ApiModelProperty(value = "The workflow status. Allowable values are 'approved', 'denied'.")
  public String getWorkflowStatus() {
    return workflowStatus;
  }

  public void setWorkflowStatus(String workflowStatus) {
    this.workflowStatus = workflowStatus;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Approval approval = (Approval) o;
    return Objects.equals(this.comments, approval.comments) &&
        Objects.equals(this.correlationId, approval.correlationId) &&
        Objects.equals(this.flowdata, approval.flowdata) &&
        Objects.equals(this.form, approval.form) &&
        Objects.equals(this.isRunning, approval.isRunning) &&
        Objects.equals(this.isSimulate, approval.isSimulate) &&
        Objects.equals(this.processState, approval.processState) &&
        Objects.equals(this.processStateDisplayName, approval.processStateDisplayName) &&
        Objects.equals(this.status, approval.status) &&
        Objects.equals(this.tasks, approval.tasks) &&
        Objects.equals(this.workflowProcessId, approval.workflowProcessId) &&
        Objects.equals(this.workflowStatus, approval.workflowStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comments, correlationId, flowdata, form, isRunning, isSimulate, processState, processStateDisplayName, status, tasks, workflowProcessId, workflowStatus);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Approval {\n");
    
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    flowdata: ").append(toIndentedString(flowdata)).append("\n");
    sb.append("    form: ").append(toIndentedString(form)).append("\n");
    sb.append("    isRunning: ").append(toIndentedString(isRunning)).append("\n");
    sb.append("    isSimulate: ").append(toIndentedString(isSimulate)).append("\n");
    sb.append("    processState: ").append(toIndentedString(processState)).append("\n");
    sb.append("    processStateDisplayName: ").append(toIndentedString(processStateDisplayName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tasks: ").append(toIndentedString(tasks)).append("\n");
    sb.append("    workflowProcessId: ").append(toIndentedString(workflowProcessId)).append("\n");
    sb.append("    workflowStatus: ").append(toIndentedString(workflowStatus)).append("\n");
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
