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
import io.swagger.client.model.ContribItem;
import io.swagger.client.model.SodApprovalPolicy;
import java.io.IOException;
/**
 * SodViolCase
 */



public class SodViolCase {
  @SerializedName("allowApprove")
  private Boolean allowApprove = null;

  @SerializedName("contributingItem")
  private ContribItem contributingItem = null;

  @SerializedName("currentStep")
  private Long currentStep = null;

  @SerializedName("policyId")
  private Long policyId = null;

  @SerializedName("policyName")
  private String policyName = null;

  @SerializedName("sodApprovalPolicy")
  private SodApprovalPolicy sodApprovalPolicy = null;

  @SerializedName("totalSteps")
  private Long totalSteps = null;

  @SerializedName("toxic")
  private Boolean toxic = null;

  @SerializedName("uniquePolicyId")
  private String uniquePolicyId = null;

  @SerializedName("uniqueViolatorId")
  private String uniqueViolatorId = null;

  @SerializedName("violatorId")
  private Long violatorId = null;

  @SerializedName("violatorName")
  private String violatorName = null;

  public SodViolCase allowApprove(Boolean allowApprove) {
    this.allowApprove = allowApprove;
    return this;
  }

   /**
   * Get allowApprove
   * @return allowApprove
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowApprove() {
    return allowApprove;
  }

  public void setAllowApprove(Boolean allowApprove) {
    this.allowApprove = allowApprove;
  }

  public SodViolCase contributingItem(ContribItem contributingItem) {
    this.contributingItem = contributingItem;
    return this;
  }

   /**
   * Get contributingItem
   * @return contributingItem
  **/
  @ApiModelProperty(value = "")
  public ContribItem getContributingItem() {
    return contributingItem;
  }

  public void setContributingItem(ContribItem contributingItem) {
    this.contributingItem = contributingItem;
  }

  public SodViolCase currentStep(Long currentStep) {
    this.currentStep = currentStep;
    return this;
  }

   /**
   * Get currentStep
   * @return currentStep
  **/
  @ApiModelProperty(value = "")
  public Long getCurrentStep() {
    return currentStep;
  }

  public void setCurrentStep(Long currentStep) {
    this.currentStep = currentStep;
  }

  public SodViolCase policyId(Long policyId) {
    this.policyId = policyId;
    return this;
  }

   /**
   * Get policyId
   * @return policyId
  **/
  @ApiModelProperty(value = "")
  public Long getPolicyId() {
    return policyId;
  }

  public void setPolicyId(Long policyId) {
    this.policyId = policyId;
  }

  public SodViolCase policyName(String policyName) {
    this.policyName = policyName;
    return this;
  }

   /**
   * Get policyName
   * @return policyName
  **/
  @ApiModelProperty(value = "")
  public String getPolicyName() {
    return policyName;
  }

  public void setPolicyName(String policyName) {
    this.policyName = policyName;
  }

  public SodViolCase sodApprovalPolicy(SodApprovalPolicy sodApprovalPolicy) {
    this.sodApprovalPolicy = sodApprovalPolicy;
    return this;
  }

   /**
   * Get sodApprovalPolicy
   * @return sodApprovalPolicy
  **/
  @ApiModelProperty(value = "")
  public SodApprovalPolicy getSodApprovalPolicy() {
    return sodApprovalPolicy;
  }

  public void setSodApprovalPolicy(SodApprovalPolicy sodApprovalPolicy) {
    this.sodApprovalPolicy = sodApprovalPolicy;
  }

  public SodViolCase totalSteps(Long totalSteps) {
    this.totalSteps = totalSteps;
    return this;
  }

   /**
   * Get totalSteps
   * @return totalSteps
  **/
  @ApiModelProperty(value = "")
  public Long getTotalSteps() {
    return totalSteps;
  }

  public void setTotalSteps(Long totalSteps) {
    this.totalSteps = totalSteps;
  }

  public SodViolCase toxic(Boolean toxic) {
    this.toxic = toxic;
    return this;
  }

   /**
   * Get toxic
   * @return toxic
  **/
  @ApiModelProperty(value = "")
  public Boolean isToxic() {
    return toxic;
  }

  public void setToxic(Boolean toxic) {
    this.toxic = toxic;
  }

  public SodViolCase uniquePolicyId(String uniquePolicyId) {
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

  public SodViolCase uniqueViolatorId(String uniqueViolatorId) {
    this.uniqueViolatorId = uniqueViolatorId;
    return this;
  }

   /**
   * Get uniqueViolatorId
   * @return uniqueViolatorId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueViolatorId() {
    return uniqueViolatorId;
  }

  public void setUniqueViolatorId(String uniqueViolatorId) {
    this.uniqueViolatorId = uniqueViolatorId;
  }

  public SodViolCase violatorId(Long violatorId) {
    this.violatorId = violatorId;
    return this;
  }

   /**
   * Get violatorId
   * @return violatorId
  **/
  @ApiModelProperty(value = "")
  public Long getViolatorId() {
    return violatorId;
  }

  public void setViolatorId(Long violatorId) {
    this.violatorId = violatorId;
  }

  public SodViolCase violatorName(String violatorName) {
    this.violatorName = violatorName;
    return this;
  }

   /**
   * Get violatorName
   * @return violatorName
  **/
  @ApiModelProperty(value = "")
  public String getViolatorName() {
    return violatorName;
  }

  public void setViolatorName(String violatorName) {
    this.violatorName = violatorName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SodViolCase sodViolCase = (SodViolCase) o;
    return Objects.equals(this.allowApprove, sodViolCase.allowApprove) &&
        Objects.equals(this.contributingItem, sodViolCase.contributingItem) &&
        Objects.equals(this.currentStep, sodViolCase.currentStep) &&
        Objects.equals(this.policyId, sodViolCase.policyId) &&
        Objects.equals(this.policyName, sodViolCase.policyName) &&
        Objects.equals(this.sodApprovalPolicy, sodViolCase.sodApprovalPolicy) &&
        Objects.equals(this.totalSteps, sodViolCase.totalSteps) &&
        Objects.equals(this.toxic, sodViolCase.toxic) &&
        Objects.equals(this.uniquePolicyId, sodViolCase.uniquePolicyId) &&
        Objects.equals(this.uniqueViolatorId, sodViolCase.uniqueViolatorId) &&
        Objects.equals(this.violatorId, sodViolCase.violatorId) &&
        Objects.equals(this.violatorName, sodViolCase.violatorName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowApprove, contributingItem, currentStep, policyId, policyName, sodApprovalPolicy, totalSteps, toxic, uniquePolicyId, uniqueViolatorId, violatorId, violatorName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SodViolCase {\n");
    
    sb.append("    allowApprove: ").append(toIndentedString(allowApprove)).append("\n");
    sb.append("    contributingItem: ").append(toIndentedString(contributingItem)).append("\n");
    sb.append("    currentStep: ").append(toIndentedString(currentStep)).append("\n");
    sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
    sb.append("    policyName: ").append(toIndentedString(policyName)).append("\n");
    sb.append("    sodApprovalPolicy: ").append(toIndentedString(sodApprovalPolicy)).append("\n");
    sb.append("    totalSteps: ").append(toIndentedString(totalSteps)).append("\n");
    sb.append("    toxic: ").append(toIndentedString(toxic)).append("\n");
    sb.append("    uniquePolicyId: ").append(toIndentedString(uniquePolicyId)).append("\n");
    sb.append("    uniqueViolatorId: ").append(toIndentedString(uniqueViolatorId)).append("\n");
    sb.append("    violatorId: ").append(toIndentedString(violatorId)).append("\n");
    sb.append("    violatorName: ").append(toIndentedString(violatorName)).append("\n");
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
