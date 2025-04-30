package com.rays.pro4.Bean;

import java.util.Date;

/**
 * TimeTable JavaBean encapsulates TimeTable attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class TimeTableBean extends BaseBean{

	private long courseId;
	private String courseName;
	private long subjectId;
	private String subjectName;
	private String semester;
	private Date examDate;
	private String examTime;
	private String description;
	
	/**
	 * get the course id
	 * @return courseId
	 */
	public long getCourseId() {
		return courseId;
	}
	/**
	 * set the course id
	 * @param courseId
	 */
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	/**
	 * get the course name
	 * @return courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * set the course name
	 * @param courseName
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * get the subject id
	 * @return subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * set the subject id
	 * @param subjectId
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * get the subject name
	 * @return subjectName
	 */
	public String getSubjectName() {
		return subjectName;
	}
	/**
	 * set the subject name
	 * @param subjectName
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	/**
	 * get the semester
	 * @return semester
	 */
	public String getSemester() {
		return semester;
	}
	/**
	 * set the semester
	 * @param semester
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}
	/**
	 * get the exam date
	 * @return examDate
	 */
	public Date getExamDate() {
		return examDate;
	}
	/**
	 * set the exam date
	 * @param examDate
	 */
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	/**
	 * get the exam time
	 * @return examTime
	 */
	public String getExamTime() {
		return examTime;
	}
	/**
	 * set the exam time
	 * @param examTime
	 */
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}
	/**
	 * get the description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * set the description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * return the id of the bean
	 * @return String
	 */
	@Override
	public String getkey() {
		return String.valueOf(id);
	}
	/**
	 * get the subject name
	 * @return String
	 */
	public String getValue() {
		return subjectName;
	}
	/**
	 * return all the attribute of bean
	 * @return String
	 */
	@Override
	public String toString() {
		return "TimeTableBean [courseId=" + courseId + ", courseName=" + courseName + ", subjectId=" + subjectId
				+ ", subjectName=" + subjectName + ", semester=" + semester + ", examDate=" + examDate + ", examTime="
				+ examTime + ", description=" + description + ", id=" + id + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime="
				+ modifiedDatetime + "]";
	}
}
