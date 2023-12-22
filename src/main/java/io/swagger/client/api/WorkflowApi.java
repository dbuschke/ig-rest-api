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
import io.swagger.client.model.Response;
import io.swagger.client.model.StartWorkflowRespose;
import io.swagger.client.model.Status;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowApi {
    private ApiClient apiClient;

    public WorkflowApi() {
        this(Configuration.getDefaultApiClient());
    }

    public WorkflowApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getWorkflow
     * @param workflowId the workflowId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWorkflowCall(String workflowId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/workflow/{workflowId}"
            .replaceAll("\\{" + "workflowId" + "\\}", apiClient.escapeString(workflowId.toString()));

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
    private com.squareup.okhttp.Call getWorkflowValidateBeforeCall(String workflowId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'workflowId' is set
        if (workflowId == null) {
            throw new ApiException("Missing the required parameter 'workflowId' when calling getWorkflow(Async)");
        }
        
        com.squareup.okhttp.Call call = getWorkflowCall(workflowId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * This is a call to get the workflow metadata.
     * NOTE to testers - Please do not test yet, there is more work to do&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Workflow Administrator 
     * @param workflowId the workflowId (required)
     * @return Approval
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Approval getWorkflow(String workflowId) throws ApiException {
        ApiResponse<Approval> resp = getWorkflowWithHttpInfo(workflowId);
        return resp.getData();
    }

    /**
     * This is a call to get the workflow metadata.
     * NOTE to testers - Please do not test yet, there is more work to do&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Workflow Administrator 
     * @param workflowId the workflowId (required)
     * @return ApiResponse&lt;Approval&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Approval> getWorkflowWithHttpInfo(String workflowId) throws ApiException {
        com.squareup.okhttp.Call call = getWorkflowValidateBeforeCall(workflowId, null, null);
        Type localVarReturnType = new TypeToken<Approval>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * This is a call to get the workflow metadata. (asynchronously)
     * NOTE to testers - Please do not test yet, there is more work to do&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator * Fulfillment Administrator * Global Administrator * Workflow Administrator 
     * @param workflowId the workflowId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWorkflowAsync(String workflowId, final ApiCallback<Approval> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWorkflowValidateBeforeCall(workflowId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Approval>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for saveSamplePrd
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @param prdType workflow sample type (note, don&#x27;t use PRD_APPROVAL) (optional, default to COST_DECISION)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call saveSamplePrdCall(String id, String name, String prdType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/workflow/saveSample";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));
        if (name != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name", name));
        if (prdType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prdType", prdType));

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
    private com.squareup.okhttp.Call saveSamplePrdValidateBeforeCall(String id, String name, String prdType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = saveSamplePrdCall(id, name, prdType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Creates a sample IG approval PRD
     * Accepted Roles: * Workflow Administrator 
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @param prdType workflow sample type (note, don&#x27;t use PRD_APPROVAL) (optional, default to COST_DECISION)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status saveSamplePrd(String id, String name, String prdType) throws ApiException {
        ApiResponse<Status> resp = saveSamplePrdWithHttpInfo(id, name, prdType);
        return resp.getData();
    }

    /**
     * Creates a sample IG approval PRD
     * Accepted Roles: * Workflow Administrator 
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @param prdType workflow sample type (note, don&#x27;t use PRD_APPROVAL) (optional, default to COST_DECISION)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> saveSamplePrdWithHttpInfo(String id, String name, String prdType) throws ApiException {
        com.squareup.okhttp.Call call = saveSamplePrdValidateBeforeCall(id, name, prdType, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a sample IG approval PRD (asynchronously)
     * Accepted Roles: * Workflow Administrator 
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @param prdType workflow sample type (note, don&#x27;t use PRD_APPROVAL) (optional, default to COST_DECISION)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call saveSamplePrdAsync(String id, String name, String prdType, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = saveSamplePrdValidateBeforeCall(id, name, prdType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for simulate
     * @param body the flowdata (required)
     * @param workflowId the workflowId type (optional)
     * @param entityType the entity type (optional)
     * @param entityId the entity Id (optional)
     * @param reason the reason (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call simulateCall(Object body, String workflowId, String entityType, String entityId, String reason, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/workflow/simulate";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (workflowId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("workflowId", workflowId));
        if (entityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityType", entityType));
        if (entityId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityId", entityId));
        if (reason != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reason", reason));

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
    private com.squareup.okhttp.Call simulateValidateBeforeCall(Object body, String workflowId, String entityType, String entityId, String reason, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling simulate(Async)");
        }
        
        com.squareup.okhttp.Call call = simulateCall(body, workflowId, entityType, entityId, reason, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start a workflow simulation
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param body the flowdata (required)
     * @param workflowId the workflowId type (optional)
     * @param entityType the entity type (optional)
     * @param entityId the entity Id (optional)
     * @param reason the reason (optional)
     * @return StartWorkflowRespose
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public StartWorkflowRespose simulate(Object body, String workflowId, String entityType, String entityId, String reason) throws ApiException {
        ApiResponse<StartWorkflowRespose> resp = simulateWithHttpInfo(body, workflowId, entityType, entityId, reason);
        return resp.getData();
    }

    /**
     * Start a workflow simulation
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param body the flowdata (required)
     * @param workflowId the workflowId type (optional)
     * @param entityType the entity type (optional)
     * @param entityId the entity Id (optional)
     * @param reason the reason (optional)
     * @return ApiResponse&lt;StartWorkflowRespose&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<StartWorkflowRespose> simulateWithHttpInfo(Object body, String workflowId, String entityType, String entityId, String reason) throws ApiException {
        com.squareup.okhttp.Call call = simulateValidateBeforeCall(body, workflowId, entityType, entityId, reason, null, null);
        Type localVarReturnType = new TypeToken<StartWorkflowRespose>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start a workflow simulation (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param body the flowdata (required)
     * @param workflowId the workflowId type (optional)
     * @param entityType the entity type (optional)
     * @param entityId the entity Id (optional)
     * @param reason the reason (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call simulateAsync(Object body, String workflowId, String entityType, String entityId, String reason, final ApiCallback<StartWorkflowRespose> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = simulateValidateBeforeCall(body, workflowId, entityType, entityId, reason, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<StartWorkflowRespose>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for simulationStatus
     * @param workflowId the workflowId (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom comments start position, default 0 (optional, default to 0)
     * @param size Size to return in comments result set, default 100 (optional, default to 100)
     * @param sortOrder of comments ASC or DESC, default ASC (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call simulationStatusCall(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/workflow/simulate/status/{workflowId}"
            .replaceAll("\\{" + "workflowId" + "\\}", apiClient.escapeString(workflowId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (includeSystemComments != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("includeSystemComments", includeSystemComments));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call simulationStatusValidateBeforeCall(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'workflowId' is set
        if (workflowId == null) {
            throw new ApiException("Missing the required parameter 'workflowId' when calling simulationStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = simulationStatusCall(workflowId, includeSystemComments, indexFrom, size, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * This is a call to get the workflow simulation process status
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param workflowId the workflowId (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom comments start position, default 0 (optional, default to 0)
     * @param size Size to return in comments result set, default 100 (optional, default to 100)
     * @param sortOrder of comments ASC or DESC, default ASC (optional, default to ASC)
     * @return Approval
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Approval simulationStatus(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder) throws ApiException {
        ApiResponse<Approval> resp = simulationStatusWithHttpInfo(workflowId, includeSystemComments, indexFrom, size, sortOrder);
        return resp.getData();
    }

    /**
     * This is a call to get the workflow simulation process status
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param workflowId the workflowId (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom comments start position, default 0 (optional, default to 0)
     * @param size Size to return in comments result set, default 100 (optional, default to 100)
     * @param sortOrder of comments ASC or DESC, default ASC (optional, default to ASC)
     * @return ApiResponse&lt;Approval&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Approval> simulationStatusWithHttpInfo(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = simulationStatusValidateBeforeCall(workflowId, includeSystemComments, indexFrom, size, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Approval>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * This is a call to get the workflow simulation process status (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param workflowId the workflowId (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom comments start position, default 0 (optional, default to 0)
     * @param size Size to return in comments result set, default 100 (optional, default to 100)
     * @param sortOrder of comments ASC or DESC, default ASC (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call simulationStatusAsync(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder, final ApiCallback<Approval> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = simulationStatusValidateBeforeCall(workflowId, includeSystemComments, indexFrom, size, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Approval>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for sumbitWorkflowApproval
     * @param body submitData (required)
     * @param taskId the taskId (optional)
     * @param action action (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call sumbitWorkflowApprovalCall(String body, String taskId, String action, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/workflow/simulate/task/submit";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (taskId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("taskId", taskId));
        if (action != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("action", action));

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
    private com.squareup.okhttp.Call sumbitWorkflowApprovalValidateBeforeCall(String body, String taskId, String action, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling sumbitWorkflowApproval(Async)");
        }
        
        com.squareup.okhttp.Call call = sumbitWorkflowApprovalCall(body, taskId, action, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Submit task
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param body submitData (required)
     * @param taskId the taskId (optional)
     * @param action action (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response sumbitWorkflowApproval(String body, String taskId, String action) throws ApiException {
        ApiResponse<Response> resp = sumbitWorkflowApprovalWithHttpInfo(body, taskId, action);
        return resp.getData();
    }

    /**
     * Submit task
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param body submitData (required)
     * @param taskId the taskId (optional)
     * @param action action (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> sumbitWorkflowApprovalWithHttpInfo(String body, String taskId, String action) throws ApiException {
        com.squareup.okhttp.Call call = sumbitWorkflowApprovalValidateBeforeCall(body, taskId, action, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Submit task (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param body submitData (required)
     * @param taskId the taskId (optional)
     * @param action action (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call sumbitWorkflowApprovalAsync(String body, String taskId, String action, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = sumbitWorkflowApprovalValidateBeforeCall(body, taskId, action, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for terminateSimulateWorkflow
     * @param workflowProcessId workflowProcessId to terminate (required)
     * @param comment a comment (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call terminateSimulateWorkflowCall(String workflowProcessId, String comment, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/workflow/simulate/terminate/{workflowProcessId}"
            .replaceAll("\\{" + "workflowProcessId" + "\\}", apiClient.escapeString(workflowProcessId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (comment != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("comment", comment));

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
    private com.squareup.okhttp.Call terminateSimulateWorkflowValidateBeforeCall(String workflowProcessId, String comment, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'workflowProcessId' is set
        if (workflowProcessId == null) {
            throw new ApiException("Missing the required parameter 'workflowProcessId' when calling terminateSimulateWorkflow(Async)");
        }
        
        com.squareup.okhttp.Call call = terminateSimulateWorkflowCall(workflowProcessId, comment, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Terminate standalone workflow
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param workflowProcessId workflowProcessId to terminate (required)
     * @param comment a comment (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status terminateSimulateWorkflow(String workflowProcessId, String comment) throws ApiException {
        ApiResponse<Status> resp = terminateSimulateWorkflowWithHttpInfo(workflowProcessId, comment);
        return resp.getData();
    }

    /**
     * Terminate standalone workflow
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param workflowProcessId workflowProcessId to terminate (required)
     * @param comment a comment (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> terminateSimulateWorkflowWithHttpInfo(String workflowProcessId, String comment) throws ApiException {
        com.squareup.okhttp.Call call = terminateSimulateWorkflowValidateBeforeCall(workflowProcessId, comment, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Terminate standalone workflow (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator * Workflow Administrator 
     * @param workflowProcessId workflowProcessId to terminate (required)
     * @param comment a comment (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call terminateSimulateWorkflowAsync(String workflowProcessId, String comment, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = terminateSimulateWorkflowValidateBeforeCall(workflowProcessId, comment, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
