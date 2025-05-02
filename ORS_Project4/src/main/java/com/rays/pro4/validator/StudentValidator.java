--- /dev/null
+++ b/ORS_Project4/src/main/java/com/rays/pro4/validator/StudentValidator.java


import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class StudentValidator {

    public static boolean validate(HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstname"))) {
            request.setAttribute("firstname", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("firstname"))) {
            request.setAttribute("firstname", PropertyReader.getValue("error.name", "First name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("lastname"))) {
            request.setAttribute("lastname", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("lastname"))) {
            request.setAttribute("lastname", PropertyReader.getValue("error.name", "Last name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("mobile"))) {
            request.setAttribute("mobile", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isMobileNo(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Mobile No. must be 10 Digit and No. Series start with 6-9");
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.require", "Email "));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.email", "Login Id"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isAge(request.getParameter("dob"))) {
            request.setAttribute("dob", "Student Age must be Greater then 18 year ");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("collegename"))) {
            request.setAttribute("collegename", PropertyReader.getValue("error.require", "College Name"));
            pass = false;
        }
        return pass;
    }
}