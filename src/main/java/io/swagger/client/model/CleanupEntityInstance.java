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
 * CleanupEntityInstance
 */



public class CleanupEntityInstance {
  @SerializedName("cleanupEntityId")
  private Long cleanupEntityId = null;

  @SerializedName("elapsedTime")
  private Long elapsedTime = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("entityId")
  private Long entityId = null;

  @SerializedName("entityId2")
  private Long entityId2 = null;

  @SerializedName("entityName")
  private String entityName = null;

  @SerializedName("entityName2")
  private String entityName2 = null;

  @SerializedName("entityName3")
  private String entityName3 = null;

  @SerializedName("entityStatus")
  private String entityStatus = null;

  @SerializedName("entityTime")
  private Long entityTime = null;

  @SerializedName("entityType")
  private String entityType = null;

  @SerializedName("errMessage")
  private String errMessage = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("startDate")
  private Long startDate = null;

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

  @SerializedName("tableCount")
  private Long tableCount = null;

  public CleanupEntityInstance cleanupEntityId(Long cleanupEntityId) {
    this.cleanupEntityId = cleanupEntityId;
    return this;
  }

   /**
   * Get cleanupEntityId
   * @return cleanupEntityId
  **/
  @ApiModelProperty(value = "")
  public Long getCleanupEntityId() {
    return cleanupEntityId;
  }

  public void setCleanupEntityId(Long cleanupEntityId) {
    this.cleanupEntityId = cleanupEntityId;
  }

  public CleanupEntityInstance elapsedTime(Long elapsedTime) {
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

  public CleanupEntityInstance endDate(Long endDate) {
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

  public CleanupEntityInstance entityId(Long entityId) {
    this.entityId = entityId;
    return this;
  }

   /**
   * Get entityId
   * @return entityId
  **/
  @ApiModelProperty(value = "")
  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  public CleanupEntityInstance entityId2(Long entityId2) {
    this.entityId2 = entityId2;
    return this;
  }

   /**
   * Get entityId2
   * @return entityId2
  **/
  @ApiModelProperty(value = "")
  public Long getEntityId2() {
    return entityId2;
  }

  public void setEntityId2(Long entityId2) {
    this.entityId2 = entityId2;
  }

  public CleanupEntityInstance entityName(String entityName) {
    this.entityName = entityName;
    return this;
  }

   /**
   * Get entityName
   * @return entityName
  **/
  @ApiModelProperty(value = "")
  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public CleanupEntityInstance entityName2(String entityName2) {
    this.entityName2 = entityName2;
    return this;
  }

   /**
   * Get entityName2
   * @return entityName2
  **/
  @ApiModelProperty(value = "")
  public String getEntityName2() {
    return entityName2;
  }

  public void setEntityName2(String entityName2) {
    this.entityName2 = entityName2;
  }

  public CleanupEntityInstance entityName3(String entityName3) {
    this.entityName3 = entityName3;
    return this;
  }

   /**
   * Get entityName3
   * @return entityName3
  **/
  @ApiModelProperty(value = "")
  public String getEntityName3() {
    return entityName3;
  }

  public void setEntityName3(String entityName3) {
    this.entityName3 = entityName3;
  }

  public CleanupEntityInstance entityStatus(String entityStatus) {
    this.entityStatus = entityStatus;
    return this;
  }

   /**
   * Get entityStatus
   * @return entityStatus
  **/
  @ApiModelProperty(value = "")
  public String getEntityStatus() {
    return entityStatus;
  }

  public void setEntityStatus(String entityStatus) {
    this.entityStatus = entityStatus;
  }

  public CleanupEntityInstance entityTime(Long entityTime) {
    this.entityTime = entityTime;
    return this;
  }

   /**
   * Get entityTime
   * @return entityTime
  **/
  @ApiModelProperty(value = "")
  public Long getEntityTime() {
    return entityTime;
  }

  public void setEntityTime(Long entityTime) {
    this.entityTime = entityTime;
  }

  public CleanupEntityInstance entityType(String entityType) {
    this.entityType = entityType;
    return this;
  }

   /**
   * Get entityType
   * @return entityType
  **/
  @ApiModelProperty(value = "")
  public String getEntityType() {
    return entityType;
  }

  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }

  public CleanupEntityInstance errMessage(String errMessage) {
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

  public CleanupEntityInstance id(Long id) {
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

  public CleanupEntityInstance startDate(Long startDate) {
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

  public CleanupEntityInstance status(StatusEnum status) {
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

  public CleanupEntityInstance tableCount(Long tableCount) {
    this.tableCount = tableCount;
    return this;
  }

   /**
   * Get tableCount
   * @return tableCount
  **/
  @ApiModelProperty(value = "")
  public Long getTableCount() {
    return tableCount;
  }

  public void setTableCount(Long tableCount) {
    this.tableCount = tableCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CleanupEntityInstance cleanupEntityInstance = (CleanupEntityInstance) o;
    return Objects.equals(this.cleanupEntityId, cleanupEntityInstance.cleanupEntityId) &&
        Objects.equals(this.elapsedTime, cleanupEntityInstance.elapsedTime) &&
        Objects.equals(this.endDate, cleanupEntityInstance.endDate) &&
        Objects.equals(this.entityId, cleanupEntityInstance.entityId) &&
        Objects.equals(this.entityId2, cleanupEntityInstance.entityId2) &&
        Objects.equals(this.entityName, cleanupEntityInstance.entityName) &&
        Objects.equals(this.entityName2, cleanupEntityInstance.entityName2) &&
        Objects.equals(this.entityName3, cleanupEntityInstance.entityName3) &&
        Objects.equals(this.entityStatus, cleanupEntityInstance.entityStatus) &&
        Objects.equals(this.entityTime, cleanupEntityInstance.entityTime) &&
        Objects.equals(this.entityType, cleanupEntityInstance.entityType) &&
        Objects.equals(this.errMessage, cleanupEntityInstance.errMessage) &&
        Objects.equals(this.id, cleanupEntityInstance.id) &&
        Objects.equals(this.startDate, cleanupEntityInstance.startDate) &&
        Objects.equals(this.status, cleanupEntityInstance.status) &&
        Objects.equals(this.tableCount, cleanupEntityInstance.tableCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cleanupEntityId, elapsedTime, endDate, entityId, entityId2, entityName, entityName2, entityName3, entityStatus, entityTime, entityType, errMessage, id, startDate, status, tableCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CleanupEntityInstance {\n");
    
    sb.append("    cleanupEntityId: ").append(toIndentedString(cleanupEntityId)).append("\n");
    sb.append("    elapsedTime: ").append(toIndentedString(elapsedTime)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    entityId: ").append(toIndentedString(entityId)).append("\n");
    sb.append("    entityId2: ").append(toIndentedString(entityId2)).append("\n");
    sb.append("    entityName: ").append(toIndentedString(entityName)).append("\n");
    sb.append("    entityName2: ").append(toIndentedString(entityName2)).append("\n");
    sb.append("    entityName3: ").append(toIndentedString(entityName3)).append("\n");
    sb.append("    entityStatus: ").append(toIndentedString(entityStatus)).append("\n");
    sb.append("    entityTime: ").append(toIndentedString(entityTime)).append("\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    errMessage: ").append(toIndentedString(errMessage)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tableCount: ").append(toIndentedString(tableCount)).append("\n");
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