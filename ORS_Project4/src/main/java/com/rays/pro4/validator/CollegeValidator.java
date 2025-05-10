package com.rays.pro4.validator;

import java.util.HashMap;


import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.Util.PropertyReader;

public class CollegeValidator {

    /**
     * Validates College data.
     *
     * @param request the request
     * @return true if data is valid, false otherwise
     */
    public static boolean validate(CollegeDTO dto) {
        boolean pass = true;
        
        if (DataValidator.isNull(dto.getName())) {
            dto.addError("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;    
        } else if (!DataValidator.isName(dto.getName())) {
            dto.addError("name", PropertyReader.getValue("error.name", "Name"));
            pass = false;
        }

        if (DataValidator.isNull(dto.getAddress())) {
            dto.addError("address", PropertyReader.getValue("error.require", "Address"));
            pass = false;      
        } else if (!DataValidator.isNotNullOrEmpty(dto.getAddress())) {
            dto.addError("address", PropertyReader.getValue("error.address", "Address"));
            pass = false;          
        }

        if (DataValidator.isNull(dto.getCity())) {
            dto.addError("city", PropertyReader.getValue("error.require", "City"));
            pass = false;  
        } else if (!DataValidator.isNotNullOrEmpty(dto.getCity())) {
            dto.addError("city", PropertyReader.getValue("error.city", "City"));
            pass = false;     
        }

        if (DataValidator.isNull(dto.getState())) {
            dto.addError("state", PropertyReader.getValue("error.require", "State"));
            pass = false;  
        } else if (!DataValidator.isNotNullOrEmpty(dto.getState())) {
            dto.addError("state", PropertyReader.getValue("error.state", "State"));
            pass = false;  
        }

        if (DataValidator.isNull(dto.getPhoneNo())) {
            dto.addError("phoneNo", PropertyReader.getValue("error.require", "Phone Number"));
            pass = false;  
        } else if (!DataValidator.isPhoneNo(dto.getPhoneNo())) {
            dto.addError("phoneNo", PropertyReader.getValue("error.phone", "Phone Number"));
            pass = false;  
        }

 return pass;
    }
}