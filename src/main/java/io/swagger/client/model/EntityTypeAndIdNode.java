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
 * EntityTypeAndIdNode
 */



public class EntityTypeAndIdNode {
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

  @SerializedName("id")
  private Long id = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  public EntityTypeAndIdNode entityType(EntityTypeEnum entityType) {
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

  public EntityTypeAndIdNode id(Long id) {
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

  public EntityTypeAndIdNode uniqueId(String uniqueId) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntityTypeAndIdNode entityTypeAndIdNode = (EntityTypeAndIdNode) o;
    return Objects.equals(this.entityType, entityTypeAndIdNode.entityType) &&
        Objects.equals(this.id, entityTypeAndIdNode.id) &&
        Objects.equals(this.uniqueId, entityTypeAndIdNode.uniqueId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityType, id, uniqueId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntityTypeAndIdNode {\n");
    
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
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
