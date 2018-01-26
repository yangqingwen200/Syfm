package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUserElement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user_element")
public class SysUserElement implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private Integer elementId;

	// Constructors

	/** default constructor */
	public SysUserElement() {
	}

	/** full constructor */
	public SysUserElement(Integer userId, Integer elementId) {
		this.userId = userId;
		this.elementId = elementId;
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

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "element_id", nullable = false)
	public Integer getElementId() {
		return this.elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

}
