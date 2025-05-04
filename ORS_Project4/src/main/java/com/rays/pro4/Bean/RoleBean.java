package com.rays.pro4.Bean;

import com.rays.pro4.DTO.BaseDTO;
import com.rays.pro4.DTO.RoleDTO;
import com.rays.pro4.Util.DataValidator;

/**
 * Role JavaBean encapsulates Role attributes.
 * 
 * @author Lokesh SOlanki
 *
 */public class RoleBean extends BaseBean{



	/**
	 * ADMIN role constant value = 1
	 */
	private static final  int ADMIN = 1; //admin

	/**
	 * STUDENT role constant value = 2
	 */
	private static final  int STUDENT = 2; //student

	/**
	 * COLLEGE role constant value = 3
	 */
	private static final  int COLLEGE = 3; //college

	/**
	 * FACULTY role constant value = 4
	 */
	private static final  int FACULTY = 4; //faculty

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
	}
	@Override
	public String getValue() {
		return name;
	}
	/**
	 * returns a new RoleDTO object
	 */
	@Override
	public RoleDTO getDTO() {
		RoleDTO dto = new RoleDTO();
		dto.setId(id);
		dto.setName(name);
		return dto;
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

