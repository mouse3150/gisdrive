package cn.com.esrichina.gcloud.commons.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "GCLOUD_GLOG")
public class GLog extends BaseEntity {

	private static final long serialVersionUID = -4107300366134408717L;

	public static final String LEVEL_INFO = "info";

	public static final String LEVEL_ERROR = "error";

	private String accountId;

	private String accountName;

	private String accountShortName;

	private String userId;

	private String username;

	private String userRealName;

	private String siteId;

	private String siteFullName;

	private String level;

	private String message;

	private String detailInfo;

	private Boolean special;

	private Boolean isNew;

	public GLog() {
		super();
	}

	public GLog(String accountId, String userId, String errorCode, String message) {
		super();
		this.accountId = accountId;
		this.userId = userId;
		this.message = message;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Index(name = "INDEX_GLOG_ACCOUNTID")
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Index(name = "INDEX_GLOG_USERID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(length = 2000)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Index(name = "INDEX_GLOG_SPECIAL")
	public Boolean getSpecial() {
		return special;
	}

	public void setSpecial(Boolean special) {
		this.special = special;
	}

	@Lob
	@Basic(fetch = FetchType.EAGER)
	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	@Index(name = "INDEX_GLOG_ISNEW")
	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getSiteFullName() {
		return siteFullName;
	}

	public void setSiteFullName(String siteFullName) {
		this.siteFullName = siteFullName;
	}

	public String getAccountShortName() {
		return accountShortName;
	}

	public void setAccountShortName(String accountShortName) {
		this.accountShortName = accountShortName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
