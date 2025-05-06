--- a/ORS_Project4/src/main/java/com/rays/pro4/controller/MarksheetCtl.java
+++ b/ORS_Project4/src/main/java/com/rays/pro4/controller/MarksheetCtl.java
package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.validator.MarksheetValidator;
 
/**
 * Marksheet functionality Controller. Performs operation for add, update, delete
 * and get Marksheet
 *
 * @author yugal
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet(name = "MarksheetCtl", urlPatterns = { "/ctl/MarksheetCtl" })
public class MarksheetCtl extends BaseCtl {

	public static final String OP_SAVE = "Save";
	public static final String OP_UPDATE = "Update";
	public static final String OP_DELETE = "Delete";

	private static Logger log = Logger.getLogger(MarksheetCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("MarksheetCtl preload method start");
		StudentModel model = new StudentModel();
		try {
			List<StudentBean> list = model.list();
			request.setAttribute("studentList", list);
		} catch (ApplicationException e) {
			log.error(e);
		}
		log.debug("MarksheetCtl preload method end");
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("MarksheetCtl validate method start");
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", "Roll No. is required");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("studentId"))) {
			request.setAttribute("studentId", "Student Name is required");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("physics"))) {
			request.setAttribute("physics", "Marks is required");
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("physics"))) {
			request.setAttribute("physics", "Enter valid marks");
			pass = false;
		} else if (DataUtility.getInt(request.getParameter("physics")) > 100
				|| DataUtility.getInt(request.getParameter("physics")) < 0) {
			request.setAttribute("physics", "Marks should be between 0 to 100");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("chemistry"))) {
			request.setAttribute("chemistry", "Marks is required");
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("chemistry"))) {
			request.setAttribute("chemistry", "Enter valid marks");
			pass = false;
		} else if (DataUtility.getInt(request.getParameter("chemistry")) > 100
				|| DataUtility.getInt(request.getParameter("chemistry")) < 0) {
			request.setAttribute("chemistry", "Marks should be between 0 to 100");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("maths"))) {
			request.setAttribute("maths", "Marks is required");
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("maths"))) {
			request.setAttribute("maths", "Enter valid marks");
			pass = false;
		} else if (DataUtility.getInt(request.getParameter("maths")) > 100
				|| DataUtility.getInt(request.getParameter("maths")) < 0) {
			request.setAttribute("maths", "Marks should be between 0 to 100");
			pass = false;
		}
		log.debug("MarksheetCtl validate method end");

		return pass;
	}

	@Override
	protected MarksheetBean populateBean(HttpServletRequest request) {
		log.debug("MarksheetCtl populateBean method start");
		MarksheetBean bean = new MarksheetBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		bean.setStudentld(DataUtility.getLong(request.getParameter("studentId")));
		bean.setPhysics(DataUtility.getInt(request.getParameter("physics")));
		bean.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
		bean.setMaths(DataUtility.getInt(request.getParameter("maths")));
		populateDTO(bean, request);
		log.debug("MarksheetCtl populateBean method end");
		return bean;
	}

	/**
	 * Contains Display logics
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("MarksheetCtl doGet method start");

		String op = DataUtility.getString(request.getParameter("operation"));
		MarksheetModel model = new MarksheetModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 || op != null) {
			MarksheetDTO dto;
			try {
				dto = model.findByPK(id);
				MarksheetBean bean = new MarksheetBean();
				bean.setId(dto.getId());
				bean.setRollNo(dto.getRollNo());
				bean.setStudentld(dto.getStudentId());
				bean.setName(dto.getName());
				bean.setPhysics(dto.getPhysics());
				bean.setChemistry(dto.getChemistry());
				bean.setMaths(dto.getMaths());

				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("MarksheetCtl doGet method end");
	}

	/**
	 * Contains Submit logics
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("MarksheetCtl doPost method start");
		String op = DataUtility.getString(request.getParameter("operation"));
		MarksheetModel model = new MarksheetModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		MarksheetBean bean = (MarksheetBean) populateBean(request);

		try {

			if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
				if (validate(request)) {
					MarksheetDTO dto = new MarksheetDTO();
					dto.setId(bean.getId());
					dto.setRollNo(bean.getRollNo());
					dto.setStudentId(bean.getStudentld());
					dto.setPhysics(bean.getPhysics());
					dto.setChemistry(bean.getChemistry());
					dto.setMaths(bean.getMaths());
					if (id > 0) {
						model.update(dto);
						ServletUtility.setSuccessMessage("Marksheet is updated", request);
					} else {
						model.add(dto);
						ServletUtility.setSuccessMessage("Marksheet is saved", request);
					}
				} else {
					ServletUtility.setBean(bean, request);
					ServletUtility.forward(getView(), request, response);
					return;
				}
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				MarksheetDTO dto=new MarksheetDTO();
				dto.setId(id);
				model.delete(dto);
				ServletUtility.redirect(ORSView.MARKSHEET_LIST_VIEW, request, response);
				return;
			}

			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			log.error(e);
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage("Roll no already exists", request);
		}

		log.debug("MarksheetCtl doPost method end");
	}

	/**
	 * Returns the VIEW page of this Controller
	 *
	 * @return
	 */
	@Override
	protected String getView() {
		return ORSView.MARKSHEET_VIEW;
	}
}