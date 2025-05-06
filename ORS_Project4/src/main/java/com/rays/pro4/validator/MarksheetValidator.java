package com.rays.pro4.validator;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class MarksheetValidator {

	public static boolean validate(final HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll No"));
			pass = false;
		} else if (!DataValidator.isRollNo(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", "Invalid Roll Number");
			pass = false;
		}

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.name", "Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("physics"))) {
            request.setAttribute("physics", PropertyReader.getValue("error.require", "Physics Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("physics"))) {
            request.setAttribute("physics", PropertyReader.getValue("error.integer", "Physics Marks"));
            pass = false;
        } else {
            int physics = DataUtility.getInt(request.getParameter("physics"));
            if (physics > 100 || physics < 0) {
                request.setAttribute("physics", "Marks must be between 0 and 100");
                pass = false;
            }
        }

        if (DataValidator.isNull(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry", PropertyReader.getValue("error.require", "Chemistry Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry", PropertyReader.getValue("error.integer", "Chemistry Marks"));
            pass = false;
        } else {
            int chemistry = DataUtility.getInt(request.getParameter("chemistry"));
            if (chemistry > 100 || chemistry < 0) {
                request.setAttribute("chemistry", "Marks must be between 0 and 100");
                pass = false;
            }
        }

        if (DataValidator.isNull(request.getParameter("maths"))) {
            request.setAttribute("maths", PropertyReader.getValue("error.require", "Maths Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("maths"))) {
            request.setAttribute("maths", PropertyReader.getValue("error.integer", "Maths Marks"));
            pass = false;
        } else {
            int maths = DataUtility.getInt(request.getParameter("maths"));
            if (maths > 100 || maths < 0) {
                request.setAttribute("maths", "Marks must be between 0 and 100");
                pass = false;
            }
        }

		return pass;
	}
}
