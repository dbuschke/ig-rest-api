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


import io.swagger.client.model.Collector;
import io.swagger.client.model.Collectortcsettings;
import io.swagger.client.model.Response;
import io.swagger.client.model.RtcChangeEventBatchDataListNode;
import io.swagger.client.model.RtcEventIngestionList;
import io.swagger.client.model.Status;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DcsCollectorsApi {
    private ApiClient apiClient;

    public DcsCollectorsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DcsCollectorsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for addCollector
     * @param body data collector node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addCollectorCall(Collector body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors";

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
    private com.squareup.okhttp.Call addCollectorValidateBeforeCall(Collector body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling addCollector(Async)");
        }
        
        com.squareup.okhttp.Call call = addCollectorCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Add new data collector
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body data collector node (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response addCollector(Collector body) throws ApiException {
        ApiResponse<Response> resp = addCollectorWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Add new data collector
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body data collector node (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> addCollectorWithHttpInfo(Collector body) throws ApiException {
        com.squareup.okhttp.Call call = addCollectorValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Add new data collector (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body data collector node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addCollectorAsync(Collector body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = addCollectorValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createSchedule
     * @param id unique id of collector (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createScheduleCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}/schedules"
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
    private com.squareup.okhttp.Call createScheduleValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling createSchedule(Async)");
        }
        
        com.squareup.okhttp.Call call = createScheduleCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create schedule for collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of collector (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response createSchedule(Long id) throws ApiException {
        ApiResponse<Response> resp = createScheduleWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Create schedule for collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of collector (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> createScheduleWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = createScheduleValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create schedule for collector. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of collector (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createScheduleAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createScheduleValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteCollector
     * @param id unique id of data collector (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteCollectorCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}"
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
    private com.squareup.okhttp.Call deleteCollectorValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteCollector(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteCollectorCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete specified data collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response deleteCollector(Long id) throws ApiException {
        ApiResponse<Response> resp = deleteCollectorWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete specified data collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> deleteCollectorWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteCollectorValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete specified data collector. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteCollectorAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteCollectorValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteRtcEventIngestions
     * @param id Ids of the productions that are to be deleted (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteRtcEventIngestionsCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/ingestions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));

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
    private com.squareup.okhttp.Call deleteRtcEventIngestionsValidateBeforeCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteRtcEventIngestionsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete identity change event productions
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Ids of the productions that are to be deleted (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteRtcEventIngestions(List<Long> id) throws ApiException {
        ApiResponse<Status> resp = deleteRtcEventIngestionsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete identity change event productions
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Ids of the productions that are to be deleted (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteRtcEventIngestionsWithHttpInfo(List<Long> id) throws ApiException {
        com.squareup.okhttp.Call call = deleteRtcEventIngestionsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete identity change event productions (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Ids of the productions that are to be deleted (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteRtcEventIngestionsAsync(List<Long> id, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteRtcEventIngestionsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAllCollectors
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAllCollectorsCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors";

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
    private com.squareup.okhttp.Call getAllCollectorsValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getAllCollectorsCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get all collectors.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAllCollectors() throws ApiException {
        ApiResponse<Response> resp = getAllCollectorsWithHttpInfo();
        return resp.getData();
    }

    /**
     * Get all collectors.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAllCollectorsWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getAllCollectorsValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get all collectors. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAllCollectorsAsync(final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAllCollectorsValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCollector
     * @param id unique id of data collector (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCollectorCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}"
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
    private com.squareup.okhttp.Call getCollectorValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getCollector(Async)");
        }
        
        com.squareup.okhttp.Call call = getCollectorCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get specified data collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getCollector(Long id) throws ApiException {
        ApiResponse<Response> resp = getCollectorWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get specified data collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getCollectorWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getCollectorValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get specified data collector. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCollectorAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCollectorValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCollectorHistory
     * @param id unique id of data collector (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCollectorHistoryCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}/history"
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
    private com.squareup.okhttp.Call getCollectorHistoryValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getCollectorHistory(Async)");
        }
        
        com.squareup.okhttp.Call call = getCollectorHistoryCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get change history list of specified data collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getCollectorHistory(Long id) throws ApiException {
        ApiResponse<Response> resp = getCollectorHistoryWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get change history list of specified data collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getCollectorHistoryWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getCollectorHistoryValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get change history list of specified data collector. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id unique id of data collector (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCollectorHistoryAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCollectorHistoryValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEventIngestionStatus
     * @param id -- collector to rport on (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getEventIngestionStatusCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}/ingestionStatus"
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
    private com.squareup.okhttp.Call getEventIngestionStatusValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getEventIngestionStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getEventIngestionStatusCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the event ingestion status for the argument collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -- collector to rport on (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getEventIngestionStatus(Long id) throws ApiException {
        ApiResponse<Response> resp = getEventIngestionStatusWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the event ingestion status for the argument collector.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -- collector to rport on (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getEventIngestionStatusWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getEventIngestionStatusValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the event ingestion status for the argument collector. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -- collector to rport on (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEventIngestionStatusAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEventIngestionStatusValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEventIngestions
     * @param id Id of the event-enabled collector (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types.  If specified, only include   ingestions that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getEventIngestionsCall(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}/ingestions"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (changeEventType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "changeEventType", changeEventType));

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
    private com.squareup.okhttp.Call getEventIngestionsValidateBeforeCall(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getEventIngestions(Async)");
        }
        
        com.squareup.okhttp.Call call = getEventIngestionsCall(id, size, indexFrom, q, sortBy, sortOrder, changeEventType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns list of RTC event ingestions.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Id of the event-enabled collector (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types.  If specified, only include   ingestions that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @return RtcEventIngestionList
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RtcEventIngestionList getEventIngestions(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType) throws ApiException {
        ApiResponse<RtcEventIngestionList> resp = getEventIngestionsWithHttpInfo(id, size, indexFrom, q, sortBy, sortOrder, changeEventType);
        return resp.getData();
    }

    /**
     * Returns list of RTC event ingestions.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Id of the event-enabled collector (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types.  If specified, only include   ingestions that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @return ApiResponse&lt;RtcEventIngestionList&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RtcEventIngestionList> getEventIngestionsWithHttpInfo(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType) throws ApiException {
        com.squareup.okhttp.Call call = getEventIngestionsValidateBeforeCall(id, size, indexFrom, q, sortBy, sortOrder, changeEventType, null, null);
        Type localVarReturnType = new TypeToken<RtcEventIngestionList>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns list of RTC event ingestions. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Id of the event-enabled collector (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types.  If specified, only include   ingestions that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEventIngestionsAsync(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType, final ApiCallback<RtcEventIngestionList> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEventIngestionsValidateBeforeCall(id, size, indexFrom, q, sortBy, sortOrder, changeEventType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RtcEventIngestionList>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getIngestionEvents
     * @param id Id of the RTC ingestion production (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types to return.  If specified, only include   events that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getIngestionEventsCall(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/ingestions/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (changeEventType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "changeEventType", changeEventType));

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
    private com.squareup.okhttp.Call getIngestionEventsValidateBeforeCall(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getIngestionEvents(Async)");
        }
        
        com.squareup.okhttp.Call call = getIngestionEventsCall(id, size, indexFrom, q, sortBy, sortOrder, changeEventType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns list of events for a given RTC ingestion production.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Id of the RTC ingestion production (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types to return.  If specified, only include   events that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @return RtcChangeEventBatchDataListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RtcChangeEventBatchDataListNode getIngestionEvents(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType) throws ApiException {
        ApiResponse<RtcChangeEventBatchDataListNode> resp = getIngestionEventsWithHttpInfo(id, size, indexFrom, q, sortBy, sortOrder, changeEventType);
        return resp.getData();
    }

    /**
     * Returns list of events for a given RTC ingestion production.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Id of the RTC ingestion production (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types to return.  If specified, only include   events that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @return ApiResponse&lt;RtcChangeEventBatchDataListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RtcChangeEventBatchDataListNode> getIngestionEventsWithHttpInfo(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType) throws ApiException {
        com.squareup.okhttp.Call call = getIngestionEventsValidateBeforeCall(id, size, indexFrom, q, sortBy, sortOrder, changeEventType, null, null);
        Type localVarReturnType = new TypeToken<RtcChangeEventBatchDataListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns list of events for a given RTC ingestion production. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id Id of the RTC ingestion production (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param q Search filter.  Used to search for strings in the change events (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param changeEventType List of change event types to return.  If specified, only include   events that have one or more of the specified change event types.   Valid event types are ALL, ALL_USER, ALL_GROUP, ALL_GROUP_TO_GROUP,   ADD_USER, MODIFY_USER, DELETE_USER, ADD_GROUP, MODIFY_GROUP, DELETE_GROUP,   ADD_GROUP_TO_GROUP, MODIFY_GROUP_TO_GROUP (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getIngestionEventsAsync(Long id, Integer size, Integer indexFrom, String q, String sortBy, String sortOrder, List<String> changeEventType, final ApiCallback<RtcChangeEventBatchDataListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getIngestionEventsValidateBeforeCall(id, size, indexFrom, q, sortBy, sortOrder, changeEventType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RtcChangeEventBatchDataListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateCollector
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateCollectorCall(Collector body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}"
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
    private com.squareup.okhttp.Call updateCollectorValidateBeforeCall(Collector body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateCollector(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateCollector(Async)");
        }
        
        com.squareup.okhttp.Call call = updateCollectorCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update data collector
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateCollector(Collector body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateCollectorWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update data collector
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateCollectorWithHttpInfo(Collector body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateCollectorValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update data collector (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateCollectorAsync(Collector body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateCollectorValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateCollectorStatus
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateCollectorStatusCall(Collector body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}/status"
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
    private com.squareup.okhttp.Call updateCollectorStatusValidateBeforeCall(Collector body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateCollectorStatus(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateCollectorStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = updateCollectorStatusCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update data collector
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateCollectorStatus(Collector body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateCollectorStatusWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update data collector
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateCollectorStatusWithHttpInfo(Collector body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateCollectorStatusValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update data collector (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateCollectorStatusAsync(Collector body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateCollectorStatusValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateCollectorWithTCSettings
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateCollectorWithTCSettingsCall(Collectortcsettings body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dcs/collectors/{id}/tcsettings"
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
    private com.squareup.okhttp.Call updateCollectorWithTCSettingsValidateBeforeCall(Collectortcsettings body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateCollectorWithTCSettings(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateCollectorWithTCSettings(Async)");
        }
        
        com.squareup.okhttp.Call call = updateCollectorWithTCSettingsCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update data collector view with test collection settings
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateCollectorWithTCSettings(Collectortcsettings body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateCollectorWithTCSettingsWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update data collector view with test collection settings
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateCollectorWithTCSettingsWithHttpInfo(Collectortcsettings body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateCollectorWithTCSettingsValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update data collector view with test collection settings (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body JSON payload (required)
     * @param id id of collector (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateCollectorWithTCSettingsAsync(Collectortcsettings body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateCollectorWithTCSettingsValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
