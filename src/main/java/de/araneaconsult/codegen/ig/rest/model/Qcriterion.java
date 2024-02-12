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
import de.araneaconsult.codegen.ig.rest.model.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Qcriterion
 */



public class Qcriterion {
  @SerializedName("attributeEntity")
  private String attributeEntity = null;

  @SerializedName("attributeKey")
  private String attributeKey = null;

  @SerializedName("bvalue")
  private Boolean bvalue = null;

  @SerializedName("daysAgo")
  private Long daysAgo = null;

  @SerializedName("daysAhead")
  private Long daysAhead = null;

  @SerializedName("dvalue")
  private Long dvalue = null;

  @SerializedName("entities")
  private List<Entity> entities = null;

  @SerializedName("fvalue")
  private Double fvalue = null;

  /**
   * Gets or Sets kind
   */
  @JsonAdapter(KindEnum.Adapter.class)
  public enum KindEnum {
    @SerializedName("Group")
    GROUP("Group"),
    @SerializedName("ReportingUpTo")
    REPORTINGUPTO("ReportingUpTo"),
    @SerializedName("BusinessRole")
    BUSINESSROLE("BusinessRole"),
    @SerializedName("Classification")
    CLASSIFICATION("Classification"),
    @SerializedName("LongCompare")
    LONGCOMPARE("LongCompare"),
    @SerializedName("DoubleCompare")
    DOUBLECOMPARE("DoubleCompare"),
    @SerializedName("DateCompare")
    DATECOMPARE("DateCompare"),
    @SerializedName("BooleanCompare")
    BOOLEANCOMPARE("BooleanCompare"),
    @SerializedName("DaysAgoCompare")
    DAYSAGOCOMPARE("DaysAgoCompare"),
    @SerializedName("DaysAheadCompare")
    DAYSAHEADCOMPARE("DaysAheadCompare");

    private String value;

    KindEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static KindEnum fromValue(String input) {
      for (KindEnum b : KindEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<KindEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final KindEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public KindEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return KindEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("kind")
  private KindEnum kind = null;

  @SerializedName("m_attributeEntity")
  private String mAttributeEntity = null;

  @SerializedName("m_attributeKey")
  private String mAttributeKey = null;

  @SerializedName("m_bvalue")
  private Boolean mBvalue = null;

  @SerializedName("m_daysAgo")
  private Long mDaysAgo = null;

  @SerializedName("m_daysAhead")
  private Long mDaysAhead = null;

  @SerializedName("m_dvalue")
  private Long mDvalue = null;

  @SerializedName("m_entities")
  private List<Entity> mEntities = null;

  @SerializedName("m_fvalue")
  private Double mFvalue = null;

  /**
   * Gets or Sets mKind
   */
  @JsonAdapter(MKindEnum.Adapter.class)
  public enum MKindEnum {
    @SerializedName("Group")
    GROUP("Group"),
    @SerializedName("ReportingUpTo")
    REPORTINGUPTO("ReportingUpTo"),
    @SerializedName("BusinessRole")
    BUSINESSROLE("BusinessRole"),
    @SerializedName("Classification")
    CLASSIFICATION("Classification"),
    @SerializedName("LongCompare")
    LONGCOMPARE("LongCompare"),
    @SerializedName("DoubleCompare")
    DOUBLECOMPARE("DoubleCompare"),
    @SerializedName("DateCompare")
    DATECOMPARE("DateCompare"),
    @SerializedName("BooleanCompare")
    BOOLEANCOMPARE("BooleanCompare"),
    @SerializedName("DaysAgoCompare")
    DAYSAGOCOMPARE("DaysAgoCompare"),
    @SerializedName("DaysAheadCompare")
    DAYSAHEADCOMPARE("DaysAheadCompare");

    private String value;

    MKindEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MKindEnum fromValue(String input) {
      for (MKindEnum b : MKindEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MKindEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MKindEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MKindEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MKindEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_kind")
  private MKindEnum mKind = null;

  @SerializedName("m_nvalue")
  private Long mNvalue = null;

  /**
   * Gets or Sets mOperator
   */
  @JsonAdapter(MOperatorEnum.Adapter.class)
  public enum MOperatorEnum {
    @SerializedName("LT")
    LT("LT"),
    @SerializedName("LE")
    LE("LE"),
    @SerializedName("EQ")
    EQ("EQ"),
    @SerializedName("GE")
    GE("GE"),
    @SerializedName("GT")
    GT("GT"),
    @SerializedName("NOT_IN")
    NOT_IN("NOT_IN"),
    @SerializedName("IS_EMPTY")
    IS_EMPTY("IS_EMPTY");

    private String value;

    MOperatorEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MOperatorEnum fromValue(String input) {
      for (MOperatorEnum b : MOperatorEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MOperatorEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MOperatorEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MOperatorEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MOperatorEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_operator")
  private MOperatorEnum mOperator = null;

  @SerializedName("m_values")
  private List<String> mValues = null;

  @SerializedName("nvalue")
  private Long nvalue = null;

  /**
   * Gets or Sets operator
   */
  @JsonAdapter(OperatorEnum.Adapter.class)
  public enum OperatorEnum {
    @SerializedName("LT")
    LT("LT"),
    @SerializedName("LE")
    LE("LE"),
    @SerializedName("EQ")
    EQ("EQ"),
    @SerializedName("GE")
    GE("GE"),
    @SerializedName("GT")
    GT("GT"),
    @SerializedName("NOT_IN")
    NOT_IN("NOT_IN"),
    @SerializedName("IS_EMPTY")
    IS_EMPTY("IS_EMPTY");

    private String value;

    OperatorEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static OperatorEnum fromValue(String input) {
      for (OperatorEnum b : OperatorEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<OperatorEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final OperatorEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public OperatorEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return OperatorEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("operator")
  private OperatorEnum operator = null;

  @SerializedName("values")
  private List<String> values = null;

  public Qcriterion attributeEntity(String attributeEntity) {
    this.attributeEntity = attributeEntity;
    return this;
  }

   /**
   * entity key
   * @return attributeEntity
  **/
  @ApiModelProperty(value = "entity key")
  public String getAttributeEntity() {
    return attributeEntity;
  }

  public void setAttributeEntity(String attributeEntity) {
    this.attributeEntity = attributeEntity;
  }

  public Qcriterion attributeKey(String attributeKey) {
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

  public Qcriterion bvalue(Boolean bvalue) {
    this.bvalue = bvalue;
    return this;
  }

   /**
   * Get bvalue
   * @return bvalue
  **/
  @ApiModelProperty(value = "")
  public Boolean isBvalue() {
    return bvalue;
  }

  public void setBvalue(Boolean bvalue) {
    this.bvalue = bvalue;
  }

  public Qcriterion daysAgo(Long daysAgo) {
    this.daysAgo = daysAgo;
    return this;
  }

   /**
   * Get daysAgo
   * @return daysAgo
  **/
  @ApiModelProperty(value = "")
  public Long getDaysAgo() {
    return daysAgo;
  }

  public void setDaysAgo(Long daysAgo) {
    this.daysAgo = daysAgo;
  }

  public Qcriterion daysAhead(Long daysAhead) {
    this.daysAhead = daysAhead;
    return this;
  }

   /**
   * Get daysAhead
   * @return daysAhead
  **/
  @ApiModelProperty(value = "")
  public Long getDaysAhead() {
    return daysAhead;
  }

  public void setDaysAhead(Long daysAhead) {
    this.daysAhead = daysAhead;
  }

  public Qcriterion dvalue(Long dvalue) {
    this.dvalue = dvalue;
    return this;
  }

   /**
   * Get dvalue
   * @return dvalue
  **/
  @ApiModelProperty(value = "")
  public Long getDvalue() {
    return dvalue;
  }

  public void setDvalue(Long dvalue) {
    this.dvalue = dvalue;
  }

  public Qcriterion entities(List<Entity> entities) {
    this.entities = entities;
    return this;
  }

  public Qcriterion addEntitiesItem(Entity entitiesItem) {
    if (this.entities == null) {
      this.entities = new ArrayList<Entity>();
    }
    this.entities.add(entitiesItem);
    return this;
  }

   /**
   * Get entities
   * @return entities
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getEntities() {
    return entities;
  }

  public void setEntities(List<Entity> entities) {
    this.entities = entities;
  }

  public Qcriterion fvalue(Double fvalue) {
    this.fvalue = fvalue;
    return this;
  }

   /**
   * Get fvalue
   * @return fvalue
  **/
  @ApiModelProperty(value = "")
  public Double getFvalue() {
    return fvalue;
  }

  public void setFvalue(Double fvalue) {
    this.fvalue = fvalue;
  }

  public Qcriterion kind(KindEnum kind) {
    this.kind = kind;
    return this;
  }

   /**
   * Get kind
   * @return kind
  **/
  @ApiModelProperty(value = "")
  public KindEnum getKind() {
    return kind;
  }

  public void setKind(KindEnum kind) {
    this.kind = kind;
  }

  public Qcriterion mAttributeEntity(String mAttributeEntity) {
    this.mAttributeEntity = mAttributeEntity;
    return this;
  }

   /**
   * Get mAttributeEntity
   * @return mAttributeEntity
  **/
  @ApiModelProperty(value = "")
  public String getMAttributeEntity() {
    return mAttributeEntity;
  }

  public void setMAttributeEntity(String mAttributeEntity) {
    this.mAttributeEntity = mAttributeEntity;
  }

  public Qcriterion mAttributeKey(String mAttributeKey) {
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

  public Qcriterion mBvalue(Boolean mBvalue) {
    this.mBvalue = mBvalue;
    return this;
  }

   /**
   * Get mBvalue
   * @return mBvalue
  **/
  @ApiModelProperty(value = "")
  public Boolean isMBvalue() {
    return mBvalue;
  }

  public void setMBvalue(Boolean mBvalue) {
    this.mBvalue = mBvalue;
  }

  public Qcriterion mDaysAgo(Long mDaysAgo) {
    this.mDaysAgo = mDaysAgo;
    return this;
  }

   /**
   * Get mDaysAgo
   * @return mDaysAgo
  **/
  @ApiModelProperty(value = "")
  public Long getMDaysAgo() {
    return mDaysAgo;
  }

  public void setMDaysAgo(Long mDaysAgo) {
    this.mDaysAgo = mDaysAgo;
  }

  public Qcriterion mDaysAhead(Long mDaysAhead) {
    this.mDaysAhead = mDaysAhead;
    return this;
  }

   /**
   * Get mDaysAhead
   * @return mDaysAhead
  **/
  @ApiModelProperty(value = "")
  public Long getMDaysAhead() {
    return mDaysAhead;
  }

  public void setMDaysAhead(Long mDaysAhead) {
    this.mDaysAhead = mDaysAhead;
  }

  public Qcriterion mDvalue(Long mDvalue) {
    this.mDvalue = mDvalue;
    return this;
  }

   /**
   * Get mDvalue
   * @return mDvalue
  **/
  @ApiModelProperty(value = "")
  public Long getMDvalue() {
    return mDvalue;
  }

  public void setMDvalue(Long mDvalue) {
    this.mDvalue = mDvalue;
  }

  public Qcriterion mEntities(List<Entity> mEntities) {
    this.mEntities = mEntities;
    return this;
  }

  public Qcriterion addMEntitiesItem(Entity mEntitiesItem) {
    if (this.mEntities == null) {
      this.mEntities = new ArrayList<Entity>();
    }
    this.mEntities.add(mEntitiesItem);
    return this;
  }

   /**
   * Get mEntities
   * @return mEntities
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMEntities() {
    return mEntities;
  }

  public void setMEntities(List<Entity> mEntities) {
    this.mEntities = mEntities;
  }

  public Qcriterion mFvalue(Double mFvalue) {
    this.mFvalue = mFvalue;
    return this;
  }

   /**
   * Get mFvalue
   * @return mFvalue
  **/
  @ApiModelProperty(value = "")
  public Double getMFvalue() {
    return mFvalue;
  }

  public void setMFvalue(Double mFvalue) {
    this.mFvalue = mFvalue;
  }

  public Qcriterion mKind(MKindEnum mKind) {
    this.mKind = mKind;
    return this;
  }

   /**
   * Get mKind
   * @return mKind
  **/
  @ApiModelProperty(value = "")
  public MKindEnum getMKind() {
    return mKind;
  }

  public void setMKind(MKindEnum mKind) {
    this.mKind = mKind;
  }

  public Qcriterion mNvalue(Long mNvalue) {
    this.mNvalue = mNvalue;
    return this;
  }

   /**
   * Get mNvalue
   * @return mNvalue
  **/
  @ApiModelProperty(value = "")
  public Long getMNvalue() {
    return mNvalue;
  }

  public void setMNvalue(Long mNvalue) {
    this.mNvalue = mNvalue;
  }

  public Qcriterion mOperator(MOperatorEnum mOperator) {
    this.mOperator = mOperator;
    return this;
  }

   /**
   * Get mOperator
   * @return mOperator
  **/
  @ApiModelProperty(value = "")
  public MOperatorEnum getMOperator() {
    return mOperator;
  }

  public void setMOperator(MOperatorEnum mOperator) {
    this.mOperator = mOperator;
  }

  public Qcriterion mValues(List<String> mValues) {
    this.mValues = mValues;
    return this;
  }

  public Qcriterion addMValuesItem(String mValuesItem) {
    if (this.mValues == null) {
      this.mValues = new ArrayList<String>();
    }
    this.mValues.add(mValuesItem);
    return this;
  }

   /**
   * Get mValues
   * @return mValues
  **/
  @ApiModelProperty(value = "")
  public List<String> getMValues() {
    return mValues;
  }

  public void setMValues(List<String> mValues) {
    this.mValues = mValues;
  }

  public Qcriterion nvalue(Long nvalue) {
    this.nvalue = nvalue;
    return this;
  }

   /**
   * Get nvalue
   * @return nvalue
  **/
  @ApiModelProperty(value = "")
  public Long getNvalue() {
    return nvalue;
  }

  public void setNvalue(Long nvalue) {
    this.nvalue = nvalue;
  }

  public Qcriterion operator(OperatorEnum operator) {
    this.operator = operator;
    return this;
  }

   /**
   * Get operator
   * @return operator
  **/
  @ApiModelProperty(value = "")
  public OperatorEnum getOperator() {
    return operator;
  }

  public void setOperator(OperatorEnum operator) {
    this.operator = operator;
  }

  public Qcriterion values(List<String> values) {
    this.values = values;
    return this;
  }

  public Qcriterion addValuesItem(String valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<String>();
    }
    this.values.add(valuesItem);
    return this;
  }

   /**
   * Get values
   * @return values
  **/
  @ApiModelProperty(value = "")
  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Qcriterion qcriterion = (Qcriterion) o;
    return Objects.equals(this.attributeEntity, qcriterion.attributeEntity) &&
        Objects.equals(this.attributeKey, qcriterion.attributeKey) &&
        Objects.equals(this.bvalue, qcriterion.bvalue) &&
        Objects.equals(this.daysAgo, qcriterion.daysAgo) &&
        Objects.equals(this.daysAhead, qcriterion.daysAhead) &&
        Objects.equals(this.dvalue, qcriterion.dvalue) &&
        Objects.equals(this.entities, qcriterion.entities) &&
        Objects.equals(this.fvalue, qcriterion.fvalue) &&
        Objects.equals(this.kind, qcriterion.kind) &&
        Objects.equals(this.mAttributeEntity, qcriterion.mAttributeEntity) &&
        Objects.equals(this.mAttributeKey, qcriterion.mAttributeKey) &&
        Objects.equals(this.mBvalue, qcriterion.mBvalue) &&
        Objects.equals(this.mDaysAgo, qcriterion.mDaysAgo) &&
        Objects.equals(this.mDaysAhead, qcriterion.mDaysAhead) &&
        Objects.equals(this.mDvalue, qcriterion.mDvalue) &&
        Objects.equals(this.mEntities, qcriterion.mEntities) &&
        Objects.equals(this.mFvalue, qcriterion.mFvalue) &&
        Objects.equals(this.mKind, qcriterion.mKind) &&
        Objects.equals(this.mNvalue, qcriterion.mNvalue) &&
        Objects.equals(this.mOperator, qcriterion.mOperator) &&
        Objects.equals(this.mValues, qcriterion.mValues) &&
        Objects.equals(this.nvalue, qcriterion.nvalue) &&
        Objects.equals(this.operator, qcriterion.operator) &&
        Objects.equals(this.values, qcriterion.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeEntity, attributeKey, bvalue, daysAgo, daysAhead, dvalue, entities, fvalue, kind, mAttributeEntity, mAttributeKey, mBvalue, mDaysAgo, mDaysAhead, mDvalue, mEntities, mFvalue, mKind, mNvalue, mOperator, mValues, nvalue, operator, values);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Qcriterion {\n");
    
    sb.append("    attributeEntity: ").append(toIndentedString(attributeEntity)).append("\n");
    sb.append("    attributeKey: ").append(toIndentedString(attributeKey)).append("\n");
    sb.append("    bvalue: ").append(toIndentedString(bvalue)).append("\n");
    sb.append("    daysAgo: ").append(toIndentedString(daysAgo)).append("\n");
    sb.append("    daysAhead: ").append(toIndentedString(daysAhead)).append("\n");
    sb.append("    dvalue: ").append(toIndentedString(dvalue)).append("\n");
    sb.append("    entities: ").append(toIndentedString(entities)).append("\n");
    sb.append("    fvalue: ").append(toIndentedString(fvalue)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    mAttributeEntity: ").append(toIndentedString(mAttributeEntity)).append("\n");
    sb.append("    mAttributeKey: ").append(toIndentedString(mAttributeKey)).append("\n");
    sb.append("    mBvalue: ").append(toIndentedString(mBvalue)).append("\n");
    sb.append("    mDaysAgo: ").append(toIndentedString(mDaysAgo)).append("\n");
    sb.append("    mDaysAhead: ").append(toIndentedString(mDaysAhead)).append("\n");
    sb.append("    mDvalue: ").append(toIndentedString(mDvalue)).append("\n");
    sb.append("    mEntities: ").append(toIndentedString(mEntities)).append("\n");
    sb.append("    mFvalue: ").append(toIndentedString(mFvalue)).append("\n");
    sb.append("    mKind: ").append(toIndentedString(mKind)).append("\n");
    sb.append("    mNvalue: ").append(toIndentedString(mNvalue)).append("\n");
    sb.append("    mOperator: ").append(toIndentedString(mOperator)).append("\n");
    sb.append("    mValues: ").append(toIndentedString(mValues)).append("\n");
    sb.append("    nvalue: ").append(toIndentedString(nvalue)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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
