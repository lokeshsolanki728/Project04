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
		final UserModel model = new UserModel();
		
		List list = null;
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		UserBean bean = (UserBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
        RoleModel roleModel = new RoleModel();
        try {
            List roleList = roleModel.list();
            request.setAttribute("RoleList", roleList);
        } catch (Exception e) {
            log.error("Error while fetching Role List", e);        }
		UserModel model1 = new UserModel();
			list = model1.search(bean, pageNo, pageSize);
		   nextList = model1.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			ServletUtility.setList(list, request);
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

		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));
		UserBean bean = (UserBean) populateBean(request);
		// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");
       
		UserModel model = new UserModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
			pageNo--;
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;

			if (ids != null && ids.length > 0) {
                boolean errorOccurred = false;
				for (String id : ids) {
					try {
						UserBean deletebean = new UserBean();
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);
					} catch (ApplicationException e) {
						log.error(e);
                        errorOccurred = true;
                        ServletUtility.setErrorMessage("Error while deleting record", request);
                        break;
					}
				}if(!errorOccurred){
                    ServletUtility.setSuccessMessage(MessageConstant.DATA_DELETE_SUCCESS, request);
				}
				ServletUtility.setSuccessMessage(MessageConstant.DATA_DELETE_SUCCESS, request);
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {

			list = model.search(bean, pageNo, pageSize);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.norrecord"), request);
			ServletUtility.handleException(e, request, response);
			
			return;
		}
		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
			ServletUtility.setErrorMessage(PropertyReader.getValue("error.norrecord"), request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("UserListCtl doGet End");

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