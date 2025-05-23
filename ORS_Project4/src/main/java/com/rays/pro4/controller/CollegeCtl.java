package com.rays.pro4.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.DTO.BaseDTO;
import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

import com.rays.pro4.validator.CollegeValidator;


@WebServlet(name = "CollegeCtl", urlPatterns = { "/ctl/CollegeCtl" })
public class CollegeCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(CollegeCtl.class);
    private final CollegeModel model = new CollegeModel();

    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("CollegeCtl Method validate Started");
        boolean pass = true;
         if (OP_DELETE.equalsIgnoreCase(DataUtility.getString(request.getParameter("operation")))) {
            return true;
        }
        pass = CollegeValidator.validate(request);
        log.debug("CollegeCtl Method validate Ended");
        return pass;
    }

    @Override
    protected void populateDTO(HttpServletRequest request, CollegeDTO dto) {
        super.populateDTO(request, dto);
       
        dto.setName(DataUtility.getString(request.getParameter("name")));
        dto.setAddress(DataUtility.getString(request.getParameter("address")));
        dto.setState(DataUtility.getString(request.getParameter("state")));
        dto.setCity(DataUtility.getString(request.getParameter("city")));
        dto.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));
    }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("CollegeCtl Method doGet Started");
        long id = DataUtility.getLong(request.getParameter("id"));
        if (id > 0) {
            try {
                CollegeDTO dto = model.findByPK(id);
                if (dto == null) {
                    ServletUtility.setErrorMessage("College not found", request);                  
                    ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
                    return;
                }
 ServletUtility.setDTO(dto, request); } catch (ApplicationException e) {
                handleDatabaseException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("CollegeCtl Method doGet Ended");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("CollegeCtl Method doPost Started");
 String op = DataUtility.getString(request.getParameter("operation"));

        if (OP_DELETE.equalsIgnoreCase(op)) {
 long id = DataUtility.getLong(request.getParameter("id"));
            try {
 model.delete(id);
                ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_SUCCESS_DELETE, request);
                ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
 log.error("Application exception during delete", e);
 handleDatabaseException(e, request, response);
 return;
            }
        }

        CollegeDTO dto = new CollegeDTO();
 populateDTO(request, dto);

        if (validate(request)) {
            try {
                if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
 if (OP_UPDATE.equalsIgnoreCase(op)) {
 CollegeDTO dtoExist = model.findByPK(dto.getId());
 dto.setCreatedBy(dtoExist.getCreatedBy());
 dto.setCreatedDatetime(dtoExist.getCreatedDatetime());
                        dto.setModifiedDatetime(new Timestamp(System.currentTimeMillis()));
                        model.update(dto);
                        ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_UPDATE, request);
 } else {
 model.add(dto); // add method populates id
                        ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_ADD, request);
                    }
                }else if (OP_CANCEL.equalsIgnoreCase(op)) {
                    ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
                    return;
                } else if (OP_RESET.equalsIgnoreCase(op)) { 
                    ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response); // Corrected redirection for reset
                    return;
                }
            } catch (ApplicationException e) {
                log.error("Application exception", e);
                handleDatabaseException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDTO(dto, request); // Set DTO in request for duplicate record error
                ServletUtility.setErrorMessage(PropertyReader.getValue("error.college.exist"), request);
            }            
            ServletUtility.forward(getView(), request, response);     
        }    
        log.debug("CollegeCtl Method doPost Ended");
    }

    @Override
    protected String getView() {
        return ORSView.COLLEGE_VIEW;
    }

    @Override
    protected void preload(HttpServletRequest request) {
    }
}