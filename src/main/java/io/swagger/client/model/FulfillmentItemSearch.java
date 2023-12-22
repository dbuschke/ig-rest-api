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
import io.swagger.client.model.SearchCriteria;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * FulfillmentItemSearch
 */



public class FulfillmentItemSearch {
  @SerializedName("appId")
  private Long appId = null;

  @SerializedName("changesetId")
  private Long changesetId = null;

  @SerializedName("comment")
  private String comment = null;

  @SerializedName("fufillerUniqueId")
  private String fufillerUniqueId = null;

  /**
   * Gets or Sets fulfillmentItemType
   */
  @JsonAdapter(FulfillmentItemTypeEnum.Adapter.class)
  public enum FulfillmentItemTypeEnum {
    @SerializedName("USER_REVIEW")
    USER_REVIEW("USER_REVIEW"),
    @SerializedName("ORPHAN_ACCOUNT")
    ORPHAN_ACCOUNT("ORPHAN_ACCOUNT"),
    @SerializedName("SOD")
    SOD("SOD"),
    @SerializedName("BUSINESS_ROLE")
    BUSINESS_ROLE("BUSINESS_ROLE"),
    @SerializedName("ALL_USER")
    ALL_USER("ALL_USER"),
    @SerializedName("ALL_ACCOUNT")
    ALL_ACCOUNT("ALL_ACCOUNT"),
    @SerializedName("ALL_ACCESS_REQUESTS")
    ALL_ACCESS_REQUESTS("ALL_ACCESS_REQUESTS"),
    @SerializedName("ALL_USER_MODIFY")
    ALL_USER_MODIFY("ALL_USER_MODIFY");

    private String value;

    FulfillmentItemTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static FulfillmentItemTypeEnum fromValue(String input) {
      for (FulfillmentItemTypeEnum b : FulfillmentItemTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<FulfillmentItemTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FulfillmentItemTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public FulfillmentItemTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return FulfillmentItemTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("fulfillmentItemType")
  private FulfillmentItemTypeEnum fulfillmentItemType = null;

  /**
   * Gets or Sets fulfillmentStatus
   */
  @JsonAdapter(FulfillmentStatusEnum.Adapter.class)
  public enum FulfillmentStatusEnum {
    @SerializedName("INITIALIZED")
    INITIALIZED("INITIALIZED"),
    @SerializedName("PENDING")
    PENDING("PENDING"),
    @SerializedName("TIMED_OUT")
    TIMED_OUT("TIMED_OUT"),
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS("IN_PROGRESS"),
    @SerializedName("REFUSED")
    REFUSED("REFUSED"),
    @SerializedName("COMPLETED")
    COMPLETED("COMPLETED"),
    @SerializedName("ERROR")
    ERROR("ERROR"),
    @SerializedName("VERIFIED")
    VERIFIED("VERIFIED"),
    @SerializedName("NOT_VERIFIED")
    NOT_VERIFIED("NOT_VERIFIED"),
    @SerializedName("VERIFICATION_IGNORED")
    VERIFICATION_IGNORED("VERIFICATION_IGNORED"),
    @SerializedName("VERIFICATION_TIMEOUT")
    VERIFICATION_TIMEOUT("VERIFICATION_TIMEOUT"),
    @SerializedName("RETRY")
    RETRY("RETRY");

    private String value;

    FulfillmentStatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static FulfillmentStatusEnum fromValue(String input) {
      for (FulfillmentStatusEnum b : FulfillmentStatusEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<FulfillmentStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FulfillmentStatusEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public FulfillmentStatusEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return FulfillmentStatusEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("fulfillmentStatus")
  private List<FulfillmentStatusEnum> fulfillmentStatus = null;

  /**
   * Gets or Sets fulfillmentTypeList
   */
  @JsonAdapter(FulfillmentTypeListEnum.Adapter.class)
  public enum FulfillmentTypeListEnum {
    @SerializedName("MANUAL")
    MANUAL("MANUAL"),
    @SerializedName("IDMWF")
    IDMWF("IDMWF"),
    @SerializedName("EXTERNAL")
    EXTERNAL("EXTERNAL"),
    @SerializedName("DAAS")
    DAAS("DAAS");

    private String value;

    FulfillmentTypeListEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static FulfillmentTypeListEnum fromValue(String input) {
      for (FulfillmentTypeListEnum b : FulfillmentTypeListEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<FulfillmentTypeListEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FulfillmentTypeListEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public FulfillmentTypeListEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return FulfillmentTypeListEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("fulfillmentTypeList")
  private List<FulfillmentTypeListEnum> fulfillmentTypeList = null;

  @SerializedName("indexFrom")
  private Integer indexFrom = null;

  @SerializedName("q")
  private String q = null;

  @SerializedName("queryColumns")
  private List<String> queryColumns = null;

  @SerializedName("searchCriteria")
  private SearchCriteria searchCriteria = null;

  @SerializedName("size")
  private Integer size = null;

  @SerializedName("sort")
  private String sort = null;

  @SerializedName("sortAscending")
  private Boolean sortAscending = null;

  public FulfillmentItemSearch appId(Long appId) {
    this.appId = appId;
    return this;
  }

   /**
   * Get appId
   * @return appId
  **/
  @ApiModelProperty(value = "")
  public Long getAppId() {
    return appId;
  }

  public void setAppId(Long appId) {
    this.appId = appId;
  }

  public FulfillmentItemSearch changesetId(Long changesetId) {
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

  public FulfillmentItemSearch comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @ApiModelProperty(value = "")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public FulfillmentItemSearch fufillerUniqueId(String fufillerUniqueId) {
    this.fufillerUniqueId = fufillerUniqueId;
    return this;
  }

   /**
   * Get fufillerUniqueId
   * @return fufillerUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getFufillerUniqueId() {
    return fufillerUniqueId;
  }

  public void setFufillerUniqueId(String fufillerUniqueId) {
    this.fufillerUniqueId = fufillerUniqueId;
  }

  public FulfillmentItemSearch fulfillmentItemType(FulfillmentItemTypeEnum fulfillmentItemType) {
    this.fulfillmentItemType = fulfillmentItemType;
    return this;
  }

   /**
   * Get fulfillmentItemType
   * @return fulfillmentItemType
  **/
  @ApiModelProperty(value = "")
  public FulfillmentItemTypeEnum getFulfillmentItemType() {
    return fulfillmentItemType;
  }

  public void setFulfillmentItemType(FulfillmentItemTypeEnum fulfillmentItemType) {
    this.fulfillmentItemType = fulfillmentItemType;
  }

  public FulfillmentItemSearch fulfillmentStatus(List<FulfillmentStatusEnum> fulfillmentStatus) {
    this.fulfillmentStatus = fulfillmentStatus;
    return this;
  }

  public FulfillmentItemSearch addFulfillmentStatusItem(FulfillmentStatusEnum fulfillmentStatusItem) {
    if (this.fulfillmentStatus == null) {
      this.fulfillmentStatus = new ArrayList<FulfillmentStatusEnum>();
    }
    this.fulfillmentStatus.add(fulfillmentStatusItem);
    return this;
  }

   /**
   * Get fulfillmentStatus
   * @return fulfillmentStatus
  **/
  @ApiModelProperty(value = "")
  public List<FulfillmentStatusEnum> getFulfillmentStatus() {
    return fulfillmentStatus;
  }

  public void setFulfillmentStatus(List<FulfillmentStatusEnum> fulfillmentStatus) {
    this.fulfillmentStatus = fulfillmentStatus;
  }

  public FulfillmentItemSearch fulfillmentTypeList(List<FulfillmentTypeListEnum> fulfillmentTypeList) {
    this.fulfillmentTypeList = fulfillmentTypeList;
    return this;
  }

  public FulfillmentItemSearch addFulfillmentTypeListItem(FulfillmentTypeListEnum fulfillmentTypeListItem) {
    if (this.fulfillmentTypeList == null) {
      this.fulfillmentTypeList = new ArrayList<FulfillmentTypeListEnum>();
    }
    this.fulfillmentTypeList.add(fulfillmentTypeListItem);
    return this;
  }

   /**
   * Get fulfillmentTypeList
   * @return fulfillmentTypeList
  **/
  @ApiModelProperty(value = "")
  public List<FulfillmentTypeListEnum> getFulfillmentTypeList() {
    return fulfillmentTypeList;
  }

  public void setFulfillmentTypeList(List<FulfillmentTypeListEnum> fulfillmentTypeList) {
    this.fulfillmentTypeList = fulfillmentTypeList;
  }

  public FulfillmentItemSearch indexFrom(Integer indexFrom) {
    this.indexFrom = indexFrom;
    return this;
  }

   /**
   * Get indexFrom
   * @return indexFrom
  **/
  @ApiModelProperty(value = "")
  public Integer getIndexFrom() {
    return indexFrom;
  }

  public void setIndexFrom(Integer indexFrom) {
    this.indexFrom = indexFrom;
  }

  public FulfillmentItemSearch q(String q) {
    this.q = q;
    return this;
  }

   /**
   * Get q
   * @return q
  **/
  @ApiModelProperty(value = "")
  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public FulfillmentItemSearch queryColumns(List<String> queryColumns) {
    this.queryColumns = queryColumns;
    return this;
  }

  public FulfillmentItemSearch addQueryColumnsItem(String queryColumnsItem) {
    if (this.queryColumns == null) {
      this.queryColumns = new ArrayList<String>();
    }
    this.queryColumns.add(queryColumnsItem);
    return this;
  }

   /**
   * Get queryColumns
   * @return queryColumns
  **/
  @ApiModelProperty(value = "")
  public List<String> getQueryColumns() {
    return queryColumns;
  }

  public void setQueryColumns(List<String> queryColumns) {
    this.queryColumns = queryColumns;
  }

  public FulfillmentItemSearch searchCriteria(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
    return this;
  }

   /**
   * Get searchCriteria
   * @return searchCriteria
  **/
  @ApiModelProperty(value = "")
  public SearchCriteria getSearchCriteria() {
    return searchCriteria;
  }

  public void setSearchCriteria(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
  }

  public FulfillmentItemSearch size(Integer size) {
    this.size = size;
    return this;
  }

   /**
   * Get size
   * @return size
  **/
  @ApiModelProperty(value = "")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public FulfillmentItemSearch sort(String sort) {
    this.sort = sort;
    return this;
  }

   /**
   * Get sort
   * @return sort
  **/
  @ApiModelProperty(value = "")
  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public FulfillmentItemSearch sortAscending(Boolean sortAscending) {
    this.sortAscending = sortAscending;
    return this;
  }

   /**
   * Get sortAscending
   * @return sortAscending
  **/
  @ApiModelProperty(value = "")
  public Boolean isSortAscending() {
    return sortAscending;
  }

  public void setSortAscending(Boolean sortAscending) {
    this.sortAscending = sortAscending;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FulfillmentItemSearch fulfillmentItemSearch = (FulfillmentItemSearch) o;
    return Objects.equals(this.appId, fulfillmentItemSearch.appId) &&
        Objects.equals(this.changesetId, fulfillmentItemSearch.changesetId) &&
        Objects.equals(this.comment, fulfillmentItemSearch.comment) &&
        Objects.equals(this.fufillerUniqueId, fulfillmentItemSearch.fufillerUniqueId) &&
        Objects.equals(this.fulfillmentItemType, fulfillmentItemSearch.fulfillmentItemType) &&
        Objects.equals(this.fulfillmentStatus, fulfillmentItemSearch.fulfillmentStatus) &&
        Objects.equals(this.fulfillmentTypeList, fulfillmentItemSearch.fulfillmentTypeList) &&
        Objects.equals(this.indexFrom, fulfillmentItemSearch.indexFrom) &&
        Objects.equals(this.q, fulfillmentItemSearch.q) &&
        Objects.equals(this.queryColumns, fulfillmentItemSearch.queryColumns) &&
        Objects.equals(this.searchCriteria, fulfillmentItemSearch.searchCriteria) &&
        Objects.equals(this.size, fulfillmentItemSearch.size) &&
        Objects.equals(this.sort, fulfillmentItemSearch.sort) &&
        Objects.equals(this.sortAscending, fulfillmentItemSearch.sortAscending);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, changesetId, comment, fufillerUniqueId, fulfillmentItemType, fulfillmentStatus, fulfillmentTypeList, indexFrom, q, queryColumns, searchCriteria, size, sort, sortAscending);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FulfillmentItemSearch {\n");
    
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    changesetId: ").append(toIndentedString(changesetId)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    fufillerUniqueId: ").append(toIndentedString(fufillerUniqueId)).append("\n");
    sb.append("    fulfillmentItemType: ").append(toIndentedString(fulfillmentItemType)).append("\n");
    sb.append("    fulfillmentStatus: ").append(toIndentedString(fulfillmentStatus)).append("\n");
    sb.append("    fulfillmentTypeList: ").append(toIndentedString(fulfillmentTypeList)).append("\n");
    sb.append("    indexFrom: ").append(toIndentedString(indexFrom)).append("\n");
    sb.append("    q: ").append(toIndentedString(q)).append("\n");
    sb.append("    queryColumns: ").append(toIndentedString(queryColumns)).append("\n");
    sb.append("    searchCriteria: ").append(toIndentedString(searchCriteria)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    sort: ").append(toIndentedString(sort)).append("\n");
    sb.append("    sortAscending: ").append(toIndentedString(sortAscending)).append("\n");
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
