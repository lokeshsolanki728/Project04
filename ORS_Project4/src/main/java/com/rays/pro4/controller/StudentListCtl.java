package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.validator.StudentValidator;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

import java.util.ArrayList;
/**
 * Student List functionality Controller. Performs operation for list, search
 * and delete operations of Student
 *
 * @author Lokesh SOlanki
 */
public class StudentListCtl extends BaseCtl {
    private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(StudentListCtl.class);
    private final StudentModel model = new StudentModel();

    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("preload method of StudentListCtl Started");
        final CollegeModel cmodel = new CollegeModel();

        try {
            List clist = cmodel.list(0, 0);
            request.setAttribute("CollegeList", clist);

        } catch (final ApplicationException e) {
            log.error(e);

        }
        log.debug("preload method of StudentListCtl Ended");

    }

    @Override
    protected StudentDTO populateDTO(HttpServletRequest request) {
        log.debug("populateDTO method of StudentListCtl Started");
        StudentDTO dto = new StudentDTO();
        dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        dto.setLastName(DataUtility.getString(request.getParameter("lastName")));
        dto.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
        log.debug("populateDTO method of StudentListCtl Ended");
        return dto;
    }

    /**
     * Contains display logics.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("doGet method of StudentListCtl Started");
        List<StudentDTO> list = null;

        StudentDTO dto = new StudentDTO();
        String orderBy = DataUtility.getString(request.getParameter("orderBy"));
        String sortOrder = DataUtility.getString(request.getParameter("sortOrder"));
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));        

        String op = DataUtility.getString(request.getParameter("operation"));  
        if(!isValidOrderByColumnListCtl(orderBy)){
            orderBy=null;
        }        
        final StudentDTO dto1 = populateDTO(request);
        try {

            list = model.search(dto1, pageNo, pageSize,orderBy,sortOrder);

            ServletUtility.setList(list, request);

            if (list == null || list.size() == 0) {

                ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
            }

            ServletUtility.setList(list, request);

            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);

            ServletUtility.forward(getView(), request, response);

        } catch (final ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug("doGet method of StudentListCtl Ended");
    }

    /**
     * Contains Submit logics.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("doPost method of StudentListCtl Started");

        List<StudentDTO> list = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        String orderBy = DataUtility.getString(request.getParameter("orderBy"));
        String sortOrder = DataUtility.getString(request.getParameter("sortOrder"));
        if(!isValidOrderByColumnListCtl(orderBy)){orderBy=null;}
        
        final String op = DataUtility.getString(request.getParameter("operation"));
        final StudentDTO dto = populateDTO(request);

        try {


            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {                    
                    if(!StudentValidator.validateSearch(dto)){
                        ServletUtility.setErrorMessage(PropertyReader.getValue("error.require", "Please enter at least one search criteria"),request);
                        ServletUtility.setList(list, request);
                        ServletUtility.forward(getView(), request, response);
                        return;
                    }

                    pageNo = 1;                    

                }
                else if (OP_NEXT.equalsIgnoreCase(op)) {

                    pageNo++;                    

                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {

                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                String[] ids = request.getParameterValues("ids");
                List<String> successfulDeletes = new ArrayList<>();
                List<String> failedDeletes = new ArrayList<>();
                StudentDTO deleteDto = new StudentDTO();
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        deleteDto.setId(DataUtility.getInt(id));
                        try {
                            model.delete(deleteDto);
                            successfulDeletes.add(id);
						} catch (ApplicationException e) {
                            log.error(e);
                            failedDeletes.add(id);
						}
                    }
                    if (!failedDeletes.isEmpty()) {
                        ServletUtility.setErrorMessage("Could not delete IDs: " + String.join(", ", failedDeletes), request);
                    }
                    if (!successfulDeletes.isEmpty()) {
                        ServletUtility.setSuccessMessage("Successfully deleted IDs: " + String.join(", ", successfulDeletes), request);
                    }
                } else {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.require", "Select at least one record"),
                            request);
                }
            }
            
             list = model.search(dto, pageNo, pageSize, orderBy, sortOrder);
            
            if (list.isEmpty()) {
                ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
            }
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (final ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
        log.debug("doPost method of StudentListCtl Ended");
    }
    
    /**
     * Returns the VIEW page of this Controller.
     *
     * @return the view
     */
	@Override
	protected String getView() {
		return ORSView.STUDENT_LIST_VIEW;
	}    
    private boolean isValidOrderByColumnListCtl(String columnName) {        if (columnName == null || columnName.isEmpty()) {            return true;        }        String[] validColumns = {"ID","firstName", "lastName", "dob", "collegeName", "mobileNo"};        for (String validColumn : validColumns) {            if (validColumn.equalsIgnoreCase(columnName)) {                return true;            }        }        return false;    }}
