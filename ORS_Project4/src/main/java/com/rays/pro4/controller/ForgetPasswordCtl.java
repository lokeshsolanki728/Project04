package com.rays.pro4.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rays.pro4.Exception.ApplicationException;
import org.apache.log4j.Logger;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Bean.RoleBean;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.controller.ORSView;

/**
 * Forget Password functionality Controller. Performs operation for Forget Password
*
* @author Shashank Singh
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = {"/ForgetPasswordCtl"})
public class ForgetPasswordCtl extends BaseCtl<UserDTO> {

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
    protected UserDTO populateBean(final HttpServletRequest request) { 
        log.debug("ForgetPasswordCtl Method populateBean Started");
        UserDTO bean = new UserDTO(); 
        bean.setLogin(DataUtility.getString(request.getParameter("login")));

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

        UserDTO dto = populateBean(request);

        if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.FORGET_PASSWORD_CTL, request, response);
            return;
        } else if (!validate(request)) {
            ServletUtility.setBean(dto, request);
            ServletUtility.forward(getView(), request, response);
            return;
        }
        try {
            model.resetPassword(dto.getLogin());
            ServletUtility.setSuccessMessage(PropertyReader.getValue("success.forget"), request);
            ServletUtility.redirect(ORSView.LOGIN_CTL, request, response);
        }
 catch (ApplicationException e) {
            handleDatabaseException(e, request, response);
            return;
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
