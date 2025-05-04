package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.*;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Model.FacultyModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Model.TimeTableModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;




import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


/**
* The Class TimeTableListCtl.
*
*  @author Lokesh SOlanki
*
*/
@WebServlet(name = "TimeTableListCtl", urlPatterns = {"/ctl/TimeTableListCtl"})
public class TimetableListCtl extends BaseCtl{


	/* The log. */
	private static Logger log = Logger.getLogger(TimeTableListCtl.class);

    private static final String DEFAULT_ORDER_BY = "courseName";
	/*
	 * (non-Javadoc)
	 * @see com.rays.pro4.controller.BaseCtl#preload(javax.servlet.http.HttpServletRequest)
	 */	
	private final TimeTableModel model=new TimeTableModel();
	protected void preload(HttpServletRequest request) {
		log.debug("preload method of TimetableListCtl Started");
        final CourseModel courseModel = new CourseModel();
        final SubjectModel subjectModel = new SubjectModel();
		try {
			final List<CourseBean> courseList = courseModel.list(0,0);
			final List<SubjectBean> subjectList = subjectModel.list(0,0);
			if(!courseList.isEmpty()){
                request.setAttribute("courseList", courseList);
            }if(!subjectList.isEmpty()){
                request.setAttribute("subjectList", subjectList);
            }
		} catch (final ApplicationException e) {
			log.error(e);
			handleDatabaseException(e, request, null);
		}		

	}
	
	
	/* (non-Javadoc)
	 * @see com.rays.pro4.controller.BaseCtl#populateBean(javax.servlet.http.HttpServletRequest)
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("TimetableListCtl Method populatebean Started");

        final TimeTableBean bean = new TimeTableBean();
        bean.populate(request);
		
        log.debug("TimetableListCtl Method populatebean Ended");
		return bean;
	}

    /**
     * Contains display logics.
     *
     * @param request the request
     * @param response the response
     * @throws javax.servlet.ServletException the servlet exception
     * @throws java.io.IOException Signals that an I/O exception has occurred.
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet method of TimetableListCtl Started");
        String orderBy = DataUtility.getString(request.getParameter("orderBy"));
        String sortOrder = DataUtility.getString(request.getParameter("sortOrder"));
        
        
        if (orderBy == null || orderBy.trim().isEmpty()) orderBy = DEFAULT_ORDER_BY;
        if (sortOrder == null || sortOrder.trim().isEmpty()) sortOrder = "asc";

        List<TimeTableBean> list;
        final List nextList;
        final int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        final int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        final TimeTableBean bean = (TimeTableBean) populateBean(request);
        final String op = DataUtility.getString(request.getParameter("operation"));
        final int page = (pageNo == 0) ? 1 : pageNo;
        final int size = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
            return;
        }
        try {
            list = model.search(bean, page, size,orderBy,sortOrder);
            nextList = model.search(bean, page + 1, size,orderBy,sortOrder);

            if (!nextList.isEmpty()) {
                request.setAttribute("nextlist", nextList.size());
            }
            if (list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
            	ServletUtility.setErrorMessage(MessageConstant.RECORD_NOT_FOUND, request);
            }
            request.setAttribute("orderBy", orderBy);
            request.setAttribute("sortOrder", sortOrder);            
             ServletUtility.setList(list, request);
             ServletUtility.setPageNo(page, request);
             ServletUtility.setPageSize(size, request);

             ServletUtility.forward(getView(), request, response);
        } catch (final ApplicationException e) {
        	
			log.error(e);

			
            handleDatabaseException(e, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("TimetableListCtl Method doGet Ended");
    }

    /**
     * Contains Submit logics.
     *
     * @param request  the request
     * @param response the response
     * @throws javax.servlet.ServletException the servlet exception
     * @throws java.io.IOException Signals that an I/O exception has occurred.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
        String op = DataUtility.getString(request.getParameter("operation"));
        String orderBy = DataUtility.getString(request.getParameter("orderBy"));
        String sortOrder = DataUtility.getString(request.getParameter("sortOrder"));
        
        if (orderBy == null || orderBy.trim().isEmpty()) orderBy = DEFAULT_ORDER_BY;
        if (sortOrder == null || sortOrder.trim().isEmpty()) sortOrder = "asc";
        List<TimeTableBean> list = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        
		final TimeTableBean bean = (TimeTableBean) populateBean(request);

		String[] ids = (String[]) request.getParameterValues("ids");
		log.debug("doPost method of TimetableListCtl Started");
		if (OP_SEARCH.equalsIgnoreCase(op)) {
		    pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;	
		} 
        
		else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
			pageNo--;
		}
		else if (OP_NEW.equalsIgnoreCase(op)) 
		{
			ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
			return ;
		}
		else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
			if (ids != null && ids.length > 0) {
                for (final String id2 : ids) {
                    try {
                        model.delete(DataUtility.getLong(id2));
                       ServletUtility.setSuccessMessage(MessageConstant.DATA_DELETE_SUCCESSFUL, request);
                    } catch (final ApplicationException e) {
                    	log.error(e);
                        handleDatabaseException(e, request, response);
                        return;
					}
                }
            } else {
                ServletUtility.setErrorMessage(MessageConstant.SELECT_AT_LEAST_ONE_RECORD, request);
			}
		}
		
        try {                  
        	list = model.search(bean, pageNo, pageSize,orderBy,sortOrder); 
        	List nextList=model.search(bean,pageNo+1,pageSize,orderBy,sortOrder);
            request.setAttribute("nextlist", nextList.size());
        } catch(final ApplicationException e){
        	log.error(e);
            handleDatabaseException(e, request, response);
            return;
        }
        if (list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
			ServletUtility.setErrorMessage(MessageConstant.RECORD_NOT_FOUND, request);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("sortOrder", sortOrder);
        ServletUtility.setList(list, request);
        ServletUtility.setPageNo(pageNo, request);
        ServletUtility.setPageSize(pageSize, request);
         ServletUtility.forward(getView(), request, response);
        log.debug("TimetableListCtl Method doPost Ended");
	}

	/**
     * Returns the View page of this Controller
     *
     * @return
     * @see com.rays.pro4.controller.BaseCtl#getView()
     *
     *
     */
	protected String getView() {
		return ORSView.TIMETABLE_LIST_VIEW;
	}

}

