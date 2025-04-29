package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.Bean.SubjectBean;
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
			SubjectBean bean = new SubjectBean();
			bean.setSubjectName("Subject-" + new Date().getTime());
			bean.setDescription("Description-" + new Date().getTime());
			bean.setCourseId(1L);
			bean.setCourseName("Course-" + new Date().getTime());
			bean.setCreatedBy("admin");
			bean.setModifiedBy("admin");
			bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
			bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(bean);
            SubjectBean addedBean = model.FindByPK(pk);
            assertEquals(bean.getSubjectName(), addedBean.getSubjectName());

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
            throw e;
		}
	}

	public static void testDelete() throws ApplicationException {
		try {
            List<SubjectBean> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10);
            }
            SubjectBean temp = list.get(0);
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
			SubjectBean bean = new SubjectBean();
            List<SubjectBean> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10);
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
            bean = model.findByName(list.get(0).getSubjectName());
            assertEquals(bean.getSubjectName(),list.get(0).getSubjectName());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static void testUpdate() throws ApplicationException, DuplicateRecordException {
		try {
            List<SubjectBean> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10);
            }
			SubjectBean bean = model.FindByPK(list.get(0).getId());
            String oldSubjectName = bean.getSubjectName();
			bean.setSubjectName("New-"+new Date().getTime());
			model.update(bean);
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
            List<SubjectBean> list = model.list(1,10);
            if(list.size() == 0){
                testadd();
                list = model.list(1,10);
            }
			SubjectBean bean = new SubjectBean();
			
			bean = model.FindByPK(list.get(0).getId());
			
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
			SubjectBean bean = new SubjectBean();
			//bean.setSubjectName("Java");
		    
			//bean.setCourseId(2L);
            bean.setCourseName("Course-");
            
			List list = new ArrayList();
			list = model.search(bean,1,10);
            boolean empty = true;
			Iterator it = list.iterator();
			while (it.hasNext()) {
                empty = false;
				bean = (SubjectBean) it.next();
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
			SubjectBean bean = new SubjectBean();
			List list = new ArrayList();
			list = model.list(1, 10);
            assertEquals(true,list.size()>=0);
			Iterator it = list.iterator();
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
