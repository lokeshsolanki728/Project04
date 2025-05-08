package com.rays.pro4.controller;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.validator.UserValidator;

@WebServlet(name = "UserCtl", urlPatterns = { "/ctl/UserCtl" })
public class UserCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	private final UserModel model = new UserModel();
	private final RoleModel roleModel = new RoleModel();
	private static final Logger log = Logger.getLogger(UserCtl.class);

	@Override
	protected void preload(final HttpServletRequest request) {
		try {
			final List l = roleModel.list();
			request.setAttribute("roleList", l);
		} catch (final ApplicationException e) {
			log.error(e);
		}
	}

	@Override
	protected boolean validate(final HttpServletRequest request) {
		log.debug("UserCtl Method validate Started");
		
		Map<String, String> errors = UserValidator.validate(request);
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            return false;
        }

		log.debug("UserCtl Method validate Ended");
		return true;
	}
	protected BaseBean populateBean(final HttpServletRequest request) {
		log.debug("UserCtl Method populateBean Started");

		final UserBean bean = new UserBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		String pass = DataUtility.getString(request.getParameter("password"));
		if (!pass.isEmpty()) {
			bean.setPassword(pass);
		}
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));

		log.debug("UserCtl Method populateBean Ended");
		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("UserCtl Method doGet Started");
		UserBean bean = new UserBean();
		
		final long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0) {
            try {
                var userDTO = model.findByPK(id);
                if (userDTO == null) {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.notfound", "User"), request);
                    ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                    return;
                } else {
                    bean = new UserBean();
                    setBean(userDTO, bean);
                    ServletUtility.setBean(bean, request);
                }
            } catch (final ApplicationException e) {
                log.error("Application Exception", e);
                handleDatabaseException(e, request, response);
                return;
            }
        }else if(id==0){
              
        }else{
                ServletUtility.setErrorMessage(PropertyReader.getValue("error.invalid","User ID"), request);
                ServletUtility.forward(ORSView.ERROR_VIEW, request, response);
                return;
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("UserCtl Method doGet Ended");
	}

	/**
	 * Contains Submit logics
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("UserCtl Method doPost Started");

		final String op = DataUtility.getString(request.getParameter("operation"));
		final long id = DataUtility.getLong(request.getParameter("id"));
		UserBean bean = (UserBean) populateBean(request);

		if (OP_SAVE.equalsIgnoreCase(op)) {
			if (validate(request)) {
				try {
					if (id > 0) {
						model.update(bean.getDTO());
					} else {
						model.add(bean.getDTO());
					}
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.save", "User"), request);
				} catch (final ApplicationException e) {
					log.error("Application Exception", e);
					handleDatabaseException(e, request, response);
					return;
				} catch (final DuplicateRecordException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage(e.getMessage(), request);
				}	} else {
				ServletUtility.setBean(bean, request);
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("UserCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.USER_VIEW;
	}

	private void setBean(UserDTO dto, UserBean bean) {
		bean.setId(dto.getId());
		bean.setFirstName(dto.getFirstName());
		bean.setLastName(dto.getLastName());
		bean.setLogin(dto.getLogin());
		bean.setGender(dto.getGender());
		bean.setDob(dto.getDob());
		bean.setMobileNo(dto.getMobileNo());
		bean.setRoleId(dto.getRoleId());
	}