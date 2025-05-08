
package com.rays.pro4.validator;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.DTO.FacultyDTO;
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
    public static boolean validate(final FacultyDTO dto, final HttpServletRequest request) {  
        final HashMap<String, String> errors = new HashMap<String, String>();
        boolean pass = true;        
        
        final String firstName = dto.getFirstName();
        final String lastName = dto.getLastName();
        final String emailId = dto.getEmailId();
        final String mobileNo = dto.getMobileNo();
        final String gender = dto.getGender();
        final String dob = DataValidator.getDateString(dto.getDob());
        final long collegeId = dto.getCollegeId();
        final long courseId = dto.getCourseId();
        final long subjectId = dto.getSubjectId();

        if (DataValidator.isNull(firstName)) {
            errors.put("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(firstName)) {
            errors.put("firstName", PropertyReader.getValue("error.name", "First Name"));
            pass = false;
        }

        if (DataValidator.isNull(lastName)) {
            errors.put("lastName", PropertyReader.getValue("error.require", "Last name"));
            pass = false;
        } else if (!DataValidator.isName(lastName)) {
            errors.put("lastName", PropertyReader.getValue("error.name", "Last name"));
            pass = false;
        }

        if (DataValidator.isNull(emailId)) {
            errors.put("emailId", PropertyReader.getValue("error.require", "Email"));
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
        } else if (!(gender.equals("Male") || gender.equals("Female"))) {
            errors.put("gender", PropertyReader.getValue("error.gender"));
            pass = false;
        }

        if (DataValidator.isNull(dob)) {
            errors.put("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(dob)) {
            errors.put("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
            pass = false;
        } else if (DataValidator.isvalidateAge(dto.getDob())) {
            errors.put("dob", PropertyReader.getValue("error.invalidAge"));
            pass = false;
        }
        
        if (collegeId <= 0) {
            errors.put("collegeId", PropertyReader.getValue("error.require", "College Name"));
            pass = false;
        }
        if (courseId <= 0) {
            errors.put("courseId", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        }        
        if (subjectId <= 0) {
            errors.put("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }

        request.setAttribute("errors", errors);
        return pass;
    }
}