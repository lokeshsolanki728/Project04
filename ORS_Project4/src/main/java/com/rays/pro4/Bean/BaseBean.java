package com.rays.pro4.Bean;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

/**
 * Parent class of all Beans in application. It contains generic attributes.
 *
 * @author Lokesh SOlanki
 */
public abstract class BaseBean implements Serializable, DropdownListBean {

    /**
     * Log to log the error
     */
    private static Logger log = Logger.getLogger(BaseBean.class);
    protected long id;
    protected String createdBy;
    protected String modifiedBy;
    protected Timestamp createdDatetime;
    protected Timestamp modifiedDatetime;

    /**
     * Returns primary key of Bean.
     *
     * @return key
     */
    public long getKey() {
        return id;
    }

    /**
     * Returns display name of Bean. It is used to create option list in
     * HTML dropdown list.
     *
     * @return value
     */
    public String getValue() {

        try {
            Field field = this.getClass().getDeclaredField("name");
            if (field != null) {

                try {
                    AccessibleObject.setAccessible(new Field[] { field }, true);
                    Object o = field.get(this);
                    return (String) o;
                } catch (IllegalAccessException e) {
                    log.error("BaseBean getValue IllegalAccessException ", e);
                    return "Can not get the value";
                }

            } else {
                return "Value is not present";
            }
        } catch (NoSuchFieldException e) {
            log.error("BaseBean getValue NoSuchFieldException ", e);
            return "Value is not present";
        } catch (SecurityException e) {
            log.error("BaseBean getValue SecurityException ", e);
            return "Can not access";
        } catch (IllegalArgumentException e) {
            log.error("BaseBean getValue IllegalArgumentException ", e);
            return "Can not get the value";
        }
    }
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
        return "BaseBean [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime="
                + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
    }
    
}
