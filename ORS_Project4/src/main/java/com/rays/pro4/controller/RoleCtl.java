package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/**
 *  @author Lokesh SOlanki
 *
 */
@ WebServlet(name="RoleCtl",urlPatterns={"/ctl/RoleCtl"})
public class RoleCtl extends BaseCtl{

	 private static final long serialVersionUID = 1L;	 

	    /** The log. */
	    private static Logger log = Logger.getLogger(RoleCtl.class);
	    private final RoleModel model = new RoleModel();
	    /**
		 * Validates input data entered by User
		 * 
		 * @param request the request
		 *            
		 * @return
		 */
	    
	    @Override
	    protected boolean validate(HttpServletRequest request) {

	        log.debug("RoleCtl Method validate Started");

	        boolean pass = true;

	        if (DataValidator.isNull(request.getParameter("name"))) {	        	
	            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
	            pass = false;
	        } else if (!DataValidator.isName(request.getParameter("name"))) {
	        	 request.setAttribute("name", PropertyReader.getValue(MessageConstant.NAME_ALPHABET));
	             pass = false;	            
			}

	        if (DataValidator.isNull(request.getParameter("description"))) {	        	
	            request.setAttribute("description",
	                    PropertyReader.getValue("error.require", "Description"));
	            pass = false;
	        }

	        log.debug("RoleCtl Method validate Ended");

	        return pass;
	    }

	    /**
		 * Populates bean object from request parameters
		 * 
		 * @param request the request
		 *            
		 * @return
		 */
	    
	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        log.debug("RoleCtl Method populatebean Started");	
	        final RoleBean bean = new RoleBean();	        
	        bean.populate(request);
	        log.debug("RoleCtl Method populatebean Ended");	        
	        return bean;
	    }

	    /**
	     * Contains Display logics.	     
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
	    protected void doGet(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        log.debug("RoleCtl Method doGet Started");

	        final String op = DataUtility.getString(request.getParameter("operation"));	        
	        final long id = DataUtility.getLong(request.getParameter("id"));
	        
	        if (id <= 0) {
	            ServletUtility.setErrorMessage("Invalid Role ID", request);
	            ServletUtility.forward(ORSView.ERROR_VIEW, request, response);
	            return;
	        }
	        if (id > 0 || op != null) {
	            RoleBean bean;
	            try {
	                bean = model.findByPK(id);
	                
	                if(bean == null) {
	                	ServletUtility.setErrorMessage("Role not found", request);
	                }
	                
	                ServletUtility.setBean(bean, request);
	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleException(e, request, response);
	                return;
	            }
	        }
	        ServletUtility.forward(getView(), request, response);
	        log.debug("RoleCtl Method doGetEnded");
	    }

	    /**   
	     * save the role
	     * @param bean the bean
	     * @param model the model
	     * @param request the request
	     * @throws DuplicateRecordException the duplicate record exception
	     * @throws ApplicationException the application exception
	     */
	    private void save(RoleBean bean, RoleModel model, HttpServletRequest request)
	            throws DuplicateRecordException, ApplicationException {
	        log.debug("save method start");
	        model.add(bean);
	        ServletUtility.setSuccessMessage(MessageConstant.ROLE_ADD, request);
	        log.debug("save method end");
	    }

	    /**     
	     * Update the role
	     * @param bean the bean
	     * @param model the model
	     * @param request the request
	     * @throws DuplicateRecordException the duplicate record exception
	     * @throws ApplicationException the application exception
	     */
	    private void update(RoleBean bean, RoleModel model, HttpServletRequest request)
	            throws DuplicateRecordException, ApplicationException {
	        log.debug("update method start");
	        model.update(bean);
	        ServletUtility.setSuccessMessage(MessageConstant.ROLE_UPDATE, request);
	        log.debug("update method end");
	    }

	    
	    /**	     
	     * Contains Submit logics.	     
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        log.debug("RoleCtl Method doPost Started");	        
	        final String op = DataUtility.getString(request.getParameter("operation"));	       
	        final long id = DataUtility.getLong(request.getParameter("id"));

	        if (OP_SAVE.equalsIgnoreCase(op)|| OP_UPDATE.equalsIgnoreCase(op)) {

	            final RoleBean bean = (RoleBean) populateBean(request);

	            try {
	                if (id > 0) {
	                	update(bean, model, request);
	                } else {
	                	save(bean, model, request);	                  
	                }
	                
	                ServletUtility.setSuccessMessage("Role is successfully saved",
	                        request);

	            } catch (final ApplicationException e) {
	                log.error("Application exception in save/update", e);	                
	                handleDatabaseException(e, request, response);
	                return;                
	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Role already exists", request);
	            }
	            
	        } else if (OP_DELETE.equalsIgnoreCase(op)) {	        	

	            RoleBean bean = (RoleBean) populateBean(request);
	            try {
	                model.delete(bean);
	                ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request,
	                        response);
	                return;
	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleException(e, request, response);
	                return;
	            }
	        } else if (OP_CANCEL.equalsIgnoreCase(op)) {	        	

	            ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
	            return;	            
	        }	        

	        ServletUtility.forward(getView(), request, response);

	        log.debug("RoleCtl Method doPOst Ended");
	    

	    /**
		 * Returns the VIEW page of this Controller
		 * 
		 * @return
		 */
	    
	   @Override
	    protected String getView() {
	        return ORSView.ROLE_VIEW;
	    }	
	
}
