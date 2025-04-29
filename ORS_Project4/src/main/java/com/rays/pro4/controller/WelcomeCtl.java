package com.rays.pro4.controller;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Util.ServletUtility;

/**
 * Welcome functionality Controller. Performs operation for Show Welcome page
 * 
 * @author Lokesh SOlanki
 */

@WebServlet(name = "WelcomeCtl", urlPatterns = { "/WelcomeCtl" })
public class WelcomeCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(WelcomeCtl.class);

    /**
     * Contains Display logics.
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("WelcomeCtl Method doGet Started");

        ServletUtility.forward(getView(), request, response);

        log.debug("WelcomeCtl Method doGet Ended");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        doGet(request, response);
    }

    
    protected String getView() {
        return ORSView.WELCOME_VIEW;
    }

}
