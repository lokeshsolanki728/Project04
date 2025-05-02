package com.rays.pro4.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.CourseValidator;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import java.io.IOException;

/**
 * The Class CourseCtl.
 * @author Lokesh Solanki
 */
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
	protected final CourseBean populateBean(final HttpServletRequest request){
		log.debug("CourseCtl populate started");
		final CourseBean bean = new CourseBean();		
		bean.populate(request);
		log.debug("CourseCtl PopulatedBean End");
		return bean;
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
	 * Save Course.
	 *
	 * @param bean the bean
	 * @param request the request
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws ApplicationException the application exception
	 */
    private void save(CourseBean bean, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("save method start");
        
        model.add(bean);
        ServletUtility.setSuccessMessage(MessageConstant.COURSE_ADD, request);
        
        log.debug("save method end");
    }
    /**
     * Update Course.
     *
     * @param bean the bean
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException the application exception
     */
    private void update(CourseBean bean, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("update method start");
        
        model.update(bean);
        ServletUtility.setSuccessMessage(MessageConstant.COURSE_UPDATE, request);
        
        log.debug("update method end");
    }

	

	/**
	 * Contains Display logics
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		log.debug("CourseCtl Method doGet Started");
		long id = DataUtility.getLong(request.getParameter("id"));
		
        if (id <= 0) {
            ServletUtility.setErrorMessage("Invalid Course ID", request);
            ServletUtility.forward(ORSView.ERROR_VIEW, request, response);
            return;
        }
		if (id > 0) {
			try {
                final CourseBean bean = findByPK(id);
				if (bean == null) {
					ServletUtility.setErrorMessage("Course not found", request);
				}				
				ServletUtility.setBean(bean, request);
			} catch (final ApplicationException e) {
				handleDatabaseException(e, request, response);
				return;
			}	
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
		final CourseBean bean = populateBean(request);
		
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
	        if (validate(request)) {
	            try {
	                if (bean.getId() > 0) {
	                    update(bean, request);
	                } else {
	                    save(bean, request);
	                }
	            } catch (final ApplicationException e) {
	                handleDatabaseException(e, request, response);
	                return;
	            } catch (final DuplicateRecordException e) {
	                ServletUtility.setErrorMessage(PropertyReader.getValue("error.course.duplicate"), request);
	            }
	            ServletUtility.setBean(bean, request);
	        }
	    } else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {			
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("CourseCtl Method doPost Ended");	
	}
	@Override
	protected String getView() {
		return ORSView.COURSE_VIEW;
	}
}
