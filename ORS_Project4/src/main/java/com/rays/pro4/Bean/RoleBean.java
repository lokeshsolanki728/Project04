package com.rays.pro4.Bean;

import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;

/**
 * Role JavaBean encapsulates Role attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
import javax.servlet.http.HttpServletRequest;public class RoleBean extends BaseBean{

	/**
	 * ADMIN role constant value = 1
	 */
	public static final  int ADMIN = 1; //admin
	
	/**
	 * STUDENT role constant value = 2
	 */
	public static final  int STUDENT = 2; //student
	
	/**
	 * COLLEGE role constant value = 3
	 */
	public static final  int COLLEGE = 3; //college
	
	/**
	 * FACULTY role constant value = 4
	 */
	public static final  int FACULTY = 4; //faculty
	
	/**
	 * KIOSK role constant value = 5
	 */
	public static final  int KIOSK = 5; //kiosk

	/**
	 *  role name
	 */
	private  String name;

	/**
	 * Description of the role
	 */
	private String description;

	/**
	 * Gets the name of the role.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the role.
	 * 
	 * @param name the name of the role
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets the description of the role.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the description of the role.
	 * 
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Returns the key (ID) of the role as a String.
	 * 
	 * @return id
	 */
	@Override
	public String getkey() {
		return String.valueOf(getId());
	 */
	@Override
	public String getValue() {
		return name;
	}
	
	
    /**
     * Populate bean object from request parameters.
     * @param request the request
     */
	
    @Override
    public void populate(HttpServletRequest request) {
        setId(DataUtility.getLong(request.getParameter("id")));
        setName(DataUtility.getString(request.getParameter("name")));
        setDescription(DataUtility.getString(request.getParameter("description")));
         setCreatedBy(DataUtility.getString(request.getParameter("createdBy")));
        setModifiedBy(DataUtility.getString(request.getParameter("modifiedBy")));
        setCreatedDatetime(DataUtility.getTimestamp(request.getParameter("createdDatetime")));
        setModifiedDatetime(DataUtility.getTimestamp(request.getParameter("modifiedDatetime")));
    }
	@Override
	/**
	 * Returns a string representation of the RoleBean.
	 * 
	 * @return string representation
	 */
	
	public String toString() {
		return "RoleBean [name=" + name + ", description=" + description + ", id=" + id
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime="
				+ createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
}

