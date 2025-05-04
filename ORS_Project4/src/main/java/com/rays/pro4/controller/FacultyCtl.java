package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.DTO.FacultyDTO;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Bean.FacultyBean;
import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.Model.FacultyModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.Util.FacultyValidator;
import com.rays.pro4.Exception.DuplicateRecordException;

/**
* The Class FacultyCtl.
*  @author Lokesh SOlanki
*/
@WebServlet(name = "FacultyCtl", urlPatterns = {"/ctl/FacultyCtl"})
public class FacultyCtl extends BaseCtl<FacultyBean>{


    /** The log. */	
	private static Logger log = Logger.getLogger(FacultyCtl.class);

	/**
	 * Model instance for performing database operations
	 */
	private final FacultyModel model = new FacultyModel();

	/**
     * Loads pre-load data
     * @param request
     */	
	@Override
	protected void preload(final HttpServletRequest request) {
		final com.rays.pro4.Model.CourseModel courseModel = new com.rays.pro4.Model.CourseModel();
		final com.rays.pro4.Model.CollegeModel comodel = new com.rays.pro4.Model.CollegeModel();
	    final SubjectModel smodel = new SubjectModel();


        
		try {
			List<CourseBean> clist = courseModel.list();
			request.setAttribute("CourseList", clist);
			List colist = comodel.list();
			request.setAttribute("CollegeList", colist);
			List<SubjectBean> slist = smodel.list();
			request.setAttribute("SubjectList", slist);
		} catch (final Exception e) {
			log.error("Error in preload method : ", e);
			handleDatabaseException(e, request, response);
		}
	}


	
	/**
     * Validates input data entered by the user.
     * @param request
     * @return
     */
	@Override
	protected boolean validate(final HttpServletRequest request) {
		log.debug("validate method start");
		final boolean pass = FacultyValidator.validate(request);
		log.debug("validate method end");
		return pass;
	}

	/**
     * Populates bean object from request parameters
	 * @param request the request
	 * @return the faculty bean
	 * @throws DuplicateRecordException
	 *             the duplicate record exception
	 * @throws ApplicationException the application exception
     */
	
	protected void populateBean(final HttpServletRequest request, FacultyBean bean){
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
        bean.setEmailId(DataUtility.getString(request.getParameter("emailId")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
    }
	@Override
	protected FacultyBean populate(final HttpServletRequest request) {
		log.debug("FacultyCtl populate Start");		
		final FacultyBean bean = new FacultyBean();		
		bean.populate(request);
		return bean;
	}

	/**
	 * Contains Display logics.
	 * @param request the request
	 * @param response
	 *            the response
	 * @throws ServletException the servlet exception
	 *            
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do get method start");
		final long id = DataUtility.getLong(request.getParameter("id"));		
		
		if (id > 0) {
			final FacultyBean bean;
			FacultyDTO dto;
			try {
				dto = model.findByPK(id);
				if(dto == null) {
					ServletUtility.setErrorMessage("Faculty not found", request);
					ServletUtility.forward(getView(), request, response);
					return;
				}
				bean = new FacultyBean();
				populateBean(dto,bean);
					return;
				}
				ServletUtility.setBean(bean, request);
				
			} catch (final Exception e) {
				log.error("Error finding faculty by ID : ", e);
				handleDatabaseException(e, request, response);
				return;
			}
		}
        log.debug("Do get of  faculty ctl Ended");
		ServletUtility.forward(getView(), request, response);
	}	

	/**	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do post method start");

		final String op = DataUtility.getString(request.getParameter("operation"));		
		final long id = DataUtility.getLong(request.getParameter("id"));		
		final FacultyBean bean = new FacultyBean();
		final FacultyDTO dto = new FacultyDTO();
		populateBean(request, bean);		

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            if (validate(request)) {
                try {
                    if (id > 0) {
                    	dto=bean.getDTO();
                        model.update(dto);
                        ServletUtility.setSuccessMessage(MessageConstant.FACULTY_UPDATE_SUCCESS, request);
                    } else {
                        model.add(bean);
                        ServletUtility.setSuccessMessage(MessageConstant.FACULTY_ADD_SUCCESS, request);
                    }
                } catch (ApplicationException | DuplicateRecordException e) {
                    log.error("Error in save method : ", e);
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.faculty.duplicate"), request);
                }
            } else {
                ServletUtility.forward(getView(), request, response);
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);		
				return;
		}else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
				return;
		}		
		ServletUtility.setBean(bean, request);		
		ServletUtility.forward(getView(), request, response);
		log.debug("Do post of  faculty ctl Ended");	
	}
	
	/**
     * Returns the VIEW page of this Controller     
     *
     * @return
     */
	@Override
	protected String getView() {
		return ORSView.FACULTY_VIEW;
	}

    /**
	 * Populates DTO object from bean
	 * 
	 * @param request
	 * @return
	 */
	protected void populateBean(FacultyDTO dto, FacultyBean bean) {
		bean.setId(dto.getId());
        bean.populate(dto);
	}
}
