package com.rays.pro4.Bean;

import java.util.Date;

import com.rays.pro4.DTO.StudentDTO;


/**
 * Student JavaBean encapsulates Student attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class StudentBean extends BaseBean {

	private String firstName; 

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
		return String.valueOf(id);
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

	/**
     * Converts the StudentBean object to a StudentDTO object.
     *
     * @return A new StudentDTO object populated with data from this StudentBean.
     */
    @Override
	public com.rays.pro4.DTO.BaseDTO getDTO() {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(firstName);
		studentDTO.setLastName(lastName);
		studentDTO.setDob(dob);
		studentDTO.setMobileNo(mobileNo);
		studentDTO.setEmail(email);
		studentDTO.setCollegeId(collegeId);
        studentDTO.setId(id);
        studentDTO.setCreatedBy(createdBy);
        studentDTO.setModifiedBy(modifiedBy);
        return studentDTO;
    }
}
    @Override
    public StudentDTO getDTO() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(id);
        studentDTO.setFirstName(firstName);
        studentDTO.setLastName(lastName);
        studentDTO.setDob(dob);
        studentDTO.setMobileNo(mobileNo);
        studentDTO.setEmail(email);
        return studentDTO;
    }
}
