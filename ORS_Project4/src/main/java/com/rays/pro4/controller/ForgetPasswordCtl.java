package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import com.rays.pro4.util.ForgetPasswordValidator;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.ServletUtility;

/**
 * Forget Password functionality Controller. Performs operation for Forget Password
 *
 * @author Lokesh SOlanki
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/ForgetPasswordCtl" })
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
        UserBean bean = new UserBean();
        bean.populate(request);
      
        
        log.debug("ForgetPasswordCtl Method populatebean Ended");

        return bean;
    }

     /**
      * Send password to the user email
      *
      * @param bean the bean
      * @param request the request
      * @throws ApplicationException the application exception
      * @throws RecordNotFoundException the record not found exception
      */
    private void sendPassword(UserBean bean, HttpServletRequest request) throws ApplicationException, RecordNotFoundException {
        log.debug("sendPassword method start");
        model.forgetPassword(bean.getLogin());
        ServletUtility.setSuccessMessage(PropertyReader.getValue("success.forget"), request);
        log.debug("sendPassword method end");
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

        final String op = DataUtility.getString(request.getParameter("operation"));
        final UserBean bean = (UserBean) populateBean(request);

        if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.FORGET_PASSWORD_CTL, request, response);
            return;
        }
        if (OP_GO.equalsIgnoreCase(op)) {
            if(validate(request)) {
                try {
                    forgetPassword(bean, request, response);
                }catch (DatabaseException | ApplicationException | RecordNotFoundException e) {
                    log.error(e);
                    handleDatabaseException(e, request, response);
                    return;
                }
                 catch (final RecordNotFoundException e) {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.loginNotExist"), request);
                    log.error("Record not found: ", e);
                } catch (final ApplicationException e) {
                   log.error("Application exception: ", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        
        log.debug("ForgetPasswordCtl Method doPost Ended");
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
