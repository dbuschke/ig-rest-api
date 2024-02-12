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
import de.araneaconsult.codegen.ig.rest.model.RequestItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Request
 */



public class Request {
  @SerializedName("commentRequired")
  private Boolean commentRequired = null;

  @SerializedName("fulfillmentTarget")
  private Long fulfillmentTarget = null;

  @SerializedName("reason")
  private String reason = null;

  @SerializedName("requestItems")
  private List<RequestItem> requestItems = null;

  /**
   * Gets or Sets requestSource
   */
  @JsonAdapter(RequestSourceEnum.Adapter.class)
  public enum RequestSourceEnum {
    @SerializedName("REVIEW")
    REVIEW("REVIEW"),
    @SerializedName("SOD")
    SOD("SOD"),
    @SerializedName("BUSINESS_ROLE_AUTO_FULFILL")
    BUSINESS_ROLE_AUTO_FULFILL("BUSINESS_ROLE_AUTO_FULFILL"),
    @SerializedName("REQUEST")
    REQUEST("REQUEST"),
    @SerializedName("DATA_POLICY_REMEDIATION")
    DATA_POLICY_REMEDIATION("DATA_POLICY_REMEDIATION"),
    @SerializedName("CERTIFICATION_POLICY_REMEDIATION")
    CERTIFICATION_POLICY_REMEDIATION("CERTIFICATION_POLICY_REMEDIATION"),
    @SerializedName("SCRIPTED_CHANGESET_POLICY_MODIFICATION")
    SCRIPTED_CHANGESET_POLICY_MODIFICATION("SCRIPTED_CHANGESET_POLICY_MODIFICATION");

    private String value;

    RequestSourceEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RequestSourceEnum fromValue(String input) {
      for (RequestSourceEnum b : RequestSourceEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RequestSourceEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RequestSourceEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RequestSourceEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RequestSourceEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("requestSource")
  private RequestSourceEnum requestSource = null;

  @SerializedName("requester")
  private String requester = null;

  @SerializedName("requesterDeleted")
  private Boolean requesterDeleted = null;

  @SerializedName("requesterDisplayName")
  private String requesterDisplayName = null;

  public Request commentRequired(Boolean commentRequired) {
    this.commentRequired = commentRequired;
    return this;
  }

   /**
   * Get commentRequired
   * @return commentRequired
  **/
  @ApiModelProperty(value = "")
  public Boolean isCommentRequired() {
    return commentRequired;
  }

  public void setCommentRequired(Boolean commentRequired) {
    this.commentRequired = commentRequired;
  }

  public Request fulfillmentTarget(Long fulfillmentTarget) {
    this.fulfillmentTarget = fulfillmentTarget;
    return this;
  }

   /**
   * Get fulfillmentTarget
   * @return fulfillmentTarget
  **/
  @ApiModelProperty(value = "")
  public Long getFulfillmentTarget() {
    return fulfillmentTarget;
  }

  public void setFulfillmentTarget(Long fulfillmentTarget) {
    this.fulfillmentTarget = fulfillmentTarget;
  }

  public Request reason(String reason) {
    this.reason = reason;
    return this;
  }

   /**
   * Get reason
   * @return reason
  **/
  @ApiModelProperty(value = "")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public Request requestItems(List<RequestItem> requestItems) {
    this.requestItems = requestItems;
    return this;
  }

  public Request addRequestItemsItem(RequestItem requestItemsItem) {
    if (this.requestItems == null) {
      this.requestItems = new ArrayList<RequestItem>();
    }
    this.requestItems.add(requestItemsItem);
    return this;
  }

   /**
   * Get requestItems
   * @return requestItems
  **/
  @ApiModelProperty(value = "")
  public List<RequestItem> getRequestItems() {
    return requestItems;
  }

  public void setRequestItems(List<RequestItem> requestItems) {
    this.requestItems = requestItems;
  }

  public Request requestSource(RequestSourceEnum requestSource) {
    this.requestSource = requestSource;
    return this;
  }

   /**
   * Get requestSource
   * @return requestSource
  **/
  @ApiModelProperty(value = "")
  public RequestSourceEnum getRequestSource() {
    return requestSource;
  }

  public void setRequestSource(RequestSourceEnum requestSource) {
    this.requestSource = requestSource;
  }

  public Request requester(String requester) {
    this.requester = requester;
    return this;
  }

   /**
   * Get requester
   * @return requester
  **/
  @ApiModelProperty(value = "")
  public String getRequester() {
    return requester;
  }

  public void setRequester(String requester) {
    this.requester = requester;
  }

  public Request requesterDeleted(Boolean requesterDeleted) {
    this.requesterDeleted = requesterDeleted;
    return this;
  }

   /**
   * Get requesterDeleted
   * @return requesterDeleted
  **/
  @ApiModelProperty(value = "")
  public Boolean isRequesterDeleted() {
    return requesterDeleted;
  }

  public void setRequesterDeleted(Boolean requesterDeleted) {
    this.requesterDeleted = requesterDeleted;
  }

  public Request requesterDisplayName(String requesterDisplayName) {
    this.requesterDisplayName = requesterDisplayName;
    return this;
  }

   /**
   * Get requesterDisplayName
   * @return requesterDisplayName
  **/
  @ApiModelProperty(value = "")
  public String getRequesterDisplayName() {
    return requesterDisplayName;
  }

  public void setRequesterDisplayName(String requesterDisplayName) {
    this.requesterDisplayName = requesterDisplayName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Request request = (Request) o;
    return Objects.equals(this.commentRequired, request.commentRequired) &&
        Objects.equals(this.fulfillmentTarget, request.fulfillmentTarget) &&
        Objects.equals(this.reason, request.reason) &&
        Objects.equals(this.requestItems, request.requestItems) &&
        Objects.equals(this.requestSource, request.requestSource) &&
        Objects.equals(this.requester, request.requester) &&
        Objects.equals(this.requesterDeleted, request.requesterDeleted) &&
        Objects.equals(this.requesterDisplayName, request.requesterDisplayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commentRequired, fulfillmentTarget, reason, requestItems, requestSource, requester, requesterDeleted, requesterDisplayName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Request {\n");
    
    sb.append("    commentRequired: ").append(toIndentedString(commentRequired)).append("\n");
    sb.append("    fulfillmentTarget: ").append(toIndentedString(fulfillmentTarget)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    requestItems: ").append(toIndentedString(requestItems)).append("\n");
    sb.append("    requestSource: ").append(toIndentedString(requestSource)).append("\n");
    sb.append("    requester: ").append(toIndentedString(requester)).append("\n");
    sb.append("    requesterDeleted: ").append(toIndentedString(requesterDeleted)).append("\n");
    sb.append("    requesterDisplayName: ").append(toIndentedString(requesterDisplayName)).append("\n");
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
