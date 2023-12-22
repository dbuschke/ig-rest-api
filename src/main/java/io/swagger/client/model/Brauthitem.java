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
 * Brauthitem
 */



public class Brauthitem {
  @SerializedName("appDisplayName")
  private String appDisplayName = null;

  @SerializedName("appId")
  private Long appId = null;

  @SerializedName("assignedCount")
  private Long assignedCount = null;

  @SerializedName("canRequestThisForOthers")
  private Boolean canRequestThisForOthers = null;

  @SerializedName("cost")
  private Long cost = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("dynamic")
  private Boolean dynamic = null;

  @SerializedName("hideResourceImage")
  private Boolean hideResourceImage = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("imageUrl")
  private String imageUrl = null;

  /**
   * Gets or Sets itemType
   */
  @JsonAdapter(ItemTypeEnum.Adapter.class)
  public enum ItemTypeEnum {
    @SerializedName("TECHNICAL_ROLE")
    TECHNICAL_ROLE("TECHNICAL_ROLE"),
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION");

    private String value;

    ItemTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ItemTypeEnum fromValue(String input) {
      for (ItemTypeEnum b : ItemTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ItemTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ItemTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ItemTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ItemTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("itemType")
  private ItemTypeEnum itemType = null;

  @SerializedName("requestInProgress")
  private Boolean requestInProgress = null;

  @SerializedName("requiresApproval")
  private Boolean requiresApproval = null;

  @SerializedName("risk")
  private Long risk = null;

  @SerializedName("showDefaultResourceImage")
  private Boolean showDefaultResourceImage = null;

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

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("userCount")
  private Long userCount = null;

  @SerializedName("userHas")
  private Boolean userHas = null;

  @SerializedName("userHasBlockMultiples")
  private Boolean userHasBlockMultiples = null;

  public Brauthitem appDisplayName(String appDisplayName) {
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

  public Brauthitem appId(Long appId) {
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

  public Brauthitem assignedCount(Long assignedCount) {
    this.assignedCount = assignedCount;
    return this;
  }

   /**
   * Get assignedCount
   * @return assignedCount
  **/
  @ApiModelProperty(value = "")
  public Long getAssignedCount() {
    return assignedCount;
  }

  public void setAssignedCount(Long assignedCount) {
    this.assignedCount = assignedCount;
  }

  public Brauthitem canRequestThisForOthers(Boolean canRequestThisForOthers) {
    this.canRequestThisForOthers = canRequestThisForOthers;
    return this;
  }

   /**
   * Get canRequestThisForOthers
   * @return canRequestThisForOthers
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanRequestThisForOthers() {
    return canRequestThisForOthers;
  }

  public void setCanRequestThisForOthers(Boolean canRequestThisForOthers) {
    this.canRequestThisForOthers = canRequestThisForOthers;
  }

  public Brauthitem cost(Long cost) {
    this.cost = cost;
    return this;
  }

   /**
   * Get cost
   * @return cost
  **/
  @ApiModelProperty(value = "")
  public Long getCost() {
    return cost;
  }

  public void setCost(Long cost) {
    this.cost = cost;
  }

  public Brauthitem description(String description) {
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

  public Brauthitem displayName(String displayName) {
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

  public Brauthitem dynamic(Boolean dynamic) {
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

  public Brauthitem hideResourceImage(Boolean hideResourceImage) {
    this.hideResourceImage = hideResourceImage;
    return this;
  }

   /**
   * Get hideResourceImage
   * @return hideResourceImage
  **/
  @ApiModelProperty(value = "")
  public Boolean isHideResourceImage() {
    return hideResourceImage;
  }

  public void setHideResourceImage(Boolean hideResourceImage) {
    this.hideResourceImage = hideResourceImage;
  }

  public Brauthitem id(Long id) {
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

  public Brauthitem imageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

   /**
   * Get imageUrl
   * @return imageUrl
  **/
  @ApiModelProperty(value = "")
  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Brauthitem itemType(ItemTypeEnum itemType) {
    this.itemType = itemType;
    return this;
  }

   /**
   * Get itemType
   * @return itemType
  **/
  @ApiModelProperty(value = "")
  public ItemTypeEnum getItemType() {
    return itemType;
  }

  public void setItemType(ItemTypeEnum itemType) {
    this.itemType = itemType;
  }

  public Brauthitem requestInProgress(Boolean requestInProgress) {
    this.requestInProgress = requestInProgress;
    return this;
  }

   /**
   * Get requestInProgress
   * @return requestInProgress
  **/
  @ApiModelProperty(value = "")
  public Boolean isRequestInProgress() {
    return requestInProgress;
  }

  public void setRequestInProgress(Boolean requestInProgress) {
    this.requestInProgress = requestInProgress;
  }

  public Brauthitem requiresApproval(Boolean requiresApproval) {
    this.requiresApproval = requiresApproval;
    return this;
  }

   /**
   * Get requiresApproval
   * @return requiresApproval
  **/
  @ApiModelProperty(value = "")
  public Boolean isRequiresApproval() {
    return requiresApproval;
  }

  public void setRequiresApproval(Boolean requiresApproval) {
    this.requiresApproval = requiresApproval;
  }

  public Brauthitem risk(Long risk) {
    this.risk = risk;
    return this;
  }

   /**
   * Get risk
   * @return risk
  **/
  @ApiModelProperty(value = "")
  public Long getRisk() {
    return risk;
  }

  public void setRisk(Long risk) {
    this.risk = risk;
  }

  public Brauthitem showDefaultResourceImage(Boolean showDefaultResourceImage) {
    this.showDefaultResourceImage = showDefaultResourceImage;
    return this;
  }

   /**
   * Get showDefaultResourceImage
   * @return showDefaultResourceImage
  **/
  @ApiModelProperty(value = "")
  public Boolean isShowDefaultResourceImage() {
    return showDefaultResourceImage;
  }

  public void setShowDefaultResourceImage(Boolean showDefaultResourceImage) {
    this.showDefaultResourceImage = showDefaultResourceImage;
  }

  public Brauthitem state(StateEnum state) {
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

  public Brauthitem uniqueId(String uniqueId) {
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

  public Brauthitem userCount(Long userCount) {
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

  public Brauthitem userHas(Boolean userHas) {
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

  public Brauthitem userHasBlockMultiples(Boolean userHasBlockMultiples) {
    this.userHasBlockMultiples = userHasBlockMultiples;
    return this;
  }

   /**
   * Get userHasBlockMultiples
   * @return userHasBlockMultiples
  **/
  @ApiModelProperty(value = "")
  public Boolean isUserHasBlockMultiples() {
    return userHasBlockMultiples;
  }

  public void setUserHasBlockMultiples(Boolean userHasBlockMultiples) {
    this.userHasBlockMultiples = userHasBlockMultiples;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Brauthitem brauthitem = (Brauthitem) o;
    return Objects.equals(this.appDisplayName, brauthitem.appDisplayName) &&
        Objects.equals(this.appId, brauthitem.appId) &&
        Objects.equals(this.assignedCount, brauthitem.assignedCount) &&
        Objects.equals(this.canRequestThisForOthers, brauthitem.canRequestThisForOthers) &&
        Objects.equals(this.cost, brauthitem.cost) &&
        Objects.equals(this.description, brauthitem.description) &&
        Objects.equals(this.displayName, brauthitem.displayName) &&
        Objects.equals(this.dynamic, brauthitem.dynamic) &&
        Objects.equals(this.hideResourceImage, brauthitem.hideResourceImage) &&
        Objects.equals(this.id, brauthitem.id) &&
        Objects.equals(this.imageUrl, brauthitem.imageUrl) &&
        Objects.equals(this.itemType, brauthitem.itemType) &&
        Objects.equals(this.requestInProgress, brauthitem.requestInProgress) &&
        Objects.equals(this.requiresApproval, brauthitem.requiresApproval) &&
        Objects.equals(this.risk, brauthitem.risk) &&
        Objects.equals(this.showDefaultResourceImage, brauthitem.showDefaultResourceImage) &&
        Objects.equals(this.state, brauthitem.state) &&
        Objects.equals(this.uniqueId, brauthitem.uniqueId) &&
        Objects.equals(this.userCount, brauthitem.userCount) &&
        Objects.equals(this.userHas, brauthitem.userHas) &&
        Objects.equals(this.userHasBlockMultiples, brauthitem.userHasBlockMultiples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appDisplayName, appId, assignedCount, canRequestThisForOthers, cost, description, displayName, dynamic, hideResourceImage, id, imageUrl, itemType, requestInProgress, requiresApproval, risk, showDefaultResourceImage, state, uniqueId, userCount, userHas, userHasBlockMultiples);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Brauthitem {\n");
    
    sb.append("    appDisplayName: ").append(toIndentedString(appDisplayName)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    assignedCount: ").append(toIndentedString(assignedCount)).append("\n");
    sb.append("    canRequestThisForOthers: ").append(toIndentedString(canRequestThisForOthers)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    dynamic: ").append(toIndentedString(dynamic)).append("\n");
    sb.append("    hideResourceImage: ").append(toIndentedString(hideResourceImage)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("    itemType: ").append(toIndentedString(itemType)).append("\n");
    sb.append("    requestInProgress: ").append(toIndentedString(requestInProgress)).append("\n");
    sb.append("    requiresApproval: ").append(toIndentedString(requiresApproval)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    showDefaultResourceImage: ").append(toIndentedString(showDefaultResourceImage)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    userCount: ").append(toIndentedString(userCount)).append("\n");
    sb.append("    userHas: ").append(toIndentedString(userHas)).append("\n");
    sb.append("    userHasBlockMultiples: ").append(toIndentedString(userHasBlockMultiples)).append("\n");
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
