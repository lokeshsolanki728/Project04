package com.rays.pro4.Bean;

import com.rays.pro4.DTO.BaseDTO;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import org.apache.log4j.Logger;
import java.sql.Timestamp;


/**
 *  This is the base bean class for all beans in the application.
 * It provides common attributes and methods that are shared across different beans.
 * All beans must inherit from this class.
 */
/**
 *
 * Parent class of all Beans in application. It contains generic attributes. *
 * @author Lokesh SOlanki *
 */
public abstract class BaseBean implements Serializable, DropdownListBean {
    /**
     * Non Business primary key
     */
    private static final long serialVersionUID = 1L;
    protected long id;
    protected String createdBy;// created by
    protected String modifiedBy;
    protected Timestamp createdDatetime;
    protected Timestamp modifiedDatetime;
    protected static Logger log = Logger.getLogger(BaseBean.class);


     public abstract String getValue() ;
    
     public abstract String getkey() ;

    /**
     * @return the id
     */
	public long getId() {
        log.debug("BaseBean getId method start");
        log.debug("BaseBean getId method end");
		return id;
	}

    /**
     * @param id the id to set
     */
	public void setId(long id) {
        log.debug("BaseBean setId method start");
		this.id = id;
        log.debug("BaseBean setId method end");
	}

    /**
     * @return the createdBy
     */
	public String getCreatedBy() {
        log.debug("BaseBean getCreatedBy method start");
        log.debug("BaseBean getCreatedBy method end");
		return createdBy;
	}

    /**
     * @param createdBy the createdBy to set
     */
	public void setCreatedBy(String createdBy) {
        log.debug("BaseBean setCreatedBy method start");
		this.createdBy = createdBy;
        log.debug("BaseBean setCreatedBy method end");
	}

    /**
     * @return the modifiedBy
     */
	public String getModifiedBy() {
        log.debug("BaseBean getModifiedBy method start");
        log.debug("BaseBean getModifiedBy method end");
		return modifiedBy;
	}

    /**
     * @param modifiedBy the modifiedBy to set
     */
	public void setModifiedBy(String modifiedBy) {
        log.debug("BaseBean setModifiedBy method start");
		this.modifiedBy = modifiedBy;
        log.debug("BaseBean setModifiedBy method end");
	}

    /**
     * @return the createdDatetime
     */
	public Timestamp getCreatedDatetime() {
        log.debug("BaseBean getCreatedDatetime method start");
        log.debug("BaseBean getCreatedDatetime method end");
		return createdDatetime;
	}

    /**
     * @param createdDatetime the createdDatetime to set
     */
	public void setCreatedDatetime(Timestamp createdDatetime) {
        log.debug("BaseBean setCreatedDatetime method start");
		this.createdDatetime = createdDatetime;
        log.debug("BaseBean setCreatedDatetime method end");
	}

    /**
     * @return the modifiedDatetime
     */
	public Timestamp getModifiedDatetime() {
        log.debug("BaseBean getModifiedDatetime method start");
        log.debug("BaseBean getModifiedDatetime method end");
		return modifiedDatetime;
	}

    /**
     * @param modifiedDatetime the modifiedDatetime to set
     */
	public void setModifiedDatetime(Timestamp modifiedDatetime) {
        log.debug("BaseBean setModifiedDatetime method start");
		this.modifiedDatetime = modifiedDatetime;
        log.debug("BaseBean setModifiedDatetime method end");
	}


	@Override
    public String toString() {
        log.debug("BaseBean toString method start");
        return "BaseBean [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime="
                + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
    }

    public abstract BaseDTO getDTO();
    
    /**
     * populate the bean from request parameters
     * @param request
     */
    public void populate(HttpServletRequest request){
        log.debug("BaseBean populate method start");
        log.debug("BaseBean populate method end");
    }
}
