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


import de.araneaconsult.codegen.ig.rest.model.CertifyItems;
import de.araneaconsult.codegen.ig.rest.model.Download;
import de.araneaconsult.codegen.ig.rest.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewReviewersApi {
    private ApiClient apiClient;

    public ReviewReviewersApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ReviewReviewersApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for cancelDownload
     * @param id - Unique download id assigned then download was started (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cancelDownloadCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/download/{id}"
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
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call cancelDownloadValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling cancelDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = cancelDownloadCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Cancel, cleanup the download CSV file
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response cancelDownload(String id) throws ApiException {
        ApiResponse<Response> resp = cancelDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Cancel, cleanup the download CSV file
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> cancelDownloadWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = cancelDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Cancel, cleanup the download CSV file (asynchronously)
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cancelDownloadAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = cancelDownloadValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for finishDownload
     * @param id - Unique download id assigned then download was started (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call finishDownloadCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/download/{id}"
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
    private com.squareup.okhttp.Call finishDownloadValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling finishDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = finishDownloadCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the download file
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response finishDownload(String id) throws ApiException {
        ApiResponse<Response> resp = finishDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the download file
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> finishDownloadWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = finishDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the download file (asynchronously)
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call finishDownloadAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = finishDownloadValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewerInstanceMembers
     * @param revrInstId reviewer instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewerInstanceMembersCall(Long revrInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{revrInstId}/members"
            .replaceAll("\\{" + "revrInstId" + "\\}", apiClient.escapeString(revrInstId.toString()));

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
    private com.squareup.okhttp.Call getReviewerInstanceMembersValidateBeforeCall(Long revrInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'revrInstId' is set
        if (revrInstId == null) {
            throw new ApiException("Missing the required parameter 'revrInstId' when calling getReviewerInstanceMembers(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewerInstanceMembersCall(revrInstId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get reviewer instance members by reviewer instance ID
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param revrInstId reviewer instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewerInstanceMembers(Long revrInstId) throws ApiException {
        ApiResponse<Response> resp = getReviewerInstanceMembersWithHttpInfo(revrInstId);
        return resp.getData();
    }

    /**
     * Get reviewer instance members by reviewer instance ID
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param revrInstId reviewer instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewerInstanceMembersWithHttpInfo(Long revrInstId) throws ApiException {
        com.squareup.okhttp.Call call = getReviewerInstanceMembersValidateBeforeCall(revrInstId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get reviewer instance members by reviewer instance ID (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param revrInstId reviewer instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewerInstanceMembersAsync(Long revrInstId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewerInstanceMembersValidateBeforeCall(revrInstId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewerInstanceMembers_0
     * @param revrInstId reviewer instance id (required)
     * @param revrInstSetId reviewer set member instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewerInstanceMembers_0Call(Long revrInstId, Long revrInstSetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{revrInstId}/members/{revrInstSetId}"
            .replaceAll("\\{" + "revrInstId" + "\\}", apiClient.escapeString(revrInstId.toString()))
            .replaceAll("\\{" + "revrInstSetId" + "\\}", apiClient.escapeString(revrInstSetId.toString()));

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
    private com.squareup.okhttp.Call getReviewerInstanceMembers_0ValidateBeforeCall(Long revrInstId, Long revrInstSetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'revrInstId' is set
        if (revrInstId == null) {
            throw new ApiException("Missing the required parameter 'revrInstId' when calling getReviewerInstanceMembers_0(Async)");
        }
        // verify the required parameter 'revrInstSetId' is set
        if (revrInstSetId == null) {
            throw new ApiException("Missing the required parameter 'revrInstSetId' when calling getReviewerInstanceMembers_0(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewerInstanceMembers_0Call(revrInstId, revrInstSetId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get reviewer instance members by reviewer instance ID and reviewer set member instance ID
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param revrInstId reviewer instance id (required)
     * @param revrInstSetId reviewer set member instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewerInstanceMembers_0(Long revrInstId, Long revrInstSetId) throws ApiException {
        ApiResponse<Response> resp = getReviewerInstanceMembers_0WithHttpInfo(revrInstId, revrInstSetId);
        return resp.getData();
    }

    /**
     * Get reviewer instance members by reviewer instance ID and reviewer set member instance ID
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param revrInstId reviewer instance id (required)
     * @param revrInstSetId reviewer set member instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewerInstanceMembers_0WithHttpInfo(Long revrInstId, Long revrInstSetId) throws ApiException {
        com.squareup.okhttp.Call call = getReviewerInstanceMembers_0ValidateBeforeCall(revrInstId, revrInstSetId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get reviewer instance members by reviewer instance ID and reviewer set member instance ID (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param revrInstId reviewer instance id (required)
     * @param revrInstSetId reviewer set member instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewerInstanceMembers_0Async(Long revrInstId, Long revrInstSetId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewerInstanceMembers_0ValidateBeforeCall(revrInstId, revrInstSetId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewerTaskById
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param actionId action id of task (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskByIdCall(Long userId, Long revInstId, Long actionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{userId}/tasks/{revInstId}/status/{actionId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "revInstId" + "\\}", apiClient.escapeString(revInstId.toString()))
            .replaceAll("\\{" + "actionId" + "\\}", apiClient.escapeString(actionId.toString()));

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
    private com.squareup.okhttp.Call getReviewerTaskByIdValidateBeforeCall(Long userId, Long revInstId, Long actionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getReviewerTaskById(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling getReviewerTaskById(Async)");
        }
        // verify the required parameter 'actionId' is set
        if (actionId == null) {
            throw new ApiException("Missing the required parameter 'actionId' when calling getReviewerTaskById(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewerTaskByIdCall(userId, revInstId, actionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get status information for specific reviewer tasks
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param actionId action id of task (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewerTaskById(Long userId, Long revInstId, Long actionId) throws ApiException {
        ApiResponse<Response> resp = getReviewerTaskByIdWithHttpInfo(userId, revInstId, actionId);
        return resp.getData();
    }

    /**
     * Get status information for specific reviewer tasks
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param actionId action id of task (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewerTaskByIdWithHttpInfo(Long userId, Long revInstId, Long actionId) throws ApiException {
        com.squareup.okhttp.Call call = getReviewerTaskByIdValidateBeforeCall(userId, revInstId, actionId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get status information for specific reviewer tasks (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param actionId action id of task (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskByIdAsync(Long userId, Long revInstId, Long actionId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewerTaskByIdValidateBeforeCall(userId, revInstId, actionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewerTaskReviewItemCounts
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgress &#x3D; show count of tasks in progress for reviewer and specified query parameters (optional, default to false)
     * @param inProgressPending &#x3D; show count of tasks in progress and pending action (i.e. no action yet) for reviewer and specified query parameters (optional, default to false)
     * @param certified &#x3D; show count of tasks in certified for reviewer and specified query parameters (optional, default to false)
     * @param pending &#x3D; show count of tasks in progress and pending for reviewer and specified query parameters (optional, default to false)
     * @param total &#x3D; show count of tasks for reviewer and specified query parameters (optional, default to false)
     * @param readyToCertify &#x3D; show the count of tasks ready to be certified/submitted for the reviewer (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskReviewItemCountsCall(Long userId, Long revInstId, List<String> qCol, String q, String qMatch, Boolean inProgress, Boolean inProgressPending, Boolean certified, Boolean pending, Boolean total, Boolean readyToCertify, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{userId}/tasks/{revInstId}/count"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "revInstId" + "\\}", apiClient.escapeString(revInstId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (qCol != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "qCol", qCol));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (inProgress != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgress", inProgress));
        if (inProgressPending != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgressPending", inProgressPending));
        if (certified != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certified", certified));
        if (pending != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pending", pending));
        if (total != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("total", total));
        if (readyToCertify != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("readyToCertify", readyToCertify));

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
    private com.squareup.okhttp.Call getReviewerTaskReviewItemCountsValidateBeforeCall(Long userId, Long revInstId, List<String> qCol, String q, String qMatch, Boolean inProgress, Boolean inProgressPending, Boolean certified, Boolean pending, Boolean total, Boolean readyToCertify, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getReviewerTaskReviewItemCounts(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling getReviewerTaskReviewItemCounts(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewerTaskReviewItemCountsCall(userId, revInstId, qCol, q, qMatch, inProgress, inProgressPending, certified, pending, total, readyToCertify, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get review items counts reviewer and review instance based on query
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgress &#x3D; show count of tasks in progress for reviewer and specified query parameters (optional, default to false)
     * @param inProgressPending &#x3D; show count of tasks in progress and pending action (i.e. no action yet) for reviewer and specified query parameters (optional, default to false)
     * @param certified &#x3D; show count of tasks in certified for reviewer and specified query parameters (optional, default to false)
     * @param pending &#x3D; show count of tasks in progress and pending for reviewer and specified query parameters (optional, default to false)
     * @param total &#x3D; show count of tasks for reviewer and specified query parameters (optional, default to false)
     * @param readyToCertify &#x3D; show the count of tasks ready to be certified/submitted for the reviewer (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewerTaskReviewItemCounts(Long userId, Long revInstId, List<String> qCol, String q, String qMatch, Boolean inProgress, Boolean inProgressPending, Boolean certified, Boolean pending, Boolean total, Boolean readyToCertify) throws ApiException {
        ApiResponse<Response> resp = getReviewerTaskReviewItemCountsWithHttpInfo(userId, revInstId, qCol, q, qMatch, inProgress, inProgressPending, certified, pending, total, readyToCertify);
        return resp.getData();
    }

    /**
     * Get review items counts reviewer and review instance based on query
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgress &#x3D; show count of tasks in progress for reviewer and specified query parameters (optional, default to false)
     * @param inProgressPending &#x3D; show count of tasks in progress and pending action (i.e. no action yet) for reviewer and specified query parameters (optional, default to false)
     * @param certified &#x3D; show count of tasks in certified for reviewer and specified query parameters (optional, default to false)
     * @param pending &#x3D; show count of tasks in progress and pending for reviewer and specified query parameters (optional, default to false)
     * @param total &#x3D; show count of tasks for reviewer and specified query parameters (optional, default to false)
     * @param readyToCertify &#x3D; show the count of tasks ready to be certified/submitted for the reviewer (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewerTaskReviewItemCountsWithHttpInfo(Long userId, Long revInstId, List<String> qCol, String q, String qMatch, Boolean inProgress, Boolean inProgressPending, Boolean certified, Boolean pending, Boolean total, Boolean readyToCertify) throws ApiException {
        com.squareup.okhttp.Call call = getReviewerTaskReviewItemCountsValidateBeforeCall(userId, revInstId, qCol, q, qMatch, inProgress, inProgressPending, certified, pending, total, readyToCertify, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get review items counts reviewer and review instance based on query (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgress &#x3D; show count of tasks in progress for reviewer and specified query parameters (optional, default to false)
     * @param inProgressPending &#x3D; show count of tasks in progress and pending action (i.e. no action yet) for reviewer and specified query parameters (optional, default to false)
     * @param certified &#x3D; show count of tasks in certified for reviewer and specified query parameters (optional, default to false)
     * @param pending &#x3D; show count of tasks in progress and pending for reviewer and specified query parameters (optional, default to false)
     * @param total &#x3D; show count of tasks for reviewer and specified query parameters (optional, default to false)
     * @param readyToCertify &#x3D; show the count of tasks ready to be certified/submitted for the reviewer (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskReviewItemCountsAsync(Long userId, Long revInstId, List<String> qCol, String q, String qMatch, Boolean inProgress, Boolean inProgressPending, Boolean certified, Boolean pending, Boolean total, Boolean readyToCertify, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewerTaskReviewItemCountsValidateBeforeCall(userId, revInstId, qCol, q, qMatch, inProgress, inProgressPending, certified, pending, total, readyToCertify, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewerTaskReviewItems
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskReviewItemsCall(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{userId}/tasks/{revInstId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "revInstId" + "\\}", apiClient.escapeString(revInstId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (qCol != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "qCol", qCol));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (inProgressOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgressOnly", inProgressOnly));
        if (certifiedOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certifiedOnly", certifiedOnly));
        if (pendingOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pendingOnly", pendingOnly));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));

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
    private com.squareup.okhttp.Call getReviewerTaskReviewItemsValidateBeforeCall(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getReviewerTaskReviewItems(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling getReviewerTaskReviewItems(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewerTaskReviewItemsCall(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, indexFrom, size, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get review items for reviewer and review instance.
     * &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewerTaskReviewItems(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        ApiResponse<Response> resp = getReviewerTaskReviewItemsWithHttpInfo(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, indexFrom, size, showCt);
        return resp.getData();
    }

    /**
     * Get review items for reviewer and review instance.
     * &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewerTaskReviewItemsWithHttpInfo(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getReviewerTaskReviewItemsValidateBeforeCall(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, indexFrom, size, showCt, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get review items for reviewer and review instance. (asynchronously)
     * &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskReviewItemsAsync(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, Integer indexFrom, Integer size, Boolean showCt, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewerTaskReviewItemsValidateBeforeCall(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, indexFrom, size, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewerTaskReviewItemsGroup
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param groupBy group by application (appName) or user (userName) or itemType (optional, default to appName)
     * @param q query value (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count, only applicable for user attribute, default is true (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskReviewItemsGroupCall(Long userId, Long revInstId, String groupBy, String q, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{userId}/tasks/{revInstId}/group"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "revInstId" + "\\}", apiClient.escapeString(revInstId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (groupBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("groupBy", groupBy));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));

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
    private com.squareup.okhttp.Call getReviewerTaskReviewItemsGroupValidateBeforeCall(Long userId, Long revInstId, String groupBy, String q, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getReviewerTaskReviewItemsGroup(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling getReviewerTaskReviewItemsGroup(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewerTaskReviewItemsGroupCall(userId, revInstId, groupBy, q, indexFrom, size, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get grouping of tasks be either user, application or item type
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param groupBy group by application (appName) or user (userName) or itemType (optional, default to appName)
     * @param q query value (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count, only applicable for user attribute, default is true (optional, default to true)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewerTaskReviewItemsGroup(Long userId, Long revInstId, String groupBy, String q, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        ApiResponse<Response> resp = getReviewerTaskReviewItemsGroupWithHttpInfo(userId, revInstId, groupBy, q, indexFrom, size, showCt);
        return resp.getData();
    }

    /**
     * Get grouping of tasks be either user, application or item type
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param groupBy group by application (appName) or user (userName) or itemType (optional, default to appName)
     * @param q query value (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count, only applicable for user attribute, default is true (optional, default to true)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewerTaskReviewItemsGroupWithHttpInfo(Long userId, Long revInstId, String groupBy, String q, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getReviewerTaskReviewItemsGroupValidateBeforeCall(userId, revInstId, groupBy, q, indexFrom, size, showCt, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get grouping of tasks be either user, application or item type (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId reviewer id (required)
     * @param revInstId review instance id (required)
     * @param groupBy group by application (appName) or user (userName) or itemType (optional, default to appName)
     * @param q query value (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count, only applicable for user attribute, default is true (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewerTaskReviewItemsGroupAsync(Long userId, Long revInstId, String groupBy, String q, Integer indexFrom, Integer size, Boolean showCt, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewerTaskReviewItemsGroupValidateBeforeCall(userId, revInstId, groupBy, q, indexFrom, size, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startPostReviewerTaskDownload
     * @param body The download request node (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly, (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startPostReviewerTaskDownloadCall(Download body, Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{userId}/tasks/{revInstId}/download"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "revInstId" + "\\}", apiClient.escapeString(revInstId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (qCol != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "qCol", qCol));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (inProgressOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgressOnly", inProgressOnly));
        if (certifiedOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certifiedOnly", certifiedOnly));
        if (pendingOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pendingOnly", pendingOnly));

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
    private com.squareup.okhttp.Call startPostReviewerTaskDownloadValidateBeforeCall(Download body, Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startPostReviewerTaskDownload(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling startPostReviewerTaskDownload(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling startPostReviewerTaskDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startPostReviewerTaskDownloadCall(body, userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start post reviewer task download
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body The download request node (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly, (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startPostReviewerTaskDownload(Download body, Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly) throws ApiException {
        ApiResponse<Response> resp = startPostReviewerTaskDownloadWithHttpInfo(body, userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly);
        return resp.getData();
    }

    /**
     * Start post reviewer task download
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body The download request node (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly, (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startPostReviewerTaskDownloadWithHttpInfo(Download body, Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly) throws ApiException {
        com.squareup.okhttp.Call call = startPostReviewerTaskDownloadValidateBeforeCall(body, userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start post reviewer task download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body The download request node (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly, (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startPostReviewerTaskDownloadAsync(Download body, Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startPostReviewerTaskDownloadValidateBeforeCall(body, userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startReviewerTaskDownload
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startReviewerTaskDownloadCall(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{userId}/tasks/{revInstId}/download"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "revInstId" + "\\}", apiClient.escapeString(revInstId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (qCol != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "qCol", qCol));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (inProgressOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgressOnly", inProgressOnly));
        if (certifiedOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certifiedOnly", certifiedOnly));
        if (pendingOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pendingOnly", pendingOnly));

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
    private com.squareup.okhttp.Call startReviewerTaskDownloadValidateBeforeCall(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling startReviewerTaskDownload(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling startReviewerTaskDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startReviewerTaskDownloadCall(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start reviewer task download
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startReviewerTaskDownload(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly) throws ApiException {
        ApiResponse<Response> resp = startReviewerTaskDownloadWithHttpInfo(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly);
        return resp.getData();
    }

    /**
     * Start reviewer task download
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startReviewerTaskDownloadWithHttpInfo(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly) throws ApiException {
        com.squareup.okhttp.Call call = startReviewerTaskDownloadValidateBeforeCall(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start reviewer task download (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param qCol query columns.  Refer to ReviewItemSortFilterType enum (optional)
     * @param q filter by value (optional)
     * @param qMatch EXACT &#x3D; search on exact q&#x3D; value, NULL&#x3D;search for null qCol value, otherwise use contains (optional)
     * @param inProgressOnly only show items that are in progress, do not use with certifiedOnly (optional)
     * @param certifiedOnly only show items that are certified, do not use with inProgressOnly (optional)
     * @param pendingOnly only show items that are pending, do not use with inProgressOnly or certifiedOnly (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startReviewerTaskDownloadAsync(Long userId, Long revInstId, String sortBy, String sortOrder, List<String> qCol, String q, String qMatch, Boolean inProgressOnly, Boolean certifiedOnly, Boolean pendingOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startReviewerTaskDownloadValidateBeforeCall(userId, revInstId, sortBy, sortOrder, qCol, q, qMatch, inProgressOnly, certifiedOnly, pendingOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for statusDownload
     * @param id - Unique download id assigned then download was started (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call statusDownloadCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/download/{id}/status"
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
    private com.squareup.okhttp.Call statusDownloadValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling statusDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = statusDownloadCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Check the status of a download
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response statusDownload(String id) throws ApiException {
        ApiResponse<Response> resp = statusDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Check the status of a download
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> statusDownloadWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = statusDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Check the status of a download (asynchronously)
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id - Unique download id assigned then download was started (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call statusDownloadAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = statusDownloadValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateItemsCertifyReviewerTasks
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateItemsCertifyReviewerTasksCall(CertifyItems body, Long userId, Long revInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/reviewers/{userId}/tasks/{revInstId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
            .replaceAll("\\{" + "revInstId" + "\\}", apiClient.escapeString(revInstId.toString()));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updateItemsCertifyReviewerTasksValidateBeforeCall(CertifyItems body, Long userId, Long revInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateItemsCertifyReviewerTasks(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling updateItemsCertifyReviewerTasks(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling updateItemsCertifyReviewerTasks(Async)");
        }
        
        com.squareup.okhttp.Call call = updateItemsCertifyReviewerTasksCall(body, userId, revInstId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Used to declare (i.e.
     * keep, remove, assign user) and certify review items.  Usage: When you are submitting decisions in bulk (e.g. KEEP or REMOVE) for multiple items, use the array actionIds.  e.g. {\&quot;items\&quot;:{\&quot;action\&quot;:\&quot;REMOVE\&quot;,\&quot;comment\&quot;:\&quot;not used\&quot;,\&quot;actionIds\&quot;:[418,454]}}  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;&lt;br/&gt;Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateItemsCertifyReviewerTasks(CertifyItems body, Long userId, Long revInstId) throws ApiException {
        ApiResponse<Response> resp = updateItemsCertifyReviewerTasksWithHttpInfo(body, userId, revInstId);
        return resp.getData();
    }

    /**
     * Used to declare (i.e.
     * keep, remove, assign user) and certify review items.  Usage: When you are submitting decisions in bulk (e.g. KEEP or REMOVE) for multiple items, use the array actionIds.  e.g. {\&quot;items\&quot;:{\&quot;action\&quot;:\&quot;REMOVE\&quot;,\&quot;comment\&quot;:\&quot;not used\&quot;,\&quot;actionIds\&quot;:[418,454]}}  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;&lt;br/&gt;Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateItemsCertifyReviewerTasksWithHttpInfo(CertifyItems body, Long userId, Long revInstId) throws ApiException {
        com.squareup.okhttp.Call call = updateItemsCertifyReviewerTasksValidateBeforeCall(body, userId, revInstId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Used to declare (i.e. (asynchronously)
     * keep, remove, assign user) and certify review items.  Usage: When you are submitting decisions in bulk (e.g. KEEP or REMOVE) for multiple items, use the array actionIds.  e.g. {\&quot;items\&quot;:{\&quot;action\&quot;:\&quot;REMOVE\&quot;,\&quot;comment\&quot;:\&quot;not used\&quot;,\&quot;actionIds\&quot;:[418,454]}}  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;&lt;br/&gt;Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateItemsCertifyReviewerTasksAsync(CertifyItems body, Long userId, Long revInstId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateItemsCertifyReviewerTasksValidateBeforeCall(body, userId, revInstId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
