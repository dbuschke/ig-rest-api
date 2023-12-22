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
 * Items
 */



public class Items {
  @SerializedName("aid")
  private Long aid = null;

  @SerializedName("appid")
  private Long appid = null;

  @SerializedName("brid")
  private Long brid = null;

  @SerializedName("pid")
  private Long pid = null;

  @SerializedName("suid")
  private Long suid = null;

  @SerializedName("suuid")
  private String suuid = null;

  @SerializedName("trid")
  private Long trid = null;

  @SerializedName("uaid")
  private String uaid = null;

  @SerializedName("ubrid")
  private String ubrid = null;

  @SerializedName("uid")
  private Long uid = null;

  @SerializedName("upid")
  private String upid = null;

  @SerializedName("utrid")
  private String utrid = null;

  @SerializedName("uuid")
  private String uuid = null;

  @SerializedName("values")
  private List<String> values = null;

  public Items aid(Long aid) {
    this.aid = aid;
    return this;
  }

   /**
   * Get aid
   * @return aid
  **/
  @ApiModelProperty(value = "")
  public Long getAid() {
    return aid;
  }

  public void setAid(Long aid) {
    this.aid = aid;
  }

  public Items appid(Long appid) {
    this.appid = appid;
    return this;
  }

   /**
   * Get appid
   * @return appid
  **/
  @ApiModelProperty(value = "")
  public Long getAppid() {
    return appid;
  }

  public void setAppid(Long appid) {
    this.appid = appid;
  }

  public Items brid(Long brid) {
    this.brid = brid;
    return this;
  }

   /**
   * Get brid
   * @return brid
  **/
  @ApiModelProperty(value = "")
  public Long getBrid() {
    return brid;
  }

  public void setBrid(Long brid) {
    this.brid = brid;
  }

  public Items pid(Long pid) {
    this.pid = pid;
    return this;
  }

   /**
   * Get pid
   * @return pid
  **/
  @ApiModelProperty(value = "")
  public Long getPid() {
    return pid;
  }

  public void setPid(Long pid) {
    this.pid = pid;
  }

  public Items suid(Long suid) {
    this.suid = suid;
    return this;
  }

   /**
   * Get suid
   * @return suid
  **/
  @ApiModelProperty(value = "")
  public Long getSuid() {
    return suid;
  }

  public void setSuid(Long suid) {
    this.suid = suid;
  }

  public Items suuid(String suuid) {
    this.suuid = suuid;
    return this;
  }

   /**
   * Get suuid
   * @return suuid
  **/
  @ApiModelProperty(value = "")
  public String getSuuid() {
    return suuid;
  }

  public void setSuuid(String suuid) {
    this.suuid = suuid;
  }

  public Items trid(Long trid) {
    this.trid = trid;
    return this;
  }

   /**
   * Get trid
   * @return trid
  **/
  @ApiModelProperty(value = "")
  public Long getTrid() {
    return trid;
  }

  public void setTrid(Long trid) {
    this.trid = trid;
  }

  public Items uaid(String uaid) {
    this.uaid = uaid;
    return this;
  }

   /**
   * Get uaid
   * @return uaid
  **/
  @ApiModelProperty(value = "")
  public String getUaid() {
    return uaid;
  }

  public void setUaid(String uaid) {
    this.uaid = uaid;
  }

  public Items ubrid(String ubrid) {
    this.ubrid = ubrid;
    return this;
  }

   /**
   * Get ubrid
   * @return ubrid
  **/
  @ApiModelProperty(value = "")
  public String getUbrid() {
    return ubrid;
  }

  public void setUbrid(String ubrid) {
    this.ubrid = ubrid;
  }

  public Items uid(Long uid) {
    this.uid = uid;
    return this;
  }

   /**
   * Get uid
   * @return uid
  **/
  @ApiModelProperty(value = "")
  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public Items upid(String upid) {
    this.upid = upid;
    return this;
  }

   /**
   * Get upid
   * @return upid
  **/
  @ApiModelProperty(value = "")
  public String getUpid() {
    return upid;
  }

  public void setUpid(String upid) {
    this.upid = upid;
  }

  public Items utrid(String utrid) {
    this.utrid = utrid;
    return this;
  }

   /**
   * Get utrid
   * @return utrid
  **/
  @ApiModelProperty(value = "")
  public String getUtrid() {
    return utrid;
  }

  public void setUtrid(String utrid) {
    this.utrid = utrid;
  }

  public Items uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   * Get uuid
   * @return uuid
  **/
  @ApiModelProperty(value = "")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Items values(List<String> values) {
    this.values = values;
    return this;
  }

  public Items addValuesItem(String valuesItem) {
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
    Items items = (Items) o;
    return Objects.equals(this.aid, items.aid) &&
        Objects.equals(this.appid, items.appid) &&
        Objects.equals(this.brid, items.brid) &&
        Objects.equals(this.pid, items.pid) &&
        Objects.equals(this.suid, items.suid) &&
        Objects.equals(this.suuid, items.suuid) &&
        Objects.equals(this.trid, items.trid) &&
        Objects.equals(this.uaid, items.uaid) &&
        Objects.equals(this.ubrid, items.ubrid) &&
        Objects.equals(this.uid, items.uid) &&
        Objects.equals(this.upid, items.upid) &&
        Objects.equals(this.utrid, items.utrid) &&
        Objects.equals(this.uuid, items.uuid) &&
        Objects.equals(this.values, items.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aid, appid, brid, pid, suid, suuid, trid, uaid, ubrid, uid, upid, utrid, uuid, values);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Items {\n");
    
    sb.append("    aid: ").append(toIndentedString(aid)).append("\n");
    sb.append("    appid: ").append(toIndentedString(appid)).append("\n");
    sb.append("    brid: ").append(toIndentedString(brid)).append("\n");
    sb.append("    pid: ").append(toIndentedString(pid)).append("\n");
    sb.append("    suid: ").append(toIndentedString(suid)).append("\n");
    sb.append("    suuid: ").append(toIndentedString(suuid)).append("\n");
    sb.append("    trid: ").append(toIndentedString(trid)).append("\n");
    sb.append("    uaid: ").append(toIndentedString(uaid)).append("\n");
    sb.append("    ubrid: ").append(toIndentedString(ubrid)).append("\n");
    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    upid: ").append(toIndentedString(upid)).append("\n");
    sb.append("    utrid: ").append(toIndentedString(utrid)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
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
