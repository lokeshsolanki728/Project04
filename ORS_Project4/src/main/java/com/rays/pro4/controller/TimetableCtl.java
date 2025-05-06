package com.rays.pro4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.apache.log4j.Logger;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Bean.TimeTableBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.TimeTableModel;
import com.rays.pro4.Model.SubjectModel;

import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;import com.rays.pro4.validator.TimeTableValidator;
import com.rays.pro4.Model.CourseModel;

//TODO: Auto-generated Javadoc

/**
* The Class TimeTableCtl.
**  @author Lokesh SOlanki
*/
import com.rays.pro4.validator.TimeTableValidator;
@WebServlet(name = "TimeTableCtl", urlPatterns = {"/ctl/TimeTableCtl"})
public class TimetableCtl extends BaseCtl{

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The log. */
    private static Logger log = Logger.getLogger(TimetableCtl.class);
    
    private final TimeTableModel model = new TimeTableModel();
    
	/** Loads pre-load data.
	 *
	 * @param request the request
	 */
	@Override protected void preload(HttpServletRequest request) {
		log.debug("preload method of TimetableCtl Started");

		final CourseModel cmodel = new CourseModel();
		final SubjectModel smodel = new SubjectModel();

		try {
			final List<CourseBean> clist = cmodel.list();
			request.setAttribute("CourseList", clist);
		} catch (final ApplicationException e) {
			log.error("Error getting course list during preload", e);
		}

		try {
			final List<SubjectBean> slist = smodel.list();
			request.setAttribute("SubjectList", slist);
		} catch (final ApplicationException e) {
			log.error("Error getting subject list during preload", e);
		}
		log.debug("preload method of TimetableCtl Ended");
	}
	
	/**
	 * Validates input data entered by User.
	 *
	 * @param request the request
	 * @return true, if successful
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
	    log.debug("validate method of TimeTable Ctl started");
	    boolean pass = TimeTableValidator.validate(request);
	   if(!pass){
	        log.debug("TimetableCtl Method validate Ended with error");
	    }
	    return pass;
	}

	
	/**
	 * Populates bean object from request parameters.
	 *
	 * @param request the request
	 * @return the base bean
	 */
	@Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("TimetableCtl Method populatebean Started");
        final TimeTableBean bean = new TimeTableBean();
        bean.populate(request);
        log.debug("TimetableCtl Method populatebean Ended");
        return bean;
    }
	
	/**
	 * Save timetable.
	 *
	 * @param bean the bean
	 * @param model the model
	 * @param request the request
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws ApplicationException the application exception
	 */
    private void save(TimeTableBean bean, TimeTableModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("save method start");
        model.add(bean);
        ServletUtility.setSuccessMessage(MessageConstant.TIMETABLE_ADD, request);
        log.debug("save method end");
    }

    /**
     * Update timetable.
     *
     * @param bean the bean
     * @param model the model
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException the application exception
     */
    private void update(TimeTableBean bean, TimeTableModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("update method start");
        model.update(bean);
        ServletUtility.setSuccessMessage(MessageConstant.TIMETABLE_UPDATE, request);
        log.debug("update method end");
    }
    
    /**
     * Contains Display logics.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("do Get method of TimeTable Ctl Started");
        
        final long id = DataUtility.getLong(request.getParameter("id"));
        
        if (id > 0) {
            TimeTableBean bean;
            try {
                bean = model.findByPK(id);
                if(bean == null){
                    ServletUtility.setErrorMessage(MessageConstant.NOT_FOUND, request);
                }if(bean != null) {
                  ServletUtility.setBean(bean, request);  
                }
            } catch (final ApplicationException e) {
                log.error("Error finding timetable by ID", e);
                handleDatabaseException(e, request, response);
                return;
            }
        }
        log.debug("do Get method of TimeTable Ctl End");
		ServletUtility.forward(getView(), request, response);
	}
    
    /**
     * Contains Submit logics.
     *
     * @param request the request
     * @param response the response 
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.  
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("do post method of TimeTable Ctl start");
        
        final String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            final TimeTableBean bean = (TimeTableBean) populateBean(request);
            try {
                if (id > 0) {
                    update(bean, model, request);
                } else {
                    save(bean, model, request);
                }
                
               ServletUtility.setBean(bean, request);
            } catch (final ApplicationException e) {
                log.error("Application exception", e);
                handleDatabaseException(e, request, response);
                return;
            } catch (final DuplicateRecordException e) {
                
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("TimeTable already exists", request);
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
            return;
        }
        
        ServletUtility.forward(getView(), request, response);
        
	}

	/* (non-Javadoc)
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.TIMETABLE_VIEW;
	}
	
}

