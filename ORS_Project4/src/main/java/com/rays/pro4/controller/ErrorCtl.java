package com.rays.pro4.controller;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.Util.ORSView;

/**
 * Error Ctl is called when there is any Error in running time
 * @author Lokesh SOlanki
 *
 */
@WebServlet(name = "ErrorCtl", urlPatterns = {"/ErrorCtl"})
public class ErrorCtl extends BaseCtl<Object> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The log. */
	private static final Logger log = Logger.getLogger(ErrorCtl.class);

	/**
	 * Contains Display logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException, IOException
	 */
	/**
	 * get the error page
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ErrorCtl doGet method started");
		ServletUtility.forward(getView(), request, response);
		log.debug("ErrorCtl doGet method End");
		

	}

	/**
	 * Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	/**
	 * Contains Submit logics
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

			log.debug("Do Post Method of Error Ctl started");
			ServletUtility.forward(getView(), request, response);
			log.debug("Do Post Method of Error Ctl End");
	}
	/** 
	 * Returns the VIEW page of this Controller
	 *
	 * @return
	 */
	@Override

	protected String getView() {
		return ORSView.ERROR_VIEW;
	}


}
