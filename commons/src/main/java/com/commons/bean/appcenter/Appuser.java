package com.commons.bean.appcenter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Appuser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "appuser")
public class Appuser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String pw;
	private String telephone;
	private Integer status;
	private String sessionId;
	private String logourl;

	// Constructors

	/** default constructor */
	public Appuser() {
	}

	/** minimal constructor */
	public Appuser(String name, String pw, String telephone, Integer status) {
		this.name = name;
		this.pw = pw;
		this.telephone = telephone;
		this.status = status;
	}

	/** full constructor */
	public Appuser(String name, String pw, String telephone, Integer status, String sessionId, String logourl) {
		this.name = name;
		this.pw = pw;
		this.telephone = telephone;
		this.status = status;
		this.sessionId = sessionId;
		this.logourl = logourl;
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
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "pw", nullable = false, length = 32)
	public String getPw() {
		return this.pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	@Column(name = "telephone", nullable = false, length = 11)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "sessionId", length = 32)
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "logourl", length = 200)
	public String getLogourl() {
		return this.logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

}
