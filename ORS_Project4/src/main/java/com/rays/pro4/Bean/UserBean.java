package com.rays.pro4.Bean;

import java.sql.Timestamp;
import java.util.Date;

import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.DTO.UserDTO;

/**
 *
 *
 * @author Lokesh SOlanki
 *
 */
import javax.servlet.http.HttpServletRequest;

public class UserBean extends BaseBean {

    private long id;

	private static final String INACTIVE = "inactive";	
	/**
	 * Active User Constant
	 */

	/**
	 * Inactive User Constant
	 */

	/**
	 * First Name of User
	 */
	private String firstName;
	/**
	 * Last Name of User
	 */
	private String lastName;
	/**
	 * Login of User
	 */
	private String login;
	/**
	 * Password of User
	 */
	private String password;
	/**
	 * Date of Birth of User
	 */
	private Date dob;
	private String mobileNo;
	private long roleId;
	private int unSuccessfulLogin;
	private String gender;
	private Timestamp lastLogin;
	private String lock = INACTIVE;
	private String registerdIP;
	private String lastLoginIP;
	/**
	 * Get first name
     * get the id of the bean
	 * @return int
	 */
    public long getId() {
        return id;
    }
    
    /**
	 * set the id of the bean
	 * @param id
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * get the password
	 * @return password
	 */
    public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * set first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * Get Last name
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * set Last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * get the login
	 * @return login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * set the login
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}
    
	 * get the dob
	 * @return dob
	 */
	public Date getDob() {
		return dob;
	}
	/**
	 * set the dob
	 * @param dob
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}
	/**
	 * get the mobile number
	 * @return mobileNo
	 */
	public String getMobileNo() {
		return mobileNo;
	}
	/**
	 * set the mobile number
	 * @param mobileNo
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	/**
	 * get the role id
	 * @return roleId
	 */
	public long getRoleId() {
		return roleId;
	}
	/**
	 * set the role id
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	/**
	 * get the unsccessfull login
	 * @return unSuccessfulLogin
	 */
	public int getUnSuccessfulLogin() {
		return unSuccessfulLogin;
	}
	/**
	 * set the unsccessfull login
	 * @param unSuccessfulLogin
	 */
	public void setUnSuccessfulLogin(int unSuccessfulLogin) {
		this.unSuccessfulLogin = unSuccessfulLogin;
	}
	/**
	 * get the gender
	 * @return gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * set the gender
	 * @param gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * get the last login
	 * @return lastLogin
	 */
	public Timestamp getLastLogin() {
		return lastLogin;
	}
	/**
	 * set the last login
	 * @param lastLogin
	 */
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
	/**
	 * get the lock
	 * @return lock
	 */
	public String getLock() {
		return lock;
	}
	/**
	 * set the lock
	 * @param lock
	 */
	public void setLock(String lock) {
		this.lock = lock;
	}
	/**
	 * get the register ip
	 * @return registerdIP
	 */
	public String getRegisterdIP() {
		return registerdIP;
	}
	/**
	 * set the register ip
	 * @param registerdIP
	 */
	public void setRegisterdIP(String registerdIP) {
		this.registerdIP = registerdIP;
	}
	/**
	 * get the last login ip
	 * @return lastLoginIP
	 */
	public String getLastLoginIP() {
		return lastLoginIP;
	}
    /**
	 * set the last login ip
	 * @param lastLoginIP
	 */
	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
	/**
	 * get the id of the bean
	 *
	 * @return String
	 */


	@Override
	public String getkey() {
		return String.valueOf(id);
	}
	/**
	 * get the value of the bean
	 * @return String
	 */
	@Override
	public String getValue() {
		return firstName + " " + lastName;
	}

	/**
     * Populate bean object from request parameters.
	 *
     * @param request the request
     */
    @Override
    public void populate(HttpServletRequest request) {    	
    	super.populate(request);
    	setFirstName(DataUtility.getString(request.getParameter("firstName")));
    	setLastName(DataUtility.getString(request.getParameter("lastName")));
    	setLogin(DataUtility.getString(request.getParameter("login")));
    	setDob(DataUtility.getDate(request.getParameter("dob")));
    	setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
    	setRoleId(DataUtility.getLong(request.getParameter("roleId")));
    	setGender(DataUtility.getString(request.getParameter("gender")));
    }

    @Override
    public UserDTO getDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setLogin(login);
        userDTO.setPassword(password);
        userDTO.setDob(dob);
        userDTO.setMobileNo(mobileNo);
        userDTO.setRoleId(roleId);
        userDTO.setUnSuccessfulLogin(unSuccessfulLogin);
        userDTO.setGender(gender);
        userDTO.setLastLogin(lastLogin);
        userDTO.setLock(lock);
        userDTO.setRegisterdIP(registerdIP);
        userDTO.setLastLoginIP(lastLoginIP);
         userDTO.setCreatedBy(createdBy);
        userDTO.setModifiedBy(modifiedBy);
        userDTO.setCreatedDatetime(createdDatetime);
        userDTO.setModifiedDatetime(modifiedDatetime);
        return userDTO;
    }
	/**
	 * return all the attributes of the bean
	 * @return String
	 */
	@Override
	public String toString() {
		return "UserBean [firstName=" + firstName + ", lastName=" + lastName + ", login=" + login + ", password=" + password + ", dob="
                + dob + ", mobileNo=" + mobileNo
				+ ", roleId=" + roleId + ", unSuccessfulLogin=" 
				+ unSuccessfulLogin + ", gender=" + gender
				+ ", lastLogin=" + lastLogin + ", lock=" + lock + ", registerdIP=" + registerdIP + ", lastLoginIP="
				+ lastLoginIP + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
}