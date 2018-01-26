package com.commons.bean.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_log_operation")
public class SysLogOperation implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer oparetorId;
	private String oparetorName;
	private Date oparatorTime;
	private String ipAddress;
	private String accessClass;
	private String accessMethod;
	private String param;
	private String descprition;

	// Constructors

	/** default constructor */
	public SysLogOperation() {
	}

	/** full constructor */
	public SysLogOperation(Integer oparetorId, String oparetorName, Date oparatorTime, String ipAddress, String accessClass, String accessMethod, String param, String descprition) {
		this.oparetorId = oparetorId;
		this.oparetorName = oparetorName;
		this.oparatorTime = oparatorTime;
		this.ipAddress = ipAddress;
		this.accessClass = accessClass;
		this.accessMethod = accessMethod;
		this.param = param;
		this.descprition = descprition;
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

	@Column(name = "oparetor_id", nullable = false)
	public Integer getOparetorId() {
		return this.oparetorId;
	}

	public void setOparetorId(Integer oparetorId) {
		this.oparetorId = oparetorId;
	}

	@Column(name = "oparetor_name", nullable = false, length = 50)
	public String getOparetorName() {
		return this.oparetorName;
	}

	public void setOparetorName(String oparetorName) {
		this.oparetorName = oparetorName;
	}

	@Column(name = "oparator_time", nullable = false, length = 19)
	public Date getOparatorTime() {
		return this.oparatorTime;
	}

	public void setOparatorTime(Date oparatorTime) {
		this.oparatorTime = oparatorTime;
	}

	@Column(name = "ip_address", nullable = false, length = 20)
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "access_class", nullable = false, length = 100)
	public String getAccessClass() {
		return this.accessClass;
	}

	public void setAccessClass(String accessClass) {
		this.accessClass = accessClass;
	}

	@Column(name = "access_method", nullable = false, length = 100)
	public String getAccessMethod() {
		return this.accessMethod;
	}

	public void setAccessMethod(String accessMethod) {
		this.accessMethod = accessMethod;
	}

	@Column(name = "param", nullable = false, length = 65535)
	public String getParam() {
		return this.param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getDescprition() {
		return descprition;
	}

	@Column(name = "descprition", nullable = false, length = 200)
	public void setDescprition(String descprition) {
		this.descprition = descprition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessClass == null) ? 0 : accessClass.hashCode());
		result = prime * result
				+ ((accessMethod == null) ? 0 : accessMethod.hashCode());
		result = prime * result
				+ ((descprition == null) ? 0 : descprition.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result
				+ ((oparatorTime == null) ? 0 : oparatorTime.hashCode());
		result = prime * result
				+ ((oparetorId == null) ? 0 : oparetorId.hashCode());
		result = prime * result
				+ ((oparetorName == null) ? 0 : oparetorName.hashCode());
		result = prime * result + ((param == null) ? 0 : param.hashCode());
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
		SysLogOperation other = (SysLogOperation) obj;
		if (accessClass == null) {
			if (other.accessClass != null)
				return false;
		} else if (!accessClass.equals(other.accessClass))
			return false;
		if (accessMethod == null) {
			if (other.accessMethod != null)
				return false;
		} else if (!accessMethod.equals(other.accessMethod))
			return false;
		if (descprition == null) {
			if (other.descprition != null)
				return false;
		} else if (!descprition.equals(other.descprition))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (oparatorTime == null) {
			if (other.oparatorTime != null)
				return false;
		} else if (!oparatorTime.equals(other.oparatorTime))
			return false;
		if (oparetorId == null) {
			if (other.oparetorId != null)
				return false;
		} else if (!oparetorId.equals(other.oparetorId))
			return false;
		if (oparetorName == null) {
			if (other.oparetorName != null)
				return false;
		} else if (!oparetorName.equals(other.oparetorName))
			return false;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		return true;
	}

}
