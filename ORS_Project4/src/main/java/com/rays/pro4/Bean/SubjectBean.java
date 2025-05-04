package com.rays.pro4.Bean;


import java.util.Date;
import com.rays.pro4.DTO.SubjectDTO;


/**
 * Subject JavaBean encapsulates Subject attributes.
 *
 * @author Lokesh SOlanki
 */
public class SubjectBean extends BaseBean {

	
    private String subjectName;
    private String description;
    private long courseId;
    private String courseName;

    /**
     * get the Subject name
     *
     * @return subjectName
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * set the Subject Name
     *
     * @param subjectName
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * get the Subject Description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the subject Description
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the course id
     *
     * @return courseId
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * set the Course Id
     *
     * @param courseId
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * get the Course name
     *
     * @return courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * set the Course name
     *
     * @param courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * return the id of the bean
     *
     * @return String
     */
    @Override
    public String getkey() {
        return String.valueOf(getId());
    }

    /**
     * return the name of the Subject
     *
     * @return String
     */
    @Override
    public String getValue() {
        return subjectName;
    }

    /**
     * return all the attributes
     *
     * @return String of attributes
     */
    @Override
    public String toString() {
        return "SubjectBean [subjectName=" + subjectName + ", description=" + description + ", courseId=" + courseId
                + ", courseName=" + courseName + ", id=" + id + ", createdBy=" + createdBy + ", modifiedBy="
                + modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime=" + modifiedDatetime + "]";   
	}
     @Override
	public com.rays.pro4.DTO.BaseDTO getDTO() {
		SubjectDTO dto = new SubjectDTO();
		dto.setId(id);
		dto.setSubjectName(subjectName);
		dto.setCourseId(courseId);
		dto.setCourseName(courseName);
		dto.setDescription(description);
		dto.setCreatedBy(createdBy);
		dto.setModifiedBy(modifiedBy);
		dto.setCreatedDatetime(createdDatetime);
		dto.setModifiedDatetime(modifiedDatetime);
		return dto;
    }
}
