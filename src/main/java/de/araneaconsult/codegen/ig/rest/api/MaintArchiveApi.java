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


import de.araneaconsult.codegen.ig.rest.model.ArcDisableEnable;
import de.araneaconsult.codegen.ig.rest.model.Archival;
import de.araneaconsult.codegen.ig.rest.model.ArchivalTables;
import de.araneaconsult.codegen.ig.rest.model.Archivals;
import de.araneaconsult.codegen.ig.rest.model.ArchiveDest;
import de.araneaconsult.codegen.ig.rest.model.ArchiveDests;
import de.araneaconsult.codegen.ig.rest.model.ArchiveReaderList;
import de.araneaconsult.codegen.ig.rest.model.MaintState;
import de.araneaconsult.codegen.ig.rest.model.OpsTransList;
import de.araneaconsult.codegen.ig.rest.model.Response;
import de.araneaconsult.codegen.ig.rest.model.Status;
import de.araneaconsult.codegen.ig.rest.model.TestConnection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintArchiveApi {
    private ApiClient apiClient;

    public MaintArchiveApi() {
        this(Configuration.getDefaultApiClient());
    }

    public MaintArchiveApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for abortOpsTransactions
     * @param ID ID of the archival (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call abortOpsTransactionsCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/{ID}/transactions"
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
    private com.squareup.okhttp.Call abortOpsTransactionsValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling abortOpsTransactions(Async)");
        }
        
        com.squareup.okhttp.Call call = abortOpsTransactionsCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Abort OPS transactions that an archival is waiting on
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archival (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status abortOpsTransactions(Long ID) throws ApiException {
        ApiResponse<Status> resp = abortOpsTransactionsWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Abort OPS transactions that an archival is waiting on
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archival (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> abortOpsTransactionsWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = abortOpsTransactionsValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Abort OPS transactions that an archival is waiting on (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archival (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call abortOpsTransactionsAsync(Long ID, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = abortOpsTransactionsValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for cancelArchival
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cancelArchivalCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/running";

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
    private com.squareup.okhttp.Call cancelArchivalValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = cancelArchivalCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Cancel the running archival, if any.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status cancelArchival() throws ApiException {
        ApiResponse<Status> resp = cancelArchivalWithHttpInfo();
        return resp.getData();
    }

    /**
     * Cancel the running archival, if any.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> cancelArchivalWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = cancelArchivalValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Cancel the running archival, if any. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cancelArchivalAsync(final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = cancelArchivalValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createArchiveDestination
     * @param body Archive destination to create (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createArchiveDestinationCall(ArchiveDest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/maint/archive/archivedest/new";

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
    private com.squareup.okhttp.Call createArchiveDestinationValidateBeforeCall(ArchiveDest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createArchiveDestination(Async)");
        }
        
        com.squareup.okhttp.Call call = createArchiveDestinationCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create new archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body Archive destination to create (required)
     * @return ArchiveDest
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArchiveDest createArchiveDestination(ArchiveDest body) throws ApiException {
        ApiResponse<ArchiveDest> resp = createArchiveDestinationWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create new archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body Archive destination to create (required)
     * @return ApiResponse&lt;ArchiveDest&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArchiveDest> createArchiveDestinationWithHttpInfo(ArchiveDest body) throws ApiException {
        com.squareup.okhttp.Call call = createArchiveDestinationValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<ArchiveDest>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create new archive destination (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body Archive destination to create (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createArchiveDestinationAsync(ArchiveDest body, final ApiCallback<ArchiveDest> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createArchiveDestinationValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArchiveDest>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteArchivals
     * @param id List of archival ids to delete (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteArchivalsCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive";

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
    private com.squareup.okhttp.Call deleteArchivalsValidateBeforeCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteArchivalsCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete archivals.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param id List of archival ids to delete (optional)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteArchivals(List<Long> id) throws ApiException {
        ApiResponse<Status> resp = deleteArchivalsWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete archivals.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param id List of archival ids to delete (optional)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteArchivalsWithHttpInfo(List<Long> id) throws ApiException {
        com.squareup.okhttp.Call call = deleteArchivalsValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete archivals. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param id List of archival ids to delete (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteArchivalsAsync(List<Long> id, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteArchivalsValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteArchiveDestination
     * @param ID ID of the archive destination to delete (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteArchiveDestinationCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/archivedest/{ID}"
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
    private com.squareup.okhttp.Call deleteArchiveDestinationValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling deleteArchiveDestination(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteArchiveDestinationCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete an existing archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archive destination to delete (required)
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteArchiveDestination(Long ID) throws ApiException {
        ApiResponse<Status> resp = deleteArchiveDestinationWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Delete an existing archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archive destination to delete (required)
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteArchiveDestinationWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = deleteArchiveDestinationValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete an existing archive destination (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archive destination to delete (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteArchiveDestinationAsync(Long ID, final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteArchiveDestinationValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteArchiveReaders
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteArchiveReadersCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/readers";

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
    private com.squareup.okhttp.Call deleteArchiveReadersValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteArchiveReadersCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete archive readers.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteArchiveReaders() throws ApiException {
        ApiResponse<Status> resp = deleteArchiveReadersWithHttpInfo();
        return resp.getData();
    }

    /**
     * Delete archive readers.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteArchiveReadersWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = deleteArchiveReadersValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete archive readers. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteArchiveReadersAsync(final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteArchiveReadersValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteNextArchivalInfo
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteNextArchivalInfoCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/next";

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
    private com.squareup.okhttp.Call deleteNextArchivalInfoValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = deleteNextArchivalInfoCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the information about the next archival to be done.
     * Also cancels the process that may be retrieving  this information.&lt;br/&gt;Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status deleteNextArchivalInfo() throws ApiException {
        ApiResponse<Status> resp = deleteNextArchivalInfoWithHttpInfo();
        return resp.getData();
    }

    /**
     * Delete the information about the next archival to be done.
     * Also cancels the process that may be retrieving  this information.&lt;br/&gt;Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> deleteNextArchivalInfoWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = deleteNextArchivalInfoValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the information about the next archival to be done. (asynchronously)
     * Also cancels the process that may be retrieving  this information.&lt;br/&gt;Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteNextArchivalInfoAsync(final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteNextArchivalInfoValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for disableArchiving
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call disableArchivingCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/disable";

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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call disableArchivingValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = disableArchivingCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Disable Archiving.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return MaintState
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MaintState disableArchiving() throws ApiException {
        ApiResponse<MaintState> resp = disableArchivingWithHttpInfo();
        return resp.getData();
    }

    /**
     * Disable Archiving.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;MaintState&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MaintState> disableArchivingWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = disableArchivingValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<MaintState>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Disable Archiving. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call disableArchivingAsync(final ApiCallback<MaintState> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = disableArchivingValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MaintState>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for enableArchiving
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call enableArchivingCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/enable";

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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call enableArchivingValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = enableArchivingCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Enable Archiving.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return MaintState
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MaintState enableArchiving() throws ApiException {
        ApiResponse<MaintState> resp = enableArchivingWithHttpInfo();
        return resp.getData();
    }

    /**
     * Enable Archiving.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;MaintState&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MaintState> enableArchivingWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = enableArchivingValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<MaintState>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Enable Archiving. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call enableArchivingAsync(final ApiCallback<MaintState> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = enableArchivingValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MaintState>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getArchivalInfo
     * @param ID Archival ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getArchivalInfoCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/{ID}"
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
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getArchivalInfoValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getArchivalInfo(Async)");
        }
        
        com.squareup.okhttp.Call call = getArchivalInfoCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return information about archival, if any.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID Archival ID (required)
     * @return Archival
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Archival getArchivalInfo(Long ID) throws ApiException {
        ApiResponse<Archival> resp = getArchivalInfoWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Return information about archival, if any.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID Archival ID (required)
     * @return ApiResponse&lt;Archival&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Archival> getArchivalInfoWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getArchivalInfoValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<Archival>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return information about archival, if any. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID Archival ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getArchivalInfoAsync(Long ID, final ApiCallback<Archival> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getArchivalInfoValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Archival>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getArchivalTables
     * @param ID Archival ID (required)
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getArchivalTablesCall(Long ID, String q, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/{ID}/tables"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getArchivalTablesValidateBeforeCall(Long ID, String q, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getArchivalTables(Async)");
        }
        
        com.squareup.okhttp.Call call = getArchivalTablesCall(ID, q, size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of tables for an archival
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID Archival ID (required)
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ArchivalTables
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArchivalTables getArchivalTables(Long ID, String q, Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        ApiResponse<ArchivalTables> resp = getArchivalTablesWithHttpInfo(ID, q, size, sortBy, sortOrder, indexFrom);
        return resp.getData();
    }

    /**
     * Return list of tables for an archival
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID Archival ID (required)
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ApiResponse&lt;ArchivalTables&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArchivalTables> getArchivalTablesWithHttpInfo(Long ID, String q, Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getArchivalTablesValidateBeforeCall(ID, q, size, sortBy, sortOrder, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<ArchivalTables>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of tables for an archival (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID Archival ID (required)
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getArchivalTablesAsync(Long ID, String q, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ApiCallback<ArchivalTables> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getArchivalTablesValidateBeforeCall(ID, q, size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArchivalTables>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getArchivals
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param status List of statuses to filter on. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getArchivalsCall(Integer size, String sortBy, String sortOrder, Integer indexFrom, List<String> status, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (status != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "status", status));

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
    private com.squareup.okhttp.Call getArchivalsValidateBeforeCall(Integer size, String sortBy, String sortOrder, Integer indexFrom, List<String> status, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getArchivalsCall(size, sortBy, sortOrder, indexFrom, status, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of archivals
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param status List of statuses to filter on. (optional)
     * @return Archivals
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Archivals getArchivals(Integer size, String sortBy, String sortOrder, Integer indexFrom, List<String> status) throws ApiException {
        ApiResponse<Archivals> resp = getArchivalsWithHttpInfo(size, sortBy, sortOrder, indexFrom, status);
        return resp.getData();
    }

    /**
     * Return list of archivals
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param status List of statuses to filter on. (optional)
     * @return ApiResponse&lt;Archivals&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Archivals> getArchivalsWithHttpInfo(Integer size, String sortBy, String sortOrder, Integer indexFrom, List<String> status) throws ApiException {
        com.squareup.okhttp.Call call = getArchivalsValidateBeforeCall(size, sortBy, sortOrder, indexFrom, status, null, null);
        Type localVarReturnType = new TypeToken<Archivals>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of archivals (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param status List of statuses to filter on. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getArchivalsAsync(Integer size, String sortBy, String sortOrder, Integer indexFrom, List<String> status, final ApiCallback<Archivals> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getArchivalsValidateBeforeCall(size, sortBy, sortOrder, indexFrom, status, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Archivals>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getArchiveDestinations
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - The starting row index. 0 is the first. Can not be null. (optional, default to 0)
     * @param size - number of rows to return within the result set (aka page size). (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getArchiveDestinationsCall(String sortBy, String sortOrder, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/archivedest";

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
    private com.squareup.okhttp.Call getArchiveDestinationsValidateBeforeCall(String sortBy, String sortOrder, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getArchiveDestinationsCall(sortBy, sortOrder, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of archival destinations
     * Accepted Roles: * Customer Administrator * Maintenance Administrator * SaaS OPS Administrator 
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - The starting row index. 0 is the first. Can not be null. (optional, default to 0)
     * @param size - number of rows to return within the result set (aka page size). (optional, default to 0)
     * @return ArchiveDests
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArchiveDests getArchiveDestinations(String sortBy, String sortOrder, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<ArchiveDests> resp = getArchiveDestinationsWithHttpInfo(sortBy, sortOrder, indexFrom, size);
        return resp.getData();
    }

    /**
     * Return list of archival destinations
     * Accepted Roles: * Customer Administrator * Maintenance Administrator * SaaS OPS Administrator 
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - The starting row index. 0 is the first. Can not be null. (optional, default to 0)
     * @param size - number of rows to return within the result set (aka page size). (optional, default to 0)
     * @return ApiResponse&lt;ArchiveDests&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArchiveDests> getArchiveDestinationsWithHttpInfo(String sortBy, String sortOrder, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getArchiveDestinationsValidateBeforeCall(sortBy, sortOrder, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<ArchiveDests>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of archival destinations (asynchronously)
     * Accepted Roles: * Customer Administrator * Maintenance Administrator * SaaS OPS Administrator 
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - The starting row index. 0 is the first. Can not be null. (optional, default to 0)
     * @param size - number of rows to return within the result set (aka page size). (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getArchiveDestinationsAsync(String sortBy, String sortOrder, Integer indexFrom, Integer size, final ApiCallback<ArchiveDests> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getArchiveDestinationsValidateBeforeCall(sortBy, sortOrder, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArchiveDests>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getArchiveDisableEnableDetail
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getArchiveDisableEnableDetailCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/disable_enable/detail";

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
    private com.squareup.okhttp.Call getArchiveDisableEnableDetailValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getArchiveDisableEnableDetailCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return information about the current archive disable/enable state.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ArcDisableEnable
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArcDisableEnable getArchiveDisableEnableDetail() throws ApiException {
        ApiResponse<ArcDisableEnable> resp = getArchiveDisableEnableDetailWithHttpInfo();
        return resp.getData();
    }

    /**
     * Return information about the current archive disable/enable state.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;ArcDisableEnable&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArcDisableEnable> getArchiveDisableEnableDetailWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getArchiveDisableEnableDetailValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<ArcDisableEnable>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return information about the current archive disable/enable state. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getArchiveDisableEnableDetailAsync(final ApiCallback<ArcDisableEnable> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getArchiveDisableEnableDetailValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArcDisableEnable>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getArchiveReaders
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getArchiveReadersCall(Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/readers";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getArchiveReadersValidateBeforeCall(Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getArchiveReadersCall(size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of archive readers.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ArchiveReaderList
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArchiveReaderList getArchiveReaders(Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        ApiResponse<ArchiveReaderList> resp = getArchiveReadersWithHttpInfo(size, sortBy, sortOrder, indexFrom);
        return resp.getData();
    }

    /**
     * Get the list of archive readers.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ApiResponse&lt;ArchiveReaderList&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArchiveReaderList> getArchiveReadersWithHttpInfo(Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getArchiveReadersValidateBeforeCall(size, sortBy, sortOrder, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<ArchiveReaderList>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of archive readers. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getArchiveReadersAsync(Integer size, String sortBy, String sortOrder, Integer indexFrom, final ApiCallback<ArchiveReaderList> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getArchiveReadersValidateBeforeCall(size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArchiveReaderList>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getNextArchivalInfo
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getNextArchivalInfoCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/next";

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
    private com.squareup.okhttp.Call getNextArchivalInfoValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getNextArchivalInfoCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return information about the next archival to be done.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return Archival
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Archival getNextArchivalInfo() throws ApiException {
        ApiResponse<Archival> resp = getNextArchivalInfoWithHttpInfo();
        return resp.getData();
    }

    /**
     * Return information about the next archival to be done.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;Archival&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Archival> getNextArchivalInfoWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getNextArchivalInfoValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Archival>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return information about the next archival to be done. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getNextArchivalInfoAsync(final ApiCallback<Archival> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getNextArchivalInfoValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Archival>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getNextArchivalInfoTables
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getNextArchivalInfoTablesCall(String q, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/next/tables";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getNextArchivalInfoTablesValidateBeforeCall(String q, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getNextArchivalInfoTablesCall(q, size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of tables for the next archival
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ArchivalTables
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArchivalTables getNextArchivalInfoTables(String q, Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        ApiResponse<ArchivalTables> resp = getNextArchivalInfoTablesWithHttpInfo(q, size, sortBy, sortOrder, indexFrom);
        return resp.getData();
    }

    /**
     * Return list of tables for the next archival
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ApiResponse&lt;ArchivalTables&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArchivalTables> getNextArchivalInfoTablesWithHttpInfo(String q, Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getNextArchivalInfoTablesValidateBeforeCall(q, size, sortBy, sortOrder, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<ArchivalTables>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of tables for the next archival (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param q String to filter results with (optional)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getNextArchivalInfoTablesAsync(String q, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ApiCallback<ArchivalTables> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getNextArchivalInfoTablesValidateBeforeCall(q, size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArchivalTables>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getOpsTransactions
     * @param ID ID of the archival (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getOpsTransactionsCall(Long ID, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/{ID}/transactions"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call getOpsTransactionsValidateBeforeCall(Long ID, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getOpsTransactions(Async)");
        }
        
        com.squareup.okhttp.Call call = getOpsTransactionsCall(ID, size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of OPS transactions an archival is waiting on.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archival (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return OpsTransList
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public OpsTransList getOpsTransactions(Long ID, Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        ApiResponse<OpsTransList> resp = getOpsTransactionsWithHttpInfo(ID, size, sortBy, sortOrder, indexFrom);
        return resp.getData();
    }

    /**
     * Get the list of OPS transactions an archival is waiting on.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archival (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @return ApiResponse&lt;OpsTransList&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<OpsTransList> getOpsTransactionsWithHttpInfo(Long ID, Integer size, String sortBy, String sortOrder, Integer indexFrom) throws ApiException {
        com.squareup.okhttp.Call call = getOpsTransactionsValidateBeforeCall(ID, size, sortBy, sortOrder, indexFrom, null, null);
        Type localVarReturnType = new TypeToken<OpsTransList>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of OPS transactions an archival is waiting on. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archival (required)
     * @param size Number of results to return, used for paging. (optional, default to 0)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getOpsTransactionsAsync(Long ID, Integer size, String sortBy, String sortOrder, Integer indexFrom, final ApiCallback<OpsTransList> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getOpsTransactionsValidateBeforeCall(ID, size, sortBy, sortOrder, indexFrom, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<OpsTransList>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getrchiveDestination
     * @param ID ID of the archive destination to retrieve (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getrchiveDestinationCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/archivedest/{ID}"
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
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getrchiveDestinationValidateBeforeCall(Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getrchiveDestination(Async)");
        }
        
        com.squareup.okhttp.Call call = getrchiveDestinationCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Retrieve an existing archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archive destination to retrieve (required)
     * @return ArchiveDest
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArchiveDest getrchiveDestination(Long ID) throws ApiException {
        ApiResponse<ArchiveDest> resp = getrchiveDestinationWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Retrieve an existing archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archive destination to retrieve (required)
     * @return ApiResponse&lt;ArchiveDest&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArchiveDest> getrchiveDestinationWithHttpInfo(Long ID) throws ApiException {
        com.squareup.okhttp.Call call = getrchiveDestinationValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<ArchiveDest>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retrieve an existing archive destination (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param ID ID of the archive destination to retrieve (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getrchiveDestinationAsync(Long ID, final ApiCallback<ArchiveDest> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getrchiveDestinationValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArchiveDest>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for modifyArchiveDestination
     * @param body Archive destination to update (required)
     * @param ID ID of the archive destination to update (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call modifyArchiveDestinationCall(ArchiveDest body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/maint/archive/archivedest/{ID}"
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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call modifyArchiveDestinationValidateBeforeCall(ArchiveDest body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling modifyArchiveDestination(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling modifyArchiveDestination(Async)");
        }
        
        com.squareup.okhttp.Call call = modifyArchiveDestinationCall(body, ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update an existing archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body Archive destination to update (required)
     * @param ID ID of the archive destination to update (required)
     * @return ArchiveDest
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArchiveDest modifyArchiveDestination(ArchiveDest body, Long ID) throws ApiException {
        ApiResponse<ArchiveDest> resp = modifyArchiveDestinationWithHttpInfo(body, ID);
        return resp.getData();
    }

    /**
     * Update an existing archive destination
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body Archive destination to update (required)
     * @param ID ID of the archive destination to update (required)
     * @return ApiResponse&lt;ArchiveDest&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArchiveDest> modifyArchiveDestinationWithHttpInfo(ArchiveDest body, Long ID) throws ApiException {
        com.squareup.okhttp.Call call = modifyArchiveDestinationValidateBeforeCall(body, ID, null, null);
        Type localVarReturnType = new TypeToken<ArchiveDest>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update an existing archive destination (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body Archive destination to update (required)
     * @param ID ID of the archive destination to update (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call modifyArchiveDestinationAsync(ArchiveDest body, Long ID, final ApiCallback<ArchiveDest> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = modifyArchiveDestinationValidateBeforeCall(body, ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArchiveDest>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startNextArchivalInfo
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startNextArchivalInfoCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/maint/archive/next";

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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call startNextArchivalInfoValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startNextArchivalInfoCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start thread to get information about the next archival to be done.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return Status
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Status startNextArchivalInfo() throws ApiException {
        ApiResponse<Status> resp = startNextArchivalInfoWithHttpInfo();
        return resp.getData();
    }

    /**
     * Start thread to get information about the next archival to be done.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @return ApiResponse&lt;Status&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Status> startNextArchivalInfoWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = startNextArchivalInfoValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start thread to get information about the next archival to be done. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startNextArchivalInfoAsync(final ApiCallback<Status> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startNextArchivalInfoValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Status>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for testConnection
     * @param body The archive destination to test connection for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call testConnectionCall(ArchiveDest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/maint/archive/archivedest/test";

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
    private com.squareup.okhttp.Call testConnectionValidateBeforeCall(ArchiveDest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling testConnection(Async)");
        }
        
        com.squareup.okhttp.Call call = testConnectionCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Test archive destination connection.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body The archive destination to test connection for (required)
     * @return TestConnection
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TestConnection testConnection(ArchiveDest body) throws ApiException {
        ApiResponse<TestConnection> resp = testConnectionWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Test archive destination connection.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body The archive destination to test connection for (required)
     * @return ApiResponse&lt;TestConnection&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TestConnection> testConnectionWithHttpInfo(ArchiveDest body) throws ApiException {
        com.squareup.okhttp.Call call = testConnectionValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<TestConnection>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Test archive destination connection. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body The archive destination to test connection for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call testConnectionAsync(ArchiveDest body, final ApiCallback<TestConnection> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = testConnectionValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TestConnection>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for testConnection_0
     * @param body The updates to the archive destination that we should test. (required)
     * @param ID ID of archive destination to test connection for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call testConnection_0Call(ArchiveDest body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/maint/archive/archivedest/test/{ID}"
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
    private com.squareup.okhttp.Call testConnection_0ValidateBeforeCall(ArchiveDest body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling testConnection_0(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling testConnection_0(Async)");
        }
        
        com.squareup.okhttp.Call call = testConnection_0Call(body, ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Test archive destination connection.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body The updates to the archive destination that we should test. (required)
     * @param ID ID of archive destination to test connection for (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response testConnection_0(ArchiveDest body, Long ID) throws ApiException {
        ApiResponse<Response> resp = testConnection_0WithHttpInfo(body, ID);
        return resp.getData();
    }

    /**
     * Test archive destination connection.
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body The updates to the archive destination that we should test. (required)
     * @param ID ID of archive destination to test connection for (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> testConnection_0WithHttpInfo(ArchiveDest body, Long ID) throws ApiException {
        com.squareup.okhttp.Call call = testConnection_0ValidateBeforeCall(body, ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Test archive destination connection. (asynchronously)
     * Accepted Roles: * Maintenance Administrator * SaaS OPS Administrator 
     * @param body The updates to the archive destination that we should test. (required)
     * @param ID ID of archive destination to test connection for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call testConnection_0Async(ArchiveDest body, Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = testConnection_0ValidateBeforeCall(body, ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
