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

package de.araneaconsult.codegen.ig.rest.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import de.araneaconsult.codegen.ig.rest.model.AppSource;
import de.araneaconsult.codegen.ig.rest.model.Collector;
import de.araneaconsult.codegen.ig.rest.model.IdentitySource;
import de.araneaconsult.codegen.ig.rest.model.Schedule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Collection
 */



public class Collection {
  @SerializedName("applicationSource")
  private AppSource applicationSource = null;

  @SerializedName("collectionId")
  private Long collectionId = null;

  @SerializedName("collector")
  private Collector collector = null;

  /**
   * Gets or Sets dscStatus
   */
  @JsonAdapter(DscStatusEnum.Adapter.class)
  public enum DscStatusEnum {
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

    DscStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DscStatusEnum fromValue(String input) {
      for (DscStatusEnum b : DscStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DscStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DscStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DscStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DscStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("dscStatus")
  private DscStatusEnum dscStatus = null;

  @SerializedName("endTime")
  private Long endTime = null;

  @SerializedName("errorPhase1")
  private Integer errorPhase1 = null;

  @SerializedName("errorPhase2")
  private Integer errorPhase2 = null;

  @SerializedName("heartBeatTime")
  private Long heartBeatTime = null;

  @SerializedName("identitySource")
  private IdentitySource identitySource = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkErrors")
  private String linkErrors = null;

  @SerializedName("linkStop")
  private String linkStop = null;

  @SerializedName("message")
  private String message = null;

  /**
   * Gets or Sets publishState
   */
  @JsonAdapter(PublishStateEnum.Adapter.class)
  public enum PublishStateEnum {
    @SerializedName("COLLECTED")
    COLLECTED("COLLECTED"),
    @SerializedName("PUBLISHED")
    PUBLISHED("PUBLISHED"),
    @SerializedName("DISCARDED")
    DISCARDED("DISCARDED");

    private String value;

    PublishStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static PublishStateEnum fromValue(String input) {
      for (PublishStateEnum b : PublishStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<PublishStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PublishStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public PublishStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return PublishStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("publishState")
  private PublishStateEnum publishState = null;

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

  @SerializedName("schedules")
  private List<Schedule> schedules = null;

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

  @SerializedName("warningPhase1")
  private Integer warningPhase1 = null;

  @SerializedName("warningPhase2")
  private Integer warningPhase2 = null;

  public Collection applicationSource(AppSource applicationSource) {
    this.applicationSource = applicationSource;
    return this;
  }

   /**
   * Get applicationSource
   * @return applicationSource
  **/
  @ApiModelProperty(value = "")
  public AppSource getApplicationSource() {
    return applicationSource;
  }

  public void setApplicationSource(AppSource applicationSource) {
    this.applicationSource = applicationSource;
  }

  public Collection collectionId(Long collectionId) {
    this.collectionId = collectionId;
    return this;
  }

   /**
   * Get collectionId
   * @return collectionId
  **/
  @ApiModelProperty(value = "")
  public Long getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(Long collectionId) {
    this.collectionId = collectionId;
  }

  public Collection collector(Collector collector) {
    this.collector = collector;
    return this;
  }

   /**
   * Get collector
   * @return collector
  **/
  @ApiModelProperty(value = "")
  public Collector getCollector() {
    return collector;
  }

  public void setCollector(Collector collector) {
    this.collector = collector;
  }

  public Collection dscStatus(DscStatusEnum dscStatus) {
    this.dscStatus = dscStatus;
    return this;
  }

   /**
   * Get dscStatus
   * @return dscStatus
  **/
  @ApiModelProperty(value = "")
  public DscStatusEnum getDscStatus() {
    return dscStatus;
  }

  public void setDscStatus(DscStatusEnum dscStatus) {
    this.dscStatus = dscStatus;
  }

  public Collection endTime(Long endTime) {
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

  public Collection errorPhase1(Integer errorPhase1) {
    this.errorPhase1 = errorPhase1;
    return this;
  }

   /**
   * Get errorPhase1
   * @return errorPhase1
  **/
  @ApiModelProperty(value = "")
  public Integer getErrorPhase1() {
    return errorPhase1;
  }

  public void setErrorPhase1(Integer errorPhase1) {
    this.errorPhase1 = errorPhase1;
  }

  public Collection errorPhase2(Integer errorPhase2) {
    this.errorPhase2 = errorPhase2;
    return this;
  }

   /**
   * Get errorPhase2
   * @return errorPhase2
  **/
  @ApiModelProperty(value = "")
  public Integer getErrorPhase2() {
    return errorPhase2;
  }

  public void setErrorPhase2(Integer errorPhase2) {
    this.errorPhase2 = errorPhase2;
  }

  public Collection heartBeatTime(Long heartBeatTime) {
    this.heartBeatTime = heartBeatTime;
    return this;
  }

   /**
   * Get heartBeatTime
   * @return heartBeatTime
  **/
  @ApiModelProperty(value = "")
  public Long getHeartBeatTime() {
    return heartBeatTime;
  }

  public void setHeartBeatTime(Long heartBeatTime) {
    this.heartBeatTime = heartBeatTime;
  }

  public Collection identitySource(IdentitySource identitySource) {
    this.identitySource = identitySource;
    return this;
  }

   /**
   * Get identitySource
   * @return identitySource
  **/
  @ApiModelProperty(value = "")
  public IdentitySource getIdentitySource() {
    return identitySource;
  }

  public void setIdentitySource(IdentitySource identitySource) {
    this.identitySource = identitySource;
  }

  public Collection link(String link) {
    this.link = link;
    return this;
  }

   /**
   * Get link
   * @return link
  **/
  @ApiModelProperty(value = "")
  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public Collection linkErrors(String linkErrors) {
    this.linkErrors = linkErrors;
    return this;
  }

   /**
   * Get linkErrors
   * @return linkErrors
  **/
  @ApiModelProperty(value = "")
  public String getLinkErrors() {
    return linkErrors;
  }

  public void setLinkErrors(String linkErrors) {
    this.linkErrors = linkErrors;
  }

  public Collection linkStop(String linkStop) {
    this.linkStop = linkStop;
    return this;
  }

   /**
   * Get linkStop
   * @return linkStop
  **/
  @ApiModelProperty(value = "")
  public String getLinkStop() {
    return linkStop;
  }

  public void setLinkStop(String linkStop) {
    this.linkStop = linkStop;
  }

  public Collection message(String message) {
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

  public Collection publishState(PublishStateEnum publishState) {
    this.publishState = publishState;
    return this;
  }

   /**
   * Get publishState
   * @return publishState
  **/
  @ApiModelProperty(value = "")
  public PublishStateEnum getPublishState() {
    return publishState;
  }

  public void setPublishState(PublishStateEnum publishState) {
    this.publishState = publishState;
  }

  public Collection runState(RunStateEnum runState) {
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

  public Collection schedules(List<Schedule> schedules) {
    this.schedules = schedules;
    return this;
  }

  public Collection addSchedulesItem(Schedule schedulesItem) {
    if (this.schedules == null) {
      this.schedules = new ArrayList<Schedule>();
    }
    this.schedules.add(schedulesItem);
    return this;
  }

   /**
   * Get schedules
   * @return schedules
  **/
  @ApiModelProperty(value = "")
  public List<Schedule> getSchedules() {
    return schedules;
  }

  public void setSchedules(List<Schedule> schedules) {
    this.schedules = schedules;
  }

  public Collection startTime(Long startTime) {
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

  public Collection status(StatusEnum status) {
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

  public Collection warningPhase1(Integer warningPhase1) {
    this.warningPhase1 = warningPhase1;
    return this;
  }

   /**
   * Get warningPhase1
   * @return warningPhase1
  **/
  @ApiModelProperty(value = "")
  public Integer getWarningPhase1() {
    return warningPhase1;
  }

  public void setWarningPhase1(Integer warningPhase1) {
    this.warningPhase1 = warningPhase1;
  }

  public Collection warningPhase2(Integer warningPhase2) {
    this.warningPhase2 = warningPhase2;
    return this;
  }

   /**
   * Get warningPhase2
   * @return warningPhase2
  **/
  @ApiModelProperty(value = "")
  public Integer getWarningPhase2() {
    return warningPhase2;
  }

  public void setWarningPhase2(Integer warningPhase2) {
    this.warningPhase2 = warningPhase2;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Collection collection = (Collection) o;
    return Objects.equals(this.applicationSource, collection.applicationSource) &&
        Objects.equals(this.collectionId, collection.collectionId) &&
        Objects.equals(this.collector, collection.collector) &&
        Objects.equals(this.dscStatus, collection.dscStatus) &&
        Objects.equals(this.endTime, collection.endTime) &&
        Objects.equals(this.errorPhase1, collection.errorPhase1) &&
        Objects.equals(this.errorPhase2, collection.errorPhase2) &&
        Objects.equals(this.heartBeatTime, collection.heartBeatTime) &&
        Objects.equals(this.identitySource, collection.identitySource) &&
        Objects.equals(this.link, collection.link) &&
        Objects.equals(this.linkErrors, collection.linkErrors) &&
        Objects.equals(this.linkStop, collection.linkStop) &&
        Objects.equals(this.message, collection.message) &&
        Objects.equals(this.publishState, collection.publishState) &&
        Objects.equals(this.runState, collection.runState) &&
        Objects.equals(this.schedules, collection.schedules) &&
        Objects.equals(this.startTime, collection.startTime) &&
        Objects.equals(this.status, collection.status) &&
        Objects.equals(this.warningPhase1, collection.warningPhase1) &&
        Objects.equals(this.warningPhase2, collection.warningPhase2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicationSource, collectionId, collector, dscStatus, endTime, errorPhase1, errorPhase2, heartBeatTime, identitySource, link, linkErrors, linkStop, message, publishState, runState, schedules, startTime, status, warningPhase1, warningPhase2);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Collection {\n");
    
    sb.append("    applicationSource: ").append(toIndentedString(applicationSource)).append("\n");
    sb.append("    collectionId: ").append(toIndentedString(collectionId)).append("\n");
    sb.append("    collector: ").append(toIndentedString(collector)).append("\n");
    sb.append("    dscStatus: ").append(toIndentedString(dscStatus)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    errorPhase1: ").append(toIndentedString(errorPhase1)).append("\n");
    sb.append("    errorPhase2: ").append(toIndentedString(errorPhase2)).append("\n");
    sb.append("    heartBeatTime: ").append(toIndentedString(heartBeatTime)).append("\n");
    sb.append("    identitySource: ").append(toIndentedString(identitySource)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkErrors: ").append(toIndentedString(linkErrors)).append("\n");
    sb.append("    linkStop: ").append(toIndentedString(linkStop)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    publishState: ").append(toIndentedString(publishState)).append("\n");
    sb.append("    runState: ").append(toIndentedString(runState)).append("\n");
    sb.append("    schedules: ").append(toIndentedString(schedules)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    warningPhase1: ").append(toIndentedString(warningPhase1)).append("\n");
    sb.append("    warningPhase2: ").append(toIndentedString(warningPhase2)).append("\n");
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
