package com.rays.pro4.DTO;

public class SubjectDTO extends BaseDTO {

    private String subjectName;
    private String description;
    private long courseId;

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
    @Override
	public String toString() {
		return "SubjectDTO [subjectName=" + subjectName + ", description=" + description + ", courseId=" + courseId
				+ ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime="
				+ createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";
	}
}