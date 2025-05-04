package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;

import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.validator.MarksheetValidator;
import org.apache.log4j.Logger;

/**
 * Marksheet functionality Controller. Performs operation for add, update,
 * delete and get Marksheet
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "MarksheetCtl", urlPatterns = {"/ctl/MarksheetCtl"})
public class MarksheetCtl extends BaseCtl<MarksheetBean> { // Start of class

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(MarksheetCtl.class);

    /**
     * Loads pre-load data
     *
     * @param request the request
     */
    @Override
    protected void preload(final HttpServletRequest request) {
        {
            StudentModel model = new StudentModel();
            try {
                List<StudentBean> list = model.list();
                request.setAttribute("studentList", list);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected boolean validate(final HttpServletRequest request) {
        log.debug("MarksheetCtl Method validate Started");
        final MarksheetBean bean = new MarksheetBean();
        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
        bean.setStudentld(DataUtility.getLong(request.getParameter("studentId")));
        bean.setPhysics(DataUtility.getInt(request.getParameter("physics")));
        bean.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
        bean.setMaths(DataUtility.getInt(request.getParameter("maths")));
        boolean pass = true;
        Map<String, String> map = MarksheetValidator.validate(bean);
        if (!map.isEmpty()) {
            pass = false;
            for (Map.Entry<String, String> entry : map.entrySet()) {


                request.setAttribute(entry.getKey(), entry.getValue());
            }
        }

        log.debug("MarksheetCtl Method validate Ended");
        return pass;
    }

    protected MarksheetDTO populateDTO(HttpServletRequest request, MarksheetDTO dto) {
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
        dto.setStudentId(DataUtility.getLong(request.getParameter("studentId")));
        dto.setPhysics(DataUtility.getInt(request.getParameter("physics")));
        dto.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
        dto.setMaths(DataUtility.getInt(request.getParameter("maths")));
        return dto;
    }

    @Override
    protected MarksheetBean populateBean(final HttpServletRequest request) {

        log.debug("MarksheetCtl Method populatebean Started");
        MarksheetBean bean = new MarksheetBean();
        bean.populate(request);
        log.debug("MarksheetCtl Method populatebean Ended");
        return bean;
    }

    protected void populateBean(HttpServletRequest request, MarksheetBean bean) {
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
        bean.setStudentld(DataUtility.getLong(request.getParameter("studentId")));
        bean.setPhysics(DataUtility.getInt(request.getParameter("physics")));
        bean.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
        bean.setMaths(DataUtility.getInt(request.getParameter("maths")));
    }

	/**
     * Contains Display logics.
     *
     * @param request  the request
     * @param response the response
     * @throws javax.servlet.ServletException the servlet exception
     * @throws java.io.IOException Signals that an I/O exception has occurred.
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        {
            MarksheetModel model = new MarksheetModel();
            long id = DataUtility.getLong(request.getParameter("id"));
            if (id > 0 || op != null) {

                MarksheetBean bean;
                try {

                    MarksheetDTO dto = model.findByPK(id);
                    bean = new MarksheetBean();
                    bean.setDTO(dto);
                    ServletUtility.setBean(bean, request);
                 } catch (ApplicationException e) {
                  log.error("Error finding Marksheet by ID", e);
                  handleDatabaseException(e, request, response);
                  return;
            }
        }
        }
        ServletUtility.forward(getView(), request, response);
    }
    /**
     * Contains Submit logics.
     *
     * @param request  the request        
     * @param response the response
     * @throws javax.servlet.ServletException the servlet exception
     * @throws java.io.IOException Signals that an I/O exception has occurred.
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            String op = null;
        
            op = DataUtility.getString(request.getParameter("operation"));
            final MarksheetBean bean = (MarksheetBean) populateBean(request);
            MarksheetModel model = new MarksheetModel();
            MarksheetDTO dto = new MarksheetDTO();
            final long id = DataUtility.getLong(request.getParameter("id"));

            if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
                try {
                    if (validate(request)) {
                        if (id > 0) {
                             ServletUtility.setSuccessMessage(MessageConstant.MARKSHEET_UPDATE, request);
                            populateBean(request, bean);
                            model.update(bean.getDTO());
                           } else {
                            populateBean(request, bean);
                            ServletUtility.setSuccessMessage(MessageConstant.MARKSHEET_ADD, request);
                            model.add(bean.getDTO());
                        }
                    } else {

                        ServletUtility.setBean(bean, request);
                    }
                } catch (ApplicationException e) {
                    log.error("Application exception", e);

                    handleDatabaseException(e, request, response);
                } catch (DuplicateRecordException e) {
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setErrorMessage(e.getMessage(), request);
                }
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.MARKSHEET_CTL, request, response);
                return;
            } else if (OP_CANCEL.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request, response);
                return;
            }
            
            if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
                 ServletUtility.setBean(bean, request);  
            }         
              ServletUtility.forward(getView(), request, response);
           
        }
    
    /**
     * Returns the VIEW page of this Controller
     *
     * @return
     */
    @Override
    protected String getView() {
        return ORSView.MARKSHEET_VIEW;
    }

}// End of class

