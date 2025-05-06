package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.TimeTableBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.TimeTableModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/**
 * TimeTable List functionality Controller. Performs operation for list, search
 * and delete operations of TimeTable
 *
 * @author Lokesh Solanki
 */
@WebServlet(name = "TimeTableListCtl", urlPatterns = { "/ctl/TimeTableListCtl" })
public class TimeTableListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(TimeTableListCtl.class);
    @Override
      
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("populateBean method of TimeTableListCtl Started");
        TimeTableBean bean = new TimeTableBean();
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
        bean.setSemester(DataUtility.getString(request.getParameter("semester")));
        bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
        log.debug("populateBean method of TimeTableListCtl Ended");
        return bean;
    }

    /**
     * Contains display logics.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("doGet method of TimeTableListCtl Started");
        List<TimeTableBean> list;
        TimeTableModel model = new TimeTableModel();
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        TimeTableBean bean = (TimeTableBean) populateBean(request);
        try {
            list = model.search(bean, pageNo, pageSize);
            ServletUtility.setList(list, request);
            if (list == null || list.size() == 0) {
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
        log.debug("doGet method of TimeTableListCtl Ended");
    }

    /**
     * Contains Submit logics.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("doPost method of TimeTableListCtl Started");
        List<TimeTableBean> list;
        TimeTableModel model = new TimeTableModel();
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        String op = DataUtility.getString(request.getParameter("operation"));
        TimeTableBean bean = (TimeTableBean) populateBean(request);

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
                pageNo = 1;
                String[] ids = request.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    TimeTableBean deletebean = new TimeTableBean();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.delete(deletebean.getId());
                    }
                    ServletUtility.setSuccessMessage(MessageConstant.DATA_DELETE_SUCCESSFUL, request);
                } else {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.require", "Select at least one record"),
                            request);
                }
            }

            list = model.search(bean, pageNo, pageSize);
            if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
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

    @Override
    protected String getView() {
        return ORSView.TIMETABLE_LIST_VIEW;
    }
}