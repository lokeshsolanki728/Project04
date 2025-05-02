package com.rays.pro4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.rays.pro4.validator.CollegeValidator;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Util.CollegeValidator;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/** 
 * College functionality Controller. Performs operation for add, update, delete
 * and get College
 *
 * @author Lokesh SOlanki
 *
 */\n\n@WebServlet(name = \"CollegeCtl\", urlPatterns = { \"/ctl/CollegeCtl\" })\npublic class CollegeCtl extends BaseCtl {\n\n\tprivate static final long serialVersionUID = 1L;\n\n\t/** The logger. */\n\tprivate static final Logger log = Logger.getLogger(CollegeCtl.class);\n\n\tprivate final com.rays.pro4.Model.CollegeModel model = new com.rays.pro4.Model.CollegeModel();\n\n\t/**\n\t * Validates input data entered by User\n\t *\n\t * @param request the request\n\t * @return true, if successful\n\t */\n\t@Override\n\tprotected boolean validate(final HttpServletRequest request) {\n\t\tlog.debug(\"CollegeCtl Method validate Started\");\n\t\tfinal boolean pass = CollegeValidator.validate(request);\n\n\t\tlog.debug(\"CollegeCtl Method validate Ended\");\n\t\treturn pass;\n\t}\n\n\t/**\n\t * Populates bean object from request parameters.\n\t *\n\t * @param request the request\n\t * @return the college bean\n\t */\n\t@Override\n\tprotected BaseBean populateBean(final HttpServletRequest request) {\n\n 		\n\t\t\n 		log.debug(\"CollegeCtl Method populateBean Started\");\n\n\t\tfinal CollegeBean bean = new CollegeBean();\n\t\tbean.populate(request);\n\n\t\tlog.debug(\"CollegeCtl Method populateBean Ended\");\n\t\treturn bean;\n\t}\n\n\t\n\n\t/**\n\t * Contains display logic. This method handle when the user want to get a\n\t * existing college or if the user want to go on the add form\n\t *\n\t * @param request  the request\n\t * @param response the response\n\t * @throws ServletException the servlet exception\n\t * @throws IOException      Signals that an I/O exception has occurred.\n\t */\n\t@Override\n\tprotected void doGet(final HttpServletRequest request, final HttpServletResponse response)\n\t\t\tthrows ServletException, IOException {\n\n\t\tlog.debug(\"CollegeCtl Method doGet Started\");\n\n\t\tfinal long id = DataUtility.getLong(request.getParameter(\"id\"));\n\n\t\tif (id > 0) {\n\t\t\tCollegeBean bean;\n\t\t\ttry {\n\t\t\t\tbean = model.findByPK(id);\n\t\t\t\tif (bean == null) {\n\t\t\t\t\tServletUtility.setErrorMessage(\"College not found\", request);\n\t\t\t\t}\n\t\t\t\tServletUtility.setBean(bean, request);\n\n\t\t\t} catch (final ApplicationException e) {\n\t\t\t\tlog.error(\"Error finding college by ID\", e);\n\t\t\t\thandleDatabaseException(e, request, response);\n\t\t\t\treturn;\n\t\t\t}\n\t\t}\n\t\tServletUtility.forward(getView(), request, response);\n\t\tlog.debug(\"CollegeCtl Method doGet Ended\");\n\t}\n\n\t/**\n\t * Save college.\n\t *\n\t * @param bean the bean\n\t * @param model the model\n\t * @param request the request\n\t * @throws DuplicateRecordException the duplicate record exception\n\t * @throws ApplicationException     the application exception\n\t */\n\tprivate final void saveCollege(final CollegeBean bean, final HttpServletRequest request)\n\t\t\tthrows DuplicateRecordException, ApplicationException {\n\t\tlog.debug(\"saveCollege method start\");\n\n\t\tfinal long pk = model.add(bean);\n\t\tServletUtility.setSuccessMessage(com.rays.pro4.Util.MessageConstant.COLLEGE_ADD, request);\n\t\tbean.setId(pk);\n\n\t\tlog.debug(\"saveCollege method end\");\n\t}\n\n\t/**\n\t * Update college.\n\t *\n\t * @param bean    the bean\n\t * @param model   the model\n\t * @param request the request\n\t * @throws DuplicateRecordException the duplicate record exception\n\t * @throws ApplicationException     the application exception\n\t */\n\tprivate final void updateCollege(final CollegeBean bean, final HttpServletRequest request)\n\t\t\tthrows DuplicateRecordException, ApplicationException {\n\t\tlog.debug(\"updateCollege method start\");\n\n\t\tmodel.update(bean);\n\t\tServletUtility.setSuccessMessage(com.rays.pro4.Util.MessageConstant.COLLEGE_UPDATE, request);\n\n\t\tlog.debug(\"updateCollege method end\");\n\t}\n\n\t/**\n\t * Contains Submit logics. this method allow user to save update, delete or\n\t * cancel and reset the form Contains Submit logics.\n\t *\n\t * @param request  the request\n\t * @param response the response\n\t * @throws ServletException the servlet exception\n\t * @throws IOException      Signals that an I/O exception has occurred.\n\t */\n\t@Override\n\tprotected void doPost(final HttpServletRequest request, final HttpServletResponse response)\n\t\t\tthrows ServletException, IOException {\n\n\t\tlog.debug(\"CollegeCtl Method doPost Started\");\n\n\t\tfinal String op = DataUtility.getString(request.getParameter(\"operation\"));\n\n\t\tfinal long id = DataUtility.getLong(request.getParameter(\"id\"));\n\n\t\tfinal CollegeBean bean = (CollegeBean) populateBean(request);\n\n\t\tif (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {\n\t\t\ttry {\n\t\t\t\tif (id > 0) {\n\t\t\t\t\tupdateCollege(bean, request);\n\t\t\t\t} else {\n\t\t\t\t\tsaveCollege(bean, request);\n\t\t\t\t}\n\t\t\t\tServletUtility.setBean(bean, request);\n\t\t\t\t\n\t\t\t} catch (final ApplicationException e) {\n\t\t\t\tlog.error(\"Application exception\", e);\n\t\t\t\thandleDatabaseException(e, request, response);\n\t\t\t\treturn;\n\n\t\t\t} catch (final DuplicateRecordException e) {\n\t\t\t\tServletUtility.setBean(bean, request);\n\t\t\t\tServletUtility.setErrorMessage(PropertyReader.getValue(\"error.college.exist\"), request);\n\t\t\t}\n\t\t} else if (OP_CANCEL.equalsIgnoreCase(op)) {\n\t\t\tServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);\n\t\t\treturn;\n\t\t} else if(OP_RESET.equalsIgnoreCase(op)){\n\t\t\tServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);\n\t\t\treturn;\n\t\t}\n\n\t\tServletUtility.forward(getView(), request, response);\n\n\t\tlog.debug(\"CollegeCtl Method doPost Ended\");\n\t}\n\n\t\n\n\t/**\n\t * Returns the VIEW page of this Controller.\n\t *\n\t * @return the string\n\t */\n\t@Override\n\tprotected String getView() {\n\t\treturn ORSView.COLLEGE_VIEW;\n\t}\n\n\t@Override\n    protected void preload(HttpServletRequest request) {\n       \n    }\n\n}\n", "status": "succeeded"}
package com.rays.pro4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.validator.CollegeValidator;

/**
 * College functionality Controller. Performs operation for add, update, delete
 * and get College
 *
 * @author Lokesh SOlanki
 *
 */

@WebServlet(name = "CollegeCtl", urlPatterns = { "/ctl/CollegeCtl" })
public class CollegeCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger log = Logger.getLogger(CollegeCtl.class);

	private final CollegeModel model = new CollegeModel();

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("CollegeCtl Method validate Started");

		boolean pass = true;
		if (OP_DELETE.equalsIgnoreCase(DataUtility.getString(request.getParameter("operation")))) {
			return true;
		}
		pass = CollegeValidator.validate(request);
		log.debug("CollegeCtl Method validate Ended");
		return pass;
	}

	/**
	 * Populates bean object from request parameters.
	 *
	 * @param request the request
	 * @return the college bean
	 */
	@Override
	protected BaseBean populateBean(final HttpServletRequest request) {

		log.debug("CollegeCtl Method populateBean Started");

		final CollegeBean bean = new CollegeBean();
		bean.populate(request);

		log.debug("CollegeCtl Method populateBean Ended");
		return bean;
	}

	/**
	 * Contains display logic. This method handle when the user want to get a
	 * existing college or if the user want to go on the add form
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CollegeCtl Method doGet Started");

		final long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {
			CollegeBean bean;
			try {
				bean = model.findByPK(id);
				if (bean == null) {
					ServletUtility.setErrorMessage("College not found", request);
				}
				ServletUtility.setBean(bean, request);

			} catch (final ApplicationException e) {
				log.error("Error finding college by ID", e);
				handleDatabaseException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("CollegeCtl Method doGet Ended");
	}

	/**
	 * Save college.
	 *
	 * @param bean the bean
	 * @param model the model
	 * @param request the request
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws ApplicationException     the application exception
	 */
	private final void saveCollege(final CollegeBean bean, final HttpServletRequest request)
			throws DuplicateRecordException, ApplicationException {
		log.debug("saveCollege method start");

		final long pk = model.add(bean);
		ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_ADD, request);
		bean.setId(pk);

		log.debug("saveCollege method end");
	}

	/**
	 * Update college.
	 *
	 * @param bean    the bean
	 * @param model   the model
	 * @param request the request
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws ApplicationException     the application exception
	 */
	private final void updateCollege(final CollegeBean bean, final HttpServletRequest request)
			throws DuplicateRecordException, ApplicationException {
		log.debug("updateCollege method start");

		model.update(bean);
		ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_UPDATE, request);

		log.debug("updateCollege method end");
	}

	/**
	 * Contains Submit logics. this method allow user to save update, delete or
	 * cancel and reset the form Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CollegeCtl Method doPost Started");

		final String op = DataUtility.getString(request.getParameter("operation"));

		final long id = DataUtility.getLong(request.getParameter("id"));
		CollegeBean bean= (CollegeBean) populateBean(request);

		if (validate(request)) {
			if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
				try {
					if (id > 0) {
						updateCollege(bean, request);
					} else {
						saveCollege(bean, request);
					}
					ServletUtility.setBean(bean, request);

				} catch (final ApplicationException e) {
					log.error("Application exception", e);
					handleDatabaseException(e, request, response);
					return;

				} catch (final DuplicateRecordException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage(PropertyReader.getValue("error.college.exist"), request);
				}
			} else if (OP_CANCEL.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
				return;
			}
		

		ServletUtility.forward(getView(), request, response);
		} else {
			ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
		}
		log.debug("CollegeCtl Method doPost Ended");
	}

	/**
	 * Returns the VIEW page of this Controller.
	 *
	 * @return the string
	 */
	@Override
	protected String getView() {
		return ORSView.COLLEGE_VIEW;
	}

	@Override
	protected void preload(HttpServletRequest request) {

	}

}

	}

	
	/**
	 * Returns the VIEW page of this Controller.
	 *
	 * @return the string
	 */
	@Override
	protected String getView() {
		return ORSView.COLLEGE_VIEW;
	}
	
    @Override
    protected void preload(HttpServletRequest request) {\n       \n    }\n\n}
		log.debug("CollegeCtl Method populate Started");
		final CollegeBean bean = new CollegeBean();		
		bean.populate(request);		
		log.debug("CollegeCtl Method populate Ended");
		return bean;
	} 

	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the college bean
	 * @throws ApplicationException the application exception
	 */
	private final CollegeBean findByPK(final long id) throws ApplicationException {
		CollegeBean bean = null;
		try {
			bean = model.findByPK(id);
		} catch (final DatabaseException e) {
			throw new ApplicationException(e.getMessage());
		}
		return bean;
	}



	/**
	 * Contains Display logics. 
	 * This method handle when the user want to get a existing college or if the 
	 * user want to go on the add form
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CollegeCtl Method doGet Started");
		final long id = DataUtility.getLong(request.getParameter("id"));		
		if (id > 0) {
			try {
				final CollegeBean bean = findByPK(id);
				if(bean == null){
					ServletUtility.setErrorMessage("College not found", request);
				}
				ServletUtility.setBean(bean, request);
			} catch (final ApplicationException e) {
				log.error("Error finding college by ID",e);
				handleDatabaseException(e, request, response);
				return;
			}
			
		}
		else{
			reset(request);
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("CollegeCtl Method doGet Ended");
	}

	
	 /**
     * Save college.
     *\n
     * @param bean the bean\n
     * @param model the model\n
     * @param request the request\n
     * @throws DuplicateRecordException the duplicate record exception\n
     * @throws ApplicationException the application exception\n
     */
	private final void saveCollege(final CollegeBean bean, final HttpServletRequest request)
			throws DuplicateRecordException, ApplicationException {
		log.debug("saveCollege method start");
		try {
			final long pk = model.add(bean);
			ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_ADD, request);
			bean.setId(pk);
		} catch (final Exception e) {
			ServletUtility.setBean(bean, request);
			handleDatabaseException(e, request, response);
		}
	}

	/**
	 * Update.
	 *
	 * @param bean     the bean
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException     the servlet exception
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @throws DuplicateRecordException the duplicate record exception
	 */
	private final void updateCollege(final CollegeBean bean, final HttpServletRequest request)
			throws DuplicateRecordException, ApplicationException {
			log.debug("updateCollege method start");
			try {
				model.update(bean);
				ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_UPDATE, request);
			} catch (final Exception e) {
				handleDatabaseException(e, request, response);
			}
	}

	/**
	 * Contains Submit logics. this method allow user to save update, delete or 
	 * cancel and reset the form
	 * Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CollegeCtl Method doPost Started");		
		final String op = DataUtility.getString(request.getParameter("operation"));	
		final long id = DataUtility.getLong(request.getParameter("id"));	
		final CollegeBean bean = populate(request);	
		if(validate(request)){	
			if (OP_SAVE.equalsIgnoreCase(op)) {	
				try {	
					saveCollege(bean, request);	
				} catch (final ApplicationException e) {
					handleDatabaseException(e, request, response);
					return;	
				}catch(final DuplicateRecordException e){
					ServletUtility.setErrorMessage(PropertyReader.getValue("error.college.exist"), request);	
				}	
			} else if (OP_UPDATE.equalsIgnoreCase(op)) {	
				try {	
					updateCollege(bean, request);	
				} catch (final ApplicationException e) {
					handleDatabaseException(e, request, response);	
					return;	
				}catch(final DuplicateRecordException e){	
					ServletUtility.setErrorMessage(PropertyReader.getValue("error.college.exist"), request);	
				}	
			} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("CollegeCtl Method doPost Ended");
	}

	/**
	 * Reset the form
	 * @param request the request
	 */
	private void reset(final HttpServletRequest request) {
		request.setAttribute("name", "");
		request.setAttribute("address", "");
		request.setAttribute("state", "");
		request.setAttribute("city", "");
		request.setAttribute("phoneNo", "");
	}



	/**
	 * Returns the VIEW page of this Controller.
	 *
	 * @return the string
	 */
	@Override
	protected String getView() {
		return ORSView.COLLEGE_VIEW;
	}

}
