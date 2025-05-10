package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rays.pro4.DTO.RoleDTO;
import org.apache.log4j.Logger;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.controller.ORSView;
import com.rays.pro4.Util.ServletUtility;

/**
 * Login functionality Controller. Performs operation for Login
 * 
 * @author Lokesh SOlanki
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "/LoginCtl" })
public class LoginCtl extends BaseCtl<UserDTO> {
	private static final long serialVersionUID = 1L;
	public static final String OP_REGISTER = "Register";
	public static final String OP_SIGN_IN = "SignIn";
	public static final String OP_LOG_OUT = "Logout";
	public static final String OP_SIGN_UP = "SignUp";
	private static Logger log = Logger.getLogger(LoginCtl.class);
	private UserModel model = new UserModel();

	/**
	 * Validates the data input by user
	 * 
	 * @param request the request
	 * @return true, if successful
	 * @see com.rays.pro4.controller.BaseCtl#validate(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("LoginCtl Method validate Started");
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("login")) && !OP_LOG_OUT.equalsIgnoreCase(op)
				&& !OP_SIGN_UP.equalsIgnoreCase(op)) {
			request.setAttribute("login", "Login id is required");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("password")) && !OP_LOG_OUT.equalsIgnoreCase(op)
				&& !OP_SIGN_UP.equalsIgnoreCase(op)) {
			request.setAttribute("password", "Password is required");

            pass = false;
        }

        return pass;
	}

    /**
     * @see com.rays.pro4.controller.BaseCtl#populateBean(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected  UserDTO populateBean(final HttpServletRequest request) {
        log.debug("LoginCtl Method populatebean Started"); 
 UserDTO bean = new UserDTO();
        bean.setLogin(request.getParameter("login"));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        return bean;
    }

	/**
	 * display logics
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see com.rays.pro4.controller.BaseCtl#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {
		log.debug("doGet method start");
		javax.servlet.http.HttpSession session = request.getSession();
		if (session.getAttribute("user") != null) {
			ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
			return;
		}
		final String operation = DataUtility.getString(request.getParameter("operation"));
		if(OP_LOG_OUT.equalsIgnoreCase(operation)){
			request.getSession().invalidate();
			ServletUtility.forward(getView(), request, response);
			return;
		}
        
		ServletUtility.forward(getView(), request, response);


		log.debug("doGet method end");
	}
	
	
	/**
	 * Login.
	 *
	 * @param bean the bean
	 * @param request the request
	 * @param response the response
	 * @throws ApplicationException the application exception
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private final void login(final UserDTO bean, final HttpServletRequest request,final HttpServletResponse response) throws ApplicationException, ServletException, IOException {	 
		log.debug("login method start");
		
		try {
            final javax.servlet.http.HttpSession session = request.getSession(true);
            UserDTO uDTO = model.authenticate(bean.getLogin(), bean.getPassword());
            if (uDTO != null) { 
 session.setAttribute("user", uDTO);
                final RoleBean rolebean = roleModel.findByPK(uDTO.getRoleId());
                if (rolebean != null) {
                    session.setAttribute("role", rolebean);
                }
				if (request.getParameter("uri") == null) { 
					ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
				} else {
					ServletUtility.redirect(request.getParameter("uri"), request, response);
				}
				return;
				 
			} else {
				ServletUtility.setErrorMessage("Invalid LoginId And Password",request);
				
				ServletUtility.forward(getView(),request,response);
			}
		}catch(DatabaseException e) {
			handleDatabaseException(e, request, response);
		}
		log.debug("login method end");
		
	}
	
	
	/**
     * Save user.
     *
     * @param bean the bean
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException the application exception
     */
    private void signUp(UserDTO bean, HttpServletRequest request)
            throws ApplicationException {
        log.debug("save method start");
            ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);

        log.debug("save method end");
    }
	
	 

	
	 
	

	

	
	

	
	
	
	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {
 
		log.debug("LoginCtl Method doPost Started");		
        UserDTO dto = new UserDTO();
		final String operation = DataUtility.getString(request.getParameter("operation")); 
        dto = bean.getDTO();

		if (OP_SIGN_IN.equalsIgnoreCase(operation)) {
			if (validate(request)) {
					try {
						login(bean, request, response);
						return;
					}catch (final ApplicationException e) {
						log.error(e);
					}
					return;
				}
		}else if (OP_SIGN_UP.equalsIgnoreCase(operation)){
			signUp(bean, request);
			return;
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("LoginCtl Method doPost Ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}
}
