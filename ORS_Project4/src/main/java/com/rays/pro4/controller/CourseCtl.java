package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.DTO.CourseDTO;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.ORSView;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


@WebServlet(name="CourseCtl", urlPatterns={"/ctl/CourseCtl"})
public class CourseCtl extends BaseCtl<CourseDTO>{

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CourseCtl.class);


	/**
	 * The model
	 */
	private final static CourseModel model = new CourseModel();

	/**
	 * Validates input data entered by User.
	 * @param request the request
	 * @return true, if successful
	 */
	@Override
	protected boolean validate(final HttpServletRequest request) {
		log.debug("CourseCtl validate started");
		// Populate the DTO before validation
		CourseDTO dto = populateDTO(request);
		final boolean pass = com.rays.pro4.validator.CourseValidator.validate(dto);

		log.debug("CourseCtl validate End");
		return pass;
	}



	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	@Override
    protected CourseDTO populateDTO(HttpServletRequest request) {
        log.debug("CourseCtl populateBean method start");
        CourseDTO dto = new CourseDTO();
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setName(DataUtility.getString(request.getParameter("name")));
        dto.setDescription(DataUtility.getString(request.getParameter("description")));
        dto.setDuration(DataUtility.getString(request.getParameter("duration")));
        updateDTO(dto, request);

		log.debug("CourseCtl populate end");
        return dto;
	}



	


	

	/**
	 * Contains Display logics
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 **/
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		log.debug("CourseCtl Method doGet Started");
        String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		CourseDTO dto = new CourseDTO();
        if(id>0 || op != null){
            try{
                dto = model.findByPK(id);
                if(dto==null) ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);                
                
                ServletUtility.setDto(dto, request); // Make sure the DTO is set for the JSP


            }catch(ApplicationException e){
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
		else {
			ServletUtility.setDto(dto, request);
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("CourseCtl Method doGet Ended");
	}
	
	
    
    
	/**
	 * Contains Submit logics
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CourseCtl Method doPost Started");
		final String op = DataUtility.getString(request.getParameter("operation"));
		CourseDTO dto = populateDTO(request);
		if(validate(request)){			
			if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
				try {
	                CourseDTO courseDTO = dto;
	                if(dto.getId() > 0){
	                	model.update(courseDTO);
	                	ServletUtility.setSuccessMessage("Course update Successfully", request);
	                }else {
	                	long pk = model.add(courseDTO);
	                	dto.setId(pk); // Update the ID in the DTO
	                	ServletUtility.setSuccessMessage("Course added Successfully", request);
	                }
	            }catch (ApplicationException e) {
	                log.error(e);
                    if (e instanceof DuplicateRecordException) {
                        ServletUtility.setErrorMessage("Course Name already exists", request);
                    } else {
                        ServletUtility.setErrorMessage(PropertyReader.getValue("error.app.exception"), request);
                    }
                    ServletUtility.setDto(dto, request); // Set the DTO back with errors
                    

	            }
            }
		    else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
				return;
			} else if (OP_CANCEL.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
				return;
			}
			ServletUtility.setDto(dto, request);
		}
	   

		ServletUtility.forward(getView(), request, response);
		log.debug("CourseCtl Method doPost Ended");
	}
	@Override
	protected String getView() {
		return ORSView.COURSE_VIEW;
	}
}
