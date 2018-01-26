package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUserRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user_role")
public class SysUserRole implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private Integer id;
	private Integer userId;
	private Integer roleId;

	// Constructors

	/** default constructor */
	public SysUserRole() {
	}

	/** full constructor */
	public SysUserRole(Integer userId, Integer roleId) {
		this.userId = userId;
		this.roleId = roleId;
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

	@Column(name = "role_id", nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysUserRole other = (SysUserRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
