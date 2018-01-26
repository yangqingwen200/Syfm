package com.commons.bean.system;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysAboutus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_aboutus")
public class SysAboutus implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private Integer id;
	private String content;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public SysAboutus() {
	}

	/** full constructor */
	public SysAboutus(String content, Date createTime, Date updateTime) {
		this.content = content;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "content", length = 65535)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "update_time", length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
