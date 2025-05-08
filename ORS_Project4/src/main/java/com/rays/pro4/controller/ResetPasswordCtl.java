package com.rays.pro4.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(urlPatterns = {"/resetPassword"})
public class ResetPasswordCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(ResetPasswordCtl.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.log(Level.INFO, "ResetPasswordCtl doGet Start");

        String token = request.getParameter("token");

        if (DataValidator.isNull(token)) {
            ServletUtility.setErrorMessage("Invalid or missing reset token.", request);
            ServletUtility.forward(JWAView.ERROR_VIEW, request, response);
            return;
        }

        UserModel model = new UserModel();
        UserDTO userDTO = null;
        try {
            userDTO = model.validateResetToken(token);
        } catch (ApplicationException e) {
            log.log(Level.SEVERE, "Application Exception in validateResetToken", e);
            ServletUtility.handleException(e, request, response);
            return;
        }

        if (userDTO == null) {
            ServletUtility.setErrorMessage("Invalid or expired reset token.", request);
            ServletUtility.forward(JWAView.ERROR_VIEW, request, response);
            return;
        }

        request.setAttribute("token", token); // Pass the token to the view
        ServletUtility.forward(JWAView.RESETPASSWORD_VIEW, request, response);

        log.log(Level.INFO, "ResetPasswordCtl doGet End");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("ResetPasswordCtl doPost Start");

        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        boolean isValid = true;

        if (DataValidator.isNull(newPassword)) {
            ServletUtility.setErrorMessage("New Password is required", request);
            isValid = false;
        }

        if (DataValidator.isNull(confirmPassword)) {
            ServletUtility.setErrorMessage("Confirm Password is required", request);
            isValid = false;
        }

        if (!DataValidator.isNull(newPassword) && !DataValidator.isNull(confirmPassword) && !newPassword.equals(confirmPassword)) {
            ServletUtility.setErrorMessage("New Password and confirm password should be same", request);
            isValid = false;
        }

        if(!isValid){
             request.setAttribute("token", token);
            ServletUtility.forward(JWAView.RESETPASSWORD_VIEW, request, response);
             return;
        }

        UserModel model = new UserModel();
        try {
            if (model.updatePasswordByToken(token, newPassword)) {
                ServletUtility.setSuccessMessage("Password changed successfully. Please login.", request);
                ServletUtility.forward(JWAView.LOGIN_VIEW, request, response);
                return;
            }else{
                 ServletUtility.setErrorMessage("Failed to reset password. Invalid or expired token.", request);
            }
        } catch (ApplicationException e) {
            log.severe("Application Exception in updatePasswordByToken: " + e.getMessage());
            ServletUtility.handleException(e, request, response);
            return;
        }
         request.setAttribute("token", token); // Keep the token if possible
        ServletUtility.forward(JWAView.RESETPASSWORD_VIEW, request, response);
         log.info("ResetPasswordCtl doPost End");
    }
}