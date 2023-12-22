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
import io.swagger.client.model.LocalizedLabel;
import java.io.IOException;
/**
 * AttributeDefinition
 */



public class AttributeDefinition {
  @SerializedName("advancedSearchable")
  private Boolean advancedSearchable = null;

  @SerializedName("allowStaticMapping")
  private Boolean allowStaticMapping = null;

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

  @SerializedName("attributeDataTypeStr")
  private Object attributeDataTypeStr = null;

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

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("displayName")
  private String displayName = null;

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

  @SerializedName("entityTypeStr")
  private Object entityTypeStr = null;

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

  @SerializedName("id")
  private Long id = null;

  @SerializedName("idmOnly")
  private Boolean idmOnly = null;

  @SerializedName("joinable")
  private Boolean joinable = null;

  @SerializedName("jsonDataKey")
  private String jsonDataKey = null;

  @SerializedName("length")
  private Integer length = null;

  @SerializedName("listable")
  private Boolean listable = null;

  @SerializedName("localizedDisplayName")
  private LocalizedLabel localizedDisplayName = null;

  @SerializedName("m_advancedSearchable")
  private Boolean mAdvancedSearchable = null;

  @SerializedName("m_allowStaticMapping")
  private Boolean mAllowStaticMapping = null;

  /**
   * Gets or Sets mAttributeDataType
   */
  @JsonAdapter(MAttributeDataTypeEnum.Adapter.class)
  public enum MAttributeDataTypeEnum {
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

    MAttributeDataTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MAttributeDataTypeEnum fromValue(String input) {
      for (MAttributeDataTypeEnum b : MAttributeDataTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MAttributeDataTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MAttributeDataTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MAttributeDataTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MAttributeDataTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_attributeDataType")
  private MAttributeDataTypeEnum mAttributeDataType = null;

  @SerializedName("m_attributeKey")
  private String mAttributeKey = null;

  /**
   * Gets or Sets mAttributeState
   */
  @JsonAdapter(MAttributeStateEnum.Adapter.class)
  public enum MAttributeStateEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),
    @SerializedName("DELETED")
    DELETED("DELETED"),
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE");

    private String value;

    MAttributeStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MAttributeStateEnum fromValue(String input) {
      for (MAttributeStateEnum b : MAttributeStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MAttributeStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MAttributeStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MAttributeStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MAttributeStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_attributeState")
  private MAttributeStateEnum mAttributeState = null;

  /**
   * Gets or Sets mAttributeType
   */
  @JsonAdapter(MAttributeTypeEnum.Adapter.class)
  public enum MAttributeTypeEnum {
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

    MAttributeTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MAttributeTypeEnum fromValue(String input) {
      for (MAttributeTypeEnum b : MAttributeTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MAttributeTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MAttributeTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MAttributeTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MAttributeTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_attributeType")
  private MAttributeTypeEnum mAttributeType = null;

  @SerializedName("m_businessRoleCriteria")
  private Boolean mBusinessRoleCriteria = null;

  @SerializedName("m_clobAttr")
  private Boolean mClobAttr = null;

  @SerializedName("m_composition")
  private String mComposition = null;

  @SerializedName("m_curatable")
  private Boolean mCuratable = null;

  /**
   * Gets or Sets mEntityType
   */
  @JsonAdapter(MEntityTypeEnum.Adapter.class)
  public enum MEntityTypeEnum {
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

    MEntityTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MEntityTypeEnum fromValue(String input) {
      for (MEntityTypeEnum b : MEntityTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MEntityTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MEntityTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MEntityTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MEntityTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_entityType")
  private MEntityTypeEnum mEntityType = null;

  @SerializedName("m_enumType")
  private String mEnumType = null;

  @SerializedName("m_excludedFromCuration")
  private Boolean mExcludedFromCuration = null;

  @SerializedName("m_excludedFromQuickSearch")
  private Boolean mExcludedFromQuickSearch = null;

  @SerializedName("m_excludedFromReview")
  private Boolean mExcludedFromReview = null;

  @SerializedName("m_excludedFromSearch")
  private Boolean mExcludedFromSearch = null;

  @SerializedName("m_excludedFromSorting")
  private Boolean mExcludedFromSorting = null;

  @SerializedName("m_extended")
  private Boolean mExtended = null;

  @SerializedName("m_fetchedLocalizedDisplayName")
  private LocalizedLabel mFetchedLocalizedDisplayName = null;

  @SerializedName("m_id")
  private Long mId = null;

  @SerializedName("m_idmOnly")
  private Boolean mIdmOnly = null;

  @SerializedName("m_joinable")
  private Boolean mJoinable = null;

  @SerializedName("m_length")
  private Integer mLength = null;

  @SerializedName("m_listable")
  private Boolean mListable = null;

  @SerializedName("m_localizedDisplayName")
  private LocalizedLabel mLocalizedDisplayName = null;

  @SerializedName("m_multiValued")
  private Boolean mMultiValued = null;

  @SerializedName("m_quickInfo")
  private Boolean mQuickInfo = null;

  @SerializedName("m_quickSearchable")
  private Boolean mQuickSearchable = null;

  @SerializedName("m_readOnly")
  private Boolean mReadOnly = null;

  @SerializedName("m_required")
  private Boolean mRequired = null;

  @SerializedName("m_reviewCriteria")
  private Boolean mReviewCriteria = null;

  @SerializedName("m_reviewable")
  private Boolean mReviewable = null;

  @SerializedName("m_searchable")
  private Boolean mSearchable = null;

  @SerializedName("m_sortable")
  private Boolean mSortable = null;

  @SerializedName("m_targetEntity")
  private String mTargetEntity = null;

  @SerializedName("m_truncatable")
  private Boolean mTruncatable = null;

  @SerializedName("m_typeAheadSearchable")
  private Boolean mTypeAheadSearchable = null;

  @SerializedName("m_used")
  private Boolean mUsed = null;

  @SerializedName("multiValued")
  private Boolean multiValued = null;

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

  public AttributeDefinition advancedSearchable(Boolean advancedSearchable) {
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

  public AttributeDefinition allowStaticMapping(Boolean allowStaticMapping) {
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

  public AttributeDefinition attributeDataType(AttributeDataTypeEnum attributeDataType) {
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

  public AttributeDefinition attributeDataTypeStr(Object attributeDataTypeStr) {
    this.attributeDataTypeStr = attributeDataTypeStr;
    return this;
  }

   /**
   * Get attributeDataTypeStr
   * @return attributeDataTypeStr
  **/
  @ApiModelProperty(value = "")
  public Object getAttributeDataTypeStr() {
    return attributeDataTypeStr;
  }

  public void setAttributeDataTypeStr(Object attributeDataTypeStr) {
    this.attributeDataTypeStr = attributeDataTypeStr;
  }

  public AttributeDefinition attributeKey(String attributeKey) {
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

  public AttributeDefinition attributeState(AttributeStateEnum attributeState) {
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

  public AttributeDefinition attributeType(AttributeTypeEnum attributeType) {
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

  public AttributeDefinition businessRoleCriteria(Boolean businessRoleCriteria) {
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

  public AttributeDefinition clobAttr(Boolean clobAttr) {
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

  public AttributeDefinition composition(String composition) {
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

  public AttributeDefinition curatable(Boolean curatable) {
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

  public AttributeDefinition deleted(Boolean deleted) {
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

  public AttributeDefinition displayName(String displayName) {
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

  public AttributeDefinition entityType(EntityTypeEnum entityType) {
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

  public AttributeDefinition entityTypeStr(Object entityTypeStr) {
    this.entityTypeStr = entityTypeStr;
    return this;
  }

   /**
   * Get entityTypeStr
   * @return entityTypeStr
  **/
  @ApiModelProperty(value = "")
  public Object getEntityTypeStr() {
    return entityTypeStr;
  }

  public void setEntityTypeStr(Object entityTypeStr) {
    this.entityTypeStr = entityTypeStr;
  }

  public AttributeDefinition enumType(String enumType) {
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

  public AttributeDefinition excludedFromCuration(Boolean excludedFromCuration) {
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

  public AttributeDefinition excludedFromQuickSearch(Boolean excludedFromQuickSearch) {
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

  public AttributeDefinition excludedFromReview(Boolean excludedFromReview) {
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

  public AttributeDefinition excludedFromSearch(Boolean excludedFromSearch) {
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

  public AttributeDefinition excludedFromSorting(Boolean excludedFromSorting) {
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

  public AttributeDefinition extended(Boolean extended) {
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

  public AttributeDefinition id(Long id) {
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

  public AttributeDefinition idmOnly(Boolean idmOnly) {
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

  public AttributeDefinition joinable(Boolean joinable) {
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

  public AttributeDefinition jsonDataKey(String jsonDataKey) {
    this.jsonDataKey = jsonDataKey;
    return this;
  }

   /**
   * Get jsonDataKey
   * @return jsonDataKey
  **/
  @ApiModelProperty(value = "")
  public String getJsonDataKey() {
    return jsonDataKey;
  }

  public void setJsonDataKey(String jsonDataKey) {
    this.jsonDataKey = jsonDataKey;
  }

  public AttributeDefinition length(Integer length) {
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

  public AttributeDefinition listable(Boolean listable) {
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

  public AttributeDefinition localizedDisplayName(LocalizedLabel localizedDisplayName) {
    this.localizedDisplayName = localizedDisplayName;
    return this;
  }

   /**
   * Get localizedDisplayName
   * @return localizedDisplayName
  **/
  @ApiModelProperty(value = "")
  public LocalizedLabel getLocalizedDisplayName() {
    return localizedDisplayName;
  }

  public void setLocalizedDisplayName(LocalizedLabel localizedDisplayName) {
    this.localizedDisplayName = localizedDisplayName;
  }

  public AttributeDefinition mAdvancedSearchable(Boolean mAdvancedSearchable) {
    this.mAdvancedSearchable = mAdvancedSearchable;
    return this;
  }

   /**
   * Get mAdvancedSearchable
   * @return mAdvancedSearchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMAdvancedSearchable() {
    return mAdvancedSearchable;
  }

  public void setMAdvancedSearchable(Boolean mAdvancedSearchable) {
    this.mAdvancedSearchable = mAdvancedSearchable;
  }

  public AttributeDefinition mAllowStaticMapping(Boolean mAllowStaticMapping) {
    this.mAllowStaticMapping = mAllowStaticMapping;
    return this;
  }

   /**
   * Get mAllowStaticMapping
   * @return mAllowStaticMapping
  **/
  @ApiModelProperty(value = "")
  public Boolean isMAllowStaticMapping() {
    return mAllowStaticMapping;
  }

  public void setMAllowStaticMapping(Boolean mAllowStaticMapping) {
    this.mAllowStaticMapping = mAllowStaticMapping;
  }

  public AttributeDefinition mAttributeDataType(MAttributeDataTypeEnum mAttributeDataType) {
    this.mAttributeDataType = mAttributeDataType;
    return this;
  }

   /**
   * Get mAttributeDataType
   * @return mAttributeDataType
  **/
  @ApiModelProperty(value = "")
  public MAttributeDataTypeEnum getMAttributeDataType() {
    return mAttributeDataType;
  }

  public void setMAttributeDataType(MAttributeDataTypeEnum mAttributeDataType) {
    this.mAttributeDataType = mAttributeDataType;
  }

  public AttributeDefinition mAttributeKey(String mAttributeKey) {
    this.mAttributeKey = mAttributeKey;
    return this;
  }

   /**
   * Get mAttributeKey
   * @return mAttributeKey
  **/
  @ApiModelProperty(value = "")
  public String getMAttributeKey() {
    return mAttributeKey;
  }

  public void setMAttributeKey(String mAttributeKey) {
    this.mAttributeKey = mAttributeKey;
  }

  public AttributeDefinition mAttributeState(MAttributeStateEnum mAttributeState) {
    this.mAttributeState = mAttributeState;
    return this;
  }

   /**
   * Get mAttributeState
   * @return mAttributeState
  **/
  @ApiModelProperty(value = "")
  public MAttributeStateEnum getMAttributeState() {
    return mAttributeState;
  }

  public void setMAttributeState(MAttributeStateEnum mAttributeState) {
    this.mAttributeState = mAttributeState;
  }

  public AttributeDefinition mAttributeType(MAttributeTypeEnum mAttributeType) {
    this.mAttributeType = mAttributeType;
    return this;
  }

   /**
   * Get mAttributeType
   * @return mAttributeType
  **/
  @ApiModelProperty(value = "")
  public MAttributeTypeEnum getMAttributeType() {
    return mAttributeType;
  }

  public void setMAttributeType(MAttributeTypeEnum mAttributeType) {
    this.mAttributeType = mAttributeType;
  }

  public AttributeDefinition mBusinessRoleCriteria(Boolean mBusinessRoleCriteria) {
    this.mBusinessRoleCriteria = mBusinessRoleCriteria;
    return this;
  }

   /**
   * Get mBusinessRoleCriteria
   * @return mBusinessRoleCriteria
  **/
  @ApiModelProperty(value = "")
  public Boolean isMBusinessRoleCriteria() {
    return mBusinessRoleCriteria;
  }

  public void setMBusinessRoleCriteria(Boolean mBusinessRoleCriteria) {
    this.mBusinessRoleCriteria = mBusinessRoleCriteria;
  }

  public AttributeDefinition mClobAttr(Boolean mClobAttr) {
    this.mClobAttr = mClobAttr;
    return this;
  }

   /**
   * Get mClobAttr
   * @return mClobAttr
  **/
  @ApiModelProperty(value = "")
  public Boolean isMClobAttr() {
    return mClobAttr;
  }

  public void setMClobAttr(Boolean mClobAttr) {
    this.mClobAttr = mClobAttr;
  }

  public AttributeDefinition mComposition(String mComposition) {
    this.mComposition = mComposition;
    return this;
  }

   /**
   * Get mComposition
   * @return mComposition
  **/
  @ApiModelProperty(value = "")
  public String getMComposition() {
    return mComposition;
  }

  public void setMComposition(String mComposition) {
    this.mComposition = mComposition;
  }

  public AttributeDefinition mCuratable(Boolean mCuratable) {
    this.mCuratable = mCuratable;
    return this;
  }

   /**
   * Get mCuratable
   * @return mCuratable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMCuratable() {
    return mCuratable;
  }

  public void setMCuratable(Boolean mCuratable) {
    this.mCuratable = mCuratable;
  }

  public AttributeDefinition mEntityType(MEntityTypeEnum mEntityType) {
    this.mEntityType = mEntityType;
    return this;
  }

   /**
   * Get mEntityType
   * @return mEntityType
  **/
  @ApiModelProperty(value = "")
  public MEntityTypeEnum getMEntityType() {
    return mEntityType;
  }

  public void setMEntityType(MEntityTypeEnum mEntityType) {
    this.mEntityType = mEntityType;
  }

  public AttributeDefinition mEnumType(String mEnumType) {
    this.mEnumType = mEnumType;
    return this;
  }

   /**
   * Get mEnumType
   * @return mEnumType
  **/
  @ApiModelProperty(value = "")
  public String getMEnumType() {
    return mEnumType;
  }

  public void setMEnumType(String mEnumType) {
    this.mEnumType = mEnumType;
  }

  public AttributeDefinition mExcludedFromCuration(Boolean mExcludedFromCuration) {
    this.mExcludedFromCuration = mExcludedFromCuration;
    return this;
  }

   /**
   * Get mExcludedFromCuration
   * @return mExcludedFromCuration
  **/
  @ApiModelProperty(value = "")
  public Boolean isMExcludedFromCuration() {
    return mExcludedFromCuration;
  }

  public void setMExcludedFromCuration(Boolean mExcludedFromCuration) {
    this.mExcludedFromCuration = mExcludedFromCuration;
  }

  public AttributeDefinition mExcludedFromQuickSearch(Boolean mExcludedFromQuickSearch) {
    this.mExcludedFromQuickSearch = mExcludedFromQuickSearch;
    return this;
  }

   /**
   * Get mExcludedFromQuickSearch
   * @return mExcludedFromQuickSearch
  **/
  @ApiModelProperty(value = "")
  public Boolean isMExcludedFromQuickSearch() {
    return mExcludedFromQuickSearch;
  }

  public void setMExcludedFromQuickSearch(Boolean mExcludedFromQuickSearch) {
    this.mExcludedFromQuickSearch = mExcludedFromQuickSearch;
  }

  public AttributeDefinition mExcludedFromReview(Boolean mExcludedFromReview) {
    this.mExcludedFromReview = mExcludedFromReview;
    return this;
  }

   /**
   * Get mExcludedFromReview
   * @return mExcludedFromReview
  **/
  @ApiModelProperty(value = "")
  public Boolean isMExcludedFromReview() {
    return mExcludedFromReview;
  }

  public void setMExcludedFromReview(Boolean mExcludedFromReview) {
    this.mExcludedFromReview = mExcludedFromReview;
  }

  public AttributeDefinition mExcludedFromSearch(Boolean mExcludedFromSearch) {
    this.mExcludedFromSearch = mExcludedFromSearch;
    return this;
  }

   /**
   * Get mExcludedFromSearch
   * @return mExcludedFromSearch
  **/
  @ApiModelProperty(value = "")
  public Boolean isMExcludedFromSearch() {
    return mExcludedFromSearch;
  }

  public void setMExcludedFromSearch(Boolean mExcludedFromSearch) {
    this.mExcludedFromSearch = mExcludedFromSearch;
  }

  public AttributeDefinition mExcludedFromSorting(Boolean mExcludedFromSorting) {
    this.mExcludedFromSorting = mExcludedFromSorting;
    return this;
  }

   /**
   * Get mExcludedFromSorting
   * @return mExcludedFromSorting
  **/
  @ApiModelProperty(value = "")
  public Boolean isMExcludedFromSorting() {
    return mExcludedFromSorting;
  }

  public void setMExcludedFromSorting(Boolean mExcludedFromSorting) {
    this.mExcludedFromSorting = mExcludedFromSorting;
  }

  public AttributeDefinition mExtended(Boolean mExtended) {
    this.mExtended = mExtended;
    return this;
  }

   /**
   * Get mExtended
   * @return mExtended
  **/
  @ApiModelProperty(value = "")
  public Boolean isMExtended() {
    return mExtended;
  }

  public void setMExtended(Boolean mExtended) {
    this.mExtended = mExtended;
  }

  public AttributeDefinition mFetchedLocalizedDisplayName(LocalizedLabel mFetchedLocalizedDisplayName) {
    this.mFetchedLocalizedDisplayName = mFetchedLocalizedDisplayName;
    return this;
  }

   /**
   * Get mFetchedLocalizedDisplayName
   * @return mFetchedLocalizedDisplayName
  **/
  @ApiModelProperty(value = "")
  public LocalizedLabel getMFetchedLocalizedDisplayName() {
    return mFetchedLocalizedDisplayName;
  }

  public void setMFetchedLocalizedDisplayName(LocalizedLabel mFetchedLocalizedDisplayName) {
    this.mFetchedLocalizedDisplayName = mFetchedLocalizedDisplayName;
  }

  public AttributeDefinition mId(Long mId) {
    this.mId = mId;
    return this;
  }

   /**
   * Get mId
   * @return mId
  **/
  @ApiModelProperty(value = "")
  public Long getMId() {
    return mId;
  }

  public void setMId(Long mId) {
    this.mId = mId;
  }

  public AttributeDefinition mIdmOnly(Boolean mIdmOnly) {
    this.mIdmOnly = mIdmOnly;
    return this;
  }

   /**
   * Get mIdmOnly
   * @return mIdmOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean isMIdmOnly() {
    return mIdmOnly;
  }

  public void setMIdmOnly(Boolean mIdmOnly) {
    this.mIdmOnly = mIdmOnly;
  }

  public AttributeDefinition mJoinable(Boolean mJoinable) {
    this.mJoinable = mJoinable;
    return this;
  }

   /**
   * Get mJoinable
   * @return mJoinable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMJoinable() {
    return mJoinable;
  }

  public void setMJoinable(Boolean mJoinable) {
    this.mJoinable = mJoinable;
  }

  public AttributeDefinition mLength(Integer mLength) {
    this.mLength = mLength;
    return this;
  }

   /**
   * Get mLength
   * @return mLength
  **/
  @ApiModelProperty(value = "")
  public Integer getMLength() {
    return mLength;
  }

  public void setMLength(Integer mLength) {
    this.mLength = mLength;
  }

  public AttributeDefinition mListable(Boolean mListable) {
    this.mListable = mListable;
    return this;
  }

   /**
   * Get mListable
   * @return mListable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMListable() {
    return mListable;
  }

  public void setMListable(Boolean mListable) {
    this.mListable = mListable;
  }

  public AttributeDefinition mLocalizedDisplayName(LocalizedLabel mLocalizedDisplayName) {
    this.mLocalizedDisplayName = mLocalizedDisplayName;
    return this;
  }

   /**
   * Get mLocalizedDisplayName
   * @return mLocalizedDisplayName
  **/
  @ApiModelProperty(value = "")
  public LocalizedLabel getMLocalizedDisplayName() {
    return mLocalizedDisplayName;
  }

  public void setMLocalizedDisplayName(LocalizedLabel mLocalizedDisplayName) {
    this.mLocalizedDisplayName = mLocalizedDisplayName;
  }

  public AttributeDefinition mMultiValued(Boolean mMultiValued) {
    this.mMultiValued = mMultiValued;
    return this;
  }

   /**
   * Get mMultiValued
   * @return mMultiValued
  **/
  @ApiModelProperty(value = "")
  public Boolean isMMultiValued() {
    return mMultiValued;
  }

  public void setMMultiValued(Boolean mMultiValued) {
    this.mMultiValued = mMultiValued;
  }

  public AttributeDefinition mQuickInfo(Boolean mQuickInfo) {
    this.mQuickInfo = mQuickInfo;
    return this;
  }

   /**
   * Get mQuickInfo
   * @return mQuickInfo
  **/
  @ApiModelProperty(value = "")
  public Boolean isMQuickInfo() {
    return mQuickInfo;
  }

  public void setMQuickInfo(Boolean mQuickInfo) {
    this.mQuickInfo = mQuickInfo;
  }

  public AttributeDefinition mQuickSearchable(Boolean mQuickSearchable) {
    this.mQuickSearchable = mQuickSearchable;
    return this;
  }

   /**
   * Get mQuickSearchable
   * @return mQuickSearchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMQuickSearchable() {
    return mQuickSearchable;
  }

  public void setMQuickSearchable(Boolean mQuickSearchable) {
    this.mQuickSearchable = mQuickSearchable;
  }

  public AttributeDefinition mReadOnly(Boolean mReadOnly) {
    this.mReadOnly = mReadOnly;
    return this;
  }

   /**
   * Get mReadOnly
   * @return mReadOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean isMReadOnly() {
    return mReadOnly;
  }

  public void setMReadOnly(Boolean mReadOnly) {
    this.mReadOnly = mReadOnly;
  }

  public AttributeDefinition mRequired(Boolean mRequired) {
    this.mRequired = mRequired;
    return this;
  }

   /**
   * Get mRequired
   * @return mRequired
  **/
  @ApiModelProperty(value = "")
  public Boolean isMRequired() {
    return mRequired;
  }

  public void setMRequired(Boolean mRequired) {
    this.mRequired = mRequired;
  }

  public AttributeDefinition mReviewCriteria(Boolean mReviewCriteria) {
    this.mReviewCriteria = mReviewCriteria;
    return this;
  }

   /**
   * Get mReviewCriteria
   * @return mReviewCriteria
  **/
  @ApiModelProperty(value = "")
  public Boolean isMReviewCriteria() {
    return mReviewCriteria;
  }

  public void setMReviewCriteria(Boolean mReviewCriteria) {
    this.mReviewCriteria = mReviewCriteria;
  }

  public AttributeDefinition mReviewable(Boolean mReviewable) {
    this.mReviewable = mReviewable;
    return this;
  }

   /**
   * Get mReviewable
   * @return mReviewable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMReviewable() {
    return mReviewable;
  }

  public void setMReviewable(Boolean mReviewable) {
    this.mReviewable = mReviewable;
  }

  public AttributeDefinition mSearchable(Boolean mSearchable) {
    this.mSearchable = mSearchable;
    return this;
  }

   /**
   * Get mSearchable
   * @return mSearchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMSearchable() {
    return mSearchable;
  }

  public void setMSearchable(Boolean mSearchable) {
    this.mSearchable = mSearchable;
  }

  public AttributeDefinition mSortable(Boolean mSortable) {
    this.mSortable = mSortable;
    return this;
  }

   /**
   * Get mSortable
   * @return mSortable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMSortable() {
    return mSortable;
  }

  public void setMSortable(Boolean mSortable) {
    this.mSortable = mSortable;
  }

  public AttributeDefinition mTargetEntity(String mTargetEntity) {
    this.mTargetEntity = mTargetEntity;
    return this;
  }

   /**
   * Get mTargetEntity
   * @return mTargetEntity
  **/
  @ApiModelProperty(value = "")
  public String getMTargetEntity() {
    return mTargetEntity;
  }

  public void setMTargetEntity(String mTargetEntity) {
    this.mTargetEntity = mTargetEntity;
  }

  public AttributeDefinition mTruncatable(Boolean mTruncatable) {
    this.mTruncatable = mTruncatable;
    return this;
  }

   /**
   * Get mTruncatable
   * @return mTruncatable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMTruncatable() {
    return mTruncatable;
  }

  public void setMTruncatable(Boolean mTruncatable) {
    this.mTruncatable = mTruncatable;
  }

  public AttributeDefinition mTypeAheadSearchable(Boolean mTypeAheadSearchable) {
    this.mTypeAheadSearchable = mTypeAheadSearchable;
    return this;
  }

   /**
   * Get mTypeAheadSearchable
   * @return mTypeAheadSearchable
  **/
  @ApiModelProperty(value = "")
  public Boolean isMTypeAheadSearchable() {
    return mTypeAheadSearchable;
  }

  public void setMTypeAheadSearchable(Boolean mTypeAheadSearchable) {
    this.mTypeAheadSearchable = mTypeAheadSearchable;
  }

  public AttributeDefinition mUsed(Boolean mUsed) {
    this.mUsed = mUsed;
    return this;
  }

   /**
   * Get mUsed
   * @return mUsed
  **/
  @ApiModelProperty(value = "")
  public Boolean isMUsed() {
    return mUsed;
  }

  public void setMUsed(Boolean mUsed) {
    this.mUsed = mUsed;
  }

  public AttributeDefinition multiValued(Boolean multiValued) {
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

  public AttributeDefinition quickInfo(Boolean quickInfo) {
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

  public AttributeDefinition quickSearchable(Boolean quickSearchable) {
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

  public AttributeDefinition readOnly(Boolean readOnly) {
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

  public AttributeDefinition required(Boolean required) {
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

  public AttributeDefinition reviewCriteria(Boolean reviewCriteria) {
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

  public AttributeDefinition reviewable(Boolean reviewable) {
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

  public AttributeDefinition searchable(Boolean searchable) {
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

  public AttributeDefinition sortable(Boolean sortable) {
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

  public AttributeDefinition targetEntity(String targetEntity) {
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

  public AttributeDefinition truncatable(Boolean truncatable) {
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

  public AttributeDefinition typeAheadSearchable(Boolean typeAheadSearchable) {
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

  public AttributeDefinition used(Boolean used) {
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
    AttributeDefinition attributeDefinition = (AttributeDefinition) o;
    return Objects.equals(this.advancedSearchable, attributeDefinition.advancedSearchable) &&
        Objects.equals(this.allowStaticMapping, attributeDefinition.allowStaticMapping) &&
        Objects.equals(this.attributeDataType, attributeDefinition.attributeDataType) &&
        Objects.equals(this.attributeDataTypeStr, attributeDefinition.attributeDataTypeStr) &&
        Objects.equals(this.attributeKey, attributeDefinition.attributeKey) &&
        Objects.equals(this.attributeState, attributeDefinition.attributeState) &&
        Objects.equals(this.attributeType, attributeDefinition.attributeType) &&
        Objects.equals(this.businessRoleCriteria, attributeDefinition.businessRoleCriteria) &&
        Objects.equals(this.clobAttr, attributeDefinition.clobAttr) &&
        Objects.equals(this.composition, attributeDefinition.composition) &&
        Objects.equals(this.curatable, attributeDefinition.curatable) &&
        Objects.equals(this.deleted, attributeDefinition.deleted) &&
        Objects.equals(this.displayName, attributeDefinition.displayName) &&
        Objects.equals(this.entityType, attributeDefinition.entityType) &&
        Objects.equals(this.entityTypeStr, attributeDefinition.entityTypeStr) &&
        Objects.equals(this.enumType, attributeDefinition.enumType) &&
        Objects.equals(this.excludedFromCuration, attributeDefinition.excludedFromCuration) &&
        Objects.equals(this.excludedFromQuickSearch, attributeDefinition.excludedFromQuickSearch) &&
        Objects.equals(this.excludedFromReview, attributeDefinition.excludedFromReview) &&
        Objects.equals(this.excludedFromSearch, attributeDefinition.excludedFromSearch) &&
        Objects.equals(this.excludedFromSorting, attributeDefinition.excludedFromSorting) &&
        Objects.equals(this.extended, attributeDefinition.extended) &&
        Objects.equals(this.id, attributeDefinition.id) &&
        Objects.equals(this.idmOnly, attributeDefinition.idmOnly) &&
        Objects.equals(this.joinable, attributeDefinition.joinable) &&
        Objects.equals(this.jsonDataKey, attributeDefinition.jsonDataKey) &&
        Objects.equals(this.length, attributeDefinition.length) &&
        Objects.equals(this.listable, attributeDefinition.listable) &&
        Objects.equals(this.localizedDisplayName, attributeDefinition.localizedDisplayName) &&
        Objects.equals(this.mAdvancedSearchable, attributeDefinition.mAdvancedSearchable) &&
        Objects.equals(this.mAllowStaticMapping, attributeDefinition.mAllowStaticMapping) &&
        Objects.equals(this.mAttributeDataType, attributeDefinition.mAttributeDataType) &&
        Objects.equals(this.mAttributeKey, attributeDefinition.mAttributeKey) &&
        Objects.equals(this.mAttributeState, attributeDefinition.mAttributeState) &&
        Objects.equals(this.mAttributeType, attributeDefinition.mAttributeType) &&
        Objects.equals(this.mBusinessRoleCriteria, attributeDefinition.mBusinessRoleCriteria) &&
        Objects.equals(this.mClobAttr, attributeDefinition.mClobAttr) &&
        Objects.equals(this.mComposition, attributeDefinition.mComposition) &&
        Objects.equals(this.mCuratable, attributeDefinition.mCuratable) &&
        Objects.equals(this.mEntityType, attributeDefinition.mEntityType) &&
        Objects.equals(this.mEnumType, attributeDefinition.mEnumType) &&
        Objects.equals(this.mExcludedFromCuration, attributeDefinition.mExcludedFromCuration) &&
        Objects.equals(this.mExcludedFromQuickSearch, attributeDefinition.mExcludedFromQuickSearch) &&
        Objects.equals(this.mExcludedFromReview, attributeDefinition.mExcludedFromReview) &&
        Objects.equals(this.mExcludedFromSearch, attributeDefinition.mExcludedFromSearch) &&
        Objects.equals(this.mExcludedFromSorting, attributeDefinition.mExcludedFromSorting) &&
        Objects.equals(this.mExtended, attributeDefinition.mExtended) &&
        Objects.equals(this.mFetchedLocalizedDisplayName, attributeDefinition.mFetchedLocalizedDisplayName) &&
        Objects.equals(this.mId, attributeDefinition.mId) &&
        Objects.equals(this.mIdmOnly, attributeDefinition.mIdmOnly) &&
        Objects.equals(this.mJoinable, attributeDefinition.mJoinable) &&
        Objects.equals(this.mLength, attributeDefinition.mLength) &&
        Objects.equals(this.mListable, attributeDefinition.mListable) &&
        Objects.equals(this.mLocalizedDisplayName, attributeDefinition.mLocalizedDisplayName) &&
        Objects.equals(this.mMultiValued, attributeDefinition.mMultiValued) &&
        Objects.equals(this.mQuickInfo, attributeDefinition.mQuickInfo) &&
        Objects.equals(this.mQuickSearchable, attributeDefinition.mQuickSearchable) &&
        Objects.equals(this.mReadOnly, attributeDefinition.mReadOnly) &&
        Objects.equals(this.mRequired, attributeDefinition.mRequired) &&
        Objects.equals(this.mReviewCriteria, attributeDefinition.mReviewCriteria) &&
        Objects.equals(this.mReviewable, attributeDefinition.mReviewable) &&
        Objects.equals(this.mSearchable, attributeDefinition.mSearchable) &&
        Objects.equals(this.mSortable, attributeDefinition.mSortable) &&
        Objects.equals(this.mTargetEntity, attributeDefinition.mTargetEntity) &&
        Objects.equals(this.mTruncatable, attributeDefinition.mTruncatable) &&
        Objects.equals(this.mTypeAheadSearchable, attributeDefinition.mTypeAheadSearchable) &&
        Objects.equals(this.mUsed, attributeDefinition.mUsed) &&
        Objects.equals(this.multiValued, attributeDefinition.multiValued) &&
        Objects.equals(this.quickInfo, attributeDefinition.quickInfo) &&
        Objects.equals(this.quickSearchable, attributeDefinition.quickSearchable) &&
        Objects.equals(this.readOnly, attributeDefinition.readOnly) &&
        Objects.equals(this.required, attributeDefinition.required) &&
        Objects.equals(this.reviewCriteria, attributeDefinition.reviewCriteria) &&
        Objects.equals(this.reviewable, attributeDefinition.reviewable) &&
        Objects.equals(this.searchable, attributeDefinition.searchable) &&
        Objects.equals(this.sortable, attributeDefinition.sortable) &&
        Objects.equals(this.targetEntity, attributeDefinition.targetEntity) &&
        Objects.equals(this.truncatable, attributeDefinition.truncatable) &&
        Objects.equals(this.typeAheadSearchable, attributeDefinition.typeAheadSearchable) &&
        Objects.equals(this.used, attributeDefinition.used);
  }

  @Override
  public int hashCode() {
    return Objects.hash(advancedSearchable, allowStaticMapping, attributeDataType, attributeDataTypeStr, attributeKey, attributeState, attributeType, businessRoleCriteria, clobAttr, composition, curatable, deleted, displayName, entityType, entityTypeStr, enumType, excludedFromCuration, excludedFromQuickSearch, excludedFromReview, excludedFromSearch, excludedFromSorting, extended, id, idmOnly, joinable, jsonDataKey, length, listable, localizedDisplayName, mAdvancedSearchable, mAllowStaticMapping, mAttributeDataType, mAttributeKey, mAttributeState, mAttributeType, mBusinessRoleCriteria, mClobAttr, mComposition, mCuratable, mEntityType, mEnumType, mExcludedFromCuration, mExcludedFromQuickSearch, mExcludedFromReview, mExcludedFromSearch, mExcludedFromSorting, mExtended, mFetchedLocalizedDisplayName, mId, mIdmOnly, mJoinable, mLength, mListable, mLocalizedDisplayName, mMultiValued, mQuickInfo, mQuickSearchable, mReadOnly, mRequired, mReviewCriteria, mReviewable, mSearchable, mSortable, mTargetEntity, mTruncatable, mTypeAheadSearchable, mUsed, multiValued, quickInfo, quickSearchable, readOnly, required, reviewCriteria, reviewable, searchable, sortable, targetEntity, truncatable, typeAheadSearchable, used);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeDefinition {\n");
    
    sb.append("    advancedSearchable: ").append(toIndentedString(advancedSearchable)).append("\n");
    sb.append("    allowStaticMapping: ").append(toIndentedString(allowStaticMapping)).append("\n");
    sb.append("    attributeDataType: ").append(toIndentedString(attributeDataType)).append("\n");
    sb.append("    attributeDataTypeStr: ").append(toIndentedString(attributeDataTypeStr)).append("\n");
    sb.append("    attributeKey: ").append(toIndentedString(attributeKey)).append("\n");
    sb.append("    attributeState: ").append(toIndentedString(attributeState)).append("\n");
    sb.append("    attributeType: ").append(toIndentedString(attributeType)).append("\n");
    sb.append("    businessRoleCriteria: ").append(toIndentedString(businessRoleCriteria)).append("\n");
    sb.append("    clobAttr: ").append(toIndentedString(clobAttr)).append("\n");
    sb.append("    composition: ").append(toIndentedString(composition)).append("\n");
    sb.append("    curatable: ").append(toIndentedString(curatable)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    entityTypeStr: ").append(toIndentedString(entityTypeStr)).append("\n");
    sb.append("    enumType: ").append(toIndentedString(enumType)).append("\n");
    sb.append("    excludedFromCuration: ").append(toIndentedString(excludedFromCuration)).append("\n");
    sb.append("    excludedFromQuickSearch: ").append(toIndentedString(excludedFromQuickSearch)).append("\n");
    sb.append("    excludedFromReview: ").append(toIndentedString(excludedFromReview)).append("\n");
    sb.append("    excludedFromSearch: ").append(toIndentedString(excludedFromSearch)).append("\n");
    sb.append("    excludedFromSorting: ").append(toIndentedString(excludedFromSorting)).append("\n");
    sb.append("    extended: ").append(toIndentedString(extended)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idmOnly: ").append(toIndentedString(idmOnly)).append("\n");
    sb.append("    joinable: ").append(toIndentedString(joinable)).append("\n");
    sb.append("    jsonDataKey: ").append(toIndentedString(jsonDataKey)).append("\n");
    sb.append("    length: ").append(toIndentedString(length)).append("\n");
    sb.append("    listable: ").append(toIndentedString(listable)).append("\n");
    sb.append("    localizedDisplayName: ").append(toIndentedString(localizedDisplayName)).append("\n");
    sb.append("    mAdvancedSearchable: ").append(toIndentedString(mAdvancedSearchable)).append("\n");
    sb.append("    mAllowStaticMapping: ").append(toIndentedString(mAllowStaticMapping)).append("\n");
    sb.append("    mAttributeDataType: ").append(toIndentedString(mAttributeDataType)).append("\n");
    sb.append("    mAttributeKey: ").append(toIndentedString(mAttributeKey)).append("\n");
    sb.append("    mAttributeState: ").append(toIndentedString(mAttributeState)).append("\n");
    sb.append("    mAttributeType: ").append(toIndentedString(mAttributeType)).append("\n");
    sb.append("    mBusinessRoleCriteria: ").append(toIndentedString(mBusinessRoleCriteria)).append("\n");
    sb.append("    mClobAttr: ").append(toIndentedString(mClobAttr)).append("\n");
    sb.append("    mComposition: ").append(toIndentedString(mComposition)).append("\n");
    sb.append("    mCuratable: ").append(toIndentedString(mCuratable)).append("\n");
    sb.append("    mEntityType: ").append(toIndentedString(mEntityType)).append("\n");
    sb.append("    mEnumType: ").append(toIndentedString(mEnumType)).append("\n");
    sb.append("    mExcludedFromCuration: ").append(toIndentedString(mExcludedFromCuration)).append("\n");
    sb.append("    mExcludedFromQuickSearch: ").append(toIndentedString(mExcludedFromQuickSearch)).append("\n");
    sb.append("    mExcludedFromReview: ").append(toIndentedString(mExcludedFromReview)).append("\n");
    sb.append("    mExcludedFromSearch: ").append(toIndentedString(mExcludedFromSearch)).append("\n");
    sb.append("    mExcludedFromSorting: ").append(toIndentedString(mExcludedFromSorting)).append("\n");
    sb.append("    mExtended: ").append(toIndentedString(mExtended)).append("\n");
    sb.append("    mFetchedLocalizedDisplayName: ").append(toIndentedString(mFetchedLocalizedDisplayName)).append("\n");
    sb.append("    mId: ").append(toIndentedString(mId)).append("\n");
    sb.append("    mIdmOnly: ").append(toIndentedString(mIdmOnly)).append("\n");
    sb.append("    mJoinable: ").append(toIndentedString(mJoinable)).append("\n");
    sb.append("    mLength: ").append(toIndentedString(mLength)).append("\n");
    sb.append("    mListable: ").append(toIndentedString(mListable)).append("\n");
    sb.append("    mLocalizedDisplayName: ").append(toIndentedString(mLocalizedDisplayName)).append("\n");
    sb.append("    mMultiValued: ").append(toIndentedString(mMultiValued)).append("\n");
    sb.append("    mQuickInfo: ").append(toIndentedString(mQuickInfo)).append("\n");
    sb.append("    mQuickSearchable: ").append(toIndentedString(mQuickSearchable)).append("\n");
    sb.append("    mReadOnly: ").append(toIndentedString(mReadOnly)).append("\n");
    sb.append("    mRequired: ").append(toIndentedString(mRequired)).append("\n");
    sb.append("    mReviewCriteria: ").append(toIndentedString(mReviewCriteria)).append("\n");
    sb.append("    mReviewable: ").append(toIndentedString(mReviewable)).append("\n");
    sb.append("    mSearchable: ").append(toIndentedString(mSearchable)).append("\n");
    sb.append("    mSortable: ").append(toIndentedString(mSortable)).append("\n");
    sb.append("    mTargetEntity: ").append(toIndentedString(mTargetEntity)).append("\n");
    sb.append("    mTruncatable: ").append(toIndentedString(mTruncatable)).append("\n");
    sb.append("    mTypeAheadSearchable: ").append(toIndentedString(mTypeAheadSearchable)).append("\n");
    sb.append("    mUsed: ").append(toIndentedString(mUsed)).append("\n");
    sb.append("    multiValued: ").append(toIndentedString(multiValued)).append("\n");
    sb.append("    quickInfo: ").append(toIndentedString(quickInfo)).append("\n");
    sb.append("    quickSearchable: ").append(toIndentedString(quickSearchable)).append("\n");
    sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    reviewCriteria: ").append(toIndentedString(reviewCriteria)).append("\n");
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
