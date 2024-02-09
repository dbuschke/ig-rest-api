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
import io.swagger.client.model.Addressee;
import io.swagger.client.model.Request;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ApprovalWorkItem
 */



public class ApprovalWorkItem {
  @SerializedName("activityId")
  private String activityId = null;

  @SerializedName("addressee")
  private String addressee = null;

  @SerializedName("addresseeDisplayName")
  private String addresseeDisplayName = null;

  @SerializedName("addresseeIdType")
  private String addresseeIdType = null;

  @SerializedName("addresseeType")
  private Long addresseeType = null;

  @SerializedName("addressees")
  private List<Addressee> addressees = null;

  @SerializedName("bulkApprovable")
  private String bulkApprovable = null;

  @SerializedName("cannotActOnTask")
  private Boolean cannotActOnTask = null;

  @SerializedName("createTime")
  private String createTime = null;

  @SerializedName("effectiveDate")
  private java.sql.Timestamp effectiveDate = null;

  @SerializedName("expirationDate")
  private java.sql.Timestamp expirationDate = null;

  @SerializedName("expirationTime")
  private String expirationTime = null;

  @SerializedName("groupId")
  private String groupId = null;

  @SerializedName("initiator")
  private String initiator = null;

  @SerializedName("initiatorDisplayName")
  private String initiatorDisplayName = null;

  @SerializedName("isAdd")
  private Boolean isAdd = null;

  @SerializedName("recipient")
  private String recipient = null;

  @SerializedName("recipientDisplayName")
  private String recipientDisplayName = null;

  @SerializedName("request")
  private Request request = null;

  @SerializedName("requestId")
  private String requestId = null;

  @SerializedName("requestItemDisplayName")
  private String requestItemDisplayName = null;

  @SerializedName("requestItemType")
  private String requestItemType = null;

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
    MODIFY_TECH_ROLE_DEFN("MODIFY_TECH_ROLE_DEFN"),
    @SerializedName("ADD_BUSINESS_ROLE_MEMBERSHIP")
    ADD_BUSINESS_ROLE_MEMBERSHIP("ADD_BUSINESS_ROLE_MEMBERSHIP"),
    @SerializedName("REMOVE_BUSINESS_ROLE_MEMBERSHIP")
    REMOVE_BUSINESS_ROLE_MEMBERSHIP("REMOVE_BUSINESS_ROLE_MEMBERSHIP");

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

  @SerializedName("resourceRequestId")
  private Long resourceRequestId = null;

  @SerializedName("resourceRequestItemId")
  private Long resourceRequestItemId = null;

  @SerializedName("status")
  private Long status = null;

  @SerializedName("totalAddressees")
  private Long totalAddressees = null;

  @SerializedName("workTaskId")
  private String workTaskId = null;

  @SerializedName("workflowName")
  private String workflowName = null;

  public ApprovalWorkItem activityId(String activityId) {
    this.activityId = activityId;
    return this;
  }

   /**
   * Get activityId
   * @return activityId
  **/
  @ApiModelProperty(value = "")
  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public ApprovalWorkItem addressee(String addressee) {
    this.addressee = addressee;
    return this;
  }

   /**
   * Get addressee
   * @return addressee
  **/
  @ApiModelProperty(value = "")
  public String getAddressee() {
    return addressee;
  }

  public void setAddressee(String addressee) {
    this.addressee = addressee;
  }

  public ApprovalWorkItem addresseeDisplayName(String addresseeDisplayName) {
    this.addresseeDisplayName = addresseeDisplayName;
    return this;
  }

   /**
   * Get addresseeDisplayName
   * @return addresseeDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getAddresseeDisplayName() {
    return addresseeDisplayName;
  }

  public void setAddresseeDisplayName(String addresseeDisplayName) {
    this.addresseeDisplayName = addresseeDisplayName;
  }

  public ApprovalWorkItem addresseeIdType(String addresseeIdType) {
    this.addresseeIdType = addresseeIdType;
    return this;
  }

   /**
   * Get addresseeIdType
   * @return addresseeIdType
  **/
  @ApiModelProperty(value = "")
  public String getAddresseeIdType() {
    return addresseeIdType;
  }

  public void setAddresseeIdType(String addresseeIdType) {
    this.addresseeIdType = addresseeIdType;
  }

  public ApprovalWorkItem addresseeType(Long addresseeType) {
    this.addresseeType = addresseeType;
    return this;
  }

   /**
   * Get addresseeType
   * @return addresseeType
  **/
  @ApiModelProperty(value = "")
  public Long getAddresseeType() {
    return addresseeType;
  }

  public void setAddresseeType(Long addresseeType) {
    this.addresseeType = addresseeType;
  }

  public ApprovalWorkItem addressees(List<Addressee> addressees) {
    this.addressees = addressees;
    return this;
  }

  public ApprovalWorkItem addAddresseesItem(Addressee addresseesItem) {
    if (this.addressees == null) {
      this.addressees = new ArrayList<Addressee>();
    }
    this.addressees.add(addresseesItem);
    return this;
  }

   /**
   * Get addressees
   * @return addressees
  **/
  @ApiModelProperty(value = "")
  public List<Addressee> getAddressees() {
    return addressees;
  }

  public void setAddressees(List<Addressee> addressees) {
    this.addressees = addressees;
  }

  public ApprovalWorkItem bulkApprovable(String bulkApprovable) {
    this.bulkApprovable = bulkApprovable;
    return this;
  }

   /**
   * Get bulkApprovable
   * @return bulkApprovable
  **/
  @ApiModelProperty(value = "")
  public String getBulkApprovable() {
    return bulkApprovable;
  }

  public void setBulkApprovable(String bulkApprovable) {
    this.bulkApprovable = bulkApprovable;
  }

  public ApprovalWorkItem cannotActOnTask(Boolean cannotActOnTask) {
    this.cannotActOnTask = cannotActOnTask;
    return this;
  }

   /**
   * Get cannotActOnTask
   * @return cannotActOnTask
  **/
  @ApiModelProperty(value = "")
  public Boolean isCannotActOnTask() {
    return cannotActOnTask;
  }

  public void setCannotActOnTask(Boolean cannotActOnTask) {
    this.cannotActOnTask = cannotActOnTask;
  }

  public ApprovalWorkItem createTime(String createTime) {
    this.createTime = createTime;
    return this;
  }

   /**
   * Get createTime
   * @return createTime
  **/
  @ApiModelProperty(value = "")
  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public ApprovalWorkItem effectiveDate(java.sql.Timestamp effectiveDate) {
    this.effectiveDate = effectiveDate;
    return this;
  }

   /**
   * Get effectiveDate
   * @return effectiveDate
  **/
  @ApiModelProperty(value = "")
  public java.sql.Timestamp getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(java.sql.Timestamp effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public ApprovalWorkItem expirationDate(java.sql.Timestamp expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

   /**
   * Get expirationDate
   * @return expirationDate
  **/
  @ApiModelProperty(value = "")
  public java.sql.Timestamp getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(java.sql.Timestamp expirationDate) {
    this.expirationDate = expirationDate;
  }

  public ApprovalWorkItem expirationTime(String expirationTime) {
    this.expirationTime = expirationTime;
    return this;
  }

   /**
   * Get expirationTime
   * @return expirationTime
  **/
  @ApiModelProperty(value = "")
  public String getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(String expirationTime) {
    this.expirationTime = expirationTime;
  }

  public ApprovalWorkItem groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

   /**
   * Get groupId
   * @return groupId
  **/
  @ApiModelProperty(value = "")
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public ApprovalWorkItem initiator(String initiator) {
    this.initiator = initiator;
    return this;
  }

   /**
   * Get initiator
   * @return initiator
  **/
  @ApiModelProperty(value = "")
  public String getInitiator() {
    return initiator;
  }

  public void setInitiator(String initiator) {
    this.initiator = initiator;
  }

  public ApprovalWorkItem initiatorDisplayName(String initiatorDisplayName) {
    this.initiatorDisplayName = initiatorDisplayName;
    return this;
  }

   /**
   * Get initiatorDisplayName
   * @return initiatorDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getInitiatorDisplayName() {
    return initiatorDisplayName;
  }

  public void setInitiatorDisplayName(String initiatorDisplayName) {
    this.initiatorDisplayName = initiatorDisplayName;
  }

  public ApprovalWorkItem isAdd(Boolean isAdd) {
    this.isAdd = isAdd;
    return this;
  }

   /**
   * Get isAdd
   * @return isAdd
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsAdd() {
    return isAdd;
  }

  public void setIsAdd(Boolean isAdd) {
    this.isAdd = isAdd;
  }

  public ApprovalWorkItem recipient(String recipient) {
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

  public ApprovalWorkItem recipientDisplayName(String recipientDisplayName) {
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

  public ApprovalWorkItem request(Request request) {
    this.request = request;
    return this;
  }

   /**
   * Get request
   * @return request
  **/
  @ApiModelProperty(value = "")
  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public ApprovalWorkItem requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

   /**
   * Get requestId
   * @return requestId
  **/
  @ApiModelProperty(value = "")
  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public ApprovalWorkItem requestItemDisplayName(String requestItemDisplayName) {
    this.requestItemDisplayName = requestItemDisplayName;
    return this;
  }

   /**
   * Get requestItemDisplayName
   * @return requestItemDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getRequestItemDisplayName() {
    return requestItemDisplayName;
  }

  public void setRequestItemDisplayName(String requestItemDisplayName) {
    this.requestItemDisplayName = requestItemDisplayName;
  }

  public ApprovalWorkItem requestItemType(String requestItemType) {
    this.requestItemType = requestItemType;
    return this;
  }

   /**
   * Get requestItemType
   * @return requestItemType
  **/
  @ApiModelProperty(value = "")
  public String getRequestItemType() {
    return requestItemType;
  }

  public void setRequestItemType(String requestItemType) {
    this.requestItemType = requestItemType;
  }

  public ApprovalWorkItem requestType(RequestTypeEnum requestType) {
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

  public ApprovalWorkItem resourceRequestId(Long resourceRequestId) {
    this.resourceRequestId = resourceRequestId;
    return this;
  }

   /**
   * Get resourceRequestId
   * @return resourceRequestId
  **/
  @ApiModelProperty(value = "")
  public Long getResourceRequestId() {
    return resourceRequestId;
  }

  public void setResourceRequestId(Long resourceRequestId) {
    this.resourceRequestId = resourceRequestId;
  }

  public ApprovalWorkItem resourceRequestItemId(Long resourceRequestItemId) {
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

  public ApprovalWorkItem status(Long status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public ApprovalWorkItem totalAddressees(Long totalAddressees) {
    this.totalAddressees = totalAddressees;
    return this;
  }

   /**
   * Get totalAddressees
   * @return totalAddressees
  **/
  @ApiModelProperty(value = "")
  public Long getTotalAddressees() {
    return totalAddressees;
  }

  public void setTotalAddressees(Long totalAddressees) {
    this.totalAddressees = totalAddressees;
  }

  public ApprovalWorkItem workTaskId(String workTaskId) {
    this.workTaskId = workTaskId;
    return this;
  }

   /**
   * Get workTaskId
   * @return workTaskId
  **/
  @ApiModelProperty(value = "")
  public String getWorkTaskId() {
    return workTaskId;
  }

  public void setWorkTaskId(String workTaskId) {
    this.workTaskId = workTaskId;
  }

  public ApprovalWorkItem workflowName(String workflowName) {
    this.workflowName = workflowName;
    return this;
  }

   /**
   * Get workflowName
   * @return workflowName
  **/
  @ApiModelProperty(value = "")
  public String getWorkflowName() {
    return workflowName;
  }

  public void setWorkflowName(String workflowName) {
    this.workflowName = workflowName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApprovalWorkItem approvalWorkItem = (ApprovalWorkItem) o;
    return Objects.equals(this.activityId, approvalWorkItem.activityId) &&
        Objects.equals(this.addressee, approvalWorkItem.addressee) &&
        Objects.equals(this.addresseeDisplayName, approvalWorkItem.addresseeDisplayName) &&
        Objects.equals(this.addresseeIdType, approvalWorkItem.addresseeIdType) &&
        Objects.equals(this.addresseeType, approvalWorkItem.addresseeType) &&
        Objects.equals(this.addressees, approvalWorkItem.addressees) &&
        Objects.equals(this.bulkApprovable, approvalWorkItem.bulkApprovable) &&
        Objects.equals(this.cannotActOnTask, approvalWorkItem.cannotActOnTask) &&
        Objects.equals(this.createTime, approvalWorkItem.createTime) &&
        Objects.equals(this.effectiveDate, approvalWorkItem.effectiveDate) &&
        Objects.equals(this.expirationDate, approvalWorkItem.expirationDate) &&
        Objects.equals(this.expirationTime, approvalWorkItem.expirationTime) &&
        Objects.equals(this.groupId, approvalWorkItem.groupId) &&
        Objects.equals(this.initiator, approvalWorkItem.initiator) &&
        Objects.equals(this.initiatorDisplayName, approvalWorkItem.initiatorDisplayName) &&
        Objects.equals(this.isAdd, approvalWorkItem.isAdd) &&
        Objects.equals(this.recipient, approvalWorkItem.recipient) &&
        Objects.equals(this.recipientDisplayName, approvalWorkItem.recipientDisplayName) &&
        Objects.equals(this.request, approvalWorkItem.request) &&
        Objects.equals(this.requestId, approvalWorkItem.requestId) &&
        Objects.equals(this.requestItemDisplayName, approvalWorkItem.requestItemDisplayName) &&
        Objects.equals(this.requestItemType, approvalWorkItem.requestItemType) &&
        Objects.equals(this.requestType, approvalWorkItem.requestType) &&
        Objects.equals(this.resourceRequestId, approvalWorkItem.resourceRequestId) &&
        Objects.equals(this.resourceRequestItemId, approvalWorkItem.resourceRequestItemId) &&
        Objects.equals(this.status, approvalWorkItem.status) &&
        Objects.equals(this.totalAddressees, approvalWorkItem.totalAddressees) &&
        Objects.equals(this.workTaskId, approvalWorkItem.workTaskId) &&
        Objects.equals(this.workflowName, approvalWorkItem.workflowName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activityId, addressee, addresseeDisplayName, addresseeIdType, addresseeType, addressees, bulkApprovable, cannotActOnTask, createTime, effectiveDate, expirationDate, expirationTime, groupId, initiator, initiatorDisplayName, isAdd, recipient, recipientDisplayName, request, requestId, requestItemDisplayName, requestItemType, requestType, resourceRequestId, resourceRequestItemId, status, totalAddressees, workTaskId, workflowName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApprovalWorkItem {\n");
    
    sb.append("    activityId: ").append(toIndentedString(activityId)).append("\n");
    sb.append("    addressee: ").append(toIndentedString(addressee)).append("\n");
    sb.append("    addresseeDisplayName: ").append(toIndentedString(addresseeDisplayName)).append("\n");
    sb.append("    addresseeIdType: ").append(toIndentedString(addresseeIdType)).append("\n");
    sb.append("    addresseeType: ").append(toIndentedString(addresseeType)).append("\n");
    sb.append("    addressees: ").append(toIndentedString(addressees)).append("\n");
    sb.append("    bulkApprovable: ").append(toIndentedString(bulkApprovable)).append("\n");
    sb.append("    cannotActOnTask: ").append(toIndentedString(cannotActOnTask)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    effectiveDate: ").append(toIndentedString(effectiveDate)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    expirationTime: ").append(toIndentedString(expirationTime)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    initiator: ").append(toIndentedString(initiator)).append("\n");
    sb.append("    initiatorDisplayName: ").append(toIndentedString(initiatorDisplayName)).append("\n");
    sb.append("    isAdd: ").append(toIndentedString(isAdd)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("    recipientDisplayName: ").append(toIndentedString(recipientDisplayName)).append("\n");
    sb.append("    request: ").append(toIndentedString(request)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    requestItemDisplayName: ").append(toIndentedString(requestItemDisplayName)).append("\n");
    sb.append("    requestItemType: ").append(toIndentedString(requestItemType)).append("\n");
    sb.append("    requestType: ").append(toIndentedString(requestType)).append("\n");
    sb.append("    resourceRequestId: ").append(toIndentedString(resourceRequestId)).append("\n");
    sb.append("    resourceRequestItemId: ").append(toIndentedString(resourceRequestItemId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    totalAddressees: ").append(toIndentedString(totalAddressees)).append("\n");
    sb.append("    workTaskId: ").append(toIndentedString(workTaskId)).append("\n");
    sb.append("    workflowName: ").append(toIndentedString(workflowName)).append("\n");
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
