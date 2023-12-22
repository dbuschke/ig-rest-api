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
import java.io.IOException;
/**
 * BusinessRoleDetectionNode
 */



public class BusinessRoleDetectionNode {
  @SerializedName("autoGrantCount")
  private Long autoGrantCount = null;

  @SerializedName("autoRequestCsetGenTime")
  private Long autoRequestCsetGenTime = null;

  @SerializedName("autoRequestGenTime")
  private Long autoRequestGenTime = null;

  @SerializedName("autoRequestStartProvTime")
  private Long autoRequestStartProvTime = null;

  @SerializedName("autoRevokeCount")
  private Long autoRevokeCount = null;

  @SerializedName("businessRoleName")
  private String businessRoleName = null;

  @SerializedName("businessRolesDone")
  private Long businessRolesDone = null;

  @SerializedName("businessRolesTodo")
  private Long businessRolesTodo = null;

  @SerializedName("detectionTriggerInfo")
  private String detectionTriggerInfo = null;

  @SerializedName("detectionTriggeredBy")
  private String detectionTriggeredBy = null;

  @SerializedName("detectionType")
  private String detectionType = null;

  @SerializedName("elapsedTime")
  private Long elapsedTime = null;

  @SerializedName("endTime")
  private Long endTime = null;

  @SerializedName("haveAutoGrantDetails")
  private Boolean haveAutoGrantDetails = null;

  @SerializedName("haveAutoRevokeDetails")
  private Boolean haveAutoRevokeDetails = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("message")
  private String message = null;

  @SerializedName("runState")
  private String runState = null;

  @SerializedName("startTime")
  private Long startTime = null;

  @SerializedName("status")
  private String status = null;

  public BusinessRoleDetectionNode autoGrantCount(Long autoGrantCount) {
    this.autoGrantCount = autoGrantCount;
    return this;
  }

   /**
   * Get autoGrantCount
   * @return autoGrantCount
  **/
  @ApiModelProperty(value = "")
  public Long getAutoGrantCount() {
    return autoGrantCount;
  }

  public void setAutoGrantCount(Long autoGrantCount) {
    this.autoGrantCount = autoGrantCount;
  }

  public BusinessRoleDetectionNode autoRequestCsetGenTime(Long autoRequestCsetGenTime) {
    this.autoRequestCsetGenTime = autoRequestCsetGenTime;
    return this;
  }

   /**
   * Get autoRequestCsetGenTime
   * @return autoRequestCsetGenTime
  **/
  @ApiModelProperty(value = "")
  public Long getAutoRequestCsetGenTime() {
    return autoRequestCsetGenTime;
  }

  public void setAutoRequestCsetGenTime(Long autoRequestCsetGenTime) {
    this.autoRequestCsetGenTime = autoRequestCsetGenTime;
  }

  public BusinessRoleDetectionNode autoRequestGenTime(Long autoRequestGenTime) {
    this.autoRequestGenTime = autoRequestGenTime;
    return this;
  }

   /**
   * Get autoRequestGenTime
   * @return autoRequestGenTime
  **/
  @ApiModelProperty(value = "")
  public Long getAutoRequestGenTime() {
    return autoRequestGenTime;
  }

  public void setAutoRequestGenTime(Long autoRequestGenTime) {
    this.autoRequestGenTime = autoRequestGenTime;
  }

  public BusinessRoleDetectionNode autoRequestStartProvTime(Long autoRequestStartProvTime) {
    this.autoRequestStartProvTime = autoRequestStartProvTime;
    return this;
  }

   /**
   * Get autoRequestStartProvTime
   * @return autoRequestStartProvTime
  **/
  @ApiModelProperty(value = "")
  public Long getAutoRequestStartProvTime() {
    return autoRequestStartProvTime;
  }

  public void setAutoRequestStartProvTime(Long autoRequestStartProvTime) {
    this.autoRequestStartProvTime = autoRequestStartProvTime;
  }

  public BusinessRoleDetectionNode autoRevokeCount(Long autoRevokeCount) {
    this.autoRevokeCount = autoRevokeCount;
    return this;
  }

   /**
   * Get autoRevokeCount
   * @return autoRevokeCount
  **/
  @ApiModelProperty(value = "")
  public Long getAutoRevokeCount() {
    return autoRevokeCount;
  }

  public void setAutoRevokeCount(Long autoRevokeCount) {
    this.autoRevokeCount = autoRevokeCount;
  }

  public BusinessRoleDetectionNode businessRoleName(String businessRoleName) {
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

  public BusinessRoleDetectionNode businessRolesDone(Long businessRolesDone) {
    this.businessRolesDone = businessRolesDone;
    return this;
  }

   /**
   * Get businessRolesDone
   * @return businessRolesDone
  **/
  @ApiModelProperty(value = "")
  public Long getBusinessRolesDone() {
    return businessRolesDone;
  }

  public void setBusinessRolesDone(Long businessRolesDone) {
    this.businessRolesDone = businessRolesDone;
  }

  public BusinessRoleDetectionNode businessRolesTodo(Long businessRolesTodo) {
    this.businessRolesTodo = businessRolesTodo;
    return this;
  }

   /**
   * Get businessRolesTodo
   * @return businessRolesTodo
  **/
  @ApiModelProperty(value = "")
  public Long getBusinessRolesTodo() {
    return businessRolesTodo;
  }

  public void setBusinessRolesTodo(Long businessRolesTodo) {
    this.businessRolesTodo = businessRolesTodo;
  }

  public BusinessRoleDetectionNode detectionTriggerInfo(String detectionTriggerInfo) {
    this.detectionTriggerInfo = detectionTriggerInfo;
    return this;
  }

   /**
   * Get detectionTriggerInfo
   * @return detectionTriggerInfo
  **/
  @ApiModelProperty(value = "")
  public String getDetectionTriggerInfo() {
    return detectionTriggerInfo;
  }

  public void setDetectionTriggerInfo(String detectionTriggerInfo) {
    this.detectionTriggerInfo = detectionTriggerInfo;
  }

  public BusinessRoleDetectionNode detectionTriggeredBy(String detectionTriggeredBy) {
    this.detectionTriggeredBy = detectionTriggeredBy;
    return this;
  }

   /**
   * Get detectionTriggeredBy
   * @return detectionTriggeredBy
  **/
  @ApiModelProperty(value = "")
  public String getDetectionTriggeredBy() {
    return detectionTriggeredBy;
  }

  public void setDetectionTriggeredBy(String detectionTriggeredBy) {
    this.detectionTriggeredBy = detectionTriggeredBy;
  }

  public BusinessRoleDetectionNode detectionType(String detectionType) {
    this.detectionType = detectionType;
    return this;
  }

   /**
   * Get detectionType
   * @return detectionType
  **/
  @ApiModelProperty(value = "")
  public String getDetectionType() {
    return detectionType;
  }

  public void setDetectionType(String detectionType) {
    this.detectionType = detectionType;
  }

  public BusinessRoleDetectionNode elapsedTime(Long elapsedTime) {
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

  public BusinessRoleDetectionNode endTime(Long endTime) {
    this.endTime = endTime;
    return this;
  }

   /**
   * Get endTime
   * @return endTime
  **/
  @ApiModelProperty(value = "")
  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  public BusinessRoleDetectionNode haveAutoGrantDetails(Boolean haveAutoGrantDetails) {
    this.haveAutoGrantDetails = haveAutoGrantDetails;
    return this;
  }

   /**
   * Get haveAutoGrantDetails
   * @return haveAutoGrantDetails
  **/
  @ApiModelProperty(value = "")
  public Boolean isHaveAutoGrantDetails() {
    return haveAutoGrantDetails;
  }

  public void setHaveAutoGrantDetails(Boolean haveAutoGrantDetails) {
    this.haveAutoGrantDetails = haveAutoGrantDetails;
  }

  public BusinessRoleDetectionNode haveAutoRevokeDetails(Boolean haveAutoRevokeDetails) {
    this.haveAutoRevokeDetails = haveAutoRevokeDetails;
    return this;
  }

   /**
   * Get haveAutoRevokeDetails
   * @return haveAutoRevokeDetails
  **/
  @ApiModelProperty(value = "")
  public Boolean isHaveAutoRevokeDetails() {
    return haveAutoRevokeDetails;
  }

  public void setHaveAutoRevokeDetails(Boolean haveAutoRevokeDetails) {
    this.haveAutoRevokeDetails = haveAutoRevokeDetails;
  }

  public BusinessRoleDetectionNode id(Long id) {
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

  public BusinessRoleDetectionNode message(String message) {
    this.message = message;
    return this;
  }

   /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(value = "")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public BusinessRoleDetectionNode runState(String runState) {
    this.runState = runState;
    return this;
  }

   /**
   * Get runState
   * @return runState
  **/
  @ApiModelProperty(value = "")
  public String getRunState() {
    return runState;
  }

  public void setRunState(String runState) {
    this.runState = runState;
  }

  public BusinessRoleDetectionNode startTime(Long startTime) {
    this.startTime = startTime;
    return this;
  }

   /**
   * Get startTime
   * @return startTime
  **/
  @ApiModelProperty(value = "")
  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public BusinessRoleDetectionNode status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BusinessRoleDetectionNode businessRoleDetectionNode = (BusinessRoleDetectionNode) o;
    return Objects.equals(this.autoGrantCount, businessRoleDetectionNode.autoGrantCount) &&
        Objects.equals(this.autoRequestCsetGenTime, businessRoleDetectionNode.autoRequestCsetGenTime) &&
        Objects.equals(this.autoRequestGenTime, businessRoleDetectionNode.autoRequestGenTime) &&
        Objects.equals(this.autoRequestStartProvTime, businessRoleDetectionNode.autoRequestStartProvTime) &&
        Objects.equals(this.autoRevokeCount, businessRoleDetectionNode.autoRevokeCount) &&
        Objects.equals(this.businessRoleName, businessRoleDetectionNode.businessRoleName) &&
        Objects.equals(this.businessRolesDone, businessRoleDetectionNode.businessRolesDone) &&
        Objects.equals(this.businessRolesTodo, businessRoleDetectionNode.businessRolesTodo) &&
        Objects.equals(this.detectionTriggerInfo, businessRoleDetectionNode.detectionTriggerInfo) &&
        Objects.equals(this.detectionTriggeredBy, businessRoleDetectionNode.detectionTriggeredBy) &&
        Objects.equals(this.detectionType, businessRoleDetectionNode.detectionType) &&
        Objects.equals(this.elapsedTime, businessRoleDetectionNode.elapsedTime) &&
        Objects.equals(this.endTime, businessRoleDetectionNode.endTime) &&
        Objects.equals(this.haveAutoGrantDetails, businessRoleDetectionNode.haveAutoGrantDetails) &&
        Objects.equals(this.haveAutoRevokeDetails, businessRoleDetectionNode.haveAutoRevokeDetails) &&
        Objects.equals(this.id, businessRoleDetectionNode.id) &&
        Objects.equals(this.message, businessRoleDetectionNode.message) &&
        Objects.equals(this.runState, businessRoleDetectionNode.runState) &&
        Objects.equals(this.startTime, businessRoleDetectionNode.startTime) &&
        Objects.equals(this.status, businessRoleDetectionNode.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autoGrantCount, autoRequestCsetGenTime, autoRequestGenTime, autoRequestStartProvTime, autoRevokeCount, businessRoleName, businessRolesDone, businessRolesTodo, detectionTriggerInfo, detectionTriggeredBy, detectionType, elapsedTime, endTime, haveAutoGrantDetails, haveAutoRevokeDetails, id, message, runState, startTime, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BusinessRoleDetectionNode {\n");
    
    sb.append("    autoGrantCount: ").append(toIndentedString(autoGrantCount)).append("\n");
    sb.append("    autoRequestCsetGenTime: ").append(toIndentedString(autoRequestCsetGenTime)).append("\n");
    sb.append("    autoRequestGenTime: ").append(toIndentedString(autoRequestGenTime)).append("\n");
    sb.append("    autoRequestStartProvTime: ").append(toIndentedString(autoRequestStartProvTime)).append("\n");
    sb.append("    autoRevokeCount: ").append(toIndentedString(autoRevokeCount)).append("\n");
    sb.append("    businessRoleName: ").append(toIndentedString(businessRoleName)).append("\n");
    sb.append("    businessRolesDone: ").append(toIndentedString(businessRolesDone)).append("\n");
    sb.append("    businessRolesTodo: ").append(toIndentedString(businessRolesTodo)).append("\n");
    sb.append("    detectionTriggerInfo: ").append(toIndentedString(detectionTriggerInfo)).append("\n");
    sb.append("    detectionTriggeredBy: ").append(toIndentedString(detectionTriggeredBy)).append("\n");
    sb.append("    detectionType: ").append(toIndentedString(detectionType)).append("\n");
    sb.append("    elapsedTime: ").append(toIndentedString(elapsedTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    haveAutoGrantDetails: ").append(toIndentedString(haveAutoGrantDetails)).append("\n");
    sb.append("    haveAutoRevokeDetails: ").append(toIndentedString(haveAutoRevokeDetails)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    runState: ").append(toIndentedString(runState)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
