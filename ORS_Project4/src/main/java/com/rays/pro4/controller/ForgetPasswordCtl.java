package com.rays.pro4.controller;

import java.io.IOException;
import com.rays.pro4.DTO.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.ApplicationException;
import org.apache.log4j.Logger;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.util.ForgetPasswordValidator;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;

import com.rays.pro4.Util.ServletUtility;

/**
 * Forget Password functionality Controller. Performs operation for Forget Password
 *
 * @author Lokesh Solanki
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = {"/ForgetPasswordCtl"})
public class ForgetPasswordCtl extends BaseCtl<UserBean> {

    /** The log. */
    private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);

    /** The model. */
    private final UserModel model = new UserModel();

    /**
     * Validates input data entered by User
     *
     * @param request the request
     * @return true, if successful
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("validate method start");

        final boolean pass = ForgetPasswordValidator.validate(request);

        log.debug("ForgetPasswordCtl Method validate Ended");

        return pass;
    }


    /* (non-Javadoc)
     * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected UserBean populateBean(HttpServletRequest request) {

        log.debug("ForgetPasswordCtl Method populatebean Started");
        populateBean(request,new UserBean());
        log.debug("ForgetPasswordCtl Method populatebean Ended");

        return new UserBean();
    }

    protected void populateBean(HttpServletRequest request,UserBean bean){

        log.debug("ForgetPasswordCtl Method populatebean Started");
        UserBean bean = new UserBean();
        bean.populate(request);

        log.debug("ForgetPasswordCtl Method populatebean Ended");

        return bean;
    }

    /**
     * Contains Display logics.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("ForgetPasswordCtl Method doGet Started");

        if (request.getSession().getAttribute("user") != null) {
            ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);

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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("ForgetPasswordCtl Method doPost Started");
        String op = DataUtility.getString(request.getParameter("operation"));
        UserDTO dto = new UserDTO();
        UserBean bean = new UserBean();
        populateBean(request,bean);
        dto=bean.getDTO();
        try {
            if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.FORGET_PASSWORD_CTL, request, response);
                return;
            } else if (OP_GO.equalsIgnoreCase(op) && validate(request)) {
                model.resetPassword(dto.getLogin());
                ServletUtility.setSuccessMessage(PropertyReader.getValue("success.forget"), request);
            }} catch (ApplicationException e) {
            handleDatabaseException(e, request, response);
            return;
        } finally {
                try {
                    ServletUtility.forward(getView(), request, response);
                } catch (ServletException | IOException e) {
                    throw new RuntimeException(e);
                }
        }

        ServletUtility.forward(getView(), request, response);
    }

    /**
     * @see in.co.rays.ors.controller.BaseCtl#getView()
     */
    @Override
    protected String getView() {
        return ORSView.FORGET_PASSWORD_VIEW;
    }
}
