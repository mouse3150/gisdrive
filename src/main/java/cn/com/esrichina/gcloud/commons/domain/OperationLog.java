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
@Table(name = "GCLOUD_OPERATION_LOG")
public class OperationLog extends BaseEntity {

	private static final long serialVersionUID = 4826083787392620827L;

	/**
	 * 操作人ID
	 */
	private String userId;

	/**
	 * 操作人用户名
	 */
	private String username;

	/**
	 * 操作人真实姓名
	 */
	private String userRealName;

	/**
	 * 操作人所属租户ID
	 */
	private String accountId;

	/**
	 * 操作人所属租户登录名
	 */
	private String accountShortName;

	/**
	 * 操作人所属租户名
	 */
	private String accountName;

	/**
	 * 操作是否成功
	 */
	private Boolean success;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 模块名
	 */
	private String modelName;

	/**
	 * 具体操作
	 */
	private String operation;

	/**
	 * 操作请求地址
	 */
	private String requestPath;

	/**
	 * 操作请求数据
	 */
	private String requestData;

	/**
	 * 操作返回数据
	 */
	private String responseData;

	@Index(name = "LOG_OP_SUCCESS")
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	@Column(length = 2000)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Index(name = "LOG_OP_MODELNAME")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Index(name = "LOG_OP_OPERATION")
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Lob
	@Basic(fetch = FetchType.EAGER)
	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	@Lob
	@Basic(fetch = FetchType.EAGER)
	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	@Index(name = "LOG_OP_USERID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	@Index(name = "LOG_OP_ACCOUNTID")
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountShortName() {
		return accountShortName;
	}

	public void setAccountShortName(String accountShortName) {
		this.accountShortName = accountShortName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	@Override
	public String toString() {
		return "OperationLog [userId=" + userId + ", username=" + username + ", userRealName=" + userRealName + ", accountId=" + accountId + ", accountShortName=" + accountShortName
				+ ", accountName=" + accountName + ", success=" + success + ", message=" + message + ", modelName=" + modelName + ", operation=" + operation + ", requestPath=" + requestPath
				+ ", requestData=" + requestData + ", responseData=" + responseData + "]";
	}

}
