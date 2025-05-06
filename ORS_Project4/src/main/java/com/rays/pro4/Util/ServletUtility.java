package com.rays.pro4.Util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Model.BaseModel;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.controller.BaseCtl;

/**
 * This class provides utility operation for Servlet container like forward,
 * redirect, handle generic exception, manage success and error message, manage
 * default Bean and List, manage pagination parameters.
 * 
 * @author Lokesh SOlanki
 *
 */

public class ServletUtility {

	/**
	 * Forward to given JSP/Servlet with request and response.
	 *
	 * @param page     : page name
	 * @param request  : request
	 * @param response : response
	 * @param map      : map
	 * @throws IOException      if there is an input/output exception
	 * @throws ServletException if there is an servlet exception
	 */
	public static void forward(String page, HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		System.out.println(map);
		
		if(map != null)
		{
			for(String key : map.keySet())
			{
				request.setAttribute(key, map.get(key));
			}
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
	}

	public static void forward(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
	}

	public static void forwardView(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		
		request.setAttribute("uri", page);
		RequestDispatcher rd = request.getRequestDispatcher(ORSView.LAYOUT_VIEW);
		rd.forward(request, response);
	}

	public static void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setAttribute("exception", e);
		ServletUtility.forward(ORSView.ERROR_CTL,request,response);
	}

/**
	 * Redirect to given JSP/Servlet
	 *
	 * @param page     : page name
	 * @param request  : request
	 * @param response : response
	 * @throws IOException      if there is an input/output exception
	 * @throws ServletException if there is an servlet exception
	 */

	public static void redirect(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendRedirect(page);
	}

	

	/**
	 * Gets error message from request scope
	 *
	 * @param request : request
	 * @return String : Error Message
	 */
	public static String getErrorMessage(HttpServletRequest request) {
		Enumeration<String> e = request.getAttributeNames();
		StringBuffer sb = new StringBuffer("<UL>");
		String name = null;

		while (e.hasMoreElements()) {
			name = e.nextElement();
			if (name.contains("error.")) {
				sb.append("<li class='error'>" + request.getAttribute(name) + "</li>");
			}
		}
		if (sb.length() > 4) {
			sb.append("</UL>");
		} else {
			sb.append("</UL>");
		}
		return sb.toString();
	}

	public static String getSuccessMessage(HttpServletRequest request) {
		String val = (String) request.getAttribute(BaseCtl.MSG_SUCCESS);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}




/**
	 * Gets a message from request scope
	 *
	 * @param property : property
	 * @param request  : request
	 * @return String : message
	 */
	public static String getMessage(String property, HttpServletRequest request) {
		String val = (String) request.getAttribute(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * Sets error message to request scope
	 *
	 * @param msg     : message
	 * @param request : request
	 */
	public static void setErrorMessage(String msg, HttpServletRequest request) {
		request.setAttribute(BaseCtl.MSG_ERROR, msg);
	}

	/**
	 * Gets error message from request.
	 * @param property : property
	 * @param request : request
	 * @return String : message
	 */
	public static String getErrorMessage(String property, HttpServletRequest request) {
		String val = (String) request.getAttribute(property);
		if (DataValidator.isNotNull(val)) {
			return val;
		}
	}

	/**
	 * Sets success message to request.
	 *
	 * @param msg     the msg
	 * @param request the request
	 */
	public static void setSuccessMessage(String msg, HttpServletRequest request) {
		request.setAttribute(BaseCtl.MSG_SUCCESS, msg);
	}





	/**
	 * Sets default Bean to request.
	 *
	 * @param bean    the bean
	 * @param request the request
	 */
	public static void setBean(BaseBean bean, HttpServletRequest request) {
		request.setAttribute("bean", bean);

	}

	/**
	 * Gets default bean from request.
	 *
	 * @param request the request
	 * @return the bean
	 */
	public static BaseBean getBean(HttpServletRequest request) {
		return (BaseBean) request.getAttribute("bean");
	}

		public static BaseBean populateBean(HttpServletRequest request, BaseBean bean) {
		Map<String, String[]> map = request.getParameterMap();
		for (String key : map.keySet()) {
			if(map.get(key).length > 0){
			String value = map.get(key)[0];
			}
		}
		return bean;

	/**
	 * gets Model from request scope
	 *
	 * @param request : request
	 * @return : BaseModel
	 */
	public static BaseModel getModel(HttpServletRequest request) {
		return (BaseModel) request.getAttribute("model");
	}

	/**
	 * Get request parameter to display. If value is null then return empty string
	 *
	 * @param property the property
	 * @param request  the request
	 * @return the parameter
	 */
	public static String getParameter(String property, HttpServletRequest request) {
		String val = (String) request.getParameter(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
	
	public static BaseBean getBean(String[] property ,HttpServletRequest request) {
		 BaseBean bean=null;
		if(property != null){
			for (String value : property) {
				if(request.getParameter(value) != null){
				System.out.println("value"+value+request.getParameter(value));
				}
			}
		}
	
		return bean;
	}
	public static void populate(BaseBean bean, HttpServletRequest request) {
		request.setAttribute("id", bean.getId());
		request.setAttribute("createdBy", bean.getCreatedBy());
		request.setAttribute("modifiedBy", bean.getModifiedBy());
		request.setAttribute("createdDatetime", bean.getCreatedDatetime());
		request.setAttribute("modifiedDatetime", bean.getModifiedDatetime());
	}
	/**
	 * Sets default List to request.
	 *
	 * @param list    the list
	 * @param request the request
	 */
	public static void setList(List list, HttpServletRequest request) {
		request.setAttribute("list", list);
	}


	/**
	 * Gets default list from request scope
	 *
	 * @param request the request
	 * @return the list
	 */
	public static List getList(HttpServletRequest request) {
		return (List) request.getAttribute("list");
	}

	 * Sets Page Number for List pages.
	 *
	 * @param pageNo  the page no
	 * @param request the request
	 */
	public static void setPageNo(int pageNo, HttpServletRequest request) {
		request.setAttribute("pageNo", pageNo);
	}

	/**
	 * Gets Page Number for List pages from request scope
	 *
	 * @param request the request
	 * @return the page no
	 */
	public static int getPageNo(HttpServletRequest request) {
		return (Integer) request.getAttribute("pageNo");
	}

	/**
	 * Sets Page Size for list pages.
	 *
	 * @param pageSize the page size
	 * @param request  the request
	 */
	public static void setPageSize(int pageSize, HttpServletRequest request) {
		request.setAttribute("pageSize", pageSize);
	}

	/**
	 * Gets Page Size for List pages from request scope
	 *
	 * @param request the request
	 * @return the page size
	 */
	public static int getPageSize(HttpServletRequest request) {
		return (Integer) request.getAttribute("pageSize");
	}

