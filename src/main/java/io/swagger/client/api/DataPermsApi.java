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


import io.swagger.client.model.Authcauses;
import io.swagger.client.model.Permission;
import io.swagger.client.model.Permissions;
import io.swagger.client.model.Response;
import io.swagger.client.model.SearchCriteria;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPermsApi {
    private ApiClient apiClient;

    public DataPermsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DataPermsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getAuthorizedByCause
     * @param id ID of AUTH_USER_RESOURCE record which contains the information about the business role,  permission, and user that was authorized. (required)
     * @param authForUser Flag indicating whether to check authorized by for perm + user or just perm. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedByCauseCall(Long id, Boolean authForUser, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/perms/authorizedby/{id}/causes"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (authForUser != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("authForUser", authForUser));

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
    private com.squareup.okhttp.Call getAuthorizedByCauseValidateBeforeCall(Long id, Boolean authForUser, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getAuthorizedByCause(Async)");
        }
        
        com.squareup.okhttp.Call call = getAuthorizedByCauseCall(id, authForUser, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of all of the causes that a business role authorizes a permission indirectly.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id ID of AUTH_USER_RESOURCE record which contains the information about the business role,  permission, and user that was authorized. (required)
     * @param authForUser Flag indicating whether to check authorized by for perm + user or just perm. (optional, default to false)
     * @return Authcauses
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Authcauses getAuthorizedByCause(Long id, Boolean authForUser) throws ApiException {
        ApiResponse<Authcauses> resp = getAuthorizedByCauseWithHttpInfo(id, authForUser);
        return resp.getData();
    }

    /**
     * Return list of all of the causes that a business role authorizes a permission indirectly.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id ID of AUTH_USER_RESOURCE record which contains the information about the business role,  permission, and user that was authorized. (required)
     * @param authForUser Flag indicating whether to check authorized by for perm + user or just perm. (optional, default to false)
     * @return ApiResponse&lt;Authcauses&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Authcauses> getAuthorizedByCauseWithHttpInfo(Long id, Boolean authForUser) throws ApiException {
        com.squareup.okhttp.Call call = getAuthorizedByCauseValidateBeforeCall(id, authForUser, null, null);
        Type localVarReturnType = new TypeToken<Authcauses>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of all of the causes that a business role authorizes a permission indirectly. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Application Administrator * Application Owner * Auditor * Authenticated User * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Review Auditor * Historical Review Owner * Historical Reviewer * Report Administrator * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param id ID of AUTH_USER_RESOURCE record which contains the information about the business role,  permission, and user that was authorized. (required)
     * @param authForUser Flag indicating whether to check authorized by for perm + user or just perm. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAuthorizedByCauseAsync(Long id, Boolean authForUser, final ApiCallback<Authcauses> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAuthorizedByCauseValidateBeforeCall(id, authForUser, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Authcauses>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPermissionByPermissionId
     * @param body - permission id to search for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPermissionByPermissionIdCall(Permission body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/perms/permissionId";

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
    private com.squareup.okhttp.Call getPermissionByPermissionIdValidateBeforeCall(Permission body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getPermissionByPermissionId(Async)");
        }
        
        com.squareup.okhttp.Call call = getPermissionByPermissionIdCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of all of the causes that a business role authorizes a permission indirectly.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - permission id to search for (required)
     * @return Authcauses
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Authcauses getPermissionByPermissionId(Permission body) throws ApiException {
        ApiResponse<Authcauses> resp = getPermissionByPermissionIdWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Return list of all of the causes that a business role authorizes a permission indirectly.
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - permission id to search for (required)
     * @return ApiResponse&lt;Authcauses&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Authcauses> getPermissionByPermissionIdWithHttpInfo(Permission body) throws ApiException {
        com.squareup.okhttp.Call call = getPermissionByPermissionIdValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Authcauses>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of all of the causes that a business role authorizes a permission indirectly. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator 
     * @param body - permission id to search for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPermissionByPermissionIdAsync(Permission body, final ApiCallback<Authcauses> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPermissionByPermissionIdValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Authcauses>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPermissionData
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - list of attributes to be returned (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search against attributes enabled for type ahead search (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPermissionDataCall(Integer size, String q, String sortBy, String sortOrder, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/perms";

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
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (reviewablesOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reviewablesOnly", reviewablesOnly));

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
    private com.squareup.okhttp.Call getPermissionDataValidateBeforeCall(Integer size, String q, String sortBy, String sortOrder, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getPermissionDataCall(size, q, sortBy, sortOrder, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of permissions from all applications, also include the information about permission
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - list of attributes to be returned (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search against attributes enabled for type ahead search (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPermissionData(Integer size, String q, String sortBy, String sortOrder, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly) throws ApiException {
        ApiResponse<Response> resp = getPermissionDataWithHttpInfo(size, q, sortBy, sortOrder, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly);
        return resp.getData();
    }

    /**
     * Return list of permissions from all applications, also include the information about permission
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - list of attributes to be returned (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search against attributes enabled for type ahead search (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPermissionDataWithHttpInfo(Integer size, String q, String sortBy, String sortOrder, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly) throws ApiException {
        com.squareup.okhttp.Call call = getPermissionDataValidateBeforeCall(size, q, sortBy, sortOrder, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of permissions from all applications, also include the information about permission (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr - list of attributes to be returned (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search against attributes enabled for type ahead search (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPermissionDataAsync(Integer size, String q, String sortBy, String sortOrder, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPermissionDataValidateBeforeCall(size, q, sortBy, sortOrder, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPermissionsByIdsDataAdvancedFilter
     * @param body - if provided, only return permissions with specified permission ids in criteria node perms. (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, get only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPermissionsByIdsDataAdvancedFilterCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/perms/byIds";

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
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (reviewablesOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reviewablesOnly", reviewablesOnly));
        if (staticOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("staticOnly", staticOnly));
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
    private com.squareup.okhttp.Call getPermissionsByIdsDataAdvancedFilterValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getPermissionsByIdsDataAdvancedFilter(Async)");
        }
        
        com.squareup.okhttp.Call call = getPermissionsByIdsDataAdvancedFilterCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of all  or, if permission ids provided, only return permissions with specified ids.
     * If permIdsJson &#x3D;&#x3D; null, return all permissions based upon the other parameters passed in.&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator * Technical Roles Administrator 
     * @param body - if provided, only return permissions with specified permission ids in criteria node perms. (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, get only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPermissionsByIdsDataAdvancedFilter(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = getPermissionsByIdsDataAdvancedFilterWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter);
        return resp.getData();
    }

    /**
     * Return list of all  or, if permission ids provided, only return permissions with specified ids.
     * If permIdsJson &#x3D;&#x3D; null, return all permissions based upon the other parameters passed in.&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator * Technical Roles Administrator 
     * @param body - if provided, only return permissions with specified permission ids in criteria node perms. (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, get only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPermissionsByIdsDataAdvancedFilterWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getPermissionsByIdsDataAdvancedFilterValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of all  or, if permission ids provided, only return permissions with specified ids. (asynchronously)
     * If permIdsJson &#x3D;&#x3D; null, return all permissions based upon the other parameters passed in.&lt;br/&gt;Accepted Roles: * Customer Administrator * Data Administrator * Technical Roles Administrator 
     * @param body - if provided, only return permissions with specified permission ids in criteria node perms. (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, get only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPermissionsByIdsDataAdvancedFilterAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPermissionsByIdsDataAdvancedFilterValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPermissionsDataAdvanced
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPermissionsDataAdvancedCall(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/perms/advanced";

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
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (reviewablesOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reviewablesOnly", reviewablesOnly));
        if (staticOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("staticOnly", staticOnly));
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
    private com.squareup.okhttp.Call getPermissionsDataAdvancedValidateBeforeCall(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getPermissionsDataAdvancedCall(size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of permissions data
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPermissionsDataAdvanced(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = getPermissionsDataAdvancedWithHttpInfo(size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter);
        return resp.getData();
    }

    /**
     * Return list of permissions data
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPermissionsDataAdvancedWithHttpInfo(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getPermissionsDataAdvancedValidateBeforeCall(size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of permissions data (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Manager * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator 
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPermissionsDataAdvancedAsync(Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPermissionsDataAdvancedValidateBeforeCall(size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPermissionsDataAdvancedFilter
     * @param body advanced search criteria (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPermissionsDataAdvancedFilterCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/perms/advanced";

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
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (reviewablesOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("reviewablesOnly", reviewablesOnly));
        if (staticOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("staticOnly", staticOnly));
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
    private com.squareup.okhttp.Call getPermissionsDataAdvancedFilterValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getPermissionsDataAdvancedFilter(Async)");
        }
        
        com.squareup.okhttp.Call call = getPermissionsDataAdvancedFilterCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of permissions data
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body advanced search criteria (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Permissions getPermissionsDataAdvancedFilter(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter) throws ApiException {
        ApiResponse<Permissions> resp = getPermissionsDataAdvancedFilterWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter);
        return resp.getData();
    }

    /**
     * Return list of permissions data
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body advanced search criteria (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Permissions> getPermissionsDataAdvancedFilterWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getPermissionsDataAdvancedFilterValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of permissions data (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Role Manager * Business Roles Administrator * Customer Administrator * Data Administrator * Review Administrator * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body advanced search criteria (required)
     * @param size - count os results to be returned (optional, default to 10)
     * @param q - search filter (optional)
     * @param sortBy the attribute to sort by (optional)
     * @param sortOrder - sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr The attributes to return in the payload. Returns all if not         present. Server can return them in any order. (optional)
     * @param qMatch - match mode (optional)
     * @param typeAheadSearch - search triggered from a type ahead suggest box (optional, default to false)
     * @param showCt show total result set count (optional, default to false)
     * @param reviewablesOnly if true, exclude permissions marked notReviewable (optional, default to false)
     * @param staticOnly if true, return only static permissions (optional, default to false)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPermissionsDataAdvancedFilterAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean reviewablesOnly, Boolean staticOnly, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPermissionsDataAdvancedFilterValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, reviewablesOnly, staticOnly, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
