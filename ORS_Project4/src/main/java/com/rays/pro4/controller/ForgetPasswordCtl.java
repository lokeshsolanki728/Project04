package com.rays.pro4.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rays.pro4.Exception.ApplicationException;
import org.apache.log4j.Logger;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Bean.RoleBean;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.controller.ORSView;

/**
 * Forget Password functionality Controller. Performs operation for Forget Password
 *
 * @author Lokesh Solanki
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = {"/ctl/ForgetPasswordCtl"})
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

        boolean pass = true;
        String login = request.getParameter("login");

        if (DataValidator.isNull(login)) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
        }
        return pass;
    }

    /**
     * Populates bean object from request parameters.
     *
     * @param request the request
     * @return the user bean
     */
    @Override
    protected UserBean populateBean(final HttpServletRequest request) {
        log.debug("ForgetPasswordCtl Method populateBean Started");
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
        HttpSession session = request.getSession();
        RoleBean role = (RoleBean) session.getAttribute("role");
        if (session.getAttribute("user") != null && (role.getId() != RoleBean.STUDENT)) {

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
        UserBean bean = populateBean(request);
        try {
            if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.FORGET_PASSWORD_CTL, request, response);
            } else if (OP_GO.equalsIgnoreCase(op) && validate(request)) {
                model.resetPassword(dto.getLogin());
                ServletUtility.setSuccessMessage(PropertyReader.getValue("success.forget"), request);
            }} catch (ApplicationException e) {
            handleDatabaseException(e, request, response);
            return;
        } finally {
            if (!validate(request)) {
                ServletUtility.setBean(bean,request);
            }
            ServletUtility.forward(getView(), request, response);
        }
    }

    /**
     * @see in.co.rays.ors.controller.BaseCtl#getView()
     */
    @Override
    protected String getView() {
        return ORSView.FORGET_PASSWORD_VIEW;
    }
}
