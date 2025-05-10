package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.DTO.BaseDTO;
import com.rays.pro4.DTO.TimeTableDTO;

import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Model.TimeTableModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.ServletUtility;

/**
 * TimeTable functionality Controller. Performs operation for add, update, delete
 * and get TimeTable
 *
 * @author Lokesh SOlanki
 *
 */
@WebServlet(name = "TimeTableCtl", urlPatterns = { "/ctl/TimeTableCtl" })
public class TimeTableCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(TimeTableCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		CourseModel cmodel = new CourseModel();
		SubjectModel smodel = new SubjectModel();
		try {
			List clist = cmodel.list();
			List slist = smodel.list();
			request.setAttribute("courseList", clist);
			request.setAttribute("subjectList", slist);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("TimeTableCtl Method validate Started");
		boolean pass = true;
		TimeTableDTO dto = (TimeTableDTO) populateDTO(request);
		if (!pass) {
		    request.setAttribute("dto", dto);
		}

		log.debug("TimeTableCtl Method validate Ended");
		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		log.debug("TimeTableCtl Method populateDTO Started");
		TimeTableDTO dto = new TimeTableDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		dto.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
		dto.setSemester(DataUtility.getString(request.getParameter("semester")));
		dto.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
		dto.setExamTime(DataUtility.getString(request.getParameter("examTime")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		log.debug("TimeTableCtl Method populateDTO Ended");
		return dto;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimeTableCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		TimeTableModel model = new TimeTableModel();
		int id = DataUtility.getInt(request.getParameter("id"));
		if (id > 0 || op != null) {
			TimeTableDTO dto;
			try {				
				dto = model.findByPK(id);

				
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				log.error("Application Exception", e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("TimeTableCtl Method doGet Ended");
	}

	/**
	 * Contains Submit logics
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimeTableCtl Method doPost Started");

		if (!validate(request)) {
			ServletUtility.forward(getView(), request, response);
			return;
		}
		TimeTableDTO dto = (TimeTableDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		TimeTableModel model = new TimeTableModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			try {
				if (dto.getId() > 0) {
					dto.setModifiedBy("root");
					model.update(dto);
					ServletUtility.setSuccessMessage("Data is successfully updated", request);
				} else {
					long pk = model.add(dto);
					ServletUtility.setSuccessMessage("Data is successfully saved", request);
				}
			} catch (ApplicationException e) {

				log.error("Application Exception", e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("TimeTable already Exist", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op) ) {

			
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error("Application Exception", e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("TimeTableCtl Method doPost Ended");
	}

    @Override
	protected String getView() {
		return ORSView.TIMETABLE_VIEW;
	}
}