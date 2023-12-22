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
import java.util.ArrayList;
import java.util.List;
/**
 * Template
 */



public class Template {
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

  @SerializedName("config")
  private String config = null;

  @SerializedName("defaultRtcEventPollingInterval")
  private Long defaultRtcEventPollingInterval = null;

  @SerializedName("defaultRtcMaxPollingTimeout")
  private Long defaultRtcMaxPollingTimeout = null;

  @SerializedName("description")
  private String description = null;

  /**
   * Gets or Sets event
   */
  @JsonAdapter(EventEnum.Adapter.class)
  public enum EventEnum {
    @SerializedName("CREATED")
    CREATED("CREATED"),
    @SerializedName("UPDATED")
    UPDATED("UPDATED"),
    @SerializedName("ACTIVATED")
    ACTIVATED("ACTIVATED"),
    @SerializedName("DEACTIVATED")
    DEACTIVATED("DEACTIVATED"),
    @SerializedName("DELETED")
    DELETED("DELETED");

    private String value;

    EventEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static EventEnum fromValue(String input) {
      for (EventEnum b : EventEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<EventEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EventEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public EventEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return EventEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("event")
  private EventEnum event = null;

  @SerializedName("eventTime")
  private Long eventTime = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("name")
  private String name = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("DELETED")
    DELETED("DELETED");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StateEnum fromValue(String input) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("state")
  private StateEnum state = null;

  @SerializedName("templateId")
  private Long templateId = null;

  @SerializedName("templateUniqueId")
  private String templateUniqueId = null;

  /**
   * Gets or Sets type
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {
    @SerializedName("DEFAULT")
    DEFAULT("DEFAULT"),
    @SerializedName("CUSTOM")
    CUSTOM("CUSTOM");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static TypeEnum fromValue(String input) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<TypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public TypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return TypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("type")
  private TypeEnum type = null;

  @SerializedName("typeDisplayName")
  private String typeDisplayName = null;

  @SerializedName("user")
  private String user = null;

  @SerializedName("version")
  private String version = null;

  @SerializedName("views")
  private List<String> views = null;

  public Template collectorType(CollectorTypeEnum collectorType) {
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

  public Template config(String config) {
    this.config = config;
    return this;
  }

   /**
   * Get config
   * @return config
  **/
  @ApiModelProperty(value = "")
  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public Template defaultRtcEventPollingInterval(Long defaultRtcEventPollingInterval) {
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

  public Template defaultRtcMaxPollingTimeout(Long defaultRtcMaxPollingTimeout) {
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

  public Template description(String description) {
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

  public Template event(EventEnum event) {
    this.event = event;
    return this;
  }

   /**
   * Get event
   * @return event
  **/
  @ApiModelProperty(value = "")
  public EventEnum getEvent() {
    return event;
  }

  public void setEvent(EventEnum event) {
    this.event = event;
  }

  public Template eventTime(Long eventTime) {
    this.eventTime = eventTime;
    return this;
  }

   /**
   * Get eventTime
   * @return eventTime
  **/
  @ApiModelProperty(value = "")
  public Long getEventTime() {
    return eventTime;
  }

  public void setEventTime(Long eventTime) {
    this.eventTime = eventTime;
  }

  public Template link(String link) {
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

  public Template name(String name) {
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

  public Template state(StateEnum state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public Template templateId(Long templateId) {
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

  public Template templateUniqueId(String templateUniqueId) {
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

  public Template type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Template typeDisplayName(String typeDisplayName) {
    this.typeDisplayName = typeDisplayName;
    return this;
  }

   /**
   * Get typeDisplayName
   * @return typeDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getTypeDisplayName() {
    return typeDisplayName;
  }

  public void setTypeDisplayName(String typeDisplayName) {
    this.typeDisplayName = typeDisplayName;
  }

  public Template user(String user) {
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @ApiModelProperty(value = "")
  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public Template version(String version) {
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

  public Template views(List<String> views) {
    this.views = views;
    return this;
  }

  public Template addViewsItem(String viewsItem) {
    if (this.views == null) {
      this.views = new ArrayList<String>();
    }
    this.views.add(viewsItem);
    return this;
  }

   /**
   * Get views
   * @return views
  **/
  @ApiModelProperty(value = "")
  public List<String> getViews() {
    return views;
  }

  public void setViews(List<String> views) {
    this.views = views;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Template template = (Template) o;
    return Objects.equals(this.collectorType, template.collectorType) &&
        Objects.equals(this.config, template.config) &&
        Objects.equals(this.defaultRtcEventPollingInterval, template.defaultRtcEventPollingInterval) &&
        Objects.equals(this.defaultRtcMaxPollingTimeout, template.defaultRtcMaxPollingTimeout) &&
        Objects.equals(this.description, template.description) &&
        Objects.equals(this.event, template.event) &&
        Objects.equals(this.eventTime, template.eventTime) &&
        Objects.equals(this.link, template.link) &&
        Objects.equals(this.name, template.name) &&
        Objects.equals(this.state, template.state) &&
        Objects.equals(this.templateId, template.templateId) &&
        Objects.equals(this.templateUniqueId, template.templateUniqueId) &&
        Objects.equals(this.type, template.type) &&
        Objects.equals(this.typeDisplayName, template.typeDisplayName) &&
        Objects.equals(this.user, template.user) &&
        Objects.equals(this.version, template.version) &&
        Objects.equals(this.views, template.views);
  }

  @Override
  public int hashCode() {
    return Objects.hash(collectorType, config, defaultRtcEventPollingInterval, defaultRtcMaxPollingTimeout, description, event, eventTime, link, name, state, templateId, templateUniqueId, type, typeDisplayName, user, version, views);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Template {\n");
    
    sb.append("    collectorType: ").append(toIndentedString(collectorType)).append("\n");
    sb.append("    config: ").append(toIndentedString(config)).append("\n");
    sb.append("    defaultRtcEventPollingInterval: ").append(toIndentedString(defaultRtcEventPollingInterval)).append("\n");
    sb.append("    defaultRtcMaxPollingTimeout: ").append(toIndentedString(defaultRtcMaxPollingTimeout)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    eventTime: ").append(toIndentedString(eventTime)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    templateUniqueId: ").append(toIndentedString(templateUniqueId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    typeDisplayName: ").append(toIndentedString(typeDisplayName)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    views: ").append(toIndentedString(views)).append("\n");
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
