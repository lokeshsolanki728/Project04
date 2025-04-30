package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Bean.FacultyBean;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Model.FacultyModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Exception.ApplicationException;

import com.rays.pro4.Util.ServletUtility;


/**
* The Class FacultyCtl.
*  @author Lokesh SOlanki
*/
@WebServlet(name = "FacultyCtl", urlPatterns = { "/ctl/FacultyCtl" })
public class FacultyCtl extends BaseCtl<FacultyBean>{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(FacultyCtl.class);

	/**
     * Loads pre-load data
     * @param request
     */
	@Override
	protected void preload(HttpServletRequest request) {

		response.setContentType("text/html");
		javax.servlet.http.HttpServletResponse resp = (HttpServletResponse) response;
		CourseModel cmodel = new CourseModel();
		CollegeModel comodel = new CollegeModel();
		SubjectModel smodel = new SubjectModel();

        try {
			java.util.List clist = cmodel.list();			
			request.setAttribute("CourseList", clist);
            java.util.List colist = comodel.list();
			request.setAttribute("CollegeList", colist);
            java.util.List slist = smodel.list();
			request.setAttribute("SubjectList",slist);
		} catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
		}
		try {

			List slist = smodel.list();
			request.setAttribute("SubjectList", slist);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
     * Validates input data entered by User
     *
     * @param request
     * @return
     */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("Faculty Ctl validate Started");
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("firstname"))) {
			request.setAttribute("firstname", PropertyReader.getValue("error.require", "FirstName"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("firstname"))) {
			request.setAttribute("firstname", PropertyReader.getValue("error.name", "First Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("lastname"))) {
			request.setAttribute("lastname", PropertyReader.getValue("error.require", "LastName"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("lastname"))) {
			request.setAttribute("lastname", PropertyReader.getValue("error.name", "Last Name"));
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("loginid"))) {
			request.setAttribute("loginid", PropertyReader.getValue("error.require", "LoginId"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("loginid"))) {
			request.setAttribute("loginid", PropertyReader.getValue("error.email", "LoginId"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobileno"))) {
			request.setAttribute("mobileno", PropertyReader.getValue("error.require", "MobileNo"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("mobileno"))) {	
			request.setAttribute("mobileno", PropertyReader.getValue("error.mobileNo"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("collegeid"))) {
			request.setAttribute("collegeid", PropertyReader.getValue("error.require", "CollegeName"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("courseid"))) {
			request.setAttribute("courseid", PropertyReader.getValue("error.require", "CourseName"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("subjectid"))) {
			request.setAttribute("subjectid", PropertyReader.getValue("error.require", "SubjectName"));
			pass = false;
		}

		log.debug("validate Ended");
		return pass;
	}	
	
	/**
     * Populates bean object from request parameters
     *
     * @param request
     * @return
     */
	@Override
	protected FacultyBean populateBean(HttpServletRequest request) {
		log.debug("populate bean faculty ctl started");
		FacultyBean bean = new FacultyBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstname")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastname")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setEmailId(DataUtility.getString(request.getParameter("loginid")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileno")));		
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeid")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseid")));
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectid")));


		populateDTO(bean, request);
		log.debug("populate bean faculty ctl Ended");
		return bean;
	}

	/**
	 * Contains Display logics.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Do get of faculty ctl Started");
		String op = DataUtility.getString(request.getParameter("operation"));

		FacultyModel model = new FacultyModel();
		Long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 || op != null) {
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean((BaseBean) bean, request);

			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
        log.debug("Do get of  faculty ctl Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Contains Submit logics.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Do post of  faculty ctl Started");

		String op = DataUtility.getString(request.getParameter("operation"));		
		long id = DataUtility.getLong(request.getParameter("id"));		
		FacultyModel model = new FacultyModel();
		FacultyBean bean = populateBean(request);
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {	
			try {
				if(OP_SAVE.equalsIgnoreCase(op)){
					bean.setId(0);
				}
				if (id > 0) {	
					model.update(bean);
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.faculty.update"), request);
				} else {
					model.add(bean);
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.faculty.add"), request);
				}
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;

			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage(PropertyReader.getValue("error.faculty.duplicate"), request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		}
		else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);		
			return;
		}
		ServletUtility.setBean((BaseBean) bean, request);
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
}
