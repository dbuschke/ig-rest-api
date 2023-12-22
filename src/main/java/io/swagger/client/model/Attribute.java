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
import io.swagger.client.model.AttributeDefinition;
import java.io.IOException;
/**
 * Attribute
 */



public class Attribute {
  @SerializedName("advancedSearchable")
  private Boolean advancedSearchable = null;

  @SerializedName("allowStaticMapping")
  private Boolean allowStaticMapping = null;

  @SerializedName("allowedOnReviewerItem")
  private Boolean allowedOnReviewerItem = null;

  /**
   * Gets or Sets attributeDataType
   */
  @JsonAdapter(AttributeDataTypeEnum.Adapter.class)
  public enum AttributeDataTypeEnum {
    @SerializedName("STRING")
    STRING("STRING"),
    @SerializedName("LONG")
    LONG("LONG"),
    @SerializedName("DOUBLE")
    DOUBLE("DOUBLE"),
    @SerializedName("BOOLEAN")
    BOOLEAN("BOOLEAN"),
    @SerializedName("DATE")
    DATE("DATE"),
    @SerializedName("LOCALE")
    LOCALE("LOCALE"),
    @SerializedName("COMPOSITE")
    COMPOSITE("COMPOSITE");

    private String value;

    AttributeDataTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AttributeDataTypeEnum fromValue(String input) {
      for (AttributeDataTypeEnum b : AttributeDataTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AttributeDataTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AttributeDataTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AttributeDataTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AttributeDataTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("attributeDataType")
  private AttributeDataTypeEnum attributeDataType = null;

  @SerializedName("attributeId")
  private Long attributeId = null;

  @SerializedName("attributeKey")
  private String attributeKey = null;

  /**
   * Gets or Sets attributeState
   */
  @JsonAdapter(AttributeStateEnum.Adapter.class)
  public enum AttributeStateEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("DELETED")
    DELETED("DELETED"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE");

    private String value;

    AttributeStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AttributeStateEnum fromValue(String input) {
      for (AttributeStateEnum b : AttributeStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AttributeStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AttributeStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AttributeStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AttributeStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("attributeState")
  private AttributeStateEnum attributeState = null;

  /**
   * Gets or Sets attributeType
   */
  @JsonAdapter(AttributeTypeEnum.Adapter.class)
  public enum AttributeTypeEnum {
    @SerializedName("COLLECTED")
    COLLECTED("COLLECTED"),
    @SerializedName("DERIVED")
    DERIVED("DERIVED"),
    @SerializedName("ARC_MANAGED")
    ARC_MANAGED("ARC_MANAGED"),
    @SerializedName("CONFIGURED")
    CONFIGURED("CONFIGURED"),
    @SerializedName("INTERNAL")
    INTERNAL("INTERNAL"),
    @SerializedName("COMPOSITE")
    COMPOSITE("COMPOSITE"),
    @SerializedName("TRANSIENT")
    TRANSIENT("TRANSIENT");

    private String value;

    AttributeTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AttributeTypeEnum fromValue(String input) {
      for (AttributeTypeEnum b : AttributeTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AttributeTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AttributeTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AttributeTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AttributeTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("attributeType")
  private AttributeTypeEnum attributeType = null;

  @SerializedName("businessRoleCriteria")
  private Boolean businessRoleCriteria = null;

  @SerializedName("clobAttr")
  private Boolean clobAttr = null;

  @SerializedName("composition")
  private String composition = null;

  @SerializedName("curatable")
  private Boolean curatable = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("displayOrder")
  private Integer displayOrder = null;

  /**
   * Gets or Sets entityType
   */
  @JsonAdapter(EntityTypeEnum.Adapter.class)
  public enum EntityTypeEnum {
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("GROUP")
    GROUP("GROUP"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("ACCOUNT_USER")
    ACCOUNT_USER("ACCOUNT_USER"),
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("PERMISSION_TO_HOLDERS")
    PERMISSION_TO_HOLDERS("PERMISSION_TO_HOLDERS"),
    @SerializedName("PERMISSIONS_FROM_HOLDER")
    PERMISSIONS_FROM_HOLDER("PERMISSIONS_FROM_HOLDER"),
    @SerializedName("PERMISSION_HIERARCHY_TO_CHILD")
    PERMISSION_HIERARCHY_TO_CHILD("PERMISSION_HIERARCHY_TO_CHILD"),
    @SerializedName("PERMISSION_HIERARCHY_TO_PARENT")
    PERMISSION_HIERARCHY_TO_PARENT("PERMISSION_HIERARCHY_TO_PARENT"),
    @SerializedName("GROUPMEMBERSHIP")
    GROUPMEMBERSHIP("GROUPMEMBERSHIP"),
    @SerializedName("GROUPTOGROUP")
    GROUPTOGROUP("GROUPTOGROUP"),
    @SerializedName("REVIEW_DEFINITION")
    REVIEW_DEFINITION("REVIEW_DEFINITION"),
    @SerializedName("REVIEWINSTANCE")
    REVIEWINSTANCE("REVIEWINSTANCE"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION"),
    @SerializedName("IDENTITY_SOURCE")
    IDENTITY_SOURCE("IDENTITY_SOURCE"),
    @SerializedName("IDMDRIVER")
    IDMDRIVER("IDMDRIVER"),
    @SerializedName("NAV_ITEM")
    NAV_ITEM("NAV_ITEM"),
    @SerializedName("REVIEW_ACT_ITEM")
    REVIEW_ACT_ITEM("REVIEW_ACT_ITEM"),
    @SerializedName("ATTRIBUTE_DEFINITION")
    ATTRIBUTE_DEFINITION("ATTRIBUTE_DEFINITION"),
    @SerializedName("DATA_SOURCE")
    DATA_SOURCE("DATA_SOURCE"),
    @SerializedName("ENTITY_SCHEMA")
    ENTITY_SCHEMA("ENTITY_SCHEMA"),
    @SerializedName("CHANGE_REQUEST_ITEM")
    CHANGE_REQUEST_ITEM("CHANGE_REQUEST_ITEM"),
    @SerializedName("TAG")
    TAG("TAG"),
    @SerializedName("AUTH_ROLE")
    AUTH_ROLE("AUTH_ROLE"),
    @SerializedName("SCHEDULE")
    SCHEDULE("SCHEDULE"),
    @SerializedName("ROLE_POLICY")
    ROLE_POLICY("ROLE_POLICY"),
    @SerializedName("SOD_POLICY")
    SOD_POLICY("SOD_POLICY"),
    @SerializedName("ACCOUNT_CATEGORY")
    ACCOUNT_CATEGORY("ACCOUNT_CATEGORY"),
    @SerializedName("SOD_CASE")
    SOD_CASE("SOD_CASE"),
    @SerializedName("ADVISOR_FEED")
    ADVISOR_FEED("ADVISOR_FEED"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("APPROVAL_POLICY")
    APPROVAL_POLICY("APPROVAL_POLICY"),
    @SerializedName("GOVERNANCE")
    GOVERNANCE("GOVERNANCE"),
    @SerializedName("COVERAGE_MAP")
    COVERAGE_MAP("COVERAGE_MAP"),
    @SerializedName("RISK_SCORE_CFG")
    RISK_SCORE_CFG("RISK_SCORE_CFG"),
    @SerializedName("ANALYTICS")
    ANALYTICS("ANALYTICS"),
    @SerializedName("ENTITY_CATEGORY")
    ENTITY_CATEGORY("ENTITY_CATEGORY"),
    @SerializedName("AFFILIATED_USER")
    AFFILIATED_USER("AFFILIATED_USER"),
    @SerializedName("SUPERVISING_USER")
    SUPERVISING_USER("SUPERVISING_USER"),
    @SerializedName("HOLDER_USER")
    HOLDER_USER("HOLDER_USER"),
    @SerializedName("PERMISSION_BOUND")
    PERMISSION_BOUND("PERMISSION_BOUND"),
    @SerializedName("SUB_PERMISSION")
    SUB_PERMISSION("SUB_PERMISSION"),
    @SerializedName("SUBORDINATE_APP")
    SUBORDINATE_APP("SUBORDINATE_APP"),
    @SerializedName("SUB_GROUP")
    SUB_GROUP("SUB_GROUP"),
    @SerializedName("NOTIFICATION_TEMPLATE")
    NOTIFICATION_TEMPLATE("NOTIFICATION_TEMPLATE"),
    @SerializedName("ACCESS_REQUEST_POLICY")
    ACCESS_REQUEST_POLICY("ACCESS_REQUEST_POLICY"),
    @SerializedName("ACCESS_REQUEST_APPROVAL_POLICY")
    ACCESS_REQUEST_APPROVAL_POLICY("ACCESS_REQUEST_APPROVAL_POLICY"),
    @SerializedName("LICENSE")
    LICENSE("LICENSE"),
    @SerializedName("DATA_POLICY")
    DATA_POLICY("DATA_POLICY"),
    @SerializedName("CERTIFICATION_POLICY")
    CERTIFICATION_POLICY("CERTIFICATION_POLICY"),
    @SerializedName("ANALYTICS_FACT")
    ANALYTICS_FACT("ANALYTICS_FACT"),
    @SerializedName("ANALYTICS_DASHBOARD")
    ANALYTICS_DASHBOARD("ANALYTICS_DASHBOARD"),
    @SerializedName("DECISION_SUPPORT_POLICY")
    DECISION_SUPPORT_POLICY("DECISION_SUPPORT_POLICY"),
    @SerializedName("TROUBLE_SHOOT_INFO")
    TROUBLE_SHOOT_INFO("TROUBLE_SHOOT_INFO"),
    @SerializedName("ARCHIVAL")
    ARCHIVAL("ARCHIVAL"),
    @SerializedName("MAINTENANCE")
    MAINTENANCE("MAINTENANCE"),
    @SerializedName("DATA_QUERY")
    DATA_QUERY("DATA_QUERY"),
    @SerializedName("ACCESS_REQUEST_ITEM")
    ACCESS_REQUEST_ITEM("ACCESS_REQUEST_ITEM"),
    @SerializedName("REVIEW_ITEM")
    REVIEW_ITEM("REVIEW_ITEM"),
    @SerializedName("FULFILLMENT_ITEM")
    FULFILLMENT_ITEM("FULFILLMENT_ITEM"),
    @SerializedName("PERMISSION_ASSIGNMENT")
    PERMISSION_ASSIGNMENT("PERMISSION_ASSIGNMENT"),
    @SerializedName("AUTHORIZATION")
    AUTHORIZATION("AUTHORIZATION"),
    @SerializedName("CERTIFICATION_POLICY_VIOLATION")
    CERTIFICATION_POLICY_VIOLATION("CERTIFICATION_POLICY_VIOLATION"),
    @SerializedName("REMEDIATION")
    REMEDIATION("REMEDIATION"),
    @SerializedName("SOD_VIOLATION")
    SOD_VIOLATION("SOD_VIOLATION"),
    @SerializedName("CAPPLICATION")
    CAPPLICATION("CAPPLICATION"),
    @SerializedName("FULFILLMENT_TARGET")
    FULFILLMENT_TARGET("FULFILLMENT_TARGET"),
    @SerializedName("FULFILLMENT_SAMPLE")
    FULFILLMENT_SAMPLE("FULFILLMENT_SAMPLE"),
    @SerializedName("USER_SOURCE")
    USER_SOURCE("USER_SOURCE"),
    @SerializedName("DATA_CENTER")
    DATA_CENTER("DATA_CENTER"),
    @SerializedName("DATA_CONN_SOURCE")
    DATA_CONN_SOURCE("DATA_CONN_SOURCE"),
    @SerializedName("ACCESS_REQUEST")
    ACCESS_REQUEST("ACCESS_REQUEST"),
    @SerializedName("ACCESS_REQUEST_APPROVAL")
    ACCESS_REQUEST_APPROVAL("ACCESS_REQUEST_APPROVAL"),
    @SerializedName("PSODV_APPROVAL")
    PSODV_APPROVAL("PSODV_APPROVAL"),
    @SerializedName("FORM")
    FORM("FORM"),
    @SerializedName("CUSER")
    CUSER("CUSER"),
    @SerializedName("REVIEW_ITEM_ACTION")
    REVIEW_ITEM_ACTION("REVIEW_ITEM_ACTION"),
    @SerializedName("USERS_AND_GROUPS")
    USERS_AND_GROUPS("USERS_AND_GROUPS"),
    @SerializedName("DASHBOARD")
    DASHBOARD("DASHBOARD"),
    @SerializedName("SOD_APPROVAL_POLICY")
    SOD_APPROVAL_POLICY("SOD_APPROVAL_POLICY"),
    @SerializedName("SOD_VIOLATION_CASE")
    SOD_VIOLATION_CASE("SOD_VIOLATION_CASE");

    private String value;

    EntityTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static EntityTypeEnum fromValue(String input) {
      for (EntityTypeEnum b : EntityTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<EntityTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EntityTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public EntityTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return EntityTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("entityType")
  private EntityTypeEnum entityType = null;

  @SerializedName("entityTypeModifiable")
  private Boolean entityTypeModifiable = null;

  @SerializedName("enumType")
  private String enumType = null;

  @SerializedName("excludedFromCuration")
  private Boolean excludedFromCuration = null;

  @SerializedName("excludedFromQuickSearch")
  private Boolean excludedFromQuickSearch = null;

  @SerializedName("excludedFromReview")
  private Boolean excludedFromReview = null;

  @SerializedName("excludedFromSearch")
  private Boolean excludedFromSearch = null;

  @SerializedName("excludedFromSorting")
  private Boolean excludedFromSorting = null;

  @SerializedName("extended")
  private Boolean extended = null;

  @SerializedName("idmOnly")
  private Boolean idmOnly = null;

  @SerializedName("joinable")
  private Boolean joinable = null;

  @SerializedName("length")
  private Integer length = null;

  @SerializedName("listable")
  private Boolean listable = null;

  @SerializedName("multiValued")
  private Boolean multiValued = null;

  @SerializedName("parentAttribute")
  private AttributeDefinition parentAttribute = null;

  @SerializedName("primaryAttributeKey")
  private String primaryAttributeKey = null;

  @SerializedName("quickInfo")
  private Boolean quickInfo = null;

  @SerializedName("quickSearchable")
  private Boolean quickSearchable = null;

  @SerializedName("readOnly")
  private Boolean readOnly = null;

  @SerializedName("required")
  private Boolean required = null;

  @SerializedName("reviewCriteria")
  private Boolean reviewCriteria = null;

  /**
   * Gets or Sets reviewType
   */
  @JsonAdapter(ReviewTypeEnum.Adapter.class)
  public enum ReviewTypeEnum {
    @SerializedName("USER_ACCESS")
    USER_ACCESS("USER_ACCESS"),
    @SerializedName("ORPHAN")
    ORPHAN("ORPHAN"),
    @SerializedName("BROLE_MEMB")
    BROLE_MEMB("BROLE_MEMB"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("STAGED")
    STAGED("STAGED"),
    @SerializedName("USER_PROFILE")
    USER_PROFILE("USER_PROFILE"),
    @SerializedName("DIRECT_REPORT")
    DIRECT_REPORT("DIRECT_REPORT"),
    @SerializedName("MICRO_CERT")
    MICRO_CERT("MICRO_CERT"),
    @SerializedName("MICRO_CERT_DATA")
    MICRO_CERT_DATA("MICRO_CERT_DATA"),
    @SerializedName("BROLE_DEFN")
    BROLE_DEFN("BROLE_DEFN"),
    @SerializedName("ACCOUNT_ACCESS")
    ACCOUNT_ACCESS("ACCOUNT_ACCESS"),
    @SerializedName("BROLE_AUTH")
    BROLE_AUTH("BROLE_AUTH"),
    @SerializedName("TROLE_DEFN")
    TROLE_DEFN("TROLE_DEFN");

    private String value;

    ReviewTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ReviewTypeEnum fromValue(String input) {
      for (ReviewTypeEnum b : ReviewTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ReviewTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReviewTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ReviewTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ReviewTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("reviewType")
  private ReviewTypeEnum reviewType = null;

  @SerializedName("reviewable")
  private Boolean reviewable = null;

  @SerializedName("searchable")
  private Boolean searchable = null;

  @SerializedName("sortable")
  private Boolean sortable = null;

  @SerializedName("targetEntity")
  private String targetEntity = null;

  @SerializedName("truncatable")
  private Boolean truncatable = null;

  @SerializedName("typeAheadSearchable")
  private Boolean typeAheadSearchable = null;

  @SerializedName("used")
  private Boolean used = null;

  public Attribute advancedSearchable(Boolean advancedSearchable) {
    this.advancedSearchable = advancedSearchable;
    return this;
  }

   /**
   * Get advancedSearchable
   * @return advancedSearchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isAdvancedSearchable() {
    return advancedSearchable;
  }

  public void setAdvancedSearchable(Boolean advancedSearchable) {
    this.advancedSearchable = advancedSearchable;
  }

  public Attribute allowStaticMapping(Boolean allowStaticMapping) {
    this.allowStaticMapping = allowStaticMapping;
    return this;
  }

   /**
   * Get allowStaticMapping
   * @return allowStaticMapping
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowStaticMapping() {
    return allowStaticMapping;
  }

  public void setAllowStaticMapping(Boolean allowStaticMapping) {
    this.allowStaticMapping = allowStaticMapping;
  }

  public Attribute allowedOnReviewerItem(Boolean allowedOnReviewerItem) {
    this.allowedOnReviewerItem = allowedOnReviewerItem;
    return this;
  }

   /**
   * Get allowedOnReviewerItem
   * @return allowedOnReviewerItem
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowedOnReviewerItem() {
    return allowedOnReviewerItem;
  }

  public void setAllowedOnReviewerItem(Boolean allowedOnReviewerItem) {
    this.allowedOnReviewerItem = allowedOnReviewerItem;
  }

  public Attribute attributeDataType(AttributeDataTypeEnum attributeDataType) {
    this.attributeDataType = attributeDataType;
    return this;
  }

   /**
   * Get attributeDataType
   * @return attributeDataType
  **/
  @ApiModelProperty(value = "")
  public AttributeDataTypeEnum getAttributeDataType() {
    return attributeDataType;
  }

  public void setAttributeDataType(AttributeDataTypeEnum attributeDataType) {
    this.attributeDataType = attributeDataType;
  }

  public Attribute attributeId(Long attributeId) {
    this.attributeId = attributeId;
    return this;
  }

   /**
   * Get attributeId
   * @return attributeId
  **/
  @ApiModelProperty(value = "")
  public Long getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(Long attributeId) {
    this.attributeId = attributeId;
  }

  public Attribute attributeKey(String attributeKey) {
    this.attributeKey = attributeKey;
    return this;
  }

   /**
   * Get attributeKey
   * @return attributeKey
  **/
  @ApiModelProperty(value = "")
  public String getAttributeKey() {
    return attributeKey;
  }

  public void setAttributeKey(String attributeKey) {
    this.attributeKey = attributeKey;
  }

  public Attribute attributeState(AttributeStateEnum attributeState) {
    this.attributeState = attributeState;
    return this;
  }

   /**
   * Get attributeState
   * @return attributeState
  **/
  @ApiModelProperty(value = "")
  public AttributeStateEnum getAttributeState() {
    return attributeState;
  }

  public void setAttributeState(AttributeStateEnum attributeState) {
    this.attributeState = attributeState;
  }

  public Attribute attributeType(AttributeTypeEnum attributeType) {
    this.attributeType = attributeType;
    return this;
  }

   /**
   * Get attributeType
   * @return attributeType
  **/
  @ApiModelProperty(value = "")
  public AttributeTypeEnum getAttributeType() {
    return attributeType;
  }

  public void setAttributeType(AttributeTypeEnum attributeType) {
    this.attributeType = attributeType;
  }

  public Attribute businessRoleCriteria(Boolean businessRoleCriteria) {
    this.businessRoleCriteria = businessRoleCriteria;
    return this;
  }

   /**
   * Get businessRoleCriteria
   * @return businessRoleCriteria
  **/
  @ApiModelProperty(value = "")
  public Boolean isBusinessRoleCriteria() {
    return businessRoleCriteria;
  }

  public void setBusinessRoleCriteria(Boolean businessRoleCriteria) {
    this.businessRoleCriteria = businessRoleCriteria;
  }

  public Attribute clobAttr(Boolean clobAttr) {
    this.clobAttr = clobAttr;
    return this;
  }

   /**
   * Get clobAttr
   * @return clobAttr
  **/
  @ApiModelProperty(value = "")
  public Boolean isClobAttr() {
    return clobAttr;
  }

  public void setClobAttr(Boolean clobAttr) {
    this.clobAttr = clobAttr;
  }

  public Attribute composition(String composition) {
    this.composition = composition;
    return this;
  }

   /**
   * Get composition
   * @return composition
  **/
  @ApiModelProperty(value = "")
  public String getComposition() {
    return composition;
  }

  public void setComposition(String composition) {
    this.composition = composition;
  }

  public Attribute curatable(Boolean curatable) {
    this.curatable = curatable;
    return this;
  }

   /**
   * Get curatable
   * @return curatable
  **/
  @ApiModelProperty(value = "")
  public Boolean isCuratable() {
    return curatable;
  }

  public void setCuratable(Boolean curatable) {
    this.curatable = curatable;
  }

  public Attribute displayName(String displayName) {
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

  public Attribute displayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

   /**
   * Get displayOrder
   * @return displayOrder
  **/
  @ApiModelProperty(value = "")
  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public Attribute entityType(EntityTypeEnum entityType) {
    this.entityType = entityType;
    return this;
  }

   /**
   * Get entityType
   * @return entityType
  **/
  @ApiModelProperty(value = "")
  public EntityTypeEnum getEntityType() {
    return entityType;
  }

  public void setEntityType(EntityTypeEnum entityType) {
    this.entityType = entityType;
  }

  public Attribute entityTypeModifiable(Boolean entityTypeModifiable) {
    this.entityTypeModifiable = entityTypeModifiable;
    return this;
  }

   /**
   * Get entityTypeModifiable
   * @return entityTypeModifiable
  **/
  @ApiModelProperty(value = "")
  public Boolean isEntityTypeModifiable() {
    return entityTypeModifiable;
  }

  public void setEntityTypeModifiable(Boolean entityTypeModifiable) {
    this.entityTypeModifiable = entityTypeModifiable;
  }

  public Attribute enumType(String enumType) {
    this.enumType = enumType;
    return this;
  }

   /**
   * Get enumType
   * @return enumType
  **/
  @ApiModelProperty(value = "")
  public String getEnumType() {
    return enumType;
  }

  public void setEnumType(String enumType) {
    this.enumType = enumType;
  }

  public Attribute excludedFromCuration(Boolean excludedFromCuration) {
    this.excludedFromCuration = excludedFromCuration;
    return this;
  }

   /**
   * Get excludedFromCuration
   * @return excludedFromCuration
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludedFromCuration() {
    return excludedFromCuration;
  }

  public void setExcludedFromCuration(Boolean excludedFromCuration) {
    this.excludedFromCuration = excludedFromCuration;
  }

  public Attribute excludedFromQuickSearch(Boolean excludedFromQuickSearch) {
    this.excludedFromQuickSearch = excludedFromQuickSearch;
    return this;
  }

   /**
   * Get excludedFromQuickSearch
   * @return excludedFromQuickSearch
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludedFromQuickSearch() {
    return excludedFromQuickSearch;
  }

  public void setExcludedFromQuickSearch(Boolean excludedFromQuickSearch) {
    this.excludedFromQuickSearch = excludedFromQuickSearch;
  }

  public Attribute excludedFromReview(Boolean excludedFromReview) {
    this.excludedFromReview = excludedFromReview;
    return this;
  }

   /**
   * Get excludedFromReview
   * @return excludedFromReview
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludedFromReview() {
    return excludedFromReview;
  }

  public void setExcludedFromReview(Boolean excludedFromReview) {
    this.excludedFromReview = excludedFromReview;
  }

  public Attribute excludedFromSearch(Boolean excludedFromSearch) {
    this.excludedFromSearch = excludedFromSearch;
    return this;
  }

   /**
   * Get excludedFromSearch
   * @return excludedFromSearch
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludedFromSearch() {
    return excludedFromSearch;
  }

  public void setExcludedFromSearch(Boolean excludedFromSearch) {
    this.excludedFromSearch = excludedFromSearch;
  }

  public Attribute excludedFromSorting(Boolean excludedFromSorting) {
    this.excludedFromSorting = excludedFromSorting;
    return this;
  }

   /**
   * Get excludedFromSorting
   * @return excludedFromSorting
  **/
  @ApiModelProperty(value = "")
  public Boolean isExcludedFromSorting() {
    return excludedFromSorting;
  }

  public void setExcludedFromSorting(Boolean excludedFromSorting) {
    this.excludedFromSorting = excludedFromSorting;
  }

  public Attribute extended(Boolean extended) {
    this.extended = extended;
    return this;
  }

   /**
   * Get extended
   * @return extended
  **/
  @ApiModelProperty(value = "")
  public Boolean isExtended() {
    return extended;
  }

  public void setExtended(Boolean extended) {
    this.extended = extended;
  }

  public Attribute idmOnly(Boolean idmOnly) {
    this.idmOnly = idmOnly;
    return this;
  }

   /**
   * Get idmOnly
   * @return idmOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean isIdmOnly() {
    return idmOnly;
  }

  public void setIdmOnly(Boolean idmOnly) {
    this.idmOnly = idmOnly;
  }

  public Attribute joinable(Boolean joinable) {
    this.joinable = joinable;
    return this;
  }

   /**
   * Get joinable
   * @return joinable
  **/
  @ApiModelProperty(value = "")
  public Boolean isJoinable() {
    return joinable;
  }

  public void setJoinable(Boolean joinable) {
    this.joinable = joinable;
  }

  public Attribute length(Integer length) {
    this.length = length;
    return this;
  }

   /**
   * Get length
   * @return length
  **/
  @ApiModelProperty(value = "")
  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Attribute listable(Boolean listable) {
    this.listable = listable;
    return this;
  }

   /**
   * Get listable
   * @return listable
  **/
  @ApiModelProperty(value = "")
  public Boolean isListable() {
    return listable;
  }

  public void setListable(Boolean listable) {
    this.listable = listable;
  }

  public Attribute multiValued(Boolean multiValued) {
    this.multiValued = multiValued;
    return this;
  }

   /**
   * Get multiValued
   * @return multiValued
  **/
  @ApiModelProperty(value = "")
  public Boolean isMultiValued() {
    return multiValued;
  }

  public void setMultiValued(Boolean multiValued) {
    this.multiValued = multiValued;
  }

  public Attribute parentAttribute(AttributeDefinition parentAttribute) {
    this.parentAttribute = parentAttribute;
    return this;
  }

   /**
   * Get parentAttribute
   * @return parentAttribute
  **/
  @ApiModelProperty(value = "")
  public AttributeDefinition getParentAttribute() {
    return parentAttribute;
  }

  public void setParentAttribute(AttributeDefinition parentAttribute) {
    this.parentAttribute = parentAttribute;
  }

  public Attribute primaryAttributeKey(String primaryAttributeKey) {
    this.primaryAttributeKey = primaryAttributeKey;
    return this;
  }

   /**
   * Get primaryAttributeKey
   * @return primaryAttributeKey
  **/
  @ApiModelProperty(value = "")
  public String getPrimaryAttributeKey() {
    return primaryAttributeKey;
  }

  public void setPrimaryAttributeKey(String primaryAttributeKey) {
    this.primaryAttributeKey = primaryAttributeKey;
  }

  public Attribute quickInfo(Boolean quickInfo) {
    this.quickInfo = quickInfo;
    return this;
  }

   /**
   * Get quickInfo
   * @return quickInfo
  **/
  @ApiModelProperty(value = "")
  public Boolean isQuickInfo() {
    return quickInfo;
  }

  public void setQuickInfo(Boolean quickInfo) {
    this.quickInfo = quickInfo;
  }

  public Attribute quickSearchable(Boolean quickSearchable) {
    this.quickSearchable = quickSearchable;
    return this;
  }

   /**
   * Get quickSearchable
   * @return quickSearchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isQuickSearchable() {
    return quickSearchable;
  }

  public void setQuickSearchable(Boolean quickSearchable) {
    this.quickSearchable = quickSearchable;
  }

  public Attribute readOnly(Boolean readOnly) {
    this.readOnly = readOnly;
    return this;
  }

   /**
   * Get readOnly
   * @return readOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
  }

  public Attribute required(Boolean required) {
    this.required = required;
    return this;
  }

   /**
   * Get required
   * @return required
  **/
  @ApiModelProperty(value = "")
  public Boolean isRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public Attribute reviewCriteria(Boolean reviewCriteria) {
    this.reviewCriteria = reviewCriteria;
    return this;
  }

   /**
   * Get reviewCriteria
   * @return reviewCriteria
  **/
  @ApiModelProperty(value = "")
  public Boolean isReviewCriteria() {
    return reviewCriteria;
  }

  public void setReviewCriteria(Boolean reviewCriteria) {
    this.reviewCriteria = reviewCriteria;
  }

  public Attribute reviewType(ReviewTypeEnum reviewType) {
    this.reviewType = reviewType;
    return this;
  }

   /**
   * Get reviewType
   * @return reviewType
  **/
  @ApiModelProperty(value = "")
  public ReviewTypeEnum getReviewType() {
    return reviewType;
  }

  public void setReviewType(ReviewTypeEnum reviewType) {
    this.reviewType = reviewType;
  }

  public Attribute reviewable(Boolean reviewable) {
    this.reviewable = reviewable;
    return this;
  }

   /**
   * Get reviewable
   * @return reviewable
  **/
  @ApiModelProperty(value = "")
  public Boolean isReviewable() {
    return reviewable;
  }

  public void setReviewable(Boolean reviewable) {
    this.reviewable = reviewable;
  }

  public Attribute searchable(Boolean searchable) {
    this.searchable = searchable;
    return this;
  }

   /**
   * Get searchable
   * @return searchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isSearchable() {
    return searchable;
  }

  public void setSearchable(Boolean searchable) {
    this.searchable = searchable;
  }

  public Attribute sortable(Boolean sortable) {
    this.sortable = sortable;
    return this;
  }

   /**
   * Get sortable
   * @return sortable
  **/
  @ApiModelProperty(value = "")
  public Boolean isSortable() {
    return sortable;
  }

  public void setSortable(Boolean sortable) {
    this.sortable = sortable;
  }

  public Attribute targetEntity(String targetEntity) {
    this.targetEntity = targetEntity;
    return this;
  }

   /**
   * Get targetEntity
   * @return targetEntity
  **/
  @ApiModelProperty(value = "")
  public String getTargetEntity() {
    return targetEntity;
  }

  public void setTargetEntity(String targetEntity) {
    this.targetEntity = targetEntity;
  }

  public Attribute truncatable(Boolean truncatable) {
    this.truncatable = truncatable;
    return this;
  }

   /**
   * Get truncatable
   * @return truncatable
  **/
  @ApiModelProperty(value = "")
  public Boolean isTruncatable() {
    return truncatable;
  }

  public void setTruncatable(Boolean truncatable) {
    this.truncatable = truncatable;
  }

  public Attribute typeAheadSearchable(Boolean typeAheadSearchable) {
    this.typeAheadSearchable = typeAheadSearchable;
    return this;
  }

   /**
   * Get typeAheadSearchable
   * @return typeAheadSearchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isTypeAheadSearchable() {
    return typeAheadSearchable;
  }

  public void setTypeAheadSearchable(Boolean typeAheadSearchable) {
    this.typeAheadSearchable = typeAheadSearchable;
  }

  public Attribute used(Boolean used) {
    this.used = used;
    return this;
  }

   /**
   * Get used
   * @return used
  **/
  @ApiModelProperty(value = "")
  public Boolean isUsed() {
    return used;
  }

  public void setUsed(Boolean used) {
    this.used = used;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Attribute attribute = (Attribute) o;
    return Objects.equals(this.advancedSearchable, attribute.advancedSearchable) &&
        Objects.equals(this.allowStaticMapping, attribute.allowStaticMapping) &&
        Objects.equals(this.allowedOnReviewerItem, attribute.allowedOnReviewerItem) &&
        Objects.equals(this.attributeDataType, attribute.attributeDataType) &&
        Objects.equals(this.attributeId, attribute.attributeId) &&
        Objects.equals(this.attributeKey, attribute.attributeKey) &&
        Objects.equals(this.attributeState, attribute.attributeState) &&
        Objects.equals(this.attributeType, attribute.attributeType) &&
        Objects.equals(this.businessRoleCriteria, attribute.businessRoleCriteria) &&
        Objects.equals(this.clobAttr, attribute.clobAttr) &&
        Objects.equals(this.composition, attribute.composition) &&
        Objects.equals(this.curatable, attribute.curatable) &&
        Objects.equals(this.displayName, attribute.displayName) &&
        Objects.equals(this.displayOrder, attribute.displayOrder) &&
        Objects.equals(this.entityType, attribute.entityType) &&
        Objects.equals(this.entityTypeModifiable, attribute.entityTypeModifiable) &&
        Objects.equals(this.enumType, attribute.enumType) &&
        Objects.equals(this.excludedFromCuration, attribute.excludedFromCuration) &&
        Objects.equals(this.excludedFromQuickSearch, attribute.excludedFromQuickSearch) &&
        Objects.equals(this.excludedFromReview, attribute.excludedFromReview) &&
        Objects.equals(this.excludedFromSearch, attribute.excludedFromSearch) &&
        Objects.equals(this.excludedFromSorting, attribute.excludedFromSorting) &&
        Objects.equals(this.extended, attribute.extended) &&
        Objects.equals(this.idmOnly, attribute.idmOnly) &&
        Objects.equals(this.joinable, attribute.joinable) &&
        Objects.equals(this.length, attribute.length) &&
        Objects.equals(this.listable, attribute.listable) &&
        Objects.equals(this.multiValued, attribute.multiValued) &&
        Objects.equals(this.parentAttribute, attribute.parentAttribute) &&
        Objects.equals(this.primaryAttributeKey, attribute.primaryAttributeKey) &&
        Objects.equals(this.quickInfo, attribute.quickInfo) &&
        Objects.equals(this.quickSearchable, attribute.quickSearchable) &&
        Objects.equals(this.readOnly, attribute.readOnly) &&
        Objects.equals(this.required, attribute.required) &&
        Objects.equals(this.reviewCriteria, attribute.reviewCriteria) &&
        Objects.equals(this.reviewType, attribute.reviewType) &&
        Objects.equals(this.reviewable, attribute.reviewable) &&
        Objects.equals(this.searchable, attribute.searchable) &&
        Objects.equals(this.sortable, attribute.sortable) &&
        Objects.equals(this.targetEntity, attribute.targetEntity) &&
        Objects.equals(this.truncatable, attribute.truncatable) &&
        Objects.equals(this.typeAheadSearchable, attribute.typeAheadSearchable) &&
        Objects.equals(this.used, attribute.used);
  }

  @Override
  public int hashCode() {
    return Objects.hash(advancedSearchable, allowStaticMapping, allowedOnReviewerItem, attributeDataType, attributeId, attributeKey, attributeState, attributeType, businessRoleCriteria, clobAttr, composition, curatable, displayName, displayOrder, entityType, entityTypeModifiable, enumType, excludedFromCuration, excludedFromQuickSearch, excludedFromReview, excludedFromSearch, excludedFromSorting, extended, idmOnly, joinable, length, listable, multiValued, parentAttribute, primaryAttributeKey, quickInfo, quickSearchable, readOnly, required, reviewCriteria, reviewType, reviewable, searchable, sortable, targetEntity, truncatable, typeAheadSearchable, used);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Attribute {\n");
    
    sb.append("    advancedSearchable: ").append(toIndentedString(advancedSearchable)).append("\n");
    sb.append("    allowStaticMapping: ").append(toIndentedString(allowStaticMapping)).append("\n");
    sb.append("    allowedOnReviewerItem: ").append(toIndentedString(allowedOnReviewerItem)).append("\n");
    sb.append("    attributeDataType: ").append(toIndentedString(attributeDataType)).append("\n");
    sb.append("    attributeId: ").append(toIndentedString(attributeId)).append("\n");
    sb.append("    attributeKey: ").append(toIndentedString(attributeKey)).append("\n");
    sb.append("    attributeState: ").append(toIndentedString(attributeState)).append("\n");
    sb.append("    attributeType: ").append(toIndentedString(attributeType)).append("\n");
    sb.append("    businessRoleCriteria: ").append(toIndentedString(businessRoleCriteria)).append("\n");
    sb.append("    clobAttr: ").append(toIndentedString(clobAttr)).append("\n");
    sb.append("    composition: ").append(toIndentedString(composition)).append("\n");
    sb.append("    curatable: ").append(toIndentedString(curatable)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    entityTypeModifiable: ").append(toIndentedString(entityTypeModifiable)).append("\n");
    sb.append("    enumType: ").append(toIndentedString(enumType)).append("\n");
    sb.append("    excludedFromCuration: ").append(toIndentedString(excludedFromCuration)).append("\n");
    sb.append("    excludedFromQuickSearch: ").append(toIndentedString(excludedFromQuickSearch)).append("\n");
    sb.append("    excludedFromReview: ").append(toIndentedString(excludedFromReview)).append("\n");
    sb.append("    excludedFromSearch: ").append(toIndentedString(excludedFromSearch)).append("\n");
    sb.append("    excludedFromSorting: ").append(toIndentedString(excludedFromSorting)).append("\n");
    sb.append("    extended: ").append(toIndentedString(extended)).append("\n");
    sb.append("    idmOnly: ").append(toIndentedString(idmOnly)).append("\n");
    sb.append("    joinable: ").append(toIndentedString(joinable)).append("\n");
    sb.append("    length: ").append(toIndentedString(length)).append("\n");
    sb.append("    listable: ").append(toIndentedString(listable)).append("\n");
    sb.append("    multiValued: ").append(toIndentedString(multiValued)).append("\n");
    sb.append("    parentAttribute: ").append(toIndentedString(parentAttribute)).append("\n");
    sb.append("    primaryAttributeKey: ").append(toIndentedString(primaryAttributeKey)).append("\n");
    sb.append("    quickInfo: ").append(toIndentedString(quickInfo)).append("\n");
    sb.append("    quickSearchable: ").append(toIndentedString(quickSearchable)).append("\n");
    sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    reviewCriteria: ").append(toIndentedString(reviewCriteria)).append("\n");
    sb.append("    reviewType: ").append(toIndentedString(reviewType)).append("\n");
    sb.append("    reviewable: ").append(toIndentedString(reviewable)).append("\n");
    sb.append("    searchable: ").append(toIndentedString(searchable)).append("\n");
    sb.append("    sortable: ").append(toIndentedString(sortable)).append("\n");
    sb.append("    targetEntity: ").append(toIndentedString(targetEntity)).append("\n");
    sb.append("    truncatable: ").append(toIndentedString(truncatable)).append("\n");
    sb.append("    typeAheadSearchable: ").append(toIndentedString(typeAheadSearchable)).append("\n");
    sb.append("    used: ").append(toIndentedString(used)).append("\n");
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
