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


import io.swagger.client.model.RemediationListNode;
import io.swagger.client.model.RemediationNode;
import io.swagger.client.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyRemediationsApi {
    private ApiClient apiClient;

    public PolicyRemediationsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyRemediationsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for addRemediation
     * @param body - remediation node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addRemediationCall(RemediationNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/remediations/new";

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
    private com.squareup.okhttp.Call addRemediationValidateBeforeCall(RemediationNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling addRemediation(Async)");
        }
        
        com.squareup.okhttp.Call call = addRemediationCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create new remediation
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - remediation node (required)
     * @return RemediationNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RemediationNode addRemediation(RemediationNode body) throws ApiException {
        ApiResponse<RemediationNode> resp = addRemediationWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create new remediation
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - remediation node (required)
     * @return ApiResponse&lt;RemediationNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RemediationNode> addRemediationWithHttpInfo(RemediationNode body) throws ApiException {
        com.squareup.okhttp.Call call = addRemediationValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create new remediation (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - remediation node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addRemediationAsync(RemediationNode body, final ApiCallback<RemediationNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = addRemediationValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAllRemediationRunsByPolicy
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId -  optional remediation id (optional)
     * @param onlyParent - true/false optional flag to return only parent runs for workflow (optional, default to true)
     * @param size - number of records to return (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAllRemediationRunsByPolicyCall(String policyUniqueId, String policyType, Long remediationId, Boolean onlyParent, Long size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/remediations/allRuns";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (policyUniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyUniqueId", policyUniqueId));
        if (policyType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyType", policyType));
        if (remediationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("remediationId", remediationId));
        if (onlyParent != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("onlyParent", onlyParent));
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
    private com.squareup.okhttp.Call getAllRemediationRunsByPolicyValidateBeforeCall(String policyUniqueId, String policyType, Long remediationId, Boolean onlyParent, Long size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getAllRemediationRunsByPolicyCall(policyUniqueId, policyType, remediationId, onlyParent, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get all remediation runs by policy unique id and policy type, and optional remediation id
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId -  optional remediation id (optional)
     * @param onlyParent - true/false optional flag to return only parent runs for workflow (optional, default to true)
     * @param size - number of records to return (optional)
     * @return RemediationListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RemediationListNode getAllRemediationRunsByPolicy(String policyUniqueId, String policyType, Long remediationId, Boolean onlyParent, Long size) throws ApiException {
        ApiResponse<RemediationListNode> resp = getAllRemediationRunsByPolicyWithHttpInfo(policyUniqueId, policyType, remediationId, onlyParent, size);
        return resp.getData();
    }

    /**
     * Get all remediation runs by policy unique id and policy type, and optional remediation id
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId -  optional remediation id (optional)
     * @param onlyParent - true/false optional flag to return only parent runs for workflow (optional, default to true)
     * @param size - number of records to return (optional)
     * @return ApiResponse&lt;RemediationListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RemediationListNode> getAllRemediationRunsByPolicyWithHttpInfo(String policyUniqueId, String policyType, Long remediationId, Boolean onlyParent, Long size) throws ApiException {
        com.squareup.okhttp.Call call = getAllRemediationRunsByPolicyValidateBeforeCall(policyUniqueId, policyType, remediationId, onlyParent, size, null, null);
        Type localVarReturnType = new TypeToken<RemediationListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get all remediation runs by policy unique id and policy type, and optional remediation id (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId -  optional remediation id (optional)
     * @param onlyParent - true/false optional flag to return only parent runs for workflow (optional, default to true)
     * @param size - number of records to return (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAllRemediationRunsByPolicyAsync(String policyUniqueId, String policyType, Long remediationId, Boolean onlyParent, Long size, final ApiCallback<RemediationListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAllRemediationRunsByPolicyValidateBeforeCall(policyUniqueId, policyType, remediationId, onlyParent, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RemediationListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRemediation
     * @param id - remediation id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRemediationCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/remediations/{id}"
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
    private com.squareup.okhttp.Call getRemediationValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getRemediation(Async)");
        }
        
        com.squareup.okhttp.Call call = getRemediationCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get remediation by specified id
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - remediation id (required)
     * @return RemediationNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RemediationNode getRemediation(Long id) throws ApiException {
        ApiResponse<RemediationNode> resp = getRemediationWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get remediation by specified id
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - remediation id (required)
     * @return ApiResponse&lt;RemediationNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RemediationNode> getRemediationWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getRemediationValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get remediation by specified id (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - remediation id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRemediationAsync(Long id, final ApiCallback<RemediationNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRemediationValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRemediationByPolicy
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param onlyActive - optional true/false flag to show all or only active remediation (optional, default to false)
     * @param size - number of records to return (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRemediationByPolicyCall(String policyUniqueId, String policyType, Boolean onlyActive, Long size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/remediations/all";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (policyUniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyUniqueId", policyUniqueId));
        if (policyType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyType", policyType));
        if (onlyActive != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("onlyActive", onlyActive));
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
    private com.squareup.okhttp.Call getRemediationByPolicyValidateBeforeCall(String policyUniqueId, String policyType, Boolean onlyActive, Long size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getRemediationByPolicyCall(policyUniqueId, policyType, onlyActive, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get all remediations by policy unique id and policy type
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param onlyActive - optional true/false flag to show all or only active remediation (optional, default to false)
     * @param size - number of records to return (optional)
     * @return RemediationListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RemediationListNode getRemediationByPolicy(String policyUniqueId, String policyType, Boolean onlyActive, Long size) throws ApiException {
        ApiResponse<RemediationListNode> resp = getRemediationByPolicyWithHttpInfo(policyUniqueId, policyType, onlyActive, size);
        return resp.getData();
    }

    /**
     * Get all remediations by policy unique id and policy type
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param onlyActive - optional true/false flag to show all or only active remediation (optional, default to false)
     * @param size - number of records to return (optional)
     * @return ApiResponse&lt;RemediationListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RemediationListNode> getRemediationByPolicyWithHttpInfo(String policyUniqueId, String policyType, Boolean onlyActive, Long size) throws ApiException {
        com.squareup.okhttp.Call call = getRemediationByPolicyValidateBeforeCall(policyUniqueId, policyType, onlyActive, size, null, null);
        Type localVarReturnType = new TypeToken<RemediationListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get all remediations by policy unique id and policy type (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param onlyActive - optional true/false flag to show all or only active remediation (optional, default to false)
     * @param size - number of records to return (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRemediationByPolicyAsync(String policyUniqueId, String policyType, Boolean onlyActive, Long size, final ApiCallback<RemediationListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRemediationByPolicyValidateBeforeCall(policyUniqueId, policyType, onlyActive, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RemediationListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for removeRemediation
     * @param id - remediation id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call removeRemediationCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/remediations/{id}"
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
    private com.squareup.okhttp.Call removeRemediationValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling removeRemediation(Async)");
        }
        
        com.squareup.okhttp.Call call = removeRemediationCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Remove (set non-active) remediation
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - remediation id (required)
     * @return RemediationNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RemediationNode removeRemediation(Long id) throws ApiException {
        ApiResponse<RemediationNode> resp = removeRemediationWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Remove (set non-active) remediation
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - remediation id (required)
     * @return ApiResponse&lt;RemediationNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RemediationNode> removeRemediationWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = removeRemediationValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Remove (set non-active) remediation (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - remediation id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call removeRemediationAsync(Long id, final ApiCallback<RemediationNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = removeRemediationValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for runPolicyRemediation
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId - remediation id, require for Data Policy in 3.7.2 (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call runPolicyRemediationCall(String policyUniqueId, String policyType, Long remediationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/remediations/runRemediation";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (policyUniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyUniqueId", policyUniqueId));
        if (policyType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyType", policyType));
        if (remediationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("remediationId", remediationId));

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
    private com.squareup.okhttp.Call runPolicyRemediationValidateBeforeCall(String policyUniqueId, String policyType, Long remediationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = runPolicyRemediationCall(policyUniqueId, policyType, remediationId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Run remediation for policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId - remediation id, require for Data Policy in 3.7.2 (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response runPolicyRemediation(String policyUniqueId, String policyType, Long remediationId) throws ApiException {
        ApiResponse<Response> resp = runPolicyRemediationWithHttpInfo(policyUniqueId, policyType, remediationId);
        return resp.getData();
    }

    /**
     * Run remediation for policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId - remediation id, require for Data Policy in 3.7.2 (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> runPolicyRemediationWithHttpInfo(String policyUniqueId, String policyType, Long remediationId) throws ApiException {
        com.squareup.okhttp.Call call = runPolicyRemediationValidateBeforeCall(policyUniqueId, policyType, remediationId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Run remediation for policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param policyUniqueId - policy unique id (optional)
     * @param policyType - policy type (optional)
     * @param remediationId - remediation id, require for Data Policy in 3.7.2 (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call runPolicyRemediationAsync(String policyUniqueId, String policyType, Long remediationId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = runPolicyRemediationValidateBeforeCall(policyUniqueId, policyType, remediationId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateRemediation
     * @param body - remediation node (required)
     * @param id - remediation id (required)
     * @param createNew - optional true/false flag to create new remediation, and set the old one as non-active, default is false (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateRemediationCall(RemediationNode body, Long id, Boolean createNew, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/remediations/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (createNew != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("createNew", createNew));

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
    private com.squareup.okhttp.Call updateRemediationValidateBeforeCall(RemediationNode body, Long id, Boolean createNew, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateRemediation(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateRemediation(Async)");
        }
        
        com.squareup.okhttp.Call call = updateRemediationCall(body, id, createNew, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update remediation
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - remediation node (required)
     * @param id - remediation id (required)
     * @param createNew - optional true/false flag to create new remediation, and set the old one as non-active, default is false (optional, default to false)
     * @return RemediationNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RemediationNode updateRemediation(RemediationNode body, Long id, Boolean createNew) throws ApiException {
        ApiResponse<RemediationNode> resp = updateRemediationWithHttpInfo(body, id, createNew);
        return resp.getData();
    }

    /**
     * Update remediation
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - remediation node (required)
     * @param id - remediation id (required)
     * @param createNew - optional true/false flag to create new remediation, and set the old one as non-active, default is false (optional, default to false)
     * @return ApiResponse&lt;RemediationNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RemediationNode> updateRemediationWithHttpInfo(RemediationNode body, Long id, Boolean createNew) throws ApiException {
        com.squareup.okhttp.Call call = updateRemediationValidateBeforeCall(body, id, createNew, null, null);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update remediation (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - remediation node (required)
     * @param id - remediation id (required)
     * @param createNew - optional true/false flag to create new remediation, and set the old one as non-active, default is false (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateRemediationAsync(RemediationNode body, Long id, Boolean createNew, final ApiCallback<RemediationNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateRemediationValidateBeforeCall(body, id, createNew, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RemediationNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
