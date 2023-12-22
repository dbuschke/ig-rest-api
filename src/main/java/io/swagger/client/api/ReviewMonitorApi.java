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


import io.swagger.client.model.Download;
import io.swagger.client.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewMonitorApi {
    private ApiClient apiClient;

    public ReviewMonitorApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ReviewMonitorApi(ApiClient apiClient) {
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
        String localVarPath = "/review/monitor/download/{id}"
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
     * Accepted Roles: * Customer Administrator * Review Administrator 
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
     * Accepted Roles: * Customer Administrator * Review Administrator 
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
     * Accepted Roles: * Customer Administrator * Review Administrator 
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
        String localVarPath = "/review/monitor/download/{id}"
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
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
     * Build call for getReviewInstanceActiveCount
     * @param hasTasks pass in true if you only want to count reviews you have tasks in, false otherwise (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceActiveCountCall(Boolean hasTasks, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/monitor/activeCount";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (hasTasks != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("hasTasks", hasTasks));

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
    private com.squareup.okhttp.Call getReviewInstanceActiveCountValidateBeforeCall(Boolean hasTasks, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getReviewInstanceActiveCountCall(hasTasks, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the active review count the user has access to
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param hasTasks pass in true if you only want to count reviews you have tasks in, false otherwise (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceActiveCount(Boolean hasTasks) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceActiveCountWithHttpInfo(hasTasks);
        return resp.getData();
    }

    /**
     * Get the active review count the user has access to
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param hasTasks pass in true if you only want to count reviews you have tasks in, false otherwise (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceActiveCountWithHttpInfo(Boolean hasTasks) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceActiveCountValidateBeforeCall(hasTasks, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the active review count the user has access to (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param hasTasks pass in true if you only want to count reviews you have tasks in, false otherwise (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceActiveCountAsync(Boolean hasTasks, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceActiveCountValidateBeforeCall(hasTasks, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceReviwer
     * @param id the review instanceId (required)
     * @param reviewerSortOrderFilter the reviewer sort order filter (required)
     * @param sortBy sort by column (optional, default to dueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param q a searchString for the reviewer name, leave null if no string (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceReviwerCall(Long id, Integer reviewerSortOrderFilter, String sortBy, String sortOrder, String q, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/monitor/{id}/reviewerSortOrder/{reviewerSortOrderFilter}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "reviewerSortOrderFilter" + "\\}", apiClient.escapeString(reviewerSortOrderFilter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
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
    private com.squareup.okhttp.Call getReviewInstanceReviwerValidateBeforeCall(Long id, Integer reviewerSortOrderFilter, String sortBy, String sortOrder, String q, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstanceReviwer(Async)");
        }
        // verify the required parameter 'reviewerSortOrderFilter' is set
        if (reviewerSortOrderFilter == null) {
            throw new ApiException("Missing the required parameter 'reviewerSortOrderFilter' when calling getReviewInstanceReviwer(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceReviwerCall(id, reviewerSortOrderFilter, sortBy, sortOrder, q, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets the line items for a reviewer group.
     * One passes in the reviewer sort  order (which can be found at /api/v15/review/monitor/{id}/reviewerGroups  - @see getReviewInstanceReviwerGroups) For example, if the review has a  supervisor and permission holder review, calling this with a 0 will bring  back the supervisor line items, calling with a 1 will bring back the  permission holder line items, and passing in a 990 will bring back the  review owner line items, if any.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id the review instanceId (required)
     * @param reviewerSortOrderFilter the reviewer sort order filter (required)
     * @param sortBy sort by column (optional, default to dueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param q a searchString for the reviewer name, leave null if no string (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceReviwer(Long id, Integer reviewerSortOrderFilter, String sortBy, String sortOrder, String q, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceReviwerWithHttpInfo(id, reviewerSortOrderFilter, sortBy, sortOrder, q, indexFrom, size);
        return resp.getData();
    }

    /**
     * Gets the line items for a reviewer group.
     * One passes in the reviewer sort  order (which can be found at /api/v15/review/monitor/{id}/reviewerGroups  - @see getReviewInstanceReviwerGroups) For example, if the review has a  supervisor and permission holder review, calling this with a 0 will bring  back the supervisor line items, calling with a 1 will bring back the  permission holder line items, and passing in a 990 will bring back the  review owner line items, if any.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id the review instanceId (required)
     * @param reviewerSortOrderFilter the reviewer sort order filter (required)
     * @param sortBy sort by column (optional, default to dueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param q a searchString for the reviewer name, leave null if no string (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceReviwerWithHttpInfo(Long id, Integer reviewerSortOrderFilter, String sortBy, String sortOrder, String q, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceReviwerValidateBeforeCall(id, reviewerSortOrderFilter, sortBy, sortOrder, q, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets the line items for a reviewer group. (asynchronously)
     * One passes in the reviewer sort  order (which can be found at /api/v15/review/monitor/{id}/reviewerGroups  - @see getReviewInstanceReviwerGroups) For example, if the review has a  supervisor and permission holder review, calling this with a 0 will bring  back the supervisor line items, calling with a 1 will bring back the  permission holder line items, and passing in a 990 will bring back the  review owner line items, if any.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id the review instanceId (required)
     * @param reviewerSortOrderFilter the reviewer sort order filter (required)
     * @param sortBy sort by column (optional, default to dueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param q a searchString for the reviewer name, leave null if no string (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceReviwerAsync(Long id, Integer reviewerSortOrderFilter, String sortBy, String sortOrder, String q, Integer indexFrom, Integer size, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceReviwerValidateBeforeCall(id, reviewerSortOrderFilter, sortBy, sortOrder, q, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstanceReviwerGroups
     * @param id the review instanceId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceReviwerGroupsCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/monitor/{id}/reviewerGroups"
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
    private com.squareup.okhttp.Call getReviewInstanceReviwerGroupsValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewInstanceReviwerGroups(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewInstanceReviwerGroupsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the unique reviewer groups for a particular review instance.
     * For   example, for a review that does a supervisor first then a permission  holder second, it would bring back those two in that order. In addition, it  will bring back a node for the review owner, for a total of 3. They are  indexed by the reviewer sort order, starting from 0. The review owner step  gets a special sort order of 990.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id the review instanceId (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstanceReviwerGroups(Long id) throws ApiException {
        ApiResponse<Response> resp = getReviewInstanceReviwerGroupsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the unique reviewer groups for a particular review instance.
     * For   example, for a review that does a supervisor first then a permission  holder second, it would bring back those two in that order. In addition, it  will bring back a node for the review owner, for a total of 3. They are  indexed by the reviewer sort order, starting from 0. The review owner step  gets a special sort order of 990.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id the review instanceId (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstanceReviwerGroupsWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstanceReviwerGroupsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the unique reviewer groups for a particular review instance. (asynchronously)
     * For   example, for a review that does a supervisor first then a permission  holder second, it would bring back those two in that order. In addition, it  will bring back a node for the review owner, for a total of 3. They are  indexed by the reviewer sort order, starting from 0. The review owner step  gets a special sort order of 990.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id the review instanceId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstanceReviwerGroupsAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstanceReviwerGroupsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewInstances
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeReviewsOnly true if only show active reviews, false if only show historical         reviews (optional, default to true)
     * @param q a searchString for the reviewname, leave null if no string (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null         if no such filter (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @param reviewStatus an array of allowable review statuses (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewInstancesCall(String sortBy, String sortOrder, Boolean activeReviewsOnly, String q, Long startDateQuery, Long endDateQuery, List<String> reviewStatus, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/monitor";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (activeReviewsOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("activeReviewsOnly", activeReviewsOnly));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (startDateQuery != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startDateQuery", startDateQuery));
        if (endDateQuery != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("endDateQuery", endDateQuery));
        if (reviewStatus != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "reviewStatus", reviewStatus));
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
    private com.squareup.okhttp.Call getReviewInstancesValidateBeforeCall(String sortBy, String sortOrder, Boolean activeReviewsOnly, String q, Long startDateQuery, Long endDateQuery, List<String> reviewStatus, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getReviewInstancesCall(sortBy, sortOrder, activeReviewsOnly, q, startDateQuery, endDateQuery, reviewStatus, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get active or historical reviews the logged in user has access to
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeReviewsOnly true if only show active reviews, false if only show historical         reviews (optional, default to true)
     * @param q a searchString for the reviewname, leave null if no string (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null         if no such filter (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @param reviewStatus an array of allowable review statuses (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewInstances(String sortBy, String sortOrder, Boolean activeReviewsOnly, String q, Long startDateQuery, Long endDateQuery, List<String> reviewStatus, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<Response> resp = getReviewInstancesWithHttpInfo(sortBy, sortOrder, activeReviewsOnly, q, startDateQuery, endDateQuery, reviewStatus, indexFrom, size);
        return resp.getData();
    }

    /**
     * Get active or historical reviews the logged in user has access to
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeReviewsOnly true if only show active reviews, false if only show historical         reviews (optional, default to true)
     * @param q a searchString for the reviewname, leave null if no string (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null         if no such filter (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @param reviewStatus an array of allowable review statuses (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewInstancesWithHttpInfo(String sortBy, String sortOrder, Boolean activeReviewsOnly, String q, Long startDateQuery, Long endDateQuery, List<String> reviewStatus, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getReviewInstancesValidateBeforeCall(sortBy, sortOrder, activeReviewsOnly, q, startDateQuery, endDateQuery, reviewStatus, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get active or historical reviews the logged in user has access to (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param sortBy sort by column (optional, default to reviewerDueDate)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param activeReviewsOnly true if only show active reviews, false if only show historical         reviews (optional, default to true)
     * @param q a searchString for the reviewname, leave null if no string (optional)
     * @param startDateQuery a date that the review must have started on or later, leave null         if no such filter (optional)
     * @param endDateQuery a date that the review must have ended before, leave null if no         such filter (optional)
     * @param reviewStatus an array of allowable review statuses (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 100)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewInstancesAsync(String sortBy, String sortOrder, Boolean activeReviewsOnly, String q, Long startDateQuery, Long endDateQuery, List<String> reviewStatus, Integer indexFrom, Integer size, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewInstancesValidateBeforeCall(sortBy, sortOrder, activeReviewsOnly, q, startDateQuery, endDateQuery, reviewStatus, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewerNearestDueDate
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewerNearestDueDateCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/monitor/{id}/due"
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
    private com.squareup.okhttp.Call getReviewerNearestDueDateValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewerNearestDueDate(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewerNearestDueDateCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the nearest due date for the logged in user, for specified review
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewerNearestDueDate(Long id) throws ApiException {
        ApiResponse<Response> resp = getReviewerNearestDueDateWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the nearest due date for the logged in user, for specified review
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewerNearestDueDateWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getReviewerNearestDueDateValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the nearest due date for the logged in user, for specified review (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer 
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewerNearestDueDateAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewerNearestDueDateValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startReviewMonitorDownload
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startReviewMonitorDownloadCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/monitor/{id}/download"
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
    private com.squareup.okhttp.Call startReviewMonitorDownloadValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling startReviewMonitorDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startReviewMonitorDownloadCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start reviewer monitor download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startReviewMonitorDownload(Long id) throws ApiException {
        ApiResponse<Response> resp = startReviewMonitorDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Start reviewer monitor download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startReviewMonitorDownloadWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = startReviewMonitorDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start reviewer monitor download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startReviewMonitorDownloadAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startReviewMonitorDownloadValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startReviewMonitorDownload_0
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startReviewMonitorDownload_0Call(Download body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/monitor/{id}/download"
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
    private com.squareup.okhttp.Call startReviewMonitorDownload_0ValidateBeforeCall(Download body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startReviewMonitorDownload_0(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling startReviewMonitorDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startReviewMonitorDownload_0Call(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start reviewer monitor download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startReviewMonitorDownload_0(Download body, Long id) throws ApiException {
        ApiResponse<Response> resp = startReviewMonitorDownload_0WithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Start reviewer monitor download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startReviewMonitorDownload_0WithHttpInfo(Download body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = startReviewMonitorDownload_0ValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start reviewer monitor download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
     * @param body -  The download request node (required)
     * @param id review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startReviewMonitorDownload_0Async(Download body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startReviewMonitorDownload_0ValidateBeforeCall(body, id, progressListener, progressRequestListener);
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
        String localVarPath = "/review/monitor/download/{id}/status"
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Owner 
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
}
