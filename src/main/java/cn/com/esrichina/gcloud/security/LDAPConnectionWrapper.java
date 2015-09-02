package cn.com.esrichina.gcloud.security;

import org.apache.directory.ldap.client.api.LdapConnection;

public class LDAPConnectionWrapper {

	private LdapConnection connection;

	private String userBase;

	private String adminUser;

	private String adminPassword;

	public LdapConnection getConnection() {
		return connection;
	}

	public void setConnection(LdapConnection connection) {
		this.connection = connection;
	}

	public String getUserBase() {
		return userBase;
	}

	public void setUserBase(String userBase) {
		this.userBase = userBase;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

}
