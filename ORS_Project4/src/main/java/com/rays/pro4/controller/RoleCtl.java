package com.rays.pro4.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.DTO.RoleDTO;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

import com.rays.pro4.Model.RoleModel;
/**
 *  @author Lokesh SOlanki
 *
 */
@WebServlet(name = "RoleCtl", urlPatterns = { "/ctl/RoleCtl" })
public class RoleCtl extends BaseCtl {

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
	 * Populates RoleDTO object from request parameters
	 * 
	 * @param request the request
	 *            
	 * @return
	 */
	protected RoleDTO populateDTO(HttpServletRequest request) {
		RoleDTO dto = new RoleDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		return dto;
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
			RoleBean bean = new RoleBean();
			bean.setId(DataUtility.getLong(request.getParameter("id")));
			bean.setName(DataUtility.getString(request.getParameter("name")));
			bean.setDescription(DataUtility.getString(request.getParameter("description")));
			bean.setCreatedBy(request.getParameter("createdBy"));
			bean.setModifiedBy(request.getParameter("modifiedBy"));
			bean.setCreatedDatetime(DataUtility.getCurrentTimestamp());
			bean.setModifiedDatetime(DataUtility.getCurrentTimestamp());
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

			String op = DataUtility.getString(request.getParameter("operation"));
			long id = DataUtility.getLong(request.getParameter("id"));
			RoleDTO dto = null;
			if (id <= 0) {
				ServletUtility.setErrorMessage("Invalid Role ID", request);
				ServletUtility.forward(ORSView.ERROR_VIEW, request, response);
				return;
			}
			if (id > 0 || op != null) {
				RoleBean bean;
	            try {	                
	                dto = model.findByPK(id);
					if (dto == null) {
						ServletUtility.setErrorMessage("Role not found", request);
					}
					bean = new RoleBean();
					bean.setId(dto.getId());
					bean.setName(dto.getName());
					bean.setDescription(dto.getDescription());
					ServletUtility.setBean(bean, request);
	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleDatabaseException(e, request, response);
	                return;
	            }
	        }
	        ServletUtility.forward(getView(), request, response);
	        log.debug("RoleCtl Method doGetEnded");
	    }
	    /**
	     * save the role
	     * 
	     * @param bean    the bean
	     * @param model   the model
	     * @param request the request
	     * @throws DuplicateRecordException the duplicate record exception the
	     * @throws ApplicationException the application exception
	     */
	    private void save(RoleBean bean, RoleModel model, HttpServletRequest request)
	            throws DuplicateRecordException, ApplicationException {
	        log.debug("save method start");
	        model.add(bean);
	        log.debug("save method end");
	    }
	    /**
	     * Update the role
	     * 
	     * @param bean    the bean
	     * @param model   the model
	     * @param request the request
	     * @throws DuplicateRecordException the duplicate record exception the
	     * @throws ApplicationException the application exception
	     *                                
	    private void update(RoleBean bean, RoleModel model, HttpServletRequest request)
	            throws DuplicateRecordException, ApplicationException {
	        log.debug("update method start");
	        model.update(bean);
	        log.debug("update method end");
	    }

	    /**
	     * Contains Submit logics.
	     * 
	     * @param request  the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
	    
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        log.debug("RoleCtl Method doPost Started");	        
	        final String op = DataUtility.getString(request.getParameter("operation"));	       
	        final long id = DataUtility.getLong(request.getParameter("id"));

	         RoleBean bean = (RoleBean) populateBean(request);
	         RoleDTO dto=new RoleDTO();

	          if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
	              if (!validate(request)) {
	                  ServletUtility.setBean(bean, request);
	                  ServletUtility.forward(getView(), request, response);
	                  return;
	              }

	              try {
	            	  bean.setName(request.getParameter("name"));
	            	  bean.setDescription(request.getParameter("description"));
	            	  dto = bean.getDTO();
	                  if (id > 0) {	                      
	                      model.update(dto);
	                      ServletUtility.setSuccessMessage(MessageConstant.ROLE_UPDATE, request);
	                  } else {	                	  
	                	  dto.setCreatedBy("root");
	                	  dto.setModifiedBy("root");
	                      model.add(dto);
	                       ServletUtility.setSuccessMessage(MessageConstant.ROLE_ADD, request);
	                  }

	              } catch (final ApplicationException e) {
	                  log.error("Application exception in save/update", e);
	                  handleDatabaseException(e, request, response);
	                  return;
	              } catch (DuplicateRecordException e) {
	                   ServletUtility.setErrorMessage("Role already exists", request);
	                   ServletUtility.setBean(bean, request);
	              }
	          } else if (OP_DELETE.equalsIgnoreCase(op)) {

	            try {
	            	dto.setId(id);
	            	dto.setName(request.getParameter("name"));
	            	dto.setDescription(request.getParameter("description"));
	                model.delete(dto);
	                ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request,
	                        response);
	                return;
	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleDatabaseException(e, request, response);
	                return;
	            }
	        } else if (OP_CANCEL.equalsIgnoreCase(op)) {	        	

	            ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
	            return;	            
	        }	        
             
			ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);

			log.debug("RoleCtl Method doPOst Ended");
	    }


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
