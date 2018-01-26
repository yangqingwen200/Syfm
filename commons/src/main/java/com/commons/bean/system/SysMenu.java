package com.commons.bean.system;

import com.commons.annotation.DescColumn;

import javax.persistence.*;
import java.util.Date;

/**
 * SysMenu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_menu")
public class SysMenu implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String path;
	private String remark;
	private Integer disploy;
	private Integer parent;
	private Date createTime;

	// Constructors

	/** default constructor */
	public SysMenu() {
	}

	/** minimal constructor */
	public SysMenu(String name, String path, Integer disploy, Integer parent) {
		this.name = name;
		this.path = path;
		this.disploy = disploy;
		this.parent = parent;
	}

	/** full constructor */
	public SysMenu(String name, String path, String remark, Integer disploy, Integer parent) {
		this.name = name;
		this.path = path;
		this.remark = remark;
		this.disploy = disploy;
		this.parent = parent;
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

	@Column(name = "name", nullable = false, length = 20)
	@DescColumn(value = "菜单名称")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "path", nullable = false, length = 150)
	@DescColumn(value = "菜单路径")
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "remark", nullable = false, length = 50)
	@DescColumn(value = "菜单备注")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "disploy", nullable = false)
	@DescColumn(value = "是否显示")
	public Integer getDisploy() {
		return this.disploy;
	}

	public void setDisploy(Integer disploy) {
		this.disploy = disploy;
	}

	@Column(name = "parent", nullable = false)
	public Integer getParent() {
		return this.parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
