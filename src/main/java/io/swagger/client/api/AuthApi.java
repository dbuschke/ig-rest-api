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


import io.swagger.client.model.Checkauthorization;
import io.swagger.client.model.Groups;
import io.swagger.client.model.Response;
import io.swagger.client.model.Roles;
import io.swagger.client.model.Status;
import io.swagger.client.model.Users;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthApi {
    private ApiClient apiClient;

    public AuthApi() {
        this(Configuration.getDefaultApiClient());
    }

    public AuthApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for assignRoleSGroup
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call assignRoleSGroupCall(String roleName, Long groupId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles/{roleName}/groups/{groupId}"
            .replaceAll("\\{" + "roleName" + "\\}", apiClient.escapeString(roleName.toString()))
            .replaceAll("\\{" + "groupId" + "\\}", apiClient.escapeString(groupId.toString()));

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
    private com.squareup.okhttp.Call assignRoleSGroupValidateBeforeCall(String roleName, Long groupId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'roleName' is set
        if (roleName == null) {
            throw new ApiException("Missing the required parameter 'roleName' when calling assignRoleSGroup(Async)");
        }
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling assignRoleSGroup(Async)");
        }
        
        com.squareup.okhttp.Call call = assignRoleSGroupCall(roleName, groupId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Assign a sgroup to a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status assignRoleSGroup(String roleName, Long groupId) throws ApiException {
        ApiResponse<Status> resp = assignRoleSGroupWithHttpInfo(roleName, groupId);
        return resp.getData();
    }

    /**
     * Assign a sgroup to a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> assignRoleSGroupWithHttpInfo(String roleName, Long groupId) throws ApiException {
        com.squareup.okhttp.Call call = assignRoleSGroupValidateBeforeCall(roleName, groupId, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Assign a sgroup to a role. (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call assignRoleSGroupAsync(String roleName, Long groupId, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = assignRoleSGroupValidateBeforeCall(roleName, groupId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for assignRoleSUser
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call assignRoleSUserCall(String roleName, Long userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles/{roleName}/users/{userId}"
            .replaceAll("\\{" + "roleName" + "\\}", apiClient.escapeString(roleName.toString()))
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()));

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
    private com.squareup.okhttp.Call assignRoleSUserValidateBeforeCall(String roleName, Long userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'roleName' is set
        if (roleName == null) {
            throw new ApiException("Missing the required parameter 'roleName' when calling assignRoleSUser(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling assignRoleSUser(Async)");
        }
        
        com.squareup.okhttp.Call call = assignRoleSUserCall(roleName, userId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Assign a suser to a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status assignRoleSUser(String roleName, Long userId) throws ApiException {
        ApiResponse<Status> resp = assignRoleSUserWithHttpInfo(roleName, userId);
        return resp.getData();
    }

    /**
     * Assign a suser to a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> assignRoleSUserWithHttpInfo(String roleName, Long userId) throws ApiException {
        com.squareup.okhttp.Call call = assignRoleSUserValidateBeforeCall(roleName, userId, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Assign a suser to a role. (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call assignRoleSUserAsync(String roleName, Long userId, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = assignRoleSUserValidateBeforeCall(roleName, userId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for checkAuthorization
     * @param permName Permission name. (required)
     * @param scopeType Scope type. (optional)
     * @param reviewDefId Unique review definition ID. (optional)
     * @param appId Application ID. (optional)
     * @param reviewInstanceId Review Instance ID. (optional)
     * @param uiItemId UI Item ID. (optional)
     * @param entityType Entity type. (optional)
     * @param sodPolicyId Unique SoD policy ID. (optional)
     * @param sodCaseId SoD case Id (optional)
     * @param rolePolicyId the unique role policy id (optional)
     * @param businessRoleId Unique Business Role ID. (optional)
     * @param approvalPolicyId Unique Approval Policy ID. (optional)
     * @param restApiUri use this to check to see if user has access to this rest api url (optional)
     * @param lookupScope Flag indicating whether to lookup scope or not. (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call checkAuthorizationCall(String permName, String scopeType, String reviewDefId, Long appId, Long reviewInstanceId, Long uiItemId, String entityType, String sodPolicyId, Long sodCaseId, String rolePolicyId, String businessRoleId, String approvalPolicyId, String restApiUri, Boolean lookupScope, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/checkauthorization/{permName}"
            .replaceAll("\\{" + "permName" + "\\}", apiClient.escapeString(permName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (scopeType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("scopeType", scopeType));
        if (reviewDefId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reviewDefId", reviewDefId));
        if (appId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("appId", appId));
        if (reviewInstanceId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reviewInstanceId", reviewInstanceId));
        if (uiItemId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uiItemId", uiItemId));
        if (entityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityType", entityType));
        if (sodPolicyId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sodPolicyId", sodPolicyId));
        if (sodCaseId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sodCaseId", sodCaseId));
        if (rolePolicyId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("rolePolicyId", rolePolicyId));
        if (businessRoleId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("businessRoleId", businessRoleId));
        if (approvalPolicyId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approvalPolicyId", approvalPolicyId));
        if (restApiUri != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("restApiUri", restApiUri));
        if (lookupScope != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("lookupScope", lookupScope));

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
    private com.squareup.okhttp.Call checkAuthorizationValidateBeforeCall(String permName, String scopeType, String reviewDefId, Long appId, Long reviewInstanceId, Long uiItemId, String entityType, String sodPolicyId, Long sodCaseId, String rolePolicyId, String businessRoleId, String approvalPolicyId, String restApiUri, Boolean lookupScope, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'permName' is set
        if (permName == null) {
            throw new ApiException("Missing the required parameter 'permName' when calling checkAuthorization(Async)");
        }
        
        com.squareup.okhttp.Call call = checkAuthorizationCall(permName, scopeType, reviewDefId, appId, reviewInstanceId, uiItemId, entityType, sodPolicyId, sodCaseId, rolePolicyId, businessRoleId, approvalPolicyId, restApiUri, lookupScope, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Check an authorization permission for the authenticated user.
     * Accepted Roles: * All Access 
     * @param permName Permission name. (required)
     * @param scopeType Scope type. (optional)
     * @param reviewDefId Unique review definition ID. (optional)
     * @param appId Application ID. (optional)
     * @param reviewInstanceId Review Instance ID. (optional)
     * @param uiItemId UI Item ID. (optional)
     * @param entityType Entity type. (optional)
     * @param sodPolicyId Unique SoD policy ID. (optional)
     * @param sodCaseId SoD case Id (optional)
     * @param rolePolicyId the unique role policy id (optional)
     * @param businessRoleId Unique Business Role ID. (optional)
     * @param approvalPolicyId Unique Approval Policy ID. (optional)
     * @param restApiUri use this to check to see if user has access to this rest api url (optional)
     * @param lookupScope Flag indicating whether to lookup scope or not. (optional, default to true)
     * @return Checkauthorization
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Checkauthorization checkAuthorization(String permName, String scopeType, String reviewDefId, Long appId, Long reviewInstanceId, Long uiItemId, String entityType, String sodPolicyId, Long sodCaseId, String rolePolicyId, String businessRoleId, String approvalPolicyId, String restApiUri, Boolean lookupScope) throws ApiException {
        ApiResponse<Checkauthorization> resp = checkAuthorizationWithHttpInfo(permName, scopeType, reviewDefId, appId, reviewInstanceId, uiItemId, entityType, sodPolicyId, sodCaseId, rolePolicyId, businessRoleId, approvalPolicyId, restApiUri, lookupScope);
        return resp.getData();
    }

    /**
     * Check an authorization permission for the authenticated user.
     * Accepted Roles: * All Access 
     * @param permName Permission name. (required)
     * @param scopeType Scope type. (optional)
     * @param reviewDefId Unique review definition ID. (optional)
     * @param appId Application ID. (optional)
     * @param reviewInstanceId Review Instance ID. (optional)
     * @param uiItemId UI Item ID. (optional)
     * @param entityType Entity type. (optional)
     * @param sodPolicyId Unique SoD policy ID. (optional)
     * @param sodCaseId SoD case Id (optional)
     * @param rolePolicyId the unique role policy id (optional)
     * @param businessRoleId Unique Business Role ID. (optional)
     * @param approvalPolicyId Unique Approval Policy ID. (optional)
     * @param restApiUri use this to check to see if user has access to this rest api url (optional)
     * @param lookupScope Flag indicating whether to lookup scope or not. (optional, default to true)
     * @return ApiResponse&lt;Checkauthorization&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Checkauthorization> checkAuthorizationWithHttpInfo(String permName, String scopeType, String reviewDefId, Long appId, Long reviewInstanceId, Long uiItemId, String entityType, String sodPolicyId, Long sodCaseId, String rolePolicyId, String businessRoleId, String approvalPolicyId, String restApiUri, Boolean lookupScope) throws ApiException {
        com.squareup.okhttp.Call call = checkAuthorizationValidateBeforeCall(permName, scopeType, reviewDefId, appId, reviewInstanceId, uiItemId, entityType, sodPolicyId, sodCaseId, rolePolicyId, businessRoleId, approvalPolicyId, restApiUri, lookupScope, null, null);
        Type localVarReturnType = new TypeToken<Checkauthorization>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Check an authorization permission for the authenticated user. (asynchronously)
     * Accepted Roles: * All Access 
     * @param permName Permission name. (required)
     * @param scopeType Scope type. (optional)
     * @param reviewDefId Unique review definition ID. (optional)
     * @param appId Application ID. (optional)
     * @param reviewInstanceId Review Instance ID. (optional)
     * @param uiItemId UI Item ID. (optional)
     * @param entityType Entity type. (optional)
     * @param sodPolicyId Unique SoD policy ID. (optional)
     * @param sodCaseId SoD case Id (optional)
     * @param rolePolicyId the unique role policy id (optional)
     * @param businessRoleId Unique Business Role ID. (optional)
     * @param approvalPolicyId Unique Approval Policy ID. (optional)
     * @param restApiUri use this to check to see if user has access to this rest api url (optional)
     * @param lookupScope Flag indicating whether to lookup scope or not. (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call checkAuthorizationAsync(String permName, String scopeType, String reviewDefId, Long appId, Long reviewInstanceId, Long uiItemId, String entityType, String sodPolicyId, Long sodCaseId, String rolePolicyId, String businessRoleId, String approvalPolicyId, String restApiUri, Boolean lookupScope, final ApiCallback<Checkauthorization> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = checkAuthorizationValidateBeforeCall(permName, scopeType, reviewDefId, appId, reviewInstanceId, uiItemId, entityType, sodPolicyId, sodCaseId, rolePolicyId, businessRoleId, approvalPolicyId, restApiUri, lookupScope, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Checkauthorization>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for checkFullReadAccess
     * @param entityType Entity type. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call checkFullReadAccessCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/checkfullreadaccess/{entityType}"
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
    private com.squareup.okhttp.Call checkFullReadAccessValidateBeforeCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling checkFullReadAccess(Async)");
        }
        
        com.squareup.okhttp.Call call = checkFullReadAccessCall(entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Check if the authenticated user has full read access to the specified entity type.
     * Accepted Roles: * All Access 
     * @param entityType Entity type. (required)
     * @return Checkauthorization
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Checkauthorization checkFullReadAccess(String entityType) throws ApiException {
        ApiResponse<Checkauthorization> resp = checkFullReadAccessWithHttpInfo(entityType);
        return resp.getData();
    }

    /**
     * Check if the authenticated user has full read access to the specified entity type.
     * Accepted Roles: * All Access 
     * @param entityType Entity type. (required)
     * @return ApiResponse&lt;Checkauthorization&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Checkauthorization> checkFullReadAccessWithHttpInfo(String entityType) throws ApiException {
        com.squareup.okhttp.Call call = checkFullReadAccessValidateBeforeCall(entityType, null, null);
        Type localVarReturnType = new TypeToken<Checkauthorization>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Check if the authenticated user has full read access to the specified entity type. (asynchronously)
     * Accepted Roles: * All Access 
     * @param entityType Entity type. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call checkFullReadAccessAsync(String entityType, final ApiCallback<Checkauthorization> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = checkFullReadAccessValidateBeforeCall(entityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Checkauthorization>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteRoleSGroup
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteRoleSGroupCall(String roleName, Long groupId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles/{roleName}/groups/{groupId}"
            .replaceAll("\\{" + "roleName" + "\\}", apiClient.escapeString(roleName.toString()))
            .replaceAll("\\{" + "groupId" + "\\}", apiClient.escapeString(groupId.toString()));

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
    private com.squareup.okhttp.Call deleteRoleSGroupValidateBeforeCall(String roleName, Long groupId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'roleName' is set
        if (roleName == null) {
            throw new ApiException("Missing the required parameter 'roleName' when calling deleteRoleSGroup(Async)");
        }
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling deleteRoleSGroup(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteRoleSGroupCall(roleName, groupId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete a sgroup from a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteRoleSGroup(String roleName, Long groupId) throws ApiException {
        ApiResponse<Status> resp = deleteRoleSGroupWithHttpInfo(roleName, groupId);
        return resp.getData();
    }

    /**
     * Delete a sgroup from a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteRoleSGroupWithHttpInfo(String roleName, Long groupId) throws ApiException {
        com.squareup.okhttp.Call call = deleteRoleSGroupValidateBeforeCall(roleName, groupId, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete a sgroup from a role. (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param groupId sgroup&#x27;s id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteRoleSGroupAsync(String roleName, Long groupId, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteRoleSGroupValidateBeforeCall(roleName, groupId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteRoleSUser
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteRoleSUserCall(String roleName, Long userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles/{roleName}/users/{userId}"
            .replaceAll("\\{" + "roleName" + "\\}", apiClient.escapeString(roleName.toString()))
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()));

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
    private com.squareup.okhttp.Call deleteRoleSUserValidateBeforeCall(String roleName, Long userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'roleName' is set
        if (roleName == null) {
            throw new ApiException("Missing the required parameter 'roleName' when calling deleteRoleSUser(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling deleteRoleSUser(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteRoleSUserCall(roleName, userId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete a suser from a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteRoleSUser(String roleName, Long userId) throws ApiException {
        ApiResponse<Status> resp = deleteRoleSUserWithHttpInfo(roleName, userId);
        return resp.getData();
    }

    /**
     * Delete a suser from a role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteRoleSUserWithHttpInfo(String roleName, Long userId) throws ApiException {
        com.squareup.okhttp.Call call = deleteRoleSUserValidateBeforeCall(roleName, userId, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete a suser from a role. (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName global role (required)
     * @param userId suser&#x27;s id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteRoleSUserAsync(String roleName, Long userId, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteRoleSUserValidateBeforeCall(roleName, userId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRoleAssignmentSGroups
     * @param roleName the role name (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRoleAssignmentSGroupsCall(String roleName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles/{roleName}/groups"
            .replaceAll("\\{" + "roleName" + "\\}", apiClient.escapeString(roleName.toString()));

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
    private com.squareup.okhttp.Call getRoleAssignmentSGroupsValidateBeforeCall(String roleName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'roleName' is set
        if (roleName == null) {
            throw new ApiException("Missing the required parameter 'roleName' when calling getRoleAssignmentSGroups(Async)");
        }
        
        com.squareup.okhttp.Call call = getRoleAssignmentSGroupsCall(roleName, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get global role assignment for groups for specific role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @return Groups
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Groups getRoleAssignmentSGroups(String roleName) throws ApiException {
        ApiResponse<Groups> resp = getRoleAssignmentSGroupsWithHttpInfo(roleName);
        return resp.getData();
    }

    /**
     * Get global role assignment for groups for specific role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @return ApiResponse&lt;Groups&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Groups> getRoleAssignmentSGroupsWithHttpInfo(String roleName) throws ApiException {
        com.squareup.okhttp.Call call = getRoleAssignmentSGroupsValidateBeforeCall(roleName, null, null);
        Type localVarReturnType = new TypeToken<Groups>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get global role assignment for groups for specific role. (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRoleAssignmentSGroupsAsync(String roleName, final ApiCallback<Groups> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRoleAssignmentSGroupsValidateBeforeCall(roleName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Groups>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRoleAssignmentSUsers
     * @param roleName the role name (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRoleAssignmentSUsersCall(String roleName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles/{roleName}/users"
            .replaceAll("\\{" + "roleName" + "\\}", apiClient.escapeString(roleName.toString()));

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
    private com.squareup.okhttp.Call getRoleAssignmentSUsersValidateBeforeCall(String roleName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'roleName' is set
        if (roleName == null) {
            throw new ApiException("Missing the required parameter 'roleName' when calling getRoleAssignmentSUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = getRoleAssignmentSUsersCall(roleName, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get global role assignment for users for specific role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getRoleAssignmentSUsers(String roleName) throws ApiException {
        ApiResponse<Users> resp = getRoleAssignmentSUsersWithHttpInfo(roleName);
        return resp.getData();
    }

    /**
     * Get global role assignment for users for specific role.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getRoleAssignmentSUsersWithHttpInfo(String roleName) throws ApiException {
        com.squareup.okhttp.Call call = getRoleAssignmentSUsersValidateBeforeCall(roleName, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get global role assignment for users for specific role. (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRoleAssignmentSUsersAsync(String roleName, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRoleAssignmentSUsersValidateBeforeCall(roleName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRoleAssignmentSUsers_0
     * @param roleName the role name (required)
     * @param userId the User Unique Identifier name (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRoleAssignmentSUsers_0Call(String roleName, String userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles/{roleName}/users/{userId}"
            .replaceAll("\\{" + "roleName" + "\\}", apiClient.escapeString(roleName.toString()))
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()));

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
    private com.squareup.okhttp.Call getRoleAssignmentSUsers_0ValidateBeforeCall(String roleName, String userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'roleName' is set
        if (roleName == null) {
            throw new ApiException("Missing the required parameter 'roleName' when calling getRoleAssignmentSUsers_0(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getRoleAssignmentSUsers_0(Async)");
        }
        
        com.squareup.okhttp.Call call = getRoleAssignmentSUsers_0Call(roleName, userId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Check user is in global role
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @param userId the User Unique Identifier name (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRoleAssignmentSUsers_0(String roleName, String userId) throws ApiException {
        ApiResponse<Response> resp = getRoleAssignmentSUsers_0WithHttpInfo(roleName, userId);
        return resp.getData();
    }

    /**
     * Check user is in global role
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @param userId the User Unique Identifier name (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRoleAssignmentSUsers_0WithHttpInfo(String roleName, String userId) throws ApiException {
        com.squareup.okhttp.Call call = getRoleAssignmentSUsers_0ValidateBeforeCall(roleName, userId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Check user is in global role (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param roleName the role name (required)
     * @param userId the User Unique Identifier name (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRoleAssignmentSUsers_0Async(String roleName, String userId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRoleAssignmentSUsers_0ValidateBeforeCall(roleName, userId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRoles
     * @param includeAssignments boolean if true, add assignments to the return (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRolesCall(Boolean includeAssignments, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/auth/roles";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (includeAssignments != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("includeAssignments", includeAssignments));

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
    private com.squareup.okhttp.Call getRolesValidateBeforeCall(Boolean includeAssignments, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getRolesCall(includeAssignments, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get all global roles.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param includeAssignments boolean if true, add assignments to the return (optional, default to false)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getRoles(Boolean includeAssignments) throws ApiException {
        ApiResponse<Roles> resp = getRolesWithHttpInfo(includeAssignments);
        return resp.getData();
    }

    /**
     * Get all global roles.
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param includeAssignments boolean if true, add assignments to the return (optional, default to false)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getRolesWithHttpInfo(Boolean includeAssignments) throws ApiException {
        com.squareup.okhttp.Call call = getRolesValidateBeforeCall(includeAssignments, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get all global roles. (asynchronously)
     * Accepted Roles: * Analytics Bootstrap Administrator * Customer Administrator * SaaS OPS Administrator * Security Officer 
     * @param includeAssignments boolean if true, add assignments to the return (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRolesAsync(Boolean includeAssignments, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRolesValidateBeforeCall(includeAssignments, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
