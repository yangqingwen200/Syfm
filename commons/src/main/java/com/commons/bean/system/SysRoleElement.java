package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysRoleElement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_role_element")
public class SysRoleElement implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer roleId;
	private Integer elementId;

	// Constructors

	/** default constructor */
	public SysRoleElement() {
	}

	/** full constructor */
	public SysRoleElement(Integer roleId, Integer elementId) {
		this.roleId = roleId;
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

	@Column(name = "role_id", nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "element_id", nullable = false)
	public Integer getElementId() {
		return this.elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elementId == null) ? 0 : elementId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SysRoleElement other = (SysRoleElement) obj;
		if (elementId == null) {
			if (other.elementId != null)
				return false;
		} else if (!elementId.equals(other.elementId))
			return false;
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
		return true;
	}

}
