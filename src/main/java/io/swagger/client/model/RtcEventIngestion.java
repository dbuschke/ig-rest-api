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
 * RtcEventIngestion
 */



public class RtcEventIngestion {
  @SerializedName("addGroupEvents")
  private Long addGroupEvents = null;

  @SerializedName("addGrpGrpEvents")
  private Long addGrpGrpEvents = null;

  @SerializedName("addUserEvents")
  private Long addUserEvents = null;

  @SerializedName("deleteGroupEvents")
  private Long deleteGroupEvents = null;

  @SerializedName("deleteUserEvents")
  private Long deleteUserEvents = null;

  @SerializedName("endTime")
  private Long endTime = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("modifyGroupEvents")
  private Long modifyGroupEvents = null;

  @SerializedName("modifyGrpGrpEvents")
  private Long modifyGrpGrpEvents = null;

  @SerializedName("modifyUserEvents")
  private Long modifyUserEvents = null;

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

  public RtcEventIngestion addGroupEvents(Long addGroupEvents) {
    this.addGroupEvents = addGroupEvents;
    return this;
  }

   /**
   * Get addGroupEvents
   * @return addGroupEvents
  **/
  @ApiModelProperty(value = "")
  public Long getAddGroupEvents() {
    return addGroupEvents;
  }

  public void setAddGroupEvents(Long addGroupEvents) {
    this.addGroupEvents = addGroupEvents;
  }

  public RtcEventIngestion addGrpGrpEvents(Long addGrpGrpEvents) {
    this.addGrpGrpEvents = addGrpGrpEvents;
    return this;
  }

   /**
   * Get addGrpGrpEvents
   * @return addGrpGrpEvents
  **/
  @ApiModelProperty(value = "")
  public Long getAddGrpGrpEvents() {
    return addGrpGrpEvents;
  }

  public void setAddGrpGrpEvents(Long addGrpGrpEvents) {
    this.addGrpGrpEvents = addGrpGrpEvents;
  }

  public RtcEventIngestion addUserEvents(Long addUserEvents) {
    this.addUserEvents = addUserEvents;
    return this;
  }

   /**
   * Get addUserEvents
   * @return addUserEvents
  **/
  @ApiModelProperty(value = "")
  public Long getAddUserEvents() {
    return addUserEvents;
  }

  public void setAddUserEvents(Long addUserEvents) {
    this.addUserEvents = addUserEvents;
  }

  public RtcEventIngestion deleteGroupEvents(Long deleteGroupEvents) {
    this.deleteGroupEvents = deleteGroupEvents;
    return this;
  }

   /**
   * Get deleteGroupEvents
   * @return deleteGroupEvents
  **/
  @ApiModelProperty(value = "")
  public Long getDeleteGroupEvents() {
    return deleteGroupEvents;
  }

  public void setDeleteGroupEvents(Long deleteGroupEvents) {
    this.deleteGroupEvents = deleteGroupEvents;
  }

  public RtcEventIngestion deleteUserEvents(Long deleteUserEvents) {
    this.deleteUserEvents = deleteUserEvents;
    return this;
  }

   /**
   * Get deleteUserEvents
   * @return deleteUserEvents
  **/
  @ApiModelProperty(value = "")
  public Long getDeleteUserEvents() {
    return deleteUserEvents;
  }

  public void setDeleteUserEvents(Long deleteUserEvents) {
    this.deleteUserEvents = deleteUserEvents;
  }

  public RtcEventIngestion endTime(Long endTime) {
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

  public RtcEventIngestion id(Long id) {
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

  public RtcEventIngestion modifyGroupEvents(Long modifyGroupEvents) {
    this.modifyGroupEvents = modifyGroupEvents;
    return this;
  }

   /**
   * Get modifyGroupEvents
   * @return modifyGroupEvents
  **/
  @ApiModelProperty(value = "")
  public Long getModifyGroupEvents() {
    return modifyGroupEvents;
  }

  public void setModifyGroupEvents(Long modifyGroupEvents) {
    this.modifyGroupEvents = modifyGroupEvents;
  }

  public RtcEventIngestion modifyGrpGrpEvents(Long modifyGrpGrpEvents) {
    this.modifyGrpGrpEvents = modifyGrpGrpEvents;
    return this;
  }

   /**
   * Get modifyGrpGrpEvents
   * @return modifyGrpGrpEvents
  **/
  @ApiModelProperty(value = "")
  public Long getModifyGrpGrpEvents() {
    return modifyGrpGrpEvents;
  }

  public void setModifyGrpGrpEvents(Long modifyGrpGrpEvents) {
    this.modifyGrpGrpEvents = modifyGrpGrpEvents;
  }

  public RtcEventIngestion modifyUserEvents(Long modifyUserEvents) {
    this.modifyUserEvents = modifyUserEvents;
    return this;
  }

   /**
   * Get modifyUserEvents
   * @return modifyUserEvents
  **/
  @ApiModelProperty(value = "")
  public Long getModifyUserEvents() {
    return modifyUserEvents;
  }

  public void setModifyUserEvents(Long modifyUserEvents) {
    this.modifyUserEvents = modifyUserEvents;
  }

  public RtcEventIngestion startTime(Long startTime) {
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

  public RtcEventIngestion status(StatusEnum status) {
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
    RtcEventIngestion rtcEventIngestion = (RtcEventIngestion) o;
    return Objects.equals(this.addGroupEvents, rtcEventIngestion.addGroupEvents) &&
        Objects.equals(this.addGrpGrpEvents, rtcEventIngestion.addGrpGrpEvents) &&
        Objects.equals(this.addUserEvents, rtcEventIngestion.addUserEvents) &&
        Objects.equals(this.deleteGroupEvents, rtcEventIngestion.deleteGroupEvents) &&
        Objects.equals(this.deleteUserEvents, rtcEventIngestion.deleteUserEvents) &&
        Objects.equals(this.endTime, rtcEventIngestion.endTime) &&
        Objects.equals(this.id, rtcEventIngestion.id) &&
        Objects.equals(this.modifyGroupEvents, rtcEventIngestion.modifyGroupEvents) &&
        Objects.equals(this.modifyGrpGrpEvents, rtcEventIngestion.modifyGrpGrpEvents) &&
        Objects.equals(this.modifyUserEvents, rtcEventIngestion.modifyUserEvents) &&
        Objects.equals(this.startTime, rtcEventIngestion.startTime) &&
        Objects.equals(this.status, rtcEventIngestion.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addGroupEvents, addGrpGrpEvents, addUserEvents, deleteGroupEvents, deleteUserEvents, endTime, id, modifyGroupEvents, modifyGrpGrpEvents, modifyUserEvents, startTime, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RtcEventIngestion {\n");
    
    sb.append("    addGroupEvents: ").append(toIndentedString(addGroupEvents)).append("\n");
    sb.append("    addGrpGrpEvents: ").append(toIndentedString(addGrpGrpEvents)).append("\n");
    sb.append("    addUserEvents: ").append(toIndentedString(addUserEvents)).append("\n");
    sb.append("    deleteGroupEvents: ").append(toIndentedString(deleteGroupEvents)).append("\n");
    sb.append("    deleteUserEvents: ").append(toIndentedString(deleteUserEvents)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    modifyGroupEvents: ").append(toIndentedString(modifyGroupEvents)).append("\n");
    sb.append("    modifyGrpGrpEvents: ").append(toIndentedString(modifyGrpGrpEvents)).append("\n");
    sb.append("    modifyUserEvents: ").append(toIndentedString(modifyUserEvents)).append("\n");
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
