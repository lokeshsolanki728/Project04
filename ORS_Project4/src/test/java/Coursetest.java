package com.rays.pro4.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;
import org.junit.Assert;

/**
 * Course Model Test classes.
 *
 * @author Lokesh SOlanki
 */
public class Coursetest  {

	public static CourseModel model = new CourseModel();


	public static void main(String[] args) throws Exception {
		testadd();
		testDelete();
        testFindByName();
        testFindByPk();
        testUpdate();
		testsearch();
		testlist();

	}


    public static void testadd() {
        try {
            CourseBean bean = new CourseBean();
            bean.setName("B.Tech");
            bean.setDescription("Engineering");
            bean.setDuration("4 Years");
            bean.setCreatedBy("admin");
            bean.setModifiedBy("admin");
            bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
            bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

            long pk = model.add(bean);
            CourseBean addedBean = model.FindByPK(pk);
            Assert.assertNotNull(addedBean);
            Assert.assertEquals("B.Tech", addedBean.getName());
            Assert.assertEquals("Engineering", addedBean.getDescription());
            Assert.assertEquals("4 Years", addedBean.getDuration());

        } catch (DuplicateRecordException e) {
            Assert.fail("Duplicate Record Exception: " + e.getMessage());
        } catch (ApplicationException e) {
            Assert.fail("Application Exception: " + e.getMessage());
        }
    }

	public static void testDelete() {
		CourseBean bean = new CourseBean();
		bean.setName("BCA");
		bean.setDescription("Computer Application");
		bean.setDuration("3 Year");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		try {
            long pk = model.add(bean);
            bean.setId(pk);
			model.Delete(bean);
			CourseBean deletedBean = model.FindByPK(pk);
			Assert.assertNull(deletedBean);
		} catch (ApplicationException | DuplicateRecordException e) {
			Assert.fail("Application Exception: " + e.getMessage());
		}
	} 

	public static void testFindByName() {
        try {
            CourseBean bean = new CourseBean();
            bean.setName("MBA");
            bean.setDescription("Master of Business Administration");
            bean.setDuration("2 Years");
            bean.setCreatedBy("admin");
            bean.setModifiedBy("admin");
            bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
            bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
                model.add(bean);
                CourseBean foundBean = model.findByName("MBA");
                Assert.assertNotNull(foundBean);
                Assert.assertEquals("MBA", foundBean.getName());
            
        } catch (ApplicationException | DuplicateRecordException e) {
            Assert.fail("Exception: " + e.getMessage());
        }
	}

	public static void testFindByPk() {
		CourseBean bean = new CourseBean();
		bean.setName("MCA");
		bean.setDescription("Master of Computer Application");
		bean.setDuration("2 Years");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		try {
			long pk = model.add(bean);
			CourseBean foundBean = model.FindByPK(pk);
			Assert.assertNotNull(foundBean);
			Assert.assertEquals("MCA", foundBean.getName());
			Assert.assertEquals(pk, foundBean.getId());
		} catch (ApplicationException | DuplicateRecordException e) {
			Assert.fail("Exception: " + e.getMessage());
		}
	}
	
	public static void testUpdate() {
		CourseBean bean = new CourseBean();
		bean.setName("BBA");
		bean.setDescription("Bachelor of Business Administration");
		bean.setDuration("3 Years");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		try {
			long pk = model.add(bean);
			bean.setId(pk);
			bean.setName("BBA (Updated)");
			model.update(bean);
			CourseBean updatedBean = model.FindByPK(pk);
			Assert.assertEquals("BBA (Updated)", updatedBean.getName());
		} catch (ApplicationException | DuplicateRecordException e) {
			Assert.fail("Exception: " + e.getMessage());
		}
	}

	public static void testsearch() {
		try {
            CourseBean bean = new CourseBean();
            List<CourseBean> list = model.search(bean);
            Assert.assertNotNull(list);
            Assert.assertTrue(list.size() > 0);

            for (CourseBean course : list) {
                Assert.assertNotNull(course.getName());
                Assert.assertNotNull(course.getDescription());
                Assert.assertNotNull(course.getDuration());
            }
        } catch (ApplicationException e) {
            Assert.fail("Application Exception: " + e.getMessage());
        }
	}
	public static void testlist() {
		try {
			List list = model.list(1, 10);
			Assert.assertTrue(list.size() > 0);
		} catch (ApplicationException e) {
			Assert.fail("Application Exception: " + e.getMessage());
		}
	}

}
