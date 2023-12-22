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


import io.swagger.client.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCompositeApi {
    private ApiClient apiClient;

    public DataCompositeApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DataCompositeApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getSupervisorsAndGroups
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                        present. Server can return them in any order. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSupervisorsAndGroupsCall(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/composite/supervisorsAndGroups";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
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
    private com.squareup.okhttp.Call getSupervisorsAndGroupsValidateBeforeCall(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getSupervisorsAndGroupsCall(indexFrom, size, q, qMatch, listAttr, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get supervisors and groups
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                        present. Server can return them in any order. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getSupervisorsAndGroups(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr) throws ApiException {
        ApiResponse<Response> resp = getSupervisorsAndGroupsWithHttpInfo(indexFrom, size, q, qMatch, listAttr);
        return resp.getData();
    }

    /**
     * Get supervisors and groups
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                        present. Server can return them in any order. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getSupervisorsAndGroupsWithHttpInfo(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr) throws ApiException {
        com.squareup.okhttp.Call call = getSupervisorsAndGroupsValidateBeforeCall(indexFrom, size, q, qMatch, listAttr, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get supervisors and groups (asynchronously)
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                        present. Server can return them in any order. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSupervisorsAndGroupsAsync(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSupervisorsAndGroupsValidateBeforeCall(indexFrom, size, q, qMatch, listAttr, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUsersAndGroups
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                         present. Server can return them in any order. (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUsersAndGroupsCall(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, Boolean showCt, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/composite/usersAndGroups";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));

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
    private com.squareup.okhttp.Call getUsersAndGroupsValidateBeforeCall(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, Boolean showCt, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getUsersAndGroupsCall(indexFrom, size, q, qMatch, listAttr, showCt, typeAheadSearch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get users and groups
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                         present. Server can return them in any order. (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getUsersAndGroups(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, Boolean showCt, Boolean typeAheadSearch) throws ApiException {
        ApiResponse<Response> resp = getUsersAndGroupsWithHttpInfo(indexFrom, size, q, qMatch, listAttr, showCt, typeAheadSearch);
        return resp.getData();
    }

    /**
     * Get users and groups
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                         present. Server can return them in any order. (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getUsersAndGroupsWithHttpInfo(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, Boolean showCt, Boolean typeAheadSearch) throws ApiException {
        com.squareup.okhttp.Call call = getUsersAndGroupsValidateBeforeCall(indexFrom, size, q, qMatch, listAttr, showCt, typeAheadSearch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get users and groups (asynchronously)
     * Accepted Roles: * Access Request Administrator * Analytics Bootstrap Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Governance Insights Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * SaaS OPS Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size result set end position (optional, default to 10)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not                         present. Server can return them in any order. (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param typeAheadSearch pass true if the the query is for a type ahead search, false otherwise (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUsersAndGroupsAsync(Integer indexFrom, Integer size, String q, String qMatch, List<String> listAttr, Boolean showCt, Boolean typeAheadSearch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUsersAndGroupsValidateBeforeCall(indexFrom, size, q, qMatch, listAttr, showCt, typeAheadSearch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
