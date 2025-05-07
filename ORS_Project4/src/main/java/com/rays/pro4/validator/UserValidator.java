package com.rays.pro4.validator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import com.rays.pro4.Util.DataValidator;
import java.util.Set;

import com.rays.pro4.Util.PropertyReader;

/**
 * User Validator class to validate User data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class UserValidator {

    private static final Set<String> ALLOWED_GENDERS = new HashSet<>(Set.of("Male", "Female", "Other"));

    /**
     * Validates the request attributes for User data.
     *
     * @param request The HttpServletRequest object.
     * @return True if the request attributes are valid, false otherwise.
     **/
    public static boolean validate(final HttpServletRequest request) {
        boolean pass = true;
        Map<String, String> errors = new HashMap<String, String>();

        String firstName = request.getParameter("firstName");
        if (DataValidator.isNull(firstName)) {
            errors.put("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(firstName)) {
            errors.put("firstName", PropertyReader.getValue("error.name", "First name"));
        }

        String lastName = request.getParameter("lastName");
        if (DataValidator.isNull(lastName)) {
            errors.put("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(lastName)) {
            errors.put("lastName", PropertyReader.getValue("error.name", "Last name"));
        }

        String login = request.getParameter("login");
        if (DataValidator.isNull(login)) {
            errors.put("login", PropertyReader.getValue("error.require", "Login"));
            pass = false;
        } else if (!DataValidator.isEmail(login)) {
            errors.put("login", PropertyReader.getValue("error.email", "Login"));
        }

        String password = request.getParameter("password");
        if (DataValidator.isNull(password)) {
            errors.put("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
        } else if (!DataValidator.isPassword(password)) {
            errors.put("password", PropertyReader.getValue("error.password", "Password"));
        }
        String confirmPassword = request.getParameter("confirmPassword");
        if (DataValidator.isNull(confirmPassword)) {
            errors.put("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
        } else if (!DataValidator.isPassword(confirmPassword)) {
            errors.put("confirmPassword", PropertyReader.getValue("error.password", "Confirm Password"));
        } else if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", PropertyReader.getValue("error.confirmPassword", "Confirm Password"));
            pass = false;
        }

        String dob = request.getParameter("dob");
        if (DataValidator.isNull(dob)) {
            errors.put("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(dob)) {
            errors.put("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
        }
        String mobileNo = request.getParameter("mobileNo");
        if (DataValidator.isNull(mobileNo)) {
            errors.put("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isMobileNo(mobileNo)) {
            errors.put("mobileNo", PropertyReader.getValue("error.mobileno"));
        }
        String gender = request.getParameter("gender");
        if (DataValidator.isNull(gender)) {
            errors.put("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        } else if (!ALLOWED_GENDERS.contains(gender)) {
            errors.put("gender", PropertyReader.getValue("error.gender"));
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
        }
        return pass;
    }
}