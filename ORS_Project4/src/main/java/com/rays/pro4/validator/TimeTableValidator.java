package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * TimeTable Validator class to validate TimeTable data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class TimeTableValidator {

    /**
     * Validates the request attributes for TimeTable data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("courseName"))) {
            request.setAttribute("courseName", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("subjectName"))) {
            request.setAttribute("subjectName", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("semester"))) {
            request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("examDate"))) {
            request.setAttribute("examDate", PropertyReader.getValue("error.require", "Exam Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("examDate"))) {
            request.setAttribute("examDate", PropertyReader.getValue("error.date", "Exam Date"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("examTime"))) {
            request.setAttribute("examTime", PropertyReader.getValue("error.require", "Exam Time"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }

        return pass;
    }
}