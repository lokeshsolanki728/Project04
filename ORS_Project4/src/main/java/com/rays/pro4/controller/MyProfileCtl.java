package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;
import com.rays.pro4.Util.UserValidator;

/**
 * My Profile functionality Controller. Performs operation for update your
 * Profile
 *
 * @author Lokesh Solanki
 */
@WebServlet(name = "MyProfileCtl", urlPatterns = { "/ctl/MyProfileCtl" })
public class MyProfileCtl extends BaseCtl<UserBean>{

	public static final String OP_CHANGE_MY_PASSWORD = "ChangePassword";

    private static Logger log = Logger.getLogger(MyProfileCtl.class);

    /**
     * Model object to perform operations
     */
    private final UserModel model = new UserModel();

    /**
     * Validates input data entered by User
     *
     * @param request the request
     * @return the boolean
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	boolean pass = true;
        log.debug("MyProfileCtl Method validate Started");
       

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName",
                    PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender",
                    PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo",
                    PropertyReader.getValue("error.require", "MobileNo"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob",
                    PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        }        
        if(!pass){
        	log.debug("MyProfileCtl Method validate Ended with error");
        }
        
        return pass;
    }

    /** Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
    @Override
    protected UserBean populateBean(HttpServletRequest request) {
        log.debug("MyProfileCtl Method populatebean Started");
        final UserBean bean = new UserBean();
        bean.populate(request);
        log.debug("MyProfileCtl Method populatebean Ended");
        return bean;
    }
    protected void preload(HttpServletRequest request) {
    	
    }

    /**
     * Contains Display logics
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * 
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        log.debug("MyProfileCtl Method doGet Started");
        HttpSession session = request.getSession();
        final UserBean userBean = (UserBean) session.getAttribute("user");
        if (userBean != null) {
	        final long id = userBean.getId();	        
	        if (id > 0) {
	            UserBean bean;
	            try {
	                bean = model.findByPK(id);
	                if(bean == null){
	                	ServletUtility.setErrorMessage("User not found", request);
	                }
	                ServletUtility.setBean(bean, request);
	            } catch (final ApplicationException e) {
	                log.error("Error finding user by ID", e);
	                handleDatabaseException(e, request, response);
	                return;
	            }
	        }
        }else {
        	ServletUtility.setErrorMessage("Please Login", request);
        }
      
        ServletUtility.forward(getView(), request, response);
        log.debug("MyProfileCtl Method doGet Ended");
    }

    
    /**
     * Contains Submit logics
     * 
     * @param request the request
     * @param response the response
     * @throws javax.servlet.ServletException
     * @throws ServletException
     * @throws IOException
     * 
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        log.debug("MyProfileCtl Method doPost Started");
        
        final UserBean userBean = (UserBean) session.getAttribute("user");
        final long id = userBean.getId();
        String op = DataUtility.getString(request.getParameter("operation"));
        
        if(OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) {
        	ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request, response);
            return;
        }
        
        if (OP_SAVE.equalsIgnoreCase(op)) {
        	
        	if(validate(request)) {
        		final UserBean bean = populateBean(request);
        		try {
        			updateUser(bean, model, userBean, request);
        		} catch (final ApplicationException | DuplicateRecordException e) {
        			log.error("Application exception", e);
        			handleDatabaseException(e, request, response);
        		}  
        	}         
        }        
       ServletUtility.forward(getView(), request, response);
        log.debug("MyProfileCtl Method doPost Ended");
    }
    
    /** Update user.
     *
     * @param bean the bean
     * @param model the model
     * @param userBean the user bean
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException the application exception
     */
    private void updateUser(UserBean bean, UserModel model,UserBean userBean, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
    	bean.setId(userBean.getId());
    	bean.setLogin(userBean.getLogin());
    	bean.setRoleId(userBean.getRoleId());
    	model.update(bean);
    	userBean.setFirstName(bean.getFirstName());
    	userBean.setLastName(bean.getLastName());
    	userBean.setGender(bean.getGender());
    	userBean.setDob(bean.getDob());
    	userBean.setMobileNo(bean.getMobileNo());
    	ServletUtility.setSuccessMessage(MessageConstant.USER_UPDATE, request);
    	
    }

    /**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
    @Override  
    protected String getView() {
        return ORSView.MY_PROFILE_VIEW;
    }
	
	
}
