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
import io.swagger.client.model.AffiliatedEntitlements;
import io.swagger.client.model.AppSource;
import io.swagger.client.model.Collectors;
import io.swagger.client.model.EntityCategory;
import io.swagger.client.model.Permissions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AppSource
 */



public class AppSource {
  @SerializedName("affiliatedAppId")
  private Long affiliatedAppId = null;

  @SerializedName("affiliatedAppLink")
  private String affiliatedAppLink = null;

  @SerializedName("affiliatedEntitlements")
  private AffiliatedEntitlements affiliatedEntitlements = null;

  @SerializedName("appSourceId")
  private Long appSourceId = null;

  @SerializedName("categories")
  private List<EntityCategory> categories = null;

  @SerializedName("collectingAppId")
  private Long collectingAppId = null;

  @SerializedName("collectorCount")
  private Long collectorCount = null;

  @SerializedName("collectorsAreConfigured")
  private Boolean collectorsAreConfigured = null;

  @SerializedName("dataCollectors")
  private Collectors dataCollectors = null;

  /**
   * Gets or Sets dataSourceType
   */
  @JsonAdapter(DataSourceTypeEnum.Adapter.class)
  public enum DataSourceTypeEnum {
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION"),
    @SerializedName("IDENTITY")
    IDENTITY("IDENTITY"),
    @SerializedName("FULFILLMENT")
    FULFILLMENT("FULFILLMENT"),
    @SerializedName("SYSTEM_FULFILLMENT")
    SYSTEM_FULFILLMENT("SYSTEM_FULFILLMENT"),
    @SerializedName("APP_COLLECTOR_SOURCE")
    APP_COLLECTOR_SOURCE("APP_COLLECTOR_SOURCE");

    private String value;

    DataSourceTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DataSourceTypeEnum fromValue(String input) {
      for (DataSourceTypeEnum b : DataSourceTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DataSourceTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DataSourceTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DataSourceTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DataSourceTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("dataSourceType")
  private DataSourceTypeEnum dataSourceType = null;

  @SerializedName("deleting")
  private Boolean deleting = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("duplicateNameCount")
  private Long duplicateNameCount = null;

  @SerializedName("entitlements")
  private Permissions entitlements = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkJoins")
  private String linkJoins = null;

  @SerializedName("linkResolutionSchedules")
  private String linkResolutionSchedules = null;

  @SerializedName("linkSchedules")
  private String linkSchedules = null;

  @SerializedName("multiAppCollector")
  private String multiAppCollector = null;

  @SerializedName("multiAppCollectorAllowable")
  private Boolean multiAppCollectorAllowable = null;

  @SerializedName("multiAppCollectorApplication")
  private AppSource multiAppCollectorApplication = null;

  @SerializedName("multiAppFlag")
  private Boolean multiAppFlag = null;

  @SerializedName("multiAppTargetAllowable")
  private Boolean multiAppTargetAllowable = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("syncPermissionsToIdm")
  private Boolean syncPermissionsToIdm = null;

  @SerializedName("targetApplications")
  private List<AppSource> targetApplications = null;

  /**
   * Gets or Sets type
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {
    @SerializedName("ENTERPRISE")
    ENTERPRISE("ENTERPRISE"),
    @SerializedName("IDM")
    IDM("IDM"),
    @SerializedName("IDMPROV")
    IDMPROV("IDMPROV");

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

  @SerializedName("uniqueApplicationId")
  private String uniqueApplicationId = null;

  public AppSource affiliatedAppId(Long affiliatedAppId) {
    this.affiliatedAppId = affiliatedAppId;
    return this;
  }

   /**
   * Get affiliatedAppId
   * @return affiliatedAppId
  **/
  @ApiModelProperty(value = "")
  public Long getAffiliatedAppId() {
    return affiliatedAppId;
  }

  public void setAffiliatedAppId(Long affiliatedAppId) {
    this.affiliatedAppId = affiliatedAppId;
  }

  public AppSource affiliatedAppLink(String affiliatedAppLink) {
    this.affiliatedAppLink = affiliatedAppLink;
    return this;
  }

   /**
   * Get affiliatedAppLink
   * @return affiliatedAppLink
  **/
  @ApiModelProperty(value = "")
  public String getAffiliatedAppLink() {
    return affiliatedAppLink;
  }

  public void setAffiliatedAppLink(String affiliatedAppLink) {
    this.affiliatedAppLink = affiliatedAppLink;
  }

  public AppSource affiliatedEntitlements(AffiliatedEntitlements affiliatedEntitlements) {
    this.affiliatedEntitlements = affiliatedEntitlements;
    return this;
  }

   /**
   * Get affiliatedEntitlements
   * @return affiliatedEntitlements
  **/
  @ApiModelProperty(value = "")
  public AffiliatedEntitlements getAffiliatedEntitlements() {
    return affiliatedEntitlements;
  }

  public void setAffiliatedEntitlements(AffiliatedEntitlements affiliatedEntitlements) {
    this.affiliatedEntitlements = affiliatedEntitlements;
  }

  public AppSource appSourceId(Long appSourceId) {
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

  public AppSource categories(List<EntityCategory> categories) {
    this.categories = categories;
    return this;
  }

  public AppSource addCategoriesItem(EntityCategory categoriesItem) {
    if (this.categories == null) {
      this.categories = new ArrayList<EntityCategory>();
    }
    this.categories.add(categoriesItem);
    return this;
  }

   /**
   * Get categories
   * @return categories
  **/
  @ApiModelProperty(value = "")
  public List<EntityCategory> getCategories() {
    return categories;
  }

  public void setCategories(List<EntityCategory> categories) {
    this.categories = categories;
  }

  public AppSource collectingAppId(Long collectingAppId) {
    this.collectingAppId = collectingAppId;
    return this;
  }

   /**
   * Get collectingAppId
   * @return collectingAppId
  **/
  @ApiModelProperty(value = "")
  public Long getCollectingAppId() {
    return collectingAppId;
  }

  public void setCollectingAppId(Long collectingAppId) {
    this.collectingAppId = collectingAppId;
  }

  public AppSource collectorCount(Long collectorCount) {
    this.collectorCount = collectorCount;
    return this;
  }

   /**
   * Get collectorCount
   * @return collectorCount
  **/
  @ApiModelProperty(value = "")
  public Long getCollectorCount() {
    return collectorCount;
  }

  public void setCollectorCount(Long collectorCount) {
    this.collectorCount = collectorCount;
  }

  public AppSource collectorsAreConfigured(Boolean collectorsAreConfigured) {
    this.collectorsAreConfigured = collectorsAreConfigured;
    return this;
  }

   /**
   * Get collectorsAreConfigured
   * @return collectorsAreConfigured
  **/
  @ApiModelProperty(value = "")
  public Boolean isCollectorsAreConfigured() {
    return collectorsAreConfigured;
  }

  public void setCollectorsAreConfigured(Boolean collectorsAreConfigured) {
    this.collectorsAreConfigured = collectorsAreConfigured;
  }

  public AppSource dataCollectors(Collectors dataCollectors) {
    this.dataCollectors = dataCollectors;
    return this;
  }

   /**
   * Get dataCollectors
   * @return dataCollectors
  **/
  @ApiModelProperty(value = "")
  public Collectors getDataCollectors() {
    return dataCollectors;
  }

  public void setDataCollectors(Collectors dataCollectors) {
    this.dataCollectors = dataCollectors;
  }

  public AppSource dataSourceType(DataSourceTypeEnum dataSourceType) {
    this.dataSourceType = dataSourceType;
    return this;
  }

   /**
   * Get dataSourceType
   * @return dataSourceType
  **/
  @ApiModelProperty(value = "")
  public DataSourceTypeEnum getDataSourceType() {
    return dataSourceType;
  }

  public void setDataSourceType(DataSourceTypeEnum dataSourceType) {
    this.dataSourceType = dataSourceType;
  }

  public AppSource deleting(Boolean deleting) {
    this.deleting = deleting;
    return this;
  }

   /**
   * Get deleting
   * @return deleting
  **/
  @ApiModelProperty(value = "")
  public Boolean isDeleting() {
    return deleting;
  }

  public void setDeleting(Boolean deleting) {
    this.deleting = deleting;
  }

  public AppSource description(String description) {
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

  public AppSource duplicateNameCount(Long duplicateNameCount) {
    this.duplicateNameCount = duplicateNameCount;
    return this;
  }

   /**
   * Get duplicateNameCount
   * @return duplicateNameCount
  **/
  @ApiModelProperty(value = "")
  public Long getDuplicateNameCount() {
    return duplicateNameCount;
  }

  public void setDuplicateNameCount(Long duplicateNameCount) {
    this.duplicateNameCount = duplicateNameCount;
  }

  public AppSource entitlements(Permissions entitlements) {
    this.entitlements = entitlements;
    return this;
  }

   /**
   * Get entitlements
   * @return entitlements
  **/
  @ApiModelProperty(value = "")
  public Permissions getEntitlements() {
    return entitlements;
  }

  public void setEntitlements(Permissions entitlements) {
    this.entitlements = entitlements;
  }

  public AppSource link(String link) {
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

  public AppSource linkJoins(String linkJoins) {
    this.linkJoins = linkJoins;
    return this;
  }

   /**
   * Get linkJoins
   * @return linkJoins
  **/
  @ApiModelProperty(value = "")
  public String getLinkJoins() {
    return linkJoins;
  }

  public void setLinkJoins(String linkJoins) {
    this.linkJoins = linkJoins;
  }

  public AppSource linkResolutionSchedules(String linkResolutionSchedules) {
    this.linkResolutionSchedules = linkResolutionSchedules;
    return this;
  }

   /**
   * Get linkResolutionSchedules
   * @return linkResolutionSchedules
  **/
  @ApiModelProperty(value = "")
  public String getLinkResolutionSchedules() {
    return linkResolutionSchedules;
  }

  public void setLinkResolutionSchedules(String linkResolutionSchedules) {
    this.linkResolutionSchedules = linkResolutionSchedules;
  }

  public AppSource linkSchedules(String linkSchedules) {
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

  public AppSource multiAppCollector(String multiAppCollector) {
    this.multiAppCollector = multiAppCollector;
    return this;
  }

   /**
   * Get multiAppCollector
   * @return multiAppCollector
  **/
  @ApiModelProperty(value = "")
  public String getMultiAppCollector() {
    return multiAppCollector;
  }

  public void setMultiAppCollector(String multiAppCollector) {
    this.multiAppCollector = multiAppCollector;
  }

  public AppSource multiAppCollectorAllowable(Boolean multiAppCollectorAllowable) {
    this.multiAppCollectorAllowable = multiAppCollectorAllowable;
    return this;
  }

   /**
   * Get multiAppCollectorAllowable
   * @return multiAppCollectorAllowable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMultiAppCollectorAllowable() {
    return multiAppCollectorAllowable;
  }

  public void setMultiAppCollectorAllowable(Boolean multiAppCollectorAllowable) {
    this.multiAppCollectorAllowable = multiAppCollectorAllowable;
  }

  public AppSource multiAppCollectorApplication(AppSource multiAppCollectorApplication) {
    this.multiAppCollectorApplication = multiAppCollectorApplication;
    return this;
  }

   /**
   * Get multiAppCollectorApplication
   * @return multiAppCollectorApplication
  **/
  @ApiModelProperty(value = "")
  public AppSource getMultiAppCollectorApplication() {
    return multiAppCollectorApplication;
  }

  public void setMultiAppCollectorApplication(AppSource multiAppCollectorApplication) {
    this.multiAppCollectorApplication = multiAppCollectorApplication;
  }

  public AppSource multiAppFlag(Boolean multiAppFlag) {
    this.multiAppFlag = multiAppFlag;
    return this;
  }

   /**
   * Get multiAppFlag
   * @return multiAppFlag
  **/
  @ApiModelProperty(value = "")
  public Boolean isMultiAppFlag() {
    return multiAppFlag;
  }

  public void setMultiAppFlag(Boolean multiAppFlag) {
    this.multiAppFlag = multiAppFlag;
  }

  public AppSource multiAppTargetAllowable(Boolean multiAppTargetAllowable) {
    this.multiAppTargetAllowable = multiAppTargetAllowable;
    return this;
  }

   /**
   * Get multiAppTargetAllowable
   * @return multiAppTargetAllowable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMultiAppTargetAllowable() {
    return multiAppTargetAllowable;
  }

  public void setMultiAppTargetAllowable(Boolean multiAppTargetAllowable) {
    this.multiAppTargetAllowable = multiAppTargetAllowable;
  }

  public AppSource name(String name) {
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

  public AppSource status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public AppSource syncPermissionsToIdm(Boolean syncPermissionsToIdm) {
    this.syncPermissionsToIdm = syncPermissionsToIdm;
    return this;
  }

   /**
   * Get syncPermissionsToIdm
   * @return syncPermissionsToIdm
  **/
  @ApiModelProperty(value = "")
  public Boolean isSyncPermissionsToIdm() {
    return syncPermissionsToIdm;
  }

  public void setSyncPermissionsToIdm(Boolean syncPermissionsToIdm) {
    this.syncPermissionsToIdm = syncPermissionsToIdm;
  }

  public AppSource targetApplications(List<AppSource> targetApplications) {
    this.targetApplications = targetApplications;
    return this;
  }

  public AppSource addTargetApplicationsItem(AppSource targetApplicationsItem) {
    if (this.targetApplications == null) {
      this.targetApplications = new ArrayList<AppSource>();
    }
    this.targetApplications.add(targetApplicationsItem);
    return this;
  }

   /**
   * Get targetApplications
   * @return targetApplications
  **/
  @ApiModelProperty(value = "")
  public List<AppSource> getTargetApplications() {
    return targetApplications;
  }

  public void setTargetApplications(List<AppSource> targetApplications) {
    this.targetApplications = targetApplications;
  }

  public AppSource type(TypeEnum type) {
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

  public AppSource uniqueApplicationId(String uniqueApplicationId) {
    this.uniqueApplicationId = uniqueApplicationId;
    return this;
  }

   /**
   * Get uniqueApplicationId
   * @return uniqueApplicationId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueApplicationId() {
    return uniqueApplicationId;
  }

  public void setUniqueApplicationId(String uniqueApplicationId) {
    this.uniqueApplicationId = uniqueApplicationId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppSource appSource = (AppSource) o;
    return Objects.equals(this.affiliatedAppId, appSource.affiliatedAppId) &&
        Objects.equals(this.affiliatedAppLink, appSource.affiliatedAppLink) &&
        Objects.equals(this.affiliatedEntitlements, appSource.affiliatedEntitlements) &&
        Objects.equals(this.appSourceId, appSource.appSourceId) &&
        Objects.equals(this.categories, appSource.categories) &&
        Objects.equals(this.collectingAppId, appSource.collectingAppId) &&
        Objects.equals(this.collectorCount, appSource.collectorCount) &&
        Objects.equals(this.collectorsAreConfigured, appSource.collectorsAreConfigured) &&
        Objects.equals(this.dataCollectors, appSource.dataCollectors) &&
        Objects.equals(this.dataSourceType, appSource.dataSourceType) &&
        Objects.equals(this.deleting, appSource.deleting) &&
        Objects.equals(this.description, appSource.description) &&
        Objects.equals(this.duplicateNameCount, appSource.duplicateNameCount) &&
        Objects.equals(this.entitlements, appSource.entitlements) &&
        Objects.equals(this.link, appSource.link) &&
        Objects.equals(this.linkJoins, appSource.linkJoins) &&
        Objects.equals(this.linkResolutionSchedules, appSource.linkResolutionSchedules) &&
        Objects.equals(this.linkSchedules, appSource.linkSchedules) &&
        Objects.equals(this.multiAppCollector, appSource.multiAppCollector) &&
        Objects.equals(this.multiAppCollectorAllowable, appSource.multiAppCollectorAllowable) &&
        Objects.equals(this.multiAppCollectorApplication, appSource.multiAppCollectorApplication) &&
        Objects.equals(this.multiAppFlag, appSource.multiAppFlag) &&
        Objects.equals(this.multiAppTargetAllowable, appSource.multiAppTargetAllowable) &&
        Objects.equals(this.name, appSource.name) &&
        Objects.equals(this.status, appSource.status) &&
        Objects.equals(this.syncPermissionsToIdm, appSource.syncPermissionsToIdm) &&
        Objects.equals(this.targetApplications, appSource.targetApplications) &&
        Objects.equals(this.type, appSource.type) &&
        Objects.equals(this.uniqueApplicationId, appSource.uniqueApplicationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(affiliatedAppId, affiliatedAppLink, affiliatedEntitlements, appSourceId, categories, collectingAppId, collectorCount, collectorsAreConfigured, dataCollectors, dataSourceType, deleting, description, duplicateNameCount, entitlements, link, linkJoins, linkResolutionSchedules, linkSchedules, multiAppCollector, multiAppCollectorAllowable, multiAppCollectorApplication, multiAppFlag, multiAppTargetAllowable, name, status, syncPermissionsToIdm, targetApplications, type, uniqueApplicationId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppSource {\n");
    
    sb.append("    affiliatedAppId: ").append(toIndentedString(affiliatedAppId)).append("\n");
    sb.append("    affiliatedAppLink: ").append(toIndentedString(affiliatedAppLink)).append("\n");
    sb.append("    affiliatedEntitlements: ").append(toIndentedString(affiliatedEntitlements)).append("\n");
    sb.append("    appSourceId: ").append(toIndentedString(appSourceId)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    collectingAppId: ").append(toIndentedString(collectingAppId)).append("\n");
    sb.append("    collectorCount: ").append(toIndentedString(collectorCount)).append("\n");
    sb.append("    collectorsAreConfigured: ").append(toIndentedString(collectorsAreConfigured)).append("\n");
    sb.append("    dataCollectors: ").append(toIndentedString(dataCollectors)).append("\n");
    sb.append("    dataSourceType: ").append(toIndentedString(dataSourceType)).append("\n");
    sb.append("    deleting: ").append(toIndentedString(deleting)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    duplicateNameCount: ").append(toIndentedString(duplicateNameCount)).append("\n");
    sb.append("    entitlements: ").append(toIndentedString(entitlements)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkJoins: ").append(toIndentedString(linkJoins)).append("\n");
    sb.append("    linkResolutionSchedules: ").append(toIndentedString(linkResolutionSchedules)).append("\n");
    sb.append("    linkSchedules: ").append(toIndentedString(linkSchedules)).append("\n");
    sb.append("    multiAppCollector: ").append(toIndentedString(multiAppCollector)).append("\n");
    sb.append("    multiAppCollectorAllowable: ").append(toIndentedString(multiAppCollectorAllowable)).append("\n");
    sb.append("    multiAppCollectorApplication: ").append(toIndentedString(multiAppCollectorApplication)).append("\n");
    sb.append("    multiAppFlag: ").append(toIndentedString(multiAppFlag)).append("\n");
    sb.append("    multiAppTargetAllowable: ").append(toIndentedString(multiAppTargetAllowable)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    syncPermissionsToIdm: ").append(toIndentedString(syncPermissionsToIdm)).append("\n");
    sb.append("    targetApplications: ").append(toIndentedString(targetApplications)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    uniqueApplicationId: ").append(toIndentedString(uniqueApplicationId)).append("\n");
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
