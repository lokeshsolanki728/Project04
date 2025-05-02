package com.rays.pro4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Util.DataBean;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * Marksheet Merit List functionality Controller. Performance operation of
 * Marksheet Merit List
 * 
 * @author Lokesh SOlanki
 */
@WebServlet(name = "MarksheetMeritListCtl", urlPatterns = "/ctl/MarksheetMeritListCtl")
public class MarksheetMeritListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(MarksheetMeritListCtl.class);
	private final MarksheetModel model = new MarksheetModel();

	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("preload method of MarksheetMeritListCtl Started");

		log.debug("preload method of MarksheetMeritListCtl End");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rays.pro4.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("MarksheetMeritListCtl Method populatebean Started");
		final MarksheetBean bean = new MarksheetBean();
		log.debug("MarksheetMeritListCtl Method populatebean End");
		return bean;
	}

	/**
	 * Contains Display logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("MarksheetMeritListCtl doGet Start");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		List<MarksheetBean> list;
		try {
			list = model.getMeritList(pageNo, pageSize);
			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
		} catch (final ApplicationException e) {
			log.error("Application exception", e);
			handleDatabaseException(e, request, response);
			return;
		}
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);

		log.debug("MarksheetMeritListCtl doGet End");
	}

	/**
	 * Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("MarksheetMeritListCtl doGet Start");

		String op = DataUtility.getString(request.getParameter("operation"));
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		List<MarksheetBean> list;

		try {
			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
				return;
			}
			list = model.getMeritList(pageNo, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
		} catch (final ApplicationException e) {
			log.error("Application exception", e);
			handleDatabaseException(e, request, response);
			return;
		}

		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(ORSView.MARKSHEET_MERIT_LIST_VIEW, request, response);
		log.debug("MarksheetMeritListCtl doPost End");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rays.pro4.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.MARKSHEET_MERIT_LIST_VIEW;
	}

}
