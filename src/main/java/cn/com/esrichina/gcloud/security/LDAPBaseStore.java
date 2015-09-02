package cn.com.esrichina.gcloud.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.ldap.model.constants.LdapSecurityConstants;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.Response;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchResultEntry;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.password.PasswordUtil;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.springframework.stereotype.Service;

import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.dto.StoreUserSearch;

@Service("ldapBaseStore")
public class LDAPBaseStore {

	public User authenticate(LDAPConfig config, String username, String password) throws LdapException, CursorException {
		LdapConnection ldapConnection = new LdapNetworkConnection(config.getIp(), config.getPort());

		try {
			Dn dn = new Dn(config.getUsernameAttribute() + "=" + username + "," + config.getUserBaseDN());
			ldapConnection.bind(dn, password);

			EntryCursor cursor = ldapConnection.search(dn, "(objectclass=*)", SearchScope.OBJECT);
			if (cursor.next()) {
				Entry entry = cursor.get();
				User user = new User();
				user.setUsername(entry.get(config.getUsernameAttribute()).getString());
				user.setRealName(entry.get(config.getUserFullnameAttribute()).getString());
				user.setEmail(entry.get(config.getUserEmailAttribute()).getString());
				// user.setPassword(password);
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				ldapConnection.unBind();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				ldapConnection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public List<User> searchUser(LDAPConfig config, StoreUserSearch userSearch) throws LdapException, CursorException {
		SearchRequest req = new SearchRequestImpl();
		req.setScope(SearchScope.ONELEVEL);
		req.addAttributes("*");
		req.setTimeLimit(0);
		req.setSizeLimit(userSearch.getCount());
		req.setBase(new Dn(config.getUserBaseDN()));

		StringBuffer sb = new StringBuffer();
		sb.append("(&");
		if (StringUtils.isNotBlank(userSearch.getUsername())) {
			sb.append("(" + config.getUsernameAttribute() + "=" + userSearch.getUsername() + ")");
		}

		if (StringUtils.isNotBlank(userSearch.getEmail())) {
			sb.append("(" + config.getUserEmailAttribute() + "=" + userSearch.getEmail() + ")");
		}

		if (StringUtils.isNotBlank(userSearch.getRealName())) {
			sb.append("(" + config.getUserFullnameAttribute() + "=" + userSearch.getRealName() + ")");
		}
		sb.append(")");

		if (sb.toString().equals("(&)")) {
			req.setFilter("(objectClass=*)");
		} else {
			req.setFilter(sb.toString());
		}

		// SearchControls controls = new SearchControls();
		// controls.setCountLimit((int) LdapServer.NO_SIZE_LIMIT);
		// controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		// PagedResults pagedSearchControl = new PagedResultsDecorator(codec);
		// pagedSearchControl.setSize(3);

		SearchCursor searchCursor = config.getConnection().search(req);

		List<User> users = new ArrayList<User>();

		while (searchCursor.next()) {
			Response response = searchCursor.get();
			if (response instanceof SearchResultEntry) {
				Entry resultEntry = ((SearchResultEntry) response).getEntry();
				User user = new User();
				user.setUsername(resultEntry.get(config.getUsernameAttribute()).getString());
				user.setRealName(resultEntry.get(config.getUserFullnameAttribute()).getString());
				if (resultEntry.containsAttribute(config.getUserEmailAttribute())) {
					user.setEmail(resultEntry.get(config.getUserEmailAttribute()).getString());
				}
				users.add(user);
			}
		}

		return users;
	}

	public User getUser(LDAPConfig config, String username) throws LdapException, CursorException {
		Dn dn = new Dn(config.getUsernameAttribute() + "=" + username + "," + config.getUserBaseDN());
		System.out.println(dn);
		EntryCursor cursor = config.getConnection().search(dn, "(objectclass=*)", SearchScope.OBJECT);
		if (cursor.next()) {
			Entry entry = cursor.get();
			System.out.println(entry);
			User user = new User();
			user.setUsername(entry.get(config.getUsernameAttribute()).getString());
			user.setRealName(entry.get(config.getUserFullnameAttribute()).getString());
			user.setEmail(entry.get(config.getUserEmailAttribute()).getString());
			// user.setPassword(password);
			return user;
		} else {
			return null;
		}
	}

	public void addUser(LDAPConfig config, User user) throws LdapException {
		LdapConnection connection = config.getConnection();

		Entry entry = new DefaultEntry(config.getUsernameAttribute() + "=" + user.getUsername() + "," + config.getUserBaseDN());
		for (String objectClass : config.getObjectClasses()) {
			entry.add("ObjectClass", objectClass);
		}
		entry.add(config.getUserFullnameAttribute(), user.getRealName());
		entry.add(config.getUserEmailAttribute(), user.getEmail());
		entry.add("userPassword", PasswordUtil.createStoragePassword(user.getPassword().getBytes(), LdapSecurityConstants.HASH_METHOD_MD5));

		connection.add(entry);
	}

	public void updateUser(LDAPConfig config, User user) {

	}

	public void deleteUser(LDAPConfig config, String username) throws LdapException {
		config.getConnection().delete("cn=" + username + "," + config.getUserBaseDN());
	}
}