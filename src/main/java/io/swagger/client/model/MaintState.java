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
import io.swagger.client.model.ArcDisableEnable;
import io.swagger.client.model.Archival;
import io.swagger.client.model.Cleanup;
import io.swagger.client.model.EnterMaintProgress;
import java.io.IOException;
/**
 * MaintState
 */



public class MaintState {
  @SerializedName("archiveDisableEnable")
  private ArcDisableEnable archiveDisableEnable = null;

  @SerializedName("canResumeLastArchive")
  private Boolean canResumeLastArchive = null;

  @SerializedName("cepEnabled")
  private Boolean cepEnabled = null;

  /**
   * Gets or Sets disableEnableStatus
   */
  @JsonAdapter(DisableEnableStatusEnum.Adapter.class)
  public enum DisableEnableStatusEnum {
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

    DisableEnableStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DisableEnableStatusEnum fromValue(String input) {
      for (DisableEnableStatusEnum b : DisableEnableStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DisableEnableStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DisableEnableStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DisableEnableStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DisableEnableStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("disableEnableStatus")
  private DisableEnableStatusEnum disableEnableStatus = null;

  @SerializedName("enteringMaintProgress")
  private EnterMaintProgress enteringMaintProgress = null;

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

  /**
   * Gets or Sets lastArchiveState
   */
  @JsonAdapter(LastArchiveStateEnum.Adapter.class)
  public enum LastArchiveStateEnum {
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("FAILED")
    FAILED("FAILED"),
    @SerializedName("CANCELED")
    CANCELED("CANCELED"),
    @SerializedName("DISABLING")
    DISABLING("DISABLING"),
    @SerializedName("ENABLING")
    ENABLING("ENABLING"),
    @SerializedName("DISABLED")
    DISABLED("DISABLED");

    private String value;

    LastArchiveStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static LastArchiveStateEnum fromValue(String input) {
      for (LastArchiveStateEnum b : LastArchiveStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<LastArchiveStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final LastArchiveStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public LastArchiveStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return LastArchiveStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("lastArchiveState")
  private LastArchiveStateEnum lastArchiveState = null;

  @SerializedName("runningArchival")
  private Archival runningArchival = null;

  @SerializedName("runningCleanup")
  private Cleanup runningCleanup = null;

  /**
   * Gets or Sets waitState
   */
  @JsonAdapter(WaitStateEnum.Adapter.class)
  public enum WaitStateEnum {
    @SerializedName("WAIT_CONTINUE_OR_EXIT")
    CONTINUE_OR_EXIT("WAIT_CONTINUE_OR_EXIT"),
    @SerializedName("WAIT_EXIT")
    EXIT("WAIT_EXIT");

    private String value;

    WaitStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static WaitStateEnum fromValue(String input) {
      for (WaitStateEnum b : WaitStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<WaitStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final WaitStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public WaitStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return WaitStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("waitState")
  private WaitStateEnum waitState = null;

  public MaintState archiveDisableEnable(ArcDisableEnable archiveDisableEnable) {
    this.archiveDisableEnable = archiveDisableEnable;
    return this;
  }

   /**
   * Get archiveDisableEnable
   * @return archiveDisableEnable
  **/
  @ApiModelProperty(value = "")
  public ArcDisableEnable getArchiveDisableEnable() {
    return archiveDisableEnable;
  }

  public void setArchiveDisableEnable(ArcDisableEnable archiveDisableEnable) {
    this.archiveDisableEnable = archiveDisableEnable;
  }

  public MaintState canResumeLastArchive(Boolean canResumeLastArchive) {
    this.canResumeLastArchive = canResumeLastArchive;
    return this;
  }

   /**
   * Get canResumeLastArchive
   * @return canResumeLastArchive
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanResumeLastArchive() {
    return canResumeLastArchive;
  }

  public void setCanResumeLastArchive(Boolean canResumeLastArchive) {
    this.canResumeLastArchive = canResumeLastArchive;
  }

  public MaintState cepEnabled(Boolean cepEnabled) {
    this.cepEnabled = cepEnabled;
    return this;
  }

   /**
   * Get cepEnabled
   * @return cepEnabled
  **/
  @ApiModelProperty(value = "")
  public Boolean isCepEnabled() {
    return cepEnabled;
  }

  public void setCepEnabled(Boolean cepEnabled) {
    this.cepEnabled = cepEnabled;
  }

  public MaintState disableEnableStatus(DisableEnableStatusEnum disableEnableStatus) {
    this.disableEnableStatus = disableEnableStatus;
    return this;
  }

   /**
   * Get disableEnableStatus
   * @return disableEnableStatus
  **/
  @ApiModelProperty(value = "")
  public DisableEnableStatusEnum getDisableEnableStatus() {
    return disableEnableStatus;
  }

  public void setDisableEnableStatus(DisableEnableStatusEnum disableEnableStatus) {
    this.disableEnableStatus = disableEnableStatus;
  }

  public MaintState enteringMaintProgress(EnterMaintProgress enteringMaintProgress) {
    this.enteringMaintProgress = enteringMaintProgress;
    return this;
  }

   /**
   * Get enteringMaintProgress
   * @return enteringMaintProgress
  **/
  @ApiModelProperty(value = "")
  public EnterMaintProgress getEnteringMaintProgress() {
    return enteringMaintProgress;
  }

  public void setEnteringMaintProgress(EnterMaintProgress enteringMaintProgress) {
    this.enteringMaintProgress = enteringMaintProgress;
  }

  public MaintState executionMode(ExecutionModeEnum executionMode) {
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

  public MaintState lastArchiveState(LastArchiveStateEnum lastArchiveState) {
    this.lastArchiveState = lastArchiveState;
    return this;
  }

   /**
   * Get lastArchiveState
   * @return lastArchiveState
  **/
  @ApiModelProperty(value = "")
  public LastArchiveStateEnum getLastArchiveState() {
    return lastArchiveState;
  }

  public void setLastArchiveState(LastArchiveStateEnum lastArchiveState) {
    this.lastArchiveState = lastArchiveState;
  }

  public MaintState runningArchival(Archival runningArchival) {
    this.runningArchival = runningArchival;
    return this;
  }

   /**
   * Get runningArchival
   * @return runningArchival
  **/
  @ApiModelProperty(value = "")
  public Archival getRunningArchival() {
    return runningArchival;
  }

  public void setRunningArchival(Archival runningArchival) {
    this.runningArchival = runningArchival;
  }

  public MaintState runningCleanup(Cleanup runningCleanup) {
    this.runningCleanup = runningCleanup;
    return this;
  }

   /**
   * Get runningCleanup
   * @return runningCleanup
  **/
  @ApiModelProperty(value = "")
  public Cleanup getRunningCleanup() {
    return runningCleanup;
  }

  public void setRunningCleanup(Cleanup runningCleanup) {
    this.runningCleanup = runningCleanup;
  }

  public MaintState waitState(WaitStateEnum waitState) {
    this.waitState = waitState;
    return this;
  }

   /**
   * Get waitState
   * @return waitState
  **/
  @ApiModelProperty(value = "")
  public WaitStateEnum getWaitState() {
    return waitState;
  }

  public void setWaitState(WaitStateEnum waitState) {
    this.waitState = waitState;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintState maintState = (MaintState) o;
    return Objects.equals(this.archiveDisableEnable, maintState.archiveDisableEnable) &&
        Objects.equals(this.canResumeLastArchive, maintState.canResumeLastArchive) &&
        Objects.equals(this.cepEnabled, maintState.cepEnabled) &&
        Objects.equals(this.disableEnableStatus, maintState.disableEnableStatus) &&
        Objects.equals(this.enteringMaintProgress, maintState.enteringMaintProgress) &&
        Objects.equals(this.executionMode, maintState.executionMode) &&
        Objects.equals(this.lastArchiveState, maintState.lastArchiveState) &&
        Objects.equals(this.runningArchival, maintState.runningArchival) &&
        Objects.equals(this.runningCleanup, maintState.runningCleanup) &&
        Objects.equals(this.waitState, maintState.waitState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(archiveDisableEnable, canResumeLastArchive, cepEnabled, disableEnableStatus, enteringMaintProgress, executionMode, lastArchiveState, runningArchival, runningCleanup, waitState);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintState {\n");
    
    sb.append("    archiveDisableEnable: ").append(toIndentedString(archiveDisableEnable)).append("\n");
    sb.append("    canResumeLastArchive: ").append(toIndentedString(canResumeLastArchive)).append("\n");
    sb.append("    cepEnabled: ").append(toIndentedString(cepEnabled)).append("\n");
    sb.append("    disableEnableStatus: ").append(toIndentedString(disableEnableStatus)).append("\n");
    sb.append("    enteringMaintProgress: ").append(toIndentedString(enteringMaintProgress)).append("\n");
    sb.append("    executionMode: ").append(toIndentedString(executionMode)).append("\n");
    sb.append("    lastArchiveState: ").append(toIndentedString(lastArchiveState)).append("\n");
    sb.append("    runningArchival: ").append(toIndentedString(runningArchival)).append("\n");
    sb.append("    runningCleanup: ").append(toIndentedString(runningCleanup)).append("\n");
    sb.append("    waitState: ").append(toIndentedString(waitState)).append("\n");
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