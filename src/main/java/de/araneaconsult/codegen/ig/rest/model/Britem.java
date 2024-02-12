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
import java.io.IOException;
/**
 * Britem
 */



public class Britem {
  @SerializedName("appDisplayName")
  private String appDisplayName = null;

  @SerializedName("appId")
  private Long appId = null;

  /**
   * Gets or Sets autoRequest
   */
  @JsonAdapter(AutoRequestEnum.Adapter.class)
  public enum AutoRequestEnum {
    @SerializedName("NO_AUTO_GRANT_OR_REVOKE")
    NO_AUTO_GRANT_OR_REVOKE("NO_AUTO_GRANT_OR_REVOKE"),
    @SerializedName("AUTO_GRANT")
    AUTO_GRANT("AUTO_GRANT"),
    @SerializedName("AUTO_REVOKE")
    AUTO_REVOKE("AUTO_REVOKE"),
    @SerializedName("AUTO_GRANT_AND_REVOKE")
    AUTO_GRANT_AND_REVOKE("AUTO_GRANT_AND_REVOKE");

    private String value;

    AutoRequestEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AutoRequestEnum fromValue(String input) {
      for (AutoRequestEnum b : AutoRequestEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AutoRequestEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AutoRequestEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AutoRequestEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AutoRequestEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("autoRequest")
  private AutoRequestEnum autoRequest = null;

  @SerializedName("containedPermsAuthorizable")
  private Boolean containedPermsAuthorizable = null;

  @SerializedName("containedPermsAuthorized")
  private Boolean containedPermsAuthorized = null;

  @SerializedName("criteria")
  private String criteria = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("dynamic")
  private Boolean dynamic = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("gracePeriod")
  private Long gracePeriod = null;

  @SerializedName("hasAccountCollector")
  private Boolean hasAccountCollector = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("isInclude")
  private Boolean isInclude = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("mandatory")
  private Boolean mandatory = null;

  @SerializedName("notFound")
  private Boolean notFound = null;

  @SerializedName("startDate")
  private Long startDate = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),
    @SerializedName("INVALID")
    INVALID("INVALID"),
    @SerializedName("MINED")
    MINED("MINED");

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

  @SerializedName("type")
  private String type = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("userCount")
  private Long userCount = null;

  @SerializedName("userHas")
  private Boolean userHas = null;

  public Britem appDisplayName(String appDisplayName) {
    this.appDisplayName = appDisplayName;
    return this;
  }

   /**
   * Get appDisplayName
   * @return appDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getAppDisplayName() {
    return appDisplayName;
  }

  public void setAppDisplayName(String appDisplayName) {
    this.appDisplayName = appDisplayName;
  }

  public Britem appId(Long appId) {
    this.appId = appId;
    return this;
  }

   /**
   * Get appId
   * @return appId
  **/
  @ApiModelProperty(value = "")
  public Long getAppId() {
    return appId;
  }

  public void setAppId(Long appId) {
    this.appId = appId;
  }

  public Britem autoRequest(AutoRequestEnum autoRequest) {
    this.autoRequest = autoRequest;
    return this;
  }

   /**
   * Get autoRequest
   * @return autoRequest
  **/
  @ApiModelProperty(value = "")
  public AutoRequestEnum getAutoRequest() {
    return autoRequest;
  }

  public void setAutoRequest(AutoRequestEnum autoRequest) {
    this.autoRequest = autoRequest;
  }

  public Britem containedPermsAuthorizable(Boolean containedPermsAuthorizable) {
    this.containedPermsAuthorizable = containedPermsAuthorizable;
    return this;
  }

   /**
   * Get containedPermsAuthorizable
   * @return containedPermsAuthorizable
  **/
  @ApiModelProperty(value = "")
  public Boolean isContainedPermsAuthorizable() {
    return containedPermsAuthorizable;
  }

  public void setContainedPermsAuthorizable(Boolean containedPermsAuthorizable) {
    this.containedPermsAuthorizable = containedPermsAuthorizable;
  }

  public Britem containedPermsAuthorized(Boolean containedPermsAuthorized) {
    this.containedPermsAuthorized = containedPermsAuthorized;
    return this;
  }

   /**
   * Get containedPermsAuthorized
   * @return containedPermsAuthorized
  **/
  @ApiModelProperty(value = "")
  public Boolean isContainedPermsAuthorized() {
    return containedPermsAuthorized;
  }

  public void setContainedPermsAuthorized(Boolean containedPermsAuthorized) {
    this.containedPermsAuthorized = containedPermsAuthorized;
  }

  public Britem criteria(String criteria) {
    this.criteria = criteria;
    return this;
  }

   /**
   * Get criteria
   * @return criteria
  **/
  @ApiModelProperty(value = "")
  public String getCriteria() {
    return criteria;
  }

  public void setCriteria(String criteria) {
    this.criteria = criteria;
  }

  public Britem description(String description) {
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

  public Britem displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @ApiModelProperty(value = "")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Britem dynamic(Boolean dynamic) {
    this.dynamic = dynamic;
    return this;
  }

   /**
   * Get dynamic
   * @return dynamic
  **/
  @ApiModelProperty(value = "")
  public Boolean isDynamic() {
    return dynamic;
  }

  public void setDynamic(Boolean dynamic) {
    this.dynamic = dynamic;
  }

  public Britem endDate(Long endDate) {
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

  public Britem gracePeriod(Long gracePeriod) {
    this.gracePeriod = gracePeriod;
    return this;
  }

   /**
   * Get gracePeriod
   * @return gracePeriod
  **/
  @ApiModelProperty(value = "")
  public Long getGracePeriod() {
    return gracePeriod;
  }

  public void setGracePeriod(Long gracePeriod) {
    this.gracePeriod = gracePeriod;
  }

  public Britem hasAccountCollector(Boolean hasAccountCollector) {
    this.hasAccountCollector = hasAccountCollector;
    return this;
  }

   /**
   * Get hasAccountCollector
   * @return hasAccountCollector
  **/
  @ApiModelProperty(value = "")
  public Boolean isHasAccountCollector() {
    return hasAccountCollector;
  }

  public void setHasAccountCollector(Boolean hasAccountCollector) {
    this.hasAccountCollector = hasAccountCollector;
  }

  public Britem id(Long id) {
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

  public Britem isInclude(Boolean isInclude) {
    this.isInclude = isInclude;
    return this;
  }

   /**
   * Get isInclude
   * @return isInclude
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsInclude() {
    return isInclude;
  }

  public void setIsInclude(Boolean isInclude) {
    this.isInclude = isInclude;
  }

  public Britem link(String link) {
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

  public Britem mandatory(Boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }

   /**
   * Get mandatory
   * @return mandatory
  **/
  @ApiModelProperty(value = "")
  public Boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(Boolean mandatory) {
    this.mandatory = mandatory;
  }

  public Britem notFound(Boolean notFound) {
    this.notFound = notFound;
    return this;
  }

   /**
   * Get notFound
   * @return notFound
  **/
  @ApiModelProperty(value = "")
  public Boolean isNotFound() {
    return notFound;
  }

  public void setNotFound(Boolean notFound) {
    this.notFound = notFound;
  }

  public Britem startDate(Long startDate) {
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

  public Britem state(StateEnum state) {
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

  public Britem type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Britem uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

   /**
   * Get uniqueId
   * @return uniqueId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Britem userCount(Long userCount) {
    this.userCount = userCount;
    return this;
  }

   /**
   * Get userCount
   * @return userCount
  **/
  @ApiModelProperty(value = "")
  public Long getUserCount() {
    return userCount;
  }

  public void setUserCount(Long userCount) {
    this.userCount = userCount;
  }

  public Britem userHas(Boolean userHas) {
    this.userHas = userHas;
    return this;
  }

   /**
   * Get userHas
   * @return userHas
  **/
  @ApiModelProperty(value = "")
  public Boolean isUserHas() {
    return userHas;
  }

  public void setUserHas(Boolean userHas) {
    this.userHas = userHas;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Britem britem = (Britem) o;
    return Objects.equals(this.appDisplayName, britem.appDisplayName) &&
        Objects.equals(this.appId, britem.appId) &&
        Objects.equals(this.autoRequest, britem.autoRequest) &&
        Objects.equals(this.containedPermsAuthorizable, britem.containedPermsAuthorizable) &&
        Objects.equals(this.containedPermsAuthorized, britem.containedPermsAuthorized) &&
        Objects.equals(this.criteria, britem.criteria) &&
        Objects.equals(this.description, britem.description) &&
        Objects.equals(this.displayName, britem.displayName) &&
        Objects.equals(this.dynamic, britem.dynamic) &&
        Objects.equals(this.endDate, britem.endDate) &&
        Objects.equals(this.gracePeriod, britem.gracePeriod) &&
        Objects.equals(this.hasAccountCollector, britem.hasAccountCollector) &&
        Objects.equals(this.id, britem.id) &&
        Objects.equals(this.isInclude, britem.isInclude) &&
        Objects.equals(this.link, britem.link) &&
        Objects.equals(this.mandatory, britem.mandatory) &&
        Objects.equals(this.notFound, britem.notFound) &&
        Objects.equals(this.startDate, britem.startDate) &&
        Objects.equals(this.state, britem.state) &&
        Objects.equals(this.type, britem.type) &&
        Objects.equals(this.uniqueId, britem.uniqueId) &&
        Objects.equals(this.userCount, britem.userCount) &&
        Objects.equals(this.userHas, britem.userHas);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appDisplayName, appId, autoRequest, containedPermsAuthorizable, containedPermsAuthorized, criteria, description, displayName, dynamic, endDate, gracePeriod, hasAccountCollector, id, isInclude, link, mandatory, notFound, startDate, state, type, uniqueId, userCount, userHas);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Britem {\n");
    
    sb.append("    appDisplayName: ").append(toIndentedString(appDisplayName)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    autoRequest: ").append(toIndentedString(autoRequest)).append("\n");
    sb.append("    containedPermsAuthorizable: ").append(toIndentedString(containedPermsAuthorizable)).append("\n");
    sb.append("    containedPermsAuthorized: ").append(toIndentedString(containedPermsAuthorized)).append("\n");
    sb.append("    criteria: ").append(toIndentedString(criteria)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    dynamic: ").append(toIndentedString(dynamic)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    gracePeriod: ").append(toIndentedString(gracePeriod)).append("\n");
    sb.append("    hasAccountCollector: ").append(toIndentedString(hasAccountCollector)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isInclude: ").append(toIndentedString(isInclude)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    mandatory: ").append(toIndentedString(mandatory)).append("\n");
    sb.append("    notFound: ").append(toIndentedString(notFound)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    userCount: ").append(toIndentedString(userCount)).append("\n");
    sb.append("    userHas: ").append(toIndentedString(userHas)).append("\n");
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
