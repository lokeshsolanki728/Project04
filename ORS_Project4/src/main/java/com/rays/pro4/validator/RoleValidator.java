package com.rays.pro4.validator;

import com.rays.pro4.DTO.RoleDTO;

/**
 * Role Validator class to validate Role data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class RoleValidator {

    /**
     * Validates the request attributes for Role data.
     *
     * @param request The HttpServletRequest object.
     * @param dto The RoleDTO object to validate.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final RoleDTO dto) {
        boolean pass = true;

        if (DataValidator.isNull(dto.getName())) {
            pass = false;
        } else if (!DataValidator.isName(dto.getName())) {
            pass = false;
        }
        

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.name", "Description"));
            pass = false;
        }

        return pass;
    }
}