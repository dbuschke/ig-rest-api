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


import io.swagger.client.model.Approval;
import io.swagger.client.model.Associations;
import io.swagger.client.model.Delegate;
import io.swagger.client.model.Entities;
import io.swagger.client.model.EntityItem;
import io.swagger.client.model.Groups;
import io.swagger.client.model.Response;
import io.swagger.client.model.UserEmailNode;
import io.swagger.client.model.UserInfo;
import io.swagger.client.model.Whoami;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WfiApi {
    private ApiClient apiClient;

    public WfiApi() {
        this(Configuration.getDefaultApiClient());
    }

    public WfiApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for executePermissionMethod
     * @param method the method (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call executePermissionMethodCall(String method, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/permission/{method}"
            .replaceAll("\\{" + "method" + "\\}", apiClient.escapeString(method.toString()));

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
    private com.squareup.okhttp.Call executePermissionMethodValidateBeforeCall(String method, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'method' is set
        if (method == null) {
            throw new ApiException("Missing the required parameter 'method' when calling executePermissionMethod(Async)");
        }
        
        com.squareup.okhttp.Call call = executePermissionMethodCall(method, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param method the method (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response executePermissionMethod(String method) throws ApiException {
        ApiResponse<Response> resp = executePermissionMethodWithHttpInfo(method);
        return resp.getData();
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param method the method (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> executePermissionMethodWithHttpInfo(String method) throws ApiException {
        com.squareup.okhttp.Call call = executePermissionMethodValidateBeforeCall(method, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param method the method (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call executePermissionMethodAsync(String method, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = executePermissionMethodValidateBeforeCall(method, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for executeUserMethod
     * @param method the method (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call executeUserMethodCall(String method, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/user/{method}"
            .replaceAll("\\{" + "method" + "\\}", apiClient.escapeString(method.toString()));

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
    private com.squareup.okhttp.Call executeUserMethodValidateBeforeCall(String method, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'method' is set
        if (method == null) {
            throw new ApiException("Missing the required parameter 'method' when calling executeUserMethod(Async)");
        }
        
        com.squareup.okhttp.Call call = executeUserMethodCall(method, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param method the method (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response executeUserMethod(String method) throws ApiException {
        ApiResponse<Response> resp = executeUserMethodWithHttpInfo(method);
        return resp.getData();
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param method the method (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> executeUserMethodWithHttpInfo(String method) throws ApiException {
        com.squareup.okhttp.Call call = executeUserMethodValidateBeforeCall(method, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param method the method (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call executeUserMethodAsync(String method, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = executeUserMethodValidateBeforeCall(method, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for generateChangeset
     * @param body the approval node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call generateChangesetCall(Approval body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/wfi/request/approval";

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
    private com.squareup.okhttp.Call generateChangesetValidateBeforeCall(Approval body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling generateChangeset(Async)");
        }
        
        com.squareup.okhttp.Call call = generateChangesetCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * This callback is from the workflow after all approvals are complete
     * Accepted Roles: * Bootstrap Admin 
     * @param body the approval node (required)
     * @return Approval
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Approval generateChangeset(Approval body) throws ApiException {
        ApiResponse<Approval> resp = generateChangesetWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * This callback is from the workflow after all approvals are complete
     * Accepted Roles: * Bootstrap Admin 
     * @param body the approval node (required)
     * @return ApiResponse&lt;Approval&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Approval> generateChangesetWithHttpInfo(Approval body) throws ApiException {
        com.squareup.okhttp.Call call = generateChangesetValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Approval>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * This callback is from the workflow after all approvals are complete (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param body the approval node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call generateChangesetAsync(Approval body, final ApiCallback<Approval> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = generateChangesetValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Approval>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContext
     * @param context the context that was passed in to the workflow editor a \&quot;conext\&quot; url param (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContextCall(String context, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/context";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (context != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("context", context));

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
    private com.squareup.okhttp.Call getContextValidateBeforeCall(String context, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getContextCall(context, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;.
     * Accepted Roles: * Bootstrap Admin 
     * @param context the context that was passed in to the workflow editor a \&quot;conext\&quot; url param (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getContext(String context) throws ApiException {
        ApiResponse<Response> resp = getContextWithHttpInfo(context);
        return resp.getData();
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;.
     * Accepted Roles: * Bootstrap Admin 
     * @param context the context that was passed in to the workflow editor a \&quot;conext\&quot; url param (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getContextWithHttpInfo(String context) throws ApiException {
        com.squareup.okhttp.Call call = getContextValidateBeforeCall(context, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;. (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param context the context that was passed in to the workflow editor a \&quot;conext\&quot; url param (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContextAsync(String context, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContextValidateBeforeCall(context, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDelegateEmail
     * @param id the unique user id of the original approver (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDelegateEmailCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/delegate/email";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));

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
    private com.squareup.okhttp.Call getDelegateEmailValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDelegateEmailCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Iemail of delegate
     * Accepted Roles: * Bootstrap Admin 
     * @param id the unique user id of the original approver (optional)
     * @return Delegate
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Delegate getDelegateEmail(String id) throws ApiException {
        ApiResponse<Delegate> resp = getDelegateEmailWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get Iemail of delegate
     * Accepted Roles: * Bootstrap Admin 
     * @param id the unique user id of the original approver (optional)
     * @return ApiResponse&lt;Delegate&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Delegate> getDelegateEmailWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = getDelegateEmailValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Delegate>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Iemail of delegate (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param id the unique user id of the original approver (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDelegateEmailAsync(String id, final ApiCallback<Delegate> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDelegateEmailValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Delegate>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEmail
     * @param id multivalued, uniqueUserId or uniqueGroupId if passing entity type user, required (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getEmailCall(List<String> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/email";

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
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getEmailValidateBeforeCall(List<String> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getEmailCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get emails - currently supports entity type &#x27;user&#x27; and &#x27;group&#x27;
     * Accepted Roles: * Bootstrap Admin 
     * @param id multivalued, uniqueUserId or uniqueGroupId if passing entity type user, required (optional)
     * @return EntityItem
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EntityItem getEmail(List<String> id) throws ApiException {
        ApiResponse<EntityItem> resp = getEmailWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get emails - currently supports entity type &#x27;user&#x27; and &#x27;group&#x27;
     * Accepted Roles: * Bootstrap Admin 
     * @param id multivalued, uniqueUserId or uniqueGroupId if passing entity type user, required (optional)
     * @return ApiResponse&lt;EntityItem&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EntityItem> getEmailWithHttpInfo(List<String> id) throws ApiException {
        com.squareup.okhttp.Call call = getEmailValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<EntityItem>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get emails - currently supports entity type &#x27;user&#x27; and &#x27;group&#x27; (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param id multivalued, uniqueUserId or uniqueGroupId if passing entity type user, required (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEmailAsync(List<String> id, final ApiCallback<EntityItem> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEmailValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EntityItem>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSchema
     * @param type the entity type (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSchemaCall(String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/schema/entity";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));

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
    private com.squareup.okhttp.Call getSchemaValidateBeforeCall(String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getSchemaCall(type, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @return EntityItem
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EntityItem getSchema(String type) throws ApiException {
        ApiResponse<EntityItem> resp = getSchemaWithHttpInfo(type);
        return resp.getData();
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @return ApiResponse&lt;EntityItem&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EntityItem> getSchemaWithHttpInfo(String type) throws ApiException {
        com.squareup.okhttp.Call call = getSchemaValidateBeforeCall(type, null, null);
        Type localVarReturnType = new TypeToken<EntityItem>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSchemaAsync(String type, final ApiCallback<EntityItem> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSchemaValidateBeforeCall(type, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EntityItem>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserByEmail
     * @param email the email of the user to look up (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserByEmailCall(String email, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/user";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (email != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("email", email));

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
    private com.squareup.okhttp.Call getUserByEmailValidateBeforeCall(String email, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getUserByEmailCall(email, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get user by email
     * Accepted Roles: * Bootstrap Admin 
     * @param email the email of the user to look up (optional)
     * @return UserEmailNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public UserEmailNode getUserByEmail(String email) throws ApiException {
        ApiResponse<UserEmailNode> resp = getUserByEmailWithHttpInfo(email);
        return resp.getData();
    }

    /**
     * Get user by email
     * Accepted Roles: * Bootstrap Admin 
     * @param email the email of the user to look up (optional)
     * @return ApiResponse&lt;UserEmailNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<UserEmailNode> getUserByEmailWithHttpInfo(String email) throws ApiException {
        com.squareup.okhttp.Call call = getUserByEmailValidateBeforeCall(email, null, null);
        Type localVarReturnType = new TypeToken<UserEmailNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get user by email (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param email the email of the user to look up (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserByEmailAsync(String email, final ApiCallback<UserEmailNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserByEmailValidateBeforeCall(email, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<UserEmailNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserGroups
     * @param body the groups to look for (required)
     * @param id the unique userid (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserGroupsCall(Groups body, String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/wfi/group/membership";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));

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
    private com.squareup.okhttp.Call getUserGroupsValidateBeforeCall(Groups body, String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserGroups(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserGroupsCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * getUserGroups
     * Accepted Roles: * Bootstrap Admin 
     * @param body the groups to look for (required)
     * @param id the unique userid (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getUserGroups(Groups body, String id) throws ApiException {
        ApiResponse<Response> resp = getUserGroupsWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * getUserGroups
     * Accepted Roles: * Bootstrap Admin 
     * @param body the groups to look for (required)
     * @param id the unique userid (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getUserGroupsWithHttpInfo(Groups body, String id) throws ApiException {
        com.squareup.okhttp.Call call = getUserGroupsValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * getUserGroups (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param body the groups to look for (required)
     * @param id the unique userid (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserGroupsAsync(Groups body, String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserGroupsValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getWorkflowAssociation
     * @param id the workflow id (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWorkflowAssociationCall(String id, Integer indexFrom, Integer size, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/association";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getWorkflowAssociationValidateBeforeCall(String id, Integer indexFrom, Integer size, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getWorkflowAssociationCall(id, indexFrom, size, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Association for this workflow - currently supports Approval Polices
     * Accepted Roles: * Bootstrap Admin 
     * @param id the workflow id (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @return Associations
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Associations getWorkflowAssociation(String id, Integer indexFrom, Integer size, String q) throws ApiException {
        ApiResponse<Associations> resp = getWorkflowAssociationWithHttpInfo(id, indexFrom, size, q);
        return resp.getData();
    }

    /**
     * Get Association for this workflow - currently supports Approval Polices
     * Accepted Roles: * Bootstrap Admin 
     * @param id the workflow id (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @return ApiResponse&lt;Associations&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Associations> getWorkflowAssociationWithHttpInfo(String id, Integer indexFrom, Integer size, String q) throws ApiException {
        com.squareup.okhttp.Call call = getWorkflowAssociationValidateBeforeCall(id, indexFrom, size, q, null, null);
        Type localVarReturnType = new TypeToken<Associations>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Association for this workflow - currently supports Approval Polices (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param id the workflow id (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWorkflowAssociationAsync(String id, Integer indexFrom, Integer size, String q, final ApiCallback<Associations> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWorkflowAssociationValidateBeforeCall(id, indexFrom, size, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Associations>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getWorkflowAssociationDetails
     * @param id the association id (optional)
     * @param type the assocation type (Only ApprovalPolicy is currently supported) (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWorkflowAssociationDetailsCall(String id, String type, Integer indexFrom, Integer size, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/association/details";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getWorkflowAssociationDetailsValidateBeforeCall(String id, String type, Integer indexFrom, Integer size, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getWorkflowAssociationDetailsCall(id, type, indexFrom, size, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Association Details for this workflow - currently supports Approval Polices, workflow policies, data policies
     * Accepted Roles: * Bootstrap Admin 
     * @param id the association id (optional)
     * @param type the assocation type (Only ApprovalPolicy is currently supported) (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @return Associations
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Associations getWorkflowAssociationDetails(String id, String type, Integer indexFrom, Integer size, String q) throws ApiException {
        ApiResponse<Associations> resp = getWorkflowAssociationDetailsWithHttpInfo(id, type, indexFrom, size, q);
        return resp.getData();
    }

    /**
     * Get Association Details for this workflow - currently supports Approval Polices, workflow policies, data policies
     * Accepted Roles: * Bootstrap Admin 
     * @param id the association id (optional)
     * @param type the assocation type (Only ApprovalPolicy is currently supported) (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @return ApiResponse&lt;Associations&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Associations> getWorkflowAssociationDetailsWithHttpInfo(String id, String type, Integer indexFrom, Integer size, String q) throws ApiException {
        com.squareup.okhttp.Call call = getWorkflowAssociationDetailsValidateBeforeCall(id, type, indexFrom, size, q, null, null);
        Type localVarReturnType = new TypeToken<Associations>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Association Details for this workflow - currently supports Approval Polices, workflow policies, data policies (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param id the association id (optional)
     * @param type the assocation type (Only ApprovalPolicy is currently supported) (optional)
     * @param indexFrom the starting row (optional, default to 0)
     * @param size the size of result set (optional, default to 0)
     * @param q case insenstive query string applied to name and description (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWorkflowAssociationDetailsAsync(String id, String type, Integer indexFrom, Integer size, String q, final ApiCallback<Associations> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWorkflowAssociationDetailsValidateBeforeCall(id, type, indexFrom, size, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Associations>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for idVaultGet
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call idVaultGetCall(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/entity";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));
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
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (matchMode != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("matchMode", matchMode));

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
    private com.squareup.okhttp.Call idVaultGetValidateBeforeCall(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = idVaultGetCall(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;, &#x27;group&#x27;, or &#x27;user~group&#x27;
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @return Entities
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Entities idVaultGet(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode) throws ApiException {
        ApiResponse<Entities> resp = idVaultGetWithHttpInfo(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode);
        return resp.getData();
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;, &#x27;group&#x27;, or &#x27;user~group&#x27;
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @return ApiResponse&lt;Entities&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Entities> idVaultGetWithHttpInfo(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode) throws ApiException {
        com.squareup.okhttp.Call call = idVaultGetValidateBeforeCall(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode, null, null);
        Type localVarReturnType = new TypeToken<Entities>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;, &#x27;group&#x27;, or &#x27;user~group&#x27; (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call idVaultGetAsync(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, final ApiCallback<Entities> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = idVaultGetValidateBeforeCall(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Entities>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for idVaultGetList
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all                         if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter                          applied against filterAttrs, if none then listAttrs, if none then all searchable values in the schema (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @param filterAttr if included, q&#x3D; values is applied to these values (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call idVaultGetListCall(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, List<String> filterAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/wfi/entity/list";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));
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
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (matchMode != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("matchMode", matchMode));
        if (filterAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "filterAttr", filterAttr));

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
    private com.squareup.okhttp.Call idVaultGetListValidateBeforeCall(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, List<String> filterAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = idVaultGetListCall(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode, filterAttr, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all                         if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter                          applied against filterAttrs, if none then listAttrs, if none then all searchable values in the schema (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @param filterAttr if included, q&#x3D; values is applied to these values (optional)
     * @return Entities
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Entities idVaultGetList(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, List<String> filterAttr) throws ApiException {
        ApiResponse<Entities> resp = idVaultGetListWithHttpInfo(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode, filterAttr);
        return resp.getData();
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all                         if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter                          applied against filterAttrs, if none then listAttrs, if none then all searchable values in the schema (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @param filterAttr if included, q&#x3D; values is applied to these values (optional)
     * @return ApiResponse&lt;Entities&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Entities> idVaultGetListWithHttpInfo(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, List<String> filterAttr) throws ApiException {
        com.squareup.okhttp.Call call = idVaultGetListValidateBeforeCall(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode, filterAttr, null, null);
        Type localVarReturnType = new TypeToken<Entities>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get ID Vault values list - current supports entity type user, group, or permission (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param type the entity type (optional)
     * @param id the entity ids (optional)
     * @param listAttr The attributes to return in the payload. Returns all                         if not present. Server can return them in                          any order. (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder sort by, either ASC for ascending or DESC for descending (optional, default to ASC)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, pass in 0 for no limit (optional, default to 10)
     * @param q Optional quick search filter value, leave blank for no filter                          applied against filterAttrs, if none then listAttrs, if none then all searchable values in the schema (optional)
     * @param matchMode match mode values EXACT, START, ANY (default ANY), only used if filterString is passed in (optional, default to ANY)
     * @param filterAttr if included, q&#x3D; values is applied to these values (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call idVaultGetListAsync(String type, List<String> id, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String matchMode, List<String> filterAttr, final ApiCallback<Entities> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = idVaultGetListValidateBeforeCall(type, id, listAttr, sortBy, sortOrder, indexFrom, size, q, matchMode, filterAttr, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Entities>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for idVaultPost
     * @param body the search criteria (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call idVaultPostCall(EntityItem body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/wfi/entity";

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
    private com.squareup.okhttp.Call idVaultPostValidateBeforeCall(EntityItem body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling idVaultPost(Async)");
        }
        
        com.squareup.okhttp.Call call = idVaultPostCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;, &#x27;group&#x27;, or &#x27;user~group&#x27;
     * Accepted Roles: * Bootstrap Admin 
     * @param body the search criteria (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response idVaultPost(EntityItem body) throws ApiException {
        ApiResponse<Response> resp = idVaultPostWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;, &#x27;group&#x27;, or &#x27;user~group&#x27;
     * Accepted Roles: * Bootstrap Admin 
     * @param body the search criteria (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> idVaultPostWithHttpInfo(EntityItem body) throws ApiException {
        com.squareup.okhttp.Call call = idVaultPostValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get ID Vault values - current supports entity type &#x27;user&#x27;, &#x27;group&#x27;, or &#x27;user~group&#x27; (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param body the search criteria (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call idVaultPostAsync(EntityItem body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = idVaultPostValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for whoamiViaPost
     * @param body UserInfo object, usually created by osp or extracted from a token (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call whoamiViaPostCall(UserInfo body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/wfi/user/details";

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
    private com.squareup.okhttp.Call whoamiViaPostValidateBeforeCall(UserInfo body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling whoamiViaPost(Async)");
        }
        
        com.squareup.okhttp.Call call = whoamiViaPostCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * whoami.
     * Accepted Roles: * Bootstrap Admin 
     * @param body UserInfo object, usually created by osp or extracted from a token (required)
     * @return Whoami
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Whoami whoamiViaPost(UserInfo body) throws ApiException {
        ApiResponse<Whoami> resp = whoamiViaPostWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * whoami.
     * Accepted Roles: * Bootstrap Admin 
     * @param body UserInfo object, usually created by osp or extracted from a token (required)
     * @return ApiResponse&lt;Whoami&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Whoami> whoamiViaPostWithHttpInfo(UserInfo body) throws ApiException {
        com.squareup.okhttp.Call call = whoamiViaPostValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Whoami>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * whoami. (asynchronously)
     * Accepted Roles: * Bootstrap Admin 
     * @param body UserInfo object, usually created by osp or extracted from a token (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call whoamiViaPostAsync(UserInfo body, final ApiCallback<Whoami> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = whoamiViaPostValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Whoami>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
