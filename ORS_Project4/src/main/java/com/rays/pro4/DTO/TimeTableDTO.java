package com.rays.pro4.DTO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeTableDTO extends BaseDTO {

    private long courseId;
    private String courseName;
    private long subjectId;
    private String subjectName;
    private String semester;
    private Date examDate;
    private String examTime;
    private String description;
    private Map<String, String> errorMessages = new HashMap<>();

    

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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    @Override
    public String toString() {
        return "TimeTableDTO{" +
                "courseId=" + courseId +
                ", subjectId=" + subjectId +
                ", semester='" + semester + '\'' +
                ", examDate=" + examDate +
                ", examTime='" + examTime + '\'' +
                ", description='" + description + '\'' +
                ", errorMessages=" + errorMessages +
                '}';
    }
}