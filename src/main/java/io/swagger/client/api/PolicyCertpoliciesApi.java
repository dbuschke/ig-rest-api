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


import io.swagger.client.model.CertPolicyCalcNode;
import io.swagger.client.model.CertPolicyListNode;
import io.swagger.client.model.CertPolicyNode;
import io.swagger.client.model.CertPolicyVioListNode;
import io.swagger.client.model.Download;
import java.io.File;
import io.swagger.client.model.Response;
import io.swagger.client.model.SearchCriteria;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyCertpoliciesApi {
    private ApiClient apiClient;

    public PolicyCertpoliciesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PolicyCertpoliciesApi(ApiClient apiClient) {
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
    public com.squareup.okhttp.Call cancelCalculationTaskCall(CertPolicyCalcNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/cancel/{id}"
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
    private com.squareup.okhttp.Call cancelCalculationTaskValidateBeforeCall(CertPolicyCalcNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - reason calculation is being interrupted (required)
     * @param id - calculation task id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response cancelCalculationTask(CertPolicyCalcNode body, Long id) throws ApiException {
        ApiResponse<Response> resp = cancelCalculationTaskWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Make request to cancel calculation task
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - reason calculation is being interrupted (required)
     * @param id - calculation task id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> cancelCalculationTaskWithHttpInfo(CertPolicyCalcNode body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = cancelCalculationTaskValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Make request to cancel calculation task (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - reason calculation is being interrupted (required)
     * @param id - calculation task id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cancelCalculationTaskAsync(CertPolicyCalcNode body, Long id, final ApiCallback<Response> callback) throws ApiException {

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
        String localVarPath = "/policy/certpolicies/download/{id}"
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
     * Cancel a download of Certification Policies.
     * Accepted Roles: * Customer Administrator 
     * @param id - The download id to delete (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response cancelDownload(String id) throws ApiException {
        ApiResponse<Response> resp = cancelDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Cancel a download of Certification Policies.
     * Accepted Roles: * Customer Administrator 
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
     * Cancel a download of Certification Policies. (asynchronously)
     * Accepted Roles: * Customer Administrator 
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
     * Build call for deleteCertPolicy
     * @param id -  certification police id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteCertPolicyCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/{id}"
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
    private com.squareup.okhttp.Call deleteCertPolicyValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteCertPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteCertPolicyCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete certification policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id -  certification police id (required)
     * @return CertPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CertPolicyNode deleteCertPolicy(Long id) throws ApiException {
        ApiResponse<CertPolicyNode> resp = deleteCertPolicyWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete certification policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id -  certification police id (required)
     * @return ApiResponse&lt;CertPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CertPolicyNode> deleteCertPolicyWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteCertPolicyValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete certification policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id -  certification police id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteCertPolicyAsync(Long id, final ApiCallback<CertPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteCertPolicyValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
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
        String localVarPath = "/policy/certpolicies/download/{id}"
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
     * Get the file of downloaded Certification Policies.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - The download id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response finishDownload(String id) throws ApiException {
        ApiResponse<Response> resp = finishDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the file of downloaded Certification Policies.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
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
     * Get the file of downloaded Certification Policies. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
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
     * Build call for getCertPolicies
     * @param body - advanced filter criteria (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param showRun -  Show last run (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCertPoliciesCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showRun, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies";

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
        if (showRun != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showRun", showRun));

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
    private com.squareup.okhttp.Call getCertPoliciesValidateBeforeCall(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showRun, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getCertPolicies(Async)");
        }
        
        com.squareup.okhttp.Call call = getCertPoliciesCall(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, showRun, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Return list of certification policies
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - advanced filter criteria (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param showRun -  Show last run (optional, default to false)
     * @return CertPolicyListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CertPolicyListNode getCertPolicies(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showRun) throws ApiException {
        ApiResponse<CertPolicyListNode> resp = getCertPoliciesWithHttpInfo(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, showRun);
        return resp.getData();
    }

    /**
     * Return list of certification policies
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - advanced filter criteria (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param showRun -  Show last run (optional, default to false)
     * @return ApiResponse&lt;CertPolicyListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CertPolicyListNode> getCertPoliciesWithHttpInfo(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showRun) throws ApiException {
        com.squareup.okhttp.Call call = getCertPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, showRun, null, null);
        Type localVarReturnType = new TypeToken<CertPolicyListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Return list of certification policies (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - advanced filter criteria (required)
     * @param size - Number of results to return, used for paging. (optional, default to 0)
     * @param q - Quick search filter, used to search across all listAttribute fields. (optional)
     * @param sortBy - The attribute to sort by. (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param indexFrom - Starting row index in result set. 0 is the first, used for paging. (optional, default to 0)
     * @param qMatch - Match mode. Valid values:   ANY, START or EXACT (optional, default to ANY)
     * @param typeAheadSearch - Search against attributes enabled for type ahead search. (optional, default to false)
     * @param showCt - Show total result set count. (optional, default to false)
     * @param showRun -  Show last run (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCertPoliciesAsync(SearchCriteria body, Integer size, String q, String sortBy, String sortOrder, Integer indexFrom, String qMatch, Boolean typeAheadSearch, Boolean showCt, Boolean showRun, final ApiCallback<CertPolicyListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCertPoliciesValidateBeforeCall(body, size, q, sortBy, sortOrder, indexFrom, qMatch, typeAheadSearch, showCt, showRun, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CertPolicyListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCertPolicy
     * @param id - certification policy id (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyCall(Long id, String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

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
    private com.squareup.okhttp.Call getCertPolicyValidateBeforeCall(Long id, String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getCertPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = getCertPolicyCall(id, uniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get certification policy by specified id or uniqueId
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy id (required)
     * @param uniqueId - certification policy unique id (optional)
     * @return CertPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CertPolicyNode getCertPolicy(Long id, String uniqueId) throws ApiException {
        ApiResponse<CertPolicyNode> resp = getCertPolicyWithHttpInfo(id, uniqueId);
        return resp.getData();
    }

    /**
     * Get certification policy by specified id or uniqueId
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy id (required)
     * @param uniqueId - certification policy unique id (optional)
     * @return ApiResponse&lt;CertPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CertPolicyNode> getCertPolicyWithHttpInfo(Long id, String uniqueId) throws ApiException {
        com.squareup.okhttp.Call call = getCertPolicyValidateBeforeCall(id, uniqueId, null, null);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get certification policy by specified id or uniqueId (asynchronously)
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy id (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyAsync(Long id, String uniqueId, final ApiCallback<CertPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCertPolicyValidateBeforeCall(id, uniqueId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCertPolicyCalcStatus
     * @param id - certification policy calculation (data production) id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyCalcStatusCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/calcStatus/{id}"
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
    private com.squareup.okhttp.Call getCertPolicyCalcStatusValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getCertPolicyCalcStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = getCertPolicyCalcStatusCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get certification policy calculation status
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy calculation (data production) id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getCertPolicyCalcStatus(Long id) throws ApiException {
        ApiResponse<Response> resp = getCertPolicyCalcStatusWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get certification policy calculation status
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy calculation (data production) id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getCertPolicyCalcStatusWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getCertPolicyCalcStatusValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get certification policy calculation status (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy calculation (data production) id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyCalcStatusAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCertPolicyCalcStatusValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCertPolicyStatus
     * @param uniqueId - certification policy unique id (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyStatusCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/status";

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
    private com.squareup.okhttp.Call getCertPolicyStatusValidateBeforeCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getCertPolicyStatusCall(uniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get certification policy status
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getCertPolicyStatus(String uniqueId) throws ApiException {
        ApiResponse<Response> resp = getCertPolicyStatusWithHttpInfo(uniqueId);
        return resp.getData();
    }

    /**
     * Get certification policy status
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getCertPolicyStatusWithHttpInfo(String uniqueId) throws ApiException {
        com.squareup.okhttp.Call call = getCertPolicyStatusValidateBeforeCall(uniqueId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get certification policy status (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyStatusAsync(String uniqueId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCertPolicyStatusValidateBeforeCall(uniqueId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCertPolicyViolations
     * @param body -  advanced filter criteria (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - optional entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param indexFrom - start index (optional, default to 0)
     * @param size - number of records (optional, default to 15)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyViolationsCall(SearchCriteria body, String uniqueId, String entityType, Integer indexFrom, Integer size, String q, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/violations";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (uniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueId", uniqueId));
        if (entityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityType", entityType));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
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
    private com.squareup.okhttp.Call getCertPolicyViolationsValidateBeforeCall(SearchCriteria body, String uniqueId, String entityType, Integer indexFrom, Integer size, String q, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getCertPolicyViolations(Async)");
        }
        
        com.squareup.okhttp.Call call = getCertPolicyViolationsCall(body, uniqueId, entityType, indexFrom, size, q, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get certification policy violations by certification policy unique id
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param body -  advanced filter criteria (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - optional entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param indexFrom - start index (optional, default to 0)
     * @param size - number of records (optional, default to 15)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @return CertPolicyVioListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CertPolicyVioListNode getCertPolicyViolations(SearchCriteria body, String uniqueId, String entityType, Integer indexFrom, Integer size, String q, Boolean showCt) throws ApiException {
        ApiResponse<CertPolicyVioListNode> resp = getCertPolicyViolationsWithHttpInfo(body, uniqueId, entityType, indexFrom, size, q, showCt);
        return resp.getData();
    }

    /**
     * Get certification policy violations by certification policy unique id
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param body -  advanced filter criteria (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - optional entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param indexFrom - start index (optional, default to 0)
     * @param size - number of records (optional, default to 15)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @return ApiResponse&lt;CertPolicyVioListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CertPolicyVioListNode> getCertPolicyViolationsWithHttpInfo(SearchCriteria body, String uniqueId, String entityType, Integer indexFrom, Integer size, String q, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getCertPolicyViolationsValidateBeforeCall(body, uniqueId, entityType, indexFrom, size, q, showCt, null, null);
        Type localVarReturnType = new TypeToken<CertPolicyVioListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get certification policy violations by certification policy unique id (asynchronously)
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param body -  advanced filter criteria (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - optional entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param indexFrom - start index (optional, default to 0)
     * @param size - number of records (optional, default to 15)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyViolationsAsync(SearchCriteria body, String uniqueId, String entityType, Integer indexFrom, Integer size, String q, Boolean showCt, final ApiCallback<CertPolicyVioListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCertPolicyViolationsValidateBeforeCall(body, uniqueId, entityType, indexFrom, size, q, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CertPolicyVioListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getCertPolicyViolationsDetail
     * @param body -  advanced search filter (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param entityUniqueId - entity unique id (optional)
     * @param sortBy -  sort attribute (optional, default to violationType)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 1000)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyViolationsDetailCall(SearchCriteria body, String uniqueId, String entityType, String entityUniqueId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/violations/detail";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (uniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueId", uniqueId));
        if (entityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityType", entityType));
        if (entityUniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityUniqueId", entityUniqueId));
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
    private com.squareup.okhttp.Call getCertPolicyViolationsDetailValidateBeforeCall(SearchCriteria body, String uniqueId, String entityType, String entityUniqueId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getCertPolicyViolationsDetail(Async)");
        }
        
        com.squareup.okhttp.Call call = getCertPolicyViolationsDetailCall(body, uniqueId, entityType, entityUniqueId, sortBy, sortOrder, indexFrom, size, q, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get certification policy violations by certification policy unique id
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param body -  advanced search filter (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param entityUniqueId - entity unique id (optional)
     * @param sortBy -  sort attribute (optional, default to violationType)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 1000)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @return CertPolicyVioListNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CertPolicyVioListNode getCertPolicyViolationsDetail(SearchCriteria body, String uniqueId, String entityType, String entityUniqueId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt) throws ApiException {
        ApiResponse<CertPolicyVioListNode> resp = getCertPolicyViolationsDetailWithHttpInfo(body, uniqueId, entityType, entityUniqueId, sortBy, sortOrder, indexFrom, size, q, showCt);
        return resp.getData();
    }

    /**
     * Get certification policy violations by certification policy unique id
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param body -  advanced search filter (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param entityUniqueId - entity unique id (optional)
     * @param sortBy -  sort attribute (optional, default to violationType)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 1000)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @return ApiResponse&lt;CertPolicyVioListNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CertPolicyVioListNode> getCertPolicyViolationsDetailWithHttpInfo(SearchCriteria body, String uniqueId, String entityType, String entityUniqueId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getCertPolicyViolationsDetailValidateBeforeCall(body, uniqueId, entityType, entityUniqueId, sortBy, sortOrder, indexFrom, size, q, showCt, null, null);
        Type localVarReturnType = new TypeToken<CertPolicyVioListNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get certification policy violations by certification policy unique id (asynchronously)
     * Accepted Roles: * Auditor * Catalog Fulfiller * Customer Administrator * Data Administrator * Review Administrator 
     * @param body -  advanced search filter (required)
     * @param uniqueId - certification policy unique id (optional)
     * @param entityType - entity type, like USER, ACCOUNT, PERMISSION (optional)
     * @param entityUniqueId - entity unique id (optional)
     * @param sortBy -  sort attribute (optional, default to violationType)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 1000)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getCertPolicyViolationsDetailAsync(SearchCriteria body, String uniqueId, String entityType, String entityUniqueId, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt, final ApiCallback<CertPolicyVioListNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getCertPolicyViolationsDetailValidateBeforeCall(body, uniqueId, entityType, entityUniqueId, sortBy, sortOrder, indexFrom, size, q, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CertPolicyVioListNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEntityCertification
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param onlyLastCert - true/false flag to show only last certified items (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getEntityCertificationCall(String entityType, String uniqueId, String certType, Boolean onlyLastCert, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/entityCertification";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (entityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityType", entityType));
        if (uniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueId", uniqueId));
        if (certType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certType", certType));
        if (onlyLastCert != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("onlyLastCert", onlyLastCert));

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
    private com.squareup.okhttp.Call getEntityCertificationValidateBeforeCall(String entityType, String uniqueId, String certType, Boolean onlyLastCert, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getEntityCertificationCall(entityType, uniqueId, certType, onlyLastCert, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Certification item counts for the specified entity.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Roles Administrator * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param onlyLastCert - true/false flag to show only last certified items (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getEntityCertification(String entityType, String uniqueId, String certType, Boolean onlyLastCert) throws ApiException {
        ApiResponse<Response> resp = getEntityCertificationWithHttpInfo(entityType, uniqueId, certType, onlyLastCert);
        return resp.getData();
    }

    /**
     * Get Certification item counts for the specified entity.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Roles Administrator * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param onlyLastCert - true/false flag to show only last certified items (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getEntityCertificationWithHttpInfo(String entityType, String uniqueId, String certType, Boolean onlyLastCert) throws ApiException {
        com.squareup.okhttp.Call call = getEntityCertificationValidateBeforeCall(entityType, uniqueId, certType, onlyLastCert, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Certification item counts for the specified entity. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Roles Administrator * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param onlyLastCert - true/false flag to show only last certified items (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEntityCertificationAsync(String entityType, String uniqueId, String certType, Boolean onlyLastCert, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEntityCertificationValidateBeforeCall(entityType, uniqueId, certType, onlyLastCert, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEntityCertificationDetail
     * @param body -  advanced search filter (required)
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param certId - review instance or certification policy id (optional)
     * @param certVioType - final decision for review or violation type for certification policy (optional)
     * @param sortBy -  sort attribute (optional)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 0)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getEntityCertificationDetailCall(SearchCriteria body, String entityType, String uniqueId, String certType, Long certId, String certVioType, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/entityCertification/detail";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (entityType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("entityType", entityType));
        if (uniqueId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("uniqueId", uniqueId));
        if (certType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certType", certType));
        if (certId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certId", certId));
        if (certVioType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("certVioType", certVioType));
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
    private com.squareup.okhttp.Call getEntityCertificationDetailValidateBeforeCall(SearchCriteria body, String entityType, String uniqueId, String certType, Long certId, String certVioType, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getEntityCertificationDetail(Async)");
        }
        
        com.squareup.okhttp.Call call = getEntityCertificationDetailCall(body, entityType, uniqueId, certType, certId, certVioType, sortBy, sortOrder, indexFrom, size, q, showCt, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get certification status for the specified entity.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Roles Administrator * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body -  advanced search filter (required)
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param certId - review instance or certification policy id (optional)
     * @param certVioType - final decision for review or violation type for certification policy (optional)
     * @param sortBy -  sort attribute (optional)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 0)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getEntityCertificationDetail(SearchCriteria body, String entityType, String uniqueId, String certType, Long certId, String certVioType, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt) throws ApiException {
        ApiResponse<Response> resp = getEntityCertificationDetailWithHttpInfo(body, entityType, uniqueId, certType, certId, certVioType, sortBy, sortOrder, indexFrom, size, q, showCt);
        return resp.getData();
    }

    /**
     * Get certification status for the specified entity.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Roles Administrator * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body -  advanced search filter (required)
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param certId - review instance or certification policy id (optional)
     * @param certVioType - final decision for review or violation type for certification policy (optional)
     * @param sortBy -  sort attribute (optional)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 0)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getEntityCertificationDetailWithHttpInfo(SearchCriteria body, String entityType, String uniqueId, String certType, Long certId, String certVioType, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt) throws ApiException {
        com.squareup.okhttp.Call call = getEntityCertificationDetailValidateBeforeCall(body, entityType, uniqueId, certType, certId, certVioType, sortBy, sortOrder, indexFrom, size, q, showCt, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get certification status for the specified entity. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Business Roles Administrator * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body -  advanced search filter (required)
     * @param entityType - entity type like USER, PERMISSION, ACCOUNT (optional)
     * @param uniqueId - entity unique id (optional)
     * @param certType - certification type: REVIEW or CERT_POLICY (optional)
     * @param certId - review instance or certification policy id (optional)
     * @param certVioType - final decision for review or violation type for certification policy (optional)
     * @param sortBy -  sort attribute (optional)
     * @param sortOrder - sort order, ASC or DESC (optional, default to ASC)
     * @param indexFrom - start index for result set (optional, default to 0)
     * @param size - number returned records (optional, default to 0)
     * @param q - search string (optional)
     * @param showCt - show total result set count (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEntityCertificationDetailAsync(SearchCriteria body, String entityType, String uniqueId, String certType, Long certId, String certVioType, String sortBy, String sortOrder, Integer indexFrom, Integer size, String q, Boolean showCt, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEntityCertificationDetailValidateBeforeCall(body, entityType, uniqueId, certType, certId, certVioType, sortBy, sortOrder, indexFrom, size, q, showCt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEstimatedCertPolicyViolations
     * @param uniqueId - certification policy unique id (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getEstimatedCertPolicyViolationsCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/estimatedViolations";

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
    private com.squareup.okhttp.Call getEstimatedCertPolicyViolationsValidateBeforeCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getEstimatedCertPolicyViolationsCall(uniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get estimated violations from never reviewed items.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getEstimatedCertPolicyViolations(String uniqueId) throws ApiException {
        ApiResponse<Response> resp = getEstimatedCertPolicyViolationsWithHttpInfo(uniqueId);
        return resp.getData();
    }

    /**
     * Get estimated violations from never reviewed items.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getEstimatedCertPolicyViolationsWithHttpInfo(String uniqueId) throws ApiException {
        com.squareup.okhttp.Call call = getEstimatedCertPolicyViolationsValidateBeforeCall(uniqueId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get estimated violations from never reviewed items. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEstimatedCertPolicyViolationsAsync(String uniqueId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEstimatedCertPolicyViolationsValidateBeforeCall(uniqueId, progressListener, progressRequestListener);
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
        String localVarPath = "/policy/certpolicies/import";

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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
        String localVarPath = "/policy/certpolicies/import/resolve/entities";

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
     * Resolve entities such as review definitions, users and groups in the import document
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Resolve entities such as review definitions, users and groups in the import document
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Resolve entities such as review definitions, users and groups in the import document (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
        String localVarPath = "/policy/certpolicies/import/preview";

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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Build call for newCertPolicy
     * @param body - certification policy node (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call newCertPolicyCall(CertPolicyNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/new";

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
    private com.squareup.okhttp.Call newCertPolicyValidateBeforeCall(CertPolicyNode body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling newCertPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = newCertPolicyCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create new certification policy
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - certification policy node (required)
     * @return CertPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CertPolicyNode newCertPolicy(CertPolicyNode body) throws ApiException {
        ApiResponse<CertPolicyNode> resp = newCertPolicyWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create new certification policy
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - certification policy node (required)
     * @return ApiResponse&lt;CertPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CertPolicyNode> newCertPolicyWithHttpInfo(CertPolicyNode body) throws ApiException {
        com.squareup.okhttp.Call call = newCertPolicyValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create new certification policy (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - certification policy node (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call newCertPolicyAsync(CertPolicyNode body, final ApiCallback<CertPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = newCertPolicyValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for runCertPolicyCalculation
     * @param uniqueId - certification policy unique id (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call runCertPolicyCalculationCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/calculate";

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
    private com.squareup.okhttp.Call runCertPolicyCalculationValidateBeforeCall(String uniqueId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = runCertPolicyCalculationCall(uniqueId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Calculate certification policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response runCertPolicyCalculation(String uniqueId) throws ApiException {
        ApiResponse<Response> resp = runCertPolicyCalculationWithHttpInfo(uniqueId);
        return resp.getData();
    }

    /**
     * Calculate certification policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> runCertPolicyCalculationWithHttpInfo(String uniqueId) throws ApiException {
        com.squareup.okhttp.Call call = runCertPolicyCalculationValidateBeforeCall(uniqueId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Calculate certification policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param uniqueId - certification policy unique id (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call runCertPolicyCalculationAsync(String uniqueId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = runCertPolicyCalculationValidateBeforeCall(uniqueId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param id - certification policy ids list (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(List<Long> id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/download";

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
     * Start the certification policy download.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy ids list (optional)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(List<Long> id) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Start the certification policy download.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy ids list (optional)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(List<Long> id) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start the certification policy download. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - certification policy ids list (optional)
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
        String localVarPath = "/policy/certpolicies/download";

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
     * Start the certification policy download.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
     * @param body -  The download request node (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload_0(Download body) throws ApiException {
        ApiResponse<Response> resp = startDownload_0WithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Start the certification policy download.
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
     * Start the certification policy download. (asynchronously)
     * Accepted Roles: * Auditor * Customer Administrator * Data Administrator * Review Administrator 
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
        String localVarPath = "/policy/certpolicies/download/{id}/status"
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
     * Check the status of a Certification Policies download.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param id - The download id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response statusDownload(String id) throws ApiException {
        ApiResponse<Response> resp = statusDownloadWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Check the status of a Certification Policies download.
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
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
     * Check the status of a Certification Policies download. (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
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
     * Build call for updateCertPolicy
     * @param body - certification policy node (required)
     * @param id - certification policy id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateCertPolicyCall(CertPolicyNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/policy/certpolicies/{id}"
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
    private com.squareup.okhttp.Call updateCertPolicyValidateBeforeCall(CertPolicyNode body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateCertPolicy(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateCertPolicy(Async)");
        }
        
        com.squareup.okhttp.Call call = updateCertPolicyCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update certification policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - certification policy node (required)
     * @param id - certification policy id (required)
     * @return CertPolicyNode
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CertPolicyNode updateCertPolicy(CertPolicyNode body, Long id) throws ApiException {
        ApiResponse<CertPolicyNode> resp = updateCertPolicyWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update certification policy
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - certification policy node (required)
     * @param id - certification policy id (required)
     * @return ApiResponse&lt;CertPolicyNode&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CertPolicyNode> updateCertPolicyWithHttpInfo(CertPolicyNode body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateCertPolicyValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update certification policy (asynchronously)
     * Accepted Roles: * Customer Administrator * Data Administrator * Review Administrator 
     * @param body - certification policy node (required)
     * @param id - certification policy id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateCertPolicyAsync(CertPolicyNode body, Long id, final ApiCallback<CertPolicyNode> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateCertPolicyValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CertPolicyNode>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
