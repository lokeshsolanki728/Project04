--- a/ORS_Project4/src/main/java/com/rays/pro4/util/MarksheetValidator.java
+++ b/ORS_Project4/src/main/java/com/rays/pro4/util/MarksheetValidator.java
@@
-package com.rays.pro4.util;
-
-import javax.servlet.http.HttpServletRequest;
-
-/**
- * Marksheet Validator class to validate Marksheet data.
- * 
- * @author Lokesh SOlanki
- *
- */
-public class MarksheetValidator {
-
-    /**
-     * Validates the request attributes for Marksheet.
-     *
-     * @param request the HttpServletRequest object
-     * @return true if the request is valid, false otherwise
-     */
-    public static boolean validate(final HttpServletRequest request) {
-        boolean pass = true;
-
-        if (DataValidator.isNull(request.getParameter("rollNo"))) {
-            request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll No"));
-            pass = false;
-        } else if (!DataValidator.isRollNo(request.getParameter("rollNo"))) {
-            request.setAttribute("rollNo", "Invalid Roll Number");
-            pass = false;
-        }
-
-        if (DataValidator.isNull(request.getParameter("name"))) {
-            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
-            pass = false;
-        } else if (!DataValidator.isName(request.getParameter("name"))) {
-            request.setAttribute("name", PropertyReader.getValue("error.name", "Name"));
-            pass = false;
-        }
-
-        if (DataValidator.isNull(request.getParameter("physics"))) {
-            request.setAttribute("physics", PropertyReader.getValue("error.require", "Physics Marks"));
-            pass = false;
-        } else if (!DataValidator.isInteger(request.getParameter("physics"))) {
-            request.setAttribute("physics", PropertyReader.getValue("error.integer", "Physics Marks"));
-            pass = false;
-        } else if (DataUtility.getInt(request.getParameter("physics")) > 100 || DataUtility.getInt(request.getParameter("physics")) < 0) {
-            request.setAttribute("physics", "Marks must be between 0 and 100");
-            pass = false;
-        }
-
-        if (DataValidator.isNull(request.getParameter("chemistry"))) {
-            request.setAttribute("chemistry", PropertyReader.getValue("error.require", "Chemistry Marks"));
-            pass = false;
-        } else if (!DataValidator.isInteger(request.getParameter("chemistry"))) {
-            request.setAttribute("chemistry", PropertyReader.getValue("error.integer", "Chemistry Marks"));
-            pass = false;
-        } else if (DataUtility.getInt(request.getParameter("chemistry")) > 100 || DataUtility.getInt(request.getParameter("chemistry")) < 0) {
-            request.setAttribute("chemistry", "Marks must be between 0 and 100");
-            pass = false;
-        }
-
-        if (DataValidator.isNull(request.getParameter("maths"))) {
-            request.setAttribute("maths", PropertyReader.getValue("error.require", "Maths Marks"));
-            pass = false;
-        } else if (!DataValidator.isInteger(request.getParameter("maths"))) {
-            request.setAttribute("maths", PropertyReader.getValue("error.integer", "Maths Marks"));
-            pass = false;
-        } else if (DataUtility.getInt(request.getParameter("maths")) > 100 || DataUtility.getInt(request.getParameter("maths")) < 0) {
-            request.setAttribute("maths", "Marks must be between 0 and 100");
-            pass = false;
-        }
-
-        return pass;
-    }
-}
package com.rays.pro4.validator;

import java.util.HashMap;
import java.util.Map;
import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Util.DataValidator;


/**
* Contains Marksheet form elements validation logic
*
* @author Lokesh SOlanki
*/
public class MarksheetValidator {


	/**
	* Validates MarksheetBean for add operation.
	*
	* @param bean Marksheet bean to be validated
	* @return Map of validation error messages
	*/
	public static Map<String, String> validate(MarksheetBean bean) {

		Map<String, String> errors = new HashMap<>();

		if (DataValidator.isNull(bean.getRollNo())) {
			errors.put("rollNo", "Roll number is required");
		} else if (!DataValidator.isRollNo(bean.getRollNo())) {
			errors.put("rollNo", "Invalid roll number format");
		}

		if (bean.getStudentld() <= 0) {
			errors.put("studentId", "Student is required");
		}
        if(bean.getPhysics() < 0 ){
            errors.put("physics", "marks can not be nagetive");
        }
         if (bean.getPhysics() > 100) {
            errors.put("physics", "marks can not be greater than 100");
        }
        if(bean.getChemistry() < 0 ){
            errors.put("chemistry", "marks can not be nagetive");
        }
         if (bean.getChemistry() > 100) {
            errors.put("chemistry", "marks can not be greater than 100");
        }
         if(bean.getMaths() < 0 ){
            errors.put("maths", "marks can not be nagetive");
        }
          if (bean.getMaths() > 100) {
            errors.put("maths", "marks can not be greater than 100");
        }

		return errors;
	}


}
package com.rays.pro4.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Marksheet Validator class to validate Marksheet data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class MarksheetValidator {

    /**
     * Validates the request attributes for Marksheet.
     *
     * @param request the HttpServletRequest object
     * @return true if the request is valid, false otherwise
     */
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
        } else if (DataUtility.getInt(request.getParameter("physics")) > 100 || DataUtility.getInt(request.getParameter("physics")) < 0) {
            request.setAttribute("physics", "Marks must be between 0 and 100");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry", PropertyReader.getValue("error.require", "Chemistry Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry", PropertyReader.getValue("error.integer", "Chemistry Marks"));
            pass = false;
        } else if (DataUtility.getInt(request.getParameter("chemistry")) > 100 || DataUtility.getInt(request.getParameter("chemistry")) < 0) {
            request.setAttribute("chemistry", "Marks must be between 0 and 100");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("maths"))) {
            request.setAttribute("maths", PropertyReader.getValue("error.require", "Maths Marks"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("maths"))) {
            request.setAttribute("maths", PropertyReader.getValue("error.integer", "Maths Marks"));
            pass = false;
        } else if (DataUtility.getInt(request.getParameter("maths")) > 100 || DataUtility.getInt(request.getParameter("maths")) < 0) {
            request.setAttribute("maths", "Marks must be between 0 and 100");
            pass = false;
        }

        return pass;
    }
}