package com.rays.pro4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.validator.CollegeListValidator;
import com.rays.pro4.controller.ORSView;
import java.io.IOException;

/**
 * College List functionality Controller. Performs operation for list, search
 * and delete operations of College
 * 
 * @author Lokesh SOlanki
 * 
 */
@WebServlet(name = "CollegeListCtl", urlPatterns = { "/ctl/CollegeListCtl" })
public class CollegeListCtl extends BaseCtl<CollegeDTO> {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(CollegeListCtl.class);	
	private final CollegeModel model = new CollegeModel();

	/**
	 * Preload method to load College data.
	 *
	 * @param request the request
	 */
	@Override
	protected void preload(final HttpServletRequest request) {		
		log.debug("preload Method Started");
		String orderBy = DataUtility.getString(request.getParameter("orderBy"));
        String sortOrder = DataUtility.getString(request.getParameter("sortOrder"));
		if (orderBy == null || orderBy.trim().length() == 0) {
			orderBy = "name";
		}
		if (sortOrder == null || sortOrder.trim().length() == 0) {
			sortOrder = "asc";
		}	
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("sortOrder", sortOrder);
		log.debug("preload Method End");
	}	

	/**
	 * Populates bean object from request parameters.
	 *
	 * @param request the request
	 * @return the college bean
	 */
	@Override
	protected CollegeDTO populateDTO(HttpServletRequest request) {
		CollegeDTO dto = new CollegeDTO();
		dto.setName(DataUtility.getString(request.getParameter("collegename")));
		dto.setCity(DataUtility.getString(request.getParameter("city")));
	}	

	
	/**
	 * validate the data send by the user
	 * @param request the request
	 * @return true if the data is valid
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("validate Method Started");
		// As per instruction 5, removing validation call if not needed for list operations.
		// If validation for search criteria is needed, implement it in CollegeListValidator
		// and validate the relevant parameters from the request or a populated DTO.
		final boolean pass = true; 
		log.debug("validate Method End");

		return pass;
	}

	/**
	 * Contains display logic.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
		throws ServletException, IOException {
		log.debug("CollegeListCtl Method doGet Started");
		final CollegeDTO dto = populateDTO(request);
		final int pageNo = 1;
		final int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		try {
			String orderBy = DataUtility.getString(request.getParameter("orderBy")); String sortOrder = DataUtility.getString(request.getParameter("sortOrder")); // Ensure orderBy and sortOrder are used
            showList(bean, request, response, pageNo, pageSize,orderBy,sortOrder);
        
		} catch (Exception e) {
			handleDatabaseException(e, request, response);
		}
		log.debug("CollegeListCtl Method doGet Ended");
	}


	/**
	 * Delete the College
	 * @param ids the id of the element to delete
	 * @param request the request
	 * @param response the response
	 * @throws ApplicationException
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private final void delete(final String[] ids, final HttpServletRequest request, final HttpServletResponse response)
			throws ApplicationException {
		log.debug("delete method start");
		if (ids == null || ids.length == 0) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.select.one"), request);
			return;
		}
		try {
			for (final String id : ids) {
				model.delete(DataUtility.getLong(id)); // Instruction 1: Call delete by ID directly
			}
			ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_SUCCESS_DELETE, request);
			log.debug("delete method end");
		} catch (ApplicationException e) { // Instruction 2: Catch ApplicationException
			log.error("Error during college delete", e);
		}
	}

	/**
	 * Contains submit logic.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
		    throws ServletException, IOException {
		        log.debug("CollegeListCtl doPost Start");
		        List<CollegeDTO> list = new ArrayList<CollegeDTO>();
		        int pageNo = DataUtility.getInt(request.getParameter("pageNo")) == 0 ? 1 : DataUtility.getInt(request.getParameter("pageNo"));
		        int pageSize = DataUtility.getInt(request.getParameter("pageSize"))== 0 ? DataUtility.getInt(PropertyReader.getValue("page.size")) : DataUtility.getInt(request.getParameter("pageSize"));
		        final String op = DataUtility.getString(request.getParameter("operation"));
		        String[] ids = request.getParameterValues("ids");		
				String orderBy = DataUtility.getString(request.getParameter("orderBy"));				
				String sortOrder = DataUtility.getString(request.getParameter("sortOrder"));
		        CollegeDTO dto = (CollegeDTO) populateDTO(request); // Use CollegeDTO
				// Populate search criteria into the DTO
				dto.setName(DataUtility.getString(request.getParameter("collegename"))); // Assuming collegename is the filter parameter name
				dto.setCity(DataUtility.getString(request.getParameter("city")));
				// Add other search criteria as needed

		        try {
		            if (OP_SEARCH.equalsIgnoreCase(op)) {
		                pageNo = 1;
						list = model.search(dto, pageNo, pageSize, orderBy, sortOrder); // Instruction 4: Simplify search logic
		            } else if (OP_NEXT.equalsIgnoreCase(op)) {
		                pageNo++;
					    list = model.search(dto, pageNo, pageSize,  orderBy, sortOrder);
		            } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
		                pageNo--;
					    list = model.search(dto, pageNo, pageSize,  orderBy, sortOrder);
		            } else if (OP_NEW.equalsIgnoreCase(op)) {
		                ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
		                return;
		            } else if (OP_DELETE.equalsIgnoreCase(op)) {
		                pageNo = 1;
		                delete(ids, request, response); // Call the delete method
						// After deletion, either show the updated list or handle the case where no records were selected
						list = model.search(dto, pageNo, pageSize, orderBy, sortOrder); // Instruction 3: Show the updated list
		            } else {
		        showList(bean, request, response, pageNo, pageSize,orderBy,sortOrder);
		        } catch (Exception e) {
		            
		            log.error(e);
		            handleDatabaseException(e, request, response);
		            return;
		        }
		        log.debug("CollegeListCtl doPost End");
	}

	/**
	 * set the data in the list
	 * 
	 * @param bean     the bean
	 * @param request  the request
	 * @param response the response
	 * @param pageNo   the page number
	 * @param pageSize the size of the page
	 * @throws ServletException
	 * @throws IOException
	 */
	private final void showList(final CollegeDTO dto, final HttpServletRequest request, final HttpServletResponse response, final int pageNo, final int pageSize, String orderBy, String sortOrder) throws ServletException, IOException {
		log.debug("showList Method Start");
		List<CollegeDTO> list = new ArrayList<CollegeDTO>(); // Instruction 6: Use CollegeDTO
		try {
			list = model.search(dto, pageNo, pageSize, orderBy, sortOrder); // Use model.search directly
			final List<CollegeDTO> nextList = model.search(dto, pageNo + 1, pageSize, orderBy, sortOrder); // Use model.search directly
			request.setAttribute("nextlist", nextList.size());
		
			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage(PropertyReader.getValue("error.record.notfound"), request);				
				
			}		
			
		} catch (final ApplicationException e) {
			log.error("error getting the list of college",e);
			handleDatabaseException(e,request,response);
			return;
		}
         setListAndPagination(list, request, pageNo, pageSize, response, orderBy, sortOrder);
		
	}	private final void setListAndPagination(final List<?> list, final HttpServletRequest request, final int pageNo,final int pageSize, HttpServletResponse response,String orderBy, String sortOrder) {
		log.debug("setListAndPagination method start");
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("sortOrder", sortOrder);
		ServletUtility.setList(list, request);			ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);		
		ServletUtility.forward(getView(), request, response);
		log.debug("setListAndPagination method end");
	}
	@Override protected String getView() {
		return ORSView.COLLEGE_LIST_VIEW;
	}	
}
