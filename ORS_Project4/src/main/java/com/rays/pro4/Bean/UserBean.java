package com.rays.pro4.Bean;

import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * User JavaBean encapsulates User attributes.
 *
 *
 * @author Lokesh SOlanki
 *
 */
public class UserBean extends BaseBean {

	public static final String ACTIVE ="Active"; //make it final
	public static final String INACTIVE="inactive"; //make it final
	/**
	 * Active User Constant
	 */
	public static final String ACTIVE = "Active";
	/**
	 * Inactive User Constant
	 */
	public static final String INACTIVE = "inactive";
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
	 * Confirm Password of User
	 */
	private String confirmPassword;
	/**
	 * Date of Birth of User
	 */
	private Date dob;
	private String mobileNo;  
	private long roleId;
	private int unSuccessfulLogin;
	private String gender;
	private Timestamp lastLogin;
	private String lock=INACTIVE;
	private String registerdIP;
	private String lastLoginIP;	
	/**
	 * Get first name
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
	/**
	 * get the password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * set the password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * get the confirm password
	 * @return confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	/**
	 * set the confirm password
	 * @param confirmPassword
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	/**
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
	 * get the active user
	 * @return String
	 */
	public static String getActive() {
		return ACTIVE;
	}
	/**
	 * get the inactive user
	 * @return String
	 */
	public static String getInactive() {
		return INACTIVE;
	}
	/**
	 * get the id of the bean
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
	 * return all the attributes of the bean
	 * @return String
	 */
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return "UserBean [firstName=" + firstName + ", lastName=" + lastName + ", login=" + login + ", password="
				+ password + ", confirmPassword=" + confirmPassword + ", dob=" + sdf.format(dob) + ", mobileNo=" + mobileNo
				+ ", roleId=" + roleId + ", unSuccessfulLogin=" + unSuccessfulLogin + ", gender=" + gender
				+ ", lastLogin=" + lastLogin + ", lock=" + lock + ", registerdIP=" + registerdIP + ", lastLoginIP="
				+ lastLoginIP + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}

}