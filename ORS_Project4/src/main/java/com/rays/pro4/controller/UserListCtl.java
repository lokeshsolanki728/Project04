package com.rays.pro4.controller;

import java.beans.Statement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.rays.pro4.Bean.BaseBean;

import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Model.UserModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/**
 * The Class UserListCtl.
 * 
 * @author Lokesh SOlanki
 */
@WebServlet(name = "UserListCtl", urlPatterns = { "/ctl/UserListCtl" })
public class UserListCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UserListCtl.class);


	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 * 
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("populateBean method of UserListCtl Started");
		final UserBean bean = new UserBean();
		bean.populate(request);
		log.debug("populateBean method of UserListCtl Ended");
		return bean;

	}	
	
	/**
	 * Contains display logics.
	 * @param request
	 * @param response
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("UserListCtl doGet Start");

		List list = null;
        UserModel model = new UserModel();
        
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		UserBean bean = (UserBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
        RoleModel roleModel = new RoleModel();
        try {
            List roleList = roleModel.list();
            request.setAttribute("RoleList", roleList);
        } catch (ApplicationException e) {
            log.error("Error while fetching Role List", e);
            ServletUtility.setErrorMessage("Error while fetching Role List", request);
            ServletUtility.forward(ORSView.ERROR_VIEW, request, response);
            return;
        }
		   list = model.search(bean, pageNo, pageSize);
		   nextList = model.search(bean, pageNo + 1, pageSize);
           
			request.setAttribute("nextlist", nextList.size());


			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);			
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			
			ServletUtility.forward(getView(), request, response);
			log.debug("UserListCtl doGet End");
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("UserListCtl doPost Start");

        UserModel model = new UserModel();

		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));
		
		// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");
        
		if (OP_SEARCH.equalsIgnoreCase(op)) {
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
            pageNo--;
        }else if (OP_NEW.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.USER_CTL, request, response);

			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;
        }else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
            List<String> errorMessages = new ArrayList<>();

			if (ids != null && ids.length > 0) {
                
				for (String id : ids) {
					try {
                        
                        model.delete(DataUtility.getLong(id));
                    } catch (ApplicationException e) {
						log.error(e);
                        errorMessages.add("Error while deleting record Id : " + id);

                        
					}
				}
                if (!errorMessages.isEmpty()) {
                    ServletUtility.setErrorMessage(String.join("<br>", errorMessages), request);
                    
                } else {
                    ServletUtility.setSuccessMessage(MessageConstant.DATA_DELETE_SUCCESS, request);
                }

			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		UserBean bean = (UserBean) populateBean(request);
		try {

			list = model.search(bean, pageNo, pageSize);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());
		} catch (ApplicationException e) {
			log.error("Application Exception", e);
            handleDatabaseException(e, request, response);
            return;
		}
		if (list == null || list.isEmpty()) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.norrecord"), request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("UserListCtl doPost End");
		
	}	
	

	/**
	 * Returns the VIEW page of this Controller
	 * @return
	 * @see com.rays.pro4.controller.BaseCtl#getView()
	 * @return
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.USER_LIST_VIEW;
	}	
}