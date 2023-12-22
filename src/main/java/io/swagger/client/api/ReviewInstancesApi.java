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


import io.swagger.client.model.CertifyItems;
import io.swagger.client.model.Download;
import io.swagger.client.model.Response;
import io.swagger.client.model.ReviewInst;
import io.swagger.client.model.Reviewassignment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewInstancesApi {
    private ApiClient apiClient;

    public ReviewInstancesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ReviewInstancesApi(ApiClient apiClient) {
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
        String localVarPath = "/review/instances/items/download/{id}"
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
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
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
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
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
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
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
        String localVarPath = "/review/instances/items/download/{id}"
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
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
     * Build call for getReviewInstance
     * @param id unqiue id of review instance (required)
     * @param showReviewerInfo Show reviewer counts and review settings such required comments, given that the logged-in user is the reviewer (optional, default to false)
     * @param manageMode returns settings to determine whether the logged-in user can edit or stop review instance.         Also returns review owner and auditor if one is defined for the review (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceCall(Long id, Boolean showReviewerInfo, Boolean manageMode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (showReviewerInfo != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showReviewerInfo", showReviewerInfo));
        if (manageMode != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("manageMode", manageMode));

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
    private com.squareup.okhttp.Call getReviewInstanceValidateBeforeCall(Long id, Boolean showReviewerInfo, Boolean manageMode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstance(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceCall(id, showReviewerInfo, manageMode, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get specified review instance
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id unqiue id of review instance (required)
     * @param showReviewerInfo Show reviewer counts and review settings such required comments, given that the logged-in user is the reviewer (optional, default to false)
     * @param manageMode returns settings to determine whether the logged-in user can edit or stop review instance.         Also returns review owner and auditor if one is defined for the review (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstance(Long id, Boolean showReviewerInfo, Boolean manageMode) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceWithHttpInfo(id, showReviewerInfo, manageMode);
        return resp.getData();
    }

    /**
     * Get specified review instance
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id unqiue id of review instance (required)
     * @param showReviewerInfo Show reviewer counts and review settings such required comments, given that the logged-in user is the reviewer (optional, default to false)
     * @param manageMode returns settings to determine whether the logged-in user can edit or stop review instance.         Also returns review owner and auditor if one is defined for the review (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceWithHttpInfo(Long id, Boolean showReviewerInfo, Boolean manageMode) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceValidateBeforeCall(id, showReviewerInfo, manageMode, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get specified review instance (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id unqiue id of review instance (required)
     * @param showReviewerInfo Show reviewer counts and review settings such required comments, given that the logged-in user is the reviewer (optional, default to false)
     * @param manageMode returns settings to determine whether the logged-in user can edit or stop review instance.         Also returns review owner and auditor if one is defined for the review (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceAsync(Long id, Boolean showReviewerInfo, Boolean manageMode, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceValidateBeforeCall(id, showReviewerInfo, manageMode, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceHistory
     * @param uniqueId review unique instance id (required)
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeMicroCertsOnly only show active micro-certification reviews (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceHistoryCall(String uniqueId, String sortBy, String sortOrder, Boolean activeMicroCertsOnly, Long startDateQuery, Long endDateQuery, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{uniqueId}/history"
            .replaceAll("\\{" + "uniqueId" + "\\}", apiClient.escapeString(uniqueId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (activeMicroCertsOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("activeMicroCertsOnly", activeMicroCertsOnly));
        if (startDateQuery != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startDateQuery", startDateQuery));
        if (endDateQuery != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("endDateQuery", endDateQuery));

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
    private com.squareup.okhttp.Call getReviewInstanceHistoryValidateBeforeCall(String uniqueId, String sortBy, String sortOrder, Boolean activeMicroCertsOnly, Long startDateQuery, Long endDateQuery, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueId' is set
        if (uniqueId == null) {
            throw new ApiException("Missing the required parameter 'uniqueId' when calling getReviewInstanceHistory(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceHistoryCall(uniqueId, sortBy, sortOrder, activeMicroCertsOnly, startDateQuery, endDateQuery, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the review instance history for a review by unique review id.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param uniqueId review unique instance id (required)
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeMicroCertsOnly only show active micro-certification reviews (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceHistory(String uniqueId, String sortBy, String sortOrder, Boolean activeMicroCertsOnly, Long startDateQuery, Long endDateQuery) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceHistoryWithHttpInfo(uniqueId, sortBy, sortOrder, activeMicroCertsOnly, startDateQuery, endDateQuery);
        return resp.getData();
    }

    /**
     * Get the review instance history for a review by unique review id.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param uniqueId review unique instance id (required)
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeMicroCertsOnly only show active micro-certification reviews (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceHistoryWithHttpInfo(String uniqueId, String sortBy, String sortOrder, Boolean activeMicroCertsOnly, Long startDateQuery, Long endDateQuery) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceHistoryValidateBeforeCall(uniqueId, sortBy, sortOrder, activeMicroCertsOnly, startDateQuery, endDateQuery, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the review instance history for a review by unique review id. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param uniqueId review unique instance id (required)
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeMicroCertsOnly only show active micro-certification reviews (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceHistoryAsync(String uniqueId, String sortBy, String sortOrder, Boolean activeMicroCertsOnly, Long startDateQuery, Long endDateQuery, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceHistoryValidateBeforeCall(uniqueId, sortBy, sortOrder, activeMicroCertsOnly, startDateQuery, endDateQuery, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceItems
     * @param id review instance id (required)
     * @param showWf show tasks approval (optional)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param q Value to filter by (optional)
     * @param qCol Attributes to filter on (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceItemsCall(Long id, Boolean showWf, String sortBy, String sortOrder, String q, List<String> qCol, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (showWf != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showWf", showWf));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qCol != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "qCol", qCol));
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
    private com.squareup.okhttp.Call getReviewInstanceItemsValidateBeforeCall(Long id, Boolean showWf, String sortBy, String sortOrder, String q, List<String> qCol, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstanceItems(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceItemsCall(id, showWf, sortBy, sortOrder, q, qCol, indexFrom, size, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get review items for specified review instance   &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @param showWf show tasks approval (optional)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param q Value to filter by (optional)
     * @param qCol Attributes to filter on (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceItems(Long id, Boolean showWf, String sortBy, String sortOrder, String q, List<String> qCol, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceItemsWithHttpInfo(id, showWf, sortBy, sortOrder, q, qCol, indexFrom, size, showCt);
        return resp.getData();
    }

    /**
     * Get review items for specified review instance   &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @param showWf show tasks approval (optional)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param q Value to filter by (optional)
     * @param qCol Attributes to filter on (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceItemsWithHttpInfo(Long id, Boolean showWf, String sortBy, String sortOrder, String q, List<String> qCol, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceItemsValidateBeforeCall(id, showWf, sortBy, sortOrder, q, qCol, indexFrom, size, showCt, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get review items for specified review instance   &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt; (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @param showWf show tasks approval (optional)
     * @param sortBy Sort items by allowable values: userName, appName, action or permName (optional, default to userName)
     * @param sortOrder Sort order (ASC or DESC) (optional, default to ASC)
     * @param q Value to filter by (optional)
     * @param qCol Attributes to filter on (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param showCt show total result set count (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceItemsAsync(Long id, Boolean showWf, String sortBy, String sortOrder, String q, List<String> qCol, Integer indexFrom, Integer size, Boolean showCt, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceItemsValidateBeforeCall(id, showWf, sortBy, sortOrder, q, qCol, indexFrom, size, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceItemsHistory
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceItemsHistoryCall(Long id, Long itemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items/{itemId}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

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
    private com.squareup.okhttp.Call getReviewInstanceItemsHistoryValidateBeforeCall(Long id, Long itemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstanceItemsHistory(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getReviewInstanceItemsHistory(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceItemsHistoryCall(id, itemId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get activity for a review instance items.
     * This will be comments and decisions&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceItemsHistory(Long id, Long itemId) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceItemsHistoryWithHttpInfo(id, itemId);
        return resp.getData();
    }

    /**
     * Get activity for a review instance items.
     * This will be comments and decisions&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceItemsHistoryWithHttpInfo(Long id, Long itemId) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceItemsHistoryValidateBeforeCall(id, itemId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get activity for a review instance items. (asynchronously)
     * This will be comments and decisions&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceItemsHistoryAsync(Long id, Long itemId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceItemsHistoryValidateBeforeCall(id, itemId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceItems_0
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceItems_0Call(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/wftasks"
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
    private com.squareup.okhttp.Call getReviewInstanceItems_0ValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstanceItems_0(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceItems_0Call(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get workflow tasks for review instance
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceItems_0(Long id) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceItems_0WithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get workflow tasks for review instance
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceItems_0WithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceItems_0ValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get workflow tasks for review instance (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceItems_0Async(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceItems_0ValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceNotifications
     * @param id The review instance ID (required)
     * @param sortBy sort by column (allowable values are &#x27;recipientDisplayName&#x27; or &#x27;notificationName&#x27;) (optional, default to recipientDisplayName)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceNotificationsCall(Long id, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/notifications"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getReviewInstanceNotificationsValidateBeforeCall(Long id, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstanceNotifications(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceNotificationsCall(id, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Routine to retrieve the notifications that will be generated for a given review instance.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id The review instance ID (required)
     * @param sortBy sort by column (allowable values are &#x27;recipientDisplayName&#x27; or &#x27;notificationName&#x27;) (optional, default to recipientDisplayName)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceNotifications(Long id, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceNotificationsWithHttpInfo(id, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Routine to retrieve the notifications that will be generated for a given review instance.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id The review instance ID (required)
     * @param sortBy sort by column (allowable values are &#x27;recipientDisplayName&#x27; or &#x27;notificationName&#x27;) (optional, default to recipientDisplayName)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceNotificationsWithHttpInfo(Long id, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceNotificationsValidateBeforeCall(id, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Routine to retrieve the notifications that will be generated for a given review instance. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id The review instance ID (required)
     * @param sortBy sort by column (allowable values are &#x27;recipientDisplayName&#x27; or &#x27;notificationName&#x27;) (optional, default to recipientDisplayName)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceNotificationsAsync(Long id, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceNotificationsValidateBeforeCall(id, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceStatus
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceStatusCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/status"
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
    private com.squareup.okhttp.Call getReviewInstanceStatusValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstanceStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceStatusCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get review status
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceStatus(Long id) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceStatusWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get review status
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceStatusWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceStatusValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get review status (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceStatusAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceStatusValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewItemAccountUsers
     * @param id review instance id (required)
     * @param itemId review item id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewItemAccountUsersCall(Long id, Long itemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items/{itemId}/sharedusers"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

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
    private com.squareup.okhttp.Call getReviewItemAccountUsersValidateBeforeCall(Long id, Long itemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewItemAccountUsers(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getReviewItemAccountUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewItemAccountUsersCall(id, itemId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get list of shared account users
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewItemAccountUsers(Long id, Long itemId) throws ApiException {
        ApiResponse<Response> resp = getReviewItemAccountUsersWithHttpInfo(id, itemId);
        return resp.getData();
    }

    /**
     * Get list of shared account users
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewItemAccountUsersWithHttpInfo(Long id, Long itemId) throws ApiException {
        com.squareup.okhttp.Call call = getReviewItemAccountUsersValidateBeforeCall(id, itemId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get list of shared account users (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewItemAccountUsersAsync(Long id, Long itemId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewItemAccountUsersValidateBeforeCall(id, itemId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewItemAttributes
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewItemAttributesCall(Long id, Long itemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items/{itemId}/attributes"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

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
    private com.squareup.okhttp.Call getReviewItemAttributesValidateBeforeCall(Long id, Long itemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewItemAttributes(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getReviewItemAttributes(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewItemAttributesCall(id, itemId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get attribute values for review item.
     * Applicable in user profile, direct report reviews, business role and technical role definition review&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewItemAttributes(Long id, Long itemId) throws ApiException {
        ApiResponse<Response> resp = getReviewItemAttributesWithHttpInfo(id, itemId);
        return resp.getData();
    }

    /**
     * Get attribute values for review item.
     * Applicable in user profile, direct report reviews, business role and technical role definition review&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewItemAttributesWithHttpInfo(Long id, Long itemId) throws ApiException {
        com.squareup.okhttp.Call call = getReviewItemAttributesValidateBeforeCall(id, itemId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get attribute values for review item. (asynchronously)
     * Applicable in user profile, direct report reviews, business role and technical role definition review&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewItemAttributesAsync(Long id, Long itemId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewItemAttributesValidateBeforeCall(id, itemId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewItemEntities
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param type item type (optional)
     * @param sortBy Sort items by allowable values: permName, appName or risk, default is permName (optional, default to name)
     * @param sortOrder Sort order (ASC or DESC), default is ASC (optional, default to ASC)
     * @param indexFrom result set start position, default is zero (optional, default to 0)
     * @param size size to return in result set, default is return all (optional)
     * @param showCt Show count of available entities (requires additional query), default is false (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewItemEntitiesCall(Long id, Long itemId, String type, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items/{itemId}/entities"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));
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
    private com.squareup.okhttp.Call getReviewItemEntitiesValidateBeforeCall(Long id, Long itemId, String type, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewItemEntities(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getReviewItemEntities(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewItemEntitiesCall(id, itemId, type, sortBy, sortOrder, indexFrom, size, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get child entities for review item.
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param type item type (optional)
     * @param sortBy Sort items by allowable values: permName, appName or risk, default is permName (optional, default to name)
     * @param sortOrder Sort order (ASC or DESC), default is ASC (optional, default to ASC)
     * @param indexFrom result set start position, default is zero (optional, default to 0)
     * @param size size to return in result set, default is return all (optional)
     * @param showCt Show count of available entities (requires additional query), default is false (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewItemEntities(Long id, Long itemId, String type, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        ApiResponse<Response> resp = getReviewItemEntitiesWithHttpInfo(id, itemId, type, sortBy, sortOrder, indexFrom, size, showCt);
        return resp.getData();
    }

    /**
     * Get child entities for review item.
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param type item type (optional)
     * @param sortBy Sort items by allowable values: permName, appName or risk, default is permName (optional, default to name)
     * @param sortOrder Sort order (ASC or DESC), default is ASC (optional, default to ASC)
     * @param indexFrom result set start position, default is zero (optional, default to 0)
     * @param size size to return in result set, default is return all (optional)
     * @param showCt Show count of available entities (requires additional query), default is false (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewItemEntitiesWithHttpInfo(Long id, Long itemId, String type, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getReviewItemEntitiesValidateBeforeCall(id, itemId, type, sortBy, sortOrder, indexFrom, size, showCt, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get child entities for review item. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param itemId review item action id (required)
     * @param type item type (optional)
     * @param sortBy Sort items by allowable values: permName, appName or risk, default is permName (optional, default to name)
     * @param sortOrder Sort order (ASC or DESC), default is ASC (optional, default to ASC)
     * @param indexFrom result set start position, default is zero (optional, default to 0)
     * @param size size to return in result set, default is return all (optional)
     * @param showCt Show count of available entities (requires additional query), default is false (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewItemEntitiesAsync(Long id, Long itemId, String type, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewItemEntitiesValidateBeforeCall(id, itemId, type, sortBy, sortOrder, indexFrom, size, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewItemFulfillment
     * @param id unique id of review instance (required)
     * @param itemId unique id of review item (required)
     * @param sortBy Column to sort by valid values include: permName, acctName or appName (optional, default to permName)
     * @param sortOrder sort order (ASC or DESC) (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewItemFulfillmentCall(Long id, Long itemId, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items/{itemId}/fulfillment"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));

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
    private com.squareup.okhttp.Call getReviewItemFulfillmentValidateBeforeCall(Long id, Long itemId, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewItemFulfillment(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getReviewItemFulfillment(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewItemFulfillmentCall(id, itemId, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get fulfillment item(s) for specified review item
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id unique id of review instance (required)
     * @param itemId unique id of review item (required)
     * @param sortBy Column to sort by valid values include: permName, acctName or appName (optional, default to permName)
     * @param sortOrder sort order (ASC or DESC) (optional, default to ASC)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewItemFulfillment(Long id, Long itemId, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<Response> resp = getReviewItemFulfillmentWithHttpInfo(id, itemId, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Get fulfillment item(s) for specified review item
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id unique id of review instance (required)
     * @param itemId unique id of review item (required)
     * @param sortBy Column to sort by valid values include: permName, acctName or appName (optional, default to permName)
     * @param sortOrder sort order (ASC or DESC) (optional, default to ASC)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewItemFulfillmentWithHttpInfo(Long id, Long itemId, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = getReviewItemFulfillmentValidateBeforeCall(id, itemId, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get fulfillment item(s) for specified review item (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id unique id of review instance (required)
     * @param itemId unique id of review item (required)
     * @param sortBy Column to sort by valid values include: permName, acctName or appName (optional, default to permName)
     * @param sortOrder sort order (ASC or DESC) (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewItemFulfillmentAsync(Long id, Long itemId, String sortBy, String sortOrder, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewItemFulfillmentValidateBeforeCall(id, itemId, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for materializeViewStatus
     * @param id unqiue id of review instance (required)
     * @param opCode operation code valid values are: enable, disable, refresh (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call materializeViewStatusCall(Long id, String opCode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/matview/{opCode}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "opCode" + "\\}", apiClient.escapeString(opCode.toString()));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call materializeViewStatusValidateBeforeCall(Long id, String opCode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling materializeViewStatus(Async)");
        }
        // verify the required parameter 'opCode' is set
        if (opCode == null) {
            throw new ApiException("Missing the required parameter 'opCode' when calling materializeViewStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = materializeViewStatusCall(id, opCode, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update status of materialized view used for review instance
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id unqiue id of review instance (required)
     * @param opCode operation code valid values are: enable, disable, refresh (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response materializeViewStatus(Long id, String opCode) throws ApiException {
        ApiResponse<Response> resp = materializeViewStatusWithHttpInfo(id, opCode);
        return resp.getData();
    }

    /**
     * Update status of materialized view used for review instance
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id unqiue id of review instance (required)
     * @param opCode operation code valid values are: enable, disable, refresh (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> materializeViewStatusWithHttpInfo(Long id, String opCode) throws ApiException {
        com.squareup.okhttp.Call call = materializeViewStatusValidateBeforeCall(id, opCode, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update status of materialized view used for review instance (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id unqiue id of review instance (required)
     * @param opCode operation code valid values are: enable, disable, refresh (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call materializeViewStatusAsync(Long id, String opCode, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = materializeViewStatusValidateBeforeCall(id, opCode, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for overrideReviewTasks
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call overrideReviewTasksCall(CertifyItems body, Long userId, Long revInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/instances/{revInstId}/items/override"
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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call overrideReviewTasksValidateBeforeCall(CertifyItems body, Long userId, Long revInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling overrideReviewTasks(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling overrideReviewTasks(Async)");
        }
        // verify the required parameter 'revInstId' is set
        if (revInstId == null) {
            throw new ApiException("Missing the required parameter 'revInstId' when calling overrideReviewTasks(Async)");
        }
        
        com.squareup.okhttp.Call call = overrideReviewTasksCall(body, userId, revInstId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Override items
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response overrideReviewTasks(CertifyItems body, Long userId, Long revInstId) throws ApiException {
        ApiResponse<Response> resp = overrideReviewTasksWithHttpInfo(body, userId, revInstId);
        return resp.getData();
    }

    /**
     * Override items
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> overrideReviewTasksWithHttpInfo(CertifyItems body, Long userId, Long revInstId) throws ApiException {
        com.squareup.okhttp.Call call = overrideReviewTasksValidateBeforeCall(body, userId, revInstId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Override items (asynchronously)
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body payload (required)
     * @param userId unqiue user id (required)
     * @param revInstId unique review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call overrideReviewTasksAsync(CertifyItems body, Long userId, Long revInstId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = overrideReviewTasksValidateBeforeCall(body, userId, revInstId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for reassignReviewItems
     * @param body JSON payload (required)
     * @param id unique id of review instance (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call reassignReviewItemsCall(Reviewassignment body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/assignments"
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
    private com.squareup.okhttp.Call reassignReviewItemsValidateBeforeCall(Reviewassignment body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling reassignReviewItems(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling reassignReviewItems(Async)");
        }
        
        com.squareup.okhttp.Call call = reassignReviewItemsCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Reassign review items for specified review instance
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body JSON payload (required)
     * @param id unique id of review instance (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response reassignReviewItems(Reviewassignment body, Long id) throws ApiException {
        ApiResponse<Response> resp = reassignReviewItemsWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Reassign review items for specified review instance
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body JSON payload (required)
     * @param id unique id of review instance (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> reassignReviewItemsWithHttpInfo(Reviewassignment body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = reassignReviewItemsValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Reassign review items for specified review instance (asynchronously)
     * Accepted Roles: * Customer Administrator * Escalation Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param body JSON payload (required)
     * @param id unique id of review instance (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call reassignReviewItemsAsync(Reviewassignment body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = reassignReviewItemsValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for recalculateReviewProgressCounts
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call recalculateReviewProgressCountsCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/progressrecalc"
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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call recalculateReviewProgressCountsValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling recalculateReviewProgressCounts(Async)");
        }
        
        com.squareup.okhttp.Call call = recalculateReviewProgressCountsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Recalculate progress counts of review
     * Accepted Roles: * Customer Administrator * Global Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response recalculateReviewProgressCounts(Long id) throws ApiException {
        ApiResponse<Response> resp = recalculateReviewProgressCountsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Recalculate progress counts of review
     * Accepted Roles: * Customer Administrator * Global Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> recalculateReviewProgressCountsWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = recalculateReviewProgressCountsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Recalculate progress counts of review (asynchronously)
     * Accepted Roles: * Customer Administrator * Global Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call recalculateReviewProgressCountsAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = recalculateReviewProgressCountsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for sendPreviewNotification
     * @param id The review instance identifier. (required)
     * @param notifyInstId The notification instance identifier. (required)
     * @param addresseeId The notification addressee instance identifier. (required)
     * @param uuid The unique user id of the particular addressee instance that would normally the notification. (required)
     * @param dest The destination email to send the preview. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call sendPreviewNotificationCall(Long id, Long notifyInstId, Long addresseeId, String uuid, String dest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/notifications/{notifyInstId}/{addresseeId}/{uuid}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "notifyInstId" + "\\}", apiClient.escapeString(notifyInstId.toString()))
            .replaceAll("\\{" + "addresseeId" + "\\}", apiClient.escapeString(addresseeId.toString()))
            .replaceAll("\\{" + "uuid" + "\\}", apiClient.escapeString(uuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (dest != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dest", dest));

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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call sendPreviewNotificationValidateBeforeCall(Long id, Long notifyInstId, Long addresseeId, String uuid, String dest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling sendPreviewNotification(Async)");
        }
        // verify the required parameter 'notifyInstId' is set
        if (notifyInstId == null) {
            throw new ApiException("Missing the required parameter 'notifyInstId' when calling sendPreviewNotification(Async)");
        }
        // verify the required parameter 'addresseeId' is set
        if (addresseeId == null) {
            throw new ApiException("Missing the required parameter 'addresseeId' when calling sendPreviewNotification(Async)");
        }
        // verify the required parameter 'uuid' is set
        if (uuid == null) {
            throw new ApiException("Missing the required parameter 'uuid' when calling sendPreviewNotification(Async)");
        }
        
        com.squareup.okhttp.Call call = sendPreviewNotificationCall(id, notifyInstId, addresseeId, uuid, dest, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Send a preview email for the specified addressee, notification instance and review instance combination.
     * The supplied query parameters  will include the email address details to use for the preview email, and the unique user id of the intended recipient.    The intended recipient is the user that has been identified to receive the notification if the review went live. This detail is used   so that the content of the notification will be setup for that user, even though the preview will be sent to the supplied destination   email address.&lt;br/&gt;Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id The review instance identifier. (required)
     * @param notifyInstId The notification instance identifier. (required)
     * @param addresseeId The notification addressee instance identifier. (required)
     * @param uuid The unique user id of the particular addressee instance that would normally the notification. (required)
     * @param dest The destination email to send the preview. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response sendPreviewNotification(Long id, Long notifyInstId, Long addresseeId, String uuid, String dest) throws ApiException {
        ApiResponse<Response> resp = sendPreviewNotificationWithHttpInfo(id, notifyInstId, addresseeId, uuid, dest);
        return resp.getData();
    }

    /**
     * Send a preview email for the specified addressee, notification instance and review instance combination.
     * The supplied query parameters  will include the email address details to use for the preview email, and the unique user id of the intended recipient.    The intended recipient is the user that has been identified to receive the notification if the review went live. This detail is used   so that the content of the notification will be setup for that user, even though the preview will be sent to the supplied destination   email address.&lt;br/&gt;Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id The review instance identifier. (required)
     * @param notifyInstId The notification instance identifier. (required)
     * @param addresseeId The notification addressee instance identifier. (required)
     * @param uuid The unique user id of the particular addressee instance that would normally the notification. (required)
     * @param dest The destination email to send the preview. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> sendPreviewNotificationWithHttpInfo(Long id, Long notifyInstId, Long addresseeId, String uuid, String dest) throws ApiException {
        com.squareup.okhttp.Call call = sendPreviewNotificationValidateBeforeCall(id, notifyInstId, addresseeId, uuid, dest, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Send a preview email for the specified addressee, notification instance and review instance combination. (asynchronously)
     * The supplied query parameters  will include the email address details to use for the preview email, and the unique user id of the intended recipient.    The intended recipient is the user that has been identified to receive the notification if the review went live. This detail is used   so that the content of the notification will be setup for that user, even though the preview will be sent to the supplied destination   email address.&lt;br/&gt;Accepted Roles: * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param id The review instance identifier. (required)
     * @param notifyInstId The notification instance identifier. (required)
     * @param addresseeId The notification addressee instance identifier. (required)
     * @param uuid The unique user id of the particular addressee instance that would normally the notification. (required)
     * @param dest The destination email to send the preview. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call sendPreviewNotificationAsync(Long id, Long notifyInstId, Long addresseeId, String uuid, String dest, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = sendPreviewNotificationValidateBeforeCall(id, notifyInstId, addresseeId, uuid, dest, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startReviewItemsDownload
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startReviewItemsDownloadCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items/download"
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
    private com.squareup.okhttp.Call startReviewItemsDownloadValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling startReviewItemsDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startReviewItemsDownloadCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start review item download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startReviewItemsDownload(Long id) throws ApiException {
        ApiResponse<Response> resp = startReviewItemsDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Start review item download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startReviewItemsDownloadWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = startReviewItemsDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start review item download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startReviewItemsDownloadAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startReviewItemsDownloadValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startReviewItemsDownload_0
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startReviewItemsDownload_0Call(Download body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/items/download"
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
    private com.squareup.okhttp.Call startReviewItemsDownload_0ValidateBeforeCall(Download body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startReviewItemsDownload_0(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling startReviewItemsDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startReviewItemsDownload_0Call(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start review item download
     * Accepted Roles: * Auditor * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startReviewItemsDownload_0(Download body, Long id) throws ApiException {
        ApiResponse<Response> resp = startReviewItemsDownload_0WithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Start review item download
     * Accepted Roles: * Auditor * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startReviewItemsDownload_0WithHttpInfo(Download body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = startReviewItemsDownload_0ValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start review item download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Review Administrator * Review Auditor * Review Owner 
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startReviewItemsDownload_0Async(Download body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startReviewItemsDownload_0ValidateBeforeCall(body, id, progressListener, progressRequestListener);
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
        String localVarPath = "/review/instances/items/download/{id}/status"
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
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
     * Build call for updateReviewInstance
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateReviewInstanceCall(ReviewInst body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}"
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
    private com.squareup.okhttp.Call updateReviewInstanceValidateBeforeCall(ReviewInst body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateReviewInstance(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateReviewInstance(Async)");
        }
        
        com.squareup.okhttp.Call call = updateReviewInstanceCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update expected end-date or review owner on running review instance
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateReviewInstance(ReviewInst body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateReviewInstanceWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update expected end-date or review owner on running review instance
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateReviewInstanceWithHttpInfo(ReviewInst body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateReviewInstanceValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update expected end-date or review owner on running review instance (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateReviewInstanceAsync(ReviewInst body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateReviewInstanceValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateReviewInstanceStatus
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateReviewInstanceStatusCall(ReviewInst body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/instances/{id}/status"
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
    private com.squareup.okhttp.Call updateReviewInstanceStatusValidateBeforeCall(ReviewInst body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateReviewInstanceStatus(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateReviewInstanceStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = updateReviewInstanceStatusCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update review instance status   &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateReviewInstanceStatus(ReviewInst body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateReviewInstanceStatusWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update review instance status   &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateReviewInstanceStatusWithHttpInfo(ReviewInst body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateReviewInstanceStatusValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update review instance status   &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt; (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body instance node (required)
     * @param id unqiue id of review instance (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateReviewInstanceStatusAsync(ReviewInst body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateReviewInstanceStatusValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
