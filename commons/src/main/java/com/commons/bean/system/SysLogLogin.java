package com.commons.bean.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysLoginLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_log_login")
public class SysLogLogin implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer userId;
	private Date loginTime;
	private String ipAddress;
	private Date logoutTime;
	private Long onlinetime;
	private String loginName;
	private String sessionId;
	private String logoutType;

	// Constructors

	@Column(name = "logout_type", length = 20)
	public String getLogoutType() {
		return logoutType;
	}

	public void setLogoutType(String logoutType) {
		this.logoutType = logoutType;
	}

	@Column(name = "session_id", nullable = false, length = 50)
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/** default constructor */
	public SysLogLogin() {
	}

	/** minimal constructor */
	public SysLogLogin(Integer userId, Date loginTime, String ipAddress,
			String loginName) {
		this.userId = userId;
		this.loginTime = loginTime;
		this.ipAddress = ipAddress;
		this.loginName = loginName;
	}

	/** full constructor */
	public SysLogLogin(Integer userId, Date loginTime, String ipAddress,
			Date logoutTime, Long onlinetime, String loginName, String logoutType, String sessionId) {
		this.userId = userId;
		this.loginTime = loginTime;
		this.ipAddress = ipAddress;
		this.logoutTime = logoutTime;
		this.onlinetime = onlinetime;
		this.loginName = loginName;
		this.logoutType = logoutType;
		this.sessionId = sessionId;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "login_time", nullable = false, length = 19)
	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "ip_address", nullable = false, length = 20)
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "logout_time", length = 19)
	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	@Column(name = "onlinetime", precision = 10, scale = 0)
	public Long getOnlinetime() {
		return this.onlinetime;
	}

	public void setOnlinetime(Long onlinetime) {
		this.onlinetime = onlinetime;
	}

	@Column(name = "login_name", nullable = false, length = 20)
	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
