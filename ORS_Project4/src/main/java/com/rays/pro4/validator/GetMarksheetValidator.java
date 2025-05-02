package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * GetMarksheet Validator class to validate GetMarksheet data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class GetMarksheetValidator {

    /**
     * Validates the request attributes for GetMarksheet data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("rollNo"))) {
            request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll No"));
            pass = false;
        }

        return pass;
    }
}