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
import io.swagger.client.model.SodApprovalStep;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * SodApprovalPolicy
 */



public class SodApprovalPolicy {
  @SerializedName("approvalSteps")
  private List<SodApprovalStep> approvalSteps = null;

  @SerializedName("approvalStepsCount")
  private Long approvalStepsCount = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("changedBy")
  private User changedBy = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("defaultPolicy")
  private Boolean defaultPolicy = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("toxicAccountCondition")
  private String toxicAccountCondition = null;

  /**
   * Gets or Sets toxicAccountConditionType
   */
  @JsonAdapter(ToxicAccountConditionTypeEnum.Adapter.class)
  public enum ToxicAccountConditionTypeEnum {
    @SerializedName("NONE")
    NONE("NONE"),
    @SerializedName("AUTO_DENY")
    AUTO_DENY("AUTO_DENY"),
    @SerializedName("EXPRESSION")
    EXPRESSION("EXPRESSION");

    private String value;

    ToxicAccountConditionTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ToxicAccountConditionTypeEnum fromValue(String input) {
      for (ToxicAccountConditionTypeEnum b : ToxicAccountConditionTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ToxicAccountConditionTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ToxicAccountConditionTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ToxicAccountConditionTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ToxicAccountConditionTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("toxicAccountConditionType")
  private ToxicAccountConditionTypeEnum toxicAccountConditionType = null;

  @SerializedName("toxicUserCondition")
  private String toxicUserCondition = null;

  /**
   * Gets or Sets toxicUserConditionType
   */
  @JsonAdapter(ToxicUserConditionTypeEnum.Adapter.class)
  public enum ToxicUserConditionTypeEnum {
    @SerializedName("NONE")
    NONE("NONE"),
    @SerializedName("AUTO_DENY")
    AUTO_DENY("AUTO_DENY"),
    @SerializedName("EXPRESSION")
    EXPRESSION("EXPRESSION");

    private String value;

    ToxicUserConditionTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ToxicUserConditionTypeEnum fromValue(String input) {
      for (ToxicUserConditionTypeEnum b : ToxicUserConditionTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ToxicUserConditionTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ToxicUserConditionTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ToxicUserConditionTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ToxicUserConditionTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("toxicUserConditionType")
  private ToxicUserConditionTypeEnum toxicUserConditionType = null;

  @SerializedName("uniquePolicyId")
  private String uniquePolicyId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  public SodApprovalPolicy approvalSteps(List<SodApprovalStep> approvalSteps) {
    this.approvalSteps = approvalSteps;
    return this;
  }

  public SodApprovalPolicy addApprovalStepsItem(SodApprovalStep approvalStepsItem) {
    if (this.approvalSteps == null) {
      this.approvalSteps = new ArrayList<SodApprovalStep>();
    }
    this.approvalSteps.add(approvalStepsItem);
    return this;
  }

   /**
   * Get approvalSteps
   * @return approvalSteps
  **/
  @ApiModelProperty(value = "")
  public List<SodApprovalStep> getApprovalSteps() {
    return approvalSteps;
  }

  public void setApprovalSteps(List<SodApprovalStep> approvalSteps) {
    this.approvalSteps = approvalSteps;
  }

  public SodApprovalPolicy approvalStepsCount(Long approvalStepsCount) {
    this.approvalStepsCount = approvalStepsCount;
    return this;
  }

   /**
   * Get approvalStepsCount
   * @return approvalStepsCount
  **/
  @ApiModelProperty(value = "")
  public Long getApprovalStepsCount() {
    return approvalStepsCount;
  }

  public void setApprovalStepsCount(Long approvalStepsCount) {
    this.approvalStepsCount = approvalStepsCount;
  }

  public SodApprovalPolicy canEditEntity(Boolean canEditEntity) {
    this.canEditEntity = canEditEntity;
    return this;
  }

   /**
   * Get canEditEntity
   * @return canEditEntity
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanEditEntity() {
    return canEditEntity;
  }

  public void setCanEditEntity(Boolean canEditEntity) {
    this.canEditEntity = canEditEntity;
  }

  public SodApprovalPolicy changedBy(User changedBy) {
    this.changedBy = changedBy;
    return this;
  }

   /**
   * Get changedBy
   * @return changedBy
  **/
  @ApiModelProperty(value = "")
  public User getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(User changedBy) {
    this.changedBy = changedBy;
  }

  public SodApprovalPolicy createdBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Get createdBy
   * @return createdBy
  **/
  @ApiModelProperty(value = "")
  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public SodApprovalPolicy creationTime(Long creationTime) {
    this.creationTime = creationTime;
    return this;
  }

   /**
   * Get creationTime
   * @return creationTime
  **/
  @ApiModelProperty(value = "")
  public Long getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Long creationTime) {
    this.creationTime = creationTime;
  }

  public SodApprovalPolicy defaultPolicy(Boolean defaultPolicy) {
    this.defaultPolicy = defaultPolicy;
    return this;
  }

   /**
   * Get defaultPolicy
   * @return defaultPolicy
  **/
  @ApiModelProperty(value = "")
  public Boolean isDefaultPolicy() {
    return defaultPolicy;
  }

  public void setDefaultPolicy(Boolean defaultPolicy) {
    this.defaultPolicy = defaultPolicy;
  }

  public SodApprovalPolicy description(String description) {
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

  public SodApprovalPolicy id(Long id) {
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

  public SodApprovalPolicy name(String name) {
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

  public SodApprovalPolicy toxicAccountCondition(String toxicAccountCondition) {
    this.toxicAccountCondition = toxicAccountCondition;
    return this;
  }

   /**
   * Get toxicAccountCondition
   * @return toxicAccountCondition
  **/
  @ApiModelProperty(value = "")
  public String getToxicAccountCondition() {
    return toxicAccountCondition;
  }

  public void setToxicAccountCondition(String toxicAccountCondition) {
    this.toxicAccountCondition = toxicAccountCondition;
  }

  public SodApprovalPolicy toxicAccountConditionType(ToxicAccountConditionTypeEnum toxicAccountConditionType) {
    this.toxicAccountConditionType = toxicAccountConditionType;
    return this;
  }

   /**
   * Get toxicAccountConditionType
   * @return toxicAccountConditionType
  **/
  @ApiModelProperty(value = "")
  public ToxicAccountConditionTypeEnum getToxicAccountConditionType() {
    return toxicAccountConditionType;
  }

  public void setToxicAccountConditionType(ToxicAccountConditionTypeEnum toxicAccountConditionType) {
    this.toxicAccountConditionType = toxicAccountConditionType;
  }

  public SodApprovalPolicy toxicUserCondition(String toxicUserCondition) {
    this.toxicUserCondition = toxicUserCondition;
    return this;
  }

   /**
   * Get toxicUserCondition
   * @return toxicUserCondition
  **/
  @ApiModelProperty(value = "")
  public String getToxicUserCondition() {
    return toxicUserCondition;
  }

  public void setToxicUserCondition(String toxicUserCondition) {
    this.toxicUserCondition = toxicUserCondition;
  }

  public SodApprovalPolicy toxicUserConditionType(ToxicUserConditionTypeEnum toxicUserConditionType) {
    this.toxicUserConditionType = toxicUserConditionType;
    return this;
  }

   /**
   * Get toxicUserConditionType
   * @return toxicUserConditionType
  **/
  @ApiModelProperty(value = "")
  public ToxicUserConditionTypeEnum getToxicUserConditionType() {
    return toxicUserConditionType;
  }

  public void setToxicUserConditionType(ToxicUserConditionTypeEnum toxicUserConditionType) {
    this.toxicUserConditionType = toxicUserConditionType;
  }

  public SodApprovalPolicy uniquePolicyId(String uniquePolicyId) {
    this.uniquePolicyId = uniquePolicyId;
    return this;
  }

   /**
   * Get uniquePolicyId
   * @return uniquePolicyId
  **/
  @ApiModelProperty(value = "")
  public String getUniquePolicyId() {
    return uniquePolicyId;
  }

  public void setUniquePolicyId(String uniquePolicyId) {
    this.uniquePolicyId = uniquePolicyId;
  }

  public SodApprovalPolicy updateTime(Long updateTime) {
    this.updateTime = updateTime;
    return this;
  }

   /**
   * Get updateTime
   * @return updateTime
  **/
  @ApiModelProperty(value = "")
  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SodApprovalPolicy sodApprovalPolicy = (SodApprovalPolicy) o;
    return Objects.equals(this.approvalSteps, sodApprovalPolicy.approvalSteps) &&
        Objects.equals(this.approvalStepsCount, sodApprovalPolicy.approvalStepsCount) &&
        Objects.equals(this.canEditEntity, sodApprovalPolicy.canEditEntity) &&
        Objects.equals(this.changedBy, sodApprovalPolicy.changedBy) &&
        Objects.equals(this.createdBy, sodApprovalPolicy.createdBy) &&
        Objects.equals(this.creationTime, sodApprovalPolicy.creationTime) &&
        Objects.equals(this.defaultPolicy, sodApprovalPolicy.defaultPolicy) &&
        Objects.equals(this.description, sodApprovalPolicy.description) &&
        Objects.equals(this.id, sodApprovalPolicy.id) &&
        Objects.equals(this.name, sodApprovalPolicy.name) &&
        Objects.equals(this.toxicAccountCondition, sodApprovalPolicy.toxicAccountCondition) &&
        Objects.equals(this.toxicAccountConditionType, sodApprovalPolicy.toxicAccountConditionType) &&
        Objects.equals(this.toxicUserCondition, sodApprovalPolicy.toxicUserCondition) &&
        Objects.equals(this.toxicUserConditionType, sodApprovalPolicy.toxicUserConditionType) &&
        Objects.equals(this.uniquePolicyId, sodApprovalPolicy.uniquePolicyId) &&
        Objects.equals(this.updateTime, sodApprovalPolicy.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvalSteps, approvalStepsCount, canEditEntity, changedBy, createdBy, creationTime, defaultPolicy, description, id, name, toxicAccountCondition, toxicAccountConditionType, toxicUserCondition, toxicUserConditionType, uniquePolicyId, updateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SodApprovalPolicy {\n");
    
    sb.append("    approvalSteps: ").append(toIndentedString(approvalSteps)).append("\n");
    sb.append("    approvalStepsCount: ").append(toIndentedString(approvalStepsCount)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    defaultPolicy: ").append(toIndentedString(defaultPolicy)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    toxicAccountCondition: ").append(toIndentedString(toxicAccountCondition)).append("\n");
    sb.append("    toxicAccountConditionType: ").append(toIndentedString(toxicAccountConditionType)).append("\n");
    sb.append("    toxicUserCondition: ").append(toIndentedString(toxicUserCondition)).append("\n");
    sb.append("    toxicUserConditionType: ").append(toIndentedString(toxicUserConditionType)).append("\n");
    sb.append("    uniquePolicyId: ").append(toIndentedString(uniquePolicyId)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
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
