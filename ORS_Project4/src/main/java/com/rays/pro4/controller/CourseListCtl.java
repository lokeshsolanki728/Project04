package com.rays.pro4.controller;

import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import org.apache.log4j.Logger;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Util.ServletUtility;

/**
 * The Class CourseListCtl.
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "CourseListCtl", urlPatterns = { "/ctl/CourseListCtl" })

public class CourseListCtl extends BaseCtl {

	/** The log. */
	public static Logger log = Logger.getLogger(CourseListCtl.class);


	/**
	 * Populates bean object from request parameters
	 *
	 * @param request
	 * @return
	 */
	@Override
	protected CourseBean populateBean(HttpServletRequest request) {
		CourseBean bean = new CourseBean();
		bean.setId(DataUtility.getLong(request.getParameter("cname")));
		populateDTO(bean, request);
		return bean;
	}

	/**
	 * Contains Display logics.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, java.io.IOException {

		log.debug("do get method of CourseCtl Started");
		List list = null;
		List nextList = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CourseBean bean = (CourseBean) populateBean(request);
		CourseModel model = new CourseModel();
		try {
			list = model.search(bean, pageNo, pageSize);
			nextList = model.search(bean, pageNo + 1, pageSize);
			request.setAttribute("nextlist", nextList.size());
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		if (list == null || list.size() == 0) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
		}
		
		setListAndPagination(list, request, pageNo, pageSize);

		log.debug("do get method of CourseCtl End");
	}

	/**
	 * set list and pagination method
	 * 
	 * @param list
	 * @param request
	 * @param pageNo
	 * @param pageSize
	 */
	private void setListAndPagination(List list, HttpServletRequest request, int pageNo, int pageSize) {
		try {
			setListAndPagination(list, request, pageNo, pageSize);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
			e.printStackTrace();
		}		
	}

	/**
	 * Contains Submit logics
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		log.debug("do Post method of CourseCtl Started");
		List list = null;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");
		CourseBean bean = (CourseBean) populateBean(request);
		CourseModel model = new CourseModel();
		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
			pageNo--;
		} 

		else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
				return;
			}
		else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					CourseBean deletebean = new CourseBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
					}
					try {
						model.Delete(deletebean);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					}
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.course.delete"), request);
				} else {
					ServletUtility.setErrorMessage(PropertyReader.getValue("error.select.one"), request);
				}
			}
		try {

			list = model.search(bean, pageNo, pageSize);
			nextList = model.search(bean, pageNo + 1, pageSize);
			request.setAttribute("nextlist", nextList.size());

			if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
				ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
			}

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}	
		
		setListAndPagination(list, request, pageNo, pageSize);
		log.debug("do Post method of CourseCtl End");
	}
	/**
	 * @return
	 */
	@Override
	protected String getView() {
		return ORSView.COURSE_LIST_VIEW;
	}

	
	
	/**
	 * set list and pagination method
	 *
	 * @param list
	 * @param request
	 * @param pageNo
	 * @param pageSize
	 */
	public void setListAndPagination(List list, HttpServletRequest request, int pageNo, int pageSize) {
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
	}



	}

}
