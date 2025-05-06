package com.rays.pro4.controller;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/**
 * Student List functionality Controller. Performs operation for list, search
 * and delete operations of Student
 *
 * @author Lokesh SOlanki
 */
public class StudentListCtl extends BaseCtl{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(StudentListCtl.class);

    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("preload method of StudentListCtl Started");
        final CollegeModel cmodel = new CollegeModel();

        try {
            List clist = cmodel.list(0, 0);
            request.setAttribute("CollegeList", clist);

        } catch (final ApplicationException e) {
            log.error(e);

        }
        log.debug("preload method of StudentListCtl Ended");

    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("populateBean method of StudentListCtl Started");
        final StudentBean bean = new StudentBean();
        bean.populate(request);
        log.debug("populateBean method of StudentListCtl Ended");
        return bean;
    }

    /**
     * Contains display logics.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("doGet method of StudentListCtl Started");
        List<StudentBean> list;
        final StudentModel model = new StudentModel();

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        String op = DataUtility.getString(request.getParameter("operation"));

        final StudentBean bean = (StudentBean) populateBean(request);

        try {

            list = model.search(bean, pageNo, pageSize,null,null);

            ServletUtility.setList(list, request);

            if (list == null || list.size() == 0) {

                ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
            }

            ServletUtility.setList(list, request);

            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);

            ServletUtility.forward(getView(), request, response);

        } catch (final ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug("doGet method of StudentListCtl Ended");
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
        log.debug("doPost method of StudentListCtl Started");

        List<StudentBean> list;
        final StudentModel model = new StudentModel();

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        final String op = DataUtility.getString(request.getParameter("operation"));

        final StudentBean bean = (StudentBean) populateBean(request);

        try {

            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {

                    pageNo = 1;

                } else if (OP_NEXT.equalsIgnoreCase(op)) {

                    pageNo++;

                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {

                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                String[] ids = request.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    StudentBean deletebean = new StudentBean();
                    for (String id : ids) {
                        try {
							deletebean.setId(DataUtility.getInt(id));
							model.delete(deletebean);
						} catch (ApplicationException e) {
							log.error(e);
							ServletUtility.handleException(e, request, response);
						}
                    }
                    ServletUtility.setSuccessMessage(MessageConstant.STUDENT_DELETE, request);
                } else {
                    ServletUtility.setErrorMessage(PropertyReader.getValue("error.require", "Select at least one record"),
                            request);
                }
            } 

            list = model.search(bean, pageNo, pageSize, null, null);
            if (list.isEmpty()) {
                ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
            }
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (final ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
        log.debug("doPost method of StudentListCtl Ended");
    }
    
    /**
     * Search student.
     *
     * @param request  the request
  * @param pageNo the page no
  * @param pageSize the page size
  * @param orderBy the order by
  * @param sortOrder the sort order
     * @return the list
     * @throws ApplicationException the application exception
     */
	private List<StudentBean> searchStudent(HttpServletRequest request, int pageNo, int pageSize, String orderBy,
			String sortOrder) throws ApplicationException {
		log.debug("searchStudent method Started");
		final StudentBean bean = (StudentBean) populateBean(request);
		final List<StudentBean> list = model.search(bean, pageNo, pageSize, orderBy, sortOrder);
		log.debug("searchStudent method end");
		return list;
	}
    /**
     * Contains Submit logics.
  *
  * @param request the request
  * @param orderBy the order by
  * @param sortOrder the sort order
  */
  private void setOrderAndSort(HttpServletRequest request,String orderBy,String sortOrder) {
	        String[] list = {"firstName","lastName","dob","collegeName","rollNo","mobileNo"};
	        ServletUtility.setOderByList(orderBy, sortOrder, list, request);
    }
	/**
	 * Returns the VIEW page of this Controller.
	 * 
	 * @return the view
	 */
	@Override
	protected String getView() {
		return ORSView.STUDENT_LIST_VIEW;
	}
    
	@Override
	protected void handleDatabaseException(Throwable e, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.handleDatabaseException(e, request, response);
	}
}
