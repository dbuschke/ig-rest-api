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
import java.io.File;
import io.swagger.client.model.ModelImport;
import io.swagger.client.model.Response;
import io.swagger.client.model.ReviewDef;
import io.swagger.client.model.ReviewInst;
import io.swagger.client.model.Schema;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewDefinitionsApi {
    private ApiClient apiClient;

    public ReviewDefinitionsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ReviewDefinitionsApi(ApiClient apiClient) {
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
     * @param id - The download id to delete (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cancelDownloadCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/download/{id}"
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
     * Cancel a download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id to delete (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response cancelDownload(String id) throws ApiException {
        ApiResponse<Response> resp = cancelDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Cancel a download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id to delete (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> cancelDownloadWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = cancelDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Cancel a download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id to delete (required)
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
     * Build call for createReviewDefinition
     * @param body JSON payload (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createReviewDefinitionCall(ReviewDef body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/definitions";

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
    private com.squareup.okhttp.Call createReviewDefinitionValidateBeforeCall(ReviewDef body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createReviewDefinition(Async)");
        }
        
        com.squareup.okhttp.Call call = createReviewDefinitionCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create review definition
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body JSON payload (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response createReviewDefinition(ReviewDef body) throws ApiException {
        ApiResponse<Response> resp = createReviewDefinitionWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create review definition
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body JSON payload (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> createReviewDefinitionWithHttpInfo(ReviewDef body) throws ApiException {
        com.squareup.okhttp.Call call = createReviewDefinitionValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create review definition (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body JSON payload (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createReviewDefinitionAsync(ReviewDef body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createReviewDefinitionValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteReviewDefinition
     * @param id primary key/id of review definition (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteReviewDefinitionCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/{id}"
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
    private com.squareup.okhttp.Call deleteReviewDefinitionValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteReviewDefinition(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteReviewDefinitionCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete specified review definition
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param id primary key/id of review definition (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response deleteReviewDefinition(Long id) throws ApiException {
        ApiResponse<Response> resp = deleteReviewDefinitionWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete specified review definition
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param id primary key/id of review definition (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> deleteReviewDefinitionWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteReviewDefinitionValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete specified review definition (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param id primary key/id of review definition (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteReviewDefinitionAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteReviewDefinitionValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for finishDownload
     * @param id - The download id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call finishDownloadCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/download/{id}"
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response finishDownload(String id) throws ApiException {
        ApiResponse<Response> resp = finishDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the download file
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id (required)
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id (required)
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
     * Build call for getAllReviewDefinitions
     * @param includeSchedule true if we should bring back schedule records (optional, default to false)
     * @param sortBy the attribute to sort by (optional, default to name)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 0)
     * @param showCt true if you wish to bring back the unfiltered count (optional, default to false)
     * @param showMicroCertCt show count of active micro-certification reviews (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional, default to ANY)
     * @param reviewType (optional) review types list (optional)
     * @param reviewTargetType (optional) filtered review target types, NOTE: use only if USER_ACCESS is passed in IG3.6, list is used in query with                            \&quot;not in\&quot; operator (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAllReviewDefinitionsCall(Boolean includeSchedule, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showMicroCertCt, String q, String qMatch, List<String> reviewType, List<String> reviewTargetType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (includeSchedule != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("includeSchedule", includeSchedule));
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
        if (showMicroCertCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showMicroCertCt", showMicroCertCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (reviewType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "reviewType", reviewType));
        if (reviewTargetType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "reviewTargetType", reviewTargetType));

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
    private com.squareup.okhttp.Call getAllReviewDefinitionsValidateBeforeCall(Boolean includeSchedule, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showMicroCertCt, String q, String qMatch, List<String> reviewType, List<String> reviewTargetType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getAllReviewDefinitionsCall(includeSchedule, sortBy, sortOrder, indexFrom, size, showCt, showMicroCertCt, q, qMatch, reviewType, reviewTargetType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get all review definitions.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param includeSchedule true if we should bring back schedule records (optional, default to false)
     * @param sortBy the attribute to sort by (optional, default to name)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 0)
     * @param showCt true if you wish to bring back the unfiltered count (optional, default to false)
     * @param showMicroCertCt show count of active micro-certification reviews (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional, default to ANY)
     * @param reviewType (optional) review types list (optional)
     * @param reviewTargetType (optional) filtered review target types, NOTE: use only if USER_ACCESS is passed in IG3.6, list is used in query with                            \&quot;not in\&quot; operator (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAllReviewDefinitions(Boolean includeSchedule, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showMicroCertCt, String q, String qMatch, List<String> reviewType, List<String> reviewTargetType) throws ApiException {
        ApiResponse<Response> resp = getAllReviewDefinitionsWithHttpInfo(includeSchedule, sortBy, sortOrder, indexFrom, size, showCt, showMicroCertCt, q, qMatch, reviewType, reviewTargetType);
        return resp.getData();
    }

    /**
     * Get all review definitions.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param includeSchedule true if we should bring back schedule records (optional, default to false)
     * @param sortBy the attribute to sort by (optional, default to name)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 0)
     * @param showCt true if you wish to bring back the unfiltered count (optional, default to false)
     * @param showMicroCertCt show count of active micro-certification reviews (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional, default to ANY)
     * @param reviewType (optional) review types list (optional)
     * @param reviewTargetType (optional) filtered review target types, NOTE: use only if USER_ACCESS is passed in IG3.6, list is used in query with                            \&quot;not in\&quot; operator (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAllReviewDefinitionsWithHttpInfo(Boolean includeSchedule, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showMicroCertCt, String q, String qMatch, List<String> reviewType, List<String> reviewTargetType) throws ApiException {
        com.squareup.okhttp.Call call = getAllReviewDefinitionsValidateBeforeCall(includeSchedule, sortBy, sortOrder, indexFrom, size, showCt, showMicroCertCt, q, qMatch, reviewType, reviewTargetType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get all review definitions. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param includeSchedule true if we should bring back schedule records (optional, default to false)
     * @param sortBy the attribute to sort by (optional, default to name)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 0)
     * @param showCt true if you wish to bring back the unfiltered count (optional, default to false)
     * @param showMicroCertCt show count of active micro-certification reviews (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode (optional, default to ANY)
     * @param reviewType (optional) review types list (optional)
     * @param reviewTargetType (optional) filtered review target types, NOTE: use only if USER_ACCESS is passed in IG3.6, list is used in query with                            \&quot;not in\&quot; operator (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAllReviewDefinitionsAsync(Boolean includeSchedule, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, Boolean showMicroCertCt, String q, String qMatch, List<String> reviewType, List<String> reviewTargetType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAllReviewDefinitionsValidateBeforeCall(includeSchedule, sortBy, sortOrder, indexFrom, size, showCt, showMicroCertCt, q, qMatch, reviewType, reviewTargetType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewDefinition
     * @param id primary key/id of review definition (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewDefinitionCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/{id}"
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
    private com.squareup.okhttp.Call getReviewDefinitionValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewDefinition(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewDefinitionCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get specified review definition.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id primary key/id of review definition (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewDefinition(Long id) throws ApiException {
        ApiResponse<Response> resp = getReviewDefinitionWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get specified review definition.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id primary key/id of review definition (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewDefinitionWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getReviewDefinitionValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get specified review definition. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id primary key/id of review definition (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewDefinitionAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewDefinitionValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewDefinitionForInstance
     * @param id unique review instance id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewDefinitionForInstanceCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/instances/{id}"
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
    private com.squareup.okhttp.Call getReviewDefinitionForInstanceValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewDefinitionForInstance(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewDefinitionForInstanceCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a review definition for a particular review instance.
     * This will inject any specific definition settings that may have been set during  review staging/preview&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id unique review instance id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewDefinitionForInstance(Long id) throws ApiException {
        ApiResponse<Response> resp = getReviewDefinitionForInstanceWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get a review definition for a particular review instance.
     * This will inject any specific definition settings that may have been set during  review staging/preview&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id unique review instance id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewDefinitionForInstanceWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getReviewDefinitionForInstanceValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a review definition for a particular review instance. (asynchronously)
     * This will inject any specific definition settings that may have been set during  review staging/preview&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Historical Review Auditor * Historical Review Owner * Review Administrator * Review Auditor * Review Monitor * Review Owner 
     * @param id unique review instance id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewDefinitionForInstanceAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewDefinitionForInstanceValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewDefinitionInstances
     * @param id primary key/id of review definition (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewDefinitionInstancesCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/{id}/instances"
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
    private com.squareup.okhttp.Call getReviewDefinitionInstancesValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getReviewDefinitionInstances(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewDefinitionInstancesCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get review instances for specified review definition.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id primary key/id of review definition (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewDefinitionInstances(Long id) throws ApiException {
        ApiResponse<Response> resp = getReviewDefinitionInstancesWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get review instances for specified review definition.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id primary key/id of review definition (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewDefinitionInstancesWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getReviewDefinitionInstancesValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get review instances for specified review definition. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id primary key/id of review definition (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewDefinitionInstancesAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewDefinitionInstancesValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReviewDisplayAttributes
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReviewDisplayAttributesCall(String reviewType, String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/attributes/{reviewType}/{entityType}"
            .replaceAll("\\{" + "reviewType" + "\\}", apiClient.escapeString(reviewType.toString()))
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()));

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
    private com.squareup.okhttp.Call getReviewDisplayAttributesValidateBeforeCall(String reviewType, String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'reviewType' is set
        if (reviewType == null) {
            throw new ApiException("Missing the required parameter 'reviewType' when calling getReviewDisplayAttributes(Async)");
        }
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getReviewDisplayAttributes(Async)");
        }
        
        com.squareup.okhttp.Call call = getReviewDisplayAttributesCall(reviewType, entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get custom display attributes for reviewer item
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getReviewDisplayAttributes(String reviewType, String entityType) throws ApiException {
        ApiResponse<Response> resp = getReviewDisplayAttributesWithHttpInfo(reviewType, entityType);
        return resp.getData();
    }

    /**
     * Get custom display attributes for reviewer item
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getReviewDisplayAttributesWithHttpInfo(String reviewType, String entityType) throws ApiException {
        com.squareup.okhttp.Call call = getReviewDisplayAttributesValidateBeforeCall(reviewType, entityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get custom display attributes for reviewer item (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer 
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReviewDisplayAttributesAsync(String reviewType, String entityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReviewDisplayAttributesValidateBeforeCall(reviewType, entityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importRoles
     * @param body - The import data (required)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importRolesCall(ModelImport body, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/definitions/import";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (reportOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reportOnly", reportOnly));

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
    private com.squareup.okhttp.Call importRolesValidateBeforeCall(ModelImport body, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = importRolesCall(body, reportOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body - The import data (required)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importRoles(ModelImport body, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importRolesWithHttpInfo(body, reportOnly);
        return resp.getData();
    }

    /**
     * Perform an import
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body - The import data (required)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importRolesWithHttpInfo(ModelImport body, Boolean reportOnly) throws ApiException {
        com.squareup.okhttp.Call call = importRolesValidateBeforeCall(body, reportOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body - The import data (required)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importRolesAsync(ModelImport body, Boolean reportOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importRolesValidateBeforeCall(body, reportOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZip
     * @param uploadedInputStream  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/import/preview";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        if (uploadedInputStream != null)
        localVarFormParams.put("uploadedInputStream", uploadedInputStream);

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "multipart/form-data"
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
    private com.squareup.okhttp.Call importZipValidateBeforeCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZip(Async)");
        }
        
        com.squareup.okhttp.Call call = importZipCall(uploadedInputStream, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param uploadedInputStream  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip(File uploadedInputStream) throws ApiException {
        ApiResponse<Response> resp = importZipWithHttpInfo(uploadedInputStream);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param uploadedInputStream  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZipWithHttpInfo(File uploadedInputStream) throws ApiException {
        com.squareup.okhttp.Call call = importZipValidateBeforeCall(uploadedInputStream, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param uploadedInputStream  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZipAsync(File uploadedInputStream, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZipValidateBeforeCall(uploadedInputStream, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZipResolveApps
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipResolveAppsCall(File uploadedInputStream, Boolean build, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/import/resolve/apps";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (build != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("build", build));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        if (uploadedInputStream != null)
        localVarFormParams.put("uploadedInputStream", uploadedInputStream);

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "multipart/form-data"
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
    private com.squareup.okhttp.Call importZipResolveAppsValidateBeforeCall(File uploadedInputStream, Boolean build, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZipResolveApps(Async)");
        }
        
        com.squareup.okhttp.Call call = importZipResolveAppsCall(uploadedInputStream, build, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZipResolveApps(File uploadedInputStream, Boolean build) throws ApiException {
        ApiResponse<Response> resp = importZipResolveAppsWithHttpInfo(uploadedInputStream, build);
        return resp.getData();
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZipResolveAppsWithHttpInfo(File uploadedInputStream, Boolean build) throws ApiException {
        com.squareup.okhttp.Call call = importZipResolveAppsValidateBeforeCall(uploadedInputStream, build, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Resolve Applications in the import document (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZipResolveAppsAsync(File uploadedInputStream, Boolean build, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZipResolveAppsValidateBeforeCall(uploadedInputStream, build, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for saveReviewDisplayAttributes
     * @param body list of attribute (required)
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call saveReviewDisplayAttributesCall(Schema body, String reviewType, String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/definitions/attributes/{reviewType}/{entityType}"
            .replaceAll("\\{" + "reviewType" + "\\}", apiClient.escapeString(reviewType.toString()))
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()));

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
    private com.squareup.okhttp.Call saveReviewDisplayAttributesValidateBeforeCall(Schema body, String reviewType, String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling saveReviewDisplayAttributes(Async)");
        }
        // verify the required parameter 'reviewType' is set
        if (reviewType == null) {
            throw new ApiException("Missing the required parameter 'reviewType' when calling saveReviewDisplayAttributes(Async)");
        }
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling saveReviewDisplayAttributes(Async)");
        }
        
        com.squareup.okhttp.Call call = saveReviewDisplayAttributesCall(body, reviewType, entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Save custom attributes to be displayed on a review item
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body list of attribute (required)
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response saveReviewDisplayAttributes(Schema body, String reviewType, String entityType) throws ApiException {
        ApiResponse<Response> resp = saveReviewDisplayAttributesWithHttpInfo(body, reviewType, entityType);
        return resp.getData();
    }

    /**
     * Save custom attributes to be displayed on a review item
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body list of attribute (required)
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> saveReviewDisplayAttributesWithHttpInfo(Schema body, String reviewType, String entityType) throws ApiException {
        com.squareup.okhttp.Call call = saveReviewDisplayAttributesValidateBeforeCall(body, reviewType, entityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Save custom attributes to be displayed on a review item (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body list of attribute (required)
     * @param reviewType review type (required)
     * @param entityType entity type (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call saveReviewDisplayAttributesAsync(Schema body, String reviewType, String entityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = saveReviewDisplayAttributesValidateBeforeCall(body, reviewType, entityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param id - The review definitions to export (optional)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(List<Long> id, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));
        if (broles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("broles", broles));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
        if (roles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roles", roles));
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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(List<Long> id, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startDownloadCall(id, broles, apps, roles, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the review definition download.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The review definitions to export (optional)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(List<Long> id, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(id, broles, apps, roles, attrFilter);
        return resp.getData();
    }

    /**
     * Start the review definition download.
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The review definitions to export (optional)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(List<Long> id, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, broles, apps, roles, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the review definition download. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The review definitions to export (optional)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(List<Long> id, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, broles, apps, roles, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_0
     * @param body -  The download request node (required)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Call(Download body, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/definitions/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (broles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("broles", broles));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
        if (roles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roles", roles));
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
    private com.squareup.okhttp.Call startDownload_0ValidateBeforeCall(Download body, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_0Call(body, broles, apps, roles, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the review definition download.
     * Accepted Roles: * Auditor * Customer Administrator * Review Administrator * Review Owner 
     * @param body -  The download request node (required)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body, broles, apps, roles, attrFilter);
        return resp.getData();
    }

    /**
     * Start the review definition download.
     * Accepted Roles: * Auditor * Customer Administrator * Review Administrator * Review Owner 
     * @param body -  The download request node (required)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_0WithHttpInfo(Download body, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, broles, apps, roles, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the review definition download. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Review Administrator * Review Owner 
     * @param body -  The download request node (required)
     * @param broles - Flag to export referenced business roles referenced by review (optional, default to false)
     * @param apps - Flag to export referenced applications referenced by review (optional, default to false)
     * @param roles - Flag to export referenced technical roles referenced by review (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Async(Download body, Boolean broles, Boolean apps, Boolean roles, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, broles, apps, roles, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startMicroCertificationReview
     * @param body review definition node populated with startup setting, unique review definition id must be populated (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startMicroCertificationReviewCall(ReviewDef body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/definitions/start";

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
    private com.squareup.okhttp.Call startMicroCertificationReviewValidateBeforeCall(ReviewDef body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startMicroCertificationReview(Async)");
        }
        
        com.squareup.okhttp.Call call = startMicroCertificationReviewCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start review based on unique id.
     * Originally purposed for MicroCertification&lt;br/&gt;Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body review definition node populated with startup setting, unique review definition id must be populated (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startMicroCertificationReview(ReviewDef body) throws ApiException {
        ApiResponse<Response> resp = startMicroCertificationReviewWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Start review based on unique id.
     * Originally purposed for MicroCertification&lt;br/&gt;Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body review definition node populated with startup setting, unique review definition id must be populated (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startMicroCertificationReviewWithHttpInfo(ReviewDef body) throws ApiException {
        com.squareup.okhttp.Call call = startMicroCertificationReviewValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start review based on unique id. (asynchronously)
     * Originally purposed for MicroCertification&lt;br/&gt;Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body review definition node populated with startup setting, unique review definition id must be populated (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startMicroCertificationReviewAsync(ReviewDef body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startMicroCertificationReviewValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startReview
     * @param body - JSON body, with (optional) expectedEndDate populated.                       If not passed will calculate using review
                       definition                       duration settings. (required)
     * @param id review definition id (required)
     * @param startInPreview determine whether to start in PREVIEW state (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startReviewCall(ReviewInst body, Long id, Boolean startInPreview, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/definitions/{id}/schedules"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (startInPreview != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startInPreview", startInPreview));

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
    private com.squareup.okhttp.Call startReviewValidateBeforeCall(ReviewInst body, Long id, Boolean startInPreview, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startReview(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling startReview(Async)");
        }
        
        com.squareup.okhttp.Call call = startReviewCall(body, id, startInPreview, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start review
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body - JSON body, with (optional) expectedEndDate populated.                       If not passed will calculate using review
                       definition                       duration settings. (required)
     * @param id review definition id (required)
     * @param startInPreview determine whether to start in PREVIEW state (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startReview(ReviewInst body, Long id, Boolean startInPreview) throws ApiException {
        ApiResponse<Response> resp = startReviewWithHttpInfo(body, id, startInPreview);
        return resp.getData();
    }

    /**
     * Start review
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body - JSON body, with (optional) expectedEndDate populated.                       If not passed will calculate using review
                       definition                       duration settings. (required)
     * @param id review definition id (required)
     * @param startInPreview determine whether to start in PREVIEW state (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startReviewWithHttpInfo(ReviewInst body, Long id, Boolean startInPreview) throws ApiException {
        com.squareup.okhttp.Call call = startReviewValidateBeforeCall(body, id, startInPreview, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start review (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator * Review Owner 
     * @param body - JSON body, with (optional) expectedEndDate populated.                       If not passed will calculate using review
                       definition                       duration settings. (required)
     * @param id review definition id (required)
     * @param startInPreview determine whether to start in PREVIEW state (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startReviewAsync(ReviewInst body, Long id, Boolean startInPreview, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startReviewValidateBeforeCall(body, id, startInPreview, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for statusDownload
     * @param id - The download id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call statusDownloadCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/review/definitions/download/{id}/status"
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response statusDownload(String id) throws ApiException {
        ApiResponse<Response> resp = statusDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Check the status of a download
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id (required)
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
     * Accepted Roles: * Auditor * Customer Administrator * Historical Review Owner * Review Administrator * Review Monitor * Review Owner 
     * @param id - The download id (required)
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
     * Build call for updateReviewDefinition
     * @param body JSON payload (required)
     * @param id primary key/id of review definition (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateReviewDefinitionCall(ReviewDef body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/review/definitions/{id}"
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
    private com.squareup.okhttp.Call updateReviewDefinitionValidateBeforeCall(ReviewDef body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateReviewDefinition(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateReviewDefinition(Async)");
        }
        
        com.squareup.okhttp.Call call = updateReviewDefinitionCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update review definition
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body JSON payload (required)
     * @param id primary key/id of review definition (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateReviewDefinition(ReviewDef body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateReviewDefinitionWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update review definition
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body JSON payload (required)
     * @param id primary key/id of review definition (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateReviewDefinitionWithHttpInfo(ReviewDef body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateReviewDefinitionValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update review definition (asynchronously)
     * Accepted Roles: * Customer Administrator * Review Administrator 
     * @param body JSON payload (required)
     * @param id primary key/id of review definition (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateReviewDefinitionAsync(ReviewDef body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateReviewDefinitionValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
