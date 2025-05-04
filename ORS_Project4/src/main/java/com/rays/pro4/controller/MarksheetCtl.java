import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.ServletUtility;
@WebServlet(name = "MarksheetCtl", urlPatterns = {"/ctl/MarksheetCtl"})
public class MarksheetCtl extends BaseCtl {
    private static final Logger log = Logger.getLogger(MarksheetCtl.class);
    @Override
    protected void preload(final HttpServletRequest request) {
        StudentModel model = new StudentModel();
        try {
            List<StudentBean> list = model.list(null, null);
            request.setAttribute("studentList", list);
        } catch (ApplicationException e) {
            log.error(e);
        }
    }
    @Override
    protected boolean validate(final HttpServletRequest request) {
        boolean pass = true;
        log.debug("MarksheetCtl validate started");
        if (DataValidator.isNull(request.getParameter("rollNo"))) {
            request.setAttribute("rollNo", "Roll No. is required");
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("studentId"))) {
            request.setAttribute("studentId", "Student Name is required");
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("physics"))) {
            request.setAttribute("physics", "Marks is required");
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("chemistry"))) {
            request.setAttribute("chemistry", "Marks is required");
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("maths"))) {
            request.setAttribute("maths", "Marks is required");
            pass = false;
        }
        log.debug("MarksheetCtl validate ended");
        return pass;
    }
    @Override
    protected MarksheetBean populateBean(final HttpServletRequest request) {
        MarksheetBean bean = new MarksheetBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
        bean.setStudentld(DataUtility.getLong(request.getParameter("studentId")));
        bean.setPhysics(DataUtility.getInt(request.getParameter("physics")));
        bean.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
        bean.setMaths(DataUtility.getInt(request.getParameter("maths")));
        populateDTO(request);
        return bean;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet method of Marksheet Ctl started");
        MarksheetModel model = new MarksheetModel();
        long id = DataUtility.getLong(request.getParameter("id"));
        if (id > 0) {
            MarksheetBean bean;
            try {
                bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        log.debug("doGet method of Marksheet Ctl ended");
        ServletUtility.forward(getView(), request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost method of Marksheet Ctl started");
        String op = DataUtility.getString(request.getParameter("operation"));
        MarksheetModel model = new MarksheetModel();
        long id = DataUtility.getLong(request.getParameter("id"));
        MarksheetBean bean = (MarksheetBean) populateBean(request);
        try {
            if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
                try {
                    if (validate(request)) {
                        if (id > 0) {
                            model.update(bean);
                            ServletUtility.setSuccessMessage("Marksheet is updated", request);
                        } else {
                            model.add(bean);
                            ServletUtility.setSuccessMessage("Marksheet is saved", request);
                        }
                    } else {
                        ServletUtility.setBean(bean, request);
                        ServletUtility.forward(getView(), request, response);
                        return;
                    }
                } catch (ApplicationException e) {
                    log.error(e);
                    ServletUtility.handleException(e, request, response);
                } catch (DuplicateRecordException e) {
                    log.error(e);
                    ServletUtility.setErrorMessage(e.getMessage(), request);
                }
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                model.delete(bean);
                ServletUtility.redirect(ORSView.MARKSHEET_LIST_VIEW, request, response);
                return;
            }
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
        }
        log.debug("doPost method of Marksheet Ctl ended");
        ServletUtility.forward(getView(), request, response);
    }
    @Override
    protected String getView() {
        return ORSView.MARKSHEET_VIEW;
    }

}