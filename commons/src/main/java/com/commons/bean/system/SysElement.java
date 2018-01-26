package com.commons.bean.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * SysElement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_element")
public class SysElement implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;
	private String functionName;
	private String description;
	private Integer disabled;
	private Integer parent;
	private String icon;
	private String buttomName;
	private Integer permissionId;
	private Date createTime;

	// Constructors

	/** default constructor */
	public SysElement() {
	}

	/** full constructor */
	public SysElement(String code, String functionName, String description, Integer disabled, Integer parent, String icon, String buttomName, Integer permissionId) {
		this.code = code;
		this.functionName = functionName;
		this.description = description;
		this.disabled = disabled;
		this.parent = parent;
		this.icon = icon;
		this.buttomName = buttomName;
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

	@Column(name = "code", nullable = false, length = 100)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "function_name", nullable = false, length = 500)
	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(name = "description", nullable = false, length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "disabled", nullable = false)
	public Integer getDisabled() {
		return this.disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	@Column(name = "parent", nullable = false)
	public Integer getParent() {
		return this.parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	@Column(name = "icon", nullable = false, length = 200)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "buttom_name", nullable = false, length = 20)
	public String getButtomName() {
		return this.buttomName;
	}

	public void setButtomName(String buttomName) {
		this.buttomName = buttomName;
	}

	@Column(name = "permission_id")
	public Integer getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
