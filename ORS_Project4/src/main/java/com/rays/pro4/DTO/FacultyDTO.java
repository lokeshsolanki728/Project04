package com.rays.pro4.DTO;

import java.text.SimpleDateFormat;

import java.util.Date;

public class FacultyDTO extends BaseDTO {

    private String firstName;
    private String lastName;
    private String gender;
    private String emailId;
    private String mobileNo;
    private Date dob;
    private long collegeId;
    private long courseId;
    private long subjectId;

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

    public String getGender() {
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(long collegeId) {
        this.collegeId = collegeId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return "FacultyDTO [firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", emailId="
                + emailId + ", mobileNo=" + mobileNo + ", dob=" + dob + ", collegeId=" + collegeId + ", courseId="
                + courseId + ", subjectId=" + subjectId + ", id=" + id + "]";
    }
}