package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.DTO.SubjectDTO;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.validator.SubjectValidator;

/**
*  Subject functionality Controller. Performs operation for add, update, delete
* 
*  @author Lokesh SOlanki
* 
*/
@WebServlet (name = "SubjectCtl" , urlPatterns = {"/ctl/SubjectCtl"})
public class SubjectCtl extends BaseCtl{

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SubjectCtl.class);

	private final SubjectModel model = new SubjectModel();
	
	
	/**
     * Populates bean object from request parameters.
     *
     * @param request the request
     * @return the base bean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.debug("populateBean method start");
        SubjectBean bean = new SubjectBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setSubjectName(DataUtility.getString(request.getParameter("subjectName")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        log.debug("populateBean method end");
        return bean;
    }
	/**
	   *
	   * @param request the request
	   */
	   @Override
	   protected void preload(HttpServletRequest request) {
	       log.debug("preload method of SubjectCtl Started");
	       CourseModel courseModel = new CourseModel();
	       try {
	           List<CourseBean> courseList = courseModel.list();
	           request.setAttribute("CourseList", courseList);
	       } catch (ApplicationException e) {
	           log.error("Error getting course list during preload", e);
	       }
	       log.debug("preload method of SubjectCtl Ended");
	   }
	
	  
	   /**
	    * Contains display logic.
	    *
	    * @param request the request
	    * @param response the response
	    * @throws ServletException the servlet exception
	    * @throws IOException Signals that an I/O exception has occurred.
	    */
	   @Override
	   protected void doGet(HttpServletRequest request, HttpServletResponse response)
	           throws ServletException, IOException {
	       log.debug("doGet method of Subject Ctl start");
	       String op = DataUtility.getString(request.getParameter("operation"));
	       long id = DataUtility.getLong(request.getParameter("id"));
	       SubjectModel model = new SubjectModel();
	       if (id > 0 || op != null) {
	           SubjectDTO bean;
	           try {
	               bean = model.findByPK(id);
	               ServletUtility.setBean(bean, request);
	           } catch (ApplicationException e) {
	               log.error("Application exception", e);
	               ServletUtility.handleException(e, request, response);
	               return;
	           }
	       }
	       ServletUtility.forward(getView(), request, response);
	       log.debug("doGet method of Subject Ctl End");
	   }
	
	
	
	   /**
	    * Contains Submit logics.
	    *
	    * @param request the request
	    * @param response the response
	    * @throws ServletException the servlet exception
	    * @throws IOException Signals that an I/O exception has occurred.
	    */
	   @Override
	   protected void doPost(HttpServletRequest request, HttpServletResponse response)
	           throws ServletException, IOException {
	       log.debug("doPost method of Subject Ctl start");
	       String op = DataUtility.getString(request.getParameter("operation"));
	       long id = DataUtility.getLong(request.getParameter("id"));
	       SubjectModel model = new SubjectModel();
	       if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
	           SubjectBean bean = (SubjectBean) populateBean(request);
	           try {
	        	    SubjectDTO dto = bean.getDTO();
	               if (id > 0) {
	                   model.update(dto);
	                   ServletUtility.setSuccessMessage("Data is successfully updated", request);
	               } else {
	                   long pk = model.add(dto);
	                   bean.setId(pk);
	                   ServletUtility.setSuccessMessage("Data is successfully saved", request);
	               }
	           } catch (ApplicationException e) {
	               log.error("Application exception", e);
	               ServletUtility.handleException(e, request, response);
	               return;
	           } catch (DuplicateRecordException e) {
	               ServletUtility.setBean(bean, request);
	               ServletUtility.setErrorMessage("Subject Name already exists", request);
	           }
	       } else if (OP_DELETE.equalsIgnoreCase(op)) {
	           SubjectBean bean = (SubjectBean) populateBean(request);
	           try {
	        	    SubjectDTO dto = bean.getDTO();
	               model.delete(dto.getId());
	               ServletUtility.redirect(ORSView.SUBJECT_LIST_VIEW, request, response);
	               return;
	           } catch (ApplicationException e) {
	               log.error("Application exception", e);
	               ServletUtility.handleException(e, request, response);
	               return;
	           }
	       } else if (OP_RESET.equalsIgnoreCase(op)) {
	           ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
	           return;
	       } else if (OP_CANCEL.equalsIgnoreCase(op)) {
	           ServletUtility.redirect(ORSView.SUBJECT_LIST_VIEW, request, response);
	           return;
	       }
	       ServletUtility.forward(getView(), request, response);
	       log.debug("doPost method of Subject Ctl End");
	   }
	
	   /**
	    * Returns the VIEW page of this Controller.
	    *
	    * @return the view
	    */
	   @Override
	   protected String getView() {
	       return ORSView.SUBJECT_VIEW;
	   }
}
