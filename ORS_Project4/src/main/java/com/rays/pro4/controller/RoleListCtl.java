package com.rays.pro4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader; 
import com.rays.pro4.Util.ServletUtility;



/**
 * Role List functionality Controller. Performs operation for list, search
 * operations of Role
 * 
 * @author Lokesh SOlanki
 */
@WebServlet(name = "RoleListCtl", urlPatterns = { "/ctl/RoleListCtl" })
public class RoleListCtl extends BaseCtl{

	 /** The log. */
    private static Logger log = Logger.getLogger(RoleListCtl.class);
    private final RoleModel model = new RoleModel();

    private int pageSize;
    /**
	 * Populates bean object from request parameters.
	 *
	 * @param request the request
	 * @return the base bean
	 */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("populateBean method of RoleListCtl Started");
        RoleBean bean = new RoleBean();      
        bean.populate(request);
        log.debug("populateBean method of RoleListCtl end");
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
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        log.debug("RoleListCtl doGet Start");
        List<RoleBean> list = null;
        final int pageNo = 1;
        pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        final RoleBean bean = (RoleBean) populateBean(request);
       try {
        	list = model.search(bean, pageNo, pageSize);
            boolean next = model.search(bean, pageNo + 1, pageSize).size() > 0;
            request.setAttribute("nextlist", next);
			if (list == null || list.isEmpty()) {                ServletUtility.setErrorMessage(PropertyReader.getValue("error.norrecord"), request);
            }            
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            handleDatabaseException(e, request, response);
            return;
        }
        log.debug("RoleListCtl doGet End");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("RoleListCtl doPost Start");
        List<RoleBean> list = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        RoleBean bean = (RoleBean) populateBean(request);
        String op = DataUtility.getString(request.getParameter("operation"));

        String[] ids = request.getParameterValues("ids");


                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
            pageNo++;
        } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
            pageNo--;
        } else if (OP_NEW.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
            return;
        } else if (OP_DELETE.equalsIgnoreCase(op)) {
            pageNo = 1;
                if (ids != null && ids.length > 0) {

                    try {
                        for (String id : ids) {
                            RoleBean rBean = new RoleBean();
                            rBean.setId(DataUtility.getInt(id));
                            model.delete(rBean);
                        }
                    } catch (ApplicationException e) {
                        log.error(e);
                        ServletUtility.setErrorMessage(MessageConstant.DATA_DELETE_ERROR, request);
                        ServletUtility.handleException(e, request, response);
                        return;
                    }
                    ServletUtility.setSuccessMessage("Role is Deleted Successfully ", request);
                    return;
                }

        }
            try {
                list = model.search(bean, pageNo, pageSize);
                request.setAttribute("nextlist", model.search(bean, pageNo + 1, pageSize).size() > 0);
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
            }
            if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
                ServletUtility.setErrorMessage("No record found ", request);
            }
            ServletUtility.setList(list, request);
            ServletUtility.setBean(bean, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);


        log.debug("RoleListCtl doPost End");
    }
    
    /**
	 * Returns the VIEW page of this Controller.
	 *
	 * @return the view
	 * 
	 */
    
        @Override
    protected String getView() {
        return ORSView.ROLE_LIST_VIEW;
    }
}
