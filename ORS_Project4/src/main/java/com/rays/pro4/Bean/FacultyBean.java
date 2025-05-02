package com.rays.pro4.Bean;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import com.rays.pro4.Util.DataUtility;
/**
 * Faculty JavaBean encapsulates Faculty attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class FacultyBean extends BaseBean{

	private String firstName;
	private String lastName;// last name
	private String gender;//gender
	private String emailId;//email
	private String mobileNo;//phone
	private long collegeId;//id of the college
	private String collegeName;//name of the college
	private long courseId;//id of the course
	private String courseName;//name of the course
	private Date dob;//date of birth
	private long subjectId;//id of the subject
	private String subjectName;// name of the subject
	
	/**
	 * Gets the first name of the faculty member.
	 *
	 * @return The first name of the faculty member.
	 */
	
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the first name of the faculty member.
	 *
	 * @param firstName The first name to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * Gets the last name of the faculty member.
	 *
	 * @return The last name of the faculty member.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets the last name of the faculty member.
	 *
	 * @param lastName The last name to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Gets the gender of the faculty member.
	 *
	 * @return The gender of the faculty member.
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * Sets the gender of the faculty member.
	 *
	 * @param gender The gender to set.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * Gets the email ID of the faculty member.
	 *
	 * @return The email ID of the faculty member.
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * Sets the email ID of the faculty member.
	 *
	 * @param emailId The email ID to set.
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * Gets the mobile number of the faculty member.
	 *
	 * @return The mobile number of the faculty member.
	 */
	public String getMobileNo() {
		return mobileNo;
	}
	/**
	 * Sets the mobile number of the faculty member.
	 *
	 * @param mobileNo The mobile number to set.
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	/**
	 * Gets the college ID associated with the faculty member.
	 *
	 * @return The college ID.
	 */
	public long getCollegeId() {
		return collegeId;
	}
	/**
	 * Sets the college ID associated with the faculty member.
	 *
	 * @param collegeId The college ID to set.
	 */
	public void setCollegeId(long collegeId) {
		this.collegeId = collegeId;
	}
	/**
	 * Gets the name of the college associated with the faculty member.
	 *
	 * @return The college name.
	 */
	public String getCollegeName() {
		return collegeName;
	}
	/**
	 * Sets the name of the college associated with the faculty member.
	 *
	 * @param collegeName The college name to set.
	 */
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	/**
	 * Gets the course ID associated with the faculty member.
	 *
	 * @return The course ID.
	 */
	public long getCourseId() {
		return courseId;
	}
	/**
	 * Sets the course ID associated with the faculty member.
	 *
	 * @param courseId The course ID to set.
	 */
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	/**
	 * Gets the name of the course associated with the faculty member.
	 *
	 * @return The course name.
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * Sets the name of the course associated with the faculty member.
	 *
	 * @param courseName The course name to set.
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * Gets the date of birth of the faculty member.
	 *
	 * @return The date of birth.
	 */
	public Date getDob() {
		return dob;
	}
	/**
	 * Sets the date of birth of the faculty member.
	 *
	 * @param dob The date of birth to set.
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}
	/**
	 * Gets the subject ID associated with the faculty member.
	 *
	 * @return The subject ID.
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * Sets the subject ID associated with the faculty member.
	 *
	 * @param subjectId The subject ID to set.
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * Gets the name of the subject associated with the faculty member.
	 *
	 * @return The subject name.
	 */
	public String getSubjectName() {
		return subjectName;
	}
	/**
	 * Sets the name of the subject associated with the faculty member.
	 *
	 * @param subjectName The subject name to set.
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	/**
	 * Returns the key (ID) of the faculty member as a String.
	 *
	 * @return The key (ID) of the faculty member as a String.
	 */
	@Override
	public String getkey() {
		
		return id+"";
	}
	/**
	 * Returns the value (full name) of the faculty member.
	 *
	 * @return The value (full name) of the faculty member.
	 */
	public String getValue() {
		return firstName + " " + lastName;
	}
	/**
	 * Returns a string representation of the FacultyBean.
	 *
	 * @return A string representation of the FacultyBean.
	 */
	@Override
	public String toString() {
		return "FacultyBean [firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", emailId="
				+ emailId + ", mobileNo=" + mobileNo + ", collegeId=" + collegeId + ", collegeName=" + collegeName
				+ ", courseId=" + courseId + ", courseName=" + courseName + ", dob=" + dob + ", subjectId=" + subjectId
				+ ", subjectName=" + subjectName + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy="
				+ modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";	
	}
	@Override
	public void populate(HttpServletRequest request) {
		// TODO Auto-generated method stub
		setId(DataUtility.getLong(request.getParameter("id")));
        setFirstName(DataUtility.getString(request.getParameter("firstName")));
        setLastName(DataUtility.getString(request.getParameter("lastName")));
        setGender(DataUtility.getString(request.getParameter("gender")));
        setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        setEmailId(DataUtility.getString(request.getParameter("emailId")));
        setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
        setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
        try {
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        	setDob(format.parse(request.getParameter("dob")));
        }catch (Exception e) {}
	}
}
