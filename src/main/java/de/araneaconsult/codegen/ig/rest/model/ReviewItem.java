/*
 * Identity Governance REST APIs
 * Welcome to the NetIQ Identity Governance API Documentation page. This is the API reference for the NetIQ Identity Governance REST API.  Below you will see the main sections of the API. Click each section in order to see the endpoints that are available in that category as well as information related to which Identity Governance roles have access. Global Administrators are not included in the accepted roles list for each API however they have access to all APIs. Those APIs that do not display a list of accepted roles are accessible for any role. An authenticated user is one that has the RT_ROLE_USER permission. The Operations Administrator SaaS is an example of a user that does not have the RT_ROLE_USER permission.  The NetIQ Identity Governance REST API uses the OAuth2 protocol for authentication. Therefore, in order to make any of these calls, you must obtain a token, and include that token in the Authentication header.  OSP = One SSO Provider   NAM = NetIQ Access Manager  **Note:** The various OAuth 2.0 endpoints described below can also be obtained from the OAuth/OpenID Connect provider 'metadata' found at the following location relative to the 'issuer URI':`[issuer-uri]/.well-known/openid-configuration`  Issuer URIs:  *   OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2 *   NAM: https://server/nidp/oauth/nam  See [Open ID Connect Discovery 1.0](\"https://openid.net/specs/openid-connect-discovery-1_0.html\") for more information.  Obtaining the Initial Access Token ==================================  ### OAuth 2.0 Resource Owner Password Credentials Grant Request  1.  Determine the OAuth 2.0 token endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/token     2.  NAM: https://server/nidp/oauth/nam/token 2.  Obtain the Identity Governance 'iac' client identifier and client secret.     1.  OSP         1.  The identifier is usually _iac_ but can be changed with the configutil or configupdate utilities.         2.  The client secret is the 'service password' specified during installation but can be changed with the configutil or configupdate utilities.     2.  NAM         1.  Open the Access Manager administrator console in a browser and navigate to the _OAuth & OpenID Connect_ tab on the _IDPCluster_ page. Select the _Client Applications_ heading.         2.  Click on the 'View' icon under the 'Actions' heading for the _Client Application_ named _iac_.         3.  Click on _Click to reveal_.         4.  Copy the _Client ID_ value and the _Client Secret_ value.         5.  Ensure that _Resource Owner Credentials_ appears in the _Grants Required_ list. If not, edit the client definition and check the _Resource Owner Credentials_ box, save the client definition, and update the IDP. 3.  Obtain the user identifier and password of a user with the required privilege for the desired API endpoint. 4.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 4.3.1](\"https://tools.ietf.org/html/rfc6749#section-4.3.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=password&username=<user-id>&password=[user-password]&client_id=[iac-client-id]&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the client and user values obtained in the steps above.  Also note the '**& amp;**' shown in this and other POST payload examples should actually be '**&**'. 5.  Issue the request to the OAuth 2.0 token endpoint. 6.  The JSON response will be similar to the following (see [RFC 6749 section 4.3.3](\"https://tools.ietf.org/html/rfc6749#section-4.3.3\")):`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119, \"refresh_token\": \"eyJhbGciOiJ...\" }` 7.  When issuing a REST request to an Identity Governance endpoint pass the access token value using an HTTP _Bearer_ authorization header (see [RFC 6750 section 2.1](\"https://tools.ietf.org/html/rfc6750#section-2.1\")):`Authorization: Bearer [access-token]`  Refresh Tokens ==============  If the authorization server is configured to return an OAuth 2.0 refresh token in the JSON result of the Resource Owner Password Credential Grant request then the refresh token should be used to obtain additional access tokens after the currently-valid access token expires.  In addition, each refresh token issued on behalf of a user causes a 'revocation entry' to be stored in an attribute on the user's LDAP object. Obtaining many refresh tokens without revoking previously obtained, unexpired refresh tokens will eventually exceed the capacity of the attribute on the user's LDAP object and will result in errors.  Therefore, if a refresh token is obtained it must be revoked after it is no longer needed.  ### Access Token Request  1.  Create an HTTP POST request with the following characteristics (see [RFC 6749 section 6](\"https://tools.ietf.org/html/rfc6749#section-6\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body: `grant_type=refresh_token&refresh_token=<refresh-token>&client_id=<iac-clientid>&client_secret=[iac-client-secret]`where the square-bracket-delimited names are replaced with the obvious values. 2.  Issue the request to the OAuth 2.0 token endpoint. 3.  The JSON result will be similar to`{ \"access_token\": \"eyJraWQiOiI0...\", \"token_type\": \"bearer\", \"expires_in\": 119 }` 4.  Use the new access token value in requests to Identity Governance REST endpoints as described above.  ### Refresh Token Revocation Request  1.  Determine the OAuth 2.0 token revocation endpoint for the authorization server:     1.  OSP: http(s)://server(:port)/osp/a/idm/auth/oauth2/revoke     2.  NAM: https://server/nidp/oauth/nam/revoke 2.  Create an HTTP POST request with the following characteristics (see [RFC 7009 section 2.1](\"https://tools.ietf.org/html/rfc7009#section-2.1\"))     1.  Content-Type: application/x-www-form-urlencoded     2.  POST body:`token=[refresh-token]&client_id=[iac-client-id]&client_secret=[iac-client-secret]` 3.  Issue the request to the OAuth 2.0 revocation endpoint.      As a shortcut to learning the API, run Identity Governance in a browser with developer tools enabled and watch the network traffic between the browser and the server.  REST API and Data/Service Access Rights =======================================  The authenticated user may need two kinds of rights to invoke a REST API:  1. Authorization to call the API. 2. Permission(s) to access data returned by the API or to access services the API uses.  The user's roles are checked to see if they have the required rights.  As an example, suppose that John Jones is the user, and the REST API he wants to call is **GET /data/perms/authorizedby/2/causes**, and it requires a permission named **ViewAuthResourceInfo**.  If John Jones does not have the authorization to call the API he will get an error that looks like this:  `{     \"Fault\": {        \"Code\": {           \"Value\": \"Sender\",           \"Subcode\": {              \"Value\": \"RestApiUnAuthorized\"           }        },        \"Reason\": {           \"Text\": \"Denying access to /data/perms/authorizedby/2/causes for user John Jones.\"        }     } }`  If John Jones is allowed to call the REST API, but does not have the **ViewAuthResourceInfo** permission, he would get an error that looks like this:  `{     \"Fault\": {        \"Code\": {           \"Value\": \"Sender\",           \"Subcode\": {              \"Value\": \"UnauthorizedDataAccess\"           }        },        \"Reason\": {           \"Text\": \"Unauthorized data access attempt: User [John Jones] has no right [ViewAuthResourceInfo] for requested data.\",           \"Stack\": null        }     } }`  To determine what permissions a particular role has, you can query the OPS database.  For example, the query below returns all permissions for the **Fulfillment Administrator** role.  To get permissions for a different role, just change **'Fulfillment Administrator'** value to the name of the role you want:  ` SELECT distinct     ar.role_name as role_short_name,     ar.role_display_name as role_display_name,     ap.permission_name as permission_name FROM auth_role_mapping arm JOIN auth_role ar on ar.id = arm.auth_role JOIN auth_scope asp on asp.id = arm.auth_scope JOIN auth_permission ap on ap.id = arm.auth_perm WHERE     asp.rest_api_uri IS NULL     and ar.role_display_name = 'Fulfillment Administrator' order by ar.role_display_name, ap.permission_name `  * * *
 *
 * OpenAPI spec version: 4.2.0-644
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package de.araneaconsult.codegen.ig.rest.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import de.araneaconsult.codegen.ig.rest.model.ReviewItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ReviewItem
 */



public class ReviewItem {
  @SerializedName("avgReviewOrder")
  private Double avgReviewOrder = null;

  @SerializedName("count")
  private Integer count = null;

  @SerializedName("itemCountActed")
  private Long itemCountActed = null;

  @SerializedName("itemCountCertified")
  private Long itemCountCertified = null;

  @SerializedName("itemCountInProgress")
  private Long itemCountInProgress = null;

  @SerializedName("itemCountOverridden")
  private Long itemCountOverridden = null;

  @SerializedName("itemCountTotal")
  private Long itemCountTotal = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkReassign")
  private String linkReassign = null;

  @SerializedName("linkTask")
  private String linkTask = null;

  @SerializedName("taskType")
  private String taskType = null;

  @SerializedName("uniqueUserId")
  private String uniqueUserId = null;

  @SerializedName("userDn")
  private String userDn = null;

  @SerializedName("userId")
  private Long userId = null;

  @SerializedName("userName")
  private String userName = null;

  @SerializedName("users")
  private List<ReviewItem> users = null;

  public ReviewItem avgReviewOrder(Double avgReviewOrder) {
    this.avgReviewOrder = avgReviewOrder;
    return this;
  }

   /**
   * Get avgReviewOrder
   * @return avgReviewOrder
  **/
  @ApiModelProperty(value = "")
  public Double getAvgReviewOrder() {
    return avgReviewOrder;
  }

  public void setAvgReviewOrder(Double avgReviewOrder) {
    this.avgReviewOrder = avgReviewOrder;
  }

  public ReviewItem count(Integer count) {
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

  public ReviewItem itemCountActed(Long itemCountActed) {
    this.itemCountActed = itemCountActed;
    return this;
  }

   /**
   * Get itemCountActed
   * @return itemCountActed
  **/
  @ApiModelProperty(value = "")
  public Long getItemCountActed() {
    return itemCountActed;
  }

  public void setItemCountActed(Long itemCountActed) {
    this.itemCountActed = itemCountActed;
  }

  public ReviewItem itemCountCertified(Long itemCountCertified) {
    this.itemCountCertified = itemCountCertified;
    return this;
  }

   /**
   * Get itemCountCertified
   * @return itemCountCertified
  **/
  @ApiModelProperty(value = "")
  public Long getItemCountCertified() {
    return itemCountCertified;
  }

  public void setItemCountCertified(Long itemCountCertified) {
    this.itemCountCertified = itemCountCertified;
  }

  public ReviewItem itemCountInProgress(Long itemCountInProgress) {
    this.itemCountInProgress = itemCountInProgress;
    return this;
  }

   /**
   * Get itemCountInProgress
   * @return itemCountInProgress
  **/
  @ApiModelProperty(value = "")
  public Long getItemCountInProgress() {
    return itemCountInProgress;
  }

  public void setItemCountInProgress(Long itemCountInProgress) {
    this.itemCountInProgress = itemCountInProgress;
  }

  public ReviewItem itemCountOverridden(Long itemCountOverridden) {
    this.itemCountOverridden = itemCountOverridden;
    return this;
  }

   /**
   * Get itemCountOverridden
   * @return itemCountOverridden
  **/
  @ApiModelProperty(value = "")
  public Long getItemCountOverridden() {
    return itemCountOverridden;
  }

  public void setItemCountOverridden(Long itemCountOverridden) {
    this.itemCountOverridden = itemCountOverridden;
  }

  public ReviewItem itemCountTotal(Long itemCountTotal) {
    this.itemCountTotal = itemCountTotal;
    return this;
  }

   /**
   * Get itemCountTotal
   * @return itemCountTotal
  **/
  @ApiModelProperty(value = "")
  public Long getItemCountTotal() {
    return itemCountTotal;
  }

  public void setItemCountTotal(Long itemCountTotal) {
    this.itemCountTotal = itemCountTotal;
  }

  public ReviewItem link(String link) {
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

  public ReviewItem linkReassign(String linkReassign) {
    this.linkReassign = linkReassign;
    return this;
  }

   /**
   * Get linkReassign
   * @return linkReassign
  **/
  @ApiModelProperty(value = "")
  public String getLinkReassign() {
    return linkReassign;
  }

  public void setLinkReassign(String linkReassign) {
    this.linkReassign = linkReassign;
  }

  public ReviewItem linkTask(String linkTask) {
    this.linkTask = linkTask;
    return this;
  }

   /**
   * Get linkTask
   * @return linkTask
  **/
  @ApiModelProperty(value = "")
  public String getLinkTask() {
    return linkTask;
  }

  public void setLinkTask(String linkTask) {
    this.linkTask = linkTask;
  }

  public ReviewItem taskType(String taskType) {
    this.taskType = taskType;
    return this;
  }

   /**
   * Get taskType
   * @return taskType
  **/
  @ApiModelProperty(value = "")
  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  public ReviewItem uniqueUserId(String uniqueUserId) {
    this.uniqueUserId = uniqueUserId;
    return this;
  }

   /**
   * Get uniqueUserId
   * @return uniqueUserId
  **/
  @ApiModelProperty(value = "")
  public String getUniqueUserId() {
    return uniqueUserId;
  }

  public void setUniqueUserId(String uniqueUserId) {
    this.uniqueUserId = uniqueUserId;
  }

  public ReviewItem userDn(String userDn) {
    this.userDn = userDn;
    return this;
  }

   /**
   * Get userDn
   * @return userDn
  **/
  @ApiModelProperty(value = "")
  public String getUserDn() {
    return userDn;
  }

  public void setUserDn(String userDn) {
    this.userDn = userDn;
  }

  public ReviewItem userId(Long userId) {
    this.userId = userId;
    return this;
  }

   /**
   * Get userId
   * @return userId
  **/
  @ApiModelProperty(value = "")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public ReviewItem userName(String userName) {
    this.userName = userName;
    return this;
  }

   /**
   * Get userName
   * @return userName
  **/
  @ApiModelProperty(value = "")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public ReviewItem users(List<ReviewItem> users) {
    this.users = users;
    return this;
  }

  public ReviewItem addUsersItem(ReviewItem usersItem) {
    if (this.users == null) {
      this.users = new ArrayList<ReviewItem>();
    }
    this.users.add(usersItem);
    return this;
  }

   /**
   * Get users
   * @return users
  **/
  @ApiModelProperty(value = "")
  public List<ReviewItem> getUsers() {
    return users;
  }

  public void setUsers(List<ReviewItem> users) {
    this.users = users;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewItem reviewItem = (ReviewItem) o;
    return Objects.equals(this.avgReviewOrder, reviewItem.avgReviewOrder) &&
        Objects.equals(this.count, reviewItem.count) &&
        Objects.equals(this.itemCountActed, reviewItem.itemCountActed) &&
        Objects.equals(this.itemCountCertified, reviewItem.itemCountCertified) &&
        Objects.equals(this.itemCountInProgress, reviewItem.itemCountInProgress) &&
        Objects.equals(this.itemCountOverridden, reviewItem.itemCountOverridden) &&
        Objects.equals(this.itemCountTotal, reviewItem.itemCountTotal) &&
        Objects.equals(this.link, reviewItem.link) &&
        Objects.equals(this.linkReassign, reviewItem.linkReassign) &&
        Objects.equals(this.linkTask, reviewItem.linkTask) &&
        Objects.equals(this.taskType, reviewItem.taskType) &&
        Objects.equals(this.uniqueUserId, reviewItem.uniqueUserId) &&
        Objects.equals(this.userDn, reviewItem.userDn) &&
        Objects.equals(this.userId, reviewItem.userId) &&
        Objects.equals(this.userName, reviewItem.userName) &&
        Objects.equals(this.users, reviewItem.users);
  }

  @Override
  public int hashCode() {
    return Objects.hash(avgReviewOrder, count, itemCountActed, itemCountCertified, itemCountInProgress, itemCountOverridden, itemCountTotal, link, linkReassign, linkTask, taskType, uniqueUserId, userDn, userId, userName, users);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewItem {\n");
    
    sb.append("    avgReviewOrder: ").append(toIndentedString(avgReviewOrder)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    itemCountActed: ").append(toIndentedString(itemCountActed)).append("\n");
    sb.append("    itemCountCertified: ").append(toIndentedString(itemCountCertified)).append("\n");
    sb.append("    itemCountInProgress: ").append(toIndentedString(itemCountInProgress)).append("\n");
    sb.append("    itemCountOverridden: ").append(toIndentedString(itemCountOverridden)).append("\n");
    sb.append("    itemCountTotal: ").append(toIndentedString(itemCountTotal)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkReassign: ").append(toIndentedString(linkReassign)).append("\n");
    sb.append("    linkTask: ").append(toIndentedString(linkTask)).append("\n");
    sb.append("    taskType: ").append(toIndentedString(taskType)).append("\n");
    sb.append("    uniqueUserId: ").append(toIndentedString(uniqueUserId)).append("\n");
    sb.append("    userDn: ").append(toIndentedString(userDn)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
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
