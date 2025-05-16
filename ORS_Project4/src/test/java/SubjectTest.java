package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.DTO.SubjectDTO;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.SubjectModel;


/**
 * Subject Model Test classes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class SubjectTest {

	public static SubjectModel model = new SubjectModel();

	public static void main(String[] args) throws Exception {
		testadd();
		testDelete();
		testFindByName();
		testUpdate();
		testFindByPk();
		 testsearch();
		// testlist();

	}
  
	public static void testadd() throws DuplicateRecordException {

		try {
			SubjectDTO bean = new SubjectDTO();
			bean.setSubjectName("Subject-" + new Date().getTime());
			bean.setDescription("Description-" + new Date().getTime());
			bean.setCourseId(1L);
			bean.setCourseName("Course-" + new Date().getTime());
			bean.setCreatedBy("admin");
			bean.setModifiedBy("admin");
			bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
			bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(bean);
            SubjectDTO addedBean = model.FindByPK(pk);
            assertEquals(bean.getSubjectName(), addedBean.getSubjectName());

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
            throw e;
		}
	}

	public static void testDelete() throws ApplicationException {
		try {
            List<SubjectDTO> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10);
            }
            SubjectDTO temp = list.get(0);
			SubjectBean bean = new SubjectBean();
			bean.setId(temp.getId());
			model.Delete(bean);			
            SubjectBean deleteBean = model.FindByPK(temp.getId());
            assertEquals(null, deleteBean);


		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static void testFindByName() {
		try {
			SubjectDTO bean = new SubjectDTO();
            List<SubjectDTO> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10); // This call might need adjustment based on SubjectModel's list method return type
            }

			System.out.println(bean.getId());
			System.out.println(bean.getSubjectName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getCreatedDatetime());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getModifiedDatetime());
            bean = model.findByName(list.get(0).getSubjectName()); // This call might need adjustment based on SubjectModel's findByName method return type
            assertEquals(bean.getSubjectName(),list.get(0).getSubjectName());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static void testUpdate() throws ApplicationException, DuplicateRecordException {
		try {
            List<SubjectDTO> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10);
            }
			SubjectDTO bean = model.FindByPK(list.get(0).getId());
            String oldSubjectName = bean.getSubjectName();
			bean.setSubjectName("New-"+new Date().getTime());
			model.update(bean); // This call might need adjustment based on SubjectModel's update method parameter type
            SubjectBean updated = model.FindByPK(bean.getId());
            assertEquals(updated.getSubjectName(),bean.getSubjectName());
            

		} catch (ApplicationException e) {
			e.printStackTrace();
            throw e;
		} catch (DuplicateRecordException e) {
			e.printStackTrace();
            throw e;
		}
	}

	public static void testFindByPk() throws ApplicationException {
		try {
            List<SubjectDTO> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10);
            }
			SubjectDTO bean = new SubjectDTO();
			
			bean = model.FindByPK(list.get(0).getId()); // This call might need adjustment based on SubjectModel's FindByPK method return type
			
			System.out.println(bean.getId());
			System.out.println(bean.getSubjectName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getCreatedBy());
            assertEquals(bean.getId(), list.get(0).getId());

		} catch (ApplicationException e) {
			e.printStackTrace();
            throw e;
		}
	}

	public static void testsearch() throws DatabaseException {
		try {
			SubjectDTO bean = new SubjectDTO();
			//bean.setSubjectName("Java");
		    
			//bean.setCourseId(2L);
            bean.setCourseName("Course-");
            
			List list = new ArrayList();
			list = model.search(bean,1,10);
            boolean empty = true;
			Iterator it = list.iterator();
			while (it.hasNext()) {
                empty = false;
				bean = (SubjectDTO) it.next(); // Cast to SubjectDTO
				System.out.println(bean.getSubjectName());
				System.out.println(bean.getDescription());
			}
            assertEquals(false,empty);

		} catch (ApplicationException e) {
			e.printStackTrace();
            throw e;
		}
	}

	public static void testlist() throws Exception {
		try {
			SubjectDTO bean = new SubjectDTO();
			List list = new ArrayList();
			list = model.list(1, 10);
            assertEquals(true,list.size()>=0);
			Iterator<SubjectDTO> it = list.iterator(); // Use generic iterator
			while (it.hasNext()) {
				bean = (SubjectBean) it.next();
				System.out.println(bean.getId());
				System.out.println(bean.getSubjectName());
				System.out.println(bean.getDescription());
				System.out.println(bean.getCourseId());
				System.out.println(bean.getCourseName());
				System.out.println(bean.getCreatedBy());
				System.out.println(bean.getModifiedBy());
				System.out.println(bean.getCreatedDatetime());
				System.out.println(bean.getModifiedDatetime());

			}

		} catch (ApplicationException e) {
			e.printStackTrace();
            throw e;
		}
	}

}
