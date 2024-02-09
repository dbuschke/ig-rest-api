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


import io.swagger.client.model.AfTaskNodePayload;
import io.swagger.client.model.AppAnalytics;
import io.swagger.client.model.AppDataSources;
import io.swagger.client.model.ApprovalWorkItems;
import io.swagger.client.model.Assignments;
import io.swagger.client.model.Brauthitems;
import io.swagger.client.model.Ids;
import io.swagger.client.model.Items;
import io.swagger.client.model.Params;
import io.swagger.client.model.PermAnalytics;
import io.swagger.client.model.Permission;
import io.swagger.client.model.Permissions;
import io.swagger.client.model.PrdNameAvailable;
import io.swagger.client.model.ReqItemInfo;
import io.swagger.client.model.Request;
import io.swagger.client.model.RequestItem;
import io.swagger.client.model.RequestStatus;
import io.swagger.client.model.Requests;
import io.swagger.client.model.Response;
import io.swagger.client.model.Role;
import io.swagger.client.model.Roles;
import io.swagger.client.model.SearchCriteria;
import io.swagger.client.model.Sod;
import io.swagger.client.model.Sods;
import io.swagger.client.model.SodvPreApprovalInfo;
import io.swagger.client.model.Sodviolcases2;
import io.swagger.client.model.StandaloneWorkflowApprovals;
import io.swagger.client.model.Status;
import io.swagger.client.model.Users;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestApi {
    private ApiClient apiClient;

    public RequestApi() {
        this(Configuration.getDefaultApiClient());
    }

    public RequestApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for addChangeRequestItemDecisionStatus
     * @param body  (required)
     * @param resourceRequestId The ID of the Resource Request record (required)
     * @param requestItemUniqueId The unique request item ID (required)
     * @param recipientUniqueId The unique recipient ID (required)
     * @param requestType The type of request (required)
     * @param instanceGuid The instance GUID (required)
     * @param requestItemStatus The request item status to set for the item. (optional)
     * @param getAll Flag indicating whether this is a user who can approve everything.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addChangeRequestItemDecisionStatusCall(RequestItem body, Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, String requestItemStatus, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/changeRequestItemDecisionStatus/{resourceRequestId}/{requestItemUniqueId}/{recipientUniqueId}/{requestType}/{instanceGuid}"
            .replaceAll("\\{" + "resourceRequestId" + "\\}", apiClient.escapeString(resourceRequestId.toString()))
            .replaceAll("\\{" + "requestItemUniqueId" + "\\}", apiClient.escapeString(requestItemUniqueId.toString()))
            .replaceAll("\\{" + "recipientUniqueId" + "\\}", apiClient.escapeString(recipientUniqueId.toString()))
            .replaceAll("\\{" + "requestType" + "\\}", apiClient.escapeString(requestType.toString()))
            .replaceAll("\\{" + "instanceGuid" + "\\}", apiClient.escapeString(instanceGuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (requestItemStatus != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requestItemStatus", requestItemStatus));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call addChangeRequestItemDecisionStatusValidateBeforeCall(RequestItem body, Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, String requestItemStatus, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling addChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'resourceRequestId' is set
        if (resourceRequestId == null) {
            throw new ApiException("Missing the required parameter 'resourceRequestId' when calling addChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'requestItemUniqueId' is set
        if (requestItemUniqueId == null) {
            throw new ApiException("Missing the required parameter 'requestItemUniqueId' when calling addChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'recipientUniqueId' is set
        if (recipientUniqueId == null) {
            throw new ApiException("Missing the required parameter 'recipientUniqueId' when calling addChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'requestType' is set
        if (requestType == null) {
            throw new ApiException("Missing the required parameter 'requestType' when calling addChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'instanceGuid' is set
        if (instanceGuid == null) {
            throw new ApiException("Missing the required parameter 'instanceGuid' when calling addChangeRequestItemDecisionStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = addChangeRequestItemDecisionStatusCall(body, resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, requestItemStatus, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Save change Request Item Decision Status
     * Accepted Roles: * All Access 
     * @param body  (required)
     * @param resourceRequestId The ID of the Resource Request record (required)
     * @param requestItemUniqueId The unique request item ID (required)
     * @param recipientUniqueId The unique recipient ID (required)
     * @param requestType The type of request (required)
     * @param instanceGuid The instance GUID (required)
     * @param requestItemStatus The request item status to set for the item. (optional)
     * @param getAll Flag indicating whether this is a user who can approve everything.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status addChangeRequestItemDecisionStatus(RequestItem body, Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, String requestItemStatus, Boolean getAll) throws ApiException {
        ApiResponse<Status> resp = addChangeRequestItemDecisionStatusWithHttpInfo(body, resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, requestItemStatus, getAll);
        return resp.getData();
    }

    /**
     * Save change Request Item Decision Status
     * Accepted Roles: * All Access 
     * @param body  (required)
     * @param resourceRequestId The ID of the Resource Request record (required)
     * @param requestItemUniqueId The unique request item ID (required)
     * @param recipientUniqueId The unique recipient ID (required)
     * @param requestType The type of request (required)
     * @param instanceGuid The instance GUID (required)
     * @param requestItemStatus The request item status to set for the item. (optional)
     * @param getAll Flag indicating whether this is a user who can approve everything.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> addChangeRequestItemDecisionStatusWithHttpInfo(RequestItem body, Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, String requestItemStatus, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = addChangeRequestItemDecisionStatusValidateBeforeCall(body, resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, requestItemStatus, getAll, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Save change Request Item Decision Status (asynchronously)
     * Accepted Roles: * All Access 
     * @param body  (required)
     * @param resourceRequestId The ID of the Resource Request record (required)
     * @param requestItemUniqueId The unique request item ID (required)
     * @param recipientUniqueId The unique recipient ID (required)
     * @param requestType The type of request (required)
     * @param instanceGuid The instance GUID (required)
     * @param requestItemStatus The request item status to set for the item. (optional)
     * @param getAll Flag indicating whether this is a user who can approve everything.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addChangeRequestItemDecisionStatusAsync(RequestItem body, Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, String requestItemStatus, Boolean getAll, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = addChangeRequestItemDecisionStatusValidateBeforeCall(body, resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, requestItemStatus, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for changePsodvApprovalTaskItemDecision
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @param itemType The type of the item.  Will be \&quot;perm\&quot; or \&quot;role\&quot;. (required)
     * @param uniqueItemId The unique request item ID (required)
     * @param decision The decision to set for the item. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call changePsodvApprovalTaskItemDecisionCall(Long approvalTaskId, String itemType, String uniqueItemId, String decision, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/changePsodvApprovalTaskItemDecision/{approvalTaskId}/{itemType}/{uniqueItemId}"
            .replaceAll("\\{" + "approvalTaskId" + "\\}", apiClient.escapeString(approvalTaskId.toString()))
            .replaceAll("\\{" + "itemType" + "\\}", apiClient.escapeString(itemType.toString()))
            .replaceAll("\\{" + "uniqueItemId" + "\\}", apiClient.escapeString(uniqueItemId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (decision != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("decision", decision));

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
    private com.squareup.okhttp.Call changePsodvApprovalTaskItemDecisionValidateBeforeCall(Long approvalTaskId, String itemType, String uniqueItemId, String decision, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'approvalTaskId' is set
        if (approvalTaskId == null) {
            throw new ApiException("Missing the required parameter 'approvalTaskId' when calling changePsodvApprovalTaskItemDecision(Async)");
        }
        // verify the required parameter 'itemType' is set
        if (itemType == null) {
            throw new ApiException("Missing the required parameter 'itemType' when calling changePsodvApprovalTaskItemDecision(Async)");
        }
        // verify the required parameter 'uniqueItemId' is set
        if (uniqueItemId == null) {
            throw new ApiException("Missing the required parameter 'uniqueItemId' when calling changePsodvApprovalTaskItemDecision(Async)");
        }
        
        com.squareup.okhttp.Call call = changePsodvApprovalTaskItemDecisionCall(approvalTaskId, itemType, uniqueItemId, decision, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Save PSODV request item decision.
     * Accepted Roles: * All Access 
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @param itemType The type of the item.  Will be \&quot;perm\&quot; or \&quot;role\&quot;. (required)
     * @param uniqueItemId The unique request item ID (required)
     * @param decision The decision to set for the item. (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status changePsodvApprovalTaskItemDecision(Long approvalTaskId, String itemType, String uniqueItemId, String decision) throws ApiException {
        ApiResponse<Status> resp = changePsodvApprovalTaskItemDecisionWithHttpInfo(approvalTaskId, itemType, uniqueItemId, decision);
        return resp.getData();
    }

    /**
     * Save PSODV request item decision.
     * Accepted Roles: * All Access 
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @param itemType The type of the item.  Will be \&quot;perm\&quot; or \&quot;role\&quot;. (required)
     * @param uniqueItemId The unique request item ID (required)
     * @param decision The decision to set for the item. (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> changePsodvApprovalTaskItemDecisionWithHttpInfo(Long approvalTaskId, String itemType, String uniqueItemId, String decision) throws ApiException {
        com.squareup.okhttp.Call call = changePsodvApprovalTaskItemDecisionValidateBeforeCall(approvalTaskId, itemType, uniqueItemId, decision, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Save PSODV request item decision. (asynchronously)
     * Accepted Roles: * All Access 
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @param itemType The type of the item.  Will be \&quot;perm\&quot; or \&quot;role\&quot;. (required)
     * @param uniqueItemId The unique request item ID (required)
     * @param decision The decision to set for the item. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call changePsodvApprovalTaskItemDecisionAsync(Long approvalTaskId, String itemType, String uniqueItemId, String decision, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = changePsodvApprovalTaskItemDecisionValidateBeforeCall(approvalTaskId, itemType, uniqueItemId, decision, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteShoppingCart
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteShoppingCartCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/cart";

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
    private com.squareup.okhttp.Call deleteShoppingCartValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteShoppingCartCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete user&#x27;s request shopping cart
     * Accepted Roles: * All Access 
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteShoppingCart() throws ApiException {
        ApiResponse<Status> resp = deleteShoppingCartWithHttpInfo();
        return resp.getData();
    }

    /**
     * Delete user&#x27;s request shopping cart
     * Accepted Roles: * All Access 
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteShoppingCartWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = deleteShoppingCartValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete user&#x27;s request shopping cart (asynchronously)
     * Accepted Roles: * All Access 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteShoppingCartAsync(final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteShoppingCartValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for finalizePsodvApprovalTask
     * @param body The SoD pre-approval information that will be used to automatically approve the SoD violation if it actually occurs. (required)
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call finalizePsodvApprovalTaskCall(SodvPreApprovalInfo body, Long approvalTaskId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/finalizePsodvApprovalTask/{approvalTaskId}"
            .replaceAll("\\{" + "approvalTaskId" + "\\}", apiClient.escapeString(approvalTaskId.toString()));

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
    private com.squareup.okhttp.Call finalizePsodvApprovalTaskValidateBeforeCall(SodvPreApprovalInfo body, Long approvalTaskId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling finalizePsodvApprovalTask(Async)");
        }
        // verify the required parameter 'approvalTaskId' is set
        if (approvalTaskId == null) {
            throw new ApiException("Missing the required parameter 'approvalTaskId' when calling finalizePsodvApprovalTask(Async)");
        }
        
        com.squareup.okhttp.Call call = finalizePsodvApprovalTaskCall(body, approvalTaskId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Finalize a PSODV approval task.
     * Accepted Roles: * All Access 
     * @param body The SoD pre-approval information that will be used to automatically approve the SoD violation if it actually occurs. (required)
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status finalizePsodvApprovalTask(SodvPreApprovalInfo body, Long approvalTaskId) throws ApiException {
        ApiResponse<Status> resp = finalizePsodvApprovalTaskWithHttpInfo(body, approvalTaskId);
        return resp.getData();
    }

    /**
     * Finalize a PSODV approval task.
     * Accepted Roles: * All Access 
     * @param body The SoD pre-approval information that will be used to automatically approve the SoD violation if it actually occurs. (required)
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> finalizePsodvApprovalTaskWithHttpInfo(SodvPreApprovalInfo body, Long approvalTaskId) throws ApiException {
        com.squareup.okhttp.Call call = finalizePsodvApprovalTaskValidateBeforeCall(body, approvalTaskId, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Finalize a PSODV approval task. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body The SoD pre-approval information that will be used to automatically approve the SoD violation if it actually occurs. (required)
     * @param approvalTaskId The ID of the PSODV approval task (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call finalizePsodvApprovalTaskAsync(SodvPreApprovalInfo body, Long approvalTaskId, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = finalizePsodvApprovalTaskValidateBeforeCall(body, approvalTaskId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for findInflightApprovals
     * @param indexFrom start index (optional)
     * @param size size (optional)
     * @param onlyOrphanedApprovals if true, only show approvals that are set to be approved by invalid users,         i.e. a deleted user, an empty group, etc (optional)
     * @param q  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call findInflightApprovalsCall(Integer indexFrom, Integer size, Boolean onlyOrphanedApprovals, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/approvals";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (onlyOrphanedApprovals != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("onlyOrphanedApprovals", onlyOrphanedApprovals));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));

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
    private com.squareup.okhttp.Call findInflightApprovalsValidateBeforeCall(Integer indexFrom, Integer size, Boolean onlyOrphanedApprovals, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = findInflightApprovalsCall(indexFrom, size, onlyOrphanedApprovals, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Find in flight approvals
     * Accepted Roles: * All Access 
     * @param indexFrom start index (optional)
     * @param size size (optional)
     * @param onlyOrphanedApprovals if true, only show approvals that are set to be approved by invalid users,         i.e. a deleted user, an empty group, etc (optional)
     * @param q  (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response findInflightApprovals(Integer indexFrom, Integer size, Boolean onlyOrphanedApprovals, String q) throws ApiException {
        ApiResponse<Response> resp = findInflightApprovalsWithHttpInfo(indexFrom, size, onlyOrphanedApprovals, q);
        return resp.getData();
    }

    /**
     * Find in flight approvals
     * Accepted Roles: * All Access 
     * @param indexFrom start index (optional)
     * @param size size (optional)
     * @param onlyOrphanedApprovals if true, only show approvals that are set to be approved by invalid users,         i.e. a deleted user, an empty group, etc (optional)
     * @param q  (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> findInflightApprovalsWithHttpInfo(Integer indexFrom, Integer size, Boolean onlyOrphanedApprovals, String q) throws ApiException {
        com.squareup.okhttp.Call call = findInflightApprovalsValidateBeforeCall(indexFrom, size, onlyOrphanedApprovals, q, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Find in flight approvals (asynchronously)
     * Accepted Roles: * All Access 
     * @param indexFrom start index (optional)
     * @param size size (optional)
     * @param onlyOrphanedApprovals if true, only show approvals that are set to be approved by invalid users,         i.e. a deleted user, an empty group, etc (optional)
     * @param q  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call findInflightApprovalsAsync(Integer indexFrom, Integer size, Boolean onlyOrphanedApprovals, String q, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = findInflightApprovalsValidateBeforeCall(indexFrom, size, onlyOrphanedApprovals, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for forwardMyWorkitem
     * @param resourceRequestId The resource request ID (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param comment Optional comment (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call forwardMyWorkitemCall(Long resourceRequestId, Boolean getAll, String comment, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/tasks/{resourceRequestId}"
            .replaceAll("\\{" + "resourceRequestId" + "\\}", apiClient.escapeString(resourceRequestId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));
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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call forwardMyWorkitemValidateBeforeCall(Long resourceRequestId, Boolean getAll, String comment, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'resourceRequestId' is set
        if (resourceRequestId == null) {
            throw new ApiException("Missing the required parameter 'resourceRequestId' when calling forwardMyWorkitem(Async)");
        }
        
        com.squareup.okhttp.Call call = forwardMyWorkitemCall(resourceRequestId, getAll, comment, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Approve/Reject approval item tasks
     * Accepted Roles: * All Access 
     * @param resourceRequestId The resource request ID (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param comment Optional comment (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response forwardMyWorkitem(Long resourceRequestId, Boolean getAll, String comment) throws ApiException {
        ApiResponse<Response> resp = forwardMyWorkitemWithHttpInfo(resourceRequestId, getAll, comment);
        return resp.getData();
    }

    /**
     * Approve/Reject approval item tasks
     * Accepted Roles: * All Access 
     * @param resourceRequestId The resource request ID (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param comment Optional comment (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> forwardMyWorkitemWithHttpInfo(Long resourceRequestId, Boolean getAll, String comment) throws ApiException {
        com.squareup.okhttp.Call call = forwardMyWorkitemValidateBeforeCall(resourceRequestId, getAll, comment, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Approve/Reject approval item tasks (asynchronously)
     * Accepted Roles: * All Access 
     * @param resourceRequestId The resource request ID (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param comment Optional comment (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call forwardMyWorkitemAsync(Long resourceRequestId, Boolean getAll, String comment, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = forwardMyWorkitemValidateBeforeCall(resourceRequestId, getAll, comment, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApplicationPermissions
     * @param ID Application ID (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of permissions to be returned (optional, default to 10)
     * @param q optional query string, used against permission name, permission description (optional)
     * @param uniqueUserId User we are requesting permissions for.  If this is not provided, then we return the set of permissions for the authenticated user. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApplicationPermissionsCall(Long ID, Integer indexFrom, Integer size, String q, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/apps/{ID}/perms"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (uniqueUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueUserId", uniqueUserId));

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
    private com.squareup.okhttp.Call getApplicationPermissionsValidateBeforeCall(Long ID, Integer indexFrom, Integer size, String q, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getApplicationPermissions(Async)");
        }
        
        com.squareup.okhttp.Call call = getApplicationPermissionsCall(ID, indexFrom, size, q, uniqueUserId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of requestable permissions for an application.
     * Accepted Roles: * All Access 
     * @param ID Application ID (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of permissions to be returned (optional, default to 10)
     * @param q optional query string, used against permission name, permission description (optional)
     * @param uniqueUserId User we are requesting permissions for.  If this is not provided, then we return the set of permissions for the authenticated user. (optional)
     * @return Permissions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permissions getApplicationPermissions(Long ID, Integer indexFrom, Integer size, String q, String uniqueUserId) throws ApiException {
        ApiResponse<Permissions> resp = getApplicationPermissionsWithHttpInfo(ID, indexFrom, size, q, uniqueUserId);
        return resp.getData();
    }

    /**
     * Get the list of requestable permissions for an application.
     * Accepted Roles: * All Access 
     * @param ID Application ID (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of permissions to be returned (optional, default to 10)
     * @param q optional query string, used against permission name, permission description (optional)
     * @param uniqueUserId User we are requesting permissions for.  If this is not provided, then we return the set of permissions for the authenticated user. (optional)
     * @return ApiResponse&lt;Permissions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permissions> getApplicationPermissionsWithHttpInfo(Long ID, Integer indexFrom, Integer size, String q, String uniqueUserId) throws ApiException {
        com.squareup.okhttp.Call call = getApplicationPermissionsValidateBeforeCall(ID, indexFrom, size, q, uniqueUserId, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of requestable permissions for an application. (asynchronously)
     * Accepted Roles: * All Access 
     * @param ID Application ID (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of permissions to be returned (optional, default to 10)
     * @param q optional query string, used against permission name, permission description (optional)
     * @param uniqueUserId User we are requesting permissions for.  If this is not provided, then we return the set of permissions for the authenticated user. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApplicationPermissionsAsync(Long ID, Integer indexFrom, Integer size, String q, String uniqueUserId, final ApiCallback<Permissions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApplicationPermissionsValidateBeforeCall(ID, indexFrom, size, q, uniqueUserId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApprovers
     * @param resourceRequestId ID of the Resource Request record whose approvers we are to return (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of approver (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApproversCall(Long resourceRequestId, Integer indexFrom, Integer size, String sortOrder, Boolean getAll, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/tasks/{resourceRequestId}/approvers"
            .replaceAll("\\{" + "resourceRequestId" + "\\}", apiClient.escapeString(resourceRequestId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));

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
    private com.squareup.okhttp.Call getApproversValidateBeforeCall(Long resourceRequestId, Integer indexFrom, Integer size, String sortOrder, Boolean getAll, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'resourceRequestId' is set
        if (resourceRequestId == null) {
            throw new ApiException("Missing the required parameter 'resourceRequestId' when calling getApprovers(Async)");
        }
        
        com.squareup.okhttp.Call call = getApproversCall(resourceRequestId, indexFrom, size, sortOrder, getAll, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get approvers for an approval task.
     * Accepted Roles: * All Access 
     * @param resourceRequestId ID of the Resource Request record whose approvers we are to return (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of approver (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getApprovers(Long resourceRequestId, Integer indexFrom, Integer size, String sortOrder, Boolean getAll, String q) throws ApiException {
        ApiResponse<Response> resp = getApproversWithHttpInfo(resourceRequestId, indexFrom, size, sortOrder, getAll, q);
        return resp.getData();
    }

    /**
     * Get approvers for an approval task.
     * Accepted Roles: * All Access 
     * @param resourceRequestId ID of the Resource Request record whose approvers we are to return (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of approver (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getApproversWithHttpInfo(Long resourceRequestId, Integer indexFrom, Integer size, String sortOrder, Boolean getAll, String q) throws ApiException {
        com.squareup.okhttp.Call call = getApproversValidateBeforeCall(resourceRequestId, indexFrom, size, sortOrder, getAll, q, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get approvers for an approval task. (asynchronously)
     * Accepted Roles: * All Access 
     * @param resourceRequestId ID of the Resource Request record whose approvers we are to return (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of approver (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApproversAsync(Long resourceRequestId, Integer indexFrom, Integer size, String sortOrder, Boolean getAll, String q, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getApproversValidateBeforeCall(resourceRequestId, indexFrom, size, sortOrder, getAll, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBusinessRoles
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against business role name, business role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of business roles that the authenticated user can request for this user. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolesCall(Integer indexFrom, Integer size, String q, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/businessroles";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (uniqueUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueUserId", uniqueUserId));

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
    private com.squareup.okhttp.Call getBusinessRolesValidateBeforeCall(Integer indexFrom, Integer size, String q, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getBusinessRolesCall(indexFrom, size, q, uniqueUserId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of requestable business roles
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against business role name, business role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of business roles that the authenticated user can request for this user. (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getBusinessRoles(Integer indexFrom, Integer size, String q, String uniqueUserId) throws ApiException {
        ApiResponse<Roles> resp = getBusinessRolesWithHttpInfo(indexFrom, size, q, uniqueUserId);
        return resp.getData();
    }

    /**
     * Get the list of requestable business roles
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against business role name, business role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of business roles that the authenticated user can request for this user. (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getBusinessRolesWithHttpInfo(Integer indexFrom, Integer size, String q, String uniqueUserId) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRolesValidateBeforeCall(indexFrom, size, q, uniqueUserId, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of requestable business roles (asynchronously)
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against business role name, business role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of business roles that the authenticated user can request for this user. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRolesAsync(Integer indexFrom, Integer size, String q, String uniqueUserId, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRolesValidateBeforeCall(indexFrom, size, q, uniqueUserId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChangeRequestItemDecisionStatus
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param requestItemUniqueId The unique request item ID. (required)
     * @param recipientUniqueId The unique recipient ID. (required)
     * @param requestType The type of request. (required)
     * @param instanceGuid The instance GUID. (required)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChangeRequestItemDecisionStatusCall(Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/changeRequestItemDecisionStatus/{resourceRequestId}/{requestItemUniqueId}/{recipientUniqueId}/{requestType}/{instanceGuid}"
            .replaceAll("\\{" + "resourceRequestId" + "\\}", apiClient.escapeString(resourceRequestId.toString()))
            .replaceAll("\\{" + "requestItemUniqueId" + "\\}", apiClient.escapeString(requestItemUniqueId.toString()))
            .replaceAll("\\{" + "recipientUniqueId" + "\\}", apiClient.escapeString(recipientUniqueId.toString()))
            .replaceAll("\\{" + "requestType" + "\\}", apiClient.escapeString(requestType.toString()))
            .replaceAll("\\{" + "instanceGuid" + "\\}", apiClient.escapeString(instanceGuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getChangeRequestItemDecisionStatusValidateBeforeCall(Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'resourceRequestId' is set
        if (resourceRequestId == null) {
            throw new ApiException("Missing the required parameter 'resourceRequestId' when calling getChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'requestItemUniqueId' is set
        if (requestItemUniqueId == null) {
            throw new ApiException("Missing the required parameter 'requestItemUniqueId' when calling getChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'recipientUniqueId' is set
        if (recipientUniqueId == null) {
            throw new ApiException("Missing the required parameter 'recipientUniqueId' when calling getChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'requestType' is set
        if (requestType == null) {
            throw new ApiException("Missing the required parameter 'requestType' when calling getChangeRequestItemDecisionStatus(Async)");
        }
        // verify the required parameter 'instanceGuid' is set
        if (instanceGuid == null) {
            throw new ApiException("Missing the required parameter 'instanceGuid' when calling getChangeRequestItemDecisionStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getChangeRequestItemDecisionStatusCall(resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Change Request Item Decision Status
     * Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param requestItemUniqueId The unique request item ID. (required)
     * @param recipientUniqueId The unique recipient ID. (required)
     * @param requestType The type of request. (required)
     * @param instanceGuid The instance GUID. (required)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status getChangeRequestItemDecisionStatus(Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, Boolean getAll) throws ApiException {
        ApiResponse<Status> resp = getChangeRequestItemDecisionStatusWithHttpInfo(resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, getAll);
        return resp.getData();
    }

    /**
     * Get Change Request Item Decision Status
     * Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param requestItemUniqueId The unique request item ID. (required)
     * @param recipientUniqueId The unique recipient ID. (required)
     * @param requestType The type of request. (required)
     * @param instanceGuid The instance GUID. (required)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> getChangeRequestItemDecisionStatusWithHttpInfo(Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getChangeRequestItemDecisionStatusValidateBeforeCall(resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, getAll, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Change Request Item Decision Status (asynchronously)
     * Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param requestItemUniqueId The unique request item ID. (required)
     * @param recipientUniqueId The unique recipient ID. (required)
     * @param requestType The type of request. (required)
     * @param instanceGuid The instance GUID. (required)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChangeRequestItemDecisionStatusAsync(Long resourceRequestId, String requestItemUniqueId, String recipientUniqueId, String requestType, String instanceGuid, Boolean getAll, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChangeRequestItemDecisionStatusValidateBeforeCall(resourceRequestId, requestItemUniqueId, recipientUniqueId, requestType, instanceGuid, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContributedToSodViolationsByBusinessRole
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param broleId Business Role ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByBusinessRoleCall(String uniqueUserId, Long broleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/broles/{broleId}/violation/cases/contributedto"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "broleId" + "\\}", apiClient.escapeString(broleId.toString()));

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
    private com.squareup.okhttp.Call getContributedToSodViolationsByBusinessRoleValidateBeforeCall(String uniqueUserId, Long broleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getContributedToSodViolationsByBusinessRole(Async)");
        }
        // verify the required parameter 'broleId' is set
        if (broleId == null) {
            throw new ApiException("Missing the required parameter 'broleId' when calling getContributedToSodViolationsByBusinessRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getContributedToSodViolationsByBusinessRoleCall(uniqueUserId, broleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of SoD violations for the user that the specified business role contributes to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param broleId Business Role ID (required)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getContributedToSodViolationsByBusinessRole(String uniqueUserId, Long broleId) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getContributedToSodViolationsByBusinessRoleWithHttpInfo(uniqueUserId, broleId);
        return resp.getData();
    }

    /**
     * Get the list of SoD violations for the user that the specified business role contributes to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param broleId Business Role ID (required)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getContributedToSodViolationsByBusinessRoleWithHttpInfo(String uniqueUserId, Long broleId) throws ApiException {
        com.squareup.okhttp.Call call = getContributedToSodViolationsByBusinessRoleValidateBeforeCall(uniqueUserId, broleId, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of SoD violations for the user that the specified business role contributes to. (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param broleId Business Role ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByBusinessRoleAsync(String uniqueUserId, Long broleId, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContributedToSodViolationsByBusinessRoleValidateBeforeCall(uniqueUserId, broleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContributedToSodViolationsByPerm
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param permId Permission ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByPermCall(String uniqueUserId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/perms/{permId}/violation/cases/contributedto"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "permId" + "\\}", apiClient.escapeString(permId.toString()));

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
    private com.squareup.okhttp.Call getContributedToSodViolationsByPermValidateBeforeCall(String uniqueUserId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getContributedToSodViolationsByPerm(Async)");
        }
        // verify the required parameter 'permId' is set
        if (permId == null) {
            throw new ApiException("Missing the required parameter 'permId' when calling getContributedToSodViolationsByPerm(Async)");
        }
        
        com.squareup.okhttp.Call call = getContributedToSodViolationsByPermCall(uniqueUserId, permId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of SoD violations for the user that the specified permission contributes to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param permId Permission ID (required)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getContributedToSodViolationsByPerm(String uniqueUserId, Long permId) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getContributedToSodViolationsByPermWithHttpInfo(uniqueUserId, permId);
        return resp.getData();
    }

    /**
     * Get the list of SoD violations for the user that the specified permission contributes to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param permId Permission ID (required)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getContributedToSodViolationsByPermWithHttpInfo(String uniqueUserId, Long permId) throws ApiException {
        com.squareup.okhttp.Call call = getContributedToSodViolationsByPermValidateBeforeCall(uniqueUserId, permId, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of SoD violations for the user that the specified permission contributes to. (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param permId Permission ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByPermAsync(String uniqueUserId, Long permId, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContributedToSodViolationsByPermValidateBeforeCall(uniqueUserId, permId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContributedToSodViolationsByRole
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param roleId Role ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByRoleCall(String uniqueUserId, Long roleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/roles/{roleId}/violation/cases/contributedto"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "roleId" + "\\}", apiClient.escapeString(roleId.toString()));

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
    private com.squareup.okhttp.Call getContributedToSodViolationsByRoleValidateBeforeCall(String uniqueUserId, Long roleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getContributedToSodViolationsByRole(Async)");
        }
        // verify the required parameter 'roleId' is set
        if (roleId == null) {
            throw new ApiException("Missing the required parameter 'roleId' when calling getContributedToSodViolationsByRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getContributedToSodViolationsByRoleCall(uniqueUserId, roleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of SoD violations for the user that the specified technical role contributes to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param roleId Role ID (required)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getContributedToSodViolationsByRole(String uniqueUserId, Long roleId) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getContributedToSodViolationsByRoleWithHttpInfo(uniqueUserId, roleId);
        return resp.getData();
    }

    /**
     * Get the list of SoD violations for the user that the specified technical role contributes to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param roleId Role ID (required)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getContributedToSodViolationsByRoleWithHttpInfo(String uniqueUserId, Long roleId) throws ApiException {
        com.squareup.okhttp.Call call = getContributedToSodViolationsByRoleValidateBeforeCall(uniqueUserId, roleId, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of SoD violations for the user that the specified technical role contributes to. (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param roleId Role ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByRoleAsync(String uniqueUserId, Long roleId, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContributedToSodViolationsByRoleValidateBeforeCall(uniqueUserId, roleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDynamicPermissionParamValues
     * @param uniquePermissionId The unique ID of the original dynamic permission. (required)
     * @param uniqueParamPermissionId The unique permission id that is a dynamic permission parameter for the original dynamic permission. (required)
     * @param key The key to get the list of values for (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 10)
     * @param q Filter to be applied to parameter values. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDynamicPermissionParamValuesCall(String uniquePermissionId, String uniqueParamPermissionId, String key, Integer indexFrom, Integer size, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/perm/{uniquePermissionId}/param/{uniqueParamPermissionId}/{key}/boundValues"
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()))
            .replaceAll("\\{" + "uniqueParamPermissionId" + "\\}", apiClient.escapeString(uniqueParamPermissionId.toString()))
            .replaceAll("\\{" + "key" + "\\}", apiClient.escapeString(key.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));

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
    private com.squareup.okhttp.Call getDynamicPermissionParamValuesValidateBeforeCall(String uniquePermissionId, String uniqueParamPermissionId, String key, Integer indexFrom, Integer size, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getDynamicPermissionParamValues(Async)");
        }
        // verify the required parameter 'uniqueParamPermissionId' is set
        if (uniqueParamPermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniqueParamPermissionId' when calling getDynamicPermissionParamValues(Async)");
        }
        // verify the required parameter 'key' is set
        if (key == null) {
            throw new ApiException("Missing the required parameter 'key' when calling getDynamicPermissionParamValues(Async)");
        }
        
        com.squareup.okhttp.Call call = getDynamicPermissionParamValuesCall(uniquePermissionId, uniqueParamPermissionId, key, indexFrom, size, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets the list of values for a particular dynamic permission parameter
     * Accepted Roles: * All Access 
     * @param uniquePermissionId The unique ID of the original dynamic permission. (required)
     * @param uniqueParamPermissionId The unique permission id that is a dynamic permission parameter for the original dynamic permission. (required)
     * @param key The key to get the list of values for (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 10)
     * @param q Filter to be applied to parameter values. (optional)
     * @return Params
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Params getDynamicPermissionParamValues(String uniquePermissionId, String uniqueParamPermissionId, String key, Integer indexFrom, Integer size, String q) throws ApiException {
        ApiResponse<Params> resp = getDynamicPermissionParamValuesWithHttpInfo(uniquePermissionId, uniqueParamPermissionId, key, indexFrom, size, q);
        return resp.getData();
    }

    /**
     * Gets the list of values for a particular dynamic permission parameter
     * Accepted Roles: * All Access 
     * @param uniquePermissionId The unique ID of the original dynamic permission. (required)
     * @param uniqueParamPermissionId The unique permission id that is a dynamic permission parameter for the original dynamic permission. (required)
     * @param key The key to get the list of values for (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 10)
     * @param q Filter to be applied to parameter values. (optional)
     * @return ApiResponse&lt;Params&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Params> getDynamicPermissionParamValuesWithHttpInfo(String uniquePermissionId, String uniqueParamPermissionId, String key, Integer indexFrom, Integer size, String q) throws ApiException {
        com.squareup.okhttp.Call call = getDynamicPermissionParamValuesValidateBeforeCall(uniquePermissionId, uniqueParamPermissionId, key, indexFrom, size, q, null, null);
        Type localVarReturnType = new TypeToken<Params>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets the list of values for a particular dynamic permission parameter (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniquePermissionId The unique ID of the original dynamic permission. (required)
     * @param uniqueParamPermissionId The unique permission id that is a dynamic permission parameter for the original dynamic permission. (required)
     * @param key The key to get the list of values for (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 10)
     * @param q Filter to be applied to parameter values. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDynamicPermissionParamValuesAsync(String uniquePermissionId, String uniqueParamPermissionId, String key, Integer indexFrom, Integer size, String q, final ApiCallback<Params> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDynamicPermissionParamValuesValidateBeforeCall(uniquePermissionId, uniqueParamPermissionId, key, indexFrom, size, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Params>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDynamicPermissionParams
     * @param uniquePermissionId the unique permission id (required)
     * @param skipTest skips the test where we check REST access for the caller - used when the ig simualator makes this call. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDynamicPermissionParamsCall(String uniquePermissionId, Boolean skipTest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/perm/{uniquePermissionId}/dynamicParams"
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (skipTest != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("skipTest", skipTest));

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
    private com.squareup.okhttp.Call getDynamicPermissionParamsValidateBeforeCall(String uniquePermissionId, Boolean skipTest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getDynamicPermissionParams(Async)");
        }
        
        com.squareup.okhttp.Call call = getDynamicPermissionParamsCall(uniquePermissionId, skipTest, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets the list of values for a particular permission
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the unique permission id (required)
     * @param skipTest skips the test where we check REST access for the caller - used when the ig simualator makes this call. (optional, default to false)
     * @return Params
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Params getDynamicPermissionParams(String uniquePermissionId, Boolean skipTest) throws ApiException {
        ApiResponse<Params> resp = getDynamicPermissionParamsWithHttpInfo(uniquePermissionId, skipTest);
        return resp.getData();
    }

    /**
     * Gets the list of values for a particular permission
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the unique permission id (required)
     * @param skipTest skips the test where we check REST access for the caller - used when the ig simualator makes this call. (optional, default to false)
     * @return ApiResponse&lt;Params&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Params> getDynamicPermissionParamsWithHttpInfo(String uniquePermissionId, Boolean skipTest) throws ApiException {
        com.squareup.okhttp.Call call = getDynamicPermissionParamsValidateBeforeCall(uniquePermissionId, skipTest, null, null);
        Type localVarReturnType = new TypeToken<Params>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets the list of values for a particular permission (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the unique permission id (required)
     * @param skipTest skips the test where we check REST access for the caller - used when the ig simualator makes this call. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDynamicPermissionParamsAsync(String uniquePermissionId, Boolean skipTest, final ApiCallback<Params> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDynamicPermissionParamsValidateBeforeCall(uniquePermissionId, skipTest, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Params>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getManagedUsers
     * @param q Quick search filter, used to search across name and job title. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getManagedUsersCall(String q, String qMatch, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/managed/users";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getManagedUsersValidateBeforeCall(String q, String qMatch, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getManagedUsersCall(q, qMatch, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get managed users.
     * Can include self and direct or downline reports.  What is returned is based on  who the access request policies allow the authenticated user to request for:   Returns self if there is an access request policy that allows the user to request resources for all users or self.  Returns direct reports if there is an access request policy that allows the user to request resources for all users,  downline reports, or direct reports.  Returns downline reports if there is an access request policy that allows the user to request resources for all users  or downline reports.   Note that the response only returns four fields for each returned user:   1. id  2. uniqueUserId  3. displayName  4. jobTitle&lt;br/&gt;Accepted Roles: * All Access 
     * @param q Quick search filter, used to search across name and job title. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getManagedUsers(String q, String qMatch, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<Users> resp = getManagedUsersWithHttpInfo(q, qMatch, indexFrom, size);
        return resp.getData();
    }

    /**
     * Get managed users.
     * Can include self and direct or downline reports.  What is returned is based on  who the access request policies allow the authenticated user to request for:   Returns self if there is an access request policy that allows the user to request resources for all users or self.  Returns direct reports if there is an access request policy that allows the user to request resources for all users,  downline reports, or direct reports.  Returns downline reports if there is an access request policy that allows the user to request resources for all users  or downline reports.   Note that the response only returns four fields for each returned user:   1. id  2. uniqueUserId  3. displayName  4. jobTitle&lt;br/&gt;Accepted Roles: * All Access 
     * @param q Quick search filter, used to search across name and job title. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getManagedUsersWithHttpInfo(String q, String qMatch, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getManagedUsersValidateBeforeCall(q, qMatch, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get managed users. (asynchronously)
     * Can include self and direct or downline reports.  What is returned is based on  who the access request policies allow the authenticated user to request for:   Returns self if there is an access request policy that allows the user to request resources for all users or self.  Returns direct reports if there is an access request policy that allows the user to request resources for all users,  downline reports, or direct reports.  Returns downline reports if there is an access request policy that allows the user to request resources for all users  or downline reports.   Note that the response only returns four fields for each returned user:   1. id  2. uniqueUserId  3. displayName  4. jobTitle&lt;br/&gt;Accepted Roles: * All Access 
     * @param q Quick search filter, used to search across name and job title. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set (optional, default to 10)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getManagedUsersAsync(String q, String qMatch, Integer indexFrom, Integer size, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getManagedUsersValidateBeforeCall(q, qMatch, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMyStandaloneWorkitems
     * @param pageOffset pageOffset starting with 0 (optional, default to 0)
     * @param pageSize  (optional, default to 100)
     * @param sortBy column to sort by (optional, default to createTime)
     * @param sortOrder \&quot;ASC\&quot; or \&quot;DESC\&quot; (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMyStandaloneWorkitemsCall(Integer pageOffset, Integer pageSize, String sortBy, String sortOrder, Boolean getAll, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/workflow/tasks";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (pageOffset != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pageOffset", pageOffset));
        if (pageSize != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pageSize", pageSize));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));

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
    private com.squareup.okhttp.Call getMyStandaloneWorkitemsValidateBeforeCall(Integer pageOffset, Integer pageSize, String sortBy, String sortOrder, Boolean getAll, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getMyStandaloneWorkitemsCall(pageOffset, pageSize, sortBy, sortOrder, getAll, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get approval item tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param pageOffset pageOffset starting with 0 (optional, default to 0)
     * @param pageSize  (optional, default to 100)
     * @param sortBy column to sort by (optional, default to createTime)
     * @param sortOrder \&quot;ASC\&quot; or \&quot;DESC\&quot; (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @return ApprovalWorkItems
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApprovalWorkItems getMyStandaloneWorkitems(Integer pageOffset, Integer pageSize, String sortBy, String sortOrder, Boolean getAll, String q) throws ApiException {
        ApiResponse<ApprovalWorkItems> resp = getMyStandaloneWorkitemsWithHttpInfo(pageOffset, pageSize, sortBy, sortOrder, getAll, q);
        return resp.getData();
    }

    /**
     * Get approval item tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param pageOffset pageOffset starting with 0 (optional, default to 0)
     * @param pageSize  (optional, default to 100)
     * @param sortBy column to sort by (optional, default to createTime)
     * @param sortOrder \&quot;ASC\&quot; or \&quot;DESC\&quot; (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @return ApiResponse&lt;ApprovalWorkItems&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApprovalWorkItems> getMyStandaloneWorkitemsWithHttpInfo(Integer pageOffset, Integer pageSize, String sortBy, String sortOrder, Boolean getAll, String q) throws ApiException {
        com.squareup.okhttp.Call call = getMyStandaloneWorkitemsValidateBeforeCall(pageOffset, pageSize, sortBy, sortOrder, getAll, q, null, null);
        Type localVarReturnType = new TypeToken<ApprovalWorkItems>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get approval item tasks for the authenticated user. (asynchronously)
     * Accepted Roles: * All Access 
     * @param pageOffset pageOffset starting with 0 (optional, default to 0)
     * @param pageSize  (optional, default to 100)
     * @param sortBy column to sort by (optional, default to createTime)
     * @param sortOrder \&quot;ASC\&quot; or \&quot;DESC\&quot; (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMyStandaloneWorkitemsAsync(Integer pageOffset, Integer pageSize, String sortBy, String sortOrder, Boolean getAll, String q, final ApiCallback<ApprovalWorkItems> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMyStandaloneWorkitemsValidateBeforeCall(pageOffset, pageSize, sortBy, sortOrder, getAll, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApprovalWorkItems>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getMyWorkitems
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy columns/attributes to sort on. Valid values: requestedby|reason|requestdate,         if not specified the default is primary key of request resource table (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param countOnly Flag indicating whether we should only return the count (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getMyWorkitemsCall(SearchCriteria body, Integer indexFrom, Integer size, String sortBy, String sortOrder, Boolean getAll, Boolean countOnly, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/tasks";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));
        if (countOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("countOnly", countOnly));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));

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
    private com.squareup.okhttp.Call getMyWorkitemsValidateBeforeCall(SearchCriteria body, Integer indexFrom, Integer size, String sortBy, String sortOrder, Boolean getAll, Boolean countOnly, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getMyWorkitems(Async)");
        }
        
        com.squareup.okhttp.Call call = getMyWorkitemsCall(body, indexFrom, size, sortBy, sortOrder, getAll, countOnly, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get approval item tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy columns/attributes to sort on. Valid values: requestedby|reason|requestdate,         if not specified the default is primary key of request resource table (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param countOnly Flag indicating whether we should only return the count (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getMyWorkitems(SearchCriteria body, Integer indexFrom, Integer size, String sortBy, String sortOrder, Boolean getAll, Boolean countOnly, String q) throws ApiException {
        ApiResponse<Response> resp = getMyWorkitemsWithHttpInfo(body, indexFrom, size, sortBy, sortOrder, getAll, countOnly, q);
        return resp.getData();
    }

    /**
     * Get approval item tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy columns/attributes to sort on. Valid values: requestedby|reason|requestdate,         if not specified the default is primary key of request resource table (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param countOnly Flag indicating whether we should only return the count (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getMyWorkitemsWithHttpInfo(SearchCriteria body, Integer indexFrom, Integer size, String sortBy, String sortOrder, Boolean getAll, Boolean countOnly, String q) throws ApiException {
        com.squareup.okhttp.Call call = getMyWorkitemsValidateBeforeCall(body, indexFrom, size, sortBy, sortOrder, getAll, countOnly, q, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get approval item tasks for the authenticated user. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy columns/attributes to sort on. Valid values: requestedby|reason|requestdate,         if not specified the default is primary key of request resource table (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getAll Flag indicating whether the user can get approval items that are not assigned to them.         NOTE: User must have the ManageRequestApprovals permission with a scope of ALL on the         EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param countOnly Flag indicating whether we should only return the count (optional, default to false)
     * @param q String to search for in name of item, type of item, person requesting item,         intended recipient, category, date requested, approver (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMyWorkitemsAsync(SearchCriteria body, Integer indexFrom, Integer size, String sortBy, String sortOrder, Boolean getAll, Boolean countOnly, String q, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getMyWorkitemsValidateBeforeCall(body, indexFrom, size, sortBy, sortOrder, getAll, countOnly, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPsodvApprovalTask
     * @param id Approval task ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPsodvApprovalTaskCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/psodvapprovaltasks/{id}"
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
    private com.squareup.okhttp.Call getPsodvApprovalTaskValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getPsodvApprovalTask(Async)");
        }
        
        com.squareup.okhttp.Call call = getPsodvApprovalTaskCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get PSODV approval task for the authenticated user.
     * Accepted Roles: * All Access 
     * @param id Approval task ID (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPsodvApprovalTask(Long id) throws ApiException {
        ApiResponse<Response> resp = getPsodvApprovalTaskWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get PSODV approval task for the authenticated user.
     * Accepted Roles: * All Access 
     * @param id Approval task ID (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPsodvApprovalTaskWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getPsodvApprovalTaskValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get PSODV approval task for the authenticated user. (asynchronously)
     * Accepted Roles: * All Access 
     * @param id Approval task ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPsodvApprovalTaskAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPsodvApprovalTaskValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPsodvApprovalTasks
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param getAll Flag indicating whether to return PSODV approvals for ALL users.  NOTE: User must have the         ManagePsodvApprovals permission with a scope of ALL on the EntityType of PSODV_APPROVAL to do this. (optional, default to false)
     * @param countOnly If true, return only the count of tasks, not the tasks themselves. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPsodvApprovalTasksCall(Integer indexFrom, Integer size, Boolean getAll, Boolean countOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/psodvapprovaltasks";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));
        if (countOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("countOnly", countOnly));

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
    private com.squareup.okhttp.Call getPsodvApprovalTasksValidateBeforeCall(Integer indexFrom, Integer size, Boolean getAll, Boolean countOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getPsodvApprovalTasksCall(indexFrom, size, getAll, countOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get PSODV approval tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param getAll Flag indicating whether to return PSODV approvals for ALL users.  NOTE: User must have the         ManagePsodvApprovals permission with a scope of ALL on the EntityType of PSODV_APPROVAL to do this. (optional, default to false)
     * @param countOnly If true, return only the count of tasks, not the tasks themselves. (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPsodvApprovalTasks(Integer indexFrom, Integer size, Boolean getAll, Boolean countOnly) throws ApiException {
        ApiResponse<Response> resp = getPsodvApprovalTasksWithHttpInfo(indexFrom, size, getAll, countOnly);
        return resp.getData();
    }

    /**
     * Get PSODV approval tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param getAll Flag indicating whether to return PSODV approvals for ALL users.  NOTE: User must have the         ManagePsodvApprovals permission with a scope of ALL on the EntityType of PSODV_APPROVAL to do this. (optional, default to false)
     * @param countOnly If true, return only the count of tasks, not the tasks themselves. (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPsodvApprovalTasksWithHttpInfo(Integer indexFrom, Integer size, Boolean getAll, Boolean countOnly) throws ApiException {
        com.squareup.okhttp.Call call = getPsodvApprovalTasksValidateBeforeCall(indexFrom, size, getAll, countOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get PSODV approval tasks for the authenticated user. (asynchronously)
     * Accepted Roles: * All Access 
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param getAll Flag indicating whether to return PSODV approvals for ALL users.  NOTE: User must have the         ManagePsodvApprovals permission with a scope of ALL on the EntityType of PSODV_APPROVAL to do this. (optional, default to false)
     * @param countOnly If true, return only the count of tasks, not the tasks themselves. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPsodvApprovalTasksAsync(Integer indexFrom, Integer size, Boolean getAll, Boolean countOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPsodvApprovalTasksValidateBeforeCall(indexFrom, size, getAll, countOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRequestItemInfo
     * @param id ID of request item to get information on. (required)
     * @param autoChangeRequest Flag indicating whether this is an auto change request ID from a business role (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRequestItemInfoCall(Long id, Boolean autoChangeRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/requestItem/{id}/info"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (autoChangeRequest != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("autoChangeRequest", autoChangeRequest));

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
    private com.squareup.okhttp.Call getRequestItemInfoValidateBeforeCall(Long id, Boolean autoChangeRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getRequestItemInfo(Async)");
        }
        
        com.squareup.okhttp.Call call = getRequestItemInfoCall(id, autoChangeRequest, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get information about a request item.
     * Accepted Roles: * All Access 
     * @param id ID of request item to get information on. (required)
     * @param autoChangeRequest Flag indicating whether this is an auto change request ID from a business role (optional, default to false)
     * @return ReqItemInfo
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ReqItemInfo getRequestItemInfo(Long id, Boolean autoChangeRequest) throws ApiException {
        ApiResponse<ReqItemInfo> resp = getRequestItemInfoWithHttpInfo(id, autoChangeRequest);
        return resp.getData();
    }

    /**
     * Get information about a request item.
     * Accepted Roles: * All Access 
     * @param id ID of request item to get information on. (required)
     * @param autoChangeRequest Flag indicating whether this is an auto change request ID from a business role (optional, default to false)
     * @return ApiResponse&lt;ReqItemInfo&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ReqItemInfo> getRequestItemInfoWithHttpInfo(Long id, Boolean autoChangeRequest) throws ApiException {
        com.squareup.okhttp.Call call = getRequestItemInfoValidateBeforeCall(id, autoChangeRequest, null, null);
        Type localVarReturnType = new TypeToken<ReqItemInfo>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get information about a request item. (asynchronously)
     * Accepted Roles: * All Access 
     * @param id ID of request item to get information on. (required)
     * @param autoChangeRequest Flag indicating whether this is an auto change request ID from a business role (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRequestItemInfoAsync(Long id, Boolean autoChangeRequest, final ApiCallback<ReqItemInfo> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRequestItemInfoValidateBeforeCall(id, autoChangeRequest, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ReqItemInfo>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRequestableApplicationList
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of applications that the authenticated user can request for this user.  If this is not provided, then we return the set of applications the authenticated user can request for self. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRequestableApplicationListCall(Integer indexFrom, Integer size, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/apps";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (uniqueUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueUserId", uniqueUserId));

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
    private com.squareup.okhttp.Call getRequestableApplicationListValidateBeforeCall(Integer indexFrom, Integer size, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getRequestableApplicationListCall(indexFrom, size, uniqueUserId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of requestable applications.
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of applications that the authenticated user can request for this user.  If this is not provided, then we return the set of applications the authenticated user can request for self. (optional)
     * @return AppDataSources
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AppDataSources getRequestableApplicationList(Integer indexFrom, Integer size, String uniqueUserId) throws ApiException {
        ApiResponse<AppDataSources> resp = getRequestableApplicationListWithHttpInfo(indexFrom, size, uniqueUserId);
        return resp.getData();
    }

    /**
     * Return list of requestable applications.
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of applications that the authenticated user can request for this user.  If this is not provided, then we return the set of applications the authenticated user can request for self. (optional)
     * @return ApiResponse&lt;AppDataSources&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AppDataSources> getRequestableApplicationListWithHttpInfo(Integer indexFrom, Integer size, String uniqueUserId) throws ApiException {
        com.squareup.okhttp.Call call = getRequestableApplicationListValidateBeforeCall(indexFrom, size, uniqueUserId, null, null);
        Type localVarReturnType = new TypeToken<AppDataSources>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of requestable applications. (asynchronously)
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of applications that the authenticated user can request for this user.  If this is not provided, then we return the set of applications the authenticated user can request for self. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRequestableApplicationListAsync(Integer indexFrom, Integer size, String uniqueUserId, final ApiCallback<AppDataSources> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRequestableApplicationListValidateBeforeCall(indexFrom, size, uniqueUserId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AppDataSources>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRequestableItemList
     * @param body Advanced search criteria (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of permissions, technical roles, and applications that the authenticated user can request for this user.  If this is not provided, then we return the set of permissions, technical roles, and applications the authenticated user can request for self. (optional)
     * @param sortBy Attribute to sort on. (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param itemType List of item types to restrict search to (optional)
     * @param q String to search for in name of item, description of item, and permission type (optional)
     * @param requiresApproval If non-null, return only items that either require approval (true) or do not require approval (false) (optional)
     * @param requestInProgress If non-null, return only items where there is a request in progress (true) or there is not a request in progress (false) (optional)
     * @param application Return only items belonging to one of these applications (optional)
     * @param category Return only items in one of these categories (optional)
     * @param groupBy group requestable items by application|app or category|cat. Null for normal requestable items. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRequestableItemListCall(SearchCriteria body, Integer indexFrom, Integer size, String uniqueUserId, String sortBy, String sortOrder, List<String> itemType, String q, Boolean requiresApproval, Boolean requestInProgress, List<Long> application, List<Long> category, String groupBy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/requestableitems";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (uniqueUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueUserId", uniqueUserId));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (itemType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "itemType", itemType));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (requiresApproval != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requiresApproval", requiresApproval));
        if (requestInProgress != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requestInProgress", requestInProgress));
        if (application != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "application", application));
        if (category != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "category", category));
        if (groupBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("groupBy", groupBy));

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
    private com.squareup.okhttp.Call getRequestableItemListValidateBeforeCall(SearchCriteria body, Integer indexFrom, Integer size, String uniqueUserId, String sortBy, String sortOrder, List<String> itemType, String q, Boolean requiresApproval, Boolean requestInProgress, List<Long> application, List<Long> category, String groupBy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getRequestableItemList(Async)");
        }
        
        com.squareup.okhttp.Call call = getRequestableItemListCall(body, indexFrom, size, uniqueUserId, sortBy, sortOrder, itemType, q, requiresApproval, requestInProgress, application, category, groupBy, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of requestable items - permissions, technical roles, and applications.
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of permissions, technical roles, and applications that the authenticated user can request for this user.  If this is not provided, then we return the set of permissions, technical roles, and applications the authenticated user can request for self. (optional)
     * @param sortBy Attribute to sort on. (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param itemType List of item types to restrict search to (optional)
     * @param q String to search for in name of item, description of item, and permission type (optional)
     * @param requiresApproval If non-null, return only items that either require approval (true) or do not require approval (false) (optional)
     * @param requestInProgress If non-null, return only items where there is a request in progress (true) or there is not a request in progress (false) (optional)
     * @param application Return only items belonging to one of these applications (optional)
     * @param category Return only items in one of these categories (optional)
     * @param groupBy group requestable items by application|app or category|cat. Null for normal requestable items. (optional)
     * @return Items
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Items getRequestableItemList(SearchCriteria body, Integer indexFrom, Integer size, String uniqueUserId, String sortBy, String sortOrder, List<String> itemType, String q, Boolean requiresApproval, Boolean requestInProgress, List<Long> application, List<Long> category, String groupBy) throws ApiException {
        ApiResponse<Items> resp = getRequestableItemListWithHttpInfo(body, indexFrom, size, uniqueUserId, sortBy, sortOrder, itemType, q, requiresApproval, requestInProgress, application, category, groupBy);
        return resp.getData();
    }

    /**
     * Return list of requestable items - permissions, technical roles, and applications.
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of permissions, technical roles, and applications that the authenticated user can request for this user.  If this is not provided, then we return the set of permissions, technical roles, and applications the authenticated user can request for self. (optional)
     * @param sortBy Attribute to sort on. (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param itemType List of item types to restrict search to (optional)
     * @param q String to search for in name of item, description of item, and permission type (optional)
     * @param requiresApproval If non-null, return only items that either require approval (true) or do not require approval (false) (optional)
     * @param requestInProgress If non-null, return only items where there is a request in progress (true) or there is not a request in progress (false) (optional)
     * @param application Return only items belonging to one of these applications (optional)
     * @param category Return only items in one of these categories (optional)
     * @param groupBy group requestable items by application|app or category|cat. Null for normal requestable items. (optional)
     * @return ApiResponse&lt;Items&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Items> getRequestableItemListWithHttpInfo(SearchCriteria body, Integer indexFrom, Integer size, String uniqueUserId, String sortBy, String sortOrder, List<String> itemType, String q, Boolean requiresApproval, Boolean requestInProgress, List<Long> application, List<Long> category, String groupBy) throws ApiException {
        com.squareup.okhttp.Call call = getRequestableItemListValidateBeforeCall(body, indexFrom, size, uniqueUserId, sortBy, sortOrder, itemType, q, requiresApproval, requestInProgress, application, category, groupBy, null, null);
        Type localVarReturnType = new TypeToken<Items>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of requestable items - permissions, technical roles, and applications. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of applications to be returned (optional, default to 10)
     * @param uniqueUserId Return the set of permissions, technical roles, and applications that the authenticated user can request for this user.  If this is not provided, then we return the set of permissions, technical roles, and applications the authenticated user can request for self. (optional)
     * @param sortBy Attribute to sort on. (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param itemType List of item types to restrict search to (optional)
     * @param q String to search for in name of item, description of item, and permission type (optional)
     * @param requiresApproval If non-null, return only items that either require approval (true) or do not require approval (false) (optional)
     * @param requestInProgress If non-null, return only items where there is a request in progress (true) or there is not a request in progress (false) (optional)
     * @param application Return only items belonging to one of these applications (optional)
     * @param category Return only items in one of these categories (optional)
     * @param groupBy group requestable items by application|app or category|cat. Null for normal requestable items. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRequestableItemListAsync(SearchCriteria body, Integer indexFrom, Integer size, String uniqueUserId, String sortBy, String sortOrder, List<String> itemType, String q, Boolean requiresApproval, Boolean requestInProgress, List<Long> application, List<Long> category, String groupBy, final ApiCallback<Items> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRequestableItemListValidateBeforeCall(body, indexFrom, size, uniqueUserId, sortBy, sortOrder, itemType, q, requiresApproval, requestInProgress, application, category, groupBy, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Items>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSampleTypes
     * @param workflowType  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSampleTypesCall(String workflowType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/workflow/sampleTypes";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (workflowType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("workflowType", workflowType));

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
    private com.squareup.okhttp.Call getSampleTypesValidateBeforeCall(String workflowType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getSampleTypesCall(workflowType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Accepted Roles: * All Access 
     * @param workflowType  (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getSampleTypes(String workflowType) throws ApiException {
        ApiResponse<Response> resp = getSampleTypesWithHttpInfo(workflowType);
        return resp.getData();
    }

    /**
     * 
     * Accepted Roles: * All Access 
     * @param workflowType  (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getSampleTypesWithHttpInfo(String workflowType) throws ApiException {
        com.squareup.okhttp.Call call = getSampleTypesValidateBeforeCall(workflowType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Accepted Roles: * All Access 
     * @param workflowType  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSampleTypesAsync(String workflowType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSampleTypesValidateBeforeCall(workflowType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getShoppingCart
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getShoppingCartCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/cart";

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
    private com.squareup.okhttp.Call getShoppingCartValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getShoppingCartCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get user&#x27;s request shopping cart content
     * Accepted Roles: * All Access 
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getShoppingCart() throws ApiException {
        ApiResponse<Response> resp = getShoppingCartWithHttpInfo();
        return resp.getData();
    }

    /**
     * Get user&#x27;s request shopping cart content
     * Accepted Roles: * All Access 
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getShoppingCartWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getShoppingCartValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get user&#x27;s request shopping cart content (asynchronously)
     * Accepted Roles: * All Access 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getShoppingCartAsync(final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getShoppingCartValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSodPolicy
     * @param ID ID of SoD policy whose information is being requested. (required)
     * @param uniqueUserId Return information about whether this user has permissions and technical roles  in the policy&#x27;s conditions. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSodPolicyCall(Long ID, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/sods/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (uniqueUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueUserId", uniqueUserId));

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
    private com.squareup.okhttp.Call getSodPolicyValidateBeforeCall(Long ID, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSodPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodPolicyCall(ID, uniqueUserId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SoD policy information, including information about whether the specified user has  permissions and technical roles in the policy&#x27;s conditions.
     * Accepted Roles: * All Access 
     * @param ID ID of SoD policy whose information is being requested. (required)
     * @param uniqueUserId Return information about whether this user has permissions and technical roles  in the policy&#x27;s conditions. (optional)
     * @return Sod
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sod getSodPolicy(Long ID, String uniqueUserId) throws ApiException {
        ApiResponse<Sod> resp = getSodPolicyWithHttpInfo(ID, uniqueUserId);
        return resp.getData();
    }

    /**
     * Get the SoD policy information, including information about whether the specified user has  permissions and technical roles in the policy&#x27;s conditions.
     * Accepted Roles: * All Access 
     * @param ID ID of SoD policy whose information is being requested. (required)
     * @param uniqueUserId Return information about whether this user has permissions and technical roles  in the policy&#x27;s conditions. (optional)
     * @return ApiResponse&lt;Sod&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sod> getSodPolicyWithHttpInfo(Long ID, String uniqueUserId) throws ApiException {
        com.squareup.okhttp.Call call = getSodPolicyValidateBeforeCall(ID, uniqueUserId, null, null);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SoD policy information, including information about whether the specified user has  permissions and technical roles in the policy&#x27;s conditions. (asynchronously)
     * Accepted Roles: * All Access 
     * @param ID ID of SoD policy whose information is being requested. (required)
     * @param uniqueUserId Return information about whether this user has permissions and technical roles  in the policy&#x27;s conditions. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSodPolicyAsync(Long ID, String uniqueUserId, final ApiCallback<Sod> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSodPolicyValidateBeforeCall(ID, uniqueUserId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getTechnicalRoles
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against tech role name, tech role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of technical roles that the authenticated user can request for this user. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getTechnicalRolesCall(Integer indexFrom, Integer size, String q, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/technicalroles";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (uniqueUserId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueUserId", uniqueUserId));

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
    private com.squareup.okhttp.Call getTechnicalRolesValidateBeforeCall(Integer indexFrom, Integer size, String q, String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getTechnicalRolesCall(indexFrom, size, q, uniqueUserId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of requestable technical roles
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against tech role name, tech role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of technical roles that the authenticated user can request for this user. (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getTechnicalRoles(Integer indexFrom, Integer size, String q, String uniqueUserId) throws ApiException {
        ApiResponse<Roles> resp = getTechnicalRolesWithHttpInfo(indexFrom, size, q, uniqueUserId);
        return resp.getData();
    }

    /**
     * Get the list of requestable technical roles
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against tech role name, tech role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of technical roles that the authenticated user can request for this user. (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getTechnicalRolesWithHttpInfo(Integer indexFrom, Integer size, String q, String uniqueUserId) throws ApiException {
        com.squareup.okhttp.Call call = getTechnicalRolesValidateBeforeCall(indexFrom, size, q, uniqueUserId, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of requestable technical roles (asynchronously)
     * Accepted Roles: * All Access 
     * @param indexFrom Index from where the result is to be returned (optional, default to 0)
     * @param size Count of technical roles to be returned (optional, default to 10)
     * @param q optional query string, used against tech role name, tech role description  If this is not provided, then we return the set of technical roles the authenticated user can request for self. (optional)
     * @param uniqueUserId Return the set of technical roles that the authenticated user can request for this user. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getTechnicalRolesAsync(Integer indexFrom, Integer size, String q, String uniqueUserId, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getTechnicalRolesValidateBeforeCall(indexFrom, size, q, uniqueUserId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserAppAnalytics
     * @param uniqueUserId Return addition information on application. (required)
     * @param ID the application id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserAppAnalyticsCall(String uniqueUserId, Long ID, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/apps/{ID}/analytics"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserAppAnalyticsValidateBeforeCall(String uniqueUserId, Long ID, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserAppAnalytics(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getUserAppAnalytics(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserAppAnalyticsCall(uniqueUserId, ID, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get application analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return addition information on application. (required)
     * @param ID the application id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return AppAnalytics
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AppAnalytics getUserAppAnalytics(String uniqueUserId, Long ID, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<AppAnalytics> resp = getUserAppAnalyticsWithHttpInfo(uniqueUserId, ID, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * Get application analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return addition information on application. (required)
     * @param ID the application id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;AppAnalytics&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AppAnalytics> getUserAppAnalyticsWithHttpInfo(String uniqueUserId, Long ID, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserAppAnalyticsValidateBeforeCall(uniqueUserId, ID, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<AppAnalytics>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get application analytics for the specified user. (asynchronously)
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return addition information on application. (required)
     * @param ID the application id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserAppAnalyticsAsync(String uniqueUserId, Long ID, Long resourceRequestId, Boolean getAll, final ApiCallback<AppAnalytics> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserAppAnalyticsValidateBeforeCall(uniqueUserId, ID, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AppAnalytics>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserApplicationInfo
     * @param uniqueUserId Return the for this user... (required)
     * @param ID The application ID (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserApplicationInfoCall(String uniqueUserId, Long ID, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/apps/{ID}"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (prototype != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prototype", prototype));

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
    private com.squareup.okhttp.Call getUserApplicationInfoValidateBeforeCall(String uniqueUserId, Long ID, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserApplicationInfo(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getUserApplicationInfo(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserApplicationInfoCall(uniqueUserId, ID, prototype, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get application metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param ID The application ID (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return Permission
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permission getUserApplicationInfo(String uniqueUserId, Long ID, Boolean prototype) throws ApiException {
        ApiResponse<Permission> resp = getUserApplicationInfoWithHttpInfo(uniqueUserId, ID, prototype);
        return resp.getData();
    }

    /**
     * Get application metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param ID The application ID (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return ApiResponse&lt;Permission&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permission> getUserApplicationInfoWithHttpInfo(String uniqueUserId, Long ID, Boolean prototype) throws ApiException {
        com.squareup.okhttp.Call call = getUserApplicationInfoValidateBeforeCall(uniqueUserId, ID, prototype, null, null);
        Type localVarReturnType = new TypeToken<Permission>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get application metadata for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param ID The application ID (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserApplicationInfoAsync(String uniqueUserId, Long ID, Boolean prototype, final ApiCallback<Permission> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserApplicationInfoValidateBeforeCall(uniqueUserId, ID, prototype, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permission>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserAssignedParams
     * @param uniqueUserId Unique user ID of user that has the dynamic permission (required)
     * @param uniquePermissionId Unique permission ID of dynamic permission (required)
     * @param instanceGuid Instance GUID for assigned entitlement (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserAssignedParamsCall(String uniqueUserId, String uniquePermissionId, String instanceGuid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/perms/{uniquePermissionId}/assignedParams"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (instanceGuid != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("instanceGuid", instanceGuid));

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
    private com.squareup.okhttp.Call getUserAssignedParamsValidateBeforeCall(String uniqueUserId, String uniquePermissionId, String instanceGuid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserAssignedParams(Async)");
        }
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getUserAssignedParams(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserAssignedParamsCall(uniqueUserId, uniquePermissionId, instanceGuid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the assigned parameters for the specified user and dynamic permission.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests  on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID of user that has the dynamic permission (required)
     * @param uniquePermissionId Unique permission ID of dynamic permission (required)
     * @param instanceGuid Instance GUID for assigned entitlement (optional)
     * @return Assignments
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Assignments getUserAssignedParams(String uniqueUserId, String uniquePermissionId, String instanceGuid) throws ApiException {
        ApiResponse<Assignments> resp = getUserAssignedParamsWithHttpInfo(uniqueUserId, uniquePermissionId, instanceGuid);
        return resp.getData();
    }

    /**
     * Get the assigned parameters for the specified user and dynamic permission.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests  on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID of user that has the dynamic permission (required)
     * @param uniquePermissionId Unique permission ID of dynamic permission (required)
     * @param instanceGuid Instance GUID for assigned entitlement (optional)
     * @return ApiResponse&lt;Assignments&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Assignments> getUserAssignedParamsWithHttpInfo(String uniqueUserId, String uniquePermissionId, String instanceGuid) throws ApiException {
        com.squareup.okhttp.Call call = getUserAssignedParamsValidateBeforeCall(uniqueUserId, uniquePermissionId, instanceGuid, null, null);
        Type localVarReturnType = new TypeToken<Assignments>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the assigned parameters for the specified user and dynamic permission. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests  on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID of user that has the dynamic permission (required)
     * @param uniquePermissionId Unique permission ID of dynamic permission (required)
     * @param instanceGuid Instance GUID for assigned entitlement (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserAssignedParamsAsync(String uniqueUserId, String uniquePermissionId, String instanceGuid, final ApiCallback<Assignments> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserAssignedParamsValidateBeforeCall(uniqueUserId, uniquePermissionId, instanceGuid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Assignments>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRoleAnalytics
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueId  (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRoleAnalyticsCall(String uniqueUserId, String uniqueId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/broles/{uniqueId}/analytics"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniqueId" + "\\}", apiClient.escapeString(uniqueId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserBusinessRoleAnalyticsValidateBeforeCall(String uniqueUserId, String uniqueId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserBusinessRoleAnalytics(Async)");
        }
        // verify the required parameter 'uniqueId' is set
        if (uniqueId == null) {
            throw new ApiException("Missing the required parameter 'uniqueId' when calling getUserBusinessRoleAnalytics(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRoleAnalyticsCall(uniqueUserId, uniqueId, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get business role analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueId  (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return PermAnalytics
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PermAnalytics getUserBusinessRoleAnalytics(String uniqueUserId, String uniqueId, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<PermAnalytics> resp = getUserBusinessRoleAnalyticsWithHttpInfo(uniqueUserId, uniqueId, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * Get business role analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueId  (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;PermAnalytics&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PermAnalytics> getUserBusinessRoleAnalyticsWithHttpInfo(String uniqueUserId, String uniqueId, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRoleAnalyticsValidateBeforeCall(uniqueUserId, uniqueId, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<PermAnalytics>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get business role analytics for the specified user. (asynchronously)
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueId  (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRoleAnalyticsAsync(String uniqueUserId, String uniqueId, Long resourceRequestId, Boolean getAll, final ApiCallback<PermAnalytics> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserBusinessRoleAnalyticsValidateBeforeCall(uniqueUserId, uniqueId, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PermAnalytics>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRoleInfo
     * @param uniqueUserId Return the for this user... (required)
     * @param uniqueId the business role unique Id (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRoleInfoCall(String uniqueUserId, String uniqueId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/broles/{uniqueId}"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniqueId" + "\\}", apiClient.escapeString(uniqueId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (prototype != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prototype", prototype));

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
    private com.squareup.okhttp.Call getUserBusinessRoleInfoValidateBeforeCall(String uniqueUserId, String uniqueId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserBusinessRoleInfo(Async)");
        }
        // verify the required parameter 'uniqueId' is set
        if (uniqueId == null) {
            throw new ApiException("Missing the required parameter 'uniqueId' when calling getUserBusinessRoleInfo(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRoleInfoCall(uniqueUserId, uniqueId, prototype, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get business role metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniqueId the business role unique Id (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role getUserBusinessRoleInfo(String uniqueUserId, String uniqueId, Boolean prototype) throws ApiException {
        ApiResponse<Role> resp = getUserBusinessRoleInfoWithHttpInfo(uniqueUserId, uniqueId, prototype);
        return resp.getData();
    }

    /**
     * Get business role metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniqueId the business role unique Id (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> getUserBusinessRoleInfoWithHttpInfo(String uniqueUserId, String uniqueId, Boolean prototype) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRoleInfoValidateBeforeCall(uniqueUserId, uniqueId, prototype, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get business role metadata for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniqueId the business role unique Id (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRoleInfoAsync(String uniqueUserId, String uniqueId, Boolean prototype, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserBusinessRoleInfoValidateBeforeCall(uniqueUserId, uniqueId, prototype, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRoleSoDViolations
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique business role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRoleSoDViolationsCall(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/broles/{uniqueRoleId}/sod"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniqueRoleId" + "\\}", apiClient.escapeString(uniqueRoleId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserBusinessRoleSoDViolationsValidateBeforeCall(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserBusinessRoleSoDViolations(Async)");
        }
        // verify the required parameter 'uniqueRoleId' is set
        if (uniqueRoleId == null) {
            throw new ApiException("Missing the required parameter 'uniqueRoleId' when calling getUserBusinessRoleSoDViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRoleSoDViolationsCall(uniqueUserId, uniqueRoleId, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified business role.
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the role on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique business role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getUserBusinessRoleSoDViolations(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getUserBusinessRoleSoDViolationsWithHttpInfo(uniqueUserId, uniqueRoleId, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified business role.
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the role on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique business role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getUserBusinessRoleSoDViolationsWithHttpInfo(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRoleSoDViolationsValidateBeforeCall(uniqueUserId, uniqueRoleId, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified business role. (asynchronously)
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the role on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique business role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRoleSoDViolationsAsync(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserBusinessRoleSoDViolationsValidateBeforeCall(uniqueUserId, uniqueRoleId, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRoles
     * @param uniqueUserId uniqueUserId of the user (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesCall(String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/user/{uniqueUserId}/broles"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

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
    private com.squareup.okhttp.Call getUserBusinessRolesValidateBeforeCall(String uniqueUserId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserBusinessRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRolesCall(uniqueUserId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business roles a user belongs to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId uniqueUserId of the user (required)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getUserBusinessRoles(String uniqueUserId) throws ApiException {
        ApiResponse<Roles> resp = getUserBusinessRolesWithHttpInfo(uniqueUserId);
        return resp.getData();
    }

    /**
     * Get the business roles a user belongs to.
     * Accepted Roles: * All Access 
     * @param uniqueUserId uniqueUserId of the user (required)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getUserBusinessRolesWithHttpInfo(String uniqueUserId) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRolesValidateBeforeCall(uniqueUserId, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business roles a user belongs to. (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniqueUserId uniqueUserId of the user (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesAsync(String uniqueUserId, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserBusinessRolesValidateBeforeCall(uniqueUserId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRolesAndAuthItems
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueBusinessRoleId Unique business role ID. (required)
     * @param q Filter to be applied to item name and description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param onlyMissingItems Only return items the user is missing (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesAndAuthItemsCall(String uniqueUserId, String uniqueBusinessRoleId, String q, Integer indexFrom, Integer size, Boolean onlyMissingItems, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/user/{uniqueUserId}/businessroles/{uniqueBusinessRoleId}/items"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniqueBusinessRoleId" + "\\}", apiClient.escapeString(uniqueBusinessRoleId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (onlyMissingItems != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("onlyMissingItems", onlyMissingItems));

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
    private com.squareup.okhttp.Call getUserBusinessRolesAndAuthItemsValidateBeforeCall(String uniqueUserId, String uniqueBusinessRoleId, String q, Integer indexFrom, Integer size, Boolean onlyMissingItems, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserBusinessRolesAndAuthItems(Async)");
        }
        // verify the required parameter 'uniqueBusinessRoleId' is set
        if (uniqueBusinessRoleId == null) {
            throw new ApiException("Missing the required parameter 'uniqueBusinessRoleId' when calling getUserBusinessRolesAndAuthItems(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRolesAndAuthItemsCall(uniqueUserId, uniqueBusinessRoleId, q, indexFrom, size, onlyMissingItems, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get items in the specified business role that are authorized for the specified user.
     * Only authorized items that  are requestable by the authenticated user for the specified user are returned.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueBusinessRoleId Unique business role ID. (required)
     * @param q Filter to be applied to item name and description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param onlyMissingItems Only return items the user is missing (optional, default to false)
     * @return Brauthitems
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Brauthitems getUserBusinessRolesAndAuthItems(String uniqueUserId, String uniqueBusinessRoleId, String q, Integer indexFrom, Integer size, Boolean onlyMissingItems) throws ApiException {
        ApiResponse<Brauthitems> resp = getUserBusinessRolesAndAuthItemsWithHttpInfo(uniqueUserId, uniqueBusinessRoleId, q, indexFrom, size, onlyMissingItems);
        return resp.getData();
    }

    /**
     * Get items in the specified business role that are authorized for the specified user.
     * Only authorized items that  are requestable by the authenticated user for the specified user are returned.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueBusinessRoleId Unique business role ID. (required)
     * @param q Filter to be applied to item name and description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param onlyMissingItems Only return items the user is missing (optional, default to false)
     * @return ApiResponse&lt;Brauthitems&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Brauthitems> getUserBusinessRolesAndAuthItemsWithHttpInfo(String uniqueUserId, String uniqueBusinessRoleId, String q, Integer indexFrom, Integer size, Boolean onlyMissingItems) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRolesAndAuthItemsValidateBeforeCall(uniqueUserId, uniqueBusinessRoleId, q, indexFrom, size, onlyMissingItems, null, null);
        Type localVarReturnType = new TypeToken<Brauthitems>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get items in the specified business role that are authorized for the specified user. (asynchronously)
     * Only authorized items that  are requestable by the authenticated user for the specified user are returned.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueBusinessRoleId Unique business role ID. (required)
     * @param q Filter to be applied to item name and description (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param onlyMissingItems Only return items the user is missing (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesAndAuthItemsAsync(String uniqueUserId, String uniqueBusinessRoleId, String q, Integer indexFrom, Integer size, Boolean onlyMissingItems, final ApiCallback<Brauthitems> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserBusinessRolesAndAuthItemsValidateBeforeCall(uniqueUserId, uniqueBusinessRoleId, q, indexFrom, size, onlyMissingItems, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Brauthitems>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRolesMetadatas
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesMetadatasCall(Ids body, String uniqueUserId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/broles/metadata"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (prototype != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prototype", prototype));

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
    private com.squareup.okhttp.Call getUserBusinessRolesMetadatasValidateBeforeCall(Ids body, String uniqueUserId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserBusinessRolesMetadatas(Async)");
        }
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserBusinessRolesMetadatas(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRolesMetadatasCall(body, uniqueUserId, prototype, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get metadata for business roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getUserBusinessRolesMetadatas(Ids body, String uniqueUserId, Boolean prototype) throws ApiException {
        ApiResponse<Roles> resp = getUserBusinessRolesMetadatasWithHttpInfo(body, uniqueUserId, prototype);
        return resp.getData();
    }

    /**
     * Get metadata for business roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getUserBusinessRolesMetadatasWithHttpInfo(Ids body, String uniqueUserId, Boolean prototype) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRolesMetadatasValidateBeforeCall(body, uniqueUserId, prototype, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get metadata for business roles for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesMetadatasAsync(Ids body, String uniqueUserId, Boolean prototype, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserBusinessRolesMetadatasValidateBeforeCall(body, uniqueUserId, prototype, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserBusinessRolesOnly
     * @param uniqueUserId Unique user ID we are requesting for. (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesOnlyCall(String uniqueUserId, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/user/{uniqueUserId}/businessroles"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
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
    private com.squareup.okhttp.Call getUserBusinessRolesOnlyValidateBeforeCall(String uniqueUserId, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserBusinessRolesOnly(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserBusinessRolesOnlyCall(uniqueUserId, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business roles have authorized requestable items for the specified user.
     * Each returned business role will  have at least one authorized item that the authenticated user can request for the specified user.  Only the business roles ID, unique ID, name, and description are returned.  Items authorized by the business role are NOT returned.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID we are requesting for. (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getUserBusinessRolesOnly(String uniqueUserId, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<Roles> resp = getUserBusinessRolesOnlyWithHttpInfo(uniqueUserId, indexFrom, size);
        return resp.getData();
    }

    /**
     * Get the business roles have authorized requestable items for the specified user.
     * Each returned business role will  have at least one authorized item that the authenticated user can request for the specified user.  Only the business roles ID, unique ID, name, and description are returned.  Items authorized by the business role are NOT returned.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID we are requesting for. (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getUserBusinessRolesOnlyWithHttpInfo(String uniqueUserId, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getUserBusinessRolesOnlyValidateBeforeCall(uniqueUserId, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business roles have authorized requestable items for the specified user. (asynchronously)
     * Each returned business role will  have at least one authorized item that the authenticated user can request for the specified user.  Only the business roles ID, unique ID, name, and description are returned.  Items authorized by the business role are NOT returned.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID we are requesting for. (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserBusinessRolesOnlyAsync(String uniqueUserId, Integer indexFrom, Integer size, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserBusinessRolesOnlyValidateBeforeCall(uniqueUserId, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserItemsSoDViolations
     * @param body List of user+item+item type objects we are going to check for Sod violations (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserItemsSoDViolationsCall(Ids body, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/users/items/sods";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserItemsSoDViolationsValidateBeforeCall(Ids body, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserItemsSoDViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserItemsSoDViolationsCall(body, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * For each of the specified user+item+item type object, get list of SoDs that would be violated if the  user was granted the specified item.
     * Accepted Roles: * All Access 
     * @param body List of user+item+item type objects we are going to check for Sod violations (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return Sods
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sods getUserItemsSoDViolations(Ids body, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<Sods> resp = getUserItemsSoDViolationsWithHttpInfo(body, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * For each of the specified user+item+item type object, get list of SoDs that would be violated if the  user was granted the specified item.
     * Accepted Roles: * All Access 
     * @param body List of user+item+item type objects we are going to check for Sod violations (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;Sods&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sods> getUserItemsSoDViolationsWithHttpInfo(Ids body, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserItemsSoDViolationsValidateBeforeCall(body, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<Sods>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * For each of the specified user+item+item type object, get list of SoDs that would be violated if the  user was granted the specified item. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body List of user+item+item type objects we are going to check for Sod violations (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserItemsSoDViolationsAsync(Ids body, Long resourceRequestId, Boolean getAll, final ApiCallback<Sods> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserItemsSoDViolationsValidateBeforeCall(body, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sods>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissionAnalytics
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionAnalyticsCall(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/perms/{uniquePermissionId}/analytics"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserPermissionAnalyticsValidateBeforeCall(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserPermissionAnalytics(Async)");
        }
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getUserPermissionAnalytics(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionAnalyticsCall(uniqueUserId, uniquePermissionId, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get permission analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return PermAnalytics
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PermAnalytics getUserPermissionAnalytics(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<PermAnalytics> resp = getUserPermissionAnalyticsWithHttpInfo(uniqueUserId, uniquePermissionId, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * Get permission analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;PermAnalytics&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PermAnalytics> getUserPermissionAnalyticsWithHttpInfo(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionAnalyticsValidateBeforeCall(uniqueUserId, uniquePermissionId, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<PermAnalytics>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get permission analytics for the specified user. (asynchronously)
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionAnalyticsAsync(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll, final ApiCallback<PermAnalytics> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserPermissionAnalyticsValidateBeforeCall(uniqueUserId, uniquePermissionId, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PermAnalytics>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissionInfo
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePermissionId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionInfoCall(String uniqueUserId, String uniquePermissionId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/perms/{uniquePermissionId}"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (prototype != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prototype", prototype));

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
    private com.squareup.okhttp.Call getUserPermissionInfoValidateBeforeCall(String uniqueUserId, String uniquePermissionId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserPermissionInfo(Async)");
        }
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getUserPermissionInfo(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionInfoCall(uniqueUserId, uniquePermissionId, prototype, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get permission metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePermissionId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return Permission
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permission getUserPermissionInfo(String uniqueUserId, String uniquePermissionId, Boolean prototype) throws ApiException {
        ApiResponse<Permission> resp = getUserPermissionInfoWithHttpInfo(uniqueUserId, uniquePermissionId, prototype);
        return resp.getData();
    }

    /**
     * Get permission metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePermissionId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return ApiResponse&lt;Permission&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permission> getUserPermissionInfoWithHttpInfo(String uniqueUserId, String uniquePermissionId, Boolean prototype) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionInfoValidateBeforeCall(uniqueUserId, uniquePermissionId, prototype, null, null);
        Type localVarReturnType = new TypeToken<Permission>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get permission metadata for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePermissionId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionInfoAsync(String uniqueUserId, String uniquePermissionId, Boolean prototype, final ApiCallback<Permission> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserPermissionInfoValidateBeforeCall(uniqueUserId, uniquePermissionId, prototype, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permission>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissionSoDViolations
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionSoDViolationsCall(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/perms/{uniquePermissionId}/sod"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserPermissionSoDViolationsValidateBeforeCall(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserPermissionSoDViolations(Async)");
        }
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getUserPermissionSoDViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionSoDViolationsCall(uniqueUserId, uniquePermissionId, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified permission.
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the permission on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getUserPermissionSoDViolations(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getUserPermissionSoDViolationsWithHttpInfo(uniqueUserId, uniquePermissionId, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified permission.
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the permission on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getUserPermissionSoDViolationsWithHttpInfo(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionSoDViolationsValidateBeforeCall(uniqueUserId, uniquePermissionId, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified permission. (asynchronously)
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the permission on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePermissionId The unique permission id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionSoDViolationsAsync(String uniqueUserId, String uniquePermissionId, Long resourceRequestId, Boolean getAll, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserPermissionSoDViolationsValidateBeforeCall(uniqueUserId, uniquePermissionId, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissions
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getParams Flag indicating whether to get permission assignment parameters (optional, default to false)
     * @param getSods Flag indicating whether to get Sods the permissions contribute to (optional, default to false)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: permname|appname|permdesc, if not specified the default is permname (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionsCall(String uniqueUserId, Boolean getParams, Boolean getSods, String q, String qMatch, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/perms"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (getParams != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getParams", getParams));
        if (getSods != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getSods", getSods));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
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
    private com.squareup.okhttp.Call getUserPermissionsValidateBeforeCall(String uniqueUserId, Boolean getParams, Boolean getSods, String q, String qMatch, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserPermissions(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionsCall(uniqueUserId, getParams, getSods, q, qMatch, sortBy, sortOrder, indexFrom, size, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get permissions for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getParams Flag indicating whether to get permission assignment parameters (optional, default to false)
     * @param getSods Flag indicating whether to get Sods the permissions contribute to (optional, default to false)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: permname|appname|permdesc, if not specified the default is permname (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @return Permissions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permissions getUserPermissions(String uniqueUserId, Boolean getParams, Boolean getSods, String q, String qMatch, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        ApiResponse<Permissions> resp = getUserPermissionsWithHttpInfo(uniqueUserId, getParams, getSods, q, qMatch, sortBy, sortOrder, indexFrom, size, showCt);
        return resp.getData();
    }

    /**
     * Get permissions for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getParams Flag indicating whether to get permission assignment parameters (optional, default to false)
     * @param getSods Flag indicating whether to get Sods the permissions contribute to (optional, default to false)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: permname|appname|permdesc, if not specified the default is permname (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @return ApiResponse&lt;Permissions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permissions> getUserPermissionsWithHttpInfo(String uniqueUserId, Boolean getParams, Boolean getSods, String q, String qMatch, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionsValidateBeforeCall(uniqueUserId, getParams, getSods, q, qMatch, sortBy, sortOrder, indexFrom, size, showCt, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get permissions for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getParams Flag indicating whether to get permission assignment parameters (optional, default to false)
     * @param getSods Flag indicating whether to get Sods the permissions contribute to (optional, default to false)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: permname|appname|permdesc, if not specified the default is permname (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionsAsync(String uniqueUserId, Boolean getParams, Boolean getSods, String q, String qMatch, String sortBy, String sortOrder, Integer indexFrom, Integer size, Boolean showCt, final ApiCallback<Permissions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserPermissionsValidateBeforeCall(uniqueUserId, getParams, getSods, q, qMatch, sortBy, sortOrder, indexFrom, size, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserPermissionsMetadatas
     * @param body list of permissions we are to get metadata for (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionsMetadatasCall(Ids body, String uniqueUserId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/perms/metadata"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (prototype != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prototype", prototype));

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
    private com.squareup.okhttp.Call getUserPermissionsMetadatasValidateBeforeCall(Ids body, String uniqueUserId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserPermissionsMetadatas(Async)");
        }
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserPermissionsMetadatas(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserPermissionsMetadatasCall(body, uniqueUserId, prototype, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get metadata for a set of permissions for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of permissions we are to get metadata for (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return Permissions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permissions getUserPermissionsMetadatas(Ids body, String uniqueUserId, Boolean prototype) throws ApiException {
        ApiResponse<Permissions> resp = getUserPermissionsMetadatasWithHttpInfo(body, uniqueUserId, prototype);
        return resp.getData();
    }

    /**
     * Get metadata for a set of permissions for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of permissions we are to get metadata for (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return ApiResponse&lt;Permissions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permissions> getUserPermissionsMetadatasWithHttpInfo(Ids body, String uniqueUserId, Boolean prototype) throws ApiException {
        com.squareup.okhttp.Call call = getUserPermissionsMetadatasValidateBeforeCall(body, uniqueUserId, prototype, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get metadata for a set of permissions for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of permissions we are to get metadata for (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserPermissionsMetadatasAsync(Ids body, String uniqueUserId, Boolean prototype, final ApiCallback<Permissions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserPermissionsMetadatasValidateBeforeCall(body, uniqueUserId, prototype, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserRequestedBusinessRoles
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserRequestedBusinessRolesCall(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/broles"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (getSods != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getSods", getSods));
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
    private com.squareup.okhttp.Call getUserRequestedBusinessRolesValidateBeforeCall(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserRequestedBusinessRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserRequestedBusinessRolesCall(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Requested Business roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getUserRequestedBusinessRoles(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<Roles> resp = getUserRequestedBusinessRolesWithHttpInfo(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Get Requested Business roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getUserRequestedBusinessRolesWithHttpInfo(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = getUserRequestedBusinessRolesValidateBeforeCall(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Requested Business roles for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserRequestedBusinessRolesAsync(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserRequestedBusinessRolesValidateBeforeCall(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserRequests
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param sortBy Attribute to sort on. Valid values: rendername|appname|permname|recipientname|requestdate, if not specified the default is requestdate (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getInProgress Flag indicating whether to return in progress requests. (optional, default to true)
     * @param getCompleted Flag indicating whether to return completed requests. (optional, default to false)
     * @param getPerms Flag indicating whether to return permission requests. (optional, default to true)
     * @param getApps Flag indicating whether to return application requests. (optional, default to true)
     * @param getAdds Flag indicating whether to return add requests. (optional, default to true)
     * @param getRemoves Flag indicating whether to return remove requests. (optional, default to true)
     * @param getAll Flag indicating whether to return requests for ALL users.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST to do this. (optional, default to false)
     * @param startDate The start date for a date range filter.  Only requests between startDate and endDate will be returned.         If startDate is NOT specified and endDate is specified, then we search for requests whose request date          is less than or equal to endDate.         If startDate is specified and endDate is NOT specified, then we search for requests whose request date         is greater than or equal to startDate.         If both startDate and endDate are specified, then we search for requests whose request date is between them.         NOTE: In both are specified endDate must be greater than or equal to startDate. (optional)
     * @param endDate The end date for a date range filter.  See startDate. (optional)
     * @param autoChangeRequest  (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserRequestsCall(SearchCriteria body, Integer indexFrom, Integer size, String q, String sortBy, String sortOrder, Boolean getInProgress, Boolean getCompleted, Boolean getPerms, Boolean getApps, Boolean getAdds, Boolean getRemoves, Boolean getAll, Long startDate, Long endDate, Boolean autoChangeRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/requests";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (getInProgress != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getInProgress", getInProgress));
        if (getCompleted != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getCompleted", getCompleted));
        if (getPerms != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getPerms", getPerms));
        if (getApps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getApps", getApps));
        if (getAdds != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAdds", getAdds));
        if (getRemoves != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getRemoves", getRemoves));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));
        if (startDate != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startDate", startDate));
        if (endDate != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("endDate", endDate));
        if (autoChangeRequest != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("autoChangeRequest", autoChangeRequest));

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
    private com.squareup.okhttp.Call getUserRequestsValidateBeforeCall(SearchCriteria body, Integer indexFrom, Integer size, String q, String sortBy, String sortOrder, Boolean getInProgress, Boolean getCompleted, Boolean getPerms, Boolean getApps, Boolean getAdds, Boolean getRemoves, Boolean getAll, Long startDate, Long endDate, Boolean autoChangeRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserRequests(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserRequestsCall(body, indexFrom, size, q, sortBy, sortOrder, getInProgress, getCompleted, getPerms, getApps, getAdds, getRemoves, getAll, startDate, endDate, autoChangeRequest, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get requests initiated by the authenticated user.
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param sortBy Attribute to sort on. Valid values: rendername|appname|permname|recipientname|requestdate, if not specified the default is requestdate (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getInProgress Flag indicating whether to return in progress requests. (optional, default to true)
     * @param getCompleted Flag indicating whether to return completed requests. (optional, default to false)
     * @param getPerms Flag indicating whether to return permission requests. (optional, default to true)
     * @param getApps Flag indicating whether to return application requests. (optional, default to true)
     * @param getAdds Flag indicating whether to return add requests. (optional, default to true)
     * @param getRemoves Flag indicating whether to return remove requests. (optional, default to true)
     * @param getAll Flag indicating whether to return requests for ALL users.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST to do this. (optional, default to false)
     * @param startDate The start date for a date range filter.  Only requests between startDate and endDate will be returned.         If startDate is NOT specified and endDate is specified, then we search for requests whose request date          is less than or equal to endDate.         If startDate is specified and endDate is NOT specified, then we search for requests whose request date         is greater than or equal to startDate.         If both startDate and endDate are specified, then we search for requests whose request date is between them.         NOTE: In both are specified endDate must be greater than or equal to startDate. (optional)
     * @param endDate The end date for a date range filter.  See startDate. (optional)
     * @param autoChangeRequest  (optional, default to false)
     * @return Requests
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Requests getUserRequests(SearchCriteria body, Integer indexFrom, Integer size, String q, String sortBy, String sortOrder, Boolean getInProgress, Boolean getCompleted, Boolean getPerms, Boolean getApps, Boolean getAdds, Boolean getRemoves, Boolean getAll, Long startDate, Long endDate, Boolean autoChangeRequest) throws ApiException {
        ApiResponse<Requests> resp = getUserRequestsWithHttpInfo(body, indexFrom, size, q, sortBy, sortOrder, getInProgress, getCompleted, getPerms, getApps, getAdds, getRemoves, getAll, startDate, endDate, autoChangeRequest);
        return resp.getData();
    }

    /**
     * Get requests initiated by the authenticated user.
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param sortBy Attribute to sort on. Valid values: rendername|appname|permname|recipientname|requestdate, if not specified the default is requestdate (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getInProgress Flag indicating whether to return in progress requests. (optional, default to true)
     * @param getCompleted Flag indicating whether to return completed requests. (optional, default to false)
     * @param getPerms Flag indicating whether to return permission requests. (optional, default to true)
     * @param getApps Flag indicating whether to return application requests. (optional, default to true)
     * @param getAdds Flag indicating whether to return add requests. (optional, default to true)
     * @param getRemoves Flag indicating whether to return remove requests. (optional, default to true)
     * @param getAll Flag indicating whether to return requests for ALL users.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST to do this. (optional, default to false)
     * @param startDate The start date for a date range filter.  Only requests between startDate and endDate will be returned.         If startDate is NOT specified and endDate is specified, then we search for requests whose request date          is less than or equal to endDate.         If startDate is specified and endDate is NOT specified, then we search for requests whose request date         is greater than or equal to startDate.         If both startDate and endDate are specified, then we search for requests whose request date is between them.         NOTE: In both are specified endDate must be greater than or equal to startDate. (optional)
     * @param endDate The end date for a date range filter.  See startDate. (optional)
     * @param autoChangeRequest  (optional, default to false)
     * @return ApiResponse&lt;Requests&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Requests> getUserRequestsWithHttpInfo(SearchCriteria body, Integer indexFrom, Integer size, String q, String sortBy, String sortOrder, Boolean getInProgress, Boolean getCompleted, Boolean getPerms, Boolean getApps, Boolean getAdds, Boolean getRemoves, Boolean getAll, Long startDate, Long endDate, Boolean autoChangeRequest) throws ApiException {
        com.squareup.okhttp.Call call = getUserRequestsValidateBeforeCall(body, indexFrom, size, q, sortBy, sortOrder, getInProgress, getCompleted, getPerms, getApps, getAdds, getRemoves, getAll, startDate, endDate, autoChangeRequest, null, null);
        Type localVarReturnType = new TypeToken<Requests>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get requests initiated by the authenticated user. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body Advanced search criteria (required)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param q Filter to be applied to app name, permission name, and recipient name. (optional)
     * @param sortBy Attribute to sort on. Valid values: rendername|appname|permname|recipientname|requestdate, if not specified the default is requestdate (optional)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param getInProgress Flag indicating whether to return in progress requests. (optional, default to true)
     * @param getCompleted Flag indicating whether to return completed requests. (optional, default to false)
     * @param getPerms Flag indicating whether to return permission requests. (optional, default to true)
     * @param getApps Flag indicating whether to return application requests. (optional, default to true)
     * @param getAdds Flag indicating whether to return add requests. (optional, default to true)
     * @param getRemoves Flag indicating whether to return remove requests. (optional, default to true)
     * @param getAll Flag indicating whether to return requests for ALL users.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST to do this. (optional, default to false)
     * @param startDate The start date for a date range filter.  Only requests between startDate and endDate will be returned.         If startDate is NOT specified and endDate is specified, then we search for requests whose request date          is less than or equal to endDate.         If startDate is specified and endDate is NOT specified, then we search for requests whose request date         is greater than or equal to startDate.         If both startDate and endDate are specified, then we search for requests whose request date is between them.         NOTE: In both are specified endDate must be greater than or equal to startDate. (optional)
     * @param endDate The end date for a date range filter.  See startDate. (optional)
     * @param autoChangeRequest  (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserRequestsAsync(SearchCriteria body, Integer indexFrom, Integer size, String q, String sortBy, String sortOrder, Boolean getInProgress, Boolean getCompleted, Boolean getPerms, Boolean getApps, Boolean getAdds, Boolean getRemoves, Boolean getAll, Long startDate, Long endDate, Boolean autoChangeRequest, final ApiCallback<Requests> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserRequestsValidateBeforeCall(body, indexFrom, size, q, sortBy, sortOrder, getInProgress, getCompleted, getPerms, getApps, getAdds, getRemoves, getAll, startDate, endDate, autoChangeRequest, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Requests>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserRoleAnalytics
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePolicyId The unique policy id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserRoleAnalyticsCall(String uniqueUserId, String uniquePolicyId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/roles/{uniquePolicyId}/analytics"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniquePolicyId" + "\\}", apiClient.escapeString(uniquePolicyId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserRoleAnalyticsValidateBeforeCall(String uniqueUserId, String uniquePolicyId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserRoleAnalytics(Async)");
        }
        // verify the required parameter 'uniquePolicyId' is set
        if (uniquePolicyId == null) {
            throw new ApiException("Missing the required parameter 'uniquePolicyId' when calling getUserRoleAnalytics(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserRoleAnalyticsCall(uniqueUserId, uniquePolicyId, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get technical role analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePolicyId The unique policy id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return PermAnalytics
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PermAnalytics getUserRoleAnalytics(String uniqueUserId, String uniquePolicyId, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<PermAnalytics> resp = getUserRoleAnalyticsWithHttpInfo(uniqueUserId, uniquePolicyId, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * Get technical role analytics for the specified user.
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePolicyId The unique policy id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;PermAnalytics&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PermAnalytics> getUserRoleAnalyticsWithHttpInfo(String uniqueUserId, String uniquePolicyId, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserRoleAnalyticsValidateBeforeCall(uniqueUserId, uniquePolicyId, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<PermAnalytics>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get technical role analytics for the specified user. (asynchronously)
     * The caller of this method needs to be the approver for the passed in request ID&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniquePolicyId The unique policy id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserRoleAnalyticsAsync(String uniqueUserId, String uniquePolicyId, Long resourceRequestId, Boolean getAll, final ApiCallback<PermAnalytics> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserRoleAnalyticsValidateBeforeCall(uniqueUserId, uniquePolicyId, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PermAnalytics>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserRoleInfo
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePolicyId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserRoleInfoCall(String uniqueUserId, String uniquePolicyId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/roles/{uniquePolicyId}"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniquePolicyId" + "\\}", apiClient.escapeString(uniquePolicyId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (prototype != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prototype", prototype));

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
    private com.squareup.okhttp.Call getUserRoleInfoValidateBeforeCall(String uniqueUserId, String uniquePolicyId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserRoleInfo(Async)");
        }
        // verify the required parameter 'uniquePolicyId' is set
        if (uniquePolicyId == null) {
            throw new ApiException("Missing the required parameter 'uniquePolicyId' when calling getUserRoleInfo(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserRoleInfoCall(uniqueUserId, uniquePolicyId, prototype, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get technical role metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePolicyId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role getUserRoleInfo(String uniqueUserId, String uniquePolicyId, Boolean prototype) throws ApiException {
        ApiResponse<Role> resp = getUserRoleInfoWithHttpInfo(uniqueUserId, uniquePolicyId, prototype);
        return resp.getData();
    }

    /**
     * Get technical role metadata for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePolicyId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> getUserRoleInfoWithHttpInfo(String uniqueUserId, String uniquePolicyId, Boolean prototype) throws ApiException {
        com.squareup.okhttp.Call call = getUserRoleInfoValidateBeforeCall(uniqueUserId, uniquePolicyId, prototype, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get technical role metadata for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the for this user... (required)
     * @param uniquePolicyId the permission details (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserRoleInfoAsync(String uniqueUserId, String uniquePolicyId, Boolean prototype, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserRoleInfoValidateBeforeCall(uniqueUserId, uniquePolicyId, prototype, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserRoleSoDViolations
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique technical role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserRoleSoDViolationsCall(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/roles/{uniqueRoleId}/sod"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()))
            .replaceAll("\\{" + "uniqueRoleId" + "\\}", apiClient.escapeString(uniqueRoleId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (resourceRequestId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceRequestId", resourceRequestId));
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));

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
    private com.squareup.okhttp.Call getUserRoleSoDViolationsValidateBeforeCall(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserRoleSoDViolations(Async)");
        }
        // verify the required parameter 'uniqueRoleId' is set
        if (uniqueRoleId == null) {
            throw new ApiException("Missing the required parameter 'uniqueRoleId' when calling getUserRoleSoDViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserRoleSoDViolationsCall(uniqueUserId, uniqueRoleId, resourceRequestId, getAll, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified technical role.
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the role on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique technical role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getUserRoleSoDViolations(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getUserRoleSoDViolationsWithHttpInfo(uniqueUserId, uniqueRoleId, resourceRequestId, getAll);
        return resp.getData();
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified technical role.
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the role on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique technical role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getUserRoleSoDViolationsWithHttpInfo(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll) throws ApiException {
        com.squareup.okhttp.Call call = getUserRoleSoDViolationsValidateBeforeCall(uniqueUserId, uniqueRoleId, resourceRequestId, getAll, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get list of SoDs that would be violated if the specified user were granted the specified technical role. (asynchronously)
     * If a WID is specified, the caller of this method needs to be the approver for the passed in WID.  If no WID is specified, the caller must be able to request the role on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Unique user ID. (required)
     * @param uniqueRoleId The unique technical role id (required)
     * @param resourceRequestId the ID of the Resource Request (optional)
     * @param getAll Flag indicating whether to return info for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserRoleSoDViolationsAsync(String uniqueUserId, String uniqueRoleId, Long resourceRequestId, Boolean getAll, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserRoleSoDViolationsValidateBeforeCall(uniqueUserId, uniqueRoleId, resourceRequestId, getAll, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserRolesMetadatas
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserRolesMetadatasCall(Ids body, String uniqueUserId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/roles/metadata"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (prototype != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prototype", prototype));

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
    private com.squareup.okhttp.Call getUserRolesMetadatasValidateBeforeCall(Ids body, String uniqueUserId, Boolean prototype, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getUserRolesMetadatas(Async)");
        }
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserRolesMetadatas(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserRolesMetadatasCall(body, uniqueUserId, prototype, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get metadata for technical roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getUserRolesMetadatas(Ids body, String uniqueUserId, Boolean prototype) throws ApiException {
        ApiResponse<Roles> resp = getUserRolesMetadatasWithHttpInfo(body, uniqueUserId, prototype);
        return resp.getData();
    }

    /**
     * Get metadata for technical roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getUserRolesMetadatasWithHttpInfo(Ids body, String uniqueUserId, Boolean prototype) throws ApiException {
        com.squareup.okhttp.Call call = getUserRolesMetadatasValidateBeforeCall(body, uniqueUserId, prototype, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get metadata for technical roles for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.  The metadata includes requiresApproval, requestInProgress, userHas, isRequestable&lt;br/&gt;Accepted Roles: * All Access 
     * @param body list of technical roles (required)
     * @param uniqueUserId Return the for this user... (required)
     * @param prototype Is this the prototype user?  If so, we get and return a little more information. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserRolesMetadatasAsync(Ids body, String uniqueUserId, Boolean prototype, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserRolesMetadatasValidateBeforeCall(body, uniqueUserId, prototype, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserTechnicalRoles
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserTechnicalRolesCall(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/users/{uniqueUserId}/roles"
            .replaceAll("\\{" + "uniqueUserId" + "\\}", apiClient.escapeString(uniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (getSods != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getSods", getSods));
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
    private com.squareup.okhttp.Call getUserTechnicalRolesValidateBeforeCall(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniqueUserId' is set
        if (uniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'uniqueUserId' when calling getUserTechnicalRoles(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserTechnicalRolesCall(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get technical roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getUserTechnicalRoles(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<Roles> resp = getUserTechnicalRolesWithHttpInfo(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Get technical roles for the specified user.
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getUserTechnicalRolesWithHttpInfo(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = getUserTechnicalRolesValidateBeforeCall(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get technical roles for the specified user. (asynchronously)
     * The passed in user must either be the authenticated user or there must be  an access request policy that allows the authenticated user to make access requests on behalf of the specified user.&lt;br/&gt;Accepted Roles: * All Access 
     * @param uniqueUserId Return the permissions currently held by this user. (required)
     * @param getSods Flag indicating whether to get Sods the roles contribute to (optional, default to false)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param showCt  (optional, default to true)
     * @param q Filter to be applied to role name and description (optional)
     * @param qMatch Match mode. Valid values: ANY, START or EXACT (optional)
     * @param sortBy Attribute to sort on. Valid values: name|description, if not specified the default is name (optional, default to name)
     * @param sortOrder ASC or DESC (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserTechnicalRolesAsync(String uniqueUserId, Boolean getSods, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String sortBy, String sortOrder, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserTechnicalRolesValidateBeforeCall(uniqueUserId, getSods, indexFrom, size, showCt, q, qMatch, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getWorkflowComments
     * @param workflowId the workflow process id (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom Result set start position, default 0 (optional, default to 0)
     * @param size Size to return in result set, default 100 (optional, default to 100)
     * @param sortOrder ASC or DESC, default ASC (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWorkflowCommentsCall(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/workflow/comments/{workflowId}"
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
    private com.squareup.okhttp.Call getWorkflowCommentsValidateBeforeCall(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'workflowId' is set
        if (workflowId == null) {
            throw new ApiException("Missing the required parameter 'workflowId' when calling getWorkflowComments(Async)");
        }
        
        com.squareup.okhttp.Call call = getWorkflowCommentsCall(workflowId, includeSystemComments, indexFrom, size, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * This is a call to get the workflow comments
     * Accepted Roles: * All Access 
     * @param workflowId the workflow process id (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom Result set start position, default 0 (optional, default to 0)
     * @param size Size to return in result set, default 100 (optional, default to 100)
     * @param sortOrder ASC or DESC, default ASC (optional, default to ASC)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getWorkflowComments(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder) throws ApiException {
        ApiResponse<Response> resp = getWorkflowCommentsWithHttpInfo(workflowId, includeSystemComments, indexFrom, size, sortOrder);
        return resp.getData();
    }

    /**
     * This is a call to get the workflow comments
     * Accepted Roles: * All Access 
     * @param workflowId the workflow process id (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom Result set start position, default 0 (optional, default to 0)
     * @param size Size to return in result set, default 100 (optional, default to 100)
     * @param sortOrder ASC or DESC, default ASC (optional, default to ASC)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getWorkflowCommentsWithHttpInfo(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = getWorkflowCommentsValidateBeforeCall(workflowId, includeSystemComments, indexFrom, size, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * This is a call to get the workflow comments (asynchronously)
     * Accepted Roles: * All Access 
     * @param workflowId the workflow process id (required)
     * @param includeSystemComments pass in true if you want to show system comments, default true (optional, default to true)
     * @param indexFrom Result set start position, default 0 (optional, default to 0)
     * @param size Size to return in result set, default 100 (optional, default to 100)
     * @param sortOrder ASC or DESC, default ASC (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWorkflowCommentsAsync(String workflowId, Boolean includeSystemComments, Integer indexFrom, Integer size, String sortOrder, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWorkflowCommentsValidateBeforeCall(workflowId, includeSystemComments, indexFrom, size, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getWorkflowForm
     * @param worktaskId  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWorkflowFormCall(String worktaskId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/standaloneworkflow/form";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (worktaskId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("worktaskId", worktaskId));

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
    private com.squareup.okhttp.Call getWorkflowFormValidateBeforeCall(String worktaskId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getWorkflowFormCall(worktaskId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * This is a call to get the standalone workflow form for a particular afworktask
     * Accepted Roles: * All Access 
     * @param worktaskId  (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getWorkflowForm(String worktaskId) throws ApiException {
        ApiResponse<Response> resp = getWorkflowFormWithHttpInfo(worktaskId);
        return resp.getData();
    }

    /**
     * This is a call to get the standalone workflow form for a particular afworktask
     * Accepted Roles: * All Access 
     * @param worktaskId  (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getWorkflowFormWithHttpInfo(String worktaskId) throws ApiException {
        com.squareup.okhttp.Call call = getWorkflowFormValidateBeforeCall(worktaskId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * This is a call to get the standalone workflow form for a particular afworktask (asynchronously)
     * Accepted Roles: * All Access 
     * @param worktaskId  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWorkflowFormAsync(String worktaskId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWorkflowFormValidateBeforeCall(worktaskId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getWorkflowStatus
     * @param workflowId the workflowId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getWorkflowStatusCall(String workflowId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/workflow/status/{workflowId}"
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
    private com.squareup.okhttp.Call getWorkflowStatusValidateBeforeCall(String workflowId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'workflowId' is set
        if (workflowId == null) {
            throw new ApiException("Missing the required parameter 'workflowId' when calling getWorkflowStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getWorkflowStatusCall(workflowId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * This is a call to get the workflow process status
     * Accepted Roles: * All Access 
     * @param workflowId the workflowId (required)
     * @return StandaloneWorkflowApprovals
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public StandaloneWorkflowApprovals getWorkflowStatus(String workflowId) throws ApiException {
        ApiResponse<StandaloneWorkflowApprovals> resp = getWorkflowStatusWithHttpInfo(workflowId);
        return resp.getData();
    }

    /**
     * This is a call to get the workflow process status
     * Accepted Roles: * All Access 
     * @param workflowId the workflowId (required)
     * @return ApiResponse&lt;StandaloneWorkflowApprovals&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<StandaloneWorkflowApprovals> getWorkflowStatusWithHttpInfo(String workflowId) throws ApiException {
        com.squareup.okhttp.Call call = getWorkflowStatusValidateBeforeCall(workflowId, null, null);
        Type localVarReturnType = new TypeToken<StandaloneWorkflowApprovals>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * This is a call to get the workflow process status (asynchronously)
     * Accepted Roles: * All Access 
     * @param workflowId the workflowId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getWorkflowStatusAsync(String workflowId, final ApiCallback<StandaloneWorkflowApprovals> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getWorkflowStatusValidateBeforeCall(workflowId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<StandaloneWorkflowApprovals>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for isPrdNameAvailable
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call isPrdNameAvailableCall(String id, String name, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/workflow/nameOk";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));
        if (name != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name", name));

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
    private com.squareup.okhttp.Call isPrdNameAvailableValidateBeforeCall(String id, String name, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = isPrdNameAvailableCall(id, name, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns node indicating if proposed formset name is available
     * Accepted Roles: * All Access 
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @return PrdNameAvailable
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PrdNameAvailable isPrdNameAvailable(String id, String name) throws ApiException {
        ApiResponse<PrdNameAvailable> resp = isPrdNameAvailableWithHttpInfo(id, name);
        return resp.getData();
    }

    /**
     * Returns node indicating if proposed formset name is available
     * Accepted Roles: * All Access 
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @return ApiResponse&lt;PrdNameAvailable&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PrdNameAvailable> isPrdNameAvailableWithHttpInfo(String id, String name) throws ApiException {
        com.squareup.okhttp.Call call = isPrdNameAvailableValidateBeforeCall(id, name, null, null);
        Type localVarReturnType = new TypeToken<PrdNameAvailable>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns node indicating if proposed formset name is available (asynchronously)
     * Accepted Roles: * All Access 
     * @param id the proposed workflow id (optional)
     * @param name the proposed workflow name (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call isPrdNameAvailableAsync(String id, String name, final ApiCallback<PrdNameAvailable> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = isPrdNameAvailableValidateBeforeCall(id, name, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PrdNameAvailable>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for reassignApproval
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param newApprover The unique id of the new approver - may be a user or a group unique id (optional)
     * @param oldApprover  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call reassignApprovalCall(Long resourceRequestId, Boolean getAll, String newApprover, String oldApprover, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/approvals/requestid/{resourceRequestId}"
            .replaceAll("\\{" + "resourceRequestId" + "\\}", apiClient.escapeString(resourceRequestId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (getAll != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getAll", getAll));
        if (newApprover != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("newApprover", newApprover));
        if (oldApprover != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("oldApprover", oldApprover));

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
    private com.squareup.okhttp.Call reassignApprovalValidateBeforeCall(Long resourceRequestId, Boolean getAll, String newApprover, String oldApprover, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'resourceRequestId' is set
        if (resourceRequestId == null) {
            throw new ApiException("Missing the required parameter 'resourceRequestId' when calling reassignApproval(Async)");
        }
        
        com.squareup.okhttp.Call call = reassignApprovalCall(resourceRequestId, getAll, newApprover, oldApprover, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * reassign approver
     * Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param newApprover The unique id of the new approver - may be a user or a group unique id (optional)
     * @param oldApprover  (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response reassignApproval(Long resourceRequestId, Boolean getAll, String newApprover, String oldApprover) throws ApiException {
        ApiResponse<Response> resp = reassignApprovalWithHttpInfo(resourceRequestId, getAll, newApprover, oldApprover);
        return resp.getData();
    }

    /**
     * reassign approver
     * Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param newApprover The unique id of the new approver - may be a user or a group unique id (optional)
     * @param oldApprover  (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> reassignApprovalWithHttpInfo(Long resourceRequestId, Boolean getAll, String newApprover, String oldApprover) throws ApiException {
        com.squareup.okhttp.Call call = reassignApprovalValidateBeforeCall(resourceRequestId, getAll, newApprover, oldApprover, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * reassign approver (asynchronously)
     * Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the Resource Request (required)
     * @param getAll Flag indicating whether to this user can approve or reject for ALL approvers.  NOTE: User must have the         ManageRequests permission with a scope of ALL on the EntityType of ACCESS_REQUEST_APPROVAL to do this. (optional, default to false)
     * @param newApprover The unique id of the new approver - may be a user or a group unique id (optional)
     * @param oldApprover  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call reassignApprovalAsync(Long resourceRequestId, Boolean getAll, String newApprover, String oldApprover, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = reassignApprovalValidateBeforeCall(resourceRequestId, getAll, newApprover, oldApprover, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for reassignWorkflowTask
     * @param body the reassign payload (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call reassignWorkflowTaskCall(AfTaskNodePayload body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/standaloneworkflow/reassign";

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
    private com.squareup.okhttp.Call reassignWorkflowTaskValidateBeforeCall(AfTaskNodePayload body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling reassignWorkflowTask(Async)");
        }
        
        com.squareup.okhttp.Call call = reassignWorkflowTaskCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * This is a call to reassign a particular afworktask
     * Accepted Roles: * All Access 
     * @param body the reassign payload (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response reassignWorkflowTask(AfTaskNodePayload body) throws ApiException {
        ApiResponse<Response> resp = reassignWorkflowTaskWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * This is a call to reassign a particular afworktask
     * Accepted Roles: * All Access 
     * @param body the reassign payload (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> reassignWorkflowTaskWithHttpInfo(AfTaskNodePayload body) throws ApiException {
        com.squareup.okhttp.Call call = reassignWorkflowTaskValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * This is a call to reassign a particular afworktask (asynchronously)
     * Accepted Roles: * All Access 
     * @param body the reassign payload (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call reassignWorkflowTaskAsync(AfTaskNodePayload body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = reassignWorkflowTaskValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for requestPermission
     * @param body The request node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call requestPermissionCall(Request body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/request";

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
    private com.squareup.okhttp.Call requestPermissionValidateBeforeCall(Request body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling requestPermission(Async)");
        }
        
        com.squareup.okhttp.Call call = requestPermissionCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Request permission
     * Accepted Roles: * All Access 
     * @param body The request node (required)
     * @return RequestStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public RequestStatus requestPermission(Request body) throws ApiException {
        ApiResponse<RequestStatus> resp = requestPermissionWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Request permission
     * Accepted Roles: * All Access 
     * @param body The request node (required)
     * @return ApiResponse&lt;RequestStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<RequestStatus> requestPermissionWithHttpInfo(Request body) throws ApiException {
        com.squareup.okhttp.Call call = requestPermissionValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<RequestStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Request permission (asynchronously)
     * Accepted Roles: * All Access 
     * @param body The request node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call requestPermissionAsync(Request body, final ApiCallback<RequestStatus> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = requestPermissionValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<RequestStatus>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for restartWorkflow
     * @param resourceRequestId The ID of the record in the resource_request table that is the request item id to retract - may be NO_WORKFLOW_ID if no workflow  was successfully started.  In this case, requestItemId should be supplied by the client. (required)
     * @param requestItemId Request item ID.  This will not necessarily be supplied.  It is an alternative to the workflowRequestId in case  no workflow was successfully started.  In that case, the client should send the requestItemId parameter.  From this, IG will lookup  the associated resource request. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call restartWorkflowCall(Long resourceRequestId, Long requestItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/workflow/{resourceRequestId}/retry"
            .replaceAll("\\{" + "resourceRequestId" + "\\}", apiClient.escapeString(resourceRequestId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (requestItemId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requestItemId", requestItemId));

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
    private com.squareup.okhttp.Call restartWorkflowValidateBeforeCall(Long resourceRequestId, Long requestItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'resourceRequestId' is set
        if (resourceRequestId == null) {
            throw new ApiException("Missing the required parameter 'resourceRequestId' when calling restartWorkflow(Async)");
        }
        
        com.squareup.okhttp.Call call = restartWorkflowCall(resourceRequestId, requestItemId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Restart resource request approval workflow.
     * If the workflow that drives timeouts and notifications for a resource request requiring approvals  is no longer running, the user may call this to restart it. It may also be that the workflow was never  successfully started for some reason.&lt;br/&gt;Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the record in the resource_request table that is the request item id to retract - may be NO_WORKFLOW_ID if no workflow  was successfully started.  In this case, requestItemId should be supplied by the client. (required)
     * @param requestItemId Request item ID.  This will not necessarily be supplied.  It is an alternative to the workflowRequestId in case  no workflow was successfully started.  In that case, the client should send the requestItemId parameter.  From this, IG will lookup  the associated resource request. (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status restartWorkflow(Long resourceRequestId, Long requestItemId) throws ApiException {
        ApiResponse<Status> resp = restartWorkflowWithHttpInfo(resourceRequestId, requestItemId);
        return resp.getData();
    }

    /**
     * Restart resource request approval workflow.
     * If the workflow that drives timeouts and notifications for a resource request requiring approvals  is no longer running, the user may call this to restart it. It may also be that the workflow was never  successfully started for some reason.&lt;br/&gt;Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the record in the resource_request table that is the request item id to retract - may be NO_WORKFLOW_ID if no workflow  was successfully started.  In this case, requestItemId should be supplied by the client. (required)
     * @param requestItemId Request item ID.  This will not necessarily be supplied.  It is an alternative to the workflowRequestId in case  no workflow was successfully started.  In that case, the client should send the requestItemId parameter.  From this, IG will lookup  the associated resource request. (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> restartWorkflowWithHttpInfo(Long resourceRequestId, Long requestItemId) throws ApiException {
        com.squareup.okhttp.Call call = restartWorkflowValidateBeforeCall(resourceRequestId, requestItemId, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Restart resource request approval workflow. (asynchronously)
     * If the workflow that drives timeouts and notifications for a resource request requiring approvals  is no longer running, the user may call this to restart it. It may also be that the workflow was never  successfully started for some reason.&lt;br/&gt;Accepted Roles: * All Access 
     * @param resourceRequestId The ID of the record in the resource_request table that is the request item id to retract - may be NO_WORKFLOW_ID if no workflow  was successfully started.  In this case, requestItemId should be supplied by the client. (required)
     * @param requestItemId Request item ID.  This will not necessarily be supplied.  It is an alternative to the workflowRequestId in case  no workflow was successfully started.  In that case, the client should send the requestItemId parameter.  From this, IG will lookup  the associated resource request. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call restartWorkflowAsync(Long resourceRequestId, Long requestItemId, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = restartWorkflowValidateBeforeCall(resourceRequestId, requestItemId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for retract
     * @param id The resource request item id (primary key) to retract (optional)
     * @param retractPerms Flag indicating whether to also retract permission requests associated  with a technical role request.  This only applies if the request item is for a technical role. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call retractCall(Long id, Boolean retractPerms, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/request/resourceItem/retract";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("id", id));
        if (retractPerms != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("retractPerms", retractPerms));

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
    private com.squareup.okhttp.Call retractValidateBeforeCall(Long id, Boolean retractPerms, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = retractCall(id, retractPerms, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Retract resource request item
     * Accepted Roles: * All Access 
     * @param id The resource request item id (primary key) to retract (optional)
     * @param retractPerms Flag indicating whether to also retract permission requests associated  with a technical role request.  This only applies if the request item is for a technical role. (optional, default to false)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status retract(Long id, Boolean retractPerms) throws ApiException {
        ApiResponse<Status> resp = retractWithHttpInfo(id, retractPerms);
        return resp.getData();
    }

    /**
     * Retract resource request item
     * Accepted Roles: * All Access 
     * @param id The resource request item id (primary key) to retract (optional)
     * @param retractPerms Flag indicating whether to also retract permission requests associated  with a technical role request.  This only applies if the request item is for a technical role. (optional, default to false)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> retractWithHttpInfo(Long id, Boolean retractPerms) throws ApiException {
        com.squareup.okhttp.Call call = retractValidateBeforeCall(id, retractPerms, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retract resource request item (asynchronously)
     * Accepted Roles: * All Access 
     * @param id The resource request item id (primary key) to retract (optional)
     * @param retractPerms Flag indicating whether to also retract permission requests associated  with a technical role request.  This only applies if the request item is for a technical role. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call retractAsync(Long id, Boolean retractPerms, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = retractValidateBeforeCall(id, retractPerms, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for saveShoppingCart
     * @param body The shopping cart contents (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call saveShoppingCartCall(String body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/cart";

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
    private com.squareup.okhttp.Call saveShoppingCartValidateBeforeCall(String body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling saveShoppingCart(Async)");
        }
        
        com.squareup.okhttp.Call call = saveShoppingCartCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Save user&#x27;s request shopping cart content
     * Accepted Roles: * All Access 
     * @param body The shopping cart contents (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status saveShoppingCart(String body) throws ApiException {
        ApiResponse<Status> resp = saveShoppingCartWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Save user&#x27;s request shopping cart content
     * Accepted Roles: * All Access 
     * @param body The shopping cart contents (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> saveShoppingCartWithHttpInfo(String body) throws ApiException {
        com.squareup.okhttp.Call call = saveShoppingCartValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Save user&#x27;s request shopping cart content (asynchronously)
     * Accepted Roles: * All Access 
     * @param body The shopping cart contents (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call saveShoppingCartAsync(String body, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = saveShoppingCartValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for submitStandaloneWorkflowApproval
     * @param body the submit data (required)
     * @param taskId the workflow task id (optional)
     * @param action the workflowAction (optional)
     * @param approver the approver, leave null if you are the approver, otherwise include         but note if you include it and are not a workflow admin, this call will get rejected (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call submitStandaloneWorkflowApprovalCall(String body, String taskId, String action, String approver, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/request/workflow/task/submit";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (taskId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("taskId", taskId));
        if (action != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("action", action));
        if (approver != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approver", approver));

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
    private com.squareup.okhttp.Call submitStandaloneWorkflowApprovalValidateBeforeCall(String body, String taskId, String action, String approver, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling submitStandaloneWorkflowApproval(Async)");
        }
        
        com.squareup.okhttp.Call call = submitStandaloneWorkflowApprovalCall(body, taskId, action, approver, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Submit standalone workflow tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param body the submit data (required)
     * @param taskId the workflow task id (optional)
     * @param action the workflowAction (optional)
     * @param approver the approver, leave null if you are the approver, otherwise include         but note if you include it and are not a workflow admin, this call will get rejected (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response submitStandaloneWorkflowApproval(String body, String taskId, String action, String approver) throws ApiException {
        ApiResponse<Response> resp = submitStandaloneWorkflowApprovalWithHttpInfo(body, taskId, action, approver);
        return resp.getData();
    }

    /**
     * Submit standalone workflow tasks for the authenticated user.
     * Accepted Roles: * All Access 
     * @param body the submit data (required)
     * @param taskId the workflow task id (optional)
     * @param action the workflowAction (optional)
     * @param approver the approver, leave null if you are the approver, otherwise include         but note if you include it and are not a workflow admin, this call will get rejected (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> submitStandaloneWorkflowApprovalWithHttpInfo(String body, String taskId, String action, String approver) throws ApiException {
        com.squareup.okhttp.Call call = submitStandaloneWorkflowApprovalValidateBeforeCall(body, taskId, action, approver, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Submit standalone workflow tasks for the authenticated user. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body the submit data (required)
     * @param taskId the workflow task id (optional)
     * @param action the workflowAction (optional)
     * @param approver the approver, leave null if you are the approver, otherwise include         but note if you include it and are not a workflow admin, this call will get rejected (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call submitStandaloneWorkflowApprovalAsync(String body, String taskId, String action, String approver, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = submitStandaloneWorkflowApprovalValidateBeforeCall(body, taskId, action, approver, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
