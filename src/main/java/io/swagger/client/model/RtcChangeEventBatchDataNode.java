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
 * RtcChangeEventBatchDataNode
 */



public class RtcChangeEventBatchDataNode {
  @SerializedName("errMessage")
  private String errMessage = null;

  @SerializedName("eventData")
  private String eventData = null;

  /**
   * Gets or Sets eventType
   */
  @JsonAdapter(EventTypeEnum.Adapter.class)
  public enum EventTypeEnum {
    @SerializedName("ADD")
    ADD("ADD"),
    @SerializedName("DELETE")
    DELETE("DELETE"),
    @SerializedName("MODIFY")
    MODIFY("MODIFY");

    private String value;

    EventTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static EventTypeEnum fromValue(String input) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<EventTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EventTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public EventTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return EventTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("eventType")
  private EventTypeEnum eventType = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("rtcChangeEventBatchId")
  private Long rtcChangeEventBatchId = null;

  @SerializedName("rtcEventIngestionId")
  private Long rtcEventIngestionId = null;

  @SerializedName("timeReceived")
  private Long timeReceived = null;

  @SerializedName("viewName")
  private String viewName = null;

  public RtcChangeEventBatchDataNode errMessage(String errMessage) {
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

  public RtcChangeEventBatchDataNode eventData(String eventData) {
    this.eventData = eventData;
    return this;
  }

   /**
   * Get eventData
   * @return eventData
  **/
  @ApiModelProperty(value = "")
  public String getEventData() {
    return eventData;
  }

  public void setEventData(String eventData) {
    this.eventData = eventData;
  }

  public RtcChangeEventBatchDataNode eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

   /**
   * Get eventType
   * @return eventType
  **/
  @ApiModelProperty(value = "")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public RtcChangeEventBatchDataNode id(Long id) {
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

  public RtcChangeEventBatchDataNode rtcChangeEventBatchId(Long rtcChangeEventBatchId) {
    this.rtcChangeEventBatchId = rtcChangeEventBatchId;
    return this;
  }

   /**
   * Get rtcChangeEventBatchId
   * @return rtcChangeEventBatchId
  **/
  @ApiModelProperty(value = "")
  public Long getRtcChangeEventBatchId() {
    return rtcChangeEventBatchId;
  }

  public void setRtcChangeEventBatchId(Long rtcChangeEventBatchId) {
    this.rtcChangeEventBatchId = rtcChangeEventBatchId;
  }

  public RtcChangeEventBatchDataNode rtcEventIngestionId(Long rtcEventIngestionId) {
    this.rtcEventIngestionId = rtcEventIngestionId;
    return this;
  }

   /**
   * Get rtcEventIngestionId
   * @return rtcEventIngestionId
  **/
  @ApiModelProperty(value = "")
  public Long getRtcEventIngestionId() {
    return rtcEventIngestionId;
  }

  public void setRtcEventIngestionId(Long rtcEventIngestionId) {
    this.rtcEventIngestionId = rtcEventIngestionId;
  }

  public RtcChangeEventBatchDataNode timeReceived(Long timeReceived) {
    this.timeReceived = timeReceived;
    return this;
  }

   /**
   * Get timeReceived
   * @return timeReceived
  **/
  @ApiModelProperty(value = "")
  public Long getTimeReceived() {
    return timeReceived;
  }

  public void setTimeReceived(Long timeReceived) {
    this.timeReceived = timeReceived;
  }

  public RtcChangeEventBatchDataNode viewName(String viewName) {
    this.viewName = viewName;
    return this;
  }

   /**
   * Get viewName
   * @return viewName
  **/
  @ApiModelProperty(value = "")
  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RtcChangeEventBatchDataNode rtcChangeEventBatchDataNode = (RtcChangeEventBatchDataNode) o;
    return Objects.equals(this.errMessage, rtcChangeEventBatchDataNode.errMessage) &&
        Objects.equals(this.eventData, rtcChangeEventBatchDataNode.eventData) &&
        Objects.equals(this.eventType, rtcChangeEventBatchDataNode.eventType) &&
        Objects.equals(this.id, rtcChangeEventBatchDataNode.id) &&
        Objects.equals(this.rtcChangeEventBatchId, rtcChangeEventBatchDataNode.rtcChangeEventBatchId) &&
        Objects.equals(this.rtcEventIngestionId, rtcChangeEventBatchDataNode.rtcEventIngestionId) &&
        Objects.equals(this.timeReceived, rtcChangeEventBatchDataNode.timeReceived) &&
        Objects.equals(this.viewName, rtcChangeEventBatchDataNode.viewName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errMessage, eventData, eventType, id, rtcChangeEventBatchId, rtcEventIngestionId, timeReceived, viewName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RtcChangeEventBatchDataNode {\n");
    
    sb.append("    errMessage: ").append(toIndentedString(errMessage)).append("\n");
    sb.append("    eventData: ").append(toIndentedString(eventData)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    rtcChangeEventBatchId: ").append(toIndentedString(rtcChangeEventBatchId)).append("\n");
    sb.append("    rtcEventIngestionId: ").append(toIndentedString(rtcEventIngestionId)).append("\n");
    sb.append("    timeReceived: ").append(toIndentedString(timeReceived)).append("\n");
    sb.append("    viewName: ").append(toIndentedString(viewName)).append("\n");
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
