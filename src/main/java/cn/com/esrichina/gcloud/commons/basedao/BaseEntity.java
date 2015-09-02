package cn.com.esrichina.gcloud.commons.basedao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@MappedSuperclass
public class BaseEntity implements Serializable {
	/**
	 * 数据表实体主键
	 */
	private String id;

	/**
	 * 记录创建时间
	 */
	private Date createDate;

	/**
	 * 记录更新时间
	 */
	private Date modifyDate;

	public BaseEntity() {

	}

	public BaseEntity(String id) {
		this.id = id;
	}

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "create_time")
	@Index(name = "INDEX_CRATEDATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "modify_time")
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
