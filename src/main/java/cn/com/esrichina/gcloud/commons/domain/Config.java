package cn.com.esrichina.gcloud.commons.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "GCLOUD_CONFIG", uniqueConstraints = { @UniqueConstraint(columnNames = { "CONFIG_KEY" }) })
public class Config extends BaseEntity {

	private static final long serialVersionUID = 8626526403156784541L;

	private String key;

	private String value;

	private String name;

	private String desc;

	public Config() {
		super();
	}

	public Config(String key, String value, String name, String desc) {
		super();
		this.key = key;
		this.value = value;
		this.name = name;
		this.desc = desc;
	}

	@Column(name = "CONFIG_KEY")
	@Index(name = "I_CONFIG_KEY")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "CONFIG_VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "CONFIG_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CONFIG_DESC")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
