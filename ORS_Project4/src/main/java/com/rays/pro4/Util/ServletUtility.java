package com.rays.pro4.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Model.BaseModel;
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
	 * @param page     the page
	 * @param request  the request
	 * @param response the response
	 * @param map 
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void forward(String page, HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
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

	/**
	 * Redirect to given JSP/Servlet
	 * 
	 * @param page
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void redirect(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendRedirect(page);
	}

	/**
	 * Handle Exception.
	 *
	 * @param e
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void handleException(Exception e,  HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// System.out.println("servlet ulitity error ctl------------>");
		request.setAttribute("exception", e);
//        response.sendRedirect(ORSView.ERROR_CTL);
//        
	}

	/**
	 * Gets error message from request.
	 *
	 * @param property the property
	 * @param request  the request
	 * @return the error message
	 */
	public static String getErrorMessage(String property, HttpServletRequest request) {

		String val = (String) request.getAttribute(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

		StringBuffer sb = new StringBuffer("<UL>");
		String name = null;

		while (e.hasMoreElements()) {
			name = e.nextElement();
			if (name.startsWith("error.")) {
				sb.append("<LI class='error'>" + request.getAttribute(name) + "</LI>");
			}
		}
		sb.append("</UL>");
		return sb.toString();
	}
	/*    */
	/**
	 * Gets a message from request
	 * 
	 * @param property
	 * @param request
	 * @return
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
	 * Sets error message to request
	 * 
	 * @param msg
	 * @param request
	 */
	public static void setErrorMessage(String msg, HttpServletRequest request) {
		request.setAttribute(BaseCtl.MSG_ERROR, msg);
	}

	/**
	 * Gets error message from request.
	 *
	 * @param request the request
	 * @return the error message
	 */
	public static String getErrorMessage(HttpServletRequest request) {
		String val = (String) request.getAttribute(BaseCtl.MSG_ERROR);
		if (val == null) {
			return "";
		} else {
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
	 * Gets success message from request.
	 *
	 * @param request the request
	 * @return the success message
	 */
	public static String getSuccessMessage(HttpServletRequest request) {
		String val = (String) request.getAttribute(BaseCtl.MSG_SUCCESS);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/*
	 * public static void setModel(BaseModel model, HttpServletRequest request) {
	 * request.setAttribute("model", model); }
	 */
	/**
	 * Sets default Bean to request.
	 *
	 * @param bean    the bean
	 * @param request the request
	 */
	public static void setBean(BaseBean bean, HttpServletRequest request) {
		request.setAttribute("bean", bean);

	}

	/*
	 * public static void setUserModel(UserModel model, HttpServletRequest request)
	 * { request.getSession().setAttribute("user", model); }
	 */
	/**
	 * Gets default bean from request.
	 *
	 * @param request the request
	 * @return the bean
	 */

	public static BaseBean getBean(HttpServletRequest request) {
		return (BaseBean) request.getAttribute("bean");
	}

	/*
	 * public static boolean isLoggedIn(HttpServletRequest request) { UserModel
	 * model = (UserModel) request.getSession().getAttribute("user"); return model
	 * != null; }
	 * 
	 *//*    *//**
				 * Returns logged in user role. s
				 * 
				 * @param property the property
				 * @param request  the request
				 * @return the parameter
				 */

	/*
	 * 
	 * public static long getRole(HttpServletRequest request) { UserModel model =
	 * (UserModel) request.getSession().getAttribute("user"); if (model != null) {
	 * return model.getRoleId(); } else { return AppRole.STUDENT; } }
	 */
	/*    *//**
			 * gets Model from request scope
			 * 
			 * @param request
			 * @return
			 */
	public static BaseModel getModel(HttpServletRequest request) {
		return (BaseModel) request.getAttribute("model");
	}

	/**
	 * Get request parameter to display. If value is null then return empty string
	 * 
	 * @param property
	 * @param request
	 * @return
	 */

	public static String getParameter(String property, HttpServletRequest request) {
		String val = (String) request.getParameter(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
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
	 * Gets default list from request.
	 *
	 * @param request the request
	 * @return the list
	 */
	public static List getList(HttpServletRequest request) {
		return (List) request.getAttribute("list");
	}

	/**
	 * Sets Page Number for List pages.
	 *
	 * @param pageNo  the page no
	 * @param request the request
	 */
	public static void setPageNo(int pageNo, HttpServletRequest request) {
		request.setAttribute("pageNo", pageNo);
	}

	/**
	 * Gets Page Number for List pages.
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
	 * Gets Page Size for List pages.
	 *
	 * @param request the request
	 * @return the page size
	 */
	public static int getPageSize(HttpServletRequest request) {
		return (Integer) request.getAttribute("pageSize");
	}
}

