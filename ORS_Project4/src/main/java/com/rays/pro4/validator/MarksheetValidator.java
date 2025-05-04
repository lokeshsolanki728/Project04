
package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Marksheet Validator class to validate Marksheet data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class MarksheetValidator {

    /**
     * Validates the request attributes for Marksheet.
     *
     * @param request the HttpServletRequest object
     * @return true if the request is valid, false otherwise
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("rollNo"))) {
            request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll No"));
            pass = false;
        } else if (!DataValidator.isRollNo(request.getParameter("rollNo"))) {
            request.setAttribute("rollNo", "Invalid Roll Number");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.name", "Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("physics"))) {
            request.setAttribute("physics", PropertyReader.getValue("error.require", "Physics Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("physics"))) {
            request.setAttribute("physics", PropertyReader.getValue("error.integer", "Physics Marks"));
            pass = false;
        } else if (DataUtility.getInt(request.getParameter("physics")) > 100 || DataUtility.getInt(request.getParameter("physics")) < 0) {
            request.setAttribute("physics", "Marks must be between 0 and 100");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry", PropertyReader.getValue("error.require", "Chemistry Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry", PropertyReader.getValue("error.integer", "Chemistry Marks"));
            pass = false;
        } else if (DataUtility.getInt(request.getParameter("chemistry")) > 100 || DataUtility.getInt(request.getParameter("chemistry")) < 0) {
            request.setAttribute("chemistry", "Marks must be between 0 and 100");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("maths"))) {
            request.setAttribute("maths", PropertyReader.getValue("error.require", "Maths Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("maths"))) {
            request.setAttribute("maths", PropertyReader.getValue("error.integer", "Maths Marks"));
            pass = false;
        } else if (DataUtility.getInt(request.getParameter("maths")) > 100 || DataUtility.getInt(request.getParameter("maths")) < 0) {
            request.setAttribute("maths", "Marks must be between 0 and 100");
            pass = false;
        }

        return pass;
    }
}
