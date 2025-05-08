
package com.rays.pro4.DTO;

import java.util.Date;
import java.util.HashMap;

public class StudentDTO extends BaseDTO {

    private String firstName;
    private String lastName;
    private Date dob;
    private String mobileNo;
    private String email;
    
    private String collegeName;
    private HashMap<String, String> errorMessages = new HashMap<String, String>();
    private long collegeId;

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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    public void setErrorMessage(String field, String message) {
        errorMessages.put(field, message);
    }

    /**
     * @return the errorMessages
     */
    public HashMap<String, String> getErrorMessages() {
        return errorMessages;
    }

    public boolean hasError() {
        return !errorMessages.isEmpty();
    }
    
    @Override
	public String toString() {
		return "StudentDTO [firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", mobileNo=" + mobileNo
				+ ", email=" + email + ", collegeName=" + collegeName + ", errorMessages=" + errorMessages + ", collegeId="
				+ collegeId + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
}