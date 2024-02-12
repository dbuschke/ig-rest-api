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

package de.araneaconsult.codegen.ig.rest.api;

import de.araneaconsult.codegen.ig.rest.ApiCallback;
import de.araneaconsult.codegen.ig.rest.ApiClient;
import de.araneaconsult.codegen.ig.rest.ApiException;
import de.araneaconsult.codegen.ig.rest.ApiResponse;
import de.araneaconsult.codegen.ig.rest.Configuration;
import de.araneaconsult.codegen.ig.rest.Pair;
import de.araneaconsult.codegen.ig.rest.ProgressRequestBody;
import de.araneaconsult.codegen.ig.rest.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import de.araneaconsult.codegen.ig.rest.model.AccountListNode;
import de.araneaconsult.codegen.ig.rest.model.Authcauses;
import de.araneaconsult.codegen.ig.rest.model.Groups;
import de.araneaconsult.codegen.ig.rest.model.PermissionsBoundComposite;
import de.araneaconsult.codegen.ig.rest.model.Response;
import de.araneaconsult.codegen.ig.rest.model.Roles;
import de.araneaconsult.codegen.ig.rest.model.SearchCriteria;
import de.araneaconsult.codegen.ig.rest.model.SystemDataListNode;
import de.araneaconsult.codegen.ig.rest.model.User;
import de.araneaconsult.codegen.ig.rest.model.Users;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUsersApi {
    private ApiClient apiClient;

    public DataUsersApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DataUsersApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getAffiliations
     * @param id id of the user to fetch affiliations for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                    duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAffiliationsCall(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/affiliations"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getAffiliationsValidateBeforeCall(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getAffiliations(Async)");
        }
        
        com.squareup.okhttp.Call call = getAffiliationsCall(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the affiliated users of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch affiliations for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                    duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getAffiliations(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Users> resp = getAffiliationsWithHttpInfo(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the affiliated users of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch affiliations for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                    duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getAffiliationsWithHttpInfo(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getAffiliationsValidateBeforeCall(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the affiliated users of the user with the specified id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch affiliations for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                    duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAffiliationsAsync(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Users> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getAffiliationsValidateBeforeCall(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRoles
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the unfiltered count of roles for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRolesCall(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/roles"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getRolesValidateBeforeCall(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getRoles(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getRolesCall(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the detected roles of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the unfiltered count of roles for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRoles(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getRolesWithHttpInfo(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the detected roles of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the unfiltered count of roles for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRolesWithHttpInfo(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getRolesValidateBeforeCall(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the detected roles of the user with the specified id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the unfiltered count of roles for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRolesAsync(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getRolesValidateBeforeCall(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSodViolations
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the total count of sods for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSodViolationsCall(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/sods"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getSodViolationsValidateBeforeCall(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSodViolations(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getSodViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodViolationsCall(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the detected SoD violations of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the total count of sods for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getSodViolations(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getSodViolationsWithHttpInfo(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the detected SoD violations of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the total count of sods for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getSodViolationsWithHttpInfo(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getSodViolationsValidateBeforeCall(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the detected SoD violations of the user with the specified id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                             this        true on the initial call to obtain the total size of the search        result list. Otherwise there will                             be duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param showUnfilteredCt When true, will show the total count of sods for the user.        Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                             exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSodViolationsAsync(SearchCriteria body, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getSodViolationsValidateBeforeCall(body, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSubordinates
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                               true on the initial call to obtain the total size of the search        result list. Otherwise there will be                        duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q k search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                        match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSubordinatesCall(Long id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/supervising"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getSubordinatesValidateBeforeCall(Long id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getSubordinates(Async)");
        }
        
        com.squareup.okhttp.Call call = getSubordinatesCall(id, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the subordinates of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                               true on the initial call to obtain the total size of the search        result list. Otherwise there will be                        duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q k search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                        match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getSubordinates(Long id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Users> resp = getSubordinatesWithHttpInfo(id, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the subordinates of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                               true on the initial call to obtain the total size of the search        result list. Otherwise there will be                        duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q k search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                        match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getSubordinatesWithHttpInfo(Long id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getSubordinatesValidateBeforeCall(id, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the subordinates of the user with the specified id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch subordinates for (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                               true on the initial call to obtain the total size of the search        result list. Otherwise there will be                        duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q k search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                        match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSubordinatesAsync(Long id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Users> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getSubordinatesValidateBeforeCall(id, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getTechnicalRoleAssignmentDetails
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getTechnicalRoleAssignmentDetailsCall(Long userId, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{userId}/role/{id}/details"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getTechnicalRoleAssignmentDetailsValidateBeforeCall(Long userId, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getTechnicalRoleAssignmentDetails(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getTechnicalRoleAssignmentDetails(Async)");
        }
        
        com.squareup.okhttp.Call call = getTechnicalRoleAssignmentDetailsCall(userId, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the details of a role assignment for the given user.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getTechnicalRoleAssignmentDetails(Long userId, Long id) throws ApiException {
        ApiResponse<Response> resp = getTechnicalRoleAssignmentDetailsWithHttpInfo(userId, id);
        return resp.getData();
    }

    /**
     * Get the details of a role assignment for the given user.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getTechnicalRoleAssignmentDetailsWithHttpInfo(Long userId, Long id) throws ApiException {
        com.squareup.okhttp.Call call = getTechnicalRoleAssignmentDetailsValidateBeforeCall(userId, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the details of a role assignment for the given user. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getTechnicalRoleAssignmentDetailsAsync(Long userId, Long id, final ApiCallback<Response> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getTechnicalRoleAssignmentDetailsValidateBeforeCall(userId, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getTechnicalRolePerms
     * @param body Filter the policies by values of specific fields (required)
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param showCt Flag to return count of search. (optional, default to false)
     * @param state ALL, return all permissions of the technical role,                with information about whether user holds/does not                          hold the permission        - ASSIGNED, return permissions held by user that are included in technical role        -                          MISSING, return permissions not held by user that are included in technical role. (optional, default to ALL)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getTechnicalRolePermsCall(SearchCriteria body, Long userId, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Boolean showCt, String state, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/users/{userId}/role/{id}/perms"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (state != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state", state));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getTechnicalRolePermsValidateBeforeCall(SearchCriteria body, Long userId, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Boolean showCt, String state, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getTechnicalRolePerms(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getTechnicalRolePerms(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getTechnicalRolePerms(Async)");
        }
        
        com.squareup.okhttp.Call call = getTechnicalRolePermsCall(body, userId, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, q, qMatch, showCt, state, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the permissions that the user does not hold for the technical role.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param showCt Flag to return count of search. (optional, default to false)
     * @param state ALL, return all permissions of the technical role,                with information about whether user holds/does not                          hold the permission        - ASSIGNED, return permissions held by user that are included in technical role        -                          MISSING, return permissions not held by user that are included in technical role. (optional, default to ALL)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getTechnicalRolePerms(SearchCriteria body, Long userId, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Boolean showCt, String state) throws ApiException {
        ApiResponse<Response> resp = getTechnicalRolePermsWithHttpInfo(body, userId, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, q, qMatch, showCt, state);
        return resp.getData();
    }

    /**
     * Get the permissions that the user does not hold for the technical role.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param showCt Flag to return count of search. (optional, default to false)
     * @param state ALL, return all permissions of the technical role,                with information about whether user holds/does not                          hold the permission        - ASSIGNED, return permissions held by user that are included in technical role        -                          MISSING, return permissions not held by user that are included in technical role. (optional, default to ALL)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getTechnicalRolePermsWithHttpInfo(SearchCriteria body, Long userId, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Boolean showCt, String state) throws ApiException {
        com.squareup.okhttp.Call call = getTechnicalRolePermsValidateBeforeCall(body, userId, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, q, qMatch, showCt, state, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the permissions that the user does not hold for the technical role. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param userId id of the user (required)
     * @param id id of the technical role (required)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param showCt Flag to return count of search. (optional, default to false)
     * @param state ALL, return all permissions of the technical role,                with information about whether user holds/does not                          hold the permission        - ASSIGNED, return permissions held by user that are included in technical role        -                          MISSING, return permissions not held by user that are included in technical role. (optional, default to ALL)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getTechnicalRolePermsAsync(SearchCriteria body, Long userId, Long id, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Boolean showCt, String state, final ApiCallback<Response> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getTechnicalRolePermsValidateBeforeCall(body, userId, id, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, q, qMatch, showCt, state, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUser
     * @param id collection id (required)
     * @param attrFilter filter for what kind of attributes to return in the payload (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted user record specified by userId.  If true, this basically means that                          we don&#x27;t care if the user record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserCall(Long id, List<String> attrFilter, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (allowDeleted != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("allowDeleted", allowDeleted));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserValidateBeforeCall(Long id, List<String> attrFilter, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUser(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserCall(id, attrFilter, allowDeleted, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get user for specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id collection id (required)
     * @param attrFilter filter for what kind of attributes to return in the payload (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted user record specified by userId.  If true, this basically means that                          we don&#x27;t care if the user record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @return User
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public User getUser(Long id, List<String> attrFilter, Boolean allowDeleted) throws ApiException {
        ApiResponse<User> resp = getUserWithHttpInfo(id, attrFilter, allowDeleted);
        return resp.getData();
    }

    /**
     * Get user for specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id collection id (required)
     * @param attrFilter filter for what kind of attributes to return in the payload (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted user record specified by userId.  If true, this basically means that                          we don&#x27;t care if the user record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @return ApiResponse&lt;User&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<User> getUserWithHttpInfo(Long id, List<String> attrFilter, Boolean allowDeleted) throws ApiException {
        com.squareup.okhttp.Call call = getUserValidateBeforeCall(id, attrFilter, allowDeleted, null, null);
        Type localVarReturnType = new TypeToken<User>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get user for specified id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id collection id (required)
     * @param attrFilter filter for what kind of attributes to return in the payload (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted user record specified by userId.  If true, this basically means that                          we don&#x27;t care if the user record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserAsync(Long id, List<String> attrFilter, Boolean allowDeleted, final ApiCallback<User> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserValidateBeforeCall(id, attrFilter, allowDeleted, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<User>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserAccounts
     * @param id id of the user to fetch accounts for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr attributes to be returned in the result (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                                this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                               duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserAccountsCall(Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/accounts"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserAccountsValidateBeforeCall(Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserAccounts(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserAccountsCall(id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the accounts of an user
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch accounts for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr attributes to be returned in the result (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                                this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                               duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return AccountListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccountListNode getUserAccounts(Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> attrFilter) throws ApiException {
        ApiResponse<AccountListNode> resp = getUserAccountsWithHttpInfo(id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, attrFilter);
        return resp.getData();
    }

    /**
     * Get the accounts of an user
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch accounts for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr attributes to be returned in the result (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                                this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                               duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;AccountListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccountListNode> getUserAccountsWithHttpInfo(Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getUserAccountsValidateBeforeCall(id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<AccountListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the accounts of an user (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch accounts for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr attributes to be returned in the result (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                                this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                               duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserAccountsAsync(Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> attrFilter, final ApiCallback<AccountListNode> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserAccountsValidateBeforeCall(id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccountListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRoles
     * @param id id of the user (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesCall(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/broles"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserBusinessRolesValidateBeforeCall(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserBusinessRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRolesCall(id, sortBy, sortOrder, listAttr, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business roles a user belongs to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getUserBusinessRoles(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter) throws ApiException {
        ApiResponse<Roles> resp = getUserBusinessRolesWithHttpInfo(id, sortBy, sortOrder, listAttr, attrFilter);
        return resp.getData();
    }

    /**
     * Get the business roles a user belongs to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getUserBusinessRolesWithHttpInfo(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRolesValidateBeforeCall(id, sortBy, sortOrder, listAttr, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business roles a user belongs to. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesAsync(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, final ApiCallback<Roles> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserBusinessRolesValidateBeforeCall(id, sortBy, sortOrder, listAttr, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserByDn
     * @param body user dn to search for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserByDnCall(User body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/users/userDn";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserByDnValidateBeforeCall(User body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserByDn(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserByDnCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return a user matching the given dn
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body user dn to search for (required)
     * @return Authcauses
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Authcauses getUserByDn(User body) throws ApiException {
        ApiResponse<Authcauses> resp = getUserByDnWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Return a user matching the given dn
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body user dn to search for (required)
     * @return ApiResponse&lt;Authcauses&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Authcauses> getUserByDnWithHttpInfo(User body) throws ApiException {
        com.squareup.okhttp.Call call = getUserByDnValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Authcauses>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return a user matching the given dn (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body user dn to search for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserByDnAsync(User body, final ApiCallback<Authcauses> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserByDnValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Authcauses>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserGroups
     * @param id id of the user to fetch groups for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any order. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserGroupsCall(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/groups"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserGroupsValidateBeforeCall(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserGroups(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserGroupsCall(id, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get groups for the specified user
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch groups for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any order. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return Groups
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Groups getUserGroups(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Groups> resp = getUserGroupsWithHttpInfo(id, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get groups for the specified user
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch groups for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any order. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return ApiResponse&lt;Groups&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Groups> getUserGroupsWithHttpInfo(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getUserGroupsValidateBeforeCall(id, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Groups>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get groups for the specified user (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch groups for (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any order. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this        true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserGroupsAsync(Long id, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Groups> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserGroupsValidateBeforeCall(id, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Groups>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserLogins
     * @param id id of the user to fetch accounts for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param accountId long account id (optional, to get info for one account) (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character* (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserLoginsCall(Long id, List<String> attrFilter, Long accountId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/systemData"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (accountId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("accountId", accountId));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserLoginsValidateBeforeCall(Long id, List<String> attrFilter, Long accountId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserLogins(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserLoginsCall(id, attrFilter, accountId, sortBy, sortOrder, indexFrom, size, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the accounts of an user
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch accounts for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param accountId long account id (optional, to get info for one account) (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character* (optional, default to ANY)
     * @return SystemDataListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SystemDataListNode getUserLogins(Long id, List<String> attrFilter, Long accountId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch) throws ApiException {
        ApiResponse<SystemDataListNode> resp = getUserLoginsWithHttpInfo(id, attrFilter, accountId, sortBy, sortOrder, indexFrom, size, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the accounts of an user
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch accounts for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param accountId long account id (optional, to get info for one account) (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character* (optional, default to ANY)
     * @return ApiResponse&lt;SystemDataListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SystemDataListNode> getUserLoginsWithHttpInfo(Long id, List<String> attrFilter, Long accountId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getUserLoginsValidateBeforeCall(id, attrFilter, accountId, sortBy, sortOrder, indexFrom, size, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<SystemDataListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the accounts of an user (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch accounts for (required)
     * @param attrFilter filters for types of attributes to return in the payload usage: attrFilter&#x3D;quickInfo (optional)
     * @param accountId long account id (optional, to get info for one account) (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character* (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserLoginsAsync(Long id, List<String> attrFilter, Long accountId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, final ApiCallback<SystemDataListNode> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserLoginsValidateBeforeCall(id, attrFilter, accountId, sortBy, sortOrder, indexFrom, size, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SystemDataListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissionComposites
     * @param id long user id (required)
     * @param permId long permission id (optional, get specific permission for all users accounts) (optional)
     * @param acctId long account id (optional, get specific permission for specific user account) (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionCompositesCall(Long id, Long permId, Long acctId, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/permissionAssignment"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (permId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("permId", permId));
        if (acctId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("acctId", acctId));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserPermissionCompositesValidateBeforeCall(Long id, Long permId, Long acctId, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserPermissionComposites(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionCompositesCall(id, permId, acctId, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get permission assignment for passed userId, permId and appId This is sister method to /data/apps/{ID}/accounts/{id}/permissionAssignment
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id long user id (required)
     * @param permId long permission id (optional, get specific permission for all users accounts) (optional)
     * @param acctId long account id (optional, get specific permission for specific user account) (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getUserPermissionComposites(Long id, Long permId, Long acctId, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<Response> resp = getUserPermissionCompositesWithHttpInfo(id, permId, acctId, indexFrom, size);
        return resp.getData();
    }

    /**
     * Get permission assignment for passed userId, permId and appId This is sister method to /data/apps/{ID}/accounts/{id}/permissionAssignment
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id long user id (required)
     * @param permId long permission id (optional, get specific permission for all users accounts) (optional)
     * @param acctId long account id (optional, get specific permission for specific user account) (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getUserPermissionCompositesWithHttpInfo(Long id, Long permId, Long acctId, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionCompositesValidateBeforeCall(id, permId, acctId, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get permission assignment for passed userId, permId and appId This is sister method to /data/apps/{ID}/accounts/{id}/permissionAssignment (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id long user id (required)
     * @param permId long permission id (optional, get specific permission for all users accounts) (optional)
     * @param acctId long account id (optional, get specific permission for specific user account) (optional)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionCompositesAsync(Long id, Long permId, Long acctId, Integer indexFrom, Integer size, final ApiCallback<Response> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserPermissionCompositesValidateBeforeCall(id, permId, acctId, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissionComposites_0
     * @param id id of the user to fetch groups for (required)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                           true on the initial call to obtain the total size of the search        result list. Otherwise there will be duplicate                      queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                      match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionComposites_0Call(Long id, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/permissionsBoundComposite"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserPermissionComposites_0ValidateBeforeCall(Long id, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserPermissionComposites_0(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionComposites_0Call(id, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get composite permissions for the specified user.
     * Combinations of permission+boundPermission will be returned.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param id id of the user to fetch groups for (required)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                           true on the initial call to obtain the total size of the search        result list. Otherwise there will be duplicate                      queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                      match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return PermissionsBoundComposite
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PermissionsBoundComposite getUserPermissionComposites_0(Long id, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<PermissionsBoundComposite> resp = getUserPermissionComposites_0WithHttpInfo(id, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get composite permissions for the specified user.
     * Combinations of permission+boundPermission will be returned.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param id id of the user to fetch groups for (required)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                           true on the initial call to obtain the total size of the search        result list. Otherwise there will be duplicate                      queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                      match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return ApiResponse&lt;PermissionsBoundComposite&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PermissionsBoundComposite> getUserPermissionComposites_0WithHttpInfo(Long id, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionComposites_0ValidateBeforeCall(id, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<PermissionsBoundComposite>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get composite permissions for the specified user. (asynchronously)
     * Combinations of permission+boundPermission will be returned.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param id id of the user to fetch groups for (required)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set this                           true on the initial call to obtain the total size of the search        result list. Otherwise there will be duplicate                      queries made, one        to obtain the result set and the other to get the count. Default: false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D; exact                      match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionComposites_0Async(Long id, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<PermissionsBoundComposite> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserPermissionComposites_0ValidateBeforeCall(id, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PermissionsBoundComposite>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPhotos
     * @param body List of users to fetch photos for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPhotosCall(Users body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/users/photos";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserPhotosValidateBeforeCall(Users body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserPhotos(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPhotosCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * DEPRECATED in AR2.0 Return photos for the argument users.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body List of users to fetch photos for (required)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getUserPhotos(Users body) throws ApiException {
        ApiResponse<Users> resp = getUserPhotosWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * DEPRECATED in AR2.0 Return photos for the argument users.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body List of users to fetch photos for (required)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getUserPhotosWithHttpInfo(Users body) throws ApiException {
        com.squareup.okhttp.Call call = getUserPhotosValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * DEPRECATED in AR2.0 Return photos for the argument users. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body List of users to fetch photos for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPhotosAsync(Users body, final ApiCallback<Users> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserPhotosValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserSupervisor
     * @param id id of the user to fetch the supervisor for (required)
     * @param listAttr the attributes to return in the payload. Returns all if not        present (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserSupervisorCall(Long id, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/{id}/supervisor"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUserSupervisorValidateBeforeCall(Long id, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserSupervisor(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserSupervisorCall(id, listAttr, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the supervisor of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch the supervisor for (required)
     * @param listAttr the attributes to return in the payload. Returns all if not        present (optional)
     * @return User
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public User getUserSupervisor(Long id, List<String> listAttr) throws ApiException {
        ApiResponse<User> resp = getUserSupervisorWithHttpInfo(id, listAttr);
        return resp.getData();
    }

    /**
     * Get the supervisor of the user with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch the supervisor for (required)
     * @param listAttr the attributes to return in the payload. Returns all if not        present (optional)
     * @return ApiResponse&lt;User&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<User> getUserSupervisorWithHttpInfo(Long id, List<String> listAttr) throws ApiException {
        com.squareup.okhttp.Call call = getUserSupervisorValidateBeforeCall(id, listAttr, null, null);
        Type localVarReturnType = new TypeToken<User>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the supervisor of the user with the specified id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id id of the user to fetch the supervisor for (required)
     * @param listAttr the attributes to return in the payload. Returns all if not        present (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserSupervisorAsync(Long id, List<String> listAttr, final ApiCallback<User> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUserSupervisorValidateBeforeCall(id, listAttr, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<User>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUsers
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUsersCall(String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (searchCollectedValuesOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("searchCollectedValuesOnly", searchCollectedValuesOnly));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUsersValidateBeforeCall(String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getUsersCall(sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get users
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor * Review Owner * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getUsers(String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly) throws ApiException {
        ApiResponse<Users> resp = getUsersWithHttpInfo(sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly);
        return resp.getData();
    }

    /**
     * Get users
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor * Review Owner * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getUsersWithHttpInfo(String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly) throws ApiException {
        com.squareup.okhttp.Call call = getUsersValidateBeforeCall(sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get users (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor * Review Owner * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUsersAsync(String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly, final ApiCallback<Users> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUsersValidateBeforeCall(sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUsersAdvanced
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch true if the query is for a type ahead search false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUsersAdvancedCall(SearchCriteria body, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/users/search";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (searchCollectedValuesOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("searchCollectedValuesOnly", searchCollectedValuesOnly));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUsersAdvancedValidateBeforeCall(SearchCriteria body, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUsersAdvanced(Async)");
        }
        
        com.squareup.okhttp.Call call = getUsersAdvancedCall(body, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get users
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch true if the query is for a type ahead search false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getUsersAdvanced(SearchCriteria body, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly) throws ApiException {
        ApiResponse<Users> resp = getUsersAdvancedWithHttpInfo(body, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly);
        return resp.getData();
    }

    /**
     * Get users
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch true if the query is for a type ahead search false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getUsersAdvancedWithHttpInfo(SearchCriteria body, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly) throws ApiException {
        com.squareup.okhttp.Call call = getUsersAdvancedValidateBeforeCall(body, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get users (asynchronously)
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param listAttr The attributes to return in the payload. Returns all if not        present. Server can return them in any                                   order. (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param showCt show total result set count (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param typeAheadSearch true if the query is for a type ahead search false otherwise (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param searchCollectedValuesOnly Run search on collected values ONLY, not on curated values (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUsersAdvancedAsync(SearchCriteria body, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, List<String> attrFilter, Boolean searchCollectedValuesOnly, final ApiCallback<Users> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUsersAdvancedValidateBeforeCall(body, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, attrFilter, searchCollectedValuesOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUsersForAccountByUnqId
     * @param uniqueAccId unique account id (required)
     * @param allowDeleted return deleted users (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUsersForAccountByUnqIdCall(String uniqueAccId, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/users/account/{uniqueAccId}"
            .replaceAll("\\{" + "uniqueAccId" + "\\}", apiClient.escapeString(uniqueAccId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (allowDeleted != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("allowDeleted", allowDeleted));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getUsersForAccountByUnqIdValidateBeforeCall(String uniqueAccId, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueAccId' is set
        if (uniqueAccId == null) {
            throw new ApiException("Missing the required parameter 'uniqueAccId' when calling getUsersForAccountByUnqId(Async)");
        }
        
        com.squareup.okhttp.Call call = getUsersForAccountByUnqIdCall(uniqueAccId, allowDeleted, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get users mapping to the account by unique account id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param uniqueAccId unique account id (required)
     * @param allowDeleted return deleted users (optional, default to true)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getUsersForAccountByUnqId(String uniqueAccId, Boolean allowDeleted) throws ApiException {
        ApiResponse<Response> resp = getUsersForAccountByUnqIdWithHttpInfo(uniqueAccId, allowDeleted);
        return resp.getData();
    }

    /**
     * Get users mapping to the account by unique account id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param uniqueAccId unique account id (required)
     * @param allowDeleted return deleted users (optional, default to true)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getUsersForAccountByUnqIdWithHttpInfo(String uniqueAccId, Boolean allowDeleted) throws ApiException {
        com.squareup.okhttp.Call call = getUsersForAccountByUnqIdValidateBeforeCall(uniqueAccId, allowDeleted, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get users mapping to the account by unique account id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param uniqueAccId unique account id (required)
     * @param allowDeleted return deleted users (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUsersForAccountByUnqIdAsync(String uniqueAccId, Boolean allowDeleted, final ApiCallback<Response> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getUsersForAccountByUnqIdValidateBeforeCall(uniqueAccId, allowDeleted, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
