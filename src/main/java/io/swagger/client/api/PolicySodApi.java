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


import io.swagger.client.model.AccountListNode;
import io.swagger.client.model.Calculated;
import io.swagger.client.model.Cases;
import io.swagger.client.model.Download;
import java.io.File;
import io.swagger.client.model.Permissions;
import io.swagger.client.model.Policies;
import io.swagger.client.model.PolicyDetectionPreview;
import io.swagger.client.model.Response;
import io.swagger.client.model.Roles;
import io.swagger.client.model.SearchCriteria;
import io.swagger.client.model.Sod;
import io.swagger.client.model.SodCase;
import io.swagger.client.model.SodCaseAction;
import io.swagger.client.model.Sodcounts;
import io.swagger.client.model.Sodcrit;
import io.swagger.client.model.Sodviolcases2;
import io.swagger.client.model.Users;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicySodApi {
    private ApiClient apiClient;

    public PolicySodApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicySodApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for addSodCaseAction
     * @param body Action to be added to SoD case. (required)
     * @param ID SoD case ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addSodCaseActionCall(SodCaseAction body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/case/{ID}/action"
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
    private com.squareup.okhttp.Call addSodCaseActionValidateBeforeCall(SodCaseAction body, Long ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling addSodCaseAction(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling addSodCaseAction(Async)");
        }
        
        com.squareup.okhttp.Call call = addSodCaseActionCall(body, ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Add a SoD case action to a SOD case.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body Action to be added to SoD case. (required)
     * @param ID SoD case ID. (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response addSodCaseAction(SodCaseAction body, Long ID) throws ApiException {
        ApiResponse<Response> resp = addSodCaseActionWithHttpInfo(body, ID);
        return resp.getData();
    }

    /**
     * Add a SoD case action to a SOD case.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body Action to be added to SoD case. (required)
     * @param ID SoD case ID. (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> addSodCaseActionWithHttpInfo(SodCaseAction body, Long ID) throws ApiException {
        com.squareup.okhttp.Call call = addSodCaseActionValidateBeforeCall(body, ID, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Add a SoD case action to a SOD case. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body Action to be added to SoD case. (required)
     * @param ID SoD case ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addSodCaseActionAsync(SodCaseAction body, Long ID, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = addSodCaseActionValidateBeforeCall(body, ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for bulkValidateConditions
     * @param body The SoD policy list containing the conditions to use to validate policies and estimate users and accounts. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call bulkValidateConditionsCall(Policies body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/bulk/validate";

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
    private com.squareup.okhttp.Call bulkValidateConditionsValidateBeforeCall(Policies body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling bulkValidateConditions(Async)");
        }
        
        com.squareup.okhttp.Call call = bulkValidateConditionsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Bulk Validate the sod condition items
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy list containing the conditions to use to validate policies and estimate users and accounts. (required)
     * @return PolicyDetectionPreview
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PolicyDetectionPreview bulkValidateConditions(Policies body) throws ApiException {
        ApiResponse<PolicyDetectionPreview> resp = bulkValidateConditionsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Bulk Validate the sod condition items
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy list containing the conditions to use to validate policies and estimate users and accounts. (required)
     * @return ApiResponse&lt;PolicyDetectionPreview&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PolicyDetectionPreview> bulkValidateConditionsWithHttpInfo(Policies body) throws ApiException {
        com.squareup.okhttp.Call call = bulkValidateConditionsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Bulk Validate the sod condition items (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy list containing the conditions to use to validate policies and estimate users and accounts. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call bulkValidateConditionsAsync(Policies body, final ApiCallback<PolicyDetectionPreview> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = bulkValidateConditionsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
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
        String localVarPath = "/policy/sod/download/{id}"
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
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner 
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
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner 
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
     * Build call for createSoDPolicy
     * @param body The SoD policy to create. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createSoDPolicyCall(Sod body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/new";

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
    private com.squareup.okhttp.Call createSoDPolicyValidateBeforeCall(Sod body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createSoDPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = createSoDPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create a SoD policy.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy to create. (required)
     * @return Sod
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sod createSoDPolicy(Sod body) throws ApiException {
        ApiResponse<Sod> resp = createSoDPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create a SoD policy.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy to create. (required)
     * @return ApiResponse&lt;Sod&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sod> createSoDPolicyWithHttpInfo(Sod body) throws ApiException {
        com.squareup.okhttp.Call call = createSoDPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a SoD policy. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy to create. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createSoDPolicyAsync(Sod body, final ApiCallback<Sod> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createSoDPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteSodPolicy
     * @param id The SoD policy ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteSodPolicyCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{id}"
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
    private com.squareup.okhttp.Call deleteSodPolicyValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteSodPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteSodPolicyCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the Sod policy with the given ID.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param id The SoD policy ID. (required)
     * @return Sod
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sod deleteSodPolicy(Long id) throws ApiException {
        ApiResponse<Sod> resp = deleteSodPolicyWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete the Sod policy with the given ID.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param id The SoD policy ID. (required)
     * @return ApiResponse&lt;Sod&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sod> deleteSodPolicyWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteSodPolicyValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the Sod policy with the given ID. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param id The SoD policy ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteSodPolicyAsync(Long id, final ApiCallback<Sod> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteSodPolicyValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for estimateUsers
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call estimateUsersCall(Sod body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/estimate";

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
    private com.squareup.okhttp.Call estimateUsersValidateBeforeCall(Sod body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling estimateUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = estimateUsersCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Estimate users matching the conditions of the SoD policy.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @return PolicyDetectionPreview
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PolicyDetectionPreview estimateUsers(Sod body) throws ApiException {
        ApiResponse<PolicyDetectionPreview> resp = estimateUsersWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Estimate users matching the conditions of the SoD policy.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @return ApiResponse&lt;PolicyDetectionPreview&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PolicyDetectionPreview> estimateUsersWithHttpInfo(Sod body) throws ApiException {
        com.squareup.okhttp.Call call = estimateUsersValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Estimate users matching the conditions of the SoD policy. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call estimateUsersAsync(Sod body, final ApiCallback<PolicyDetectionPreview> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = estimateUsersValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
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
        String localVarPath = "/policy/sod/download/{id}"
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
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
     * Build call for getAccountSoDCase
     * @param ID SoD policy ID. (required)
     * @param accountId Account ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAccountSoDCaseCall(Long ID, Long accountId, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{ID}/case/account/{accountId}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "accountId" + "\\}", apiClient.escapeString(accountId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getAccountSoDCaseValidateBeforeCall(Long ID, Long accountId, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getAccountSoDCase(Async)");
        }
        // verify the required parameter 'accountId' is set
        if (accountId == null) {
            throw new ApiException("Missing the required parameter 'accountId' when calling getAccountSoDCase(Async)");
        }
        
        com.squareup.okhttp.Call call = getAccountSoDCaseCall(ID, accountId, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SOD case information associated with a SOD policy and a particular account, if any.
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD policy ID. (required)
     * @param accountId Account ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return SodCase
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SodCase getAccountSoDCase(Long ID, Long accountId, List<String> attrFilter) throws ApiException {
        ApiResponse<SodCase> resp = getAccountSoDCaseWithHttpInfo(ID, accountId, attrFilter);
        return resp.getData();
    }

    /**
     * Get the SOD case information associated with a SOD policy and a particular account, if any.
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD policy ID. (required)
     * @param accountId Account ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;SodCase&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SodCase> getAccountSoDCaseWithHttpInfo(Long ID, Long accountId, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getAccountSoDCaseValidateBeforeCall(ID, accountId, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<SodCase>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SOD case information associated with a SOD policy and a particular account, if any. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD policy ID. (required)
     * @param accountId Account ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAccountSoDCaseAsync(Long ID, Long accountId, List<String> attrFilter, final ApiCallback<SodCase> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAccountSoDCaseValidateBeforeCall(ID, accountId, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SodCase>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContributedToSodViolationsByBusinessRole
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByBusinessRoleCall(Long userId, Long broleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/cases/contributedto/{userId}/brole/{broleId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
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
    private com.squareup.okhttp.Call getContributedToSodViolationsByBusinessRoleValidateBeforeCall(Long userId, Long broleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getContributedToSodViolationsByBusinessRole(Async)");
        }
        // verify the required parameter 'broleId' is set
        if (broleId == null) {
            throw new ApiException("Missing the required parameter 'broleId' when calling getContributedToSodViolationsByBusinessRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getContributedToSodViolationsByBusinessRoleCall(userId, broleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of SoD violations for the user that the specified business role contributes to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @return Cases
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Cases getContributedToSodViolationsByBusinessRole(Long userId, Long broleId) throws ApiException {
        ApiResponse<Cases> resp = getContributedToSodViolationsByBusinessRoleWithHttpInfo(userId, broleId);
        return resp.getData();
    }

    /**
     * Get the list of SoD violations for the user that the specified business role contributes to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @return ApiResponse&lt;Cases&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Cases> getContributedToSodViolationsByBusinessRoleWithHttpInfo(Long userId, Long broleId) throws ApiException {
        com.squareup.okhttp.Call call = getContributedToSodViolationsByBusinessRoleValidateBeforeCall(userId, broleId, null, null);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of SoD violations for the user that the specified business role contributes to. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByBusinessRoleAsync(Long userId, Long broleId, final ApiCallback<Cases> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContributedToSodViolationsByBusinessRoleValidateBeforeCall(userId, broleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContributedToSodViolationsByPerm
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByPermCall(Long userId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/cases/contributedto/{userId}/perm/{permId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
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
    private com.squareup.okhttp.Call getContributedToSodViolationsByPermValidateBeforeCall(Long userId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getContributedToSodViolationsByPerm(Async)");
        }
        // verify the required parameter 'permId' is set
        if (permId == null) {
            throw new ApiException("Missing the required parameter 'permId' when calling getContributedToSodViolationsByPerm(Async)");
        }
        
        com.squareup.okhttp.Call call = getContributedToSodViolationsByPermCall(userId, permId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of SoD violations for the user that the specified permission contributes to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @return Cases
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Cases getContributedToSodViolationsByPerm(Long userId, Long permId) throws ApiException {
        ApiResponse<Cases> resp = getContributedToSodViolationsByPermWithHttpInfo(userId, permId);
        return resp.getData();
    }

    /**
     * Get the list of SoD violations for the user that the specified permission contributes to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @return ApiResponse&lt;Cases&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Cases> getContributedToSodViolationsByPermWithHttpInfo(Long userId, Long permId) throws ApiException {
        com.squareup.okhttp.Call call = getContributedToSodViolationsByPermValidateBeforeCall(userId, permId, null, null);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of SoD violations for the user that the specified permission contributes to. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByPermAsync(Long userId, Long permId, final ApiCallback<Cases> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContributedToSodViolationsByPermValidateBeforeCall(userId, permId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContributedToSodViolationsByRole
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByRoleCall(Long userId, Long roleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/cases/contributedto/{userId}/role/{roleId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
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
    private com.squareup.okhttp.Call getContributedToSodViolationsByRoleValidateBeforeCall(Long userId, Long roleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getContributedToSodViolationsByRole(Async)");
        }
        // verify the required parameter 'roleId' is set
        if (roleId == null) {
            throw new ApiException("Missing the required parameter 'roleId' when calling getContributedToSodViolationsByRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getContributedToSodViolationsByRoleCall(userId, roleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of SoD violations for the user that the specified technical role contributes to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @return Cases
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Cases getContributedToSodViolationsByRole(Long userId, Long roleId) throws ApiException {
        ApiResponse<Cases> resp = getContributedToSodViolationsByRoleWithHttpInfo(userId, roleId);
        return resp.getData();
    }

    /**
     * Get the list of SoD violations for the user that the specified technical role contributes to.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @return ApiResponse&lt;Cases&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Cases> getContributedToSodViolationsByRoleWithHttpInfo(Long userId, Long roleId) throws ApiException {
        com.squareup.okhttp.Call call = getContributedToSodViolationsByRoleValidateBeforeCall(userId, roleId, null, null);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of SoD violations for the user that the specified technical role contributes to. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContributedToSodViolationsByRoleAsync(Long userId, Long roleId, final ApiCallback<Cases> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getContributedToSodViolationsByRoleValidateBeforeCall(userId, roleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCurrentSodViolations
     * @param body SoD criteria - specifies user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to user name and policy name. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCurrentSodViolationsCall(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/current/cases";

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
    private com.squareup.okhttp.Call getCurrentSodViolationsValidateBeforeCall(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getCurrentSodViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getCurrentSodViolationsCall(body, indexFrom, size, sortBy, sortOrder, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of current SoD violations for the users specified in sodCriteria.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - specifies user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to user name and policy name. (optional)
     * @return Cases
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Cases getCurrentSodViolations(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q) throws ApiException {
        ApiResponse<Cases> resp = getCurrentSodViolationsWithHttpInfo(body, indexFrom, size, sortBy, sortOrder, q);
        return resp.getData();
    }

    /**
     * Get the list of current SoD violations for the users specified in sodCriteria.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - specifies user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to user name and policy name. (optional)
     * @return ApiResponse&lt;Cases&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Cases> getCurrentSodViolationsWithHttpInfo(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q) throws ApiException {
        com.squareup.okhttp.Call call = getCurrentSodViolationsValidateBeforeCall(body, indexFrom, size, sortBy, sortOrder, q, null, null);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of current SoD violations for the users specified in sodCriteria. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - specifies user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to user name and policy name. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCurrentSodViolationsAsync(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, final ApiCallback<Cases> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCurrentSodViolationsValidateBeforeCall(body, indexFrom, size, sortBy, sortOrder, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPotentialSodViolations
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsCall(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/potential/cases";

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
    private com.squareup.okhttp.Call getPotentialSodViolationsValidateBeforeCall(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getPotentialSodViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getPotentialSodViolationsCall(body, indexFrom, size, sortBy, sortOrder, q, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of potential new SoD violations that would occur if the users specified in sodCriteria had the permissions and technical roles  specified in sodCriteria in addition to the permissions and technical roles they already have.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @return Cases
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Cases getPotentialSodViolations(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q) throws ApiException {
        ApiResponse<Cases> resp = getPotentialSodViolationsWithHttpInfo(body, indexFrom, size, sortBy, sortOrder, q);
        return resp.getData();
    }

    /**
     * Get the list of potential new SoD violations that would occur if the users specified in sodCriteria had the permissions and technical roles  specified in sodCriteria in addition to the permissions and technical roles they already have.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @return ApiResponse&lt;Cases&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Cases> getPotentialSodViolationsWithHttpInfo(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q) throws ApiException {
        com.squareup.okhttp.Call call = getPotentialSodViolationsValidateBeforeCall(body, indexFrom, size, sortBy, sortOrder, q, null, null);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of potential new SoD violations that would occur if the users specified in sodCriteria had the permissions and technical roles  specified in sodCriteria in addition to the permissions and technical roles they already have. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsAsync(Sodcrit body, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, final ApiCallback<Cases> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPotentialSodViolationsValidateBeforeCall(body, indexFrom, size, sortBy, sortOrder, q, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPotentialSodViolationsByBusinessRole
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsByBusinessRoleCall(Long userId, Long broleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/cases/potential/{userId}/brole/{broleId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
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
    private com.squareup.okhttp.Call getPotentialSodViolationsByBusinessRoleValidateBeforeCall(Long userId, Long broleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getPotentialSodViolationsByBusinessRole(Async)");
        }
        // verify the required parameter 'broleId' is set
        if (broleId == null) {
            throw new ApiException("Missing the required parameter 'broleId' when calling getPotentialSodViolationsByBusinessRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getPotentialSodViolationsByBusinessRoleCall(userId, broleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified business role.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getPotentialSodViolationsByBusinessRole(Long userId, Long broleId) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getPotentialSodViolationsByBusinessRoleWithHttpInfo(userId, broleId);
        return resp.getData();
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified business role.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getPotentialSodViolationsByBusinessRoleWithHttpInfo(Long userId, Long broleId) throws ApiException {
        com.squareup.okhttp.Call call = getPotentialSodViolationsByBusinessRoleValidateBeforeCall(userId, broleId, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified business role. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param broleId Business Role ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsByBusinessRoleAsync(Long userId, Long broleId, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPotentialSodViolationsByBusinessRoleValidateBeforeCall(userId, broleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPotentialSodViolationsByPerm
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsByPermCall(Long userId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/cases/potential/{userId}/perm/{permId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
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
    private com.squareup.okhttp.Call getPotentialSodViolationsByPermValidateBeforeCall(Long userId, Long permId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getPotentialSodViolationsByPerm(Async)");
        }
        // verify the required parameter 'permId' is set
        if (permId == null) {
            throw new ApiException("Missing the required parameter 'permId' when calling getPotentialSodViolationsByPerm(Async)");
        }
        
        com.squareup.okhttp.Call call = getPotentialSodViolationsByPermCall(userId, permId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified permission.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getPotentialSodViolationsByPerm(Long userId, Long permId) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getPotentialSodViolationsByPermWithHttpInfo(userId, permId);
        return resp.getData();
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified permission.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getPotentialSodViolationsByPermWithHttpInfo(Long userId, Long permId) throws ApiException {
        com.squareup.okhttp.Call call = getPotentialSodViolationsByPermValidateBeforeCall(userId, permId, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified permission. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param permId Permission ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsByPermAsync(Long userId, Long permId, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPotentialSodViolationsByPermValidateBeforeCall(userId, permId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPotentialSodViolationsByRole
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsByRoleCall(Long userId, Long roleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/cases/potential/{userId}/role/{roleId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()))
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
    private com.squareup.okhttp.Call getPotentialSodViolationsByRoleValidateBeforeCall(Long userId, Long roleId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getPotentialSodViolationsByRole(Async)");
        }
        // verify the required parameter 'roleId' is set
        if (roleId == null) {
            throw new ApiException("Missing the required parameter 'roleId' when calling getPotentialSodViolationsByRole(Async)");
        }
        
        com.squareup.okhttp.Call call = getPotentialSodViolationsByRoleCall(userId, roleId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified technical role.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @return Sodviolcases2
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodviolcases2 getPotentialSodViolationsByRole(Long userId, Long roleId) throws ApiException {
        ApiResponse<Sodviolcases2> resp = getPotentialSodViolationsByRoleWithHttpInfo(userId, roleId);
        return resp.getData();
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified technical role.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @return ApiResponse&lt;Sodviolcases2&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodviolcases2> getPotentialSodViolationsByRoleWithHttpInfo(Long userId, Long roleId) throws ApiException {
        com.squareup.okhttp.Call call = getPotentialSodViolationsByRoleValidateBeforeCall(userId, roleId, null, null);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of potential SoD violations that would occur for the user if the user were to obtain the specified technical role. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param userId User ID (required)
     * @param roleId Role ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPotentialSodViolationsByRoleAsync(Long userId, Long roleId, final ApiCallback<Sodviolcases2> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPotentialSodViolationsByRoleValidateBeforeCall(userId, roleId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodviolcases2>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSoD
     * @param ID The SoD policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of SOD policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted SoD policy. (optional, default to false)
     * @param userId If non-null, for each permission and role in the SoD policy, return information about whether the specified user has                          those permissions and roles and whether there are pending and/remove requests for those permissions and roles. (optional)
     * @param violationId Id of SoD violation to filter condition expressions (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSoDCall(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, Long userId, Long violationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (allowDeleted != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("allowDeleted", allowDeleted));
        if (userId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("userId", userId));
        if (violationId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("violationId", violationId));

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
    private com.squareup.okhttp.Call getSoDValidateBeforeCall(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, Long userId, Long violationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSoD(Async)");
        }
        
        com.squareup.okhttp.Call call = getSoDCall(ID, attrFilter, listAttr, allowDeleted, userId, violationId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SoD policy with the given ID.
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Governance Insights Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Potential SoD Violation Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param ID The SoD policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of SOD policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted SoD policy. (optional, default to false)
     * @param userId If non-null, for each permission and role in the SoD policy, return information about whether the specified user has                          those permissions and roles and whether there are pending and/remove requests for those permissions and roles. (optional)
     * @param violationId Id of SoD violation to filter condition expressions (optional)
     * @return Sod
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sod getSoD(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, Long userId, Long violationId) throws ApiException {
        ApiResponse<Sod> resp = getSoDWithHttpInfo(ID, attrFilter, listAttr, allowDeleted, userId, violationId);
        return resp.getData();
    }

    /**
     * Get the SoD policy with the given ID.
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Governance Insights Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Potential SoD Violation Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param ID The SoD policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of SOD policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted SoD policy. (optional, default to false)
     * @param userId If non-null, for each permission and role in the SoD policy, return information about whether the specified user has                          those permissions and roles and whether there are pending and/remove requests for those permissions and roles. (optional)
     * @param violationId Id of SoD violation to filter condition expressions (optional)
     * @return ApiResponse&lt;Sod&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sod> getSoDWithHttpInfo(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, Long userId, Long violationId) throws ApiException {
        com.squareup.okhttp.Call call = getSoDValidateBeforeCall(ID, attrFilter, listAttr, allowDeleted, userId, violationId, null, null);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SoD policy with the given ID. (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Auditor * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Governance Insights Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Potential SoD Violation Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param ID The SoD policy ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param listAttr List of SOD policy attributes to return (optional)
     * @param allowDeleted Allow retrieval of deleted SoD policy. (optional, default to false)
     * @param userId If non-null, for each permission and role in the SoD policy, return information about whether the specified user has                          those permissions and roles and whether there are pending and/remove requests for those permissions and roles. (optional)
     * @param violationId Id of SoD violation to filter condition expressions (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSoDAsync(Long ID, List<String> attrFilter, List<String> listAttr, Boolean allowDeleted, Long userId, Long violationId, final ApiCallback<Sod> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSoDValidateBeforeCall(ID, attrFilter, listAttr, allowDeleted, userId, violationId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSoDCalculatedAttr
     * @param ID The SoD policy ID (required)
     * @param attrName Name of calculated attribute to be retrieved. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSoDCalculatedAttrCall(Long ID, String attrName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{ID}/{attrName}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "attrName" + "\\}", apiClient.escapeString(attrName.toString()));

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
    private com.squareup.okhttp.Call getSoDCalculatedAttrValidateBeforeCall(Long ID, String attrName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSoDCalculatedAttr(Async)");
        }
        // verify the required parameter 'attrName' is set
        if (attrName == null) {
            throw new ApiException("Missing the required parameter 'attrName' when calling getSoDCalculatedAttr(Async)");
        }
        
        com.squareup.okhttp.Call call = getSoDCalculatedAttrCall(ID, attrName, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the calculated attribute (attrName) for the given SoD policy ID.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Review Administrator * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param ID The SoD policy ID (required)
     * @param attrName Name of calculated attribute to be retrieved. (required)
     * @return Calculated
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Calculated getSoDCalculatedAttr(Long ID, String attrName) throws ApiException {
        ApiResponse<Calculated> resp = getSoDCalculatedAttrWithHttpInfo(ID, attrName);
        return resp.getData();
    }

    /**
     * Get the calculated attribute (attrName) for the given SoD policy ID.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Review Administrator * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param ID The SoD policy ID (required)
     * @param attrName Name of calculated attribute to be retrieved. (required)
     * @return ApiResponse&lt;Calculated&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Calculated> getSoDCalculatedAttrWithHttpInfo(Long ID, String attrName) throws ApiException {
        com.squareup.okhttp.Call call = getSoDCalculatedAttrValidateBeforeCall(ID, attrName, null, null);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the calculated attribute (attrName) for the given SoD policy ID. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Review Administrator * Role owner * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator 
     * @param ID The SoD policy ID (required)
     * @param attrName Name of calculated attribute to be retrieved. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSoDCalculatedAttrAsync(Long ID, String attrName, final ApiCallback<Calculated> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSoDCalculatedAttrValidateBeforeCall(ID, attrName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Calculated>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSoDCase
     * @param ID SoD case ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSoDCaseCall(Long ID, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/case/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getSoDCaseValidateBeforeCall(Long ID, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSoDCase(Async)");
        }
        
        com.squareup.okhttp.Call call = getSoDCaseCall(ID, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SOD case information.
     * Accepted Roles: * Auditor * Business Role Manager&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD case ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return SodCase
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SodCase getSoDCase(Long ID, List<String> attrFilter) throws ApiException {
        ApiResponse<SodCase> resp = getSoDCaseWithHttpInfo(ID, attrFilter);
        return resp.getData();
    }

    /**
     * Get the SOD case information.
     * Accepted Roles: * Auditor * Business Role Manager&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD case ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;SodCase&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SodCase> getSoDCaseWithHttpInfo(Long ID, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getSoDCaseValidateBeforeCall(ID, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<SodCase>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SOD case information. (asynchronously)
     * Accepted Roles: * Auditor * Business Role Manager&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD case ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSoDCaseAsync(Long ID, List<String> attrFilter, final ApiCallback<SodCase> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSoDCaseValidateBeforeCall(ID, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SodCase>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSoDCases
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter List of filters the cases by their state. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSoDCasesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, List<String> stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/case";

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
        if (stateFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "stateFilter", stateFilter));

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
    private com.squareup.okhttp.Call getSoDCasesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, List<String> stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSoDCases(Async)");
        }
        
        com.squareup.okhttp.Call call = getSoDCasesCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of SoD cases, also include the information about the case.
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter List of filters the cases by their state. (optional)
     * @return Cases
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Cases getSoDCases(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, List<String> stateFilter) throws ApiException {
        ApiResponse<Cases> resp = getSoDCasesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, stateFilter);
        return resp.getData();
    }

    /**
     * Return list of SoD cases, also include the information about the case.
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter List of filters the cases by their state. (optional)
     * @return ApiResponse&lt;Cases&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Cases> getSoDCasesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, List<String> stateFilter) throws ApiException {
        com.squareup.okhttp.Call call = getSoDCasesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, stateFilter, null, null);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of SoD cases, also include the information about the case. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param size Number of results to return, used for paging. (optional)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt Show total result set count. (optional, default to false)
     * @param stateFilter List of filters the cases by their state. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSoDCasesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, String qMatch, Boolean typeAheadSearch, Boolean showCt, List<String> stateFilter, final ApiCallback<Cases> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSoDCasesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Cases>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSoDPolicies
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param getViolationCounts Flag indicating whether to return violation counts for each policy (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSoDPoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Boolean getViolationCounts, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod";

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
        if (stateFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("stateFilter", stateFilter));
        if (getViolationCounts != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("getViolationCounts", getViolationCounts));

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
    private com.squareup.okhttp.Call getSoDPoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Boolean getViolationCounts, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSoDPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getSoDPoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, getViolationCounts, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of SoD policies, also include the information about policy
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param getViolationCounts Flag indicating whether to return violation counts for each policy (optional, default to false)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies getSoDPolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Boolean getViolationCounts) throws ApiException {
        ApiResponse<Policies> resp = getSoDPoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, getViolationCounts);
        return resp.getData();
    }

    /**
     * Return list of SoD policies, also include the information about policy
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param getViolationCounts Flag indicating whether to return violation counts for each policy (optional, default to false)
     * @return ApiResponse&lt;Policies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Policies> getSoDPoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Boolean getViolationCounts) throws ApiException {
        com.squareup.okhttp.Call call = getSoDPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, getViolationCounts, null, null);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of SoD policies, also include the information about policy (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Separation of Duties owner * Technical Roles Administrator 
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param getViolationCounts Flag indicating whether to return violation counts for each policy (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSoDPoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Boolean getViolationCounts, final ApiCallback<Policies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSoDPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, getViolationCounts, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSodConditionBusinessRolesForUser
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSodConditionBusinessRolesForUserCall(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/{ID}/item/{itemId}/brole"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

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
        if (stateFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("stateFilter", stateFilter));
        if (approvalPolicy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("approvalPolicy", approvalPolicy));

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
    private com.squareup.okhttp.Call getSodConditionBusinessRolesForUserValidateBeforeCall(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSodConditionBusinessRolesForUser(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSodConditionBusinessRolesForUser(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getSodConditionBusinessRolesForUser(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodConditionBusinessRolesForUserCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of Business roles held by the user matching the condition item expression
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getSodConditionBusinessRolesForUser(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy) throws ApiException {
        ApiResponse<Roles> resp = getSodConditionBusinessRolesForUserWithHttpInfo(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy);
        return resp.getData();
    }

    /**
     * Return list of Business roles held by the user matching the condition item expression
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getSodConditionBusinessRolesForUserWithHttpInfo(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy) throws ApiException {
        com.squareup.okhttp.Call call = getSodConditionBusinessRolesForUserValidateBeforeCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of Business roles held by the user matching the condition item expression (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
     * @param stateFilter Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param approvalPolicy Approval policy to filter on, if any. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSodConditionBusinessRolesForUserAsync(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, Long approvalPolicy, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSodConditionBusinessRolesForUserValidateBeforeCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, approvalPolicy, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSodConditionPermissionsForUser
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
    public com.squareup.okhttp.Call getSodConditionPermissionsForUserCall(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/{ID}/item/{itemId}/perm"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

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
    private com.squareup.okhttp.Call getSodConditionPermissionsForUserValidateBeforeCall(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSodConditionPermissionsForUser(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSodConditionPermissionsForUser(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getSodConditionPermissionsForUser(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodConditionPermissionsForUserCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the permissions the given user holds that match the criteria in the given condition item.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
    public Permissions getSodConditionPermissionsForUser(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        ApiResponse<Permissions> resp = getSodConditionPermissionsForUserWithHttpInfo(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt);
        return resp.getData();
    }

    /**
     * Get the permissions the given user holds that match the criteria in the given condition item.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
    public ApiResponse<Permissions> getSodConditionPermissionsForUserWithHttpInfo(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getSodConditionPermissionsForUserValidateBeforeCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, null, null);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the permissions the given user holds that match the criteria in the given condition item. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
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
    public com.squareup.okhttp.Call getSodConditionPermissionsForUserAsync(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean showCt, final ApiCallback<Permissions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSodConditionPermissionsForUserValidateBeforeCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Permissions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSodConditionRolesForUser
     * @param body - Filter the policies by values of specific fields (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSodConditionRolesForUserCall(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/{ID}/item/{itemId}/role"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "itemId" + "\\}", apiClient.escapeString(itemId.toString()));

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
        if (stateFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("stateFilter", stateFilter));

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
    private com.squareup.okhttp.Call getSodConditionRolesForUserValidateBeforeCall(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSodConditionRolesForUser(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getSodConditionRolesForUser(Async)");
        }
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getSodConditionRolesForUser(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodConditionRolesForUserCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of technical roles the given user holds that match the criteria in the given condition item.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @return Roles
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Roles getSodConditionRolesForUser(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter) throws ApiException {
        ApiResponse<Roles> resp = getSodConditionRolesForUserWithHttpInfo(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter);
        return resp.getData();
    }

    /**
     * Return list of technical roles the given user holds that match the criteria in the given condition item.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @return ApiResponse&lt;Roles&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Roles> getSodConditionRolesForUserWithHttpInfo(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter) throws ApiException {
        com.squareup.okhttp.Call call = getSodConditionRolesForUserValidateBeforeCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, null, null);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of technical roles the given user holds that match the criteria in the given condition item. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * SoD Step Approver * Technical Roles Administrator 
     * @param body - Filter the policies by values of specific fields (required)
     * @param ID The Sod Violation Id (required)
     * @param itemId SoD condition item ID. (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param listAttr - List of attributes to be returned and fields used for quick search. (optional)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param stateFilter - Filter the policies by their state. Valid values: ACTIVE, INACTIVE or INVALID (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSodConditionRolesForUserAsync(SearchCriteria body, Long ID, Long itemId, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, List<String> listAttr, List<String> attrFilter, String qMatch, Boolean typeAheadSearch, Boolean showCt, String stateFilter, final ApiCallback<Roles> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSodConditionRolesForUserValidateBeforeCall(body, ID, itemId, size, q, sortBy, sortOrder, indexFrom, listAttr, attrFilter, qMatch, typeAheadSearch, showCt, stateFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Roles>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getSodViolationCounts
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getSodViolationCountsCall(Sodcrit body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/counts";

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
    private com.squareup.okhttp.Call getSodViolationCountsValidateBeforeCall(Sodcrit body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getSodViolationCounts(Async)");
        }
        
        com.squareup.okhttp.Call call = getSodViolationCountsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SoD violation counts.
     * Three counts are returned: 1) Count of SoD policies that would be violated if a user had all of the permissions  and technical roles in the sodCriteria, 2) Count of current SoD violations for the users specified in the sodCriteria, and 3) Count of  potential new SoD violations for the users specified in the sodCriteria if the users had all of the permissions, technical roles, and  business roles specified in the sodCriteria in addition to the permissions, technical roles, and business roles they already have.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @return Sodcounts
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sodcounts getSodViolationCounts(Sodcrit body) throws ApiException {
        ApiResponse<Sodcounts> resp = getSodViolationCountsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Get the SoD violation counts.
     * Three counts are returned: 1) Count of SoD policies that would be violated if a user had all of the permissions  and technical roles in the sodCriteria, 2) Count of current SoD violations for the users specified in the sodCriteria, and 3) Count of  potential new SoD violations for the users specified in the sodCriteria if the users had all of the permissions, technical roles, and  business roles specified in the sodCriteria in addition to the permissions, technical roles, and business roles they already have.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @return ApiResponse&lt;Sodcounts&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sodcounts> getSodViolationCountsWithHttpInfo(Sodcrit body) throws ApiException {
        com.squareup.okhttp.Call call = getSodViolationCountsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Sodcounts>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SoD violation counts. (asynchronously)
     * Three counts are returned: 1) Count of SoD policies that would be violated if a user had all of the permissions  and technical roles in the sodCriteria, 2) Count of current SoD violations for the users specified in the sodCriteria, and 3) Count of  potential new SoD violations for the users specified in the sodCriteria if the users had all of the permissions, technical roles, and  business roles specified in the sodCriteria in addition to the permissions, technical roles, and business roles they already have.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions, technical roles, and user criteria (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getSodViolationCountsAsync(Sodcrit body, final ApiCallback<Sodcounts> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getSodViolationCountsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sodcounts>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getUserSoDCase
     * @param ID SoD policy ID. (required)
     * @param userId User ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getUserSoDCaseCall(Long ID, Long userId, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{ID}/case/user/{userId}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()))
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getUserSoDCaseValidateBeforeCall(Long ID, Long userId, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getUserSoDCase(Async)");
        }
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getUserSoDCase(Async)");
        }
        
        com.squareup.okhttp.Call call = getUserSoDCaseCall(ID, userId, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the SOD case information associated with a SOD policy and a particular user, if any.
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD policy ID. (required)
     * @param userId User ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return SodCase
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SodCase getUserSoDCase(Long ID, Long userId, List<String> attrFilter) throws ApiException {
        ApiResponse<SodCase> resp = getUserSoDCaseWithHttpInfo(ID, userId, attrFilter);
        return resp.getData();
    }

    /**
     * Get the SOD case information associated with a SOD policy and a particular user, if any.
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD policy ID. (required)
     * @param userId User ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @return ApiResponse&lt;SodCase&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SodCase> getUserSoDCaseWithHttpInfo(Long ID, Long userId, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = getUserSoDCaseValidateBeforeCall(ID, userId, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<SodCase>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the SOD case information associated with a SOD policy and a particular user, if any. (asynchronously)
     * Accepted Roles: * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param ID SoD policy ID. (required)
     * @param userId User ID. (required)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getUserSoDCaseAsync(Long ID, Long userId, List<String> attrFilter, final ApiCallback<SodCase> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getUserSoDCaseValidateBeforeCall(ID, userId, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SodCase>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getViolatedPolicies
     * @param body SoD criteria - includes list of permissions and technical roles (required)
     * @param listAttr List of SoD attributes to be returned (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @param qMatch Matching mode for filterString (optional)
     * @param stateFilter Policy state filter. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getViolatedPoliciesCall(Sodcrit body, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, String qMatch, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/violation/policies";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (qMatch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("qMatch", qMatch));
        if (stateFilter != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("stateFilter", stateFilter));

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
    private com.squareup.okhttp.Call getViolatedPoliciesValidateBeforeCall(Sodcrit body, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, String qMatch, String stateFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getViolatedPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getViolatedPoliciesCall(body, listAttr, attrFilter, indexFrom, size, sortBy, sortOrder, q, qMatch, stateFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the list of SoD policies that would be violated if a user had the permissions and technical roles specified in sodCriteria.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions and technical roles (required)
     * @param listAttr List of SoD attributes to be returned (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @param qMatch Matching mode for filterString (optional)
     * @param stateFilter Policy state filter. (optional)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies getViolatedPolicies(Sodcrit body, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, String qMatch, String stateFilter) throws ApiException {
        ApiResponse<Policies> resp = getViolatedPoliciesWithHttpInfo(body, listAttr, attrFilter, indexFrom, size, sortBy, sortOrder, q, qMatch, stateFilter);
        return resp.getData();
    }

    /**
     * Get the list of SoD policies that would be violated if a user had the permissions and technical roles specified in sodCriteria.
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions and technical roles (required)
     * @param listAttr List of SoD attributes to be returned (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @param qMatch Matching mode for filterString (optional)
     * @param stateFilter Policy state filter. (optional)
     * @return ApiResponse&lt;Policies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Policies> getViolatedPoliciesWithHttpInfo(Sodcrit body, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, String qMatch, String stateFilter) throws ApiException {
        com.squareup.okhttp.Call call = getViolatedPoliciesValidateBeforeCall(body, listAttr, attrFilter, indexFrom, size, sortBy, sortOrder, q, qMatch, stateFilter, null, null);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the list of SoD policies that would be violated if a user had the permissions and technical roles specified in sodCriteria. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller * Auditor * Business Role Approver * Business Role Fulfiller * Business Role Manager * Business Role owner * Business Roles Administrator * Change Request Fulfiller * Customer Administrator * Data Administrator * Escalation Reviewer * Fulfillment Administrator * Historical Reviewer * Review Administrator * Review Auditor * Review Monitor * Review Owner * Reviewer * Role owner * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body SoD criteria - includes list of permissions and technical roles (required)
     * @param listAttr List of SoD attributes to be returned (optional)
     * @param attrFilter List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param indexFrom Starting row to return in result set. 0 is first. (optional, default to 0)
     * @param size Number of rows to return. 0 means return all rows. (optional, default to 10)
     * @param sortBy Sort attribute (optional)
     * @param sortOrder Sort order (optional, default to ASC)
     * @param q Additional filter to be applied to quick search attributes on SoD policies. (optional)
     * @param qMatch Matching mode for filterString (optional)
     * @param stateFilter Policy state filter. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getViolatedPoliciesAsync(Sodcrit body, List<String> listAttr, List<String> attrFilter, Integer indexFrom, Integer size, String sortBy, String sortOrder, String q, String qMatch, String stateFilter, final ApiCallback<Policies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getViolatedPoliciesValidateBeforeCall(body, listAttr, attrFilter, indexFrom, size, sortBy, sortOrder, q, qMatch, stateFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getViolatingAccounts
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getViolatingAccountsCall(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{ID}/accounts"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
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
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));

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
    private com.squareup.okhttp.Call getViolatingAccountsValidateBeforeCall(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getViolatingAccounts(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getViolatingAccounts(Async)");
        }
        
        com.squareup.okhttp.Call call = getViolatingAccountsCall(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of accounts that are in violation of a particular SoD policy.
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @return AccountListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AccountListNode getViolatingAccounts(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch) throws ApiException {
        ApiResponse<AccountListNode> resp = getViolatingAccountsWithHttpInfo(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch);
        return resp.getData();
    }

    /**
     * Return list of accounts that are in violation of a particular SoD policy.
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @return ApiResponse&lt;AccountListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AccountListNode> getViolatingAccountsWithHttpInfo(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch) throws ApiException {
        com.squareup.okhttp.Call call = getViolatingAccountsValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, null, null);
        Type localVarReturnType = new TypeToken<AccountListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of accounts that are in violation of a particular SoD policy. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getViolatingAccountsAsync(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, final ApiCallback<AccountListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getViolatingAccountsValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AccountListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getViolatingUsers
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getViolatingUsersCall(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{ID}/users"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (listAttr != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "listAttr", listAttr));
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
        if (typeAheadSearch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("typeAheadSearch", typeAheadSearch));

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
    private com.squareup.okhttp.Call getViolatingUsersValidateBeforeCall(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getViolatingUsers(Async)");
        }
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling getViolatingUsers(Async)");
        }
        
        com.squareup.okhttp.Call call = getViolatingUsersCall(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of users that are in violation of a particular SoD policy.
     * Accepted Roles: * Auditor * Customer Administrator * Role owner * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @return Users
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Users getViolatingUsers(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch) throws ApiException {
        ApiResponse<Users> resp = getViolatingUsersWithHttpInfo(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch);
        return resp.getData();
    }

    /**
     * Return list of users that are in violation of a particular SoD policy.
     * Accepted Roles: * Auditor * Customer Administrator * Role owner * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @return ApiResponse&lt;Users&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Users> getViolatingUsersWithHttpInfo(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch) throws ApiException {
        com.squareup.okhttp.Call call = getViolatingUsersValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, null, null);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of users that are in violation of a particular SoD policy. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Role owner * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver 
     * @param body list of attribute keys and values to use as search criteria (required)
     * @param ID The ID of the SoD policy. (required)
     * @param sortBy The attribute to sort by. (optional)
     * @param sortOrder Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param listAttr List of attributes to be returned and fields used for quick search. (optional)
     * @param indexFrom Starting row index in result set. 0 is the first. (optional, default to 0)
     * @param size Number of results to return, used for paging. (optional, default to 10)
     * @param showCt Show total result set count. (optional, default to false)
     * @param q Quick search filter, used to search across all listAttribute fields. (optional)
     * @param qMatch Match mode. Valid values:   ANY, START or EXACT (optional)
     * @param typeAheadSearch Search against attributes enabled for type ahead search. (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getViolatingUsersAsync(SearchCriteria body, Long ID, String sortBy, String sortOrder, List<String> listAttr, Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, Boolean typeAheadSearch, final ApiCallback<Users> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getViolatingUsersValidateBeforeCall(body, ID, sortBy, sortOrder, listAttr, indexFrom, size, showCt, q, qMatch, typeAheadSearch, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Users>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for importZip
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipCall(File uploadedInputStream, Boolean refs, Boolean reportOnly, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/import";

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
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip(File uploadedInputStream, Boolean refs, Boolean reportOnly) throws ApiException {
        ApiResponse<Response> resp = importZipWithHttpInfo(uploadedInputStream, refs, reportOnly);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
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
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param refs the import refs (optional, default to false)
     * @param reportOnly the report only (optional, default to false)
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
     * Build call for importZipResolveApps
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call importZipResolveAppsCall(File uploadedInputStream, Boolean build, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/import/resolve/apps";

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
    private com.squareup.okhttp.Call importZipResolveAppsValidateBeforeCall(File uploadedInputStream, Boolean build, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'uploadedInputStream' is set
        if (uploadedInputStream == null) {
            throw new ApiException("Missing the required parameter 'uploadedInputStream' when calling importZipResolveApps(Async)");
        }
        
        com.squareup.okhttp.Call call = importZipResolveAppsCall(uploadedInputStream, build, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZipResolveApps(File uploadedInputStream, Boolean build) throws ApiException {
        ApiResponse<Response> resp = importZipResolveAppsWithHttpInfo(uploadedInputStream, build);
        return resp.getData();
    }

    /**
     * Resolve Applications in the import document
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> importZipResolveAppsWithHttpInfo(File uploadedInputStream, Boolean build) throws ApiException {
        com.squareup.okhttp.Call call = importZipResolveAppsValidateBeforeCall(uploadedInputStream, build, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Resolve Applications in the import document (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @param build the build application list (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call importZipResolveAppsAsync(File uploadedInputStream, Boolean build, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = importZipResolveAppsValidateBeforeCall(uploadedInputStream, build, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/sod/import/preview";

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
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param uploadedInputStream  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response importZip_0(File uploadedInputStream) throws ApiException {
        ApiResponse<Response> resp = importZip_0WithHttpInfo(uploadedInputStream);
        return resp.getData();
    }

    /**
     * Perform an import preview which will attempt to resolve objects and object references.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
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
     * Perform an import preview which will attempt to resolve objects and object references. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
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
     * Build call for setSodPoliciesOwner
     * @param body The list of sod policies Nodes which to set the owner for. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setSodPoliciesOwnerCall(Policies body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/bulk/policyOwner";

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
    private com.squareup.okhttp.Call setSodPoliciesOwnerValidateBeforeCall(Policies body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setSodPoliciesOwner(Async)");
        }
        
        com.squareup.okhttp.Call call = setSodPoliciesOwnerCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the owners of the given sod policy IDs.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The list of sod policies Nodes which to set the owner for. (required)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies setSodPoliciesOwner(Policies body) throws ApiException {
        ApiResponse<Policies> resp = setSodPoliciesOwnerWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Set the owners of the given sod policy IDs.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The list of sod policies Nodes which to set the owner for. (required)
     * @return ApiResponse&lt;Policies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Policies> setSodPoliciesOwnerWithHttpInfo(Policies body) throws ApiException {
        com.squareup.okhttp.Call call = setSodPoliciesOwnerValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the owners of the given sod policy IDs. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The list of sod policies Nodes which to set the owner for. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setSodPoliciesOwnerAsync(Policies body, final ApiCallback<Policies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setSodPoliciesOwnerValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for setSodPoliciesState
     * @param body The list of sod policy Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call setSodPoliciesStateCall(Policies body, String state, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/bulk/policyState";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (state != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state", state));

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
    private com.squareup.okhttp.Call setSodPoliciesStateValidateBeforeCall(Policies body, String state, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling setSodPoliciesState(Async)");
        }
        
        com.squareup.okhttp.Call call = setSodPoliciesStateCall(body, state, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Set the state of the given IDs.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The list of sod policy Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @return Policies
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Policies setSodPoliciesState(Policies body, String state) throws ApiException {
        ApiResponse<Policies> resp = setSodPoliciesStateWithHttpInfo(body, state);
        return resp.getData();
    }

    /**
     * Set the state of the given IDs.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The list of sod policy Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @return ApiResponse&lt;Policies&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Policies> setSodPoliciesStateWithHttpInfo(Policies body, String state) throws ApiException {
        com.squareup.okhttp.Call call = setSodPoliciesStateValidateBeforeCall(body, state, null, null);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Set the state of the given IDs. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The list of sod policy Nodes which to set the state for. (required)
     * @param state The policy state: ACTIVE, INACTIVE (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call setSodPoliciesStateAsync(Policies body, String state, final ApiCallback<Policies> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = setSodPoliciesStateValidateBeforeCall(body, state, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Policies>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param id - The SoDs to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(List<Long> id, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/sod/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (id != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "id", id));
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (roles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roles", roles));
        if (broles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("broles", broles));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(List<Long> id, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = startDownloadCall(id, refs, roles, broles, apps, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the SoD download
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param id - The SoDs to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(List<Long> id, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(id, refs, roles, broles, apps, attrFilter);
        return resp.getData();
    }

    /**
     * Start the SoD download
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param id - The SoDs to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(List<Long> id, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, refs, roles, broles, apps, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the SoD download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param id - The SoDs to export (optional)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(List<Long> id, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, refs, roles, broles, apps, attrFilter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload_0
     * @param body -  The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Call(Download body, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/download";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (refs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("refs", refs));
        if (roles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("roles", roles));
        if (broles != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("broles", broles));
        if (apps != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("apps", apps));
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
    private com.squareup.okhttp.Call startDownload_0ValidateBeforeCall(Download body, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload_0(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownload_0Call(body, refs, roles, broles, apps, attrFilter, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start the SoD download
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body -  The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body, refs, roles, broles, apps, attrFilter);
        return resp.getData();
    }

    /**
     * Start the SoD download
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body -  The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownload_0WithHttpInfo(Download body, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter) throws ApiException {
        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, refs, roles, broles, apps, attrFilter, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the SoD download (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body -  The download request node (required)
     * @param refs - Flag to include owner references in export file (optional, default to false)
     * @param roles - Flag to export roles referenced by the SoD (optional, default to false)
     * @param broles - Flag to export business roles referenced by the (optional, default to false)
     * @param apps - Flag to export applications referenced by SPermissions in the SoD policy (optional, default to false)
     * @param attrFilter - List of attribute filters to return in the payload: listable, curatable, quickInfo (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownload_0Async(Download body, Boolean refs, Boolean roles, Boolean broles, Boolean apps, List<String> attrFilter, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownload_0ValidateBeforeCall(body, refs, roles, broles, apps, attrFilter, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/sod/download/{id}/status"
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
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
     * Accepted Roles: * Auditor * Customer Administrator * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator * Separation of Duties owner * SoD Step Approver * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
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
     * Build call for updateSoDPolicy
     * @param body The updates to the SoD policy. (required)
     * @param id The SoD policy ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateSoDPolicyCall(Sod body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/{id}"
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
    private com.squareup.okhttp.Call updateSoDPolicyValidateBeforeCall(Sod body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateSoDPolicy(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateSoDPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = updateSoDPolicyCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update SoD policy.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The updates to the SoD policy. (required)
     * @param id The SoD policy ID (required)
     * @return Sod
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Sod updateSoDPolicy(Sod body, Long id) throws ApiException {
        ApiResponse<Sod> resp = updateSoDPolicyWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update SoD policy.
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The updates to the SoD policy. (required)
     * @param id The SoD policy ID (required)
     * @return ApiResponse&lt;Sod&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Sod> updateSoDPolicyWithHttpInfo(Sod body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateSoDPolicyValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update SoD policy. (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The updates to the SoD policy. (required)
     * @param id The SoD policy ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateSoDPolicyAsync(Sod body, Long id, final ApiCallback<Sod> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateSoDPolicyValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Sod>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for validateConditions
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call validateConditionsCall(Sod body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/sod/validate";

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
    private com.squareup.okhttp.Call validateConditionsValidateBeforeCall(Sod body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling validateConditions(Async)");
        }
        
        com.squareup.okhttp.Call call = validateConditionsCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Validate the sod condition items
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @return PolicyDetectionPreview
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PolicyDetectionPreview validateConditions(Sod body) throws ApiException {
        ApiResponse<PolicyDetectionPreview> resp = validateConditionsWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Validate the sod condition items
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @return ApiResponse&lt;PolicyDetectionPreview&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PolicyDetectionPreview> validateConditionsWithHttpInfo(Sod body) throws ApiException {
        com.squareup.okhttp.Call call = validateConditionsValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Validate the sod condition items (asynchronously)
     * Accepted Roles: * Customer Administrator * Separation of Duties Administrator 
     * @param body The SoD policy containing the conditions to use to estimate users and accounts. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call validateConditionsAsync(Sod body, final ApiCallback<PolicyDetectionPreview> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = validateConditionsValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PolicyDetectionPreview>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
