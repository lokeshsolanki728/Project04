package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.rays.pro4.Util.MessageConstant;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;

//TODO: Auto-generated Javadoc
/**
 * User registration functionality Controller. Performs operation for User
 * Registration
 * 
 * @author Lokesh SOlanki
 */
@WebServlet(name = "UserRegistrationCtl", urlPatterns = { "/UserRegistrationCtl" })
public class UserRegistrationCtl extends BaseCtl {

	/** The Constant OP_SIGN_UP. */
	public static final String OP_SIGN_UP = "SignUp";

	private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(UserRegistrationCtl.class);
    private final UserModel model = new UserModel();
    
    /**
	 * Validates input data entered by User.
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("UserRegistrationCtl Method validate Started");

		boolean pass = true;
        String login = request.getParameter("login");
        String dob = request.getParameter("dob");

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.name.alphabet"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue(MessageConstant.NAME_ALPHABET));
            pass = false;
        }        

        if (DataValidator.isNull(login)) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
        } else if (!DataValidator.isEmail(login)) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login Id"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isMobileNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No. contain 10 Digits & Series start with 6-9");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("password"))) {
            request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
        } else if (!DataValidator.isPassword(request.getParameter("password"))) {
            request.setAttribute("password", "Password contain 8 letters with alpha-numeric & special Character");
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }

        if (DataValidator.isNull(dob)) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isAge(dob)) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Minimum Age 18 year"));
            pass = false;
        }

        if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))
                && !"".equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", "Password and Confirm Password Must be Same");
            pass = false;
        }
		log.debug("UserRegistrationCtl Method validate Ended");

		return pass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */
    protected void populateBean(HttpServletRequest request, UserBean bean) {
		log.debug("UserRegistrationCtl Method populatebean Started");
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
		log.debug("UserRegistrationCtl Method populatebean Ended");
	}
	
	/**
	 * Contains Display logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		log.debug("UserRegistrationCtl Method doGet Started");
		ServletUtility.forward(getView(), request, response);

	}

	/**
	 * Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("UserRegistrationCtl Method doPost Started");

		final String op = DataUtility.getString(request.getParameter("operation"));
        
        final UserBean bean = new UserBean();
        populateBean(request,bean);
		if (OP_SIGN_UP.equalsIgnoreCase(op)) {
            if(validate(request)){
                try {
                    save(bean, request);                   
                } catch (final DuplicateRecordException e) {
                    log.error("Duplicate record exception", e);
                    ServletUtility.setErrorMessage("Login Id Already Exists", request);
                }catch (final ApplicationException | DatabaseException e) {
                    log.error(e);
                    handleDatabaseException(e, request, response);
                }           
            }
            ServletUtility.forward(getView(), request, response);
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
        }
    }
	
	private void save(UserBean bean, HttpServletRequest request) 
            throws DuplicateRecordException, ApplicationException {
        log.debug("save method start");
        long pk = model.registerUser(bean);
        bean.setId(pk);
        ServletUtility.setSuccessMessage(MessageConstant.USER_ADD, request);
        log.debug("save method end");
    }

	@Override
	protected String getView() {
		return ORSView.USER_REGISTRATION_VIEW;	

	}

}
