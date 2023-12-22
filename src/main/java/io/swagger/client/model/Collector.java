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
import io.swagger.client.model.AttributeJoins;
import io.swagger.client.model.ResolutionAttributes;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Collector
 */



public class Collector {
  @SerializedName("appSourceId")
  private Long appSourceId = null;

  @SerializedName("collectorAttributeJoins")
  private AttributeJoins collectorAttributeJoins = null;

  @SerializedName("collectorId")
  private Long collectorId = null;

  /**
   * Gets or Sets collectorType
   */
  @JsonAdapter(CollectorTypeEnum.Adapter.class)
  public enum CollectorTypeEnum {
    @SerializedName("IDENTITY")
    IDENTITY("IDENTITY"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("ROLE")
    ROLE("ROLE"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION"),
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("FULFILLMENT")
    FULFILLMENT("FULFILLMENT"),
    @SerializedName("SYSTEM_FULFILLMENT")
    SYSTEM_FULFILLMENT("SYSTEM_FULFILLMENT"),
    @SerializedName("CURATION")
    CURATION("CURATION"),
    @SerializedName("GRAVEYARD")
    GRAVEYARD("GRAVEYARD"),
    @SerializedName("APP_COLLECTOR")
    APP_COLLECTOR("APP_COLLECTOR"),
    @SerializedName("HISTORY")
    HISTORY("HISTORY");

    private String value;

    CollectorTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static CollectorTypeEnum fromValue(String input) {
      for (CollectorTypeEnum b : CollectorTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<CollectorTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CollectorTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public CollectorTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return CollectorTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("collectorType")
  private CollectorTypeEnum collectorType = null;

  @SerializedName("configuration")
  private String _configuration = null;

  @SerializedName("dataSourceConnectionId")
  private Long dataSourceConnectionId = null;

  @SerializedName("dataSourceConnectionUniqueId")
  private String dataSourceConnectionUniqueId = null;

  @SerializedName("defaultRtcEventPollingInterval")
  private Long defaultRtcEventPollingInterval = null;

  @SerializedName("defaultRtcMaxPollingTimeout")
  private Long defaultRtcMaxPollingTimeout = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("fulfillmentTargetId")
  private Long fulfillmentTargetId = null;

  @SerializedName("hasPublished")
  private Boolean hasPublished = null;

  @SerializedName("identitySourceId")
  private Long identitySourceId = null;

  @SerializedName("lastCollectionMessage")
  private String lastCollectionMessage = null;

  /**
   * Gets or Sets lastCollectionStatus
   */
  @JsonAdapter(LastCollectionStatusEnum.Adapter.class)
  public enum LastCollectionStatusEnum {
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

    LastCollectionStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static LastCollectionStatusEnum fromValue(String input) {
      for (LastCollectionStatusEnum b : LastCollectionStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<LastCollectionStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final LastCollectionStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public LastCollectionStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return LastCollectionStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("lastCollectionStatus")
  private LastCollectionStatusEnum lastCollectionStatus = null;

  @SerializedName("lastCollectionTime")
  private Long lastCollectionTime = null;

  @SerializedName("lastUpdateTime")
  private Long lastUpdateTime = null;

  @SerializedName("lastUpdatedBy")
  private User lastUpdatedBy = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkSchedules")
  private String linkSchedules = null;

  @SerializedName("linkStatus")
  private String linkStatus = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("resolutionAttributes")
  private List<ResolutionAttributes> resolutionAttributes = null;

  @SerializedName("rtcEventPollingInterval")
  private Long rtcEventPollingInterval = null;

  @SerializedName("rtcLastPollTime")
  private Long rtcLastPollTime = null;

  @SerializedName("rtcMaxPollingTimeout")
  private Long rtcMaxPollingTimeout = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("DISABLED")
    DISABLED("DISABLED");

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

  @SerializedName("templateId")
  private Long templateId = null;

  @SerializedName("templateUniqueId")
  private String templateUniqueId = null;

  @SerializedName("templateVersion")
  private String templateVersion = null;

  @SerializedName("version")
  private String version = null;

  public Collector appSourceId(Long appSourceId) {
    this.appSourceId = appSourceId;
    return this;
  }

   /**
   * Get appSourceId
   * @return appSourceId
  **/
  @ApiModelProperty(value = "")
  public Long getAppSourceId() {
    return appSourceId;
  }

  public void setAppSourceId(Long appSourceId) {
    this.appSourceId = appSourceId;
  }

  public Collector collectorAttributeJoins(AttributeJoins collectorAttributeJoins) {
    this.collectorAttributeJoins = collectorAttributeJoins;
    return this;
  }

   /**
   * Get collectorAttributeJoins
   * @return collectorAttributeJoins
  **/
  @ApiModelProperty(value = "")
  public AttributeJoins getCollectorAttributeJoins() {
    return collectorAttributeJoins;
  }

  public void setCollectorAttributeJoins(AttributeJoins collectorAttributeJoins) {
    this.collectorAttributeJoins = collectorAttributeJoins;
  }

  public Collector collectorId(Long collectorId) {
    this.collectorId = collectorId;
    return this;
  }

   /**
   * Get collectorId
   * @return collectorId
  **/
  @ApiModelProperty(value = "")
  public Long getCollectorId() {
    return collectorId;
  }

  public void setCollectorId(Long collectorId) {
    this.collectorId = collectorId;
  }

  public Collector collectorType(CollectorTypeEnum collectorType) {
    this.collectorType = collectorType;
    return this;
  }

   /**
   * Get collectorType
   * @return collectorType
  **/
  @ApiModelProperty(value = "")
  public CollectorTypeEnum getCollectorType() {
    return collectorType;
  }

  public void setCollectorType(CollectorTypeEnum collectorType) {
    this.collectorType = collectorType;
  }

  public Collector _configuration(String _configuration) {
    this._configuration = _configuration;
    return this;
  }

   /**
   * Get _configuration
   * @return _configuration
  **/
  @ApiModelProperty(value = "")
  public String getConfiguration() {
    return _configuration;
  }

  public void setConfiguration(String _configuration) {
    this._configuration = _configuration;
  }

  public Collector dataSourceConnectionId(Long dataSourceConnectionId) {
    this.dataSourceConnectionId = dataSourceConnectionId;
    return this;
  }

   /**
   * Get dataSourceConnectionId
   * @return dataSourceConnectionId
  **/
  @ApiModelProperty(value = "")
  public Long getDataSourceConnectionId() {
    return dataSourceConnectionId;
  }

  public void setDataSourceConnectionId(Long dataSourceConnectionId) {
    this.dataSourceConnectionId = dataSourceConnectionId;
  }

  public Collector dataSourceConnectionUniqueId(String dataSourceConnectionUniqueId) {
    this.dataSourceConnectionUniqueId = dataSourceConnectionUniqueId;
    return this;
  }

   /**
   * Get dataSourceConnectionUniqueId
   * @return dataSourceConnectionUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getDataSourceConnectionUniqueId() {
    return dataSourceConnectionUniqueId;
  }

  public void setDataSourceConnectionUniqueId(String dataSourceConnectionUniqueId) {
    this.dataSourceConnectionUniqueId = dataSourceConnectionUniqueId;
  }

  public Collector defaultRtcEventPollingInterval(Long defaultRtcEventPollingInterval) {
    this.defaultRtcEventPollingInterval = defaultRtcEventPollingInterval;
    return this;
  }

   /**
   * Get defaultRtcEventPollingInterval
   * @return defaultRtcEventPollingInterval
  **/
  @ApiModelProperty(value = "")
  public Long getDefaultRtcEventPollingInterval() {
    return defaultRtcEventPollingInterval;
  }

  public void setDefaultRtcEventPollingInterval(Long defaultRtcEventPollingInterval) {
    this.defaultRtcEventPollingInterval = defaultRtcEventPollingInterval;
  }

  public Collector defaultRtcMaxPollingTimeout(Long defaultRtcMaxPollingTimeout) {
    this.defaultRtcMaxPollingTimeout = defaultRtcMaxPollingTimeout;
    return this;
  }

   /**
   * Get defaultRtcMaxPollingTimeout
   * @return defaultRtcMaxPollingTimeout
  **/
  @ApiModelProperty(value = "")
  public Long getDefaultRtcMaxPollingTimeout() {
    return defaultRtcMaxPollingTimeout;
  }

  public void setDefaultRtcMaxPollingTimeout(Long defaultRtcMaxPollingTimeout) {
    this.defaultRtcMaxPollingTimeout = defaultRtcMaxPollingTimeout;
  }

  public Collector description(String description) {
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

  public Collector fulfillmentTargetId(Long fulfillmentTargetId) {
    this.fulfillmentTargetId = fulfillmentTargetId;
    return this;
  }

   /**
   * Get fulfillmentTargetId
   * @return fulfillmentTargetId
  **/
  @ApiModelProperty(value = "")
  public Long getFulfillmentTargetId() {
    return fulfillmentTargetId;
  }

  public void setFulfillmentTargetId(Long fulfillmentTargetId) {
    this.fulfillmentTargetId = fulfillmentTargetId;
  }

  public Collector hasPublished(Boolean hasPublished) {
    this.hasPublished = hasPublished;
    return this;
  }

   /**
   * Get hasPublished
   * @return hasPublished
  **/
  @ApiModelProperty(value = "")
  public Boolean isHasPublished() {
    return hasPublished;
  }

  public void setHasPublished(Boolean hasPublished) {
    this.hasPublished = hasPublished;
  }

  public Collector identitySourceId(Long identitySourceId) {
    this.identitySourceId = identitySourceId;
    return this;
  }

   /**
   * Get identitySourceId
   * @return identitySourceId
  **/
  @ApiModelProperty(value = "")
  public Long getIdentitySourceId() {
    return identitySourceId;
  }

  public void setIdentitySourceId(Long identitySourceId) {
    this.identitySourceId = identitySourceId;
  }

  public Collector lastCollectionMessage(String lastCollectionMessage) {
    this.lastCollectionMessage = lastCollectionMessage;
    return this;
  }

   /**
   * Get lastCollectionMessage
   * @return lastCollectionMessage
  **/
  @ApiModelProperty(value = "")
  public String getLastCollectionMessage() {
    return lastCollectionMessage;
  }

  public void setLastCollectionMessage(String lastCollectionMessage) {
    this.lastCollectionMessage = lastCollectionMessage;
  }

  public Collector lastCollectionStatus(LastCollectionStatusEnum lastCollectionStatus) {
    this.lastCollectionStatus = lastCollectionStatus;
    return this;
  }

   /**
   * Get lastCollectionStatus
   * @return lastCollectionStatus
  **/
  @ApiModelProperty(value = "")
  public LastCollectionStatusEnum getLastCollectionStatus() {
    return lastCollectionStatus;
  }

  public void setLastCollectionStatus(LastCollectionStatusEnum lastCollectionStatus) {
    this.lastCollectionStatus = lastCollectionStatus;
  }

  public Collector lastCollectionTime(Long lastCollectionTime) {
    this.lastCollectionTime = lastCollectionTime;
    return this;
  }

   /**
   * Get lastCollectionTime
   * @return lastCollectionTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastCollectionTime() {
    return lastCollectionTime;
  }

  public void setLastCollectionTime(Long lastCollectionTime) {
    this.lastCollectionTime = lastCollectionTime;
  }

  public Collector lastUpdateTime(Long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
    return this;
  }

   /**
   * Get lastUpdateTime
   * @return lastUpdateTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public Collector lastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
    return this;
  }

   /**
   * Get lastUpdatedBy
   * @return lastUpdatedBy
  **/
  @ApiModelProperty(value = "")
  public User getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public Collector link(String link) {
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

  public Collector linkSchedules(String linkSchedules) {
    this.linkSchedules = linkSchedules;
    return this;
  }

   /**
   * Get linkSchedules
   * @return linkSchedules
  **/
  @ApiModelProperty(value = "")
  public String getLinkSchedules() {
    return linkSchedules;
  }

  public void setLinkSchedules(String linkSchedules) {
    this.linkSchedules = linkSchedules;
  }

  public Collector linkStatus(String linkStatus) {
    this.linkStatus = linkStatus;
    return this;
  }

   /**
   * Get linkStatus
   * @return linkStatus
  **/
  @ApiModelProperty(value = "")
  public String getLinkStatus() {
    return linkStatus;
  }

  public void setLinkStatus(String linkStatus) {
    this.linkStatus = linkStatus;
  }

  public Collector name(String name) {
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

  public Collector resolutionAttributes(List<ResolutionAttributes> resolutionAttributes) {
    this.resolutionAttributes = resolutionAttributes;
    return this;
  }

  public Collector addResolutionAttributesItem(ResolutionAttributes resolutionAttributesItem) {
    if (this.resolutionAttributes == null) {
      this.resolutionAttributes = new ArrayList<ResolutionAttributes>();
    }
    this.resolutionAttributes.add(resolutionAttributesItem);
    return this;
  }

   /**
   * Get resolutionAttributes
   * @return resolutionAttributes
  **/
  @ApiModelProperty(value = "")
  public List<ResolutionAttributes> getResolutionAttributes() {
    return resolutionAttributes;
  }

  public void setResolutionAttributes(List<ResolutionAttributes> resolutionAttributes) {
    this.resolutionAttributes = resolutionAttributes;
  }

  public Collector rtcEventPollingInterval(Long rtcEventPollingInterval) {
    this.rtcEventPollingInterval = rtcEventPollingInterval;
    return this;
  }

   /**
   * Get rtcEventPollingInterval
   * @return rtcEventPollingInterval
  **/
  @ApiModelProperty(value = "")
  public Long getRtcEventPollingInterval() {
    return rtcEventPollingInterval;
  }

  public void setRtcEventPollingInterval(Long rtcEventPollingInterval) {
    this.rtcEventPollingInterval = rtcEventPollingInterval;
  }

  public Collector rtcLastPollTime(Long rtcLastPollTime) {
    this.rtcLastPollTime = rtcLastPollTime;
    return this;
  }

   /**
   * Get rtcLastPollTime
   * @return rtcLastPollTime
  **/
  @ApiModelProperty(value = "")
  public Long getRtcLastPollTime() {
    return rtcLastPollTime;
  }

  public void setRtcLastPollTime(Long rtcLastPollTime) {
    this.rtcLastPollTime = rtcLastPollTime;
  }

  public Collector rtcMaxPollingTimeout(Long rtcMaxPollingTimeout) {
    this.rtcMaxPollingTimeout = rtcMaxPollingTimeout;
    return this;
  }

   /**
   * Get rtcMaxPollingTimeout
   * @return rtcMaxPollingTimeout
  **/
  @ApiModelProperty(value = "")
  public Long getRtcMaxPollingTimeout() {
    return rtcMaxPollingTimeout;
  }

  public void setRtcMaxPollingTimeout(Long rtcMaxPollingTimeout) {
    this.rtcMaxPollingTimeout = rtcMaxPollingTimeout;
  }

  public Collector status(StatusEnum status) {
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

  public Collector templateId(Long templateId) {
    this.templateId = templateId;
    return this;
  }

   /**
   * Get templateId
   * @return templateId
  **/
  @ApiModelProperty(value = "")
  public Long getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Long templateId) {
    this.templateId = templateId;
  }

  public Collector templateUniqueId(String templateUniqueId) {
    this.templateUniqueId = templateUniqueId;
    return this;
  }

   /**
   * Get templateUniqueId
   * @return templateUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getTemplateUniqueId() {
    return templateUniqueId;
  }

  public void setTemplateUniqueId(String templateUniqueId) {
    this.templateUniqueId = templateUniqueId;
  }

  public Collector templateVersion(String templateVersion) {
    this.templateVersion = templateVersion;
    return this;
  }

   /**
   * Get templateVersion
   * @return templateVersion
  **/
  @ApiModelProperty(value = "")
  public String getTemplateVersion() {
    return templateVersion;
  }

  public void setTemplateVersion(String templateVersion) {
    this.templateVersion = templateVersion;
  }

  public Collector version(String version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(value = "")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Collector collector = (Collector) o;
    return Objects.equals(this.appSourceId, collector.appSourceId) &&
        Objects.equals(this.collectorAttributeJoins, collector.collectorAttributeJoins) &&
        Objects.equals(this.collectorId, collector.collectorId) &&
        Objects.equals(this.collectorType, collector.collectorType) &&
        Objects.equals(this._configuration, collector._configuration) &&
        Objects.equals(this.dataSourceConnectionId, collector.dataSourceConnectionId) &&
        Objects.equals(this.dataSourceConnectionUniqueId, collector.dataSourceConnectionUniqueId) &&
        Objects.equals(this.defaultRtcEventPollingInterval, collector.defaultRtcEventPollingInterval) &&
        Objects.equals(this.defaultRtcMaxPollingTimeout, collector.defaultRtcMaxPollingTimeout) &&
        Objects.equals(this.description, collector.description) &&
        Objects.equals(this.fulfillmentTargetId, collector.fulfillmentTargetId) &&
        Objects.equals(this.hasPublished, collector.hasPublished) &&
        Objects.equals(this.identitySourceId, collector.identitySourceId) &&
        Objects.equals(this.lastCollectionMessage, collector.lastCollectionMessage) &&
        Objects.equals(this.lastCollectionStatus, collector.lastCollectionStatus) &&
        Objects.equals(this.lastCollectionTime, collector.lastCollectionTime) &&
        Objects.equals(this.lastUpdateTime, collector.lastUpdateTime) &&
        Objects.equals(this.lastUpdatedBy, collector.lastUpdatedBy) &&
        Objects.equals(this.link, collector.link) &&
        Objects.equals(this.linkSchedules, collector.linkSchedules) &&
        Objects.equals(this.linkStatus, collector.linkStatus) &&
        Objects.equals(this.name, collector.name) &&
        Objects.equals(this.resolutionAttributes, collector.resolutionAttributes) &&
        Objects.equals(this.rtcEventPollingInterval, collector.rtcEventPollingInterval) &&
        Objects.equals(this.rtcLastPollTime, collector.rtcLastPollTime) &&
        Objects.equals(this.rtcMaxPollingTimeout, collector.rtcMaxPollingTimeout) &&
        Objects.equals(this.status, collector.status) &&
        Objects.equals(this.templateId, collector.templateId) &&
        Objects.equals(this.templateUniqueId, collector.templateUniqueId) &&
        Objects.equals(this.templateVersion, collector.templateVersion) &&
        Objects.equals(this.version, collector.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appSourceId, collectorAttributeJoins, collectorId, collectorType, _configuration, dataSourceConnectionId, dataSourceConnectionUniqueId, defaultRtcEventPollingInterval, defaultRtcMaxPollingTimeout, description, fulfillmentTargetId, hasPublished, identitySourceId, lastCollectionMessage, lastCollectionStatus, lastCollectionTime, lastUpdateTime, lastUpdatedBy, link, linkSchedules, linkStatus, name, resolutionAttributes, rtcEventPollingInterval, rtcLastPollTime, rtcMaxPollingTimeout, status, templateId, templateUniqueId, templateVersion, version);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Collector {\n");
    
    sb.append("    appSourceId: ").append(toIndentedString(appSourceId)).append("\n");
    sb.append("    collectorAttributeJoins: ").append(toIndentedString(collectorAttributeJoins)).append("\n");
    sb.append("    collectorId: ").append(toIndentedString(collectorId)).append("\n");
    sb.append("    collectorType: ").append(toIndentedString(collectorType)).append("\n");
    sb.append("    _configuration: ").append(toIndentedString(_configuration)).append("\n");
    sb.append("    dataSourceConnectionId: ").append(toIndentedString(dataSourceConnectionId)).append("\n");
    sb.append("    dataSourceConnectionUniqueId: ").append(toIndentedString(dataSourceConnectionUniqueId)).append("\n");
    sb.append("    defaultRtcEventPollingInterval: ").append(toIndentedString(defaultRtcEventPollingInterval)).append("\n");
    sb.append("    defaultRtcMaxPollingTimeout: ").append(toIndentedString(defaultRtcMaxPollingTimeout)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    fulfillmentTargetId: ").append(toIndentedString(fulfillmentTargetId)).append("\n");
    sb.append("    hasPublished: ").append(toIndentedString(hasPublished)).append("\n");
    sb.append("    identitySourceId: ").append(toIndentedString(identitySourceId)).append("\n");
    sb.append("    lastCollectionMessage: ").append(toIndentedString(lastCollectionMessage)).append("\n");
    sb.append("    lastCollectionStatus: ").append(toIndentedString(lastCollectionStatus)).append("\n");
    sb.append("    lastCollectionTime: ").append(toIndentedString(lastCollectionTime)).append("\n");
    sb.append("    lastUpdateTime: ").append(toIndentedString(lastUpdateTime)).append("\n");
    sb.append("    lastUpdatedBy: ").append(toIndentedString(lastUpdatedBy)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkSchedules: ").append(toIndentedString(linkSchedules)).append("\n");
    sb.append("    linkStatus: ").append(toIndentedString(linkStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    resolutionAttributes: ").append(toIndentedString(resolutionAttributes)).append("\n");
    sb.append("    rtcEventPollingInterval: ").append(toIndentedString(rtcEventPollingInterval)).append("\n");
    sb.append("    rtcLastPollTime: ").append(toIndentedString(rtcLastPollTime)).append("\n");
    sb.append("    rtcMaxPollingTimeout: ").append(toIndentedString(rtcMaxPollingTimeout)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    templateUniqueId: ").append(toIndentedString(templateUniqueId)).append("\n");
    sb.append("    templateVersion: ").append(toIndentedString(templateVersion)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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
