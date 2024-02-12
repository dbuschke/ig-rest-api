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


import de.araneaconsult.codegen.ig.rest.model.ApprovalPolicies;
import de.araneaconsult.codegen.ig.rest.model.ApprovalPolicy;
import de.araneaconsult.codegen.ig.rest.model.Approvalpolicycheck;
import de.araneaconsult.codegen.ig.rest.model.Download;
import de.araneaconsult.codegen.ig.rest.model.ModelImport;
import de.araneaconsult.codegen.ig.rest.model.Response;
import de.araneaconsult.codegen.ig.rest.model.SearchCriteria;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyBroleApprovalPolicyApi {
    private ApiClient apiClient;

    public PolicyBroleApprovalPolicyApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyBroleApprovalPolicyApi(ApiClient apiClient) {
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
        String localVarPath = "/policy/brole/approvalPolicy/download/{id}"
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
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
     * Build call for createApprovalPolicy
     * @param body The approval policy to create. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createApprovalPolicyCall(ApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/new";

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
    private com.squareup.okhttp.Call createApprovalPolicyValidateBeforeCall(ApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = createApprovalPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create a approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body The approval policy to create. (required)
     * @return ApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalPolicy createApprovalPolicy(ApprovalPolicy body) throws ApiException {
        ApiResponse<ApprovalPolicy> resp = createApprovalPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create a approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body The approval policy to create. (required)
     * @return ApiResponse&lt;ApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalPolicy> createApprovalPolicyWithHttpInfo(ApprovalPolicy body) throws ApiException {
        com.squareup.okhttp.Call call = createApprovalPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a approval policy. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body The approval policy to create. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createApprovalPolicyAsync(ApprovalPolicy body, final ApiCallback<ApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createApprovalPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteApprovalPolicy
     * @param id The approval policy ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteApprovalPolicyCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/{id}"
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
    private com.squareup.okhttp.Call deleteApprovalPolicyValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteApprovalPolicyCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the approval policy with the given ID.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The approval policy ID. (required)
     * @return ApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalPolicy deleteApprovalPolicy(Long id) throws ApiException {
        ApiResponse<ApprovalPolicy> resp = deleteApprovalPolicyWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete the approval policy with the given ID.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The approval policy ID. (required)
     * @return ApiResponse&lt;ApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalPolicy> deleteApprovalPolicyWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteApprovalPolicyValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the approval policy with the given ID. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param id The approval policy ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteApprovalPolicyAsync(Long id, final ApiCallback<ApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteApprovalPolicyValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
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
        String localVarPath = "/policy/brole/approvalPolicy/download/{id}"
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Build call for getApprovalPolicies
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
     * @param showGlobal Include the global default approval policy in the result set. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApprovalPoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showGlobal, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy";

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
        if (showGlobal != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showGlobal", showGlobal));

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
    private com.squareup.okhttp.Call getApprovalPoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showGlobal, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getApprovalPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getApprovalPoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, showGlobal, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of approval policies, also include the information about  approval policy
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * @param showGlobal Include the global default approval policy in the result set. (optional, default to false)
     * @return ApprovalPolicies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalPolicies getApprovalPolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showGlobal) throws ApiException {
        ApiResponse<ApprovalPolicies> resp = getApprovalPoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, showGlobal);
        return resp.getData();
    }

    /**
     * Return list of approval policies, also include the information about  approval policy
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * @param showGlobal Include the global default approval policy in the result set. (optional, default to false)
     * @return ApiResponse&lt;ApprovalPolicies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalPolicies> getApprovalPoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showGlobal) throws ApiException {
        com.squareup.okhttp.Call call = getApprovalPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, showGlobal, null, null);
        Type localVarReturnType = new TypeToken<ApprovalPolicies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of approval policies, also include the information about  approval policy (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * @param showGlobal Include the global default approval policy in the result set. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApprovalPoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showGlobal, final ApiCallback<ApprovalPolicies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApprovalPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, showGlobal, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalPolicies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApprovalPolicy
     * @param ID The approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApprovalPolicyCall(Long ID, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

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
    private com.squareup.okhttp.Call getApprovalPolicyValidateBeforeCall(Long ID, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = getApprovalPolicyCall(ID, attrFilter, sortBy, sortOrder, listAttr, allowDeleted, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the approval policy with the given ID.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @return ApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalPolicy getApprovalPolicy(Long ID, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        ApiResponse<ApprovalPolicy> resp = getApprovalPolicyWithHttpInfo(ID, attrFilter, sortBy, sortOrder, listAttr, allowDeleted);
        return resp.getData();
    }

    /**
     * Get the approval policy with the given ID.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @return ApiResponse&lt;ApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalPolicy> getApprovalPolicyWithHttpInfo(Long ID, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        com.squareup.okhttp.Call call = getApprovalPolicyValidateBeforeCall(ID, attrFilter, sortBy, sortOrder, listAttr, allowDeleted, null, null);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the approval policy with the given ID. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param ID The approval policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApprovalPolicyAsync(Long ID, List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ApiCallback<ApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApprovalPolicyValidateBeforeCall(ID, attrFilter, sortBy, sortOrder, listAttr, allowDeleted, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApprovalPolicyStateChanges
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @param ID The approval policy ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApprovalPolicyStateChangesCall(ApprovalPolicy body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/{ID}/stateChanges"
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
    private com.squareup.okhttp.Call getApprovalPolicyStateChangesValidateBeforeCall(ApprovalPolicy body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getApprovalPolicyStateChanges(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getApprovalPolicyStateChanges(Async)");
        }
        
        com.squareup.okhttp.Call call = getApprovalPolicyStateChangesCall(body, ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business roles that will require an approval state changes given the requested changes.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @param ID The approval policy ID. (required)
     * @return Approvalpolicycheck
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Approvalpolicycheck getApprovalPolicyStateChanges(ApprovalPolicy body, Long ID) throws ApiException {
        ApiResponse<Approvalpolicycheck> resp = getApprovalPolicyStateChangesWithHttpInfo(body, ID);
        return resp.getData();
    }

    /**
     * Get the business roles that will require an approval state changes given the requested changes.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @param ID The approval policy ID. (required)
     * @return ApiResponse&lt;Approvalpolicycheck&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Approvalpolicycheck> getApprovalPolicyStateChangesWithHttpInfo(ApprovalPolicy body, Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getApprovalPolicyStateChangesValidateBeforeCall(body, ID, null, null);
        Type localVarReturnType = new TypeToken<Approvalpolicycheck>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business roles that will require an approval state changes given the requested changes. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @param ID The approval policy ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApprovalPolicyStateChangesAsync(ApprovalPolicy body, Long ID, final ApiCallback<Approvalpolicycheck> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApprovalPolicyStateChangesValidateBeforeCall(body, ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Approvalpolicycheck>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getGlobalApprovalPolicy
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getGlobalApprovalPolicyCall(List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/global";

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
    private com.squareup.okhttp.Call getGlobalApprovalPolicyValidateBeforeCall(List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getGlobalApprovalPolicyCall(attrFilter, sortBy, sortOrder, listAttr, allowDeleted, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the GLOBAL approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @return ApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalPolicy getGlobalApprovalPolicy(List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        ApiResponse<ApprovalPolicy> resp = getGlobalApprovalPolicyWithHttpInfo(attrFilter, sortBy, sortOrder, listAttr, allowDeleted);
        return resp.getData();
    }

    /**
     * Get the GLOBAL approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @return ApiResponse&lt;ApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalPolicy> getGlobalApprovalPolicyWithHttpInfo(List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted) throws ApiException {
        com.squareup.okhttp.Call call = getGlobalApprovalPolicyValidateBeforeCall(attrFilter, sortBy, sortOrder, listAttr, allowDeleted, null, null);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the GLOBAL approval policy. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param sortBy The approval policy attribute name used for sorting. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of approval policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted approval policies. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getGlobalApprovalPolicyAsync(List<String> attrFilter, String sortBy, String sortOrder, List<String> listAttr, Boolean allowDeleted, final ApiCallback<ApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getGlobalApprovalPolicyValidateBeforeCall(attrFilter, sortBy, sortOrder, listAttr, allowDeleted, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getNewApprovalPolicyStateChanges
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getNewApprovalPolicyStateChangesCall(ApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/global/stateChanges";

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
    private com.squareup.okhttp.Call getNewApprovalPolicyStateChangesValidateBeforeCall(ApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getNewApprovalPolicyStateChanges(Async)");
        }
        
        com.squareup.okhttp.Call call = getNewApprovalPolicyStateChangesCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business roles that will require an approval state changes given the requested changes.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @return Approvalpolicycheck
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Approvalpolicycheck getNewApprovalPolicyStateChanges(ApprovalPolicy body) throws ApiException {
        ApiResponse<Approvalpolicycheck> resp = getNewApprovalPolicyStateChangesWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Get the business roles that will require an approval state changes given the requested changes.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @return ApiResponse&lt;Approvalpolicycheck&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Approvalpolicycheck> getNewApprovalPolicyStateChangesWithHttpInfo(ApprovalPolicy body) throws ApiException {
        com.squareup.okhttp.Call call = getNewApprovalPolicyStateChangesValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Approvalpolicycheck>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business roles that will require an approval state changes given the requested changes. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body Contains requested changes to either implicit/explicit approval and/or business roles added or deleted from the approval policy. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getNewApprovalPolicyStateChangesAsync(ApprovalPolicy body, final ApiCallback<Approvalpolicycheck> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getNewApprovalPolicyStateChangesValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Approvalpolicycheck>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importApprovalPolicies
     * @param body - The import data (required)
     * @param refs - Flag to import references to approvers (optional, default to false)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importApprovalPoliciesCall(ModelImport body, Boolean refs, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/import";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
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
    private com.squareup.okhttp.Call importApprovalPoliciesValidateBeforeCall(ModelImport body, Boolean refs, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importApprovalPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = importApprovalPoliciesCall(body, refs, reportOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Import approval policies.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body - The import data (required)
     * @param refs - Flag to import references to approvers (optional, default to false)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importApprovalPolicies(ModelImport body, Boolean refs, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importApprovalPoliciesWithHttpInfo(body, refs, reportOnly);
        return resp.getData();
    }

    /**
     * Import approval policies.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body - The import data (required)
     * @param refs - Flag to import references to approvers (optional, default to false)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importApprovalPoliciesWithHttpInfo(ModelImport body, Boolean refs, Boolean reportOnly) throws ApiException {
        com.squareup.okhttp.Call call = importApprovalPoliciesValidateBeforeCall(body, refs, reportOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Import approval policies. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body - The import data (required)
     * @param refs - Flag to import references to approvers (optional, default to false)
     * @param reportOnly - Flag to only generate the report, do not create entities (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importApprovalPoliciesAsync(ModelImport body, Boolean refs, Boolean reportOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importApprovalPoliciesValidateBeforeCall(body, refs, reportOnly, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/brole/approvalPolicy/import/preview";

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
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body - The import data (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importPreview(ModelImport body) throws ApiException {
        ApiResponse<Response> resp = importPreviewWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Build call for startDownload
     * @param id - The approval policies to export (optional)
     * @param refs - Flag to include approvers references in export file (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(List<Long> id, Boolean refs, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(List<Long> id, Boolean refs, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startDownloadCall(id, refs, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the approval policy download
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The approval policies to export (optional)
     * @param refs - Flag to include approvers references in export file (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(List<Long> id, Boolean refs, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(id, refs, attrFilter);
        return resp.getData();
    }

    /**
     * Start the approval policy download
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The approval policies to export (optional)
     * @param refs - Flag to include approvers references in export file (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(List<Long> id, Boolean refs, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, refs, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the approval policy download (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param id - The approval policies to export (optional)
     * @param refs - Flag to include approvers references in export file (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(List<Long> id, Boolean refs, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, refs, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_0
     * @param body - The download request node (required)
     * @param refs - Flag to export associated approvers (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Call(Download body, Boolean refs, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
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
    private com.squareup.okhttp.Call startDownload_0ValidateBeforeCall(Download body, Boolean refs, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_0Call(body, refs, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the approval policy download
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body - The download request node (required)
     * @param refs - Flag to export associated approvers (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body, Boolean refs, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body, refs, attrFilter);
        return resp.getData();
    }

    /**
     * Start the approval policy download
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body - The download request node (required)
     * @param refs - Flag to export associated approvers (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_0WithHttpInfo(Download body, Boolean refs, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, refs, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the approval policy download (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
     * @param body - The download request node (required)
     * @param refs - Flag to export associated approvers (optional, default to false)
     * @param attrFilter - List of attribute filters: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Async(Download body, Boolean refs, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, refs, attrFilter, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/brole/approvalPolicy/download/{id}/status"
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator * Role owner 
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
     * Build call for updateApprovalPolicy
     * @param body The updates to the approval policy. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateApprovalPolicyCall(ApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/global";

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
    private com.squareup.okhttp.Call updateApprovalPolicyValidateBeforeCall(ApprovalPolicy body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateApprovalPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = updateApprovalPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update global default approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the approval policy. (required)
     * @return ApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalPolicy updateApprovalPolicy(ApprovalPolicy body) throws ApiException {
        ApiResponse<ApprovalPolicy> resp = updateApprovalPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Update global default approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the approval policy. (required)
     * @return ApiResponse&lt;ApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalPolicy> updateApprovalPolicyWithHttpInfo(ApprovalPolicy body) throws ApiException {
        com.squareup.okhttp.Call call = updateApprovalPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update global default approval policy. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the approval policy. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateApprovalPolicyAsync(ApprovalPolicy body, final ApiCallback<ApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateApprovalPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateApprovalPolicy_0
     * @param body The updates to the approval policy. (required)
     * @param id The approval policy ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateApprovalPolicy_0Call(ApprovalPolicy body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/brole/approvalPolicy/{id}"
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
    private com.squareup.okhttp.Call updateApprovalPolicy_0ValidateBeforeCall(ApprovalPolicy body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateApprovalPolicy_0(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateApprovalPolicy_0(Async)");
        }
        
        com.squareup.okhttp.Call call = updateApprovalPolicy_0Call(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the approval policy. (required)
     * @param id The approval policy ID (required)
     * @return ApprovalPolicy
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalPolicy updateApprovalPolicy_0(ApprovalPolicy body, Long id) throws ApiException {
        ApiResponse<ApprovalPolicy> resp = updateApprovalPolicy_0WithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update approval policy.
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the approval policy. (required)
     * @param id The approval policy ID (required)
     * @return ApiResponse&lt;ApprovalPolicy&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalPolicy> updateApprovalPolicy_0WithHttpInfo(ApprovalPolicy body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateApprovalPolicy_0ValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update approval policy. (asynchronously)
     * Accepted Roles: * Business Role Manager * Business Roles Administrator * Customer Administrator 
     * @param body The updates to the approval policy. (required)
     * @param id The approval policy ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateApprovalPolicy_0Async(ApprovalPolicy body, Long id, final ApiCallback<ApprovalPolicy> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateApprovalPolicy_0ValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalPolicy>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
