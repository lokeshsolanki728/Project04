
package com.rays.pro4.validator;

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
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.name", "First name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.name", "Last name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("emailId"))) {
            request.setAttribute("emailId", PropertyReader.getValue("error.require", "Email "));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("emailId"))) {
            request.setAttribute("emailId", PropertyReader.getValue("error.email", "Email"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isMobileNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No. must be 10 Digit and No. Series start with 6-9");
            pass = false;
        }


        


        
        
        if (DataValidator.isNull(request.getParameter("subjectName"))) {
            request.setAttribute("subjectName", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.name", "gender"));
            pass = false;
        }

        return pass;
    }
}