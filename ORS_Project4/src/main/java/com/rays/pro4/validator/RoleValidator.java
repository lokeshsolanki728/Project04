package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Role Validator class to validate Role data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class RoleValidator {

    /**
     * Validates the request attributes for Role data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.name", "Name"));
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