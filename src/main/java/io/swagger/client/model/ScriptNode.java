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
 * ScriptNode
 */



public class ScriptNode {
  @SerializedName("application")
  private Long application = null;

  @SerializedName("changeset")
  private Object changeset = null;

  @SerializedName("changesetId")
  private Long changesetId = null;

  @SerializedName("debugContext")
  private String debugContext = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("draftScript")
  private String draftScript = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("isLibrary")
  private Boolean isLibrary = null;

  @SerializedName("isSystem")
  private Boolean isSystem = null;

  @SerializedName("isTemplate")
  private Boolean isTemplate = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("publishTime")
  private Long publishTime = null;

  @SerializedName("publishedScript")
  private String publishedScript = null;

  @SerializedName("result")
  private String result = null;

  @SerializedName("script")
  private String script = null;

  /**
   * the script language
   */
  @JsonAdapter(ScriptLanguageEnum.Adapter.class)
  public enum ScriptLanguageEnum {
    @SerializedName("JAVASCRIPT")
    JAVASCRIPT("JAVASCRIPT");

    private String value;

    ScriptLanguageEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ScriptLanguageEnum fromValue(String input) {
      for (ScriptLanguageEnum b : ScriptLanguageEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ScriptLanguageEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ScriptLanguageEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ScriptLanguageEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ScriptLanguageEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("scriptLanguage")
  private ScriptLanguageEnum scriptLanguage = null;

  /**
   * the script type
   */
  @JsonAdapter(ScriptTypeEnum.Adapter.class)
  public enum ScriptTypeEnum {
    @SerializedName("FULFILLMENT_CHANGESET_POLICY")
    FULFILLMENT_CHANGESET_POLICY("FULFILLMENT_CHANGESET_POLICY");

    private String value;

    ScriptTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ScriptTypeEnum fromValue(String input) {
      for (ScriptTypeEnum b : ScriptTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ScriptTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ScriptTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public ScriptTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ScriptTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("scriptType")
  private ScriptTypeEnum scriptType = null;

  @SerializedName("usageCount")
  private Long usageCount = null;

  public ScriptNode application(Long application) {
    this.application = application;
    return this;
  }

   /**
   * the application
   * @return application
  **/
  @ApiModelProperty(value = "the application")
  public Long getApplication() {
    return application;
  }

  public void setApplication(Long application) {
    this.application = application;
  }

  public ScriptNode changeset(Object changeset) {
    this.changeset = changeset;
    return this;
  }

   /**
   * Get changeset
   * @return changeset
  **/
  @ApiModelProperty(value = "")
  public Object getChangeset() {
    return changeset;
  }

  public void setChangeset(Object changeset) {
    this.changeset = changeset;
  }

  public ScriptNode changesetId(Long changesetId) {
    this.changesetId = changesetId;
    return this;
  }

   /**
   * the changeset id
   * @return changesetId
  **/
  @ApiModelProperty(value = "the changeset id")
  public Long getChangesetId() {
    return changesetId;
  }

  public void setChangesetId(Long changesetId) {
    this.changesetId = changesetId;
  }

  public ScriptNode debugContext(String debugContext) {
    this.debugContext = debugContext;
    return this;
  }

   /**
   * the debug context
   * @return debugContext
  **/
  @ApiModelProperty(value = "the debug context")
  public String getDebugContext() {
    return debugContext;
  }

  public void setDebugContext(String debugContext) {
    this.debugContext = debugContext;
  }

  public ScriptNode description(String description) {
    this.description = description;
    return this;
  }

   /**
   * the description
   * @return description
  **/
  @ApiModelProperty(value = "the description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ScriptNode draftScript(String draftScript) {
    this.draftScript = draftScript;
    return this;
  }

   /**
   * the draft script
   * @return draftScript
  **/
  @ApiModelProperty(value = "the draft script")
  public String getDraftScript() {
    return draftScript;
  }

  public void setDraftScript(String draftScript) {
    this.draftScript = draftScript;
  }

  public ScriptNode id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * the id
   * @return id
  **/
  @ApiModelProperty(value = "the id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ScriptNode isLibrary(Boolean isLibrary) {
    this.isLibrary = isLibrary;
    return this;
  }

   /**
   * the is library
   * @return isLibrary
  **/
  @ApiModelProperty(value = "the is library")
  public Boolean isIsLibrary() {
    return isLibrary;
  }

  public void setIsLibrary(Boolean isLibrary) {
    this.isLibrary = isLibrary;
  }

  public ScriptNode isSystem(Boolean isSystem) {
    this.isSystem = isSystem;
    return this;
  }

   /**
   * the is system
   * @return isSystem
  **/
  @ApiModelProperty(value = "the is system")
  public Boolean isIsSystem() {
    return isSystem;
  }

  public void setIsSystem(Boolean isSystem) {
    this.isSystem = isSystem;
  }

  public ScriptNode isTemplate(Boolean isTemplate) {
    this.isTemplate = isTemplate;
    return this;
  }

   /**
   * the is template
   * @return isTemplate
  **/
  @ApiModelProperty(value = "the is template")
  public Boolean isIsTemplate() {
    return isTemplate;
  }

  public void setIsTemplate(Boolean isTemplate) {
    this.isTemplate = isTemplate;
  }

  public ScriptNode name(String name) {
    this.name = name;
    return this;
  }

   /**
   * the name
   * @return name
  **/
  @ApiModelProperty(value = "the name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ScriptNode publishTime(Long publishTime) {
    this.publishTime = publishTime;
    return this;
  }

   /**
   * the publish time
   * @return publishTime
  **/
  @ApiModelProperty(value = "the publish time")
  public Long getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Long publishTime) {
    this.publishTime = publishTime;
  }

  public ScriptNode publishedScript(String publishedScript) {
    this.publishedScript = publishedScript;
    return this;
  }

   /**
   * the published script
   * @return publishedScript
  **/
  @ApiModelProperty(value = "the published script")
  public String getPublishedScript() {
    return publishedScript;
  }

  public void setPublishedScript(String publishedScript) {
    this.publishedScript = publishedScript;
  }

  public ScriptNode result(String result) {
    this.result = result;
    return this;
  }

   /**
   * the result
   * @return result
  **/
  @ApiModelProperty(value = "the result")
  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public ScriptNode script(String script) {
    this.script = script;
    return this;
  }

   /**
   * the script
   * @return script
  **/
  @ApiModelProperty(value = "the script")
  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public ScriptNode scriptLanguage(ScriptLanguageEnum scriptLanguage) {
    this.scriptLanguage = scriptLanguage;
    return this;
  }

   /**
   * the script language
   * @return scriptLanguage
  **/
  @ApiModelProperty(value = "the script language")
  public ScriptLanguageEnum getScriptLanguage() {
    return scriptLanguage;
  }

  public void setScriptLanguage(ScriptLanguageEnum scriptLanguage) {
    this.scriptLanguage = scriptLanguage;
  }

  public ScriptNode scriptType(ScriptTypeEnum scriptType) {
    this.scriptType = scriptType;
    return this;
  }

   /**
   * the script type
   * @return scriptType
  **/
  @ApiModelProperty(value = "the script type")
  public ScriptTypeEnum getScriptType() {
    return scriptType;
  }

  public void setScriptType(ScriptTypeEnum scriptType) {
    this.scriptType = scriptType;
  }

  public ScriptNode usageCount(Long usageCount) {
    this.usageCount = usageCount;
    return this;
  }

   /**
   * the usage count
   * @return usageCount
  **/
  @ApiModelProperty(value = "the usage count")
  public Long getUsageCount() {
    return usageCount;
  }

  public void setUsageCount(Long usageCount) {
    this.usageCount = usageCount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScriptNode scriptNode = (ScriptNode) o;
    return Objects.equals(this.application, scriptNode.application) &&
        Objects.equals(this.changeset, scriptNode.changeset) &&
        Objects.equals(this.changesetId, scriptNode.changesetId) &&
        Objects.equals(this.debugContext, scriptNode.debugContext) &&
        Objects.equals(this.description, scriptNode.description) &&
        Objects.equals(this.draftScript, scriptNode.draftScript) &&
        Objects.equals(this.id, scriptNode.id) &&
        Objects.equals(this.isLibrary, scriptNode.isLibrary) &&
        Objects.equals(this.isSystem, scriptNode.isSystem) &&
        Objects.equals(this.isTemplate, scriptNode.isTemplate) &&
        Objects.equals(this.name, scriptNode.name) &&
        Objects.equals(this.publishTime, scriptNode.publishTime) &&
        Objects.equals(this.publishedScript, scriptNode.publishedScript) &&
        Objects.equals(this.result, scriptNode.result) &&
        Objects.equals(this.script, scriptNode.script) &&
        Objects.equals(this.scriptLanguage, scriptNode.scriptLanguage) &&
        Objects.equals(this.scriptType, scriptNode.scriptType) &&
        Objects.equals(this.usageCount, scriptNode.usageCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(application, changeset, changesetId, debugContext, description, draftScript, id, isLibrary, isSystem, isTemplate, name, publishTime, publishedScript, result, script, scriptLanguage, scriptType, usageCount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScriptNode {\n");
    
    sb.append("    application: ").append(toIndentedString(application)).append("\n");
    sb.append("    changeset: ").append(toIndentedString(changeset)).append("\n");
    sb.append("    changesetId: ").append(toIndentedString(changesetId)).append("\n");
    sb.append("    debugContext: ").append(toIndentedString(debugContext)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    draftScript: ").append(toIndentedString(draftScript)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isLibrary: ").append(toIndentedString(isLibrary)).append("\n");
    sb.append("    isSystem: ").append(toIndentedString(isSystem)).append("\n");
    sb.append("    isTemplate: ").append(toIndentedString(isTemplate)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    publishTime: ").append(toIndentedString(publishTime)).append("\n");
    sb.append("    publishedScript: ").append(toIndentedString(publishedScript)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    script: ").append(toIndentedString(script)).append("\n");
    sb.append("    scriptLanguage: ").append(toIndentedString(scriptLanguage)).append("\n");
    sb.append("    scriptType: ").append(toIndentedString(scriptType)).append("\n");
    sb.append("    usageCount: ").append(toIndentedString(usageCount)).append("\n");
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
