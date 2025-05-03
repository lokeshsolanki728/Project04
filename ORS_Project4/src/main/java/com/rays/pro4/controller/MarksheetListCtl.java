package com.rays.pro4.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;import org.apache.log4j.Logger;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * Marksheet List functionality Controller. Performs operation for list, search
 * and delete operations of Marksheet
 *
 * @author SunilOS
 * @author Lokesh SOlanki
 * @version 1.0
 *
* @version 1.0
* @Copyright (c) SunilOS
*/

/**
* Servlet implementation class MarksheetlistCtl
*  @author  Lokesh SOlanki
*/
@WebServlet(name = "MarksheetListCtl", urlPatterns = { "/ctl/MarksheetListCtl" })
public class MarksheetListCtl extends BaseCtl<MarksheetBean> {
	private final MarksheetModel model = new MarksheetModel();
	/** The log. */
	private static final Logger log = Logger.getLogger(MarksheetListCtl.class);

	/**
	 * Populates bean object from request parameters.
	 * 
	 * @param request
	 * @return
	 */
	

	@Override
	protected MarksheetBean populateBean(HttpServletRequest request) {
		log.debug("populateBean method start");
		final MarksheetBean bean = new MarksheetBean();
		bean.populate(request);
		log.debug("populateBean method end");
		return bean;
	}

	/**
	 * public MarksheetListCtl() {
	 * 
	 * }
	 * 
	 * 
	 * /**
	 * Contains Display logics
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */


	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("preload method started");
		log.debug("preload method end");
	}

	/**
	 * Contains Display logics
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("doGet method started");
		final int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		final int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		List list = null;
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		final MarksheetBean bean = (MarksheetBean) populateBean(request);
		try {
			list = model.search(bean, pageNo, pageSize);
			final List nextList = model.search(bean, pageNo + 1, pageSize);
			request.setAttribute("nextlist", !nextList.isEmpty());
			if (list.isEmpty()) {
				ServletUtility.setErrorMessage(MessageConstant.NO_RECORD_FOUND, request);
			}
			
			ServletUtility.setList(list, request);
		} catch (final ApplicationException e) {
			log.error("Application exception in doGet", e);
			handleDatabaseException(e, request, response);
			return;
		}

		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		
		log.debug("doGet method end");
		ServletUtility.forward(getView(), request, response);	
	}

	/**
	 * Contains Submit logics.
	 * 
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */

	private void deleteMarksheet(String[] ids,HttpServletRequest request) throws ApplicationException {
		log.debug("delete method start");
		if (ids != null && ids.length > 0) {
			MarksheetBean deletebean = new MarksheetBean();
			for (String id : ids) {
				bean.setId(DataUtility.getInt(id));
				model.delete(bean);
			}
			ServletUtility.setSuccessMessage(MessageConstant.DATA_DELETE_SUCCESSFUL, request);
		} else {
			ServletUtility.setErrorMessage(MessageConstant.SELECT_AT_LEAST_ONE, request);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("doPost method start");
		List list = null; final List nextList;
		final String operation = DataUtility.getString(request.getParameter("operation")); 
		 int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
	        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
	        pageNo = (pageNo == 0) ? 1 : pageNo;
	        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
	        final MarksheetBean bean = (MarksheetBean) populateBean(request);
	        final String[] ids = request.getParameterValues("ids");
		    
	        if (OP_SEARCH.equalsIgnoreCase(operation)) {
	            pageNo = 1;
	        } else if (OP_NEXT.equalsIgnoreCase(operation)) {
	            pageNo++;
	        } else if (OP_PREVIOUS.equalsIgnoreCase(operation) && pageNo > 1) {
	            pageNo--;
	        } else if (OP_NEW.equalsIgnoreCase(operation)) {
	            ServletUtility.redirect(ORSView.MARKSHEET_CTL, request, response);
	            return;
	        } else if (OP_RESET.equalsIgnoreCase(operation) || OP_BACK.equalsIgnoreCase(operation)) {
	            ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request, response);
	            return;
	        }
	        else if (OP_DELETE.equalsIgnoreCase(operation)) {
	        	pageNo = 1;
	            try {
	                deleteMarksheet(ids,request);
	            } catch (ApplicationException e) {
				log.error("Application exception in doPost", e);
				handleDatabaseException(e, request, response);
				return;
			}
		}
		try {
			list = model.search(bean, pageNo, pageSize);
			nextList=model.search(bean, pageNo+1, pageSize);
            request.setAttribute("nextlist", nextList.size());
		} catch (final ApplicationException e) {
			log.error("Application exception in doPost", e);
			handleDatabaseException(e, request, response);
			return;
		}
		if(list.isEmpty()) {
			ServletUtility.setErrorMessage(MessageConstant.NO_RECORD_FOUND, request);
		}
		ServletUtility.setList(list, request);
		
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		 log.debug("doPost method end");
		 ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return the view
	 */
	@Override

	protected String getView() {
		return ORSView.MARKSHEET_LIST_VIEW;
	}
 }
