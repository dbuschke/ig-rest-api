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
import java.util.ArrayList;
import java.util.List;
/**
 * Mine
 */



public class Mine {
  @SerializedName("appThreshold")
  private Integer appThreshold = null;

  @SerializedName("attributes")
  private List<String> attributes = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("includeAuthorizations")
  private Boolean includeAuthorizations = null;

  @SerializedName("mode")
  private String mode = null;

  @SerializedName("occurs")
  private Integer occurs = null;

  @SerializedName("permThreshold")
  private Integer permThreshold = null;

  @SerializedName("roleThreshold")
  private Integer roleThreshold = null;

  @SerializedName("setCount")
  private Integer setCount = null;

  @SerializedName("setSize")
  private Integer setSize = null;

  @SerializedName("topN")
  private Integer topN = null;

  public Mine appThreshold(Integer appThreshold) {
    this.appThreshold = appThreshold;
    return this;
  }

   /**
   * Get appThreshold
   * @return appThreshold
  **/
  @ApiModelProperty(value = "")
  public Integer getAppThreshold() {
    return appThreshold;
  }

  public void setAppThreshold(Integer appThreshold) {
    this.appThreshold = appThreshold;
  }

  public Mine attributes(List<String> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Mine addAttributesItem(String attributesItem) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<String>();
    }
    this.attributes.add(attributesItem);
    return this;
  }

   /**
   * Get attributes
   * @return attributes
  **/
  @ApiModelProperty(value = "")
  public List<String> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<String> attributes) {
    this.attributes = attributes;
  }

  public Mine description(String description) {
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

  public Mine includeAuthorizations(Boolean includeAuthorizations) {
    this.includeAuthorizations = includeAuthorizations;
    return this;
  }

   /**
   * Get includeAuthorizations
   * @return includeAuthorizations
  **/
  @ApiModelProperty(value = "")
  public Boolean isIncludeAuthorizations() {
    return includeAuthorizations;
  }

  public void setIncludeAuthorizations(Boolean includeAuthorizations) {
    this.includeAuthorizations = includeAuthorizations;
  }

  public Mine mode(String mode) {
    this.mode = mode;
    return this;
  }

   /**
   * Get mode
   * @return mode
  **/
  @ApiModelProperty(value = "")
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public Mine occurs(Integer occurs) {
    this.occurs = occurs;
    return this;
  }

   /**
   * Get occurs
   * @return occurs
  **/
  @ApiModelProperty(value = "")
  public Integer getOccurs() {
    return occurs;
  }

  public void setOccurs(Integer occurs) {
    this.occurs = occurs;
  }

  public Mine permThreshold(Integer permThreshold) {
    this.permThreshold = permThreshold;
    return this;
  }

   /**
   * Get permThreshold
   * @return permThreshold
  **/
  @ApiModelProperty(value = "")
  public Integer getPermThreshold() {
    return permThreshold;
  }

  public void setPermThreshold(Integer permThreshold) {
    this.permThreshold = permThreshold;
  }

  public Mine roleThreshold(Integer roleThreshold) {
    this.roleThreshold = roleThreshold;
    return this;
  }

   /**
   * Get roleThreshold
   * @return roleThreshold
  **/
  @ApiModelProperty(value = "")
  public Integer getRoleThreshold() {
    return roleThreshold;
  }

  public void setRoleThreshold(Integer roleThreshold) {
    this.roleThreshold = roleThreshold;
  }

  public Mine setCount(Integer setCount) {
    this.setCount = setCount;
    return this;
  }

   /**
   * Get setCount
   * @return setCount
  **/
  @ApiModelProperty(value = "")
  public Integer getSetCount() {
    return setCount;
  }

  public void setSetCount(Integer setCount) {
    this.setCount = setCount;
  }

  public Mine setSize(Integer setSize) {
    this.setSize = setSize;
    return this;
  }

   /**
   * Get setSize
   * @return setSize
  **/
  @ApiModelProperty(value = "")
  public Integer getSetSize() {
    return setSize;
  }

  public void setSetSize(Integer setSize) {
    this.setSize = setSize;
  }

  public Mine topN(Integer topN) {
    this.topN = topN;
    return this;
  }

   /**
   * Get topN
   * @return topN
  **/
  @ApiModelProperty(value = "")
  public Integer getTopN() {
    return topN;
  }

  public void setTopN(Integer topN) {
    this.topN = topN;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Mine mine = (Mine) o;
    return Objects.equals(this.appThreshold, mine.appThreshold) &&
        Objects.equals(this.attributes, mine.attributes) &&
        Objects.equals(this.description, mine.description) &&
        Objects.equals(this.includeAuthorizations, mine.includeAuthorizations) &&
        Objects.equals(this.mode, mine.mode) &&
        Objects.equals(this.occurs, mine.occurs) &&
        Objects.equals(this.permThreshold, mine.permThreshold) &&
        Objects.equals(this.roleThreshold, mine.roleThreshold) &&
        Objects.equals(this.setCount, mine.setCount) &&
        Objects.equals(this.setSize, mine.setSize) &&
        Objects.equals(this.topN, mine.topN);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appThreshold, attributes, description, includeAuthorizations, mode, occurs, permThreshold, roleThreshold, setCount, setSize, topN);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Mine {\n");
    
    sb.append("    appThreshold: ").append(toIndentedString(appThreshold)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    includeAuthorizations: ").append(toIndentedString(includeAuthorizations)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    occurs: ").append(toIndentedString(occurs)).append("\n");
    sb.append("    permThreshold: ").append(toIndentedString(permThreshold)).append("\n");
    sb.append("    roleThreshold: ").append(toIndentedString(roleThreshold)).append("\n");
    sb.append("    setCount: ").append(toIndentedString(setCount)).append("\n");
    sb.append("    setSize: ").append(toIndentedString(setSize)).append("\n");
    sb.append("    topN: ").append(toIndentedString(topN)).append("\n");
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
