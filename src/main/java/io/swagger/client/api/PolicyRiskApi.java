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
import io.swagger.client.model.ModelConfiguration;
import io.swagger.client.model.ModelImport;
import io.swagger.client.model.Response;
import io.swagger.client.model.RiskScoreCfg;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyRiskApi {
    private ApiClient apiClient;

    public PolicyRiskApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyRiskApi(ApiClient apiClient) {
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
        String localVarPath = "/policy/risk/download/{id}"
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
     * Accepted Roles: * Customer Administrator 
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
     * Accepted Roles: * Customer Administrator 
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
     * Accepted Roles: * Customer Administrator 
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
     * Build call for createRiskScoreConfiguration
     * @param body - risk score configuration node to create (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createRiskScoreConfigurationCall(RiskScoreCfg body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/risk/cfg";

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
    private com.squareup.okhttp.Call createRiskScoreConfigurationValidateBeforeCall(RiskScoreCfg body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createRiskScoreConfiguration(Async)");
        }
        
        com.squareup.okhttp.Call call = createRiskScoreConfigurationCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create risk score configuration
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - risk score configuration node to create (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response createRiskScoreConfiguration(RiskScoreCfg body) throws ApiException {
        ApiResponse<Response> resp = createRiskScoreConfigurationWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create risk score configuration
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - risk score configuration node to create (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> createRiskScoreConfigurationWithHttpInfo(RiskScoreCfg body) throws ApiException {
        com.squareup.okhttp.Call call = createRiskScoreConfigurationValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create risk score configuration (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - risk score configuration node to create (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createRiskScoreConfigurationAsync(RiskScoreCfg body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createRiskScoreConfigurationValidateBeforeCall(body, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/risk/download/{id}"
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
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
     * Build call for getDefaultRiskScoreConfiguration
     * @param entityType - entityType                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDefaultRiskScoreConfigurationCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/cfg/{entityType}/default"
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
    private com.squareup.okhttp.Call getDefaultRiskScoreConfigurationValidateBeforeCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getDefaultRiskScoreConfiguration(Async)");
        }
        
        com.squareup.okhttp.Call call = getDefaultRiskScoreConfigurationCall(entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get default risk score policy configuration for a spcified entity type
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDefaultRiskScoreConfiguration(String entityType) throws ApiException {
        ApiResponse<Response> resp = getDefaultRiskScoreConfigurationWithHttpInfo(entityType);
        return resp.getData();
    }

    /**
     * Get default risk score policy configuration for a spcified entity type
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDefaultRiskScoreConfigurationWithHttpInfo(String entityType) throws ApiException {
        com.squareup.okhttp.Call call = getDefaultRiskScoreConfigurationValidateBeforeCall(entityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get default risk score policy configuration for a spcified entity type (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDefaultRiskScoreConfigurationAsync(String entityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDefaultRiskScoreConfigurationValidateBeforeCall(entityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskFactorScores
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskFactorScoresCall(String entityType, String entityUniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/score/factors/{entityType}/{entityUniqueId}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "entityUniqueId" + "\\}", apiClient.escapeString(entityUniqueId.toString()));

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
    private com.squareup.okhttp.Call getRiskFactorScoresValidateBeforeCall(String entityType, String entityUniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskFactorScores(Async)");
        }
        // verify the required parameter 'entityUniqueId' is set
        if (entityUniqueId == null) {
            throw new ApiException("Missing the required parameter 'entityUniqueId' when calling getRiskFactorScores(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskFactorScoresCall(entityType, entityUniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get risk score for a specified entity
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskFactorScores(String entityType, String entityUniqueId) throws ApiException {
        ApiResponse<Response> resp = getRiskFactorScoresWithHttpInfo(entityType, entityUniqueId);
        return resp.getData();
    }

    /**
     * Get risk score for a specified entity
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskFactorScoresWithHttpInfo(String entityType, String entityUniqueId) throws ApiException {
        com.squareup.okhttp.Call call = getRiskFactorScoresValidateBeforeCall(entityType, entityUniqueId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get risk score for a specified entity (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskFactorScoresAsync(String entityType, String entityUniqueId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskFactorScoresValidateBeforeCall(entityType, entityUniqueId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskLevelConfiguration
     * @param locale locale to fetch the risk level labels in. The preferred locale of                    the logged in user (or the default locale if preferred locale is                    not set) will be used in case the locale code is not passed. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskLevelConfigurationCall(String locale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/levels";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (locale != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("locale", locale));

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
    private com.squareup.okhttp.Call getRiskLevelConfigurationValidateBeforeCall(String locale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getRiskLevelConfigurationCall(locale, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get risk level configuration.
     * This information is used to  drive the risk levels and currency settings of the Identity Governance catalog.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param locale locale to fetch the risk level labels in. The preferred locale of                    the logged in user (or the default locale if preferred locale is                    not set) will be used in case the locale code is not passed. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskLevelConfiguration(String locale) throws ApiException {
        ApiResponse<Response> resp = getRiskLevelConfigurationWithHttpInfo(locale);
        return resp.getData();
    }

    /**
     * Get risk level configuration.
     * This information is used to  drive the risk levels and currency settings of the Identity Governance catalog.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param locale locale to fetch the risk level labels in. The preferred locale of                    the logged in user (or the default locale if preferred locale is                    not set) will be used in case the locale code is not passed. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskLevelConfigurationWithHttpInfo(String locale) throws ApiException {
        com.squareup.okhttp.Call call = getRiskLevelConfigurationValidateBeforeCall(locale, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get risk level configuration. (asynchronously)
     * This information is used to  drive the risk levels and currency settings of the Identity Governance catalog.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param locale locale to fetch the risk level labels in. The preferred locale of                    the logged in user (or the default locale if preferred locale is                    not set) will be used in case the locale code is not passed. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskLevelConfigurationAsync(String locale, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskLevelConfigurationValidateBeforeCall(locale, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskLevelCounts
     * @param propertyClass The class name to calculate risk counts for (User or Application) (required)
     * @param countCalculated if true, get count from risk calculations.  If false, get count using &#x27;risk&#x27; column in suser table. default is true (optional, default to true)
     * @param locale locale to fetch the risk level labels in. The preferred locale of                         the logged in user (or the default locale if preferred locale is                         not set) will be used in case the locale code is not passed. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskLevelCountsCall(String propertyClass, Boolean countCalculated, String locale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/levels/count/{class}"
            .replaceAll("\\{" + "class" + "\\}", apiClient.escapeString(propertyClass.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (countCalculated != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("countCalculated", countCalculated));
        if (locale != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("locale", locale));

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
    private com.squareup.okhttp.Call getRiskLevelCountsValidateBeforeCall(String propertyClass, Boolean countCalculated, String locale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'propertyClass' is set
        if (propertyClass == null) {
            throw new ApiException("Missing the required parameter 'propertyClass' when calling getRiskLevelCounts(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskLevelCountsCall(propertyClass, countCalculated, locale, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the count of entities within each risk level.
     * Accepted Roles: * Auditor * Authenticated User * Customer Administrator * Data Administrator 
     * @param propertyClass The class name to calculate risk counts for (User or Application) (required)
     * @param countCalculated if true, get count from risk calculations.  If false, get count using &#x27;risk&#x27; column in suser table. default is true (optional, default to true)
     * @param locale locale to fetch the risk level labels in. The preferred locale of                         the logged in user (or the default locale if preferred locale is                         not set) will be used in case the locale code is not passed. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskLevelCounts(String propertyClass, Boolean countCalculated, String locale) throws ApiException {
        ApiResponse<Response> resp = getRiskLevelCountsWithHttpInfo(propertyClass, countCalculated, locale);
        return resp.getData();
    }

    /**
     * Get the count of entities within each risk level.
     * Accepted Roles: * Auditor * Authenticated User * Customer Administrator * Data Administrator 
     * @param propertyClass The class name to calculate risk counts for (User or Application) (required)
     * @param countCalculated if true, get count from risk calculations.  If false, get count using &#x27;risk&#x27; column in suser table. default is true (optional, default to true)
     * @param locale locale to fetch the risk level labels in. The preferred locale of                         the logged in user (or the default locale if preferred locale is                         not set) will be used in case the locale code is not passed. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskLevelCountsWithHttpInfo(String propertyClass, Boolean countCalculated, String locale) throws ApiException {
        com.squareup.okhttp.Call call = getRiskLevelCountsValidateBeforeCall(propertyClass, countCalculated, locale, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the count of entities within each risk level. (asynchronously)
     * Accepted Roles: * Auditor * Authenticated User * Customer Administrator * Data Administrator 
     * @param propertyClass The class name to calculate risk counts for (User or Application) (required)
     * @param countCalculated if true, get count from risk calculations.  If false, get count using &#x27;risk&#x27; column in suser table. default is true (optional, default to true)
     * @param locale locale to fetch the risk level labels in. The preferred locale of                         the logged in user (or the default locale if preferred locale is                         not set) will be used in case the locale code is not passed. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskLevelCountsAsync(String propertyClass, Boolean countCalculated, String locale, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskLevelCountsValidateBeforeCall(propertyClass, countCalculated, locale, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskScoreCalculationStatus
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreCalculationStatusCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/score/{entityType}"
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
    private com.squareup.okhttp.Call getRiskScoreCalculationStatusValidateBeforeCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskScoreCalculationStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskScoreCalculationStatusCall(entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a risk score calculation status for a specified entity type.
     * Actual risk score value will be populated only when the entity type is GOVERNANCE&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskScoreCalculationStatus(String entityType) throws ApiException {
        ApiResponse<Response> resp = getRiskScoreCalculationStatusWithHttpInfo(entityType);
        return resp.getData();
    }

    /**
     * Get a risk score calculation status for a specified entity type.
     * Actual risk score value will be populated only when the entity type is GOVERNANCE&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskScoreCalculationStatusWithHttpInfo(String entityType) throws ApiException {
        com.squareup.okhttp.Call call = getRiskScoreCalculationStatusValidateBeforeCall(entityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a risk score calculation status for a specified entity type. (asynchronously)
     * Actual risk score value will be populated only when the entity type is GOVERNANCE&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreCalculationStatusAsync(String entityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskScoreCalculationStatusValidateBeforeCall(entityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskScoreCalculationStatusByStatusType
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt; (required)
     * @param riskScoreStatusType - risk calc status can be one of the following                    &lt;li&gt;COMPLETED&lt;/li&gt;                    &lt;li&gt;CANCELED&lt;/li&gt;                    &lt;li&gt;ERROR&lt;/li&gt;                    &lt;li&gt;IN_PROGRESS&lt;/li&gt;                    &lt;li&gt;REQUESTED&lt;/li&gt;                    &lt;li&gt;SKIPPED&lt;/li&gt; (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreCalculationStatusByStatusTypeCall(String entityType, String riskScoreStatusType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/score/{entityType}/status/{riskScoreStatusType}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "riskScoreStatusType" + "\\}", apiClient.escapeString(riskScoreStatusType.toString()));

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
    private com.squareup.okhttp.Call getRiskScoreCalculationStatusByStatusTypeValidateBeforeCall(String entityType, String riskScoreStatusType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskScoreCalculationStatusByStatusType(Async)");
        }
        // verify the required parameter 'riskScoreStatusType' is set
        if (riskScoreStatusType == null) {
            throw new ApiException("Missing the required parameter 'riskScoreStatusType' when calling getRiskScoreCalculationStatusByStatusType(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskScoreCalculationStatusByStatusTypeCall(entityType, riskScoreStatusType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a risk score calculation status for a specified entity type.
     * Actual risk score value will be populated only when the entity type is GOVERNANCE&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt; (required)
     * @param riskScoreStatusType - risk calc status can be one of the following                    &lt;li&gt;COMPLETED&lt;/li&gt;                    &lt;li&gt;CANCELED&lt;/li&gt;                    &lt;li&gt;ERROR&lt;/li&gt;                    &lt;li&gt;IN_PROGRESS&lt;/li&gt;                    &lt;li&gt;REQUESTED&lt;/li&gt;                    &lt;li&gt;SKIPPED&lt;/li&gt; (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskScoreCalculationStatusByStatusType(String entityType, String riskScoreStatusType) throws ApiException {
        ApiResponse<Response> resp = getRiskScoreCalculationStatusByStatusTypeWithHttpInfo(entityType, riskScoreStatusType);
        return resp.getData();
    }

    /**
     * Get a risk score calculation status for a specified entity type.
     * Actual risk score value will be populated only when the entity type is GOVERNANCE&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt; (required)
     * @param riskScoreStatusType - risk calc status can be one of the following                    &lt;li&gt;COMPLETED&lt;/li&gt;                    &lt;li&gt;CANCELED&lt;/li&gt;                    &lt;li&gt;ERROR&lt;/li&gt;                    &lt;li&gt;IN_PROGRESS&lt;/li&gt;                    &lt;li&gt;REQUESTED&lt;/li&gt;                    &lt;li&gt;SKIPPED&lt;/li&gt; (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskScoreCalculationStatusByStatusTypeWithHttpInfo(String entityType, String riskScoreStatusType) throws ApiException {
        com.squareup.okhttp.Call call = getRiskScoreCalculationStatusByStatusTypeValidateBeforeCall(entityType, riskScoreStatusType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a risk score calculation status for a specified entity type. (asynchronously)
     * Actual risk score value will be populated only when the entity type is GOVERNANCE&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt; (required)
     * @param riskScoreStatusType - risk calc status can be one of the following                    &lt;li&gt;COMPLETED&lt;/li&gt;                    &lt;li&gt;CANCELED&lt;/li&gt;                    &lt;li&gt;ERROR&lt;/li&gt;                    &lt;li&gt;IN_PROGRESS&lt;/li&gt;                    &lt;li&gt;REQUESTED&lt;/li&gt;                    &lt;li&gt;SKIPPED&lt;/li&gt; (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreCalculationStatusByStatusTypeAsync(String entityType, String riskScoreStatusType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskScoreCalculationStatusByStatusTypeValidateBeforeCall(entityType, riskScoreStatusType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskScoreCalculationStatus_0
     * @param entityType - entity type to calculate risk score for                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entity unique id, for example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreCalculationStatus_0Call(String entityType, String entityUniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/score/{entityType}/{entityUniqueId}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "entityUniqueId" + "\\}", apiClient.escapeString(entityUniqueId.toString()));

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
    private com.squareup.okhttp.Call getRiskScoreCalculationStatus_0ValidateBeforeCall(String entityType, String entityUniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskScoreCalculationStatus_0(Async)");
        }
        // verify the required parameter 'entityUniqueId' is set
        if (entityUniqueId == null) {
            throw new ApiException("Missing the required parameter 'entityUniqueId' when calling getRiskScoreCalculationStatus_0(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskScoreCalculationStatus_0Call(entityType, entityUniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a risk score for a specified entity
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entity unique id, for example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskScoreCalculationStatus_0(String entityType, String entityUniqueId) throws ApiException {
        ApiResponse<Response> resp = getRiskScoreCalculationStatus_0WithHttpInfo(entityType, entityUniqueId);
        return resp.getData();
    }

    /**
     * Get a risk score for a specified entity
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entity unique id, for example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskScoreCalculationStatus_0WithHttpInfo(String entityType, String entityUniqueId) throws ApiException {
        com.squareup.okhttp.Call call = getRiskScoreCalculationStatus_0ValidateBeforeCall(entityType, entityUniqueId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a risk score for a specified entity (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type to calculate risk score for                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entity unique id, for example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreCalculationStatus_0Async(String entityType, String entityUniqueId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskScoreCalculationStatus_0ValidateBeforeCall(entityType, entityUniqueId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskScoreConfiguration
     * @param entityType - entityType (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreConfigurationCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/cfg/{entityType}"
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
    private com.squareup.okhttp.Call getRiskScoreConfigurationValidateBeforeCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskScoreConfiguration(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskScoreConfigurationCall(entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get risk score policy for a specified entity type  Entity type can be one of the following:  &lt;li&gt;GOVERNANCE&lt;/li&gt;  &lt;li&gt;APPLICATION&lt;/li&gt;  &lt;li&gt;USER&lt;/li&gt;
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskScoreConfiguration(String entityType) throws ApiException {
        ApiResponse<Response> resp = getRiskScoreConfigurationWithHttpInfo(entityType);
        return resp.getData();
    }

    /**
     * Get risk score policy for a specified entity type  Entity type can be one of the following:  &lt;li&gt;GOVERNANCE&lt;/li&gt;  &lt;li&gt;APPLICATION&lt;/li&gt;  &lt;li&gt;USER&lt;/li&gt;
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskScoreConfigurationWithHttpInfo(String entityType) throws ApiException {
        com.squareup.okhttp.Call call = getRiskScoreConfigurationValidateBeforeCall(entityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get risk score policy for a specified entity type  Entity type can be one of the following:  &lt;li&gt;GOVERNANCE&lt;/li&gt;  &lt;li&gt;APPLICATION&lt;/li&gt;  &lt;li&gt;USER&lt;/li&gt; (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskScoreConfigurationAsync(String entityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskScoreConfigurationValidateBeforeCall(entityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskScores
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskScoresCall(String entityType, String entityUniqueId, Integer numDataPoints, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/scores/{entityType}/{entityUniqueId}/{numDataPoints}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "entityUniqueId" + "\\}", apiClient.escapeString(entityUniqueId.toString()))
            .replaceAll("\\{" + "numDataPoints" + "\\}", apiClient.escapeString(numDataPoints.toString()));

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
    private com.squareup.okhttp.Call getRiskScoresValidateBeforeCall(String entityType, String entityUniqueId, Integer numDataPoints, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskScores(Async)");
        }
        // verify the required parameter 'entityUniqueId' is set
        if (entityUniqueId == null) {
            throw new ApiException("Missing the required parameter 'entityUniqueId' when calling getRiskScores(Async)");
        }
        // verify the required parameter 'numDataPoints' is set
        if (numDataPoints == null) {
            throw new ApiException("Missing the required parameter 'numDataPoints' when calling getRiskScores(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskScoresCall(entityType, entityUniqueId, numDataPoints, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity limited by number of data points
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskScores(String entityType, String entityUniqueId, Integer numDataPoints) throws ApiException {
        ApiResponse<Response> resp = getRiskScoresWithHttpInfo(entityType, entityUniqueId, numDataPoints);
        return resp.getData();
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity limited by number of data points
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskScoresWithHttpInfo(String entityType, String entityUniqueId, Integer numDataPoints) throws ApiException {
        com.squareup.okhttp.Call call = getRiskScoresValidateBeforeCall(entityType, entityUniqueId, numDataPoints, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity limited by number of data points (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskScoresAsync(String entityType, String entityUniqueId, Integer numDataPoints, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskScoresValidateBeforeCall(entityType, entityUniqueId, numDataPoints, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskScoresForSpecificDate
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @param targetCalcDate - used to limit results to this target calculation date. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskScoresForSpecificDateCall(String entityType, String entityUniqueId, Integer numDataPoints, Long targetCalcDate, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/scores/{entityType}/{entityUniqueId}/{numDataPoints}/date/{targetCalcDate}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "entityUniqueId" + "\\}", apiClient.escapeString(entityUniqueId.toString()))
            .replaceAll("\\{" + "numDataPoints" + "\\}", apiClient.escapeString(numDataPoints.toString()))
            .replaceAll("\\{" + "targetCalcDate" + "\\}", apiClient.escapeString(targetCalcDate.toString()));

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
    private com.squareup.okhttp.Call getRiskScoresForSpecificDateValidateBeforeCall(String entityType, String entityUniqueId, Integer numDataPoints, Long targetCalcDate, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskScoresForSpecificDate(Async)");
        }
        // verify the required parameter 'entityUniqueId' is set
        if (entityUniqueId == null) {
            throw new ApiException("Missing the required parameter 'entityUniqueId' when calling getRiskScoresForSpecificDate(Async)");
        }
        // verify the required parameter 'numDataPoints' is set
        if (numDataPoints == null) {
            throw new ApiException("Missing the required parameter 'numDataPoints' when calling getRiskScoresForSpecificDate(Async)");
        }
        // verify the required parameter 'targetCalcDate' is set
        if (targetCalcDate == null) {
            throw new ApiException("Missing the required parameter 'targetCalcDate' when calling getRiskScoresForSpecificDate(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskScoresForSpecificDateCall(entityType, entityUniqueId, numDataPoints, targetCalcDate, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity limited by number of data points and target calculation date
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @param targetCalcDate - used to limit results to this target calculation date. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskScoresForSpecificDate(String entityType, String entityUniqueId, Integer numDataPoints, Long targetCalcDate) throws ApiException {
        ApiResponse<Response> resp = getRiskScoresForSpecificDateWithHttpInfo(entityType, entityUniqueId, numDataPoints, targetCalcDate);
        return resp.getData();
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity limited by number of data points and target calculation date
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @param targetCalcDate - used to limit results to this target calculation date. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskScoresForSpecificDateWithHttpInfo(String entityType, String entityUniqueId, Integer numDataPoints, Long targetCalcDate) throws ApiException {
        com.squareup.okhttp.Call call = getRiskScoresForSpecificDateValidateBeforeCall(entityType, entityUniqueId, numDataPoints, targetCalcDate, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity limited by number of data points and target calculation date (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param numDataPoints - number of data points to return. (required)
     * @param targetCalcDate - used to limit results to this target calculation date. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskScoresForSpecificDateAsync(String entityType, String entityUniqueId, Integer numDataPoints, Long targetCalcDate, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskScoresForSpecificDateValidateBeforeCall(entityType, entityUniqueId, numDataPoints, targetCalcDate, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRiskScoresInDateRange
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param startTime - start calculation time (optional)
     * @param endTime - end calculation time (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRiskScoresInDateRangeCall(String entityType, String entityUniqueId, Long startTime, Long endTime, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/scores/{entityType}/{entityUniqueId}/all"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "entityUniqueId" + "\\}", apiClient.escapeString(entityUniqueId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (startTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startTime", startTime));
        if (endTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("endTime", endTime));

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
    private com.squareup.okhttp.Call getRiskScoresInDateRangeValidateBeforeCall(String entityType, String entityUniqueId, Long startTime, Long endTime, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getRiskScoresInDateRange(Async)");
        }
        // verify the required parameter 'entityUniqueId' is set
        if (entityUniqueId == null) {
            throw new ApiException("Missing the required parameter 'entityUniqueId' when calling getRiskScoresInDateRange(Async)");
        }
        
        com.squareup.okhttp.Call call = getRiskScoresInDateRangeCall(entityType, entityUniqueId, startTime, endTime, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity within the specific date time range
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param startTime - start calculation time (optional)
     * @param endTime - end calculation time (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRiskScoresInDateRange(String entityType, String entityUniqueId, Long startTime, Long endTime) throws ApiException {
        ApiResponse<Response> resp = getRiskScoresInDateRangeWithHttpInfo(entityType, entityUniqueId, startTime, endTime);
        return resp.getData();
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity within the specific date time range
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param startTime - start calculation time (optional)
     * @param endTime - end calculation time (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRiskScoresInDateRangeWithHttpInfo(String entityType, String entityUniqueId, Long startTime, Long endTime) throws ApiException {
        com.squareup.okhttp.Call call = getRiskScoresInDateRangeValidateBeforeCall(entityType, entityUniqueId, startTime, endTime, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a list of previously calculated risk scores for a specified entity within the specific date time range (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entityType                        Entity type can be one of the following                        &lt;li&gt;GOVENTANCE&lt;/li&gt;                        &lt;li&gt;APPLICATION&lt;/li&gt;                        &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param entityUniqueId - entityUniqueId. For example unique id of a user or application.                        In case of GOVERNANCE entity type unique id should be set to GOVERNANCE (required)
     * @param startTime - start calculation time (optional)
     * @param endTime - end calculation time (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRiskScoresInDateRangeAsync(String entityType, String entityUniqueId, Long startTime, Long endTime, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRiskScoresInDateRangeValidateBeforeCall(entityType, entityUniqueId, startTime, endTime, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importPreview
     * @param body - The import data (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importPreviewCall(ModelImport body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/risk/import/preview";

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
    private com.squareup.okhttp.Call importPreviewValidateBeforeCall(ModelImport body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importPreview(Async)");
        }
        
        com.squareup.okhttp.Call call = importPreviewCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importPreview(ModelImport body) throws ApiException {
        ApiResponse<Response> resp = importPreviewWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importPreviewWithHttpInfo(ModelImport body) throws ApiException {
        com.squareup.okhttp.Call call = importPreviewValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importPreviewAsync(ModelImport body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importPreviewValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importResolveApplications
     * @param body - The import data (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importResolveApplicationsCall(ModelImport body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/risk/import/resolve/apps";

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
    private com.squareup.okhttp.Call importResolveApplicationsValidateBeforeCall(ModelImport body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importResolveApplications(Async)");
        }
        
        com.squareup.okhttp.Call call = importResolveApplicationsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importResolveApplications(ModelImport body) throws ApiException {
        ApiResponse<Response> resp = importResolveApplicationsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Perform an import which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importResolveApplicationsWithHttpInfo(ModelImport body) throws ApiException {
        com.squareup.okhttp.Call call = importResolveApplicationsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importResolveApplicationsAsync(ModelImport body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importResolveApplicationsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importRiskPolicies
     * @param body - The import data (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importRiskPoliciesCall(ModelImport body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/risk/import";

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
    private com.squareup.okhttp.Call importRiskPoliciesValidateBeforeCall(ModelImport body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importRiskPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = importRiskPoliciesCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importRiskPolicies(ModelImport body) throws ApiException {
        ApiResponse<Response> resp = importRiskPoliciesWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importRiskPoliciesWithHttpInfo(ModelImport body) throws ApiException {
        com.squareup.okhttp.Call call = importRiskPoliciesValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - The import data (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importRiskPoliciesAsync(ModelImport body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importRiskPoliciesValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for requestRiskScoreCalculation
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call requestRiskScoreCalculationCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/score/{entityType}"
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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call requestRiskScoreCalculationValidateBeforeCall(String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling requestRiskScoreCalculation(Async)");
        }
        
        com.squareup.okhttp.Call call = requestRiskScoreCalculationCall(entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start risk score calculation on demand
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response requestRiskScoreCalculation(String entityType) throws ApiException {
        ApiResponse<Response> resp = requestRiskScoreCalculationWithHttpInfo(entityType);
        return resp.getData();
    }

    /**
     * Start risk score calculation on demand
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> requestRiskScoreCalculationWithHttpInfo(String entityType) throws ApiException {
        com.squareup.okhttp.Call call = requestRiskScoreCalculationValidateBeforeCall(entityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start risk score calculation on demand (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call requestRiskScoreCalculationAsync(String entityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = requestRiskScoreCalculationValidateBeforeCall(entityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param exportRiskUserAttrDef - Flag to include user attribute definition referenced by &#x27;User Risk Score Policy&#x27; (optional, default to false)
     * @param exportRiskAppAttrDef - Flag to include application attribute definition referenced by &#x27;Application Risk Score Policy&#x27; (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (exportRiskUserAttrDef != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("exportRiskUserAttrDef", exportRiskUserAttrDef));
        if (exportRiskAppAttrDef != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("exportRiskAppAttrDef", exportRiskAppAttrDef));

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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startDownloadCall(exportRiskUserAttrDef, exportRiskAppAttrDef, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start downloading Risk Policies  - Risk Level Configuration  - Governance Risk Score Policy  - Application Risk Score Policy  - User Risk Score Policy  - Risk Score Schedule Policy  - User attribute definition referenced by &#x27;User Risk Score Policy&#x27;  - Application attribute definition referenced by &#x27;Application Risk Score Policy&#x27;
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param exportRiskUserAttrDef - Flag to include user attribute definition referenced by &#x27;User Risk Score Policy&#x27; (optional, default to false)
     * @param exportRiskAppAttrDef - Flag to include application attribute definition referenced by &#x27;Application Risk Score Policy&#x27; (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(exportRiskUserAttrDef, exportRiskAppAttrDef);
        return resp.getData();
    }

    /**
     * Start downloading Risk Policies  - Risk Level Configuration  - Governance Risk Score Policy  - Application Risk Score Policy  - User Risk Score Policy  - Risk Score Schedule Policy  - User attribute definition referenced by &#x27;User Risk Score Policy&#x27;  - Application attribute definition referenced by &#x27;Application Risk Score Policy&#x27;
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param exportRiskUserAttrDef - Flag to include user attribute definition referenced by &#x27;User Risk Score Policy&#x27; (optional, default to false)
     * @param exportRiskAppAttrDef - Flag to include application attribute definition referenced by &#x27;Application Risk Score Policy&#x27; (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(exportRiskUserAttrDef, exportRiskAppAttrDef, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start downloading Risk Policies  - Risk Level Configuration  - Governance Risk Score Policy  - Application Risk Score Policy  - User Risk Score Policy  - Risk Score Schedule Policy  - User attribute definition referenced by &#x27;User Risk Score Policy&#x27;  - Application attribute definition referenced by &#x27;Application Risk Score Policy&#x27; (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param exportRiskUserAttrDef - Flag to include user attribute definition referenced by &#x27;User Risk Score Policy&#x27; (optional, default to false)
     * @param exportRiskAppAttrDef - Flag to include application attribute definition referenced by &#x27;Application Risk Score Policy&#x27; (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(exportRiskUserAttrDef, exportRiskAppAttrDef, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_0
     * @param body -  The download request node (required)
     * @param exportRiskUserAttrDef - Export user attribute defs (optional, default to false)
     * @param exportRiskAppAttrDef - Export application attribute defs (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Call(Download body, Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/risk/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (exportRiskUserAttrDef != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("exportRiskUserAttrDef", exportRiskUserAttrDef));
        if (exportRiskAppAttrDef != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("exportRiskAppAttrDef", exportRiskAppAttrDef));

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
    private com.squareup.okhttp.Call startDownload_0ValidateBeforeCall(Download body, Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_0Call(body, exportRiskUserAttrDef, exportRiskAppAttrDef, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start downloading Risk Policies  - Risk Level Configuration  - Governance Risk Score Policy  - Application Risk Score Policy  - User Risk Score Policy  - Risk Score Schedule Policy  - User attribute definition referenced by &#x27;User Risk Score Policy&#x27;
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param body -  The download request node (required)
     * @param exportRiskUserAttrDef - Export user attribute defs (optional, default to false)
     * @param exportRiskAppAttrDef - Export application attribute defs (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body, Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body, exportRiskUserAttrDef, exportRiskAppAttrDef);
        return resp.getData();
    }

    /**
     * Start downloading Risk Policies  - Risk Level Configuration  - Governance Risk Score Policy  - Application Risk Score Policy  - User Risk Score Policy  - Risk Score Schedule Policy  - User attribute definition referenced by &#x27;User Risk Score Policy&#x27;
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param body -  The download request node (required)
     * @param exportRiskUserAttrDef - Export user attribute defs (optional, default to false)
     * @param exportRiskAppAttrDef - Export application attribute defs (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_0WithHttpInfo(Download body, Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, exportRiskUserAttrDef, exportRiskAppAttrDef, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start downloading Risk Policies  - Risk Level Configuration  - Governance Risk Score Policy  - Application Risk Score Policy  - User Risk Score Policy  - Risk Score Schedule Policy  - User attribute definition referenced by &#x27;User Risk Score Policy&#x27; (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
     * @param body -  The download request node (required)
     * @param exportRiskUserAttrDef - Export user attribute defs (optional, default to false)
     * @param exportRiskAppAttrDef - Export application attribute defs (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Async(Download body, Boolean exportRiskUserAttrDef, Boolean exportRiskAppAttrDef, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, exportRiskUserAttrDef, exportRiskAppAttrDef, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/risk/download/{id}/status"
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator 
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
     * Build call for terminateRiskScoreCalculation
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param calcInstId - calculationInstanceId from getRequest;                       maps to risk_score_task.risk_score_calc_id                       risk_score_task is derived from data_production (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call terminateRiskScoreCalculationCall(String entityType, Long calcInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/risk/score/{entityType}/{calcInstId}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "calcInstId" + "\\}", apiClient.escapeString(calcInstId.toString()));

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
    private com.squareup.okhttp.Call terminateRiskScoreCalculationValidateBeforeCall(String entityType, Long calcInstId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling terminateRiskScoreCalculation(Async)");
        }
        // verify the required parameter 'calcInstId' is set
        if (calcInstId == null) {
            throw new ApiException("Missing the required parameter 'calcInstId' when calling terminateRiskScoreCalculation(Async)");
        }
        
        com.squareup.okhttp.Call call = terminateRiskScoreCalculationCall(entityType, calcInstId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start risk score calculation on demand
     * Accepted Roles: * Customer Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param calcInstId - calculationInstanceId from getRequest;                       maps to risk_score_task.risk_score_calc_id                       risk_score_task is derived from data_production (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response terminateRiskScoreCalculation(String entityType, Long calcInstId) throws ApiException {
        ApiResponse<Response> resp = terminateRiskScoreCalculationWithHttpInfo(entityType, calcInstId);
        return resp.getData();
    }

    /**
     * Start risk score calculation on demand
     * Accepted Roles: * Customer Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param calcInstId - calculationInstanceId from getRequest;                       maps to risk_score_task.risk_score_calc_id                       risk_score_task is derived from data_production (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> terminateRiskScoreCalculationWithHttpInfo(String entityType, Long calcInstId) throws ApiException {
        com.squareup.okhttp.Call call = terminateRiskScoreCalculationValidateBeforeCall(entityType, calcInstId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start risk score calculation on demand (asynchronously)
     * Accepted Roles: * Customer Administrator 
     * @param entityType - entity type to calculate risk score for                    Entity type can be one of the following                    &lt;li&gt;GOVENTANCE&lt;/li&gt;                    &lt;li&gt;APPLICATION&lt;/li&gt;                    &lt;li&gt;USER&lt;/li&gt;USER (required)
     * @param calcInstId - calculationInstanceId from getRequest;                       maps to risk_score_task.risk_score_calc_id                       risk_score_task is derived from data_production (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call terminateRiskScoreCalculationAsync(String entityType, Long calcInstId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = terminateRiskScoreCalculationValidateBeforeCall(entityType, calcInstId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateRiskLevelConfiguration
     * @param body The key/value pairs representing the risk levels and currency
                   settings for the Identity Governance catalog. There are 1-N entry tuples
                   (min/max/label) for each of the risk levels. The content is taken
                   as a whole, so always include every entry, else the missing risk
                   levels will get deleted.
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Label&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Min&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Max&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.currency&lt;/li&gt;
                   &lt;/ul&gt;
                   &lt;br/&gt;
                   The following currency values are supported:
                   &lt;ul&gt;
                   &lt;li&gt;USD: United States Dollar&lt;/li&gt;
                   &lt;li&gt;EUR: Euro Member Countries&lt;/li&gt;
                   &lt;li&gt;GBP: United Kingdom Pound&lt;/li&gt;
                   &lt;li&gt;JPY: Japan Yen&lt;/li&gt;
                   &lt;li&gt;BRL: Brazil Real&lt;/li&gt;
                   &lt;li&gt;CNY: China Yuan Renminbi&lt;/li&gt;
                   &lt;/ul&gt; (required)
     * @param locale locale to update the risk level labels in. The preferred locale of                    the logged in user will be used in case the locale code is not                    passed. (optional)
     * @param reset true to reset (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateRiskLevelConfigurationCall(ModelConfiguration body, String locale, Boolean reset, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/risk/levels";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (locale != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("locale", locale));
        if (reset != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reset", reset));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updateRiskLevelConfigurationValidateBeforeCall(ModelConfiguration body, String locale, Boolean reset, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateRiskLevelConfiguration(Async)");
        }
        
        com.squareup.okhttp.Call call = updateRiskLevelConfigurationCall(body, locale, reset, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update risk level configuration configuration.
     * This information is used  to drive the risk levels and currency settings of the Identity Governance  catalog.  As of now UI restricted to configure 2 to 10 risk levels, but API allows to configure more than 10.  Admin is able to configure more than 10 Risk Levels using PUT /api/policy/risk/levels.  In order for more than 10 risk levels show properly in UI, CSS customization is required.  Functionality to set more than 10 risk levels via REST API may be depreciated in the next release.&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body The key/value pairs representing the risk levels and currency
                   settings for the Identity Governance catalog. There are 1-N entry tuples
                   (min/max/label) for each of the risk levels. The content is taken
                   as a whole, so always include every entry, else the missing risk
                   levels will get deleted.
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Label&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Min&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Max&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.currency&lt;/li&gt;
                   &lt;/ul&gt;
                   &lt;br/&gt;
                   The following currency values are supported:
                   &lt;ul&gt;
                   &lt;li&gt;USD: United States Dollar&lt;/li&gt;
                   &lt;li&gt;EUR: Euro Member Countries&lt;/li&gt;
                   &lt;li&gt;GBP: United Kingdom Pound&lt;/li&gt;
                   &lt;li&gt;JPY: Japan Yen&lt;/li&gt;
                   &lt;li&gt;BRL: Brazil Real&lt;/li&gt;
                   &lt;li&gt;CNY: China Yuan Renminbi&lt;/li&gt;
                   &lt;/ul&gt; (required)
     * @param locale locale to update the risk level labels in. The preferred locale of                    the logged in user will be used in case the locale code is not                    passed. (optional)
     * @param reset true to reset (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateRiskLevelConfiguration(ModelConfiguration body, String locale, Boolean reset) throws ApiException {
        ApiResponse<Response> resp = updateRiskLevelConfigurationWithHttpInfo(body, locale, reset);
        return resp.getData();
    }

    /**
     * Update risk level configuration configuration.
     * This information is used  to drive the risk levels and currency settings of the Identity Governance  catalog.  As of now UI restricted to configure 2 to 10 risk levels, but API allows to configure more than 10.  Admin is able to configure more than 10 Risk Levels using PUT /api/policy/risk/levels.  In order for more than 10 risk levels show properly in UI, CSS customization is required.  Functionality to set more than 10 risk levels via REST API may be depreciated in the next release.&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body The key/value pairs representing the risk levels and currency
                   settings for the Identity Governance catalog. There are 1-N entry tuples
                   (min/max/label) for each of the risk levels. The content is taken
                   as a whole, so always include every entry, else the missing risk
                   levels will get deleted.
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Label&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Min&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Max&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.currency&lt;/li&gt;
                   &lt;/ul&gt;
                   &lt;br/&gt;
                   The following currency values are supported:
                   &lt;ul&gt;
                   &lt;li&gt;USD: United States Dollar&lt;/li&gt;
                   &lt;li&gt;EUR: Euro Member Countries&lt;/li&gt;
                   &lt;li&gt;GBP: United Kingdom Pound&lt;/li&gt;
                   &lt;li&gt;JPY: Japan Yen&lt;/li&gt;
                   &lt;li&gt;BRL: Brazil Real&lt;/li&gt;
                   &lt;li&gt;CNY: China Yuan Renminbi&lt;/li&gt;
                   &lt;/ul&gt; (required)
     * @param locale locale to update the risk level labels in. The preferred locale of                    the logged in user will be used in case the locale code is not                    passed. (optional)
     * @param reset true to reset (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateRiskLevelConfigurationWithHttpInfo(ModelConfiguration body, String locale, Boolean reset) throws ApiException {
        com.squareup.okhttp.Call call = updateRiskLevelConfigurationValidateBeforeCall(body, locale, reset, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update risk level configuration configuration. (asynchronously)
     * This information is used  to drive the risk levels and currency settings of the Identity Governance  catalog.  As of now UI restricted to configure 2 to 10 risk levels, but API allows to configure more than 10.  Admin is able to configure more than 10 Risk Levels using PUT /api/policy/risk/levels.  In order for more than 10 risk levels show properly in UI, CSS customization is required.  Functionality to set more than 10 risk levels via REST API may be depreciated in the next release.&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body The key/value pairs representing the risk levels and currency
                   settings for the Identity Governance catalog. There are 1-N entry tuples
                   (min/max/label) for each of the risk levels. The content is taken
                   as a whole, so always include every entry, else the missing risk
                   levels will get deleted.
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Label&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Min&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.riskLevel.X.Max&lt;/li&gt;
                   &lt;li&gt;com.netiq.iac.persistence.datacuration.currency&lt;/li&gt;
                   &lt;/ul&gt;
                   &lt;br/&gt;
                   The following currency values are supported:
                   &lt;ul&gt;
                   &lt;li&gt;USD: United States Dollar&lt;/li&gt;
                   &lt;li&gt;EUR: Euro Member Countries&lt;/li&gt;
                   &lt;li&gt;GBP: United Kingdom Pound&lt;/li&gt;
                   &lt;li&gt;JPY: Japan Yen&lt;/li&gt;
                   &lt;li&gt;BRL: Brazil Real&lt;/li&gt;
                   &lt;li&gt;CNY: China Yuan Renminbi&lt;/li&gt;
                   &lt;/ul&gt; (required)
     * @param locale locale to update the risk level labels in. The preferred locale of                    the logged in user will be used in case the locale code is not                    passed. (optional)
     * @param reset true to reset (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateRiskLevelConfigurationAsync(ModelConfiguration body, String locale, Boolean reset, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateRiskLevelConfigurationValidateBeforeCall(body, locale, reset, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateRiskScoreConfiguration
     * @param body - risk score configuration node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateRiskScoreConfigurationCall(RiskScoreCfg body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/risk/cfg";

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
    private com.squareup.okhttp.Call updateRiskScoreConfigurationValidateBeforeCall(RiskScoreCfg body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateRiskScoreConfiguration(Async)");
        }
        
        com.squareup.okhttp.Call call = updateRiskScoreConfigurationCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update risk score policy configuration
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - risk score configuration node (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateRiskScoreConfiguration(RiskScoreCfg body) throws ApiException {
        ApiResponse<Response> resp = updateRiskScoreConfigurationWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Update risk score policy configuration
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - risk score configuration node (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateRiskScoreConfigurationWithHttpInfo(RiskScoreCfg body) throws ApiException {
        com.squareup.okhttp.Call call = updateRiskScoreConfigurationValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update risk score policy configuration (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - risk score configuration node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateRiskScoreConfigurationAsync(RiskScoreCfg body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateRiskScoreConfigurationValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
