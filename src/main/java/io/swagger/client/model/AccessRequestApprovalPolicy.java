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
import io.swagger.client.model.AccessApprovalStep;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AccessRequestApprovalPolicy
 */



public class AccessRequestApprovalPolicy {
  @SerializedName("approvalCondition")
  private String approvalCondition = null;

  /**
   * Gets or Sets approvalConditionReq
   */
  @JsonAdapter(ApprovalConditionReqEnum.Adapter.class)
  public enum ApprovalConditionReqEnum {
    @SerializedName("NO_REQUESTS")
    NO_REQUESTS("NO_REQUESTS"),
    @SerializedName("GRANT_REQUESTS")
    GRANT_REQUESTS("GRANT_REQUESTS"),
    @SerializedName("REVOKE_REQUESTS")
    REVOKE_REQUESTS("REVOKE_REQUESTS"),
    @SerializedName("ALL_REQUESTS")
    ALL_REQUESTS("ALL_REQUESTS");

    private String value;

    ApprovalConditionReqEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ApprovalConditionReqEnum fromValue(String input) {
      for (ApprovalConditionReqEnum b : ApprovalConditionReqEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ApprovalConditionReqEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ApprovalConditionReqEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ApprovalConditionReqEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ApprovalConditionReqEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("approvalConditionReq")
  private ApprovalConditionReqEnum approvalConditionReq = null;

  @SerializedName("approvalSteps")
  private List<AccessApprovalStep> approvalSteps = null;

  @SerializedName("approvalStepsCount")
  private Long approvalStepsCount = null;

  @SerializedName("autoApproveAuthItems")
  private Boolean autoApproveAuthItems = null;

  /**
   * Gets or Sets autoApproveAuthItemsReq
   */
  @JsonAdapter(AutoApproveAuthItemsReqEnum.Adapter.class)
  public enum AutoApproveAuthItemsReqEnum {
    @SerializedName("NO_REQUESTS")
    NO_REQUESTS("NO_REQUESTS"),
    @SerializedName("GRANT_REQUESTS")
    GRANT_REQUESTS("GRANT_REQUESTS"),
    @SerializedName("REVOKE_REQUESTS")
    REVOKE_REQUESTS("REVOKE_REQUESTS"),
    @SerializedName("ALL_REQUESTS")
    ALL_REQUESTS("ALL_REQUESTS");

    private String value;

    AutoApproveAuthItemsReqEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static AutoApproveAuthItemsReqEnum fromValue(String input) {
      for (AutoApproveAuthItemsReqEnum b : AutoApproveAuthItemsReqEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<AutoApproveAuthItemsReqEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AutoApproveAuthItemsReqEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public AutoApproveAuthItemsReqEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return AutoApproveAuthItemsReqEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("autoApproveAuthItemsReq")
  private AutoApproveAuthItemsReqEnum autoApproveAuthItemsReq = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("changedBy")
  private User changedBy = null;

  @SerializedName("commentRequired")
  private Boolean commentRequired = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("defaultPolicy")
  private Boolean defaultPolicy = null;

  @SerializedName("denialCondition")
  private String denialCondition = null;

  /**
   * Gets or Sets denialConditionReq
   */
  @JsonAdapter(DenialConditionReqEnum.Adapter.class)
  public enum DenialConditionReqEnum {
    @SerializedName("NO_REQUESTS")
    NO_REQUESTS("NO_REQUESTS"),
    @SerializedName("GRANT_REQUESTS")
    GRANT_REQUESTS("GRANT_REQUESTS"),
    @SerializedName("REVOKE_REQUESTS")
    REVOKE_REQUESTS("REVOKE_REQUESTS"),
    @SerializedName("ALL_REQUESTS")
    ALL_REQUESTS("ALL_REQUESTS");

    private String value;

    DenialConditionReqEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DenialConditionReqEnum fromValue(String input) {
      for (DenialConditionReqEnum b : DenialConditionReqEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DenialConditionReqEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DenialConditionReqEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DenialConditionReqEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DenialConditionReqEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("denialConditionReq")
  private DenialConditionReqEnum denialConditionReq = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("uniquePolicyId")
  private String uniquePolicyId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  public AccessRequestApprovalPolicy approvalCondition(String approvalCondition) {
    this.approvalCondition = approvalCondition;
    return this;
  }

   /**
   * Get approvalCondition
   * @return approvalCondition
  **/
  @ApiModelProperty(value = "")
  public String getApprovalCondition() {
    return approvalCondition;
  }

  public void setApprovalCondition(String approvalCondition) {
    this.approvalCondition = approvalCondition;
  }

  public AccessRequestApprovalPolicy approvalConditionReq(ApprovalConditionReqEnum approvalConditionReq) {
    this.approvalConditionReq = approvalConditionReq;
    return this;
  }

   /**
   * Get approvalConditionReq
   * @return approvalConditionReq
  **/
  @ApiModelProperty(value = "")
  public ApprovalConditionReqEnum getApprovalConditionReq() {
    return approvalConditionReq;
  }

  public void setApprovalConditionReq(ApprovalConditionReqEnum approvalConditionReq) {
    this.approvalConditionReq = approvalConditionReq;
  }

  public AccessRequestApprovalPolicy approvalSteps(List<AccessApprovalStep> approvalSteps) {
    this.approvalSteps = approvalSteps;
    return this;
  }

  public AccessRequestApprovalPolicy addApprovalStepsItem(AccessApprovalStep approvalStepsItem) {
    if (this.approvalSteps == null) {
      this.approvalSteps = new ArrayList<AccessApprovalStep>();
    }
    this.approvalSteps.add(approvalStepsItem);
    return this;
  }

   /**
   * Get approvalSteps
   * @return approvalSteps
  **/
  @ApiModelProperty(value = "")
  public List<AccessApprovalStep> getApprovalSteps() {
    return approvalSteps;
  }

  public void setApprovalSteps(List<AccessApprovalStep> approvalSteps) {
    this.approvalSteps = approvalSteps;
  }

  public AccessRequestApprovalPolicy approvalStepsCount(Long approvalStepsCount) {
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

  public AccessRequestApprovalPolicy autoApproveAuthItems(Boolean autoApproveAuthItems) {
    this.autoApproveAuthItems = autoApproveAuthItems;
    return this;
  }

   /**
   * Get autoApproveAuthItems
   * @return autoApproveAuthItems
  **/
  @ApiModelProperty(value = "")
  public Boolean isAutoApproveAuthItems() {
    return autoApproveAuthItems;
  }

  public void setAutoApproveAuthItems(Boolean autoApproveAuthItems) {
    this.autoApproveAuthItems = autoApproveAuthItems;
  }

  public AccessRequestApprovalPolicy autoApproveAuthItemsReq(AutoApproveAuthItemsReqEnum autoApproveAuthItemsReq) {
    this.autoApproveAuthItemsReq = autoApproveAuthItemsReq;
    return this;
  }

   /**
   * Get autoApproveAuthItemsReq
   * @return autoApproveAuthItemsReq
  **/
  @ApiModelProperty(value = "")
  public AutoApproveAuthItemsReqEnum getAutoApproveAuthItemsReq() {
    return autoApproveAuthItemsReq;
  }

  public void setAutoApproveAuthItemsReq(AutoApproveAuthItemsReqEnum autoApproveAuthItemsReq) {
    this.autoApproveAuthItemsReq = autoApproveAuthItemsReq;
  }

  public AccessRequestApprovalPolicy canEditEntity(Boolean canEditEntity) {
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

  public AccessRequestApprovalPolicy changedBy(User changedBy) {
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

  public AccessRequestApprovalPolicy commentRequired(Boolean commentRequired) {
    this.commentRequired = commentRequired;
    return this;
  }

   /**
   * Get commentRequired
   * @return commentRequired
  **/
  @ApiModelProperty(value = "")
  public Boolean isCommentRequired() {
    return commentRequired;
  }

  public void setCommentRequired(Boolean commentRequired) {
    this.commentRequired = commentRequired;
  }

  public AccessRequestApprovalPolicy createdBy(User createdBy) {
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

  public AccessRequestApprovalPolicy creationTime(Long creationTime) {
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

  public AccessRequestApprovalPolicy defaultPolicy(Boolean defaultPolicy) {
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

  public AccessRequestApprovalPolicy denialCondition(String denialCondition) {
    this.denialCondition = denialCondition;
    return this;
  }

   /**
   * Get denialCondition
   * @return denialCondition
  **/
  @ApiModelProperty(value = "")
  public String getDenialCondition() {
    return denialCondition;
  }

  public void setDenialCondition(String denialCondition) {
    this.denialCondition = denialCondition;
  }

  public AccessRequestApprovalPolicy denialConditionReq(DenialConditionReqEnum denialConditionReq) {
    this.denialConditionReq = denialConditionReq;
    return this;
  }

   /**
   * Get denialConditionReq
   * @return denialConditionReq
  **/
  @ApiModelProperty(value = "")
  public DenialConditionReqEnum getDenialConditionReq() {
    return denialConditionReq;
  }

  public void setDenialConditionReq(DenialConditionReqEnum denialConditionReq) {
    this.denialConditionReq = denialConditionReq;
  }

  public AccessRequestApprovalPolicy description(String description) {
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

  public AccessRequestApprovalPolicy id(Long id) {
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

  public AccessRequestApprovalPolicy name(String name) {
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

  public AccessRequestApprovalPolicy uniquePolicyId(String uniquePolicyId) {
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

  public AccessRequestApprovalPolicy updateTime(Long updateTime) {
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
    AccessRequestApprovalPolicy accessRequestApprovalPolicy = (AccessRequestApprovalPolicy) o;
    return Objects.equals(this.approvalCondition, accessRequestApprovalPolicy.approvalCondition) &&
        Objects.equals(this.approvalConditionReq, accessRequestApprovalPolicy.approvalConditionReq) &&
        Objects.equals(this.approvalSteps, accessRequestApprovalPolicy.approvalSteps) &&
        Objects.equals(this.approvalStepsCount, accessRequestApprovalPolicy.approvalStepsCount) &&
        Objects.equals(this.autoApproveAuthItems, accessRequestApprovalPolicy.autoApproveAuthItems) &&
        Objects.equals(this.autoApproveAuthItemsReq, accessRequestApprovalPolicy.autoApproveAuthItemsReq) &&
        Objects.equals(this.canEditEntity, accessRequestApprovalPolicy.canEditEntity) &&
        Objects.equals(this.changedBy, accessRequestApprovalPolicy.changedBy) &&
        Objects.equals(this.commentRequired, accessRequestApprovalPolicy.commentRequired) &&
        Objects.equals(this.createdBy, accessRequestApprovalPolicy.createdBy) &&
        Objects.equals(this.creationTime, accessRequestApprovalPolicy.creationTime) &&
        Objects.equals(this.defaultPolicy, accessRequestApprovalPolicy.defaultPolicy) &&
        Objects.equals(this.denialCondition, accessRequestApprovalPolicy.denialCondition) &&
        Objects.equals(this.denialConditionReq, accessRequestApprovalPolicy.denialConditionReq) &&
        Objects.equals(this.description, accessRequestApprovalPolicy.description) &&
        Objects.equals(this.id, accessRequestApprovalPolicy.id) &&
        Objects.equals(this.name, accessRequestApprovalPolicy.name) &&
        Objects.equals(this.uniquePolicyId, accessRequestApprovalPolicy.uniquePolicyId) &&
        Objects.equals(this.updateTime, accessRequestApprovalPolicy.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvalCondition, approvalConditionReq, approvalSteps, approvalStepsCount, autoApproveAuthItems, autoApproveAuthItemsReq, canEditEntity, changedBy, commentRequired, createdBy, creationTime, defaultPolicy, denialCondition, denialConditionReq, description, id, name, uniquePolicyId, updateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccessRequestApprovalPolicy {\n");
    
    sb.append("    approvalCondition: ").append(toIndentedString(approvalCondition)).append("\n");
    sb.append("    approvalConditionReq: ").append(toIndentedString(approvalConditionReq)).append("\n");
    sb.append("    approvalSteps: ").append(toIndentedString(approvalSteps)).append("\n");
    sb.append("    approvalStepsCount: ").append(toIndentedString(approvalStepsCount)).append("\n");
    sb.append("    autoApproveAuthItems: ").append(toIndentedString(autoApproveAuthItems)).append("\n");
    sb.append("    autoApproveAuthItemsReq: ").append(toIndentedString(autoApproveAuthItemsReq)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    commentRequired: ").append(toIndentedString(commentRequired)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    defaultPolicy: ").append(toIndentedString(defaultPolicy)).append("\n");
    sb.append("    denialCondition: ").append(toIndentedString(denialCondition)).append("\n");
    sb.append("    denialConditionReq: ").append(toIndentedString(denialConditionReq)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
