package com.rays.pro4.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

/**
* The Class CourseCtl.
*  @author Lokesh SOlanki
*/
@WebServlet(name="CourseCtl", urlPatterns={"/ctl/CourseCtl"})
public class CourseCtl extends BaseCtl<CourseBean>{

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CourseCtl.class);

	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("CourseCtl validate started");
		boolean pass = true;
		String op = DataUtility.getString(request.getParameter("operation"));
		if (OP_CANCEL.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) { return pass; }
		if (DataValidator.isNull(request.getParameter("name"))) { request.setAttribute("name", PropertyReader.getValue("error.require", "Course Name"));
			 pass = false ;
		}else if (!DataValidator.isName(request.getParameter("name"))) { request.setAttribute("name", PropertyReader.getValue("error.name", "Course name"));
			 pass = false ;
		}
		if (DataValidator.isNull(request.getParameter("duration"))) {
			request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
			pass = false ;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false ;
		}

		log.debug("CourseCtl validate End");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	@Override	
	protected CourseBean populateBean(HttpServletRequest request){
		
		log.debug("CourseCtl PopulatedBean started");
		CourseBean bean = new CourseBean();
		
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDuration(DataUtility.getString(request.getParameter("duration")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
	
		populateDTO(bean, request);
		log.debug("CourseCtl PopulatedBean End");
		return bean;
	}
	
	/**
	 * Contains Display logics
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		CourseModel model = new CourseModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		
		if(id>0){
			CourseBean bean;
			try{
				bean = model.FindByPK(id);
				ServletUtility.setBean(bean, request);

			}catch(ApplicationException e){
				log.error(e);
				
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}
    
    
	/**
	 * Contains Submit logics
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String op = DataUtility.getString(request.getParameter("operation"));
		CourseModel model = new CourseModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		CourseBean bean =populateBean(request);
		
		if(OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)){
			try{
				if(OP_SAVE.equalsIgnoreCase(op)){
					bean.setId(0);
				}
				if(id>0){
					model.update(bean);	
					ServletUtility.setSuccessMessage(PropertyReader.getValue("success.course.update"), request);
				}else{					model.add(bean);
					
				}
				
			}catch(ApplicationException e ){
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setErrorMessage(PropertyReader.getValue("error.course.duplicate"), request);
			}
			ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
			return;
		}else if(OP_RESET.equalsIgnoreCase(op)){
		    ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
		    return;
		}
		else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		}
	
		
		ServletUtility.forward(getView(), request, response );
		
	
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	@Override
	protected String getView() {
		return ORSView.COURSE_VIEW;
	}
}
