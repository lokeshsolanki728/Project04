package com.rays.pro4.controller;


import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import org.apache.log4j.Logger;

import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.util.CourseListValidator;
import com.rays.pro4.Util.PropertyReader;;
import com.rays.pro4.controller.BaseCtl;
import com.rays.pro4.Util.ServletUtility;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
/**
 * The Class CourseListCtl.
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "CourseListCtl", urlPatterns = { "/ctl/CourseListCtl" })
public class CourseListCtl extends BaseCtl<CourseBean> {

	private static final long serialVersionUID = 1L;

	/** The log. */
	public static final Logger log = Logger.getLogger(CourseListCtl.class);
	private final CourseModel model = new CourseModel();
	
	 * Populates bean object from request parameters
	 *
	 * @param request the request
	 * @return the course bean
	 */
	@Override
	protected CourseBean populateBean(final HttpServletRequest request) {
		final CourseBean bean = new CourseBean();
		bean.populate(request);	
		return bean;
	}	
	
	/**
	 * Search Course.
	 *
	 * @param bean the bean
	 * @param pageNo the page no
	 * @param pageSize the page size
	 * @return the list
	 * @throws ApplicationException the application exception
	 */
	private List<CourseBean> searchCourse(final CourseBean bean, final int pageNo, final int pageSize)
			throws ApplicationException{
		return model.search(bean,pageNo,pageSize);
	}
	
	
	
	/**
	 * Contains Display logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do get method of CourseListCtl Started");
		List<CourseBean> list = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CourseBean bean = populateBean(request);
	
		try {
			list = searchCourse(bean,pageNo,pageSize);
			if(list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);			
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (final ApplicationException e) {
			handleDatabaseException(e, request, response);
		}	
		log.debug("doGet method of CourseListCtl End");
	}
	
	/**
	 * Validates input data entered by User
	 *
	 * @param request the request
	 * @return the boolean
	 */
	@Override
	protected final boolean validate(HttpServletRequest request) {
		
		
		
		log.debug("validate Method Started");
		final boolean pass = CourseListValidator.validate(request);
		if(!pass)
			log.debug("validate Method End with error");
		return pass;
	}

	
	/**
	 * Delete.
	 *
	 * @param ids the ids
	 * @param request the request
	 * @param response the response
	 * @throws ApplicationException the application exception
	 */
	private final void delete(final String[] ids, final HttpServletRequest request, final HttpServletResponse response)
			throws ApplicationException {
		log.debug("delete method started");
		if (ids != null && ids.length > 0) {
			for (final String id : ids) {
				model.delete(model.findByPK(DataUtility.getInt(id)));
			}
			ServletUtility.setSuccessMessage(MessageConstant.COURSE_SUCCESS_DELETE, request);
		} else {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.select.one"), request);
		}
		log.debug("delete method end");
	}
	/**
	 * Contains Submit logics.
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred. 
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do Post method of CourseListCtl Started");
		if(!validate(request)) {
			final int[] pageData = paginate(request);
			int pageNo = pageData[0];
			int pageSize = pageData[1];
			final String op = DataUtility.getString(request.getParameter("operation"));
			final String[] ids = request.getParameterValues("ids");
			final CourseBean bean = populate(request);
			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				try {
					delete(ids, request, response);
				} catch (ApplicationException e) {
					handleDatabaseException(e, request, response);
					return;
				}
			} else {
				showList(bean, request, response, pageNo, pageSize);
			}
		}
		log.debug("do Post method of CourseListCtl End");
	}	
	/** Contains Submit logics.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	
	
	/** set the data in the list.
	 * 
	 * @param bean     the bean
	 * @param request  the request
	 * @param response the response
	 * @param pageNo   the page number
	 * @param pageSize the size of the page
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred. */
	private final void showList(final CourseBean bean, final HttpServletRequest request, final HttpServletResponse response,
			final int pageNo, final int pageSize) throws ServletException, IOException {
		log.debug("showList Method Start");
		List<CourseBean> list = null;	
		try {
			list = searchCourse(bean, pageNo, pageSize);
		}catch (final Exception e) {
			handleDatabaseException(e, request, response);
		}
		if (list == null || list.isEmpty()&& !OP_DELETE.equalsIgnoreCase(DataUtility.getString(request.getParameter("operation")))) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
		}
		setListAndPagination(list, request, pageNo, pageSize);	
	}
	
	/**
	 * Sets the list and pagination.
	 *
	 * @param list the list
	 * @param request the request
	 * @param pageNo the page no
	 * @param pageSize the page size
	 */
	private final void setListAndPagination(final List list, final HttpServletRequest request, final int pageNo,final int pageSize) {
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
	}
	
	

	/** manage the pagination.
	 * @param request the request
	 * @return the page data
	 */	
	private int[] paginate(final HttpServletRequest request) {
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue(\"page.size\")) : pageSize;
		return new int[] { pageNo, pageSize };
	}	
	@Override
	protected String getView() {
		return ORSView.COURSE_LIST_VIEW;
	}
}
