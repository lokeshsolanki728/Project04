package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.Bean.FacultyBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.FacultyModel;
import com.rays.pro4.Util.FacultyListValidator;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/**
 * The Class FacultyListCtl.
 * 
 * @author Lokesh SOlanki
 */
@WebServlet(name = "FacultyListCtl", urlPatterns = { "/ctl/FacultyListCtl" })
public class FacultyListCtl extends BaseCtl<FacultyBean> {

	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(FacultyListCtl.class);

	private final FacultyModel model = new FacultyModel();
	
	/**
	 * Preload method to add data to request object.
	 *
	 * @param request the request
	 */
	@Override
	protected void preload(final HttpServletRequest request) {
		log.debug("preload method of FacultyListCtl Started");
		final int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		showList(populate(request), request, response, 1, pageSize);
		log.debug("preload method of FacultyListCtl Ended");
	}

	/**
	 * populate the bean
	 * @param request the request
	 * @return the bean
	 */
	/**
	 * Populates bean object from request parameters.
	 * 
	 * @param request the request
	 * @return the college bean
	 */
	@Override
	protected FacultyBean populate(final HttpServletRequest request) {
		final FacultyBean bean = new FacultyBean();
		bean.populate(request);
		return bean;
	}

	
	/**
	 * Search method.
	 * 
	 * @param bean     the bean
	 * @param pageNo   the page no
	 * @param pageSize the page size
	 * @return the list
	 * @throws ApplicationException the application exception
	 */
	private final List<FacultyBean> searchFaculty(final FacultyBean bean, final int pageNo, final int pageSize)
			throws ApplicationException {
		return model.search(bean, pageNo, pageSize);
	}

	/**
	 * Validates the data send by the user.
	 * 
	 * @param request the request
	 * @return true if the data is valid
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("validate Method Started");
		final boolean pass = FacultyListValidator.validate(request);
		if (!pass) {
			log.debug("validate Method End with error");
		}
		log.debug("validate Method End");
		return pass;
	}

	/**
	 * Contains Display logics. Display List functionality of Faculty List
	 * 
	 * @param request  request
	 * @param response response
	 * @throws ServletException  the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException 
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Do get method of FacultyCtl Started");
		final FacultyBean bean = populate(request);
		final int pageNo = 1;
		final int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		try {
			showList(bean, request, response, pageNo, pageSize);
		} catch (Exception e) {
			handleDatabaseException(e, request, response);
		}
		log.debug("Do get method of FacultyListCtl End");
	}

	/**
	 * Delete the Faculty.
	 * 
	 * @param ids      the id of the element to delete
	 * @param request  request
	 * @param response response
	 * @throws ApplicationException the application exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException
	 */
	private final void delete(final String[] ids, final HttpServletRequest request, final HttpServletResponse response)
			throws ApplicationException {
		if (ids != null && ids.length > 0) {
			final FacultyBean deletebean = new FacultyBean();
			for (final String id : ids) {
				deletebean.setId(DataUtility.getInt(id));
				model.delete(deletebean);
			}
			ServletUtility.setSuccessMessage(MessageConstant.FACULTY_SUCCESS_DELETE, request);
		} else {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.select.one"), request);
		}
	}

	/**
	 * Contains Submit logics. Submit List functionality of Faculty List
	 * 
	 * @param request  request
	 * @param response response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do Post method of FacultyListCtl Started");
		final String op = DataUtility.getString(request.getParameter("operation"));
		final String[] ids = request.getParameterValues("ids");
		final int[] pageData = paginate(request);
		int pageNo = pageData[0];
		int pageSize = pageData[1];

			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				try {
					delete(ids, request, response);
				} catch (ApplicationException e) {
					handleDatabaseException(e, request, response);
					return;
				}
			}
			final FacultyBean bean = populate(request);
			showList(bean, request, response, pageNo, pageSize);
		 if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
		}
		log.debug("do Post method of FacultyListCtl End");}
	}

	/**
	 * set the data in the list.
	 * 
	 * @param bean     bean
	 * @param request  request
	 * @param response response
	 * @param pageNo   pageNo
	 * @param pageSize pageSize
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	/**
	 * showList Method. set the data in the list
	 * 
	 * @param bean     the bean
	 * @param request  the request
	 * @param response the response
	 * @param pageNo   the page number
	 * @param pageSize the size of the page
	 * @throws ServletException  the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ApplicationException the application exception
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private final void showList(final FacultyBean bean, final HttpServletRequest request,
			final HttpServletResponse response, final int pageNo, final int pageSize)
			throws ServletException, IOException {
		log.debug("showList Method Start");
		List<FacultyBean> list = null;
		try {
			list = searchFaculty(bean, pageNo, pageSize);
			final List<FacultyBean> nextList = searchFaculty(bean, pageNo + 1, pageSize);
			request.setAttribute("nextlist", nextList.size());
		} catch (ApplicationException e) {
			log.error(e);
			handleDatabaseException(e, request, response);
			return;
		}
		if (list.isEmpty()
				&& !OP_DELETE.equalsIgnoreCase(DataUtility.getString(request.getParameter("operation")))) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
		}
		setListAndPagination(list, request, pageNo, pageSize);
	}

	/**
	 * manage the pagination.
	 * 
	 * @param request the request
	 * @return the page data
	 */
	private int[] paginate(final HttpServletRequest request) {
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		return new int[] { pageNo, pageSize };
	}

	/**
	 * setListAndPagination method
	 * @param list  list
	 * @param request request
	 * @param pageNo pageNo
	 * @param pageSize pageSize
	 */
	private final void setListAndPagination(final List list, final HttpServletRequest request, final int pageNo,
			final int pageSize) {
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
	}

	/** 
	 * get the view
	 */
	@Override
	protected String getView() {
		return ORSView.FACULTY_LIST_VIEW;
	}
}

