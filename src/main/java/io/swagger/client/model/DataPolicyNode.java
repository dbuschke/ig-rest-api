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
import io.swagger.client.model.DataProductionNode;
import io.swagger.client.model.RemediationNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * DataPolicyNode
 */



public class DataPolicyNode {
  @SerializedName("activity")
  private List<DataProductionNode> activity = null;

  @SerializedName("calcAuto")
  private Boolean calcAuto = null;

  @SerializedName("calcRunningId")
  private Long calcRunningId = null;

  @SerializedName("calculationTime")
  private Long calculationTime = null;

  @SerializedName("changedBy")
  private String changedBy = null;

  @SerializedName("createdBy")
  private String createdBy = null;

  @SerializedName("creationTime")
  private Long creationTime = null;

  @SerializedName("criteria")
  private String criteria = null;

  /**
   * Gets or Sets criteriaType
   */
  @JsonAdapter(CriteriaTypeEnum.Adapter.class)
  public enum CriteriaTypeEnum {
    @SerializedName("COUNT")
    COUNT("COUNT"),
    @SerializedName("CRITERIA")
    CRITERIA("CRITERIA"),
    @SerializedName("ENTITY_CHANGE")
    ENTITY_CHANGE("ENTITY_CHANGE"),
    @SerializedName("VALUE_CHANGE")
    VALUE_CHANGE("VALUE_CHANGE"),
    @SerializedName("ATTRIBUTE_CHANGE")
    ATTRIBUTE_CHANGE("ATTRIBUTE_CHANGE");

    private String value;

    CriteriaTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static CriteriaTypeEnum fromValue(String input) {
      for (CriteriaTypeEnum b : CriteriaTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<CriteriaTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CriteriaTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public CriteriaTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return CriteriaTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("criteriaType")
  private CriteriaTypeEnum criteriaType = null;

  /**
   * Gets or Sets dataSourceType
   */
  @JsonAdapter(DataSourceTypeEnum.Adapter.class)
  public enum DataSourceTypeEnum {
    @SerializedName("APPLICATION")
    APPLICATION("APPLICATION"),
    @SerializedName("IDENTITY")
    IDENTITY("IDENTITY"),
    @SerializedName("FULFILLMENT")
    FULFILLMENT("FULFILLMENT"),
    @SerializedName("SYSTEM_FULFILLMENT")
    SYSTEM_FULFILLMENT("SYSTEM_FULFILLMENT"),
    @SerializedName("APP_COLLECTOR_SOURCE")
    APP_COLLECTOR_SOURCE("APP_COLLECTOR_SOURCE");

    private String value;

    DataSourceTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DataSourceTypeEnum fromValue(String input) {
      for (DataSourceTypeEnum b : DataSourceTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DataSourceTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DataSourceTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public DataSourceTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DataSourceTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("dataSourceType")
  private DataSourceTypeEnum dataSourceType = null;

  @SerializedName("deleted")
  private Boolean deleted = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("lastCalculationTime")
  private Long lastCalculationTime = null;

  @SerializedName("name")
  private String name = null;

  /**
   * Gets or Sets policyType
   */
  @JsonAdapter(PolicyTypeEnum.Adapter.class)
  public enum PolicyTypeEnum {
    @SerializedName("COLLECTION")
    COLLECTION("COLLECTION"),
    @SerializedName("PUBLICATION")
    PUBLICATION("PUBLICATION");

    private String value;

    PolicyTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static PolicyTypeEnum fromValue(String input) {
      for (PolicyTypeEnum b : PolicyTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<PolicyTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PolicyTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public PolicyTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return PolicyTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("policyType")
  private PolicyTypeEnum policyType = null;

  @SerializedName("remediations")
  private List<RemediationNode> remediations = null;

  @SerializedName("secondCriteria")
  private String secondCriteria = null;

  @SerializedName("totalViolation")
  private Long totalViolation = null;

  @SerializedName("uniqueId")
  private String uniqueId = null;

  @SerializedName("updateTime")
  private Long updateTime = null;

  public DataPolicyNode activity(List<DataProductionNode> activity) {
    this.activity = activity;
    return this;
  }

  public DataPolicyNode addActivityItem(DataProductionNode activityItem) {
    if (this.activity == null) {
      this.activity = new ArrayList<DataProductionNode>();
    }
    this.activity.add(activityItem);
    return this;
  }

   /**
   * Get activity
   * @return activity
  **/
  @ApiModelProperty(value = "")
  public List<DataProductionNode> getActivity() {
    return activity;
  }

  public void setActivity(List<DataProductionNode> activity) {
    this.activity = activity;
  }

  public DataPolicyNode calcAuto(Boolean calcAuto) {
    this.calcAuto = calcAuto;
    return this;
  }

   /**
   * Get calcAuto
   * @return calcAuto
  **/
  @ApiModelProperty(value = "")
  public Boolean isCalcAuto() {
    return calcAuto;
  }

  public void setCalcAuto(Boolean calcAuto) {
    this.calcAuto = calcAuto;
  }

  public DataPolicyNode calcRunningId(Long calcRunningId) {
    this.calcRunningId = calcRunningId;
    return this;
  }

   /**
   * Get calcRunningId
   * @return calcRunningId
  **/
  @ApiModelProperty(value = "")
  public Long getCalcRunningId() {
    return calcRunningId;
  }

  public void setCalcRunningId(Long calcRunningId) {
    this.calcRunningId = calcRunningId;
  }

  public DataPolicyNode calculationTime(Long calculationTime) {
    this.calculationTime = calculationTime;
    return this;
  }

   /**
   * Get calculationTime
   * @return calculationTime
  **/
  @ApiModelProperty(value = "")
  public Long getCalculationTime() {
    return calculationTime;
  }

  public void setCalculationTime(Long calculationTime) {
    this.calculationTime = calculationTime;
  }

  public DataPolicyNode changedBy(String changedBy) {
    this.changedBy = changedBy;
    return this;
  }

   /**
   * Get changedBy
   * @return changedBy
  **/
  @ApiModelProperty(value = "")
  public String getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(String changedBy) {
    this.changedBy = changedBy;
  }

  public DataPolicyNode createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Get createdBy
   * @return createdBy
  **/
  @ApiModelProperty(value = "")
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public DataPolicyNode creationTime(Long creationTime) {
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

  public DataPolicyNode criteria(String criteria) {
    this.criteria = criteria;
    return this;
  }

   /**
   * Get criteria
   * @return criteria
  **/
  @ApiModelProperty(value = "")
  public String getCriteria() {
    return criteria;
  }

  public void setCriteria(String criteria) {
    this.criteria = criteria;
  }

  public DataPolicyNode criteriaType(CriteriaTypeEnum criteriaType) {
    this.criteriaType = criteriaType;
    return this;
  }

   /**
   * Get criteriaType
   * @return criteriaType
  **/
  @ApiModelProperty(value = "")
  public CriteriaTypeEnum getCriteriaType() {
    return criteriaType;
  }

  public void setCriteriaType(CriteriaTypeEnum criteriaType) {
    this.criteriaType = criteriaType;
  }

  public DataPolicyNode dataSourceType(DataSourceTypeEnum dataSourceType) {
    this.dataSourceType = dataSourceType;
    return this;
  }

   /**
   * Get dataSourceType
   * @return dataSourceType
  **/
  @ApiModelProperty(value = "")
  public DataSourceTypeEnum getDataSourceType() {
    return dataSourceType;
  }

  public void setDataSourceType(DataSourceTypeEnum dataSourceType) {
    this.dataSourceType = dataSourceType;
  }

  public DataPolicyNode deleted(Boolean deleted) {
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

  public DataPolicyNode description(String description) {
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

  public DataPolicyNode id(Long id) {
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

  public DataPolicyNode lastCalculationTime(Long lastCalculationTime) {
    this.lastCalculationTime = lastCalculationTime;
    return this;
  }

   /**
   * Get lastCalculationTime
   * @return lastCalculationTime
  **/
  @ApiModelProperty(value = "")
  public Long getLastCalculationTime() {
    return lastCalculationTime;
  }

  public void setLastCalculationTime(Long lastCalculationTime) {
    this.lastCalculationTime = lastCalculationTime;
  }

  public DataPolicyNode name(String name) {
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

  public DataPolicyNode policyType(PolicyTypeEnum policyType) {
    this.policyType = policyType;
    return this;
  }

   /**
   * Get policyType
   * @return policyType
  **/
  @ApiModelProperty(value = "")
  public PolicyTypeEnum getPolicyType() {
    return policyType;
  }

  public void setPolicyType(PolicyTypeEnum policyType) {
    this.policyType = policyType;
  }

  public DataPolicyNode remediations(List<RemediationNode> remediations) {
    this.remediations = remediations;
    return this;
  }

  public DataPolicyNode addRemediationsItem(RemediationNode remediationsItem) {
    if (this.remediations == null) {
      this.remediations = new ArrayList<RemediationNode>();
    }
    this.remediations.add(remediationsItem);
    return this;
  }

   /**
   * Get remediations
   * @return remediations
  **/
  @ApiModelProperty(value = "")
  public List<RemediationNode> getRemediations() {
    return remediations;
  }

  public void setRemediations(List<RemediationNode> remediations) {
    this.remediations = remediations;
  }

  public DataPolicyNode secondCriteria(String secondCriteria) {
    this.secondCriteria = secondCriteria;
    return this;
  }

   /**
   * Get secondCriteria
   * @return secondCriteria
  **/
  @ApiModelProperty(value = "")
  public String getSecondCriteria() {
    return secondCriteria;
  }

  public void setSecondCriteria(String secondCriteria) {
    this.secondCriteria = secondCriteria;
  }

  public DataPolicyNode totalViolation(Long totalViolation) {
    this.totalViolation = totalViolation;
    return this;
  }

   /**
   * Get totalViolation
   * @return totalViolation
  **/
  @ApiModelProperty(value = "")
  public Long getTotalViolation() {
    return totalViolation;
  }

  public void setTotalViolation(Long totalViolation) {
    this.totalViolation = totalViolation;
  }

  public DataPolicyNode uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

   /**
   * Get uniqueId
   * @return uniqueId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public DataPolicyNode updateTime(Long updateTime) {
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
    DataPolicyNode dataPolicyNode = (DataPolicyNode) o;
    return Objects.equals(this.activity, dataPolicyNode.activity) &&
        Objects.equals(this.calcAuto, dataPolicyNode.calcAuto) &&
        Objects.equals(this.calcRunningId, dataPolicyNode.calcRunningId) &&
        Objects.equals(this.calculationTime, dataPolicyNode.calculationTime) &&
        Objects.equals(this.changedBy, dataPolicyNode.changedBy) &&
        Objects.equals(this.createdBy, dataPolicyNode.createdBy) &&
        Objects.equals(this.creationTime, dataPolicyNode.creationTime) &&
        Objects.equals(this.criteria, dataPolicyNode.criteria) &&
        Objects.equals(this.criteriaType, dataPolicyNode.criteriaType) &&
        Objects.equals(this.dataSourceType, dataPolicyNode.dataSourceType) &&
        Objects.equals(this.deleted, dataPolicyNode.deleted) &&
        Objects.equals(this.description, dataPolicyNode.description) &&
        Objects.equals(this.id, dataPolicyNode.id) &&
        Objects.equals(this.lastCalculationTime, dataPolicyNode.lastCalculationTime) &&
        Objects.equals(this.name, dataPolicyNode.name) &&
        Objects.equals(this.policyType, dataPolicyNode.policyType) &&
        Objects.equals(this.remediations, dataPolicyNode.remediations) &&
        Objects.equals(this.secondCriteria, dataPolicyNode.secondCriteria) &&
        Objects.equals(this.totalViolation, dataPolicyNode.totalViolation) &&
        Objects.equals(this.uniqueId, dataPolicyNode.uniqueId) &&
        Objects.equals(this.updateTime, dataPolicyNode.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activity, calcAuto, calcRunningId, calculationTime, changedBy, createdBy, creationTime, criteria, criteriaType, dataSourceType, deleted, description, id, lastCalculationTime, name, policyType, remediations, secondCriteria, totalViolation, uniqueId, updateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataPolicyNode {\n");
    
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    calcAuto: ").append(toIndentedString(calcAuto)).append("\n");
    sb.append("    calcRunningId: ").append(toIndentedString(calcRunningId)).append("\n");
    sb.append("    calculationTime: ").append(toIndentedString(calculationTime)).append("\n");
    sb.append("    changedBy: ").append(toIndentedString(changedBy)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    criteria: ").append(toIndentedString(criteria)).append("\n");
    sb.append("    criteriaType: ").append(toIndentedString(criteriaType)).append("\n");
    sb.append("    dataSourceType: ").append(toIndentedString(dataSourceType)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lastCalculationTime: ").append(toIndentedString(lastCalculationTime)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    policyType: ").append(toIndentedString(policyType)).append("\n");
    sb.append("    remediations: ").append(toIndentedString(remediations)).append("\n");
    sb.append("    secondCriteria: ").append(toIndentedString(secondCriteria)).append("\n");
    sb.append("    totalViolation: ").append(toIndentedString(totalViolation)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
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