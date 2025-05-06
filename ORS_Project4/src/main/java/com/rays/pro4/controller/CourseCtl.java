package com.rays.pro4.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.DTO.CourseDTO;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.validator.CourseValidator;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.controller.ORSView;

import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


@WebServlet(name="CourseCtl", urlPatterns={"/ctl/CourseCtl"})
public class CourseCtl extends BaseCtl<CourseBean>{

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CourseCtl.class);


	/**
	 * The model
	 */
	private final static CourseModel model = new CourseModel();

	/**
	 * Validates input data entered by User.
	 * @param request the request
	 * @return true, if successful
	 */
	@Override
	protected boolean validate(final HttpServletRequest request) {
		log.debug("CourseCtl validate started");
		final boolean pass = CourseValidator.validate(request);
		
		
		log.debug("CourseCtl validate End");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	@Override
    protected CourseBean populateBean(HttpServletRequest request) {
        log.debug("CourseCtl populateBean method start");
        CourseBean bean = new CourseBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        bean.setDuration(DataUtility.getString(request.getParameter("duration")));
        populate(bean, request);
    
        bean.setCreatedDatetime(DataUtility.getCurrentTimestamp());
		
		
		bean.populate(request);
		log.debug("CourseCtl populate end");

	}


	/**
	 * Find by pk.
	 * @param id the id
	 * @return the course bean
	 * @throws ApplicationException the application exception
	 */
	public CourseBean findByPK(final long id) throws ApplicationException {
		CourseBean bean = null;
		bean = model.findByPK(id);
		return bean;
	}



	


	

	/**
	 * Contains Display logics
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 **/
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		log.debug("CourseCtl Method doGet Started");
        String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		CourseBean courseBean = new CourseBean();
        if(id>0 || op != null){
           CourseDTO dto;
            try{
                dto=model.findByPK(id);
                
                courseBean.setId(dto.getId());
                courseBean.setName(dto.getName());
                courseBean.setDescription(bean.getDescription());
                courseBean.setDuration(bean.getDuration());


                ServletUtility.setBean(courseBean, request);


            }catch(ApplicationException e){
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
		else {
			ServletUtility.setBean(courseBean, request);
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("CourseCtl Method doGet Ended");
	}
	
	
    
    
	/**
	 * Contains Submit logics
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CourseCtl Method doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		CourseBean bean = populateBean(request);
		if(validate(request)){
			if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
				try {
	                CourseDTO courseDTO = bean.getDTO();
	                if(id>0){
	                	model.update(courseDTO);
	                	ServletUtility.setSuccessMessage("Course update Successfully", request);
	                }else {
	                	long pk = model.add(courseDTO);
	                	bean.setId(pk);
	                	ServletUtility.setSuccessMessage("Course added Successfully", request);
	                }
	            }catch (ApplicationException e) {
	            	if(e instanceof DuplicateRecordException){
	            		ServletUtility.setErrorMessage("Course already exists", request);
	            	} else ServletUtility.setErrorMessage(PropertyReader.getValue("error.course.duplicate"), request);
	            	ServletUtility.setBean(bean, request);
	            }
				catch (Exception e) {
					ServletUtility.setBean(bean, request);
				}
	          }
		    else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
				return;
			} else if (OP_CANCEL.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
				return;
			}
			ServletUtility.setBean(bean, request);
		}
	   

		ServletUtility.forward(getView(), request, response);
		log.debug("CourseCtl Method doPost Ended");
	}
	@Override
	protected String getView() {
		return ORSView.COURSE_VIEW;
	}
}
