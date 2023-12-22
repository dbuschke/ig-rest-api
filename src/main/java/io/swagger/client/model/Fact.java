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
import io.swagger.client.model.MetricsDataStore;
import java.io.IOException;
/**
 * Fact
 */



public class Fact {
  @SerializedName("active")
  private Boolean active = null;

  @SerializedName("canManage")
  private Boolean canManage = null;

  @SerializedName("chartable")
  private Boolean chartable = null;

  @SerializedName("config")
  private String config = null;

  @SerializedName("count")
  private Integer count = null;

  @SerializedName("dataType")
  private String dataType = null;

  @SerializedName("descCatID")
  private String descCatID = null;

  @SerializedName("descFB")
  private String descFB = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("entityType")
  private String entityType = null;

  @SerializedName("error")
  private String error = null;

  @SerializedName("factID")
  private String factID = null;

  @SerializedName("firstTimeMSUTC")
  private String firstTimeMSUTC = null;

  @SerializedName("historyRef")
  private String historyRef = null;

  @SerializedName("intervalMS")
  private Long intervalMS = null;

  @SerializedName("lastTimeMSUTC")
  private String lastTimeMSUTC = null;

  @SerializedName("locator")
  private String locator = null;

  @SerializedName("metricsDataStore")
  private MetricsDataStore metricsDataStore = null;

  @SerializedName("nextTimeStartMSUTC")
  private String nextTimeStartMSUTC = null;

  @SerializedName("ref")
  private String ref = null;

  @SerializedName("startOnMSUTC")
  private String startOnMSUTC = null;

  @SerializedName("stdName")
  private String stdName = null;

  @SerializedName("warnMessage")
  private String warnMessage = null;

  public Fact active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @ApiModelProperty(value = "")
  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Fact canManage(Boolean canManage) {
    this.canManage = canManage;
    return this;
  }

   /**
   * Get canManage
   * @return canManage
  **/
  @ApiModelProperty(value = "")
  public Boolean isCanManage() {
    return canManage;
  }

  public void setCanManage(Boolean canManage) {
    this.canManage = canManage;
  }

  public Fact chartable(Boolean chartable) {
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

  public Fact config(String config) {
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

  public Fact count(Integer count) {
    this.count = count;
    return this;
  }

   /**
   * Get count
   * @return count
  **/
  @ApiModelProperty(value = "")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Fact dataType(String dataType) {
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

  public Fact descCatID(String descCatID) {
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

  public Fact descFB(String descFB) {
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

  public Fact displayName(String displayName) {
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

  public Fact entityType(String entityType) {
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

  public Fact error(String error) {
    this.error = error;
    return this;
  }

   /**
   * Get error
   * @return error
  **/
  @ApiModelProperty(value = "")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Fact factID(String factID) {
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

  public Fact firstTimeMSUTC(String firstTimeMSUTC) {
    this.firstTimeMSUTC = firstTimeMSUTC;
    return this;
  }

   /**
   * Get firstTimeMSUTC
   * @return firstTimeMSUTC
  **/
  @ApiModelProperty(value = "")
  public String getFirstTimeMSUTC() {
    return firstTimeMSUTC;
  }

  public void setFirstTimeMSUTC(String firstTimeMSUTC) {
    this.firstTimeMSUTC = firstTimeMSUTC;
  }

  public Fact historyRef(String historyRef) {
    this.historyRef = historyRef;
    return this;
  }

   /**
   * Get historyRef
   * @return historyRef
  **/
  @ApiModelProperty(value = "")
  public String getHistoryRef() {
    return historyRef;
  }

  public void setHistoryRef(String historyRef) {
    this.historyRef = historyRef;
  }

  public Fact intervalMS(Long intervalMS) {
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

  public Fact lastTimeMSUTC(String lastTimeMSUTC) {
    this.lastTimeMSUTC = lastTimeMSUTC;
    return this;
  }

   /**
   * Get lastTimeMSUTC
   * @return lastTimeMSUTC
  **/
  @ApiModelProperty(value = "")
  public String getLastTimeMSUTC() {
    return lastTimeMSUTC;
  }

  public void setLastTimeMSUTC(String lastTimeMSUTC) {
    this.lastTimeMSUTC = lastTimeMSUTC;
  }

  public Fact locator(String locator) {
    this.locator = locator;
    return this;
  }

   /**
   * Get locator
   * @return locator
  **/
  @ApiModelProperty(value = "")
  public String getLocator() {
    return locator;
  }

  public void setLocator(String locator) {
    this.locator = locator;
  }

  public Fact metricsDataStore(MetricsDataStore metricsDataStore) {
    this.metricsDataStore = metricsDataStore;
    return this;
  }

   /**
   * Get metricsDataStore
   * @return metricsDataStore
  **/
  @ApiModelProperty(value = "")
  public MetricsDataStore getMetricsDataStore() {
    return metricsDataStore;
  }

  public void setMetricsDataStore(MetricsDataStore metricsDataStore) {
    this.metricsDataStore = metricsDataStore;
  }

  public Fact nextTimeStartMSUTC(String nextTimeStartMSUTC) {
    this.nextTimeStartMSUTC = nextTimeStartMSUTC;
    return this;
  }

   /**
   * Get nextTimeStartMSUTC
   * @return nextTimeStartMSUTC
  **/
  @ApiModelProperty(value = "")
  public String getNextTimeStartMSUTC() {
    return nextTimeStartMSUTC;
  }

  public void setNextTimeStartMSUTC(String nextTimeStartMSUTC) {
    this.nextTimeStartMSUTC = nextTimeStartMSUTC;
  }

  public Fact ref(String ref) {
    this.ref = ref;
    return this;
  }

   /**
   * Get ref
   * @return ref
  **/
  @ApiModelProperty(value = "")
  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public Fact startOnMSUTC(String startOnMSUTC) {
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

  public Fact stdName(String stdName) {
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

  public Fact warnMessage(String warnMessage) {
    this.warnMessage = warnMessage;
    return this;
  }

   /**
   * Get warnMessage
   * @return warnMessage
  **/
  @ApiModelProperty(value = "")
  public String getWarnMessage() {
    return warnMessage;
  }

  public void setWarnMessage(String warnMessage) {
    this.warnMessage = warnMessage;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Fact fact = (Fact) o;
    return Objects.equals(this.active, fact.active) &&
        Objects.equals(this.canManage, fact.canManage) &&
        Objects.equals(this.chartable, fact.chartable) &&
        Objects.equals(this.config, fact.config) &&
        Objects.equals(this.count, fact.count) &&
        Objects.equals(this.dataType, fact.dataType) &&
        Objects.equals(this.descCatID, fact.descCatID) &&
        Objects.equals(this.descFB, fact.descFB) &&
        Objects.equals(this.displayName, fact.displayName) &&
        Objects.equals(this.entityType, fact.entityType) &&
        Objects.equals(this.error, fact.error) &&
        Objects.equals(this.factID, fact.factID) &&
        Objects.equals(this.firstTimeMSUTC, fact.firstTimeMSUTC) &&
        Objects.equals(this.historyRef, fact.historyRef) &&
        Objects.equals(this.intervalMS, fact.intervalMS) &&
        Objects.equals(this.lastTimeMSUTC, fact.lastTimeMSUTC) &&
        Objects.equals(this.locator, fact.locator) &&
        Objects.equals(this.metricsDataStore, fact.metricsDataStore) &&
        Objects.equals(this.nextTimeStartMSUTC, fact.nextTimeStartMSUTC) &&
        Objects.equals(this.ref, fact.ref) &&
        Objects.equals(this.startOnMSUTC, fact.startOnMSUTC) &&
        Objects.equals(this.stdName, fact.stdName) &&
        Objects.equals(this.warnMessage, fact.warnMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, canManage, chartable, config, count, dataType, descCatID, descFB, displayName, entityType, error, factID, firstTimeMSUTC, historyRef, intervalMS, lastTimeMSUTC, locator, metricsDataStore, nextTimeStartMSUTC, ref, startOnMSUTC, stdName, warnMessage);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Fact {\n");
    
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    canManage: ").append(toIndentedString(canManage)).append("\n");
    sb.append("    chartable: ").append(toIndentedString(chartable)).append("\n");
    sb.append("    config: ").append(toIndentedString(config)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    descCatID: ").append(toIndentedString(descCatID)).append("\n");
    sb.append("    descFB: ").append(toIndentedString(descFB)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    factID: ").append(toIndentedString(factID)).append("\n");
    sb.append("    firstTimeMSUTC: ").append(toIndentedString(firstTimeMSUTC)).append("\n");
    sb.append("    historyRef: ").append(toIndentedString(historyRef)).append("\n");
    sb.append("    intervalMS: ").append(toIndentedString(intervalMS)).append("\n");
    sb.append("    lastTimeMSUTC: ").append(toIndentedString(lastTimeMSUTC)).append("\n");
    sb.append("    locator: ").append(toIndentedString(locator)).append("\n");
    sb.append("    metricsDataStore: ").append(toIndentedString(metricsDataStore)).append("\n");
    sb.append("    nextTimeStartMSUTC: ").append(toIndentedString(nextTimeStartMSUTC)).append("\n");
    sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
    sb.append("    startOnMSUTC: ").append(toIndentedString(startOnMSUTC)).append("\n");
    sb.append("    stdName: ").append(toIndentedString(stdName)).append("\n");
    sb.append("    warnMessage: ").append(toIndentedString(warnMessage)).append("\n");
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
