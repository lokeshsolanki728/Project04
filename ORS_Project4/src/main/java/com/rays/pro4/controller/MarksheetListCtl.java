package com.rays.pro4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.controller.ORSView;
import com.rays.pro4.DTO.MarksheetDTO;

/**
 * Marksheet List functionality Controller. Performs operation for list, search
 * and delete operations of Marksheet
 *
 * @author SunilOS
 * @author Lokesh Solanki
 * @version 1.0 Copyright (c) SunilOS
*/

/**
 * Servlet implementation class MarksheetlistCtl
 * 
 * @author Lokesh Solanki
 */
@WebServlet(name = "MarksheetListCtl", urlPatterns = { "/ctl/MarksheetListCtl" })
public class MarksheetListCtl extends BaseCtl<MarksheetDTO> {
	private final MarksheetModel model = new MarksheetModel();
	/** The log. */ 
	private static final Logger log = Logger.getLogger(MarksheetListCtl.class);

	@Override
	protected MarksheetBean populateBean(HttpServletRequest request) {
		log.debug("populateBean method start");
		final MarksheetDTO bean = new MarksheetDTO();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		log.debug("populateBean method end");
		return bean;
	}

	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("preload method started");
		log.debug("preload method end");
	}
	
	@Override
	protected boolean validate(HttpServletRequest request) {
        log.debug("validate method start");
		boolean pass=true;
		if(DataValidator.isNull(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", MessageConstant.getMessage("error.require", "Roll no"));
			pass=false;
		}
		if(DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", MessageConstant.getMessage("error.require", "Name"));
			pass=false;
		}
		log.debug("validate method end");
		return pass;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("doGet method started");
		List<MarksheetDTO> list=new ArrayList<>();
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		try {
			 list=model.list(pageNo, pageSize);
			 ServletUtility.setList(list, request);
			if(list==null || list.size()==0) {
				ServletUtility.setErrorMessage("No record found", request);
			}
		} catch (final ApplicationException e) {
			log.error("Application exception in doGet", e);
			handleDatabaseException(e, request, response);
			return;
		}
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("doGet method end");
	}

	private void deleteMarksheet(String[] ids, HttpServletRequest request) throws ApplicationException {
		log.debug("delete method start");
		MarksheetDTO deletebean = new MarksheetDTO();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				deletebean.setId(DataUtility.getInt(id));
				model.delete(deletebean);
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
		List list = null;
		String[] ids = request.getParameterValues("ids");
	\tint pageNo = DataUtility.getInt(request.getParameter(\"pageNo\")) == 0 ? 1 : DataUtility.getInt(request.getParameter(\"pageNo\"));
	\tint pageSize = DataUtility.getInt(request.getParameter(\"pageSize\")) == 0 ? DataUtility.getInt(PropertyReader.getValue(\"page.size\")) : DataUtility.getInt(request.getParameter(\"pageSize\"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		String operation = DataUtility.getString(request.getParameter("operation"));
	\tMarksheetDTO bean = populateBean(request);
		if(OP_SEARCH.equalsIgnoreCase(operation)|| OP_NEXT.equalsIgnoreCase(operation)||OP_PREVIOUS.equalsIgnoreCase(operation)) {
\t\t\tif (OP_SEARCH.equalsIgnoreCase(operation)) {
            pageNo = 1;
        } else if (OP_NEXT.equalsIgnoreCase(operation)) {
            pageNo++;
        } else if (OP_PREVIOUS.equalsIgnoreCase(operation) && pageNo > 1) {
            pageNo--;
        }
		}
        else if (OP_DELETE.equalsIgnoreCase(operation)) {
			try {
				deleteMarksheet(ids, request);
			} catch (ApplicationException e) {
				log.error("Application exception in doPost", e);
				handleDatabaseException(e, request, response);
				return;
			}
		}
		
		try {
			list = model.search(bean, pageNo, pageSize);
		} catch (ApplicationException e) {
			log.error(e);
			handleDatabaseException(e, request, response);
			return;
		}
		if (list.isEmpty()) {
			ServletUtility.setErrorMessage(MessageConstant.NO_RECORD_FOUND, request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("doPost method end");
	}

	@Override
	protected String getView() {
		return ORSView.MARKSHEET_LIST_VIEW;
	}
}
