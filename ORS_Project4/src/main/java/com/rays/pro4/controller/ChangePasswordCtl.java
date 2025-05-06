package com.rays.pro4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import com.rays.pro4.util.ChangePasswordValidator; 
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Model.UserModel;

import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.ServletUtility;

/**
 * The Class ORSView is a controller that use to provide page forward and redirect.
* The Class ChangePasswordCtl is a controller that allows users to change their password.
* It handles the display and submission logic for the change password form.
* 
* @author Lokesh SOlanki
*/
@WebServlet(name = "ChangePasswordCtl", urlPatterns = { "/ctl/ChangePasswordCtl" })
public class ChangePasswordCtl extends BaseCtl<UserBean>{

	private static final long serialVersionUID = 1L;

	/**
	 * Change My Profile operation constant
	 */
	public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";

	/** The logger. */
	private static Logger log = Logger.getLogger(ChangePasswordCtl.class);
	
	/**
     * The model.
     */
    
    
    /**
     * The model.
     */
    
    
	private final UserModel model = new UserModel();

	/**
	 * Validates the input data entered by the user.
	 * 
	 * @param request The HttpServletRequest object.
	 * @return True if the request is valid, false otherwise.
	 */
	@Override
	protected boolean validate(final HttpServletRequest request) {

	    log.debug("ChangePasswordCtl Method validate Started");
	    final boolean pass = ChangePasswordValidator.validate(request);
	    if (!pass) {
	        log.debug("ChangePasswordCtl Method validate Ended with error");
	    }
	
	    log.debug("ChangePasswordCtl Method validate Ended");
	    return pass;
	}

	/**
     * Populates bean object from request parameters.
     *
     * @param request The HttpServletRequest object.
     * @return The UserBean populated with request parameters.
     */
    @Override
    protected UserBean populateBean(HttpServletRequest request) {
		log.debug("ChangePasswordCtl Method populateBean Started");
		UserBean bean = new UserBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setPassword(DataUtility.getString(request.getParameter("oldPassword")));
		bean.setNewPassword(DataUtility.getString(request.getParameter("newPassword")));
		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
		
		log.debug("ChangePasswordCtl Method populateBean Ended");
		
		
        return bean;
    }     
    /**
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs.
	     */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ChangePasswordCtl Method doGet Started");
		final HttpSession session = request.getSession();
		if(session.getAttribute("user")==null){
			ServletUtility.redirect(ORSView.LOGIN_CTL, request, response);
			return;
		}		
		ServletUtility.forward(getView(), request, response);	
		log.debug("ChangePasswordCtl Method doGet Ended");
	}
	
	
    /**
     * Handles GET requests for displaying the change password view.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs.
	 */
	
	
	
	/**
	 * Change password.
	 *
	 * @param bean        the bean
	 * @param newPassword the new password
	 * @param request     the request
	 * @param response    the response
	 * @throws ApplicationException the application exception
	 * @throws IOException          Signals that an I/O exception has
	 *                              occurred.
	 * @throws ServletException     the servlet exception
	 */
	private void changePassword(final UserBean bean, final String newPassword, final HttpServletRequest request,
			final HttpServletResponse response) throws ApplicationException, IOException, ServletException {
		log.debug("ChangePasswordCtl Method changePassword Started");
		final HttpSession session = request.getSession();
		final UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean == null) {
			ServletUtility.forward(getView(), request, response);
			return;
		}
		final long id = userBean.getId();
		final UserDTO dto = bean.getDTO();
		try {
			model.changePassword(id, dto.getPassword(), newPassword);
			ServletUtility.setSuccessMessage(MessageConstant.PASSWORD_CHANGE, request);
			ServletUtility.forward(getView(), request, response);
			log.debug("ChangePasswordCtl Method changePassword Ended");
		} catch (final Exception e) {
			log.error("ChangePasswordCtl Method changePassword error", e);
			ServletUtility.setErrorMessage("Password change failed", request);
			ServletUtility.forward(getView(), request, response);
		}
	}

	/**
	 * Handles POST requests for processing the change password form
	 * submission.
	 * 
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		log.debug("ChangePasswordCtl Method doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		UserBean bean = populateBean(request);
		String newPassword = bean.getNewPassword();

		try {
			if (OP_SAVE.equalsIgnoreCase(op)) {
				if (!validate(request)) {
					ServletUtility.forward(getView(), request, response);
					return;
				}
				changePassword(bean, newPassword, request, response);
			} else if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.MY_PROFILE_CTL, request, response);
				return;
			}
			ServletUtility.forward(getView(), request, response);
			return;
		} catch (ApplicationException e) {
			log.error(e);
			handleDatabaseException(e, request, response);
		}
		log.debug("ChangePasswordCtl Method doPost Ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return change password view
	 */
	@Override
	protected String getView() {
		return ORSView.CHANGE_PASSWORD_VIEW;
	}
}