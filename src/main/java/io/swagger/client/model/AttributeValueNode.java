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
import io.swagger.client.model.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AttributeValueNode
 */



public class AttributeValueNode {
  @SerializedName("attributeDataType")
  private String attributeDataType = null;

  @SerializedName("attributeDisplayValue")
  private Entity attributeDisplayValue = null;

  @SerializedName("attributeDisplayValues")
  private List<Entity> attributeDisplayValues = null;

  @SerializedName("attributeFulfillmentCodegen")
  private String attributeFulfillmentCodegen = null;

  @SerializedName("attributeFulfillmentCodegenCommentSuffix")
  private String attributeFulfillmentCodegenCommentSuffix = null;

  @SerializedName("attributeId")
  private Long attributeId = null;

  @SerializedName("attributeKey")
  private String attributeKey = null;

  @SerializedName("attributeType")
  private String attributeType = null;

  @SerializedName("attributeValue")
  private String attributeValue = null;

  @SerializedName("attributeValues")
  private List<String> attributeValues = null;

  /**
   * Gets or Sets codegenStrategy
   */
  @JsonAdapter(CodegenStrategyEnum.Adapter.class)
  public enum CodegenStrategyEnum {
    @SerializedName("NONE")
    NONE("NONE"),
    @SerializedName("LOCALIZED_GETTER")
    LOCALIZED_GETTER("LOCALIZED_GETTER"),
    @SerializedName("FULFILLMENT_CONTEXT")
    FULFILLMENT_CONTEXT("FULFILLMENT_CONTEXT");

    private String value;

    CodegenStrategyEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static CodegenStrategyEnum fromValue(String input) {
      for (CodegenStrategyEnum b : CodegenStrategyEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<CodegenStrategyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CodegenStrategyEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public CodegenStrategyEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return CodegenStrategyEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("codegenStrategy")
  private CodegenStrategyEnum codegenStrategy = null;

  /**
   * Gets or Sets curationType
   */
  @JsonAdapter(CurationTypeEnum.Adapter.class)
  public enum CurationTypeEnum {
    @SerializedName("UPDATE")
    UPDATE("UPDATE"),
    @SerializedName("SET_NULL")
    SET_NULL("SET_NULL"),
    @SerializedName("UNDO_CURATION")
    UNDO_CURATION("UNDO_CURATION");

    private String value;

    CurationTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static CurationTypeEnum fromValue(String input) {
      for (CurationTypeEnum b : CurationTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<CurationTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CurationTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public CurationTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return CurationTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("curationType")
  private CurationTypeEnum curationType = null;

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

  @SerializedName("enumType")
  private String enumType = null;

  @SerializedName("fulfillmentContextKey")
  private String fulfillmentContextKey = null;

  public AttributeValueNode attributeDataType(String attributeDataType) {
    this.attributeDataType = attributeDataType;
    return this;
  }

   /**
   * Get attributeDataType
   * @return attributeDataType
  **/
  @ApiModelProperty(value = "")
  public String getAttributeDataType() {
    return attributeDataType;
  }

  public void setAttributeDataType(String attributeDataType) {
    this.attributeDataType = attributeDataType;
  }

  public AttributeValueNode attributeDisplayValue(Entity attributeDisplayValue) {
    this.attributeDisplayValue = attributeDisplayValue;
    return this;
  }

   /**
   * Get attributeDisplayValue
   * @return attributeDisplayValue
  **/
  @ApiModelProperty(value = "")
  public Entity getAttributeDisplayValue() {
    return attributeDisplayValue;
  }

  public void setAttributeDisplayValue(Entity attributeDisplayValue) {
    this.attributeDisplayValue = attributeDisplayValue;
  }

  public AttributeValueNode attributeDisplayValues(List<Entity> attributeDisplayValues) {
    this.attributeDisplayValues = attributeDisplayValues;
    return this;
  }

  public AttributeValueNode addAttributeDisplayValuesItem(Entity attributeDisplayValuesItem) {
    if (this.attributeDisplayValues == null) {
      this.attributeDisplayValues = new ArrayList<Entity>();
    }
    this.attributeDisplayValues.add(attributeDisplayValuesItem);
    return this;
  }

   /**
   * Get attributeDisplayValues
   * @return attributeDisplayValues
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getAttributeDisplayValues() {
    return attributeDisplayValues;
  }

  public void setAttributeDisplayValues(List<Entity> attributeDisplayValues) {
    this.attributeDisplayValues = attributeDisplayValues;
  }

  public AttributeValueNode attributeFulfillmentCodegen(String attributeFulfillmentCodegen) {
    this.attributeFulfillmentCodegen = attributeFulfillmentCodegen;
    return this;
  }

   /**
   * Get attributeFulfillmentCodegen
   * @return attributeFulfillmentCodegen
  **/
  @ApiModelProperty(value = "")
  public String getAttributeFulfillmentCodegen() {
    return attributeFulfillmentCodegen;
  }

  public void setAttributeFulfillmentCodegen(String attributeFulfillmentCodegen) {
    this.attributeFulfillmentCodegen = attributeFulfillmentCodegen;
  }

  public AttributeValueNode attributeFulfillmentCodegenCommentSuffix(String attributeFulfillmentCodegenCommentSuffix) {
    this.attributeFulfillmentCodegenCommentSuffix = attributeFulfillmentCodegenCommentSuffix;
    return this;
  }

   /**
   * Get attributeFulfillmentCodegenCommentSuffix
   * @return attributeFulfillmentCodegenCommentSuffix
  **/
  @ApiModelProperty(value = "")
  public String getAttributeFulfillmentCodegenCommentSuffix() {
    return attributeFulfillmentCodegenCommentSuffix;
  }

  public void setAttributeFulfillmentCodegenCommentSuffix(String attributeFulfillmentCodegenCommentSuffix) {
    this.attributeFulfillmentCodegenCommentSuffix = attributeFulfillmentCodegenCommentSuffix;
  }

  public AttributeValueNode attributeId(Long attributeId) {
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

  public AttributeValueNode attributeKey(String attributeKey) {
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

  public AttributeValueNode attributeType(String attributeType) {
    this.attributeType = attributeType;
    return this;
  }

   /**
   * Get attributeType
   * @return attributeType
  **/
  @ApiModelProperty(value = "")
  public String getAttributeType() {
    return attributeType;
  }

  public void setAttributeType(String attributeType) {
    this.attributeType = attributeType;
  }

  public AttributeValueNode attributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
    return this;
  }

   /**
   * Get attributeValue
   * @return attributeValue
  **/
  @ApiModelProperty(value = "")
  public String getAttributeValue() {
    return attributeValue;
  }

  public void setAttributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
  }

  public AttributeValueNode attributeValues(List<String> attributeValues) {
    this.attributeValues = attributeValues;
    return this;
  }

  public AttributeValueNode addAttributeValuesItem(String attributeValuesItem) {
    if (this.attributeValues == null) {
      this.attributeValues = new ArrayList<String>();
    }
    this.attributeValues.add(attributeValuesItem);
    return this;
  }

   /**
   * Get attributeValues
   * @return attributeValues
  **/
  @ApiModelProperty(value = "")
  public List<String> getAttributeValues() {
    return attributeValues;
  }

  public void setAttributeValues(List<String> attributeValues) {
    this.attributeValues = attributeValues;
  }

  public AttributeValueNode codegenStrategy(CodegenStrategyEnum codegenStrategy) {
    this.codegenStrategy = codegenStrategy;
    return this;
  }

   /**
   * Get codegenStrategy
   * @return codegenStrategy
  **/
  @ApiModelProperty(value = "")
  public CodegenStrategyEnum getCodegenStrategy() {
    return codegenStrategy;
  }

  public void setCodegenStrategy(CodegenStrategyEnum codegenStrategy) {
    this.codegenStrategy = codegenStrategy;
  }

  public AttributeValueNode curationType(CurationTypeEnum curationType) {
    this.curationType = curationType;
    return this;
  }

   /**
   * Get curationType
   * @return curationType
  **/
  @ApiModelProperty(value = "")
  public CurationTypeEnum getCurationType() {
    return curationType;
  }

  public void setCurationType(CurationTypeEnum curationType) {
    this.curationType = curationType;
  }

  public AttributeValueNode entityType(EntityTypeEnum entityType) {
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

  public AttributeValueNode enumType(String enumType) {
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

  public AttributeValueNode fulfillmentContextKey(String fulfillmentContextKey) {
    this.fulfillmentContextKey = fulfillmentContextKey;
    return this;
  }

   /**
   * Get fulfillmentContextKey
   * @return fulfillmentContextKey
  **/
  @ApiModelProperty(value = "")
  public String getFulfillmentContextKey() {
    return fulfillmentContextKey;
  }

  public void setFulfillmentContextKey(String fulfillmentContextKey) {
    this.fulfillmentContextKey = fulfillmentContextKey;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributeValueNode attributeValueNode = (AttributeValueNode) o;
    return Objects.equals(this.attributeDataType, attributeValueNode.attributeDataType) &&
        Objects.equals(this.attributeDisplayValue, attributeValueNode.attributeDisplayValue) &&
        Objects.equals(this.attributeDisplayValues, attributeValueNode.attributeDisplayValues) &&
        Objects.equals(this.attributeFulfillmentCodegen, attributeValueNode.attributeFulfillmentCodegen) &&
        Objects.equals(this.attributeFulfillmentCodegenCommentSuffix, attributeValueNode.attributeFulfillmentCodegenCommentSuffix) &&
        Objects.equals(this.attributeId, attributeValueNode.attributeId) &&
        Objects.equals(this.attributeKey, attributeValueNode.attributeKey) &&
        Objects.equals(this.attributeType, attributeValueNode.attributeType) &&
        Objects.equals(this.attributeValue, attributeValueNode.attributeValue) &&
        Objects.equals(this.attributeValues, attributeValueNode.attributeValues) &&
        Objects.equals(this.codegenStrategy, attributeValueNode.codegenStrategy) &&
        Objects.equals(this.curationType, attributeValueNode.curationType) &&
        Objects.equals(this.entityType, attributeValueNode.entityType) &&
        Objects.equals(this.enumType, attributeValueNode.enumType) &&
        Objects.equals(this.fulfillmentContextKey, attributeValueNode.fulfillmentContextKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeDataType, attributeDisplayValue, attributeDisplayValues, attributeFulfillmentCodegen, attributeFulfillmentCodegenCommentSuffix, attributeId, attributeKey, attributeType, attributeValue, attributeValues, codegenStrategy, curationType, entityType, enumType, fulfillmentContextKey);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeValueNode {\n");
    
    sb.append("    attributeDataType: ").append(toIndentedString(attributeDataType)).append("\n");
    sb.append("    attributeDisplayValue: ").append(toIndentedString(attributeDisplayValue)).append("\n");
    sb.append("    attributeDisplayValues: ").append(toIndentedString(attributeDisplayValues)).append("\n");
    sb.append("    attributeFulfillmentCodegen: ").append(toIndentedString(attributeFulfillmentCodegen)).append("\n");
    sb.append("    attributeFulfillmentCodegenCommentSuffix: ").append(toIndentedString(attributeFulfillmentCodegenCommentSuffix)).append("\n");
    sb.append("    attributeId: ").append(toIndentedString(attributeId)).append("\n");
    sb.append("    attributeKey: ").append(toIndentedString(attributeKey)).append("\n");
    sb.append("    attributeType: ").append(toIndentedString(attributeType)).append("\n");
    sb.append("    attributeValue: ").append(toIndentedString(attributeValue)).append("\n");
    sb.append("    attributeValues: ").append(toIndentedString(attributeValues)).append("\n");
    sb.append("    codegenStrategy: ").append(toIndentedString(codegenStrategy)).append("\n");
    sb.append("    curationType: ").append(toIndentedString(curationType)).append("\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    enumType: ").append(toIndentedString(enumType)).append("\n");
    sb.append("    fulfillmentContextKey: ").append(toIndentedString(fulfillmentContextKey)).append("\n");
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
