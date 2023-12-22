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
 * CertPolicyCalcNode
 */



public class CertPolicyCalcNode {
  @SerializedName("certPolicyId")
  private Long certPolicyId = null;

  @SerializedName("endTime")
  private Long endTime = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("message")
  private String message = null;

  /**
   * Gets or Sets runState
   */
  @JsonAdapter(RunStateEnum.Adapter.class)
  public enum RunStateEnum {
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("READY_PHASE_1")
    READY_PHASE_1("READY_PHASE_1"),
    @SerializedName("RUN_PHASE_1")
    RUN_PHASE_1("RUN_PHASE_1"),
    @SerializedName("RUNNING_PHASE_1")
    RUNNING_PHASE_1("RUNNING_PHASE_1"),
    @SerializedName("ERR_PHASE_1")
    ERR_PHASE_1("ERR_PHASE_1"),
    @SerializedName("DONE_PHASE_1")
    DONE_PHASE_1("DONE_PHASE_1"),
    @SerializedName("RUN_PHASE_2")
    RUN_PHASE_2("RUN_PHASE_2"),
    @SerializedName("RUNNING_PHASE_2")
    RUNNING_PHASE_2("RUNNING_PHASE_2"),
    @SerializedName("ERR_PHASE_2")
    ERR_PHASE_2("ERR_PHASE_2"),
    @SerializedName("DONE_PHASE_2")
    DONE_PHASE_2("DONE_PHASE_2"),
    @SerializedName("CANCEL_EXECUTING")
    CANCEL_EXECUTING("CANCEL_EXECUTING"),
    @SerializedName("CANCEL_PHASE_1")
    CANCEL_PHASE_1("CANCEL_PHASE_1"),
    @SerializedName("CANCELLING_PHASE_1")
    CANCELLING_PHASE_1("CANCELLING_PHASE_1"),
    @SerializedName("CANCEL_PHASE_2")
    CANCEL_PHASE_2("CANCEL_PHASE_2"),
    @SerializedName("CANCELLING_PHASE_2")
    CANCELLING_PHASE_2("CANCELLING_PHASE_2"),
    @SerializedName("COMPLETE")
    COMPLETE("COMPLETE"),
    @SerializedName("STOPPED")
    STOPPED("STOPPED"),
    @SerializedName("CANCEL_EXECUTION_FAILED")
    CANCEL_EXECUTION_FAILED("CANCEL_EXECUTION_FAILED"),
    @SerializedName("TERMINATE_EXECUTING")
    TERMINATE_EXECUTING("TERMINATE_EXECUTING"),
    @SerializedName("TERMINATED")
    TERMINATED("TERMINATED"),
    @SerializedName("TERMINATE_FAILED")
    TERMINATE_FAILED("TERMINATE_FAILED");

    private String value;

    RunStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RunStateEnum fromValue(String input) {
      for (RunStateEnum b : RunStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RunStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RunStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RunStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RunStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("runState")
  private RunStateEnum runState = null;

  @SerializedName("startTime")
  private Long startTime = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("RUNNING")
    RUNNING("RUNNING"),
    @SerializedName("CANCEL_REQUESTED")
    CANCEL_REQUESTED("CANCEL_REQUESTED"),
    @SerializedName("CANCEL_PENDING")
    CANCEL_PENDING("CANCEL_PENDING"),
    @SerializedName("TERMINATE_PENDING")
    TERMINATE_PENDING("TERMINATE_PENDING"),
    @SerializedName("CANCELLED")
    CANCELLED("CANCELLED"),
    @SerializedName("FAILED")
    FAILED("FAILED"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("TERMINATED")
    TERMINATED("TERMINATED"),
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("BULK_DETECTION")
    BULK_DETECTION("BULK_DETECTION");

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

  public CertPolicyCalcNode certPolicyId(Long certPolicyId) {
    this.certPolicyId = certPolicyId;
    return this;
  }

   /**
   * Get certPolicyId
   * @return certPolicyId
  **/
  @ApiModelProperty(value = "")
  public Long getCertPolicyId() {
    return certPolicyId;
  }

  public void setCertPolicyId(Long certPolicyId) {
    this.certPolicyId = certPolicyId;
  }

  public CertPolicyCalcNode endTime(Long endTime) {
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

  public CertPolicyCalcNode id(Long id) {
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

  public CertPolicyCalcNode message(String message) {
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

  public CertPolicyCalcNode runState(RunStateEnum runState) {
    this.runState = runState;
    return this;
  }

   /**
   * Get runState
   * @return runState
  **/
  @ApiModelProperty(value = "")
  public RunStateEnum getRunState() {
    return runState;
  }

  public void setRunState(RunStateEnum runState) {
    this.runState = runState;
  }

  public CertPolicyCalcNode startTime(Long startTime) {
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

  public CertPolicyCalcNode status(StatusEnum status) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CertPolicyCalcNode certPolicyCalcNode = (CertPolicyCalcNode) o;
    return Objects.equals(this.certPolicyId, certPolicyCalcNode.certPolicyId) &&
        Objects.equals(this.endTime, certPolicyCalcNode.endTime) &&
        Objects.equals(this.id, certPolicyCalcNode.id) &&
        Objects.equals(this.message, certPolicyCalcNode.message) &&
        Objects.equals(this.runState, certPolicyCalcNode.runState) &&
        Objects.equals(this.startTime, certPolicyCalcNode.startTime) &&
        Objects.equals(this.status, certPolicyCalcNode.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(certPolicyId, endTime, id, message, runState, startTime, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CertPolicyCalcNode {\n");
    
    sb.append("    certPolicyId: ").append(toIndentedString(certPolicyId)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
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
