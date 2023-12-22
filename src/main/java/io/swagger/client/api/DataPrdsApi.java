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


import io.swagger.client.model.ModelConfiguration;
import io.swagger.client.model.Prd;
import io.swagger.client.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPrdsApi {
    private ApiClient apiClient;

    public DataPrdsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DataPrdsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getPrdByDn
     * @param body PRD request with the dn of the prd to get details for. Note that
        although this JSON structure contains a display name and
        description, only the dn is used to lookup the actual prd from the
        remote RBPM server. (required)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioing\&quot; for ones that can be used for IDM Resource         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission         synchronization. (optional, default to ExternalProvisioning)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPrdByDnCall(Prd body, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/prds/prd";

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
    private com.squareup.okhttp.Call getPrdByDnValidateBeforeCall(Prd body, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling getPrdByDn(Async)");
        }
        
        com.squareup.okhttp.Call call = getPrdByDnCall(body, type, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get details of a particular prd.
     * Note that even though this only returns  data, a POST is used to avoid problems of passing a DN as an URL param.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body PRD request with the dn of the prd to get details for. Note that
        although this JSON structure contains a display name and
        description, only the dn is used to lookup the actual prd from the
        remote RBPM server. (required)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioing\&quot; for ones that can be used for IDM Resource         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission         synchronization. (optional, default to ExternalProvisioning)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPrdByDn(Prd body, String type) throws ApiException {
        ApiResponse<Response> resp = getPrdByDnWithHttpInfo(body, type);
        return resp.getData();
    }

    /**
     * Get details of a particular prd.
     * Note that even though this only returns  data, a POST is used to avoid problems of passing a DN as an URL param.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body PRD request with the dn of the prd to get details for. Note that
        although this JSON structure contains a display name and
        description, only the dn is used to lookup the actual prd from the
        remote RBPM server. (required)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioing\&quot; for ones that can be used for IDM Resource         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission         synchronization. (optional, default to ExternalProvisioning)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPrdByDnWithHttpInfo(Prd body, String type) throws ApiException {
        com.squareup.okhttp.Call call = getPrdByDnValidateBeforeCall(body, type, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get details of a particular prd. (asynchronously)
     * Note that even though this only returns  data, a POST is used to avoid problems of passing a DN as an URL param.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body PRD request with the dn of the prd to get details for. Note that
        although this JSON structure contains a display name and
        description, only the dn is used to lookup the actual prd from the
        remote RBPM server. (required)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioing\&quot; for ones that can be used for IDM Resource         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission         synchronization. (optional, default to ExternalProvisioning)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPrdByDnAsync(Prd body, String type, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPrdByDnValidateBeforeCall(body, type, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getPrds
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, 0 - no limit (optional, default to 10)
     * @param showCt show total result set count, true or false (optional, default to false)
     * @param q Case insensitive quick search filter value, can be * meaning no         filter, otherwise it looks for that string somewhere in prd name         or description (optional)
     * @param qMatch match mode (not used at the present time) (optional)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioning\&quot; for ones that can be used for IDM Resource,         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission,         or \&quot;Approval\&quot; for PRD approvals of access requests         synchronization. (optional, default to ExternalProvisioning)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getPrdsCall(Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/data/prds";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
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
    private com.squareup.okhttp.Call getPrdsValidateBeforeCall(Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String type, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        com.squareup.okhttp.Call call = getPrdsCall(indexFrom, size, showCt, q, qMatch, type, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Gets a list of external workflows which can be used to process access  review fulfillment.
     * The PRDs are fetched from the remote IDM RBPM system  which has been defined on the settings page, or via REST endpoint  /api/configuration/provisioning. The list can be filtered. It is  important to note that there is a built in filter in addition to the  name/description filter. By passing in type of \&quot;ExternalProvisioing\&quot;,  only PRDs which have an input field of &#x27;changesetId&#x27; and &#x27;appId&#x27; are  considered. This is because those values are passed to the PRD when it is  initiated during the fulfillment process. By passing in type of  \&quot;ResourceProvisioning\&quot;, the API brings back PRDS that can be used for IDM  Resource Provisioning which are used during IDM Resource to Identity  Governance Permission synchronization.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, 0 - no limit (optional, default to 10)
     * @param showCt show total result set count, true or false (optional, default to false)
     * @param q Case insensitive quick search filter value, can be * meaning no         filter, otherwise it looks for that string somewhere in prd name         or description (optional)
     * @param qMatch match mode (not used at the present time) (optional)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioning\&quot; for ones that can be used for IDM Resource,         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission,         or \&quot;Approval\&quot; for PRD approvals of access requests         synchronization. (optional, default to ExternalProvisioning)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response getPrds(Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String type) throws ApiException {
        ApiResponse<Response> resp = getPrdsWithHttpInfo(indexFrom, size, showCt, q, qMatch, type);
        return resp.getData();
    }

    /**
     * Gets a list of external workflows which can be used to process access  review fulfillment.
     * The PRDs are fetched from the remote IDM RBPM system  which has been defined on the settings page, or via REST endpoint  /api/configuration/provisioning. The list can be filtered. It is  important to note that there is a built in filter in addition to the  name/description filter. By passing in type of \&quot;ExternalProvisioing\&quot;,  only PRDs which have an input field of &#x27;changesetId&#x27; and &#x27;appId&#x27; are  considered. This is because those values are passed to the PRD when it is  initiated during the fulfillment process. By passing in type of  \&quot;ResourceProvisioning\&quot;, the API brings back PRDS that can be used for IDM  Resource Provisioning which are used during IDM Resource to Identity  Governance Permission synchronization.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, 0 - no limit (optional, default to 10)
     * @param showCt show total result set count, true or false (optional, default to false)
     * @param q Case insensitive quick search filter value, can be * meaning no         filter, otherwise it looks for that string somewhere in prd name         or description (optional)
     * @param qMatch match mode (not used at the present time) (optional)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioning\&quot; for ones that can be used for IDM Resource,         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission,         or \&quot;Approval\&quot; for PRD approvals of access requests         synchronization. (optional, default to ExternalProvisioning)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> getPrdsWithHttpInfo(Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String type) throws ApiException {
        com.squareup.okhttp.Call call = getPrdsValidateBeforeCall(indexFrom, size, showCt, q, qMatch, type, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of external workflows which can be used to process access  review fulfillment. (asynchronously)
     * The PRDs are fetched from the remote IDM RBPM system  which has been defined on the settings page, or via REST endpoint  /api/configuration/provisioning. The list can be filtered. It is  important to note that there is a built in filter in addition to the  name/description filter. By passing in type of \&quot;ExternalProvisioing\&quot;,  only PRDs which have an input field of &#x27;changesetId&#x27; and &#x27;appId&#x27; are  considered. This is because those values are passed to the PRD when it is  initiated during the fulfillment process. By passing in type of  \&quot;ResourceProvisioning\&quot;, the API brings back PRDS that can be used for IDM  Resource Provisioning which are used during IDM Resource to Identity  Governance Permission synchronization.&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Access Change Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Application Administrator * Application Owner * Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Authenticated User&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Business Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Change Request Fulfiller&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Customer Administrator * Data Administrator * Escalation Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Fulfillment Administrator * Historical Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Historical Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Report Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Auditor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Monitor&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Review Owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Reviewer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Role owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Security Officer&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Separation of Duties owner&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * SoD Step Approver&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) * Technical Roles Administrator&amp;nbsp(requires url param of &#x27;attrFilter&#x3D;quickInfo&#x27;) 
     * @param indexFrom result set start position (optional, default to 0)
     * @param size size to return in result set, 0 - no limit (optional, default to 10)
     * @param showCt show total result set count, true or false (optional, default to false)
     * @param q Case insensitive quick search filter value, can be * meaning no         filter, otherwise it looks for that string somewhere in prd name         or description (optional)
     * @param qMatch match mode (not used at the present time) (optional)
     * @param type type of prd, pass in \&quot;ExternalProvisioning\&quot; (or null) to return         prds that can be used for external provisioning, or         \&quot;ResourceProvisioning\&quot; for ones that can be used for IDM Resource,         or \&quot;Requstable\&quot; for ones that can be requested Provisioning used         during IDM Resource to Identity Governance Permission,         or \&quot;Approval\&quot; for PRD approvals of access requests         synchronization. (optional, default to ExternalProvisioning)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getPrdsAsync(Integer indexFrom, Integer size, Boolean showCt, String q, String qMatch, String type, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getPrdsValidateBeforeCall(indexFrom, size, showCt, q, qMatch, type, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for testConnection
     * @param body a configuration node with the following keys:
        &lt;ul&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.url&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.userid&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.password&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.cert&lt;/li&gt;
        &lt;/ul&gt; (required)
     * @param permissionSyncSupport pass in true if you only wish to verify the remote system supports         permission sync (i.e. idm rbpm 4.5 or higher (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call testConnectionCall(ModelConfiguration body, Boolean permissionSyncSupport, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/data/prds/externalSystem/test";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (permissionSyncSupport != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("permissionSyncSupport", permissionSyncSupport));

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
    private com.squareup.okhttp.Call testConnectionValidateBeforeCall(ModelConfiguration body, Boolean permissionSyncSupport, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling testConnection(Async)");
        }
        
        com.squareup.okhttp.Call call = testConnectionCall(body, permissionSyncSupport, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Tests the connection, verifies external workflow engine values are  correct.
     * Note that even though this only performs a test of the  connection, a POST to workaround not being able to pass urls as an url  param&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body a configuration node with the following keys:
        &lt;ul&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.url&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.userid&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.password&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.cert&lt;/li&gt;
        &lt;/ul&gt; (required)
     * @param permissionSyncSupport pass in true if you only wish to verify the remote system supports         permission sync (i.e. idm rbpm 4.5 or higher (optional, default to false)
     * @return Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response testConnection(ModelConfiguration body, Boolean permissionSyncSupport) throws ApiException {
        ApiResponse<Response> resp = testConnectionWithHttpInfo(body, permissionSyncSupport);
        return resp.getData();
    }

    /**
     * Tests the connection, verifies external workflow engine values are  correct.
     * Note that even though this only performs a test of the  connection, a POST to workaround not being able to pass urls as an url  param&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body a configuration node with the following keys:
        &lt;ul&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.url&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.userid&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.password&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.cert&lt;/li&gt;
        &lt;/ul&gt; (required)
     * @param permissionSyncSupport pass in true if you only wish to verify the remote system supports         permission sync (i.e. idm rbpm 4.5 or higher (optional, default to false)
     * @return ApiResponse&lt;Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Response> testConnectionWithHttpInfo(ModelConfiguration body, Boolean permissionSyncSupport) throws ApiException {
        com.squareup.okhttp.Call call = testConnectionValidateBeforeCall(body, permissionSyncSupport, null, null);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Tests the connection, verifies external workflow engine values are  correct. (asynchronously)
     * Note that even though this only performs a test of the  connection, a POST to workaround not being able to pass urls as an url  param&lt;br/&gt;Accepted Roles: * Access Request Administrator * Application Administrator * Application Owner * Auditor * Customer Administrator * Data Administrator * Fulfillment Administrator * Review Administrator * Security Officer * Separation of Duties Administrator * Technical Roles Administrator 
     * @param body a configuration node with the following keys:
        &lt;ul&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.url&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.userid&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.password&lt;/li&gt;
        &lt;li&gt;com.netiq.iac.externalProvisioning.cert&lt;/li&gt;
        &lt;/ul&gt; (required)
     * @param permissionSyncSupport pass in true if you only wish to verify the remote system supports         permission sync (i.e. idm rbpm 4.5 or higher (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call testConnectionAsync(ModelConfiguration body, Boolean permissionSyncSupport, final ApiCallback<Response> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = testConnectionValidateBeforeCall(body, permissionSyncSupport, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Response>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
