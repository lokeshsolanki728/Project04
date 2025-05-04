package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.DTO.RoleDTO;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

import com.rays.pro4.Model.RoleModel;
/**
 * Role functionality controller. to perform add,delete and update operation
 *  @author Lokesh SOlanki
 *
 */

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

	@Override
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
	    	List list = null;
	        int pageNo = 1;
	        int pageSize = 10;
	        RoleBean bean = (RoleBean) populateBean(request);

	        String op = com.rays.pro4.Util.DataUtility.getString(request.getParameter("operation"));
	         
	        try {
				list = model.search(bean, pageNo, pageSize);
			} catch (ApplicationException e1) {
				e1.printStackTrace();
				 log.error("Database Exception in RoleCtl.doGet" + e1.getMessage());
			}
	        if(list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("Record not Found", request);
				 ServletUtility.forward(getView(), request, response);
			} else {
				request.setAttribute("list", list);
				ServletUtility.forward(getView(), request, response);
			}
			
			 try {
				 
				RoleDTO dto= new RoleDTO();
	                dto = model.findByPK(bean.getId());
	               if(dto!=null)
	               ServletUtility.setBean(dto.getRoleBean(), request);
	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleDatabaseException(e, request, response);
	                return;
	            }
	        }
	        ServletUtility.forward(getView(), request, response);
	         
	        log.debug("RoleCtl Method doGetEnded");
	    }
	  
	    @Override
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        log.debug("RoleCtl Method doPost Started");	        
	        final String op = DataUtility.getString(request.getParameter("operation"));	       

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
	            	  if (bean.getId() > 0) {
							model.update(dto);
							ServletUtility.setSuccessMessage("Role Successfully Updated", request);
							ServletUtility.forward(getView(), request, response);
							return;
						} else {
							dto.setCreatedBy("root");
							dto.setModifiedBy("root");
							model.add(dto);
							ServletUtility.setSuccessMessage("Role Successfully Added", request);
							ServletUtility.forward(getView(), request, response);
							return;
						}
	              } catch (com.rays.pro4.Exception.DuplicateRecordException e) {
	            	    ServletUtility.setErrorMessage("Role already exists", request);
						ServletUtility.forward(getView(), request, response);
	                return;
	              }
	              catch (final ApplicationException e) {
	                  log.error("Application exception in save/update", e);
	                  handleDatabaseException(e, request, response);
	                  return;
	              } 
	          } else if (OP_DELETE.equalsIgnoreCase(op)) {

	            try {
	            	model.delete(bean);
	            	 ServletUtility.setSuccessMessage("Role Successfully Deleted", request);
	                ServletUtility.redirect(ORSView.ROLE_LIST_VIEW, request,
	                        response);
	                return;
	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleDatabaseException(e, request, response);
	                return;
	            }
	        } else if (OP_CANCEL.equalsIgnoreCase(op)) {	        	

	            ServletUtility.redirect(ORSView.ROLE_LIST_VIEW, request, response);
	            return;	            
	        } else {	                	  
	                	  dto.setCreatedBy("root");
	                	  dto.setModifiedBy("root");
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
	   @Override
		protected void preload(HttpServletRequest request) {
			request.setAttribute("roleList", model.list());
		}

}
