package cn.com.esrichina.gcloud.commons.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "GCLOUD_ALERTMSG")
public class AlertMsg extends BaseEntity {

	private static final long serialVersionUID = 4038317297443770461L;

	public static final String TARGET_TYPE_AUTOPROXY = "autoproxy";
	public static final String TARGET_TYPE_ACCOUNT_OVERVIEW = "account_overview";
	public static final String TARGET_TYPE_ACCOUNT_PORTAL_APPLY = "account_portal_apply";
	public static final String TARGET_TYPE_PORTAL = "portal";
	public static final String TARGET_TYPE_SITE = "site";
	public static final String TARGET_TYPE_VM = "vm";
	public static final String TARGET_TYPE_RESOURCEPOOL = "resource_pool";
	public static final String TARGET_TYPE_OPERATIONLOG = "operationlog";

	public static final String LEVEL_INFO = "info";
	public static final String LEVEL_WARN = "warn";
	public static final String LEVEL_ERROR = "error";

	public static final String STATUS_NEW = "new";
	public static final String STATUS_RESOLVED = "resolved";

	// 对象类型
	private String targetType;

	// 对象唯一标示
	private String targetId;

	private String targetName;

	private String level;

	private String msgKey;

	private String msgDetail;

	private String status;

	@Index(name = "I_ALERT_TARGETTYPE")
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	@Index(name = "I_ALERT_TARGETID")
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@Index(name = "I_ALERT_LEVEL")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMsgDetail() {
		return msgDetail;
	}

	public void setMsgDetail(String msgDetail) {
		this.msgDetail = msgDetail;
	}

	@Index(name = "I_ALERT_STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Index(name = "I_ALERT_KEY")
	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

}
