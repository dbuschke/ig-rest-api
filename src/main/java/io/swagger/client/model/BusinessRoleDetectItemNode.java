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
import java.io.IOException;
/**
 * BusinessRoleDetectItemNode
 */



public class BusinessRoleDetectItemNode {
  @SerializedName("authProcessTime")
  private Long authProcessTime = null;

  @SerializedName("authSaveTime")
  private Long authSaveTime = null;

  @SerializedName("businessRoleDetectionId")
  private Long businessRoleDetectionId = null;

  @SerializedName("businessRoleName")
  private String businessRoleName = null;

  @SerializedName("deactivation")
  private Boolean deactivation = null;

  @SerializedName("deletedAuthCount")
  private Long deletedAuthCount = null;

  @SerializedName("deletedMemberCount")
  private Long deletedMemberCount = null;

  @SerializedName("detectSequence")
  private Long detectSequence = null;

  @SerializedName("elapsedTime")
  private Long elapsedTime = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("errMessage")
  private String errMessage = null;

  @SerializedName("haveAuthDetails")
  private Boolean haveAuthDetails = null;

  @SerializedName("haveMemberDetails")
  private Boolean haveMemberDetails = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("membershipProcessTime")
  private Long membershipProcessTime = null;

  @SerializedName("membershipSaveTime")
  private Long membershipSaveTime = null;

  @SerializedName("newAuthCount")
  private Long newAuthCount = null;

  @SerializedName("newMemberCount")
  private Long newMemberCount = null;

  @SerializedName("startDate")
  private Long startDate = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS("IN_PROGRESS"),
    @SerializedName("ERROR")
    ERROR("ERROR"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
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

  @SerializedName("totalAuthCount")
  private Long totalAuthCount = null;

  @SerializedName("totalMemberCount")
  private Long totalMemberCount = null;

  @SerializedName("uniqueBusinessRoleId")
  private String uniqueBusinessRoleId = null;

  public BusinessRoleDetectItemNode authProcessTime(Long authProcessTime) {
    this.authProcessTime = authProcessTime;
    return this;
  }

   /**
   * Get authProcessTime
   * @return authProcessTime
  **/
  @ApiModelProperty(value = "")
  public Long getAuthProcessTime() {
    return authProcessTime;
  }

  public void setAuthProcessTime(Long authProcessTime) {
    this.authProcessTime = authProcessTime;
  }

  public BusinessRoleDetectItemNode authSaveTime(Long authSaveTime) {
    this.authSaveTime = authSaveTime;
    return this;
  }

   /**
   * Get authSaveTime
   * @return authSaveTime
  **/
  @ApiModelProperty(value = "")
  public Long getAuthSaveTime() {
    return authSaveTime;
  }

  public void setAuthSaveTime(Long authSaveTime) {
    this.authSaveTime = authSaveTime;
  }

  public BusinessRoleDetectItemNode businessRoleDetectionId(Long businessRoleDetectionId) {
    this.businessRoleDetectionId = businessRoleDetectionId;
    return this;
  }

   /**
   * Get businessRoleDetectionId
   * @return businessRoleDetectionId
  **/
  @ApiModelProperty(value = "")
  public Long getBusinessRoleDetectionId() {
    return businessRoleDetectionId;
  }

  public void setBusinessRoleDetectionId(Long businessRoleDetectionId) {
    this.businessRoleDetectionId = businessRoleDetectionId;
  }

  public BusinessRoleDetectItemNode businessRoleName(String businessRoleName) {
    this.businessRoleName = businessRoleName;
    return this;
  }

   /**
   * Get businessRoleName
   * @return businessRoleName
  **/
  @ApiModelProperty(value = "")
  public String getBusinessRoleName() {
    return businessRoleName;
  }

  public void setBusinessRoleName(String businessRoleName) {
    this.businessRoleName = businessRoleName;
  }

  public BusinessRoleDetectItemNode deactivation(Boolean deactivation) {
    this.deactivation = deactivation;
    return this;
  }

   /**
   * Get deactivation
   * @return deactivation
  **/
  @ApiModelProperty(value = "")
  public Boolean isDeactivation() {
    return deactivation;
  }

  public void setDeactivation(Boolean deactivation) {
    this.deactivation = deactivation;
  }

  public BusinessRoleDetectItemNode deletedAuthCount(Long deletedAuthCount) {
    this.deletedAuthCount = deletedAuthCount;
    return this;
  }

   /**
   * Get deletedAuthCount
   * @return deletedAuthCount
  **/
  @ApiModelProperty(value = "")
  public Long getDeletedAuthCount() {
    return deletedAuthCount;
  }

  public void setDeletedAuthCount(Long deletedAuthCount) {
    this.deletedAuthCount = deletedAuthCount;
  }

  public BusinessRoleDetectItemNode deletedMemberCount(Long deletedMemberCount) {
    this.deletedMemberCount = deletedMemberCount;
    return this;
  }

   /**
   * Get deletedMemberCount
   * @return deletedMemberCount
  **/
  @ApiModelProperty(value = "")
  public Long getDeletedMemberCount() {
    return deletedMemberCount;
  }

  public void setDeletedMemberCount(Long deletedMemberCount) {
    this.deletedMemberCount = deletedMemberCount;
  }

  public BusinessRoleDetectItemNode detectSequence(Long detectSequence) {
    this.detectSequence = detectSequence;
    return this;
  }

   /**
   * Get detectSequence
   * @return detectSequence
  **/
  @ApiModelProperty(value = "")
  public Long getDetectSequence() {
    return detectSequence;
  }

  public void setDetectSequence(Long detectSequence) {
    this.detectSequence = detectSequence;
  }

  public BusinessRoleDetectItemNode elapsedTime(Long elapsedTime) {
    this.elapsedTime = elapsedTime;
    return this;
  }

   /**
   * Get elapsedTime
   * @return elapsedTime
  **/
  @ApiModelProperty(value = "")
  public Long getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(Long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public BusinessRoleDetectItemNode endDate(Long endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * Get endDate
   * @return endDate
  **/
  @ApiModelProperty(value = "")
  public Long getEndDate() {
    return endDate;
  }

  public void setEndDate(Long endDate) {
    this.endDate = endDate;
  }

  public BusinessRoleDetectItemNode errMessage(String errMessage) {
    this.errMessage = errMessage;
    return this;
  }

   /**
   * Get errMessage
   * @return errMessage
  **/
  @ApiModelProperty(value = "")
  public String getErrMessage() {
    return errMessage;
  }

  public void setErrMessage(String errMessage) {
    this.errMessage = errMessage;
  }

  public BusinessRoleDetectItemNode haveAuthDetails(Boolean haveAuthDetails) {
    this.haveAuthDetails = haveAuthDetails;
    return this;
  }

   /**
   * Get haveAuthDetails
   * @return haveAuthDetails
  **/
  @ApiModelProperty(value = "")
  public Boolean isHaveAuthDetails() {
    return haveAuthDetails;
  }

  public void setHaveAuthDetails(Boolean haveAuthDetails) {
    this.haveAuthDetails = haveAuthDetails;
  }

  public BusinessRoleDetectItemNode haveMemberDetails(Boolean haveMemberDetails) {
    this.haveMemberDetails = haveMemberDetails;
    return this;
  }

   /**
   * Get haveMemberDetails
   * @return haveMemberDetails
  **/
  @ApiModelProperty(value = "")
  public Boolean isHaveMemberDetails() {
    return haveMemberDetails;
  }

  public void setHaveMemberDetails(Boolean haveMemberDetails) {
    this.haveMemberDetails = haveMemberDetails;
  }

  public BusinessRoleDetectItemNode id(Long id) {
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

  public BusinessRoleDetectItemNode membershipProcessTime(Long membershipProcessTime) {
    this.membershipProcessTime = membershipProcessTime;
    return this;
  }

   /**
   * Get membershipProcessTime
   * @return membershipProcessTime
  **/
  @ApiModelProperty(value = "")
  public Long getMembershipProcessTime() {
    return membershipProcessTime;
  }

  public void setMembershipProcessTime(Long membershipProcessTime) {
    this.membershipProcessTime = membershipProcessTime;
  }

  public BusinessRoleDetectItemNode membershipSaveTime(Long membershipSaveTime) {
    this.membershipSaveTime = membershipSaveTime;
    return this;
  }

   /**
   * Get membershipSaveTime
   * @return membershipSaveTime
  **/
  @ApiModelProperty(value = "")
  public Long getMembershipSaveTime() {
    return membershipSaveTime;
  }

  public void setMembershipSaveTime(Long membershipSaveTime) {
    this.membershipSaveTime = membershipSaveTime;
  }

  public BusinessRoleDetectItemNode newAuthCount(Long newAuthCount) {
    this.newAuthCount = newAuthCount;
    return this;
  }

   /**
   * Get newAuthCount
   * @return newAuthCount
  **/
  @ApiModelProperty(value = "")
  public Long getNewAuthCount() {
    return newAuthCount;
  }

  public void setNewAuthCount(Long newAuthCount) {
    this.newAuthCount = newAuthCount;
  }

  public BusinessRoleDetectItemNode newMemberCount(Long newMemberCount) {
    this.newMemberCount = newMemberCount;
    return this;
  }

   /**
   * Get newMemberCount
   * @return newMemberCount
  **/
  @ApiModelProperty(value = "")
  public Long getNewMemberCount() {
    return newMemberCount;
  }

  public void setNewMemberCount(Long newMemberCount) {
    this.newMemberCount = newMemberCount;
  }

  public BusinessRoleDetectItemNode startDate(Long startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * Get startDate
   * @return startDate
  **/
  @ApiModelProperty(value = "")
  public Long getStartDate() {
    return startDate;
  }

  public void setStartDate(Long startDate) {
    this.startDate = startDate;
  }

  public BusinessRoleDetectItemNode status(StatusEnum status) {
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

  public BusinessRoleDetectItemNode totalAuthCount(Long totalAuthCount) {
    this.totalAuthCount = totalAuthCount;
    return this;
  }

   /**
   * Get totalAuthCount
   * @return totalAuthCount
  **/
  @ApiModelProperty(value = "")
  public Long getTotalAuthCount() {
    return totalAuthCount;
  }

  public void setTotalAuthCount(Long totalAuthCount) {
    this.totalAuthCount = totalAuthCount;
  }

  public BusinessRoleDetectItemNode totalMemberCount(Long totalMemberCount) {
    this.totalMemberCount = totalMemberCount;
    return this;
  }

   /**
   * Get totalMemberCount
   * @return totalMemberCount
  **/
  @ApiModelProperty(value = "")
  public Long getTotalMemberCount() {
    return totalMemberCount;
  }

  public void setTotalMemberCount(Long totalMemberCount) {
    this.totalMemberCount = totalMemberCount;
  }

  public BusinessRoleDetectItemNode uniqueBusinessRoleId(String uniqueBusinessRoleId) {
    this.uniqueBusinessRoleId = uniqueBusinessRoleId;
    return this;
  }

   /**
   * Get uniqueBusinessRoleId
   * @return uniqueBusinessRoleId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueBusinessRoleId() {
    return uniqueBusinessRoleId;
  }

  public void setUniqueBusinessRoleId(String uniqueBusinessRoleId) {
    this.uniqueBusinessRoleId = uniqueBusinessRoleId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BusinessRoleDetectItemNode businessRoleDetectItemNode = (BusinessRoleDetectItemNode) o;
    return Objects.equals(this.authProcessTime, businessRoleDetectItemNode.authProcessTime) &&
        Objects.equals(this.authSaveTime, businessRoleDetectItemNode.authSaveTime) &&
        Objects.equals(this.businessRoleDetectionId, businessRoleDetectItemNode.businessRoleDetectionId) &&
        Objects.equals(this.businessRoleName, businessRoleDetectItemNode.businessRoleName) &&
        Objects.equals(this.deactivation, businessRoleDetectItemNode.deactivation) &&
        Objects.equals(this.deletedAuthCount, businessRoleDetectItemNode.deletedAuthCount) &&
        Objects.equals(this.deletedMemberCount, businessRoleDetectItemNode.deletedMemberCount) &&
        Objects.equals(this.detectSequence, businessRoleDetectItemNode.detectSequence) &&
        Objects.equals(this.elapsedTime, businessRoleDetectItemNode.elapsedTime) &&
        Objects.equals(this.endDate, businessRoleDetectItemNode.endDate) &&
        Objects.equals(this.errMessage, businessRoleDetectItemNode.errMessage) &&
        Objects.equals(this.haveAuthDetails, businessRoleDetectItemNode.haveAuthDetails) &&
        Objects.equals(this.haveMemberDetails, businessRoleDetectItemNode.haveMemberDetails) &&
        Objects.equals(this.id, businessRoleDetectItemNode.id) &&
        Objects.equals(this.membershipProcessTime, businessRoleDetectItemNode.membershipProcessTime) &&
        Objects.equals(this.membershipSaveTime, businessRoleDetectItemNode.membershipSaveTime) &&
        Objects.equals(this.newAuthCount, businessRoleDetectItemNode.newAuthCount) &&
        Objects.equals(this.newMemberCount, businessRoleDetectItemNode.newMemberCount) &&
        Objects.equals(this.startDate, businessRoleDetectItemNode.startDate) &&
        Objects.equals(this.status, businessRoleDetectItemNode.status) &&
        Objects.equals(this.totalAuthCount, businessRoleDetectItemNode.totalAuthCount) &&
        Objects.equals(this.totalMemberCount, businessRoleDetectItemNode.totalMemberCount) &&
        Objects.equals(this.uniqueBusinessRoleId, businessRoleDetectItemNode.uniqueBusinessRoleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authProcessTime, authSaveTime, businessRoleDetectionId, businessRoleName, deactivation, deletedAuthCount, deletedMemberCount, detectSequence, elapsedTime, endDate, errMessage, haveAuthDetails, haveMemberDetails, id, membershipProcessTime, membershipSaveTime, newAuthCount, newMemberCount, startDate, status, totalAuthCount, totalMemberCount, uniqueBusinessRoleId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BusinessRoleDetectItemNode {\n");
    
    sb.append("    authProcessTime: ").append(toIndentedString(authProcessTime)).append("\n");
    sb.append("    authSaveTime: ").append(toIndentedString(authSaveTime)).append("\n");
    sb.append("    businessRoleDetectionId: ").append(toIndentedString(businessRoleDetectionId)).append("\n");
    sb.append("    businessRoleName: ").append(toIndentedString(businessRoleName)).append("\n");
    sb.append("    deactivation: ").append(toIndentedString(deactivation)).append("\n");
    sb.append("    deletedAuthCount: ").append(toIndentedString(deletedAuthCount)).append("\n");
    sb.append("    deletedMemberCount: ").append(toIndentedString(deletedMemberCount)).append("\n");
    sb.append("    detectSequence: ").append(toIndentedString(detectSequence)).append("\n");
    sb.append("    elapsedTime: ").append(toIndentedString(elapsedTime)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    errMessage: ").append(toIndentedString(errMessage)).append("\n");
    sb.append("    haveAuthDetails: ").append(toIndentedString(haveAuthDetails)).append("\n");
    sb.append("    haveMemberDetails: ").append(toIndentedString(haveMemberDetails)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    membershipProcessTime: ").append(toIndentedString(membershipProcessTime)).append("\n");
    sb.append("    membershipSaveTime: ").append(toIndentedString(membershipSaveTime)).append("\n");
    sb.append("    newAuthCount: ").append(toIndentedString(newAuthCount)).append("\n");
    sb.append("    newMemberCount: ").append(toIndentedString(newMemberCount)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    totalAuthCount: ").append(toIndentedString(totalAuthCount)).append("\n");
    sb.append("    totalMemberCount: ").append(toIndentedString(totalMemberCount)).append("\n");
    sb.append("    uniqueBusinessRoleId: ").append(toIndentedString(uniqueBusinessRoleId)).append("\n");
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
