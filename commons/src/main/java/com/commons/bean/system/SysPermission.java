package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysPermission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_permission")
public class SysPermission implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String permission;
	private String remark;
	private String code;
	private Integer parent;
	private Integer iselement;

	// Constructors

	/** default constructor */
	public SysPermission() {
	}

	/** minimal constructor */
	public SysPermission(String permission, String code, Integer parent, Integer iselement) {
		this.permission = permission;
		this.code = code;
		this.parent = parent;
		this.iselement = iselement;
	}

	/** full constructor */
	public SysPermission(String permission, String remark, String code, Integer parent, Integer iselement) {
		this.permission = permission;
		this.remark = remark;
		this.code = code;
		this.parent = parent;
		this.iselement = iselement;
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

	@Column(name = "permission", nullable = false, length = 20)
	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Column(name = "remark", nullable = false, length = 50)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "code", nullable = false, length = 100)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "parent", nullable = false)
	public Integer getParent() {
		return this.parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	@Column(name = "iselement", nullable = false)
	public Integer getIselement() {
		return this.iselement;
	}

	public void setIselement(Integer iselement) {
		this.iselement = iselement;
	}

}
