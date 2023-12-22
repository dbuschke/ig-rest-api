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
import io.swagger.client.model.AccessRequestApprovalPolicy;
import io.swagger.client.model.ReviewDef;
import io.swagger.client.model.Statement;
import io.swagger.client.model.Status;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Coverage
 */



public class Coverage {
  @SerializedName("accessReqApprovPolicies")
  private List<AccessRequestApprovalPolicy> accessReqApprovPolicies = null;

  @SerializedName("canEditEntity")
  private Boolean canEditEntity = null;

  @SerializedName("canViewEntity")
  private Boolean canViewEntity = null;

  @SerializedName("coverageMapId")
  private String coverageMapId = null;

  @SerializedName("createdBy")
  private User createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("modificationDate")
  private Long modificationDate = null;

  @SerializedName("modifier")
  private User modifier = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("numStatements")
  private Long numStatements = null;

  @SerializedName("optimizeOutput")
  private Status optimizeOutput = null;

  @SerializedName("reviewDefinitions")
  private List<ReviewDef> reviewDefinitions = null;

  @SerializedName("statementContent")
  private String statementContent = null;

  @SerializedName("statements")
  private List<Statement> statements = null;

  @SerializedName("type")
  private String type = null;

  public Coverage accessReqApprovPolicies(List<AccessRequestApprovalPolicy> accessReqApprovPolicies) {
    this.accessReqApprovPolicies = accessReqApprovPolicies;
    return this;
  }

  public Coverage addAccessReqApprovPoliciesItem(AccessRequestApprovalPolicy accessReqApprovPoliciesItem) {
    if (this.accessReqApprovPolicies == null) {
      this.accessReqApprovPolicies = new ArrayList<AccessRequestApprovalPolicy>();
    }
    this.accessReqApprovPolicies.add(accessReqApprovPoliciesItem);
    return this;
  }

   /**
   * Get accessReqApprovPolicies
   * @return accessReqApprovPolicies
  **/
  @ApiModelProperty(value = "")
  public List<AccessRequestApprovalPolicy> getAccessReqApprovPolicies() {
    return accessReqApprovPolicies;
  }

  public void setAccessReqApprovPolicies(List<AccessRequestApprovalPolicy> accessReqApprovPolicies) {
    this.accessReqApprovPolicies = accessReqApprovPolicies;
  }

  public Coverage canEditEntity(Boolean canEditEntity) {
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

  public Coverage canViewEntity(Boolean canViewEntity) {
    this.canViewEntity = canViewEntity;
    return this;
  }

   /**
   * Get canViewEntity
   * @return canViewEntity
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanViewEntity() {
    return canViewEntity;
  }

  public void setCanViewEntity(Boolean canViewEntity) {
    this.canViewEntity = canViewEntity;
  }

  public Coverage coverageMapId(String coverageMapId) {
    this.coverageMapId = coverageMapId;
    return this;
  }

   /**
   * Get coverageMapId
   * @return coverageMapId
  **/
  @ApiModelProperty(value = "")
  public String getCoverageMapId() {
    return coverageMapId;
  }

  public void setCoverageMapId(String coverageMapId) {
    this.coverageMapId = coverageMapId;
  }

  public Coverage createdBy(User createdBy) {
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

  public Coverage creationTime(Long creationTime) {
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

  public Coverage description(String description) {
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

  public Coverage link(String link) {
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

  public Coverage modificationDate(Long modificationDate) {
    this.modificationDate = modificationDate;
    return this;
  }

   /**
   * Get modificationDate
   * @return modificationDate
  **/
  @ApiModelProperty(value = "")
  public Long getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(Long modificationDate) {
    this.modificationDate = modificationDate;
  }

  public Coverage modifier(User modifier) {
    this.modifier = modifier;
    return this;
  }

   /**
   * Get modifier
   * @return modifier
  **/
  @ApiModelProperty(value = "")
  public User getModifier() {
    return modifier;
  }

  public void setModifier(User modifier) {
    this.modifier = modifier;
  }

  public Coverage name(String name) {
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

  public Coverage numStatements(Long numStatements) {
    this.numStatements = numStatements;
    return this;
  }

   /**
   * Get numStatements
   * @return numStatements
  **/
  @ApiModelProperty(value = "")
  public Long getNumStatements() {
    return numStatements;
  }

  public void setNumStatements(Long numStatements) {
    this.numStatements = numStatements;
  }

  public Coverage optimizeOutput(Status optimizeOutput) {
    this.optimizeOutput = optimizeOutput;
    return this;
  }

   /**
   * Get optimizeOutput
   * @return optimizeOutput
  **/
  @ApiModelProperty(value = "")
  public Status getOptimizeOutput() {
    return optimizeOutput;
  }

  public void setOptimizeOutput(Status optimizeOutput) {
    this.optimizeOutput = optimizeOutput;
  }

  public Coverage reviewDefinitions(List<ReviewDef> reviewDefinitions) {
    this.reviewDefinitions = reviewDefinitions;
    return this;
  }

  public Coverage addReviewDefinitionsItem(ReviewDef reviewDefinitionsItem) {
    if (this.reviewDefinitions == null) {
      this.reviewDefinitions = new ArrayList<ReviewDef>();
    }
    this.reviewDefinitions.add(reviewDefinitionsItem);
    return this;
  }

   /**
   * Get reviewDefinitions
   * @return reviewDefinitions
  **/
  @ApiModelProperty(value = "")
  public List<ReviewDef> getReviewDefinitions() {
    return reviewDefinitions;
  }

  public void setReviewDefinitions(List<ReviewDef> reviewDefinitions) {
    this.reviewDefinitions = reviewDefinitions;
  }

  public Coverage statementContent(String statementContent) {
    this.statementContent = statementContent;
    return this;
  }

   /**
   * Get statementContent
   * @return statementContent
  **/
  @ApiModelProperty(value = "")
  public String getStatementContent() {
    return statementContent;
  }

  public void setStatementContent(String statementContent) {
    this.statementContent = statementContent;
  }

  public Coverage statements(List<Statement> statements) {
    this.statements = statements;
    return this;
  }

  public Coverage addStatementsItem(Statement statementsItem) {
    if (this.statements == null) {
      this.statements = new ArrayList<Statement>();
    }
    this.statements.add(statementsItem);
    return this;
  }

   /**
   * Get statements
   * @return statements
  **/
  @ApiModelProperty(value = "")
  public List<Statement> getStatements() {
    return statements;
  }

  public void setStatements(List<Statement> statements) {
    this.statements = statements;
  }

  public Coverage type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coverage coverage = (Coverage) o;
    return Objects.equals(this.accessReqApprovPolicies, coverage.accessReqApprovPolicies) &&
        Objects.equals(this.canEditEntity, coverage.canEditEntity) &&
        Objects.equals(this.canViewEntity, coverage.canViewEntity) &&
        Objects.equals(this.coverageMapId, coverage.coverageMapId) &&
        Objects.equals(this.createdBy, coverage.createdBy) &&
        Objects.equals(this.creationTime, coverage.creationTime) &&
        Objects.equals(this.description, coverage.description) &&
        Objects.equals(this.link, coverage.link) &&
        Objects.equals(this.modificationDate, coverage.modificationDate) &&
        Objects.equals(this.modifier, coverage.modifier) &&
        Objects.equals(this.name, coverage.name) &&
        Objects.equals(this.numStatements, coverage.numStatements) &&
        Objects.equals(this.optimizeOutput, coverage.optimizeOutput) &&
        Objects.equals(this.reviewDefinitions, coverage.reviewDefinitions) &&
        Objects.equals(this.statementContent, coverage.statementContent) &&
        Objects.equals(this.statements, coverage.statements) &&
        Objects.equals(this.type, coverage.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessReqApprovPolicies, canEditEntity, canViewEntity, coverageMapId, createdBy, creationTime, description, link, modificationDate, modifier, name, numStatements, optimizeOutput, reviewDefinitions, statementContent, statements, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Coverage {\n");
    
    sb.append("    accessReqApprovPolicies: ").append(toIndentedString(accessReqApprovPolicies)).append("\n");
    sb.append("    canEditEntity: ").append(toIndentedString(canEditEntity)).append("\n");
    sb.append("    canViewEntity: ").append(toIndentedString(canViewEntity)).append("\n");
    sb.append("    coverageMapId: ").append(toIndentedString(coverageMapId)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    modificationDate: ").append(toIndentedString(modificationDate)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    numStatements: ").append(toIndentedString(numStatements)).append("\n");
    sb.append("    optimizeOutput: ").append(toIndentedString(optimizeOutput)).append("\n");
    sb.append("    reviewDefinitions: ").append(toIndentedString(reviewDefinitions)).append("\n");
    sb.append("    statementContent: ").append(toIndentedString(statementContent)).append("\n");
    sb.append("    statements: ").append(toIndentedString(statements)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
