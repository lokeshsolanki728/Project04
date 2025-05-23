
package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.DataUtility;
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
public class MarksheetCtl extends BaseCtl<MarksheetDTO> {

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
		HashMap<String, String> errors = new HashMap<String, String>();
		MarksheetDTO bean = populateBean(request);
		boolean pass=MarksheetValidator.validate(bean, errors); // Ensure validate method in MarksheetValidator accepts MarksheetDTO
		if (!pass) {
			ServletUtility.setErrors(errors, request);
		}

		log.debug("MarksheetCtl validate method end");

		return pass;
	}

	@Override
	protected MarksheetBean populateBean(HttpServletRequest request) {
		log.debug("MarksheetCtl populateBean method start");
		MarksheetDTO bean = new MarksheetDTO(); // Change MarksheetBean to MarksheetDTO
		bean.setId(DataUtility.getLong(request.getParameter("id"))); // Assuming ID is part of MarksheetDTO
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		bean.setStudentId(DataUtility.getLong(request.getParameter("studentId")));
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
				dto = model.findByPK(id); // findByPK returns MarksheetDTO
				
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
		MarksheetDTO bean = (MarksheetDTO) populateBean(request); // Change MarksheetBean to MarksheetDTO and cast
		HashMap<String, String> errors = new HashMap<String, String>();
		if(!validate(request)){
			ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
 
		try {

			if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
				MarksheetDTO dto = bean.getDTO();
				if (id > 0) {
					MarksheetDTO tempDTO = model.findByPK(id);
					dto.setCreatedBy(tempDTO.getCreatedBy());
					dto.setModifiedBy(tempDTO.getModifiedBy());
					dto.setCreatedDatetime(tempDTO.getCreatedDatetime());
					dto.setModifiedDatetime(tempDTO.getModifiedDatetime());
					model.update(dto);
					ServletUtility.setSuccessMessage("Marksheet is updated", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Marksheet is saved", request);
				
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
