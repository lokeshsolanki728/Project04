package com.rays.pro4.DTO;

import java.util.Date;

public class TimeTableDTO extends BaseDTO {

    private long courseId;
    private long subjectId;
    private String semester;
    private Date examDate;
    private String examTime;
    private String description;

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

    @Override
    public String toString() {
        return "TimeTableDTO{" +
                "courseId=" + courseId +
                ", subjectId=" + subjectId +
                ", semester='" + semester + '\'' +
                ", examDate=" + examDate +
                ", examTime='" + examTime + '\'' +
                ", description='" + description + '\'' + '}';
    }
}