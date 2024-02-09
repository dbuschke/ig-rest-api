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


import io.swagger.client.model.JobTrigger;
import io.swagger.client.model.Response;
import io.swagger.client.model.Schedule;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleApi {
    private ApiClient apiClient;

    public ScheduleApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ScheduleApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for createSchedule
     * @param body node describing schedule (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createScheduleCall(Schedule body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules";

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
    private com.squareup.okhttp.Call createScheduleValidateBeforeCall(Schedule body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createSchedule(Async)");
        }
        
        com.squareup.okhttp.Call call = createScheduleCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create a collection or risk scoring  schedule.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body node describing schedule (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response createSchedule(Schedule body) throws ApiException {
        ApiResponse<Response> resp = createScheduleWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create a collection or risk scoring  schedule.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body node describing schedule (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> createScheduleWithHttpInfo(Schedule body) throws ApiException {
        com.squareup.okhttp.Call call = createScheduleValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a collection or risk scoring  schedule. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body node describing schedule (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createScheduleAsync(Schedule body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createScheduleValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteSchedule
     * @param id id of schedule (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteScheduleCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules/{id}"
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
    private com.squareup.okhttp.Call deleteScheduleValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteSchedule(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteScheduleCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the indicated schedule.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id id of schedule (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response deleteSchedule(String id) throws ApiException {
        ApiResponse<Response> resp = deleteScheduleWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete the indicated schedule.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id id of schedule (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> deleteScheduleWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = deleteScheduleValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the indicated schedule. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param id id of schedule (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteScheduleAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteScheduleValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCollectionSchedule
     * @param id schedule unique ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCollectionScheduleCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules/{id}"
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
    private com.squareup.okhttp.Call getCollectionScheduleValidateBeforeCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getCollectionSchedule(Async)");
        }
        
        com.squareup.okhttp.Call call = getCollectionScheduleCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the collection schedule with specified unique ID.
     * Information returned includes previous &amp; next run times.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id schedule unique ID (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getCollectionSchedule(String id) throws ApiException {
        ApiResponse<Response> resp = getCollectionScheduleWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the collection schedule with specified unique ID.
     * Information returned includes previous &amp; next run times.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id schedule unique ID (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getCollectionScheduleWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = getCollectionScheduleValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the collection schedule with specified unique ID. (asynchronously)
     * Information returned includes previous &amp; next run times.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id schedule unique ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCollectionScheduleAsync(String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCollectionScheduleValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCollectionSchedules
     * @param sortBy field to sort results on; one of [&#x27;name&#x27;, &#x27;last&#x27;, &#x27;next&#x27;] (optional, default to next)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 750)
     * @param q (&#x27;q&#x27;) search name and description (optional)
     * @param qMatch (&#x27;qmatch&#x27;) how to match the search string (optional)
     * @param sourceType Data source type, if restricting schedules to those referencing a specified data source. (optional)
     * @param scheduleKind - job schedule kind (optional)
     * @param sourceID DataSource id, if restricting schedules to those referencing a specified data source. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCollectionSchedulesCall(String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, String sourceType, String scheduleKind, Long sourceID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (sourceType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sourceType", sourceType));
        if (scheduleKind != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("scheduleKind", scheduleKind));
        if (sourceID != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sourceID", sourceID));

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
    private com.squareup.okhttp.Call getCollectionSchedulesValidateBeforeCall(String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, String sourceType, String scheduleKind, Long sourceID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getCollectionSchedulesCall(sortBy, sortOrder, indexFrom, size, q, qMatch, sourceType, scheduleKind, sourceID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get summary information about all collection schedules.
     * (GET /api/schedule/schedules)  Information returned includes previous &amp; next run times.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param sortBy field to sort results on; one of [&#x27;name&#x27;, &#x27;last&#x27;, &#x27;next&#x27;] (optional, default to next)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 750)
     * @param q (&#x27;q&#x27;) search name and description (optional)
     * @param qMatch (&#x27;qmatch&#x27;) how to match the search string (optional)
     * @param sourceType Data source type, if restricting schedules to those referencing a specified data source. (optional)
     * @param scheduleKind - job schedule kind (optional)
     * @param sourceID DataSource id, if restricting schedules to those referencing a specified data source. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getCollectionSchedules(String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, String sourceType, String scheduleKind, Long sourceID) throws ApiException {
        ApiResponse<Response> resp = getCollectionSchedulesWithHttpInfo(sortBy, sortOrder, indexFrom, size, q, qMatch, sourceType, scheduleKind, sourceID);
        return resp.getData();
    }

    /**
     * Get summary information about all collection schedules.
     * (GET /api/schedule/schedules)  Information returned includes previous &amp; next run times.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param sortBy field to sort results on; one of [&#x27;name&#x27;, &#x27;last&#x27;, &#x27;next&#x27;] (optional, default to next)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 750)
     * @param q (&#x27;q&#x27;) search name and description (optional)
     * @param qMatch (&#x27;qmatch&#x27;) how to match the search string (optional)
     * @param sourceType Data source type, if restricting schedules to those referencing a specified data source. (optional)
     * @param scheduleKind - job schedule kind (optional)
     * @param sourceID DataSource id, if restricting schedules to those referencing a specified data source. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getCollectionSchedulesWithHttpInfo(String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, String sourceType, String scheduleKind, Long sourceID) throws ApiException {
        com.squareup.okhttp.Call call = getCollectionSchedulesValidateBeforeCall(sortBy, sortOrder, indexFrom, size, q, qMatch, sourceType, scheduleKind, sourceID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get summary information about all collection schedules. (asynchronously)
     * (GET /api/schedule/schedules)  Information returned includes previous &amp; next run times.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param sortBy field to sort results on; one of [&#x27;name&#x27;, &#x27;last&#x27;, &#x27;next&#x27;] (optional, default to next)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 750)
     * @param q (&#x27;q&#x27;) search name and description (optional)
     * @param qMatch (&#x27;qmatch&#x27;) how to match the search string (optional)
     * @param sourceType Data source type, if restricting schedules to those referencing a specified data source. (optional)
     * @param scheduleKind - job schedule kind (optional)
     * @param sourceID DataSource id, if restricting schedules to those referencing a specified data source. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCollectionSchedulesAsync(String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, String sourceType, String scheduleKind, Long sourceID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCollectionSchedulesValidateBeforeCall(sortBy, sortOrder, indexFrom, size, q, qMatch, sourceType, scheduleKind, sourceID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDataSources
     * @param q substring to search for in name or description (optional)
     * @param qMatch match mode for substring; one of (\&quot;START\&quot;, \&quot;EXACT\&quot;, \&quot;ANY\&quot;) (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDataSourcesCall(String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/sources";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getDataSourcesValidateBeforeCall(String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getDataSourcesCall(q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get data sources whose name or description match the search parameters.
     * Information returned includes name and id of schedules that operate on that data source.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param q substring to search for in name or description (optional)
     * @param qMatch match mode for substring; one of (\&quot;START\&quot;, \&quot;EXACT\&quot;, \&quot;ANY\&quot;) (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDataSources(String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getDataSourcesWithHttpInfo(q, qMatch);
        return resp.getData();
    }

    /**
     * Get data sources whose name or description match the search parameters.
     * Information returned includes name and id of schedules that operate on that data source.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param q substring to search for in name or description (optional)
     * @param qMatch match mode for substring; one of (\&quot;START\&quot;, \&quot;EXACT\&quot;, \&quot;ANY\&quot;) (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDataSourcesWithHttpInfo(String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getDataSourcesValidateBeforeCall(q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get data sources whose name or description match the search parameters. (asynchronously)
     * Information returned includes name and id of schedules that operate on that data source.&lt;br/&gt;Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param q substring to search for in name or description (optional)
     * @param qMatch match mode for substring; one of (\&quot;START\&quot;, \&quot;EXACT\&quot;, \&quot;ANY\&quot;) (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDataSourcesAsync(String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDataSourcesValidateBeforeCall(q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPreviousCollections
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param dsId data source id (optional), it is used for data sources filter (optional)
     * @param dspType data source production type (optional), have to be with dsId (optional)
     * @param isCollection true/false flag to return collections (true) or publications (false), ignore if dsId and dsType are passed (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPreviousCollectionsCall(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Long dsId, String dspType, Boolean isCollection, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/previousCollections";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (startTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startTime", startTime));
        if (endTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("endTime", endTime));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (dsId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dsId", dsId));
        if (dspType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("dspType", dspType));
        if (isCollection != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("isCollection", isCollection));

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
    private com.squareup.okhttp.Call getPreviousCollectionsValidateBeforeCall(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Long dsId, String dspType, Boolean isCollection, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getPreviousCollectionsCall(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch, dsId, dspType, isCollection, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get summary information about previous collections of identity and application sources.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param dsId data source id (optional), it is used for data sources filter (optional)
     * @param dspType data source production type (optional), have to be with dsId (optional)
     * @param isCollection true/false flag to return collections (true) or publications (false), ignore if dsId and dsType are passed (optional, default to true)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPreviousCollections(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Long dsId, String dspType, Boolean isCollection) throws ApiException {
        ApiResponse<Response> resp = getPreviousCollectionsWithHttpInfo(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch, dsId, dspType, isCollection);
        return resp.getData();
    }

    /**
     * Get summary information about previous collections of identity and application sources.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param dsId data source id (optional), it is used for data sources filter (optional)
     * @param dspType data source production type (optional), have to be with dsId (optional)
     * @param isCollection true/false flag to return collections (true) or publications (false), ignore if dsId and dsType are passed (optional, default to true)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPreviousCollectionsWithHttpInfo(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Long dsId, String dspType, Boolean isCollection) throws ApiException {
        com.squareup.okhttp.Call call = getPreviousCollectionsValidateBeforeCall(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch, dsId, dspType, isCollection, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get summary information about previous collections of identity and application sources. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param dsId data source id (optional), it is used for data sources filter (optional)
     * @param dspType data source production type (optional), have to be with dsId (optional)
     * @param isCollection true/false flag to return collections (true) or publications (false), ignore if dsId and dsType are passed (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPreviousCollectionsAsync(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, Long dsId, String dspType, Boolean isCollection, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPreviousCollectionsValidateBeforeCall(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch, dsId, dspType, isCollection, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getScheduleRun
     * @param schedId unique ID for the schedule (TBD: superfluous) (required)
     * @param runId id for the specific run (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getScheduleRunCall(String schedId, Long runId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules/{schedId}/runs/{runId}"
            .replaceAll("\\{" + "schedId" + "\\}", apiClient.escapeString(schedId.toString()))
            .replaceAll("\\{" + "runId" + "\\}", apiClient.escapeString(runId.toString()));

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
    private com.squareup.okhttp.Call getScheduleRunValidateBeforeCall(String schedId, Long runId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'schedId' is set
        if (schedId == null) {
            throw new ApiException("Missing the required parameter 'schedId' when calling getScheduleRun(Async)");
        }
        // verify the required parameter 'runId' is set
        if (runId == null) {
            throw new ApiException("Missing the required parameter 'runId' when calling getScheduleRun(Async)");
        }
        
        com.squareup.okhttp.Call call = getScheduleRunCall(schedId, runId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * get source information about a specific schedule run
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param schedId unique ID for the schedule (TBD: superfluous) (required)
     * @param runId id for the specific run (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getScheduleRun(String schedId, Long runId) throws ApiException {
        ApiResponse<Response> resp = getScheduleRunWithHttpInfo(schedId, runId);
        return resp.getData();
    }

    /**
     * get source information about a specific schedule run
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param schedId unique ID for the schedule (TBD: superfluous) (required)
     * @param runId id for the specific run (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getScheduleRunWithHttpInfo(String schedId, Long runId) throws ApiException {
        com.squareup.okhttp.Call call = getScheduleRunValidateBeforeCall(schedId, runId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * get source information about a specific schedule run (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param schedId unique ID for the schedule (TBD: superfluous) (required)
     * @param runId id for the specific run (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getScheduleRunAsync(String schedId, Long runId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getScheduleRunValidateBeforeCall(schedId, runId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getScheduleRuns
     * @param id long ID for the schedule run (job_end table id) (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getScheduleRunsCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules/runs/{id}"
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
    private com.squareup.okhttp.Call getScheduleRunsValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getScheduleRuns(Async)");
        }
        
        com.squareup.okhttp.Call call = getScheduleRunsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * get the run by the specified id
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id long ID for the schedule run (job_end table id) (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getScheduleRuns(Long id) throws ApiException {
        ApiResponse<Response> resp = getScheduleRunsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * get the run by the specified id
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id long ID for the schedule run (job_end table id) (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getScheduleRunsWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getScheduleRunsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * get the run by the specified id (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id long ID for the schedule run (job_end table id) (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getScheduleRunsAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getScheduleRunsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getScheduleRuns_0
     * @param id unique ID for the schedule (required)
     * @param size number of most recent runs to summarize, (optional, default to 10)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getScheduleRuns_0Call(String id, Integer size, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules/{id}/runs"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getScheduleRuns_0ValidateBeforeCall(String id, Integer size, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getScheduleRuns_0(Async)");
        }
        
        com.squareup.okhttp.Call call = getScheduleRuns_0Call(id, size, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * get the runs of a specified schedule
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id unique ID for the schedule (required)
     * @param size number of most recent runs to summarize, (optional, default to 10)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getScheduleRuns_0(String id, Integer size, Integer indexFrom) throws ApiException {
        ApiResponse<Response> resp = getScheduleRuns_0WithHttpInfo(id, size, indexFrom);
        return resp.getData();
    }

    /**
     * get the runs of a specified schedule
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id unique ID for the schedule (required)
     * @param size number of most recent runs to summarize, (optional, default to 10)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getScheduleRuns_0WithHttpInfo(String id, Integer size, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getScheduleRuns_0ValidateBeforeCall(id, size, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * get the runs of a specified schedule (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param id unique ID for the schedule (required)
     * @param size number of most recent runs to summarize, (optional, default to 10)
     * @param indexFrom starting row index in result set. 0 is the first. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getScheduleRuns_0Async(String id, Integer size, Integer indexFrom, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getScheduleRuns_0ValidateBeforeCall(id, size, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUniquePreviousCollections
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param indexFrom base-0 start in underlying sorted results (optional, default to 0)
     * @param size count of results staring from indexFrom (optional, default to 0)
     * @param dspType data source production type list (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUniquePreviousCollectionsCall(Long startTime, Long endTime, String q, String qMatch, Integer indexFrom, Integer size, List<String> dspType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/uniquePreviousCollections";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (startTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startTime", startTime));
        if (endTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("endTime", endTime));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (dspType != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "dspType", dspType));

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
    private com.squareup.okhttp.Call getUniquePreviousCollectionsValidateBeforeCall(Long startTime, Long endTime, String q, String qMatch, Integer indexFrom, Integer size, List<String> dspType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getUniquePreviousCollectionsCall(startTime, endTime, q, qMatch, indexFrom, size, dspType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get summary information about unique previous collections of identity and application sources.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param indexFrom base-0 start in underlying sorted results (optional, default to 0)
     * @param size count of results staring from indexFrom (optional, default to 0)
     * @param dspType data source production type list (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getUniquePreviousCollections(Long startTime, Long endTime, String q, String qMatch, Integer indexFrom, Integer size, List<String> dspType) throws ApiException {
        ApiResponse<Response> resp = getUniquePreviousCollectionsWithHttpInfo(startTime, endTime, q, qMatch, indexFrom, size, dspType);
        return resp.getData();
    }

    /**
     * Get summary information about unique previous collections of identity and application sources.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param indexFrom base-0 start in underlying sorted results (optional, default to 0)
     * @param size count of results staring from indexFrom (optional, default to 0)
     * @param dspType data source production type list (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getUniquePreviousCollectionsWithHttpInfo(Long startTime, Long endTime, String q, String qMatch, Integer indexFrom, Integer size, List<String> dspType) throws ApiException {
        com.squareup.okhttp.Call call = getUniquePreviousCollectionsValidateBeforeCall(startTime, endTime, q, qMatch, indexFrom, size, dspType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get summary information about unique previous collections of identity and application sources. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime beginning of period for production times. (optional)
     * @param endTime end of period for production times. (optional)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param indexFrom base-0 start in underlying sorted results (optional, default to 0)
     * @param size count of results staring from indexFrom (optional, default to 0)
     * @param dspType data source production type list (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUniquePreviousCollectionsAsync(Long startTime, Long endTime, String q, String qMatch, Integer indexFrom, Integer size, List<String> dspType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUniquePreviousCollectionsValidateBeforeCall(startTime, endTime, q, qMatch, indexFrom, size, dspType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUpcomingCollections
     * @param startTime Start of period for schedule times to include. (optional)
     * @param endTime End of period for schedule times to include. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUpcomingCollectionsCall(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/schedule/upcomingCollections";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (startTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("startTime", startTime));
        if (endTime != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("endTime", endTime));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getUpcomingCollectionsValidateBeforeCall(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getUpcomingCollectionsCall(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get summary information about upcoming collections of identity and application sources.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime Start of period for schedule times to include. (optional)
     * @param endTime End of period for schedule times to include. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getUpcomingCollections(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch) throws ApiException {
        ApiResponse<Response> resp = getUpcomingCollectionsWithHttpInfo(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch);
        return resp.getData();
    }

    /**
     * Get summary information about upcoming collections of identity and application sources.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime Start of period for schedule times to include. (optional)
     * @param endTime End of period for schedule times to include. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getUpcomingCollectionsWithHttpInfo(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch) throws ApiException {
        com.squareup.okhttp.Call call = getUpcomingCollectionsValidateBeforeCall(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get summary information about upcoming collections of identity and application sources. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param startTime Start of period for schedule times to include. (optional)
     * @param endTime End of period for schedule times to include. (optional)
     * @param sortBy field to sort results on;                one of [&#x27;name&#x27; (data source name), &#x27;scheduleName&#x27;, &#x27;date&#x27;] (optional, default to date)
     * @param sortOrder &#x27;ASC&#x27; or &#x27;DESC&#x27; (optional, default to ASC)
     * @param indexFrom first index in 0-based result set (optional, default to 0)
     * @param size max results to return from (optional, default to 10)
     * @param q (&#x27;q&#x27;) search name &amp; description of data source and schedule (optional)
     * @param qMatch (&#x27;qMatch&#x27;) how to match the search string (optional, default to ANY)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUpcomingCollectionsAsync(Long startTime, Long endTime, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, String qMatch, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUpcomingCollectionsValidateBeforeCall(startTime, endTime, sortBy, sortOrder, indexFrom, size, q, qMatch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for previewTrigger
     * @param body The schedule trigger to analyze (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call previewTriggerCall(JobTrigger body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules/preview";

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
    private com.squareup.okhttp.Call previewTriggerValidateBeforeCall(JobTrigger body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling previewTrigger(Async)");
        }
        
        com.squareup.okhttp.Call call = previewTriggerCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Preview a schedule trigger.
     * (\&quot;Get data about a custom schedule run\&quot;)&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body The schedule trigger to analyze (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response previewTrigger(JobTrigger body) throws ApiException {
        ApiResponse<Response> resp = previewTriggerWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Preview a schedule trigger.
     * (\&quot;Get data about a custom schedule run\&quot;)&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body The schedule trigger to analyze (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> previewTriggerWithHttpInfo(JobTrigger body) throws ApiException {
        com.squareup.okhttp.Call call = previewTriggerValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Preview a schedule trigger. (asynchronously)
     * (\&quot;Get data about a custom schedule run\&quot;)&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body The schedule trigger to analyze (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call previewTriggerAsync(JobTrigger body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = previewTriggerValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateSchedule
     * @param body replacement schedule (required)
     * @param id schedule id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateScheduleCall(Schedule body, String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/schedule/schedules/{id}"
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
    private com.squareup.okhttp.Call updateScheduleValidateBeforeCall(Schedule body, String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateSchedule(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateSchedule(Async)");
        }
        
        com.squareup.okhttp.Call call = updateScheduleCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the definition of a collection or risk scoring schedule
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body replacement schedule (required)
     * @param id schedule id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateSchedule(Schedule body, String id) throws ApiException {
        ApiResponse<Response> resp = updateScheduleWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update the definition of a collection or risk scoring schedule
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body replacement schedule (required)
     * @param id schedule id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateScheduleWithHttpInfo(Schedule body, String id) throws ApiException {
        com.squareup.okhttp.Call call = updateScheduleValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the definition of a collection or risk scoring schedule (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body replacement schedule (required)
     * @param id schedule id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateScheduleAsync(Schedule body, String id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateScheduleValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
