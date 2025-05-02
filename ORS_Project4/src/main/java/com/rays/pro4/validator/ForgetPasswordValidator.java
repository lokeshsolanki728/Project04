package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * ForgetPassword Validator class to validate ForgetPassword data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class ForgetPasswordValidator {

    /**
     * Validates the request attributes for ForgetPassword data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Email Id"));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Email "));
            pass = false;
        }

        return pass;
    }
}