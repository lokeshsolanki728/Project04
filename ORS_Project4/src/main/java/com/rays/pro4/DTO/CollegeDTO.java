package com.rays.pro4.DTO;

/**
 * College DTO class.
 *
 * @author Lokesh Solanki
 */
public class CollegeDTO extends BaseDTO {

	/**
	 * college name
	 */
	public String name;

	/**
	 * college address
	 */
	public String address;
	
	/**
	 * college state
	 */
	public String state;
	
	/**
	 * college city
	 */
	public String city;
	
	/**
	 * college phone
	 */
	public String phoneNo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Override
	public String toString() {
		return "CollegeDTO [name=" + name + ", address=" + address + ", state=" + state + ", city=" + city
				+ ", phoneNo=" + phoneNo + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
}