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


import io.swagger.client.model.DataPolicyCalcNode;
import io.swagger.client.model.DataPolicyListNode;
import io.swagger.client.model.DataPolicyNode;
import io.swagger.client.model.DataPolicyResultListNode;
import io.swagger.client.model.Download;
import java.io.File;
import io.swagger.client.model.Ids;
import io.swagger.client.model.Response;
import io.swagger.client.model.SearchCriteria;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyDatapoliciesApi {
    private ApiClient apiClient;

    public PolicyDatapoliciesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyDatapoliciesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for cancelCalculationTask
     * @param body - reason calculation is being interrupted (required)
     * @param id - calculation task id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cancelCalculationTaskCall(DataPolicyCalcNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/cancel/{id}"
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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call cancelCalculationTaskValidateBeforeCall(DataPolicyCalcNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling cancelCalculationTask(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling cancelCalculationTask(Async)");
        }
        
        com.squareup.okhttp.Call call = cancelCalculationTaskCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Make request to cancel calculation task
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - reason calculation is being interrupted (required)
     * @param id - calculation task id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response cancelCalculationTask(DataPolicyCalcNode body, Long id) throws ApiException {
        ApiResponse<Response> resp = cancelCalculationTaskWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Make request to cancel calculation task
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - reason calculation is being interrupted (required)
     * @param id - calculation task id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> cancelCalculationTaskWithHttpInfo(DataPolicyCalcNode body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = cancelCalculationTaskValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Make request to cancel calculation task (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - reason calculation is being interrupted (required)
     * @param id - calculation task id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cancelCalculationTaskAsync(DataPolicyCalcNode body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = cancelCalculationTaskValidateBeforeCall(body, id, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/datapolicies/download/{id}"
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Build call for deleteDataPolicy
     * @param id -  data police id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteDataPolicyCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/{id}"
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
    private com.squareup.okhttp.Call deleteDataPolicyValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteDataPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteDataPolicyCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Mark data policy as deleted
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -  data police id (required)
     * @return DataPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyNode deleteDataPolicy(Long id) throws ApiException {
        ApiResponse<DataPolicyNode> resp = deleteDataPolicyWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Mark data policy as deleted
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -  data police id (required)
     * @return ApiResponse&lt;DataPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyNode> deleteDataPolicyWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteDataPolicyValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Mark data policy as deleted (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -  data police id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteDataPolicyAsync(Long id, final ApiCallback<DataPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteDataPolicyValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for estimateDataPolicyViolation
     * @param body - DataPolicyNode object (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call estimateDataPolicyViolationCall(DataPolicyNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/estimateViolation";

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
    private com.squareup.okhttp.Call estimateDataPolicyViolationValidateBeforeCall(DataPolicyNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling estimateDataPolicyViolation(Async)");
        }
        
        com.squareup.okhttp.Call call = estimateDataPolicyViolationCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get estimated violations against new created data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - DataPolicyNode object (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response estimateDataPolicyViolation(DataPolicyNode body) throws ApiException {
        ApiResponse<Response> resp = estimateDataPolicyViolationWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Get estimated violations against new created data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - DataPolicyNode object (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> estimateDataPolicyViolationWithHttpInfo(DataPolicyNode body) throws ApiException {
        com.squareup.okhttp.Call call = estimateDataPolicyViolationValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get estimated violations against new created data policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - DataPolicyNode object (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call estimateDataPolicyViolationAsync(DataPolicyNode body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = estimateDataPolicyViolationValidateBeforeCall(body, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/datapolicies/download/{id}"
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Build call for getDataPolicies
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param policyType - Filter the policies by dataPolityType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION (optional)
     * @param criteriaType -  criteria types list (optional)
     * @param showRun -  Show last run (optional, default to false)
     * @param excludeCount -  exclude COUNT criteria policies, default is false (optional, default to false)
     * @param forSchedule - flag to return data policies for schedule, default is false; all others are ignored if this one is true (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, String policyType, String dataSourceType, List<String> criteriaType, Boolean showRun, Boolean excludeCount, Boolean forSchedule, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies";

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
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (policyType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyType", policyType));
        if (dataSourceType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dataSourceType", dataSourceType));
        if (criteriaType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "criteriaType", criteriaType));
        if (showRun != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showRun", showRun));
        if (excludeCount != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("excludeCount", excludeCount));
        if (forSchedule != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("forSchedule", forSchedule));

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
    private com.squareup.okhttp.Call getDataPoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, String policyType, String dataSourceType, List<String> criteriaType, Boolean showRun, Boolean excludeCount, Boolean forSchedule, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getDataPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getDataPoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, policyType, dataSourceType, criteriaType, showRun, excludeCount, forSchedule, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param policyType - Filter the policies by dataPolityType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION (optional)
     * @param criteriaType -  criteria types list (optional)
     * @param showRun -  Show last run (optional, default to false)
     * @param excludeCount -  exclude COUNT criteria policies, default is false (optional, default to false)
     * @param forSchedule - flag to return data policies for schedule, default is false; all others are ignored if this one is true (optional, default to false)
     * @return DataPolicyListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyListNode getDataPolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, String policyType, String dataSourceType, List<String> criteriaType, Boolean showRun, Boolean excludeCount, Boolean forSchedule) throws ApiException {
        ApiResponse<DataPolicyListNode> resp = getDataPoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, policyType, dataSourceType, criteriaType, showRun, excludeCount, forSchedule);
        return resp.getData();
    }

    /**
     * Return list of data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param policyType - Filter the policies by dataPolityType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION (optional)
     * @param criteriaType -  criteria types list (optional)
     * @param showRun -  Show last run (optional, default to false)
     * @param excludeCount -  exclude COUNT criteria policies, default is false (optional, default to false)
     * @param forSchedule - flag to return data policies for schedule, default is false; all others are ignored if this one is true (optional, default to false)
     * @return ApiResponse&lt;DataPolicyListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyListNode> getDataPoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, String policyType, String dataSourceType, List<String> criteriaType, Boolean showRun, Boolean excludeCount, Boolean forSchedule) throws ApiException {
        com.squareup.okhttp.Call call = getDataPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, policyType, dataSourceType, criteriaType, showRun, excludeCount, forSchedule, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of data policies (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param policyType - Filter the policies by dataPolityType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION (optional)
     * @param criteriaType -  criteria types list (optional)
     * @param showRun -  Show last run (optional, default to false)
     * @param excludeCount -  exclude COUNT criteria policies, default is false (optional, default to false)
     * @param forSchedule - flag to return data policies for schedule, default is false; all others are ignored if this one is true (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, String policyType, String dataSourceType, List<String> criteriaType, Boolean showRun, Boolean excludeCount, Boolean forSchedule, final ApiCallback<DataPolicyListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, policyType, dataSourceType, criteriaType, showRun, excludeCount, forSchedule, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicy
     * @param id - data policy id, return active data policy by id (required)
     * @param uniqueId - (optional) data policy unique id, return active data policy by uniqueId, passed data policy id is ignored (optional)
     * @param remediationRunId - (optional) remediation run id, return data policy associated with passed remediation run, passed data policy id is ignored (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyCall(Long id, String uniqueId, Long remediationRunId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (uniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueId", uniqueId));
        if (remediationRunId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("remediationRunId", remediationRunId));

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
    private com.squareup.okhttp.Call getDataPolicyValidateBeforeCall(Long id, String uniqueId, Long remediationRunId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getDataPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = getDataPolicyCall(id, uniqueId, remediationRunId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy by specified id or unique id or remediation run id
     * Accepted Roles: * Customer Administrator * Data Administrator * Fulfillment Administrator 
     * @param id - data policy id, return active data policy by id (required)
     * @param uniqueId - (optional) data policy unique id, return active data policy by uniqueId, passed data policy id is ignored (optional)
     * @param remediationRunId - (optional) remediation run id, return data policy associated with passed remediation run, passed data policy id is ignored (optional)
     * @return DataPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyNode getDataPolicy(Long id, String uniqueId, Long remediationRunId) throws ApiException {
        ApiResponse<DataPolicyNode> resp = getDataPolicyWithHttpInfo(id, uniqueId, remediationRunId);
        return resp.getData();
    }

    /**
     * Get data policy by specified id or unique id or remediation run id
     * Accepted Roles: * Customer Administrator * Data Administrator * Fulfillment Administrator 
     * @param id - data policy id, return active data policy by id (required)
     * @param uniqueId - (optional) data policy unique id, return active data policy by uniqueId, passed data policy id is ignored (optional)
     * @param remediationRunId - (optional) remediation run id, return data policy associated with passed remediation run, passed data policy id is ignored (optional)
     * @return ApiResponse&lt;DataPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyNode> getDataPolicyWithHttpInfo(Long id, String uniqueId, Long remediationRunId) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyValidateBeforeCall(id, uniqueId, remediationRunId, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy by specified id or unique id or remediation run id (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Fulfillment Administrator 
     * @param id - data policy id, return active data policy by id (required)
     * @param uniqueId - (optional) data policy unique id, return active data policy by uniqueId, passed data policy id is ignored (optional)
     * @param remediationRunId - (optional) remediation run id, return data policy associated with passed remediation run, passed data policy id is ignored (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyAsync(Long id, String uniqueId, Long remediationRunId, final ApiCallback<DataPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyValidateBeforeCall(id, uniqueId, remediationRunId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicyCalcStatus
     * @param id - data policy calculation (data production) id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyCalcStatusCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/calcStatus/{id}"
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
    private com.squareup.okhttp.Call getDataPolicyCalcStatusValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getDataPolicyCalcStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getDataPolicyCalcStatusCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy calculation status
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id - data policy calculation (data production) id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDataPolicyCalcStatus(Long id) throws ApiException {
        ApiResponse<Response> resp = getDataPolicyCalcStatusWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get data policy calculation status
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id - data policy calculation (data production) id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDataPolicyCalcStatusWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyCalcStatusValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy calculation status (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id - data policy calculation (data production) id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyCalcStatusAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyCalcStatusValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicyComparison
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId - data source collection ids (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyComparisonCall(Long mainDpnId, List<Long> dpnId, List<Long> dpId, Boolean returnEntities, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/compare";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (mainDpnId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("mainDpnId", mainDpnId));
        if (dpnId != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "dpnId", dpnId));
        if (dpId != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "dpId", dpId));
        if (returnEntities != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("returnEntities", returnEntities));
        if (showCurated != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCurated", showCurated));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
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
    private com.squareup.okhttp.Call getDataPolicyComparisonValidateBeforeCall(Long mainDpnId, List<Long> dpnId, List<Long> dpId, Boolean returnEntities, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDataPolicyComparisonCall(mainDpnId, dpnId, dpId, returnEntities, showCurated, size, indexFrom, listAttr, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Compare passed data source collections or publications against the specified data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId - data source collection ids (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @return DataPolicyResultListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyResultListNode getDataPolicyComparison(Long mainDpnId, List<Long> dpnId, List<Long> dpId, Boolean returnEntities, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr) throws ApiException {
        ApiResponse<DataPolicyResultListNode> resp = getDataPolicyComparisonWithHttpInfo(mainDpnId, dpnId, dpId, returnEntities, showCurated, size, indexFrom, listAttr);
        return resp.getData();
    }

    /**
     * Compare passed data source collections or publications against the specified data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId - data source collection ids (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @return ApiResponse&lt;DataPolicyResultListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyResultListNode> getDataPolicyComparisonWithHttpInfo(Long mainDpnId, List<Long> dpnId, List<Long> dpId, Boolean returnEntities, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyComparisonValidateBeforeCall(mainDpnId, dpnId, dpId, returnEntities, showCurated, size, indexFrom, listAttr, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyResultListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Compare passed data source collections or publications against the specified data policies (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId - data source collection ids (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyComparisonAsync(Long mainDpnId, List<Long> dpnId, List<Long> dpId, Boolean returnEntities, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr, final ApiCallback<DataPolicyResultListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyComparisonValidateBeforeCall(mainDpnId, dpnId, dpId, returnEntities, showCurated, size, indexFrom, listAttr, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyResultListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicyPreviousComparison
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId -  data source collection id (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param returnCollection -  return only previous collection if any (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyPreviousComparisonCall(Long mainDpnId, Long dpnId, List<Long> dpId, Boolean returnEntities, Boolean returnCollection, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/comparePrevious";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (mainDpnId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("mainDpnId", mainDpnId));
        if (dpnId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dpnId", dpnId));
        if (dpId != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "dpId", dpId));
        if (returnEntities != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("returnEntities", returnEntities));
        if (returnCollection != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("returnCollection", returnCollection));
        if (showCurated != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCurated", showCurated));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
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
    private com.squareup.okhttp.Call getDataPolicyPreviousComparisonValidateBeforeCall(Long mainDpnId, Long dpnId, List<Long> dpId, Boolean returnEntities, Boolean returnCollection, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDataPolicyPreviousComparisonCall(mainDpnId, dpnId, dpId, returnEntities, returnCollection, showCurated, size, indexFrom, listAttr, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Compare passed data source collection or publication with the previous one against the specified data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId -  data source collection id (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param returnCollection -  return only previous collection if any (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @return DataPolicyResultListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyResultListNode getDataPolicyPreviousComparison(Long mainDpnId, Long dpnId, List<Long> dpId, Boolean returnEntities, Boolean returnCollection, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr) throws ApiException {
        ApiResponse<DataPolicyResultListNode> resp = getDataPolicyPreviousComparisonWithHttpInfo(mainDpnId, dpnId, dpId, returnEntities, returnCollection, showCurated, size, indexFrom, listAttr);
        return resp.getData();
    }

    /**
     * Compare passed data source collection or publication with the previous one against the specified data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId -  data source collection id (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param returnCollection -  return only previous collection if any (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @return ApiResponse&lt;DataPolicyResultListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyResultListNode> getDataPolicyPreviousComparisonWithHttpInfo(Long mainDpnId, Long dpnId, List<Long> dpId, Boolean returnEntities, Boolean returnCollection, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyPreviousComparisonValidateBeforeCall(mainDpnId, dpnId, dpId, returnEntities, returnCollection, showCurated, size, indexFrom, listAttr, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyResultListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Compare passed data source collection or publication with the previous one against the specified data policies (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param mainDpnId - main dta source collection id the first to compare (optional)
     * @param dpnId -  data source collection id (optional)
     * @param dpId - data policy ids (optional)
     * @param returnEntities - return entities or entity ids and text (optional, default to false)
     * @param returnCollection -  return only previous collection if any (optional, default to false)
     * @param showCurated - compare with or without curation values (optional, default to false)
     * @param size - Number of results to return, used for paging (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging (optional, default to 0)
     * @param listAttr -  The attributes to return in the payload. Returns all if not present. Server can return them in any order. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyPreviousComparisonAsync(Long mainDpnId, Long dpnId, List<Long> dpId, Boolean returnEntities, Boolean returnCollection, Boolean showCurated, Integer size, Integer indexFrom, List<String> listAttr, final ApiCallback<DataPolicyResultListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyPreviousComparisonValidateBeforeCall(mainDpnId, dpnId, dpId, returnEntities, returnCollection, showCurated, size, indexFrom, listAttr, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyResultListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicyStatus
     * @param uniqueId - data policy unique id (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyStatusCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/status";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (uniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueId", uniqueId));

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
    private com.squareup.okhttp.Call getDataPolicyStatusValidateBeforeCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDataPolicyStatusCall(uniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy status
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uniqueId - data policy unique id (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDataPolicyStatus(String uniqueId) throws ApiException {
        ApiResponse<Response> resp = getDataPolicyStatusWithHttpInfo(uniqueId);
        return resp.getData();
    }

    /**
     * Get data policy status
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uniqueId - data policy unique id (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDataPolicyStatusWithHttpInfo(String uniqueId) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyStatusValidateBeforeCall(uniqueId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy status (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uniqueId - data policy unique id (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyStatusAsync(String uniqueId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyStatusValidateBeforeCall(uniqueId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicyVioAttrValues
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyVioAttrValuesCall(Long violationId, Integer size, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/vioAttrValues";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (violationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("violationId", violationId));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getDataPolicyVioAttrValuesValidateBeforeCall(Long violationId, Integer size, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDataPolicyVioAttrValuesCall(violationId, size, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy violation attribute values
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDataPolicyVioAttrValues(Long violationId, Integer size, Integer indexFrom) throws ApiException {
        ApiResponse<Response> resp = getDataPolicyVioAttrValuesWithHttpInfo(violationId, size, indexFrom);
        return resp.getData();
    }

    /**
     * Get data policy violation attribute values
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDataPolicyVioAttrValuesWithHttpInfo(Long violationId, Integer size, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyVioAttrValuesValidateBeforeCall(violationId, size, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy violation attribute values (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyVioAttrValuesAsync(Long violationId, Integer size, Integer indexFrom, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyVioAttrValuesValidateBeforeCall(violationId, size, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicyVioPermAssgEntity
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyVioPermAssgEntityCall(Long violationId, Integer size, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/vioPermAssgEntity";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (violationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("violationId", violationId));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getDataPolicyVioPermAssgEntityValidateBeforeCall(Long violationId, Integer size, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDataPolicyVioPermAssgEntityCall(violationId, size, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy violation of permission assignment entity
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDataPolicyVioPermAssgEntity(Long violationId, Integer size, Integer indexFrom) throws ApiException {
        ApiResponse<Response> resp = getDataPolicyVioPermAssgEntityWithHttpInfo(violationId, size, indexFrom);
        return resp.getData();
    }

    /**
     * Get data policy violation of permission assignment entity
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDataPolicyVioPermAssgEntityWithHttpInfo(Long violationId, Integer size, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyVioPermAssgEntityValidateBeforeCall(violationId, size, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy violation of permission assignment entity (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param violationId - Data policy violation id (optional)
     * @param size - Number of results to return, used for paging., default value is 0 to return all records (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyVioPermAssgEntityAsync(Long violationId, Integer size, Integer indexFrom, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyVioPermAssgEntityValidateBeforeCall(violationId, size, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataPolicyViolations
     * @param dpId - Data policy long id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param showResolved - true/false flag to show resolved violations only (optional, default to false)
     * @param startTime - start time of detected violation date (optional)
     * @param endTime - end time for of detected violation date (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyViolationsCall(Long dpId, Integer size, Integer indexFrom, String q, String qMatch, Boolean showResolved, Long startTime, Long endTime, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/violations";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (dpId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dpId", dpId));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (showResolved != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showResolved", showResolved));
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
    private com.squareup.okhttp.Call getDataPolicyViolationsValidateBeforeCall(Long dpId, Integer size, Integer indexFrom, String q, String qMatch, Boolean showResolved, Long startTime, Long endTime, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDataPolicyViolationsCall(dpId, size, indexFrom, q, qMatch, showResolved, startTime, endTime, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy violations
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dpId - Data policy long id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param showResolved - true/false flag to show resolved violations only (optional, default to false)
     * @param startTime - start time of detected violation date (optional)
     * @param endTime - end time for of detected violation date (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDataPolicyViolations(Long dpId, Integer size, Integer indexFrom, String q, String qMatch, Boolean showResolved, Long startTime, Long endTime) throws ApiException {
        ApiResponse<Response> resp = getDataPolicyViolationsWithHttpInfo(dpId, size, indexFrom, q, qMatch, showResolved, startTime, endTime);
        return resp.getData();
    }

    /**
     * Get data policy violations
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dpId - Data policy long id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param showResolved - true/false flag to show resolved violations only (optional, default to false)
     * @param startTime - start time of detected violation date (optional)
     * @param endTime - end time for of detected violation date (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDataPolicyViolationsWithHttpInfo(Long dpId, Integer size, Integer indexFrom, String q, String qMatch, Boolean showResolved, Long startTime, Long endTime) throws ApiException {
        com.squareup.okhttp.Call call = getDataPolicyViolationsValidateBeforeCall(dpId, size, indexFrom, q, qMatch, showResolved, startTime, endTime, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy violations (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dpId - Data policy long id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 15)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param showResolved - true/false flag to show resolved violations only (optional, default to false)
     * @param startTime - start time of detected violation date (optional)
     * @param endTime - end time for of detected violation date (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataPolicyViolationsAsync(Long dpId, Integer size, Integer indexFrom, String q, String qMatch, Boolean showResolved, Long startTime, Long endTime, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataPolicyViolationsValidateBeforeCall(dpId, size, indexFrom, q, qMatch, showResolved, startTime, endTime, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDefaultDataPolicies
     * @param policyType - Filter the policies by dataPolicyType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION* (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDefaultDataPoliciesCall(String policyType, String dataSourceType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/default";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (policyType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("policyType", policyType));
        if (dataSourceType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dataSourceType", dataSourceType));

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
    private com.squareup.okhttp.Call getDefaultDataPoliciesValidateBeforeCall(String policyType, String dataSourceType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDefaultDataPoliciesCall(policyType, dataSourceType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get default data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param policyType - Filter the policies by dataPolicyType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION* (optional)
     * @return DataPolicyListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyListNode getDefaultDataPolicies(String policyType, String dataSourceType) throws ApiException {
        ApiResponse<DataPolicyListNode> resp = getDefaultDataPoliciesWithHttpInfo(policyType, dataSourceType);
        return resp.getData();
    }

    /**
     * Get default data policies
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param policyType - Filter the policies by dataPolicyType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION* (optional)
     * @return ApiResponse&lt;DataPolicyListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyListNode> getDefaultDataPoliciesWithHttpInfo(String policyType, String dataSourceType) throws ApiException {
        com.squareup.okhttp.Call call = getDefaultDataPoliciesValidateBeforeCall(policyType, dataSourceType, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get default data policies (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param policyType - Filter the policies by dataPolicyType. Valid values: COLLECTION OR PUBLICATION (optional)
     * @param dataSourceType - Filter the policies by dataSourceType. Valid values: IDENTITY OR APPLICATION* (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDefaultDataPoliciesAsync(String policyType, String dataSourceType, final ApiCallback<DataPolicyListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDefaultDataPoliciesValidateBeforeCall(policyType, dataSourceType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDetectionHistory
     * @param dataPolicyId - Data policy id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional, default to startTime)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to DESC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDetectionHistoryCall(Long dataPolicyId, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/detectionHistory";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (dataPolicyId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dataPolicyId", dataPolicyId));
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
    private com.squareup.okhttp.Call getDetectionHistoryValidateBeforeCall(Long dataPolicyId, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDetectionHistoryCall(dataPolicyId, size, indexFrom, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy detection history
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dataPolicyId - Data policy id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional, default to startTime)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to DESC)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDetectionHistory(Long dataPolicyId, Integer size, Integer indexFrom, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<Response> resp = getDetectionHistoryWithHttpInfo(dataPolicyId, size, indexFrom, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Get data policy detection history
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dataPolicyId - Data policy id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional, default to startTime)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to DESC)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDetectionHistoryWithHttpInfo(Long dataPolicyId, Integer size, Integer indexFrom, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = getDetectionHistoryValidateBeforeCall(dataPolicyId, size, indexFrom, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy detection history (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dataPolicyId - Data policy id (optional)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param sortBy - The attribute to sort by. (optional, default to startTime)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to DESC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDetectionHistoryAsync(Long dataPolicyId, Integer size, Integer indexFrom, String sortBy, String sortOrder, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDetectionHistoryValidateBeforeCall(dataPolicyId, size, indexFrom, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getVolationDates
     * @param dataPolicyId - Data policy id (optional)
     * @param size - number returned records, all by default (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getVolationDatesCall(Long dataPolicyId, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/violationDates";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (dataPolicyId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dataPolicyId", dataPolicyId));
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
    private com.squareup.okhttp.Call getVolationDatesValidateBeforeCall(Long dataPolicyId, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getVolationDatesCall(dataPolicyId, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data policy violation counts by detected dates
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dataPolicyId - Data policy id (optional)
     * @param size - number returned records, all by default (optional, default to 0)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getVolationDates(Long dataPolicyId, Integer size) throws ApiException {
        ApiResponse<Response> resp = getVolationDatesWithHttpInfo(dataPolicyId, size);
        return resp.getData();
    }

    /**
     * Get data policy violation counts by detected dates
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dataPolicyId - Data policy id (optional)
     * @param size - number returned records, all by default (optional, default to 0)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getVolationDatesWithHttpInfo(Long dataPolicyId, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getVolationDatesValidateBeforeCall(dataPolicyId, size, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data policy violation counts by detected dates (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param dataPolicyId - Data policy id (optional)
     * @param size - number returned records, all by default (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getVolationDatesAsync(Long dataPolicyId, Integer size, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getVolationDatesValidateBeforeCall(dataPolicyId, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZip
     * @param uploadedInputStream  (required)
     * @param refs - true/false flag to import references (optional, default to false)
     * @param reportOnly - true/false flag to only generate a report (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipCall(File uploadedInputStream, Boolean refs, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/import";

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
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uploadedInputStream  (required)
     * @param refs - true/false flag to import references (optional, default to false)
     * @param reportOnly - true/false flag to only generate a report (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip(File uploadedInputStream, Boolean refs, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importZipWithHttpInfo(uploadedInputStream, refs, reportOnly);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uploadedInputStream  (required)
     * @param refs - true/false flag to import references (optional, default to false)
     * @param reportOnly - true/false flag to only generate a report (optional, default to false)
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uploadedInputStream  (required)
     * @param refs - true/false flag to import references (optional, default to false)
     * @param reportOnly - true/false flag to only generate a report (optional, default to false)
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
     * Build call for importZipResolveEntities
     * @param uploadedInputStream  (required)
     * @param build - true/false flag to build entities list (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipResolveEntitiesCall(File uploadedInputStream, Boolean build, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/import/resolve/entities";

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
    private com.squareup.okhttp.Call importZipResolveEntitiesValidateBeforeCall(File uploadedInputStream, Boolean build, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZipResolveEntities(Async)");
        }
        
        com.squareup.okhttp.Call call = importZipResolveEntitiesCall(uploadedInputStream, build, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uploadedInputStream  (required)
     * @param build - true/false flag to build entities list (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZipResolveEntities(File uploadedInputStream, Boolean build) throws ApiException {
        ApiResponse<Response> resp = importZipResolveEntitiesWithHttpInfo(uploadedInputStream, build);
        return resp.getData();
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uploadedInputStream  (required)
     * @param build - true/false flag to build entities list (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZipResolveEntitiesWithHttpInfo(File uploadedInputStream, Boolean build) throws ApiException {
        com.squareup.okhttp.Call call = importZipResolveEntitiesValidateBeforeCall(uploadedInputStream, build, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Resolve Applications in the import document (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uploadedInputStream  (required)
     * @param build - true/false flag to build entities list (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZipResolveEntitiesAsync(File uploadedInputStream, Boolean build, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZipResolveEntitiesValidateBeforeCall(uploadedInputStream, build, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/datapolicies/import/preview";

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
     * Perform an import preview from zip file which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param uploadedInputStream  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip_0(File uploadedInputStream) throws ApiException {
        ApiResponse<Response> resp = importZip_0WithHttpInfo(uploadedInputStream);
        return resp.getData();
    }

    /**
     * Perform an import preview from zip file which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Perform an import preview from zip file which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Build call for resolveDataPolicyViolation
     * @param body - id nodes (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call resolveDataPolicyViolationCall(Ids body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/resolveViolation";

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
    private com.squareup.okhttp.Call resolveDataPolicyViolationValidateBeforeCall(Ids body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling resolveDataPolicyViolation(Async)");
        }
        
        com.squareup.okhttp.Call call = resolveDataPolicyViolationCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Resolve violations
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - id nodes (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response resolveDataPolicyViolation(Ids body) throws ApiException {
        ApiResponse<Response> resp = resolveDataPolicyViolationWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Resolve violations
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - id nodes (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> resolveDataPolicyViolationWithHttpInfo(Ids body) throws ApiException {
        com.squareup.okhttp.Call call = resolveDataPolicyViolationValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Resolve violations (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - id nodes (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call resolveDataPolicyViolationAsync(Ids body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = resolveDataPolicyViolationValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for runDataPolicyCalculation
     * @param id - data policy long id (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call runDataPolicyCalculationCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/calculate";

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
    private com.squareup.okhttp.Call runDataPolicyCalculationValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = runDataPolicyCalculationCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Calculate data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id - data policy long id (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response runDataPolicyCalculation(Long id) throws ApiException {
        ApiResponse<Response> resp = runDataPolicyCalculationWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Calculate data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id - data policy long id (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> runDataPolicyCalculationWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = runDataPolicyCalculationValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Calculate data policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id - data policy long id (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call runDataPolicyCalculationAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = runDataPolicyCalculationValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param id -  list of policy long id (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/download";

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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startDownloadCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the data policy download
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -  list of policy long id (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(List<Long> id) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Start the data policy download
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -  list of policy long id (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(List<Long> id) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the data policy download (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id -  list of policy long id (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(List<Long> id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_0
     * @param body -  The download request node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Call(Download body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/download";

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
    private com.squareup.okhttp.Call startDownload_0ValidateBeforeCall(Download body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_0Call(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the data policy download
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body -  The download request node (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Start the data policy download
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body -  The download request node (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_0WithHttpInfo(Download body) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the data policy download (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body -  The download request node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Async(Download body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/datapolicies/download/{id}/status"
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Accepted Roles: * Customer Administrator * Data Administrator 
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
     * Build call for updateDataPolicy
     * @param body - data policy node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateDataPolicyCall(DataPolicyNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/new";

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
    private com.squareup.okhttp.Call updateDataPolicyValidateBeforeCall(DataPolicyNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateDataPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = updateDataPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create new data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - data policy node (required)
     * @return DataPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyNode updateDataPolicy(DataPolicyNode body) throws ApiException {
        ApiResponse<DataPolicyNode> resp = updateDataPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create new data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - data policy node (required)
     * @return ApiResponse&lt;DataPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyNode> updateDataPolicyWithHttpInfo(DataPolicyNode body) throws ApiException {
        com.squareup.okhttp.Call call = updateDataPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create new data policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - data policy node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateDataPolicyAsync(DataPolicyNode body, final ApiCallback<DataPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateDataPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateDataPolicy_0
     * @param body - data policy node (required)
     * @param id - data policy id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateDataPolicy_0Call(DataPolicyNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/datapolicies/{id}"
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
    private com.squareup.okhttp.Call updateDataPolicy_0ValidateBeforeCall(DataPolicyNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateDataPolicy_0(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateDataPolicy_0(Async)");
        }
        
        com.squareup.okhttp.Call call = updateDataPolicy_0Call(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - data policy node (required)
     * @param id - data policy id (required)
     * @return DataPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public DataPolicyNode updateDataPolicy_0(DataPolicyNode body, Long id) throws ApiException {
        ApiResponse<DataPolicyNode> resp = updateDataPolicy_0WithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update data policy
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - data policy node (required)
     * @param id - data policy id (required)
     * @return ApiResponse&lt;DataPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<DataPolicyNode> updateDataPolicy_0WithHttpInfo(DataPolicyNode body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateDataPolicy_0ValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update data policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - data policy node (required)
     * @param id - data policy id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateDataPolicy_0Async(DataPolicyNode body, Long id, final ApiCallback<DataPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateDataPolicy_0ValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<DataPolicyNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
