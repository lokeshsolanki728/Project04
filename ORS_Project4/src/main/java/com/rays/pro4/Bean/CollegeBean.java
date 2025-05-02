package com.rays.pro4.Bean;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataUtility;

/**
 * College JavaBean encapsulates College attributes.
 * @author Lokesh SOlanki
 *
 */
public class CollegeBean extends BaseBean{


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
    public void populate(HttpServletRequest request) {
        setId(DataUtility.getLong(request.getParameter("id")));
        setName(DataUtility.getString(request.getParameter("name")));
        setAddress(DataUtility.getString(request.getParameter("address")));
        setState(DataUtility.getString(request.getParameter("state")));
        setCity(DataUtility.getString(request.getParameter("city")));
        setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));
    }	@Override
	public String toString() {
		return "CollegeBean [name=" + name + ", address=" + address + ", state=" + state + ", city=" + city
				+ ", phoneNo=" + phoneNo + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}

}
