package cn.com.esrichina.gcloud.commons.domain;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "GCLOUD_ACCOUNT")
public class Account extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6182867470848442678L;

	/**
	 * 租户登录名
	 */
	private String shortName;

	/**
	 * 租户名
	 */
	private String name;

	/**
	 * 租户描述
	 */
	private String description;

	/**
	 * 租户可创建的最大站点数
	 */
	private Integer maxSite;

	/**
	 * 资源池组名
	 */
	private String orgName;

	/**
	 * 租户是否可创建高可用站点
	 */
	private Boolean createHASite;

	/**
	 * 租户下的用户默认集群最大节点数
	 */
	private Integer defMaxNode;

	/**
	 * 租户下的用户默认集群最小节点数
	 */
	private Integer defMinNode;

	/**
	 * 租户下的用户默认可创建的最大站点数
	 */
	private Integer defMaxSite;

	/**
	 * 租户下的用户站点默认是否自动调节
	 */
	private Boolean defAutoAdjust;

	/**
	 * 租户下的用户站点集群最小CPU
	 */
	private Integer defMinCPU;

	/**
	 * 租户下的用户站点集群最大CPU
	 */
	private Integer defMaxCPU;

	/**
	 * 租户下的用户站点集群最小CPU持续时间
	 */
	private Integer defMinTime;

	/**
	 * 租户下的用户站点集群最大CPU持续时间
	 */
	private Integer defMaxTime;

	/**
	 * 租户能用的注册数据库资源
	 */
	private Set<String> dbRes;

	/**
	 * 租户能用的存储资源
	 */
	private Set<String> storageRes;

	/**
	 * 租户能用的虚拟机配置
	 */
	private Set<String> vmConfigurations;

	/**
	 * 租户能用的镜像模板类型
	 */
	private Set<String> vmImageTypes;

	/**
	 * 鉴权类型:LDAP or null
	 */
	private String securityType;

	/**
	 * 是否只鉴权
	 */
	private Boolean securityAuthenticateOnly;

	/**
	 * 鉴权配置信息，Json格式
	 */
	private String securityConfig;

	public Account() {
		super();
	}

	public Account(String id) {
		super();
		this.setId(id);
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getMaxSite() {
		return maxSite;
	}

	public void setMaxSite(Integer maxSite) {
		this.maxSite = maxSite;
	}

	public Integer getDefMaxNode() {
		return defMaxNode;
	}

	public void setDefMaxNode(Integer defMaxNode) {
		this.defMaxNode = defMaxNode;
	}

	public Integer getDefMinNode() {
		return defMinNode;
	}

	public void setDefMinNode(Integer defMinNode) {
		this.defMinNode = defMinNode;
	}

	public Integer getDefMaxSite() {
		return defMaxSite;
	}

	public void setDefMaxSite(Integer defMaxSite) {
		this.defMaxSite = defMaxSite;
	}

	public Boolean getDefAutoAdjust() {
		return defAutoAdjust;
	}

	public void setDefAutoAdjust(Boolean defAutoAdjust) {
		this.defAutoAdjust = defAutoAdjust;
	}

	public Integer getDefMinCPU() {
		return defMinCPU;
	}

	public void setDefMinCPU(Integer defMinCPU) {
		this.defMinCPU = defMinCPU;
	}

	public Integer getDefMaxCPU() {
		return defMaxCPU;
	}

	public void setDefMaxCPU(Integer defMaxCPU) {
		this.defMaxCPU = defMaxCPU;
	}

	public Integer getDefMinTime() {
		return defMinTime;
	}

	public void setDefMinTime(Integer defMinTime) {
		this.defMinTime = defMinTime;
	}

	public Integer getDefMaxTime() {
		return defMaxTime;
	}

	public void setDefMaxTime(Integer defMaxTime) {
		this.defMaxTime = defMaxTime;
	}

	public Boolean getCreateHASite() {
		return createHASite;
	}

	public void setCreateHASite(Boolean createHASite) {
		this.createHASite = createHASite;
	}

	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	@Column(length = 2000)
	public String getSecurityConfig() {
		return securityConfig;
	}

	public void setSecurityConfig(String securityConfig) {
		this.securityConfig = securityConfig;
	}

	public Boolean getSecurityAuthenticateOnly() {
		return securityAuthenticateOnly;
	}

	public void setSecurityAuthenticateOnly(Boolean securityAuthenticateOnly) {
		this.securityAuthenticateOnly = securityAuthenticateOnly;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GCLOUD_ACCOUNT_DBRES", joinColumns = @JoinColumn(name = "dbres_id"))
	public Set<String> getDbRes() {
		return dbRes;
	}

	public void setDbRes(Set<String> dbRes) {
		this.dbRes = dbRes;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GCLOUD_ACCOUNT_STORAGERES", joinColumns = @JoinColumn(name = "storageres_id"))
	public Set<String> getStorageRes() {
		return storageRes;
	}

	public void setStorageRes(Set<String> storageRes) {
		this.storageRes = storageRes;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GCLOUD_ACCOUNT_VMCONF", joinColumns = @JoinColumn(name = "vmconf_id"))
	public Set<String> getVmConfigurations() {
		return vmConfigurations;
	}

	public void setVmConfigurations(Set<String> vmConfigurations) {
		this.vmConfigurations = vmConfigurations;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GCLOUD_ACCOUNT_IMAGETYPE", joinColumns = @JoinColumn(name = "imagetype_id"))
	public Set<String> getVmImageTypes() {
		return vmImageTypes;
	}

	public void setVmImageTypes(Set<String> vmImageTypes) {
		this.vmImageTypes = vmImageTypes;
	}

}
