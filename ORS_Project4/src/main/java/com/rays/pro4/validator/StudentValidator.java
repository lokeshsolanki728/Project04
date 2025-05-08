package com.rays.pro4.validator;

import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class StudentValidator {

    public static boolean validate(StudentDTO dto) {

        boolean pass = true;

        if (DataValidator.isNull(dto.getFirstName())) {
            dto.setErrorMessage("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(dto.getFirstName())) {
            dto.setErrorMessage("firstName", PropertyReader.getValue("error.name", "First name"));
            pass = false;
        }
        if (DataValidator.isNull(dto.getLastName())) {
            dto.setErrorMessage("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(dto.getLastName())) {
            dto.setErrorMessage("lastName", PropertyReader.getValue("error.name", "Last name"));
            pass = false;
        }
        if (DataValidator.isNull(dto.getMobileNo())) {
            dto.setErrorMessage("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isMobileNo(dto.getMobileNo())) {
            dto.setErrorMessage("mobileNo", "Mobile No. must be 10 Digit and No. Series start with 6-9");
            pass = false;
        }
        if (DataValidator.isNull(dto.getEmail())) {
            dto.setErrorMessage("email", PropertyReader.getValue("error.require", "Email "));
            pass = false;
        } else if (!DataValidator.isEmail(dto.getEmail())) {
            dto.setErrorMessage("email", PropertyReader.getValue("error.email", "Login Id"));
            pass = false;
        }
         if (DataValidator.isNull(DataValidator.getDateString(dto.getDob()))) {
            dto.setErrorMessage("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isAge(DataValidator.getDateString(dto.getDob()))) {        
            dto.setErrorMessage("dob", "Student Age must be Greater then 18 year ");
            pass = false;
        }

        if (dto.getCollegeId() <= 0) {
            dto.setErrorMessage("collegeId", PropertyReader.getValue("error.require", "College Name"));
            pass = false;
        }     
        return pass;
    }
    
     /**
     * Validates search criteria in StudentDTO
     *
     * @param dto the StudentDTO to validate
     * @return true if validation passes, false otherwise
     */
    public static boolean validateSearch(StudentDTO dto) {
        boolean pass = true;
        
         if (DataValidator.isNotNull(dto.getFirstName()) && !DataValidator.isName(dto.getFirstName())) {
            dto.setErrorMessage("firstName", PropertyReader.getValue("error.name", "First Name"));
            pass = false;
        }
        if (DataValidator.isNotNull(dto.getLastName()) && !DataValidator.isName(dto.getLastName())) {
            dto.setErrorMessage("lastName", PropertyReader.getValue("error.name", "Last Name"));
            pass = false;
        }
        if (DataValidator.isNotNull(dto.getMobileNo()) && !DataValidator.isMobileNo(dto.getMobileNo())) {
            dto.setErrorMessage("mobileNo", "Mobile No. must be 10 Digit and No. Series start with 6-9");
            pass = false;
        }
        if (DataValidator.isNotNull(dto.getEmail()) && !DataValidator.isEmail(dto.getEmail())) {
            dto.setErrorMessage("email", PropertyReader.getValue("error.email", "Email"));
            pass = false;
        }
        return pass;
    }   
}