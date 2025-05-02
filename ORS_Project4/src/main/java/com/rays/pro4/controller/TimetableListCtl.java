package com.rays.pro4.controller;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.*;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;
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


	/* The log. /
	private static Logger log = Logger.getLogger(TimeTableListCtl.class);

	/* (non-Javadoc)
	 * @see com.rays.pro4.controller.BaseCtl#preload(javax.servlet.http.HttpServletRequest)
	 */
	private final TimeTableModel model=new TimeTableModel();
	protected void preload(HttpServletRequest request) {
		log.debug("preload method of TimetableListCtl Started");
        final CourseModel courseModel = new CourseModel();
        final SubjectModel subjectModel = new SubjectModel();
		try {
			final List<CourseBean> courseList = courseModel.list();
			final List<SubjectBean> subjectList = subjectModel.list();
			
			if(courseList.isEmpty()||subjectList.isEmpty()) {
				ServletUtility.setErrorMessage("No Record Found", request);
			}
			request.setAttribute("courseList", courseList);
			request.setAttribute("subjectList", subjectList);
		} catch (final ApplicationException e) {
			log.error("Error getting course or subject list during preload", e);
			handleDatabaseException(e, request, null);
		}
		request.setAttribute("courseList", list);
		request.setAttribute("subjectList", list2);
		request.setAttribute("examtime", list3);

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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimetableListCtl Method doGet Started");
		List<TimeTableBean> list  = null ;
		List nextList=null;
		
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
          
		//System.out.println(pageSize+"hhhhhhh");
		
       TimeTableModel model = new TimeTableModel();
		
       TimeTableBean bean =(TimeTableBean) populateBean(request);

		String op = DataUtility.getString(request.getParameter("operation"));
		
		pageNo = (pageNo == 0) ? 1 : pageNo;
		
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		if(OP_RESET.equalsIgnoreCase(op)||OP_BACK.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
			return;
		}
	    

		try {
			list = model.search(bean, pageNo, pageSize);

			nextList=model.search(bean,pageNo+1,pageSize);
			
			request.setAttribute("nextlist", nextList.size());
			
			if (list.isEmpty()) {
				ServletUtility.setErrorMessage("No record Found", request);
	        }
			ServletUtility.setBean(bean, request);
	        ServletUtility.setList(list, request);
	        ServletUtility.setPageNo(pageNo, request);
	        ServletUtility.setPageSize(pageSize, request);
	        ServletUtility.forward(getView(), request, response);
	        log.debug("TimetableListCtl Method doGet Ended");
		} catch (ApplicationException e) {
			log.error("Error searching timetable", e);
			handleDatabaseException(e, request, response);
			return;
		}
	}

    /**
     * Contains Submit logics.
     *
     * @param request the request
     * @param response the response
     * @throws javax.servlet.ServletException the servlet exception
     * @throws java.io.IOException Signals that an I/O exception has occurred.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("TimetableListCtl Method doPost Started");
		List<TimeTableBean> list = null;
		List<TimeTableBean> nextList=null;

		final String op = DataUtility.getString(request.getParameter("operation"));
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		final TimeTableBean bean = (TimeTableBean) populateBean(request);
		
		String[] ids = (String[]) request.getParameterValues("ids");
				
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
						pageNo=1;
						if (ids != null && ids.length > 0) {
							for (String id2 : ids) {
                                try {
                                    final TimeTableBean bean3 = new TimeTableBean();
                                    bean3.setId(DataUtility.getLong(id2));
                                    model.delete(bean3);
                                    ServletUtility.setSuccessMessage(MessageConstant.DATA_DELETE_SUCCESSFUL, request);
                                } catch (final ApplicationException e) {
                                    log.error("Error deleting timetable", e);
                                    handleDatabaseException(e, request, response);
                                    return;
                                }
							}
						}else{
							ServletUtility.setErrorMessage("Select at least one Record", request);
						}
					}
					
				try {
                    list = model.search(bean, pageNo, pageSize);
                    nextList=model.search(bean,pageNo+1,pageSize);
                    request.setAttribute("nextlist", nextList.size());
                    ServletUtility.setBean(bean, request);
                }
                catch(final ApplicationException e){
                    log.error("Error searching timetable", e);
                    handleDatabaseException(e, request, response);
                    return;
                }
                if(list.isEmpty() && !OP_DELETE.equalsIgnoreCase(op))
                {
                    ServletUtility.setErrorMessage("No Record Found", request);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setList(list, request);
                ServletUtility.setPageNo(pageNo, request);
                ServletUtility.setPageSize(pageSize, request);
                ServletUtility.forward(getView(), request, response);
                log.debug("TimetableListCtl Method doPost Ended");
		}

	/**
     * Returns the VIEW page of this Controller
     *
     * @return
     * @see com.rays.pro4.controller.BaseCtl#getView()
     *
     *
     */
	 */
	@Override
	protected String getView() {
		return ORSView.TIMETABLE_LIST_VIEW;
	}

	
}
