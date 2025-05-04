package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * GetMarksheetValidator class to validate GetMarksheet data.
 * Validates the roll number field for non-null and optional format.
 * 
 * @author Lokesh
 */
public class GetMarksheetValidator {

    /**
     * Validates the request attributes for GetMarksheet data.
     *
     * @param request The HttpServletRequest object.
     * @return true if validation passes, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;

        String rollNo = request.getParameter("rollNo");

        // Check if roll number is null or blank
        if (DataValidator.isNull(rollNo)) {
            request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll No"));
            pass = false;
        } 
        // Optional: Check if roll number matches expected pattern (e.g., "AB1234")
        else if (!rollNo.matches("[A-Z]{2}\\d{4}")) {
            request.setAttribute("rollNo", "Roll No format is invalid (e.g., AB1234)");
            pass = false;
        }

        return pass;
    }
}
