package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUserPermission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user_permission")
public class SysUserPermission implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private Integer permissionId;

	// Constructors

	/** default constructor */
	public SysUserPermission() {
	}

	/** full constructor */
	public SysUserPermission(Integer userId, Integer permissionId) {
		this.userId = userId;
		this.permissionId = permissionId;
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

	@Column(name = "userId", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "permissionId", nullable = false)
	public Integer getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

}
