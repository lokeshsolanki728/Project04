package com.rays.pro4.DTO;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

public abstract class BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	protected long id;
	protected String createdBy;
	protected String modifiedBy;
	protected Timestamp createdDatetime;
	protected Timestamp modifiedDatetime;

	private static Logger log = Logger.getLogger(BaseDTO.class);

	public long getId() {
		log.debug("BaseDTO getID method started");
		return id;
	}

	public void setId(long id) {
		log.debug("BaseDTO setId method started");
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
        return "BaseDTO [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
    }
}