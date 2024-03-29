package de.araneaconsult.idm.oauth;

public class RefreshToken {
	private String access_token;
	private String token_type;
	private Integer expires_in;
	
	public String getAccessToken() {
		return access_token;
	}
	
	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}
	
	public String getTokenType() {
		return token_type;
	}
	
	public void setTokenType(String token_type) {
		this.token_type = token_type;
	}
	
	public Integer getExpiresIn() {
		return expires_in;
	}
	
	public void setExpiresIn(Integer expires_in) {
		this.expires_in = expires_in;
	}
}
