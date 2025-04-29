package com.rays.pro4.controller;

import java.io.IOException;
import java.util.Map;

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
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


/**
* College functionality Controller. Performs operation for add, update, delete
* and get College
* 
* @author Lokesh SOlanki
* 
*/

@WebServlet(name ="CollegeCtl", urlPatterns ={"/ctl/CollegeCtl"})
public class CollegeCtl extends BaseCtl{

private static final long serialVersionUID = 1L;
	
	/** The log. */
	private static Logger log = Logger.getLogger(CollegeCtl.class);
	
	protected boolean validate(HttpServletRequest request) {
		log.debug("CollegeCtl Method validate Started");
		boolean pass = true;
		String op=DataUtility.getString(request.getParameter("operation"));
		if(OP_CANCEL.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)){return pass;}
		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name","Name must contains alphabet only");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("address"))) {
			request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("state"))) {
			request.setAttribute("state", PropertyReader.getValue("error.require", "State"));
			pass = false;
		}else if (!DataValidator.isName(request.getParameter("state"))) {
			request.setAttribute("state", PropertyReader.getValue("error.name", "State"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("city"))) {
			request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
			pass = false;
		}else if (!DataValidator.isName(request.getParameter("city"))) {
			request.setAttribute("city", PropertyReader.getValue("error.name", "City"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", PropertyReader.getValue("error.require", "Mobile No"));
			pass = false;	
		}else if (!DataValidator.isMobileNo(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", "Mobile No. must be 10 Digit and No. Series start with 6-9");
			pass = false;
		}

		log.debug("CollegeCtl Method validate Ended");
		return pass;
	}

	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("CollegeCtl Method populatebean Started");
		CollegeBean bean = new CollegeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setState(DataUtility.getString(request.getParameter("state")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
		bean.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));

		populateDTO(bean, request);
		log.debug("CollegeCtl Method populatebean Ended");
		return bean;
	}

	/**
	 * Contains display logic.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		
		CollegeModel model = new CollegeModel();
		
		if (id > 0) {
			CollegeBean bean;
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CollegeCtl Method doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		
		CollegeModel model = new CollegeModel();
		CollegeBean bean = (CollegeBean) populateBean(request);
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op) ) {
			
			try {
				if (id > 0) {
					model.update(bean);
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.college.update"), request);
				} else {
					long pk = model.add(bean);
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.college.add"), request);
					bean.setId(pk);
				}
				ServletUtility.setBean(bean, request);
				ServletUtility.forward(getView(), request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage(PropertyReader.getValue("error.college.exist"), request);
					ServletUtility.forward(getView(), request, response);
					return;
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			return;
		}		else if (OP_CANCEL.equalsIgnoreCase(op) ) {

			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
			return;
		}
		/*else if (OP_DELETE.equalsIgnoreCase(op)) {
*/

		ServletUtility.redirect(getView(), request, response);
		log.debug("CollegeCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.COLLEGE_VIEW;
	}

	
}
