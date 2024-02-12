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
import java.io.File;
import de.araneaconsult.codegen.ig.rest.model.FormsetMapping;
import de.araneaconsult.codegen.ig.rest.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestFormsApi {
    private ApiClient apiClient;

    public RequestFormsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public RequestFormsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for associateFormset
     * @param body the instructions on how to map (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call associateFormsetCall(FormsetMapping body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/requestForms/formsetAssociation";

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
    private com.squareup.okhttp.Call associateFormsetValidateBeforeCall(FormsetMapping body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling associateFormset(Async)");
        }
        
        com.squareup.okhttp.Call call = associateFormsetCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * adds a formset to application/permission mapping
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the instructions on how to map (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response associateFormset(FormsetMapping body) throws ApiException {
        ApiResponse<Response> resp = associateFormsetWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * adds a formset to application/permission mapping
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the instructions on how to map (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> associateFormsetWithHttpInfo(FormsetMapping body) throws ApiException {
        com.squareup.okhttp.Call call = associateFormsetValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * adds a formset to application/permission mapping (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the instructions on how to map (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call associateFormsetAsync(FormsetMapping body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = associateFormsetValidateBeforeCall(body, progressListener, progressRequestListener);
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
        String localVarPath = "/requestForms/forms/download/{id}"
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
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
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
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
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
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
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
     * Build call for cloneFormset
     * @param formsetId the formset ID (required)
     * @param newFormsetName the new Formset Name name (required)
     * @param requestFormName the new Request Name name (optional)
     * @param approvalFormName the new approval Name name (optional)
     * @param applicationId an optional application id if you want to link this formset to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this formset to a Permission, mutually exclusive with                            applicationId (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cloneFormsetCall(Long formsetId, String newFormsetName, String requestFormName, String approvalFormName, Long applicationId, String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/formset/{formsetId}/copyFrom/{newFormsetName}"
            .replaceAll("\\{" + "formsetId" + "\\}", apiClient.escapeString(formsetId.toString()))
            .replaceAll("\\{" + "newFormsetName" + "\\}", apiClient.escapeString(newFormsetName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (requestFormName != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requestFormName", requestFormName));
        if (approvalFormName != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approvalFormName", approvalFormName));
        if (applicationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("applicationId", applicationId));
        if (uniquePermissionId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniquePermissionId", uniquePermissionId));

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
    private com.squareup.okhttp.Call cloneFormsetValidateBeforeCall(Long formsetId, String newFormsetName, String requestFormName, String approvalFormName, Long applicationId, String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formsetId' is set
        if (formsetId == null) {
            throw new ApiException("Missing the required parameter 'formsetId' when calling cloneFormset(Async)");
        }
        // verify the required parameter 'newFormsetName' is set
        if (newFormsetName == null) {
            throw new ApiException("Missing the required parameter 'newFormsetName' when calling cloneFormset(Async)");
        }
        
        com.squareup.okhttp.Call call = cloneFormsetCall(formsetId, newFormsetName, requestFormName, approvalFormName, applicationId, uniquePermissionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Copies a formset
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetId the formset ID (required)
     * @param newFormsetName the new Formset Name name (required)
     * @param requestFormName the new Request Name name (optional)
     * @param approvalFormName the new approval Name name (optional)
     * @param applicationId an optional application id if you want to link this formset to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this formset to a Permission, mutually exclusive with                            applicationId (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response cloneFormset(Long formsetId, String newFormsetName, String requestFormName, String approvalFormName, Long applicationId, String uniquePermissionId) throws ApiException {
        ApiResponse<Response> resp = cloneFormsetWithHttpInfo(formsetId, newFormsetName, requestFormName, approvalFormName, applicationId, uniquePermissionId);
        return resp.getData();
    }

    /**
     * Copies a formset
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetId the formset ID (required)
     * @param newFormsetName the new Formset Name name (required)
     * @param requestFormName the new Request Name name (optional)
     * @param approvalFormName the new approval Name name (optional)
     * @param applicationId an optional application id if you want to link this formset to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this formset to a Permission, mutually exclusive with                            applicationId (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> cloneFormsetWithHttpInfo(Long formsetId, String newFormsetName, String requestFormName, String approvalFormName, Long applicationId, String uniquePermissionId) throws ApiException {
        com.squareup.okhttp.Call call = cloneFormsetValidateBeforeCall(formsetId, newFormsetName, requestFormName, approvalFormName, applicationId, uniquePermissionId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Copies a formset (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetId the formset ID (required)
     * @param newFormsetName the new Formset Name name (required)
     * @param requestFormName the new Request Name name (optional)
     * @param approvalFormName the new approval Name name (optional)
     * @param applicationId an optional application id if you want to link this formset to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this formset to a Permission, mutually exclusive with                            applicationId (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cloneFormsetAsync(Long formsetId, String newFormsetName, String requestFormName, String approvalFormName, Long applicationId, String uniquePermissionId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = cloneFormsetValidateBeforeCall(formsetId, newFormsetName, requestFormName, approvalFormName, applicationId, uniquePermissionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createFormset
     * @param body the formset json string (required)
     * @param formsetName the form name (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createFormsetCall(String body, String formsetName, Long applicationId, String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/requestForms/formset/{formsetName}"
            .replaceAll("\\{" + "formsetName" + "\\}", apiClient.escapeString(formsetName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (applicationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("applicationId", applicationId));
        if (uniquePermissionId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniquePermissionId", uniquePermissionId));

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
    private com.squareup.okhttp.Call createFormsetValidateBeforeCall(String body, String formsetName, Long applicationId, String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createFormset(Async)");
        }
        // verify the required parameter 'formsetName' is set
        if (formsetName == null) {
            throw new ApiException("Missing the required parameter 'formsetName' when calling createFormset(Async)");
        }
        
        com.squareup.okhttp.Call call = createFormsetCall(body, formsetName, applicationId, uniquePermissionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Saves a formset, optionally links it to an application or permission
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the formset json string (required)
     * @param formsetName the form name (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response createFormset(String body, String formsetName, Long applicationId, String uniquePermissionId) throws ApiException {
        ApiResponse<Response> resp = createFormsetWithHttpInfo(body, formsetName, applicationId, uniquePermissionId);
        return resp.getData();
    }

    /**
     * Saves a formset, optionally links it to an application or permission
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the formset json string (required)
     * @param formsetName the form name (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> createFormsetWithHttpInfo(String body, String formsetName, Long applicationId, String uniquePermissionId) throws ApiException {
        com.squareup.okhttp.Call call = createFormsetValidateBeforeCall(body, formsetName, applicationId, uniquePermissionId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Saves a formset, optionally links it to an application or permission (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the formset json string (required)
     * @param formsetName the form name (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createFormsetAsync(String body, String formsetName, Long applicationId, String uniquePermissionId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createFormsetValidateBeforeCall(body, formsetName, applicationId, uniquePermissionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteForm
     * @param formId the form ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteFormCall(Long formId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/{formId}"
            .replaceAll("\\{" + "formId" + "\\}", apiClient.escapeString(formId.toString()));

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
    private com.squareup.okhttp.Call deleteFormValidateBeforeCall(Long formId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formId' is set
        if (formId == null) {
            throw new ApiException("Missing the required parameter 'formId' when calling deleteForm(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteFormCall(formId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Deletes a form
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formId the form ID (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response deleteForm(Long formId) throws ApiException {
        ApiResponse<Response> resp = deleteFormWithHttpInfo(formId);
        return resp.getData();
    }

    /**
     * Deletes a form
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formId the form ID (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> deleteFormWithHttpInfo(Long formId) throws ApiException {
        com.squareup.okhttp.Call call = deleteFormValidateBeforeCall(formId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a form (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formId the form ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteFormAsync(Long formId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteFormValidateBeforeCall(formId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for finishDownload
     * @param id The download id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call finishDownloadCall(String id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/download/{id}"
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
     * Get the download file.
     * Accepted Roles: * All Access 
     * @param id The download id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response finishDownload(String id) throws ApiException {
        ApiResponse<Response> resp = finishDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the download file.
     * Accepted Roles: * All Access 
     * @param id The download id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> finishDownloadWithHttpInfo(String id) throws ApiException {
        com.squareup.okhttp.Call call = finishDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the download file. (asynchronously)
     * Accepted Roles: * All Access 
     * @param id The download id (required)
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
     * Build call for getAppContext
     * @param applicationId the application ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAppContextCall(Long applicationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/applicationContext/{applicationId}"
            .replaceAll("\\{" + "applicationId" + "\\}", apiClient.escapeString(applicationId.toString()));

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
    private com.squareup.okhttp.Call getAppContextValidateBeforeCall(Long applicationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'applicationId' is set
        if (applicationId == null) {
            throw new ApiException("Missing the required parameter 'applicationId' when calling getAppContext(Async)");
        }
        
        com.squareup.okhttp.Call call = getAppContextCall(applicationId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets the form context for a given application, used to populate the form dialog
     * Accepted Roles: * All Access 
     * @param applicationId the application ID (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAppContext(Long applicationId) throws ApiException {
        ApiResponse<Response> resp = getAppContextWithHttpInfo(applicationId);
        return resp.getData();
    }

    /**
     * Gets the form context for a given application, used to populate the form dialog
     * Accepted Roles: * All Access 
     * @param applicationId the application ID (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAppContextWithHttpInfo(Long applicationId) throws ApiException {
        com.squareup.okhttp.Call call = getAppContextValidateBeforeCall(applicationId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets the form context for a given application, used to populate the form dialog (asynchronously)
     * Accepted Roles: * All Access 
     * @param applicationId the application ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAppContextAsync(Long applicationId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAppContextValidateBeforeCall(applicationId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAppForm
     * @param applicationId the application ID (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning the                       first form, and other metadata (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAppFormCall(Long applicationId, String formType, Boolean publishedForm, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/application/{applicationId}/{formType}"
            .replaceAll("\\{" + "applicationId" + "\\}", apiClient.escapeString(applicationId.toString()))
            .replaceAll("\\{" + "formType" + "\\}", apiClient.escapeString(formType.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (publishedForm != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("publishedForm", publishedForm));

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
    private com.squareup.okhttp.Call getAppFormValidateBeforeCall(Long applicationId, String formType, Boolean publishedForm, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'applicationId' is set
        if (applicationId == null) {
            throw new ApiException("Missing the required parameter 'applicationId' when calling getAppForm(Async)");
        }
        // verify the required parameter 'formType' is set
        if (formType == null) {
            throw new ApiException("Missing the required parameter 'formType' when calling getAppForm(Async)");
        }
        
        com.squareup.okhttp.Call call = getAppFormCall(applicationId, formType, publishedForm, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets a form for a particular application/formType
     * Accepted Roles: * All Access 
     * @param applicationId the application ID (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning the                       first form, and other metadata (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAppForm(Long applicationId, String formType, Boolean publishedForm) throws ApiException {
        ApiResponse<Response> resp = getAppFormWithHttpInfo(applicationId, formType, publishedForm);
        return resp.getData();
    }

    /**
     * Gets a form for a particular application/formType
     * Accepted Roles: * All Access 
     * @param applicationId the application ID (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning the                       first form, and other metadata (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAppFormWithHttpInfo(Long applicationId, String formType, Boolean publishedForm) throws ApiException {
        com.squareup.okhttp.Call call = getAppFormValidateBeforeCall(applicationId, formType, publishedForm, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a form for a particular application/formType (asynchronously)
     * Accepted Roles: * All Access 
     * @param applicationId the application ID (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning the                       first form, and other metadata (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAppFormAsync(Long applicationId, String formType, Boolean publishedForm, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAppFormValidateBeforeCall(applicationId, formType, publishedForm, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getDefaultForm
     * @param formEntityType the form Entity Type (required)
     * @param formType the formType, should be one of REQUEST, APPROVAL (required)
     * @param publishedFormOnly true if you wish to retrieve the published version of the form only, else return the system default                           if this is not set, it will return the draft form, if any (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getDefaultFormCall(String formEntityType, String formType, Boolean publishedFormOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/default/{formEntityType}/{formType}"
            .replaceAll("\\{" + "formEntityType" + "\\}", apiClient.escapeString(formEntityType.toString()))
            .replaceAll("\\{" + "formType" + "\\}", apiClient.escapeString(formType.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (publishedFormOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("publishedFormOnly", publishedFormOnly));

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
    private com.squareup.okhttp.Call getDefaultFormValidateBeforeCall(String formEntityType, String formType, Boolean publishedFormOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formEntityType' is set
        if (formEntityType == null) {
            throw new ApiException("Missing the required parameter 'formEntityType' when calling getDefaultForm(Async)");
        }
        // verify the required parameter 'formType' is set
        if (formType == null) {
            throw new ApiException("Missing the required parameter 'formType' when calling getDefaultForm(Async)");
        }
        
        com.squareup.okhttp.Call call = getDefaultFormCall(formEntityType, formType, publishedFormOnly, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets the default form associated with an form entity type/form type
     * Accepted Roles: * All Access 
     * @param formEntityType the form Entity Type (required)
     * @param formType the formType, should be one of REQUEST, APPROVAL (required)
     * @param publishedFormOnly true if you wish to retrieve the published version of the form only, else return the system default                           if this is not set, it will return the draft form, if any (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getDefaultForm(String formEntityType, String formType, Boolean publishedFormOnly) throws ApiException {
        ApiResponse<Response> resp = getDefaultFormWithHttpInfo(formEntityType, formType, publishedFormOnly);
        return resp.getData();
    }

    /**
     * Gets the default form associated with an form entity type/form type
     * Accepted Roles: * All Access 
     * @param formEntityType the form Entity Type (required)
     * @param formType the formType, should be one of REQUEST, APPROVAL (required)
     * @param publishedFormOnly true if you wish to retrieve the published version of the form only, else return the system default                           if this is not set, it will return the draft form, if any (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getDefaultFormWithHttpInfo(String formEntityType, String formType, Boolean publishedFormOnly) throws ApiException {
        com.squareup.okhttp.Call call = getDefaultFormValidateBeforeCall(formEntityType, formType, publishedFormOnly, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets the default form associated with an form entity type/form type (asynchronously)
     * Accepted Roles: * All Access 
     * @param formEntityType the form Entity Type (required)
     * @param formType the formType, should be one of REQUEST, APPROVAL (required)
     * @param publishedFormOnly true if you wish to retrieve the published version of the form only, else return the system default                           if this is not set, it will return the draft form, if any (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getDefaultFormAsync(String formEntityType, String formType, Boolean publishedFormOnly, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getDefaultFormValidateBeforeCall(formEntityType, formType, publishedFormOnly, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getForm
     * @param formId the form ID (required)
     * @param returnContext true if you wish to include the request context in the result set (optional, default to false)
     * @param requestItemId the request item id, required if return context is true. Should be a unique permission if (if permission form) or app id                       (if application form (optional)
     * @param formOrder the form order, required if return context is true. Use 0 if request, otherwise 1, 2 etc for approval 1, approval 2, etc (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFormCall(Long formId, Boolean returnContext, String requestItemId, Integer formOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/id/{formId}"
            .replaceAll("\\{" + "formId" + "\\}", apiClient.escapeString(formId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (returnContext != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("returnContext", returnContext));
        if (requestItemId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requestItemId", requestItemId));
        if (formOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("formOrder", formOrder));

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
    private com.squareup.okhttp.Call getFormValidateBeforeCall(Long formId, Boolean returnContext, String requestItemId, Integer formOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formId' is set
        if (formId == null) {
            throw new ApiException("Missing the required parameter 'formId' when calling getForm(Async)");
        }
        
        com.squareup.okhttp.Call call = getFormCall(formId, returnContext, requestItemId, formOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets a form, optionally adds the request context
     * Accepted Roles: * All Access 
     * @param formId the form ID (required)
     * @param returnContext true if you wish to include the request context in the result set (optional, default to false)
     * @param requestItemId the request item id, required if return context is true. Should be a unique permission if (if permission form) or app id                       (if application form (optional)
     * @param formOrder the form order, required if return context is true. Use 0 if request, otherwise 1, 2 etc for approval 1, approval 2, etc (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getForm(Long formId, Boolean returnContext, String requestItemId, Integer formOrder) throws ApiException {
        ApiResponse<Response> resp = getFormWithHttpInfo(formId, returnContext, requestItemId, formOrder);
        return resp.getData();
    }

    /**
     * Gets a form, optionally adds the request context
     * Accepted Roles: * All Access 
     * @param formId the form ID (required)
     * @param returnContext true if you wish to include the request context in the result set (optional, default to false)
     * @param requestItemId the request item id, required if return context is true. Should be a unique permission if (if permission form) or app id                       (if application form (optional)
     * @param formOrder the form order, required if return context is true. Use 0 if request, otherwise 1, 2 etc for approval 1, approval 2, etc (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFormWithHttpInfo(Long formId, Boolean returnContext, String requestItemId, Integer formOrder) throws ApiException {
        com.squareup.okhttp.Call call = getFormValidateBeforeCall(formId, returnContext, requestItemId, formOrder, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a form, optionally adds the request context (asynchronously)
     * Accepted Roles: * All Access 
     * @param formId the form ID (required)
     * @param returnContext true if you wish to include the request context in the result set (optional, default to false)
     * @param requestItemId the request item id, required if return context is true. Should be a unique permission if (if permission form) or app id                       (if application form (optional)
     * @param formOrder the form order, required if return context is true. Use 0 if request, otherwise 1, 2 etc for approval 1, approval 2, etc (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFormAsync(Long formId, Boolean returnContext, String requestItemId, Integer formOrder, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFormValidateBeforeCall(formId, returnContext, requestItemId, formOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFormset
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with others (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with others (optional)
     * @param defaultEntityType an optional default entity type, if you want to find the default forms for either a PERMISSION or APPLICATION, mutually exclusive with others (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFormsetCall(Long applicationId, String uniquePermissionId, String defaultEntityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/formset";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (applicationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("applicationId", applicationId));
        if (uniquePermissionId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniquePermissionId", uniquePermissionId));
        if (defaultEntityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("defaultEntityType", defaultEntityType));

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
    private com.squareup.okhttp.Call getFormsetValidateBeforeCall(Long applicationId, String uniquePermissionId, String defaultEntityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getFormsetCall(applicationId, uniquePermissionId, defaultEntityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets formset linked it to an application or permission
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with others (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with others (optional)
     * @param defaultEntityType an optional default entity type, if you want to find the default forms for either a PERMISSION or APPLICATION, mutually exclusive with others (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFormset(Long applicationId, String uniquePermissionId, String defaultEntityType) throws ApiException {
        ApiResponse<Response> resp = getFormsetWithHttpInfo(applicationId, uniquePermissionId, defaultEntityType);
        return resp.getData();
    }

    /**
     * Gets formset linked it to an application or permission
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with others (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with others (optional)
     * @param defaultEntityType an optional default entity type, if you want to find the default forms for either a PERMISSION or APPLICATION, mutually exclusive with others (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFormsetWithHttpInfo(Long applicationId, String uniquePermissionId, String defaultEntityType) throws ApiException {
        com.squareup.okhttp.Call call = getFormsetValidateBeforeCall(applicationId, uniquePermissionId, defaultEntityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets formset linked it to an application or permission (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with others (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with others (optional)
     * @param defaultEntityType an optional default entity type, if you want to find the default forms for either a PERMISSION or APPLICATION, mutually exclusive with others (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFormsetAsync(Long applicationId, String uniquePermissionId, String defaultEntityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFormsetValidateBeforeCall(applicationId, uniquePermissionId, defaultEntityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getImage
     * @param entityType the form entity type (required)
     * @param entityId the entity id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getImageCall(String entityType, String entityId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/customIcon/{entityType}/{entityId}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()))
            .replaceAll("\\{" + "entityId" + "\\}", apiClient.escapeString(entityId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "image/_*"
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
    private com.squareup.okhttp.Call getImageValidateBeforeCall(String entityType, String entityId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling getImage(Async)");
        }
        // verify the required parameter 'entityId' is set
        if (entityId == null) {
            throw new ApiException("Missing the required parameter 'entityId' when calling getImage(Async)");
        }
        
        com.squareup.okhttp.Call call = getImageCall(entityType, entityId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets form image
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param entityType the form entity type (required)
     * @param entityId the entity id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getImage(String entityType, String entityId) throws ApiException {
        ApiResponse<Response> resp = getImageWithHttpInfo(entityType, entityId);
        return resp.getData();
    }

    /**
     * Gets form image
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param entityType the form entity type (required)
     * @param entityId the entity id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getImageWithHttpInfo(String entityType, String entityId) throws ApiException {
        com.squareup.okhttp.Call call = getImageValidateBeforeCall(entityType, entityId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets form image (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param entityType the form entity type (required)
     * @param entityId the entity id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getImageAsync(String entityType, String entityId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getImageValidateBeforeCall(entityType, entityId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPermContext
     * @param uniquePermissionId the uniquePermissionId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPermContextCall(String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/permissionContext/{uniquePermissionId}"
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()));

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
    private com.squareup.okhttp.Call getPermContextValidateBeforeCall(String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getPermContext(Async)");
        }
        
        com.squareup.okhttp.Call call = getPermContextCall(uniquePermissionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets permissionContext for a permission for use in form population
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the uniquePermissionId (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPermContext(String uniquePermissionId) throws ApiException {
        ApiResponse<Response> resp = getPermContextWithHttpInfo(uniquePermissionId);
        return resp.getData();
    }

    /**
     * Gets permissionContext for a permission for use in form population
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the uniquePermissionId (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPermContextWithHttpInfo(String uniquePermissionId) throws ApiException {
        com.squareup.okhttp.Call call = getPermContextValidateBeforeCall(uniquePermissionId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets permissionContext for a permission for use in form population (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the uniquePermissionId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPermContextAsync(String uniquePermissionId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPermContextValidateBeforeCall(uniquePermissionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPermForm
     * @param uniquePermissionId the uniquePermissionId (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning                            the first form, and other metadata (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPermFormCall(String uniquePermissionId, String formType, Boolean publishedForm, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/permission/{uniquePermissionId}/{formType}"
            .replaceAll("\\{" + "uniquePermissionId" + "\\}", apiClient.escapeString(uniquePermissionId.toString()))
            .replaceAll("\\{" + "formType" + "\\}", apiClient.escapeString(formType.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (publishedForm != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("publishedForm", publishedForm));

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
    private com.squareup.okhttp.Call getPermFormValidateBeforeCall(String uniquePermissionId, String formType, Boolean publishedForm, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uniquePermissionId' is set
        if (uniquePermissionId == null) {
            throw new ApiException("Missing the required parameter 'uniquePermissionId' when calling getPermForm(Async)");
        }
        // verify the required parameter 'formType' is set
        if (formType == null) {
            throw new ApiException("Missing the required parameter 'formType' when calling getPermForm(Async)");
        }
        
        com.squareup.okhttp.Call call = getPermFormCall(uniquePermissionId, formType, publishedForm, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets a form associated with an permission/form type
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the uniquePermissionId (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning                            the first form, and other metadata (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPermForm(String uniquePermissionId, String formType, Boolean publishedForm) throws ApiException {
        ApiResponse<Response> resp = getPermFormWithHttpInfo(uniquePermissionId, formType, publishedForm);
        return resp.getData();
    }

    /**
     * Gets a form associated with an permission/form type
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the uniquePermissionId (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning                            the first form, and other metadata (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPermFormWithHttpInfo(String uniquePermissionId, String formType, Boolean publishedForm) throws ApiException {
        com.squareup.okhttp.Call call = getPermFormValidateBeforeCall(uniquePermissionId, formType, publishedForm, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a form associated with an permission/form type (asynchronously)
     * Accepted Roles: * All Access 
     * @param uniquePermissionId the uniquePermissionId (required)
     * @param formType the formType (required)
     * @param publishedForm true if you wish to retrieve the published version of the form note if this flag is set a minimal node is returning                            the first form, and other metadata (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPermFormAsync(String uniquePermissionId, String formType, Boolean publishedForm, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPermFormValidateBeforeCall(uniquePermissionId, formType, publishedForm, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importForms
     * @param body the import data (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importFormsCall(String body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/import";

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
    private com.squareup.okhttp.Call importFormsValidateBeforeCall(String body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling importForms(Async)");
        }
        
        com.squareup.okhttp.Call call = importFormsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the import data (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importForms(String body) throws ApiException {
        ApiResponse<Response> resp = importFormsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Perform an import
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the import data (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importFormsWithHttpInfo(String body) throws ApiException {
        com.squareup.okhttp.Call call = importFormsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the import data (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importFormsAsync(String body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importFormsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZip
     * @param uploadedInputStream  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/import/preview";

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
    private com.squareup.okhttp.Call importZipValidateBeforeCall(File uploadedInputStream, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZip(Async)");
        }
        
        com.squareup.okhttp.Call call = importZipCall(uploadedInputStream, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param uploadedInputStream  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip(File uploadedInputStream) throws ApiException {
        ApiResponse<Response> resp = importZipWithHttpInfo(uploadedInputStream);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param uploadedInputStream  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZipWithHttpInfo(File uploadedInputStream) throws ApiException {
        com.squareup.okhttp.Call call = importZipValidateBeforeCall(uploadedInputStream, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param uploadedInputStream  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZipAsync(File uploadedInputStream, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZipValidateBeforeCall(uploadedInputStream, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for isFormNameAvailable
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param formEntityType the form entity type (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call isFormNameAvailableCall(String formName, String formType, String formEntityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/nameOk/{formName}/{formType}/{formEntityType}"
            .replaceAll("\\{" + "formName" + "\\}", apiClient.escapeString(formName.toString()))
            .replaceAll("\\{" + "formType" + "\\}", apiClient.escapeString(formType.toString()))
            .replaceAll("\\{" + "formEntityType" + "\\}", apiClient.escapeString(formEntityType.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

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
    private com.squareup.okhttp.Call isFormNameAvailableValidateBeforeCall(String formName, String formType, String formEntityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formName' is set
        if (formName == null) {
            throw new ApiException("Missing the required parameter 'formName' when calling isFormNameAvailable(Async)");
        }
        // verify the required parameter 'formType' is set
        if (formType == null) {
            throw new ApiException("Missing the required parameter 'formType' when calling isFormNameAvailable(Async)");
        }
        // verify the required parameter 'formEntityType' is set
        if (formEntityType == null) {
            throw new ApiException("Missing the required parameter 'formEntityType' when calling isFormNameAvailable(Async)");
        }
        
        com.squareup.okhttp.Call call = isFormNameAvailableCall(formName, formType, formEntityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns node indicating if proposed form name is available
     * Accepted Roles: * All Access 
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param formEntityType the form entity type (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response isFormNameAvailable(String formName, String formType, String formEntityType) throws ApiException {
        ApiResponse<Response> resp = isFormNameAvailableWithHttpInfo(formName, formType, formEntityType);
        return resp.getData();
    }

    /**
     * Returns node indicating if proposed form name is available
     * Accepted Roles: * All Access 
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param formEntityType the form entity type (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> isFormNameAvailableWithHttpInfo(String formName, String formType, String formEntityType) throws ApiException {
        com.squareup.okhttp.Call call = isFormNameAvailableValidateBeforeCall(formName, formType, formEntityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns node indicating if proposed form name is available (asynchronously)
     * Accepted Roles: * All Access 
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param formEntityType the form entity type (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call isFormNameAvailableAsync(String formName, String formType, String formEntityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = isFormNameAvailableValidateBeforeCall(formName, formType, formEntityType, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for isFormsetNameAvailable
     * @param formsetName the form set Name (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call isFormsetNameAvailableCall(String formsetName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/formset/nameOk/{formsetName}"
            .replaceAll("\\{" + "formsetName" + "\\}", apiClient.escapeString(formsetName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

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
    private com.squareup.okhttp.Call isFormsetNameAvailableValidateBeforeCall(String formsetName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formsetName' is set
        if (formsetName == null) {
            throw new ApiException("Missing the required parameter 'formsetName' when calling isFormsetNameAvailable(Async)");
        }
        
        com.squareup.okhttp.Call call = isFormsetNameAvailableCall(formsetName, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Returns node indicating if proposed formset name is available
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetName the form set Name (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response isFormsetNameAvailable(String formsetName) throws ApiException {
        ApiResponse<Response> resp = isFormsetNameAvailableWithHttpInfo(formsetName);
        return resp.getData();
    }

    /**
     * Returns node indicating if proposed formset name is available
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetName the form set Name (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> isFormsetNameAvailableWithHttpInfo(String formsetName) throws ApiException {
        com.squareup.okhttp.Call call = isFormsetNameAvailableValidateBeforeCall(formsetName, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Returns node indicating if proposed formset name is available (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetName the form set Name (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call isFormsetNameAvailableAsync(String formsetName, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = isFormsetNameAvailableValidateBeforeCall(formsetName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for postForm
     * @param body the form (required)
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param entityType the FormEntityType (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call postFormCall(String body, String formName, String formType, String entityType, Long applicationId, String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/{formName}/{formType}/{entityType}"
            .replaceAll("\\{" + "formName" + "\\}", apiClient.escapeString(formName.toString()))
            .replaceAll("\\{" + "formType" + "\\}", apiClient.escapeString(formType.toString()))
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (applicationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("applicationId", applicationId));
        if (uniquePermissionId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniquePermissionId", uniquePermissionId));

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
    private com.squareup.okhttp.Call postFormValidateBeforeCall(String body, String formName, String formType, String entityType, Long applicationId, String uniquePermissionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling postForm(Async)");
        }
        // verify the required parameter 'formName' is set
        if (formName == null) {
            throw new ApiException("Missing the required parameter 'formName' when calling postForm(Async)");
        }
        // verify the required parameter 'formType' is set
        if (formType == null) {
            throw new ApiException("Missing the required parameter 'formType' when calling postForm(Async)");
        }
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling postForm(Async)");
        }
        
        com.squareup.okhttp.Call call = postFormCall(body, formName, formType, entityType, applicationId, uniquePermissionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Saves a form, optionally links it to an application or permission
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the form (required)
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param entityType the FormEntityType (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response postForm(String body, String formName, String formType, String entityType, Long applicationId, String uniquePermissionId) throws ApiException {
        ApiResponse<Response> resp = postFormWithHttpInfo(body, formName, formType, entityType, applicationId, uniquePermissionId);
        return resp.getData();
    }

    /**
     * Saves a form, optionally links it to an application or permission
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the form (required)
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param entityType the FormEntityType (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> postFormWithHttpInfo(String body, String formName, String formType, String entityType, Long applicationId, String uniquePermissionId) throws ApiException {
        com.squareup.okhttp.Call call = postFormValidateBeforeCall(body, formName, formType, entityType, applicationId, uniquePermissionId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Saves a form, optionally links it to an application or permission (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the form (required)
     * @param formName the form Name (required)
     * @param formType the formType (required)
     * @param entityType the FormEntityType (required)
     * @param applicationId an optional application id if you want to link this form to an application, mutually exclusive with                            uniquePermissionId (optional)
     * @param uniquePermissionId an optional unique Permission Id id if you want to link this form to a Permission, mutually exclusive with                            applicationId (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call postFormAsync(String body, String formName, String formType, String entityType, Long applicationId, String uniquePermissionId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = postFormValidateBeforeCall(body, formName, formType, entityType, applicationId, uniquePermissionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for putForm
     * @param body the form, only required for publish (required)
     * @param formId the form ID (required)
     * @param publishForm true if publish form, update if we just want to update the draft (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call putFormCall(String body, Long formId, Boolean publishForm, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/{formId}"
            .replaceAll("\\{" + "formId" + "\\}", apiClient.escapeString(formId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (publishForm != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("publishForm", publishForm));

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
    private com.squareup.okhttp.Call putFormValidateBeforeCall(String body, Long formId, Boolean publishForm, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling putForm(Async)");
        }
        // verify the required parameter 'formId' is set
        if (formId == null) {
            throw new ApiException("Missing the required parameter 'formId' when calling putForm(Async)");
        }
        
        com.squareup.okhttp.Call call = putFormCall(body, formId, publishForm, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Updates a form, or publishes it.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the form, only required for publish (required)
     * @param formId the form ID (required)
     * @param publishForm true if publish form, update if we just want to update the draft (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response putForm(String body, Long formId, Boolean publishForm) throws ApiException {
        ApiResponse<Response> resp = putFormWithHttpInfo(body, formId, publishForm);
        return resp.getData();
    }

    /**
     * Updates a form, or publishes it.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the form, only required for publish (required)
     * @param formId the form ID (required)
     * @param publishForm true if publish form, update if we just want to update the draft (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> putFormWithHttpInfo(String body, Long formId, Boolean publishForm) throws ApiException {
        com.squareup.okhttp.Call call = putFormValidateBeforeCall(body, formId, publishForm, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a form, or publishes it. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body the form, only required for publish (required)
     * @param formId the form ID (required)
     * @param publishForm true if publish form, update if we just want to update the draft (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call putFormAsync(String body, Long formId, Boolean publishForm, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = putFormValidateBeforeCall(body, formId, publishForm, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for recipientContext
     * @param recipientUniqueUserId the recipientUniqueUserId (required)
     * @param uniquePermissionId the uniquePermissionId, mutually exclusive with applicationId, uniqueId and uniquePolicyId (optional)
     * @param uniquePolicyId the uniquePolicyId, mutually exclusive with uniqueId, applicationId and uniquePermissionId (optional)
     * @param applicationId the applicationId, mutually exclusive with uniquePermissionId and uniqueId (optional)
     * @param uniqueAccountId the uniqueAccountId, only pass when deleting permission access (optional)
     * @param uniqueId the business role uniqueId, mutually exclusive with uniquePolicyId, applicationId and uniquePermissionId (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call recipientContextCall(String recipientUniqueUserId, String uniquePermissionId, String uniquePolicyId, Long applicationId, String uniqueAccountId, String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/requestContext/{recipientUniqueUserId}"
            .replaceAll("\\{" + "recipientUniqueUserId" + "\\}", apiClient.escapeString(recipientUniqueUserId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (uniquePermissionId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniquePermissionId", uniquePermissionId));
        if (uniquePolicyId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniquePolicyId", uniquePolicyId));
        if (applicationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("applicationId", applicationId));
        if (uniqueAccountId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueAccountId", uniqueAccountId));
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
    private com.squareup.okhttp.Call recipientContextValidateBeforeCall(String recipientUniqueUserId, String uniquePermissionId, String uniquePolicyId, Long applicationId, String uniqueAccountId, String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'recipientUniqueUserId' is set
        if (recipientUniqueUserId == null) {
            throw new ApiException("Missing the required parameter 'recipientUniqueUserId' when calling recipientContext(Async)");
        }
        
        com.squareup.okhttp.Call call = recipientContextCall(recipientUniqueUserId, uniquePermissionId, uniquePolicyId, applicationId, uniqueAccountId, uniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets requestContext for use in form population
     * Accepted Roles: * All Access 
     * @param recipientUniqueUserId the recipientUniqueUserId (required)
     * @param uniquePermissionId the uniquePermissionId, mutually exclusive with applicationId, uniqueId and uniquePolicyId (optional)
     * @param uniquePolicyId the uniquePolicyId, mutually exclusive with uniqueId, applicationId and uniquePermissionId (optional)
     * @param applicationId the applicationId, mutually exclusive with uniquePermissionId and uniqueId (optional)
     * @param uniqueAccountId the uniqueAccountId, only pass when deleting permission access (optional)
     * @param uniqueId the business role uniqueId, mutually exclusive with uniquePolicyId, applicationId and uniquePermissionId (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response recipientContext(String recipientUniqueUserId, String uniquePermissionId, String uniquePolicyId, Long applicationId, String uniqueAccountId, String uniqueId) throws ApiException {
        ApiResponse<Response> resp = recipientContextWithHttpInfo(recipientUniqueUserId, uniquePermissionId, uniquePolicyId, applicationId, uniqueAccountId, uniqueId);
        return resp.getData();
    }

    /**
     * Gets requestContext for use in form population
     * Accepted Roles: * All Access 
     * @param recipientUniqueUserId the recipientUniqueUserId (required)
     * @param uniquePermissionId the uniquePermissionId, mutually exclusive with applicationId, uniqueId and uniquePolicyId (optional)
     * @param uniquePolicyId the uniquePolicyId, mutually exclusive with uniqueId, applicationId and uniquePermissionId (optional)
     * @param applicationId the applicationId, mutually exclusive with uniquePermissionId and uniqueId (optional)
     * @param uniqueAccountId the uniqueAccountId, only pass when deleting permission access (optional)
     * @param uniqueId the business role uniqueId, mutually exclusive with uniquePolicyId, applicationId and uniquePermissionId (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> recipientContextWithHttpInfo(String recipientUniqueUserId, String uniquePermissionId, String uniquePolicyId, Long applicationId, String uniqueAccountId, String uniqueId) throws ApiException {
        com.squareup.okhttp.Call call = recipientContextValidateBeforeCall(recipientUniqueUserId, uniquePermissionId, uniquePolicyId, applicationId, uniqueAccountId, uniqueId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets requestContext for use in form population (asynchronously)
     * Accepted Roles: * All Access 
     * @param recipientUniqueUserId the recipientUniqueUserId (required)
     * @param uniquePermissionId the uniquePermissionId, mutually exclusive with applicationId, uniqueId and uniquePolicyId (optional)
     * @param uniquePolicyId the uniquePolicyId, mutually exclusive with uniqueId, applicationId and uniquePermissionId (optional)
     * @param applicationId the applicationId, mutually exclusive with uniquePermissionId and uniqueId (optional)
     * @param uniqueAccountId the uniqueAccountId, only pass when deleting permission access (optional)
     * @param uniqueId the business role uniqueId, mutually exclusive with uniquePolicyId, applicationId and uniquePermissionId (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call recipientContextAsync(String recipientUniqueUserId, String uniquePermissionId, String uniquePolicyId, Long applicationId, String uniqueAccountId, String uniqueId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = recipientContextValidateBeforeCall(recipientUniqueUserId, uniquePermissionId, uniquePolicyId, applicationId, uniqueAccountId, uniqueId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for renameForm
     * @param formId the form ID (required)
     * @param formName the form name (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call renameFormCall(Long formId, String formName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/{formId}/rename/{formName}"
            .replaceAll("\\{" + "formId" + "\\}", apiClient.escapeString(formId.toString()))
            .replaceAll("\\{" + "formName" + "\\}", apiClient.escapeString(formName.toString()));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call renameFormValidateBeforeCall(Long formId, String formName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formId' is set
        if (formId == null) {
            throw new ApiException("Missing the required parameter 'formId' when calling renameForm(Async)");
        }
        // verify the required parameter 'formName' is set
        if (formName == null) {
            throw new ApiException("Missing the required parameter 'formName' when calling renameForm(Async)");
        }
        
        com.squareup.okhttp.Call call = renameFormCall(formId, formName, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Renames a form
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formId the form ID (required)
     * @param formName the form name (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response renameForm(Long formId, String formName) throws ApiException {
        ApiResponse<Response> resp = renameFormWithHttpInfo(formId, formName);
        return resp.getData();
    }

    /**
     * Renames a form
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formId the form ID (required)
     * @param formName the form name (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> renameFormWithHttpInfo(Long formId, String formName) throws ApiException {
        com.squareup.okhttp.Call call = renameFormValidateBeforeCall(formId, formName, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Renames a form (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formId the form ID (required)
     * @param formName the form name (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call renameFormAsync(Long formId, String formName, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = renameFormValidateBeforeCall(formId, formName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for renameFormset
     * @param formsetId the formset ID (required)
     * @param formsetName the formset name (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call renameFormsetCall(Long formsetId, String formsetName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/formset/{formsetId}/rename/{formsetName}"
            .replaceAll("\\{" + "formsetId" + "\\}", apiClient.escapeString(formsetId.toString()))
            .replaceAll("\\{" + "formsetName" + "\\}", apiClient.escapeString(formsetName.toString()));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call renameFormsetValidateBeforeCall(Long formsetId, String formsetName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formsetId' is set
        if (formsetId == null) {
            throw new ApiException("Missing the required parameter 'formsetId' when calling renameFormset(Async)");
        }
        // verify the required parameter 'formsetName' is set
        if (formsetName == null) {
            throw new ApiException("Missing the required parameter 'formsetName' when calling renameFormset(Async)");
        }
        
        com.squareup.okhttp.Call call = renameFormsetCall(formsetId, formsetName, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Renames a formset
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetId the formset ID (required)
     * @param formsetName the formset name (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response renameFormset(Long formsetId, String formsetName) throws ApiException {
        ApiResponse<Response> resp = renameFormsetWithHttpInfo(formsetId, formsetName);
        return resp.getData();
    }

    /**
     * Renames a formset
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetId the formset ID (required)
     * @param formsetName the formset name (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> renameFormsetWithHttpInfo(Long formsetId, String formsetName) throws ApiException {
        com.squareup.okhttp.Call call = renameFormsetValidateBeforeCall(formsetId, formsetName, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Renames a formset (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetId the formset ID (required)
     * @param formsetName the formset name (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call renameFormsetAsync(Long formsetId, String formsetName, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = renameFormsetValidateBeforeCall(formsetId, formsetName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for revertForm
     * @param formsetMapId the form set ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call revertFormCall(Long formsetMapId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/formsetMap/{formsetMapId}/revert"
            .replaceAll("\\{" + "formsetMapId" + "\\}", apiClient.escapeString(formsetMapId.toString()));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call revertFormValidateBeforeCall(Long formsetMapId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'formsetMapId' is set
        if (formsetMapId == null) {
            throw new ApiException("Missing the required parameter 'formsetMapId' when calling revertForm(Async)");
        }
        
        com.squareup.okhttp.Call call = revertFormCall(formsetMapId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * reverts a form to last published value
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetMapId the form set ID (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response revertForm(Long formsetMapId) throws ApiException {
        ApiResponse<Response> resp = revertFormWithHttpInfo(formsetMapId);
        return resp.getData();
    }

    /**
     * reverts a form to last published value
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetMapId the form set ID (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> revertFormWithHttpInfo(Long formsetMapId) throws ApiException {
        com.squareup.okhttp.Call call = revertFormValidateBeforeCall(formsetMapId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * reverts a form to last published value (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formsetMapId the form set ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call revertFormAsync(Long formsetMapId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = revertFormValidateBeforeCall(formsetMapId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for searchForms
     * @param formEntityType the Form Entity Type (APPLICATION, PERMISSION, soon TECHNICAL_ROLE), don&#x27;t include for all) (optional)
     * @param formType the formType (REQUEST, APPROVAL), don&#x27;t include for all (optional)
     * @param q the form name query (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call searchFormsCall(String formEntityType, String formType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (formEntityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("formEntityType", formEntityType));
        if (formType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("formType", formType));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
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
    private com.squareup.okhttp.Call searchFormsValidateBeforeCall(String formEntityType, String formType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = searchFormsCall(formEntityType, formType, q, indexFrom, size, sortBy, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Searches for forms associated with an FormEntityType/FormType
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formEntityType the Form Entity Type (APPLICATION, PERMISSION, soon TECHNICAL_ROLE), don&#x27;t include for all) (optional)
     * @param formType the formType (REQUEST, APPROVAL), don&#x27;t include for all (optional)
     * @param q the form name query (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response searchForms(String formEntityType, String formType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<Response> resp = searchFormsWithHttpInfo(formEntityType, formType, q, indexFrom, size, sortBy, sortOrder);
        return resp.getData();
    }

    /**
     * Searches for forms associated with an FormEntityType/FormType
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formEntityType the Form Entity Type (APPLICATION, PERMISSION, soon TECHNICAL_ROLE), don&#x27;t include for all) (optional)
     * @param formType the formType (REQUEST, APPROVAL), don&#x27;t include for all (optional)
     * @param q the form name query (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> searchFormsWithHttpInfo(String formEntityType, String formType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = searchFormsValidateBeforeCall(formEntityType, formType, q, indexFrom, size, sortBy, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Searches for forms associated with an FormEntityType/FormType (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param formEntityType the Form Entity Type (APPLICATION, PERMISSION, soon TECHNICAL_ROLE), don&#x27;t include for all) (optional)
     * @param formType the formType (REQUEST, APPROVAL), don&#x27;t include for all (optional)
     * @param q the form name query (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy The attribute to sort by, alllowed values (form_name, entity_type, form_type) (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC* (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call searchFormsAsync(String formEntityType, String formType, String q, Integer indexFrom, Integer size, String sortBy, String sortOrder, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = searchFormsValidateBeforeCall(formEntityType, formType, q, indexFrom, size, sortBy, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for searchFormset
     * @param q Filter to be applied to item name (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy ASC or DESC (optional)
     * @param excludeFormset the form set id string of a formset(s) to exclude (optional)
     * @param formEntityType the form Entity Type filter, PERMISSION or APPLICATION (optional)
     * @param sortOrder the sort order (optional, default to ASC)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call searchFormsetCall(String q, Integer indexFrom, Integer size, String sortBy, String excludeFormset, String formEntityType, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/requestForms/formsetSearch";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (excludeFormset != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("excludeFormset", excludeFormset));
        if (formEntityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("formEntityType", formEntityType));
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
    private com.squareup.okhttp.Call searchFormsetValidateBeforeCall(String q, Integer indexFrom, Integer size, String sortBy, String excludeFormset, String formEntityType, String sortOrder, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = searchFormsetCall(q, indexFrom, size, sortBy, excludeFormset, formEntityType, sortOrder, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Searches for formsets
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param q Filter to be applied to item name (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy ASC or DESC (optional)
     * @param excludeFormset the form set id string of a formset(s) to exclude (optional)
     * @param formEntityType the form Entity Type filter, PERMISSION or APPLICATION (optional)
     * @param sortOrder the sort order (optional, default to ASC)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response searchFormset(String q, Integer indexFrom, Integer size, String sortBy, String excludeFormset, String formEntityType, String sortOrder) throws ApiException {
        ApiResponse<Response> resp = searchFormsetWithHttpInfo(q, indexFrom, size, sortBy, excludeFormset, formEntityType, sortOrder);
        return resp.getData();
    }

    /**
     * Searches for formsets
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param q Filter to be applied to item name (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy ASC or DESC (optional)
     * @param excludeFormset the form set id string of a formset(s) to exclude (optional)
     * @param formEntityType the form Entity Type filter, PERMISSION or APPLICATION (optional)
     * @param sortOrder the sort order (optional, default to ASC)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> searchFormsetWithHttpInfo(String q, Integer indexFrom, Integer size, String sortBy, String excludeFormset, String formEntityType, String sortOrder) throws ApiException {
        com.squareup.okhttp.Call call = searchFormsetValidateBeforeCall(q, indexFrom, size, sortBy, excludeFormset, formEntityType, sortOrder, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Searches for formsets (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param q Filter to be applied to item name (optional)
     * @param indexFrom Result set start position. (optional, default to 0)
     * @param size Size to return in result set. (optional, default to 0)
     * @param sortBy ASC or DESC (optional)
     * @param excludeFormset the form set id string of a formset(s) to exclude (optional)
     * @param formEntityType the form Entity Type filter, PERMISSION or APPLICATION (optional)
     * @param sortOrder the sort order (optional, default to ASC)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call searchFormsetAsync(String q, Integer indexFrom, Integer size, String sortBy, String excludeFormset, String formEntityType, String sortOrder, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = searchFormsetValidateBeforeCall(q, indexFrom, size, sortBy, excludeFormset, formEntityType, sortOrder, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param body - The download request node (required)
     * @param entityType the ent type (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(Download body, String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/requestForms/forms/download/{entityType}"
            .replaceAll("\\{" + "entityType" + "\\}", apiClient.escapeString(entityType.toString()));

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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(Download body, String entityType, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload(Async)");
        }
        // verify the required parameter 'entityType' is set
        if (entityType == null) {
            throw new ApiException("Missing the required parameter 'entityType' when calling startDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownloadCall(body, entityType, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the custom form download.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - The download request node (required)
     * @param entityType the ent type (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(Download body, String entityType) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(body, entityType);
        return resp.getData();
    }

    /**
     * Start the custom form download.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - The download request node (required)
     * @param entityType the ent type (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(Download body, String entityType) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, entityType, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the custom form download. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Customer Administrator * Data Administrator * Global Administrator 
     * @param body - The download request node (required)
     * @param entityType the ent type (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(Download body, String entityType, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, entityType, progressListener, progressRequestListener);
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
        String localVarPath = "/requestForms/forms/download/{id}/status"
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
     * Accepted Roles: * All Access 
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
     * Accepted Roles: * All Access 
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
     * Accepted Roles: * All Access 
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
}
