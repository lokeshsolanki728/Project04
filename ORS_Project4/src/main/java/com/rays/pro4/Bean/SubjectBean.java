package com.rays.pro4.Bean;

public class SubjectBean extends BaseBean{

	
	
	private String subjectName;
	private String description;
	private long courseId;
	private String courseName;
	
	
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	@Override
	public String getkey() {
		return String.valueOf(id);
	}
	public String getValue() {
		return subjectName;
	}

	@Override
	public String toString() {
		return "SubjectBean [subjectName=" + subjectName + ", description=" + description + ", courseId=" + courseId
				+ ", courseName=" + courseName + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy="
				+ modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}



}
