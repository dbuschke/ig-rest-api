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
 * RiskFactorCfg
 */



public class RiskFactorCfg {
  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("factorAttrKey")
  private String factorAttrKey = null;

  @SerializedName("factorUniqueId")
  private String factorUniqueId = null;

  @SerializedName("factorWeight")
  private Long factorWeight = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("lowerLimit")
  private Long lowerLimit = null;

  @SerializedName("maxRiskValue")
  private Long maxRiskValue = null;

  @SerializedName("name")
  private String name = null;

  /**
   * Gets or Sets operation
   */
  @JsonAdapter(OperationEnum.Adapter.class)
  public enum OperationEnum {
    @SerializedName("AVG")
    AVG("AVG"),
    @SerializedName("MAX")
    MAX("MAX");

    private String value;

    OperationEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static OperationEnum fromValue(String input) {
      for (OperationEnum b : OperationEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<OperationEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final OperationEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public OperationEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return OperationEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("operation")
  private OperationEnum operation = null;

  @SerializedName("rfId")
  private Long rfId = null;

  @SerializedName("rfType")
  private String rfType = null;

  @SerializedName("rfVersion")
  private Long rfVersion = null;

  @SerializedName("riskCondition")
  private String riskCondition = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  @SerializedName("upperLimit")
  private Long upperLimit = null;

  public RiskFactorCfg creationTime(Long creationTime) {
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

  public RiskFactorCfg deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Get deleted
   * @return deleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public RiskFactorCfg factorAttrKey(String factorAttrKey) {
    this.factorAttrKey = factorAttrKey;
    return this;
  }

   /**
   * Get factorAttrKey
   * @return factorAttrKey
  **/
  @ApiModelProperty(value = "")
  public String getFactorAttrKey() {
    return factorAttrKey;
  }

  public void setFactorAttrKey(String factorAttrKey) {
    this.factorAttrKey = factorAttrKey;
  }

  public RiskFactorCfg factorUniqueId(String factorUniqueId) {
    this.factorUniqueId = factorUniqueId;
    return this;
  }

   /**
   * Get factorUniqueId
   * @return factorUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getFactorUniqueId() {
    return factorUniqueId;
  }

  public void setFactorUniqueId(String factorUniqueId) {
    this.factorUniqueId = factorUniqueId;
  }

  public RiskFactorCfg factorWeight(Long factorWeight) {
    this.factorWeight = factorWeight;
    return this;
  }

   /**
   * Get factorWeight
   * @return factorWeight
  **/
  @ApiModelProperty(value = "")
  public Long getFactorWeight() {
    return factorWeight;
  }

  public void setFactorWeight(Long factorWeight) {
    this.factorWeight = factorWeight;
  }

  public RiskFactorCfg id(Long id) {
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

  public RiskFactorCfg lowerLimit(Long lowerLimit) {
    this.lowerLimit = lowerLimit;
    return this;
  }

   /**
   * Get lowerLimit
   * @return lowerLimit
  **/
  @ApiModelProperty(value = "")
  public Long getLowerLimit() {
    return lowerLimit;
  }

  public void setLowerLimit(Long lowerLimit) {
    this.lowerLimit = lowerLimit;
  }

  public RiskFactorCfg maxRiskValue(Long maxRiskValue) {
    this.maxRiskValue = maxRiskValue;
    return this;
  }

   /**
   * Get maxRiskValue
   * @return maxRiskValue
  **/
  @ApiModelProperty(value = "")
  public Long getMaxRiskValue() {
    return maxRiskValue;
  }

  public void setMaxRiskValue(Long maxRiskValue) {
    this.maxRiskValue = maxRiskValue;
  }

  public RiskFactorCfg name(String name) {
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

  public RiskFactorCfg operation(OperationEnum operation) {
    this.operation = operation;
    return this;
  }

   /**
   * Get operation
   * @return operation
  **/
  @ApiModelProperty(value = "")
  public OperationEnum getOperation() {
    return operation;
  }

  public void setOperation(OperationEnum operation) {
    this.operation = operation;
  }

  public RiskFactorCfg rfId(Long rfId) {
    this.rfId = rfId;
    return this;
  }

   /**
   * Get rfId
   * @return rfId
  **/
  @ApiModelProperty(value = "")
  public Long getRfId() {
    return rfId;
  }

  public void setRfId(Long rfId) {
    this.rfId = rfId;
  }

  public RiskFactorCfg rfType(String rfType) {
    this.rfType = rfType;
    return this;
  }

   /**
   * Get rfType
   * @return rfType
  **/
  @ApiModelProperty(value = "")
  public String getRfType() {
    return rfType;
  }

  public void setRfType(String rfType) {
    this.rfType = rfType;
  }

  public RiskFactorCfg rfVersion(Long rfVersion) {
    this.rfVersion = rfVersion;
    return this;
  }

   /**
   * Get rfVersion
   * @return rfVersion
  **/
  @ApiModelProperty(value = "")
  public Long getRfVersion() {
    return rfVersion;
  }

  public void setRfVersion(Long rfVersion) {
    this.rfVersion = rfVersion;
  }

  public RiskFactorCfg riskCondition(String riskCondition) {
    this.riskCondition = riskCondition;
    return this;
  }

   /**
   * Get riskCondition
   * @return riskCondition
  **/
  @ApiModelProperty(value = "")
  public String getRiskCondition() {
    return riskCondition;
  }

  public void setRiskCondition(String riskCondition) {
    this.riskCondition = riskCondition;
  }

  public RiskFactorCfg updateTime(Long updateTime) {
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

  public RiskFactorCfg upperLimit(Long upperLimit) {
    this.upperLimit = upperLimit;
    return this;
  }

   /**
   * Get upperLimit
   * @return upperLimit
  **/
  @ApiModelProperty(value = "")
  public Long getUpperLimit() {
    return upperLimit;
  }

  public void setUpperLimit(Long upperLimit) {
    this.upperLimit = upperLimit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskFactorCfg riskFactorCfg = (RiskFactorCfg) o;
    return Objects.equals(this.creationTime, riskFactorCfg.creationTime) &&
        Objects.equals(this.deleted, riskFactorCfg.deleted) &&
        Objects.equals(this.factorAttrKey, riskFactorCfg.factorAttrKey) &&
        Objects.equals(this.factorUniqueId, riskFactorCfg.factorUniqueId) &&
        Objects.equals(this.factorWeight, riskFactorCfg.factorWeight) &&
        Objects.equals(this.id, riskFactorCfg.id) &&
        Objects.equals(this.lowerLimit, riskFactorCfg.lowerLimit) &&
        Objects.equals(this.maxRiskValue, riskFactorCfg.maxRiskValue) &&
        Objects.equals(this.name, riskFactorCfg.name) &&
        Objects.equals(this.operation, riskFactorCfg.operation) &&
        Objects.equals(this.rfId, riskFactorCfg.rfId) &&
        Objects.equals(this.rfType, riskFactorCfg.rfType) &&
        Objects.equals(this.rfVersion, riskFactorCfg.rfVersion) &&
        Objects.equals(this.riskCondition, riskFactorCfg.riskCondition) &&
        Objects.equals(this.updateTime, riskFactorCfg.updateTime) &&
        Objects.equals(this.upperLimit, riskFactorCfg.upperLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationTime, deleted, factorAttrKey, factorUniqueId, factorWeight, id, lowerLimit, maxRiskValue, name, operation, rfId, rfType, rfVersion, riskCondition, updateTime, upperLimit);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskFactorCfg {\n");
    
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    factorAttrKey: ").append(toIndentedString(factorAttrKey)).append("\n");
    sb.append("    factorUniqueId: ").append(toIndentedString(factorUniqueId)).append("\n");
    sb.append("    factorWeight: ").append(toIndentedString(factorWeight)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lowerLimit: ").append(toIndentedString(lowerLimit)).append("\n");
    sb.append("    maxRiskValue: ").append(toIndentedString(maxRiskValue)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    rfId: ").append(toIndentedString(rfId)).append("\n");
    sb.append("    rfType: ").append(toIndentedString(rfType)).append("\n");
    sb.append("    rfVersion: ").append(toIndentedString(rfVersion)).append("\n");
    sb.append("    riskCondition: ").append(toIndentedString(riskCondition)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    upperLimit: ").append(toIndentedString(upperLimit)).append("\n");
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
