package com.rays.pro4.Bean;




/**
 * Role JavaBean encapsulates Role attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class RoleBean extends BaseBean{

	/**
	 * ADMIN role constant
	 */
	public static final  int ADMIN = 1;
	
	/**
	 * STUDENT role constant
	 */
	public static final  int STUDENT = 2;
	
	/**
	 * COLLEGE role constant
	 */
	public static final  int COLLEGE = 3;
	
	/**
	 * FACULTY role constant
	 */
	public static final  int FACULTY = 4;
	
	/**
	 * KIOSK role constant
	 */
	public static final  int KIOSK = 5;

	/**
	 * Name of the role
	 */
	private String name;

	/**
	 * Description of the role
	 */
	private String description;

	/**
	 * Gets the name of the role.
	 * 
	 * @return The name of the role.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the role.
	 * 
	 * @param name The name of the role.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets the description of the role.
	 * 
	 * @return The description of the role.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the description of the role.
	 * 
	 * @param description The description of the role.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Returns the key (ID) of the role as a String.
	 * 
	 * @return The key (ID) of the role as a String.
	 */
	@Override
	public String getkey() {
		return String.valueOf(id);
	}
	/**
	 * Returns the value (name) of the role.
	 * 
	 * @return The value (name) of the role.
	 */
	@Override
	public String getValue() {
		return name;
	}
	/**
	 * Returns a string representation of the RoleBean.
	 * 
	 * @return A string representation of the RoleBean.
	 */
	@Override
	public String toString() {
		return "RoleBean [name=" + name + ", description=" + description + ", id=" + id
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime="
				+ createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
}

