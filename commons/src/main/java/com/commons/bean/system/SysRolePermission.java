package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysRolePermission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_role_permission")
public class SysRolePermission implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer roleId;
	private Integer permissionId;

	// Constructors

	/** default constructor */
	public SysRolePermission() {
	}

	/** full constructor */
	public SysRolePermission(Integer roleId, Integer permissionId) {
		this.roleId = roleId;
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

	@Column(name = "role_id", nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "permission_id", nullable = false)
	public Integer getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((permissionId == null) ? 0 : permissionId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
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
		SysRolePermission other = (SysRolePermission) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (permissionId == null) {
			if (other.permissionId != null)
				return false;
		} else if (!permissionId.equals(other.permissionId))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		return true;
	}

}
