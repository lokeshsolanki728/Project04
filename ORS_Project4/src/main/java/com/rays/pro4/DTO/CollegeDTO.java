package com.rays.pro4.DTO;

/**
 * College DTO class.
 *
 * @author Lokesh Solanki
 * @version 1.0
 * @Copyright (c) SunilOS
 */
public class CollegeDTO extends BaseDTO {

	/**
	 * College Name
	 */
	private String name;

	/**
	 * College Address
	 */
	private String address;
	
	/**
	 * College State
	 */
	private String state;
	
	/**
	 * College City
	 */
	private String city;
	
	/**
	 * College Phone Number
	 */
	private String phoneNo;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Gets the phone no.
	 *
	 * @return the phone no
	 */
	public String getPhoneNo() {
		return phoneNo;
	}
	
	/**
	 * Sets the phone no.
	 *
	 * @param phoneNo the new phone no
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * Returns the string representation of the CollegeDTO.
	 */
	@Override
	public String toString() {
		return "CollegeDTO [name=" + name + ", address=" + address + ", state=" + state + ", city=" + city
				+ ", phoneNo=" + phoneNo + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy="
				+ modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime
				+ "]";
	}
}