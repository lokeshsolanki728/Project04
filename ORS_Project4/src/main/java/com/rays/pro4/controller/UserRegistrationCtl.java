package com.rays.pro4.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.rays.pro4.Util.MessageConstant;

import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.DatabaseException;;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.validator.UserValidator;
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
public class UserRegistrationCtl extends BaseCtl<UserDTO> {

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
        Map<String, String> errors = UserValidator.validate(request);
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            log.debug("UserRegistrationCtl Method validate Ended with errors");
            return false;
        }
        log.debug("UserRegistrationCtl Method validate Ended");
        return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */
    @Override
    protected UserDTO populateBean(HttpServletRequest request) {
		log.debug("UserRegistrationCtl Method populatebean Started");
        UserDTO bean = new UserDTO();

		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));        
		log.debug("UserRegistrationCtl Method populatebean Ended");
        return bean;
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		log.debug("UserRegistrationCtl Method doPost Started");

		final String op = DataUtility.getString(request.getParameter("operation"));
        
        final UserDTO bean = populateBean(request);

		bean.setRoleId(2);
		bean.setCreatedDatetime(DataUtility.getCurrentTimestamp());
        bean.setModifiedDatetime(DataUtility.getCurrentTimestamp());
		if (OP_SIGN_UP.equalsIgnoreCase(op)) {
             if (validate(request)) {
                 try {
                     save(bean, request);
                 } catch (final DuplicateRecordException e) {
                     log.error("Duplicate record exception", e);
                     ServletUtility.setErrorMessage("Login Id Already Exists", request);
                 } catch (final ApplicationException e) {
                     log.error(e);
                     handleDatabaseException(e, request, response);
            }
            ServletUtility.forward(getView(), request, response);
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
        }
    }
	
	private void save(UserDTO bean, HttpServletRequest request)
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
