package com.rays.pro4.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class CourseValidator {

    public static boolean validate(HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        boolean pass = true;        
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String duration = request.getParameter("duration");

        if (DataValidator.isNullOrEmpty(name)) {
            errors.put("name", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        } else if (!DataValidator.isName(name)) {
            errors.put("name", PropertyReader.getValue("error.name", "Course Name"));
            pass = false;
        }
        
        if (DataValidator.isNullOrEmpty(description)) {
            errors.put("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }else if (!DataValidator.isDescription(description)) {
            errors.put("description", PropertyReader.getValue("error.description", "Course Description"));
        }
        
        if (DataValidator.isNullOrEmpty(duration)) {
            errors.put("duration", PropertyReader.getValue("error.require", "Duration"));
            pass = false;
        }
        request.setAttribute("errors", errors);
        return pass;
    }
}