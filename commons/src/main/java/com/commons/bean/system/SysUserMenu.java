package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUserMenu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user_menu")
public class SysUserMenu implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private Integer menuId;

	// Constructors

	/** default constructor */
	public SysUserMenu() {
	}

	/** full constructor */
	public SysUserMenu(Integer userId, Integer menuId) {
		this.userId = userId;
		this.menuId = menuId;
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

	@Column(name = "menuId", nullable = false)
	public Integer getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

}
