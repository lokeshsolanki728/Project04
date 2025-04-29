package com.rays.pro4.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;



/**
* The Class ChangePasswordCtl.
* 
* @author Lokesh SOlanki
*/
@WebServlet(name = "ChangePasswordCtl", urlPatterns = { "/ctl/ChangePasswordCtl" })
public class ChangePasswordCtl extends BaseCtl{

	private static final long serialVersionUID = 1L;

	/** The Constant OP_CHANGE_MY_PROFILE. */
	public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";

	/** The log. */
	private static Logger log = Logger.getLogger(ChangePasswordCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request){

		log.debug("ChangePasswordCtl Method validate Started");

		boolean pass = true;

		String op = request.getParameter("operation");

		if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {

			return pass;
		}
		if (DataValidator.isNull(request.getParameter("oldPassword"))) {
			request.setAttribute("oldPassword", PropertyReader.getValue("error.require", "Old Password"));			
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("newPassword"))) {
			request.setAttribute("newPassword", PropertyReader.getValue("error.require", "New Password"));
			pass = false;
		}
		else if(request.getParameter("oldPassword").equals(request.getParameter("newPassword"))){
		request.setAttribute("newPassword", PropertyReader.getValue("error.newPassword.oldPassword"));
		pass = false;
		}
		
		else if (!DataValidator.isPassword(request.getParameter("newPassword"))) {
			  request.setAttribute("newPassword",PropertyReader.getValue("error.password"));
			  pass = false; 
			  }
			 
		
		if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
			request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
			pass = false;		
		}	
		else if (!DataValidator.isPassword(request.getParameter("confirmPassword"))) {
			  request.setAttribute("confirmPassword",PropertyReader.getValue("error.password"));
			  pass = false; 
		}else if(!request.getParameter("newPassword").equals(request.getParameter("confirmPassword"))){
			request.setAttribute("confirmPassword", PropertyReader.getValue("error.confirmPassword.notMatch"));
			pass = false;
		}
		
		log.debug("ChangePasswordCtl Method validate Ended");

		return pass;
	}

	@Override	
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("ChangePasswordCtl Method populatebean Started");

		UserBean bean = new UserBean();

		bean.setPassword(DataUtility.getString(request.getParameter("oldPassword")));

		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

		populateDTO(bean, request);

		log.debug("ChangePasswordCtl Method populatebean Ended");

		return bean;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)	
			throws ServletException, IOException {
		ServletUtility.forward(getView(), request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);

		
		log.debug("ChangePasswordCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		UserModel model = new UserModel();

		UserBean bean = (UserBean) populateBean(request);

		UserBean UserBean = (UserBean) session.getAttribute("user");

		String newPassword = (String) request.getParameter("newPassword");

		long id = UserBean.getId();

		if (OP_SAVE.equalsIgnoreCase(op)) {
			try {
				boolean flag = model.changePassword(id, bean.getPassword(), newPassword);
				if (flag == true) {
					bean = model.findByLogin(UserBean.getLogin());
					session.setAttribute("user", bean);
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.password.change"), request);
				}
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (RecordNotFoundException ex) {
				ServletUtility.setErrorMessage(ex.getMessage(), request);
			}
		} else if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.MY_PROFILE_CTL, request, response);


			return;

		}
		ServletUtility.forward(ORSView.CHANGE_PASSWORD_VIEW, request, response);
		log.debug("ChangePasswordCtl Method doGet Ended");
	}

	
	@Override
	protected String getView() {
		return ORSView.CHANGE_PASSWORD_VIEW;
	}
	

}
