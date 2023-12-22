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
import io.swagger.client.model.Burole;
import io.swagger.client.model.Groups;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * AllowedItemRequester
 */



public class AllowedItemRequester {
  @SerializedName("allowForAll")
  private Boolean allowForAll = null;

  @SerializedName("allowForBusinessRoles")
  private Boolean allowForBusinessRoles = null;

  @SerializedName("allowForDirectReports")
  private Boolean allowForDirectReports = null;

  @SerializedName("allowForDownlineReports")
  private Boolean allowForDownlineReports = null;

  @SerializedName("allowForGroups")
  private Boolean allowForGroups = null;

  @SerializedName("allowForMatching")
  private Boolean allowForMatching = null;

  @SerializedName("allowForSelf")
  private Boolean allowForSelf = null;

  @SerializedName("businessRoles")
  private List<Burole> businessRoles = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("groups")
  private List<Groups> groups = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("matchingCriteria")
  private String matchingCriteria = null;

  @SerializedName("notFound")
  private Boolean notFound = null;

  /**
   * Gets or Sets requesterType
   */
  @JsonAdapter(RequesterTypeEnum.Adapter.class)
  public enum RequesterTypeEnum {
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("GROUP")
    GROUP("GROUP"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("ALL_USERS")
    ALL_USERS("ALL_USERS");

    private String value;

    RequesterTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RequesterTypeEnum fromValue(String input) {
      for (RequesterTypeEnum b : RequesterTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RequesterTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RequesterTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RequesterTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RequesterTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("requesterType")
  private RequesterTypeEnum requesterType = null;

  @SerializedName("requesterUniqueId")
  private String requesterUniqueId = null;

  public AllowedItemRequester allowForAll(Boolean allowForAll) {
    this.allowForAll = allowForAll;
    return this;
  }

   /**
   * Get allowForAll
   * @return allowForAll
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowForAll() {
    return allowForAll;
  }

  public void setAllowForAll(Boolean allowForAll) {
    this.allowForAll = allowForAll;
  }

  public AllowedItemRequester allowForBusinessRoles(Boolean allowForBusinessRoles) {
    this.allowForBusinessRoles = allowForBusinessRoles;
    return this;
  }

   /**
   * Get allowForBusinessRoles
   * @return allowForBusinessRoles
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowForBusinessRoles() {
    return allowForBusinessRoles;
  }

  public void setAllowForBusinessRoles(Boolean allowForBusinessRoles) {
    this.allowForBusinessRoles = allowForBusinessRoles;
  }

  public AllowedItemRequester allowForDirectReports(Boolean allowForDirectReports) {
    this.allowForDirectReports = allowForDirectReports;
    return this;
  }

   /**
   * Get allowForDirectReports
   * @return allowForDirectReports
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowForDirectReports() {
    return allowForDirectReports;
  }

  public void setAllowForDirectReports(Boolean allowForDirectReports) {
    this.allowForDirectReports = allowForDirectReports;
  }

  public AllowedItemRequester allowForDownlineReports(Boolean allowForDownlineReports) {
    this.allowForDownlineReports = allowForDownlineReports;
    return this;
  }

   /**
   * Get allowForDownlineReports
   * @return allowForDownlineReports
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowForDownlineReports() {
    return allowForDownlineReports;
  }

  public void setAllowForDownlineReports(Boolean allowForDownlineReports) {
    this.allowForDownlineReports = allowForDownlineReports;
  }

  public AllowedItemRequester allowForGroups(Boolean allowForGroups) {
    this.allowForGroups = allowForGroups;
    return this;
  }

   /**
   * Get allowForGroups
   * @return allowForGroups
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowForGroups() {
    return allowForGroups;
  }

  public void setAllowForGroups(Boolean allowForGroups) {
    this.allowForGroups = allowForGroups;
  }

  public AllowedItemRequester allowForMatching(Boolean allowForMatching) {
    this.allowForMatching = allowForMatching;
    return this;
  }

   /**
   * Get allowForMatching
   * @return allowForMatching
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowForMatching() {
    return allowForMatching;
  }

  public void setAllowForMatching(Boolean allowForMatching) {
    this.allowForMatching = allowForMatching;
  }

  public AllowedItemRequester allowForSelf(Boolean allowForSelf) {
    this.allowForSelf = allowForSelf;
    return this;
  }

   /**
   * Get allowForSelf
   * @return allowForSelf
  **/
  @ApiModelProperty(value = "")
  public Boolean isAllowForSelf() {
    return allowForSelf;
  }

  public void setAllowForSelf(Boolean allowForSelf) {
    this.allowForSelf = allowForSelf;
  }

  public AllowedItemRequester businessRoles(List<Burole> businessRoles) {
    this.businessRoles = businessRoles;
    return this;
  }

  public AllowedItemRequester addBusinessRolesItem(Burole businessRolesItem) {
    if (this.businessRoles == null) {
      this.businessRoles = new ArrayList<Burole>();
    }
    this.businessRoles.add(businessRolesItem);
    return this;
  }

   /**
   * Get businessRoles
   * @return businessRoles
  **/
  @ApiModelProperty(value = "")
  public List<Burole> getBusinessRoles() {
    return businessRoles;
  }

  public void setBusinessRoles(List<Burole> businessRoles) {
    this.businessRoles = businessRoles;
  }

  public AllowedItemRequester displayName(String displayName) {
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

  public AllowedItemRequester groups(List<Groups> groups) {
    this.groups = groups;
    return this;
  }

  public AllowedItemRequester addGroupsItem(Groups groupsItem) {
    if (this.groups == null) {
      this.groups = new ArrayList<Groups>();
    }
    this.groups.add(groupsItem);
    return this;
  }

   /**
   * Get groups
   * @return groups
  **/
  @ApiModelProperty(value = "")
  public List<Groups> getGroups() {
    return groups;
  }

  public void setGroups(List<Groups> groups) {
    this.groups = groups;
  }

  public AllowedItemRequester id(Long id) {
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

  public AllowedItemRequester link(String link) {
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

  public AllowedItemRequester matchingCriteria(String matchingCriteria) {
    this.matchingCriteria = matchingCriteria;
    return this;
  }

   /**
   * Get matchingCriteria
   * @return matchingCriteria
  **/
  @ApiModelProperty(value = "")
  public String getMatchingCriteria() {
    return matchingCriteria;
  }

  public void setMatchingCriteria(String matchingCriteria) {
    this.matchingCriteria = matchingCriteria;
  }

  public AllowedItemRequester notFound(Boolean notFound) {
    this.notFound = notFound;
    return this;
  }

   /**
   * Get notFound
   * @return notFound
  **/
  @ApiModelProperty(value = "")
  public Boolean isNotFound() {
    return notFound;
  }

  public void setNotFound(Boolean notFound) {
    this.notFound = notFound;
  }

  public AllowedItemRequester requesterType(RequesterTypeEnum requesterType) {
    this.requesterType = requesterType;
    return this;
  }

   /**
   * Get requesterType
   * @return requesterType
  **/
  @ApiModelProperty(value = "")
  public RequesterTypeEnum getRequesterType() {
    return requesterType;
  }

  public void setRequesterType(RequesterTypeEnum requesterType) {
    this.requesterType = requesterType;
  }

  public AllowedItemRequester requesterUniqueId(String requesterUniqueId) {
    this.requesterUniqueId = requesterUniqueId;
    return this;
  }

   /**
   * Get requesterUniqueId
   * @return requesterUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getRequesterUniqueId() {
    return requesterUniqueId;
  }

  public void setRequesterUniqueId(String requesterUniqueId) {
    this.requesterUniqueId = requesterUniqueId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllowedItemRequester allowedItemRequester = (AllowedItemRequester) o;
    return Objects.equals(this.allowForAll, allowedItemRequester.allowForAll) &&
        Objects.equals(this.allowForBusinessRoles, allowedItemRequester.allowForBusinessRoles) &&
        Objects.equals(this.allowForDirectReports, allowedItemRequester.allowForDirectReports) &&
        Objects.equals(this.allowForDownlineReports, allowedItemRequester.allowForDownlineReports) &&
        Objects.equals(this.allowForGroups, allowedItemRequester.allowForGroups) &&
        Objects.equals(this.allowForMatching, allowedItemRequester.allowForMatching) &&
        Objects.equals(this.allowForSelf, allowedItemRequester.allowForSelf) &&
        Objects.equals(this.businessRoles, allowedItemRequester.businessRoles) &&
        Objects.equals(this.displayName, allowedItemRequester.displayName) &&
        Objects.equals(this.groups, allowedItemRequester.groups) &&
        Objects.equals(this.id, allowedItemRequester.id) &&
        Objects.equals(this.link, allowedItemRequester.link) &&
        Objects.equals(this.matchingCriteria, allowedItemRequester.matchingCriteria) &&
        Objects.equals(this.notFound, allowedItemRequester.notFound) &&
        Objects.equals(this.requesterType, allowedItemRequester.requesterType) &&
        Objects.equals(this.requesterUniqueId, allowedItemRequester.requesterUniqueId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowForAll, allowForBusinessRoles, allowForDirectReports, allowForDownlineReports, allowForGroups, allowForMatching, allowForSelf, businessRoles, displayName, groups, id, link, matchingCriteria, notFound, requesterType, requesterUniqueId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllowedItemRequester {\n");
    
    sb.append("    allowForAll: ").append(toIndentedString(allowForAll)).append("\n");
    sb.append("    allowForBusinessRoles: ").append(toIndentedString(allowForBusinessRoles)).append("\n");
    sb.append("    allowForDirectReports: ").append(toIndentedString(allowForDirectReports)).append("\n");
    sb.append("    allowForDownlineReports: ").append(toIndentedString(allowForDownlineReports)).append("\n");
    sb.append("    allowForGroups: ").append(toIndentedString(allowForGroups)).append("\n");
    sb.append("    allowForMatching: ").append(toIndentedString(allowForMatching)).append("\n");
    sb.append("    allowForSelf: ").append(toIndentedString(allowForSelf)).append("\n");
    sb.append("    businessRoles: ").append(toIndentedString(businessRoles)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    matchingCriteria: ").append(toIndentedString(matchingCriteria)).append("\n");
    sb.append("    notFound: ").append(toIndentedString(notFound)).append("\n");
    sb.append("    requesterType: ").append(toIndentedString(requesterType)).append("\n");
    sb.append("    requesterUniqueId: ").append(toIndentedString(requesterUniqueId)).append("\n");
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
