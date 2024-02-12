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
import de.araneaconsult.codegen.ig.rest.model.CleanupEntityItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * MaintSchedule
 */



public class MaintSchedule {
  @SerializedName("active")
  private Boolean active = null;

  @SerializedName("allCleanupOlderThanDays")
  private Long allCleanupOlderThanDays = null;

  @SerializedName("archiveConcurrent")
  private Boolean archiveConcurrent = null;

  @SerializedName("cancelRunningCleanup")
  private Boolean cancelRunningCleanup = null;

  @SerializedName("cleanupEntityItems")
  private List<CleanupEntityItem> cleanupEntityItems = null;

  @SerializedName("createDate")
  private Long createDate = null;

  @SerializedName("createdBy")
  private String createdBy = null;

  @SerializedName("createdByDisplayName")
  private String createdByDisplayName = null;

  /**
   * Gets or Sets dayOfMonth
   */
  @JsonAdapter(DayOfMonthEnum.Adapter.class)
  public enum DayOfMonthEnum {
    @SerializedName("FIXED_DAY_OF_MONTH")
    FIXED_DAY_OF_MONTH("FIXED_DAY_OF_MONTH"),
    @SerializedName("LAST_DAY_OF_MONTH")
    LAST_DAY_OF_MONTH("LAST_DAY_OF_MONTH"),
    @SerializedName("FIRST_OCCURRENCE")
    FIRST_OCCURRENCE("FIRST_OCCURRENCE"),
    @SerializedName("SECOND_OCCURRENCE")
    SECOND_OCCURRENCE("SECOND_OCCURRENCE"),
    @SerializedName("THIRD_OCCURRENCE")
    THIRD_OCCURRENCE("THIRD_OCCURRENCE"),
    @SerializedName("FOURTH_OCCURRENCE")
    FOURTH_OCCURRENCE("FOURTH_OCCURRENCE"),
    @SerializedName("LAST_OCCURRENCE")
    LAST_OCCURRENCE("LAST_OCCURRENCE");

    private String value;

    DayOfMonthEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DayOfMonthEnum fromValue(String input) {
      for (DayOfMonthEnum b : DayOfMonthEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DayOfMonthEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DayOfMonthEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DayOfMonthEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DayOfMonthEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("dayOfMonth")
  private DayOfMonthEnum dayOfMonth = null;

  /**
   * Gets or Sets dayOfWeek
   */
  @JsonAdapter(DayOfWeekEnum.Adapter.class)
  public enum DayOfWeekEnum {
    @SerializedName("SUN")
    SUN("SUN"),
    @SerializedName("MON")
    MON("MON"),
    @SerializedName("TUE")
    TUE("TUE"),
    @SerializedName("WED")
    WED("WED"),
    @SerializedName("THU")
    THU("THU"),
    @SerializedName("FRI")
    FRI("FRI"),
    @SerializedName("SAT")
    SAT("SAT");

    private String value;

    DayOfWeekEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DayOfWeekEnum fromValue(String input) {
      for (DayOfWeekEnum b : DayOfWeekEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DayOfWeekEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DayOfWeekEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DayOfWeekEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DayOfWeekEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("dayOfWeek")
  private DayOfWeekEnum dayOfWeek = null;

  @SerializedName("deleteDate")
  private Long deleteDate = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("deletedBy")
  private String deletedBy = null;

  @SerializedName("deletedByDisplayName")
  private String deletedByDisplayName = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("endTime")
  private Long endTime = null;

  /**
   * Gets or Sets finalizeDayOfMonth
   */
  @JsonAdapter(FinalizeDayOfMonthEnum.Adapter.class)
  public enum FinalizeDayOfMonthEnum {
    @SerializedName("FIXED_DAY_OF_MONTH")
    FIXED_DAY_OF_MONTH("FIXED_DAY_OF_MONTH"),
    @SerializedName("LAST_DAY_OF_MONTH")
    LAST_DAY_OF_MONTH("LAST_DAY_OF_MONTH"),
    @SerializedName("FIRST_OCCURRENCE")
    FIRST_OCCURRENCE("FIRST_OCCURRENCE"),
    @SerializedName("SECOND_OCCURRENCE")
    SECOND_OCCURRENCE("SECOND_OCCURRENCE"),
    @SerializedName("THIRD_OCCURRENCE")
    THIRD_OCCURRENCE("THIRD_OCCURRENCE"),
    @SerializedName("FOURTH_OCCURRENCE")
    FOURTH_OCCURRENCE("FOURTH_OCCURRENCE"),
    @SerializedName("LAST_OCCURRENCE")
    LAST_OCCURRENCE("LAST_OCCURRENCE");

    private String value;

    FinalizeDayOfMonthEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static FinalizeDayOfMonthEnum fromValue(String input) {
      for (FinalizeDayOfMonthEnum b : FinalizeDayOfMonthEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<FinalizeDayOfMonthEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FinalizeDayOfMonthEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public FinalizeDayOfMonthEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return FinalizeDayOfMonthEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("finalizeDayOfMonth")
  private FinalizeDayOfMonthEnum finalizeDayOfMonth = null;

  /**
   * Gets or Sets finalizeDayOfWeek
   */
  @JsonAdapter(FinalizeDayOfWeekEnum.Adapter.class)
  public enum FinalizeDayOfWeekEnum {
    @SerializedName("SUN")
    SUN("SUN"),
    @SerializedName("MON")
    MON("MON"),
    @SerializedName("TUE")
    TUE("TUE"),
    @SerializedName("WED")
    WED("WED"),
    @SerializedName("THU")
    THU("THU"),
    @SerializedName("FRI")
    FRI("FRI"),
    @SerializedName("SAT")
    SAT("SAT");

    private String value;

    FinalizeDayOfWeekEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static FinalizeDayOfWeekEnum fromValue(String input) {
      for (FinalizeDayOfWeekEnum b : FinalizeDayOfWeekEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<FinalizeDayOfWeekEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FinalizeDayOfWeekEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public FinalizeDayOfWeekEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return FinalizeDayOfWeekEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("finalizeDayOfWeek")
  private FinalizeDayOfWeekEnum finalizeDayOfWeek = null;

  @SerializedName("finalizeHourOfDay")
  private Integer finalizeHourOfDay = null;

  @SerializedName("finalizeMaximumHours")
  private Long finalizeMaximumHours = null;

  @SerializedName("finalizeMinuteOfHour")
  private Integer finalizeMinuteOfHour = null;

  @SerializedName("finalizeTime")
  private Long finalizeTime = null;

  @SerializedName("finalizeTimeZoneId")
  private String finalizeTimeZoneId = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("lastRunTime")
  private Long lastRunTime = null;

  @SerializedName("maximumRunTime")
  private Long maximumRunTime = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("nextRunTime")
  private Long nextRunTime = null;

  @SerializedName("pauseBetweenIterations")
  private Long pauseBetweenIterations = null;

  @SerializedName("repeatInterval")
  private Integer repeatInterval = null;

  /**
   * Gets or Sets repeatIntervalUnit
   */
  @JsonAdapter(RepeatIntervalUnitEnum.Adapter.class)
  public enum RepeatIntervalUnitEnum {
    @SerializedName("MILLIS")
    MILLIS("MILLIS"),
    @SerializedName("SECONDS")
    SECONDS("SECONDS"),
    @SerializedName("MINUTES")
    MINUTES("MINUTES"),
    @SerializedName("HOURS")
    HOURS("HOURS"),
    @SerializedName("DAYS")
    DAYS("DAYS"),
    @SerializedName("WEEKS")
    WEEKS("WEEKS"),
    @SerializedName("MONTHS")
    MONTHS("MONTHS"),
    @SerializedName("YEARS")
    YEARS("YEARS");

    private String value;

    RepeatIntervalUnitEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RepeatIntervalUnitEnum fromValue(String input) {
      for (RepeatIntervalUnitEnum b : RepeatIntervalUnitEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RepeatIntervalUnitEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RepeatIntervalUnitEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RepeatIntervalUnitEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RepeatIntervalUnitEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("repeatIntervalUnit")
  private RepeatIntervalUnitEnum repeatIntervalUnit = null;

  @SerializedName("resumeArchive")
  private Boolean resumeArchive = null;

  /**
   * Gets or Sets scheduleType
   */
  @JsonAdapter(ScheduleTypeEnum.Adapter.class)
  public enum ScheduleTypeEnum {
    @SerializedName("ARCHIVE")
    ARCHIVE("ARCHIVE"),
    @SerializedName("CLEANUP")
    CLEANUP("CLEANUP"),
    @SerializedName("ARCHIVE_AND_CLEANUP")
    ARCHIVE_AND_CLEANUP("ARCHIVE_AND_CLEANUP");

    private String value;

    ScheduleTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ScheduleTypeEnum fromValue(String input) {
      for (ScheduleTypeEnum b : ScheduleTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ScheduleTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ScheduleTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ScheduleTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ScheduleTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("scheduleType")
  private ScheduleTypeEnum scheduleType = null;

  @SerializedName("startTime")
  private Long startTime = null;

  @SerializedName("updateDate")
  private Long updateDate = null;

  @SerializedName("updatedBy")
  private String updatedBy = null;

  @SerializedName("updatedByDisplayName")
  private String updatedByDisplayName = null;

  public MaintSchedule active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @ApiModelProperty(value = "")
  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public MaintSchedule allCleanupOlderThanDays(Long allCleanupOlderThanDays) {
    this.allCleanupOlderThanDays = allCleanupOlderThanDays;
    return this;
  }

   /**
   * Get allCleanupOlderThanDays
   * @return allCleanupOlderThanDays
  **/
  @ApiModelProperty(value = "")
  public Long getAllCleanupOlderThanDays() {
    return allCleanupOlderThanDays;
  }

  public void setAllCleanupOlderThanDays(Long allCleanupOlderThanDays) {
    this.allCleanupOlderThanDays = allCleanupOlderThanDays;
  }

  public MaintSchedule archiveConcurrent(Boolean archiveConcurrent) {
    this.archiveConcurrent = archiveConcurrent;
    return this;
  }

   /**
   * Get archiveConcurrent
   * @return archiveConcurrent
  **/
  @ApiModelProperty(value = "")
  public Boolean isArchiveConcurrent() {
    return archiveConcurrent;
  }

  public void setArchiveConcurrent(Boolean archiveConcurrent) {
    this.archiveConcurrent = archiveConcurrent;
  }

  public MaintSchedule cancelRunningCleanup(Boolean cancelRunningCleanup) {
    this.cancelRunningCleanup = cancelRunningCleanup;
    return this;
  }

   /**
   * Get cancelRunningCleanup
   * @return cancelRunningCleanup
  **/
  @ApiModelProperty(value = "")
  public Boolean isCancelRunningCleanup() {
    return cancelRunningCleanup;
  }

  public void setCancelRunningCleanup(Boolean cancelRunningCleanup) {
    this.cancelRunningCleanup = cancelRunningCleanup;
  }

  public MaintSchedule cleanupEntityItems(List<CleanupEntityItem> cleanupEntityItems) {
    this.cleanupEntityItems = cleanupEntityItems;
    return this;
  }

  public MaintSchedule addCleanupEntityItemsItem(CleanupEntityItem cleanupEntityItemsItem) {
    if (this.cleanupEntityItems == null) {
      this.cleanupEntityItems = new ArrayList<CleanupEntityItem>();
    }
    this.cleanupEntityItems.add(cleanupEntityItemsItem);
    return this;
  }

   /**
   * Get cleanupEntityItems
   * @return cleanupEntityItems
  **/
  @ApiModelProperty(value = "")
  public List<CleanupEntityItem> getCleanupEntityItems() {
    return cleanupEntityItems;
  }

  public void setCleanupEntityItems(List<CleanupEntityItem> cleanupEntityItems) {
    this.cleanupEntityItems = cleanupEntityItems;
  }

  public MaintSchedule createDate(Long createDate) {
    this.createDate = createDate;
    return this;
  }

   /**
   * Get createDate
   * @return createDate
  **/
  @ApiModelProperty(value = "")
  public Long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Long createDate) {
    this.createDate = createDate;
  }

  public MaintSchedule createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Get createdBy
   * @return createdBy
  **/
  @ApiModelProperty(value = "")
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public MaintSchedule createdByDisplayName(String createdByDisplayName) {
    this.createdByDisplayName = createdByDisplayName;
    return this;
  }

   /**
   * Get createdByDisplayName
   * @return createdByDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getCreatedByDisplayName() {
    return createdByDisplayName;
  }

  public void setCreatedByDisplayName(String createdByDisplayName) {
    this.createdByDisplayName = createdByDisplayName;
  }

  public MaintSchedule dayOfMonth(DayOfMonthEnum dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
    return this;
  }

   /**
   * Get dayOfMonth
   * @return dayOfMonth
  **/
  @ApiModelProperty(value = "")
  public DayOfMonthEnum getDayOfMonth() {
    return dayOfMonth;
  }

  public void setDayOfMonth(DayOfMonthEnum dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }

  public MaintSchedule dayOfWeek(DayOfWeekEnum dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
    return this;
  }

   /**
   * Get dayOfWeek
   * @return dayOfWeek
  **/
  @ApiModelProperty(value = "")
  public DayOfWeekEnum getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeekEnum dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public MaintSchedule deleteDate(Long deleteDate) {
    this.deleteDate = deleteDate;
    return this;
  }

   /**
   * Get deleteDate
   * @return deleteDate
  **/
  @ApiModelProperty(value = "")
  public Long getDeleteDate() {
    return deleteDate;
  }

  public void setDeleteDate(Long deleteDate) {
    this.deleteDate = deleteDate;
  }

  public MaintSchedule deleted(Boolean deleted) {
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

  public MaintSchedule deletedBy(String deletedBy) {
    this.deletedBy = deletedBy;
    return this;
  }

   /**
   * Get deletedBy
   * @return deletedBy
  **/
  @ApiModelProperty(value = "")
  public String getDeletedBy() {
    return deletedBy;
  }

  public void setDeletedBy(String deletedBy) {
    this.deletedBy = deletedBy;
  }

  public MaintSchedule deletedByDisplayName(String deletedByDisplayName) {
    this.deletedByDisplayName = deletedByDisplayName;
    return this;
  }

   /**
   * Get deletedByDisplayName
   * @return deletedByDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getDeletedByDisplayName() {
    return deletedByDisplayName;
  }

  public void setDeletedByDisplayName(String deletedByDisplayName) {
    this.deletedByDisplayName = deletedByDisplayName;
  }

  public MaintSchedule description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MaintSchedule endTime(Long endTime) {
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

  public MaintSchedule finalizeDayOfMonth(FinalizeDayOfMonthEnum finalizeDayOfMonth) {
    this.finalizeDayOfMonth = finalizeDayOfMonth;
    return this;
  }

   /**
   * Get finalizeDayOfMonth
   * @return finalizeDayOfMonth
  **/
  @ApiModelProperty(value = "")
  public FinalizeDayOfMonthEnum getFinalizeDayOfMonth() {
    return finalizeDayOfMonth;
  }

  public void setFinalizeDayOfMonth(FinalizeDayOfMonthEnum finalizeDayOfMonth) {
    this.finalizeDayOfMonth = finalizeDayOfMonth;
  }

  public MaintSchedule finalizeDayOfWeek(FinalizeDayOfWeekEnum finalizeDayOfWeek) {
    this.finalizeDayOfWeek = finalizeDayOfWeek;
    return this;
  }

   /**
   * Get finalizeDayOfWeek
   * @return finalizeDayOfWeek
  **/
  @ApiModelProperty(value = "")
  public FinalizeDayOfWeekEnum getFinalizeDayOfWeek() {
    return finalizeDayOfWeek;
  }

  public void setFinalizeDayOfWeek(FinalizeDayOfWeekEnum finalizeDayOfWeek) {
    this.finalizeDayOfWeek = finalizeDayOfWeek;
  }

  public MaintSchedule finalizeHourOfDay(Integer finalizeHourOfDay) {
    this.finalizeHourOfDay = finalizeHourOfDay;
    return this;
  }

   /**
   * Get finalizeHourOfDay
   * @return finalizeHourOfDay
  **/
  @ApiModelProperty(value = "")
  public Integer getFinalizeHourOfDay() {
    return finalizeHourOfDay;
  }

  public void setFinalizeHourOfDay(Integer finalizeHourOfDay) {
    this.finalizeHourOfDay = finalizeHourOfDay;
  }

  public MaintSchedule finalizeMaximumHours(Long finalizeMaximumHours) {
    this.finalizeMaximumHours = finalizeMaximumHours;
    return this;
  }

   /**
   * Get finalizeMaximumHours
   * @return finalizeMaximumHours
  **/
  @ApiModelProperty(value = "")
  public Long getFinalizeMaximumHours() {
    return finalizeMaximumHours;
  }

  public void setFinalizeMaximumHours(Long finalizeMaximumHours) {
    this.finalizeMaximumHours = finalizeMaximumHours;
  }

  public MaintSchedule finalizeMinuteOfHour(Integer finalizeMinuteOfHour) {
    this.finalizeMinuteOfHour = finalizeMinuteOfHour;
    return this;
  }

   /**
   * Get finalizeMinuteOfHour
   * @return finalizeMinuteOfHour
  **/
  @ApiModelProperty(value = "")
  public Integer getFinalizeMinuteOfHour() {
    return finalizeMinuteOfHour;
  }

  public void setFinalizeMinuteOfHour(Integer finalizeMinuteOfHour) {
    this.finalizeMinuteOfHour = finalizeMinuteOfHour;
  }

  public MaintSchedule finalizeTime(Long finalizeTime) {
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

  public MaintSchedule finalizeTimeZoneId(String finalizeTimeZoneId) {
    this.finalizeTimeZoneId = finalizeTimeZoneId;
    return this;
  }

   /**
   * Get finalizeTimeZoneId
   * @return finalizeTimeZoneId
  **/
  @ApiModelProperty(value = "")
  public String getFinalizeTimeZoneId() {
    return finalizeTimeZoneId;
  }

  public void setFinalizeTimeZoneId(String finalizeTimeZoneId) {
    this.finalizeTimeZoneId = finalizeTimeZoneId;
  }

  public MaintSchedule id(Long id) {
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

  public MaintSchedule lastRunTime(Long lastRunTime) {
    this.lastRunTime = lastRunTime;
    return this;
  }

   /**
   * Get lastRunTime
   * @return lastRunTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastRunTime() {
    return lastRunTime;
  }

  public void setLastRunTime(Long lastRunTime) {
    this.lastRunTime = lastRunTime;
  }

  public MaintSchedule maximumRunTime(Long maximumRunTime) {
    this.maximumRunTime = maximumRunTime;
    return this;
  }

   /**
   * Get maximumRunTime
   * @return maximumRunTime
  **/
  @ApiModelProperty(value = "")
  public Long getMaximumRunTime() {
    return maximumRunTime;
  }

  public void setMaximumRunTime(Long maximumRunTime) {
    this.maximumRunTime = maximumRunTime;
  }

  public MaintSchedule name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MaintSchedule nextRunTime(Long nextRunTime) {
    this.nextRunTime = nextRunTime;
    return this;
  }

   /**
   * Get nextRunTime
   * @return nextRunTime
  **/
  @ApiModelProperty(value = "")
  public Long getNextRunTime() {
    return nextRunTime;
  }

  public void setNextRunTime(Long nextRunTime) {
    this.nextRunTime = nextRunTime;
  }

  public MaintSchedule pauseBetweenIterations(Long pauseBetweenIterations) {
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

  public MaintSchedule repeatInterval(Integer repeatInterval) {
    this.repeatInterval = repeatInterval;
    return this;
  }

   /**
   * Get repeatInterval
   * @return repeatInterval
  **/
  @ApiModelProperty(value = "")
  public Integer getRepeatInterval() {
    return repeatInterval;
  }

  public void setRepeatInterval(Integer repeatInterval) {
    this.repeatInterval = repeatInterval;
  }

  public MaintSchedule repeatIntervalUnit(RepeatIntervalUnitEnum repeatIntervalUnit) {
    this.repeatIntervalUnit = repeatIntervalUnit;
    return this;
  }

   /**
   * Get repeatIntervalUnit
   * @return repeatIntervalUnit
  **/
  @ApiModelProperty(value = "")
  public RepeatIntervalUnitEnum getRepeatIntervalUnit() {
    return repeatIntervalUnit;
  }

  public void setRepeatIntervalUnit(RepeatIntervalUnitEnum repeatIntervalUnit) {
    this.repeatIntervalUnit = repeatIntervalUnit;
  }

  public MaintSchedule resumeArchive(Boolean resumeArchive) {
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

  public MaintSchedule scheduleType(ScheduleTypeEnum scheduleType) {
    this.scheduleType = scheduleType;
    return this;
  }

   /**
   * Get scheduleType
   * @return scheduleType
  **/
  @ApiModelProperty(value = "")
  public ScheduleTypeEnum getScheduleType() {
    return scheduleType;
  }

  public void setScheduleType(ScheduleTypeEnum scheduleType) {
    this.scheduleType = scheduleType;
  }

  public MaintSchedule startTime(Long startTime) {
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

  public MaintSchedule updateDate(Long updateDate) {
    this.updateDate = updateDate;
    return this;
  }

   /**
   * Get updateDate
   * @return updateDate
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Long updateDate) {
    this.updateDate = updateDate;
  }

  public MaintSchedule updatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

   /**
   * Get updatedBy
   * @return updatedBy
  **/
  @ApiModelProperty(value = "")
  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public MaintSchedule updatedByDisplayName(String updatedByDisplayName) {
    this.updatedByDisplayName = updatedByDisplayName;
    return this;
  }

   /**
   * Get updatedByDisplayName
   * @return updatedByDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getUpdatedByDisplayName() {
    return updatedByDisplayName;
  }

  public void setUpdatedByDisplayName(String updatedByDisplayName) {
    this.updatedByDisplayName = updatedByDisplayName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintSchedule maintSchedule = (MaintSchedule) o;
    return Objects.equals(this.active, maintSchedule.active) &&
        Objects.equals(this.allCleanupOlderThanDays, maintSchedule.allCleanupOlderThanDays) &&
        Objects.equals(this.archiveConcurrent, maintSchedule.archiveConcurrent) &&
        Objects.equals(this.cancelRunningCleanup, maintSchedule.cancelRunningCleanup) &&
        Objects.equals(this.cleanupEntityItems, maintSchedule.cleanupEntityItems) &&
        Objects.equals(this.createDate, maintSchedule.createDate) &&
        Objects.equals(this.createdBy, maintSchedule.createdBy) &&
        Objects.equals(this.createdByDisplayName, maintSchedule.createdByDisplayName) &&
        Objects.equals(this.dayOfMonth, maintSchedule.dayOfMonth) &&
        Objects.equals(this.dayOfWeek, maintSchedule.dayOfWeek) &&
        Objects.equals(this.deleteDate, maintSchedule.deleteDate) &&
        Objects.equals(this.deleted, maintSchedule.deleted) &&
        Objects.equals(this.deletedBy, maintSchedule.deletedBy) &&
        Objects.equals(this.deletedByDisplayName, maintSchedule.deletedByDisplayName) &&
        Objects.equals(this.description, maintSchedule.description) &&
        Objects.equals(this.endTime, maintSchedule.endTime) &&
        Objects.equals(this.finalizeDayOfMonth, maintSchedule.finalizeDayOfMonth) &&
        Objects.equals(this.finalizeDayOfWeek, maintSchedule.finalizeDayOfWeek) &&
        Objects.equals(this.finalizeHourOfDay, maintSchedule.finalizeHourOfDay) &&
        Objects.equals(this.finalizeMaximumHours, maintSchedule.finalizeMaximumHours) &&
        Objects.equals(this.finalizeMinuteOfHour, maintSchedule.finalizeMinuteOfHour) &&
        Objects.equals(this.finalizeTime, maintSchedule.finalizeTime) &&
        Objects.equals(this.finalizeTimeZoneId, maintSchedule.finalizeTimeZoneId) &&
        Objects.equals(this.id, maintSchedule.id) &&
        Objects.equals(this.lastRunTime, maintSchedule.lastRunTime) &&
        Objects.equals(this.maximumRunTime, maintSchedule.maximumRunTime) &&
        Objects.equals(this.name, maintSchedule.name) &&
        Objects.equals(this.nextRunTime, maintSchedule.nextRunTime) &&
        Objects.equals(this.pauseBetweenIterations, maintSchedule.pauseBetweenIterations) &&
        Objects.equals(this.repeatInterval, maintSchedule.repeatInterval) &&
        Objects.equals(this.repeatIntervalUnit, maintSchedule.repeatIntervalUnit) &&
        Objects.equals(this.resumeArchive, maintSchedule.resumeArchive) &&
        Objects.equals(this.scheduleType, maintSchedule.scheduleType) &&
        Objects.equals(this.startTime, maintSchedule.startTime) &&
        Objects.equals(this.updateDate, maintSchedule.updateDate) &&
        Objects.equals(this.updatedBy, maintSchedule.updatedBy) &&
        Objects.equals(this.updatedByDisplayName, maintSchedule.updatedByDisplayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, allCleanupOlderThanDays, archiveConcurrent, cancelRunningCleanup, cleanupEntityItems, createDate, createdBy, createdByDisplayName, dayOfMonth, dayOfWeek, deleteDate, deleted, deletedBy, deletedByDisplayName, description, endTime, finalizeDayOfMonth, finalizeDayOfWeek, finalizeHourOfDay, finalizeMaximumHours, finalizeMinuteOfHour, finalizeTime, finalizeTimeZoneId, id, lastRunTime, maximumRunTime, name, nextRunTime, pauseBetweenIterations, repeatInterval, repeatIntervalUnit, resumeArchive, scheduleType, startTime, updateDate, updatedBy, updatedByDisplayName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintSchedule {\n");
    
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    allCleanupOlderThanDays: ").append(toIndentedString(allCleanupOlderThanDays)).append("\n");
    sb.append("    archiveConcurrent: ").append(toIndentedString(archiveConcurrent)).append("\n");
    sb.append("    cancelRunningCleanup: ").append(toIndentedString(cancelRunningCleanup)).append("\n");
    sb.append("    cleanupEntityItems: ").append(toIndentedString(cleanupEntityItems)).append("\n");
    sb.append("    createDate: ").append(toIndentedString(createDate)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    createdByDisplayName: ").append(toIndentedString(createdByDisplayName)).append("\n");
    sb.append("    dayOfMonth: ").append(toIndentedString(dayOfMonth)).append("\n");
    sb.append("    dayOfWeek: ").append(toIndentedString(dayOfWeek)).append("\n");
    sb.append("    deleteDate: ").append(toIndentedString(deleteDate)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    deletedBy: ").append(toIndentedString(deletedBy)).append("\n");
    sb.append("    deletedByDisplayName: ").append(toIndentedString(deletedByDisplayName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    finalizeDayOfMonth: ").append(toIndentedString(finalizeDayOfMonth)).append("\n");
    sb.append("    finalizeDayOfWeek: ").append(toIndentedString(finalizeDayOfWeek)).append("\n");
    sb.append("    finalizeHourOfDay: ").append(toIndentedString(finalizeHourOfDay)).append("\n");
    sb.append("    finalizeMaximumHours: ").append(toIndentedString(finalizeMaximumHours)).append("\n");
    sb.append("    finalizeMinuteOfHour: ").append(toIndentedString(finalizeMinuteOfHour)).append("\n");
    sb.append("    finalizeTime: ").append(toIndentedString(finalizeTime)).append("\n");
    sb.append("    finalizeTimeZoneId: ").append(toIndentedString(finalizeTimeZoneId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lastRunTime: ").append(toIndentedString(lastRunTime)).append("\n");
    sb.append("    maximumRunTime: ").append(toIndentedString(maximumRunTime)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    nextRunTime: ").append(toIndentedString(nextRunTime)).append("\n");
    sb.append("    pauseBetweenIterations: ").append(toIndentedString(pauseBetweenIterations)).append("\n");
    sb.append("    repeatInterval: ").append(toIndentedString(repeatInterval)).append("\n");
    sb.append("    repeatIntervalUnit: ").append(toIndentedString(repeatIntervalUnit)).append("\n");
    sb.append("    resumeArchive: ").append(toIndentedString(resumeArchive)).append("\n");
    sb.append("    scheduleType: ").append(toIndentedString(scheduleType)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    updateDate: ").append(toIndentedString(updateDate)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
    sb.append("    updatedByDisplayName: ").append(toIndentedString(updatedByDisplayName)).append("\n");
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
