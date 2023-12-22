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
 * InconsistencyDetectionNode
 */



public class InconsistencyDetectionNode {
  @SerializedName("autoGrant")
  private Boolean autoGrant = null;

  @SerializedName("canceledByUserId")
  private String canceledByUserId = null;

  @SerializedName("canceledByUserName")
  private String canceledByUserName = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("elapsedTime")
  private Long elapsedTime = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("errMessage")
  private String errMessage = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("inconsistencyCount")
  private Long inconsistencyCount = null;

  @SerializedName("prevMemWindowDays")
  private Integer prevMemWindowDays = null;

  /**
   * Gets or Sets resourceType
   */
  @JsonAdapter(ResourceTypeEnum.Adapter.class)
  public enum ResourceTypeEnum {
    @SerializedName("TECHNICAL_ROLE")
    TECHNICAL_ROLE("TECHNICAL_ROLE"),
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION");

    private String value;

    ResourceTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ResourceTypeEnum fromValue(String input) {
      for (ResourceTypeEnum b : ResourceTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ResourceTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ResourceTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ResourceTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ResourceTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("resourceType")
  private ResourceTypeEnum resourceType = null;

  @SerializedName("startDate")
  private Long startDate = null;

  @SerializedName("startedByUserId")
  private String startedByUserId = null;

  @SerializedName("startedByUserName")
  private String startedByUserName = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("RUNNING")
    RUNNING("RUNNING"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("CANCELED")
    CANCELED("CANCELED"),
    @SerializedName("CANCELING")
    CANCELING("CANCELING"),
    @SerializedName("FAILED")
    FAILED("FAILED"),
    @SerializedName("PARTIALLY_COMPLETED")
    PARTIALLY_COMPLETED("PARTIALLY_COMPLETED");

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

  public InconsistencyDetectionNode autoGrant(Boolean autoGrant) {
    this.autoGrant = autoGrant;
    return this;
  }

   /**
   * Get autoGrant
   * @return autoGrant
  **/
  @ApiModelProperty(value = "")
  public Boolean isAutoGrant() {
    return autoGrant;
  }

  public void setAutoGrant(Boolean autoGrant) {
    this.autoGrant = autoGrant;
  }

  public InconsistencyDetectionNode canceledByUserId(String canceledByUserId) {
    this.canceledByUserId = canceledByUserId;
    return this;
  }

   /**
   * Get canceledByUserId
   * @return canceledByUserId
  **/
  @ApiModelProperty(value = "")
  public String getCanceledByUserId() {
    return canceledByUserId;
  }

  public void setCanceledByUserId(String canceledByUserId) {
    this.canceledByUserId = canceledByUserId;
  }

  public InconsistencyDetectionNode canceledByUserName(String canceledByUserName) {
    this.canceledByUserName = canceledByUserName;
    return this;
  }

   /**
   * Get canceledByUserName
   * @return canceledByUserName
  **/
  @ApiModelProperty(value = "")
  public String getCanceledByUserName() {
    return canceledByUserName;
  }

  public void setCanceledByUserName(String canceledByUserName) {
    this.canceledByUserName = canceledByUserName;
  }

  public InconsistencyDetectionNode deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Get deleted
   * @return deleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public InconsistencyDetectionNode elapsedTime(Long elapsedTime) {
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

  public InconsistencyDetectionNode endDate(Long endDate) {
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

  public InconsistencyDetectionNode errMessage(String errMessage) {
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

  public InconsistencyDetectionNode id(Long id) {
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

  public InconsistencyDetectionNode inconsistencyCount(Long inconsistencyCount) {
    this.inconsistencyCount = inconsistencyCount;
    return this;
  }

   /**
   * Get inconsistencyCount
   * @return inconsistencyCount
  **/
  @ApiModelProperty(value = "")
  public Long getInconsistencyCount() {
    return inconsistencyCount;
  }

  public void setInconsistencyCount(Long inconsistencyCount) {
    this.inconsistencyCount = inconsistencyCount;
  }

  public InconsistencyDetectionNode prevMemWindowDays(Integer prevMemWindowDays) {
    this.prevMemWindowDays = prevMemWindowDays;
    return this;
  }

   /**
   * Get prevMemWindowDays
   * @return prevMemWindowDays
  **/
  @ApiModelProperty(value = "")
  public Integer getPrevMemWindowDays() {
    return prevMemWindowDays;
  }

  public void setPrevMemWindowDays(Integer prevMemWindowDays) {
    this.prevMemWindowDays = prevMemWindowDays;
  }

  public InconsistencyDetectionNode resourceType(ResourceTypeEnum resourceType) {
    this.resourceType = resourceType;
    return this;
  }

   /**
   * Get resourceType
   * @return resourceType
  **/
  @ApiModelProperty(value = "")
  public ResourceTypeEnum getResourceType() {
    return resourceType;
  }

  public void setResourceType(ResourceTypeEnum resourceType) {
    this.resourceType = resourceType;
  }

  public InconsistencyDetectionNode startDate(Long startDate) {
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

  public InconsistencyDetectionNode startedByUserId(String startedByUserId) {
    this.startedByUserId = startedByUserId;
    return this;
  }

   /**
   * Get startedByUserId
   * @return startedByUserId
  **/
  @ApiModelProperty(value = "")
  public String getStartedByUserId() {
    return startedByUserId;
  }

  public void setStartedByUserId(String startedByUserId) {
    this.startedByUserId = startedByUserId;
  }

  public InconsistencyDetectionNode startedByUserName(String startedByUserName) {
    this.startedByUserName = startedByUserName;
    return this;
  }

   /**
   * Get startedByUserName
   * @return startedByUserName
  **/
  @ApiModelProperty(value = "")
  public String getStartedByUserName() {
    return startedByUserName;
  }

  public void setStartedByUserName(String startedByUserName) {
    this.startedByUserName = startedByUserName;
  }

  public InconsistencyDetectionNode status(StatusEnum status) {
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
    InconsistencyDetectionNode inconsistencyDetectionNode = (InconsistencyDetectionNode) o;
    return Objects.equals(this.autoGrant, inconsistencyDetectionNode.autoGrant) &&
        Objects.equals(this.canceledByUserId, inconsistencyDetectionNode.canceledByUserId) &&
        Objects.equals(this.canceledByUserName, inconsistencyDetectionNode.canceledByUserName) &&
        Objects.equals(this.deleted, inconsistencyDetectionNode.deleted) &&
        Objects.equals(this.elapsedTime, inconsistencyDetectionNode.elapsedTime) &&
        Objects.equals(this.endDate, inconsistencyDetectionNode.endDate) &&
        Objects.equals(this.errMessage, inconsistencyDetectionNode.errMessage) &&
        Objects.equals(this.id, inconsistencyDetectionNode.id) &&
        Objects.equals(this.inconsistencyCount, inconsistencyDetectionNode.inconsistencyCount) &&
        Objects.equals(this.prevMemWindowDays, inconsistencyDetectionNode.prevMemWindowDays) &&
        Objects.equals(this.resourceType, inconsistencyDetectionNode.resourceType) &&
        Objects.equals(this.startDate, inconsistencyDetectionNode.startDate) &&
        Objects.equals(this.startedByUserId, inconsistencyDetectionNode.startedByUserId) &&
        Objects.equals(this.startedByUserName, inconsistencyDetectionNode.startedByUserName) &&
        Objects.equals(this.status, inconsistencyDetectionNode.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autoGrant, canceledByUserId, canceledByUserName, deleted, elapsedTime, endDate, errMessage, id, inconsistencyCount, prevMemWindowDays, resourceType, startDate, startedByUserId, startedByUserName, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InconsistencyDetectionNode {\n");
    
    sb.append("    autoGrant: ").append(toIndentedString(autoGrant)).append("\n");
    sb.append("    canceledByUserId: ").append(toIndentedString(canceledByUserId)).append("\n");
    sb.append("    canceledByUserName: ").append(toIndentedString(canceledByUserName)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    elapsedTime: ").append(toIndentedString(elapsedTime)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    errMessage: ").append(toIndentedString(errMessage)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    inconsistencyCount: ").append(toIndentedString(inconsistencyCount)).append("\n");
    sb.append("    prevMemWindowDays: ").append(toIndentedString(prevMemWindowDays)).append("\n");
    sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    startedByUserId: ").append(toIndentedString(startedByUserId)).append("\n");
    sb.append("    startedByUserName: ").append(toIndentedString(startedByUserName)).append("\n");
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
