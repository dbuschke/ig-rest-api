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


import io.swagger.client.model.ChangeItem;
import io.swagger.client.model.ChangeItemList;
import io.swagger.client.model.Changeset;
import io.swagger.client.model.ConfigurationTarget;
import io.swagger.client.model.Download;
import io.swagger.client.model.FulfillmentItemSearch;
import io.swagger.client.model.ProvisionItems;
import io.swagger.client.model.Requests;
import io.swagger.client.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProvisioningApi {
    private ApiClient apiClient;

    public ProvisioningApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ProvisioningApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for changeItemVerification
     * @param appid - application id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call changeItemVerificationCall(Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/verify/application/{appid}"
            .replaceAll("\\{" + "appid" + "\\}", apiClient.escapeString(appid.toString()));

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
    private com.squareup.okhttp.Call changeItemVerificationValidateBeforeCall(Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'appid' is set
        if (appid == null) {
            throw new ApiException("Missing the required parameter 'appid' when calling changeItemVerification(Async)");
        }
        
        com.squareup.okhttp.Call call = changeItemVerificationCall(appid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * start the provisioning change item verification
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param appid - application id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response changeItemVerification(Long appid) throws ApiException {
        ApiResponse<Response> resp = changeItemVerificationWithHttpInfo(appid);
        return resp.getData();
    }

    /**
     * start the provisioning change item verification
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param appid - application id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> changeItemVerificationWithHttpInfo(Long appid) throws ApiException {
        com.squareup.okhttp.Call call = changeItemVerificationValidateBeforeCall(appid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * start the provisioning change item verification (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param appid - application id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call changeItemVerificationAsync(Long appid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = changeItemVerificationValidateBeforeCall(appid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for createFufillmentTarget
     * @param body the fulfillment target configuration payload (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call createFufillmentTargetCall(ConfigurationTarget body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/configuration/targets";

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
    private com.squareup.okhttp.Call createFufillmentTargetValidateBeforeCall(ConfigurationTarget body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createFufillmentTarget(Async)");
        }
        
        com.squareup.okhttp.Call call = createFufillmentTargetCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create a configuration target.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment target configuration payload (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response createFufillmentTarget(ConfigurationTarget body) throws ApiException {
        ApiResponse<Response> resp = createFufillmentTargetWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Create a configuration target.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment target configuration payload (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> createFufillmentTargetWithHttpInfo(ConfigurationTarget body) throws ApiException {
        com.squareup.okhttp.Call call = createFufillmentTargetValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a configuration target. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment target configuration payload (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createFufillmentTargetAsync(ConfigurationTarget body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = createFufillmentTargetValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteFufillmentTarget
     * @param targetId the fulfillment target configuration id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteFufillmentTargetCall(Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/configuration/targets/{targetId}"
            .replaceAll("\\{" + "targetId" + "\\}", apiClient.escapeString(targetId.toString()));

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
    private com.squareup.okhttp.Call deleteFufillmentTargetValidateBeforeCall(Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'targetId' is set
        if (targetId == null) {
            throw new ApiException("Missing the required parameter 'targetId' when calling deleteFufillmentTarget(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteFufillmentTargetCall(targetId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete a configuration target.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param targetId the fulfillment target configuration id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response deleteFufillmentTarget(Long targetId) throws ApiException {
        ApiResponse<Response> resp = deleteFufillmentTargetWithHttpInfo(targetId);
        return resp.getData();
    }

    /**
     * Delete a configuration target.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param targetId the fulfillment target configuration id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> deleteFufillmentTargetWithHttpInfo(Long targetId) throws ApiException {
        com.squareup.okhttp.Call call = deleteFufillmentTargetValidateBeforeCall(targetId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete a configuration target. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param targetId the fulfillment target configuration id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteFufillmentTargetAsync(Long targetId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteFufillmentTargetValidateBeforeCall(targetId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getAllChangesetFF
     * @param fulfillerId the fulfiller id (required)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getAllChangesetFFCall(Long fulfillerId, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/fullfillers/{fulfillerId}/changesets"
            .replaceAll("\\{" + "fulfillerId" + "\\}", apiClient.escapeString(fulfillerId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (inProgressOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgressOnly", inProgressOnly));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (addFulfillmentContext != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("addFulfillmentContext", addFulfillmentContext));

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
    private com.squareup.okhttp.Call getAllChangesetFFValidateBeforeCall(Long fulfillerId, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillerId' is set
        if (fulfillerId == null) {
            throw new ApiException("Missing the required parameter 'fulfillerId' when calling getAllChangesetFF(Async)");
        }
        
        com.squareup.okhttp.Call call = getAllChangesetFFCall(fulfillerId, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the open tasks for this user for all changesets
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param fulfillerId the fulfiller id (required)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getAllChangesetFF(Long fulfillerId, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext) throws ApiException {
        ApiResponse<Response> resp = getAllChangesetFFWithHttpInfo(fulfillerId, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext);
        return resp.getData();
    }

    /**
     * Get the open tasks for this user for all changesets
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param fulfillerId the fulfiller id (required)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getAllChangesetFFWithHttpInfo(Long fulfillerId, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext) throws ApiException {
        com.squareup.okhttp.Call call = getAllChangesetFFValidateBeforeCall(fulfillerId, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the open tasks for this user for all changesets (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param fulfillerId the fulfiller id (required)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getAllChangesetFFAsync(Long fulfillerId, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getAllChangesetFFValidateBeforeCall(fulfillerId, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChangeItemAction
     * @param id - the change item action id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChangeItemActionCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/changeitem/action/{id}"
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
    private com.squareup.okhttp.Call getChangeItemActionValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getChangeItemAction(Async)");
        }
        
        com.squareup.okhttp.Call call = getChangeItemActionCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the change item action as per id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id - the change item action id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getChangeItemAction(Long id) throws ApiException {
        ApiResponse<Response> resp = getChangeItemActionWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the change item action as per id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id - the change item action id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getChangeItemActionWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getChangeItemActionValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the change item action as per id (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id - the change item action id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChangeItemActionAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChangeItemActionValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChangeset
     * @param id the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param fulfillerId filter for fulfiller (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param processId the processId, if not supplied no check (optional)
     * @param fulfiller the fulfiller as known by the fulfillment system (i.e. dn if it is        a workflow, incident number if service                                     now), if not supplied no        check (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param curated return curated values (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param addFulfillmentContext if true, this adds any fulfillment context parameters that are setup on configuration page (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChangesetCall(Long id, Long appSourceId, String provisioningType, Long fulfillerId, String changeRequestState, String action, String processId, String fulfiller, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean curated, Boolean showCt, Boolean addFulfillmentContext, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (appSourceId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("appSourceId", appSourceId));
        if (provisioningType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("provisioningType", provisioningType));
        if (fulfillerId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("fulfillerId", fulfillerId));
        if (changeRequestState != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("changeRequestState", changeRequestState));
        if (action != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("action", action));
        if (processId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("processId", processId));
        if (fulfiller != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("fulfiller", fulfiller));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (inProgressOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgressOnly", inProgressOnly));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (curated != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("curated", curated));
        if (showCt != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("showCt", showCt));
        if (addFulfillmentContext != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("addFulfillmentContext", addFulfillmentContext));

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
    private com.squareup.okhttp.Call getChangesetValidateBeforeCall(Long id, Long appSourceId, String provisioningType, Long fulfillerId, String changeRequestState, String action, String processId, String fulfiller, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean curated, Boolean showCt, Boolean addFulfillmentContext, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getChangeset(Async)");
        }
        
        com.squareup.okhttp.Call call = getChangesetCall(id, appSourceId, provisioningType, fulfillerId, changeRequestState, action, processId, fulfiller, sortBy, inProgressOnly, indexFrom, size, curated, showCt, addFulfillmentContext, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the changeset associated with the changeset id.
     * &lt;p&gt;  &lt;br/&gt;  &lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;  &lt;/p&gt;&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Customer Administrator * Fulfillment Administrator * Historical Review Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) 
     * @param id the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param fulfillerId filter for fulfiller (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param processId the processId, if not supplied no check (optional)
     * @param fulfiller the fulfiller as known by the fulfillment system (i.e. dn if it is        a workflow, incident number if service                                     now), if not supplied no        check (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param curated return curated values (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param addFulfillmentContext if true, this adds any fulfillment context parameters that are setup on configuration page (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getChangeset(Long id, Long appSourceId, String provisioningType, Long fulfillerId, String changeRequestState, String action, String processId, String fulfiller, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean curated, Boolean showCt, Boolean addFulfillmentContext) throws ApiException {
        ApiResponse<Response> resp = getChangesetWithHttpInfo(id, appSourceId, provisioningType, fulfillerId, changeRequestState, action, processId, fulfiller, sortBy, inProgressOnly, indexFrom, size, curated, showCt, addFulfillmentContext);
        return resp.getData();
    }

    /**
     * Get the changeset associated with the changeset id.
     * &lt;p&gt;  &lt;br/&gt;  &lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;  &lt;/p&gt;&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Customer Administrator * Fulfillment Administrator * Historical Review Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) 
     * @param id the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param fulfillerId filter for fulfiller (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param processId the processId, if not supplied no check (optional)
     * @param fulfiller the fulfiller as known by the fulfillment system (i.e. dn if it is        a workflow, incident number if service                                     now), if not supplied no        check (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param curated return curated values (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param addFulfillmentContext if true, this adds any fulfillment context parameters that are setup on configuration page (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getChangesetWithHttpInfo(Long id, Long appSourceId, String provisioningType, Long fulfillerId, String changeRequestState, String action, String processId, String fulfiller, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean curated, Boolean showCt, Boolean addFulfillmentContext) throws ApiException {
        com.squareup.okhttp.Call call = getChangesetValidateBeforeCall(id, appSourceId, provisioningType, fulfillerId, changeRequestState, action, processId, fulfiller, sortBy, inProgressOnly, indexFrom, size, curated, showCt, addFulfillmentContext, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the changeset associated with the changeset id. (asynchronously)
     * &lt;p&gt;  &lt;br/&gt;  &lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;  &lt;/p&gt;&lt;br/&gt;Accepted Roles: * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Application Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Customer Administrator * Fulfillment Administrator * Historical Review Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;fulfillerId&#x27;) 
     * @param id the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param fulfillerId filter for fulfiller (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param processId the processId, if not supplied no check (optional)
     * @param fulfiller the fulfiller as known by the fulfillment system (i.e. dn if it is        a workflow, incident number if service                                     now), if not supplied no        check (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param curated return curated values (optional)
     * @param showCt show total result set count (optional, default to false)
     * @param addFulfillmentContext if true, this adds any fulfillment context parameters that are setup on configuration page (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChangesetAsync(Long id, Long appSourceId, String provisioningType, Long fulfillerId, String changeRequestState, String action, String processId, String fulfiller, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean curated, Boolean showCt, Boolean addFulfillmentContext, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChangesetValidateBeforeCall(id, appSourceId, provisioningType, fulfillerId, changeRequestState, action, processId, fulfiller, sortBy, inProgressOnly, indexFrom, size, curated, showCt, addFulfillmentContext, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChangesetFF
     * @param fulfillerId fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChangesetFFCall(Long fulfillerId, Long changesetId, Long appSourceId, String provisioningType, String changeRequestState, String action, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/fullfillers/{fulfillerId}/changesets/{changesetId}"
            .replaceAll("\\{" + "fulfillerId" + "\\}", apiClient.escapeString(fulfillerId.toString()))
            .replaceAll("\\{" + "changesetId" + "\\}", apiClient.escapeString(changesetId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (appSourceId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("appSourceId", appSourceId));
        if (provisioningType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("provisioningType", provisioningType));
        if (changeRequestState != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("changeRequestState", changeRequestState));
        if (action != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("action", action));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (inProgressOnly != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("inProgressOnly", inProgressOnly));
        if (indexFrom != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("indexFrom", indexFrom));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (addFulfillmentContext != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("addFulfillmentContext", addFulfillmentContext));

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
    private com.squareup.okhttp.Call getChangesetFFValidateBeforeCall(Long fulfillerId, Long changesetId, Long appSourceId, String provisioningType, String changeRequestState, String action, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'fulfillerId' is set
        if (fulfillerId == null) {
            throw new ApiException("Missing the required parameter 'fulfillerId' when calling getChangesetFF(Async)");
        }
        // verify the required parameter 'changesetId' is set
        if (changesetId == null) {
            throw new ApiException("Missing the required parameter 'changesetId' when calling getChangesetFF(Async)");
        }
        
        com.squareup.okhttp.Call call = getChangesetFFCall(fulfillerId, changesetId, appSourceId, provisioningType, changeRequestState, action, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the open tasks for this user for this changeset
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param fulfillerId fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getChangesetFF(Long fulfillerId, Long changesetId, Long appSourceId, String provisioningType, String changeRequestState, String action, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext) throws ApiException {
        ApiResponse<Response> resp = getChangesetFFWithHttpInfo(fulfillerId, changesetId, appSourceId, provisioningType, changeRequestState, action, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext);
        return resp.getData();
    }

    /**
     * Get the open tasks for this user for this changeset
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param fulfillerId fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getChangesetFFWithHttpInfo(Long fulfillerId, Long changesetId, Long appSourceId, String provisioningType, String changeRequestState, String action, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext) throws ApiException {
        com.squareup.okhttp.Call call = getChangesetFFValidateBeforeCall(fulfillerId, changesetId, appSourceId, provisioningType, changeRequestState, action, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the open tasks for this user for this changeset (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param fulfillerId fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @param appSourceId filter for application (optional)
     * @param provisioningType filter for provisioning type (optional)
     * @param changeRequestState the change request item state (optional)
     * @param action the change item action type (optional)
     * @param sortBy the filter to sort the results on (optional)
     * @param inProgressOnly return in progress items only if true, return all if false (optional)
     * @param indexFrom starting index (optional, default to 0)
     * @param size the change item count to return (optional, default to 100)
     * @param addFulfillmentContext the add fulfillment context (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChangesetFFAsync(Long fulfillerId, Long changesetId, Long appSourceId, String provisioningType, String changeRequestState, String action, String sortBy, Boolean inProgressOnly, Integer indexFrom, Integer size, Boolean addFulfillmentContext, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChangesetFFValidateBeforeCall(fulfillerId, changesetId, appSourceId, provisioningType, changeRequestState, action, sortBy, inProgressOnly, indexFrom, size, addFulfillmentContext, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChangesetItem
     * @param id the changse request item (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChangesetItemCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/changeitem/{id}"
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
    private com.squareup.okhttp.Call getChangesetItemValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getChangesetItem(Async)");
        }
        
        com.squareup.okhttp.Call call = getChangesetItemCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the change item associated with the id.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id the changse request item (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getChangesetItem(Long id) throws ApiException {
        ApiResponse<Response> resp = getChangesetItemWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the change item associated with the id.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id the changse request item (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getChangesetItemWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getChangesetItemValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the change item associated with the id. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id the changse request item (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChangesetItemAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChangesetItemValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getChangesetStatusCount
     * @param id the change set id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getChangesetStatusCountCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{id}/status"
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
    private com.squareup.okhttp.Call getChangesetStatusCountValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getChangesetStatusCount(Async)");
        }
        
        com.squareup.okhttp.Call call = getChangesetStatusCountCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the status count for passed changeSetId
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id the change set id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getChangesetStatusCount(Long id) throws ApiException {
        ApiResponse<Response> resp = getChangesetStatusCountWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Get the status count for passed changeSetId
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id the change set id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getChangesetStatusCountWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = getChangesetStatusCountValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the status count for passed changeSetId (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param id the change set id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getChangesetStatusCountAsync(Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getChangesetStatusCountValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFufillmentTarget
     * @param targetId the fulfillment target configuration id (required)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param isForExport - pass in true if you want content disposition to indicate this is a download, false (or not included) otherwise (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFufillmentTargetCall(Long targetId, List<String> attrFilter, Boolean isForExport, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/configuration/targets/{targetId}"
            .replaceAll("\\{" + "targetId" + "\\}", apiClient.escapeString(targetId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (attrFilter != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "attrFilter", attrFilter));
        if (isForExport != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("isForExport", isForExport));

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
    private com.squareup.okhttp.Call getFufillmentTargetValidateBeforeCall(Long targetId, List<String> attrFilter, Boolean isForExport, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'targetId' is set
        if (targetId == null) {
            throw new ApiException("Missing the required parameter 'targetId' when calling getFufillmentTarget(Async)");
        }
        
        com.squareup.okhttp.Call call = getFufillmentTargetCall(targetId, attrFilter, isForExport, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a configuration target.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param targetId the fulfillment target configuration id (required)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param isForExport - pass in true if you want content disposition to indicate this is a download, false (or not included) otherwise (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFufillmentTarget(Long targetId, List<String> attrFilter, Boolean isForExport) throws ApiException {
        ApiResponse<Response> resp = getFufillmentTargetWithHttpInfo(targetId, attrFilter, isForExport);
        return resp.getData();
    }

    /**
     * Get a configuration target.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param targetId the fulfillment target configuration id (required)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param isForExport - pass in true if you want content disposition to indicate this is a download, false (or not included) otherwise (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFufillmentTargetWithHttpInfo(Long targetId, List<String> attrFilter, Boolean isForExport) throws ApiException {
        com.squareup.okhttp.Call call = getFufillmentTargetValidateBeforeCall(targetId, attrFilter, isForExport, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a configuration target. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param targetId the fulfillment target configuration id (required)
     * @param attrFilter - filters for types of attributes to return in the payload (optional)
     * @param isForExport - pass in true if you want content disposition to indicate this is a download, false (or not included) otherwise (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFufillmentTargetAsync(Long targetId, List<String> attrFilter, Boolean isForExport, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFufillmentTargetValidateBeforeCall(targetId, attrFilter, isForExport, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFufillmentTargets
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFufillmentTargetsCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/configuration/targets";

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
    private com.squareup.okhttp.Call getFufillmentTargetsValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getFufillmentTargetsCall(progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the configuration targets.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFufillmentTargets() throws ApiException {
        ApiResponse<Response> resp = getFufillmentTargetsWithHttpInfo();
        return resp.getData();
    }

    /**
     * Get the configuration targets.
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFufillmentTargetsWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getFufillmentTargetsValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the configuration targets. (asynchronously)
     * Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFufillmentTargetsAsync(final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFufillmentTargetsValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFufillmentTemplate
     * @param targetId the template id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFufillmentTemplateCall(Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/configuration/localizedtemplate/{targetId}"
            .replaceAll("\\{" + "targetId" + "\\}", apiClient.escapeString(targetId.toString()));

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
    private com.squareup.okhttp.Call getFufillmentTemplateValidateBeforeCall(Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'targetId' is set
        if (targetId == null) {
            throw new ApiException("Missing the required parameter 'targetId' when calling getFufillmentTemplate(Async)");
        }
        
        com.squareup.okhttp.Call call = getFufillmentTemplateCall(targetId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get a configuration template which has been localized.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param targetId the template id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getFufillmentTemplate(Long targetId) throws ApiException {
        ApiResponse<Response> resp = getFufillmentTemplateWithHttpInfo(targetId);
        return resp.getData();
    }

    /**
     * Get a configuration template which has been localized.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param targetId the template id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getFufillmentTemplateWithHttpInfo(Long targetId) throws ApiException {
        com.squareup.okhttp.Call call = getFufillmentTemplateValidateBeforeCall(targetId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a configuration template which has been localized. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param targetId the template id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFufillmentTemplateAsync(Long targetId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFufillmentTemplateValidateBeforeCall(targetId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for retryChangesetProvisioning
     * @param body the fulfillment search node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call retryChangesetProvisioningCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/retryRetries";

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
    private com.squareup.okhttp.Call retryChangesetProvisioningValidateBeforeCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling retryChangesetProvisioning(Async)");
        }
        
        com.squareup.okhttp.Call call = retryChangesetProvisioningCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * retry provisioning changesets in retry state, base the selections on the search criteria
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response retryChangesetProvisioning(FulfillmentItemSearch body) throws ApiException {
        ApiResponse<Response> resp = retryChangesetProvisioningWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * retry provisioning changesets in retry state, base the selections on the search criteria
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> retryChangesetProvisioningWithHttpInfo(FulfillmentItemSearch body) throws ApiException {
        com.squareup.okhttp.Call call = retryChangesetProvisioningValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * retry provisioning changesets in retry state, base the selections on the search criteria (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call retryChangesetProvisioningAsync(FulfillmentItemSearch body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = retryChangesetProvisioningValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startAutoAppProvisioning
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startAutoAppProvisioningCall(Long cid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{cid}/application/{appid}/auto"
            .replaceAll("\\{" + "cid" + "\\}", apiClient.escapeString(cid.toString()))
            .replaceAll("\\{" + "appid" + "\\}", apiClient.escapeString(appid.toString()));

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
    private com.squareup.okhttp.Call startAutoAppProvisioningValidateBeforeCall(Long cid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'cid' is set
        if (cid == null) {
            throw new ApiException("Missing the required parameter 'cid' when calling startAutoAppProvisioning(Async)");
        }
        // verify the required parameter 'appid' is set
        if (appid == null) {
            throw new ApiException("Missing the required parameter 'appid' when calling startAutoAppProvisioning(Async)");
        }
        
        com.squareup.okhttp.Call call = startAutoAppProvisioningCall(cid, appid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start auto provisioning for an application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startAutoAppProvisioning(Long cid, Long appid) throws ApiException {
        ApiResponse<Response> resp = startAutoAppProvisioningWithHttpInfo(cid, appid);
        return resp.getData();
    }

    /**
     * Start auto provisioning for an application
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startAutoAppProvisioningWithHttpInfo(Long cid, Long appid) throws ApiException {
        com.squareup.okhttp.Call call = startAutoAppProvisioningValidateBeforeCall(cid, appid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start auto provisioning for an application (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startAutoAppProvisioningAsync(Long cid, Long appid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startAutoAppProvisioningValidateBeforeCall(cid, appid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startAutoCIProvisioning
     * @param body - the change item list node (required)
     * @param cid - the changeset id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startAutoCIProvisioningCall(ChangeItemList body, Long cid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{cid}/items/auto"
            .replaceAll("\\{" + "cid" + "\\}", apiClient.escapeString(cid.toString()));

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
    private com.squareup.okhttp.Call startAutoCIProvisioningValidateBeforeCall(ChangeItemList body, Long cid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startAutoCIProvisioning(Async)");
        }
        // verify the required parameter 'cid' is set
        if (cid == null) {
            throw new ApiException("Missing the required parameter 'cid' when calling startAutoCIProvisioning(Async)");
        }
        
        com.squareup.okhttp.Call call = startAutoCIProvisioningCall(body, cid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start auto provisioning for a list of changeItems
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - the change item list node (required)
     * @param cid - the changeset id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startAutoCIProvisioning(ChangeItemList body, Long cid) throws ApiException {
        ApiResponse<Response> resp = startAutoCIProvisioningWithHttpInfo(body, cid);
        return resp.getData();
    }

    /**
     * Start auto provisioning for a list of changeItems
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - the change item list node (required)
     * @param cid - the changeset id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startAutoCIProvisioningWithHttpInfo(ChangeItemList body, Long cid) throws ApiException {
        com.squareup.okhttp.Call call = startAutoCIProvisioningValidateBeforeCall(body, cid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start auto provisioning for a list of changeItems (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - the change item list node (required)
     * @param cid - the changeset id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startAutoCIProvisioningAsync(ChangeItemList body, Long cid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startAutoCIProvisioningValidateBeforeCall(body, cid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startAutoProvisioning
     * @param cid - the changeset id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startAutoProvisioningCall(Long cid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{cid}/auto"
            .replaceAll("\\{" + "cid" + "\\}", apiClient.escapeString(cid.toString()));

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
    private com.squareup.okhttp.Call startAutoProvisioningValidateBeforeCall(Long cid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'cid' is set
        if (cid == null) {
            throw new ApiException("Missing the required parameter 'cid' when calling startAutoProvisioning(Async)");
        }
        
        com.squareup.okhttp.Call call = startAutoProvisioningCall(cid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * start auto-provisioning for all the items in the changeset
     * &lt;p&gt;  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;  &lt;/p&gt;&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startAutoProvisioning(Long cid) throws ApiException {
        ApiResponse<Response> resp = startAutoProvisioningWithHttpInfo(cid);
        return resp.getData();
    }

    /**
     * start auto-provisioning for all the items in the changeset
     * &lt;p&gt;  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;  &lt;/p&gt;&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startAutoProvisioningWithHttpInfo(Long cid) throws ApiException {
        com.squareup.okhttp.Call call = startAutoProvisioningValidateBeforeCall(cid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * start auto-provisioning for all the items in the changeset (asynchronously)
     * &lt;p&gt;  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;  &lt;/p&gt;&lt;br/&gt;Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startAutoProvisioningAsync(Long cid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startAutoProvisioningValidateBeforeCall(cid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startDownload
     * @param body -  The download request node (required)
     * @param targetId data source id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startDownloadCall(Download body, Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/configuration/targets/{targetId}/download"
            .replaceAll("\\{" + "targetId" + "\\}", apiClient.escapeString(targetId.toString()));

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
    private com.squareup.okhttp.Call startDownloadValidateBeforeCall(Download body, Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startDownload(Async)");
        }
        // verify the required parameter 'targetId' is set
        if (targetId == null) {
            throw new ApiException("Missing the required parameter 'targetId' when calling startDownload(Async)");
        }
        
        com.squareup.okhttp.Call call = startDownloadCall(body, targetId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start data source download for the specified id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body -  The download request node (required)
     * @param targetId data source id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startDownload(Download body, Long targetId) throws ApiException {
        ApiResponse<Response> resp = startDownloadWithHttpInfo(body, targetId);
        return resp.getData();
    }

    /**
     * Start data source download for the specified id
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body -  The download request node (required)
     * @param targetId data source id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startDownloadWithHttpInfo(Download body, Long targetId) throws ApiException {
        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, targetId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start data source download for the specified id (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body -  The download request node (required)
     * @param targetId data source id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startDownloadAsync(Download body, Long targetId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startDownloadValidateBeforeCall(body, targetId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startExternalWorkflow
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startExternalWorkflowCall(Long cid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{cid}/application/{appid}/external"
            .replaceAll("\\{" + "cid" + "\\}", apiClient.escapeString(cid.toString()))
            .replaceAll("\\{" + "appid" + "\\}", apiClient.escapeString(appid.toString()));

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
    private com.squareup.okhttp.Call startExternalWorkflowValidateBeforeCall(Long cid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'cid' is set
        if (cid == null) {
            throw new ApiException("Missing the required parameter 'cid' when calling startExternalWorkflow(Async)");
        }
        // verify the required parameter 'appid' is set
        if (appid == null) {
            throw new ApiException("Missing the required parameter 'appid' when calling startExternalWorkflow(Async)");
        }
        
        com.squareup.okhttp.Call call = startExternalWorkflowCall(cid, appid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Start an external provisioning workflow
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startExternalWorkflow(Long cid, Long appid) throws ApiException {
        ApiResponse<Response> resp = startExternalWorkflowWithHttpInfo(cid, appid);
        return resp.getData();
    }

    /**
     * Start an external provisioning workflow
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startExternalWorkflowWithHttpInfo(Long cid, Long appid) throws ApiException {
        com.squareup.okhttp.Call call = startExternalWorkflowValidateBeforeCall(cid, appid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Start an external provisioning workflow (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startExternalWorkflowAsync(Long cid, Long appid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startExternalWorkflowValidateBeforeCall(cid, appid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for startRetryProvisioning
     * @param body - optional list of request item nodes that should be retried if        included, all nodes in this list are
                                  RETRYied (required)
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call startRetryProvisioningCall(Requests body, Long cid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{cid}/application/{appid}/retry"
            .replaceAll("\\{" + "cid" + "\\}", apiClient.escapeString(cid.toString()))
            .replaceAll("\\{" + "appid" + "\\}", apiClient.escapeString(appid.toString()));

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
    private com.squareup.okhttp.Call startRetryProvisioningValidateBeforeCall(Requests body, Long cid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling startRetryProvisioning(Async)");
        }
        // verify the required parameter 'cid' is set
        if (cid == null) {
            throw new ApiException("Missing the required parameter 'cid' when calling startRetryProvisioning(Async)");
        }
        // verify the required parameter 'appid' is set
        if (appid == null) {
            throw new ApiException("Missing the required parameter 'appid' when calling startRetryProvisioning(Async)");
        }
        
        com.squareup.okhttp.Call call = startRetryProvisioningCall(body, cid, appid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Retry provisioning for a changeset/application/change request item
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - optional list of request item nodes that should be retried if        included, all nodes in this list are
                                  RETRYied (required)
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response startRetryProvisioning(Requests body, Long cid, Long appid) throws ApiException {
        ApiResponse<Response> resp = startRetryProvisioningWithHttpInfo(body, cid, appid);
        return resp.getData();
    }

    /**
     * Retry provisioning for a changeset/application/change request item
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - optional list of request item nodes that should be retried if        included, all nodes in this list are
                                  RETRYied (required)
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> startRetryProvisioningWithHttpInfo(Requests body, Long cid, Long appid) throws ApiException {
        com.squareup.okhttp.Call call = startRetryProvisioningValidateBeforeCall(body, cid, appid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retry provisioning for a changeset/application/change request item (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body - optional list of request item nodes that should be retried if        included, all nodes in this list are
                                  RETRYied (required)
     * @param cid - the changeset id (required)
     * @param appid - the application id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call startRetryProvisioningAsync(Requests body, Long cid, Long appid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = startRetryProvisioningValidateBeforeCall(body, cid, appid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for terminateChangeItemProvisioning
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @param csiid - the changeitemId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call terminateChangeItemProvisioningCall(Long csid, Long appid, Long csiid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{csid}/application/{appid}/changeitem/{csiid}/terminate"
            .replaceAll("\\{" + "csid" + "\\}", apiClient.escapeString(csid.toString()))
            .replaceAll("\\{" + "appid" + "\\}", apiClient.escapeString(appid.toString()))
            .replaceAll("\\{" + "csiid" + "\\}", apiClient.escapeString(csiid.toString()));

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
    private com.squareup.okhttp.Call terminateChangeItemProvisioningValidateBeforeCall(Long csid, Long appid, Long csiid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'csid' is set
        if (csid == null) {
            throw new ApiException("Missing the required parameter 'csid' when calling terminateChangeItemProvisioning(Async)");
        }
        // verify the required parameter 'appid' is set
        if (appid == null) {
            throw new ApiException("Missing the required parameter 'appid' when calling terminateChangeItemProvisioning(Async)");
        }
        // verify the required parameter 'csiid' is set
        if (csiid == null) {
            throw new ApiException("Missing the required parameter 'csiid' when calling terminateChangeItemProvisioning(Async)");
        }
        
        com.squareup.okhttp.Call call = terminateChangeItemProvisioningCall(csid, appid, csiid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * terminate a provisioning changeitem, set it to error state
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @param csiid - the changeitemId (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response terminateChangeItemProvisioning(Long csid, Long appid, Long csiid) throws ApiException {
        ApiResponse<Response> resp = terminateChangeItemProvisioningWithHttpInfo(csid, appid, csiid);
        return resp.getData();
    }

    /**
     * terminate a provisioning changeitem, set it to error state
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @param csiid - the changeitemId (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> terminateChangeItemProvisioningWithHttpInfo(Long csid, Long appid, Long csiid) throws ApiException {
        com.squareup.okhttp.Call call = terminateChangeItemProvisioningValidateBeforeCall(csid, appid, csiid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * terminate a provisioning changeitem, set it to error state (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @param csiid - the changeitemId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call terminateChangeItemProvisioningAsync(Long csid, Long appid, Long csiid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = terminateChangeItemProvisioningValidateBeforeCall(csid, appid, csiid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for terminateChangesetProvisioning
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call terminateChangesetProvisioningCall(Long csid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{csid}/application/{appid}/terminate"
            .replaceAll("\\{" + "csid" + "\\}", apiClient.escapeString(csid.toString()))
            .replaceAll("\\{" + "appid" + "\\}", apiClient.escapeString(appid.toString()));

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
    private com.squareup.okhttp.Call terminateChangesetProvisioningValidateBeforeCall(Long csid, Long appid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'csid' is set
        if (csid == null) {
            throw new ApiException("Missing the required parameter 'csid' when calling terminateChangesetProvisioning(Async)");
        }
        // verify the required parameter 'appid' is set
        if (appid == null) {
            throw new ApiException("Missing the required parameter 'appid' when calling terminateChangesetProvisioning(Async)");
        }
        
        com.squareup.okhttp.Call call = terminateChangesetProvisioningCall(csid, appid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * terminate a provisioning changeset, set it to error state
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response terminateChangesetProvisioning(Long csid, Long appid) throws ApiException {
        ApiResponse<Response> resp = terminateChangesetProvisioningWithHttpInfo(csid, appid);
        return resp.getData();
    }

    /**
     * terminate a provisioning changeset, set it to error state
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> terminateChangesetProvisioningWithHttpInfo(Long csid, Long appid) throws ApiException {
        com.squareup.okhttp.Call call = terminateChangesetProvisioningValidateBeforeCall(csid, appid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * terminate a provisioning changeset, set it to error state (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param csid - the changesetId (required)
     * @param appid - the application id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call terminateChangesetProvisioningAsync(Long csid, Long appid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = terminateChangesetProvisioningValidateBeforeCall(csid, appid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for terminateChangesetProvisioning_0
     * @param body the fulfillment search node  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call terminateChangesetProvisioning_0Call(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/terminateRetries";

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
    private com.squareup.okhttp.Call terminateChangesetProvisioning_0ValidateBeforeCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling terminateChangesetProvisioning_0(Async)");
        }
        
        com.squareup.okhttp.Call call = terminateChangesetProvisioning_0Call(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * terminate provisioning changesets in retry state, base the selections on the search criteria
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response terminateChangesetProvisioning_0(FulfillmentItemSearch body) throws ApiException {
        ApiResponse<Response> resp = terminateChangesetProvisioning_0WithHttpInfo(body);
        return resp.getData();
    }

    /**
     * terminate provisioning changesets in retry state, base the selections on the search criteria
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> terminateChangesetProvisioning_0WithHttpInfo(FulfillmentItemSearch body) throws ApiException {
        com.squareup.okhttp.Call call = terminateChangesetProvisioning_0ValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * terminate provisioning changesets in retry state, base the selections on the search criteria (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment search node  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call terminateChangesetProvisioning_0Async(FulfillmentItemSearch body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = terminateChangesetProvisioning_0ValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for transformAutoToManual
     * @param cid - the changeset id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call transformAutoToManualCall(Long cid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{cid}/auto2manual"
            .replaceAll("\\{" + "cid" + "\\}", apiClient.escapeString(cid.toString()));

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
    private com.squareup.okhttp.Call transformAutoToManualValidateBeforeCall(Long cid, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'cid' is set
        if (cid == null) {
            throw new ApiException("Missing the required parameter 'cid' when calling transformAutoToManual(Async)");
        }
        
        com.squareup.okhttp.Call call = transformAutoToManualCall(cid, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Transform the auto-provisioning change items to manual prov items
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response transformAutoToManual(Long cid) throws ApiException {
        ApiResponse<Response> resp = transformAutoToManualWithHttpInfo(cid);
        return resp.getData();
    }

    /**
     * Transform the auto-provisioning change items to manual prov items
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> transformAutoToManualWithHttpInfo(Long cid) throws ApiException {
        com.squareup.okhttp.Call call = transformAutoToManualValidateBeforeCall(cid, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Transform the auto-provisioning change items to manual prov items (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param cid - the changeset id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call transformAutoToManualAsync(Long cid, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = transformAutoToManualValidateBeforeCall(cid, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateAllChangeItemsState
     * @param body the FulfillmentSearchNode (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateAllChangeItemsStateCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/allchangeitems";

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
    private com.squareup.okhttp.Call updateAllChangeItemsStateValidateBeforeCall(FulfillmentItemSearch body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateAllChangeItemsState(Async)");
        }
        
        com.squareup.okhttp.Call call = updateAllChangeItemsStateCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the change item status based on the fulfiller action, this is for the ALL FULFILLED/ALL_DECLINED case based upon the current filter
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the FulfillmentSearchNode (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateAllChangeItemsState(FulfillmentItemSearch body) throws ApiException {
        ApiResponse<Response> resp = updateAllChangeItemsStateWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Update the change item status based on the fulfiller action, this is for the ALL FULFILLED/ALL_DECLINED case based upon the current filter
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the FulfillmentSearchNode (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateAllChangeItemsStateWithHttpInfo(FulfillmentItemSearch body) throws ApiException {
        com.squareup.okhttp.Call call = updateAllChangeItemsStateValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the change item status based on the fulfiller action, this is for the ALL FULFILLED/ALL_DECLINED case based upon the current filter (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the FulfillmentSearchNode (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateAllChangeItemsStateAsync(FulfillmentItemSearch body, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateAllChangeItemsStateValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateChangeItemStatus
     * @param body the change item node (required)
     * @param id the change request item id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateChangeItemStatusCall(ChangeItem body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/changeitem/{id}"
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
    private com.squareup.okhttp.Call updateChangeItemStatusValidateBeforeCall(ChangeItem body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateChangeItemStatus(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateChangeItemStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = updateChangeItemStatusCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the change request item status.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the change item node (required)
     * @param id the change request item id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateChangeItemStatus(ChangeItem body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateChangeItemStatusWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update the change request item status.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the change item node (required)
     * @param id the change request item id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateChangeItemStatusWithHttpInfo(ChangeItem body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateChangeItemStatusValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the change request item status. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the change item node (required)
     * @param id the change request item id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateChangeItemStatusAsync(ChangeItem body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateChangeItemStatusValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateChangeItemsState
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param changesetId the changeset id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateChangeItemsStateCall(ProvisionItems body, Long changesetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{changesetId}/changeitems"
            .replaceAll("\\{" + "changesetId" + "\\}", apiClient.escapeString(changesetId.toString()));

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
    private com.squareup.okhttp.Call updateChangeItemsStateValidateBeforeCall(ProvisionItems body, Long changesetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateChangeItemsState(Async)");
        }
        // verify the required parameter 'changesetId' is set
        if (changesetId == null) {
            throw new ApiException("Missing the required parameter 'changesetId' when calling updateChangeItemsState(Async)");
        }
        
        com.squareup.okhttp.Call call = updateChangeItemsStateCall(body, changesetId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the change item status based on the fulfiller action
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Review Owner * Separation of Duties owner 
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param changesetId the changeset id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateChangeItemsState(ProvisionItems body, Long changesetId) throws ApiException {
        ApiResponse<Response> resp = updateChangeItemsStateWithHttpInfo(body, changesetId);
        return resp.getData();
    }

    /**
     * Update the change item status based on the fulfiller action
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Review Owner * Separation of Duties owner 
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param changesetId the changeset id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateChangeItemsStateWithHttpInfo(ProvisionItems body, Long changesetId) throws ApiException {
        com.squareup.okhttp.Call call = updateChangeItemsStateValidateBeforeCall(body, changesetId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the change item status based on the fulfiller action (asynchronously)
     * Accepted Roles: * Application Access Change Fulfiller * Application Owner * Change Request Fulfiller * Customer Administrator * Fulfillment Administrator * Review Owner * Separation of Duties owner 
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param changesetId the changeset id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateChangeItemsStateAsync(ProvisionItems body, Long changesetId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateChangeItemsStateValidateBeforeCall(body, changesetId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateChangeItemsStateFF
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param fulfillerId the fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateChangeItemsStateFFCall(ProvisionItems body, Long fulfillerId, Long changesetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/fullfillers/{fulfillerId}/changesets/{changesetId}"
            .replaceAll("\\{" + "fulfillerId" + "\\}", apiClient.escapeString(fulfillerId.toString()))
            .replaceAll("\\{" + "changesetId" + "\\}", apiClient.escapeString(changesetId.toString()));

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
    private com.squareup.okhttp.Call updateChangeItemsStateFFValidateBeforeCall(ProvisionItems body, Long fulfillerId, Long changesetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateChangeItemsStateFF(Async)");
        }
        // verify the required parameter 'fulfillerId' is set
        if (fulfillerId == null) {
            throw new ApiException("Missing the required parameter 'fulfillerId' when calling updateChangeItemsStateFF(Async)");
        }
        // verify the required parameter 'changesetId' is set
        if (changesetId == null) {
            throw new ApiException("Missing the required parameter 'changesetId' when calling updateChangeItemsStateFF(Async)");
        }
        
        com.squareup.okhttp.Call call = updateChangeItemsStateFFCall(body, fulfillerId, changesetId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the change item status based on the fulfiller action  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param fulfillerId the fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateChangeItemsStateFF(ProvisionItems body, Long fulfillerId, Long changesetId) throws ApiException {
        ApiResponse<Response> resp = updateChangeItemsStateFFWithHttpInfo(body, fulfillerId, changesetId);
        return resp.getData();
    }

    /**
     * Update the change item status based on the fulfiller action  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt;
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param fulfillerId the fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateChangeItemsStateFFWithHttpInfo(ProvisionItems body, Long fulfillerId, Long changesetId) throws ApiException {
        com.squareup.okhttp.Call call = updateChangeItemsStateFFValidateBeforeCall(body, fulfillerId, changesetId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the change item status based on the fulfiller action  &lt;br/&gt;&lt;strong&gt;Note: This is used by workflow&lt;/strong&gt;&lt;br/&gt; (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfiller action node containing the actions for the change                         items (required)
     * @param fulfillerId the fulfiller id (required)
     * @param changesetId the changeset id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateChangeItemsStateFFAsync(ProvisionItems body, Long fulfillerId, Long changesetId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateChangeItemsStateFFValidateBeforeCall(body, fulfillerId, changesetId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateChangesetStatus
     * @param body change set (required)
     * @param id the changeset id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateChangesetStatusCall(Changeset body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/changeset/{id}"
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
    private com.squareup.okhttp.Call updateChangesetStatusValidateBeforeCall(Changeset body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateChangesetStatus(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateChangesetStatus(Async)");
        }
        
        com.squareup.okhttp.Call call = updateChangesetStatusCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update the change set status.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body change set (required)
     * @param id the changeset id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateChangesetStatus(Changeset body, Long id) throws ApiException {
        ApiResponse<Response> resp = updateChangesetStatusWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update the change set status.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body change set (required)
     * @param id the changeset id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateChangesetStatusWithHttpInfo(Changeset body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateChangesetStatusValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update the change set status. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body change set (required)
     * @param id the changeset id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateChangesetStatusAsync(Changeset body, Long id, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateChangesetStatusValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateFufillmentTarget
     * @param body the fulfillment target configuration payload (required)
     * @param targetId the fulfillment target id (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateFufillmentTargetCall(ConfigurationTarget body, Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/provisioning/configuration/targets/{targetId}"
            .replaceAll("\\{" + "targetId" + "\\}", apiClient.escapeString(targetId.toString()));

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
    private com.squareup.okhttp.Call updateFufillmentTargetValidateBeforeCall(ConfigurationTarget body, Long targetId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateFufillmentTarget(Async)");
        }
        // verify the required parameter 'targetId' is set
        if (targetId == null) {
            throw new ApiException("Missing the required parameter 'targetId' when calling updateFufillmentTarget(Async)");
        }
        
        com.squareup.okhttp.Call call = updateFufillmentTargetCall(body, targetId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update a configuration target.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment target configuration payload (required)
     * @param targetId the fulfillment target id (required)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response updateFufillmentTarget(ConfigurationTarget body, Long targetId) throws ApiException {
        ApiResponse<Response> resp = updateFufillmentTargetWithHttpInfo(body, targetId);
        return resp.getData();
    }

    /**
     * Update a configuration target.
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment target configuration payload (required)
     * @param targetId the fulfillment target id (required)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> updateFufillmentTargetWithHttpInfo(ConfigurationTarget body, Long targetId) throws ApiException {
        com.squareup.okhttp.Call call = updateFufillmentTargetValidateBeforeCall(body, targetId, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update a configuration target. (asynchronously)
     * Accepted Roles: * Customer Administrator * Fulfillment Administrator 
     * @param body the fulfillment target configuration payload (required)
     * @param targetId the fulfillment target id (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateFufillmentTargetAsync(ConfigurationTarget body, Long targetId, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateFufillmentTargetValidateBeforeCall(body, targetId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
