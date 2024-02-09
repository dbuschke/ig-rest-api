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
 * Trole
 */



public class Trole {
  @SerializedName("assignedCount")
  private Long assignedCount = null;

  @SerializedName("categories")
  private List<String> categories = null;

  @SerializedName("cost")
  private Long cost = null;

  /**
   * Gets or Sets currency
   */
  @JsonAdapter(CurrencyEnum.Adapter.class)
  public enum CurrencyEnum {
    @SerializedName("USD")
    USD("USD"),
    @SerializedName("EUR")
    EUR("EUR"),
    @SerializedName("GBP")
    GBP("GBP"),
    @SerializedName("JPY")
    JPY("JPY"),
    @SerializedName("BRL")
    BRL("BRL"),
    @SerializedName("CNY")
    CNY("CNY"),
    @SerializedName("UNDEFINED")
    UNDEFINED("UNDEFINED");

    private String value;

    CurrencyEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static CurrencyEnum fromValue(String input) {
      for (CurrencyEnum b : CurrencyEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<CurrencyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CurrencyEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public CurrencyEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return CurrencyEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("currency")
  private CurrencyEnum currency = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("isRequestable")
  private Boolean isRequestable = null;

  @SerializedName("permissionCount")
  private Long permissionCount = null;

  @SerializedName("requestInProgress")
  private Boolean requestInProgress = null;

  @SerializedName("requiresApproval")
  private Boolean requiresApproval = null;

  @SerializedName("risk")
  private Long risk = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("userCount")
  private Long userCount = null;

  @SerializedName("userHas")
  private Boolean userHas = null;

  @SerializedName("userHasPermissions")
  private Boolean userHasPermissions = null;

  public Trole assignedCount(Long assignedCount) {
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

  public Trole categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public Trole addCategoriesItem(String categoriesItem) {
    if (this.categories == null) {
      this.categories = new ArrayList<String>();
    }
    this.categories.add(categoriesItem);
    return this;
  }

   /**
   * Get categories
   * @return categories
  **/
  @ApiModelProperty(value = "")
  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public Trole cost(Long cost) {
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

  public Trole currency(CurrencyEnum currency) {
    this.currency = currency;
    return this;
  }

   /**
   * Get currency
   * @return currency
  **/
  @ApiModelProperty(value = "")
  public CurrencyEnum getCurrency() {
    return currency;
  }

  public void setCurrency(CurrencyEnum currency) {
    this.currency = currency;
  }

  public Trole description(String description) {
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

  public Trole displayName(String displayName) {
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

  public Trole id(Long id) {
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

  public Trole isRequestable(Boolean isRequestable) {
    this.isRequestable = isRequestable;
    return this;
  }

   /**
   * Get isRequestable
   * @return isRequestable
  **/
  @ApiModelProperty(value = "")
  public Boolean isIsRequestable() {
    return isRequestable;
  }

  public void setIsRequestable(Boolean isRequestable) {
    this.isRequestable = isRequestable;
  }

  public Trole permissionCount(Long permissionCount) {
    this.permissionCount = permissionCount;
    return this;
  }

   /**
   * Get permissionCount
   * @return permissionCount
  **/
  @ApiModelProperty(value = "")
  public Long getPermissionCount() {
    return permissionCount;
  }

  public void setPermissionCount(Long permissionCount) {
    this.permissionCount = permissionCount;
  }

  public Trole requestInProgress(Boolean requestInProgress) {
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

  public Trole requiresApproval(Boolean requiresApproval) {
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

  public Trole risk(Long risk) {
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

  public Trole uniqueId(String uniqueId) {
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

  public Trole userCount(Long userCount) {
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

  public Trole userHas(Boolean userHas) {
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

  public Trole userHasPermissions(Boolean userHasPermissions) {
    this.userHasPermissions = userHasPermissions;
    return this;
  }

   /**
   * Get userHasPermissions
   * @return userHasPermissions
  **/
  @ApiModelProperty(value = "")
  public Boolean isUserHasPermissions() {
    return userHasPermissions;
  }

  public void setUserHasPermissions(Boolean userHasPermissions) {
    this.userHasPermissions = userHasPermissions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Trole trole = (Trole) o;
    return Objects.equals(this.assignedCount, trole.assignedCount) &&
        Objects.equals(this.categories, trole.categories) &&
        Objects.equals(this.cost, trole.cost) &&
        Objects.equals(this.currency, trole.currency) &&
        Objects.equals(this.description, trole.description) &&
        Objects.equals(this.displayName, trole.displayName) &&
        Objects.equals(this.id, trole.id) &&
        Objects.equals(this.isRequestable, trole.isRequestable) &&
        Objects.equals(this.permissionCount, trole.permissionCount) &&
        Objects.equals(this.requestInProgress, trole.requestInProgress) &&
        Objects.equals(this.requiresApproval, trole.requiresApproval) &&
        Objects.equals(this.risk, trole.risk) &&
        Objects.equals(this.uniqueId, trole.uniqueId) &&
        Objects.equals(this.userCount, trole.userCount) &&
        Objects.equals(this.userHas, trole.userHas) &&
        Objects.equals(this.userHasPermissions, trole.userHasPermissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assignedCount, categories, cost, currency, description, displayName, id, isRequestable, permissionCount, requestInProgress, requiresApproval, risk, uniqueId, userCount, userHas, userHasPermissions);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Trole {\n");
    
    sb.append("    assignedCount: ").append(toIndentedString(assignedCount)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isRequestable: ").append(toIndentedString(isRequestable)).append("\n");
    sb.append("    permissionCount: ").append(toIndentedString(permissionCount)).append("\n");
    sb.append("    requestInProgress: ").append(toIndentedString(requestInProgress)).append("\n");
    sb.append("    requiresApproval: ").append(toIndentedString(requiresApproval)).append("\n");
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    userCount: ").append(toIndentedString(userCount)).append("\n");
    sb.append("    userHas: ").append(toIndentedString(userHas)).append("\n");
    sb.append("    userHasPermissions: ").append(toIndentedString(userHasPermissions)).append("\n");
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
