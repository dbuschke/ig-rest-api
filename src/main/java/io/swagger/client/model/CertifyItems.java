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
import io.swagger.client.model.ApprovalTask;
import io.swagger.client.model.ReviewItemAction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * CertifyItems
 */



public class CertifyItems {
  @SerializedName("approvalItems")
  private List<ApprovalTask> approvalItems = null;

  @SerializedName("certify")
  private Boolean certify = null;

  @SerializedName("items")
  private List<ReviewItemAction> items = null;

  @SerializedName("itemsApprovedOrDenied")
  private Integer itemsApprovedOrDenied = null;

  @SerializedName("itemsCertified")
  private Integer itemsCertified = null;

  @SerializedName("itemsFailed")
  private List<ReviewItemAction> itemsFailed = null;

  @SerializedName("itemsFailedSelfReview")
  private Integer itemsFailedSelfReview = null;

  @SerializedName("itemsUpdated")
  private Integer itemsUpdated = null;

  public CertifyItems approvalItems(List<ApprovalTask> approvalItems) {
    this.approvalItems = approvalItems;
    return this;
  }

  public CertifyItems addApprovalItemsItem(ApprovalTask approvalItemsItem) {
    if (this.approvalItems == null) {
      this.approvalItems = new ArrayList<ApprovalTask>();
    }
    this.approvalItems.add(approvalItemsItem);
    return this;
  }

   /**
   * Get approvalItems
   * @return approvalItems
  **/
  @ApiModelProperty(value = "")
  public List<ApprovalTask> getApprovalItems() {
    return approvalItems;
  }

  public void setApprovalItems(List<ApprovalTask> approvalItems) {
    this.approvalItems = approvalItems;
  }

  public CertifyItems certify(Boolean certify) {
    this.certify = certify;
    return this;
  }

   /**
   * Get certify
   * @return certify
  **/
  @ApiModelProperty(value = "")
  public Boolean isCertify() {
    return certify;
  }

  public void setCertify(Boolean certify) {
    this.certify = certify;
  }

  public CertifyItems items(List<ReviewItemAction> items) {
    this.items = items;
    return this;
  }

  public CertifyItems addItemsItem(ReviewItemAction itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<ReviewItemAction>();
    }
    this.items.add(itemsItem);
    return this;
  }

   /**
   * Get items
   * @return items
  **/
  @ApiModelProperty(value = "")
  public List<ReviewItemAction> getItems() {
    return items;
  }

  public void setItems(List<ReviewItemAction> items) {
    this.items = items;
  }

  public CertifyItems itemsApprovedOrDenied(Integer itemsApprovedOrDenied) {
    this.itemsApprovedOrDenied = itemsApprovedOrDenied;
    return this;
  }

   /**
   * Get itemsApprovedOrDenied
   * @return itemsApprovedOrDenied
  **/
  @ApiModelProperty(value = "")
  public Integer getItemsApprovedOrDenied() {
    return itemsApprovedOrDenied;
  }

  public void setItemsApprovedOrDenied(Integer itemsApprovedOrDenied) {
    this.itemsApprovedOrDenied = itemsApprovedOrDenied;
  }

  public CertifyItems itemsCertified(Integer itemsCertified) {
    this.itemsCertified = itemsCertified;
    return this;
  }

   /**
   * Get itemsCertified
   * @return itemsCertified
  **/
  @ApiModelProperty(value = "")
  public Integer getItemsCertified() {
    return itemsCertified;
  }

  public void setItemsCertified(Integer itemsCertified) {
    this.itemsCertified = itemsCertified;
  }

  public CertifyItems itemsFailed(List<ReviewItemAction> itemsFailed) {
    this.itemsFailed = itemsFailed;
    return this;
  }

  public CertifyItems addItemsFailedItem(ReviewItemAction itemsFailedItem) {
    if (this.itemsFailed == null) {
      this.itemsFailed = new ArrayList<ReviewItemAction>();
    }
    this.itemsFailed.add(itemsFailedItem);
    return this;
  }

   /**
   * Get itemsFailed
   * @return itemsFailed
  **/
  @ApiModelProperty(value = "")
  public List<ReviewItemAction> getItemsFailed() {
    return itemsFailed;
  }

  public void setItemsFailed(List<ReviewItemAction> itemsFailed) {
    this.itemsFailed = itemsFailed;
  }

  public CertifyItems itemsFailedSelfReview(Integer itemsFailedSelfReview) {
    this.itemsFailedSelfReview = itemsFailedSelfReview;
    return this;
  }

   /**
   * Get itemsFailedSelfReview
   * @return itemsFailedSelfReview
  **/
  @ApiModelProperty(value = "")
  public Integer getItemsFailedSelfReview() {
    return itemsFailedSelfReview;
  }

  public void setItemsFailedSelfReview(Integer itemsFailedSelfReview) {
    this.itemsFailedSelfReview = itemsFailedSelfReview;
  }

  public CertifyItems itemsUpdated(Integer itemsUpdated) {
    this.itemsUpdated = itemsUpdated;
    return this;
  }

   /**
   * Get itemsUpdated
   * @return itemsUpdated
  **/
  @ApiModelProperty(value = "")
  public Integer getItemsUpdated() {
    return itemsUpdated;
  }

  public void setItemsUpdated(Integer itemsUpdated) {
    this.itemsUpdated = itemsUpdated;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CertifyItems certifyItems = (CertifyItems) o;
    return Objects.equals(this.approvalItems, certifyItems.approvalItems) &&
        Objects.equals(this.certify, certifyItems.certify) &&
        Objects.equals(this.items, certifyItems.items) &&
        Objects.equals(this.itemsApprovedOrDenied, certifyItems.itemsApprovedOrDenied) &&
        Objects.equals(this.itemsCertified, certifyItems.itemsCertified) &&
        Objects.equals(this.itemsFailed, certifyItems.itemsFailed) &&
        Objects.equals(this.itemsFailedSelfReview, certifyItems.itemsFailedSelfReview) &&
        Objects.equals(this.itemsUpdated, certifyItems.itemsUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvalItems, certify, items, itemsApprovedOrDenied, itemsCertified, itemsFailed, itemsFailedSelfReview, itemsUpdated);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CertifyItems {\n");
    
    sb.append("    approvalItems: ").append(toIndentedString(approvalItems)).append("\n");
    sb.append("    certify: ").append(toIndentedString(certify)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    itemsApprovedOrDenied: ").append(toIndentedString(itemsApprovedOrDenied)).append("\n");
    sb.append("    itemsCertified: ").append(toIndentedString(itemsCertified)).append("\n");
    sb.append("    itemsFailed: ").append(toIndentedString(itemsFailed)).append("\n");
    sb.append("    itemsFailedSelfReview: ").append(toIndentedString(itemsFailedSelfReview)).append("\n");
    sb.append("    itemsUpdated: ").append(toIndentedString(itemsUpdated)).append("\n");
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