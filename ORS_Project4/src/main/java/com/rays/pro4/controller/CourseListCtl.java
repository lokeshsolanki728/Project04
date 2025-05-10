package com.rays.pro4.controller;

import com.rays.pro4.controller.ORSView;
import java.util.List;
import javax.servlet.ServletException;
import com.rays.pro4.DTO.CourseDTO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import com.rays.pro4.Exception.ApplicationException;
import org.apache.log4j.Logger;

import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.validator.CourseValidator;
import com.rays.pro4.Util.DataValidator;
import java.util.HashMap;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;;
import com.rays.pro4.Util.ServletUtility;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

/**
 * The Class CourseListCtl.
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "CourseListCtl", urlPatterns = { "/ctl/CourseListCtl" })
public class CourseListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	public static final Logger log = Logger.getLogger(CourseListCtl.class);
	private final CourseModel model = new CourseModel(); // Use final keyword

	/**
	 * Populates DTO object from request parameters
	 * @param request the request
	 * @return the course DTO
	 */
	@Override
	protected CourseDTO populateDTO(final HttpServletRequest request) {
		final CourseDTO dto = new CourseDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setDuration(DataUtility.getString(request.getParameter("duration")));
		updateDTO(dto, request);
		return dto;
	}

	/**
	 * Search Course.
	 *
	 * @param bean      the bean
	 * @param pageNo    the page no
	 * @param pageSize  the page size
	 * @param orderBy   the order by
	 * @param sortOrder the sort order
	 * @return the list
	 * @throws ApplicationException the application exception
	 */
	private List<CourseDTO> searchCourse(final CourseDTO dto, final int pageNo, final int pageSize,
			final String orderBy, final String sortOrder) throws ApplicationException {
		log.debug("searchCourse method start");
		List<CourseDTO> list = model.search(dto, pageNo, pageSize, orderBy, sortOrder);
		log.debug("searchCourse method end");
		return list;
	}
	
	
	
	/**
	 * Contains Display logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("do get method of CourseListCtl Started");
		List<CourseDTO> list = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CourseDTO dto = populateDTO(request);
		String orderBy = "name";
		String sortOrder = "asc";
		
		try {	
			list = searchCourse(dto, pageNo, pageSize, orderBy, sortOrder);

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
			
			
		} catch (final ApplicationException e) {
			ServletUtility.setList(list, request);
			handleDatabaseException(e, request, response);
		}	
		log.debug("doGet method of CourseListCtl End");
	}
	
	/**
	 * Validates input data entered by User
	 *
	 * @param request the request
	 * @return the boolean
	 */
	@Override
	protected final boolean validate(HttpServletRequest request) {
		log.debug("validate Method Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		if (OP_SEARCH.equalsIgnoreCase(op)) {
			CourseDTO dto = populateDTO(request);
			boolean pass=CourseValidator.validate(dto);
			if(!pass) return pass;
		}


		boolean pass=CourseValidator.validate(dto);
		if(!pass) return pass;
		log.debug("validate Method End");
		return pass;
	}
	

	
	/**
	 * Delete.
	 *
	 * @param ids the ids
	 * @param request the request
	 * @param response the response
	 * @throws ApplicationException the application exception
	 */
	private final void delete(final String[] ids, final HttpServletRequest request, final HttpServletResponse response)
			throws ApplicationException {
		log.debug("delete method started");
		if (ids != null && ids.length > 0) {
			for (final String id : ids) {
				model.delete(model.findByPK(DataUtility.getInt(id)));
			}
			ServletUtility.setSuccessMessage(MessageConstant.COURSE_SUCCESS_DELETE, request);
		} else {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.select.one"), request);			
			
		}
		log.debug("delete method end");
	}
	/**
	 * Contains Submit logics.
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred. 
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		log.debug("do Post method of CourseListCtl Started");
		if(!validate(request)) {
			 ServletUtility.forward(getView(), request, response);
			 return;
		}
		
		List<CourseDTO> list = null;
		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");
		String orderBy = "name";
		String sortOrder = "asc";
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		CourseDTO dto = populateDTO(request);
		
		
		try {

			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
					pageNo--;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op) && ids!=null) {
				pageNo = 1;
					delete(ids, request, response);
			}else if(OP_BACK.equalsIgnoreCase(op)){
				list = searchCourse(dto, pageNo, pageSize, orderBy, sortOrder);
				if(list==null || list.isEmpty()) {
					ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);
				}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			
			ServletUtility.handleException(e, request, response);
		}
		
		log.debug("do Post method of CourseListCtl End");
	}
	@Override
	protected String getView() {
		return ORSView.COURSE_LIST_VIEW;
	}
}
