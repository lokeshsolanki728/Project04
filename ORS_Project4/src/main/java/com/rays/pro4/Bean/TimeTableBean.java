package com.rays.pro4.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataUtility;
import org.apache.log4j.Logger;


/**
 * TimeTable JavaBean encapsulates TimeTable attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class TimeTableBean extends BaseBean{

    private static Logger log = Logger.getLogger(TimeTableBean.class);

    private long courseId;
    private String courseName;
    private long subjectId;
    private String subjectName;
    private String semester;
    private Date examDate;
    private String examTime;
    private String description;

    /**
     * Gets the course ID.
     *
     * @return The course ID.
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets the course ID.
     *
     * @param courseId The course ID to set.
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the course name.
     *
     * @return The course name.
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name.
     *
     * @param courseName The course name to set.
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets the subject ID.
     *
     * @return The subject ID.
     */
    public long getSubjectId() {
        return subjectId;
    }

    /**
     * Sets the subject ID.
     *
     * @param subjectId The subject ID to set.
     */
    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Gets the subject name.
     *
     * @return The subject name.
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Sets the subject name.
     *
     * @param subjectName The subject name to set.
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * Gets the semester.
     *
     * @return The semester.
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Sets the semester.
     *
     * @param semester The semester to set.
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * Gets the exam date.
     *
     * @return The exam date.
     */
    public Date getExamDate() {
        return examDate;
    }

    /**
     * Sets the exam date.
     *
     * @param examDate The exam date to set.
     */
    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    /**
     * Gets the exam time.
     *
     * @return The exam time.
     */
    public String getExamTime() {
        return examTime;
    }

    /**
     * Sets the exam time.
     *
     * @param examTime The exam time to set.
     */
    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    /**
     * Gets the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description The description to set.
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
     *
     * @return String
     */
    @Override

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

	   /**
     *  Populate bean object from request parameters
     * @param request the request
     */
    @Override
    public void populate(HttpServletRequest request) {
        setId(DataUtility.getLong(request.getParameter("id")));
        setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        setCourseName(DataUtility.getString(request.getParameter("courseName")));
        setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
        setSubjectName(DataUtility.getString(request.getParameter("subjectName")));
        setSemester(DataUtility.getString(request.getParameter("semester")));
        setExamTime(DataUtility.getString(request.getParameter("examTime")));
        setDescription(DataUtility.getString(request.getParameter("description")));
         try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            setExamDate(sdf.parse(request.getParameter("examDate")));
        } catch (Exception e) {
            log.error("Populate Exception", e);
        }
    }


}
