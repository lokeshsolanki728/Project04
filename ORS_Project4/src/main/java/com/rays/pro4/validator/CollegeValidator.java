package com.rays.pro4.validator;

import javax.servlet.http.HttpServletRequest;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class CollegeValidator {

    public static boolean validate(HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.name", "Name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("address"))) {
            request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("address"))) {
            request.setAttribute("address", PropertyReader.getValue("error.name", "Address"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("city"))) {
            request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("city"))) {
            request.setAttribute("city", PropertyReader.getValue("error.name", "City"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("state"))) {
			request.setAttribute("state", PropertyReader.getValue("error.require", "State"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("state"))) {
			request.setAttribute("state", PropertyReader.getValue("error.name", "State"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", PropertyReader.getValue("error.require", "Phone Number"));
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", PropertyReader.getValue("error.phone", "Phone Number"));
            pass = false;
        }

        return pass;
    }
}