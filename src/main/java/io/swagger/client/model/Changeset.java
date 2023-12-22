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
import io.swagger.client.model.ChangeItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Changeset
 */



public class Changeset {
  @SerializedName("auto")
  private List<ChangeItem> auto = null;

  @SerializedName("changesetId")
  private Long changesetId = null;

  @SerializedName("daas")
  private List<ChangeItem> daas = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("expiryTimeout")
  private Long expiryTimeout = null;

  @SerializedName("external")
  private List<ChangeItem> external = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkReviewInstance")
  private String linkReviewInstance = null;

  @SerializedName("linkReviewOwner")
  private String linkReviewOwner = null;

  @SerializedName("manual")
  private List<ChangeItem> manual = null;

  @SerializedName("processId")
  private String processId = null;

  @SerializedName("reviewInstId")
  private Long reviewInstId = null;

  @SerializedName("reviewName")
  private String reviewName = null;

  @SerializedName("reviewOwner")
  private Long reviewOwner = null;

  @SerializedName("reviewOwnerName")
  private String reviewOwnerName = null;

  @SerializedName("startDate")
  private Long startDate = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    @SerializedName("INITIALIZED")
    INITIALIZED("INITIALIZED"),
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("PARTIAL")
    PARTIAL("PARTIAL"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("TIMED_OUT")
    TIMED_OUT("TIMED_OUT"),
    @SerializedName("ERROR")
    ERROR("ERROR");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StatusEnum fromValue(String input) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("status")
  private StatusEnum status = null;

  @SerializedName("totalSearch")
  private Integer totalSearch = null;

  public Changeset auto(List<ChangeItem> auto) {
    this.auto = auto;
    return this;
  }

  public Changeset addAutoItem(ChangeItem autoItem) {
    if (this.auto == null) {
      this.auto = new ArrayList<ChangeItem>();
    }
    this.auto.add(autoItem);
    return this;
  }

   /**
   * Get auto
   * @return auto
  **/
  @ApiModelProperty(value = "")
  public List<ChangeItem> getAuto() {
    return auto;
  }

  public void setAuto(List<ChangeItem> auto) {
    this.auto = auto;
  }

  public Changeset changesetId(Long changesetId) {
    this.changesetId = changesetId;
    return this;
  }

   /**
   * Get changesetId
   * @return changesetId
  **/
  @ApiModelProperty(value = "")
  public Long getChangesetId() {
    return changesetId;
  }

  public void setChangesetId(Long changesetId) {
    this.changesetId = changesetId;
  }

  public Changeset daas(List<ChangeItem> daas) {
    this.daas = daas;
    return this;
  }

  public Changeset addDaasItem(ChangeItem daasItem) {
    if (this.daas == null) {
      this.daas = new ArrayList<ChangeItem>();
    }
    this.daas.add(daasItem);
    return this;
  }

   /**
   * Get daas
   * @return daas
  **/
  @ApiModelProperty(value = "")
  public List<ChangeItem> getDaas() {
    return daas;
  }

  public void setDaas(List<ChangeItem> daas) {
    this.daas = daas;
  }

  public Changeset endDate(Long endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * Get endDate
   * @return endDate
  **/
  @ApiModelProperty(value = "")
  public Long getEndDate() {
    return endDate;
  }

  public void setEndDate(Long endDate) {
    this.endDate = endDate;
  }

  public Changeset expiryTimeout(Long expiryTimeout) {
    this.expiryTimeout = expiryTimeout;
    return this;
  }

   /**
   * Get expiryTimeout
   * @return expiryTimeout
  **/
  @ApiModelProperty(value = "")
  public Long getExpiryTimeout() {
    return expiryTimeout;
  }

  public void setExpiryTimeout(Long expiryTimeout) {
    this.expiryTimeout = expiryTimeout;
  }

  public Changeset external(List<ChangeItem> external) {
    this.external = external;
    return this;
  }

  public Changeset addExternalItem(ChangeItem externalItem) {
    if (this.external == null) {
      this.external = new ArrayList<ChangeItem>();
    }
    this.external.add(externalItem);
    return this;
  }

   /**
   * Get external
   * @return external
  **/
  @ApiModelProperty(value = "")
  public List<ChangeItem> getExternal() {
    return external;
  }

  public void setExternal(List<ChangeItem> external) {
    this.external = external;
  }

  public Changeset link(String link) {
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

  public Changeset linkReviewInstance(String linkReviewInstance) {
    this.linkReviewInstance = linkReviewInstance;
    return this;
  }

   /**
   * Get linkReviewInstance
   * @return linkReviewInstance
  **/
  @ApiModelProperty(value = "")
  public String getLinkReviewInstance() {
    return linkReviewInstance;
  }

  public void setLinkReviewInstance(String linkReviewInstance) {
    this.linkReviewInstance = linkReviewInstance;
  }

  public Changeset linkReviewOwner(String linkReviewOwner) {
    this.linkReviewOwner = linkReviewOwner;
    return this;
  }

   /**
   * Get linkReviewOwner
   * @return linkReviewOwner
  **/
  @ApiModelProperty(value = "")
  public String getLinkReviewOwner() {
    return linkReviewOwner;
  }

  public void setLinkReviewOwner(String linkReviewOwner) {
    this.linkReviewOwner = linkReviewOwner;
  }

  public Changeset manual(List<ChangeItem> manual) {
    this.manual = manual;
    return this;
  }

  public Changeset addManualItem(ChangeItem manualItem) {
    if (this.manual == null) {
      this.manual = new ArrayList<ChangeItem>();
    }
    this.manual.add(manualItem);
    return this;
  }

   /**
   * Get manual
   * @return manual
  **/
  @ApiModelProperty(value = "")
  public List<ChangeItem> getManual() {
    return manual;
  }

  public void setManual(List<ChangeItem> manual) {
    this.manual = manual;
  }

  public Changeset processId(String processId) {
    this.processId = processId;
    return this;
  }

   /**
   * Get processId
   * @return processId
  **/
  @ApiModelProperty(value = "")
  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
  }

  public Changeset reviewInstId(Long reviewInstId) {
    this.reviewInstId = reviewInstId;
    return this;
  }

   /**
   * Get reviewInstId
   * @return reviewInstId
  **/
  @ApiModelProperty(value = "")
  public Long getReviewInstId() {
    return reviewInstId;
  }

  public void setReviewInstId(Long reviewInstId) {
    this.reviewInstId = reviewInstId;
  }

  public Changeset reviewName(String reviewName) {
    this.reviewName = reviewName;
    return this;
  }

   /**
   * Get reviewName
   * @return reviewName
  **/
  @ApiModelProperty(value = "")
  public String getReviewName() {
    return reviewName;
  }

  public void setReviewName(String reviewName) {
    this.reviewName = reviewName;
  }

  public Changeset reviewOwner(Long reviewOwner) {
    this.reviewOwner = reviewOwner;
    return this;
  }

   /**
   * Get reviewOwner
   * @return reviewOwner
  **/
  @ApiModelProperty(value = "")
  public Long getReviewOwner() {
    return reviewOwner;
  }

  public void setReviewOwner(Long reviewOwner) {
    this.reviewOwner = reviewOwner;
  }

  public Changeset reviewOwnerName(String reviewOwnerName) {
    this.reviewOwnerName = reviewOwnerName;
    return this;
  }

   /**
   * Get reviewOwnerName
   * @return reviewOwnerName
  **/
  @ApiModelProperty(value = "")
  public String getReviewOwnerName() {
    return reviewOwnerName;
  }

  public void setReviewOwnerName(String reviewOwnerName) {
    this.reviewOwnerName = reviewOwnerName;
  }

  public Changeset startDate(Long startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * Get startDate
   * @return startDate
  **/
  @ApiModelProperty(value = "")
  public Long getStartDate() {
    return startDate;
  }

  public void setStartDate(Long startDate) {
    this.startDate = startDate;
  }

  public Changeset status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Changeset totalSearch(Integer totalSearch) {
    this.totalSearch = totalSearch;
    return this;
  }

   /**
   * Get totalSearch
   * @return totalSearch
  **/
  @ApiModelProperty(value = "")
  public Integer getTotalSearch() {
    return totalSearch;
  }

  public void setTotalSearch(Integer totalSearch) {
    this.totalSearch = totalSearch;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Changeset changeset = (Changeset) o;
    return Objects.equals(this.auto, changeset.auto) &&
        Objects.equals(this.changesetId, changeset.changesetId) &&
        Objects.equals(this.daas, changeset.daas) &&
        Objects.equals(this.endDate, changeset.endDate) &&
        Objects.equals(this.expiryTimeout, changeset.expiryTimeout) &&
        Objects.equals(this.external, changeset.external) &&
        Objects.equals(this.link, changeset.link) &&
        Objects.equals(this.linkReviewInstance, changeset.linkReviewInstance) &&
        Objects.equals(this.linkReviewOwner, changeset.linkReviewOwner) &&
        Objects.equals(this.manual, changeset.manual) &&
        Objects.equals(this.processId, changeset.processId) &&
        Objects.equals(this.reviewInstId, changeset.reviewInstId) &&
        Objects.equals(this.reviewName, changeset.reviewName) &&
        Objects.equals(this.reviewOwner, changeset.reviewOwner) &&
        Objects.equals(this.reviewOwnerName, changeset.reviewOwnerName) &&
        Objects.equals(this.startDate, changeset.startDate) &&
        Objects.equals(this.status, changeset.status) &&
        Objects.equals(this.totalSearch, changeset.totalSearch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auto, changesetId, daas, endDate, expiryTimeout, external, link, linkReviewInstance, linkReviewOwner, manual, processId, reviewInstId, reviewName, reviewOwner, reviewOwnerName, startDate, status, totalSearch);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Changeset {\n");
    
    sb.append("    auto: ").append(toIndentedString(auto)).append("\n");
    sb.append("    changesetId: ").append(toIndentedString(changesetId)).append("\n");
    sb.append("    daas: ").append(toIndentedString(daas)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    expiryTimeout: ").append(toIndentedString(expiryTimeout)).append("\n");
    sb.append("    external: ").append(toIndentedString(external)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkReviewInstance: ").append(toIndentedString(linkReviewInstance)).append("\n");
    sb.append("    linkReviewOwner: ").append(toIndentedString(linkReviewOwner)).append("\n");
    sb.append("    manual: ").append(toIndentedString(manual)).append("\n");
    sb.append("    processId: ").append(toIndentedString(processId)).append("\n");
    sb.append("    reviewInstId: ").append(toIndentedString(reviewInstId)).append("\n");
    sb.append("    reviewName: ").append(toIndentedString(reviewName)).append("\n");
    sb.append("    reviewOwner: ").append(toIndentedString(reviewOwner)).append("\n");
    sb.append("    reviewOwnerName: ").append(toIndentedString(reviewOwnerName)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    totalSearch: ").append(toIndentedString(totalSearch)).append("\n");
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
