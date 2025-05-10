package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.DTO.FacultyDTO;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Util.FacultyValidator;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.FacultyModel;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.controller.ORSView;

/**
 * The Class FacultyListCtl.
 * 
 * @author Lokesh SOlanki
 */
@WebServlet(name = "FacultyListCtl", urlPatterns = { "/ctl/FacultyListCtl" })
public class FacultyListCtl extends BaseCtl<FacultyDTO> {

	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(FacultyListCtl.class);

	private final FacultyModel model = new FacultyModel();
	
	/**
	 * Preload method to add data to request object.
	 *
	 * @param request the request
	 */
	@Override
	protected void preload(final HttpServletRequest request) {
		log.debug("preload method of FacultyListCtl Started");

		CollegeModel collegeModel = new CollegeModel();
		CourseModel courseModel = new CourseModel();

		try {
			List collegeList = collegeModel.list();
			List courseList = courseModel.list();
			request.setAttribute("CollegeList", collegeList);
			request.setAttribute("CourseList", courseList);
		} catch (ApplicationException e) {
			log.error("Error loading College and Course lists in FacultyListCtl preload", e);
		}
	}

	/**
	 * populateDTO the dto
	 * @param request the request
	 * @return the dto
	 */
	/**
	 * Populates DTO object from request parameters.
	 * 
	 * @param request the request
	 * @return the faculty dto
	 */
	protected FacultyDTO populateDTO(final HttpServletRequest request) {
		final FacultyDTO dto = new FacultyDTO();
        dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        dto.setLastName(DataUtility.getString(request.getParameter("lastName")));
        dto.setEmailId(DataUtility.getString(request.getParameter("emailId")));
		return dto;

	}

	
	/**
	 * Search method.
	 * 
	 * @param bean     the bean
	 * @param pageNo   the page no
	 * @param pageSize the page size
	 * @return the list
	 * @throws ApplicationException the application exception
	 */
	private final List<FacultyDTO> searchFaculty(final FacultyDTO dto, final int pageNo, final int pageSize,
            final String orderBy, final String sortOrder)
			throws ApplicationException {
		return model.search(dto, pageNo, pageSize,orderBy,sortOrder);
	} 

	/**
	 * Validates the data send by the user.
	 * 
	 * @param request the request
	 * @return true if the data is valid
	 */
	@Override
	protected boolean validate(final HttpServletRequest request) {
		return FacultyValidator.validate(request);
	}
	
	
	/**
	 * Contains Display logics. Display List functionality of Faculty List
	 * 
	 * @param request  request
	 * @param response response
	 * @throws ServletException  the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException 
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("doGet method of FacultyCtl Started");
		try {
			FacultyDTO dto = populateDTO(request);			
			int pageNo = 1;
			int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
            String orderBy = DataUtility.getString(request.getParameter("orderBy")); String sortOrder = DataUtility.getString(request.getParameter("sortOrder")); List<FacultyDTO> list = searchFaculty(dto, pageNo, pageSize, orderBy, sortOrder);
			
			
			
			setListAndPagination(list, request, pageNo, pageSize);
			
		} catch (ApplicationException e) {
			handleDatabaseException(e, request, response);
		}
		log.debug("Do get method of FacultyListCtl End");
		


	}

	/**
	 * Delete the Faculty.
	 * 
	 * @param ids      the id of the element to delete
	 * @param request  request
	 * @param response response
	 * @throws ApplicationException the application exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException
	 */
	private final void delete(final String[] ids, final HttpServletRequest request, final HttpServletResponse response)
			throws ApplicationException {
		if (ids != null && ids.length > 0) {
			FacultyDTO dto = new FacultyDTO();
			for (final String id : ids) {
				dto.setId(DataUtility.getInt(id));
				model.delete(dto);
			}
			ServletUtility.setSuccessMessage(PropertyReader.getValue("success.faculty.delete"), request);
		} else {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.select.one"), request);
		}
		
	}

	/**
	 * Contains Submit logics. Submit List functionality of Faculty List
	 * 
	 * @param request  request
	 * @param response response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do Post method of FacultyListCtl Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		String orderBy = "firstName";
		String sortOrder = "asc";
        String[] ids = request.getParameterValues("ids");
        FacultyDTO dto = populateDTO(request);
		if(validate(request)){
			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				
				pageNo--;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				try {
					delete(ids, request, response);
				} catch (ApplicationException e) {
					handleDatabaseException(e, request, response);
					return;
                }
                ServletUtility.setBean(dto,request);
			}

          
            List<FacultyDTO> list = showList(dto, request, response, pageNo, pageSize, orderBy, sortOrder);
            if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
		}
		log.debug("do Post method of FacultyListCtl End");
	}
	}
	
	/**
	 * set the data in the list.
	 * 
	 * @param bean     bean
	 * @param request  request
	 * @param response response
	 * @param pageNo   pageNo
	 * @param pageSize pageSize
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	/**
	 * showList Method. set the data in the list
	 * 
	 * @param bean     the bean
	 * @param request  the request
	 * @param response the response
	 * @param pageNo   the page number
	 * @param pageSize the size of the page
	 * @throws ServletException  the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ApplicationException the application exception
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private final List<FacultyDTO> showList(final FacultyDTO dto, final HttpServletRequest request, final HttpServletResponse response, final int pageNo, final int pageSize,
                                  final String orderBy, final String sortOrder)
			throws ServletException, IOException {
        
	

		log.debug("showList Method Start");
		List<FacultyDTO> list = null;
		try {
			list = searchFaculty(dto, pageNo, pageSize,orderBy,sortOrder);
		} catch (ApplicationException e) {
			handleDatabaseException(e, request, response);
			return;
		}
		if (list.isEmpty()
				&& !OP_DELETE.equalsIgnoreCase(DataUtility.getString(request.getParameter("operation")))) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
		}
		
		setListAndPagination(list, request, pageNo, pageSize);
		return list; 
	}
	/**
	 * setListAndPagination method
	 * @param list  list
	 * @param request request
	 * @param pageNo pageNo
	 * @param pageSize pageSize
	 */
	private final void setListAndPagination(final List<FacultyDTO> list, final HttpServletRequest request,
            final int pageNo, final int pageSize) {
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
	}

	/** 
	 * get the view
	 */
	@Override
	protected String getView() {
		return ORSView.FACULTY_LIST_VIEW;
	}
}

