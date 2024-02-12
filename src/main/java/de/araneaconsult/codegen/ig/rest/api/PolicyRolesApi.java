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


import de.araneaconsult.codegen.ig.rest.model.Calculated;
import de.araneaconsult.codegen.ig.rest.model.Download;
import de.araneaconsult.codegen.ig.rest.model.EntityCategorySearch;
import java.io.File;
import de.araneaconsult.codegen.ig.rest.model.Permissions;
import de.araneaconsult.codegen.ig.rest.model.Policies;
import de.araneaconsult.codegen.ig.rest.model.PolicyDetectionPreview;
import de.araneaconsult.codegen.ig.rest.model.Response;
import de.araneaconsult.codegen.ig.rest.model.Role;
import de.araneaconsult.codegen.ig.rest.model.Roles;
import de.araneaconsult.codegen.ig.rest.model.SearchCriteria;
import de.araneaconsult.codegen.ig.rest.model.Suggestions;
import de.araneaconsult.codegen.ig.rest.model.Users;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyRolesApi {
    private ApiClient apiClient;

    public PolicyRolesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyRolesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for assignRoles
     * @param uploadedInputStream  (required)
     * @param preview the preview (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call assignRolesCall(File uploadedInputStream, Boolean preview, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/bulk/assign";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (preview != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("preview", preview));

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
    private com.squareup.okhttp.Call assignRolesValidateBeforeCall(File uploadedInputStream, Boolean preview, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling assignRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = assignRolesCall(uploadedInputStream, preview, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Assign the users specified in the CSV file to the given technical roles
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param preview the preview (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response assignRoles(File uploadedInputStream, Boolean preview) throws ApiException {
        ApiResponse<Response> resp = assignRolesWithHttpInfo(uploadedInputStream, preview);
        return resp.getData();
    }

    /**
     * Assign the users specified in the CSV file to the given technical roles
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param preview the preview (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> assignRolesWithHttpInfo(File uploadedInputStream, Boolean preview) throws ApiException {
        com.squareup.okhttp.Call call = assignRolesValidateBeforeCall(uploadedInputStream, preview, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Assign the users specified in the CSV file to the given technical roles (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param preview the preview (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call assignRolesAsync(File uploadedInputStream, Boolean preview, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = assignRolesValidateBeforeCall(uploadedInputStream, preview, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/roles/download/{id}"
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Build call for createRolePolicy
     * @param body - The role policy to create. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createRolePolicyCall(Role body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/new";

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
    private com.squareup.okhttp.Call createRolePolicyValidateBeforeCall(Role body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createRolePolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = createRolePolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create a role policy.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy to create. (required)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role createRolePolicy(Role body) throws ApiException {
        ApiResponse<Role> resp = createRolePolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create a role policy.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy to create. (required)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> createRolePolicyWithHttpInfo(Role body) throws ApiException {
        com.squareup.okhttp.Call call = createRolePolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a role policy. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy to create. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createRolePolicyAsync(Role body, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createRolePolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createRoles
     * @param body - The technical role suggestions to create (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createRolesCall(Suggestions body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/mine";

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
    private com.squareup.okhttp.Call createRolesValidateBeforeCall(Suggestions body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = createRolesCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create roles from a mining operation
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The technical role suggestions to create (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response createRoles(Suggestions body) throws ApiException {
        ApiResponse<Response> resp = createRolesWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create roles from a mining operation
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The technical role suggestions to create (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> createRolesWithHttpInfo(Suggestions body) throws ApiException {
        com.squareup.okhttp.Call call = createRolesValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create roles from a mining operation (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The technical role suggestions to create (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createRolesAsync(Suggestions body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createRolesValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deletePolicy
     * @param id - The role policy ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deletePolicyCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}"
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
    private com.squareup.okhttp.Call deletePolicyValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deletePolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = deletePolicyCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the role policy with the given ID.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id - The role policy ID. (required)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role deletePolicy(Long id) throws ApiException {
        ApiResponse<Role> resp = deletePolicyWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete the role policy with the given ID.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id - The role policy ID. (required)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> deletePolicyWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deletePolicyValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the role policy with the given ID. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id - The role policy ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deletePolicyAsync(Long id, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deletePolicyValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteTechRoles
     * @param id The list of technical role IDs to delete. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteTechRolesCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/bulk";

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
    private com.squareup.okhttp.Call deleteTechRolesValidateBeforeCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteTechRolesCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Bulk delete the technical roles with the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id The list of technical role IDs to delete. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response deleteTechRoles(List<Long> id) throws ApiException {
        ApiResponse<Response> resp = deleteTechRolesWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Bulk delete the technical roles with the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id The list of technical role IDs to delete. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> deleteTechRolesWithHttpInfo(List<Long> id) throws ApiException {
        com.squareup.okhttp.Call call = deleteTechRolesValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Bulk delete the technical roles with the given IDs. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id The list of technical role IDs to delete. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteTechRolesAsync(List<Long> id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteTechRolesValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for estimateUsers
     * @param body - The role policy containing the permissions to use to estimate users. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call estimateUsersCall(Role body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/estimate";

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
    private com.squareup.okhttp.Call estimateUsersValidateBeforeCall(Role body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling estimateUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = estimateUsersCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Estimate users containing all the permissions of the role policy.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to use to estimate users. (required)
     * @return PolicyDetectionPreview
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PolicyDetectionPreview estimateUsers(Role body) throws ApiException {
        ApiResponse<PolicyDetectionPreview> resp = estimateUsersWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Estimate users containing all the permissions of the role policy.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to use to estimate users. (required)
     * @return ApiResponse&lt;PolicyDetectionPreview&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PolicyDetectionPreview> estimateUsersWithHttpInfo(Role body) throws ApiException {
        com.squareup.okhttp.Call call = estimateUsersValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Estimate users containing all the permissions of the role policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to use to estimate users. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call estimateUsersAsync(Role body, final ApiCallback<PolicyDetectionPreview> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = estimateUsersValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
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
        String localVarPath = "/policy/roles/download/{id}"
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Build call for getBusinessRolesReferencingRole
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolesReferencingRoleCall(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}/broles"
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
    private com.squareup.okhttp.Call getBusinessRolesReferencingRoleValidateBeforeCall(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getBusinessRolesReferencingRole(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getBusinessRolesReferencingRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getBusinessRolesReferencingRoleCall(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the Business Roles that reference the specified role
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies getBusinessRolesReferencingRole(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Policies> resp = getBusinessRolesReferencingRoleWithHttpInfo(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the Business Roles that reference the specified role
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @return ApiResponse&lt;Policies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Policies> getBusinessRolesReferencingRoleWithHttpInfo(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRolesReferencingRoleValidateBeforeCall(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the Business Roles that reference the specified role (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolesReferencingRoleAsync(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Policies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRolesReferencingRoleValidateBeforeCall(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEstimateUsers
     * @param body - list of attribute keys and values to use as search criteria (required)
     * @param id - The ID of the Role policy. (required)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr -  List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom -  Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - Number of results to return, used for paging. (optional, default to 10)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getEstimateUsersCall(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/estimate/users"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
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
    private com.squareup.okhttp.Call getEstimateUsersValidateBeforeCall(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getEstimateUsers(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getEstimateUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = getEstimateUsersCall(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of users that are in a particular role.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - list of attribute keys and values to use as search criteria (required)
     * @param id - The ID of the Role policy. (required)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr -  List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom -  Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - Number of results to return, used for paging. (optional, default to 10)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getEstimateUsers(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Users> resp = getEstimateUsersWithHttpInfo(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Return list of users that are in a particular role.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - list of attribute keys and values to use as search criteria (required)
     * @param id - The ID of the Role policy. (required)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr -  List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom -  Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - Number of results to return, used for paging. (optional, default to 10)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getEstimateUsersWithHttpInfo(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getEstimateUsersValidateBeforeCall(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of users that are in a particular role. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - list of attribute keys and values to use as search criteria (required)
     * @param id - The ID of the Role policy. (required)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr -  List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom -  Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - Number of results to return, used for paging. (optional, default to 10)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEstimateUsersAsync(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEstimateUsersValidateBeforeCall(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getReferencesForList
     * @param ID - The role id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getReferencesForListCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{ID}/references"
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
    private com.squareup.okhttp.Call getReferencesForListValidateBeforeCall(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getReferencesForList(Async)");
        }
        
        com.squareup.okhttp.Call call = getReferencesForListCall(ID, size, q, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the references for the given role policy.
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param ID - The role id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getReferencesForList(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        ApiResponse<Roles> resp = getReferencesForListWithHttpInfo(ID, size, q, sortBy, sortOrder, indexFrom);
        return resp.getData();
    }

    /**
     * Get the references for the given role policy.
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param ID - The role id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getReferencesForListWithHttpInfo(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getReferencesForListValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the references for the given role policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param ID - The role id (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getReferencesForListAsync(Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getReferencesForListValidateBeforeCall(ID, size, q, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRole
     * @param id - The role policy ID (required)
     * @param attrFilter -  List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy - The role policy attribute name used for sorting. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - List of permission attributes to return. (optional)
     * @param allowDeleted - Allow deleted policies to be returned (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRoleCall(Long id, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (allowDeleted != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("allowDeleted", allowDeleted));

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
    private com.squareup.okhttp.Call getRoleValidateBeforeCall(Long id, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getRoleCall(id, attrFilter, sortBy, sortOrder, listAttr, allowDeleted, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the role policy with the given ID.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - The role policy ID (required)
     * @param attrFilter -  List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy - The role policy attribute name used for sorting. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - List of permission attributes to return. (optional)
     * @param allowDeleted - Allow deleted policies to be returned (optional, default to false)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role getRole(Long id, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        ApiResponse<Role> resp = getRoleWithHttpInfo(id, attrFilter, sortBy, sortOrder, listAttr, allowDeleted);
        return resp.getData();
    }

    /**
     * Get the role policy with the given ID.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - The role policy ID (required)
     * @param attrFilter -  List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy - The role policy attribute name used for sorting. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - List of permission attributes to return. (optional)
     * @param allowDeleted - Allow deleted policies to be returned (optional, default to false)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> getRoleWithHttpInfo(Long id, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        com.squareup.okhttp.Call call = getRoleValidateBeforeCall(id, attrFilter, sortBy, sortOrder, listAttr, allowDeleted, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the role policy with the given ID. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - The role policy ID (required)
     * @param attrFilter -  List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy - The role policy attribute name used for sorting. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - List of permission attributes to return. (optional)
     * @param allowDeleted - Allow deleted policies to be returned (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRoleAsync(Long id, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRoleValidateBeforeCall(id, attrFilter, sortBy, sortOrder, listAttr, allowDeleted, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRoleCalculatedAttr
     * @param id -  The role policy ID (required)
     * @param attrName - Name of calculated attribute to be retrieved. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRoleCalculatedAttrCall(Long id, String attrName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}/{attrName}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()))
            .replaceAll("\\{" + "attrName" + "\\}", apiClient.escapeString(attrName.toString()));

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
    private com.squareup.okhttp.Call getRoleCalculatedAttrValidateBeforeCall(Long id, String attrName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getRoleCalculatedAttr(Async)");
        }
        // verify the required parameter 'attrName' is set
        if (attrName == null) {
            throw new ApiException("Missing the required parameter 'attrName' when calling getRoleCalculatedAttr(Async)");
        }
        
        com.squareup.okhttp.Call call = getRoleCalculatedAttrCall(id, attrName, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the calculated attribute (attrName) for the given role policy ID.
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id -  The role policy ID (required)
     * @param attrName - Name of calculated attribute to be retrieved. (required)
     * @return Calculated
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Calculated getRoleCalculatedAttr(Long id, String attrName) throws ApiException {
        ApiResponse<Calculated> resp = getRoleCalculatedAttrWithHttpInfo(id, attrName);
        return resp.getData();
    }

    /**
     * Get the calculated attribute (attrName) for the given role policy ID.
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id -  The role policy ID (required)
     * @param attrName - Name of calculated attribute to be retrieved. (required)
     * @return ApiResponse&lt;Calculated&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Calculated> getRoleCalculatedAttrWithHttpInfo(Long id, String attrName) throws ApiException {
        com.squareup.okhttp.Call call = getRoleCalculatedAttrValidateBeforeCall(id, attrName, null, null);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the calculated attribute (attrName) for the given role policy ID. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param id -  The role policy ID (required)
     * @param attrName - Name of calculated attribute to be retrieved. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRoleCalculatedAttrAsync(Long id, String attrName, final ApiCallback<Calculated> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRoleCalculatedAttrValidateBeforeCall(id, attrName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRolePolicies
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRolePoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles";

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
    private com.squareup.okhttp.Call getRolePoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getRolePolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getRolePoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of logical role policies, also include the information about  policy
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getRolePolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter) throws ApiException {
        ApiResponse<Roles> resp = getRolePoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter);
        return resp.getData();
    }

    /**
     * Return list of logical role policies, also include the information about  policy
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getRolePoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter) throws ApiException {
        com.squareup.okhttp.Call call = getRolePoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of logical role policies, also include the information about  policy (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Review Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRolePoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRolePoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRolePoliciesByIds
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRolePoliciesByIdsCall(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/byId";

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
    private com.squareup.okhttp.Call getRolePoliciesByIdsValidateBeforeCall(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getRolePoliciesByIds(Async)");
        }
        
        com.squareup.okhttp.Call call = getRolePoliciesByIdsCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of logical role policies, also include the information about  policy
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getRolePoliciesByIds(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter) throws ApiException {
        ApiResponse<Roles> resp = getRolePoliciesByIdsWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter);
        return resp.getData();
    }

    /**
     * Return list of logical role policies, also include the information about  policy
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getRolePoliciesByIdsWithHttpInfo(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter) throws ApiException {
        com.squareup.okhttp.Call call = getRolePoliciesByIdsValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of logical role policies, also include the information about  policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRolePoliciesByIdsAsync(EntityCategorySearch body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRolePoliciesByIdsValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRolePolicyPerms
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRolePolicyPermsCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{ID}/perms"
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
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));

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
    private com.squareup.okhttp.Call getRolePolicyPermsValidateBeforeCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getRolePolicyPerms(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getRolePolicyPerms(Async)");
        }
        
        com.squareup.okhttp.Call call = getRolePolicyPermsCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of permissions assigned to an technical role policy
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return Permissions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permissions getRolePolicyPerms(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        ApiResponse<Permissions> resp = getRolePolicyPermsWithHttpInfo(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt);
        return resp.getData();
    }

    /**
     * Return list of permissions assigned to an technical role policy
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return ApiResponse&lt;Permissions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permissions> getRolePolicyPermsWithHttpInfo(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getRolePolicyPermsValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of permissions assigned to an technical role policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRolePolicyPermsAsync(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ApiCallback<Permissions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRolePolicyPermsValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
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
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRoleSodsCall(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}/sods"
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
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - id of the role (required)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies getRoleSods(Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Policies> resp = getRoleSodsWithHttpInfo(id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the SoD policies that reference the specified role
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - id of the role (required)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
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
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id - id of the role (required)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr - attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy - column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size - number of rows to return within the result set (optional, default to 10)
     * @param showCt - When true, will show the total search count (e.g.         totalSearch:10) of rows in the query. You should only set this         true on the initial call to obtain the total size of the search         result list. Otherwise there will be duplicate queries made, one         to obtain the result set and the other to get the count. Default:         false (optional, default to false)
     * @param q - Quick search filter value (optional)
     * @param qMatch - match mode, how to compare value to quick search string         allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,         ANY &#x3D; match any place in string, START &#x3D; match on starting         character (optional, default to ANY)
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
     * Build call for getSodsReferencingRole
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSodsReferencingRoleCall(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}/sods"
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
    private com.squareup.okhttp.Call getSodsReferencingRoleValidateBeforeCall(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSodsReferencingRole(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getSodsReferencingRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodsReferencingRoleCall(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SoD policies that reference the specified role
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies getSodsReferencingRole(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        ApiResponse<Policies> resp = getSodsReferencingRoleWithHttpInfo(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch);
        return resp.getData();
    }

    /**
     * Get the SoD policies that reference the specified role
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @return ApiResponse&lt;Policies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Policies> getSodsReferencingRoleWithHttpInfo(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getSodsReferencingRoleValidateBeforeCall(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SoD policies that reference the specified role (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body SearchCriteriaNode instance (required)
     * @param id id of the role (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr attributes to return in the payload. Returns all if not present (optional)
     * @param sortBy column to sort by. Valid values: any sortable ARC User Attribute (optional)
     * @param sortOrder sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size number of rows to return within the result set (optional, default to 10)
     * @param showCt When true, will show the total search count (e.g.                          totalSearch:10) of rows in the query. You should only set this                          true on the initial call to obtain the total size of the search                          result list. Otherwise there will be duplicate queries made, one                          to obtain the result set and the other to get the count. Default:                          false (optional, default to false)
     * @param q Quick search filter value (optional)
     * @param qMatch match mode, how to compare value to quick search string                          allowable values: START, ANY, EXACT, where EXACT &#x3D; exact match,                          ANY &#x3D; match any place in string, START &#x3D; match on starting                          character (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSodsReferencingRoleAsync(SearchCriteria body, Long id, List<String> attrFilter, List<String> listAttr, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, final ApiCallback<Policies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSodsReferencingRoleValidateBeforeCall(body, id, attrFilter, listAttr, sortBy, sortOrder, indexFrom, size, showCt, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUsersInRole
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param id The ID of the Role policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param detected Flag indicating to return detected or assigned role assignments (optional, default to true)
     * @param type Type of user (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUsersInRoleCall(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean detected, String type, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}/users"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
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
        if (detected != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detected", detected));
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));

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
    private com.squareup.okhttp.Call getUsersInRoleValidateBeforeCall(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean detected, String type, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUsersInRole(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getUsersInRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getUsersInRoleCall(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, detected, type, typeAheadSearch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of users that are in a particular role.
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param id The ID of the Role policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param detected Flag indicating to return detected or assigned role assignments (optional, default to true)
     * @param type Type of user (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getUsersInRole(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean detected, String type, Boolean typeAheadSearch) throws ApiException {
        ApiResponse<Users> resp = getUsersInRoleWithHttpInfo(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, detected, type, typeAheadSearch);
        return resp.getData();
    }

    /**
     * Return list of users that are in a particular role.
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param id The ID of the Role policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param detected Flag indicating to return detected or assigned role assignments (optional, default to true)
     * @param type Type of user (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getUsersInRoleWithHttpInfo(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean detected, String type, Boolean typeAheadSearch) throws ApiException {
        com.squareup.okhttp.Call call = getUsersInRoleValidateBeforeCall(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, detected, type, typeAheadSearch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of users that are in a particular role. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param id The ID of the Role policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param detected Flag indicating to return detected or assigned role assignments (optional, default to true)
     * @param type Type of user (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUsersInRoleAsync(SearchCriteria body, Long id, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean detected, String type, Boolean typeAheadSearch, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUsersInRoleValidateBeforeCall(body, id, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, detected, type, typeAheadSearch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZip
     * @param uploadedInputStream  (required)
     * @param refs true to import references (optional, default to false)
     * @param reportOnly true for report only (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipCall(File uploadedInputStream, Boolean refs, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/import";

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
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param refs true to import references (optional, default to false)
     * @param reportOnly true for report only (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip(File uploadedInputStream, Boolean refs, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importZipWithHttpInfo(uploadedInputStream, refs, reportOnly);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param refs true to import references (optional, default to false)
     * @param reportOnly true for report only (optional, default to false)
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
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param refs true to import references (optional, default to false)
     * @param reportOnly true for report only (optional, default to false)
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
     * Build call for importZipResolveApps
     * @param uploadedInputStream  (required)
     * @param build true to build application list (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipResolveAppsCall(File uploadedInputStream, Boolean build, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/import/resolve/apps";

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
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param build true to build application list (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZipResolveApps(File uploadedInputStream, Boolean build) throws ApiException {
        ApiResponse<Response> resp = importZipResolveAppsWithHttpInfo(uploadedInputStream, build);
        return resp.getData();
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param build true to build application list (optional, default to false)
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
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param build true to build application list (optional, default to false)
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
     * Build call for importZip_0
     * @param uploadedInputStream  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZip_0Call(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/import/preview";

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
    private com.squareup.okhttp.Call importZip_0ValidateBeforeCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZip_0(Async)");
        }
        
        com.squareup.okhttp.Call call = importZip_0Call(uploadedInputStream, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip_0(File uploadedInputStream) throws ApiException {
        ApiResponse<Response> resp = importZip_0WithHttpInfo(uploadedInputStream);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZip_0WithHttpInfo(File uploadedInputStream) throws ApiException {
        com.squareup.okhttp.Call call = importZip_0ValidateBeforeCall(uploadedInputStream, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param uploadedInputStream  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZip_0Async(File uploadedInputStream, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZip_0ValidateBeforeCall(uploadedInputStream, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for matchRolePermissions
     * @param body - The role policy containing the permissions to match. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call matchRolePermissionsCall(Role body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/match/permissions";

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
    private com.squareup.okhttp.Call matchRolePermissionsValidateBeforeCall(Role body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling matchRolePermissions(Async)");
        }
        
        com.squareup.okhttp.Call call = matchRolePermissionsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Find existing role policies having the same permissions as the given role policy.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to match. (required)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles matchRolePermissions(Role body) throws ApiException {
        ApiResponse<Roles> resp = matchRolePermissionsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Find existing role policies having the same permissions as the given role policy.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to match. (required)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> matchRolePermissionsWithHttpInfo(Role body) throws ApiException {
        com.squareup.okhttp.Call call = matchRolePermissionsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Find existing role policies having the same permissions as the given role policy. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to match. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call matchRolePermissionsAsync(Role body, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = matchRolePermissionsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
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
        String localVarPath = "/policy/roles/mine/{id}/status"
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Build call for setTechRolesAuthorizationRole
     * @param body The list of technical role Nodes which to set the authorization role flag. (required)
     * @param authorizationRole The policy authorization role: true or false (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setTechRolesAuthorizationRoleCall(Roles body, Boolean authorizationRole, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/bulk/authorizationrole";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (authorizationRole != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("authorizationRole", authorizationRole));

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
    private com.squareup.okhttp.Call setTechRolesAuthorizationRoleValidateBeforeCall(Roles body, Boolean authorizationRole, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setTechRolesAuthorizationRole(Async)");
        }
        
        com.squareup.okhttp.Call call = setTechRolesAuthorizationRoleCall(body, authorizationRole, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the authorization role flag of the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the authorization role flag. (required)
     * @param authorizationRole The policy authorization role: true or false (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles setTechRolesAuthorizationRole(Roles body, Boolean authorizationRole) throws ApiException {
        ApiResponse<Roles> resp = setTechRolesAuthorizationRoleWithHttpInfo(body, authorizationRole);
        return resp.getData();
    }

    /**
     * Set the authorization role flag of the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the authorization role flag. (required)
     * @param authorizationRole The policy authorization role: true or false (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> setTechRolesAuthorizationRoleWithHttpInfo(Roles body, Boolean authorizationRole) throws ApiException {
        com.squareup.okhttp.Call call = setTechRolesAuthorizationRoleValidateBeforeCall(body, authorizationRole, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the authorization role flag of the given IDs. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the authorization role flag. (required)
     * @param authorizationRole The policy authorization role: true or false (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setTechRolesAuthorizationRoleAsync(Roles body, Boolean authorizationRole, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setTechRolesAuthorizationRoleValidateBeforeCall(body, authorizationRole, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setTechRolesOwner
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setTechRolesOwnerCall(Roles body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/bulk/policyOwner";

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
    private com.squareup.okhttp.Call setTechRolesOwnerValidateBeforeCall(Roles body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setTechRolesOwner(Async)");
        }
        
        com.squareup.okhttp.Call call = setTechRolesOwnerCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the state of the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles setTechRolesOwner(Roles body) throws ApiException {
        ApiResponse<Roles> resp = setTechRolesOwnerWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Set the state of the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> setTechRolesOwnerWithHttpInfo(Roles body) throws ApiException {
        com.squareup.okhttp.Call call = setTechRolesOwnerValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the state of the given IDs. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setTechRolesOwnerAsync(Roles body, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setTechRolesOwnerValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setTechRolesState
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setTechRolesStateCall(Roles body, String state, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/bulk/policyState";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (state != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state", state));

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
    private com.squareup.okhttp.Call setTechRolesStateValidateBeforeCall(Roles body, String state, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setTechRolesState(Async)");
        }
        
        com.squareup.okhttp.Call call = setTechRolesStateCall(body, state, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the state of the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles setTechRolesState(Roles body, String state) throws ApiException {
        ApiResponse<Roles> resp = setTechRolesStateWithHttpInfo(body, state);
        return resp.getData();
    }

    /**
     * Set the state of the given IDs.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> setTechRolesStateWithHttpInfo(Roles body, String state) throws ApiException {
        com.squareup.okhttp.Call call = setTechRolesStateValidateBeforeCall(body, state, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the state of the given IDs. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical role Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setTechRolesStateAsync(Roles body, String state, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setTechRolesStateValidateBeforeCall(body, state, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setTechRolesState_0
     * @param body The list of technical roles or users to assign the technical role (required)
     * @param roleId The id of the technical roles to assign the users in the request node (optional)
     * @param preview true for preview (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setTechRolesState_0Call(Download body, Long roleId, Boolean preview, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/bulk/promoteDetected";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (roleId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roleId", roleId));
        if (preview != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("preview", preview));

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
    private com.squareup.okhttp.Call setTechRolesState_0ValidateBeforeCall(Download body, Long roleId, Boolean preview, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setTechRolesState_0(Async)");
        }
        
        com.squareup.okhttp.Call call = setTechRolesState_0Call(body, roleId, preview, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Promote any detected roles to assigned roles
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical roles or users to assign the technical role (required)
     * @param roleId The id of the technical roles to assign the users in the request node (optional)
     * @param preview true for preview (optional, default to false)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles setTechRolesState_0(Download body, Long roleId, Boolean preview) throws ApiException {
        ApiResponse<Roles> resp = setTechRolesState_0WithHttpInfo(body, roleId, preview);
        return resp.getData();
    }

    /**
     * Promote any detected roles to assigned roles
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical roles or users to assign the technical role (required)
     * @param roleId The id of the technical roles to assign the users in the request node (optional)
     * @param preview true for preview (optional, default to false)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> setTechRolesState_0WithHttpInfo(Download body, Long roleId, Boolean preview) throws ApiException {
        com.squareup.okhttp.Call call = setTechRolesState_0ValidateBeforeCall(body, roleId, preview, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Promote any detected roles to assigned roles (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The list of technical roles or users to assign the technical role (required)
     * @param roleId The id of the technical roles to assign the users in the request node (optional)
     * @param preview true for preview (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setTechRolesState_0Async(Download body, Long roleId, Boolean preview, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setTechRolesState_0ValidateBeforeCall(body, roleId, preview, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for sortRolePermissions
     * @param body - The role policy containing the permissions to sort. (required)
     * @param sortBy -  The permission column name used to sort. (optional)
     * @param sortOrder - The order to sort ASC or DSC (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call sortRolePermissionsCall(Role body, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/sort/permissions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call sortRolePermissionsValidateBeforeCall(Role body, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling sortRolePermissions(Async)");
        }
        
        com.squareup.okhttp.Call call = sortRolePermissionsCall(body, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Sort the permissions contained in the given role policy.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to sort. (required)
     * @param sortBy -  The permission column name used to sort. (optional)
     * @param sortOrder - The order to sort ASC or DSC (optional, default to ASC)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role sortRolePermissions(Role body, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<Role> resp = sortRolePermissionsWithHttpInfo(body, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Sort the permissions contained in the given role policy.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to sort. (required)
     * @param sortBy -  The permission column name used to sort. (optional)
     * @param sortOrder - The order to sort ASC or DSC (optional, default to ASC)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> sortRolePermissionsWithHttpInfo(Role body, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = sortRolePermissionsValidateBeforeCall(body, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Sort the permissions contained in the given role policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body - The role policy containing the permissions to sort. (required)
     * @param sortBy -  The permission column name used to sort. (optional)
     * @param sortOrder - The order to sort ASC or DSC (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call sortRolePermissionsAsync(Role body, String sortBy, String sortOrder, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = sortRolePermissionsValidateBeforeCall(body, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param id - The technical roles to export (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(List<Long> id, Boolean refs, Boolean apps, Boolean cats, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/roles/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
        if (cats != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("cats", cats));
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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(List<Long> id, Boolean refs, Boolean apps, Boolean cats, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startDownloadCall(id, refs, apps, cats, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the technical role download.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id - The technical roles to export (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(List<Long> id, Boolean refs, Boolean apps, Boolean cats, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(id, refs, apps, cats, attrFilter);
        return resp.getData();
    }

    /**
     * Start the technical role download.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id - The technical roles to export (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(List<Long> id, Boolean refs, Boolean apps, Boolean cats, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, refs, apps, cats, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the technical role download. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param id - The technical roles to export (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(List<Long> id, Boolean refs, Boolean apps, Boolean cats, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, refs, apps, cats, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_0
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Call(Download body, List<String> attrFilter, Boolean refs, Boolean apps, Boolean cats, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
        if (cats != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("cats", cats));

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
    private com.squareup.okhttp.Call startDownload_0ValidateBeforeCall(Download body, List<String> attrFilter, Boolean refs, Boolean apps, Boolean cats, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_0Call(body, attrFilter, refs, apps, cats, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the technical role download.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body, List<String> attrFilter, Boolean refs, Boolean apps, Boolean cats) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body, attrFilter, refs, apps, cats);
        return resp.getData();
    }

    /**
     * Start the technical role download.
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_0WithHttpInfo(Download body, List<String> attrFilter, Boolean refs, Boolean apps, Boolean cats) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, attrFilter, refs, apps, cats, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the technical role download. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param refs - Flag to include references in export (optional, default to false)
     * @param apps - Flag to export referenced applications (optional, default to false)
     * @param cats - Flag to export referenced categories (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Async(Download body, List<String> attrFilter, Boolean refs, Boolean apps, Boolean cats, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, attrFilter, refs, apps, cats, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/roles/download/{id}/status"
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Accepted Roles: * Access Request Administrator * Auditor * Customer Administrator * Role owner * Technical Roles Administrator 
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
     * Build call for updateRolePolicy
     * @param body The updated to the role policy. (required)
     * @param id - The role policy ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateRolePolicyCall(Role body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/roles/{id}"
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
    private com.squareup.okhttp.Call updateRolePolicyValidateBeforeCall(Role body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateRolePolicy(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateRolePolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = updateRolePolicyCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update logical role policy.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The updated to the role policy. (required)
     * @param id - The role policy ID. (required)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role updateRolePolicy(Role body, Long id) throws ApiException {
        ApiResponse<Role> resp = updateRolePolicyWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update logical role policy.
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The updated to the role policy. (required)
     * @param id - The role policy ID. (required)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> updateRolePolicyWithHttpInfo(Role body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateRolePolicyValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update logical role policy. (asynchronously)
     * Accepted Roles: * Customer Administrator * Role owner * Technical Roles Administrator 
     * @param body The updated to the role policy. (required)
     * @param id - The role policy ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateRolePolicyAsync(Role body, Long id, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateRolePolicyValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
