package cn.com.esrichina.gcloud.security.web.request;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import cn.com.esrichina.gcloud.security.domain.Authority;

public class UnlockUserRequest {

	private String description;
	
	private Integer maxSite;

	private Integer minNode;

	private Integer maxNode;

	private Boolean createHASite;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Authority> specAuthorities;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> vmConfigurations;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> vmImageTypes;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public Set<Authority> getSpecAuthorities() {
		return specAuthorities;
	}

	public void setSpecAuthorities(Set<Authority> specAuthorities) {
		this.specAuthorities = specAuthorities;
	}

	public Set<String> getVmConfigurations() {
		return vmConfigurations;
	}

	public void setVmConfigurations(Set<String> vmConfigurations) {
		this.vmConfigurations = vmConfigurations;
	}

	public Set<String> getVmImageTypes() {
		return vmImageTypes;
	}

	public void setVmImageTypes(Set<String> vmImageTypes) {
		this.vmImageTypes = vmImageTypes;
	}

}
