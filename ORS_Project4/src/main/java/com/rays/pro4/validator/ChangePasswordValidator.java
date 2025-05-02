package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Change Password Validator class to validate Change Password data.
 * 
 * @author Lokesh SOlanki
 * 
 * @version 1.0
 *
 */
public class ChangePasswordValidator {

    /**
     * Validates the request attributes for Change Password data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        final String oldPassword = request.getParameter("oldPassword");
        final String newPassword = request.getParameter("newPassword");
        final String confirmPassword = request.getParameter("confirmPassword");
        
        if (DataValidator.isNull(oldPassword)) {
            request.setAttribute("oldPassword", PropertyReader.getValue("error.require", "Old Password"));
            pass = false;
        } else if (!DataValidator.isPassword(oldPassword)) {
        	request.setAttribute("oldPassword", PropertyReader.getValue("error.password", "Old Password"));
            pass = false;
        }

        if (DataValidator.isNull(newPassword)) {
            request.setAttribute("newPassword", PropertyReader.getValue("error.require", "New Password"));
            pass = false;
        } else if (!DataValidator.isPassword(newPassword)) {
        	request.setAttribute("newPassword", PropertyReader.getValue("error.password", "New Password"));
            pass = false;
        }

        if (DataValidator.isNull(confirmPassword)) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
        }else if (!DataValidator.isPassword(confirmPassword)) {
        	request.setAttribute("confirmPassword", PropertyReader.getValue("error.password", "confirm Password"));
            pass = false;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.confirmPassword","Passwords"));
            pass = false;
        }

        return pass;
    }
}