package com.rays.pro4.controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import org.apache.log4j.Logger;

import com.rays.pro4.DTO.SubjectDTO;
/**
 * Subject List functionality Controller. Performs operation for list, search and
 * delete
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "SubjectListCtl", urlPatterns = "/ctl/SubjectListCtl")
public class SubjectListCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(SubjectListCtl.class);

    @Override
    protected SubjectDTO populateDTO(HttpServletRequest request) {
        SubjectDTO dto = new SubjectDTO();
        dto.setId(DataUtility.getLong(request.getParameter("subjectId")));
        dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        dto.setSubjectName(DataUtility.getString(request.getParameter("subjectName")));
        dto.setCourseName(DataUtility.getString(request.getParameter("courseName")));
        return dto;
    }

    /**
     * Contains Display logics.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        log.debug("SubjectListCtl doGet Start");
        List list = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        SubjectDTO dto = populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        SubjectModel model = new SubjectModel();
        try {
            list = model.search(dto, pageNo, pageSize);
            ServletUtility.setList(list, request);
            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage(PropertyReader.getValue("error.norrecord"), request);
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
        log.debug("SubjectListCtl doGet End");
    }

    /**
     * Contains Submit logics
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        log.debug("SubjectListCtl doPost Start");
        List list;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        SubjectDTO dto = populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        SubjectModel model = new SubjectModel();
        String[] ids = request.getParameterValues("ids");

        try {
            if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {
                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        SubjectDTO deleteDto = new SubjectDTO();
                        deleteDto.setId(DataUtility.getLong(id));
 model.delete(deleteDto.getId());
                        ServletUtility.setSuccessMessage(PropertyReader.getValue("success.subject.delete"), request);
                    }
                } else {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.require.selectone"), request);
                }
            }

            list = model.search(dto, pageNo, pageSize);
            if (!OP_DELETE.equalsIgnoreCase(op)) {
                if (list == null || list.size() == 0) {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.norrecord"), request);
                }
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
        log.debug("SubjectListCtl doPost End");
    }
    @Override
    protected String getView() {
        return ORSView.SUBJECT_LIST_VIEW;
    }
}
