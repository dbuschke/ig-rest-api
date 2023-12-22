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
import io.swagger.client.model.CompControl;
import io.swagger.client.model.RemovalItem;
import io.swagger.client.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * SodCaseAction
 */



public class SodCaseAction {
  /**
   * Gets or Sets action
   */
  @JsonAdapter(ActionEnum.Adapter.class)
  public enum ActionEnum {
    @SerializedName("APPROVAL")
    APPROVAL("APPROVAL"),
    @SerializedName("RESOLVE")
    RESOLVE("RESOLVE"),
    @SerializedName("CLOSED")
    CLOSED("CLOSED"),
    @SerializedName("RESTART")
    RESTART("RESTART");

    private String value;

    ActionEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ActionEnum fromValue(String input) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ActionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ActionEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ActionEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ActionEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("action")
  private ActionEnum action = null;

  @SerializedName("actionComment")
  private String actionComment = null;

  @SerializedName("actionControls")
  private List<CompControl> actionControls = null;

  @SerializedName("actionTime")
  private Long actionTime = null;

  @SerializedName("caseId")
  private Long caseId = null;

  @SerializedName("controlPeriod")
  private Long controlPeriod = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("performedBy")
  private User performedBy = null;

  @SerializedName("removalItems")
  private List<RemovalItem> removalItems = null;

  @SerializedName("step")
  private Long step = null;

  public SodCaseAction action(ActionEnum action) {
    this.action = action;
    return this;
  }

   /**
   * Get action
   * @return action
  **/
  @ApiModelProperty(value = "")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public SodCaseAction actionComment(String actionComment) {
    this.actionComment = actionComment;
    return this;
  }

   /**
   * Get actionComment
   * @return actionComment
  **/
  @ApiModelProperty(value = "")
  public String getActionComment() {
    return actionComment;
  }

  public void setActionComment(String actionComment) {
    this.actionComment = actionComment;
  }

  public SodCaseAction actionControls(List<CompControl> actionControls) {
    this.actionControls = actionControls;
    return this;
  }

  public SodCaseAction addActionControlsItem(CompControl actionControlsItem) {
    if (this.actionControls == null) {
      this.actionControls = new ArrayList<CompControl>();
    }
    this.actionControls.add(actionControlsItem);
    return this;
  }

   /**
   * Get actionControls
   * @return actionControls
  **/
  @ApiModelProperty(value = "")
  public List<CompControl> getActionControls() {
    return actionControls;
  }

  public void setActionControls(List<CompControl> actionControls) {
    this.actionControls = actionControls;
  }

  public SodCaseAction actionTime(Long actionTime) {
    this.actionTime = actionTime;
    return this;
  }

   /**
   * Get actionTime
   * @return actionTime
  **/
  @ApiModelProperty(value = "")
  public Long getActionTime() {
    return actionTime;
  }

  public void setActionTime(Long actionTime) {
    this.actionTime = actionTime;
  }

  public SodCaseAction caseId(Long caseId) {
    this.caseId = caseId;
    return this;
  }

   /**
   * Get caseId
   * @return caseId
  **/
  @ApiModelProperty(value = "")
  public Long getCaseId() {
    return caseId;
  }

  public void setCaseId(Long caseId) {
    this.caseId = caseId;
  }

  public SodCaseAction controlPeriod(Long controlPeriod) {
    this.controlPeriod = controlPeriod;
    return this;
  }

   /**
   * Get controlPeriod
   * @return controlPeriod
  **/
  @ApiModelProperty(value = "")
  public Long getControlPeriod() {
    return controlPeriod;
  }

  public void setControlPeriod(Long controlPeriod) {
    this.controlPeriod = controlPeriod;
  }

  public SodCaseAction id(Long id) {
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

  public SodCaseAction performedBy(User performedBy) {
    this.performedBy = performedBy;
    return this;
  }

   /**
   * Get performedBy
   * @return performedBy
  **/
  @ApiModelProperty(value = "")
  public User getPerformedBy() {
    return performedBy;
  }

  public void setPerformedBy(User performedBy) {
    this.performedBy = performedBy;
  }

  public SodCaseAction removalItems(List<RemovalItem> removalItems) {
    this.removalItems = removalItems;
    return this;
  }

  public SodCaseAction addRemovalItemsItem(RemovalItem removalItemsItem) {
    if (this.removalItems == null) {
      this.removalItems = new ArrayList<RemovalItem>();
    }
    this.removalItems.add(removalItemsItem);
    return this;
  }

   /**
   * Get removalItems
   * @return removalItems
  **/
  @ApiModelProperty(value = "")
  public List<RemovalItem> getRemovalItems() {
    return removalItems;
  }

  public void setRemovalItems(List<RemovalItem> removalItems) {
    this.removalItems = removalItems;
  }

  public SodCaseAction step(Long step) {
    this.step = step;
    return this;
  }

   /**
   * Get step
   * @return step
  **/
  @ApiModelProperty(value = "")
  public Long getStep() {
    return step;
  }

  public void setStep(Long step) {
    this.step = step;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SodCaseAction sodCaseAction = (SodCaseAction) o;
    return Objects.equals(this.action, sodCaseAction.action) &&
        Objects.equals(this.actionComment, sodCaseAction.actionComment) &&
        Objects.equals(this.actionControls, sodCaseAction.actionControls) &&
        Objects.equals(this.actionTime, sodCaseAction.actionTime) &&
        Objects.equals(this.caseId, sodCaseAction.caseId) &&
        Objects.equals(this.controlPeriod, sodCaseAction.controlPeriod) &&
        Objects.equals(this.id, sodCaseAction.id) &&
        Objects.equals(this.performedBy, sodCaseAction.performedBy) &&
        Objects.equals(this.removalItems, sodCaseAction.removalItems) &&
        Objects.equals(this.step, sodCaseAction.step);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, actionComment, actionControls, actionTime, caseId, controlPeriod, id, performedBy, removalItems, step);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SodCaseAction {\n");
    
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    actionComment: ").append(toIndentedString(actionComment)).append("\n");
    sb.append("    actionControls: ").append(toIndentedString(actionControls)).append("\n");
    sb.append("    actionTime: ").append(toIndentedString(actionTime)).append("\n");
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    controlPeriod: ").append(toIndentedString(controlPeriod)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    performedBy: ").append(toIndentedString(performedBy)).append("\n");
    sb.append("    removalItems: ").append(toIndentedString(removalItems)).append("\n");
    sb.append("    step: ").append(toIndentedString(step)).append("\n");
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
