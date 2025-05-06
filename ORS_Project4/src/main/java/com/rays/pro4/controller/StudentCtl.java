package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.DataTransferUtility;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
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
            List<CollegeBean> collegeList = collegeModel.list();
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
    protected boolean validate(HttpServletRequest request) {
        log.debug("StudentCtl Method validate Started");
        boolean pass = true;
        StudentBean bean=new StudentBean();
        bean.setFirstName(DataUtility.getString(request.getParameter("firstname")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastname")));
        try {
            bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        } catch (Exception e) {
            request.setAttribute("dob", "Invalid Date Format");
            pass=false;
        }
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobile")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegename")));

        Map<String, String> map=StudentValidator.validate(bean);
        if(!map.isEmpty()){
            pass=false;
            for(Map.Entry<String, String> entry : map.entrySet()){
                request.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        if(!pass){
            log.debug("StudentCtl Method validate Ended with error");
        }
        return pass;
    }
    
    /**
     * Populates bean object from request parameters.
     *
     * @param request the request
     */
    protected void populateBean(HttpServletRequest request, StudentBean bean){

        log.debug("StudentCtl Method populateBean Started");
        bean.setFirstName(DataUtility.getString(request.getParameter("firstname")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastname")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobile")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegename")));
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setCreatedBy(DataUtility.getString(request.getParameter("createdby")));
        bean.setModifiedBy(DataUtility.getString(request.getParameter("modifiedby")));

        log.debug("StudentCtl Method populateBean Ended");
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
                }
                 StudentBean bean= new StudentBean();
                DataTransferUtility.copyDtoToBean(dto, bean);

                ServletUtility.setBean(bean, request);

            } catch (final ApplicationException e) {
                log.error("Error finding student by ID", e);
                handleDatabaseException(e, request, response);
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
    private void saveStudent(StudentBean bean, StudentModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("saveStudent method start");
        model.add(bean);
        ServletUtility.setSuccessMessage(MessageConstant.STUDENT_ADD, request);
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
    private void updateStudent(StudentBean bean, StudentModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("updateStudent method start");
         model.update(bean.getDTO());
        ServletUtility.setSuccessMessage(MessageConstant.STUDENT_UPDATE, request);
        log.debug("updateStudent method end");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("StudentCtl Method doPost Started");

        String operation = DataUtility.getString(request.getParameter("operation"));
        StudentBean bean = new StudentBean();
        populateBean(request, bean);

        try {
            if (OP_SAVE.equalsIgnoreCase(operation) || OP_UPDATE.equalsIgnoreCase(operation)) {
                if(!validate(request)){
                    ServletUtility.setBean(bean, request);
                    ServletUtility.forward(getView(), request, response);
                    return;
                }
                if (bean.getId() > 0) {
                    updateStudent(bean, model, request);
                } else {
                    saveStudent(bean, model, request);
                }
                ServletUtility.forward(getView(), request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(operation)) {
                ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
                return;
            }else if (OP_CANCEL.equalsIgnoreCase(operation)){
                ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
                return;
            }
        } catch (ApplicationException e) {
                log.error("Application exception", e);
                handleDatabaseException(e, request, response);
                return;                
            } catch (final DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Student Email Id already exists", request);
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
