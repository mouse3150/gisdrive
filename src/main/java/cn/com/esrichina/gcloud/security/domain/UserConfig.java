package cn.com.esrichina.gcloud.security.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

/**
 * 用户的业务相关配置，目前暂时先是这些，以后可以再增加
 */
@Entity
@Table(name = "SECURITY_USER_CONFIG")
public class UserConfig extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -2751846057633615144L;

	private String userId;

	private Integer maxSite;

	private Integer minNode;

	private Integer maxNode;

	private Boolean createHASite;

	private Set<Authority> specAuthorities;

	private Set<String> vmConfigurations;

	private Set<String> vmImageTypes;

	public Integer getMaxSite() {
		return maxSite;
	}

	public void setMaxSite(Integer maxSite) {
		this.maxSite = maxSite;
	}

	public Integer getMinNode() {
		return minNode;
	}

	public void setMinNode(Integer minNode) {
		this.minNode = minNode;
	}

	public Integer getMaxNode() {
		return maxNode;
	}

	public void setMaxNode(Integer maxNode) {
		this.maxNode = maxNode;
	}

	public Boolean getCreateHASite() {
		return createHASite;
	}

	public void setCreateHASite(Boolean createHASite) {
		this.createHASite = createHASite;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@OneToMany(fetch = FetchType.EAGER)
	@CollectionTable(name = "GCLOUD_USERCONFIG_AUTHORITIES", joinColumns = @JoinColumn(name = "auth_id"))
	public Set<Authority> getSpecAuthorities() {
		return specAuthorities;
	}

	public void setSpecAuthorities(Set<Authority> specAuthorities) {
		this.specAuthorities = specAuthorities;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GCLOUD_USERCONFIG_VMCONF", joinColumns = @JoinColumn(name = "vmconf_id"))
	public Set<String> getVmConfigurations() {
		return vmConfigurations;
	}

	public void setVmConfigurations(Set<String> vmConfigurations) {
		this.vmConfigurations = vmConfigurations;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GCLOUD_USERCONFIG_IMAGETYPE", joinColumns = @JoinColumn(name = "imagetype_id"))
	public Set<String> getVmImageTypes() {
		return vmImageTypes;
	}

	public void setVmImageTypes(Set<String> vmImageTypes) {
		this.vmImageTypes = vmImageTypes;
	}

}