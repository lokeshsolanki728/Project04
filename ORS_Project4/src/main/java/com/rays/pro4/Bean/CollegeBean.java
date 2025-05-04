package com.rays.pro4.Bean;

import java.io.Serializable;

import com.rays.pro4.DTO.CollegeDTO;
/**
 * College JavaBean encapsulates College attributes.
 * @author Lokesh SOlanki
 *
 * */
public class CollegeBean extends BaseBean implements Serializable{

    public static final long serialVersionUID = 1L;
    private String name;
    private String address;
	private String state;
	private String city;
	private String phoneNo;
	
	
	
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
	public String getkey() {
		return id + "";
	}
	@Override
	public String getValue() {
		return name;
	}
    @Override
	public com.rays.pro4.DTO.BaseDTO getDTO() {
		CollegeDTO collegeDTO = new CollegeDTO();
		collegeDTO.setName(name);
		collegeDTO.setAddress(address);
		collegeDTO.setCity(city);
		collegeDTO.setState(state);
		collegeDTO.setPhoneNo(phoneNo);
		collegeDTO.setId(id);
		collegeDTO.setCreatedBy(createdBy);
		collegeDTO.setModifiedBy(modifiedBy);
		return collegeDTO;
	}
	@Override
	public String toString() {
		return "CollegeBean [name=" + name + ", address=" + address + ", state=" + state + ", city=" + city
				+ ", phoneNo=" + phoneNo + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}

}
