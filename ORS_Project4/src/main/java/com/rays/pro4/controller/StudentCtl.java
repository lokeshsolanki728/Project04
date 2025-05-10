package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Model.StudentModel;
import java.util.List;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ORSView;
import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.validator.StudentValidator;

/**
 * Student functionality Controller. Performs operation for add, update, delete
 * and get Student
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "StudentCtl", urlPatterns = {"/ctl/StudentCtl"})
public class StudentCtl extends BaseCtl {

    /** The log. */
    private static Logger log = Logger.getLogger(StudentCtl.class);
    private final StudentModel model = new StudentModel();
    
    /**
     * Loads pre-load data
     *
     * @param request the request
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("preload method of StudentCtl Started");

        final CollegeModel collegeModel = new CollegeModel();
        try {
             List collegeList = collegeModel.list();
            request.setAttribute("collegeList", collegeList);
        } catch (final ApplicationException e) {
            log.error("Error getting college list during preload", e);
        }
        log.debug("preload method of StudentCtl Ended");
    }


    /**
     * Validates input data entered by User
     *
     * @param request the request
     * @return
     */
    @Override
 protected boolean validate(StudentDTO dto) {
        log.debug("StudentCtl Method validate Started");
        
       boolean pass = StudentValidator.validate(dto);
       return pass;
    }

    /**
     * Populates DTO object from request parameters.
     *
     * @param request the request
     * @param request the request
     */
    protected void populateDTO(HttpServletRequest request, StudentDTO dto){
        log.debug("StudentCtl Method populateDTO Started");
        dto.setFirstName(DataUtility.getString(request.getParameter("firstname")));
        dto.setLastName(DataUtility.getString(request.getParameter("lastname")));
        dto.setDob(DataUtility.getDate(request.getParameter("dob")));
        dto.setMobileNo(DataUtility.getString(request.getParameter("mobile")));
        dto.setEmail(DataUtility.getString(request.getParameter("email")));
        dto.setCollegeId(DataUtility.getLong(request.getParameter("collegename")));
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setCreatedBy(DataUtility.getString(request.getParameter("createdby")));
        dto.setModifiedBy(DataUtility.getString(request.getParameter("modifiedby")));
        
        log.debug("StudentCtl Method populateDTO Ended");
    }

    /**
     * Contains Display logics.
     *
     * @param request  the request
     * @param response the response
     * @throws javax.servlet.ServletException the servlet exception
     * @throws java.io.IOException            Signals that an I/O exception has occurred.
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.debug("StudentCtl Method doGet Started");

        final long id = DataUtility.getLong(request.getParameter("id"));
        final String operation = DataUtility.getString(request.getParameter("operation"));

        if (id > 0) {
            StudentDTO dto;
            try {
                dto = model.findByPK(id);
                if(dto == null){
                	ServletUtility.setErrorMessage("Student not found", request);
                } ServletUtility.setDto(dto, request);
            }catch (final ApplicationException e) {
                ServletUtility.setErrorMessage("Error fetching student details: " + e.getMessage(), request);
                ServletUtility.forward(getView(), request, response);
                log.error("Error finding student by ID", e);
          return;
            }
        }
        ServletUtility.forward(getView(), request, response);
    }    

    
    /**
     * Save student.
     *
     * @param bean the bean
 * @param model the model
 * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException the application exception
     */
    private void saveStudent(StudentDTO dto, StudentModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("saveStudent method start");
        model.add(dto);       
        ServletUtility.setSuccessMessage("Student Added Successfully", request);
        log.debug("saveStudent method end" );
    }

    /**
     * Update student.
     *
     * @param bean the bean
     * @param model the model
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException the application exception
     */
    private void updateStudent(StudentDTO dto, StudentModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("updateStudent method start");
         model.update(dto);
        ServletUtility.setSuccessMessage("Student Update Successfully", request);
        log.debug("updateStudent method end");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

           log.debug("StudentCtl Method doPost Started");
            StudentDTO dto = new StudentDTO();
            String operation = DataUtility.getString(request.getParameter("operation"));

            
            try {
            	
               if (OP_SAVE.equalsIgnoreCase(operation) || OP_UPDATE.equalsIgnoreCase(operation)) {
                    if (!validate(dto)) {
 populateDTO(request, dto);

                        ServletUtility.setDto(dto, request);
                        ServletUtility.forward(getView(), request, response);
                        return;
                    }
                    if (dto.getId() > 0) {
                         updateStudent(dto, model, request);
                    } else {                        
                        // Set createdBy, modifiedBy, createdDatetime, and modifiedDatetime
                        // Use placeholder values for now
                        dto.setCreatedBy("admin"); // Placeholder
                        dto.setModifiedBy("admin"); // Placeholder
                        dto.setCreatedDatetime(new java.sql.Timestamp(System.currentTimeMillis())); // Placeholder
                        // dto.setCreatedDatetime(new Timestamp(System.currentTimeMillis()));
                        dto.setModifiedDatetime(new java.sql.Timestamp(System.currentTimeMillis())); // Placeholder

                         saveStudent(dto, model, request);
                    }
                    ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(operation)) {
                ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
                return;
            }else if (OP_CANCEL.equalsIgnoreCase(operation)){
                ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
                return;

            }
           } catch ( DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("Student Email Id already exists", request);
        } catch (ApplicationException e) {
                log.error("Application exception", e);
                return;                
            }
        ServletUtility.forward(getView(), request, response);

        log.debug("StudentCtl Method doPost Ended");
    }


    /** 
	 * Returns the VIEW page of this Controller
	 * @return
	 */
    @Override
    protected String getView() {
        return ORSView.STUDENT_VIEW;
    }

}
