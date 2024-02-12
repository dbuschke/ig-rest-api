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

package de.araneaconsult.codegen.ig.rest.api;

import de.araneaconsult.codegen.ig.rest.ApiCallback;
import de.araneaconsult.codegen.ig.rest.ApiClient;
import de.araneaconsult.codegen.ig.rest.ApiException;
import de.araneaconsult.codegen.ig.rest.ApiResponse;
import de.araneaconsult.codegen.ig.rest.Configuration;
import de.araneaconsult.codegen.ig.rest.Pair;
import de.araneaconsult.codegen.ig.rest.ProgressRequestBody;
import de.araneaconsult.codegen.ig.rest.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import de.araneaconsult.codegen.ig.rest.model.Dashboard;
import de.araneaconsult.codegen.ig.rest.model.DashboardConfig;
import de.araneaconsult.codegen.ig.rest.model.DashboardConfigListNode;
import de.araneaconsult.codegen.ig.rest.model.DashboardListNode;
import de.araneaconsult.codegen.ig.rest.model.DashboardWidget;
import de.araneaconsult.codegen.ig.rest.model.DashboardWidgetListNode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardApi {
    private ApiClient apiClient;

    public DashboardApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DashboardApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for deleteDashboard
     * @param id - dashboard id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteDashboardCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/{id}"
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
    private com.squareup.okhttp.Call deleteDashboardValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteDashboard(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteDashboardCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - dashboard id (required)
     * @return Dashboard
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Dashboard deleteDashboard(Long id) throws ApiException {
        ApiResponse<Dashboard> resp = deleteDashboardWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - dashboard id (required)
     * @return ApiResponse&lt;Dashboard&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Dashboard> deleteDashboardWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteDashboardValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete dashboard (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - dashboard id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteDashboardAsync(Long id, final ApiCallback<Dashboard> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteDashboardValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteDashboardConfig
     * @param id - config id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteDashboardConfigCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/configs/{id}"
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
    private com.squareup.okhttp.Call deleteDashboardConfigValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteDashboardConfig(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteDashboardConfigCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete config
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - config id (required)
     * @return DashboardConfig
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardConfig deleteDashboardConfig(Long id) throws ApiException {
        ApiResponse<DashboardConfig> resp = deleteDashboardConfigWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete config
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - config id (required)
     * @return ApiResponse&lt;DashboardConfig&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardConfig> deleteDashboardConfigWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteDashboardConfigValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete config (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - config id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteDashboardConfigAsync(Long id, final ApiCallback<DashboardConfig> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteDashboardConfigValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteWidget
     * @param id - widget id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteWidgetCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/widgets/{id}"
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
    private com.squareup.okhttp.Call deleteWidgetValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteWidget(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteWidgetCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete widget
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - widget id (required)
     * @return DashboardWidget
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardWidget deleteWidget(Long id) throws ApiException {
        ApiResponse<DashboardWidget> resp = deleteWidgetWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete widget
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - widget id (required)
     * @return ApiResponse&lt;DashboardWidget&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardWidget> deleteWidgetWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteWidgetValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete widget (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param id - widget id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteWidgetAsync(Long id, final ApiCallback<DashboardWidget> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteWidgetValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getActiveDashboardConfig
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getActiveDashboardConfigCall(Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/activeConfig";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (full != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("full", full));

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
    private com.squareup.okhttp.Call getActiveDashboardConfigValidateBeforeCall(Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getActiveDashboardConfigCall(full, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get active dashboard config
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return DashboardConfig
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardConfig getActiveDashboardConfig(Boolean full) throws ApiException {
        ApiResponse<DashboardConfig> resp = getActiveDashboardConfigWithHttpInfo(full);
        return resp.getData();
    }

    /**
     * Get active dashboard config
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return ApiResponse&lt;DashboardConfig&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardConfig> getActiveDashboardConfigWithHttpInfo(Boolean full) throws ApiException {
        com.squareup.okhttp.Call call = getActiveDashboardConfigValidateBeforeCall(full, null, null);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get active dashboard config (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getActiveDashboardConfigAsync(Boolean full, final ApiCallback<DashboardConfig> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getActiveDashboardConfigValidateBeforeCall(full, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDashboardById
     * @param id - dashboard id (required)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDashboardByIdCall(Long id, Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (full != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("full", full));

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
    private com.squareup.okhttp.Call getDashboardByIdValidateBeforeCall(Long id, Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getDashboardById(Async)");
        }
        
        com.squareup.okhttp.Call call = getDashboardByIdCall(id, full, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get dashboard by id
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - dashboard id (required)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return Dashboard
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Dashboard getDashboardById(Long id, Boolean full) throws ApiException {
        ApiResponse<Dashboard> resp = getDashboardByIdWithHttpInfo(id, full);
        return resp.getData();
    }

    /**
     * Get dashboard by id
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - dashboard id (required)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return ApiResponse&lt;Dashboard&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Dashboard> getDashboardByIdWithHttpInfo(Long id, Boolean full) throws ApiException {
        com.squareup.okhttp.Call call = getDashboardByIdValidateBeforeCall(id, full, null, null);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get dashboard by id (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - dashboard id (required)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDashboardByIdAsync(Long id, Boolean full, final ApiCallback<Dashboard> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDashboardByIdValidateBeforeCall(id, full, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDashboardConfigById
     * @param id - config id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDashboardConfigByIdCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/configs/{id}"
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
    private com.squareup.okhttp.Call getDashboardConfigByIdValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getDashboardConfigById(Async)");
        }
        
        com.squareup.okhttp.Call call = getDashboardConfigByIdCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get dashboard config by id
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - config id (required)
     * @return DashboardConfig
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardConfig getDashboardConfigById(Long id) throws ApiException {
        ApiResponse<DashboardConfig> resp = getDashboardConfigByIdWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get dashboard config by id
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - config id (required)
     * @return ApiResponse&lt;DashboardConfig&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardConfig> getDashboardConfigByIdWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getDashboardConfigByIdValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get dashboard config by id (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - config id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDashboardConfigByIdAsync(Long id, final ApiCallback<DashboardConfig> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDashboardConfigByIdValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDashboardConfigs
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDashboardConfigsCall(Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/configs";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (full != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("full", full));

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
    private com.squareup.okhttp.Call getDashboardConfigsValidateBeforeCall(Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDashboardConfigsCall(full, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get dashboard configs
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return DashboardConfigListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardConfigListNode getDashboardConfigs(Boolean full) throws ApiException {
        ApiResponse<DashboardConfigListNode> resp = getDashboardConfigsWithHttpInfo(full);
        return resp.getData();
    }

    /**
     * Get dashboard configs
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return ApiResponse&lt;DashboardConfigListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardConfigListNode> getDashboardConfigsWithHttpInfo(Boolean full) throws ApiException {
        com.squareup.okhttp.Call call = getDashboardConfigsValidateBeforeCall(full, null, null);
        Type localVarReturnType = new TypeToken<DashboardConfigListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get dashboard configs (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDashboardConfigsAsync(Boolean full, final ApiCallback<DashboardConfigListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDashboardConfigsValidateBeforeCall(full, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardConfigListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDashboards
     * @param dashboardType - filter the dashboards by dashboardType. Valid values: DEFAULT and CUSTOM (optional)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDashboardsCall(String dashboardType, Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (dashboardType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dashboardType", dashboardType));
        if (full != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("full", full));

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
    private com.squareup.okhttp.Call getDashboardsValidateBeforeCall(String dashboardType, Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDashboardsCall(dashboardType, full, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get dashboards
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param dashboardType - filter the dashboards by dashboardType. Valid values: DEFAULT and CUSTOM (optional)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return DashboardListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardListNode getDashboards(String dashboardType, Boolean full) throws ApiException {
        ApiResponse<DashboardListNode> resp = getDashboardsWithHttpInfo(dashboardType, full);
        return resp.getData();
    }

    /**
     * Get dashboards
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param dashboardType - filter the dashboards by dashboardType. Valid values: DEFAULT and CUSTOM (optional)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @return ApiResponse&lt;DashboardListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardListNode> getDashboardsWithHttpInfo(String dashboardType, Boolean full) throws ApiException {
        com.squareup.okhttp.Call call = getDashboardsValidateBeforeCall(dashboardType, full, null, null);
        Type localVarReturnType = new TypeToken<DashboardListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get dashboards (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param dashboardType - filter the dashboards by dashboardType. Valid values: DEFAULT and CUSTOM (optional)
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDashboardsAsync(String dashboardType, Boolean full, final ApiCallback<DashboardListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDashboardsValidateBeforeCall(dashboardType, full, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getWidgetById
     * @param id - widget id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWidgetByIdCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/widgets/{id}"
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
    private com.squareup.okhttp.Call getWidgetByIdValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getWidgetById(Async)");
        }
        
        com.squareup.okhttp.Call call = getWidgetByIdCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get widget by id
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - widget id (required)
     * @return DashboardWidget
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardWidget getWidgetById(Long id) throws ApiException {
        ApiResponse<DashboardWidget> resp = getWidgetByIdWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get widget by id
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - widget id (required)
     * @return ApiResponse&lt;DashboardWidget&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardWidget> getWidgetByIdWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getWidgetByIdValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get widget by id (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id - widget id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWidgetByIdAsync(Long id, final ApiCallback<DashboardWidget> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWidgetByIdValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getWidgets
     * @param onlyCustom - true/false flag to return custom/all widgets (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWidgetsCall(Boolean onlyCustom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/widgets";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (onlyCustom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("onlyCustom", onlyCustom));

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
    private com.squareup.okhttp.Call getWidgetsValidateBeforeCall(Boolean onlyCustom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getWidgetsCall(onlyCustom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get widgets
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param onlyCustom - true/false flag to return custom/all widgets (optional, default to false)
     * @return DashboardWidgetListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardWidgetListNode getWidgets(Boolean onlyCustom) throws ApiException {
        ApiResponse<DashboardWidgetListNode> resp = getWidgetsWithHttpInfo(onlyCustom);
        return resp.getData();
    }

    /**
     * Get widgets
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param onlyCustom - true/false flag to return custom/all widgets (optional, default to false)
     * @return ApiResponse&lt;DashboardWidgetListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardWidgetListNode> getWidgetsWithHttpInfo(Boolean onlyCustom) throws ApiException {
        com.squareup.okhttp.Call call = getWidgetsValidateBeforeCall(onlyCustom, null, null);
        Type localVarReturnType = new TypeToken<DashboardWidgetListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get widgets (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Review Administrator * Review Owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param onlyCustom - true/false flag to return custom/all widgets (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWidgetsAsync(Boolean onlyCustom, final ApiCallback<DashboardWidgetListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWidgetsValidateBeforeCall(onlyCustom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardWidgetListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for newDashboard
     * @param body - dashboard node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call newDashboardCall(Dashboard body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dashboard/new";

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
    private com.squareup.okhttp.Call newDashboardValidateBeforeCall(Dashboard body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling newDashboard(Async)");
        }
        
        com.squareup.okhttp.Call call = newDashboardCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create new dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @return Dashboard
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Dashboard newDashboard(Dashboard body) throws ApiException {
        ApiResponse<Dashboard> resp = newDashboardWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create new dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @return ApiResponse&lt;Dashboard&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Dashboard> newDashboardWithHttpInfo(Dashboard body) throws ApiException {
        com.squareup.okhttp.Call call = newDashboardValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create new dashboard (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call newDashboardAsync(Dashboard body, final ApiCallback<Dashboard> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = newDashboardValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for newDashboardConfig
     * @param body - dashboard config node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call newDashboardConfigCall(DashboardConfig body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dashboard/configs/new";

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
    private com.squareup.okhttp.Call newDashboardConfigValidateBeforeCall(DashboardConfig body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling newDashboardConfig(Async)");
        }
        
        com.squareup.okhttp.Call call = newDashboardConfigCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create new dashboard config
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard config node (required)
     * @return DashboardConfig
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardConfig newDashboardConfig(DashboardConfig body) throws ApiException {
        ApiResponse<DashboardConfig> resp = newDashboardConfigWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create new dashboard config
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard config node (required)
     * @return ApiResponse&lt;DashboardConfig&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardConfig> newDashboardConfigWithHttpInfo(DashboardConfig body) throws ApiException {
        com.squareup.okhttp.Call call = newDashboardConfigValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create new dashboard config (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard config node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call newDashboardConfigAsync(DashboardConfig body, final ApiCallback<DashboardConfig> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = newDashboardConfigValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for newWidget
     * @param body - dashboard widget node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call newWidgetCall(DashboardWidget body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dashboard/widgets/new";

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
    private com.squareup.okhttp.Call newWidgetValidateBeforeCall(DashboardWidget body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling newWidget(Async)");
        }
        
        com.squareup.okhttp.Call call = newWidgetCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create new widget
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard widget node (required)
     * @return DashboardWidget
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardWidget newWidget(DashboardWidget body) throws ApiException {
        ApiResponse<DashboardWidget> resp = newWidgetWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create new widget
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard widget node (required)
     * @return ApiResponse&lt;DashboardWidget&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardWidget> newWidgetWithHttpInfo(DashboardWidget body) throws ApiException {
        com.squareup.okhttp.Call call = newWidgetValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create new widget (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard widget node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call newWidgetAsync(DashboardWidget body, final ApiCallback<DashboardWidget> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = newWidgetValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for restoreDashboardConfig
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call restoreDashboardConfigCall(Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/dashboard/configs/restore";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (full != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("full", full));

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
    private com.squareup.okhttp.Call restoreDashboardConfigValidateBeforeCall(Boolean full, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = restoreDashboardConfigCall(full, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Restore default dashboard config as active
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to false)
     * @return DashboardConfig
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardConfig restoreDashboardConfig(Boolean full) throws ApiException {
        ApiResponse<DashboardConfig> resp = restoreDashboardConfigWithHttpInfo(full);
        return resp.getData();
    }

    /**
     * Restore default dashboard config as active
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to false)
     * @return ApiResponse&lt;DashboardConfig&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardConfig> restoreDashboardConfigWithHttpInfo(Boolean full) throws ApiException {
        com.squareup.okhttp.Call call = restoreDashboardConfigValidateBeforeCall(full, null, null);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Restore default dashboard config as active (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param full - true/false flag to return layouts and widgets with dashboards (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call restoreDashboardConfigAsync(Boolean full, final ApiCallback<DashboardConfig> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = restoreDashboardConfigValidateBeforeCall(full, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateDashboard
     * @param body - dashboard node (required)
     * @param id - dashboard id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateDashboardCall(Dashboard body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dashboard/{id}"
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
    private com.squareup.okhttp.Call updateDashboardValidateBeforeCall(Dashboard body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateDashboard(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateDashboard(Async)");
        }
        
        com.squareup.okhttp.Call call = updateDashboardCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - dashboard id (required)
     * @return Dashboard
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Dashboard updateDashboard(Dashboard body, Long id) throws ApiException {
        ApiResponse<Dashboard> resp = updateDashboardWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - dashboard id (required)
     * @return ApiResponse&lt;Dashboard&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Dashboard> updateDashboardWithHttpInfo(Dashboard body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateDashboardValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update dashboard (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - dashboard id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateDashboardAsync(Dashboard body, Long id, final ApiCallback<Dashboard> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateDashboardValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateDashboardConfig
     * @param body - dashboard config node (required)
     * @param id - dashboard config id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateDashboardConfigCall(DashboardConfig body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dashboard/configs/{id}"
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
    private com.squareup.okhttp.Call updateDashboardConfigValidateBeforeCall(DashboardConfig body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateDashboardConfig(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateDashboardConfig(Async)");
        }
        
        com.squareup.okhttp.Call call = updateDashboardConfigCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update dashboard config
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard config node (required)
     * @param id - dashboard config id (required)
     * @return DashboardConfig
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardConfig updateDashboardConfig(DashboardConfig body, Long id) throws ApiException {
        ApiResponse<DashboardConfig> resp = updateDashboardConfigWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update dashboard config
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard config node (required)
     * @param id - dashboard config id (required)
     * @return ApiResponse&lt;DashboardConfig&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardConfig> updateDashboardConfigWithHttpInfo(DashboardConfig body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateDashboardConfigValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update dashboard config (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard config node (required)
     * @param id - dashboard config id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateDashboardConfigAsync(DashboardConfig body, Long id, final ApiCallback<DashboardConfig> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateDashboardConfigValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardConfig>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateMAUDashboard
     * @param body - dashboard node (required)
     * @param id - MAU dashboard id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateMAUDashboardCall(Dashboard body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dashboard/mau/{id}"
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
    private com.squareup.okhttp.Call updateMAUDashboardValidateBeforeCall(Dashboard body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMAUDashboard(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateMAUDashboard(Async)");
        }
        
        com.squareup.okhttp.Call call = updateMAUDashboardCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update MAU Usage dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - MAU dashboard id (required)
     * @return Dashboard
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Dashboard updateMAUDashboard(Dashboard body, Long id) throws ApiException {
        ApiResponse<Dashboard> resp = updateMAUDashboardWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update MAU Usage dashboard
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - MAU dashboard id (required)
     * @return ApiResponse&lt;Dashboard&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Dashboard> updateMAUDashboardWithHttpInfo(Dashboard body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateMAUDashboardValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update MAU Usage dashboard (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - MAU dashboard id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMAUDashboardAsync(Dashboard body, Long id, final ApiCallback<Dashboard> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateMAUDashboardValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Dashboard>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateWidget
     * @param body - dashboard node (required)
     * @param id - widget id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateWidgetCall(DashboardWidget body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/dashboard/widgets/{id}"
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
    private com.squareup.okhttp.Call updateWidgetValidateBeforeCall(DashboardWidget body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateWidget(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateWidget(Async)");
        }
        
        com.squareup.okhttp.Call call = updateWidgetCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update widget
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - widget id (required)
     * @return DashboardWidget
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DashboardWidget updateWidget(DashboardWidget body, Long id) throws ApiException {
        ApiResponse<DashboardWidget> resp = updateWidgetWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update widget
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - widget id (required)
     * @return ApiResponse&lt;DashboardWidget&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DashboardWidget> updateWidgetWithHttpInfo(DashboardWidget body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateWidgetValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update widget (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - dashboard node (required)
     * @param id - widget id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateWidgetAsync(DashboardWidget body, Long id, final ApiCallback<DashboardWidget> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateWidgetValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DashboardWidget>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
