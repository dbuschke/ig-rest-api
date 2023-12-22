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


import io.swagger.client.model.Burole;
import io.swagger.client.model.Filter;
import io.swagger.client.model.Role;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltersApi {
    private ApiClient apiClient;

    public FiltersApi() {
        this(Configuration.getDefaultApiClient());
    }

    public FiltersApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for deleteFilter
     * @param id - The entity filter ID. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteFilterCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/filters/{id}"
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
    private com.squareup.okhttp.Call deleteFilterValidateBeforeCall(Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteFilter(Async)");
        }
        
        com.squareup.okhttp.Call call = deleteFilterCall(id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Delete the entity filter with the given ID.
     * Accepted Roles: * All Access 
     * @param id - The entity filter ID. (required)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role deleteFilter(Long id) throws ApiException {
        ApiResponse<Role> resp = deleteFilterWithHttpInfo(id);
        return resp.getData();
    }

    /**
     * Delete the entity filter with the given ID.
     * Accepted Roles: * All Access 
     * @param id - The entity filter ID. (required)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> deleteFilterWithHttpInfo(Long id) throws ApiException {
        com.squareup.okhttp.Call call = deleteFilterValidateBeforeCall(id, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete the entity filter with the given ID. (asynchronously)
     * Accepted Roles: * All Access 
     * @param id - The entity filter ID. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteFilterAsync(Long id, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deleteFilterValidateBeforeCall(id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFilter
     * @param propertyClass - Type of filters to return for the user (required)
     * @param id - The filter id. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFilterCall(String propertyClass, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/filters/{class}/{id}"
            .replaceAll("\\{" + "class" + "\\}", apiClient.escapeString(propertyClass.toString()))
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
    private com.squareup.okhttp.Call getFilterValidateBeforeCall(String propertyClass, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'propertyClass' is set
        if (propertyClass == null) {
            throw new ApiException("Missing the required parameter 'propertyClass' when calling getFilter(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getFilter(Async)");
        }
        
        com.squareup.okhttp.Call call = getFilterCall(propertyClass, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the filter.
     * Accepted Roles: * All Access 
     * @param propertyClass - Type of filters to return for the user (required)
     * @param id - The filter id. (required)
     * @return Filter
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Filter getFilter(String propertyClass, Long id) throws ApiException {
        ApiResponse<Filter> resp = getFilterWithHttpInfo(propertyClass, id);
        return resp.getData();
    }

    /**
     * Get the filter.
     * Accepted Roles: * All Access 
     * @param propertyClass - Type of filters to return for the user (required)
     * @param id - The filter id. (required)
     * @return ApiResponse&lt;Filter&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Filter> getFilterWithHttpInfo(String propertyClass, Long id) throws ApiException {
        com.squareup.okhttp.Call call = getFilterValidateBeforeCall(propertyClass, id, null, null);
        Type localVarReturnType = new TypeToken<Filter>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the filter. (asynchronously)
     * Accepted Roles: * All Access 
     * @param propertyClass - Type of filters to return for the user (required)
     * @param id - The filter id. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFilterAsync(String propertyClass, Long id, final ApiCallback<Filter> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFilterValidateBeforeCall(propertyClass, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Filter>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getFilters
     * @param propertyClass - Type of filters to return for the user (required)
     * @param q - String to filter the entity filters (optional)
     * @param sortBy - Column to sort by (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param quickInfo - Return quick info only, no filter data. (optional, default to false)
     * @param indexFrom - Starting index (optional, default to 0)
     * @param size - Number of filters to return. (optional, default to 0)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getFiltersCall(String propertyClass, String q, String sortBy, String sortOrder, Boolean quickInfo, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/filters/{class}"
            .replaceAll("\\{" + "class" + "\\}", apiClient.escapeString(propertyClass.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (q != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("q", q));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortOrder", sortOrder));
        if (quickInfo != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("quickInfo", quickInfo));
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
    private com.squareup.okhttp.Call getFiltersValidateBeforeCall(String propertyClass, String q, String sortBy, String sortOrder, Boolean quickInfo, Integer indexFrom, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'propertyClass' is set
        if (propertyClass == null) {
            throw new ApiException("Missing the required parameter 'propertyClass' when calling getFilters(Async)");
        }
        
        com.squareup.okhttp.Call call = getFiltersCall(propertyClass, q, sortBy, sortOrder, quickInfo, indexFrom, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get the logged in users filters for the given entity type
     * Accepted Roles: * All Access 
     * @param propertyClass - Type of filters to return for the user (required)
     * @param q - String to filter the entity filters (optional)
     * @param sortBy - Column to sort by (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param quickInfo - Return quick info only, no filter data. (optional, default to false)
     * @param indexFrom - Starting index (optional, default to 0)
     * @param size - Number of filters to return. (optional, default to 0)
     * @return Role
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Role getFilters(String propertyClass, String q, String sortBy, String sortOrder, Boolean quickInfo, Integer indexFrom, Integer size) throws ApiException {
        ApiResponse<Role> resp = getFiltersWithHttpInfo(propertyClass, q, sortBy, sortOrder, quickInfo, indexFrom, size);
        return resp.getData();
    }

    /**
     * Get the logged in users filters for the given entity type
     * Accepted Roles: * All Access 
     * @param propertyClass - Type of filters to return for the user (required)
     * @param q - String to filter the entity filters (optional)
     * @param sortBy - Column to sort by (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param quickInfo - Return quick info only, no filter data. (optional, default to false)
     * @param indexFrom - Starting index (optional, default to 0)
     * @param size - Number of filters to return. (optional, default to 0)
     * @return ApiResponse&lt;Role&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Role> getFiltersWithHttpInfo(String propertyClass, String q, String sortBy, String sortOrder, Boolean quickInfo, Integer indexFrom, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = getFiltersValidateBeforeCall(propertyClass, q, sortBy, sortOrder, quickInfo, indexFrom, size, null, null);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the logged in users filters for the given entity type (asynchronously)
     * Accepted Roles: * All Access 
     * @param propertyClass - Type of filters to return for the user (required)
     * @param q - String to filter the entity filters (optional)
     * @param sortBy - Column to sort by (optional, default to name)
     * @param sortOrder - Sort order. Valid values: ASC or DESC (optional, default to ASC)
     * @param quickInfo - Return quick info only, no filter data. (optional, default to false)
     * @param indexFrom - Starting index (optional, default to 0)
     * @param size - Number of filters to return. (optional, default to 0)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getFiltersAsync(String propertyClass, String q, String sortBy, String sortOrder, Boolean quickInfo, Integer indexFrom, Integer size, final ApiCallback<Role> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getFiltersValidateBeforeCall(propertyClass, q, sortBy, sortOrder, quickInfo, indexFrom, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Role>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for saveEntityFilter
     * @param body The entity filter to create. (required)
     * @param overwrite - Allow an existing policy to be overwritten (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call saveEntityFilterCall(Filter body, Boolean overwrite, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/filters";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (overwrite != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("overwrite", overwrite));

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
    private com.squareup.okhttp.Call saveEntityFilterValidateBeforeCall(Filter body, Boolean overwrite, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling saveEntityFilter(Async)");
        }
        
        com.squareup.okhttp.Call call = saveEntityFilterCall(body, overwrite, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Create a new entity filter.
     * Accepted Roles: * All Access 
     * @param body The entity filter to create. (required)
     * @param overwrite - Allow an existing policy to be overwritten (optional, default to false)
     * @return Burole
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Burole saveEntityFilter(Filter body, Boolean overwrite) throws ApiException {
        ApiResponse<Burole> resp = saveEntityFilterWithHttpInfo(body, overwrite);
        return resp.getData();
    }

    /**
     * Create a new entity filter.
     * Accepted Roles: * All Access 
     * @param body The entity filter to create. (required)
     * @param overwrite - Allow an existing policy to be overwritten (optional, default to false)
     * @return ApiResponse&lt;Burole&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Burole> saveEntityFilterWithHttpInfo(Filter body, Boolean overwrite) throws ApiException {
        com.squareup.okhttp.Call call = saveEntityFilterValidateBeforeCall(body, overwrite, null, null);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a new entity filter. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body The entity filter to create. (required)
     * @param overwrite - Allow an existing policy to be overwritten (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call saveEntityFilterAsync(Filter body, Boolean overwrite, final ApiCallback<Burole> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = saveEntityFilterValidateBeforeCall(body, overwrite, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Burole>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateFilter
     * @param body The update to the entity filter. (required)
     * @param id - The filter id. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateFilterCall(Filter body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/filters/{id}"
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
    private com.squareup.okhttp.Call updateFilterValidateBeforeCall(Filter body, Long id, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateFilter(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateFilter(Async)");
        }
        
        com.squareup.okhttp.Call call = updateFilterCall(body, id, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update filter.
     * Accepted Roles: * All Access 
     * @param body The update to the entity filter. (required)
     * @param id - The filter id. (required)
     * @return Filter
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Filter updateFilter(Filter body, Long id) throws ApiException {
        ApiResponse<Filter> resp = updateFilterWithHttpInfo(body, id);
        return resp.getData();
    }

    /**
     * Update filter.
     * Accepted Roles: * All Access 
     * @param body The update to the entity filter. (required)
     * @param id - The filter id. (required)
     * @return ApiResponse&lt;Filter&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Filter> updateFilterWithHttpInfo(Filter body, Long id) throws ApiException {
        com.squareup.okhttp.Call call = updateFilterValidateBeforeCall(body, id, null, null);
        Type localVarReturnType = new TypeToken<Filter>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update filter. (asynchronously)
     * Accepted Roles: * All Access 
     * @param body The update to the entity filter. (required)
     * @param id - The filter id. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateFilterAsync(Filter body, Long id, final ApiCallback<Filter> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateFilterValidateBeforeCall(body, id, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Filter>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
