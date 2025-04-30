 package com.rays.pro4.controller;

import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
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

public abstract class BaseCtl<T extends BaseBean> extends HttpServlet {

	/**	
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.rays.pro4.resources.System");

	/**
	 * Save operation constant
	 */
	public static final String OP_SAVE = "Save";
	/**
	 * Cancel operation constant
	 */
	public static final String OP_CANCEL = "Cancel";
	/**
	 * Delete operation constant
	 */
	public static final String OP_DELETE = "Delete";
	/**
	 * List operation constant
	 */
	public static final String OP_LIST = "List";
	/**
	 * Search operation constant
	 */
	public static final String OP_SEARCH = "Search";
	/**
	 * View operation constant
	 */
	public static final String OP_VIEW = "View";
	/**
	 * Next operation constant
	 */
	public static final String OP_NEXT = "Next";
	/**
	 * Previous operation constant
	 */
	public static final String OP_PREVIOUS = "Previous";
	/**
	 * New operation constant
	 */
	public static final String OP_NEW = "New";
	/**
	 * Go operation constant
	 */
	public static final String OP_GO = "Go";
	/**
	 * Back operation constant
	 */
	public static final String OP_BACK = "Back";
	/**
	 * Logout operation constant
	 */
	public static final String OP_LOG_OUT = "Logout";
	/**
	 * Reset operation constant
	 */
	public static final String OP_RESET = "Reset";
	/**
	 * Update operation constant
	 */
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
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) {
	}

	/**
	 * Validates input data entered by User
	 *
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) {
		
		return true;

	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request http request
	 * @return
	 */
	protected abstract T populateBean(HttpServletRequest request);

	/**
	 * Populates Generic attributes in DTO
	 *
	 * @param dto Bean object
	 * @param request  http request
	 * @return
	 */
	protected BaseBean populateDTO(BaseBean dto, HttpServletRequest request) {
		
		String createdBy = "";
		String modifiedBy = "";
		try {
			createdBy = RESOURCE_BUNDLE.getString("DEFAULT_USER");
			modifiedBy = RESOURCE_BUNDLE.getString("DEFAULT_USER");
		} catch (MissingResourceException e) {
			System.out.println("default user not found");
		}
		Objects.requireNonNull(modifiedBy, "default user can not be null");
		
		//String createdBy = request.getParameter("createdBy");
		
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
