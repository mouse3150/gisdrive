package cn.com.esrichina.gcloud.commons.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "GCLOUD_EMAIL_HISTORY")
public class EmailHistory extends BaseEntity {
	private static final long serialVersionUID = -7521366130491316413L;
	private String title;
	private String content;

	private String to;
	private Boolean success;
	private String errorMsg;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "toEmail")
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
