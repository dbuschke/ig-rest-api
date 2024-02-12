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
import de.araneaconsult.codegen.ig.rest.model.Qcriterion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ReviewingTargets
 */



public class ReviewingTargets {
  /**
   * the acct conjunction
   */
  @JsonAdapter(AcctConjunctionEnum.Adapter.class)
  public enum AcctConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    AcctConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AcctConjunctionEnum fromValue(String input) {
      for (AcctConjunctionEnum b : AcctConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AcctConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AcctConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AcctConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AcctConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("acctConjunction")
  private AcctConjunctionEnum acctConjunction = null;

  @SerializedName("acctCriteria")
  private List<Qcriterion> acctCriteria = null;

  @SerializedName("apiVersion")
  private String apiVersion = null;

  /**
   * the app conjunction
   */
  @JsonAdapter(AppConjunctionEnum.Adapter.class)
  public enum AppConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    AppConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AppConjunctionEnum fromValue(String input) {
      for (AppConjunctionEnum b : AppConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AppConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AppConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AppConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AppConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("appConjunction")
  private AppConjunctionEnum appConjunction = null;

  @SerializedName("appCriteria")
  private List<Qcriterion> appCriteria = null;

  @SerializedName("application")
  private Entity application = null;

  @SerializedName("assignedRoles")
  private Boolean assignedRoles = null;

  /**
   * the b role conjunction
   */
  @JsonAdapter(BRoleConjunctionEnum.Adapter.class)
  public enum BRoleConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    BRoleConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static BRoleConjunctionEnum fromValue(String input) {
      for (BRoleConjunctionEnum b : BRoleConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<BRoleConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final BRoleConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public BRoleConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return BRoleConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("bRoleConjunction")
  private BRoleConjunctionEnum bRoleConjunction = null;

  @SerializedName("bRoleCriteria")
  private List<Qcriterion> bRoleCriteria = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("groupInRoles")
  private Boolean groupInRoles = null;

  @SerializedName("includeAccounts")
  private Boolean includeAccounts = null;

  @SerializedName("includeCategorizedOrphanAccts")
  private Boolean includeCategorizedOrphanAccts = null;

  @SerializedName("includePermissions")
  private Boolean includePermissions = null;

  /**
   * Gets or Sets mAcctConjunction
   */
  @JsonAdapter(MAcctConjunctionEnum.Adapter.class)
  public enum MAcctConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MAcctConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MAcctConjunctionEnum fromValue(String input) {
      for (MAcctConjunctionEnum b : MAcctConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MAcctConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MAcctConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MAcctConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MAcctConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_acctConjunction")
  private MAcctConjunctionEnum mAcctConjunction = null;

  @SerializedName("m_acctCriteria")
  private List<Qcriterion> mAcctCriteria = null;

  @SerializedName("m_apiVersion")
  private String mApiVersion = null;

  /**
   * Gets or Sets mAppConjunction
   */
  @JsonAdapter(MAppConjunctionEnum.Adapter.class)
  public enum MAppConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MAppConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MAppConjunctionEnum fromValue(String input) {
      for (MAppConjunctionEnum b : MAppConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MAppConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MAppConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MAppConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MAppConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_appConjunction")
  private MAppConjunctionEnum mAppConjunction = null;

  @SerializedName("m_appCriteria")
  private List<Qcriterion> mAppCriteria = null;

  @SerializedName("m_application")
  private Entity mApplication = null;

  @SerializedName("m_assignedRolesOnly")
  private Boolean mAssignedRolesOnly = null;

  /**
   * Gets or Sets mBRoleConjunction
   */
  @JsonAdapter(MBRoleConjunctionEnum.Adapter.class)
  public enum MBRoleConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MBRoleConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MBRoleConjunctionEnum fromValue(String input) {
      for (MBRoleConjunctionEnum b : MBRoleConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MBRoleConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MBRoleConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MBRoleConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MBRoleConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_bRoleConjunction")
  private MBRoleConjunctionEnum mBRoleConjunction = null;

  @SerializedName("m_bRoleCriteria")
  private List<Qcriterion> mBRoleCriteria = null;

  @SerializedName("m_description")
  private String mDescription = null;

  @SerializedName("m_groupInRoles")
  private Boolean mGroupInRoles = null;

  @SerializedName("m_includeAccounts")
  private Boolean mIncludeAccounts = null;

  @SerializedName("m_includeCategorizedOrphanAccts")
  private Boolean mIncludeCategorizedOrphanAccts = null;

  @SerializedName("m_includePermissions")
  private Boolean mIncludePermissions = null;

  /**
   * Gets or Sets mOrphanAcctConjunction
   */
  @JsonAdapter(MOrphanAcctConjunctionEnum.Adapter.class)
  public enum MOrphanAcctConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MOrphanAcctConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MOrphanAcctConjunctionEnum fromValue(String input) {
      for (MOrphanAcctConjunctionEnum b : MOrphanAcctConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MOrphanAcctConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MOrphanAcctConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MOrphanAcctConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MOrphanAcctConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_orphanAcctConjunction")
  private MOrphanAcctConjunctionEnum mOrphanAcctConjunction = null;

  @SerializedName("m_orphanAcctCriteria")
  private List<Qcriterion> mOrphanAcctCriteria = null;

  /**
   * Gets or Sets mPermConjunction
   */
  @JsonAdapter(MPermConjunctionEnum.Adapter.class)
  public enum MPermConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MPermConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MPermConjunctionEnum fromValue(String input) {
      for (MPermConjunctionEnum b : MPermConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MPermConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MPermConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MPermConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MPermConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_permConjunction")
  private MPermConjunctionEnum mPermConjunction = null;

  @SerializedName("m_permCriteria")
  private List<Qcriterion> mPermCriteria = null;

  /**
   * Gets or Sets mReviewSubtype
   */
  @JsonAdapter(MReviewSubtypeEnum.Adapter.class)
  public enum MReviewSubtypeEnum {
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("ROLE")
    ROLE("ROLE"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION"),
    @SerializedName("ORPHAN")
    ORPHAN("ORPHAN"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("MAPPED_ACCOUNT")
    MAPPED_ACCOUNT("MAPPED_ACCOUNT");

    private String value;

    MReviewSubtypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MReviewSubtypeEnum fromValue(String input) {
      for (MReviewSubtypeEnum b : MReviewSubtypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MReviewSubtypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MReviewSubtypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MReviewSubtypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MReviewSubtypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_reviewSubtype")
  private List<MReviewSubtypeEnum> mReviewSubtype = null;

  /**
   * Gets or Sets mReviewType
   */
  @JsonAdapter(MReviewTypeEnum.Adapter.class)
  public enum MReviewTypeEnum {
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

    MReviewTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MReviewTypeEnum fromValue(String input) {
      for (MReviewTypeEnum b : MReviewTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MReviewTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MReviewTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MReviewTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MReviewTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_reviewType")
  private MReviewTypeEnum mReviewType = null;

  /**
   * Gets or Sets mRoleConjunction
   */
  @JsonAdapter(MRoleConjunctionEnum.Adapter.class)
  public enum MRoleConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MRoleConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MRoleConjunctionEnum fromValue(String input) {
      for (MRoleConjunctionEnum b : MRoleConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MRoleConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MRoleConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MRoleConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MRoleConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_roleConjunction")
  private MRoleConjunctionEnum mRoleConjunction = null;

  @SerializedName("m_roleCriteria")
  private List<Qcriterion> mRoleCriteria = null;

  @SerializedName("m_selectedAccts")
  private List<Entity> mSelectedAccts = null;

  @SerializedName("m_selectedApps")
  private List<Entity> mSelectedApps = null;

  @SerializedName("m_selectedBRoles")
  private List<Entity> mSelectedBRoles = null;

  @SerializedName("m_selectedOrphanAccts")
  private List<Entity> mSelectedOrphanAccts = null;

  @SerializedName("m_selectedPerms")
  private List<Entity> mSelectedPerms = null;

  @SerializedName("m_selectedRoles")
  private List<Entity> mSelectedRoles = null;

  @SerializedName("m_selectedUsers")
  private List<Entity> mSelectedUsers = null;

  @SerializedName("m_type")
  private String mType = null;

  /**
   * Gets or Sets mUserConjunction
   */
  @JsonAdapter(MUserConjunctionEnum.Adapter.class)
  public enum MUserConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MUserConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MUserConjunctionEnum fromValue(String input) {
      for (MUserConjunctionEnum b : MUserConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MUserConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MUserConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MUserConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MUserConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_userConjunction")
  private MUserConjunctionEnum mUserConjunction = null;

  @SerializedName("m_userCriteria")
  private List<Qcriterion> mUserCriteria = null;

  /**
   * Gets or Sets mUsersAre
   */
  @JsonAdapter(MUsersAreEnum.Adapter.class)
  public enum MUsersAreEnum {
    @SerializedName("OWNERS")
    OWNERS("OWNERS"),
    @SerializedName("HOLDERS")
    HOLDERS("HOLDERS");

    private String value;

    MUsersAreEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MUsersAreEnum fromValue(String input) {
      for (MUsersAreEnum b : MUsersAreEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MUsersAreEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MUsersAreEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MUsersAreEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MUsersAreEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_usersAre")
  private List<MUsersAreEnum> mUsersAre = null;

  @SerializedName("m_usersAreDirectReports")
  private Boolean mUsersAreDirectReports = null;

  /**
   * Gets or Sets mUsersHaving
   */
  @JsonAdapter(MUsersHavingEnum.Adapter.class)
  public enum MUsersHavingEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    MUsersHavingEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static MUsersHavingEnum fromValue(String input) {
      for (MUsersHavingEnum b : MUsersHavingEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<MUsersHavingEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MUsersHavingEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public MUsersHavingEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return MUsersHavingEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("m_usersHaving")
  private MUsersHavingEnum mUsersHaving = null;

  /**
   * the orphan acct conjunction
   */
  @JsonAdapter(OrphanAcctConjunctionEnum.Adapter.class)
  public enum OrphanAcctConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    OrphanAcctConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static OrphanAcctConjunctionEnum fromValue(String input) {
      for (OrphanAcctConjunctionEnum b : OrphanAcctConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<OrphanAcctConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final OrphanAcctConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public OrphanAcctConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return OrphanAcctConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("orphanAcctConjunction")
  private OrphanAcctConjunctionEnum orphanAcctConjunction = null;

  @SerializedName("orphanAcctCriteria")
  private List<Qcriterion> orphanAcctCriteria = null;

  @SerializedName("permAssignmentCriteria")
  private List<Qcriterion> permAssignmentCriteria = null;

  /**
   * the perm conjunction
   */
  @JsonAdapter(PermConjunctionEnum.Adapter.class)
  public enum PermConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    PermConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static PermConjunctionEnum fromValue(String input) {
      for (PermConjunctionEnum b : PermConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<PermConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PermConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public PermConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return PermConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("permConjunction")
  private PermConjunctionEnum permConjunction = null;

  @SerializedName("permCriteria")
  private List<Qcriterion> permCriteria = null;

  @SerializedName("permOnlyCriteria")
  private List<Qcriterion> permOnlyCriteria = null;

  /**
   * Gets or Sets reviewSubtype
   */
  @JsonAdapter(ReviewSubtypeEnum.Adapter.class)
  public enum ReviewSubtypeEnum {
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("ACCOUNT")
    ACCOUNT("ACCOUNT"),
    @SerializedName("PERMISSION")
    PERMISSION("PERMISSION"),
    @SerializedName("ROLE")
    ROLE("ROLE"),
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION"),
    @SerializedName("ORPHAN")
    ORPHAN("ORPHAN"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("MAPPED_ACCOUNT")
    MAPPED_ACCOUNT("MAPPED_ACCOUNT");

    private String value;

    ReviewSubtypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ReviewSubtypeEnum fromValue(String input) {
      for (ReviewSubtypeEnum b : ReviewSubtypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ReviewSubtypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReviewSubtypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ReviewSubtypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ReviewSubtypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("reviewSubtype")
  private List<ReviewSubtypeEnum> reviewSubtype = null;

  /**
   * the review type
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

  /**
   * the role conjunction
   */
  @JsonAdapter(RoleConjunctionEnum.Adapter.class)
  public enum RoleConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    RoleConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RoleConjunctionEnum fromValue(String input) {
      for (RoleConjunctionEnum b : RoleConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RoleConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RoleConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RoleConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RoleConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("roleConjunction")
  private RoleConjunctionEnum roleConjunction = null;

  @SerializedName("roleCriteria")
  private List<Qcriterion> roleCriteria = null;

  @SerializedName("selectedAccts")
  private List<Entity> selectedAccts = null;

  @SerializedName("selectedApps")
  private List<Entity> selectedApps = null;

  @SerializedName("selectedBRoles")
  private List<Entity> selectedBRoles = null;

  @SerializedName("selectedOrphanAccts")
  private List<Entity> selectedOrphanAccts = null;

  @SerializedName("selectedPerms")
  private List<Entity> selectedPerms = null;

  @SerializedName("selectedRoles")
  private List<Entity> selectedRoles = null;

  @SerializedName("selectedUsers")
  private List<Entity> selectedUsers = null;

  @SerializedName("type")
  private String type = null;

  /**
   * the user conjunction
   */
  @JsonAdapter(UserConjunctionEnum.Adapter.class)
  public enum UserConjunctionEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    UserConjunctionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static UserConjunctionEnum fromValue(String input) {
      for (UserConjunctionEnum b : UserConjunctionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<UserConjunctionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final UserConjunctionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public UserConjunctionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return UserConjunctionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("userConjunction")
  private UserConjunctionEnum userConjunction = null;

  @SerializedName("userCriteria")
  private List<Qcriterion> userCriteria = null;

  /**
   * Gets or Sets usersAre
   */
  @JsonAdapter(UsersAreEnum.Adapter.class)
  public enum UsersAreEnum {
    @SerializedName("OWNERS")
    OWNERS("OWNERS"),
    @SerializedName("HOLDERS")
    HOLDERS("HOLDERS");

    private String value;

    UsersAreEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static UsersAreEnum fromValue(String input) {
      for (UsersAreEnum b : UsersAreEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<UsersAreEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final UsersAreEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public UsersAreEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return UsersAreEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("usersAre")
  private List<UsersAreEnum> usersAre = null;

  @SerializedName("usersAreDirectReports")
  private Boolean usersAreDirectReports = null;

  /**
   * the users having
   */
  @JsonAdapter(UsersHavingEnum.Adapter.class)
  public enum UsersHavingEnum {
    @SerializedName("AllOf")
    ALLOF("AllOf"),
    @SerializedName("AnyOf")
    ANYOF("AnyOf"),
    @SerializedName("MultipleOf")
    MULTIPLEOF("MultipleOf");

    private String value;

    UsersHavingEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static UsersHavingEnum fromValue(String input) {
      for (UsersHavingEnum b : UsersHavingEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<UsersHavingEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final UsersHavingEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public UsersHavingEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return UsersHavingEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("usersHaving")
  private UsersHavingEnum usersHaving = null;

  public ReviewingTargets acctConjunction(AcctConjunctionEnum acctConjunction) {
    this.acctConjunction = acctConjunction;
    return this;
  }

   /**
   * the acct conjunction
   * @return acctConjunction
  **/
  @ApiModelProperty(value = "the acct conjunction")
  public AcctConjunctionEnum getAcctConjunction() {
    return acctConjunction;
  }

  public void setAcctConjunction(AcctConjunctionEnum acctConjunction) {
    this.acctConjunction = acctConjunction;
  }

  public ReviewingTargets acctCriteria(List<Qcriterion> acctCriteria) {
    this.acctCriteria = acctCriteria;
    return this;
  }

  public ReviewingTargets addAcctCriteriaItem(Qcriterion acctCriteriaItem) {
    if (this.acctCriteria == null) {
      this.acctCriteria = new ArrayList<Qcriterion>();
    }
    this.acctCriteria.add(acctCriteriaItem);
    return this;
  }

   /**
   * the acct criteria
   * @return acctCriteria
  **/
  @ApiModelProperty(value = "the acct criteria")
  public List<Qcriterion> getAcctCriteria() {
    return acctCriteria;
  }

  public void setAcctCriteria(List<Qcriterion> acctCriteria) {
    this.acctCriteria = acctCriteria;
  }

  public ReviewingTargets apiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
    return this;
  }

   /**
   * the api version
   * @return apiVersion
  **/
  @ApiModelProperty(value = "the api version")
  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public ReviewingTargets appConjunction(AppConjunctionEnum appConjunction) {
    this.appConjunction = appConjunction;
    return this;
  }

   /**
   * the app conjunction
   * @return appConjunction
  **/
  @ApiModelProperty(value = "the app conjunction")
  public AppConjunctionEnum getAppConjunction() {
    return appConjunction;
  }

  public void setAppConjunction(AppConjunctionEnum appConjunction) {
    this.appConjunction = appConjunction;
  }

  public ReviewingTargets appCriteria(List<Qcriterion> appCriteria) {
    this.appCriteria = appCriteria;
    return this;
  }

  public ReviewingTargets addAppCriteriaItem(Qcriterion appCriteriaItem) {
    if (this.appCriteria == null) {
      this.appCriteria = new ArrayList<Qcriterion>();
    }
    this.appCriteria.add(appCriteriaItem);
    return this;
  }

   /**
   * the app criteria
   * @return appCriteria
  **/
  @ApiModelProperty(value = "the app criteria")
  public List<Qcriterion> getAppCriteria() {
    return appCriteria;
  }

  public void setAppCriteria(List<Qcriterion> appCriteria) {
    this.appCriteria = appCriteria;
  }

  public ReviewingTargets application(Entity application) {
    this.application = application;
    return this;
  }

   /**
   * Get application
   * @return application
  **/
  @ApiModelProperty(value = "")
  public Entity getApplication() {
    return application;
  }

  public void setApplication(Entity application) {
    this.application = application;
  }

  public ReviewingTargets assignedRoles(Boolean assignedRoles) {
    this.assignedRoles = assignedRoles;
    return this;
  }

   /**
   * true if we will be considering assigned technical roles only, false (or null) if detected only
   * @return assignedRoles
  **/
  @ApiModelProperty(value = "true if we will be considering assigned technical roles only, false (or null) if detected only")
  public Boolean isAssignedRoles() {
    return assignedRoles;
  }

  public void setAssignedRoles(Boolean assignedRoles) {
    this.assignedRoles = assignedRoles;
  }

  public ReviewingTargets bRoleConjunction(BRoleConjunctionEnum bRoleConjunction) {
    this.bRoleConjunction = bRoleConjunction;
    return this;
  }

   /**
   * the b role conjunction
   * @return bRoleConjunction
  **/
  @ApiModelProperty(value = "the b role conjunction")
  public BRoleConjunctionEnum getBRoleConjunction() {
    return bRoleConjunction;
  }

  public void setBRoleConjunction(BRoleConjunctionEnum bRoleConjunction) {
    this.bRoleConjunction = bRoleConjunction;
  }

  public ReviewingTargets bRoleCriteria(List<Qcriterion> bRoleCriteria) {
    this.bRoleCriteria = bRoleCriteria;
    return this;
  }

  public ReviewingTargets addBRoleCriteriaItem(Qcriterion bRoleCriteriaItem) {
    if (this.bRoleCriteria == null) {
      this.bRoleCriteria = new ArrayList<Qcriterion>();
    }
    this.bRoleCriteria.add(bRoleCriteriaItem);
    return this;
  }

   /**
   * the b role criteria
   * @return bRoleCriteria
  **/
  @ApiModelProperty(value = "the b role criteria")
  public List<Qcriterion> getBRoleCriteria() {
    return bRoleCriteria;
  }

  public void setBRoleCriteria(List<Qcriterion> bRoleCriteria) {
    this.bRoleCriteria = bRoleCriteria;
  }

  public ReviewingTargets description(String description) {
    this.description = description;
    return this;
  }

   /**
   * the description
   * @return description
  **/
  @ApiModelProperty(value = "the description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ReviewingTargets groupInRoles(Boolean groupInRoles) {
    this.groupInRoles = groupInRoles;
    return this;
  }

   /**
   * the group in roles
   * @return groupInRoles
  **/
  @ApiModelProperty(value = "the group in roles")
  public Boolean isGroupInRoles() {
    return groupInRoles;
  }

  public void setGroupInRoles(Boolean groupInRoles) {
    this.groupInRoles = groupInRoles;
  }

  public ReviewingTargets includeAccounts(Boolean includeAccounts) {
    this.includeAccounts = includeAccounts;
    return this;
  }

   /**
   * the include accounts
   * @return includeAccounts
  **/
  @ApiModelProperty(value = "the include accounts")
  public Boolean isIncludeAccounts() {
    return includeAccounts;
  }

  public void setIncludeAccounts(Boolean includeAccounts) {
    this.includeAccounts = includeAccounts;
  }

  public ReviewingTargets includeCategorizedOrphanAccts(Boolean includeCategorizedOrphanAccts) {
    this.includeCategorizedOrphanAccts = includeCategorizedOrphanAccts;
    return this;
  }

   /**
   * the include categorized orphan accts
   * @return includeCategorizedOrphanAccts
  **/
  @ApiModelProperty(value = "the include categorized orphan accts")
  public Boolean isIncludeCategorizedOrphanAccts() {
    return includeCategorizedOrphanAccts;
  }

  public void setIncludeCategorizedOrphanAccts(Boolean includeCategorizedOrphanAccts) {
    this.includeCategorizedOrphanAccts = includeCategorizedOrphanAccts;
  }

  public ReviewingTargets includePermissions(Boolean includePermissions) {
    this.includePermissions = includePermissions;
    return this;
  }

   /**
   * the include permissions
   * @return includePermissions
  **/
  @ApiModelProperty(value = "the include permissions")
  public Boolean isIncludePermissions() {
    return includePermissions;
  }

  public void setIncludePermissions(Boolean includePermissions) {
    this.includePermissions = includePermissions;
  }

  public ReviewingTargets mAcctConjunction(MAcctConjunctionEnum mAcctConjunction) {
    this.mAcctConjunction = mAcctConjunction;
    return this;
  }

   /**
   * Get mAcctConjunction
   * @return mAcctConjunction
  **/
  @ApiModelProperty(value = "")
  public MAcctConjunctionEnum getMAcctConjunction() {
    return mAcctConjunction;
  }

  public void setMAcctConjunction(MAcctConjunctionEnum mAcctConjunction) {
    this.mAcctConjunction = mAcctConjunction;
  }

  public ReviewingTargets mAcctCriteria(List<Qcriterion> mAcctCriteria) {
    this.mAcctCriteria = mAcctCriteria;
    return this;
  }

  public ReviewingTargets addMAcctCriteriaItem(Qcriterion mAcctCriteriaItem) {
    if (this.mAcctCriteria == null) {
      this.mAcctCriteria = new ArrayList<Qcriterion>();
    }
    this.mAcctCriteria.add(mAcctCriteriaItem);
    return this;
  }

   /**
   * Get mAcctCriteria
   * @return mAcctCriteria
  **/
  @ApiModelProperty(value = "")
  public List<Qcriterion> getMAcctCriteria() {
    return mAcctCriteria;
  }

  public void setMAcctCriteria(List<Qcriterion> mAcctCriteria) {
    this.mAcctCriteria = mAcctCriteria;
  }

  public ReviewingTargets mApiVersion(String mApiVersion) {
    this.mApiVersion = mApiVersion;
    return this;
  }

   /**
   * Get mApiVersion
   * @return mApiVersion
  **/
  @ApiModelProperty(value = "")
  public String getMApiVersion() {
    return mApiVersion;
  }

  public void setMApiVersion(String mApiVersion) {
    this.mApiVersion = mApiVersion;
  }

  public ReviewingTargets mAppConjunction(MAppConjunctionEnum mAppConjunction) {
    this.mAppConjunction = mAppConjunction;
    return this;
  }

   /**
   * Get mAppConjunction
   * @return mAppConjunction
  **/
  @ApiModelProperty(value = "")
  public MAppConjunctionEnum getMAppConjunction() {
    return mAppConjunction;
  }

  public void setMAppConjunction(MAppConjunctionEnum mAppConjunction) {
    this.mAppConjunction = mAppConjunction;
  }

  public ReviewingTargets mAppCriteria(List<Qcriterion> mAppCriteria) {
    this.mAppCriteria = mAppCriteria;
    return this;
  }

  public ReviewingTargets addMAppCriteriaItem(Qcriterion mAppCriteriaItem) {
    if (this.mAppCriteria == null) {
      this.mAppCriteria = new ArrayList<Qcriterion>();
    }
    this.mAppCriteria.add(mAppCriteriaItem);
    return this;
  }

   /**
   * Get mAppCriteria
   * @return mAppCriteria
  **/
  @ApiModelProperty(value = "")
  public List<Qcriterion> getMAppCriteria() {
    return mAppCriteria;
  }

  public void setMAppCriteria(List<Qcriterion> mAppCriteria) {
    this.mAppCriteria = mAppCriteria;
  }

  public ReviewingTargets mApplication(Entity mApplication) {
    this.mApplication = mApplication;
    return this;
  }

   /**
   * Get mApplication
   * @return mApplication
  **/
  @ApiModelProperty(value = "")
  public Entity getMApplication() {
    return mApplication;
  }

  public void setMApplication(Entity mApplication) {
    this.mApplication = mApplication;
  }

  public ReviewingTargets mAssignedRolesOnly(Boolean mAssignedRolesOnly) {
    this.mAssignedRolesOnly = mAssignedRolesOnly;
    return this;
  }

   /**
   * Get mAssignedRolesOnly
   * @return mAssignedRolesOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean isMAssignedRolesOnly() {
    return mAssignedRolesOnly;
  }

  public void setMAssignedRolesOnly(Boolean mAssignedRolesOnly) {
    this.mAssignedRolesOnly = mAssignedRolesOnly;
  }

  public ReviewingTargets mBRoleConjunction(MBRoleConjunctionEnum mBRoleConjunction) {
    this.mBRoleConjunction = mBRoleConjunction;
    return this;
  }

   /**
   * Get mBRoleConjunction
   * @return mBRoleConjunction
  **/
  @ApiModelProperty(value = "")
  public MBRoleConjunctionEnum getMBRoleConjunction() {
    return mBRoleConjunction;
  }

  public void setMBRoleConjunction(MBRoleConjunctionEnum mBRoleConjunction) {
    this.mBRoleConjunction = mBRoleConjunction;
  }

  public ReviewingTargets mBRoleCriteria(List<Qcriterion> mBRoleCriteria) {
    this.mBRoleCriteria = mBRoleCriteria;
    return this;
  }

  public ReviewingTargets addMBRoleCriteriaItem(Qcriterion mBRoleCriteriaItem) {
    if (this.mBRoleCriteria == null) {
      this.mBRoleCriteria = new ArrayList<Qcriterion>();
    }
    this.mBRoleCriteria.add(mBRoleCriteriaItem);
    return this;
  }

   /**
   * Get mBRoleCriteria
   * @return mBRoleCriteria
  **/
  @ApiModelProperty(value = "")
  public List<Qcriterion> getMBRoleCriteria() {
    return mBRoleCriteria;
  }

  public void setMBRoleCriteria(List<Qcriterion> mBRoleCriteria) {
    this.mBRoleCriteria = mBRoleCriteria;
  }

  public ReviewingTargets mDescription(String mDescription) {
    this.mDescription = mDescription;
    return this;
  }

   /**
   * Get mDescription
   * @return mDescription
  **/
  @ApiModelProperty(value = "")
  public String getMDescription() {
    return mDescription;
  }

  public void setMDescription(String mDescription) {
    this.mDescription = mDescription;
  }

  public ReviewingTargets mGroupInRoles(Boolean mGroupInRoles) {
    this.mGroupInRoles = mGroupInRoles;
    return this;
  }

   /**
   * Get mGroupInRoles
   * @return mGroupInRoles
  **/
  @ApiModelProperty(value = "")
  public Boolean isMGroupInRoles() {
    return mGroupInRoles;
  }

  public void setMGroupInRoles(Boolean mGroupInRoles) {
    this.mGroupInRoles = mGroupInRoles;
  }

  public ReviewingTargets mIncludeAccounts(Boolean mIncludeAccounts) {
    this.mIncludeAccounts = mIncludeAccounts;
    return this;
  }

   /**
   * Get mIncludeAccounts
   * @return mIncludeAccounts
  **/
  @ApiModelProperty(value = "")
  public Boolean isMIncludeAccounts() {
    return mIncludeAccounts;
  }

  public void setMIncludeAccounts(Boolean mIncludeAccounts) {
    this.mIncludeAccounts = mIncludeAccounts;
  }

  public ReviewingTargets mIncludeCategorizedOrphanAccts(Boolean mIncludeCategorizedOrphanAccts) {
    this.mIncludeCategorizedOrphanAccts = mIncludeCategorizedOrphanAccts;
    return this;
  }

   /**
   * Get mIncludeCategorizedOrphanAccts
   * @return mIncludeCategorizedOrphanAccts
  **/
  @ApiModelProperty(value = "")
  public Boolean isMIncludeCategorizedOrphanAccts() {
    return mIncludeCategorizedOrphanAccts;
  }

  public void setMIncludeCategorizedOrphanAccts(Boolean mIncludeCategorizedOrphanAccts) {
    this.mIncludeCategorizedOrphanAccts = mIncludeCategorizedOrphanAccts;
  }

  public ReviewingTargets mIncludePermissions(Boolean mIncludePermissions) {
    this.mIncludePermissions = mIncludePermissions;
    return this;
  }

   /**
   * Get mIncludePermissions
   * @return mIncludePermissions
  **/
  @ApiModelProperty(value = "")
  public Boolean isMIncludePermissions() {
    return mIncludePermissions;
  }

  public void setMIncludePermissions(Boolean mIncludePermissions) {
    this.mIncludePermissions = mIncludePermissions;
  }

  public ReviewingTargets mOrphanAcctConjunction(MOrphanAcctConjunctionEnum mOrphanAcctConjunction) {
    this.mOrphanAcctConjunction = mOrphanAcctConjunction;
    return this;
  }

   /**
   * Get mOrphanAcctConjunction
   * @return mOrphanAcctConjunction
  **/
  @ApiModelProperty(value = "")
  public MOrphanAcctConjunctionEnum getMOrphanAcctConjunction() {
    return mOrphanAcctConjunction;
  }

  public void setMOrphanAcctConjunction(MOrphanAcctConjunctionEnum mOrphanAcctConjunction) {
    this.mOrphanAcctConjunction = mOrphanAcctConjunction;
  }

  public ReviewingTargets mOrphanAcctCriteria(List<Qcriterion> mOrphanAcctCriteria) {
    this.mOrphanAcctCriteria = mOrphanAcctCriteria;
    return this;
  }

  public ReviewingTargets addMOrphanAcctCriteriaItem(Qcriterion mOrphanAcctCriteriaItem) {
    if (this.mOrphanAcctCriteria == null) {
      this.mOrphanAcctCriteria = new ArrayList<Qcriterion>();
    }
    this.mOrphanAcctCriteria.add(mOrphanAcctCriteriaItem);
    return this;
  }

   /**
   * Get mOrphanAcctCriteria
   * @return mOrphanAcctCriteria
  **/
  @ApiModelProperty(value = "")
  public List<Qcriterion> getMOrphanAcctCriteria() {
    return mOrphanAcctCriteria;
  }

  public void setMOrphanAcctCriteria(List<Qcriterion> mOrphanAcctCriteria) {
    this.mOrphanAcctCriteria = mOrphanAcctCriteria;
  }

  public ReviewingTargets mPermConjunction(MPermConjunctionEnum mPermConjunction) {
    this.mPermConjunction = mPermConjunction;
    return this;
  }

   /**
   * Get mPermConjunction
   * @return mPermConjunction
  **/
  @ApiModelProperty(value = "")
  public MPermConjunctionEnum getMPermConjunction() {
    return mPermConjunction;
  }

  public void setMPermConjunction(MPermConjunctionEnum mPermConjunction) {
    this.mPermConjunction = mPermConjunction;
  }

  public ReviewingTargets mPermCriteria(List<Qcriterion> mPermCriteria) {
    this.mPermCriteria = mPermCriteria;
    return this;
  }

  public ReviewingTargets addMPermCriteriaItem(Qcriterion mPermCriteriaItem) {
    if (this.mPermCriteria == null) {
      this.mPermCriteria = new ArrayList<Qcriterion>();
    }
    this.mPermCriteria.add(mPermCriteriaItem);
    return this;
  }

   /**
   * Get mPermCriteria
   * @return mPermCriteria
  **/
  @ApiModelProperty(value = "")
  public List<Qcriterion> getMPermCriteria() {
    return mPermCriteria;
  }

  public void setMPermCriteria(List<Qcriterion> mPermCriteria) {
    this.mPermCriteria = mPermCriteria;
  }

  public ReviewingTargets mReviewSubtype(List<MReviewSubtypeEnum> mReviewSubtype) {
    this.mReviewSubtype = mReviewSubtype;
    return this;
  }

  public ReviewingTargets addMReviewSubtypeItem(MReviewSubtypeEnum mReviewSubtypeItem) {
    if (this.mReviewSubtype == null) {
      this.mReviewSubtype = new ArrayList<MReviewSubtypeEnum>();
    }
    this.mReviewSubtype.add(mReviewSubtypeItem);
    return this;
  }

   /**
   * Get mReviewSubtype
   * @return mReviewSubtype
  **/
  @ApiModelProperty(value = "")
  public List<MReviewSubtypeEnum> getMReviewSubtype() {
    return mReviewSubtype;
  }

  public void setMReviewSubtype(List<MReviewSubtypeEnum> mReviewSubtype) {
    this.mReviewSubtype = mReviewSubtype;
  }

  public ReviewingTargets mReviewType(MReviewTypeEnum mReviewType) {
    this.mReviewType = mReviewType;
    return this;
  }

   /**
   * Get mReviewType
   * @return mReviewType
  **/
  @ApiModelProperty(value = "")
  public MReviewTypeEnum getMReviewType() {
    return mReviewType;
  }

  public void setMReviewType(MReviewTypeEnum mReviewType) {
    this.mReviewType = mReviewType;
  }

  public ReviewingTargets mRoleConjunction(MRoleConjunctionEnum mRoleConjunction) {
    this.mRoleConjunction = mRoleConjunction;
    return this;
  }

   /**
   * Get mRoleConjunction
   * @return mRoleConjunction
  **/
  @ApiModelProperty(value = "")
  public MRoleConjunctionEnum getMRoleConjunction() {
    return mRoleConjunction;
  }

  public void setMRoleConjunction(MRoleConjunctionEnum mRoleConjunction) {
    this.mRoleConjunction = mRoleConjunction;
  }

  public ReviewingTargets mRoleCriteria(List<Qcriterion> mRoleCriteria) {
    this.mRoleCriteria = mRoleCriteria;
    return this;
  }

  public ReviewingTargets addMRoleCriteriaItem(Qcriterion mRoleCriteriaItem) {
    if (this.mRoleCriteria == null) {
      this.mRoleCriteria = new ArrayList<Qcriterion>();
    }
    this.mRoleCriteria.add(mRoleCriteriaItem);
    return this;
  }

   /**
   * Get mRoleCriteria
   * @return mRoleCriteria
  **/
  @ApiModelProperty(value = "")
  public List<Qcriterion> getMRoleCriteria() {
    return mRoleCriteria;
  }

  public void setMRoleCriteria(List<Qcriterion> mRoleCriteria) {
    this.mRoleCriteria = mRoleCriteria;
  }

  public ReviewingTargets mSelectedAccts(List<Entity> mSelectedAccts) {
    this.mSelectedAccts = mSelectedAccts;
    return this;
  }

  public ReviewingTargets addMSelectedAcctsItem(Entity mSelectedAcctsItem) {
    if (this.mSelectedAccts == null) {
      this.mSelectedAccts = new ArrayList<Entity>();
    }
    this.mSelectedAccts.add(mSelectedAcctsItem);
    return this;
  }

   /**
   * Get mSelectedAccts
   * @return mSelectedAccts
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMSelectedAccts() {
    return mSelectedAccts;
  }

  public void setMSelectedAccts(List<Entity> mSelectedAccts) {
    this.mSelectedAccts = mSelectedAccts;
  }

  public ReviewingTargets mSelectedApps(List<Entity> mSelectedApps) {
    this.mSelectedApps = mSelectedApps;
    return this;
  }

  public ReviewingTargets addMSelectedAppsItem(Entity mSelectedAppsItem) {
    if (this.mSelectedApps == null) {
      this.mSelectedApps = new ArrayList<Entity>();
    }
    this.mSelectedApps.add(mSelectedAppsItem);
    return this;
  }

   /**
   * Get mSelectedApps
   * @return mSelectedApps
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMSelectedApps() {
    return mSelectedApps;
  }

  public void setMSelectedApps(List<Entity> mSelectedApps) {
    this.mSelectedApps = mSelectedApps;
  }

  public ReviewingTargets mSelectedBRoles(List<Entity> mSelectedBRoles) {
    this.mSelectedBRoles = mSelectedBRoles;
    return this;
  }

  public ReviewingTargets addMSelectedBRolesItem(Entity mSelectedBRolesItem) {
    if (this.mSelectedBRoles == null) {
      this.mSelectedBRoles = new ArrayList<Entity>();
    }
    this.mSelectedBRoles.add(mSelectedBRolesItem);
    return this;
  }

   /**
   * Get mSelectedBRoles
   * @return mSelectedBRoles
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMSelectedBRoles() {
    return mSelectedBRoles;
  }

  public void setMSelectedBRoles(List<Entity> mSelectedBRoles) {
    this.mSelectedBRoles = mSelectedBRoles;
  }

  public ReviewingTargets mSelectedOrphanAccts(List<Entity> mSelectedOrphanAccts) {
    this.mSelectedOrphanAccts = mSelectedOrphanAccts;
    return this;
  }

  public ReviewingTargets addMSelectedOrphanAcctsItem(Entity mSelectedOrphanAcctsItem) {
    if (this.mSelectedOrphanAccts == null) {
      this.mSelectedOrphanAccts = new ArrayList<Entity>();
    }
    this.mSelectedOrphanAccts.add(mSelectedOrphanAcctsItem);
    return this;
  }

   /**
   * Get mSelectedOrphanAccts
   * @return mSelectedOrphanAccts
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMSelectedOrphanAccts() {
    return mSelectedOrphanAccts;
  }

  public void setMSelectedOrphanAccts(List<Entity> mSelectedOrphanAccts) {
    this.mSelectedOrphanAccts = mSelectedOrphanAccts;
  }

  public ReviewingTargets mSelectedPerms(List<Entity> mSelectedPerms) {
    this.mSelectedPerms = mSelectedPerms;
    return this;
  }

  public ReviewingTargets addMSelectedPermsItem(Entity mSelectedPermsItem) {
    if (this.mSelectedPerms == null) {
      this.mSelectedPerms = new ArrayList<Entity>();
    }
    this.mSelectedPerms.add(mSelectedPermsItem);
    return this;
  }

   /**
   * Get mSelectedPerms
   * @return mSelectedPerms
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMSelectedPerms() {
    return mSelectedPerms;
  }

  public void setMSelectedPerms(List<Entity> mSelectedPerms) {
    this.mSelectedPerms = mSelectedPerms;
  }

  public ReviewingTargets mSelectedRoles(List<Entity> mSelectedRoles) {
    this.mSelectedRoles = mSelectedRoles;
    return this;
  }

  public ReviewingTargets addMSelectedRolesItem(Entity mSelectedRolesItem) {
    if (this.mSelectedRoles == null) {
      this.mSelectedRoles = new ArrayList<Entity>();
    }
    this.mSelectedRoles.add(mSelectedRolesItem);
    return this;
  }

   /**
   * Get mSelectedRoles
   * @return mSelectedRoles
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMSelectedRoles() {
    return mSelectedRoles;
  }

  public void setMSelectedRoles(List<Entity> mSelectedRoles) {
    this.mSelectedRoles = mSelectedRoles;
  }

  public ReviewingTargets mSelectedUsers(List<Entity> mSelectedUsers) {
    this.mSelectedUsers = mSelectedUsers;
    return this;
  }

  public ReviewingTargets addMSelectedUsersItem(Entity mSelectedUsersItem) {
    if (this.mSelectedUsers == null) {
      this.mSelectedUsers = new ArrayList<Entity>();
    }
    this.mSelectedUsers.add(mSelectedUsersItem);
    return this;
  }

   /**
   * Get mSelectedUsers
   * @return mSelectedUsers
  **/
  @ApiModelProperty(value = "")
  public List<Entity> getMSelectedUsers() {
    return mSelectedUsers;
  }

  public void setMSelectedUsers(List<Entity> mSelectedUsers) {
    this.mSelectedUsers = mSelectedUsers;
  }

  public ReviewingTargets mType(String mType) {
    this.mType = mType;
    return this;
  }

   /**
   * Get mType
   * @return mType
  **/
  @ApiModelProperty(value = "")
  public String getMType() {
    return mType;
  }

  public void setMType(String mType) {
    this.mType = mType;
  }

  public ReviewingTargets mUserConjunction(MUserConjunctionEnum mUserConjunction) {
    this.mUserConjunction = mUserConjunction;
    return this;
  }

   /**
   * Get mUserConjunction
   * @return mUserConjunction
  **/
  @ApiModelProperty(value = "")
  public MUserConjunctionEnum getMUserConjunction() {
    return mUserConjunction;
  }

  public void setMUserConjunction(MUserConjunctionEnum mUserConjunction) {
    this.mUserConjunction = mUserConjunction;
  }

  public ReviewingTargets mUserCriteria(List<Qcriterion> mUserCriteria) {
    this.mUserCriteria = mUserCriteria;
    return this;
  }

  public ReviewingTargets addMUserCriteriaItem(Qcriterion mUserCriteriaItem) {
    if (this.mUserCriteria == null) {
      this.mUserCriteria = new ArrayList<Qcriterion>();
    }
    this.mUserCriteria.add(mUserCriteriaItem);
    return this;
  }

   /**
   * Get mUserCriteria
   * @return mUserCriteria
  **/
  @ApiModelProperty(value = "")
  public List<Qcriterion> getMUserCriteria() {
    return mUserCriteria;
  }

  public void setMUserCriteria(List<Qcriterion> mUserCriteria) {
    this.mUserCriteria = mUserCriteria;
  }

  public ReviewingTargets mUsersAre(List<MUsersAreEnum> mUsersAre) {
    this.mUsersAre = mUsersAre;
    return this;
  }

  public ReviewingTargets addMUsersAreItem(MUsersAreEnum mUsersAreItem) {
    if (this.mUsersAre == null) {
      this.mUsersAre = new ArrayList<MUsersAreEnum>();
    }
    this.mUsersAre.add(mUsersAreItem);
    return this;
  }

   /**
   * Get mUsersAre
   * @return mUsersAre
  **/
  @ApiModelProperty(value = "")
  public List<MUsersAreEnum> getMUsersAre() {
    return mUsersAre;
  }

  public void setMUsersAre(List<MUsersAreEnum> mUsersAre) {
    this.mUsersAre = mUsersAre;
  }

  public ReviewingTargets mUsersAreDirectReports(Boolean mUsersAreDirectReports) {
    this.mUsersAreDirectReports = mUsersAreDirectReports;
    return this;
  }

   /**
   * Get mUsersAreDirectReports
   * @return mUsersAreDirectReports
  **/
  @ApiModelProperty(value = "")
  public Boolean isMUsersAreDirectReports() {
    return mUsersAreDirectReports;
  }

  public void setMUsersAreDirectReports(Boolean mUsersAreDirectReports) {
    this.mUsersAreDirectReports = mUsersAreDirectReports;
  }

  public ReviewingTargets mUsersHaving(MUsersHavingEnum mUsersHaving) {
    this.mUsersHaving = mUsersHaving;
    return this;
  }

   /**
   * Get mUsersHaving
   * @return mUsersHaving
  **/
  @ApiModelProperty(value = "")
  public MUsersHavingEnum getMUsersHaving() {
    return mUsersHaving;
  }

  public void setMUsersHaving(MUsersHavingEnum mUsersHaving) {
    this.mUsersHaving = mUsersHaving;
  }

  public ReviewingTargets orphanAcctConjunction(OrphanAcctConjunctionEnum orphanAcctConjunction) {
    this.orphanAcctConjunction = orphanAcctConjunction;
    return this;
  }

   /**
   * the orphan acct conjunction
   * @return orphanAcctConjunction
  **/
  @ApiModelProperty(value = "the orphan acct conjunction")
  public OrphanAcctConjunctionEnum getOrphanAcctConjunction() {
    return orphanAcctConjunction;
  }

  public void setOrphanAcctConjunction(OrphanAcctConjunctionEnum orphanAcctConjunction) {
    this.orphanAcctConjunction = orphanAcctConjunction;
  }

  public ReviewingTargets orphanAcctCriteria(List<Qcriterion> orphanAcctCriteria) {
    this.orphanAcctCriteria = orphanAcctCriteria;
    return this;
  }

  public ReviewingTargets addOrphanAcctCriteriaItem(Qcriterion orphanAcctCriteriaItem) {
    if (this.orphanAcctCriteria == null) {
      this.orphanAcctCriteria = new ArrayList<Qcriterion>();
    }
    this.orphanAcctCriteria.add(orphanAcctCriteriaItem);
    return this;
  }

   /**
   * the orphan acct criteria
   * @return orphanAcctCriteria
  **/
  @ApiModelProperty(value = "the orphan acct criteria")
  public List<Qcriterion> getOrphanAcctCriteria() {
    return orphanAcctCriteria;
  }

  public void setOrphanAcctCriteria(List<Qcriterion> orphanAcctCriteria) {
    this.orphanAcctCriteria = orphanAcctCriteria;
  }

  public ReviewingTargets permAssignmentCriteria(List<Qcriterion> permAssignmentCriteria) {
    this.permAssignmentCriteria = permAssignmentCriteria;
    return this;
  }

  public ReviewingTargets addPermAssignmentCriteriaItem(Qcriterion permAssignmentCriteriaItem) {
    if (this.permAssignmentCriteria == null) {
      this.permAssignmentCriteria = new ArrayList<Qcriterion>();
    }
    this.permAssignmentCriteria.add(permAssignmentCriteriaItem);
    return this;
  }

   /**
   * the perm assignment criteria
   * @return permAssignmentCriteria
  **/
  @ApiModelProperty(value = "the perm assignment criteria")
  public List<Qcriterion> getPermAssignmentCriteria() {
    return permAssignmentCriteria;
  }

  public void setPermAssignmentCriteria(List<Qcriterion> permAssignmentCriteria) {
    this.permAssignmentCriteria = permAssignmentCriteria;
  }

  public ReviewingTargets permConjunction(PermConjunctionEnum permConjunction) {
    this.permConjunction = permConjunction;
    return this;
  }

   /**
   * the perm conjunction
   * @return permConjunction
  **/
  @ApiModelProperty(value = "the perm conjunction")
  public PermConjunctionEnum getPermConjunction() {
    return permConjunction;
  }

  public void setPermConjunction(PermConjunctionEnum permConjunction) {
    this.permConjunction = permConjunction;
  }

  public ReviewingTargets permCriteria(List<Qcriterion> permCriteria) {
    this.permCriteria = permCriteria;
    return this;
  }

  public ReviewingTargets addPermCriteriaItem(Qcriterion permCriteriaItem) {
    if (this.permCriteria == null) {
      this.permCriteria = new ArrayList<Qcriterion>();
    }
    this.permCriteria.add(permCriteriaItem);
    return this;
  }

   /**
   * the perm criteria
   * @return permCriteria
  **/
  @ApiModelProperty(value = "the perm criteria")
  public List<Qcriterion> getPermCriteria() {
    return permCriteria;
  }

  public void setPermCriteria(List<Qcriterion> permCriteria) {
    this.permCriteria = permCriteria;
  }

  public ReviewingTargets permOnlyCriteria(List<Qcriterion> permOnlyCriteria) {
    this.permOnlyCriteria = permOnlyCriteria;
    return this;
  }

  public ReviewingTargets addPermOnlyCriteriaItem(Qcriterion permOnlyCriteriaItem) {
    if (this.permOnlyCriteria == null) {
      this.permOnlyCriteria = new ArrayList<Qcriterion>();
    }
    this.permOnlyCriteria.add(permOnlyCriteriaItem);
    return this;
  }

   /**
   * the perm only criteria
   * @return permOnlyCriteria
  **/
  @ApiModelProperty(value = "the perm only criteria")
  public List<Qcriterion> getPermOnlyCriteria() {
    return permOnlyCriteria;
  }

  public void setPermOnlyCriteria(List<Qcriterion> permOnlyCriteria) {
    this.permOnlyCriteria = permOnlyCriteria;
  }

  public ReviewingTargets reviewSubtype(List<ReviewSubtypeEnum> reviewSubtype) {
    this.reviewSubtype = reviewSubtype;
    return this;
  }

  public ReviewingTargets addReviewSubtypeItem(ReviewSubtypeEnum reviewSubtypeItem) {
    if (this.reviewSubtype == null) {
      this.reviewSubtype = new ArrayList<ReviewSubtypeEnum>();
    }
    this.reviewSubtype.add(reviewSubtypeItem);
    return this;
  }

   /**
   * the review subtype
   * @return reviewSubtype
  **/
  @ApiModelProperty(value = "the review subtype")
  public List<ReviewSubtypeEnum> getReviewSubtype() {
    return reviewSubtype;
  }

  public void setReviewSubtype(List<ReviewSubtypeEnum> reviewSubtype) {
    this.reviewSubtype = reviewSubtype;
  }

  public ReviewingTargets reviewType(ReviewTypeEnum reviewType) {
    this.reviewType = reviewType;
    return this;
  }

   /**
   * the review type
   * @return reviewType
  **/
  @ApiModelProperty(value = "the review type")
  public ReviewTypeEnum getReviewType() {
    return reviewType;
  }

  public void setReviewType(ReviewTypeEnum reviewType) {
    this.reviewType = reviewType;
  }

  public ReviewingTargets roleConjunction(RoleConjunctionEnum roleConjunction) {
    this.roleConjunction = roleConjunction;
    return this;
  }

   /**
   * the role conjunction
   * @return roleConjunction
  **/
  @ApiModelProperty(value = "the role conjunction")
  public RoleConjunctionEnum getRoleConjunction() {
    return roleConjunction;
  }

  public void setRoleConjunction(RoleConjunctionEnum roleConjunction) {
    this.roleConjunction = roleConjunction;
  }

  public ReviewingTargets roleCriteria(List<Qcriterion> roleCriteria) {
    this.roleCriteria = roleCriteria;
    return this;
  }

  public ReviewingTargets addRoleCriteriaItem(Qcriterion roleCriteriaItem) {
    if (this.roleCriteria == null) {
      this.roleCriteria = new ArrayList<Qcriterion>();
    }
    this.roleCriteria.add(roleCriteriaItem);
    return this;
  }

   /**
   * the role criteria
   * @return roleCriteria
  **/
  @ApiModelProperty(value = "the role criteria")
  public List<Qcriterion> getRoleCriteria() {
    return roleCriteria;
  }

  public void setRoleCriteria(List<Qcriterion> roleCriteria) {
    this.roleCriteria = roleCriteria;
  }

  public ReviewingTargets selectedAccts(List<Entity> selectedAccts) {
    this.selectedAccts = selectedAccts;
    return this;
  }

  public ReviewingTargets addSelectedAcctsItem(Entity selectedAcctsItem) {
    if (this.selectedAccts == null) {
      this.selectedAccts = new ArrayList<Entity>();
    }
    this.selectedAccts.add(selectedAcctsItem);
    return this;
  }

   /**
   * the selected accts
   * @return selectedAccts
  **/
  @ApiModelProperty(value = "the selected accts")
  public List<Entity> getSelectedAccts() {
    return selectedAccts;
  }

  public void setSelectedAccts(List<Entity> selectedAccts) {
    this.selectedAccts = selectedAccts;
  }

  public ReviewingTargets selectedApps(List<Entity> selectedApps) {
    this.selectedApps = selectedApps;
    return this;
  }

  public ReviewingTargets addSelectedAppsItem(Entity selectedAppsItem) {
    if (this.selectedApps == null) {
      this.selectedApps = new ArrayList<Entity>();
    }
    this.selectedApps.add(selectedAppsItem);
    return this;
  }

   /**
   * the selected apps
   * @return selectedApps
  **/
  @ApiModelProperty(value = "the selected apps")
  public List<Entity> getSelectedApps() {
    return selectedApps;
  }

  public void setSelectedApps(List<Entity> selectedApps) {
    this.selectedApps = selectedApps;
  }

  public ReviewingTargets selectedBRoles(List<Entity> selectedBRoles) {
    this.selectedBRoles = selectedBRoles;
    return this;
  }

  public ReviewingTargets addSelectedBRolesItem(Entity selectedBRolesItem) {
    if (this.selectedBRoles == null) {
      this.selectedBRoles = new ArrayList<Entity>();
    }
    this.selectedBRoles.add(selectedBRolesItem);
    return this;
  }

   /**
   * the selected b roles
   * @return selectedBRoles
  **/
  @ApiModelProperty(value = "the selected b roles")
  public List<Entity> getSelectedBRoles() {
    return selectedBRoles;
  }

  public void setSelectedBRoles(List<Entity> selectedBRoles) {
    this.selectedBRoles = selectedBRoles;
  }

  public ReviewingTargets selectedOrphanAccts(List<Entity> selectedOrphanAccts) {
    this.selectedOrphanAccts = selectedOrphanAccts;
    return this;
  }

  public ReviewingTargets addSelectedOrphanAcctsItem(Entity selectedOrphanAcctsItem) {
    if (this.selectedOrphanAccts == null) {
      this.selectedOrphanAccts = new ArrayList<Entity>();
    }
    this.selectedOrphanAccts.add(selectedOrphanAcctsItem);
    return this;
  }

   /**
   * the selected orphan accts
   * @return selectedOrphanAccts
  **/
  @ApiModelProperty(value = "the selected orphan accts")
  public List<Entity> getSelectedOrphanAccts() {
    return selectedOrphanAccts;
  }

  public void setSelectedOrphanAccts(List<Entity> selectedOrphanAccts) {
    this.selectedOrphanAccts = selectedOrphanAccts;
  }

  public ReviewingTargets selectedPerms(List<Entity> selectedPerms) {
    this.selectedPerms = selectedPerms;
    return this;
  }

  public ReviewingTargets addSelectedPermsItem(Entity selectedPermsItem) {
    if (this.selectedPerms == null) {
      this.selectedPerms = new ArrayList<Entity>();
    }
    this.selectedPerms.add(selectedPermsItem);
    return this;
  }

   /**
   * the selected perms
   * @return selectedPerms
  **/
  @ApiModelProperty(value = "the selected perms")
  public List<Entity> getSelectedPerms() {
    return selectedPerms;
  }

  public void setSelectedPerms(List<Entity> selectedPerms) {
    this.selectedPerms = selectedPerms;
  }

  public ReviewingTargets selectedRoles(List<Entity> selectedRoles) {
    this.selectedRoles = selectedRoles;
    return this;
  }

  public ReviewingTargets addSelectedRolesItem(Entity selectedRolesItem) {
    if (this.selectedRoles == null) {
      this.selectedRoles = new ArrayList<Entity>();
    }
    this.selectedRoles.add(selectedRolesItem);
    return this;
  }

   /**
   * the selected roles
   * @return selectedRoles
  **/
  @ApiModelProperty(value = "the selected roles")
  public List<Entity> getSelectedRoles() {
    return selectedRoles;
  }

  public void setSelectedRoles(List<Entity> selectedRoles) {
    this.selectedRoles = selectedRoles;
  }

  public ReviewingTargets selectedUsers(List<Entity> selectedUsers) {
    this.selectedUsers = selectedUsers;
    return this;
  }

  public ReviewingTargets addSelectedUsersItem(Entity selectedUsersItem) {
    if (this.selectedUsers == null) {
      this.selectedUsers = new ArrayList<Entity>();
    }
    this.selectedUsers.add(selectedUsersItem);
    return this;
  }

   /**
   * the selected users
   * @return selectedUsers
  **/
  @ApiModelProperty(value = "the selected users")
  public List<Entity> getSelectedUsers() {
    return selectedUsers;
  }

  public void setSelectedUsers(List<Entity> selectedUsers) {
    this.selectedUsers = selectedUsers;
  }

  public ReviewingTargets type(String type) {
    this.type = type;
    return this;
  }

   /**
   * the type
   * @return type
  **/
  @ApiModelProperty(value = "the type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ReviewingTargets userConjunction(UserConjunctionEnum userConjunction) {
    this.userConjunction = userConjunction;
    return this;
  }

   /**
   * the user conjunction
   * @return userConjunction
  **/
  @ApiModelProperty(value = "the user conjunction")
  public UserConjunctionEnum getUserConjunction() {
    return userConjunction;
  }

  public void setUserConjunction(UserConjunctionEnum userConjunction) {
    this.userConjunction = userConjunction;
  }

  public ReviewingTargets userCriteria(List<Qcriterion> userCriteria) {
    this.userCriteria = userCriteria;
    return this;
  }

  public ReviewingTargets addUserCriteriaItem(Qcriterion userCriteriaItem) {
    if (this.userCriteria == null) {
      this.userCriteria = new ArrayList<Qcriterion>();
    }
    this.userCriteria.add(userCriteriaItem);
    return this;
  }

   /**
   * the user criteria
   * @return userCriteria
  **/
  @ApiModelProperty(value = "the user criteria")
  public List<Qcriterion> getUserCriteria() {
    return userCriteria;
  }

  public void setUserCriteria(List<Qcriterion> userCriteria) {
    this.userCriteria = userCriteria;
  }

  public ReviewingTargets usersAre(List<UsersAreEnum> usersAre) {
    this.usersAre = usersAre;
    return this;
  }

  public ReviewingTargets addUsersAreItem(UsersAreEnum usersAreItem) {
    if (this.usersAre == null) {
      this.usersAre = new ArrayList<UsersAreEnum>();
    }
    this.usersAre.add(usersAreItem);
    return this;
  }

   /**
   * the users are
   * @return usersAre
  **/
  @ApiModelProperty(value = "the users are")
  public List<UsersAreEnum> getUsersAre() {
    return usersAre;
  }

  public void setUsersAre(List<UsersAreEnum> usersAre) {
    this.usersAre = usersAre;
  }

  public ReviewingTargets usersAreDirectReports(Boolean usersAreDirectReports) {
    this.usersAreDirectReports = usersAreDirectReports;
    return this;
  }

   /**
   * the users are direct reports
   * @return usersAreDirectReports
  **/
  @ApiModelProperty(value = "the users are direct reports")
  public Boolean isUsersAreDirectReports() {
    return usersAreDirectReports;
  }

  public void setUsersAreDirectReports(Boolean usersAreDirectReports) {
    this.usersAreDirectReports = usersAreDirectReports;
  }

  public ReviewingTargets usersHaving(UsersHavingEnum usersHaving) {
    this.usersHaving = usersHaving;
    return this;
  }

   /**
   * the users having
   * @return usersHaving
  **/
  @ApiModelProperty(value = "the users having")
  public UsersHavingEnum getUsersHaving() {
    return usersHaving;
  }

  public void setUsersHaving(UsersHavingEnum usersHaving) {
    this.usersHaving = usersHaving;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewingTargets reviewingTargets = (ReviewingTargets) o;
    return Objects.equals(this.acctConjunction, reviewingTargets.acctConjunction) &&
        Objects.equals(this.acctCriteria, reviewingTargets.acctCriteria) &&
        Objects.equals(this.apiVersion, reviewingTargets.apiVersion) &&
        Objects.equals(this.appConjunction, reviewingTargets.appConjunction) &&
        Objects.equals(this.appCriteria, reviewingTargets.appCriteria) &&
        Objects.equals(this.application, reviewingTargets.application) &&
        Objects.equals(this.assignedRoles, reviewingTargets.assignedRoles) &&
        Objects.equals(this.bRoleConjunction, reviewingTargets.bRoleConjunction) &&
        Objects.equals(this.bRoleCriteria, reviewingTargets.bRoleCriteria) &&
        Objects.equals(this.description, reviewingTargets.description) &&
        Objects.equals(this.groupInRoles, reviewingTargets.groupInRoles) &&
        Objects.equals(this.includeAccounts, reviewingTargets.includeAccounts) &&
        Objects.equals(this.includeCategorizedOrphanAccts, reviewingTargets.includeCategorizedOrphanAccts) &&
        Objects.equals(this.includePermissions, reviewingTargets.includePermissions) &&
        Objects.equals(this.mAcctConjunction, reviewingTargets.mAcctConjunction) &&
        Objects.equals(this.mAcctCriteria, reviewingTargets.mAcctCriteria) &&
        Objects.equals(this.mApiVersion, reviewingTargets.mApiVersion) &&
        Objects.equals(this.mAppConjunction, reviewingTargets.mAppConjunction) &&
        Objects.equals(this.mAppCriteria, reviewingTargets.mAppCriteria) &&
        Objects.equals(this.mApplication, reviewingTargets.mApplication) &&
        Objects.equals(this.mAssignedRolesOnly, reviewingTargets.mAssignedRolesOnly) &&
        Objects.equals(this.mBRoleConjunction, reviewingTargets.mBRoleConjunction) &&
        Objects.equals(this.mBRoleCriteria, reviewingTargets.mBRoleCriteria) &&
        Objects.equals(this.mDescription, reviewingTargets.mDescription) &&
        Objects.equals(this.mGroupInRoles, reviewingTargets.mGroupInRoles) &&
        Objects.equals(this.mIncludeAccounts, reviewingTargets.mIncludeAccounts) &&
        Objects.equals(this.mIncludeCategorizedOrphanAccts, reviewingTargets.mIncludeCategorizedOrphanAccts) &&
        Objects.equals(this.mIncludePermissions, reviewingTargets.mIncludePermissions) &&
        Objects.equals(this.mOrphanAcctConjunction, reviewingTargets.mOrphanAcctConjunction) &&
        Objects.equals(this.mOrphanAcctCriteria, reviewingTargets.mOrphanAcctCriteria) &&
        Objects.equals(this.mPermConjunction, reviewingTargets.mPermConjunction) &&
        Objects.equals(this.mPermCriteria, reviewingTargets.mPermCriteria) &&
        Objects.equals(this.mReviewSubtype, reviewingTargets.mReviewSubtype) &&
        Objects.equals(this.mReviewType, reviewingTargets.mReviewType) &&
        Objects.equals(this.mRoleConjunction, reviewingTargets.mRoleConjunction) &&
        Objects.equals(this.mRoleCriteria, reviewingTargets.mRoleCriteria) &&
        Objects.equals(this.mSelectedAccts, reviewingTargets.mSelectedAccts) &&
        Objects.equals(this.mSelectedApps, reviewingTargets.mSelectedApps) &&
        Objects.equals(this.mSelectedBRoles, reviewingTargets.mSelectedBRoles) &&
        Objects.equals(this.mSelectedOrphanAccts, reviewingTargets.mSelectedOrphanAccts) &&
        Objects.equals(this.mSelectedPerms, reviewingTargets.mSelectedPerms) &&
        Objects.equals(this.mSelectedRoles, reviewingTargets.mSelectedRoles) &&
        Objects.equals(this.mSelectedUsers, reviewingTargets.mSelectedUsers) &&
        Objects.equals(this.mType, reviewingTargets.mType) &&
        Objects.equals(this.mUserConjunction, reviewingTargets.mUserConjunction) &&
        Objects.equals(this.mUserCriteria, reviewingTargets.mUserCriteria) &&
        Objects.equals(this.mUsersAre, reviewingTargets.mUsersAre) &&
        Objects.equals(this.mUsersAreDirectReports, reviewingTargets.mUsersAreDirectReports) &&
        Objects.equals(this.mUsersHaving, reviewingTargets.mUsersHaving) &&
        Objects.equals(this.orphanAcctConjunction, reviewingTargets.orphanAcctConjunction) &&
        Objects.equals(this.orphanAcctCriteria, reviewingTargets.orphanAcctCriteria) &&
        Objects.equals(this.permAssignmentCriteria, reviewingTargets.permAssignmentCriteria) &&
        Objects.equals(this.permConjunction, reviewingTargets.permConjunction) &&
        Objects.equals(this.permCriteria, reviewingTargets.permCriteria) &&
        Objects.equals(this.permOnlyCriteria, reviewingTargets.permOnlyCriteria) &&
        Objects.equals(this.reviewSubtype, reviewingTargets.reviewSubtype) &&
        Objects.equals(this.reviewType, reviewingTargets.reviewType) &&
        Objects.equals(this.roleConjunction, reviewingTargets.roleConjunction) &&
        Objects.equals(this.roleCriteria, reviewingTargets.roleCriteria) &&
        Objects.equals(this.selectedAccts, reviewingTargets.selectedAccts) &&
        Objects.equals(this.selectedApps, reviewingTargets.selectedApps) &&
        Objects.equals(this.selectedBRoles, reviewingTargets.selectedBRoles) &&
        Objects.equals(this.selectedOrphanAccts, reviewingTargets.selectedOrphanAccts) &&
        Objects.equals(this.selectedPerms, reviewingTargets.selectedPerms) &&
        Objects.equals(this.selectedRoles, reviewingTargets.selectedRoles) &&
        Objects.equals(this.selectedUsers, reviewingTargets.selectedUsers) &&
        Objects.equals(this.type, reviewingTargets.type) &&
        Objects.equals(this.userConjunction, reviewingTargets.userConjunction) &&
        Objects.equals(this.userCriteria, reviewingTargets.userCriteria) &&
        Objects.equals(this.usersAre, reviewingTargets.usersAre) &&
        Objects.equals(this.usersAreDirectReports, reviewingTargets.usersAreDirectReports) &&
        Objects.equals(this.usersHaving, reviewingTargets.usersHaving);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acctConjunction, acctCriteria, apiVersion, appConjunction, appCriteria, application, assignedRoles, bRoleConjunction, bRoleCriteria, description, groupInRoles, includeAccounts, includeCategorizedOrphanAccts, includePermissions, mAcctConjunction, mAcctCriteria, mApiVersion, mAppConjunction, mAppCriteria, mApplication, mAssignedRolesOnly, mBRoleConjunction, mBRoleCriteria, mDescription, mGroupInRoles, mIncludeAccounts, mIncludeCategorizedOrphanAccts, mIncludePermissions, mOrphanAcctConjunction, mOrphanAcctCriteria, mPermConjunction, mPermCriteria, mReviewSubtype, mReviewType, mRoleConjunction, mRoleCriteria, mSelectedAccts, mSelectedApps, mSelectedBRoles, mSelectedOrphanAccts, mSelectedPerms, mSelectedRoles, mSelectedUsers, mType, mUserConjunction, mUserCriteria, mUsersAre, mUsersAreDirectReports, mUsersHaving, orphanAcctConjunction, orphanAcctCriteria, permAssignmentCriteria, permConjunction, permCriteria, permOnlyCriteria, reviewSubtype, reviewType, roleConjunction, roleCriteria, selectedAccts, selectedApps, selectedBRoles, selectedOrphanAccts, selectedPerms, selectedRoles, selectedUsers, type, userConjunction, userCriteria, usersAre, usersAreDirectReports, usersHaving);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewingTargets {\n");
    
    sb.append("    acctConjunction: ").append(toIndentedString(acctConjunction)).append("\n");
    sb.append("    acctCriteria: ").append(toIndentedString(acctCriteria)).append("\n");
    sb.append("    apiVersion: ").append(toIndentedString(apiVersion)).append("\n");
    sb.append("    appConjunction: ").append(toIndentedString(appConjunction)).append("\n");
    sb.append("    appCriteria: ").append(toIndentedString(appCriteria)).append("\n");
    sb.append("    application: ").append(toIndentedString(application)).append("\n");
    sb.append("    assignedRoles: ").append(toIndentedString(assignedRoles)).append("\n");
    sb.append("    bRoleConjunction: ").append(toIndentedString(bRoleConjunction)).append("\n");
    sb.append("    bRoleCriteria: ").append(toIndentedString(bRoleCriteria)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    groupInRoles: ").append(toIndentedString(groupInRoles)).append("\n");
    sb.append("    includeAccounts: ").append(toIndentedString(includeAccounts)).append("\n");
    sb.append("    includeCategorizedOrphanAccts: ").append(toIndentedString(includeCategorizedOrphanAccts)).append("\n");
    sb.append("    includePermissions: ").append(toIndentedString(includePermissions)).append("\n");
    sb.append("    mAcctConjunction: ").append(toIndentedString(mAcctConjunction)).append("\n");
    sb.append("    mAcctCriteria: ").append(toIndentedString(mAcctCriteria)).append("\n");
    sb.append("    mApiVersion: ").append(toIndentedString(mApiVersion)).append("\n");
    sb.append("    mAppConjunction: ").append(toIndentedString(mAppConjunction)).append("\n");
    sb.append("    mAppCriteria: ").append(toIndentedString(mAppCriteria)).append("\n");
    sb.append("    mApplication: ").append(toIndentedString(mApplication)).append("\n");
    sb.append("    mAssignedRolesOnly: ").append(toIndentedString(mAssignedRolesOnly)).append("\n");
    sb.append("    mBRoleConjunction: ").append(toIndentedString(mBRoleConjunction)).append("\n");
    sb.append("    mBRoleCriteria: ").append(toIndentedString(mBRoleCriteria)).append("\n");
    sb.append("    mDescription: ").append(toIndentedString(mDescription)).append("\n");
    sb.append("    mGroupInRoles: ").append(toIndentedString(mGroupInRoles)).append("\n");
    sb.append("    mIncludeAccounts: ").append(toIndentedString(mIncludeAccounts)).append("\n");
    sb.append("    mIncludeCategorizedOrphanAccts: ").append(toIndentedString(mIncludeCategorizedOrphanAccts)).append("\n");
    sb.append("    mIncludePermissions: ").append(toIndentedString(mIncludePermissions)).append("\n");
    sb.append("    mOrphanAcctConjunction: ").append(toIndentedString(mOrphanAcctConjunction)).append("\n");
    sb.append("    mOrphanAcctCriteria: ").append(toIndentedString(mOrphanAcctCriteria)).append("\n");
    sb.append("    mPermConjunction: ").append(toIndentedString(mPermConjunction)).append("\n");
    sb.append("    mPermCriteria: ").append(toIndentedString(mPermCriteria)).append("\n");
    sb.append("    mReviewSubtype: ").append(toIndentedString(mReviewSubtype)).append("\n");
    sb.append("    mReviewType: ").append(toIndentedString(mReviewType)).append("\n");
    sb.append("    mRoleConjunction: ").append(toIndentedString(mRoleConjunction)).append("\n");
    sb.append("    mRoleCriteria: ").append(toIndentedString(mRoleCriteria)).append("\n");
    sb.append("    mSelectedAccts: ").append(toIndentedString(mSelectedAccts)).append("\n");
    sb.append("    mSelectedApps: ").append(toIndentedString(mSelectedApps)).append("\n");
    sb.append("    mSelectedBRoles: ").append(toIndentedString(mSelectedBRoles)).append("\n");
    sb.append("    mSelectedOrphanAccts: ").append(toIndentedString(mSelectedOrphanAccts)).append("\n");
    sb.append("    mSelectedPerms: ").append(toIndentedString(mSelectedPerms)).append("\n");
    sb.append("    mSelectedRoles: ").append(toIndentedString(mSelectedRoles)).append("\n");
    sb.append("    mSelectedUsers: ").append(toIndentedString(mSelectedUsers)).append("\n");
    sb.append("    mType: ").append(toIndentedString(mType)).append("\n");
    sb.append("    mUserConjunction: ").append(toIndentedString(mUserConjunction)).append("\n");
    sb.append("    mUserCriteria: ").append(toIndentedString(mUserCriteria)).append("\n");
    sb.append("    mUsersAre: ").append(toIndentedString(mUsersAre)).append("\n");
    sb.append("    mUsersAreDirectReports: ").append(toIndentedString(mUsersAreDirectReports)).append("\n");
    sb.append("    mUsersHaving: ").append(toIndentedString(mUsersHaving)).append("\n");
    sb.append("    orphanAcctConjunction: ").append(toIndentedString(orphanAcctConjunction)).append("\n");
    sb.append("    orphanAcctCriteria: ").append(toIndentedString(orphanAcctCriteria)).append("\n");
    sb.append("    permAssignmentCriteria: ").append(toIndentedString(permAssignmentCriteria)).append("\n");
    sb.append("    permConjunction: ").append(toIndentedString(permConjunction)).append("\n");
    sb.append("    permCriteria: ").append(toIndentedString(permCriteria)).append("\n");
    sb.append("    permOnlyCriteria: ").append(toIndentedString(permOnlyCriteria)).append("\n");
    sb.append("    reviewSubtype: ").append(toIndentedString(reviewSubtype)).append("\n");
    sb.append("    reviewType: ").append(toIndentedString(reviewType)).append("\n");
    sb.append("    roleConjunction: ").append(toIndentedString(roleConjunction)).append("\n");
    sb.append("    roleCriteria: ").append(toIndentedString(roleCriteria)).append("\n");
    sb.append("    selectedAccts: ").append(toIndentedString(selectedAccts)).append("\n");
    sb.append("    selectedApps: ").append(toIndentedString(selectedApps)).append("\n");
    sb.append("    selectedBRoles: ").append(toIndentedString(selectedBRoles)).append("\n");
    sb.append("    selectedOrphanAccts: ").append(toIndentedString(selectedOrphanAccts)).append("\n");
    sb.append("    selectedPerms: ").append(toIndentedString(selectedPerms)).append("\n");
    sb.append("    selectedRoles: ").append(toIndentedString(selectedRoles)).append("\n");
    sb.append("    selectedUsers: ").append(toIndentedString(selectedUsers)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    userConjunction: ").append(toIndentedString(userConjunction)).append("\n");
    sb.append("    userCriteria: ").append(toIndentedString(userCriteria)).append("\n");
    sb.append("    usersAre: ").append(toIndentedString(usersAre)).append("\n");
    sb.append("    usersAreDirectReports: ").append(toIndentedString(usersAreDirectReports)).append("\n");
    sb.append("    usersHaving: ").append(toIndentedString(usersHaving)).append("\n");
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
