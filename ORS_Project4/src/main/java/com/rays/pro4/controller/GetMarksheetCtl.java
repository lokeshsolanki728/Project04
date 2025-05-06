package com.rays.pro4.controller;

import com.rays.pro4.controller.ORSView;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Get Marksheet functionality Controller. Performs operation for Get Marksheet
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "GetMarksheetCtl", urlPatterns = {"/ctl/GetMarksheetCtl"})
public class GetMarksheetCtl extends BaseCtl<MarksheetBean> {

	private static Logger log = Logger.getLogger(GetMarksheetCtl.class);

	/** the model */
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
        boolean pass = true;
        if (DataValidator.isNull(request.getParameter("rollNo"))) {
            request.setAttribute("rollNo", PropertyReader.getValue("error.require", "RollNo"));
            pass = false;
        }
		log.debug("GetMarksheetCTL Method validate Ended");
		return pass;
	}

	
	/**
	 * Populates bean object from request parameters.
     *
	 * @param request the request
	 * @return the marksheet bean
	 */
	protected final MarksheetBean populateBean(final HttpServletRequest request) {
		final MarksheetBean bean = new MarksheetBean();
        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		return bean;
	}
	/**
	 * Concept of Display method.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.	
	 */

	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
        log.debug("GetMarksheetCtl Method doGet Started");
        log.debug("GetMarksheetCtl Method doGet Ended");
	}

	/**
	 * Concept of Submit Method.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
     */


	private void getMarksheet(final MarksheetDTO dto, final HttpServletRequest request) throws ApplicationException {
        log.debug("getMarksheet Method Started");
        final MarksheetDTO marksheetDTO = model.findByRollNo(dto.getRollNo());
        if (marksheetDTO == null) {
            ServletUtility.setErrorMessage("RollNo Does Not Exists", request);
            ServletUtility.setBean(new MarksheetBean(),request);
        } else {
            ServletUtility.setBean(marksheetDTO.getBean(), request);
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
        MarksheetBean populateBean = new MarksheetBean();
        populateBean.setRollNo(request.getParameter("rollNo"));
        dto.setRollNo(request.getParameter("rollNo"));
        dto.setName(request.getParameter("name"));
        dto.setPhysics(DataUtility.getInt(request.getParameter("physics")));
        dto.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
        dto.setMaths(DataUtility.getInt(request.getParameter("maths")));

		if (OP_GO.equalsIgnoreCase(operation)) {
			if (validate(request)) {				
                try {
                    getMarksheet(dto, request);
                } catch (final ApplicationException e) {
                    log.error("Error in get marksheet", e);
                    MarksheetBean bean = populateBean(request);
                    ServletUtility.setBean(bean, request);
					handleDatabaseException(e, request, response);
					return;
				}
			}
		} else if (OP_RESET.equalsIgnoreCase(operation)) {
			ServletUtility.redirect(ORSView.GET_MARKSHEET_CTL, request, response);
			return;
		}
       if(!validate(request)){
            ServletUtility.setBean(populateBean(request),request);
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
