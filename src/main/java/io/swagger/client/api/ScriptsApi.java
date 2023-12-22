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


import io.swagger.client.model.Debug;
import io.swagger.client.model.Response;
import io.swagger.client.model.ScriptList;
import io.swagger.client.model.ScriptMapping;
import io.swagger.client.model.ScriptNode;
import io.swagger.client.model.Status;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptsApi {
    private ApiClient apiClient;

    public ScriptsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ScriptsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for associateScript
     * @param body the instructions on how to map (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call associateScriptCall(ScriptMapping body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/scripts/association";

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
    private com.squareup.okhttp.Call associateScriptValidateBeforeCall(ScriptMapping body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling associateScript(Async)");
        }
        
        com.squareup.okhttp.Call call = associateScriptCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Associates a script with an application.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the instructions on how to map (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status associateScript(ScriptMapping body) throws ApiException {
        ApiResponse<Status> resp = associateScriptWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Associates a script with an application.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the instructions on how to map (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> associateScriptWithHttpInfo(ScriptMapping body) throws ApiException {
        com.squareup.okhttp.Call call = associateScriptValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Associates a script with an application. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the instructions on how to map (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call associateScriptAsync(ScriptMapping body, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = associateScriptValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for associateScript_0
     * @param id The script to be associated (required)
     * @param application The ID of the application the script is to be associated with (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call associateScript_0Call(Long id, Long application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts/{id}/position"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (application != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("application", application));

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
    private com.squareup.okhttp.Call associateScript_0ValidateBeforeCall(Long id, Long application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling associateScript_0(Async)");
        }
        
        com.squareup.okhttp.Call call = associateScript_0Call(id, application, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create an association between a script and an application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be associated (required)
     * @param application The ID of the application the script is to be associated with (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status associateScript_0(Long id, Long application) throws ApiException {
        ApiResponse<Status> resp = associateScript_0WithHttpInfo(id, application);
        return resp.getData();
    }

    /**
     * Create an association between a script and an application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be associated (required)
     * @param application The ID of the application the script is to be associated with (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> associateScript_0WithHttpInfo(Long id, Long application) throws ApiException {
        com.squareup.okhttp.Call call = associateScript_0ValidateBeforeCall(id, application, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create an association between a script and an application (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be associated (required)
     * @param application The ID of the application the script is to be associated with (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call associateScript_0Async(Long id, Long application, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = associateScript_0ValidateBeforeCall(id, application, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createScript
     * @param body the script plus any associate application (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createScriptCall(ScriptNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/scripts";

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
    private com.squareup.okhttp.Call createScriptValidateBeforeCall(ScriptNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createScript(Async)");
        }
        
        com.squareup.okhttp.Call call = createScriptCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Creates a script, optionally links it to an application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the script plus any associate application (required)
     * @return ScriptNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScriptNode createScript(ScriptNode body) throws ApiException {
        ApiResponse<ScriptNode> resp = createScriptWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Creates a script, optionally links it to an application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the script plus any associate application (required)
     * @return ApiResponse&lt;ScriptNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ScriptNode> createScriptWithHttpInfo(ScriptNode body) throws ApiException {
        com.squareup.okhttp.Call call = createScriptValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a script, optionally links it to an application (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the script plus any associate application (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createScriptAsync(ScriptNode body, final ApiCallback<ScriptNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createScriptValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteScript
     * @param id the script description id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteScriptCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts/{id}"
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
    private com.squareup.okhttp.Call deleteScriptValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteScript(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteScriptCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Deletes a script
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script description id (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteScript(Long id) throws ApiException {
        ApiResponse<Status> resp = deleteScriptWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Deletes a script
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script description id (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteScriptWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteScriptValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a script (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script description id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteScriptAsync(Long id, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteScriptValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getScript
     * @param id the script description id (passing in a -1 returns the sample script) (required)
     * @param includeDeleted search for deleted ones too (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getScriptCall(Long id, Boolean includeDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "includeDeleted" + "\\}", apiClient.escapeString(includeDeleted.toString()));

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
    private com.squareup.okhttp.Call getScriptValidateBeforeCall(Long id, Boolean includeDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getScript(Async)");
        }
        // verify the required parameter 'includeDeleted' is set
        if (includeDeleted == null) {
            throw new ApiException("Missing the required parameter 'includeDeleted' when calling getScript(Async)");
        }
        
        com.squareup.okhttp.Call call = getScriptCall(id, includeDeleted, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Searches for a particular script by passing in scriptDescription id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script description id (passing in a -1 returns the sample script) (required)
     * @param includeDeleted search for deleted ones too (required)
     * @return ScriptNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScriptNode getScript(Long id, Boolean includeDeleted) throws ApiException {
        ApiResponse<ScriptNode> resp = getScriptWithHttpInfo(id, includeDeleted);
        return resp.getData();
    }

    /**
     * Searches for a particular script by passing in scriptDescription id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script description id (passing in a -1 returns the sample script) (required)
     * @param includeDeleted search for deleted ones too (required)
     * @return ApiResponse&lt;ScriptNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ScriptNode> getScriptWithHttpInfo(Long id, Boolean includeDeleted) throws ApiException {
        com.squareup.okhttp.Call call = getScriptValidateBeforeCall(id, includeDeleted, null, null);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Searches for a particular script by passing in scriptDescription id (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script description id (passing in a -1 returns the sample script) (required)
     * @param includeDeleted search for deleted ones too (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getScriptAsync(Long id, Boolean includeDeleted, final ApiCallback<ScriptNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getScriptValidateBeforeCall(id, includeDeleted, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for isScriptNameAvailable
     * @param name the script Name (required)
     * @param id the id (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call isScriptNameAvailableCall(String name, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts/nameOk/{name}"
            .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "*/*"
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
    private com.squareup.okhttp.Call isScriptNameAvailableValidateBeforeCall(String name, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'name' is set
        if (name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling isScriptNameAvailable(Async)");
        }
        
        com.squareup.okhttp.Call call = isScriptNameAvailableCall(name, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns node indicating if proposed script name is available
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param name the script Name (required)
     * @param id the id (optional, default to 0)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response isScriptNameAvailable(String name, Long id) throws ApiException {
        ApiResponse<Response> resp = isScriptNameAvailableWithHttpInfo(name, id);
        return resp.getData();
    }

    /**
     * Returns node indicating if proposed script name is available
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param name the script Name (required)
     * @param id the id (optional, default to 0)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> isScriptNameAvailableWithHttpInfo(String name, Long id) throws ApiException {
        com.squareup.okhttp.Call call = isScriptNameAvailableValidateBeforeCall(name, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns node indicating if proposed script name is available (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param name the script Name (required)
     * @param id the id (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call isScriptNameAvailableAsync(String name, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = isScriptNameAvailableValidateBeforeCall(name, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for positionScript
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param up Flag indicating whether to move the script up or down in order, true&#x3D;up, false&#x3D;down (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call positionScriptCall(Long id, Long application, Boolean up, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts/{id}/position"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (application != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("application", application));
        if (up != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("up", up));

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
    private com.squareup.okhttp.Call positionScriptValidateBeforeCall(Long id, Long application, Boolean up, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling positionScript(Async)");
        }
        
        com.squareup.okhttp.Call call = positionScriptCall(id, application, up, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Moves a script up or down in order within an application it is associated with.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param up Flag indicating whether to move the script up or down in order, true&#x3D;up, false&#x3D;down (optional, default to false)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status positionScript(Long id, Long application, Boolean up) throws ApiException {
        ApiResponse<Status> resp = positionScriptWithHttpInfo(id, application, up);
        return resp.getData();
    }

    /**
     * Moves a script up or down in order within an application it is associated with.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param up Flag indicating whether to move the script up or down in order, true&#x3D;up, false&#x3D;down (optional, default to false)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> positionScriptWithHttpInfo(Long id, Long application, Boolean up) throws ApiException {
        com.squareup.okhttp.Call call = positionScriptValidateBeforeCall(id, application, up, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Moves a script up or down in order within an application it is associated with. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param up Flag indicating whether to move the script up or down in order, true&#x3D;up, false&#x3D;down (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call positionScriptAsync(Long id, Long application, Boolean up, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = positionScriptValidateBeforeCall(id, application, up, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for putForm
     * @param id the script ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call putFormCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts/{id}/publish"
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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call putFormValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling putForm(Async)");
        }
        
        com.squareup.okhttp.Call call = putFormCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * publishes a script
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script ID (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status putForm(Long id) throws ApiException {
        ApiResponse<Status> resp = putFormWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * publishes a script
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script ID (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> putFormWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = putFormValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * publishes a script (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id the script ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call putFormAsync(Long id, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = putFormValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for searchScripts
     * @param scriptType the Script type (CHANGESET_POLICY), don&#x27;t include                            for all) (optional)
     * @param q search on script name/description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values                            (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @param application Application ID.  If this is passed in the scriptType, q, sortBy, and sortOrder parameters are ignored. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call searchScriptsCall(String scriptType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, Long application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (scriptType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("scriptType", scriptType));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (application != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("application", application));

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
    private com.squareup.okhttp.Call searchScriptsValidateBeforeCall(String scriptType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, Long application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = searchScriptsCall(scriptType, q, indexFrom, size, sortBy, sortOrder, application, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns scripts associated with an application or based on script type and other criteria
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param scriptType the Script type (CHANGESET_POLICY), don&#x27;t include                            for all) (optional)
     * @param q search on script name/description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values                            (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @param application Application ID.  If this is passed in the scriptType, q, sortBy, and sortOrder parameters are ignored. (optional)
     * @return ScriptList
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScriptList searchScripts(String scriptType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, Long application) throws ApiException {
        ApiResponse<ScriptList> resp = searchScriptsWithHttpInfo(scriptType, q, indexFrom, size, sortBy, sortOrder, application);
        return resp.getData();
    }

    /**
     * Returns scripts associated with an application or based on script type and other criteria
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param scriptType the Script type (CHANGESET_POLICY), don&#x27;t include                            for all) (optional)
     * @param q search on script name/description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values                            (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @param application Application ID.  If this is passed in the scriptType, q, sortBy, and sortOrder parameters are ignored. (optional)
     * @return ApiResponse&lt;ScriptList&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ScriptList> searchScriptsWithHttpInfo(String scriptType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, Long application) throws ApiException {
        com.squareup.okhttp.Call call = searchScriptsValidateBeforeCall(scriptType, q, indexFrom, size, sortBy, sortOrder, application, null, null);
        Type localVarReturnType = new TypeToken<ScriptList>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns scripts associated with an application or based on script type and other criteria (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param scriptType the Script type (CHANGESET_POLICY), don&#x27;t include                            for all) (optional)
     * @param q search on script name/description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values                            (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @param application Application ID.  If this is passed in the scriptType, q, sortBy, and sortOrder parameters are ignored. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call searchScriptsAsync(String scriptType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, Long application, final ApiCallback<ScriptList> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = searchScriptsValidateBeforeCall(scriptType, q, indexFrom, size, sortBy, sortOrder, application, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ScriptList>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setDebugContext
     * @param body The debug context to be set (required)
     * @param id The script ID (required)
     * @param application The ID of the application (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setDebugContextCall(Debug body, Long id, Long application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/scripts/{id}/debugContext"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (application != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("application", application));

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
    private com.squareup.okhttp.Call setDebugContextValidateBeforeCall(Debug body, Long id, Long application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setDebugContext(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling setDebugContext(Async)");
        }
        
        com.squareup.okhttp.Call call = setDebugContextCall(body, id, application, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the debug context for a script and a given application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body The debug context to be set (required)
     * @param id The script ID (required)
     * @param application The ID of the application (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status setDebugContext(Debug body, Long id, Long application) throws ApiException {
        ApiResponse<Status> resp = setDebugContextWithHttpInfo(body, id, application);
        return resp.getData();
    }

    /**
     * Set the debug context for a script and a given application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body The debug context to be set (required)
     * @param id The script ID (required)
     * @param application The ID of the application (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> setDebugContextWithHttpInfo(Debug body, Long id, Long application) throws ApiException {
        com.squareup.okhttp.Call call = setDebugContextValidateBeforeCall(body, id, application, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the debug context for a script and a given application (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body The debug context to be set (required)
     * @param id The script ID (required)
     * @param application The ID of the application (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setDebugContextAsync(Debug body, Long id, Long application, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setDebugContextValidateBeforeCall(body, id, application, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for simulateFulfillmentChangesetPolicy
     * @param body script and metadata about the script you wish to run (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call simulateFulfillmentChangesetPolicyCall(ScriptNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/scripts/changeset/simulate";

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
    private com.squareup.okhttp.Call simulateFulfillmentChangesetPolicyValidateBeforeCall(ScriptNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling simulateFulfillmentChangesetPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = simulateFulfillmentChangesetPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * simulate a changeset policy
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body script and metadata about the script you wish to run (required)
     * @return ScriptNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScriptNode simulateFulfillmentChangesetPolicy(ScriptNode body) throws ApiException {
        ApiResponse<ScriptNode> resp = simulateFulfillmentChangesetPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * simulate a changeset policy
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body script and metadata about the script you wish to run (required)
     * @return ApiResponse&lt;ScriptNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ScriptNode> simulateFulfillmentChangesetPolicyWithHttpInfo(ScriptNode body) throws ApiException {
        com.squareup.okhttp.Call call = simulateFulfillmentChangesetPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * simulate a changeset policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body script and metadata about the script you wish to run (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call simulateFulfillmentChangesetPolicyAsync(ScriptNode body, final ApiCallback<ScriptNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = simulateFulfillmentChangesetPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for unassociateScript
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param deleteIfLastApp Flag indicating whether to delete the script if this is the last application it is associated with. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call unassociateScriptCall(Long id, Long application, Boolean deleteIfLastApp, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scripts/{id}/position"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (application != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("application", application));
        if (deleteIfLastApp != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("deleteIfLastApp", deleteIfLastApp));

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
    private com.squareup.okhttp.Call unassociateScriptValidateBeforeCall(Long id, Long application, Boolean deleteIfLastApp, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling unassociateScript(Async)");
        }
        
        com.squareup.okhttp.Call call = unassociateScriptCall(id, application, deleteIfLastApp, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Unassociates a script from an application.
     * Optionally will delete the script if this is the last application it is associated with.&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param deleteIfLastApp Flag indicating whether to delete the script if this is the last application it is associated with. (optional, default to false)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status unassociateScript(Long id, Long application, Boolean deleteIfLastApp) throws ApiException {
        ApiResponse<Status> resp = unassociateScriptWithHttpInfo(id, application, deleteIfLastApp);
        return resp.getData();
    }

    /**
     * Unassociates a script from an application.
     * Optionally will delete the script if this is the last application it is associated with.&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param deleteIfLastApp Flag indicating whether to delete the script if this is the last application it is associated with. (optional, default to false)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> unassociateScriptWithHttpInfo(Long id, Long application, Boolean deleteIfLastApp) throws ApiException {
        com.squareup.okhttp.Call call = unassociateScriptValidateBeforeCall(id, application, deleteIfLastApp, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Unassociates a script from an application. (asynchronously)
     * Optionally will delete the script if this is the last application it is associated with.&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param id The script to be ordered (required)
     * @param application The ID of the application (optional)
     * @param deleteIfLastApp Flag indicating whether to delete the script if this is the last application it is associated with. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call unassociateScriptAsync(Long id, Long application, Boolean deleteIfLastApp, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = unassociateScriptValidateBeforeCall(id, application, deleteIfLastApp, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateScript
     * @param body the script plus any associate application (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateScriptCall(ScriptNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/scripts";

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
    private com.squareup.okhttp.Call updateScriptValidateBeforeCall(ScriptNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateScript(Async)");
        }
        
        com.squareup.okhttp.Call call = updateScriptCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * updates a script
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the script plus any associate application (required)
     * @return ScriptNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScriptNode updateScript(ScriptNode body) throws ApiException {
        ApiResponse<ScriptNode> resp = updateScriptWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * updates a script
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the script plus any associate application (required)
     * @return ApiResponse&lt;ScriptNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ScriptNode> updateScriptWithHttpInfo(ScriptNode body) throws ApiException {
        com.squareup.okhttp.Call call = updateScriptValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * updates a script (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator * Global Administrator 
     * @param body the script plus any associate application (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateScriptAsync(ScriptNode body, final ApiCallback<ScriptNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateScriptValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ScriptNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
