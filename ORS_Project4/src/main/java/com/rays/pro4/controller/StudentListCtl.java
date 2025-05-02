
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
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.MessageConstant;
import com.rays.pro4.Util.ServletUtility;


//TODO: Auto-generated Javadoc
/**
* Student List functionality Controller. Performs operation for list, search
* and delete operations of Student
* 
*  @author Lokesh SOlanki
*/
@WebServlet (name = "StudentListCtl" , urlPatterns = {"/ctl/StudentListCtl"})
public class StudentListCtl extends BaseCtl{

	/** The log. */
    private static Logger log = Logger.getLogger(StudentListCtl.class);
    private final StudentModel model = new StudentModel();
    /**
	 * Loads pre-load data.
	 *
	 * @param request the request
     */
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


    /**
	 * Populates bean object from request parameters.
	 *
	 * @param request the request
	 * @return the base bean
	 * @see com.rays.pro4.controller.BaseCtl#populateBean(javax.servlet.http.HttpServletRequest)
	 */
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
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet method of StudentListCtl Started");
        final List<StudentBean> list;

        final int pageNo = 1;
        final int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        final StudentBean bean = (StudentBean) populateBean(request);

        try {
            list = model.search(bean, pageNo, pageSize);

            if (list.isEmpty()) {
                ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);

        } catch (final ApplicationException e) {
            log.error(e);
            handleDatabaseException(e, request, response);
            return;
        }
        log.debug("doGet method of StudentListCtl Ended");
    }

    /**
     * Delete list of student.
     *
     * @param request  the request
     * @throws ApplicationException the application exception
     */
    private void deleteStudent(HttpServletRequest request) throws ApplicationException {
        log.debug("deleteStudent method Started");
        final String[] ids = request.getParameterValues("ids");
        StudentBean deletebean = new StudentBean();
          if (ids != null && ids.length > 0) {
            for (final String id : ids) {
                deletebean.setId(DataUtility.getInt(id));
                model.delete(deletebean);
                
            }
             ServletUtility.setSuccessMessage(MessageConstant.STUDENT_DELETE, request);
        }
        else {
            ServletUtility.setErrorMessage(PropertyReader.getValue("error.require", "Select at least one record"), request);
        }
        log.debug("deleteStudent method ended");
    }

    /**
     * Search student.
     *
     * @param request  the request
     * @param pageNo the page no
     * @param pageSize the page size
     * @return the list
     * @throws ApplicationException the application exception
     */
    private List<StudentBean> searchStudent(HttpServletRequest request, int pageNo, int pageSize)
            throws ApplicationException {
        log.debug("searchStudent method Started");

        final StudentBean bean = (StudentBean) populateBean(request);
        final List<StudentBean> list = model.search(bean, pageNo, pageSize);
        log.debug("searchStudent method end");
        return list;
    }
    /**
     * Contains Submit logics.
     * @param request the request
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
   @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost method of StudentListCtl Started");
        List<StudentBean> list = new ArrayList<>();
        final String op = DataUtility.getString(request.getParameter("operation"));
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        final StudentBean bean = (StudentBean) populateBean(request);

        try {
            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;
            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;
            } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
                pageNo--;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                deleteStudent(request);
            }
            list = searchStudent(request, pageNo, pageSize);
        } catch (final ApplicationException e) {
            log.error(e);
            handleDatabaseException(e, request, response);
            return;
        }
        if (list.isEmpty() && !OP_DELETE.equalsIgnoreCase(op)) {
            ServletUtility.setErrorMessage(PropertyReader.getValue("record.notfound"), request);
        }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);

        log.debug("StudentListCtl doGet End");
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
}
