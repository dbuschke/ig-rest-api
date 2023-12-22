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
 * Locale
 */



public class Locale {
  @SerializedName("country")
  private String country = null;

  @SerializedName("displayCountry")
  private String displayCountry = null;

  @SerializedName("displayLanguage")
  private String displayLanguage = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("displayScript")
  private String displayScript = null;

  @SerializedName("displayVariant")
  private String displayVariant = null;

  @SerializedName("extensionKeys")
  private List<String> extensionKeys = null;

  @SerializedName("iSO3Country")
  private String iSO3Country = null;

  @SerializedName("iSO3Language")
  private String iSO3Language = null;

  @SerializedName("language")
  private String language = null;

  @SerializedName("script")
  private String script = null;

  @SerializedName("unicodeLocaleAttributes")
  private List<String> unicodeLocaleAttributes = null;

  @SerializedName("unicodeLocaleKeys")
  private List<String> unicodeLocaleKeys = null;

  @SerializedName("variant")
  private String variant = null;

  public Locale country(String country) {
    this.country = country;
    return this;
  }

   /**
   * Get country
   * @return country
  **/
  @ApiModelProperty(value = "")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Locale displayCountry(String displayCountry) {
    this.displayCountry = displayCountry;
    return this;
  }

   /**
   * Get displayCountry
   * @return displayCountry
  **/
  @ApiModelProperty(value = "")
  public String getDisplayCountry() {
    return displayCountry;
  }

  public void setDisplayCountry(String displayCountry) {
    this.displayCountry = displayCountry;
  }

  public Locale displayLanguage(String displayLanguage) {
    this.displayLanguage = displayLanguage;
    return this;
  }

   /**
   * Get displayLanguage
   * @return displayLanguage
  **/
  @ApiModelProperty(value = "")
  public String getDisplayLanguage() {
    return displayLanguage;
  }

  public void setDisplayLanguage(String displayLanguage) {
    this.displayLanguage = displayLanguage;
  }

  public Locale displayName(String displayName) {
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

  public Locale displayScript(String displayScript) {
    this.displayScript = displayScript;
    return this;
  }

   /**
   * Get displayScript
   * @return displayScript
  **/
  @ApiModelProperty(value = "")
  public String getDisplayScript() {
    return displayScript;
  }

  public void setDisplayScript(String displayScript) {
    this.displayScript = displayScript;
  }

  public Locale displayVariant(String displayVariant) {
    this.displayVariant = displayVariant;
    return this;
  }

   /**
   * Get displayVariant
   * @return displayVariant
  **/
  @ApiModelProperty(value = "")
  public String getDisplayVariant() {
    return displayVariant;
  }

  public void setDisplayVariant(String displayVariant) {
    this.displayVariant = displayVariant;
  }

  public Locale extensionKeys(List<String> extensionKeys) {
    this.extensionKeys = extensionKeys;
    return this;
  }

  public Locale addExtensionKeysItem(String extensionKeysItem) {
    if (this.extensionKeys == null) {
      this.extensionKeys = new ArrayList<String>();
    }
    this.extensionKeys.add(extensionKeysItem);
    return this;
  }

   /**
   * Get extensionKeys
   * @return extensionKeys
  **/
  @ApiModelProperty(value = "")
  public List<String> getExtensionKeys() {
    return extensionKeys;
  }

  public void setExtensionKeys(List<String> extensionKeys) {
    this.extensionKeys = extensionKeys;
  }

  public Locale iSO3Country(String iSO3Country) {
    this.iSO3Country = iSO3Country;
    return this;
  }

   /**
   * Get iSO3Country
   * @return iSO3Country
  **/
  @ApiModelProperty(value = "")
  public String getISO3Country() {
    return iSO3Country;
  }

  public void setISO3Country(String iSO3Country) {
    this.iSO3Country = iSO3Country;
  }

  public Locale iSO3Language(String iSO3Language) {
    this.iSO3Language = iSO3Language;
    return this;
  }

   /**
   * Get iSO3Language
   * @return iSO3Language
  **/
  @ApiModelProperty(value = "")
  public String getISO3Language() {
    return iSO3Language;
  }

  public void setISO3Language(String iSO3Language) {
    this.iSO3Language = iSO3Language;
  }

  public Locale language(String language) {
    this.language = language;
    return this;
  }

   /**
   * Get language
   * @return language
  **/
  @ApiModelProperty(value = "")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Locale script(String script) {
    this.script = script;
    return this;
  }

   /**
   * Get script
   * @return script
  **/
  @ApiModelProperty(value = "")
  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public Locale unicodeLocaleAttributes(List<String> unicodeLocaleAttributes) {
    this.unicodeLocaleAttributes = unicodeLocaleAttributes;
    return this;
  }

  public Locale addUnicodeLocaleAttributesItem(String unicodeLocaleAttributesItem) {
    if (this.unicodeLocaleAttributes == null) {
      this.unicodeLocaleAttributes = new ArrayList<String>();
    }
    this.unicodeLocaleAttributes.add(unicodeLocaleAttributesItem);
    return this;
  }

   /**
   * Get unicodeLocaleAttributes
   * @return unicodeLocaleAttributes
  **/
  @ApiModelProperty(value = "")
  public List<String> getUnicodeLocaleAttributes() {
    return unicodeLocaleAttributes;
  }

  public void setUnicodeLocaleAttributes(List<String> unicodeLocaleAttributes) {
    this.unicodeLocaleAttributes = unicodeLocaleAttributes;
  }

  public Locale unicodeLocaleKeys(List<String> unicodeLocaleKeys) {
    this.unicodeLocaleKeys = unicodeLocaleKeys;
    return this;
  }

  public Locale addUnicodeLocaleKeysItem(String unicodeLocaleKeysItem) {
    if (this.unicodeLocaleKeys == null) {
      this.unicodeLocaleKeys = new ArrayList<String>();
    }
    this.unicodeLocaleKeys.add(unicodeLocaleKeysItem);
    return this;
  }

   /**
   * Get unicodeLocaleKeys
   * @return unicodeLocaleKeys
  **/
  @ApiModelProperty(value = "")
  public List<String> getUnicodeLocaleKeys() {
    return unicodeLocaleKeys;
  }

  public void setUnicodeLocaleKeys(List<String> unicodeLocaleKeys) {
    this.unicodeLocaleKeys = unicodeLocaleKeys;
  }

  public Locale variant(String variant) {
    this.variant = variant;
    return this;
  }

   /**
   * Get variant
   * @return variant
  **/
  @ApiModelProperty(value = "")
  public String getVariant() {
    return variant;
  }

  public void setVariant(String variant) {
    this.variant = variant;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Locale locale = (Locale) o;
    return Objects.equals(this.country, locale.country) &&
        Objects.equals(this.displayCountry, locale.displayCountry) &&
        Objects.equals(this.displayLanguage, locale.displayLanguage) &&
        Objects.equals(this.displayName, locale.displayName) &&
        Objects.equals(this.displayScript, locale.displayScript) &&
        Objects.equals(this.displayVariant, locale.displayVariant) &&
        Objects.equals(this.extensionKeys, locale.extensionKeys) &&
        Objects.equals(this.iSO3Country, locale.iSO3Country) &&
        Objects.equals(this.iSO3Language, locale.iSO3Language) &&
        Objects.equals(this.language, locale.language) &&
        Objects.equals(this.script, locale.script) &&
        Objects.equals(this.unicodeLocaleAttributes, locale.unicodeLocaleAttributes) &&
        Objects.equals(this.unicodeLocaleKeys, locale.unicodeLocaleKeys) &&
        Objects.equals(this.variant, locale.variant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(country, displayCountry, displayLanguage, displayName, displayScript, displayVariant, extensionKeys, iSO3Country, iSO3Language, language, script, unicodeLocaleAttributes, unicodeLocaleKeys, variant);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Locale {\n");
    
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    displayCountry: ").append(toIndentedString(displayCountry)).append("\n");
    sb.append("    displayLanguage: ").append(toIndentedString(displayLanguage)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    displayScript: ").append(toIndentedString(displayScript)).append("\n");
    sb.append("    displayVariant: ").append(toIndentedString(displayVariant)).append("\n");
    sb.append("    extensionKeys: ").append(toIndentedString(extensionKeys)).append("\n");
    sb.append("    iSO3Country: ").append(toIndentedString(iSO3Country)).append("\n");
    sb.append("    iSO3Language: ").append(toIndentedString(iSO3Language)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    script: ").append(toIndentedString(script)).append("\n");
    sb.append("    unicodeLocaleAttributes: ").append(toIndentedString(unicodeLocaleAttributes)).append("\n");
    sb.append("    unicodeLocaleKeys: ").append(toIndentedString(unicodeLocaleKeys)).append("\n");
    sb.append("    variant: ").append(toIndentedString(variant)).append("\n");
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
