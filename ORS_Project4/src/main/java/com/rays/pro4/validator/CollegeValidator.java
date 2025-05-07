package com.rays.pro4.validator;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class CollegeValidator {

    /**
     * Validates College data.
     *
     * @param request the request
     * @return true if data is valid
     */
    public static boolean validate(HttpServletRequest request) {
        HashMap<String, String> errors = new HashMap<String, String>();
        boolean pass = true;
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String phoneNo = request.getParameter("phoneNo");
        
        if (DataValidator.isNull(name)) {
            errors.put("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;    
        }else if(!DataValidator.isName(name)){
            errors.put("name", PropertyReader.getValue("error.name", "Name"));
            pass = false;
        }

        if (DataValidator.isNull(address)) {
            errors.put("address", PropertyReader.getValue("error.require", "Address"));
            pass = false;      
        }else if (!DataValidator.isNotNullOrEmpty(address)) {
            errors.put("address", PropertyReader.getValue("error.address", "Address"));
            pass = false;          
        }

        if (DataValidator.isNull(city)) {
            errors.put("city", PropertyReader.getValue("error.require", "City"));
            pass = false;  
        }else if (!DataValidator.isNotNullOrEmpty(city)) {
            errors.put("city", PropertyReader.getValue("error.city", "City"));
            pass = false;     
        }

        if (DataValidator.isNull(state)) {
            errors.put("state", PropertyReader.getValue("error.require", "State"));
            pass = false;  
        }else if (!DataValidator.isNotNullOrEmpty(state)) {
            errors.put("state", PropertyReader.getValue("error.state", "State"));
            pass = false;  
        }

        if (DataValidator.isNull(phoneNo)) {
            errors.put("phoneNo", PropertyReader.getValue("error.require", "Phone Number"));
            pass = false;  
        }else if (!DataValidator.isPhoneNo(phoneNo)) {
            errors.put("phoneNo", PropertyReader.getValue("error.phone", "Phone Number"));
            pass = false;  
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
        }

        return pass;
    }
}