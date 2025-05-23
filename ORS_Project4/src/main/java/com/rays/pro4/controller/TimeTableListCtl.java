package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.DTO.TimeTableDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.TimeTableModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.validator.TimeTableValidator;
import com.rays.pro4.Util.ServletUtility;
@WebServlet(name = "TimeTableListCtl", urlPatterns = { "/ctl/TimeTableListCtl" })
public class TimeTableListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(TimeTableListCtl.class);
    
    @Override
	protected boolean validate(HttpServletRequest request){
		return true; // Validation is typically handled in TimeTableCtl
	}

		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		dto.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
		dto.setSemester(DataUtility.getString(request.getParameter("semester")));
		dto.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
		return dto;
    }

    /**
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        log.debug("doGet method of TimeTableListCtl Started");
        List<TimeTableDTO> list;
        TimeTableModel model = new TimeTableModel();
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        TimeTableDTO dto = (TimeTableDTO) populateDTO(request);
        if (!TimeTableValidator.validate(dto)) {
            ServletUtility.setErrorMessage("Validation failed for search criteria.", request);
            ServletUtility.forward(getView(), request, response);
            return;
        }
        try {
            
            list = model.search(dto, pageNo, pageSize);
            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
            }
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug("doGet method of TimeTableListCtl Ended");
    }

    /**
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("TimeTableListCtl doPost Started");
        List<TimeTableDTO> list;
        TimeTableModel model = new TimeTableModel();
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        TimeTableDTO dto = (TimeTableDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));

        try {
            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op)
                    || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo=1;
                String[] ids = request.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        TimeTableDTO deletebean = new TimeTableDTO();
						                deletebean.setId(DataUtility.getInt(id));
                        model.delete(deletebean);
                    }
                    ServletUtility.setSuccessMessage("TimeTable Delete Successfully", request);
                } else {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.require", "Select at least one record"),
                            request);
                }
            }
            
            list = model.search(dto, pageNo, pageSize);
            if (!OP_DELETE.equalsIgnoreCase(op) && (list == null || list.size() == 0)) {
                ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
            }
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug("doPost method of TimeTableListCtl Ended");
    }

    protected String getView() {
        return ORSView.TIMETABLE_LIST_VIEW;
    }
}