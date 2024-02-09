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
import java.util.ArrayList;
import java.util.List;
/**
 * Archival
 */



public class Archival {
  /**
   * Gets or Sets archivalPhase
   */
  @JsonAdapter(ArchivalPhaseEnum.Adapter.class)
  public enum ArchivalPhaseEnum {
    @SerializedName("PAUSING_BETWEEN_ITERATIONS")
    PAUSING_BETWEEN_ITERATIONS("PAUSING_BETWEEN_ITERATIONS"),
    @SerializedName("UPDATING_STATISTICS")
    UPDATING_STATISTICS("UPDATING_STATISTICS"),
    @SerializedName("ESTIMATING_WORK_TODO")
    ESTIMATING_WORK_TODO("ESTIMATING_WORK_TODO"),
    @SerializedName("CLEARING_ARCHIVE_TABLES")
    CLEARING_ARCHIVE_TABLES("CLEARING_ARCHIVE_TABLES"),
    @SerializedName("ARCHIVING")
    ARCHIVING("ARCHIVING"),
    @SerializedName("WAITING_ARCHIVAL_READERS")
    WAITING_ARCHIVAL_READERS("WAITING_ARCHIVAL_READERS"),
    @SerializedName("WAITING_OPS_TRANSACTIONS")
    WAITING_OPS_TRANSACTIONS("WAITING_OPS_TRANSACTIONS"),
    @SerializedName("RECONNECTING")
    RECONNECTING("RECONNECTING"),
    @SerializedName("UPDATE_ARCHIVE_MGMT_SCHEMA")
    UPDATE_ARCHIVE_MGMT_SCHEMA("UPDATE_ARCHIVE_MGMT_SCHEMA"),
    @SerializedName("UPDATE_ARCHIVE_SCHEMA")
    UPDATE_ARCHIVE_SCHEMA("UPDATE_ARCHIVE_SCHEMA"),
    @SerializedName("RETRIEVE_ESTIMATED_WORK")
    RETRIEVE_ESTIMATED_WORK("RETRIEVE_ESTIMATED_WORK");

    private String value;

    ArchivalPhaseEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ArchivalPhaseEnum fromValue(String input) {
      for (ArchivalPhaseEnum b : ArchivalPhaseEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ArchivalPhaseEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ArchivalPhaseEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ArchivalPhaseEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ArchivalPhaseEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("archivalPhase")
  private ArchivalPhaseEnum archivalPhase = null;

  @SerializedName("archivalPhaseStartTime")
  private Long archivalPhaseStartTime = null;

  @SerializedName("archiveDestName")
  private String archiveDestName = null;

  @SerializedName("archiveReaderCount")
  private Long archiveReaderCount = null;

  @SerializedName("archiveReadersWaitTime")
  private Long archiveReadersWaitTime = null;

  /**
   * Gets or Sets archiveTask
   */
  @JsonAdapter(ArchiveTaskEnum.Adapter.class)
  public enum ArchiveTaskEnum {
    @SerializedName("ARCHIVE")
    ARCHIVE("ARCHIVE"),
    @SerializedName("DISABLE")
    DISABLE("DISABLE"),
    @SerializedName("ENABLE")
    ENABLE("ENABLE");

    private String value;

    ArchiveTaskEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ArchiveTaskEnum fromValue(String input) {
      for (ArchiveTaskEnum b : ArchiveTaskEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ArchiveTaskEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ArchiveTaskEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ArchiveTaskEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ArchiveTaskEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("archiveTask")
  private ArchiveTaskEnum archiveTask = null;

  @SerializedName("archivingTime")
  private Long archivingTime = null;

  @SerializedName("clearArchiveTablesTime")
  private Long clearArchiveTablesTime = null;

  @SerializedName("currentTableName")
  private String currentTableName = null;

  @SerializedName("deleteReadBatches")
  private Long deleteReadBatches = null;

  @SerializedName("deleteReadTime")
  private Long deleteReadTime = null;

  @SerializedName("deleteWriteBatches")
  private Long deleteWriteBatches = null;

  @SerializedName("deleteWriteTime")
  private Long deleteWriteTime = null;

  @SerializedName("deletesDone")
  private Long deletesDone = null;

  @SerializedName("deletesEndDate")
  private Long deletesEndDate = null;

  @SerializedName("deletesStartDate")
  private Long deletesStartDate = null;

  @SerializedName("deletesTodo")
  private Long deletesTodo = null;

  @SerializedName("elapsedTime")
  private Long elapsedTime = null;

  @SerializedName("endByDate")
  private Long endByDate = null;

  @SerializedName("endCalculationTime")
  private Long endCalculationTime = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("errMessage")
  private String errMessage = null;

  @SerializedName("estimateWorkTodoTime")
  private Long estimateWorkTodoTime = null;

  /**
   * Gets or Sets executionMode
   */
  @JsonAdapter(ExecutionModeEnum.Adapter.class)
  public enum ExecutionModeEnum {
    @SerializedName("NORMAL")
    NORMAL("NORMAL"),
    @SerializedName("ENTERING_MAINTENANCE")
    ENTERING_MAINTENANCE("ENTERING_MAINTENANCE"),
    @SerializedName("MAINTENANCE_COPY")
    MAINTENANCE_COPY("MAINTENANCE_COPY"),
    @SerializedName("MAINTENANCE_PURGE")
    MAINTENANCE_PURGE("MAINTENANCE_PURGE"),
    @SerializedName("MAINTENANCE_PURGE_NO_LOCKOUT")
    MAINTENANCE_PURGE_NO_LOCKOUT("MAINTENANCE_PURGE_NO_LOCKOUT"),
    @SerializedName("MAINTENANCE_COPY_CONCURRENT")
    MAINTENANCE_COPY_CONCURRENT("MAINTENANCE_COPY_CONCURRENT");

    private String value;

    ExecutionModeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ExecutionModeEnum fromValue(String input) {
      for (ExecutionModeEnum b : ExecutionModeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ExecutionModeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ExecutionModeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ExecutionModeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ExecutionModeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("executionMode")
  private ExecutionModeEnum executionMode = null;

  @SerializedName("finalizeEndTime")
  private Long finalizeEndTime = null;

  @SerializedName("finalizeTime")
  private Long finalizeTime = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("iterationCount")
  private Long iterationCount = null;

  @SerializedName("maintSchedName")
  private String maintSchedName = null;

  @SerializedName("opsTransWaitCount")
  private Long opsTransWaitCount = null;

  @SerializedName("opsTransWaitTime")
  private Long opsTransWaitTime = null;

  @SerializedName("opsTransactions")
  private List<String> opsTransactions = null;

  @SerializedName("pauseBetweenIterations")
  private Long pauseBetweenIterations = null;

  @SerializedName("pausedTime")
  private Long pausedTime = null;

  @SerializedName("processingTableName")
  private String processingTableName = null;

  @SerializedName("reconnectTime")
  private Long reconnectTime = null;

  @SerializedName("resumeArchive")
  private Boolean resumeArchive = null;

  @SerializedName("resumedByUserId")
  private String resumedByUserId = null;

  @SerializedName("resumedByUserName")
  private String resumedByUserName = null;

  @SerializedName("resumedDate")
  private Long resumedDate = null;

  @SerializedName("retrieveEstimatedWorkTime")
  private Long retrieveEstimatedWorkTime = null;

  @SerializedName("startCalculationTime")
  private Long startCalculationTime = null;

  @SerializedName("startDate")
  private Long startDate = null;

  @SerializedName("startedByUserId")
  private String startedByUserId = null;

  @SerializedName("startedByUserName")
  private String startedByUserName = null;

  @SerializedName("statsUpdateTime")
  private Long statsUpdateTime = null;

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

  @SerializedName("tableDeletesDone")
  private Long tableDeletesDone = null;

  @SerializedName("tableDeletesTodo")
  private Long tableDeletesTodo = null;

  @SerializedName("tableUpdatesDone")
  private Long tableUpdatesDone = null;

  @SerializedName("tableUpdatesTodo")
  private Long tableUpdatesTodo = null;

  @SerializedName("tablesDone")
  private Long tablesDone = null;

  @SerializedName("tablesTodo")
  private Long tablesTodo = null;

  @SerializedName("updateExtMgmtSchemaTime")
  private Long updateExtMgmtSchemaTime = null;

  @SerializedName("updateExternalSchemaTime")
  private Long updateExternalSchemaTime = null;

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

  @SerializedName("updatesDone")
  private Long updatesDone = null;

  @SerializedName("updatesEndDate")
  private Long updatesEndDate = null;

  @SerializedName("updatesStartDate")
  private Long updatesStartDate = null;

  @SerializedName("updatesTodo")
  private Long updatesTodo = null;

  public Archival archivalPhase(ArchivalPhaseEnum archivalPhase) {
    this.archivalPhase = archivalPhase;
    return this;
  }

   /**
   * Get archivalPhase
   * @return archivalPhase
  **/
  @ApiModelProperty(value = "")
  public ArchivalPhaseEnum getArchivalPhase() {
    return archivalPhase;
  }

  public void setArchivalPhase(ArchivalPhaseEnum archivalPhase) {
    this.archivalPhase = archivalPhase;
  }

  public Archival archivalPhaseStartTime(Long archivalPhaseStartTime) {
    this.archivalPhaseStartTime = archivalPhaseStartTime;
    return this;
  }

   /**
   * Get archivalPhaseStartTime
   * @return archivalPhaseStartTime
  **/
  @ApiModelProperty(value = "")
  public Long getArchivalPhaseStartTime() {
    return archivalPhaseStartTime;
  }

  public void setArchivalPhaseStartTime(Long archivalPhaseStartTime) {
    this.archivalPhaseStartTime = archivalPhaseStartTime;
  }

  public Archival archiveDestName(String archiveDestName) {
    this.archiveDestName = archiveDestName;
    return this;
  }

   /**
   * Get archiveDestName
   * @return archiveDestName
  **/
  @ApiModelProperty(value = "")
  public String getArchiveDestName() {
    return archiveDestName;
  }

  public void setArchiveDestName(String archiveDestName) {
    this.archiveDestName = archiveDestName;
  }

  public Archival archiveReaderCount(Long archiveReaderCount) {
    this.archiveReaderCount = archiveReaderCount;
    return this;
  }

   /**
   * Get archiveReaderCount
   * @return archiveReaderCount
  **/
  @ApiModelProperty(value = "")
  public Long getArchiveReaderCount() {
    return archiveReaderCount;
  }

  public void setArchiveReaderCount(Long archiveReaderCount) {
    this.archiveReaderCount = archiveReaderCount;
  }

  public Archival archiveReadersWaitTime(Long archiveReadersWaitTime) {
    this.archiveReadersWaitTime = archiveReadersWaitTime;
    return this;
  }

   /**
   * Get archiveReadersWaitTime
   * @return archiveReadersWaitTime
  **/
  @ApiModelProperty(value = "")
  public Long getArchiveReadersWaitTime() {
    return archiveReadersWaitTime;
  }

  public void setArchiveReadersWaitTime(Long archiveReadersWaitTime) {
    this.archiveReadersWaitTime = archiveReadersWaitTime;
  }

  public Archival archiveTask(ArchiveTaskEnum archiveTask) {
    this.archiveTask = archiveTask;
    return this;
  }

   /**
   * Get archiveTask
   * @return archiveTask
  **/
  @ApiModelProperty(value = "")
  public ArchiveTaskEnum getArchiveTask() {
    return archiveTask;
  }

  public void setArchiveTask(ArchiveTaskEnum archiveTask) {
    this.archiveTask = archiveTask;
  }

  public Archival archivingTime(Long archivingTime) {
    this.archivingTime = archivingTime;
    return this;
  }

   /**
   * Get archivingTime
   * @return archivingTime
  **/
  @ApiModelProperty(value = "")
  public Long getArchivingTime() {
    return archivingTime;
  }

  public void setArchivingTime(Long archivingTime) {
    this.archivingTime = archivingTime;
  }

  public Archival clearArchiveTablesTime(Long clearArchiveTablesTime) {
    this.clearArchiveTablesTime = clearArchiveTablesTime;
    return this;
  }

   /**
   * Get clearArchiveTablesTime
   * @return clearArchiveTablesTime
  **/
  @ApiModelProperty(value = "")
  public Long getClearArchiveTablesTime() {
    return clearArchiveTablesTime;
  }

  public void setClearArchiveTablesTime(Long clearArchiveTablesTime) {
    this.clearArchiveTablesTime = clearArchiveTablesTime;
  }

  public Archival currentTableName(String currentTableName) {
    this.currentTableName = currentTableName;
    return this;
  }

   /**
   * Get currentTableName
   * @return currentTableName
  **/
  @ApiModelProperty(value = "")
  public String getCurrentTableName() {
    return currentTableName;
  }

  public void setCurrentTableName(String currentTableName) {
    this.currentTableName = currentTableName;
  }

  public Archival deleteReadBatches(Long deleteReadBatches) {
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

  public Archival deleteReadTime(Long deleteReadTime) {
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

  public Archival deleteWriteBatches(Long deleteWriteBatches) {
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

  public Archival deleteWriteTime(Long deleteWriteTime) {
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

  public Archival deletesDone(Long deletesDone) {
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

  public Archival deletesEndDate(Long deletesEndDate) {
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

  public Archival deletesStartDate(Long deletesStartDate) {
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

  public Archival deletesTodo(Long deletesTodo) {
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

  public Archival elapsedTime(Long elapsedTime) {
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

  public Archival endByDate(Long endByDate) {
    this.endByDate = endByDate;
    return this;
  }

   /**
   * Get endByDate
   * @return endByDate
  **/
  @ApiModelProperty(value = "")
  public Long getEndByDate() {
    return endByDate;
  }

  public void setEndByDate(Long endByDate) {
    this.endByDate = endByDate;
  }

  public Archival endCalculationTime(Long endCalculationTime) {
    this.endCalculationTime = endCalculationTime;
    return this;
  }

   /**
   * Get endCalculationTime
   * @return endCalculationTime
  **/
  @ApiModelProperty(value = "")
  public Long getEndCalculationTime() {
    return endCalculationTime;
  }

  public void setEndCalculationTime(Long endCalculationTime) {
    this.endCalculationTime = endCalculationTime;
  }

  public Archival endDate(Long endDate) {
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

  public Archival errMessage(String errMessage) {
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

  public Archival estimateWorkTodoTime(Long estimateWorkTodoTime) {
    this.estimateWorkTodoTime = estimateWorkTodoTime;
    return this;
  }

   /**
   * Get estimateWorkTodoTime
   * @return estimateWorkTodoTime
  **/
  @ApiModelProperty(value = "")
  public Long getEstimateWorkTodoTime() {
    return estimateWorkTodoTime;
  }

  public void setEstimateWorkTodoTime(Long estimateWorkTodoTime) {
    this.estimateWorkTodoTime = estimateWorkTodoTime;
  }

  public Archival executionMode(ExecutionModeEnum executionMode) {
    this.executionMode = executionMode;
    return this;
  }

   /**
   * Get executionMode
   * @return executionMode
  **/
  @ApiModelProperty(value = "")
  public ExecutionModeEnum getExecutionMode() {
    return executionMode;
  }

  public void setExecutionMode(ExecutionModeEnum executionMode) {
    this.executionMode = executionMode;
  }

  public Archival finalizeEndTime(Long finalizeEndTime) {
    this.finalizeEndTime = finalizeEndTime;
    return this;
  }

   /**
   * Get finalizeEndTime
   * @return finalizeEndTime
  **/
  @ApiModelProperty(value = "")
  public Long getFinalizeEndTime() {
    return finalizeEndTime;
  }

  public void setFinalizeEndTime(Long finalizeEndTime) {
    this.finalizeEndTime = finalizeEndTime;
  }

  public Archival finalizeTime(Long finalizeTime) {
    this.finalizeTime = finalizeTime;
    return this;
  }

   /**
   * Get finalizeTime
   * @return finalizeTime
  **/
  @ApiModelProperty(value = "")
  public Long getFinalizeTime() {
    return finalizeTime;
  }

  public void setFinalizeTime(Long finalizeTime) {
    this.finalizeTime = finalizeTime;
  }

  public Archival id(Long id) {
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

  public Archival iterationCount(Long iterationCount) {
    this.iterationCount = iterationCount;
    return this;
  }

   /**
   * Get iterationCount
   * @return iterationCount
  **/
  @ApiModelProperty(value = "")
  public Long getIterationCount() {
    return iterationCount;
  }

  public void setIterationCount(Long iterationCount) {
    this.iterationCount = iterationCount;
  }

  public Archival maintSchedName(String maintSchedName) {
    this.maintSchedName = maintSchedName;
    return this;
  }

   /**
   * Get maintSchedName
   * @return maintSchedName
  **/
  @ApiModelProperty(value = "")
  public String getMaintSchedName() {
    return maintSchedName;
  }

  public void setMaintSchedName(String maintSchedName) {
    this.maintSchedName = maintSchedName;
  }

  public Archival opsTransWaitCount(Long opsTransWaitCount) {
    this.opsTransWaitCount = opsTransWaitCount;
    return this;
  }

   /**
   * Get opsTransWaitCount
   * @return opsTransWaitCount
  **/
  @ApiModelProperty(value = "")
  public Long getOpsTransWaitCount() {
    return opsTransWaitCount;
  }

  public void setOpsTransWaitCount(Long opsTransWaitCount) {
    this.opsTransWaitCount = opsTransWaitCount;
  }

  public Archival opsTransWaitTime(Long opsTransWaitTime) {
    this.opsTransWaitTime = opsTransWaitTime;
    return this;
  }

   /**
   * Get opsTransWaitTime
   * @return opsTransWaitTime
  **/
  @ApiModelProperty(value = "")
  public Long getOpsTransWaitTime() {
    return opsTransWaitTime;
  }

  public void setOpsTransWaitTime(Long opsTransWaitTime) {
    this.opsTransWaitTime = opsTransWaitTime;
  }

  public Archival opsTransactions(List<String> opsTransactions) {
    this.opsTransactions = opsTransactions;
    return this;
  }

  public Archival addOpsTransactionsItem(String opsTransactionsItem) {
    if (this.opsTransactions == null) {
      this.opsTransactions = new ArrayList<String>();
    }
    this.opsTransactions.add(opsTransactionsItem);
    return this;
  }

   /**
   * Get opsTransactions
   * @return opsTransactions
  **/
  @ApiModelProperty(value = "")
  public List<String> getOpsTransactions() {
    return opsTransactions;
  }

  public void setOpsTransactions(List<String> opsTransactions) {
    this.opsTransactions = opsTransactions;
  }

  public Archival pauseBetweenIterations(Long pauseBetweenIterations) {
    this.pauseBetweenIterations = pauseBetweenIterations;
    return this;
  }

   /**
   * Get pauseBetweenIterations
   * @return pauseBetweenIterations
  **/
  @ApiModelProperty(value = "")
  public Long getPauseBetweenIterations() {
    return pauseBetweenIterations;
  }

  public void setPauseBetweenIterations(Long pauseBetweenIterations) {
    this.pauseBetweenIterations = pauseBetweenIterations;
  }

  public Archival pausedTime(Long pausedTime) {
    this.pausedTime = pausedTime;
    return this;
  }

   /**
   * Get pausedTime
   * @return pausedTime
  **/
  @ApiModelProperty(value = "")
  public Long getPausedTime() {
    return pausedTime;
  }

  public void setPausedTime(Long pausedTime) {
    this.pausedTime = pausedTime;
  }

  public Archival processingTableName(String processingTableName) {
    this.processingTableName = processingTableName;
    return this;
  }

   /**
   * Get processingTableName
   * @return processingTableName
  **/
  @ApiModelProperty(value = "")
  public String getProcessingTableName() {
    return processingTableName;
  }

  public void setProcessingTableName(String processingTableName) {
    this.processingTableName = processingTableName;
  }

  public Archival reconnectTime(Long reconnectTime) {
    this.reconnectTime = reconnectTime;
    return this;
  }

   /**
   * Get reconnectTime
   * @return reconnectTime
  **/
  @ApiModelProperty(value = "")
  public Long getReconnectTime() {
    return reconnectTime;
  }

  public void setReconnectTime(Long reconnectTime) {
    this.reconnectTime = reconnectTime;
  }

  public Archival resumeArchive(Boolean resumeArchive) {
    this.resumeArchive = resumeArchive;
    return this;
  }

   /**
   * Get resumeArchive
   * @return resumeArchive
  **/
  @ApiModelProperty(value = "")
  public Boolean isResumeArchive() {
    return resumeArchive;
  }

  public void setResumeArchive(Boolean resumeArchive) {
    this.resumeArchive = resumeArchive;
  }

  public Archival resumedByUserId(String resumedByUserId) {
    this.resumedByUserId = resumedByUserId;
    return this;
  }

   /**
   * Get resumedByUserId
   * @return resumedByUserId
  **/
  @ApiModelProperty(value = "")
  public String getResumedByUserId() {
    return resumedByUserId;
  }

  public void setResumedByUserId(String resumedByUserId) {
    this.resumedByUserId = resumedByUserId;
  }

  public Archival resumedByUserName(String resumedByUserName) {
    this.resumedByUserName = resumedByUserName;
    return this;
  }

   /**
   * Get resumedByUserName
   * @return resumedByUserName
  **/
  @ApiModelProperty(value = "")
  public String getResumedByUserName() {
    return resumedByUserName;
  }

  public void setResumedByUserName(String resumedByUserName) {
    this.resumedByUserName = resumedByUserName;
  }

  public Archival resumedDate(Long resumedDate) {
    this.resumedDate = resumedDate;
    return this;
  }

   /**
   * Get resumedDate
   * @return resumedDate
  **/
  @ApiModelProperty(value = "")
  public Long getResumedDate() {
    return resumedDate;
  }

  public void setResumedDate(Long resumedDate) {
    this.resumedDate = resumedDate;
  }

  public Archival retrieveEstimatedWorkTime(Long retrieveEstimatedWorkTime) {
    this.retrieveEstimatedWorkTime = retrieveEstimatedWorkTime;
    return this;
  }

   /**
   * Get retrieveEstimatedWorkTime
   * @return retrieveEstimatedWorkTime
  **/
  @ApiModelProperty(value = "")
  public Long getRetrieveEstimatedWorkTime() {
    return retrieveEstimatedWorkTime;
  }

  public void setRetrieveEstimatedWorkTime(Long retrieveEstimatedWorkTime) {
    this.retrieveEstimatedWorkTime = retrieveEstimatedWorkTime;
  }

  public Archival startCalculationTime(Long startCalculationTime) {
    this.startCalculationTime = startCalculationTime;
    return this;
  }

   /**
   * Get startCalculationTime
   * @return startCalculationTime
  **/
  @ApiModelProperty(value = "")
  public Long getStartCalculationTime() {
    return startCalculationTime;
  }

  public void setStartCalculationTime(Long startCalculationTime) {
    this.startCalculationTime = startCalculationTime;
  }

  public Archival startDate(Long startDate) {
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

  public Archival startedByUserId(String startedByUserId) {
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

  public Archival startedByUserName(String startedByUserName) {
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

  public Archival statsUpdateTime(Long statsUpdateTime) {
    this.statsUpdateTime = statsUpdateTime;
    return this;
  }

   /**
   * Get statsUpdateTime
   * @return statsUpdateTime
  **/
  @ApiModelProperty(value = "")
  public Long getStatsUpdateTime() {
    return statsUpdateTime;
  }

  public void setStatsUpdateTime(Long statsUpdateTime) {
    this.statsUpdateTime = statsUpdateTime;
  }

  public Archival status(StatusEnum status) {
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

  public Archival tableDeletesDone(Long tableDeletesDone) {
    this.tableDeletesDone = tableDeletesDone;
    return this;
  }

   /**
   * Get tableDeletesDone
   * @return tableDeletesDone
  **/
  @ApiModelProperty(value = "")
  public Long getTableDeletesDone() {
    return tableDeletesDone;
  }

  public void setTableDeletesDone(Long tableDeletesDone) {
    this.tableDeletesDone = tableDeletesDone;
  }

  public Archival tableDeletesTodo(Long tableDeletesTodo) {
    this.tableDeletesTodo = tableDeletesTodo;
    return this;
  }

   /**
   * Get tableDeletesTodo
   * @return tableDeletesTodo
  **/
  @ApiModelProperty(value = "")
  public Long getTableDeletesTodo() {
    return tableDeletesTodo;
  }

  public void setTableDeletesTodo(Long tableDeletesTodo) {
    this.tableDeletesTodo = tableDeletesTodo;
  }

  public Archival tableUpdatesDone(Long tableUpdatesDone) {
    this.tableUpdatesDone = tableUpdatesDone;
    return this;
  }

   /**
   * Get tableUpdatesDone
   * @return tableUpdatesDone
  **/
  @ApiModelProperty(value = "")
  public Long getTableUpdatesDone() {
    return tableUpdatesDone;
  }

  public void setTableUpdatesDone(Long tableUpdatesDone) {
    this.tableUpdatesDone = tableUpdatesDone;
  }

  public Archival tableUpdatesTodo(Long tableUpdatesTodo) {
    this.tableUpdatesTodo = tableUpdatesTodo;
    return this;
  }

   /**
   * Get tableUpdatesTodo
   * @return tableUpdatesTodo
  **/
  @ApiModelProperty(value = "")
  public Long getTableUpdatesTodo() {
    return tableUpdatesTodo;
  }

  public void setTableUpdatesTodo(Long tableUpdatesTodo) {
    this.tableUpdatesTodo = tableUpdatesTodo;
  }

  public Archival tablesDone(Long tablesDone) {
    this.tablesDone = tablesDone;
    return this;
  }

   /**
   * Get tablesDone
   * @return tablesDone
  **/
  @ApiModelProperty(value = "")
  public Long getTablesDone() {
    return tablesDone;
  }

  public void setTablesDone(Long tablesDone) {
    this.tablesDone = tablesDone;
  }

  public Archival tablesTodo(Long tablesTodo) {
    this.tablesTodo = tablesTodo;
    return this;
  }

   /**
   * Get tablesTodo
   * @return tablesTodo
  **/
  @ApiModelProperty(value = "")
  public Long getTablesTodo() {
    return tablesTodo;
  }

  public void setTablesTodo(Long tablesTodo) {
    this.tablesTodo = tablesTodo;
  }

  public Archival updateExtMgmtSchemaTime(Long updateExtMgmtSchemaTime) {
    this.updateExtMgmtSchemaTime = updateExtMgmtSchemaTime;
    return this;
  }

   /**
   * Get updateExtMgmtSchemaTime
   * @return updateExtMgmtSchemaTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateExtMgmtSchemaTime() {
    return updateExtMgmtSchemaTime;
  }

  public void setUpdateExtMgmtSchemaTime(Long updateExtMgmtSchemaTime) {
    this.updateExtMgmtSchemaTime = updateExtMgmtSchemaTime;
  }

  public Archival updateExternalSchemaTime(Long updateExternalSchemaTime) {
    this.updateExternalSchemaTime = updateExternalSchemaTime;
    return this;
  }

   /**
   * Get updateExternalSchemaTime
   * @return updateExternalSchemaTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateExternalSchemaTime() {
    return updateExternalSchemaTime;
  }

  public void setUpdateExternalSchemaTime(Long updateExternalSchemaTime) {
    this.updateExternalSchemaTime = updateExternalSchemaTime;
  }

  public Archival updateMergeTime(Long updateMergeTime) {
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

  public Archival updateReadBatches(Long updateReadBatches) {
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

  public Archival updateReadTime(Long updateReadTime) {
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

  public Archival updateWriteBatches(Long updateWriteBatches) {
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

  public Archival updateWriteTime(Long updateWriteTime) {
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

  public Archival updatesDone(Long updatesDone) {
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

  public Archival updatesEndDate(Long updatesEndDate) {
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

  public Archival updatesStartDate(Long updatesStartDate) {
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

  public Archival updatesTodo(Long updatesTodo) {
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
    Archival archival = (Archival) o;
    return Objects.equals(this.archivalPhase, archival.archivalPhase) &&
        Objects.equals(this.archivalPhaseStartTime, archival.archivalPhaseStartTime) &&
        Objects.equals(this.archiveDestName, archival.archiveDestName) &&
        Objects.equals(this.archiveReaderCount, archival.archiveReaderCount) &&
        Objects.equals(this.archiveReadersWaitTime, archival.archiveReadersWaitTime) &&
        Objects.equals(this.archiveTask, archival.archiveTask) &&
        Objects.equals(this.archivingTime, archival.archivingTime) &&
        Objects.equals(this.clearArchiveTablesTime, archival.clearArchiveTablesTime) &&
        Objects.equals(this.currentTableName, archival.currentTableName) &&
        Objects.equals(this.deleteReadBatches, archival.deleteReadBatches) &&
        Objects.equals(this.deleteReadTime, archival.deleteReadTime) &&
        Objects.equals(this.deleteWriteBatches, archival.deleteWriteBatches) &&
        Objects.equals(this.deleteWriteTime, archival.deleteWriteTime) &&
        Objects.equals(this.deletesDone, archival.deletesDone) &&
        Objects.equals(this.deletesEndDate, archival.deletesEndDate) &&
        Objects.equals(this.deletesStartDate, archival.deletesStartDate) &&
        Objects.equals(this.deletesTodo, archival.deletesTodo) &&
        Objects.equals(this.elapsedTime, archival.elapsedTime) &&
        Objects.equals(this.endByDate, archival.endByDate) &&
        Objects.equals(this.endCalculationTime, archival.endCalculationTime) &&
        Objects.equals(this.endDate, archival.endDate) &&
        Objects.equals(this.errMessage, archival.errMessage) &&
        Objects.equals(this.estimateWorkTodoTime, archival.estimateWorkTodoTime) &&
        Objects.equals(this.executionMode, archival.executionMode) &&
        Objects.equals(this.finalizeEndTime, archival.finalizeEndTime) &&
        Objects.equals(this.finalizeTime, archival.finalizeTime) &&
        Objects.equals(this.id, archival.id) &&
        Objects.equals(this.iterationCount, archival.iterationCount) &&
        Objects.equals(this.maintSchedName, archival.maintSchedName) &&
        Objects.equals(this.opsTransWaitCount, archival.opsTransWaitCount) &&
        Objects.equals(this.opsTransWaitTime, archival.opsTransWaitTime) &&
        Objects.equals(this.opsTransactions, archival.opsTransactions) &&
        Objects.equals(this.pauseBetweenIterations, archival.pauseBetweenIterations) &&
        Objects.equals(this.pausedTime, archival.pausedTime) &&
        Objects.equals(this.processingTableName, archival.processingTableName) &&
        Objects.equals(this.reconnectTime, archival.reconnectTime) &&
        Objects.equals(this.resumeArchive, archival.resumeArchive) &&
        Objects.equals(this.resumedByUserId, archival.resumedByUserId) &&
        Objects.equals(this.resumedByUserName, archival.resumedByUserName) &&
        Objects.equals(this.resumedDate, archival.resumedDate) &&
        Objects.equals(this.retrieveEstimatedWorkTime, archival.retrieveEstimatedWorkTime) &&
        Objects.equals(this.startCalculationTime, archival.startCalculationTime) &&
        Objects.equals(this.startDate, archival.startDate) &&
        Objects.equals(this.startedByUserId, archival.startedByUserId) &&
        Objects.equals(this.startedByUserName, archival.startedByUserName) &&
        Objects.equals(this.statsUpdateTime, archival.statsUpdateTime) &&
        Objects.equals(this.status, archival.status) &&
        Objects.equals(this.tableDeletesDone, archival.tableDeletesDone) &&
        Objects.equals(this.tableDeletesTodo, archival.tableDeletesTodo) &&
        Objects.equals(this.tableUpdatesDone, archival.tableUpdatesDone) &&
        Objects.equals(this.tableUpdatesTodo, archival.tableUpdatesTodo) &&
        Objects.equals(this.tablesDone, archival.tablesDone) &&
        Objects.equals(this.tablesTodo, archival.tablesTodo) &&
        Objects.equals(this.updateExtMgmtSchemaTime, archival.updateExtMgmtSchemaTime) &&
        Objects.equals(this.updateExternalSchemaTime, archival.updateExternalSchemaTime) &&
        Objects.equals(this.updateMergeTime, archival.updateMergeTime) &&
        Objects.equals(this.updateReadBatches, archival.updateReadBatches) &&
        Objects.equals(this.updateReadTime, archival.updateReadTime) &&
        Objects.equals(this.updateWriteBatches, archival.updateWriteBatches) &&
        Objects.equals(this.updateWriteTime, archival.updateWriteTime) &&
        Objects.equals(this.updatesDone, archival.updatesDone) &&
        Objects.equals(this.updatesEndDate, archival.updatesEndDate) &&
        Objects.equals(this.updatesStartDate, archival.updatesStartDate) &&
        Objects.equals(this.updatesTodo, archival.updatesTodo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(archivalPhase, archivalPhaseStartTime, archiveDestName, archiveReaderCount, archiveReadersWaitTime, archiveTask, archivingTime, clearArchiveTablesTime, currentTableName, deleteReadBatches, deleteReadTime, deleteWriteBatches, deleteWriteTime, deletesDone, deletesEndDate, deletesStartDate, deletesTodo, elapsedTime, endByDate, endCalculationTime, endDate, errMessage, estimateWorkTodoTime, executionMode, finalizeEndTime, finalizeTime, id, iterationCount, maintSchedName, opsTransWaitCount, opsTransWaitTime, opsTransactions, pauseBetweenIterations, pausedTime, processingTableName, reconnectTime, resumeArchive, resumedByUserId, resumedByUserName, resumedDate, retrieveEstimatedWorkTime, startCalculationTime, startDate, startedByUserId, startedByUserName, statsUpdateTime, status, tableDeletesDone, tableDeletesTodo, tableUpdatesDone, tableUpdatesTodo, tablesDone, tablesTodo, updateExtMgmtSchemaTime, updateExternalSchemaTime, updateMergeTime, updateReadBatches, updateReadTime, updateWriteBatches, updateWriteTime, updatesDone, updatesEndDate, updatesStartDate, updatesTodo);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Archival {\n");
    
    sb.append("    archivalPhase: ").append(toIndentedString(archivalPhase)).append("\n");
    sb.append("    archivalPhaseStartTime: ").append(toIndentedString(archivalPhaseStartTime)).append("\n");
    sb.append("    archiveDestName: ").append(toIndentedString(archiveDestName)).append("\n");
    sb.append("    archiveReaderCount: ").append(toIndentedString(archiveReaderCount)).append("\n");
    sb.append("    archiveReadersWaitTime: ").append(toIndentedString(archiveReadersWaitTime)).append("\n");
    sb.append("    archiveTask: ").append(toIndentedString(archiveTask)).append("\n");
    sb.append("    archivingTime: ").append(toIndentedString(archivingTime)).append("\n");
    sb.append("    clearArchiveTablesTime: ").append(toIndentedString(clearArchiveTablesTime)).append("\n");
    sb.append("    currentTableName: ").append(toIndentedString(currentTableName)).append("\n");
    sb.append("    deleteReadBatches: ").append(toIndentedString(deleteReadBatches)).append("\n");
    sb.append("    deleteReadTime: ").append(toIndentedString(deleteReadTime)).append("\n");
    sb.append("    deleteWriteBatches: ").append(toIndentedString(deleteWriteBatches)).append("\n");
    sb.append("    deleteWriteTime: ").append(toIndentedString(deleteWriteTime)).append("\n");
    sb.append("    deletesDone: ").append(toIndentedString(deletesDone)).append("\n");
    sb.append("    deletesEndDate: ").append(toIndentedString(deletesEndDate)).append("\n");
    sb.append("    deletesStartDate: ").append(toIndentedString(deletesStartDate)).append("\n");
    sb.append("    deletesTodo: ").append(toIndentedString(deletesTodo)).append("\n");
    sb.append("    elapsedTime: ").append(toIndentedString(elapsedTime)).append("\n");
    sb.append("    endByDate: ").append(toIndentedString(endByDate)).append("\n");
    sb.append("    endCalculationTime: ").append(toIndentedString(endCalculationTime)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    errMessage: ").append(toIndentedString(errMessage)).append("\n");
    sb.append("    estimateWorkTodoTime: ").append(toIndentedString(estimateWorkTodoTime)).append("\n");
    sb.append("    executionMode: ").append(toIndentedString(executionMode)).append("\n");
    sb.append("    finalizeEndTime: ").append(toIndentedString(finalizeEndTime)).append("\n");
    sb.append("    finalizeTime: ").append(toIndentedString(finalizeTime)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    iterationCount: ").append(toIndentedString(iterationCount)).append("\n");
    sb.append("    maintSchedName: ").append(toIndentedString(maintSchedName)).append("\n");
    sb.append("    opsTransWaitCount: ").append(toIndentedString(opsTransWaitCount)).append("\n");
    sb.append("    opsTransWaitTime: ").append(toIndentedString(opsTransWaitTime)).append("\n");
    sb.append("    opsTransactions: ").append(toIndentedString(opsTransactions)).append("\n");
    sb.append("    pauseBetweenIterations: ").append(toIndentedString(pauseBetweenIterations)).append("\n");
    sb.append("    pausedTime: ").append(toIndentedString(pausedTime)).append("\n");
    sb.append("    processingTableName: ").append(toIndentedString(processingTableName)).append("\n");
    sb.append("    reconnectTime: ").append(toIndentedString(reconnectTime)).append("\n");
    sb.append("    resumeArchive: ").append(toIndentedString(resumeArchive)).append("\n");
    sb.append("    resumedByUserId: ").append(toIndentedString(resumedByUserId)).append("\n");
    sb.append("    resumedByUserName: ").append(toIndentedString(resumedByUserName)).append("\n");
    sb.append("    resumedDate: ").append(toIndentedString(resumedDate)).append("\n");
    sb.append("    retrieveEstimatedWorkTime: ").append(toIndentedString(retrieveEstimatedWorkTime)).append("\n");
    sb.append("    startCalculationTime: ").append(toIndentedString(startCalculationTime)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    startedByUserId: ").append(toIndentedString(startedByUserId)).append("\n");
    sb.append("    startedByUserName: ").append(toIndentedString(startedByUserName)).append("\n");
    sb.append("    statsUpdateTime: ").append(toIndentedString(statsUpdateTime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tableDeletesDone: ").append(toIndentedString(tableDeletesDone)).append("\n");
    sb.append("    tableDeletesTodo: ").append(toIndentedString(tableDeletesTodo)).append("\n");
    sb.append("    tableUpdatesDone: ").append(toIndentedString(tableUpdatesDone)).append("\n");
    sb.append("    tableUpdatesTodo: ").append(toIndentedString(tableUpdatesTodo)).append("\n");
    sb.append("    tablesDone: ").append(toIndentedString(tablesDone)).append("\n");
    sb.append("    tablesTodo: ").append(toIndentedString(tablesTodo)).append("\n");
    sb.append("    updateExtMgmtSchemaTime: ").append(toIndentedString(updateExtMgmtSchemaTime)).append("\n");
    sb.append("    updateExternalSchemaTime: ").append(toIndentedString(updateExternalSchemaTime)).append("\n");
    sb.append("    updateMergeTime: ").append(toIndentedString(updateMergeTime)).append("\n");
    sb.append("    updateReadBatches: ").append(toIndentedString(updateReadBatches)).append("\n");
    sb.append("    updateReadTime: ").append(toIndentedString(updateReadTime)).append("\n");
    sb.append("    updateWriteBatches: ").append(toIndentedString(updateWriteBatches)).append("\n");
    sb.append("    updateWriteTime: ").append(toIndentedString(updateWriteTime)).append("\n");
    sb.append("    updatesDone: ").append(toIndentedString(updatesDone)).append("\n");
    sb.append("    updatesEndDate: ").append(toIndentedString(updatesEndDate)).append("\n");
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
