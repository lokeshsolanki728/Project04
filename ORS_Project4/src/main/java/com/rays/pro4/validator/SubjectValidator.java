package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Subject Validator class to validate Subject data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class SubjectValidator {

    /**
     * Validates the request attributes for Subject data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("subjectName"))) {
            request.setAttribute("subjectName", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("subjectName"))) {
            request.setAttribute("subjectName", PropertyReader.getValue("error.name", "Subject name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.name", "Description"));
            pass = false;
        }
        
        return pass;
    }
}