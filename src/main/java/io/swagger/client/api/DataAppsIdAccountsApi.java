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

package io.swagger.client.api;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.model.AccountNode;
import io.swagger.client.model.PermissionsBoundComposite;
import io.swagger.client.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAppsIdAccountsApi {
    private ApiClient apiClient;

    public DataAppsIdAccountsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DataAppsIdAccountsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getAccountPermissionComposites
     * @param id - id of the account to fetch permissions for (required)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr Permission attributes to return.  If not present, a minimal set of attributes is returned. (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAccountPermissionCompositesCall(Long id, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{id}/permissionsBoundComposite"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getAccountPermissionCompositesValidateBeforeCall(Long id, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getAccountPermissionComposites(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccountPermissionCompositesCall(id, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get composite permissions for the specified account.
     * Combinations of permission+boundPermission will be returned.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - id of the account to fetch permissions for (required)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr Permission attributes to return.  If not present, a minimal set of attributes is returned. (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return PermissionsBoundComposite
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PermissionsBoundComposite getAccountPermissionComposites(Long id, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<PermissionsBoundComposite> resp = getAccountPermissionCompositesWithHttpInfo(id, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get composite permissions for the specified account.
     * Combinations of permission+boundPermission will be returned.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - id of the account to fetch permissions for (required)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr Permission attributes to return.  If not present, a minimal set of attributes is returned. (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return ApiResponse&lt;PermissionsBoundComposite&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PermissionsBoundComposite> getAccountPermissionCompositesWithHttpInfo(Long id, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getAccountPermissionCompositesValidateBeforeCall(id, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<PermissionsBoundComposite>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get composite permissions for the specified account. (asynchronously)
     * Combinations of permission+boundPermission will be returned.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - id of the account to fetch permissions for (required)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr Permission attributes to return.  If not present, a minimal set of attributes is returned. (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAccountPermissionCompositesAsync(Long id, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<PermissionsBoundComposite> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccountPermissionCompositesValidateBeforeCall(id, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PermissionsBoundComposite>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccountPermissions
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAccountPermissionsCall(Long accId, Long ID, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{accId}/perms"
            .replaceAll("\\{" + "accId" + "\\}", apiClient.escapeString(accId.toString()))
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getAccountPermissionsValidateBeforeCall(Long accId, Long ID, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'accId' is set
        if (accId == null) {
            throw new ApiException("Missing the required parameter 'accId' when calling getAccountPermissions(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccountPermissions(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccountPermissionsCall(accId, ID, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * GET mapped permissions for the specified account
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAccountPermissions(Long accId, Long ID, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getAccountPermissionsWithHttpInfo(accId, ID, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * GET mapped permissions for the specified account
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAccountPermissionsWithHttpInfo(Long accId, Long ID, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getAccountPermissionsValidateBeforeCall(accId, ID, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * GET mapped permissions for the specified account (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAccountPermissionsAsync(Long accId, Long ID, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccountPermissionsValidateBeforeCall(accId, ID, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccountUsers
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAccountUsersCall(Long accId, Long ID, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, List<String> listAttr, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{accId}/users"
            .replaceAll("\\{" + "accId" + "\\}", apiClient.escapeString(accId.toString()))
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
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
    private com.squareup.okhttp.Call getAccountUsersValidateBeforeCall(Long accId, Long ID, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, List<String> listAttr, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'accId' is set
        if (accId == null) {
            throw new ApiException("Missing the required parameter 'accId' when calling getAccountUsers(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccountUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccountUsersCall(accId, ID, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, listAttr, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * GET mapped users for the specified account
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAccountUsers(Long accId, Long ID, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, List<String> listAttr, String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getAccountUsersWithHttpInfo(accId, ID, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, listAttr, q, qMatch);
        return resp.getData();
    }

    /**
     * GET mapped users for the specified account
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAccountUsersWithHttpInfo(Long accId, Long ID, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, List<String> listAttr, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getAccountUsersValidateBeforeCall(accId, ID, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, listAttr, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * GET mapped users for the specified account (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param accId - id of the account to fetch users for (required)
     * @param ID - application id (required)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @param sortBy - the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAccountUsersAsync(Long accId, Long ID, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, List<String> listAttr, String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccountUsersValidateBeforeCall(accId, ID, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, listAttr, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApplicationAccountById
     * @param ID - Application ID (required)
     * @param accId - Account ID (required)
     * @param listAttr - list of attributes to be returned (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted account record specified by accId.  If true, this  basically means that we don&#x27;t care if the account record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApplicationAccountByIdCall(Long ID, Long accId, List<String> listAttr, Boolean allowDeleted, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{accId}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "accId" + "\\}", apiClient.escapeString(accId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (allowDeleted != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("allowDeleted", allowDeleted));
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
    private com.squareup.okhttp.Call getApplicationAccountByIdValidateBeforeCall(Long ID, Long accId, List<String> listAttr, Boolean allowDeleted, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getApplicationAccountById(Async)");
        }
        // verify the required parameter 'accId' is set
        if (accId == null) {
            throw new ApiException("Missing the required parameter 'accId' when calling getApplicationAccountById(Async)");
        }
        
        com.squareup.okhttp.Call call = getApplicationAccountByIdCall(ID, accId, listAttr, allowDeleted, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the details of the account with ID accId associated with an application having the ID appId
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID - Application ID (required)
     * @param accId - Account ID (required)
     * @param listAttr - list of attributes to be returned (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted account record specified by accId.  If true, this  basically means that we don&#x27;t care if the account record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getApplicationAccountById(Long ID, Long accId, List<String> listAttr, Boolean allowDeleted, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = getApplicationAccountByIdWithHttpInfo(ID, accId, listAttr, allowDeleted, attrFilter);
        return resp.getData();
    }

    /**
     * Get the details of the account with ID accId associated with an application having the ID appId
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID - Application ID (required)
     * @param accId - Account ID (required)
     * @param listAttr - list of attributes to be returned (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted account record specified by accId.  If true, this  basically means that we don&#x27;t care if the account record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getApplicationAccountByIdWithHttpInfo(Long ID, Long accId, List<String> listAttr, Boolean allowDeleted, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getApplicationAccountByIdValidateBeforeCall(ID, accId, listAttr, allowDeleted, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the details of the account with ID accId associated with an application having the ID appId (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID - Application ID (required)
     * @param accId - Account ID (required)
     * @param listAttr - list of attributes to be returned (optional)
     * @param allowDeleted Flag indicating whether to allow getting a deleted account record specified by accId.  If true, this  basically means that we don&#x27;t care if the account record has been deleted or is not in the current published snapshot. (optional, default to false)
     * @param attrFilter - filter for what kind of attributes to return in the payload (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApplicationAccountByIdAsync(Long ID, Long accId, List<String> listAttr, Boolean allowDeleted, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApplicationAccountByIdValidateBeforeCall(ID, accId, listAttr, allowDeleted, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApplicationAccounts
     * @param ID - Application ID (required)
     * @param sortBy - parameter to be sorted on (optional)
     * @param sortOrder - ascending or descending order (optional, default to ASC)
     * @param listAttr - attributes to be returned in the result (optional)
     * @param indexFrom - index from where the result is to be returned (optional, default to 0)
     * @param size - count os results to be returned (optional, default to 10)
     * @param showCt - show total result set count (optional, default to false)
     * @param q - search filter (optional)
     * @param qMatch - match mode (optional)
     * @param curateType - MAPPED, UNMAPPED, SYSTEM, ALL, NONE (optional)
     * @param searchCollectedValuesOnly - search accounts on collected values only (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApplicationAccountsCall(Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> curateType, Boolean searchCollectedValuesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
        if (curateType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "curateType", curateType));
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
    private com.squareup.okhttp.Call getApplicationAccountsValidateBeforeCall(Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> curateType, Boolean searchCollectedValuesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getApplicationAccounts(Async)");
        }
        
        com.squareup.okhttp.Call call = getApplicationAccountsCall(ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, curateType, searchCollectedValuesOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of accounts associated with an application by application ID
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID - Application ID (required)
     * @param sortBy - parameter to be sorted on (optional)
     * @param sortOrder - ascending or descending order (optional, default to ASC)
     * @param listAttr - attributes to be returned in the result (optional)
     * @param indexFrom - index from where the result is to be returned (optional, default to 0)
     * @param size - count os results to be returned (optional, default to 10)
     * @param showCt - show total result set count (optional, default to false)
     * @param q - search filter (optional)
     * @param qMatch - match mode (optional)
     * @param curateType - MAPPED, UNMAPPED, SYSTEM, ALL, NONE (optional)
     * @param searchCollectedValuesOnly - search accounts on collected values only (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getApplicationAccounts(Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> curateType, Boolean searchCollectedValuesOnly) throws ApiException {
        ApiResponse<Response> resp = getApplicationAccountsWithHttpInfo(ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, curateType, searchCollectedValuesOnly);
        return resp.getData();
    }

    /**
     * Get the list of accounts associated with an application by application ID
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID - Application ID (required)
     * @param sortBy - parameter to be sorted on (optional)
     * @param sortOrder - ascending or descending order (optional, default to ASC)
     * @param listAttr - attributes to be returned in the result (optional)
     * @param indexFrom - index from where the result is to be returned (optional, default to 0)
     * @param size - count os results to be returned (optional, default to 10)
     * @param showCt - show total result set count (optional, default to false)
     * @param q - search filter (optional)
     * @param qMatch - match mode (optional)
     * @param curateType - MAPPED, UNMAPPED, SYSTEM, ALL, NONE (optional)
     * @param searchCollectedValuesOnly - search accounts on collected values only (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getApplicationAccountsWithHttpInfo(Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> curateType, Boolean searchCollectedValuesOnly) throws ApiException {
        com.squareup.okhttp.Call call = getApplicationAccountsValidateBeforeCall(ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, curateType, searchCollectedValuesOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of accounts associated with an application by application ID (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID - Application ID (required)
     * @param sortBy - parameter to be sorted on (optional)
     * @param sortOrder - ascending or descending order (optional, default to ASC)
     * @param listAttr - attributes to be returned in the result (optional)
     * @param indexFrom - index from where the result is to be returned (optional, default to 0)
     * @param size - count os results to be returned (optional, default to 10)
     * @param showCt - show total result set count (optional, default to false)
     * @param q - search filter (optional)
     * @param qMatch - match mode (optional)
     * @param curateType - MAPPED, UNMAPPED, SYSTEM, ALL, NONE (optional)
     * @param searchCollectedValuesOnly - search accounts on collected values only (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApplicationAccountsAsync(Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, List<String> curateType, Boolean searchCollectedValuesOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApplicationAccountsValidateBeforeCall(ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, curateType, searchCollectedValuesOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApplicationAccountsPermissionAssignmentCause
     * @param ID application id (required)
     * @param accId account id (required)
     * @param permId permission id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApplicationAccountsPermissionAssignmentCauseCall(Long ID, Long accId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{accId}/perms/{permId}/assignments"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "accId" + "\\}", apiClient.escapeString(accId.toString()))
            .replaceAll("\\{" + "permId" + "\\}", apiClient.escapeString(permId.toString()));

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
    private com.squareup.okhttp.Call getApplicationAccountsPermissionAssignmentCauseValidateBeforeCall(Long ID, Long accId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getApplicationAccountsPermissionAssignmentCause(Async)");
        }
        // verify the required parameter 'accId' is set
        if (accId == null) {
            throw new ApiException("Missing the required parameter 'accId' when calling getApplicationAccountsPermissionAssignmentCause(Async)");
        }
        // verify the required parameter 'permId' is set
        if (permId == null) {
            throw new ApiException("Missing the required parameter 'permId' when calling getApplicationAccountsPermissionAssignmentCause(Async)");
        }
        
        com.squareup.okhttp.Call call = getApplicationAccountsPermissionAssignmentCauseCall(ID, accId, permId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get cause (trace) of permission assignment
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID application id (required)
     * @param accId account id (required)
     * @param permId permission id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getApplicationAccountsPermissionAssignmentCause(Long ID, Long accId, Long permId) throws ApiException {
        ApiResponse<Response> resp = getApplicationAccountsPermissionAssignmentCauseWithHttpInfo(ID, accId, permId);
        return resp.getData();
    }

    /**
     * Get cause (trace) of permission assignment
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID application id (required)
     * @param accId account id (required)
     * @param permId permission id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getApplicationAccountsPermissionAssignmentCauseWithHttpInfo(Long ID, Long accId, Long permId) throws ApiException {
        com.squareup.okhttp.Call call = getApplicationAccountsPermissionAssignmentCauseValidateBeforeCall(ID, accId, permId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get cause (trace) of permission assignment (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param ID application id (required)
     * @param accId account id (required)
     * @param permId permission id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApplicationAccountsPermissionAssignmentCauseAsync(Long ID, Long accId, Long permId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApplicationAccountsPermissionAssignmentCauseValidateBeforeCall(ID, accId, permId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSodViolations
     * @param accId - id of the account to fetch violations for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param showUnfilteredCt - When true, will show the total count of sods for the user.         Default: false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSodViolationsCall(Long accId, Long ID, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{accId}/sods"
            .replaceAll("\\{" + "accId" + "\\}", apiClient.escapeString(accId.toString()))
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getSodViolationsValidateBeforeCall(Long accId, Long ID, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'accId' is set
        if (accId == null) {
            throw new ApiException("Missing the required parameter 'accId' when calling getSodViolations(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSodViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodViolationsCall(accId, ID, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the detected SoD violations of the account with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param accId - id of the account to fetch violations for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param showUnfilteredCt - When true, will show the total count of sods for the user.         Default: false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getSodViolations(Long accId, Long ID, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getSodViolationsWithHttpInfo(accId, ID, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the detected SoD violations of the account with the specified id
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param accId - id of the account to fetch violations for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param showUnfilteredCt - When true, will show the total count of sods for the user.         Default: false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getSodViolationsWithHttpInfo(Long accId, Long ID, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getSodViolationsValidateBeforeCall(accId, ID, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the detected SoD violations of the account with the specified id (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param accId - id of the account to fetch violations for (required)
     * @param ID - application id (required)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param showUnfilteredCt - When true, will show the total count of sods for the user.         Default: false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSodViolationsAsync(Long accId, Long ID, List<String> listAttr, List<String> attrFilter, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showUnfilteredCt, String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSodViolationsValidateBeforeCall(accId, ID, listAttr, attrFilter, sortBy, sortOrder, indexFrom, size, showCt, showUnfilteredCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissionComposites
     * @param id - long account id (required)
     * @param permId - long permission id (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionCompositesCall(Long id, Long permId, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{id}/permissionAssignment"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (permId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("permId", permId));
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
    private com.squareup.okhttp.Call getUserPermissionCompositesValidateBeforeCall(Long id, Long permId, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUserPermissionComposites(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionCompositesCall(id, permId, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get permission assignment for passed account and permission.
     * This is sister method to /data/users/{id}/permissionAssignment&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - long account id (required)
     * @param permId - long permission id (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getUserPermissionComposites(Long id, Long permId, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<Response> resp = getUserPermissionCompositesWithHttpInfo(id, permId, indexFrom, size);
        return resp.getData();
    }

    /**
     * Get permission assignment for passed account and permission.
     * This is sister method to /data/users/{id}/permissionAssignment&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - long account id (required)
     * @param permId - long permission id (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getUserPermissionCompositesWithHttpInfo(Long id, Long permId, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionCompositesValidateBeforeCall(id, permId, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get permission assignment for passed account and permission. (asynchronously)
     * This is sister method to /data/users/{id}/permissionAssignment&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - long account id (required)
     * @param permId - long permission id (optional)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionCompositesAsync(Long id, Long permId, Integer indexFrom, Integer size, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserPermissionCompositesValidateBeforeCall(id, permId, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateApplicationAccount
     * @param body - the acount node object with curated data to be updated (required)
     * @param ID - id of the application the account belongs to (required)
     * @param accId - id of the account to update the curated data for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateApplicationAccountCall(AccountNode body, Long ID, Long accId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/apps/{ID}/accounts/{accId}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "accId" + "\\}", apiClient.escapeString(accId.toString()));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updateApplicationAccountValidateBeforeCall(AccountNode body, Long ID, Long accId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateApplicationAccount(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling updateApplicationAccount(Async)");
        }
        // verify the required parameter 'accId' is set
        if (accId == null) {
            throw new ApiException("Missing the required parameter 'accId' when calling updateApplicationAccount(Async)");
        }
        
        com.squareup.okhttp.Call call = updateApplicationAccountCall(body, ID, accId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the curated data for an application account
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body - the acount node object with curated data to be updated (required)
     * @param ID - id of the application the account belongs to (required)
     * @param accId - id of the account to update the curated data for (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateApplicationAccount(AccountNode body, Long ID, Long accId) throws ApiException {
        ApiResponse<Response> resp = updateApplicationAccountWithHttpInfo(body, ID, accId);
        return resp.getData();
    }

    /**
     * Update the curated data for an application account
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body - the acount node object with curated data to be updated (required)
     * @param ID - id of the application the account belongs to (required)
     * @param accId - id of the account to update the curated data for (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateApplicationAccountWithHttpInfo(AccountNode body, Long ID, Long accId) throws ApiException {
        com.squareup.okhttp.Call call = updateApplicationAccountValidateBeforeCall(body, ID, accId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the curated data for an application account (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body - the acount node object with curated data to be updated (required)
     * @param ID - id of the application the account belongs to (required)
     * @param accId - id of the account to update the curated data for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateApplicationAccountAsync(AccountNode body, Long ID, Long accId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateApplicationAccountValidateBeforeCall(body, ID, accId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
