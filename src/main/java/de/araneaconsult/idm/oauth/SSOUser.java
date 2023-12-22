package de.araneaconsult.idm.oauth;

import java.util.Date;

import com.google.gson.annotations.JsonAdapter;

import de.araneaconsult.json.SingleStringArrayJsonAdapter;

public class SSOUser {
	// {"first_name":"UserApplication","last_name":"Administrator","roles":["cn=secAdmin,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=roleManager,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=reportAdmin,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=rbpmAdmin,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=resourceAdmin,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=roleAdmin,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=complianceAdmin,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=provManager,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system","cn=provAdmin,cn=System,cn=Level20,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=User Application Driver,cn=driverset1,o=system"],"name":"cn=uaadmin,ou=sa,o=data","client":"rc","auth_src_id":"bisadus","expiration":"1536158513588"}
	private String first_name;
	private String last_name;
	@JsonAdapter(SingleStringArrayJsonAdapter.class)
	private String[] roles;
	private String name;
	private String client;
	private String auth_src_id;
	private long expiration = -1;
	private Token token;
	
	public String getFirst_name() {
		return first_name;
	}
	
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getAuth_src_id() {
		return auth_src_id;
	}

	public void setAuth_src_id(String auth_src_id) {
		this.auth_src_id = auth_src_id;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
	
	public boolean check() {
		Date now = new Date();
		if (now.getTime() > token.getExpirationTimestamp()) {
			return false;
		}
		
		return true;
	}
}
