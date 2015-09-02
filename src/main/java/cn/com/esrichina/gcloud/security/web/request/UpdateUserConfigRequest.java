package cn.com.esrichina.gcloud.security.web.request;

import java.util.Set;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;
import cn.com.esrichina.gcloud.security.domain.Authority;

public class UpdateUserConfigRequest extends RestRequest {
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
