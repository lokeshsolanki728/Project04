package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.GetMarksheetValidator;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
/**
 * Get Marksheet functionality Controller. Performs operation for Get Marksheet
 *
 * @author Lokesh SOlanki
*/
@WebServlet(name = "GetMarksheetCtl", urlPatterns = { "/ctl/GetMarksheetCtl" })
public class GetMarksheetCtl extends BaseCtl<MarksheetBean> {

	private static Logger log = Logger.getLogger(GetMarksheetCtl.class);

	/**
	 * the model
	 */
	private final MarksheetModel model = new MarksheetModel();
	
	/**
	 * Validates input data entered by User
	 *
	 * @param request the request
	 * @return true, if successful
	 */
	@Override
	protected boolean validate(final HttpServletRequest request) {
		log.debug("GetMarksheetCTL Method validate Started");
		final boolean pass = GetMarksheetValidator.validate(request);
		log.debug("GetMarksheetCTL Method validate Ended");
		return pass;
	}

	protected void populateBean(HttpServletRequest request, MarksheetBean bean) {
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

	}

	/**
	 * Populates dto object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected final void populateDTO(final HttpServletRequest request, MarksheetDTO dto) {
		log.debug("GetMarksheetCtl Method populateBean Started");
		dto.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		log.debug("GetMarksheetCtl Method populatebean Ended");
	}
	
	/**
	 * Populates bean object from request parameters
	 */
	protected final MarksheetBean populateBean(final HttpServletRequest request) {
		final MarksheetBean bean = new MarksheetBean();
		bean.populate(request);
		log.debug("GetMarksheetCtl Method populatebean Ended");
		return bean;
	}



	/**
	 * Concept of Display method.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.	
	 */

	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("GetMarksheetCtl Method doGet Started");		
		ServletUtility.forward(getView(), request, response);
		log.debug("GetMarksheetCtl Method doGet Ended");
	}

	/**
	 * Concept of Submit Method.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */


	private void getMarksheet(final MarksheetDTO dto, final HttpServletRequest request) throws ApplicationException {
		log.debug("getMarksheet Method Started");
        final MarksheetDTO marksheetDTO = model.findByRollNo(dto.getRollNo());
        if (marksheetBean == null) {
            ServletUtility.setErrorMessage("RollNo Does Not Exists", request);
        } else {
            ServletUtility.setBean(marksheetBean, request);
        }
        log.debug("getMarksheet Method End");
	}

	/**
	 * Do post.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("GetMarksheetCtl Method doPost Started");
		final String operation = DataUtility.getString(request.getParameter("operation"));

		final MarksheetDTO dto = new MarksheetDTO();
		populateDTO(request, dto);

		if (OP_GO.equalsIgnoreCase(operation)) {
			if (validate(request)) {				
				try {
					getMarksheet(dto, request);					
				} catch (final ApplicationException e) {
					log.error("Error in get marksheet", e);
					handleDatabaseException(e, request, response);
					return;
				}
			}
		} else if (OP_RESET.equalsIgnoreCase(operation)) {
			ServletUtility.redirect(ORSView.GET_MARKSHEET_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("MarksheetCtl Method do post Ended");
	}	

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	@Override
	protected String getView() {
		return ORSView.GET_MARKSHEET_VIEW;
	}
}
