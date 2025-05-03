package com.rays.pro4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
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
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("CollegeCtl Method populateBean Started");
        CollegeBean bean = new CollegeBean();
        bean.populate(request);
        log.debug("CollegeCtl Method populateBean Ended");
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("CollegeCtl Method doGet Started");
        long id = DataUtility.getLong(request.getParameter("id"));
        if (id > 0) {
            try {
                CollegeBean bean = model.findByPK(id);
                if (bean == null) {
                    ServletUtility.setErrorMessage("College not found", request);
                }
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                log.error("Error finding college by ID", e);
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
        long id = DataUtility.getLong(request.getParameter("id"));
        CollegeBean bean = (CollegeBean) populateBean(request);

        if (validate(request)) {
            
            try {
                if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
                    if (id > 0){
                    model.update(bean);
                    ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_UPDATE, request);
                    }else {
                         long pk = model.add(bean);
                         bean.setId(pk);
                         ServletUtility.setSuccessMessage(MessageConstant.COLLEGE_ADD, request);
                    }
                    ServletUtility.setBean(bean, request);
                }else if (OP_CANCEL.equalsIgnoreCase(op)) {
                    ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
                    return;
                } else if (OP_RESET.equalsIgnoreCase(op)) {
                    ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
                    return;
                }
            } catch (ApplicationException e) {
                log.error("Application exception", e);
                handleDatabaseException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage(PropertyReader.getValue("error.college.exist"), request);
            }
            ServletUtility.forward(getView(), request, response);
        }else {
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