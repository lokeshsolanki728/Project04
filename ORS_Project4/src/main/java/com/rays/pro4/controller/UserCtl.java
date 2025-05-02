java
package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.UserValidator;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class UserCtl.
 *
 * @author Lokesh SOlanki
 *
 */
@WebServlet(name = "UserCtl", urlPatterns = {"/ctl/UserCtl"})
public class UserCtl extends BaseCtl<UserBean> {

    private static final long serialVersionUID = 1L;

    /**
     * The log.
     */
    private static Logger log = Logger.getLogger(UserCtl.class);
    private final UserModel model = new UserModel();

    /**
     * Loads pre-load data
     *
     * @param request the request
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("preload method of UserCtl Started");

        RoleModel roleModel = new RoleModel();
        try {
            List<RoleBean> roleList = roleModel.list();
            request.setAttribute("roleList", roleList);
        } catch (final Exception e) {
            log.error("Error while loading role list", e);
        }
        log.debug("preload method of UserCtl Ended");
    }

    /**
     * @param request the request
     * @return true, if successful
     * @throws ServletException the servlet exception
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("UserCtl Method validate Started");
        final boolean pass = UserValidator.validate(request);

        if (!pass) {
            log.debug("UserCtl Method validate Ended with error");
        }
        return pass;
    }

    /**
     * Populates bean object from request parameters.
     *
     * @param request the request
     * @param request the request
     * @return the base bean
     * @throws ServletException the servlet exception
     */
    @Override
    protected UserBean populateBean(HttpServletRequest request) {
        log.debug("UserCtl Method populatebean Started");

        final UserBean bean = new UserBean();
        bean.populate(request);

        log.debug("UserCtl Method populatebean Ended");
        return bean;

    }

    /**
     * Contains Display logics.
     *
     * @param request the request javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *                Contains display logic.
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     *                           javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("UserCtl Method doGet Started");
        final long id = DataUtility.getLong(request.getParameter("id"));
        if (id <= 0) {
            ServletUtility.setErrorMessage("Invalid User ID", request);
            ServletUtility.forward(ORSView.ERROR_VIEW, request, response);
            return;
        }
        if (id > 0) {

            UserBean bean;
            try {
                bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        log.debug("UserCtl Method doGet Ended");
        ServletUtility.forward(getView(), request, response);

    }

    /**
     * Save role.
     *
     * @param bean    the bean
     * @param model   the model
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException     the application exception
     */
    private void save(UserBean bean, UserModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("save method start");
        model.add(bean);
        ServletUtility.setSuccessMessage(MessageConstant.USER_ADD, request);
        log.debug("save method end");
    }

    /**
     * Update role.
     *
     * @param bean    the bean
     * @param model   the model
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException     the application exception
     */
    private void update(UserBean bean, UserModel model, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        log.debug("update method start");
        model.update(bean);
        ServletUtility.setSuccessMessage(MessageConstant.USER_UPDATE, request);
        log.debug("update method end");
    }

    /**
     * Contains submit logic.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        log.debug("UserCtl Method doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));
        UserBean bean = (UserBean) populateBean(request);

        try {
        	if (!validate(request)) {
                ServletUtility.setBean(bean, request);
                ServletUtility.forward(getView(), request, response);
                return;
            }
            if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
                UserBean existBean = model.findByLogin(bean.getLogin());
               if (existBean != null && (id == 0 || existBean.getId() != id)) {
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.user.login.duplicate"), request);
                    ServletUtility.forward(getView(), request, response);
                    return;
                }

                if (id > 0) {
                    update(bean, model, request);
                } else {
                    save(bean, model, request);
                }
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                model.delete(bean);
                ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                return;
            } else if (OP_CANCEL.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                return;
            }

        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
            return;
        } catch (DuplicateRecordException e) {
            log.error(e);
            ServletUtility.setErrorMessage(PropertyReader.getValue("error.user.login.duplicate"), request);
        } finally {
            ServletUtility.setBean(bean, request);
            ServletUtility.forward(getView(), request, response);
        }


    }


    /**
     * Returns the VIEW page of this Controller
     *
     * @return the view
     */
    @Override
    protected String getView() {
        return ORSView.USER_VIEW;
    }
}
