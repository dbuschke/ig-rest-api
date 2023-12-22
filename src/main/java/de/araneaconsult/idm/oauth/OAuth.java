package de.araneaconsult.idm.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringJoiner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

public class OAuth {
	//https://t-dbu-01.araneaconsult.de:8543 rbpm novell uaadmin novell header
		
	private String baseURL;
	private String clientID;
	private String clientPwd;
	
	/**
	 * @param baseURL Base URL of OSP (e.g. https://myserver:8543/osp)
	 * @param clientID as specified in configupdate.sh
	 * @param clientPwd as specified in configupdate.sh
	 */
	public OAuth(String baseURL, String clientID, String clientPwd) {
		this.baseURL = baseURL;
		this.clientID = clientID;
		this.clientPwd = clientPwd;
	}
	
	/**
	 * Requesting a token the "browser" way
	 * 
	 * @param redirectURL URL which the user should be send to by OSP after successful authentication
	 * @param code Code which will be send back after successful authentication
	 * @return Token to use for requests
	 * @throws IOException
	 * @throws ResponseException
	 */
	public Token requestUserToken(String redirectURL, String code) throws IOException, ResponseException {
		StringJoiner sj = new StringJoiner("&");
		sj.add("grant_type=authorization_code");
		sj.add("response_type=token");
		sj.add("redirect_uri=" + redirectURL);
		sj.add("code=" + URLEncoder.encode(code, "UTF-8"));
		
		String res = post(baseURL + "/a/idm/auth/oauth2/grant", clientID, clientPwd, "application/x-www-form-urlencoded", sj.toString());
		return json2Object(res, Token.class);
	}

	/**
	 * Requesting a token the "server" way
	 * 
	 * @param user User which should be used to authenticate
	 * @param pass Password which should be used to authenticate
	 * @return Token to use for requests
	 * @throws IOException
	 * @throws ResponseException
	 */
	public Token requestToken(String user, String pass) throws IOException, ResponseException {
		String res = post(baseURL + "/a/idm/auth/oauth2/grant", clientID, clientPwd, "application/x-www-form-urlencoded", "grant_type=password&username=" + user + "&password=" + pass);
		return json2Object(res, Token.class);
	}
	
	/**
	 * Refresh a nearly expired token (w/o authenticating)
	 * 
	 * @param token Token provided by OSP
	 * @return new Token to use for requests
	 * @throws IOException
	 * @throws ResponseException
	 */
	public RefreshToken refreshToken(Token token) throws IOException, ResponseException {
		String res = post(baseURL + "/a/idm/auth/oauth2/grant", clientID, clientPwd, "application/x-www-form-urlencoded", "grant_type=refresh_token&refresh_token=" + token.getRefreshToken());
		return json2Object(res, RefreshToken.class);
	}
	
	/**
	 * Get further informations about an User from OSP
	 * 
	 * @param token Token provided by OSP
	 * @param attributes (not used)
	 * @return User attributes (last_name,first_name,roles,initials,name,client,cacheable,expiration,auth_src_id,email)
	 * @throws IOException
	 * @throws ResponseException
	 */
	public SSOUser requestUserAttributes(Token token, String attributes) throws IOException, ResponseException {
		StringJoiner sj = new StringJoiner("&");
		sj.add("attributes=" + URLEncoder.encode("last_name,first_name,roles,initials,name,client,cacheable,expiration,auth_src_id,email", "UTF-8"));
		sj.add("access_token=" + URLEncoder.encode(token.getAccessToken(), "UTF-8"));

		String res = get(baseURL + "/a/idm/auth/oauth2/getattributes?" + sj.toString(), clientID, clientPwd);
		return json2Object(res, SSOUser.class);
	}

	/**
	 * @param rediretURL
	 * @param sso_state
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getAuthenticationURL(String rediretURL, String sso_state) throws UnsupportedEncodingException {
		return baseURL + "/a/idm/auth/oauth2/grant?response_type=code&redirect_uri=" + URLEncoder.encode(rediretURL, "UTF-8") + "&client_id=rc&state=" + URLEncoder.encode(sso_state, "UTF-8");
	}

	private <T> T json2Object(String json, Class<T> clazz) {
		Gson gson = new Gson();
		T obj = gson.fromJson(json, clazz);
		return obj;
	}


	private String post(String surl, String user, String pass, String contentType, String data) throws IOException, ResponseException {
		return request("POST", surl, user, pass, contentType, data);
	}

	private String get(String surl, String user, String pass) throws IOException, ResponseException {
		return request("GET", surl, user, pass, null, null);
	}

	private String request(String method, String surl, String user, String pass, String contentType, String data) throws IOException, ResponseException {
		HttpsURLConnection connection = null;
		try {
			URL url = new URL(surl);
			connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestMethod(method);
			if (contentType != null) {
				connection.setRequestProperty("Content-Type", contentType);
			}
			
			if (user != null && pass != null) {
				String encodedAuth = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));
				connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
			}
			
			// Send Data
			if (data != null) {
				connection.setDoOutput(true);
				OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				osw.write(data);
				osw.flush();
				osw.close();
			}
			
			// timeouts against blocking software
			connection.setConnectTimeout(3 * 1000);
			connection.setReadTimeout(30 * 1000);
			
			// Get Response
			int responseCode = connection.getResponseCode();
			if (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
				return getResponse(connection.getInputStream());
			} else {
				String errorResponse = getResponse(connection.getErrorStream());
				throw new ResponseException(responseCode, errorResponse);
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private String getResponse(InputStream is) throws IOException {
		if (is != null) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();
			
			return response.toString();
		} else {
			return "";
		}
	}
}
