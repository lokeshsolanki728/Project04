package com.rays.pro4.validator;

import java.util.Map;

import com.rays.pro4.DTO.CourseDTO;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class CourseValidator {
    /**
     * Validates CourseDTO
     * 
     * @param dto the CourseDTO to validate
     * @return true if validation passes, false otherwise
     */
    public static boolean validate(CourseDTO dto) {
        boolean pass = true;

        if (DataValidator.isNullOrEmpty(dto.getName())) {
            dto.setErrorMessage("name", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        } else if (!DataValidator.isName(dto.getName())) {
            dto.setErrorMessage("name", PropertyReader.getValue("error.name", "Course Name"));
            pass = false;
        }

        if (DataValidator.isNullOrEmpty(dto.getDescription())) {
            dto.setErrorMessage("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        } else if (!DataValidator.isDescription(dto.getDescription())) {
             dto.setErrorMessage("description", PropertyReader.getValue("error.description", "Course Description"));
             pass = false;
        }

        if (DataValidator.isNullOrEmpty(dto.getDuration())) {
            dto.setErrorMessage("duration", PropertyReader.getValue("error.require", "Duration"));
            pass = false;
        }
        return pass;
    }
}