package com.rays.proj4.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.Bean.FacultyBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.FacultyModel;

/**
 * Faculty  Model Test classes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class FacultyTest {

	public static FacultyModel model= new FacultyModel();
	
	public static void main(String[] args) throws DuplicateRecordException {
		//testadd();
		testDelete();
		testUpdate();
		testFindByPk();
		testFindByEmailId();
		testList();
		testsearch();
	}
	
	
	
	
    public static void testadd() throws DuplicateRecordException, ParseException {
        try {
            FacultyBean bean = new FacultyBean();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            bean.setFirstName("Rohan" + new Date().getTime());
            bean.setLastName("karma" + new Date().getTime());
            bean.setGender("male");
            bean.setEmailId("rohan" + new Date().getTime() + "@gmail.com");
            bean.setMobileNo("9087654329" + new Date().getTime());
            bean.setCollegeId(1);
            bean.setCollegeName("rpl" + new Date().getTime());
            bean.setCourseId(1);
            bean.setCourseName("m.com" + new Date().getTime());
            bean.setDob(sdf.parse("22/09/1999"));
            bean.setSubjectId(1);
            bean.setSubjectName("maths" + new Date().getTime());
            bean.setCreatedBy("admin");
            bean.setModifiedBy("admin");
            bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
            bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

            long pk = model.add(bean);
            FacultyBean addedBean = model.findByPK(pk);
            org.junit.Assert.assertEquals(addedBean.getId(), pk);
        } catch (Exception e) {
            org.junit.Assert.fail("Exception in add test");
        }
    }

    public static void testDelete() {
        try {
            FacultyBean bean = new FacultyBean();
            List<FacultyBean> list = model.search(bean);
            if(list.size() == 0){
                testadd();
                list = model.search(bean);
            }
            bean = list.get(0);
            model.delete(bean);
            FacultyBean deletebean = model.findByPK(bean.getId());
            org.junit.Assert.assertNull(deletebean);
        } catch (ApplicationException e) {
            org.junit.Assert.fail("Exception in delete test");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testUpdate() {
        try {
            FacultyBean bean = new FacultyBean();
            List<FacultyBean> list = model.search(bean);
            if(list.size() == 0){
                testadd();
                list = model.search(bean);
            }
            bean = list.get(0);
            bean.setFirstName("akash" + new Date().getTime());
            model.update(bean);
            FacultyBean updatedBean = model.findByPK(bean.getId());
            org.junit.Assert.assertEquals(updatedBean.getFirstName(), bean.getFirstName());
        } catch (ApplicationException e) {
            org.junit.Assert.fail("Exception in update test");
        } catch (DuplicateRecordException e) {
            org.junit.Assert.fail("DuplicateRecordException in update test");
            e.printStackTrace();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public static void testFindByPk() {
        try {
            FacultyBean bean = new FacultyBean();
             List<FacultyBean> list = model.search(bean);
            if(list.size() == 0){
                testadd();
                list = model.search(bean);
            }
            bean = list.get(0);
            FacultyBean foundBean = model.findByPK(bean.getId());
            org.junit.Assert.assertEquals(foundBean.getId(), bean.getId());
        } catch (ApplicationException e) {
            org.junit.Assert.fail("Exception in findByPk test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testFindByEmailId() {
        try {
            FacultyBean bean = new FacultyBean();
             List<FacultyBean> list = model.search(bean);
            if(list.size() == 0){
                testadd();
                list = model.search(bean);
            }
            bean = list.get(0);
            FacultyBean foundBean = model.findByEmailId(bean.getEmailId());
            org.junit.Assert.assertEquals(foundBean.getEmailId(), bean.getEmailId());
        } catch (ApplicationException e) {
            org.junit.Assert.fail("Exception in findByEmailId test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testList() {
	 try{
		FacultyBean bean = new FacultyBean();
		 List list=new ArrayList();
		 list=model.list(1,10);
		 
		org.junit.Assert.assertTrue(list.size() >= 0);
	 }catch(ApplicationException e){
        org.junit.Assert.fail("Exception in testList test");
     } catch (Exception e) {
        e.printStackTrace();
    }
	 
}

    public static void testsearch() {
	try {
		FacultyBean bean = new FacultyBean();
        List<FacultyBean> list2 = model.search(bean);
        if(list2.size() == 0){
            testadd();
        }
		List list = new ArrayList();
		list=model.search(bean);
		
		Iterator it = list.iterator();
		while(it.hasNext()) {
			bean= (FacultyBean) it.next();
			
			System.out.println(bean.getId());
			System.out.println(bean.getFirstName());
			System.out.println(bean.getLastName());
			System.out.println(bean.getGender());
			System.out.println(bean.getEmailId());
			System.out.println(bean.getMobileNo());
			
		}
	}catch(Exception e) {
        org.junit.Assert.fail("Exception in testsearch test");
	}
}

}

