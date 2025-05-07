package com.rays.pro4.controller;

import java.io.IOException;import java.util.HashMap;
import java.util.Date;
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
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Bean.FacultyBean;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.Model.FacultyModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.validator.FacultyValidator;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.ORSView;

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
		final CourseModel courseModel = new CourseModel();		
		final CollegeModel comodel = new CollegeModel();
	    final SubjectModel smodel = new SubjectModel();

 
        
		try {
			List<CourseBean> clist = courseModel.list();
			request.setAttribute("CourseList", clist);
			List<CollegeBean> colist = comodel.list();
			request.setAttribute("CollegeList", colist);
			List<SubjectBean> slist = smodel.list();
			request.setAttribute("SubjectList", slist);
		} catch (DatabaseException e) {
            log.error("Database exception in preload", e);
            ServletUtility.handleException(e, request, response);
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
	
	protected FacultyBean populateBean(final HttpServletRequest request, FacultyBean bean){
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));        
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
        bean.setEmailId(DataUtility.getString(request.getParameter("emailId")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        if (bean.getId() == 0) {
            bean.setCreatedDatetime(DataUtility.getCurrentTimestamp());
        }
        bean.setModifiedDatetime(DataUtility.getCurrentTimestamp());

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
		log.debug("doGet method start");
		final long id = DataUtility.getLong(request.getParameter("id"));		
		
		if (id > 0) {
			FacultyDTO dto = null;
			try {
				dto = model.findByPK(id);
				if(dto == null) {
					ServletUtility.setErrorMessage("Faculty not found", request);
					ServletUtility.forward(getView(), request, response);
					return;
				}		
				FacultyBean bean = populateBean(dto, new FacultyBean());	
				ServletUtility.setBean(bean, request);
			} catch (final ApplicationException e) {
				log.error("ApplicationException in doGet", e);
                ServletUtility.handleException(e, request, response);
				return;
			}
		}
        log.debug("doGet of  faculty ctl Ended");
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
		log.debug("doPost method start");
        final String op = DataUtility.getString(request.getParameter("operation"));
        final long id = DataUtility.getLong(request.getParameter("id"));

        if (!validate(request)) {
            ServletUtility.forward(getView(), request, response);
            return;
        }
        FacultyBean bean = new FacultyBean();
        bean.setId(id);

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            try {
                if (id > 0) {
                    FacultyDTO oldDto = model.findByPK(id);
                    if (oldDto == null) {
                        ServletUtility.setErrorMessage("Faculty not found", request);
                        ServletUtility.forward(getView(), request, response);
                        return;
                    }
                    bean.setCreatedBy(oldDto.getCreatedBy());
                    bean.setModifiedBy(oldDto.getModifiedBy());
                    bean.setCreatedDatetime(oldDto.getCreatedDatetime());

                    bean = populateBean(request, bean);
                    FacultyDTO dto = bean.getDTO();
                    model.update(dto);
                    ServletUtility.setSuccessMessage(MessageConstant.FACULTY_UPDATE_SUCCESS, request);
                } else {
                    bean = populateBean(request, bean);
                    FacultyDTO dto = bean.getDTO();
                    model.add(dto);
                    ServletUtility.setSuccessMessage(MessageConstant.FACULTY_ADD_SUCCESS, request);
                }
            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException in doPost", e);
                ServletUtility.setErrorMessage(PropertyReader.getValue("error.faculty.duplicate"), request);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException | RecordNotFoundException e) {
                log.error("ApplicationException in doPost", e);
                ServletUtility.handleException(e, request, response);
            }
        }else if (OP_CANCEL.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);		
				return;
		}else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
				return;
		}
		ServletUtility.setBean(bean, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("doPost of  faculty ctl Ended");	
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
	 * @param dto the dto
	 * @param bean the bean
	 */
	protected FacultyBean populateBean(FacultyDTO dto, FacultyBean bean) {
		bean.setId(dto.getId());		
		bean.setFirstName(dto.getFirstName());
		bean.setLastName(dto.getLastName());
		bean.setCollegeId(dto.getCollegeId());
		bean.setCourseId(dto.getCourseId());
		bean.setSubjectId(dto.getSubjectId());
		bean.setEmailId(dto.getEmailId());
		bean.setMobileNo(dto.getMobileNo());
		bean.setCreatedDatetime(dto.getCreatedDatetime());
		bean.setModifiedDatetime(dto.getModifiedDatetime());
		return bean;
	}
