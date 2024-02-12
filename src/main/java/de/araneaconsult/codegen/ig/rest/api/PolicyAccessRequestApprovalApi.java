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


import de.araneaconsult.codegen.ig.rest.model.AccessRequestApprovalPolicies;
import de.araneaconsult.codegen.ig.rest.model.AccessRequestApprovalPolicy;
import de.araneaconsult.codegen.ig.rest.model.AccessRequestPolicy;
import de.araneaconsult.codegen.ig.rest.model.AppDataSources;
import de.araneaconsult.codegen.ig.rest.model.AssignedPolicies;
import de.araneaconsult.codegen.ig.rest.model.Download;
import de.araneaconsult.codegen.ig.rest.model.EmailTemplates;
import java.io.File;
import de.araneaconsult.codegen.ig.rest.model.ModelImport;
import de.araneaconsult.codegen.ig.rest.model.Permissions;
import de.araneaconsult.codegen.ig.rest.model.RequestItems;
import de.araneaconsult.codegen.ig.rest.model.Response;
import de.araneaconsult.codegen.ig.rest.model.Roles;
import de.araneaconsult.codegen.ig.rest.model.SearchCriteria;
import de.araneaconsult.codegen.ig.rest.model.Status;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyAccessRequestApprovalApi {
    private ApiClient apiClient;

    public PolicyAccessRequestApprovalApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyAccessRequestApprovalApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for checkRequestItems
     * @param body List of resources to get the approval policy for. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call checkRequestItemsCall(RequestItems body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/resourcepolicies";

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
    private com.squareup.okhttp.Call checkRequestItemsValidateBeforeCall(RequestItems body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling checkRequestItems(Async)");
        }
        
        com.squareup.okhttp.Call call = checkRequestItemsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the approval policies for the specified resources.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of resources to get the approval policy for. (required)
     * @return AssignedPolicies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AssignedPolicies checkRequestItems(RequestItems body) throws ApiException {
        ApiResponse<AssignedPolicies> resp = checkRequestItemsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Get the approval policies for the specified resources.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of resources to get the approval policy for. (required)
     * @return ApiResponse&lt;AssignedPolicies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AssignedPolicies> checkRequestItemsWithHttpInfo(RequestItems body) throws ApiException {
        com.squareup.okhttp.Call call = checkRequestItemsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<AssignedPolicies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the approval policies for the specified resources. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of resources to get the approval policy for. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call checkRequestItemsAsync(RequestItems body, final ApiCallback<AssignedPolicies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = checkRequestItemsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AssignedPolicies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for clearRequestItems
     * @param body List of request items to assign to the default access request approval policy. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call clearRequestItemsCall(RequestItems body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/default/approvalitems";

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
    private com.squareup.okhttp.Call clearRequestItemsValidateBeforeCall(RequestItems body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling clearRequestItems(Async)");
        }
        
        com.squareup.okhttp.Call call = clearRequestItemsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Assign approval items to the default access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items to assign to the default access request approval policy. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response clearRequestItems(RequestItems body) throws ApiException {
        ApiResponse<Response> resp = clearRequestItemsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Assign approval items to the default access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items to assign to the default access request approval policy. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> clearRequestItemsWithHttpInfo(RequestItems body) throws ApiException {
        com.squareup.okhttp.Call call = clearRequestItemsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Assign approval items to the default access request approval policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items to assign to the default access request approval policy. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call clearRequestItemsAsync(RequestItems body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = clearRequestItemsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createAccessRequestApprovalPolicy
     * @param body The access request approval policy to create. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createAccessRequestApprovalPolicyCall(AccessRequestApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/new";

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
    private com.squareup.okhttp.Call createAccessRequestApprovalPolicyValidateBeforeCall(AccessRequestApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createAccessRequestApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = createAccessRequestApprovalPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create an access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body The access request approval policy to create. (required)
     * @return AccessRequestApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccessRequestApprovalPolicy createAccessRequestApprovalPolicy(AccessRequestApprovalPolicy body) throws ApiException {
        ApiResponse<AccessRequestApprovalPolicy> resp = createAccessRequestApprovalPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create an access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body The access request approval policy to create. (required)
     * @return ApiResponse&lt;AccessRequestApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccessRequestApprovalPolicy> createAccessRequestApprovalPolicyWithHttpInfo(AccessRequestApprovalPolicy body) throws ApiException {
        com.squareup.okhttp.Call call = createAccessRequestApprovalPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create an access request approval policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body The access request approval policy to create. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createAccessRequestApprovalPolicyAsync(AccessRequestApprovalPolicy body, final ApiCallback<AccessRequestApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createAccessRequestApprovalPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteAccessRequestApprovalPolicy
     * @param ID The policy ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteAccessRequestApprovalPolicyCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}"
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
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call deleteAccessRequestApprovalPolicyValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling deleteAccessRequestApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteAccessRequestApprovalPolicyCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the access request approval policy with the given ID.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param ID The policy ID. (required)
     * @return AccessRequestApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccessRequestApprovalPolicy deleteAccessRequestApprovalPolicy(Long ID) throws ApiException {
        ApiResponse<AccessRequestApprovalPolicy> resp = deleteAccessRequestApprovalPolicyWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Delete the access request approval policy with the given ID.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param ID The policy ID. (required)
     * @return ApiResponse&lt;AccessRequestApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccessRequestApprovalPolicy> deleteAccessRequestApprovalPolicyWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = deleteAccessRequestApprovalPolicyValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the access request approval policy with the given ID. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param ID The policy ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteAccessRequestApprovalPolicyAsync(Long ID, final ApiCallback<AccessRequestApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteAccessRequestApprovalPolicyValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccessRequestApprovalPolicies
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @param excludeDefault Exclude getting the default approval policy. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAccessRequestApprovalPoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, Boolean excludeDefault, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval";

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
        if (excludeDefault != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("excludeDefault", excludeDefault));

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
    private com.squareup.okhttp.Call getAccessRequestApprovalPoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, Boolean excludeDefault, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAccessRequestApprovalPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccessRequestApprovalPoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, excludeDefault, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return a list of access request approval policies with optional filtering, sorting and paging.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @param excludeDefault Exclude getting the default approval policy. (optional, default to false)
     * @return AccessRequestApprovalPolicies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccessRequestApprovalPolicies getAccessRequestApprovalPolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, Boolean excludeDefault) throws ApiException {
        ApiResponse<AccessRequestApprovalPolicies> resp = getAccessRequestApprovalPoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, excludeDefault);
        return resp.getData();
    }

    /**
     * Return a list of access request approval policies with optional filtering, sorting and paging.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @param excludeDefault Exclude getting the default approval policy. (optional, default to false)
     * @return ApiResponse&lt;AccessRequestApprovalPolicies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccessRequestApprovalPolicies> getAccessRequestApprovalPoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, Boolean excludeDefault) throws ApiException {
        com.squareup.okhttp.Call call = getAccessRequestApprovalPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, excludeDefault, null, null);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return a list of access request approval policies with optional filtering, sorting and paging. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @param excludeDefault Exclude getting the default approval policy. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAccessRequestApprovalPoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, Boolean excludeDefault, final ApiCallback<AccessRequestApprovalPolicies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccessRequestApprovalPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, excludeDefault, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccessRequestApprovalPolicy
     * @param ID The access request approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted access request approval policy. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyCall(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
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
    private com.squareup.okhttp.Call getAccessRequestApprovalPolicyValidateBeforeCall(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccessRequestApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyCall(ID, attrFilter, listAttr, allowDeleted, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the access request approval policy with the given ID.
     * Accepted Roles: * Access Request Administrator * Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID The access request approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted access request approval policy. (optional, default to false)
     * @return AccessRequestApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccessRequestApprovalPolicy getAccessRequestApprovalPolicy(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        ApiResponse<AccessRequestApprovalPolicy> resp = getAccessRequestApprovalPolicyWithHttpInfo(ID, attrFilter, listAttr, allowDeleted);
        return resp.getData();
    }

    /**
     * Get the access request approval policy with the given ID.
     * Accepted Roles: * Access Request Administrator * Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID The access request approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted access request approval policy. (optional, default to false)
     * @return ApiResponse&lt;AccessRequestApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccessRequestApprovalPolicy> getAccessRequestApprovalPolicyWithHttpInfo(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyValidateBeforeCall(ID, attrFilter, listAttr, allowDeleted, null, null);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the access request approval policy with the given ID. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID The access request approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted access request approval policy. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyAsync(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, final ApiCallback<AccessRequestApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyValidateBeforeCall(ID, attrFilter, listAttr, allowDeleted, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccessRequestApprovalPolicyApps
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyAppsCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/apps"
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
    private com.squareup.okhttp.Call getAccessRequestApprovalPolicyAppsValidateBeforeCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAccessRequestApprovalPolicyApps(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccessRequestApprovalPolicyApps(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyAppsCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of applications assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return AppDataSources
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AppDataSources getAccessRequestApprovalPolicyApps(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        ApiResponse<AppDataSources> resp = getAccessRequestApprovalPolicyAppsWithHttpInfo(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt);
        return resp.getData();
    }

    /**
     * Return list of applications assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return ApiResponse&lt;AppDataSources&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AppDataSources> getAccessRequestApprovalPolicyAppsWithHttpInfo(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyAppsValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, null, null);
        Type localVarReturnType = new TypeToken<AppDataSources>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of applications assigned to an access request approval policy (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyAppsAsync(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ApiCallback<AppDataSources> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyAppsValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AppDataSources>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccessRequestApprovalPolicyBusinessRoles
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyBusinessRolesCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/broles"
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
    private com.squareup.okhttp.Call getAccessRequestApprovalPolicyBusinessRolesValidateBeforeCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAccessRequestApprovalPolicyBusinessRoles(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccessRequestApprovalPolicyBusinessRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyBusinessRolesCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of business roles assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getAccessRequestApprovalPolicyBusinessRoles(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        ApiResponse<Roles> resp = getAccessRequestApprovalPolicyBusinessRolesWithHttpInfo(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt);
        return resp.getData();
    }

    /**
     * Return list of business roles assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getAccessRequestApprovalPolicyBusinessRolesWithHttpInfo(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyBusinessRolesValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of business roles assigned to an access request approval policy (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyBusinessRolesAsync(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyBusinessRolesValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccessRequestApprovalPolicyPerms
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyPermsCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/perms"
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
    private com.squareup.okhttp.Call getAccessRequestApprovalPolicyPermsValidateBeforeCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAccessRequestApprovalPolicyPerms(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccessRequestApprovalPolicyPerms(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyPermsCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of permissions assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public Permissions getAccessRequestApprovalPolicyPerms(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        ApiResponse<Permissions> resp = getAccessRequestApprovalPolicyPermsWithHttpInfo(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt);
        return resp.getData();
    }

    /**
     * Return list of permissions assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public ApiResponse<Permissions> getAccessRequestApprovalPolicyPermsWithHttpInfo(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyPermsValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of permissions assigned to an access request approval policy (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyPermsAsync(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ApiCallback<Permissions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyPermsValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccessRequestApprovalPolicyRoles
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyRolesCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/roles"
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
    private com.squareup.okhttp.Call getAccessRequestApprovalPolicyRolesValidateBeforeCall(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getAccessRequestApprovalPolicyRoles(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccessRequestApprovalPolicyRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyRolesCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of technical roles assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getAccessRequestApprovalPolicyRoles(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        ApiResponse<Roles> resp = getAccessRequestApprovalPolicyRolesWithHttpInfo(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt);
        return resp.getData();
    }

    /**
     * Return list of technical roles assigned to an access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param showCt Show total result set count. (optional, default to false)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getAccessRequestApprovalPolicyRolesWithHttpInfo(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyRolesValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of technical roles assigned to an access request approval policy (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID ID of access request approval policy. (required)
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
    public com.squareup.okhttp.Call getAccessRequestApprovalPolicyRolesAsync(SearchCriteria body, Long ID, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccessRequestApprovalPolicyRolesValidateBeforeCall(body, ID, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAccessRequestPolicyForRole
     * @param propertyClass The type of access request item. (required)
     * @param ID The role policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request policy attributes to return (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAccessRequestPolicyForRoleCall(String propertyClass, Long ID, List<String> attrFilter, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{class}/{ID}"
            .replaceAll("\\{" + "class" + "\\}", apiClient.escapeString(propertyClass.toString()))
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));

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
    private com.squareup.okhttp.Call getAccessRequestPolicyForRoleValidateBeforeCall(String propertyClass, Long ID, List<String> attrFilter, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'propertyClass' is set
        if (propertyClass == null) {
            throw new ApiException("Missing the required parameter 'propertyClass' when calling getAccessRequestPolicyForRole(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccessRequestPolicyForRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccessRequestPolicyForRoleCall(propertyClass, ID, attrFilter, listAttr, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the access request appproval policy for the given access request item ID.
     * Access request items types can be: \&quot;role\&quot;, \&quot;perm\&quot; or \&quot;app\&quot;&lt;br/&gt;Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param propertyClass The type of access request item. (required)
     * @param ID The role policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request policy attributes to return (optional)
     * @return AccessRequestPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccessRequestPolicy getAccessRequestPolicyForRole(String propertyClass, Long ID, List<String> attrFilter, List<String> listAttr) throws ApiException {
        ApiResponse<AccessRequestPolicy> resp = getAccessRequestPolicyForRoleWithHttpInfo(propertyClass, ID, attrFilter, listAttr);
        return resp.getData();
    }

    /**
     * Get the access request appproval policy for the given access request item ID.
     * Access request items types can be: \&quot;role\&quot;, \&quot;perm\&quot; or \&quot;app\&quot;&lt;br/&gt;Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param propertyClass The type of access request item. (required)
     * @param ID The role policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request policy attributes to return (optional)
     * @return ApiResponse&lt;AccessRequestPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccessRequestPolicy> getAccessRequestPolicyForRoleWithHttpInfo(String propertyClass, Long ID, List<String> attrFilter, List<String> listAttr) throws ApiException {
        com.squareup.okhttp.Call call = getAccessRequestPolicyForRoleValidateBeforeCall(propertyClass, ID, attrFilter, listAttr, null, null);
        Type localVarReturnType = new TypeToken<AccessRequestPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the access request appproval policy for the given access request item ID. (asynchronously)
     * Access request items types can be: \&quot;role\&quot;, \&quot;perm\&quot; or \&quot;app\&quot;&lt;br/&gt;Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param propertyClass The type of access request item. (required)
     * @param ID The role policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request policy attributes to return (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAccessRequestPolicyForRoleAsync(String propertyClass, Long ID, List<String> attrFilter, List<String> listAttr, final ApiCallback<AccessRequestPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccessRequestPolicyForRoleValidateBeforeCall(propertyClass, ID, attrFilter, listAttr, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccessRequestPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApprovalEmailTemplates
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApprovalEmailTemplatesCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/emailtemplates";

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
    private com.squareup.okhttp.Call getApprovalEmailTemplatesValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getApprovalEmailTemplatesCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the approval notification and reminder email template names.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @return EmailTemplates
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EmailTemplates getApprovalEmailTemplates() throws ApiException {
        ApiResponse<EmailTemplates> resp = getApprovalEmailTemplatesWithHttpInfo();
        return resp.getData();
    }

    /**
     * Get the approval notification and reminder email template names.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @return ApiResponse&lt;EmailTemplates&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EmailTemplates> getApprovalEmailTemplatesWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getApprovalEmailTemplatesValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<EmailTemplates>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the approval notification and reminder email template names. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApprovalEmailTemplatesAsync(final ApiCallback<EmailTemplates> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApprovalEmailTemplatesValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EmailTemplates>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDefaultAccessRequestApprovalPolicy
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDefaultAccessRequestApprovalPolicyCall(List<String> attrFilter, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/default";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));

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
    private com.squareup.okhttp.Call getDefaultAccessRequestApprovalPolicyValidateBeforeCall(List<String> attrFilter, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDefaultAccessRequestApprovalPolicyCall(attrFilter, listAttr, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the default access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @return AccessRequestApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccessRequestApprovalPolicy getDefaultAccessRequestApprovalPolicy(List<String> attrFilter, List<String> listAttr) throws ApiException {
        ApiResponse<AccessRequestApprovalPolicy> resp = getDefaultAccessRequestApprovalPolicyWithHttpInfo(attrFilter, listAttr);
        return resp.getData();
    }

    /**
     * Get the default access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @return ApiResponse&lt;AccessRequestApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccessRequestApprovalPolicy> getDefaultAccessRequestApprovalPolicyWithHttpInfo(List<String> attrFilter, List<String> listAttr) throws ApiException {
        com.squareup.okhttp.Call call = getDefaultAccessRequestApprovalPolicyValidateBeforeCall(attrFilter, listAttr, null, null);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the default access request approval policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of access request approval policy attributes to return (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDefaultAccessRequestApprovalPolicyAsync(List<String> attrFilter, List<String> listAttr, final ApiCallback<AccessRequestApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDefaultAccessRequestApprovalPolicyValidateBeforeCall(attrFilter, listAttr, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importAssignments
     * @param body - The import data (required)
     * @param ID - ID of access request approval policy to update assignments (required)
     * @param reportOnly - Flag to only generate report (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importAssignmentsCall(ModelImport body, Long ID, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/import"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call importAssignmentsValidateBeforeCall(ModelImport body, Long ID, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importAssignments(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling importAssignments(Async)");
        }
        
        com.squareup.okhttp.Call call = importAssignmentsCall(body, ID, reportOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import of assignments for the given access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body - The import data (required)
     * @param ID - ID of access request approval policy to update assignments (required)
     * @param reportOnly - Flag to only generate report (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importAssignments(ModelImport body, Long ID, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importAssignmentsWithHttpInfo(body, ID, reportOnly);
        return resp.getData();
    }

    /**
     * Perform an import of assignments for the given access request approval policy
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body - The import data (required)
     * @param ID - ID of access request approval policy to update assignments (required)
     * @param reportOnly - Flag to only generate report (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importAssignmentsWithHttpInfo(ModelImport body, Long ID, Boolean reportOnly) throws ApiException {
        com.squareup.okhttp.Call call = importAssignmentsValidateBeforeCall(body, ID, reportOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import of assignments for the given access request approval policy (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body - The import data (required)
     * @param ID - ID of access request approval policy to update assignments (required)
     * @param reportOnly - Flag to only generate report (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importAssignmentsAsync(ModelImport body, Long ID, Boolean reportOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importAssignmentsValidateBeforeCall(body, ID, reportOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importMultiPart
     * @param uploadedInputStream  (required)
     * @param refs - Flag to import references to allowed and disallowed item requesters (optional, default to false)
     * @param assignments - Flag to import access request policy assignments (optional, default to false)
     * @param reportOnly - Flag to generate the report but not create any objects (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importMultiPartCall(File uploadedInputStream, Boolean refs, Boolean assignments, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/import";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (assignments != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("assignments", assignments));
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
    private com.squareup.okhttp.Call importMultiPartValidateBeforeCall(File uploadedInputStream, Boolean refs, Boolean assignments, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importMultiPart(Async)");
        }
        
        com.squareup.okhttp.Call call = importMultiPartCall(uploadedInputStream, refs, assignments, reportOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import from zip file
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param uploadedInputStream  (required)
     * @param refs - Flag to import references to allowed and disallowed item requesters (optional, default to false)
     * @param assignments - Flag to import access request policy assignments (optional, default to false)
     * @param reportOnly - Flag to generate the report but not create any objects (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importMultiPart(File uploadedInputStream, Boolean refs, Boolean assignments, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importMultiPartWithHttpInfo(uploadedInputStream, refs, assignments, reportOnly);
        return resp.getData();
    }

    /**
     * Perform an import from zip file
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param uploadedInputStream  (required)
     * @param refs - Flag to import references to allowed and disallowed item requesters (optional, default to false)
     * @param assignments - Flag to import access request policy assignments (optional, default to false)
     * @param reportOnly - Flag to generate the report but not create any objects (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importMultiPartWithHttpInfo(File uploadedInputStream, Boolean refs, Boolean assignments, Boolean reportOnly) throws ApiException {
        com.squareup.okhttp.Call call = importMultiPartValidateBeforeCall(uploadedInputStream, refs, assignments, reportOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import from zip file (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param uploadedInputStream  (required)
     * @param refs - Flag to import references to allowed and disallowed item requesters (optional, default to false)
     * @param assignments - Flag to import access request policy assignments (optional, default to false)
     * @param reportOnly - Flag to generate the report but not create any objects (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importMultiPartAsync(File uploadedInputStream, Boolean refs, Boolean assignments, Boolean reportOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importMultiPartValidateBeforeCall(uploadedInputStream, refs, assignments, reportOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importPreviewForID
     * @param body - The import data (required)
     * @param ID - The id of the access request policy to import assignments for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importPreviewForIDCall(ModelImport body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/import/preview"
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
    private com.squareup.okhttp.Call importPreviewForIDValidateBeforeCall(ModelImport body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importPreviewForID(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling importPreviewForID(Async)");
        }
        
        com.squareup.okhttp.Call call = importPreviewForIDCall(body, ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body - The import data (required)
     * @param ID - The id of the access request policy to import assignments for (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importPreviewForID(ModelImport body, Long ID) throws ApiException {
        ApiResponse<Response> resp = importPreviewForIDWithHttpInfo(body, ID);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body - The import data (required)
     * @param ID - The id of the access request policy to import assignments for (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importPreviewForIDWithHttpInfo(ModelImport body, Long ID) throws ApiException {
        com.squareup.okhttp.Call call = importPreviewForIDValidateBeforeCall(body, ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references  of the import document, returning a import preview document to the client. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body - The import data (required)
     * @param ID - The id of the access request policy to import assignments for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importPreviewForIDAsync(ModelImport body, Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importPreviewForIDValidateBeforeCall(body, ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importPreviewMultiPart
     * @param uploadedInputStream  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importPreviewMultiPartCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/import/preview";

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
    private com.squareup.okhttp.Call importPreviewMultiPartValidateBeforeCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importPreviewMultiPart(Async)");
        }
        
        com.squareup.okhttp.Call call = importPreviewMultiPartCall(uploadedInputStream, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param uploadedInputStream  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importPreviewMultiPart(File uploadedInputStream) throws ApiException {
        ApiResponse<Response> resp = importPreviewMultiPartWithHttpInfo(uploadedInputStream);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param uploadedInputStream  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importPreviewMultiPartWithHttpInfo(File uploadedInputStream) throws ApiException {
        com.squareup.okhttp.Call call = importPreviewMultiPartValidateBeforeCall(uploadedInputStream, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param uploadedInputStream  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importPreviewMultiPartAsync(File uploadedInputStream, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importPreviewMultiPartValidateBeforeCall(uploadedInputStream, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/access/request/approval/import/resolve/apps";

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
     * Resolve Applications in the import document
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body - The import data (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importResolveApplications(ModelImport body) throws ApiException {
        ApiResponse<Response> resp = importResolveApplicationsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
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
     * Resolve Applications in the import document (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
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
     * Build call for setApprovalLevel
     * @param body List of request items whose access request policy is to be cleared. (required)
     * @param ID ID of access request policy (required)
     * @param level Technical role approval level (optional, default to permission)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setApprovalLevelCall(RequestItems body, Long ID, String level, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/requestitems/approval"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (level != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("level", level));

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
    private com.squareup.okhttp.Call setApprovalLevelValidateBeforeCall(RequestItems body, Long ID, String level, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setApprovalLevel(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling setApprovalLevel(Async)");
        }
        
        com.squareup.okhttp.Call call = setApprovalLevelCall(body, ID, level, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Assign the approval level for the access request approval policy for the specified items.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items whose access request policy is to be cleared. (required)
     * @param ID ID of access request policy (required)
     * @param level Technical role approval level (optional, default to permission)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status setApprovalLevel(RequestItems body, Long ID, String level) throws ApiException {
        ApiResponse<Status> resp = setApprovalLevelWithHttpInfo(body, ID, level);
        return resp.getData();
    }

    /**
     * Assign the approval level for the access request approval policy for the specified items.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items whose access request policy is to be cleared. (required)
     * @param ID ID of access request policy (required)
     * @param level Technical role approval level (optional, default to permission)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> setApprovalLevelWithHttpInfo(RequestItems body, Long ID, String level) throws ApiException {
        com.squareup.okhttp.Call call = setApprovalLevelValidateBeforeCall(body, ID, level, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Assign the approval level for the access request approval policy for the specified items. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items whose access request policy is to be cleared. (required)
     * @param ID ID of access request policy (required)
     * @param level Technical role approval level (optional, default to permission)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setApprovalLevelAsync(RequestItems body, Long ID, String level, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setApprovalLevelValidateBeforeCall(body, ID, level, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setRequestItems
     * @param body List of request items to set in the request approval policy. (required)
     * @param ID ID of access request approval policy (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setRequestItemsCall(RequestItems body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}/approvalitems"
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
    private com.squareup.okhttp.Call setRequestItemsValidateBeforeCall(RequestItems body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setRequestItems(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling setRequestItems(Async)");
        }
        
        com.squareup.okhttp.Call call = setRequestItemsCall(body, ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set approval items for an access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items to set in the request approval policy. (required)
     * @param ID ID of access request approval policy (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response setRequestItems(RequestItems body, Long ID) throws ApiException {
        ApiResponse<Response> resp = setRequestItemsWithHttpInfo(body, ID);
        return resp.getData();
    }

    /**
     * Set approval items for an access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items to set in the request approval policy. (required)
     * @param ID ID of access request approval policy (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> setRequestItemsWithHttpInfo(RequestItems body, Long ID) throws ApiException {
        com.squareup.okhttp.Call call = setRequestItemsValidateBeforeCall(body, ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set approval items for an access request approval policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body List of request items to set in the request approval policy. (required)
     * @param ID ID of access request approval policy (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setRequestItemsAsync(RequestItems body, Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setRequestItemsValidateBeforeCall(body, ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param roles - Export associated technical roles (optional)
     * @param broles - Export associated business roles (optional)
     * @param apps - Export associated application (optional)
     * @param _default - Export default policy (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(Download body, List<String> attrFilter, Boolean roles, Boolean broles, Boolean apps, Boolean _default, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (roles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roles", roles));
        if (broles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("broles", broles));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
        if (_default != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("default", _default));

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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(Download body, List<String> attrFilter, Boolean roles, Boolean broles, Boolean apps, Boolean _default, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownloadCall(body, attrFilter, roles, broles, apps, _default, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the access request approval policy download.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param roles - Export associated technical roles (optional)
     * @param broles - Export associated business roles (optional)
     * @param apps - Export associated application (optional)
     * @param _default - Export default policy (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(Download body, List<String> attrFilter, Boolean roles, Boolean broles, Boolean apps, Boolean _default) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(body, attrFilter, roles, broles, apps, _default);
        return resp.getData();
    }

    /**
     * Start the access request approval policy download.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param roles - Export associated technical roles (optional)
     * @param broles - Export associated business roles (optional)
     * @param apps - Export associated application (optional)
     * @param _default - Export default policy (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(Download body, List<String> attrFilter, Boolean roles, Boolean broles, Boolean apps, Boolean _default) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, attrFilter, roles, broles, apps, _default, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the access request approval policy download. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body -  The download request node (required)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param roles - Export associated technical roles (optional)
     * @param broles - Export associated business roles (optional)
     * @param apps - Export associated application (optional)
     * @param _default - Export default policy (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(Download body, List<String> attrFilter, Boolean roles, Boolean broles, Boolean apps, Boolean _default, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, attrFilter, roles, broles, apps, _default, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateAccessRequestApprovalPolicy
     * @param body The updates to the access request approval policy. (required)
     * @param ID The access request approval policy ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateAccessRequestApprovalPolicyCall(AccessRequestApprovalPolicy body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/access/request/approval/{ID}"
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
    private com.squareup.okhttp.Call updateAccessRequestApprovalPolicyValidateBeforeCall(AccessRequestApprovalPolicy body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateAccessRequestApprovalPolicy(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling updateAccessRequestApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = updateAccessRequestApprovalPolicyCall(body, ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body The updates to the access request approval policy. (required)
     * @param ID The access request approval policy ID (required)
     * @return AccessRequestApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccessRequestApprovalPolicy updateAccessRequestApprovalPolicy(AccessRequestApprovalPolicy body, Long ID) throws ApiException {
        ApiResponse<AccessRequestApprovalPolicy> resp = updateAccessRequestApprovalPolicyWithHttpInfo(body, ID);
        return resp.getData();
    }

    /**
     * Update access request approval policy.
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body The updates to the access request approval policy. (required)
     * @param ID The access request approval policy ID (required)
     * @return ApiResponse&lt;AccessRequestApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccessRequestApprovalPolicy> updateAccessRequestApprovalPolicyWithHttpInfo(AccessRequestApprovalPolicy body, Long ID) throws ApiException {
        com.squareup.okhttp.Call call = updateAccessRequestApprovalPolicyValidateBeforeCall(body, ID, null, null);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update access request approval policy. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Customer Administrator 
     * @param body The updates to the access request approval policy. (required)
     * @param ID The access request approval policy ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateAccessRequestApprovalPolicyAsync(AccessRequestApprovalPolicy body, Long ID, final ApiCallback<AccessRequestApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateAccessRequestApprovalPolicyValidateBeforeCall(body, ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccessRequestApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
