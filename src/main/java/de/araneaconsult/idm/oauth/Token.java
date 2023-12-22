package de.araneaconsult.idm.oauth;

import java.util.Date;

public class Token {
	private String access_token;
	private String token_type;
	private Integer expires_in;
	private String refresh_token;
	private Date lastRefresh = new Date();
	
	/**
	 * Getting the access token which should be used for further requests
	 * 
	 * @return Token
	 */
	public String getAccessToken() {
		return access_token;
	}
	
	/**
	 * @param access_token
	 */
	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}
	
	/**
	 * Type of Access Token
	 * 
	 * @return token type
	 */
	public String getTokenType() {
		return token_type;
	}
	
	/**
	 * @param token_type
	 */
	public void setTokenType(String token_type) {
		this.token_type = token_type;
	}
	
	/**
	 * Expiration of this token in seconds since requesting this token
	 * 
	 * @return expiration in seconds
	 */
	public Integer getExpiresIn() {
		return expires_in;
	}
	
	/**
	 * @param expires_in
	 */
	public void setExpiresIn(Integer expires_in) {
		this.expires_in = expires_in;
		lastRefresh = new Date();
	}
	
	/**
	 * Expiration of this token as absolute Unix timestamp in msec (Java default)
	 * 
	 * @return Unix timestamp of token expiration in msec
	 */
	public long getExpirationTimestamp() {
		if (expires_in != null) {
			return lastRefresh.getTime() + (expires_in * 1000);
		} else {
			return -1;
		}
	}
	
	/**
	 * Getting refreshed token send by OSP
	 * @see OAuth.refreshToken(Token)
	 * 
	 * @return
	 */
	public String getRefreshToken() {
		return refresh_token;
	}
	
	/**
	 * @param refresh_token
	 */
	public void setRefreshToken(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
	/**
	 * @return
	 */
	public String getAuthHeader() {
		return getTokenType() + " " + getAccessToken();
	}
	
	/**
	 * After requesting a new token by refresh method use this to update this token object
	 * @see OAuth.refreshToken(Token)
	 * 
	 * @param refreshToken
	 */
	public void update(RefreshToken refreshToken) {
		setAccessToken(refreshToken.getAccessToken());
		setTokenType(refreshToken.getTokenType());
		setExpiresIn(refreshToken.getExpiresIn());
	}
}
