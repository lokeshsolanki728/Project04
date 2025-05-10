package com.rays.pro4.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.controller.ORSView;
import com.rays.pro4.Util.ServletUtility;

/**My Profile functionality Controller. Performs operation for update your
 * Profile
 *
 * @author Lokesh Solanki
 */
@WebServlet(name = "MyProfileCtl", urlPatterns = {"/ctl/MyProfileCtl"})
public class MyProfileCtl extends BaseCtl {

	public static final String OP_CHANGE_MY_PASSWORD = "ChangePassword";

    private static Logger log = Logger.getLogger(MyProfileCtl.class);

    /**
     * Model object to perform operations
     */
    private final RoleModel roleModel = new RoleModel();
    private final UserModel model = new UserModel();

    /**
     * Validates input data entered by User
     *
     * @param request
     * @return
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("MyProfileCtl Method validate Started");
        boolean pass = true;
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
        if (!pass) {
            log.debug("MyProfileCtl Method validate Ended with error");
        }
        return pass;
    }
    /**
     * Populates dto object from request parameters
     *
     * @param request the request
     * @param dto     the dto
     */
    @Override
    protected UserDTO populateBean(HttpServletRequest request) {
        log.debug("MyProfileCtl Method populatebean Started");
        UserDTO bean = new UserDTO();
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));        
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
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
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("MyProfileCtl Method doGet Started");
        HttpSession session = request.getSession();
        final UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO != null) {
            final long id = userBean.getId();
            if (id > 0) {
                UserDTO dto;
                try {
                    dto = model.findByPK(id);
                    if (dto == null) {
                        ServletUtility.setErrorMessage("Record not found", request);
                    }
                    request.setAttribute("firstName", dto.getFirstName());
                    request.setAttribute("lastName", dto.getLastName());
                    request.setAttribute("gender", dto.getGender());
                    request.setAttribute("mobileNo", dto.getMobileNo());
                    request.setAttribute("dob", DataUtility.getDateString(dto.getDob()));
                    request.setAttribute("login", dto.getLogin());
                } catch (final ApplicationException e) {
                    handleDatabaseException(e, request, response);
                    return;
                }
            }
        } else {
            ServletUtility.setErrorMessage("Please Login", request);
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("MyProfileCtl Method doGet Ended");
    }

    /**Contains Submit logics
     *
     * @param request the request
     * @param response the response
     * @throws javax.servlet.ServletException
     * @throws ServletException
     * @throws IOException     
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        log.debug("MyProfileCtl Method doPost Started");
        final UserDTO userDTO = (UserDTO) session.getAttribute("user");
        final long id = userDTO.getId();
        String op = DataUtility.getString(request.getParameter("operation"));

        if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request, response);
            return;
        }
        if (OP_SAVE.equalsIgnoreCase(op)) {
            if (validate(request)) {
                final UserDTO dto = populateBean(request);
                try {
                    updateUser(dto, model, userDTO, request);
                } catch (final ApplicationException | DuplicateRecordException e) {
                    log.error("Error in My profile", e);
                    handleDatabaseException(e, request, response);
                }
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("MyProfileCtl Method doPost Ended");
    }

    /**
     * Update user.
     *
     * @param userToUpdate the user DTO with updated information
     * @param model    the model
     * @param currentUserDTO the current user DTO from the session
     * @param request the request
     * @throws DuplicateRecordException the duplicate record exception
     * @throws ApplicationException the application exception
     */
    private void updateUser(UserDTO userToUpdate, UserModel model, UserDTO currentUserDTO, HttpServletRequest request)
            throws DuplicateRecordException, ApplicationException {
        
        userToUpdate.setId(currentUserDTO.getId());
        userToUpdate.setLogin(currentUserDTO.getLogin());
        userToUpdate.setRoleId(currentUserDTO.getRoleId());
        userToUpdate.setPassword(currentUserDTO.getPassword()); // Keep the old password unless changed specifically elsewhere
        userToUpdate.setModifiedBy(currentUserDTO.getLogin());

        model.update(userToUpdate);
        
        // Update the user DTO in the session with the new information
        currentUserDTO.populate(userToUpdate);
        ServletUtility.setSuccessMessage(MessageConstant.USER_UPDATE, request);
    }
    /**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
    @Override
    protected String getView() { return ORSView.MY_PROFILE_VIEW; }



}
