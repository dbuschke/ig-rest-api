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


import io.swagger.client.model.Analyze;
import io.swagger.client.model.AuthInfoList;
import io.swagger.client.model.Auths;
import io.swagger.client.model.BrDetectPreview;
import io.swagger.client.model.Burole;
import io.swagger.client.model.Buroleusers;
import io.swagger.client.model.Calculated;
import io.swagger.client.model.Containedperms;
import io.swagger.client.model.DetectItems;
import io.swagger.client.model.Detections;
import io.swagger.client.model.Detetions;
import io.swagger.client.model.Download;
import io.swagger.client.model.EntityCategorySearch;
import java.io.File;
import io.swagger.client.model.Mine;
import io.swagger.client.model.Permissions;
import io.swagger.client.model.Policies;
import io.swagger.client.model.Resource;
import io.swagger.client.model.Resources;
import io.swagger.client.model.Response;
import io.swagger.client.model.Roles;
import io.swagger.client.model.SearchCriteria;
import io.swagger.client.model.Status;
import io.swagger.client.model.Users;
import io.swagger.client.model.Values;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyBroleApi {
    private ApiClient apiClient;

    public PolicyBroleApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyBroleApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for analyzeBusinessRoleCandidate
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call analyzeBusinessRoleCandidateCall(Values body, String mode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/mine/analyze";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (mode != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("mode", mode));

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
    private com.squareup.okhttp.Call analyzeBusinessRoleCandidateValidateBeforeCall(Values body, String mode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling analyzeBusinessRoleCandidate(Async)");
        }
        
        com.squareup.okhttp.Call call = analyzeBusinessRoleCandidateCall(body, mode, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Match the mined business roles to existing roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole analyzeBusinessRoleCandidate(Values body, String mode) throws ApiException {
        ApiResponse<Burole> resp = analyzeBusinessRoleCandidateWithHttpInfo(body, mode);
        return resp.getData();
    }

    /**
     * Match the mined business roles to existing roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> analyzeBusinessRoleCandidateWithHttpInfo(Values body, String mode) throws ApiException {
        com.squareup.okhttp.Call call = analyzeBusinessRoleCandidateValidateBeforeCall(body, mode, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Match the mined business roles to existing roles (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call analyzeBusinessRoleCandidateAsync(Values body, String mode, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = analyzeBusinessRoleCandidateValidateBeforeCall(body, mode, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for autoRequestResources
     * @param body Resources to request. (required)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call autoRequestResourcesCall(Resources body, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/autorequests";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));

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
    private com.squareup.okhttp.Call autoRequestResourcesValidateBeforeCall(Resources body, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling autoRequestResources(Async)");
        }
        
        com.squareup.okhttp.Call call = autoRequestResourcesCall(body, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Request resources that are inconsistent with either auto-grant or auto-revoke policy of business role authorizations.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Resources to request. (required)
     * @param q Quick search filter, used to search across all fields (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status autoRequestResources(Resources body, String q) throws ApiException {
        ApiResponse<Status> resp = autoRequestResourcesWithHttpInfo(body, q);
        return resp.getData();
    }

    /**
     * Request resources that are inconsistent with either auto-grant or auto-revoke policy of business role authorizations.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Resources to request. (required)
     * @param q Quick search filter, used to search across all fields (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> autoRequestResourcesWithHttpInfo(Resources body, String q) throws ApiException {
        com.squareup.okhttp.Call call = autoRequestResourcesValidateBeforeCall(body, q, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Request resources that are inconsistent with either auto-grant or auto-revoke policy of business role authorizations. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Resources to request. (required)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call autoRequestResourcesAsync(Resources body, String q, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = autoRequestResourcesValidateBeforeCall(body, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for cancelAnalysis
     * @param id - The analysis id to delete (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cancelAnalysisCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze/{id}"
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
    private com.squareup.okhttp.Call cancelAnalysisValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling cancelAnalysis(Async)");
        }
        
        com.squareup.okhttp.Call call = cancelAnalysisCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Cancel a analysis
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id - The analysis id to delete (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response cancelAnalysis(String id) throws ApiException {
        ApiResponse<Response> resp = cancelAnalysisWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Cancel a analysis
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id - The analysis id to delete (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> cancelAnalysisWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = cancelAnalysisValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Cancel a analysis (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id - The analysis id to delete (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cancelAnalysisAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = cancelAnalysisValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
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
        String localVarPath = "/policy/brole/download/{id}"
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator 
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator 
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator 
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
     * Build call for cancelInconsistencyDetection
     * @param id ID of detection to be canceled. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cancelInconsistencyDetectionCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/inconsistencydetections/{id}"
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
    private com.squareup.okhttp.Call cancelInconsistencyDetectionValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling cancelInconsistencyDetection(Async)");
        }
        
        com.squareup.okhttp.Call call = cancelInconsistencyDetectionCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Cancel an inconsistency detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id ID of detection to be canceled. (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status cancelInconsistencyDetection(Long id) throws ApiException {
        ApiResponse<Status> resp = cancelInconsistencyDetectionWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Cancel an inconsistency detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id ID of detection to be canceled. (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> cancelInconsistencyDetectionWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = cancelInconsistencyDetectionValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Cancel an inconsistency detection. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id ID of detection to be canceled. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cancelInconsistencyDetectionAsync(Long id, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = cancelInconsistencyDetectionValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for compareMembers
     * @param id1 - One of the business roles to compare (optional)
     * @param id2 - One of the business roles to compare (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to br1)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call compareMembersCall(Long id1, Long id2, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/compare/members";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id1 != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id1", id1));
        if (id2 != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id2", id2));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
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
    private com.squareup.okhttp.Call compareMembersValidateBeforeCall(Long id1, Long id2, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = compareMembersCall(id1, id2, size, q, sortBy, sortOrder, indexFrom, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of business roles that reference the business role specified by the given id
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id1 - One of the business roles to compare (optional)
     * @param id2 - One of the business roles to compare (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to br1)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response compareMembers(Long id1, Long id2, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch) throws ApiException {
        ApiResponse<Response> resp = compareMembersWithHttpInfo(id1, id2, size, q, sortBy, sortOrder, indexFrom, qMatch);
        return resp.getData();
    }

    /**
     * Get the list of business roles that reference the business role specified by the given id
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id1 - One of the business roles to compare (optional)
     * @param id2 - One of the business roles to compare (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to br1)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> compareMembersWithHttpInfo(Long id1, Long id2, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = compareMembersValidateBeforeCall(id1, id2, size, q, sortBy, sortOrder, indexFrom, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of business roles that reference the business role specified by the given id (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id1 - One of the business roles to compare (optional)
     * @param id2 - One of the business roles to compare (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to br1)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call compareMembersAsync(Long id1, Long id2, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = compareMembersValidateBeforeCall(id1, id2, size, q, sortBy, sortOrder, indexFrom, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createBusinessRole
     * @param body The business role to create. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createBusinessRoleCall(Burole body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/new";

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

        String[] localVarAuthNames = new String[] { "OSP" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call createBusinessRoleValidateBeforeCall(Burole body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createBusinessRole(Async)");
        }
        
        com.squareup.okhttp.Call call = createBusinessRoleCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role to create. (required)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole createBusinessRole(Burole body) throws ApiException {
        ApiResponse<Burole> resp = createBusinessRoleWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role to create. (required)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> createBusinessRoleWithHttpInfo(Burole body) throws ApiException {
        com.squareup.okhttp.Call call = createBusinessRoleValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a business role. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role to create. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createBusinessRoleAsync(Burole body, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createBusinessRoleValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createMinedBusinessRoles
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createMinedBusinessRolesCall(Values body, String mode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/mine/broles";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (mode != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("mode", mode));

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
    private com.squareup.okhttp.Call createMinedBusinessRolesValidateBeforeCall(Values body, String mode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMinedBusinessRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = createMinedBusinessRolesCall(body, mode, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create the mined business roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole createMinedBusinessRoles(Values body, String mode) throws ApiException {
        ApiResponse<Burole> resp = createMinedBusinessRolesWithHttpInfo(body, mode);
        return resp.getData();
    }

    /**
     * Create the mined business roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> createMinedBusinessRolesWithHttpInfo(Values body, String mode) throws ApiException {
        com.squareup.okhttp.Call call = createMinedBusinessRolesValidateBeforeCall(body, mode, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create the mined business roles (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The user attribute values to add as business role criteria (required)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMinedBusinessRolesAsync(Values body, String mode, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createMinedBusinessRolesValidateBeforeCall(body, mode, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteAnalysisResults
     * @param id - The analysis id to delete (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteAnalysisResultsCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze/{id}/results"
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
    private com.squareup.okhttp.Call deleteAnalysisResultsValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteAnalysisResults(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteAnalysisResultsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the analysis results
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id - The analysis id to delete (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response deleteAnalysisResults(String id) throws ApiException {
        ApiResponse<Response> resp = deleteAnalysisResultsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete the analysis results
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id - The analysis id to delete (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> deleteAnalysisResultsWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = deleteAnalysisResultsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the analysis results (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id - The analysis id to delete (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteAnalysisResultsAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteAnalysisResultsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteBusinessRole
     * @param id The business role ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteBusinessRoleCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{id}"
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
    private com.squareup.okhttp.Call deleteBusinessRoleValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteBusinessRole(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteBusinessRoleCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the business role with the given ID.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The business role ID. (required)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole deleteBusinessRole(Long id) throws ApiException {
        ApiResponse<Burole> resp = deleteBusinessRoleWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete the business role with the given ID.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The business role ID. (required)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> deleteBusinessRoleWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteBusinessRoleValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the business role with the given ID. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The business role ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteBusinessRoleAsync(Long id, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteBusinessRoleValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteBusinessRoleDetections
     * @param id IDs of business role detections to be deleted. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteBusinessRoleDetectionsCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/detections";

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
    private com.squareup.okhttp.Call deleteBusinessRoleDetectionsValidateBeforeCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteBusinessRoleDetectionsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete business role detections.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id IDs of business role detections to be deleted. (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteBusinessRoleDetections(List<Long> id) throws ApiException {
        ApiResponse<Status> resp = deleteBusinessRoleDetectionsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete business role detections.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id IDs of business role detections to be deleted. (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteBusinessRoleDetectionsWithHttpInfo(List<Long> id) throws ApiException {
        com.squareup.okhttp.Call call = deleteBusinessRoleDetectionsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete business role detections. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id IDs of business role detections to be deleted. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteBusinessRoleDetectionsAsync(List<Long> id, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteBusinessRoleDetectionsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteBusinessRoles
     * @param id The list of business role IDs to delete. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteBusinessRolesCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/bulk";

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
    private com.squareup.okhttp.Call deleteBusinessRolesValidateBeforeCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteBusinessRolesCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Bulk delete the business roles with the given IDs.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The list of business role IDs to delete. (optional)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole deleteBusinessRoles(List<Long> id) throws ApiException {
        ApiResponse<Burole> resp = deleteBusinessRolesWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Bulk delete the business roles with the given IDs.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The list of business role IDs to delete. (optional)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> deleteBusinessRolesWithHttpInfo(List<Long> id) throws ApiException {
        com.squareup.okhttp.Call call = deleteBusinessRolesValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Bulk delete the business roles with the given IDs. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The list of business role IDs to delete. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteBusinessRolesAsync(List<Long> id, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteBusinessRolesValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for estimateDeactivationImpact
     * @param ID ID of the published business role whose deactivate impact is to be estimated. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                           &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                         deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                         &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                         of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call estimateDeactivationImpactCall(Long ID, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/deactivatepreview/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (countDetail != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("countDetail", countDetail));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
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
    private com.squareup.okhttp.Call estimateDeactivationImpactValidateBeforeCall(Long ID, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling estimateDeactivationImpact(Async)");
        }
        
        com.squareup.okhttp.Call call = estimateDeactivationImpactCall(ID, countDetail, size, indexFrom, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Preview what would change if the specified business role were to be deactivated.
     * The preview information returned includes various counts.  Number of new users that would become members of the business role, number of users that would lose membership in the business role, etc.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the published business role whose deactivate impact is to be estimated. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                           &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                         deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                         &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                         of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @return BrDetectPreview
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BrDetectPreview estimateDeactivationImpact(Long ID, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<BrDetectPreview> resp = estimateDeactivationImpactWithHttpInfo(ID, countDetail, size, indexFrom, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Preview what would change if the specified business role were to be deactivated.
     * The preview information returned includes various counts.  Number of new users that would become members of the business role, number of users that would lose membership in the business role, etc.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the published business role whose deactivate impact is to be estimated. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                           &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                         deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                         &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                         of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @return ApiResponse&lt;BrDetectPreview&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BrDetectPreview> estimateDeactivationImpactWithHttpInfo(Long ID, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = estimateDeactivationImpactValidateBeforeCall(ID, countDetail, size, indexFrom, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<BrDetectPreview>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Preview what would change if the specified business role were to be deactivated. (asynchronously)
     * The preview information returned includes various counts.  Number of new users that would become members of the business role, number of users that would lose membership in the business role, etc.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the published business role whose deactivate impact is to be estimated. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                           &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                         deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                         &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                         of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call estimateDeactivationImpactAsync(Long ID, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ApiCallback<BrDetectPreview> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = estimateDeactivationImpactValidateBeforeCall(ID, countDetail, size, indexFrom, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BrDetectPreview>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for estimatePublishImpact
     * @param body Business role node which defines what the business role would look like if it were to be published. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                         &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                          deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                           &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                           of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call estimatePublishImpactCall(Burole body, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/publishpreview";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (countDetail != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("countDetail", countDetail));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
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
    private com.squareup.okhttp.Call estimatePublishImpactValidateBeforeCall(Burole body, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling estimatePublishImpact(Async)");
        }
        
        com.squareup.okhttp.Call call = estimatePublishImpactCall(body, countDetail, size, indexFrom, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Preview what would change if the specified business role were to be published.
     * The preview information returned includes various counts.  Number of new users that would become members of the business role, number of users that would lose membership in the business role, etc.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Business role node which defines what the business role would look like if it were to be published. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                         &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                          deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                           &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                           of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @return BrDetectPreview
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BrDetectPreview estimatePublishImpact(Burole body, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<BrDetectPreview> resp = estimatePublishImpactWithHttpInfo(body, countDetail, size, indexFrom, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Preview what would change if the specified business role were to be published.
     * The preview information returned includes various counts.  Number of new users that would become members of the business role, number of users that would lose membership in the business role, etc.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Business role node which defines what the business role would look like if it were to be published. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                         &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                          deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                           &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                           of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @return ApiResponse&lt;BrDetectPreview&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BrDetectPreview> estimatePublishImpactWithHttpInfo(Burole body, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = estimatePublishImpactValidateBeforeCall(body, countDetail, size, indexFrom, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<BrDetectPreview>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Preview what would change if the specified business role were to be published. (asynchronously)
     * The preview information returned includes various counts.  Number of new users that would become members of the business role, number of users that would lose membership in the business role, etc.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Business role node which defines what the business role would look like if it were to be published. (required)
     * @param countDetail This specifies a particular count that the user would like more information on.  It may be one of the following: &lt;ul&gt;                         &lt;li&gt;newMembers.  This will return the list of new members.&lt;/li&gt;    &lt;li&gt;deletedMembers.  This will return the list of                          deleted members.&lt;/li&gt;    &lt;li&gt;newPermAuths.  This will return the list of new permission authorizations.&lt;/li&gt;                           &lt;li&gt;deletedPermAuths.  This will return the list of deleted permission authorizations.&lt;/li&gt;    &lt;li&gt;newRoleAuths.                           This will return the list of new technical role authorizations.&lt;/li&gt;    &lt;li&gt;deletedRoleAuths.  This will return the list                           of deleted technical role authorizations.&lt;/li&gt;    &lt;li&gt;newAppAuths.  This will return the list of new application                         authorizations.&lt;/li&gt;    &lt;li&gt;deletedAppAuths.  This will return the list of deleted application authorizations.&lt;/li&gt;                         &lt;li&gt;grantPermRequests.  This will return the list of permission grant requests.&lt;/li&gt;    &lt;li&gt;revokePermRequests.                         This will return the list of permission revoke requests.&lt;/li&gt;    &lt;li&gt;grantAppRequests.  This will return the list of                         application grant requests.&lt;/li&gt;    &lt;li&gt;revokeAppRequests.  This will return the list of application revoke                         requests.&lt;/li&gt;                         &lt;/ul&gt; (optional)
     * @param size Number of results to return, used for paging.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param indexFrom Starting row index in result set. 0 is the first.  This is only used if the countDetail query parameter is set. (optional, default to 0)
     * @param sortBy column to sort by.  This is only applicable when returning lists that have more than one column.  When returning                         authorizations or grant/revoke requests, the sortBy may be either \&quot;userName\&quot; or \&quot;resourceName\&quot;. (optional, default to userName)
     * @param sortOrder Sort order. Valid values: ASC or DESC.  This is only used if the countDetail query parameter is set. (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call estimatePublishImpactAsync(Burole body, String countDetail, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ApiCallback<BrDetectPreview> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = estimatePublishImpactValidateBeforeCall(body, countDetail, size, indexFrom, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BrDetectPreview>(){}.getType();
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
        String localVarPath = "/policy/brole/download/{id}"
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Build call for getAnalysisResults
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAnalysisResultsCall(String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, String authType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze/{id}/results"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (authType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("authType", authType));

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
    private com.squareup.okhttp.Call getAnalysisResultsValidateBeforeCall(String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, String authType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getAnalysisResults(Async)");
        }
        
        com.squareup.okhttp.Call call = getAnalysisResultsCall(id, size, q, sortBy, sortOrder, indexFrom, qMatch, authType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the results of a analysis
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAnalysisResults(String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, String authType) throws ApiException {
        ApiResponse<Response> resp = getAnalysisResultsWithHttpInfo(id, size, q, sortBy, sortOrder, indexFrom, qMatch, authType);
        return resp.getData();
    }

    /**
     * Get the results of a analysis
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAnalysisResultsWithHttpInfo(String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, String authType) throws ApiException {
        com.squareup.okhttp.Call call = getAnalysisResultsValidateBeforeCall(id, size, q, sortBy, sortOrder, indexFrom, qMatch, authType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the results of a analysis (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAnalysisResultsAsync(String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, String authType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAnalysisResultsValidateBeforeCall(id, size, q, sortBy, sortOrder, indexFrom, qMatch, authType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAnalysisStatus
     * @param id - The analysis id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAnalysisStatusCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze/{id}/status"
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
    private com.squareup.okhttp.Call getAnalysisStatusValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getAnalysisStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getAnalysisStatusCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Check the status of a analysis
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAnalysisStatus(String id) throws ApiException {
        ApiResponse<Response> resp = getAnalysisStatusWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Check the status of a analysis
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAnalysisStatusWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = getAnalysisStatusValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Check the status of a analysis (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAnalysisStatusAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAnalysisStatusValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAnalysisUsers
     * @param body - Advanced search filter (required)
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param listAttr - Attributes to return (optional)
     * @param attrFilter - Attribute filter (optional)
     * @param authId - ID of authorization to return users for (optional)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @param br - business role to return users for (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAnalysisUsersCall(SearchCriteria body, String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, List<String> listAttr, List<String> attrFilter, String authId, String authType, Long br, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze/{id}/users"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (authId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("authId", authId));
        if (authType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("authType", authType));
        if (br != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("br", br));

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
    private com.squareup.okhttp.Call getAnalysisUsersValidateBeforeCall(SearchCriteria body, String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, List<String> listAttr, List<String> attrFilter, String authId, String authType, Long br, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAnalysisUsers(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getAnalysisUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = getAnalysisUsersCall(body, id, size, q, sortBy, sortOrder, indexFrom, qMatch, listAttr, attrFilter, authId, authType, br, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the users associated with the given analysis and authorization type
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - Advanced search filter (required)
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param listAttr - Attributes to return (optional)
     * @param attrFilter - Attribute filter (optional)
     * @param authId - ID of authorization to return users for (optional)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @param br - business role to return users for (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAnalysisUsers(SearchCriteria body, String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, List<String> listAttr, List<String> attrFilter, String authId, String authType, Long br) throws ApiException {
        ApiResponse<Response> resp = getAnalysisUsersWithHttpInfo(body, id, size, q, sortBy, sortOrder, indexFrom, qMatch, listAttr, attrFilter, authId, authType, br);
        return resp.getData();
    }

    /**
     * Get the users associated with the given analysis and authorization type
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - Advanced search filter (required)
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param listAttr - Attributes to return (optional)
     * @param attrFilter - Attribute filter (optional)
     * @param authId - ID of authorization to return users for (optional)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @param br - business role to return users for (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAnalysisUsersWithHttpInfo(SearchCriteria body, String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, List<String> listAttr, List<String> attrFilter, String authId, String authType, Long br) throws ApiException {
        com.squareup.okhttp.Call call = getAnalysisUsersValidateBeforeCall(body, id, size, q, sortBy, sortOrder, indexFrom, qMatch, listAttr, attrFilter, authId, authType, br, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the users associated with the given analysis and authorization type (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - Advanced search filter (required)
     * @param id - The analysis id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param listAttr - Attributes to return (optional)
     * @param attrFilter - Attribute filter (optional)
     * @param authId - ID of authorization to return users for (optional)
     * @param authType - Type of common authorization analysis results requested (optional)
     * @param br - business role to return users for (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAnalysisUsersAsync(SearchCriteria body, String id, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, List<String> listAttr, List<String> attrFilter, String authId, String authType, Long br, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAnalysisUsersValidateBeforeCall(body, id, size, q, sortBy, sortOrder, indexFrom, qMatch, listAttr, attrFilter, authId, authType, br, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAuthorizedAppEffectiveness
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedAppEffectivenessCall(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/effectiveness/apps"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
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
    private com.squareup.okhttp.Call getAuthorizedAppEffectivenessValidateBeforeCall(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAuthorizedAppEffectiveness(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAuthorizedAppEffectiveness(Async)");
        }
        
        com.squareup.okhttp.Call call = getAuthorizedAppEffectivenessCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return effectiveness of the business role application authorization.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getAuthorizedAppEffectiveness(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Users> resp = getAuthorizedAppEffectivenessWithHttpInfo(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Return effectiveness of the business role application authorization.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getAuthorizedAppEffectivenessWithHttpInfo(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getAuthorizedAppEffectivenessValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return effectiveness of the business role application authorization. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedAppEffectivenessAsync(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAuthorizedAppEffectivenessValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAuthorizedPermissionEffectiveness
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedPermissionEffectivenessCall(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/effectiveness/perms"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
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
    private com.squareup.okhttp.Call getAuthorizedPermissionEffectivenessValidateBeforeCall(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAuthorizedPermissionEffectiveness(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAuthorizedPermissionEffectiveness(Async)");
        }
        
        com.squareup.okhttp.Call call = getAuthorizedPermissionEffectivenessCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return effectiveness of the business role permission authorizations
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return Permissions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permissions getAuthorizedPermissionEffectiveness(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Permissions> resp = getAuthorizedPermissionEffectivenessWithHttpInfo(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Return effectiveness of the business role permission authorizations
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return ApiResponse&lt;Permissions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permissions> getAuthorizedPermissionEffectivenessWithHttpInfo(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getAuthorizedPermissionEffectivenessValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return effectiveness of the business role permission authorizations (asynchronously)
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedPermissionEffectivenessAsync(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ApiCallback<Permissions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAuthorizedPermissionEffectivenessValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAuthorizedResourceInfo
     * @param uniqueUserId Unique user ID.  If non-null, will return information as it pertains to the user in the latest snapshot. (optional)
     * @param userId Long user ID.  Only used if uniqueUserId is null.  If used, will return information as it pertains to user specified by                          the id .  Use this if you want to get the information for a user in a particular snapshot. (optional)
     * @param uniqueResourceId Unique ID of the resource we are returning information on. (optional)
     * @param resourceId Long ID of the resource we are returning information on.  This will only be used if uniqueResourceId is null. (optional)
     * @param resourceType Type of entity the resource is.  May be PERMISSION, TECHNICAL_ROLE, or ACCOUNT. NOTE: If resourceType is ACCOUNT,                          uniqueResourceId is assumed to be a unique account ID, and will be used to lookup the account and find its associated                          application.  If uniqueResourceId is null and resourceId is non-null, then resourceId is assumed to be the long account                          ID. If uniqueResourceId and resourceId are both null, the appId parameter will be used as the application ID. (optional)
     * @param appId Application ID to be used if resourceType is ACCOUNT and both uniqueResourceId and resourceId are null. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedResourceInfoCall(String uniqueUserId, Long userId, String uniqueResourceId, Long resourceId, String resourceType, Long appId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/authresourceinfo";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (uniqueUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueUserId", uniqueUserId));
        if (userId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("userId", userId));
        if (uniqueResourceId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueResourceId", uniqueResourceId));
        if (resourceId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceId", resourceId));
        if (resourceType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceType", resourceType));
        if (appId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("appId", appId));

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
    private com.squareup.okhttp.Call getAuthorizedResourceInfoValidateBeforeCall(String uniqueUserId, Long userId, String uniqueResourceId, Long resourceId, String resourceType, Long appId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getAuthorizedResourceInfoCall(uniqueUserId, userId, uniqueResourceId, resourceId, resourceType, appId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get information about roles that authorize a resource (permission, technical role, or account) for a given user.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Potential SoD Violation Step Approver * Review Administrator * Review Auditor * Review Owner * Reviewer * Role owner * SoD Step Approver 
     * @param uniqueUserId Unique user ID.  If non-null, will return information as it pertains to the user in the latest snapshot. (optional)
     * @param userId Long user ID.  Only used if uniqueUserId is null.  If used, will return information as it pertains to user specified by                          the id .  Use this if you want to get the information for a user in a particular snapshot. (optional)
     * @param uniqueResourceId Unique ID of the resource we are returning information on. (optional)
     * @param resourceId Long ID of the resource we are returning information on.  This will only be used if uniqueResourceId is null. (optional)
     * @param resourceType Type of entity the resource is.  May be PERMISSION, TECHNICAL_ROLE, or ACCOUNT. NOTE: If resourceType is ACCOUNT,                          uniqueResourceId is assumed to be a unique account ID, and will be used to lookup the account and find its associated                          application.  If uniqueResourceId is null and resourceId is non-null, then resourceId is assumed to be the long account                          ID. If uniqueResourceId and resourceId are both null, the appId parameter will be used as the application ID. (optional)
     * @param appId Application ID to be used if resourceType is ACCOUNT and both uniqueResourceId and resourceId are null. (optional)
     * @return AuthInfoList
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AuthInfoList getAuthorizedResourceInfo(String uniqueUserId, Long userId, String uniqueResourceId, Long resourceId, String resourceType, Long appId) throws ApiException {
        ApiResponse<AuthInfoList> resp = getAuthorizedResourceInfoWithHttpInfo(uniqueUserId, userId, uniqueResourceId, resourceId, resourceType, appId);
        return resp.getData();
    }

    /**
     * Get information about roles that authorize a resource (permission, technical role, or account) for a given user.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Potential SoD Violation Step Approver * Review Administrator * Review Auditor * Review Owner * Reviewer * Role owner * SoD Step Approver 
     * @param uniqueUserId Unique user ID.  If non-null, will return information as it pertains to the user in the latest snapshot. (optional)
     * @param userId Long user ID.  Only used if uniqueUserId is null.  If used, will return information as it pertains to user specified by                          the id .  Use this if you want to get the information for a user in a particular snapshot. (optional)
     * @param uniqueResourceId Unique ID of the resource we are returning information on. (optional)
     * @param resourceId Long ID of the resource we are returning information on.  This will only be used if uniqueResourceId is null. (optional)
     * @param resourceType Type of entity the resource is.  May be PERMISSION, TECHNICAL_ROLE, or ACCOUNT. NOTE: If resourceType is ACCOUNT,                          uniqueResourceId is assumed to be a unique account ID, and will be used to lookup the account and find its associated                          application.  If uniqueResourceId is null and resourceId is non-null, then resourceId is assumed to be the long account                          ID. If uniqueResourceId and resourceId are both null, the appId parameter will be used as the application ID. (optional)
     * @param appId Application ID to be used if resourceType is ACCOUNT and both uniqueResourceId and resourceId are null. (optional)
     * @return ApiResponse&lt;AuthInfoList&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AuthInfoList> getAuthorizedResourceInfoWithHttpInfo(String uniqueUserId, Long userId, String uniqueResourceId, Long resourceId, String resourceType, Long appId) throws ApiException {
        com.squareup.okhttp.Call call = getAuthorizedResourceInfoValidateBeforeCall(uniqueUserId, userId, uniqueResourceId, resourceId, resourceType, appId, null, null);
        Type localVarReturnType = new TypeToken<AuthInfoList>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get information about roles that authorize a resource (permission, technical role, or account) for a given user. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Escalation Reviewer * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Potential SoD Violation Step Approver * Review Administrator * Review Auditor * Review Owner * Reviewer * Role owner * SoD Step Approver 
     * @param uniqueUserId Unique user ID.  If non-null, will return information as it pertains to the user in the latest snapshot. (optional)
     * @param userId Long user ID.  Only used if uniqueUserId is null.  If used, will return information as it pertains to user specified by                          the id .  Use this if you want to get the information for a user in a particular snapshot. (optional)
     * @param uniqueResourceId Unique ID of the resource we are returning information on. (optional)
     * @param resourceId Long ID of the resource we are returning information on.  This will only be used if uniqueResourceId is null. (optional)
     * @param resourceType Type of entity the resource is.  May be PERMISSION, TECHNICAL_ROLE, or ACCOUNT. NOTE: If resourceType is ACCOUNT,                          uniqueResourceId is assumed to be a unique account ID, and will be used to lookup the account and find its associated                          application.  If uniqueResourceId is null and resourceId is non-null, then resourceId is assumed to be the long account                          ID. If uniqueResourceId and resourceId are both null, the appId parameter will be used as the application ID. (optional)
     * @param appId Application ID to be used if resourceType is ACCOUNT and both uniqueResourceId and resourceId are null. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedResourceInfoAsync(String uniqueUserId, Long userId, String uniqueResourceId, Long resourceId, String resourceType, Long appId, final ApiCallback<AuthInfoList> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAuthorizedResourceInfoValidateBeforeCall(uniqueUserId, userId, uniqueResourceId, resourceId, resourceType, appId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AuthInfoList>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAuthorizedRolesEffectiveness
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedRolesEffectivenessCall(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/effectiveness/roles"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
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
    private com.squareup.okhttp.Call getAuthorizedRolesEffectivenessValidateBeforeCall(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAuthorizedRolesEffectiveness(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAuthorizedRolesEffectiveness(Async)");
        }
        
        com.squareup.okhttp.Call call = getAuthorizedRolesEffectivenessCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return effectiveness of the business role technical role authorization.
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getAuthorizedRolesEffectiveness(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Users> resp = getAuthorizedRolesEffectivenessWithHttpInfo(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Return effectiveness of the business role technical role authorization.
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getAuthorizedRolesEffectivenessWithHttpInfo(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getAuthorizedRolesEffectivenessValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return effectiveness of the business role technical role authorization. (asynchronously)
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param ID Business role id (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedRolesEffectivenessAsync(Buroleusers body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAuthorizedRolesEffectivenessValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAutoRequestPolicies
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param autoGrant Flag indicating whether to return business roles that auto-grant one or more resources or business roles that                          auto-revoke one or more resources. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAutoRequestPoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, Boolean autoGrant, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/autorequest";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (autoGrant != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("autoGrant", autoGrant));

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
    private com.squareup.okhttp.Call getAutoRequestPoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, Boolean autoGrant, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAutoRequestPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getAutoRequestPoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, autoGrant, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of published business roles that auto-grant or auto-revoke one or more resources.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param autoGrant Flag indicating whether to return business roles that auto-grant one or more resources or business roles that                          auto-revoke one or more resources. (optional, default to false)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getAutoRequestPolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, Boolean autoGrant) throws ApiException {
        ApiResponse<Roles> resp = getAutoRequestPoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, autoGrant);
        return resp.getData();
    }

    /**
     * Return list of published business roles that auto-grant or auto-revoke one or more resources.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param autoGrant Flag indicating whether to return business roles that auto-grant one or more resources or business roles that                          auto-revoke one or more resources. (optional, default to false)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getAutoRequestPoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, Boolean autoGrant) throws ApiException {
        com.squareup.okhttp.Call call = getAutoRequestPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, autoGrant, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of published business roles that auto-grant or auto-revoke one or more resources. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param autoGrant Flag indicating whether to return business roles that auto-grant one or more resources or business roles that                          auto-revoke one or more resources. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAutoRequestPoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, Boolean autoGrant, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAutoRequestPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, autoGrant, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBRDetectStatus
     * @param ID The business role ID whose detection status we are going to get. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBRDetectStatusCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/detStatus"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getBRDetectStatusValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBRDetectStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getBRDetectStatusCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the detection status for a business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose detection status we are going to get. (required)
     * @return Calculated
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Calculated getBRDetectStatus(Long ID) throws ApiException {
        ApiResponse<Calculated> resp = getBRDetectStatusWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get the detection status for a business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose detection status we are going to get. (required)
     * @return ApiResponse&lt;Calculated&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Calculated> getBRDetectStatusWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getBRDetectStatusValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the detection status for a business role. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose detection status we are going to get. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBRDetectStatusAsync(Long ID, final ApiCallback<Calculated> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBRDetectStatusValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBRLastDetectTime
     * @param ID The business role ID whose last detection time we are going to get. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBRLastDetectTimeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/lastDetTime"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getBRLastDetectTimeValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBRLastDetectTime(Async)");
        }
        
        com.squareup.okhttp.Call call = getBRLastDetectTimeCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the last detection time for a business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose last detection time we are going to get. (required)
     * @return Calculated
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Calculated getBRLastDetectTime(Long ID) throws ApiException {
        ApiResponse<Calculated> resp = getBRLastDetectTimeWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get the last detection time for a business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose last detection time we are going to get. (required)
     * @return ApiResponse&lt;Calculated&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Calculated> getBRLastDetectTimeWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getBRLastDetectTimeValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the last detection time for a business role. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose last detection time we are going to get. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBRLastDetectTimeAsync(Long ID, final ApiCallback<Calculated> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBRLastDetectTimeValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBRUserCount
     * @param ID The business role ID whose user count we are going to get. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBRUserCountCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/userCount"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getBRUserCountValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBRUserCount(Async)");
        }
        
        com.squareup.okhttp.Call call = getBRUserCountCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the user count for a business role.
     * The count is only returned if the business role is published and the business role detection is in a  completed state.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose user count we are going to get. (required)
     * @return Calculated
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Calculated getBRUserCount(Long ID) throws ApiException {
        ApiResponse<Calculated> resp = getBRUserCountWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get the user count for a business role.
     * The count is only returned if the business role is published and the business role detection is in a  completed state.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose user count we are going to get. (required)
     * @return ApiResponse&lt;Calculated&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Calculated> getBRUserCountWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getBRUserCountValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the user count for a business role. (asynchronously)
     * The count is only returned if the business role is published and the business role detection is in a  completed state.&lt;br/&gt;Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner 
     * @param ID The business role ID whose user count we are going to get. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBRUserCountAsync(Long ID, final ApiCallback<Calculated> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBRUserCountValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBrDetectAuths
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param authTypeFilter Type of authorizations to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBrDetectAuthsCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String authTypeFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/detitems/{ID}/auths"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (authTypeFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("authTypeFilter", authTypeFilter));

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
    private com.squareup.okhttp.Call getBrDetectAuthsValidateBeforeCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String authTypeFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBrDetectAuths(Async)");
        }
        
        com.squareup.okhttp.Call call = getBrDetectAuthsCall(ID, size, q, sortBy, sortOrder, indexFrom, authTypeFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of authorizations for a business role detect item.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param authTypeFilter Type of authorizations to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @return Auths
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Auths getBrDetectAuths(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String authTypeFilter) throws ApiException {
        ApiResponse<Auths> resp = getBrDetectAuthsWithHttpInfo(ID, size, q, sortBy, sortOrder, indexFrom, authTypeFilter);
        return resp.getData();
    }

    /**
     * Return list of authorizations for a business role detect item.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param authTypeFilter Type of authorizations to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @return ApiResponse&lt;Auths&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Auths> getBrDetectAuthsWithHttpInfo(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String authTypeFilter) throws ApiException {
        com.squareup.okhttp.Call call = getBrDetectAuthsValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, authTypeFilter, null, null);
        Type localVarReturnType = new TypeToken<Auths>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of authorizations for a business role detect item. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param authTypeFilter Type of authorizations to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBrDetectAuthsAsync(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String authTypeFilter, final ApiCallback<Auths> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBrDetectAuthsValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, authTypeFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Auths>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBrDetectMembers
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param memberTypeFilter Type of memberships to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBrDetectMembersCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String memberTypeFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/detitems/{ID}/members"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (memberTypeFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("memberTypeFilter", memberTypeFilter));

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
    private com.squareup.okhttp.Call getBrDetectMembersValidateBeforeCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String memberTypeFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBrDetectMembers(Async)");
        }
        
        com.squareup.okhttp.Call call = getBrDetectMembersCall(ID, size, q, sortBy, sortOrder, indexFrom, memberTypeFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of members for a business role detect item.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param memberTypeFilter Type of memberships to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @return DetectItems
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DetectItems getBrDetectMembers(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String memberTypeFilter) throws ApiException {
        ApiResponse<DetectItems> resp = getBrDetectMembersWithHttpInfo(ID, size, q, sortBy, sortOrder, indexFrom, memberTypeFilter);
        return resp.getData();
    }

    /**
     * Return list of members for a business role detect item.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param memberTypeFilter Type of memberships to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @return ApiResponse&lt;DetectItems&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DetectItems> getBrDetectMembersWithHttpInfo(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String memberTypeFilter) throws ApiException {
        com.squareup.okhttp.Call call = getBrDetectMembersValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, memberTypeFilter, null, null);
        Type localVarReturnType = new TypeToken<DetectItems>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of members for a business role detect item. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detect item. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param memberTypeFilter Type of memberships to return.  May be ALL, NEW, or DELETED (optional, default to ALL)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBrDetectMembersAsync(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String memberTypeFilter, final ApiCallback<DetectItems> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBrDetectMembersValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, memberTypeFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DetectItems>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBrdAutoRequests
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param autoGrant Flag indicating whether to get auto grant requests (optional, default to true)
     * @param autoRevoke Flag indicating whether to get auto revoke requests (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBrdAutoRequestsCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, Boolean autoGrant, Boolean autoRevoke, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/detections/{ID}/autorequests"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (autoGrant != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("autoGrant", autoGrant));
        if (autoRevoke != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("autoRevoke", autoRevoke));

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
    private com.squareup.okhttp.Call getBrdAutoRequestsValidateBeforeCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, Boolean autoGrant, Boolean autoRevoke, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBrdAutoRequests(Async)");
        }
        
        com.squareup.okhttp.Call call = getBrdAutoRequestsCall(ID, size, q, sortBy, sortOrder, indexFrom, autoGrant, autoRevoke, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of auto-requests for a business role detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param autoGrant Flag indicating whether to get auto grant requests (optional, default to true)
     * @param autoRevoke Flag indicating whether to get auto revoke requests (optional, default to true)
     * @return Auths
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Auths getBrdAutoRequests(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, Boolean autoGrant, Boolean autoRevoke) throws ApiException {
        ApiResponse<Auths> resp = getBrdAutoRequestsWithHttpInfo(ID, size, q, sortBy, sortOrder, indexFrom, autoGrant, autoRevoke);
        return resp.getData();
    }

    /**
     * Return list of auto-requests for a business role detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param autoGrant Flag indicating whether to get auto grant requests (optional, default to true)
     * @param autoRevoke Flag indicating whether to get auto revoke requests (optional, default to true)
     * @return ApiResponse&lt;Auths&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Auths> getBrdAutoRequestsWithHttpInfo(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, Boolean autoGrant, Boolean autoRevoke) throws ApiException {
        com.squareup.okhttp.Call call = getBrdAutoRequestsValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, autoGrant, autoRevoke, null, null);
        Type localVarReturnType = new TypeToken<Auths>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of auto-requests for a business role detection. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param autoGrant Flag indicating whether to get auto grant requests (optional, default to true)
     * @param autoRevoke Flag indicating whether to get auto revoke requests (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBrdAutoRequestsAsync(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, Boolean autoGrant, Boolean autoRevoke, final ApiCallback<Auths> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBrdAutoRequestsValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, autoGrant, autoRevoke, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Auths>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBrdBusinessRoles
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBrdBusinessRolesCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/detections/{ID}/businessroles"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));

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
    private com.squareup.okhttp.Call getBrdBusinessRolesValidateBeforeCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBrdBusinessRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getBrdBusinessRolesCall(ID, size, q, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of business roles for a business role detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return DetectItems
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DetectItems getBrdBusinessRoles(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        ApiResponse<DetectItems> resp = getBrdBusinessRolesWithHttpInfo(ID, size, q, sortBy, sortOrder, indexFrom);
        return resp.getData();
    }

    /**
     * Return list of business roles for a business role detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ApiResponse&lt;DetectItems&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DetectItems> getBrdBusinessRolesWithHttpInfo(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getBrdBusinessRolesValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<DetectItems>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of business roles for a business role detection. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the business role detection. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBrdBusinessRolesAsync(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ApiCallback<DetectItems> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBrdBusinessRolesValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DetectItems>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBusinessRole
     * @param ID The business role ID. (required)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param checkUserId Include a flag on each authorized resource indicating whether the specified user has the resource or not. If this is                          null, no check is made. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleCall(Long ID, List<String> listAttr, List<String> attrFilter, Long checkUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (checkUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("checkUserId", checkUserId));

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
    private com.squareup.okhttp.Call getBusinessRoleValidateBeforeCall(Long ID, List<String> listAttr, List<String> attrFilter, Long checkUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getBusinessRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getBusinessRoleCall(ID, listAttr, attrFilter, checkUserId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business role with the given ID.
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID The business role ID. (required)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param checkUserId Include a flag on each authorized resource indicating whether the specified user has the resource or not. If this is                          null, no check is made. (optional)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole getBusinessRole(Long ID, List<String> listAttr, List<String> attrFilter, Long checkUserId) throws ApiException {
        ApiResponse<Burole> resp = getBusinessRoleWithHttpInfo(ID, listAttr, attrFilter, checkUserId);
        return resp.getData();
    }

    /**
     * Get the business role with the given ID.
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID The business role ID. (required)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param checkUserId Include a flag on each authorized resource indicating whether the specified user has the resource or not. If this is                          null, no check is made. (optional)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> getBusinessRoleWithHttpInfo(Long ID, List<String> listAttr, List<String> attrFilter, Long checkUserId) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRoleValidateBeforeCall(ID, listAttr, attrFilter, checkUserId, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business role with the given ID. (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID The business role ID. (required)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param checkUserId Include a flag on each authorized resource indicating whether the specified user has the resource or not. If this is                          null, no check is made. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleAsync(Long ID, List<String> listAttr, List<String> attrFilter, Long checkUserId, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRoleValidateBeforeCall(ID, listAttr, attrFilter, checkUserId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBusinessRoleDetections
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param detectionFilter Detection filter (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleDetectionsCall(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String detectionFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/detections";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (detectionFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detectionFilter", detectionFilter));

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
    private com.squareup.okhttp.Call getBusinessRoleDetectionsValidateBeforeCall(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String detectionFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getBusinessRoleDetectionsCall(size, q, sortBy, sortOrder, indexFrom, detectionFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of business role detections.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param detectionFilter Detection filter (optional)
     * @return Detections
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Detections getBusinessRoleDetections(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String detectionFilter) throws ApiException {
        ApiResponse<Detections> resp = getBusinessRoleDetectionsWithHttpInfo(size, q, sortBy, sortOrder, indexFrom, detectionFilter);
        return resp.getData();
    }

    /**
     * Return list of business role detections.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param detectionFilter Detection filter (optional)
     * @return ApiResponse&lt;Detections&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Detections> getBusinessRoleDetectionsWithHttpInfo(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String detectionFilter) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRoleDetectionsValidateBeforeCall(size, q, sortBy, sortOrder, indexFrom, detectionFilter, null, null);
        Type localVarReturnType = new TypeToken<Detections>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of business role detections. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param detectionFilter Detection filter (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleDetectionsAsync(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String detectionFilter, final ApiCallback<Detections> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRoleDetectionsValidateBeforeCall(size, q, sortBy, sortOrder, indexFrom, detectionFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Detections>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBusinessRolePolicies
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolePoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (stateFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("stateFilter", stateFilter));
        if (approvalPolicy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approvalPolicy", approvalPolicy));

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
    private com.squareup.okhttp.Call getBusinessRolePoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getBusinessRolePolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getBusinessRolePoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of business roles, also include the information about business role
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getBusinessRolePolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy) throws ApiException {
        ApiResponse<Roles> resp = getBusinessRolePoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy);
        return resp.getData();
    }

    /**
     * Return list of business roles, also include the information about business role
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getBusinessRolePoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRolePoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of business roles, also include the information about business role (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolePoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRolePoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBusinessRolePoliciesById
     * @param body Filter the policies by values of specific fields (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolePoliciesByIdCall(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/byId";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (stateFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("stateFilter", stateFilter));
        if (approvalPolicy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approvalPolicy", approvalPolicy));

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
    private com.squareup.okhttp.Call getBusinessRolePoliciesByIdValidateBeforeCall(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getBusinessRolePoliciesById(Async)");
        }
        
        com.squareup.okhttp.Call call = getBusinessRolePoliciesByIdCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of business roles filtered by id list,if provided.
     * Also include the information about business role&lt;br/&gt;Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getBusinessRolePoliciesById(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy) throws ApiException {
        ApiResponse<Roles> resp = getBusinessRolePoliciesByIdWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy);
        return resp.getData();
    }

    /**
     * Return list of business roles filtered by id list,if provided.
     * Also include the information about business role&lt;br/&gt;Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getBusinessRolePoliciesByIdWithHttpInfo(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRolePoliciesByIdValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of business roles filtered by id list,if provided. (asynchronously)
     * Also include the information about business role&lt;br/&gt;Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Filter the policies by values of specific fields (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter Filter the policies by their approval state. Valid values: DRAFT, REJECTED, PENDING_APPROVAL,  APPROVED, PUBLISHED, ARCHIVE, MINED (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolePoliciesByIdAsync(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRolePoliciesByIdValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContainedPerms
     * @param id ID of the permission (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContainedPermsCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/authorizedperm/{id}/containedperms"
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
    private com.squareup.okhttp.Call getContainedPermsValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getContainedPerms(Async)");
        }
        
        com.squareup.okhttp.Call call = getContainedPermsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of contained permissions for the given permission.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
     * @param id ID of the permission (required)
     * @return Containedperms
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Containedperms getContainedPerms(Long id) throws ApiException {
        ApiResponse<Containedperms> resp = getContainedPermsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Return list of contained permissions for the given permission.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
     * @param id ID of the permission (required)
     * @return ApiResponse&lt;Containedperms&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Containedperms> getContainedPermsWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getContainedPermsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Containedperms>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of contained permissions for the given permission. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
     * @param id ID of the permission (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContainedPermsAsync(Long id, final ApiCallback<Containedperms> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContainedPermsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Containedperms>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContainedRolePerms
     * @param id ID of the technical role (required)
     * @param getIndirectPerms Flag indicating whether to get indirect permissions. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContainedRolePermsCall(Long id, Boolean getIndirectPerms, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/authorizedrole/{id}/containedperms"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (getIndirectPerms != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getIndirectPerms", getIndirectPerms));

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
    private com.squareup.okhttp.Call getContainedRolePermsValidateBeforeCall(Long id, Boolean getIndirectPerms, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getContainedRolePerms(Async)");
        }
        
        com.squareup.okhttp.Call call = getContainedRolePermsCall(id, getIndirectPerms, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of contained permissions for the given technical role.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
     * @param id ID of the technical role (required)
     * @param getIndirectPerms Flag indicating whether to get indirect permissions. (optional, default to false)
     * @return Containedperms
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Containedperms getContainedRolePerms(Long id, Boolean getIndirectPerms) throws ApiException {
        ApiResponse<Containedperms> resp = getContainedRolePermsWithHttpInfo(id, getIndirectPerms);
        return resp.getData();
    }

    /**
     * Return list of contained permissions for the given technical role.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
     * @param id ID of the technical role (required)
     * @param getIndirectPerms Flag indicating whether to get indirect permissions. (optional, default to false)
     * @return ApiResponse&lt;Containedperms&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Containedperms> getContainedRolePermsWithHttpInfo(Long id, Boolean getIndirectPerms) throws ApiException {
        com.squareup.okhttp.Call call = getContainedRolePermsValidateBeforeCall(id, getIndirectPerms, null, null);
        Type localVarReturnType = new TypeToken<Containedperms>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of contained permissions for the given technical role. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
     * @param id ID of the technical role (required)
     * @param getIndirectPerms Flag indicating whether to get indirect permissions. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContainedRolePermsAsync(Long id, Boolean getIndirectPerms, final ApiCallback<Containedperms> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContainedRolePermsValidateBeforeCall(id, getIndirectPerms, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Containedperms>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getInconsistencyDetections
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getInconsistencyDetectionsCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/inconsistencydetections";

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
    private com.squareup.okhttp.Call getInconsistencyDetectionsValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getInconsistencyDetectionsCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of inconsistency detections
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @return Detetions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Detetions getInconsistencyDetections() throws ApiException {
        ApiResponse<Detetions> resp = getInconsistencyDetectionsWithHttpInfo();
        return resp.getData();
    }

    /**
     * Return list of inconsistency detections
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @return ApiResponse&lt;Detetions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Detetions> getInconsistencyDetectionsWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getInconsistencyDetectionsValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Detetions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of inconsistency detections (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getInconsistencyDetectionsAsync(final ApiCallback<Detetions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getInconsistencyDetectionsValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Detetions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getInconsistentResource
     * @param ID ID of the inconsistent resource to be retrieved. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getInconsistentResourceCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/inconsistentresource/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getInconsistentResourceValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getInconsistentResource(Async)");
        }
        
        com.squareup.okhttp.Call call = getInconsistentResourceCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return additional information about an inconsistent resource
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the inconsistent resource to be retrieved. (required)
     * @return Resource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Resource getInconsistentResource(Long ID) throws ApiException {
        ApiResponse<Resource> resp = getInconsistentResourceWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Return additional information about an inconsistent resource
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the inconsistent resource to be retrieved. (required)
     * @return ApiResponse&lt;Resource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Resource> getInconsistentResourceWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getInconsistentResourceValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Resource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return additional information about an inconsistent resource (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the inconsistent resource to be retrieved. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getInconsistentResourceAsync(Long ID, final ApiCallback<Resource> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getInconsistentResourceValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Resource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getInconsistentResourcesFromDetection
     * @param ID ID of the inconsistency detection to retrieve inconsistent resources from (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getInconsistentResourcesFromDetectionCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/inconsistentresources/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));

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
    private com.squareup.okhttp.Call getInconsistentResourcesFromDetectionValidateBeforeCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getInconsistentResourcesFromDetection(Async)");
        }
        
        com.squareup.okhttp.Call call = getInconsistentResourcesFromDetectionCall(ID, size, q, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of user+resources whose assignments are inconsistent with either auto-grant or auto-revoke policy of business role authorizations.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the inconsistency detection to retrieve inconsistent resources from (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return Resources
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Resources getInconsistentResourcesFromDetection(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        ApiResponse<Resources> resp = getInconsistentResourcesFromDetectionWithHttpInfo(ID, size, q, sortBy, sortOrder, indexFrom);
        return resp.getData();
    }

    /**
     * Return list of user+resources whose assignments are inconsistent with either auto-grant or auto-revoke policy of business role authorizations.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the inconsistency detection to retrieve inconsistent resources from (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ApiResponse&lt;Resources&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Resources> getInconsistentResourcesFromDetectionWithHttpInfo(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getInconsistentResourcesFromDetectionValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<Resources>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of user+resources whose assignments are inconsistent with either auto-grant or auto-revoke policy of business role authorizations. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID ID of the inconsistency detection to retrieve inconsistent resources from (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all fields (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getInconsistentResourcesFromDetectionAsync(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ApiCallback<Resources> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getInconsistentResourcesFromDetectionValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Resources>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getManagers
     * @param ID The business role ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getManagersCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/managers"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getManagersValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getManagers(Async)");
        }
        
        com.squareup.okhttp.Call call = getManagersCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business role managers.
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getManagers(Long ID) throws ApiException {
        ApiResponse<Response> resp = getManagersWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get the business role managers.
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getManagersWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getManagersValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business role managers. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getManagersAsync(Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getManagersValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMatchingUsers
     * @param body Advanced search criteria for users.  This will be ANDed with the membership criteria of the business role and
                                   the quick search filter. (required)
     * @param ID The ID of the business role. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the                                    membership criteria of the business role and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersCall(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/users"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
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
    private com.squareup.okhttp.Call getMatchingUsersValidateBeforeCall(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getMatchingUsers(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getMatchingUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = getMatchingUsersCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of users that match the membership criteria for the specified business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users.  This will be ANDed with the membership criteria of the business role and
                                   the quick search filter. (required)
     * @param ID The ID of the business role. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the                                    membership criteria of the business role and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getMatchingUsers(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Users> resp = getMatchingUsersWithHttpInfo(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Return list of users that match the membership criteria for the specified business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users.  This will be ANDed with the membership criteria of the business role and
                                   the quick search filter. (required)
     * @param ID The ID of the business role. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the                                    membership criteria of the business role and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getMatchingUsersWithHttpInfo(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getMatchingUsersValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of users that match the membership criteria for the specified business role. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Escalation Reviewer * Historical Review Owner * Historical Reviewer * Review Administrator * Review Auditor * Review Owner * Reviewer * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users.  This will be ANDed with the membership criteria of the business role and
                                   the quick search filter. (required)
     * @param ID The ID of the business role. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the                                    membership criteria of the business role and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersAsync(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMatchingUsersValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMatchingUsers2
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsers2Call(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/matchingusers";

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (useAllCriteria != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("useAllCriteria", useAllCriteria));

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
    private com.squareup.okhttp.Call getMatchingUsers2ValidateBeforeCall(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getMatchingUsers2(Async)");
        }
        
        com.squareup.okhttp.Call call = getMatchingUsers2Call(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of users that match the membership criteria for a business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getMatchingUsers2(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        ApiResponse<Users> resp = getMatchingUsers2WithHttpInfo(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria);
        return resp.getData();
    }

    /**
     * Return list of users that match the membership criteria for a business role.
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getMatchingUsers2WithHttpInfo(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        com.squareup.okhttp.Call call = getMatchingUsers2ValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of users that match the membership criteria for a business role. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsers2Async(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMatchingUsers2ValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMatchingUsersApplications
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersApplicationsCall(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/matchingusers/apps";

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (useAllCriteria != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("useAllCriteria", useAllCriteria));

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
    private com.squareup.okhttp.Call getMatchingUsersApplicationsValidateBeforeCall(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getMatchingUsersApplications(Async)");
        }
        
        com.squareup.okhttp.Call call = getMatchingUsersApplicationsCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of applications for users that match the membership criteria for a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getMatchingUsersApplications(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        ApiResponse<Users> resp = getMatchingUsersApplicationsWithHttpInfo(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria);
        return resp.getData();
    }

    /**
     * Return list of applications for users that match the membership criteria for a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getMatchingUsersApplicationsWithHttpInfo(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        com.squareup.okhttp.Call call = getMatchingUsersApplicationsValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of applications for users that match the membership criteria for a business role. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersApplicationsAsync(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMatchingUsersApplicationsValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMatchingUsersPermissions
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersPermissionsCall(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/matchingusers/permissions";

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (useAllCriteria != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("useAllCriteria", useAllCriteria));

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
    private com.squareup.okhttp.Call getMatchingUsersPermissionsValidateBeforeCall(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getMatchingUsersPermissions(Async)");
        }
        
        com.squareup.okhttp.Call call = getMatchingUsersPermissionsCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of permissions for users that match the membership criteria for a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getMatchingUsersPermissions(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        ApiResponse<Users> resp = getMatchingUsersPermissionsWithHttpInfo(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria);
        return resp.getData();
    }

    /**
     * Return list of permissions for users that match the membership criteria for a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getMatchingUsersPermissionsWithHttpInfo(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        com.squareup.okhttp.Call call = getMatchingUsersPermissionsValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of permissions for users that match the membership criteria for a business role. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersPermissionsAsync(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMatchingUsersPermissionsValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMatchingUsersRoles
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersRolesCall(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/matchingusers/roles";

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
        if (showUnfilteredCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showUnfilteredCt", showUnfilteredCt));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (useAllCriteria != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("useAllCriteria", useAllCriteria));

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
    private com.squareup.okhttp.Call getMatchingUsersRolesValidateBeforeCall(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getMatchingUsersRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getMatchingUsersRolesCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of technical roles for users that match the membership criteria for a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getMatchingUsersRoles(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        ApiResponse<Users> resp = getMatchingUsersRolesWithHttpInfo(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria);
        return resp.getData();
    }

    /**
     * Return list of technical roles for users that match the membership criteria for a business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getMatchingUsersRolesWithHttpInfo(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria) throws ApiException {
        com.squareup.okhttp.Call call = getMatchingUsersRolesValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of technical roles for users that match the membership criteria for a business role. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body Advanced search criteria for users as well as business role criteria nodes that we want to match against. These
                              criteria will be ANDed with each other as well as the quick search filter, if any. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showUnfilteredCt Show unfiltered count. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all quick search fields. This criteria will be ANDed with the role                               criteria nodes and the advanced search criteria, if any. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param useAllCriteria Use all membership criteria, including any that might be expired. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMatchingUsersRolesAsync(Buroleusers body, String sortBy, String sortOrder, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, Boolean showUnfilteredCt, Boolean showCt, String q, String qMatch, Boolean useAllCriteria, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMatchingUsersRolesValidateBeforeCall(body, sortBy, sortOrder, listAttr, attrFilter, indexFrom, size, showUnfilteredCt, showCt, q, qMatch, useAllCriteria, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMiningSuggestions
     * @param id - The mining id (required)
     * @param mode - Mining suggestion type (ATTR, AUTO, AUTO2) (optional, default to ATTR)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMiningSuggestionsCall(String id, String mode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/mine/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (mode != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("mode", mode));

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
    private com.squareup.okhttp.Call getMiningSuggestionsValidateBeforeCall(String id, String mode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getMiningSuggestions(Async)");
        }
        
        com.squareup.okhttp.Call call = getMiningSuggestionsCall(id, mode, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Check the mining suggestions for the given id
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The mining id (required)
     * @param mode - Mining suggestion type (ATTR, AUTO, AUTO2) (optional, default to ATTR)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getMiningSuggestions(String id, String mode) throws ApiException {
        ApiResponse<Response> resp = getMiningSuggestionsWithHttpInfo(id, mode);
        return resp.getData();
    }

    /**
     * Check the mining suggestions for the given id
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The mining id (required)
     * @param mode - Mining suggestion type (ATTR, AUTO, AUTO2) (optional, default to ATTR)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getMiningSuggestionsWithHttpInfo(String id, String mode) throws ApiException {
        com.squareup.okhttp.Call call = getMiningSuggestionsValidateBeforeCall(id, mode, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Check the mining suggestions for the given id (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The mining id (required)
     * @param mode - Mining suggestion type (ATTR, AUTO, AUTO2) (optional, default to ATTR)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMiningSuggestionsAsync(String id, String mode, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMiningSuggestionsValidateBeforeCall(id, mode, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getOwners
     * @param ID The business role ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getOwnersCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/owners"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getOwnersValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getOwners(Async)");
        }
        
        com.squareup.okhttp.Call call = getOwnersCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business role owners.
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getOwners(Long ID) throws ApiException {
        ApiResponse<Response> resp = getOwnersWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get the business role owners.
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getOwnersWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getOwnersValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business role owners. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getOwnersAsync(Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getOwnersValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPendingApprovals
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPendingApprovalsCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/pendingApproval";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
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
    private com.squareup.okhttp.Call getPendingApprovalsValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getPendingApprovals(Async)");
        }
        
        com.squareup.okhttp.Call call = getPendingApprovalsCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of business roles pending approval of the logged in user.
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getPendingApprovals(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch) throws ApiException {
        ApiResponse<Roles> resp = getPendingApprovalsWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch);
        return resp.getData();
    }

    /**
     * Return list of business roles pending approval of the logged in user.
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getPendingApprovalsWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getPendingApprovalsValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of business roles pending approval of the logged in user. (asynchronously)
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPendingApprovalsAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPendingApprovalsValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRoleSods
     * @param id - id of the role (required)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default:        false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRoleSodsCall(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{id}/sods"
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
    private com.squareup.okhttp.Call getRoleSodsValidateBeforeCall(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getRoleSods(Async)");
        }
        
        com.squareup.okhttp.Call call = getRoleSodsCall(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SoD policies that reference the specified role
     * Accepted Roles: * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param id - id of the role (required)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default:        false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies getRoleSods(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Policies> resp = getRoleSodsWithHttpInfo(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the SoD policies that reference the specified role
     * Accepted Roles: * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param id - id of the role (required)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default:        false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @return ApiResponse&lt;Policies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Policies> getRoleSodsWithHttpInfo(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getRoleSodsValidateBeforeCall(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SoD policies that reference the specified role (asynchronously)
     * Accepted Roles: * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param id - id of the role (required)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.        totalSearch:10) of rows in the query. You should only set                          this true on the initial call to obtain the total size of the search        result list. Otherwise there will be                          duplicate queries made, one        to obtain the result set and the other to get the count. Default:        false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string        allowable values: START, ANY, EXACT, where EXACT &#x3D;                          exact match,        ANY &#x3D; match any place in string, START &#x3D; match on starting        character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRoleSodsAsync(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Policies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRoleSodsValidateBeforeCall(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getfulfiller
     * @param ID The business role ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getfulfillerCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/fulfiller"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getfulfillerValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getfulfiller(Async)");
        }
        
        com.squareup.okhttp.Call call = getfulfillerCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business role fulfiller.
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getfulfiller(Long ID) throws ApiException {
        ApiResponse<Response> resp = getfulfillerWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get the business role fulfiller.
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getfulfillerWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getfulfillerValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business role fulfiller. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The business role ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getfulfillerAsync(Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getfulfillerValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZip
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipCall(File uploadedInputStream, Boolean refs, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/import";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (reportOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reportOnly", reportOnly));

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
    private com.squareup.okhttp.Call importZipValidateBeforeCall(File uploadedInputStream, Boolean refs, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZip(Async)");
        }
        
        com.squareup.okhttp.Call call = importZipCall(uploadedInputStream, refs, reportOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip(File uploadedInputStream, Boolean refs, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importZipWithHttpInfo(uploadedInputStream, refs, reportOnly);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZipWithHttpInfo(File uploadedInputStream, Boolean refs, Boolean reportOnly) throws ApiException {
        com.squareup.okhttp.Call call = importZipValidateBeforeCall(uploadedInputStream, refs, reportOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZipAsync(File uploadedInputStream, Boolean refs, Boolean reportOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZipValidateBeforeCall(uploadedInputStream, refs, reportOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZipPreview
     * @param uploadedInputStream  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipPreviewCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/import/preview";

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
    private com.squareup.okhttp.Call importZipPreviewValidateBeforeCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZipPreview(Async)");
        }
        
        com.squareup.okhttp.Call call = importZipPreviewCall(uploadedInputStream, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZipPreview(File uploadedInputStream) throws ApiException {
        ApiResponse<Response> resp = importZipPreviewWithHttpInfo(uploadedInputStream);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZipPreviewWithHttpInfo(File uploadedInputStream) throws ApiException {
        com.squareup.okhttp.Call call = importZipPreviewValidateBeforeCall(uploadedInputStream, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZipPreviewAsync(File uploadedInputStream, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZipPreviewValidateBeforeCall(uploadedInputStream, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/brole/import/resolve/apps";

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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
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
     * Build call for includeReferences
     * @param ID - The analysis id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call includeReferencesCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{ID}/references"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call includeReferencesValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling includeReferences(Async)");
        }
        
        com.squareup.okhttp.Call call = includeReferencesCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of business roles that reference the business role specified by the given id
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID - The analysis id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response includeReferences(Long ID) throws ApiException {
        ApiResponse<Response> resp = includeReferencesWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get the list of business roles that reference the business role specified by the given id
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID - The analysis id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> includeReferencesWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = includeReferencesValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of business roles that reference the business role specified by the given id (asynchronously)
     * Accepted Roles: * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID - The analysis id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call includeReferencesAsync(Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = includeReferencesValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for mineRoles
     * @param body - The mining attribute data and occurrence limit (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param async - flag to indicate if mining should be done asynchronously or in line (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call mineRolesCall(Mine body, Integer size, String sortBy, String sortOrder, Integer indexFrom, Boolean showCt, String mode, Boolean async, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/mine";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (mode != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("mode", mode));
        if (async != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("async", async));

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
    private com.squareup.okhttp.Call mineRolesValidateBeforeCall(Mine body, Integer size, String sortBy, String sortOrder, Integer indexFrom, Boolean showCt, String mode, Boolean async, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling mineRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = mineRolesCall(body, size, sortBy, sortOrder, indexFrom, showCt, mode, async, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform a query of the user catalog, mining values of the specified attributes.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The mining attribute data and occurrence limit (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param async - flag to indicate if mining should be done asynchronously or in line (optional, default to true)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response mineRoles(Mine body, Integer size, String sortBy, String sortOrder, Integer indexFrom, Boolean showCt, String mode, Boolean async) throws ApiException {
        ApiResponse<Response> resp = mineRolesWithHttpInfo(body, size, sortBy, sortOrder, indexFrom, showCt, mode, async);
        return resp.getData();
    }

    /**
     * Perform a query of the user catalog, mining values of the specified attributes.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The mining attribute data and occurrence limit (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param async - flag to indicate if mining should be done asynchronously or in line (optional, default to true)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> mineRolesWithHttpInfo(Mine body, Integer size, String sortBy, String sortOrder, Integer indexFrom, Boolean showCt, String mode, Boolean async) throws ApiException {
        com.squareup.okhttp.Call call = mineRolesValidateBeforeCall(body, size, sortBy, sortOrder, indexFrom, showCt, mode, async, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform a query of the user catalog, mining values of the specified attributes. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The mining attribute data and occurrence limit (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param mode - Mining mode - ATTR (Selected Attributes) or AUTO (Selected analytics) (optional, default to ATTR)
     * @param async - flag to indicate if mining should be done asynchronously or in line (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call mineRolesAsync(Mine body, Integer size, String sortBy, String sortOrder, Integer indexFrom, Boolean showCt, String mode, Boolean async, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = mineRolesValidateBeforeCall(body, size, sortBy, sortOrder, indexFrom, showCt, mode, async, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for miningStatus
     * @param id - The mining id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call miningStatusCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/mine/{id}/status"
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
    private com.squareup.okhttp.Call miningStatusValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling miningStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = miningStatusCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Check the status of a mining request
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The mining id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response miningStatus(String id) throws ApiException {
        ApiResponse<Response> resp = miningStatusWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Check the status of a mining request
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The mining id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> miningStatusWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = miningStatusValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Check the status of a mining request (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The mining id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call miningStatusAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = miningStatusValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for publishBusinessRoles
     * @param body - The business role managers to update. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call publishBusinessRolesCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalState";

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
    private com.squareup.okhttp.Call publishBusinessRolesValidateBeforeCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling publishBusinessRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = publishBusinessRolesCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Publish, submit for approval or promote the given business roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The business role managers to update. (required)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole publishBusinessRoles(Auths body) throws ApiException {
        ApiResponse<Burole> resp = publishBusinessRolesWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Publish, submit for approval or promote the given business roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The business role managers to update. (required)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> publishBusinessRolesWithHttpInfo(Auths body) throws ApiException {
        com.squareup.okhttp.Call call = publishBusinessRolesValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Publish, submit for approval or promote the given business roles (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The business role managers to update. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call publishBusinessRolesAsync(Auths body, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = publishBusinessRolesValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setApprovalPolicy
     * @param body - The business role managers to update. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setApprovalPolicyCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approval";

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
    private com.squareup.okhttp.Call setApprovalPolicyValidateBeforeCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = setApprovalPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the business role approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The business role managers to update. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response setApprovalPolicy(Auths body) throws ApiException {
        ApiResponse<Response> resp = setApprovalPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Set the business role approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The business role managers to update. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> setApprovalPolicyWithHttpInfo(Auths body) throws ApiException {
        com.squareup.okhttp.Call call = setApprovalPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the business role approval policy. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The business role managers to update. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setApprovalPolicyAsync(Auths body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setApprovalPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setApprovalState
     * @param body The updates to the business role. (required)
     * @param id The business role ID. (required)
     * @param discardDraft Flag to indicate whether discard the draft or published version when  transitioning from published to approved and there is                      a working draft of the business role. (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setApprovalStateCall(Burole body, Long id, Boolean discardDraft, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{id}/approvalState"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (discardDraft != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("discardDraft", discardDraft));

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
    private com.squareup.okhttp.Call setApprovalStateValidateBeforeCall(Burole body, Long id, Boolean discardDraft, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setApprovalState(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling setApprovalState(Async)");
        }
        
        com.squareup.okhttp.Call call = setApprovalStateCall(body, id, discardDraft, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Change business role&#x27;s approval state.
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the business role. (required)
     * @param id The business role ID. (required)
     * @param discardDraft Flag to indicate whether discard the draft or published version when  transitioning from published to approved and there is                      a working draft of the business role. (optional, default to true)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole setApprovalState(Burole body, Long id, Boolean discardDraft) throws ApiException {
        ApiResponse<Burole> resp = setApprovalStateWithHttpInfo(body, id, discardDraft);
        return resp.getData();
    }

    /**
     * Change business role&#x27;s approval state.
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the business role. (required)
     * @param id The business role ID. (required)
     * @param discardDraft Flag to indicate whether discard the draft or published version when  transitioning from published to approved and there is                      a working draft of the business role. (optional, default to true)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> setApprovalStateWithHttpInfo(Burole body, Long id, Boolean discardDraft) throws ApiException {
        com.squareup.okhttp.Call call = setApprovalStateValidateBeforeCall(body, id, discardDraft, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Change business role&#x27;s approval state. (asynchronously)
     * Accepted Roles: * Business Role Approver * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the business role. (required)
     * @param id The business role ID. (required)
     * @param discardDraft Flag to indicate whether discard the draft or published version when  transitioning from published to approved and there is                      a working draft of the business role. (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setApprovalStateAsync(Burole body, Long id, Boolean discardDraft, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setApprovalStateValidateBeforeCall(body, id, discardDraft, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setFulfiller
     * @param body The business role fulfiller. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setFulfillerCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/fulfiller";

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
    private com.squareup.okhttp.Call setFulfillerValidateBeforeCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setFulfiller(Async)");
        }
        
        com.squareup.okhttp.Call call = setFulfillerCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the business role fulfiller.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role fulfiller. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response setFulfiller(Auths body) throws ApiException {
        ApiResponse<Response> resp = setFulfillerWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Set the business role fulfiller.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role fulfiller. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> setFulfillerWithHttpInfo(Auths body) throws ApiException {
        com.squareup.okhttp.Call call = setFulfillerValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the business role fulfiller. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role fulfiller. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setFulfillerAsync(Auths body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setFulfillerValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setManagers
     * @param body The business role managers to update. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setManagersCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/managers";

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
    private com.squareup.okhttp.Call setManagersValidateBeforeCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setManagers(Async)");
        }
        
        com.squareup.okhttp.Call call = setManagersCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the business role managers.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role managers to update. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response setManagers(Auths body) throws ApiException {
        ApiResponse<Response> resp = setManagersWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Set the business role managers.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role managers to update. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> setManagersWithHttpInfo(Auths body) throws ApiException {
        com.squareup.okhttp.Call call = setManagersValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the business role managers. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role managers to update. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setManagersAsync(Auths body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setManagersValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setOwners
     * @param body The business role owners. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setOwnersCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/owners";

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
    private com.squareup.okhttp.Call setOwnersValidateBeforeCall(Auths body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setOwners(Async)");
        }
        
        com.squareup.okhttp.Call call = setOwnersCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the business role owners.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role owners. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response setOwners(Auths body) throws ApiException {
        ApiResponse<Response> resp = setOwnersWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Set the business role owners.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role owners. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> setOwnersWithHttpInfo(Auths body) throws ApiException {
        com.squareup.okhttp.Call call = setOwnersValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the business role owners. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body The business role owners. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setOwnersAsync(Auths body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setOwnersValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startAnalysis
     * @param body - The analysis request information (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startAnalysisCall(Analyze body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze";

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
    private com.squareup.okhttp.Call startAnalysisValidateBeforeCall(Analyze body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startAnalysis(Async)");
        }
        
        com.squareup.okhttp.Call call = startAnalysisCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start analysis of the given Business Roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The analysis request information (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startAnalysis(Analyze body) throws ApiException {
        ApiResponse<Response> resp = startAnalysisWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Start analysis of the given Business Roles
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The analysis request information (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startAnalysisWithHttpInfo(Analyze body) throws ApiException {
        com.squareup.okhttp.Call call = startAnalysisValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start analysis of the given Business Roles (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The analysis request information (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startAnalysisAsync(Analyze body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startAnalysisValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(String id, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze/download/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(String id, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling startDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownloadCall(id, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start downloading the analysis results as a CSV
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(String id, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(id, attrFilter);
        return resp.getData();
    }

    /**
     * Start downloading the analysis results as a CSV
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(String id, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start downloading the analysis results as a CSV (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(String id, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_0
     * @param body -  The download request node (required)
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Call(Download body, String id, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/analyze/download/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call startDownload_0ValidateBeforeCall(Download body, String id, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_0(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling startDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_0Call(body, id, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start downloading the analysis results as a CSV
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body -  The download request node (required)
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body, String id, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body, id, attrFilter);
        return resp.getData();
    }

    /**
     * Start downloading the analysis results as a CSV
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body -  The download request node (required)
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_0WithHttpInfo(Download body, String id, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, id, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start downloading the analysis results as a CSV (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body -  The download request node (required)
     * @param id - The analysis id to download (required)
     * @param attrFilter List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Async(Download body, String id, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, id, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_1
     * @param id - The business roles to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_1Call(List<Long> id, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
        if (roles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roles", roles));
        if (approval != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approval", approval));
        if (cats != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("cats", cats));
        if (broles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("broles", broles));
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
    private com.squareup.okhttp.Call startDownload_1ValidateBeforeCall(List<Long> id, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startDownload_1Call(id, refs, apps, roles, approval, cats, broles, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start downloading the Business Roles
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The business roles to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_1(List<Long> id, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownload_1WithHttpInfo(id, refs, apps, roles, approval, cats, broles, attrFilter);
        return resp.getData();
    }

    /**
     * Start downloading the Business Roles
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The business roles to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_1WithHttpInfo(List<Long> id, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_1ValidateBeforeCall(id, refs, apps, roles, approval, cats, broles, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start downloading the Business Roles (asynchronously)
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The business roles to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_1Async(List<Long> id, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_1ValidateBeforeCall(id, refs, apps, roles, approval, cats, broles, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_2
     * @param body - The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_2Call(Download body, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
        if (roles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roles", roles));
        if (approval != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approval", approval));
        if (cats != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("cats", cats));
        if (broles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("broles", broles));
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
    private com.squareup.okhttp.Call startDownload_2ValidateBeforeCall(Download body, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_2(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_2Call(body, refs, apps, roles, approval, cats, broles, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start downloading the Business Roles
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_2(Download body, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownload_2WithHttpInfo(body, refs, apps, roles, approval, cats, broles, attrFilter);
        return resp.getData();
    }

    /**
     * Start downloading the Business Roles
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_2WithHttpInfo(Download body, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_2ValidateBeforeCall(body, refs, apps, roles, approval, cats, broles, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start downloading the Business Roles (asynchronously)
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param body - The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param apps - Flag to export applications referenced by the business role (optional, default to false)
     * @param roles - Flag to export technical roles referenced by the business role (optional, default to false)
     * @param approval - Flag to export assigned approval policies (optional, default to false)
     * @param cats - Flag to export entity categories (optional, default to false)
     * @param broles - Flag to export included business roles (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_2Async(Download body, Boolean refs, Boolean apps, Boolean roles, Boolean approval, Boolean cats, Boolean broles, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_2ValidateBeforeCall(body, refs, apps, roles, approval, cats, broles, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startInconsistencyDetection
     * @param resourceType Type of resource to start an inconsistency detection on. (optional)
     * @param autoGrant Flag indicating whether to start the detection for auto-grants (true) or auto-revokes (false) (optional, default to true)
     * @param prevMembWindowDays Previous member window days - used only for auto-revoke detections. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startInconsistencyDetectionCall(String resourceType, Boolean autoGrant, Integer prevMembWindowDays, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/inconsistencydetections/new";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceType", resourceType));
        if (autoGrant != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("autoGrant", autoGrant));
        if (prevMembWindowDays != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prevMembWindowDays", prevMembWindowDays));

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
    private com.squareup.okhttp.Call startInconsistencyDetectionValidateBeforeCall(String resourceType, Boolean autoGrant, Integer prevMembWindowDays, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startInconsistencyDetectionCall(resourceType, autoGrant, prevMembWindowDays, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start an inconsistency detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param resourceType Type of resource to start an inconsistency detection on. (optional)
     * @param autoGrant Flag indicating whether to start the detection for auto-grants (true) or auto-revokes (false) (optional, default to true)
     * @param prevMembWindowDays Previous member window days - used only for auto-revoke detections. (optional, default to 0)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status startInconsistencyDetection(String resourceType, Boolean autoGrant, Integer prevMembWindowDays) throws ApiException {
        ApiResponse<Status> resp = startInconsistencyDetectionWithHttpInfo(resourceType, autoGrant, prevMembWindowDays);
        return resp.getData();
    }

    /**
     * Start an inconsistency detection.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param resourceType Type of resource to start an inconsistency detection on. (optional)
     * @param autoGrant Flag indicating whether to start the detection for auto-grants (true) or auto-revokes (false) (optional, default to true)
     * @param prevMembWindowDays Previous member window days - used only for auto-revoke detections. (optional, default to 0)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> startInconsistencyDetectionWithHttpInfo(String resourceType, Boolean autoGrant, Integer prevMembWindowDays) throws ApiException {
        com.squareup.okhttp.Call call = startInconsistencyDetectionValidateBeforeCall(resourceType, autoGrant, prevMembWindowDays, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start an inconsistency detection. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator 
     * @param resourceType Type of resource to start an inconsistency detection on. (optional)
     * @param autoGrant Flag indicating whether to start the detection for auto-grants (true) or auto-revokes (false) (optional, default to true)
     * @param prevMembWindowDays Previous member window days - used only for auto-revoke detections. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startInconsistencyDetectionAsync(String resourceType, Boolean autoGrant, Integer prevMembWindowDays, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startInconsistencyDetectionValidateBeforeCall(resourceType, autoGrant, prevMembWindowDays, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
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
        String localVarPath = "/policy/brole/download/{id}/status"
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Build call for updateBusinessRole
     * @param body The updates to the business role. (required)
     * @param id The business role ID (required)
     * @param manage Flag to indicate whether to save the business role data or the owners and administrator data. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateBusinessRoleCall(Burole body, Long id, Boolean manage, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (manage != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("manage", manage));

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
    private com.squareup.okhttp.Call updateBusinessRoleValidateBeforeCall(Burole body, Long id, Boolean manage, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateBusinessRole(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateBusinessRole(Async)");
        }
        
        com.squareup.okhttp.Call call = updateBusinessRoleCall(body, id, manage, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the business role. (required)
     * @param id The business role ID (required)
     * @param manage Flag to indicate whether to save the business role data or the owners and administrator data. (optional, default to false)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole updateBusinessRole(Burole body, Long id, Boolean manage) throws ApiException {
        ApiResponse<Burole> resp = updateBusinessRoleWithHttpInfo(body, id, manage);
        return resp.getData();
    }

    /**
     * Update business role.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the business role. (required)
     * @param id The business role ID (required)
     * @param manage Flag to indicate whether to save the business role data or the owners and administrator data. (optional, default to false)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> updateBusinessRoleWithHttpInfo(Burole body, Long id, Boolean manage) throws ApiException {
        com.squareup.okhttp.Call call = updateBusinessRoleValidateBeforeCall(body, id, manage, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update business role. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the business role. (required)
     * @param id The business role ID (required)
     * @param manage Flag to indicate whether to save the business role data or the owners and administrator data. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateBusinessRoleAsync(Burole body, Long id, Boolean manage, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateBusinessRoleValidateBeforeCall(body, id, manage, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
