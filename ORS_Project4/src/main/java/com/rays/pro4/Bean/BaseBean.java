package com.rays.pro4.Bean;

import java.io.Serializable;

import java.sql.Timestamp;

/**
 * * Parent class of all Beans in application. It contains generic attributes.
 * 
 * @author Lokesh SOlanki
 *
 */

public abstract class BaseBean implements Serializable, DropdownListBean {

	protected long id;
	protected String createdBy;
	protected String modifiedBy;
	protected Timestamp createdDatetime;
	protected Timestamp modifiedDatetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Timestamp getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Timestamp modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}
	
	@Override
	public String toString() {
		return "BaseBean [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
	

}
