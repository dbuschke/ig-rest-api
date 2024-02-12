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
import de.araneaconsult.codegen.ig.rest.model.Archival;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Archivals
 */



public class Archivals {
  @SerializedName("archivals")
  private List<Archival> archivals = null;

  @SerializedName("canceledCount")
  private Long canceledCount = null;

  @SerializedName("completedCount")
  private Long completedCount = null;

  @SerializedName("failedCount")
  private Long failedCount = null;

  @SerializedName("partiallyCompletedCount")
  private Long partiallyCompletedCount = null;

  @SerializedName("runningCount")
  private Long runningCount = null;

  @SerializedName("totalFilteredArchivals")
  private Long totalFilteredArchivals = null;

  @SerializedName("unfilteredCount")
  private Long unfilteredCount = null;

  public Archivals archivals(List<Archival> archivals) {
    this.archivals = archivals;
    return this;
  }

  public Archivals addArchivalsItem(Archival archivalsItem) {
    if (this.archivals == null) {
      this.archivals = new ArrayList<Archival>();
    }
    this.archivals.add(archivalsItem);
    return this;
  }

   /**
   * Get archivals
   * @return archivals
  **/
  @ApiModelProperty(value = "")
  public List<Archival> getArchivals() {
    return archivals;
  }

  public void setArchivals(List<Archival> archivals) {
    this.archivals = archivals;
  }

  public Archivals canceledCount(Long canceledCount) {
    this.canceledCount = canceledCount;
    return this;
  }

   /**
   * Get canceledCount
   * @return canceledCount
  **/
  @ApiModelProperty(value = "")
  public Long getCanceledCount() {
    return canceledCount;
  }

  public void setCanceledCount(Long canceledCount) {
    this.canceledCount = canceledCount;
  }

  public Archivals completedCount(Long completedCount) {
    this.completedCount = completedCount;
    return this;
  }

   /**
   * Get completedCount
   * @return completedCount
  **/
  @ApiModelProperty(value = "")
  public Long getCompletedCount() {
    return completedCount;
  }

  public void setCompletedCount(Long completedCount) {
    this.completedCount = completedCount;
  }

  public Archivals failedCount(Long failedCount) {
    this.failedCount = failedCount;
    return this;
  }

   /**
   * Get failedCount
   * @return failedCount
  **/
  @ApiModelProperty(value = "")
  public Long getFailedCount() {
    return failedCount;
  }

  public void setFailedCount(Long failedCount) {
    this.failedCount = failedCount;
  }

  public Archivals partiallyCompletedCount(Long partiallyCompletedCount) {
    this.partiallyCompletedCount = partiallyCompletedCount;
    return this;
  }

   /**
   * Get partiallyCompletedCount
   * @return partiallyCompletedCount
  **/
  @ApiModelProperty(value = "")
  public Long getPartiallyCompletedCount() {
    return partiallyCompletedCount;
  }

  public void setPartiallyCompletedCount(Long partiallyCompletedCount) {
    this.partiallyCompletedCount = partiallyCompletedCount;
  }

  public Archivals runningCount(Long runningCount) {
    this.runningCount = runningCount;
    return this;
  }

   /**
   * Get runningCount
   * @return runningCount
  **/
  @ApiModelProperty(value = "")
  public Long getRunningCount() {
    return runningCount;
  }

  public void setRunningCount(Long runningCount) {
    this.runningCount = runningCount;
  }

  public Archivals totalFilteredArchivals(Long totalFilteredArchivals) {
    this.totalFilteredArchivals = totalFilteredArchivals;
    return this;
  }

   /**
   * Get totalFilteredArchivals
   * @return totalFilteredArchivals
  **/
  @ApiModelProperty(value = "")
  public Long getTotalFilteredArchivals() {
    return totalFilteredArchivals;
  }

  public void setTotalFilteredArchivals(Long totalFilteredArchivals) {
    this.totalFilteredArchivals = totalFilteredArchivals;
  }

  public Archivals unfilteredCount(Long unfilteredCount) {
    this.unfilteredCount = unfilteredCount;
    return this;
  }

   /**
   * Get unfilteredCount
   * @return unfilteredCount
  **/
  @ApiModelProperty(value = "")
  public Long getUnfilteredCount() {
    return unfilteredCount;
  }

  public void setUnfilteredCount(Long unfilteredCount) {
    this.unfilteredCount = unfilteredCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Archivals archivals = (Archivals) o;
    return Objects.equals(this.archivals, archivals.archivals) &&
        Objects.equals(this.canceledCount, archivals.canceledCount) &&
        Objects.equals(this.completedCount, archivals.completedCount) &&
        Objects.equals(this.failedCount, archivals.failedCount) &&
        Objects.equals(this.partiallyCompletedCount, archivals.partiallyCompletedCount) &&
        Objects.equals(this.runningCount, archivals.runningCount) &&
        Objects.equals(this.totalFilteredArchivals, archivals.totalFilteredArchivals) &&
        Objects.equals(this.unfilteredCount, archivals.unfilteredCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(archivals, canceledCount, completedCount, failedCount, partiallyCompletedCount, runningCount, totalFilteredArchivals, unfilteredCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Archivals {\n");
    
    sb.append("    archivals: ").append(toIndentedString(archivals)).append("\n");
    sb.append("    canceledCount: ").append(toIndentedString(canceledCount)).append("\n");
    sb.append("    completedCount: ").append(toIndentedString(completedCount)).append("\n");
    sb.append("    failedCount: ").append(toIndentedString(failedCount)).append("\n");
    sb.append("    partiallyCompletedCount: ").append(toIndentedString(partiallyCompletedCount)).append("\n");
    sb.append("    runningCount: ").append(toIndentedString(runningCount)).append("\n");
    sb.append("    totalFilteredArchivals: ").append(toIndentedString(totalFilteredArchivals)).append("\n");
    sb.append("    unfilteredCount: ").append(toIndentedString(unfilteredCount)).append("\n");
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
