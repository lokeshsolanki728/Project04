package com.rays.pro4.controller;

import java.io.IOException;

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
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


//TODO: Auto-generated Javadoc
/**
* Get Marksheet functionality Controller. Performs operation for Get Marksheet
* 
* @author Lokesh SOlanki
*/
@WebServlet(name = "GetMarksheetCtl", urlPatterns = { "/ctl/GetMarksheetCtl" })
public class GetMarksheetCtl extends BaseCtl<MarksheetBean>{

	  private static Logger log = Logger.getLogger(GetMarksheetCtl.class);

	    /**
		 * Validates input data entered by User
		 * 
		 * @param request
		 * @return
		 */

		@Override

	    protected boolean validate(HttpServletRequest request) {

	        log.debug("GetMarksheetCTL Method validate Started");

	        boolean pass = true;

	        if (DataValidator.isNull(request.getParameter("rollNo"))) {
	            request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll Number"));
	            pass = false;
	        }
	        else if (!DataValidator.isRollNo(request.getParameter("rollNo"))) {
				request.setAttribute("rollNo", "Roll No. must be in Formate (0000XX000000)");
				pass = false;
	        }
	        
	        log.debug("GetMarksheetCTL Method validate Ended");
	        return pass;
	    }

	    /**
		 * Populates bean object from request parameters
		 * 
		 * @param request
		 * @return
		 */
		@Override

	    protected MarksheetBean populateBean(HttpServletRequest request) {

	        log.debug("GetMarksheetCtl Method populatebean Started");

	        MarksheetBean bean = new MarksheetBean();
	        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

	        log.debug("GetMarksheetCtl Method populatebean Ended");
	        return bean;
	    }

	    /**
	     * Concept of Display method.
	     *
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
		/**
		 * Contains Display logics
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
	    protected void doGet(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	     
	    	ServletUtility.forward(getView(), request, response);
	    }

	    /**
	     * Concept of Submit Method.
	     *
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
		/**
		 * Contains Submit logics
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {

	        log.debug("GetMarksheetCtl Method doGet Started");
	        String op = DataUtility.getString(request.getParameter("operation"));
	        long id = DataUtility.getLong(request.getParameter("id"));

	        // get model
	        MarksheetBean bean = (MarksheetBean) populateBean(request);


	        if (OP_GO.equalsIgnoreCase(op)) {

	            try {
	                bean = model.findByRollNo(bean.getRollNo());
	                MarksheetModel model = new MarksheetModel();

	                bean=model.findByRollNo(bean.getRollNo());
	             
	      
	                if (bean != null) {
	                    ServletUtility.setBean(bean, request);
	                }else {
	                    ServletUtility.setErrorMessage("RollNo Does Not Exists",request);
	                    
	                }
	            } catch (ApplicationException e) {
	               e.printStackTrace();
	            	log.error(e);
	                ServletUtility.handleException(e, request, response);
	                return;
	            }
	        }
	            else if (OP_RESET.equalsIgnoreCase(op)) {
	            	ServletUtility.redirect(ORSView.GET_MARKSHEET_CTL, request, response);
	            	return ;
				}
	        ServletUtility.forward(getView(), request, response);
	        log.debug("MarksheetCtl Method doGet Ended");
	    }

	    /**
		 * Returns the VIEW page of this Controller
		 * 
		 * @return
		 */
		@Override


	    protected String getView() {
	        return ORSView.GET_MARKSHEET_VIEW ;
	    }

}
