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
import de.araneaconsult.codegen.ig.rest.model.ChangeItemAction;
import de.araneaconsult.codegen.ig.rest.model.ItemValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ChangeItem
 */



public class ChangeItem {
  @SerializedName("account")
  private String account = null;

  @SerializedName("accountName")
  private String accountName = null;

  @SerializedName("actions")
  private List<ChangeItemAction> actions = null;

  @SerializedName("appName")
  private String appName = null;

  @SerializedName("appSourceId")
  private Long appSourceId = null;

  @SerializedName("changeItemAction")
  private ChangeItemAction changeItemAction = null;

  @SerializedName("changeItemId")
  private Long changeItemId = null;

  @SerializedName("changeRequestDate")
  private Long changeRequestDate = null;

  /**
   * Gets or Sets changeRequestType
   */
  @JsonAdapter(ChangeRequestTypeEnum.Adapter.class)
  public enum ChangeRequestTypeEnum {
    @SerializedName("REMOVE_BUS_ROLE_ASSIGNMENT")
    REMOVE_BUS_ROLE_ASSIGNMENT("REMOVE_BUS_ROLE_ASSIGNMENT"),
    @SerializedName("ADD_USER_TO_ACCOUNT")
    ADD_USER_TO_ACCOUNT("ADD_USER_TO_ACCOUNT"),
    @SerializedName("REMOVE_PERMISSION_ASSIGNMENT")
    REMOVE_PERMISSION_ASSIGNMENT("REMOVE_PERMISSION_ASSIGNMENT"),
    @SerializedName("REMOVE_ACCOUNT_ASSIGNMENT")
    REMOVE_ACCOUNT_ASSIGNMENT("REMOVE_ACCOUNT_ASSIGNMENT"),
    @SerializedName("MODIFY_PERMISSION_ASSIGNMENT")
    MODIFY_PERMISSION_ASSIGNMENT("MODIFY_PERMISSION_ASSIGNMENT"),
    @SerializedName("MODIFY_ACCOUNT_ASSIGNMENT")
    MODIFY_ACCOUNT_ASSIGNMENT("MODIFY_ACCOUNT_ASSIGNMENT"),
    @SerializedName("MODIFY_TECH_ROLE_ASSIGNMENT")
    MODIFY_TECH_ROLE_ASSIGNMENT("MODIFY_TECH_ROLE_ASSIGNMENT"),
    @SerializedName("REMOVE_ACCOUNT")
    REMOVE_ACCOUNT("REMOVE_ACCOUNT"),
    @SerializedName("ADD_PERMISSION_TO_USER")
    ADD_PERMISSION_TO_USER("ADD_PERMISSION_TO_USER"),
    @SerializedName("ADD_APPLICATION_TO_USER")
    ADD_APPLICATION_TO_USER("ADD_APPLICATION_TO_USER"),
    @SerializedName("REMOVE_APPLICATION_FROM_USER")
    REMOVE_APPLICATION_FROM_USER("REMOVE_APPLICATION_FROM_USER"),
    @SerializedName("ADD_TECH_ROLE_TO_USER")
    ADD_TECH_ROLE_TO_USER("ADD_TECH_ROLE_TO_USER"),
    @SerializedName("REMOVE_ACCOUNT_PERMISSION")
    REMOVE_ACCOUNT_PERMISSION("REMOVE_ACCOUNT_PERMISSION"),
    @SerializedName("MODIFY_ACCOUNT")
    MODIFY_ACCOUNT("MODIFY_ACCOUNT"),
    @SerializedName("MODIFY_USER_PROFILE")
    MODIFY_USER_PROFILE("MODIFY_USER_PROFILE"),
    @SerializedName("MODIFY_USER_SUPERVISOR")
    MODIFY_USER_SUPERVISOR("MODIFY_USER_SUPERVISOR"),
    @SerializedName("DATA_VIOLATION_USER")
    DATA_VIOLATION_USER("DATA_VIOLATION_USER"),
    @SerializedName("DATA_VIOLATION_PERMISSION")
    DATA_VIOLATION_PERMISSION("DATA_VIOLATION_PERMISSION"),
    @SerializedName("DATA_VIOLATION_ACCOUNT")
    DATA_VIOLATION_ACCOUNT("DATA_VIOLATION_ACCOUNT"),
    @SerializedName("CERTIFICATION_POLICY_VIOLATION")
    CERTIFICATION_POLICY_VIOLATION("CERTIFICATION_POLICY_VIOLATION"),
    @SerializedName("MODIFY_BUS_ROLE_DEFN")
    MODIFY_BUS_ROLE_DEFN("MODIFY_BUS_ROLE_DEFN"),
    @SerializedName("REMOVE_TECH_ROLE_ASSIGNMENT")
    REMOVE_TECH_ROLE_ASSIGNMENT("REMOVE_TECH_ROLE_ASSIGNMENT"),
    @SerializedName("REMOVE_BUS_ROLE_AUTHS")
    REMOVE_BUS_ROLE_AUTHS("REMOVE_BUS_ROLE_AUTHS"),
    @SerializedName("MODIFY_TECH_ROLE_DEFN")
    MODIFY_TECH_ROLE_DEFN("MODIFY_TECH_ROLE_DEFN"),
    @SerializedName("ADD_BUSINESS_ROLE_MEMBERSHIP")
    ADD_BUSINESS_ROLE_MEMBERSHIP("ADD_BUSINESS_ROLE_MEMBERSHIP"),
    @SerializedName("REMOVE_BUSINESS_ROLE_MEMBERSHIP")
    REMOVE_BUSINESS_ROLE_MEMBERSHIP("REMOVE_BUSINESS_ROLE_MEMBERSHIP");

    private String value;

    ChangeRequestTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ChangeRequestTypeEnum fromValue(String input) {
      for (ChangeRequestTypeEnum b : ChangeRequestTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ChangeRequestTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ChangeRequestTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ChangeRequestTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ChangeRequestTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("changeRequestType")
  private ChangeRequestTypeEnum changeRequestType = null;

  @SerializedName("email")
  private List<String> email = null;

  @SerializedName("fulfillerDn")
  private String fulfillerDn = null;

  @SerializedName("fulfillerId")
  private Long fulfillerId = null;

  @SerializedName("fulfillerName")
  private String fulfillerName = null;

  @SerializedName("fulfillerUniqueId")
  private String fulfillerUniqueId = null;

  @SerializedName("fulfillmentContext")
  private Object fulfillmentContext = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("linkAccount")
  private String linkAccount = null;

  @SerializedName("linkApplication")
  private String linkApplication = null;

  @SerializedName("linkCauses")
  private String linkCauses = null;

  @SerializedName("linkFulfiller")
  private String linkFulfiller = null;

  @SerializedName("linkPermission")
  private String linkPermission = null;

  @SerializedName("linkRequester")
  private String linkRequester = null;

  @SerializedName("linkReviewInstance")
  private String linkReviewInstance = null;

  @SerializedName("linkUser")
  private String linkUser = null;

  @SerializedName("permId")
  private Long permId = null;

  @SerializedName("permName")
  private String permName = null;

  @SerializedName("processId")
  private String processId = null;

  @SerializedName("provisioningApplication")
  private String provisioningApplication = null;

  /**
   * Gets or Sets provisioningType
   */
  @JsonAdapter(ProvisioningTypeEnum.Adapter.class)
  public enum ProvisioningTypeEnum {
    @SerializedName("AUTO")
    AUTO("AUTO"),
    @SerializedName("MANUAL")
    MANUAL("MANUAL"),
    @SerializedName("EXTERNAL")
    EXTERNAL("EXTERNAL"),
    @SerializedName("DAAS")
    DAAS("DAAS"),
    @SerializedName("INTERNAL")
    INTERNAL("INTERNAL");

    private String value;

    ProvisioningTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ProvisioningTypeEnum fromValue(String input) {
      for (ProvisioningTypeEnum b : ProvisioningTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ProvisioningTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ProvisioningTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ProvisioningTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ProvisioningTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("provisioningType")
  private ProvisioningTypeEnum provisioningType = null;

  @SerializedName("reason")
  private String reason = null;

  @SerializedName("requesterId")
  private Long requesterId = null;

  @SerializedName("requesterName")
  private String requesterName = null;

  @SerializedName("reviewInstId")
  private Long reviewInstId = null;

  @SerializedName("userId")
  private Long userId = null;

  @SerializedName("userName")
  private String userName = null;

  @SerializedName("values")
  private List<ItemValue> values = null;

  public ChangeItem account(String account) {
    this.account = account;
    return this;
  }

   /**
   * Get account
   * @return account
  **/
  @ApiModelProperty(value = "")
  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public ChangeItem accountName(String accountName) {
    this.accountName = accountName;
    return this;
  }

   /**
   * Get accountName
   * @return accountName
  **/
  @ApiModelProperty(value = "")
  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public ChangeItem actions(List<ChangeItemAction> actions) {
    this.actions = actions;
    return this;
  }

  public ChangeItem addActionsItem(ChangeItemAction actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<ChangeItemAction>();
    }
    this.actions.add(actionsItem);
    return this;
  }

   /**
   * Get actions
   * @return actions
  **/
  @ApiModelProperty(value = "")
  public List<ChangeItemAction> getActions() {
    return actions;
  }

  public void setActions(List<ChangeItemAction> actions) {
    this.actions = actions;
  }

  public ChangeItem appName(String appName) {
    this.appName = appName;
    return this;
  }

   /**
   * Get appName
   * @return appName
  **/
  @ApiModelProperty(value = "")
  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public ChangeItem appSourceId(Long appSourceId) {
    this.appSourceId = appSourceId;
    return this;
  }

   /**
   * Get appSourceId
   * @return appSourceId
  **/
  @ApiModelProperty(value = "")
  public Long getAppSourceId() {
    return appSourceId;
  }

  public void setAppSourceId(Long appSourceId) {
    this.appSourceId = appSourceId;
  }

  public ChangeItem changeItemAction(ChangeItemAction changeItemAction) {
    this.changeItemAction = changeItemAction;
    return this;
  }

   /**
   * Get changeItemAction
   * @return changeItemAction
  **/
  @ApiModelProperty(value = "")
  public ChangeItemAction getChangeItemAction() {
    return changeItemAction;
  }

  public void setChangeItemAction(ChangeItemAction changeItemAction) {
    this.changeItemAction = changeItemAction;
  }

  public ChangeItem changeItemId(Long changeItemId) {
    this.changeItemId = changeItemId;
    return this;
  }

   /**
   * Get changeItemId
   * @return changeItemId
  **/
  @ApiModelProperty(value = "")
  public Long getChangeItemId() {
    return changeItemId;
  }

  public void setChangeItemId(Long changeItemId) {
    this.changeItemId = changeItemId;
  }

  public ChangeItem changeRequestDate(Long changeRequestDate) {
    this.changeRequestDate = changeRequestDate;
    return this;
  }

   /**
   * Get changeRequestDate
   * @return changeRequestDate
  **/
  @ApiModelProperty(value = "")
  public Long getChangeRequestDate() {
    return changeRequestDate;
  }

  public void setChangeRequestDate(Long changeRequestDate) {
    this.changeRequestDate = changeRequestDate;
  }

  public ChangeItem changeRequestType(ChangeRequestTypeEnum changeRequestType) {
    this.changeRequestType = changeRequestType;
    return this;
  }

   /**
   * Get changeRequestType
   * @return changeRequestType
  **/
  @ApiModelProperty(value = "")
  public ChangeRequestTypeEnum getChangeRequestType() {
    return changeRequestType;
  }

  public void setChangeRequestType(ChangeRequestTypeEnum changeRequestType) {
    this.changeRequestType = changeRequestType;
  }

  public ChangeItem email(List<String> email) {
    this.email = email;
    return this;
  }

  public ChangeItem addEmailItem(String emailItem) {
    if (this.email == null) {
      this.email = new ArrayList<String>();
    }
    this.email.add(emailItem);
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @ApiModelProperty(value = "")
  public List<String> getEmail() {
    return email;
  }

  public void setEmail(List<String> email) {
    this.email = email;
  }

  public ChangeItem fulfillerDn(String fulfillerDn) {
    this.fulfillerDn = fulfillerDn;
    return this;
  }

   /**
   * Get fulfillerDn
   * @return fulfillerDn
  **/
  @ApiModelProperty(value = "")
  public String getFulfillerDn() {
    return fulfillerDn;
  }

  public void setFulfillerDn(String fulfillerDn) {
    this.fulfillerDn = fulfillerDn;
  }

  public ChangeItem fulfillerId(Long fulfillerId) {
    this.fulfillerId = fulfillerId;
    return this;
  }

   /**
   * Get fulfillerId
   * @return fulfillerId
  **/
  @ApiModelProperty(value = "")
  public Long getFulfillerId() {
    return fulfillerId;
  }

  public void setFulfillerId(Long fulfillerId) {
    this.fulfillerId = fulfillerId;
  }

  public ChangeItem fulfillerName(String fulfillerName) {
    this.fulfillerName = fulfillerName;
    return this;
  }

   /**
   * Get fulfillerName
   * @return fulfillerName
  **/
  @ApiModelProperty(value = "")
  public String getFulfillerName() {
    return fulfillerName;
  }

  public void setFulfillerName(String fulfillerName) {
    this.fulfillerName = fulfillerName;
  }

  public ChangeItem fulfillerUniqueId(String fulfillerUniqueId) {
    this.fulfillerUniqueId = fulfillerUniqueId;
    return this;
  }

   /**
   * Get fulfillerUniqueId
   * @return fulfillerUniqueId
  **/
  @ApiModelProperty(value = "")
  public String getFulfillerUniqueId() {
    return fulfillerUniqueId;
  }

  public void setFulfillerUniqueId(String fulfillerUniqueId) {
    this.fulfillerUniqueId = fulfillerUniqueId;
  }

  public ChangeItem fulfillmentContext(Object fulfillmentContext) {
    this.fulfillmentContext = fulfillmentContext;
    return this;
  }

   /**
   * Get fulfillmentContext
   * @return fulfillmentContext
  **/
  @ApiModelProperty(value = "")
  public Object getFulfillmentContext() {
    return fulfillmentContext;
  }

  public void setFulfillmentContext(Object fulfillmentContext) {
    this.fulfillmentContext = fulfillmentContext;
  }

  public ChangeItem link(String link) {
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

  public ChangeItem linkAccount(String linkAccount) {
    this.linkAccount = linkAccount;
    return this;
  }

   /**
   * Get linkAccount
   * @return linkAccount
  **/
  @ApiModelProperty(value = "")
  public String getLinkAccount() {
    return linkAccount;
  }

  public void setLinkAccount(String linkAccount) {
    this.linkAccount = linkAccount;
  }

  public ChangeItem linkApplication(String linkApplication) {
    this.linkApplication = linkApplication;
    return this;
  }

   /**
   * Get linkApplication
   * @return linkApplication
  **/
  @ApiModelProperty(value = "")
  public String getLinkApplication() {
    return linkApplication;
  }

  public void setLinkApplication(String linkApplication) {
    this.linkApplication = linkApplication;
  }

  public ChangeItem linkCauses(String linkCauses) {
    this.linkCauses = linkCauses;
    return this;
  }

   /**
   * Get linkCauses
   * @return linkCauses
  **/
  @ApiModelProperty(value = "")
  public String getLinkCauses() {
    return linkCauses;
  }

  public void setLinkCauses(String linkCauses) {
    this.linkCauses = linkCauses;
  }

  public ChangeItem linkFulfiller(String linkFulfiller) {
    this.linkFulfiller = linkFulfiller;
    return this;
  }

   /**
   * Get linkFulfiller
   * @return linkFulfiller
  **/
  @ApiModelProperty(value = "")
  public String getLinkFulfiller() {
    return linkFulfiller;
  }

  public void setLinkFulfiller(String linkFulfiller) {
    this.linkFulfiller = linkFulfiller;
  }

  public ChangeItem linkPermission(String linkPermission) {
    this.linkPermission = linkPermission;
    return this;
  }

   /**
   * Get linkPermission
   * @return linkPermission
  **/
  @ApiModelProperty(value = "")
  public String getLinkPermission() {
    return linkPermission;
  }

  public void setLinkPermission(String linkPermission) {
    this.linkPermission = linkPermission;
  }

  public ChangeItem linkRequester(String linkRequester) {
    this.linkRequester = linkRequester;
    return this;
  }

   /**
   * Get linkRequester
   * @return linkRequester
  **/
  @ApiModelProperty(value = "")
  public String getLinkRequester() {
    return linkRequester;
  }

  public void setLinkRequester(String linkRequester) {
    this.linkRequester = linkRequester;
  }

  public ChangeItem linkReviewInstance(String linkReviewInstance) {
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

  public ChangeItem linkUser(String linkUser) {
    this.linkUser = linkUser;
    return this;
  }

   /**
   * Get linkUser
   * @return linkUser
  **/
  @ApiModelProperty(value = "")
  public String getLinkUser() {
    return linkUser;
  }

  public void setLinkUser(String linkUser) {
    this.linkUser = linkUser;
  }

  public ChangeItem permId(Long permId) {
    this.permId = permId;
    return this;
  }

   /**
   * Get permId
   * @return permId
  **/
  @ApiModelProperty(value = "")
  public Long getPermId() {
    return permId;
  }

  public void setPermId(Long permId) {
    this.permId = permId;
  }

  public ChangeItem permName(String permName) {
    this.permName = permName;
    return this;
  }

   /**
   * Get permName
   * @return permName
  **/
  @ApiModelProperty(value = "")
  public String getPermName() {
    return permName;
  }

  public void setPermName(String permName) {
    this.permName = permName;
  }

  public ChangeItem processId(String processId) {
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

  public ChangeItem provisioningApplication(String provisioningApplication) {
    this.provisioningApplication = provisioningApplication;
    return this;
  }

   /**
   * Get provisioningApplication
   * @return provisioningApplication
  **/
  @ApiModelProperty(value = "")
  public String getProvisioningApplication() {
    return provisioningApplication;
  }

  public void setProvisioningApplication(String provisioningApplication) {
    this.provisioningApplication = provisioningApplication;
  }

  public ChangeItem provisioningType(ProvisioningTypeEnum provisioningType) {
    this.provisioningType = provisioningType;
    return this;
  }

   /**
   * Get provisioningType
   * @return provisioningType
  **/
  @ApiModelProperty(value = "")
  public ProvisioningTypeEnum getProvisioningType() {
    return provisioningType;
  }

  public void setProvisioningType(ProvisioningTypeEnum provisioningType) {
    this.provisioningType = provisioningType;
  }

  public ChangeItem reason(String reason) {
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

  public ChangeItem requesterId(Long requesterId) {
    this.requesterId = requesterId;
    return this;
  }

   /**
   * Get requesterId
   * @return requesterId
  **/
  @ApiModelProperty(value = "")
  public Long getRequesterId() {
    return requesterId;
  }

  public void setRequesterId(Long requesterId) {
    this.requesterId = requesterId;
  }

  public ChangeItem requesterName(String requesterName) {
    this.requesterName = requesterName;
    return this;
  }

   /**
   * Get requesterName
   * @return requesterName
  **/
  @ApiModelProperty(value = "")
  public String getRequesterName() {
    return requesterName;
  }

  public void setRequesterName(String requesterName) {
    this.requesterName = requesterName;
  }

  public ChangeItem reviewInstId(Long reviewInstId) {
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

  public ChangeItem userId(Long userId) {
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

  public ChangeItem userName(String userName) {
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

  public ChangeItem values(List<ItemValue> values) {
    this.values = values;
    return this;
  }

  public ChangeItem addValuesItem(ItemValue valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<ItemValue>();
    }
    this.values.add(valuesItem);
    return this;
  }

   /**
   * Get values
   * @return values
  **/
  @ApiModelProperty(value = "")
  public List<ItemValue> getValues() {
    return values;
  }

  public void setValues(List<ItemValue> values) {
    this.values = values;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChangeItem changeItem = (ChangeItem) o;
    return Objects.equals(this.account, changeItem.account) &&
        Objects.equals(this.accountName, changeItem.accountName) &&
        Objects.equals(this.actions, changeItem.actions) &&
        Objects.equals(this.appName, changeItem.appName) &&
        Objects.equals(this.appSourceId, changeItem.appSourceId) &&
        Objects.equals(this.changeItemAction, changeItem.changeItemAction) &&
        Objects.equals(this.changeItemId, changeItem.changeItemId) &&
        Objects.equals(this.changeRequestDate, changeItem.changeRequestDate) &&
        Objects.equals(this.changeRequestType, changeItem.changeRequestType) &&
        Objects.equals(this.email, changeItem.email) &&
        Objects.equals(this.fulfillerDn, changeItem.fulfillerDn) &&
        Objects.equals(this.fulfillerId, changeItem.fulfillerId) &&
        Objects.equals(this.fulfillerName, changeItem.fulfillerName) &&
        Objects.equals(this.fulfillerUniqueId, changeItem.fulfillerUniqueId) &&
        Objects.equals(this.fulfillmentContext, changeItem.fulfillmentContext) &&
        Objects.equals(this.link, changeItem.link) &&
        Objects.equals(this.linkAccount, changeItem.linkAccount) &&
        Objects.equals(this.linkApplication, changeItem.linkApplication) &&
        Objects.equals(this.linkCauses, changeItem.linkCauses) &&
        Objects.equals(this.linkFulfiller, changeItem.linkFulfiller) &&
        Objects.equals(this.linkPermission, changeItem.linkPermission) &&
        Objects.equals(this.linkRequester, changeItem.linkRequester) &&
        Objects.equals(this.linkReviewInstance, changeItem.linkReviewInstance) &&
        Objects.equals(this.linkUser, changeItem.linkUser) &&
        Objects.equals(this.permId, changeItem.permId) &&
        Objects.equals(this.permName, changeItem.permName) &&
        Objects.equals(this.processId, changeItem.processId) &&
        Objects.equals(this.provisioningApplication, changeItem.provisioningApplication) &&
        Objects.equals(this.provisioningType, changeItem.provisioningType) &&
        Objects.equals(this.reason, changeItem.reason) &&
        Objects.equals(this.requesterId, changeItem.requesterId) &&
        Objects.equals(this.requesterName, changeItem.requesterName) &&
        Objects.equals(this.reviewInstId, changeItem.reviewInstId) &&
        Objects.equals(this.userId, changeItem.userId) &&
        Objects.equals(this.userName, changeItem.userName) &&
        Objects.equals(this.values, changeItem.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(account, accountName, actions, appName, appSourceId, changeItemAction, changeItemId, changeRequestDate, changeRequestType, email, fulfillerDn, fulfillerId, fulfillerName, fulfillerUniqueId, fulfillmentContext, link, linkAccount, linkApplication, linkCauses, linkFulfiller, linkPermission, linkRequester, linkReviewInstance, linkUser, permId, permName, processId, provisioningApplication, provisioningType, reason, requesterId, requesterName, reviewInstId, userId, userName, values);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChangeItem {\n");
    
    sb.append("    account: ").append(toIndentedString(account)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
    sb.append("    appName: ").append(toIndentedString(appName)).append("\n");
    sb.append("    appSourceId: ").append(toIndentedString(appSourceId)).append("\n");
    sb.append("    changeItemAction: ").append(toIndentedString(changeItemAction)).append("\n");
    sb.append("    changeItemId: ").append(toIndentedString(changeItemId)).append("\n");
    sb.append("    changeRequestDate: ").append(toIndentedString(changeRequestDate)).append("\n");
    sb.append("    changeRequestType: ").append(toIndentedString(changeRequestType)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    fulfillerDn: ").append(toIndentedString(fulfillerDn)).append("\n");
    sb.append("    fulfillerId: ").append(toIndentedString(fulfillerId)).append("\n");
    sb.append("    fulfillerName: ").append(toIndentedString(fulfillerName)).append("\n");
    sb.append("    fulfillerUniqueId: ").append(toIndentedString(fulfillerUniqueId)).append("\n");
    sb.append("    fulfillmentContext: ").append(toIndentedString(fulfillmentContext)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    linkAccount: ").append(toIndentedString(linkAccount)).append("\n");
    sb.append("    linkApplication: ").append(toIndentedString(linkApplication)).append("\n");
    sb.append("    linkCauses: ").append(toIndentedString(linkCauses)).append("\n");
    sb.append("    linkFulfiller: ").append(toIndentedString(linkFulfiller)).append("\n");
    sb.append("    linkPermission: ").append(toIndentedString(linkPermission)).append("\n");
    sb.append("    linkRequester: ").append(toIndentedString(linkRequester)).append("\n");
    sb.append("    linkReviewInstance: ").append(toIndentedString(linkReviewInstance)).append("\n");
    sb.append("    linkUser: ").append(toIndentedString(linkUser)).append("\n");
    sb.append("    permId: ").append(toIndentedString(permId)).append("\n");
    sb.append("    permName: ").append(toIndentedString(permName)).append("\n");
    sb.append("    processId: ").append(toIndentedString(processId)).append("\n");
    sb.append("    provisioningApplication: ").append(toIndentedString(provisioningApplication)).append("\n");
    sb.append("    provisioningType: ").append(toIndentedString(provisioningType)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    requesterId: ").append(toIndentedString(requesterId)).append("\n");
    sb.append("    requesterName: ").append(toIndentedString(requesterName)).append("\n");
    sb.append("    reviewInstId: ").append(toIndentedString(reviewInstId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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
