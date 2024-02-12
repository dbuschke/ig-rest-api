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


import de.araneaconsult.codegen.ig.rest.model.Download;
import de.araneaconsult.codegen.ig.rest.model.FulfillmentCatalogUpdateInfoNode;
import de.araneaconsult.codegen.ig.rest.model.FulfillmentItemSearch;
import de.araneaconsult.codegen.ig.rest.model.FulfillmentList;
import de.araneaconsult.codegen.ig.rest.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FulfillmentApi {
    private ApiClient apiClient;

    public FulfillmentApi() {
        this(Configuration.getDefaultApiClient());
    }

    public FulfillmentApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for bulkFulfill
     * @param body the fulfillment search node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call bulkFulfillCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/bulkFulfill";

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
    private com.squareup.okhttp.Call bulkFulfillValidateBeforeCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling bulkFulfill(Async)");
        }
        
        com.squareup.okhttp.Call call = bulkFulfillCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Bulk fulfill all items in the identified in the search node
     * Accepted Roles: * Application Access Change Fulfiller * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response bulkFulfill(FulfillmentItemSearch body) throws ApiException {
        ApiResponse<Response> resp = bulkFulfillWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Bulk fulfill all items in the identified in the search node
     * Accepted Roles: * Application Access Change Fulfiller * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> bulkFulfillWithHttpInfo(FulfillmentItemSearch body) throws ApiException {
        com.squareup.okhttp.Call call = bulkFulfillValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Bulk fulfill all items in the identified in the search node (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call bulkFulfillAsync(FulfillmentItemSearch body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = bulkFulfillValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBusinessRoleAuthorizationFulfillmentItem
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to iname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (permission, technical role or application) (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleAuthorizationFulfillmentItemCall(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/items/{fulfillmentItemId}/broleauth"
            .replaceAll("\\{" + "fulfillmentItemId" + "\\}", apiClient.escapeString(fulfillmentItemId.toString()));

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
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));

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
    private com.squareup.okhttp.Call getBusinessRoleAuthorizationFulfillmentItemValidateBeforeCall(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillmentItemId' is set
        if (fulfillmentItemId == null) {
            throw new ApiException("Missing the required parameter 'fulfillmentItemId' when calling getBusinessRoleAuthorizationFulfillmentItem(Async)");
        }
        
        com.squareup.okhttp.Call call = getBusinessRoleAuthorizationFulfillmentItemCall(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the business role authorizations that require fulfillment
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to iname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (permission, technical role or application) (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getBusinessRoleAuthorizationFulfillmentItem(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type) throws ApiException {
        ApiResponse<Response> resp = getBusinessRoleAuthorizationFulfillmentItemWithHttpInfo(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type);
        return resp.getData();
    }

    /**
     * Get the business role authorizations that require fulfillment
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to iname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (permission, technical role or application) (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getBusinessRoleAuthorizationFulfillmentItemWithHttpInfo(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRoleAuthorizationFulfillmentItemValidateBeforeCall(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the business role authorizations that require fulfillment (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to iname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (permission, technical role or application) (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleAuthorizationFulfillmentItemAsync(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRoleAuthorizationFulfillmentItemValidateBeforeCall(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getBusinessRoleAuthorizationFulfillmentItem_0
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleAuthorizationFulfillmentItem_0Call(Long fulfillmentItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/items/{fulfillmentItemId}/broleauthct"
            .replaceAll("\\{" + "fulfillmentItemId" + "\\}", apiClient.escapeString(fulfillmentItemId.toString()));

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
    private com.squareup.okhttp.Call getBusinessRoleAuthorizationFulfillmentItem_0ValidateBeforeCall(Long fulfillmentItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillmentItemId' is set
        if (fulfillmentItemId == null) {
            throw new ApiException("Missing the required parameter 'fulfillmentItemId' when calling getBusinessRoleAuthorizationFulfillmentItem_0(Async)");
        }
        
        com.squareup.okhttp.Call call = getBusinessRoleAuthorizationFulfillmentItem_0Call(fulfillmentItemId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the counts by type of business role authorizations that require fulfillment
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getBusinessRoleAuthorizationFulfillmentItem_0(Long fulfillmentItemId) throws ApiException {
        ApiResponse<Response> resp = getBusinessRoleAuthorizationFulfillmentItem_0WithHttpInfo(fulfillmentItemId);
        return resp.getData();
    }

    /**
     * Get the counts by type of business role authorizations that require fulfillment
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getBusinessRoleAuthorizationFulfillmentItem_0WithHttpInfo(Long fulfillmentItemId) throws ApiException {
        com.squareup.okhttp.Call call = getBusinessRoleAuthorizationFulfillmentItem_0ValidateBeforeCall(fulfillmentItemId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the counts by type of business role authorizations that require fulfillment (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getBusinessRoleAuthorizationFulfillmentItem_0Async(Long fulfillmentItemId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getBusinessRoleAuthorizationFulfillmentItem_0ValidateBeforeCall(fulfillmentItemId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCatalogFulfillmentTargets
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCatalogFulfillmentTargetsCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/catalogUpdate";

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
    private com.squareup.okhttp.Call getCatalogFulfillmentTargetsValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getCatalogFulfillmentTargetsCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the catalog fulfillment list
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getCatalogFulfillmentTargets() throws ApiException {
        ApiResponse<Response> resp = getCatalogFulfillmentTargetsWithHttpInfo();
        return resp.getData();
    }

    /**
     * Get the catalog fulfillment list
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getCatalogFulfillmentTargetsWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getCatalogFulfillmentTargetsValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the catalog fulfillment list (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCatalogFulfillmentTargetsAsync(final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCatalogFulfillmentTargetsValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChildEntityFulfillmentItems
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to aname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChildEntityFulfillmentItemsCall(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/items/{fulfillmentItemId}/entities"
            .replaceAll("\\{" + "fulfillmentItemId" + "\\}", apiClient.escapeString(fulfillmentItemId.toString()));

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
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));

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
    private com.squareup.okhttp.Call getChildEntityFulfillmentItemsValidateBeforeCall(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillmentItemId' is set
        if (fulfillmentItemId == null) {
            throw new ApiException("Missing the required parameter 'fulfillmentItemId' when calling getChildEntityFulfillmentItems(Async)");
        }
        
        com.squareup.okhttp.Call call = getChildEntityFulfillmentItemsCall(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get child entities (e.g.
     * permissions) that are involved in this fulfillment&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to aname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getChildEntityFulfillmentItems(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type) throws ApiException {
        ApiResponse<Response> resp = getChildEntityFulfillmentItemsWithHttpInfo(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type);
        return resp.getData();
    }

    /**
     * Get child entities (e.g.
     * permissions) that are involved in this fulfillment&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to aname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getChildEntityFulfillmentItemsWithHttpInfo(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type) throws ApiException {
        com.squareup.okhttp.Call call = getChildEntityFulfillmentItemsValidateBeforeCall(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get child entities (e.g. (asynchronously)
     * permissions) that are involved in this fulfillment&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change reqeqst item id (required)
     * @param sortBy sortBy column,    iname&#x3D;item/authorization name,    uname&#x3D;requester name,   aname&#x3D;application name (only applicable to permission type) (optional, default to aname)
     * @param sortOrder sort order: ASC or DESC (optional, default to ASC)
     * @param indexFrom offset within database search to return results from (optional, default to 0)
     * @param size size of result set (optional, default to 0)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChildEntityFulfillmentItemsAsync(Long fulfillmentItemId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String type, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChildEntityFulfillmentItemsValidateBeforeCall(fulfillmentItemId, sortBy, sortOrder, indexFrom, size, type, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChildEntityFulfillmentItemsCount
     * @param fulfillmentItemId change request item id (required)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChildEntityFulfillmentItemsCountCall(Long fulfillmentItemId, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/items/{fulfillmentItemId}/entities/count"
            .replaceAll("\\{" + "fulfillmentItemId" + "\\}", apiClient.escapeString(fulfillmentItemId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (type != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type", type));

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
    private com.squareup.okhttp.Call getChildEntityFulfillmentItemsCountValidateBeforeCall(Long fulfillmentItemId, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillmentItemId' is set
        if (fulfillmentItemId == null) {
            throw new ApiException("Missing the required parameter 'fulfillmentItemId' when calling getChildEntityFulfillmentItemsCount(Async)");
        }
        
        com.squareup.okhttp.Call call = getChildEntityFulfillmentItemsCountCall(fulfillmentItemId, type, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get count of child entities involved in this fulfillment
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change request item id (required)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getChildEntityFulfillmentItemsCount(Long fulfillmentItemId, String type) throws ApiException {
        ApiResponse<Response> resp = getChildEntityFulfillmentItemsCountWithHttpInfo(fulfillmentItemId, type);
        return resp.getData();
    }

    /**
     * Get count of child entities involved in this fulfillment
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change request item id (required)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getChildEntityFulfillmentItemsCountWithHttpInfo(Long fulfillmentItemId, String type) throws ApiException {
        com.squareup.okhttp.Call call = getChildEntityFulfillmentItemsCountValidateBeforeCall(fulfillmentItemId, type, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get count of child entities involved in this fulfillment (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId change request item id (required)
     * @param type business role authorization type (e.g. PERMISSION) (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChildEntityFulfillmentItemsCountAsync(Long fulfillmentItemId, String type, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChildEntityFulfillmentItemsCountValidateBeforeCall(fulfillmentItemId, type, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFulfillmentItem
     * @param fulfillmentItemId fulfillment id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentItemCall(Long fulfillmentItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/items/{fulfillmentItemId}"
            .replaceAll("\\{" + "fulfillmentItemId" + "\\}", apiClient.escapeString(fulfillmentItemId.toString()));

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
    private com.squareup.okhttp.Call getFulfillmentItemValidateBeforeCall(Long fulfillmentItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillmentItemId' is set
        if (fulfillmentItemId == null) {
            throw new ApiException("Missing the required parameter 'fulfillmentItemId' when calling getFulfillmentItem(Async)");
        }
        
        com.squareup.okhttp.Call call = getFulfillmentItemCall(fulfillmentItemId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get fulfillment item information
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId fulfillment id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFulfillmentItem(Long fulfillmentItemId) throws ApiException {
        ApiResponse<Response> resp = getFulfillmentItemWithHttpInfo(fulfillmentItemId);
        return resp.getData();
    }

    /**
     * Get fulfillment item information
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId fulfillment id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFulfillmentItemWithHttpInfo(Long fulfillmentItemId) throws ApiException {
        com.squareup.okhttp.Call call = getFulfillmentItemValidateBeforeCall(fulfillmentItemId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get fulfillment item information (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId fulfillment id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentItemAsync(Long fulfillmentItemId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFulfillmentItemValidateBeforeCall(fulfillmentItemId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFulfillmentItemAttributes
     * @param fulfillmentItemId fulfillment id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentItemAttributesCall(Long fulfillmentItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/items/{fulfillmentItemId}/attributes"
            .replaceAll("\\{" + "fulfillmentItemId" + "\\}", apiClient.escapeString(fulfillmentItemId.toString()));

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
    private com.squareup.okhttp.Call getFulfillmentItemAttributesValidateBeforeCall(Long fulfillmentItemId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillmentItemId' is set
        if (fulfillmentItemId == null) {
            throw new ApiException("Missing the required parameter 'fulfillmentItemId' when calling getFulfillmentItemAttributes(Async)");
        }
        
        com.squareup.okhttp.Call call = getFulfillmentItemAttributesCall(fulfillmentItemId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get fulfillment item attributes.
     * Only valid for changeRequestTypes  MODIFY_USER_SUPERVISOR and MODIFY_USER_PROFILE&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId fulfillment id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFulfillmentItemAttributes(Long fulfillmentItemId) throws ApiException {
        ApiResponse<Response> resp = getFulfillmentItemAttributesWithHttpInfo(fulfillmentItemId);
        return resp.getData();
    }

    /**
     * Get fulfillment item attributes.
     * Only valid for changeRequestTypes  MODIFY_USER_SUPERVISOR and MODIFY_USER_PROFILE&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId fulfillment id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFulfillmentItemAttributesWithHttpInfo(Long fulfillmentItemId) throws ApiException {
        com.squareup.okhttp.Call call = getFulfillmentItemAttributesValidateBeforeCall(fulfillmentItemId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get fulfillment item attributes. (asynchronously)
     * Only valid for changeRequestTypes  MODIFY_USER_SUPERVISOR and MODIFY_USER_PROFILE&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param fulfillmentItemId fulfillment id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentItemAttributesAsync(Long fulfillmentItemId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFulfillmentItemAttributesValidateBeforeCall(fulfillmentItemId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFulfillmentItems
     * @param body the fulfillment search node  (required)
     * @param groupByAppChangeset if true, group by application/changeset combo - note this is ONLY         used in retry processing, since collapsing other types of         fulfillment items will filter out 2-n fulfillment items for each         app/changset combo (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentItemsCall(FulfillmentItemSearch body, Boolean groupByAppChangeset, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/items";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (groupByAppChangeset != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("groupByAppChangeset", groupByAppChangeset));

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
    private com.squareup.okhttp.Call getFulfillmentItemsValidateBeforeCall(FulfillmentItemSearch body, Boolean groupByAppChangeset, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getFulfillmentItems(Async)");
        }
        
        com.squareup.okhttp.Call call = getFulfillmentItemsCall(body, groupByAppChangeset, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the fulfillment items
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param body the fulfillment search node  (required)
     * @param groupByAppChangeset if true, group by application/changeset combo - note this is ONLY         used in retry processing, since collapsing other types of         fulfillment items will filter out 2-n fulfillment items for each         app/changset combo (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFulfillmentItems(FulfillmentItemSearch body, Boolean groupByAppChangeset) throws ApiException {
        ApiResponse<Response> resp = getFulfillmentItemsWithHttpInfo(body, groupByAppChangeset);
        return resp.getData();
    }

    /**
     * Get the fulfillment items
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param body the fulfillment search node  (required)
     * @param groupByAppChangeset if true, group by application/changeset combo - note this is ONLY         used in retry processing, since collapsing other types of         fulfillment items will filter out 2-n fulfillment items for each         app/changset combo (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFulfillmentItemsWithHttpInfo(FulfillmentItemSearch body, Boolean groupByAppChangeset) throws ApiException {
        com.squareup.okhttp.Call call = getFulfillmentItemsValidateBeforeCall(body, groupByAppChangeset, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the fulfillment items (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Auditor * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Separation of Duties owner 
     * @param body the fulfillment search node  (required)
     * @param groupByAppChangeset if true, group by application/changeset combo - note this is ONLY         used in retry processing, since collapsing other types of         fulfillment items will filter out 2-n fulfillment items for each         app/changset combo (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentItemsAsync(FulfillmentItemSearch body, Boolean groupByAppChangeset, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFulfillmentItemsValidateBeforeCall(body, groupByAppChangeset, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFulfillmentSample
     * @param sampleId the sample if of type SampleIds (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentSampleCall(String sampleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/samples/{sampleId}"
            .replaceAll("\\{" + "sampleId" + "\\}", apiClient.escapeString(sampleId.toString()));

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
    private com.squareup.okhttp.Call getFulfillmentSampleValidateBeforeCall(String sampleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'sampleId' is set
        if (sampleId == null) {
            throw new ApiException("Missing the required parameter 'sampleId' when calling getFulfillmentSample(Async)");
        }
        
        com.squareup.okhttp.Call call = getFulfillmentSampleCall(sampleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets a fulfillment sample
     * Accepted Roles: * All Access 
     * @param sampleId the sample if of type SampleIds (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFulfillmentSample(String sampleId) throws ApiException {
        ApiResponse<Response> resp = getFulfillmentSampleWithHttpInfo(sampleId);
        return resp.getData();
    }

    /**
     * Gets a fulfillment sample
     * Accepted Roles: * All Access 
     * @param sampleId the sample if of type SampleIds (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFulfillmentSampleWithHttpInfo(String sampleId) throws ApiException {
        com.squareup.okhttp.Call call = getFulfillmentSampleValidateBeforeCall(sampleId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a fulfillment sample (asynchronously)
     * Accepted Roles: * All Access 
     * @param sampleId the sample if of type SampleIds (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentSampleAsync(String sampleId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFulfillmentSampleValidateBeforeCall(sampleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFulfillmentSampleList
     * @param fulfillmentTargetClass the fulfillment Target Class (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentSampleListCall(String fulfillmentTargetClass, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/samplesList/{fulfillmentTargetClass}"
            .replaceAll("\\{" + "fulfillmentTargetClass" + "\\}", apiClient.escapeString(fulfillmentTargetClass.toString()));

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
    private com.squareup.okhttp.Call getFulfillmentSampleListValidateBeforeCall(String fulfillmentTargetClass, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillmentTargetClass' is set
        if (fulfillmentTargetClass == null) {
            throw new ApiException("Missing the required parameter 'fulfillmentTargetClass' when calling getFulfillmentSampleList(Async)");
        }
        
        com.squareup.okhttp.Call call = getFulfillmentSampleListCall(fulfillmentTargetClass, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets a fulfillment sample
     * Accepted Roles: * All Access 
     * @param fulfillmentTargetClass the fulfillment Target Class (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFulfillmentSampleList(String fulfillmentTargetClass) throws ApiException {
        ApiResponse<Response> resp = getFulfillmentSampleListWithHttpInfo(fulfillmentTargetClass);
        return resp.getData();
    }

    /**
     * Gets a fulfillment sample
     * Accepted Roles: * All Access 
     * @param fulfillmentTargetClass the fulfillment Target Class (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFulfillmentSampleListWithHttpInfo(String fulfillmentTargetClass) throws ApiException {
        com.squareup.okhttp.Call call = getFulfillmentSampleListValidateBeforeCall(fulfillmentTargetClass, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a fulfillment sample (asynchronously)
     * Accepted Roles: * All Access 
     * @param fulfillmentTargetClass the fulfillment Target Class (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentSampleListAsync(String fulfillmentTargetClass, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFulfillmentSampleListValidateBeforeCall(fulfillmentTargetClass, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFulfillmentSchema
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentSchemaCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/fulfillment/schema";

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
    private com.squareup.okhttp.Call getFulfillmentSchemaValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getFulfillmentSchemaCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the fulfillment items
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFulfillmentSchema() throws ApiException {
        ApiResponse<Response> resp = getFulfillmentSchemaWithHttpInfo();
        return resp.getData();
    }

    /**
     * Get the fulfillment items
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFulfillmentSchemaWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getFulfillmentSchemaValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the fulfillment items (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFulfillmentSchemaAsync(final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFulfillmentSchemaValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getIgnoreBulkItemsCount
     * @param body the fulfillment search node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getIgnoreBulkItemsCountCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/ignoreBulkItems/count";

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
    private com.squareup.okhttp.Call getIgnoreBulkItemsCountValidateBeforeCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getIgnoreBulkItemsCount(Async)");
        }
        
        com.squareup.okhttp.Call call = getIgnoreBulkItemsCountCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the count of fulfillment items that can be ignored.
     * This applies an additional filter in addition to whatever filter the user is already passing in.  note indexStart and size are ignored.    Here is the additional filter:    canManage &amp;&amp; item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.NOT_VERIFIED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.REFUSED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.VERIFICATION_TIMEOUT ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.ERROR ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.TIMED_OUT ||   (item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.PENDING &amp;&amp; item.changeType &#x3D;&#x3D;&#x3D; FulfillerProcess.EXTERNAL_WORKFLOW);&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getIgnoreBulkItemsCount(FulfillmentItemSearch body) throws ApiException {
        ApiResponse<Response> resp = getIgnoreBulkItemsCountWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Get the count of fulfillment items that can be ignored.
     * This applies an additional filter in addition to whatever filter the user is already passing in.  note indexStart and size are ignored.    Here is the additional filter:    canManage &amp;&amp; item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.NOT_VERIFIED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.REFUSED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.VERIFICATION_TIMEOUT ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.ERROR ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.TIMED_OUT ||   (item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.PENDING &amp;&amp; item.changeType &#x3D;&#x3D;&#x3D; FulfillerProcess.EXTERNAL_WORKFLOW);&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getIgnoreBulkItemsCountWithHttpInfo(FulfillmentItemSearch body) throws ApiException {
        com.squareup.okhttp.Call call = getIgnoreBulkItemsCountValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the count of fulfillment items that can be ignored. (asynchronously)
     * This applies an additional filter in addition to whatever filter the user is already passing in.  note indexStart and size are ignored.    Here is the additional filter:    canManage &amp;&amp; item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.NOT_VERIFIED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.REFUSED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.VERIFICATION_TIMEOUT ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.ERROR ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.TIMED_OUT ||   (item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.PENDING &amp;&amp; item.changeType &#x3D;&#x3D;&#x3D; FulfillerProcess.EXTERNAL_WORKFLOW);&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getIgnoreBulkItemsCountAsync(FulfillmentItemSearch body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getIgnoreBulkItemsCountValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRetryItemsCount
     * @param body the fulfillment search node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRetryItemsCountCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/retryitems/count";

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
    private com.squareup.okhttp.Call getRetryItemsCountValidateBeforeCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getRetryItemsCount(Async)");
        }
        
        com.squareup.okhttp.Call call = getRetryItemsCountCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns the count of fulfillment items that can be retried
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRetryItemsCount(FulfillmentItemSearch body) throws ApiException {
        ApiResponse<Response> resp = getRetryItemsCountWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Returns the count of fulfillment items that can be retried
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRetryItemsCountWithHttpInfo(FulfillmentItemSearch body) throws ApiException {
        com.squareup.okhttp.Call call = getRetryItemsCountValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns the count of fulfillment items that can be retried (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRetryItemsCountAsync(FulfillmentItemSearch body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRetryItemsCountValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getRetryItemsRolledUp
     * @param body the fulfillment search node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getRetryItemsRolledUpCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/retryitems";

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
    private com.squareup.okhttp.Call getRetryItemsRolledUpValidateBeforeCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getRetryItemsRolledUp(Async)");
        }
        
        com.squareup.okhttp.Call call = getRetryItemsRolledUpCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns the list of fulfillment items that can be retried
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getRetryItemsRolledUp(FulfillmentItemSearch body) throws ApiException {
        ApiResponse<Response> resp = getRetryItemsRolledUpWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Returns the list of fulfillment items that can be retried
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getRetryItemsRolledUpWithHttpInfo(FulfillmentItemSearch body) throws ApiException {
        com.squareup.okhttp.Call call = getRetryItemsRolledUpValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns the list of fulfillment items that can be retried (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getRetryItemsRolledUpAsync(FulfillmentItemSearch body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getRetryItemsRolledUpValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for ignoreBulkItems
     * @param body the fulfillment search node  (required)
     * @param comment user reason for change (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call ignoreBulkItemsCall(FulfillmentItemSearch body, String comment, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/ignoreBulkItems";

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
    private com.squareup.okhttp.Call ignoreBulkItemsValidateBeforeCall(FulfillmentItemSearch body, String comment, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling ignoreBulkItems(Async)");
        }
        
        com.squareup.okhttp.Call call = ignoreBulkItemsCall(body, comment, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the fulfillment items that can be ignored.
     * This allows the user to bulk ignore items based upon a query  where they don&#x27;t have to provide each item.    This applies an additional filter in addition to whatever filter the user is already passing in.  note indexStart and size are ignored.    Here is the additional filter:    NOTE: canManage is controlled by access to this menu item    canManage &amp;&amp; item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.NOT_VERIFIED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.REFUSED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.VERIFICATION_TIMEOUT ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.ERROR ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.TIMED_OUT ||   (item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.PENDING &amp;&amp; item.changeType &#x3D;&#x3D;&#x3D; FulfillerProcess.EXTERNAL_WORKFLOW);&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param comment user reason for change (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response ignoreBulkItems(FulfillmentItemSearch body, String comment) throws ApiException {
        ApiResponse<Response> resp = ignoreBulkItemsWithHttpInfo(body, comment);
        return resp.getData();
    }

    /**
     * Get the fulfillment items that can be ignored.
     * This allows the user to bulk ignore items based upon a query  where they don&#x27;t have to provide each item.    This applies an additional filter in addition to whatever filter the user is already passing in.  note indexStart and size are ignored.    Here is the additional filter:    NOTE: canManage is controlled by access to this menu item    canManage &amp;&amp; item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.NOT_VERIFIED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.REFUSED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.VERIFICATION_TIMEOUT ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.ERROR ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.TIMED_OUT ||   (item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.PENDING &amp;&amp; item.changeType &#x3D;&#x3D;&#x3D; FulfillerProcess.EXTERNAL_WORKFLOW);&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param comment user reason for change (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> ignoreBulkItemsWithHttpInfo(FulfillmentItemSearch body, String comment) throws ApiException {
        com.squareup.okhttp.Call call = ignoreBulkItemsValidateBeforeCall(body, comment, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the fulfillment items that can be ignored. (asynchronously)
     * This allows the user to bulk ignore items based upon a query  where they don&#x27;t have to provide each item.    This applies an additional filter in addition to whatever filter the user is already passing in.  note indexStart and size are ignored.    Here is the additional filter:    NOTE: canManage is controlled by access to this menu item    canManage &amp;&amp; item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.NOT_VERIFIED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.REFUSED ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.VERIFICATION_TIMEOUT ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.ERROR ||   item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.TIMED_OUT ||   (item.fulfillmentStatus &#x3D;&#x3D;&#x3D; ChangeRequestItemState.PENDING &amp;&amp; item.changeType &#x3D;&#x3D;&#x3D; FulfillerProcess.EXTERNAL_WORKFLOW);&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param comment user reason for change (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call ignoreBulkItemsAsync(FulfillmentItemSearch body, String comment, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = ignoreBulkItemsValidateBeforeCall(body, comment, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param body - The download request node (required)
     * @param sampleId sample id, see SampleIds enum (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(Download body, String sampleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/samples/{sampleId}/download"
            .replaceAll("\\{" + "sampleId" + "\\}", apiClient.escapeString(sampleId.toString()));

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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(Download body, String sampleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload(Async)");
        }
        // verify the required parameter 'sampleId' is set
        if (sampleId == null) {
            throw new ApiException("Missing the required parameter 'sampleId' when calling startDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownloadCall(body, sampleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start data source download for the specified id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - The download request node (required)
     * @param sampleId sample id, see SampleIds enum (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(Download body, String sampleId) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(body, sampleId);
        return resp.getData();
    }

    /**
     * Start data source download for the specified id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - The download request node (required)
     * @param sampleId sample id, see SampleIds enum (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(Download body, String sampleId) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, sampleId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start data source download for the specified id (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - The download request node (required)
     * @param sampleId sample id, see SampleIds enum (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(Download body, String sampleId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, sampleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateCatalogFulfillmentTargets
     * @param body the fulfillment catalog update info node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateCatalogFulfillmentTargetsCall(FulfillmentCatalogUpdateInfoNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/catalogUpdate";

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
    private com.squareup.okhttp.Call updateCatalogFulfillmentTargetsValidateBeforeCall(FulfillmentCatalogUpdateInfoNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateCatalogFulfillmentTargets(Async)");
        }
        
        com.squareup.okhttp.Call call = updateCatalogFulfillmentTargetsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the fulfillment item status
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment catalog update info node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateCatalogFulfillmentTargets(FulfillmentCatalogUpdateInfoNode body) throws ApiException {
        ApiResponse<Response> resp = updateCatalogFulfillmentTargetsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Update the fulfillment item status
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment catalog update info node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateCatalogFulfillmentTargetsWithHttpInfo(FulfillmentCatalogUpdateInfoNode body) throws ApiException {
        com.squareup.okhttp.Call call = updateCatalogFulfillmentTargetsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the fulfillment item status (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment catalog update info node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateCatalogFulfillmentTargetsAsync(FulfillmentCatalogUpdateInfoNode body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateCatalogFulfillmentTargetsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateFulfillmentItems
     * @param body the fulfillment list node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateFulfillmentItemsCall(FulfillmentList body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/fulfillment/status";

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
    private com.squareup.okhttp.Call updateFulfillmentItemsValidateBeforeCall(FulfillmentList body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateFulfillmentItems(Async)");
        }
        
        com.squareup.okhttp.Call call = updateFulfillmentItemsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the fulfillment item status
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment list node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateFulfillmentItems(FulfillmentList body) throws ApiException {
        ApiResponse<Response> resp = updateFulfillmentItemsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Update the fulfillment item status
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment list node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateFulfillmentItemsWithHttpInfo(FulfillmentList body) throws ApiException {
        com.squareup.okhttp.Call call = updateFulfillmentItemsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the fulfillment item status (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment list node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateFulfillmentItemsAsync(FulfillmentList body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateFulfillmentItemsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
