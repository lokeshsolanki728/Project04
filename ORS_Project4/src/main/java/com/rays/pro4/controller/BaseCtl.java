package com.rays.pro4.controller;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.DTO.BaseDTO;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Model.BaseModel;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Model.UserModel;
import org.apache.log4j.Logger;
import com.rays.pro4.Exception.ApplicationException;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.ServletUtility;

import java.io.IOException;

/**
 * Base controller class of project. It contains (1) Generic operations (2)
 * Generic constants (3) Generic work flow.
 *
 * @author Lokesh SOlanki
 */

public abstract class BaseCtl<T extends BaseBean> extends HttpServlet {

	/** Default serial version ID
	 */	
	protected long id = 0;
    protected String createdBy = null;
    protected String modifiedBy = null;
    protected Logger log = Logger.getLogger(this.getClass());   
	/**
	 * Save operation constant
	 */
	public static final String OP_SAVE = "Save"; //Save
	/**
	 * Cancel operation constant
	 */
	public static final String OP_CANCEL = "Cancel"; // Cancel
	/**
	 * Delete operation constant
	 */
	public static final String OP_DELETE = "Delete"; // Delete
	/**
	 * List operation constant
	 */
	public static final String OP_LIST = "List"; //List
	/**
	 * Search operation constant
	 */
	public static final String OP_SEARCH = "Search"; //Search
	/**
	 * View operation constant
	 */
	public static final String OP_VIEW = "View"; // View
	/**
	 * Next operation constant
	 */
	public static final String OP_NEXT = "Next"; // Next
	/**
	 * Previous operation constant
	 */
	public static final String OP_PREVIOUS = "Previous"; //Previous
	/**
	 * New operation constant
	 */
	public static final String OP_NEW = "New"; //New
	/**
	 * Go operation constant
	 */
	public static final String OP_GO = "Go"; // Go
	/**
	 * Back operation constant
	 */
	public static final String OP_BACK = "Back"; // Back
	/**
	 * Logout operation constant
	 */
	public static final String OP_LOG_OUT = "Logout"; //Logout
	/**
	 * Reset operation constant
	 */
	public static final String OP_RESET = "Reset"; //Reset
	/**
	 * Update operation constant
	 */
	public static final String OP_UPDATE = "Update";

	/**
	 * Success message key constant
	 */

    private static final long serialVersionUID = 1L;
   
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle("com.rays.pro4.resources.System");		
	 
	protected void preload(HttpServletRequest request) {
		
		RoleModel model = new RoleModel();
        try {
            List<RoleBean> list = model.list();
            request.setAttribute("roleList", list);
        } catch (ApplicationException e) {
            log.error(e);
        }
	}	

		protected BaseModel getModel(String modelName){
        if(modelName.equalsIgnoreCase("User")){
            return new UserModel();
        }else{
            return null;
        }
	}

	
	 
	protected boolean validate(HttpServletRequest request) {
		return true;
	}

	/**
	 * 
	 * @return
	 */
	protected abstract T populateBean(HttpServletRequest request);
    protected void populateBean(HttpServletRequest request, T bean) {

        BaseDTO dto = bean.getDto();


		long id = DataUtility.getLong(request.getParameter("id"));
		
		String createdBy = request.getParameter("createdby");
		String modifiedBy = request.getParameter("modifiedby");
        long cdt = DataUtility.getLong(request.getParameter("createdDateTime"));
        if (!DataValidator.isNull(createdBy)){
            dto.setCreatedBy(createdBy);
        } if (!DataValidator.isNull(modifiedBy)){
            dto.setModifiedBy(modifiedBy);
        }
        if (id > 0) {
            dto.setId(id);
        }
        if (cdt > 0) {
			dto.setCreatedDatetime(DataUtility.getTimestamp(cdt));			
        } else {
			dto.setCreatedDatetime(DataUtility.getCurrentTimestamp());           
        }        
        dto.setModifiedDateTime(DataUtility.getCurrentTimestamp());
	}	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Base Ctl service method start");
		// Load the preloaded data required to display at HTML form
		preload(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		T bean = populateBean(request);
		if (!DataValidator.isNull(op) && !OP_CANCEL.equalsIgnoreCase(op)
				&& !OP_VIEW.equalsIgnoreCase(op) && !OP_DELETE.equalsIgnoreCase(op)
				&& !OP_RESET.equalsIgnoreCase(op)) {
			if (!validate(request)) {
				ServletUtility.setBean(bean, request);
				
				return;
			}
		}
		ServletUtility.setBean(bean, request);
		try {
            super.service(request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.setErrorMessage(e.getMessage(), request);            
            ServletUtility.forward(getView(), request, response);
        } catch (Exception e) {
            log.error(e);
            ServletUtility.setErrorMessage(e.getMessage(), request);            ServletUtility.forward(getView(), request, response);            
		}
		log.debug("Base Ctl service method end");
	}
	
	protected abstract String getView();
		
}

