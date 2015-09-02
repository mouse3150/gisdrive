package cn.com.esrichina.gcloud.security;

import java.io.Serializable;
import java.util.Set;

import org.apache.directory.ldap.client.api.LdapConnection;

public class LDAPConfig implements Serializable {

	private static final long serialVersionUID = 3667305733122237715L;

	private transient LdapConnection connection;

	private String ip;

	private Integer port;

	private String user;

	private String userPassword;

	private String isPasswordEncrypted;

	private String userBaseDN;

	private String usernameAttribute;

	private String userFullnameAttribute;

	private String userEmailAttribute;

	private String userSearchAttribute;

	private String caseSensitive;

	private Set<String> objectClasses;

	public LdapConnection getConnection() {
		return connection;
	}

	public void setConnection(LdapConnection connection) {
		this.connection = connection;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getIsPasswordEncrypted() {
		return isPasswordEncrypted;
	}

	public void setIsPasswordEncrypted(String isPasswordEncrypted) {
		this.isPasswordEncrypted = isPasswordEncrypted;
	}

	public String getUserBaseDN() {
		return userBaseDN;
	}

	public void setUserBaseDN(String userBaseDN) {
		this.userBaseDN = userBaseDN;
	}

	public String getUsernameAttribute() {
		return usernameAttribute;
	}

	public void setUsernameAttribute(String usernameAttribute) {
		this.usernameAttribute = usernameAttribute;
	}

	public String getUserFullnameAttribute() {
		return userFullnameAttribute;
	}

	public void setUserFullnameAttribute(String userFullnameAttribute) {
		this.userFullnameAttribute = userFullnameAttribute;
	}

	public String getUserEmailAttribute() {
		return userEmailAttribute;
	}

	public void setUserEmailAttribute(String userEmailAttribute) {
		this.userEmailAttribute = userEmailAttribute;
	}

	public String getUserSearchAttribute() {
		return userSearchAttribute;
	}

	public void setUserSearchAttribute(String userSearchAttribute) {
		this.userSearchAttribute = userSearchAttribute;
	}

	public String getCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(String caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public Set<String> getObjectClasses() {
		return objectClasses;
	}

	public void setObjectClasses(Set<String> objectClasses) {
		this.objectClasses = objectClasses;
	}

}
