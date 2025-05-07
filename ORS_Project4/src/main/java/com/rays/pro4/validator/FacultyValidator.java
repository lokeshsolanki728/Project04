
package com.rays.pro4.validator;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

/**
 * Faculty Validator class to validate Faculty data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class FacultyValidator {

    /**
     * Validates the request attributes for Faculty data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final HttpServletRequest request) {
        final HashMap<String, String> errors = new HashMap<String, String>();
        boolean pass = true;        
        
        final String firstName = request.getParameter("firstName");
        final String lastName = request.getParameter("lastName");
        final String emailId = request.getParameter("emailId");
        final String mobileNo = request.getParameter("mobileNo");
        final String gender = request.getParameter("gender");
        final String dob = request.getParameter("dob");
        final String collegeId = request.getParameter("collegeId");
        final String courseId = request.getParameter("courseId");
        final String subjectId = request.getParameter("subjectId");

        if (DataValidator.isNull(firstName)) {
            errors.put("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(firstName)) {
            errors.put("firstName", PropertyReader.getValue("error.name", "First name"));
            pass = false;
        }

        if (DataValidator.isNull(lastName)) {
            errors.put("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(lastName)) {
            errors.put("lastName", PropertyReader.getValue("error.name", "Last name"));
            pass = false;
        }

        if (DataValidator.isNull(emailId)) {
            errors.put("emailId", PropertyReader.getValue("error.require", "Email "));
            pass = false;
        } else if (!DataValidator.isEmail(emailId)) {
            errors.put("emailId", PropertyReader.getValue("error.email", "Email"));
            pass = false;
        }

        if (DataValidator.isNull(mobileNo)) {
            errors.put("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isMobileNo(mobileNo)) {
            errors.put("mobileNo", PropertyReader.getValue("error.mobileNo"));
            pass = false;
        }

        if (DataValidator.isNull(gender)) {
            errors.put("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        } else if (!gender.equals("Male") && !gender.equals("Female")) {
            errors.put("gender", PropertyReader.getValue("error.gender"));
            pass = false;
        }

        if (DataValidator.isNull(dob)) {
            errors.put("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(dob)) {
            errors.put("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
            pass = false;
        }
        
        if (DataValidator.isNull(collegeId) || DataValidator.getLong(collegeId) <= 0) {
            errors.put("collegeId", PropertyReader.getValue("error.require", "College Name"));
            pass = false;
        }
        if (DataValidator.isNull(courseId) || DataValidator.getLong(courseId) <= 0) {
            errors.put("courseId", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        }        
        if (DataValidator.isNull(subjectId) || DataValidator.getLong(subjectId) <= 0) {
            errors.put("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }

        request.setAttribute("errors", errors);
        return pass;
    }
}