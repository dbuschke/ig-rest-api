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
 * FactRegReq
 */



public class FactRegReq {
  @SerializedName("chartable")
  private Boolean chartable = null;

  @SerializedName("config")
  private String config = null;

  @SerializedName("dataType")
  private String dataType = null;

  @SerializedName("descCatID")
  private String descCatID = null;

  @SerializedName("descFB")
  private String descFB = null;

  @SerializedName("dryRun")
  private Boolean dryRun = null;

  @SerializedName("entityType")
  private String entityType = null;

  @SerializedName("factID")
  private String factID = null;

  @SerializedName("factTable")
  private String factTable = null;

  @SerializedName("intervalMS")
  private Long intervalMS = null;

  @SerializedName("metricsDataStoreId")
  private Long metricsDataStoreId = null;

  @SerializedName("skipValidate")
  private Boolean skipValidate = null;

  @SerializedName("startOnMSUTC")
  private String startOnMSUTC = null;

  @SerializedName("stdName")
  private String stdName = null;

  public FactRegReq chartable(Boolean chartable) {
    this.chartable = chartable;
    return this;
  }

   /**
   * Get chartable
   * @return chartable
  **/
  @ApiModelProperty(value = "")
  public Boolean isChartable() {
    return chartable;
  }

  public void setChartable(Boolean chartable) {
    this.chartable = chartable;
  }

  public FactRegReq config(String config) {
    this.config = config;
    return this;
  }

   /**
   * Get config
   * @return config
  **/
  @ApiModelProperty(value = "")
  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public FactRegReq dataType(String dataType) {
    this.dataType = dataType;
    return this;
  }

   /**
   * Get dataType
   * @return dataType
  **/
  @ApiModelProperty(value = "")
  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public FactRegReq descCatID(String descCatID) {
    this.descCatID = descCatID;
    return this;
  }

   /**
   * Get descCatID
   * @return descCatID
  **/
  @ApiModelProperty(value = "")
  public String getDescCatID() {
    return descCatID;
  }

  public void setDescCatID(String descCatID) {
    this.descCatID = descCatID;
  }

  public FactRegReq descFB(String descFB) {
    this.descFB = descFB;
    return this;
  }

   /**
   * Get descFB
   * @return descFB
  **/
  @ApiModelProperty(value = "")
  public String getDescFB() {
    return descFB;
  }

  public void setDescFB(String descFB) {
    this.descFB = descFB;
  }

  public FactRegReq dryRun(Boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

   /**
   * Get dryRun
   * @return dryRun
  **/
  @ApiModelProperty(value = "")
  public Boolean isDryRun() {
    return dryRun;
  }

  public void setDryRun(Boolean dryRun) {
    this.dryRun = dryRun;
  }

  public FactRegReq entityType(String entityType) {
    this.entityType = entityType;
    return this;
  }

   /**
   * Get entityType
   * @return entityType
  **/
  @ApiModelProperty(value = "")
  public String getEntityType() {
    return entityType;
  }

  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }

  public FactRegReq factID(String factID) {
    this.factID = factID;
    return this;
  }

   /**
   * Get factID
   * @return factID
  **/
  @ApiModelProperty(value = "")
  public String getFactID() {
    return factID;
  }

  public void setFactID(String factID) {
    this.factID = factID;
  }

  public FactRegReq factTable(String factTable) {
    this.factTable = factTable;
    return this;
  }

   /**
   * Get factTable
   * @return factTable
  **/
  @ApiModelProperty(value = "")
  public String getFactTable() {
    return factTable;
  }

  public void setFactTable(String factTable) {
    this.factTable = factTable;
  }

  public FactRegReq intervalMS(Long intervalMS) {
    this.intervalMS = intervalMS;
    return this;
  }

   /**
   * Get intervalMS
   * @return intervalMS
  **/
  @ApiModelProperty(value = "")
  public Long getIntervalMS() {
    return intervalMS;
  }

  public void setIntervalMS(Long intervalMS) {
    this.intervalMS = intervalMS;
  }

  public FactRegReq metricsDataStoreId(Long metricsDataStoreId) {
    this.metricsDataStoreId = metricsDataStoreId;
    return this;
  }

   /**
   * Get metricsDataStoreId
   * @return metricsDataStoreId
  **/
  @ApiModelProperty(value = "")
  public Long getMetricsDataStoreId() {
    return metricsDataStoreId;
  }

  public void setMetricsDataStoreId(Long metricsDataStoreId) {
    this.metricsDataStoreId = metricsDataStoreId;
  }

  public FactRegReq skipValidate(Boolean skipValidate) {
    this.skipValidate = skipValidate;
    return this;
  }

   /**
   * Get skipValidate
   * @return skipValidate
  **/
  @ApiModelProperty(value = "")
  public Boolean isSkipValidate() {
    return skipValidate;
  }

  public void setSkipValidate(Boolean skipValidate) {
    this.skipValidate = skipValidate;
  }

  public FactRegReq startOnMSUTC(String startOnMSUTC) {
    this.startOnMSUTC = startOnMSUTC;
    return this;
  }

   /**
   * Get startOnMSUTC
   * @return startOnMSUTC
  **/
  @ApiModelProperty(value = "")
  public String getStartOnMSUTC() {
    return startOnMSUTC;
  }

  public void setStartOnMSUTC(String startOnMSUTC) {
    this.startOnMSUTC = startOnMSUTC;
  }

  public FactRegReq stdName(String stdName) {
    this.stdName = stdName;
    return this;
  }

   /**
   * Get stdName
   * @return stdName
  **/
  @ApiModelProperty(value = "")
  public String getStdName() {
    return stdName;
  }

  public void setStdName(String stdName) {
    this.stdName = stdName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactRegReq factRegReq = (FactRegReq) o;
    return Objects.equals(this.chartable, factRegReq.chartable) &&
        Objects.equals(this.config, factRegReq.config) &&
        Objects.equals(this.dataType, factRegReq.dataType) &&
        Objects.equals(this.descCatID, factRegReq.descCatID) &&
        Objects.equals(this.descFB, factRegReq.descFB) &&
        Objects.equals(this.dryRun, factRegReq.dryRun) &&
        Objects.equals(this.entityType, factRegReq.entityType) &&
        Objects.equals(this.factID, factRegReq.factID) &&
        Objects.equals(this.factTable, factRegReq.factTable) &&
        Objects.equals(this.intervalMS, factRegReq.intervalMS) &&
        Objects.equals(this.metricsDataStoreId, factRegReq.metricsDataStoreId) &&
        Objects.equals(this.skipValidate, factRegReq.skipValidate) &&
        Objects.equals(this.startOnMSUTC, factRegReq.startOnMSUTC) &&
        Objects.equals(this.stdName, factRegReq.stdName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chartable, config, dataType, descCatID, descFB, dryRun, entityType, factID, factTable, intervalMS, metricsDataStoreId, skipValidate, startOnMSUTC, stdName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactRegReq {\n");
    
    sb.append("    chartable: ").append(toIndentedString(chartable)).append("\n");
    sb.append("    config: ").append(toIndentedString(config)).append("\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    descCatID: ").append(toIndentedString(descCatID)).append("\n");
    sb.append("    descFB: ").append(toIndentedString(descFB)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    factID: ").append(toIndentedString(factID)).append("\n");
    sb.append("    factTable: ").append(toIndentedString(factTable)).append("\n");
    sb.append("    intervalMS: ").append(toIndentedString(intervalMS)).append("\n");
    sb.append("    metricsDataStoreId: ").append(toIndentedString(metricsDataStoreId)).append("\n");
    sb.append("    skipValidate: ").append(toIndentedString(skipValidate)).append("\n");
    sb.append("    startOnMSUTC: ").append(toIndentedString(startOnMSUTC)).append("\n");
    sb.append("    stdName: ").append(toIndentedString(stdName)).append("\n");
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
