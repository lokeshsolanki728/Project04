package com.rays.pro4.Bean;

import com.rays.pro4.DTO.BaseDTO;

import java.io.Serializable;
import com.rays.pro4.DTO.RoleDTO;
import com.rays.pro4.Util.DataValidator;

/**
 * Role JavaBean encapsulates Role attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class RoleBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;



	/**
	 *  role name
	 */
	private  String name;

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
		return "RoleBean [name=" + name + ", id=" + id
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime="
				+ createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
}

