 package com.rays.pro4.controller;

import java.io.IOException;
import java.util.MissingFormatArgumentException;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/**
 * Base controller class of project. It contain (1) Generic operations (2)
 * Generic constants (3) Generic work flow
 *
 * @author  Lokesh SOlanki
 *
 */

public abstract class BaseCtl extends HttpServlet {

	/**	
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.rays.pro4.resources.System");

	public static final String OP_SAVE = "Save";
	public static final String OP_CANCEL = "Cancel";
	public static final String OP_DELETE = "Delete";
	public static final String OP_LIST = "List";
	public static final String OP_SEARCH = "Search";
	public static final String OP_VIEW = "View";
	public static final String OP_NEXT = "Next";
	public static final String OP_PREVIOUS = "Previous";
	public static final String OP_NEW = "New";
	public static final String OP_GO = "Go";
	public static final String OP_BACK = "Back";
	public static final String OP_LOG_OUT = "Logout";
	public static final String OP_RESET = "Reset";
	public static final String OP_UPDATE = "Update";

	/**
	 * Success message key constant
	 */
	public static final String MSG_SUCCESS = "success";

	/**
	 * Error message key constant
	 */
	public static final String MSG_ERROR = "error";

	/**
	 * Validates input data entered by User
	 *
	 * @param request
	 * @return
	 */
	protected  boolean validate(HttpServletRequest request) {
		
		return true;
		
	}
	}

	/**
	 * Loads list and other data required to display at HTML form
	 *
	 * @param request
	 */
	protected void preload(HttpServletRequest request) {
	}

	/**
	 * Populates bean object from request parameters
	 *
	 * @param request
	 * @return
	 */
	protected abstract BaseBean populateBean(HttpServletRequest request) ;
	

	/**
	 * Populates Generic attributes in DTO
	 *
	 * @param dto
	 * @param request
	 * @return
	 */
	protected BaseBean populateDTO(BaseBean dto, HttpServletRequest request) {
		
		String defaultUser= "root";
		try {
			defaultUser = RESOURCE_BUNDLE.getString("DEFAULT_USER");
		} catch (MissingResourceException e) {
			System.out.println("default user not found");
		}
				
		Objects.requireNonNull(defaultUser, "default user can not be null");
		
		String createdBy = request.getParameter("createdBy");
		String modifiedBy = defaultUser;

		UserBean userbean = (UserBean) request.getSession().getAttribute("user");

		if (userbean == null) {
		} else {

			modifiedBy = userbean.getLogin();

			// If record is created first time
			if (DataValidator.isNull(createdBy)) {
				createdBy = modifiedBy;
			}

		}

		dto.setCreatedBy(createdBy);
		dto.setModifiedBy(modifiedBy);

		long cdt = DataUtility.getLong(request.getParameter("createdDatetime"));

		if (cdt > 0) {
			dto.setCreatedDatetime(DataUtility.getTimestamp(cdt));
		} else {
			dto.setCreatedDatetime(DataUtility.getCurrentTimestamp());
		}

		dto.setModifiedDatetime(DataUtility.getCurrentTimestamp());

		return dto;
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Load the preloaded data required to display at HTML form
		preload(request); 

		String op = DataUtility.getString(request.getParameter("operation")); 
		// Check if operation is not DELETE, VIEW, CANCEL, and NULL then
		// perform input data validation		
		if (DataValidator.isNotNull(op) && !OP_CANCEL.equalsIgnoreCase(op) && !OP_VIEW.equalsIgnoreCase(op) && !OP_DELETE.equalsIgnoreCase(op) && !OP_RESET.equalsIgnoreCase(op)) {		
			// messages

			if (!validate(request)) {
				
				BaseBean bean = (BaseBean) populateBean(request);
				//wapis se inserted data dikhe jo phle in put kiya tha 
				ServletUtility.setBean(bean, request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		}try {
				super.service(request, response);
		}catch (Exception e) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.default"), request);
			ServletUtility.forward(getView(), request, response);
		}
	}

	/**
	 * Returns the VIEW page of this Controller
	 *
	 * @return
	 */
	protected abstract String getView();
	
	
}
