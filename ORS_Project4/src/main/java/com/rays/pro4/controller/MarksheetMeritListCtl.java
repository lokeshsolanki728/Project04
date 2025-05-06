package com.rays.pro4.controller;

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
import com.rays.pro4.Util.DataUtility;
import java.io.IOException;
import com.rays.pro4.controller.ORSView;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

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

		log.debug("MarksheetMeritListCtl doGet method Start"); 

		final int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));		
		final List<MarksheetBean> list;
		String view=getView();
		try {
			list = model.getMeritList(1, pageSize);
			if (list.isEmpty()) {
				ServletUtility.setErrorMessage(MessageConstant.NORECORD_MSG, request);
			}
			ServletUtility.setList(list, request);
		} catch (final ApplicationException e) {
			log.error("Application exception", e);
			handleDatabaseException(e, request, response);
			return;
		}		
		ServletUtility.setPageNo(1, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(view, request, response);		
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
		log.debug("MarksheetMeritListCtl doPost method Start");

		final String op = DataUtility.getString(request.getParameter("operation"));
		final int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		final String view = ORSView.MARKSHEET_MERIT_LIST_VIEW;
		int pNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		final List<MarksheetBean> list;

		
		try {
			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
				return;
			}
			list=model.getMeritList(pNo, pageSize);
			if (list.isEmpty()) {
				ServletUtility.setErrorMessage(MessageConstant.NORECORD_MSG, request);
			}
			ServletUtility.setList(list, request);			
		} catch (final ApplicationException e) {
			log.error("Application exception", e);
			handleDatabaseException(e, request, response);
			return;
		}

		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);		
		ServletUtility.forward(view, request, response);
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
