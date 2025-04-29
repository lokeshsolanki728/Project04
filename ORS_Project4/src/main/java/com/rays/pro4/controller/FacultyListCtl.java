package com.rays.pro4.controller;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.FacultyBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.FacultyModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/** 
* The Class FacultyListCtl.
*  @author Lokesh SOlanki
*/
@WebServlet (name = "FacultyListCtl" , urlPatterns = {"/ctl/FacultyListCtl"})
public class FacultyListCtl extends BaseCtl{


	/** The log. */
	public static Logger log = Logger.getLogger(FacultyListCtl.class);
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		return true;
		
	}
	    
	protected BaseBean populateBean(HttpServletRequest request) {

		FacultyBean bean = new FacultyBean();

		bean.setFirstName(DataUtility.getString(request.getParameter("firstname")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastname")));
		bean.setEmailId(DataUtility.getString(request.getParameter("login")));
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeid")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseid")));
		
	return bean;
	}
	private void setListAndPagination(List list, HttpServletRequest request, int pageNo, int pageSize) {
		try {
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
	}
	@Override
	protected String getView() {
		return ORSView.FACULTY_LIST_VIEW;
	}
	
	
	

    /**
     * Contains Display logics.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, java.io.IOException {
		
		log.debug("Do get method of FacultyCtl Started");
		List list;
		List nextList=null;
		
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));		
		
		FacultyModel model = new FacultyModel();
		FacultyBean bean = (FacultyBean) populateBean(request);
		
		String op = DataUtility.getString(request.getParameter("operation"));
		try {
			list = model.search(bean, pageNo, pageSize);
			
			  nextList=model.search(bean,pageNo+1,pageSize);
			  request.setAttribute("nextlist", nextList.size());
			
			if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage(PropertyReader.getValue("error.norrecord"), request);
            }
			ServletUtility.setList(list, request);
	        ServletUtility.setPageNo(pageNo, request);
	        ServletUtility.setPageSize(pageSize, request);
	        ServletUtility.forward(getView(), request, response);
	        
        } catch (ApplicationException e) {			
        	log.error(e);        	
			ServletUtility.handleException(e, request, response);
			return ;
		}
	    
		log.debug("Do get method of FacultyCtl End");
	}
    
    /** 
     * Contains Submit logics.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do Post method of FacultyCtl Started");
		List list=null;
		List nextList = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo==0)?1:pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		
		String op = DataUtility.getString(request.getParameter("operation"));

		FacultyBean bean = (FacultyBean)populateBean(request);
		FacultyModel model = new FacultyModel();

		String[] ids = (String[]) request.getParameterValues("ids");
		    
		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
			if (pageNo > 1) {
				pageNo--;
			} else {
				pageNo = 1;
			}
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
			return;
		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {			
			pageNo = 1;
			if (ids != null && ids.length != 0) {
				FacultyBean deletebean = new FacultyBean();
				
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					
				}try {
					model.delete(deletebean);
				} catch (ApplicationException e) {					
					log.error(e);
					ServletUtility.handleException(e, request, response);
					return;
				}
				ServletUtility.setSuccessMessage(PropertyReader.getValue("success.faculty.delete"), request);
			} else {
				ServletUtility.setErrorMessage(PropertyReader.getValue("error.require.selectone"), request);
			}
		}		
		try {
			list = model.search(bean, pageNo, pageSize);			
			nextList = model.search(bean, pageNo + 1, pageSize);
			request.setAttribute("nextlist", nextList.size());
			
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {			
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
		}
		ServletUtility.setBean(bean, request);		
		setListAndPagination(list, request, pageNo, pageSize);		
		log.debug("do Post method of FacultyCtl End");
	}
}
