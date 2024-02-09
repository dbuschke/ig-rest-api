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
 * ArchivalTable
 */



public class ArchivalTable {
  @SerializedName("deleteReadBatches")
  private Long deleteReadBatches = null;

  @SerializedName("deleteReadTime")
  private Long deleteReadTime = null;

  @SerializedName("deleteWriteBatches")
  private Long deleteWriteBatches = null;

  @SerializedName("deleteWriteTime")
  private Long deleteWriteTime = null;

  @SerializedName("deletesArchived")
  private Boolean deletesArchived = null;

  @SerializedName("deletesDone")
  private Long deletesDone = null;

  @SerializedName("deletesElapsedTime")
  private Long deletesElapsedTime = null;

  @SerializedName("deletesEndDate")
  private Long deletesEndDate = null;

  @SerializedName("deletesStartDate")
  private Long deletesStartDate = null;

  @SerializedName("deletesTodo")
  private Long deletesTodo = null;

  @SerializedName("errMessage")
  private String errMessage = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("lastDeleteId")
  private Long lastDeleteId = null;

  @SerializedName("lastUpdateId")
  private Long lastUpdateId = null;

  @SerializedName("maxDeleteId")
  private Long maxDeleteId = null;

  @SerializedName("maxUpdateId")
  private Long maxUpdateId = null;

  @SerializedName("minDeleteId")
  private Long minDeleteId = null;

  @SerializedName("minUpdateId")
  private Long minUpdateId = null;

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

  @SerializedName("tableName")
  private String tableName = null;

  @SerializedName("updateMergeTime")
  private Long updateMergeTime = null;

  @SerializedName("updateReadBatches")
  private Long updateReadBatches = null;

  @SerializedName("updateReadTime")
  private Long updateReadTime = null;

  @SerializedName("updateWriteBatches")
  private Long updateWriteBatches = null;

  @SerializedName("updateWriteTime")
  private Long updateWriteTime = null;

  @SerializedName("updatesArchived")
  private Boolean updatesArchived = null;

  @SerializedName("updatesDone")
  private Long updatesDone = null;

  @SerializedName("updatesElapsedTime")
  private Long updatesElapsedTime = null;

  @SerializedName("updatesEndDate")
  private Long updatesEndDate = null;

  @SerializedName("updatesMerged")
  private Boolean updatesMerged = null;

  @SerializedName("updatesStartDate")
  private Long updatesStartDate = null;

  @SerializedName("updatesTodo")
  private Long updatesTodo = null;

  public ArchivalTable deleteReadBatches(Long deleteReadBatches) {
    this.deleteReadBatches = deleteReadBatches;
    return this;
  }

   /**
   * Get deleteReadBatches
   * @return deleteReadBatches
  **/
  @ApiModelProperty(value = "")
  public Long getDeleteReadBatches() {
    return deleteReadBatches;
  }

  public void setDeleteReadBatches(Long deleteReadBatches) {
    this.deleteReadBatches = deleteReadBatches;
  }

  public ArchivalTable deleteReadTime(Long deleteReadTime) {
    this.deleteReadTime = deleteReadTime;
    return this;
  }

   /**
   * Get deleteReadTime
   * @return deleteReadTime
  **/
  @ApiModelProperty(value = "")
  public Long getDeleteReadTime() {
    return deleteReadTime;
  }

  public void setDeleteReadTime(Long deleteReadTime) {
    this.deleteReadTime = deleteReadTime;
  }

  public ArchivalTable deleteWriteBatches(Long deleteWriteBatches) {
    this.deleteWriteBatches = deleteWriteBatches;
    return this;
  }

   /**
   * Get deleteWriteBatches
   * @return deleteWriteBatches
  **/
  @ApiModelProperty(value = "")
  public Long getDeleteWriteBatches() {
    return deleteWriteBatches;
  }

  public void setDeleteWriteBatches(Long deleteWriteBatches) {
    this.deleteWriteBatches = deleteWriteBatches;
  }

  public ArchivalTable deleteWriteTime(Long deleteWriteTime) {
    this.deleteWriteTime = deleteWriteTime;
    return this;
  }

   /**
   * Get deleteWriteTime
   * @return deleteWriteTime
  **/
  @ApiModelProperty(value = "")
  public Long getDeleteWriteTime() {
    return deleteWriteTime;
  }

  public void setDeleteWriteTime(Long deleteWriteTime) {
    this.deleteWriteTime = deleteWriteTime;
  }

  public ArchivalTable deletesArchived(Boolean deletesArchived) {
    this.deletesArchived = deletesArchived;
    return this;
  }

   /**
   * Get deletesArchived
   * @return deletesArchived
  **/
  @ApiModelProperty(value = "")
  public Boolean isDeletesArchived() {
    return deletesArchived;
  }

  public void setDeletesArchived(Boolean deletesArchived) {
    this.deletesArchived = deletesArchived;
  }

  public ArchivalTable deletesDone(Long deletesDone) {
    this.deletesDone = deletesDone;
    return this;
  }

   /**
   * Get deletesDone
   * @return deletesDone
  **/
  @ApiModelProperty(value = "")
  public Long getDeletesDone() {
    return deletesDone;
  }

  public void setDeletesDone(Long deletesDone) {
    this.deletesDone = deletesDone;
  }

  public ArchivalTable deletesElapsedTime(Long deletesElapsedTime) {
    this.deletesElapsedTime = deletesElapsedTime;
    return this;
  }

   /**
   * Get deletesElapsedTime
   * @return deletesElapsedTime
  **/
  @ApiModelProperty(value = "")
  public Long getDeletesElapsedTime() {
    return deletesElapsedTime;
  }

  public void setDeletesElapsedTime(Long deletesElapsedTime) {
    this.deletesElapsedTime = deletesElapsedTime;
  }

  public ArchivalTable deletesEndDate(Long deletesEndDate) {
    this.deletesEndDate = deletesEndDate;
    return this;
  }

   /**
   * Get deletesEndDate
   * @return deletesEndDate
  **/
  @ApiModelProperty(value = "")
  public Long getDeletesEndDate() {
    return deletesEndDate;
  }

  public void setDeletesEndDate(Long deletesEndDate) {
    this.deletesEndDate = deletesEndDate;
  }

  public ArchivalTable deletesStartDate(Long deletesStartDate) {
    this.deletesStartDate = deletesStartDate;
    return this;
  }

   /**
   * Get deletesStartDate
   * @return deletesStartDate
  **/
  @ApiModelProperty(value = "")
  public Long getDeletesStartDate() {
    return deletesStartDate;
  }

  public void setDeletesStartDate(Long deletesStartDate) {
    this.deletesStartDate = deletesStartDate;
  }

  public ArchivalTable deletesTodo(Long deletesTodo) {
    this.deletesTodo = deletesTodo;
    return this;
  }

   /**
   * Get deletesTodo
   * @return deletesTodo
  **/
  @ApiModelProperty(value = "")
  public Long getDeletesTodo() {
    return deletesTodo;
  }

  public void setDeletesTodo(Long deletesTodo) {
    this.deletesTodo = deletesTodo;
  }

  public ArchivalTable errMessage(String errMessage) {
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

  public ArchivalTable id(Long id) {
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

  public ArchivalTable lastDeleteId(Long lastDeleteId) {
    this.lastDeleteId = lastDeleteId;
    return this;
  }

   /**
   * Get lastDeleteId
   * @return lastDeleteId
  **/
  @ApiModelProperty(value = "")
  public Long getLastDeleteId() {
    return lastDeleteId;
  }

  public void setLastDeleteId(Long lastDeleteId) {
    this.lastDeleteId = lastDeleteId;
  }

  public ArchivalTable lastUpdateId(Long lastUpdateId) {
    this.lastUpdateId = lastUpdateId;
    return this;
  }

   /**
   * Get lastUpdateId
   * @return lastUpdateId
  **/
  @ApiModelProperty(value = "")
  public Long getLastUpdateId() {
    return lastUpdateId;
  }

  public void setLastUpdateId(Long lastUpdateId) {
    this.lastUpdateId = lastUpdateId;
  }

  public ArchivalTable maxDeleteId(Long maxDeleteId) {
    this.maxDeleteId = maxDeleteId;
    return this;
  }

   /**
   * Get maxDeleteId
   * @return maxDeleteId
  **/
  @ApiModelProperty(value = "")
  public Long getMaxDeleteId() {
    return maxDeleteId;
  }

  public void setMaxDeleteId(Long maxDeleteId) {
    this.maxDeleteId = maxDeleteId;
  }

  public ArchivalTable maxUpdateId(Long maxUpdateId) {
    this.maxUpdateId = maxUpdateId;
    return this;
  }

   /**
   * Get maxUpdateId
   * @return maxUpdateId
  **/
  @ApiModelProperty(value = "")
  public Long getMaxUpdateId() {
    return maxUpdateId;
  }

  public void setMaxUpdateId(Long maxUpdateId) {
    this.maxUpdateId = maxUpdateId;
  }

  public ArchivalTable minDeleteId(Long minDeleteId) {
    this.minDeleteId = minDeleteId;
    return this;
  }

   /**
   * Get minDeleteId
   * @return minDeleteId
  **/
  @ApiModelProperty(value = "")
  public Long getMinDeleteId() {
    return minDeleteId;
  }

  public void setMinDeleteId(Long minDeleteId) {
    this.minDeleteId = minDeleteId;
  }

  public ArchivalTable minUpdateId(Long minUpdateId) {
    this.minUpdateId = minUpdateId;
    return this;
  }

   /**
   * Get minUpdateId
   * @return minUpdateId
  **/
  @ApiModelProperty(value = "")
  public Long getMinUpdateId() {
    return minUpdateId;
  }

  public void setMinUpdateId(Long minUpdateId) {
    this.minUpdateId = minUpdateId;
  }

  public ArchivalTable status(StatusEnum status) {
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

  public ArchivalTable tableName(String tableName) {
    this.tableName = tableName;
    return this;
  }

   /**
   * Get tableName
   * @return tableName
  **/
  @ApiModelProperty(value = "")
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public ArchivalTable updateMergeTime(Long updateMergeTime) {
    this.updateMergeTime = updateMergeTime;
    return this;
  }

   /**
   * Get updateMergeTime
   * @return updateMergeTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateMergeTime() {
    return updateMergeTime;
  }

  public void setUpdateMergeTime(Long updateMergeTime) {
    this.updateMergeTime = updateMergeTime;
  }

  public ArchivalTable updateReadBatches(Long updateReadBatches) {
    this.updateReadBatches = updateReadBatches;
    return this;
  }

   /**
   * Get updateReadBatches
   * @return updateReadBatches
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateReadBatches() {
    return updateReadBatches;
  }

  public void setUpdateReadBatches(Long updateReadBatches) {
    this.updateReadBatches = updateReadBatches;
  }

  public ArchivalTable updateReadTime(Long updateReadTime) {
    this.updateReadTime = updateReadTime;
    return this;
  }

   /**
   * Get updateReadTime
   * @return updateReadTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateReadTime() {
    return updateReadTime;
  }

  public void setUpdateReadTime(Long updateReadTime) {
    this.updateReadTime = updateReadTime;
  }

  public ArchivalTable updateWriteBatches(Long updateWriteBatches) {
    this.updateWriteBatches = updateWriteBatches;
    return this;
  }

   /**
   * Get updateWriteBatches
   * @return updateWriteBatches
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateWriteBatches() {
    return updateWriteBatches;
  }

  public void setUpdateWriteBatches(Long updateWriteBatches) {
    this.updateWriteBatches = updateWriteBatches;
  }

  public ArchivalTable updateWriteTime(Long updateWriteTime) {
    this.updateWriteTime = updateWriteTime;
    return this;
  }

   /**
   * Get updateWriteTime
   * @return updateWriteTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateWriteTime() {
    return updateWriteTime;
  }

  public void setUpdateWriteTime(Long updateWriteTime) {
    this.updateWriteTime = updateWriteTime;
  }

  public ArchivalTable updatesArchived(Boolean updatesArchived) {
    this.updatesArchived = updatesArchived;
    return this;
  }

   /**
   * Get updatesArchived
   * @return updatesArchived
  **/
  @ApiModelProperty(value = "")
  public Boolean isUpdatesArchived() {
    return updatesArchived;
  }

  public void setUpdatesArchived(Boolean updatesArchived) {
    this.updatesArchived = updatesArchived;
  }

  public ArchivalTable updatesDone(Long updatesDone) {
    this.updatesDone = updatesDone;
    return this;
  }

   /**
   * Get updatesDone
   * @return updatesDone
  **/
  @ApiModelProperty(value = "")
  public Long getUpdatesDone() {
    return updatesDone;
  }

  public void setUpdatesDone(Long updatesDone) {
    this.updatesDone = updatesDone;
  }

  public ArchivalTable updatesElapsedTime(Long updatesElapsedTime) {
    this.updatesElapsedTime = updatesElapsedTime;
    return this;
  }

   /**
   * Get updatesElapsedTime
   * @return updatesElapsedTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdatesElapsedTime() {
    return updatesElapsedTime;
  }

  public void setUpdatesElapsedTime(Long updatesElapsedTime) {
    this.updatesElapsedTime = updatesElapsedTime;
  }

  public ArchivalTable updatesEndDate(Long updatesEndDate) {
    this.updatesEndDate = updatesEndDate;
    return this;
  }

   /**
   * Get updatesEndDate
   * @return updatesEndDate
  **/
  @ApiModelProperty(value = "")
  public Long getUpdatesEndDate() {
    return updatesEndDate;
  }

  public void setUpdatesEndDate(Long updatesEndDate) {
    this.updatesEndDate = updatesEndDate;
  }

  public ArchivalTable updatesMerged(Boolean updatesMerged) {
    this.updatesMerged = updatesMerged;
    return this;
  }

   /**
   * Get updatesMerged
   * @return updatesMerged
  **/
  @ApiModelProperty(value = "")
  public Boolean isUpdatesMerged() {
    return updatesMerged;
  }

  public void setUpdatesMerged(Boolean updatesMerged) {
    this.updatesMerged = updatesMerged;
  }

  public ArchivalTable updatesStartDate(Long updatesStartDate) {
    this.updatesStartDate = updatesStartDate;
    return this;
  }

   /**
   * Get updatesStartDate
   * @return updatesStartDate
  **/
  @ApiModelProperty(value = "")
  public Long getUpdatesStartDate() {
    return updatesStartDate;
  }

  public void setUpdatesStartDate(Long updatesStartDate) {
    this.updatesStartDate = updatesStartDate;
  }

  public ArchivalTable updatesTodo(Long updatesTodo) {
    this.updatesTodo = updatesTodo;
    return this;
  }

   /**
   * Get updatesTodo
   * @return updatesTodo
  **/
  @ApiModelProperty(value = "")
  public Long getUpdatesTodo() {
    return updatesTodo;
  }

  public void setUpdatesTodo(Long updatesTodo) {
    this.updatesTodo = updatesTodo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArchivalTable archivalTable = (ArchivalTable) o;
    return Objects.equals(this.deleteReadBatches, archivalTable.deleteReadBatches) &&
        Objects.equals(this.deleteReadTime, archivalTable.deleteReadTime) &&
        Objects.equals(this.deleteWriteBatches, archivalTable.deleteWriteBatches) &&
        Objects.equals(this.deleteWriteTime, archivalTable.deleteWriteTime) &&
        Objects.equals(this.deletesArchived, archivalTable.deletesArchived) &&
        Objects.equals(this.deletesDone, archivalTable.deletesDone) &&
        Objects.equals(this.deletesElapsedTime, archivalTable.deletesElapsedTime) &&
        Objects.equals(this.deletesEndDate, archivalTable.deletesEndDate) &&
        Objects.equals(this.deletesStartDate, archivalTable.deletesStartDate) &&
        Objects.equals(this.deletesTodo, archivalTable.deletesTodo) &&
        Objects.equals(this.errMessage, archivalTable.errMessage) &&
        Objects.equals(this.id, archivalTable.id) &&
        Objects.equals(this.lastDeleteId, archivalTable.lastDeleteId) &&
        Objects.equals(this.lastUpdateId, archivalTable.lastUpdateId) &&
        Objects.equals(this.maxDeleteId, archivalTable.maxDeleteId) &&
        Objects.equals(this.maxUpdateId, archivalTable.maxUpdateId) &&
        Objects.equals(this.minDeleteId, archivalTable.minDeleteId) &&
        Objects.equals(this.minUpdateId, archivalTable.minUpdateId) &&
        Objects.equals(this.status, archivalTable.status) &&
        Objects.equals(this.tableName, archivalTable.tableName) &&
        Objects.equals(this.updateMergeTime, archivalTable.updateMergeTime) &&
        Objects.equals(this.updateReadBatches, archivalTable.updateReadBatches) &&
        Objects.equals(this.updateReadTime, archivalTable.updateReadTime) &&
        Objects.equals(this.updateWriteBatches, archivalTable.updateWriteBatches) &&
        Objects.equals(this.updateWriteTime, archivalTable.updateWriteTime) &&
        Objects.equals(this.updatesArchived, archivalTable.updatesArchived) &&
        Objects.equals(this.updatesDone, archivalTable.updatesDone) &&
        Objects.equals(this.updatesElapsedTime, archivalTable.updatesElapsedTime) &&
        Objects.equals(this.updatesEndDate, archivalTable.updatesEndDate) &&
        Objects.equals(this.updatesMerged, archivalTable.updatesMerged) &&
        Objects.equals(this.updatesStartDate, archivalTable.updatesStartDate) &&
        Objects.equals(this.updatesTodo, archivalTable.updatesTodo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deleteReadBatches, deleteReadTime, deleteWriteBatches, deleteWriteTime, deletesArchived, deletesDone, deletesElapsedTime, deletesEndDate, deletesStartDate, deletesTodo, errMessage, id, lastDeleteId, lastUpdateId, maxDeleteId, maxUpdateId, minDeleteId, minUpdateId, status, tableName, updateMergeTime, updateReadBatches, updateReadTime, updateWriteBatches, updateWriteTime, updatesArchived, updatesDone, updatesElapsedTime, updatesEndDate, updatesMerged, updatesStartDate, updatesTodo);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArchivalTable {\n");
    
    sb.append("    deleteReadBatches: ").append(toIndentedString(deleteReadBatches)).append("\n");
    sb.append("    deleteReadTime: ").append(toIndentedString(deleteReadTime)).append("\n");
    sb.append("    deleteWriteBatches: ").append(toIndentedString(deleteWriteBatches)).append("\n");
    sb.append("    deleteWriteTime: ").append(toIndentedString(deleteWriteTime)).append("\n");
    sb.append("    deletesArchived: ").append(toIndentedString(deletesArchived)).append("\n");
    sb.append("    deletesDone: ").append(toIndentedString(deletesDone)).append("\n");
    sb.append("    deletesElapsedTime: ").append(toIndentedString(deletesElapsedTime)).append("\n");
    sb.append("    deletesEndDate: ").append(toIndentedString(deletesEndDate)).append("\n");
    sb.append("    deletesStartDate: ").append(toIndentedString(deletesStartDate)).append("\n");
    sb.append("    deletesTodo: ").append(toIndentedString(deletesTodo)).append("\n");
    sb.append("    errMessage: ").append(toIndentedString(errMessage)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lastDeleteId: ").append(toIndentedString(lastDeleteId)).append("\n");
    sb.append("    lastUpdateId: ").append(toIndentedString(lastUpdateId)).append("\n");
    sb.append("    maxDeleteId: ").append(toIndentedString(maxDeleteId)).append("\n");
    sb.append("    maxUpdateId: ").append(toIndentedString(maxUpdateId)).append("\n");
    sb.append("    minDeleteId: ").append(toIndentedString(minDeleteId)).append("\n");
    sb.append("    minUpdateId: ").append(toIndentedString(minUpdateId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tableName: ").append(toIndentedString(tableName)).append("\n");
    sb.append("    updateMergeTime: ").append(toIndentedString(updateMergeTime)).append("\n");
    sb.append("    updateReadBatches: ").append(toIndentedString(updateReadBatches)).append("\n");
    sb.append("    updateReadTime: ").append(toIndentedString(updateReadTime)).append("\n");
    sb.append("    updateWriteBatches: ").append(toIndentedString(updateWriteBatches)).append("\n");
    sb.append("    updateWriteTime: ").append(toIndentedString(updateWriteTime)).append("\n");
    sb.append("    updatesArchived: ").append(toIndentedString(updatesArchived)).append("\n");
    sb.append("    updatesDone: ").append(toIndentedString(updatesDone)).append("\n");
    sb.append("    updatesElapsedTime: ").append(toIndentedString(updatesElapsedTime)).append("\n");
    sb.append("    updatesEndDate: ").append(toIndentedString(updatesEndDate)).append("\n");
    sb.append("    updatesMerged: ").append(toIndentedString(updatesMerged)).append("\n");
    sb.append("    updatesStartDate: ").append(toIndentedString(updatesStartDate)).append("\n");
    sb.append("    updatesTodo: ").append(toIndentedString(updatesTodo)).append("\n");
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
