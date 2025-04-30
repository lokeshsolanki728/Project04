package com.rays.pro4.Bean;

import java.util.Date;

/**
 * Faculty JavaBean encapsulates Faculty attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class FacultyBean extends BaseBean{

	private String firstName;
	private String lastName;
	private String gender;
	private String emailId;
	private String mobileNo;
	private long collegeId;
	private String collegeName;
	private long courseId;
	private String courseName;
	private Date dob;
	private long subjectId; 
	private String subjectName;
	
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getgender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public long getCollegeId() {
		return collegeId;
	}
	public void setCollegeId(long collegeId) {
		this.collegeId = collegeId;
	}
	public String getCollegeName() {
		return collegeName;
	}
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	public long getCourseId() {
		return courseId;
	}
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	@Override
	public String getkey() {
		return id+"";
	}
	public String getValue() {
		return firstName + " " + lastName;
	}
	@Override
	public String toString() {
		return "FacultyBean [firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", emailId="
				+ emailId + ", mobileNo=" + mobileNo + ", collegeId=" + collegeId + ", collegeName=" + collegeName
				+ ", courseId=" + courseId + ", courseName=" + courseName + ", dob=" + dob + ", subjectId=" + subjectId
				+ ", subjectName=" + subjectName + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy="
				+ modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
	
}
