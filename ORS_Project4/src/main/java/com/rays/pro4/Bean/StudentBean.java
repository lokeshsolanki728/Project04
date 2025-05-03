package com.rays.pro4.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.rays.pro4.Util.DataUtility;

/**
 * Student JavaBean encapsulates Student attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class StudentBean extends BaseBean {

	private String firstName; 
	private static Logger log = Logger.getLogger(StudentBean.class);

	private String lastName; 
	private Date dob; 
	private String mobileNo; 
	private String email; 
	private long collegeId; 
	private String collegeName; 

	/**
	 * Gets the first name of the student.
	 * 
	 * @return The first name of the student.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the student.
	 * 
	 * @param firstName The first name to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name of the student.
	 * 
	 * @return The last name of the student.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the student.
	 * 
	 * @param lastName The last name to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the date of birth of the student.
	 * 
	 * @return The date of birth.
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * Sets the date of birth of the student.
	 * 
	 * @param dob The date of birth to set.
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * Gets the mobile number of the student.
	 * 
	 * @return The mobile number.
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * Sets the mobile number of the student.
	 * 
	 * @param mobileNo The mobile number to set.
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * Gets the email ID of the student.
	 * 
	 * @return The email ID.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email ID of the student.
	 * 
	 * @param email The email ID to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the college ID associated with the student.
	 * 
	 * @return The college ID.
	 */
	public long getCollegeId() {
		return collegeId;
	}

	/**
	 * Sets the college ID associated with the student.
	 * 
	 * @param collegeId The college ID to set.
	 */
	public void setCollegeId(long collegeId) {
		this.collegeId = collegeId;
	}

	/**
	 * Gets the name of the college associated with the student.
	 * 
	 * @return The college name.
	 */
	public String getCollegeName() {
		return collegeName;
	}

	/**
	 * Sets the name of the college associated with the student.
	 * 
	 * @param collegeName The college name to set.
	 */
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	/**
	 * Returns the key (ID) of the student as a String.
	 * 
	 * @return The key (ID) of the student as a String.
	 */
	@Override
	public String getkey() {
		return String.valueOf(getKey());
	}

	/**
	 * Returns the value (full name) of the student.
	 * 
	 * @return The value (full name) of the student.
	 */
	public String getValue() {
		return firstName + " " + lastName;
	}

	/**
	 * Returns a string representation of the StudentBean.
	 * 
	 * @return A string representation of the StudentBean.
	 */
	@Override
	public String toString() {
		return "StudentBean [firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", mobileNo="
				+ mobileNo + ", email=" + email + ", collegeId=" + collegeId + ", collegeName=" + collegeName + ", id="
				+ id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime="
				+ createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
	
	@Override
	public void populate(HttpServletRequest request) {
	    log.debug("StudentBean populate method start");

	    setId(DataUtility.getLong(request.getParameter("id")));
	    setFirstName(DataUtility.getString(request.getParameter("firstname")));
	    setLastName(DataUtility.getString(request.getParameter("lastname")));
	    setDob(DataUtility.getDate(request.getParameter("dob")));

	    setMobileNo(DataUtility.getString(request.getParameter("mobile")));
	    try{
	        setEmail(DataUtility.getString(request.getParameter("email")));
	        setCollegeId(DataUtility.getLong(request.getParameter("collegename")));
	        setCreatedBy(DataUtility.getString(request.getParameter("createdby")));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	log.debug("StudentBean populate method end");
}
