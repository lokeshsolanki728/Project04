package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.CourseBean;
import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.Util.SubjectValidator;

/**
* The Class SubjectCtl.
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
	 * Preload.
	 *
	 * @param request the request
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("preload method of SubjectCtl Started");
		final CourseModel courseModel = new CourseModel();
		try {
			final List<CourseBean> courseList = courseModel.list();
			request.setAttribute("CourseList", courseList);
		} catch (final ApplicationException e) {
			log.error("Error getting course list during preload", e);
		}
		log.debug("preload method of SubjectCtl Ended");
	}

	/**
	 * Validate.
	 *
	 * @param request the request
	 * @return true, if successful
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("validate Method of Subject Ctl start");
		boolean pass = SubjectValidator.validate(request);
		if(!pass){
			return false;
		}
		log.debug("validate Method of Subject Ctl  End");
		return pass;
	}

	/**
	 * Populate bean.
	 *
	 * @param request the request
	 * @return the base bean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("Populate bean Method of Subject Ctl start");
		final SubjectBean bean = new SubjectBean();
		bean.populate(request);
		log.debug("PopulateBean Method of Subject Ctl End");
		return bean;
	}

	/**
	 * Do get.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("Do get Method of Subject Ctl start ");
		final long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0) {
			SubjectBean bean;
			try {
				bean = model.findByPK(id);
				if (bean == null) {
					ServletUtility.setErrorMessage("Subject not found", request);
				}
				ServletUtility.setBean(bean, request);
			} catch (final ApplicationException e) {
				log.error("Error finding Subject by ID", e);
				handleDatabaseException(e, request, response);
				return;
			}
		}
		log.debug("Do get Method of Subject Ctl End");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Save.
	 *
	 * @param bean the bean
	 * @param model the model
	 * @param request the request
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws ApplicationException the application exception
	 */
	private void save(SubjectBean bean, SubjectModel model, HttpServletRequest request) throws DuplicateRecordException, ApplicationException {
		log.debug("save method start");
		model.add(bean);
		ServletUtility.setSuccessMessage(MessageConstant.SUBJECT_ADD, request);
		log.debug("save method end");
	}

	/**
	 * Update.
	 *
	 * @param bean the bean
	 * @param model the model
	 * @param request the request
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws ApplicationException the application exception
	 */
	private void update(SubjectBean bean, SubjectModel model, HttpServletRequest request) throws DuplicateRecordException, ApplicationException {
		log.debug("update method start");
		model.update(bean);
		ServletUtility.setSuccessMessage(MessageConstant.SUBJECT_UPDATE, request);
		log.debug("update method end");
	}

	/**
	 * Do post.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("Do post Method of Subject Ctl start");
		final String op = DataUtility.getString(request.getParameter("operation"));
		final long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			final SubjectBean bean = (SubjectBean) populateBean(request);
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
				ServletUtility.setErrorMessage("Subject name already exists", request);
			}
			ServletUtility.setBean(bean, request);
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("Do post Method of Subject Ctl End");
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	@Override
	protected String getView() {
		return ORSView.SUBJECT_VIEW;
	}
}
