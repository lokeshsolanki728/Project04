package com.rays.proj4.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.StudentModel;

/**
 * Student  Model Test classes.
 * @author Lokesh SOlanki
 *
 */
public class StudentTest {


	public static StudentModel model= new StudentModel();
	
	
	public static void main(String[] args) throws ParseException {
		testAdd();
		testDelete();
		testUpdate();
		testFindByPK();
		testFindByEmailId();
		testSearch();
		testList();
		
	}


	public static void testAdd() throws ParseException, DuplicateRecordException, DatabaseException {
		
		try{
			StudentDTO bean=new StudentDTO();
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            long id = new Date().getTime();
            bean.setFirstName("FirstName" + id);
			bean.setLastName("LastName" + id);
            bean.setDob(sdf.parse("22/09/2000"));
            bean.setMobileNo("99999999" + id);
            bean.setEmail("test" + id + "@gmail.com");
			bean.setCollegeId(1l);
			bean.setCreatedBy("admin");
			bean.setModifiedBy("admin");
			bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
			bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
			long pk = model.add(bean);
			StudentDTO addbean = model.findByPK(pk);
            org.junit.Assert.assertNotNull(addbean);
            org.junit.Assert.assertEquals(bean.getFirstName(), addbean.getFirstName());
            org.junit.Assert.assertEquals(bean.getLastName(), addbean.getLastName());

		}catch(ApplicationException e){
			org.junit.Assert.fail("Test add fail" + e.getMessage());
		}catch(DuplicateRecordException e){
			org.junit.Assert.fail("Test add fail" + e.getMessage());
		}
	}
	
	public static void testDelete() throws ApplicationException, DatabaseException{
		
		try{
			  StudentDTO bean = new StudentDTO();
	            long pk = new Date().getTime();
	            bean.setId(pk);
	            model.delete(bean);
	            StudentDTO deletebean = model.findByPK(pk);
	            org.junit.Assert.assertNull(deletebean);
		}catch(ApplicationException e){
			org.junit.Assert.fail("Test Delete fail" + e.getMessage());
		}
	}
	public static void testUpdate() throws ApplicationException, DuplicateRecordException, ParseException, DatabaseException {

        try {
             SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            StudentDTO bean = model.findByPK(1L);
            bean.setFirstName("Update name");
            bean.setLastName("Update last");
             bean.setDob(sdf.parse("01/01/2001"));
            bean.setCollegeId(1L);
            model.update(bean);
            StudentBean updateBean = model.findByPK(1L);
            org.junit.Assert.assertNotNull(updateBean);
            org.junit.Assert.assertEquals("Update name", updateBean.getFirstName());
            org.junit.Assert.assertEquals("Update last", updateBean.getLastName());

            
        } catch (ApplicationException e) {
          org.junit.Assert.fail("Test Update fail" + e.getMessage());
        } catch (DuplicateRecordException e) {
           org.junit.Assert.fail("Test Update fail" + e.getMessage());
        }
    }
	public static void testFindByPK() throws ApplicationException, DatabaseException{
		try{
			StudentDTO bean = new StudentDTO();
			Long pk=1L;
			bean = model.findByPK(pk);
             org.junit.Assert.assertNotNull(bean);
            org.junit.Assert.assertEquals(pk, bean.getId());
		}catch(ApplicationException e){
			 org.junit.Assert.fail("Test Find By PK fail" + e.getMessage());
		}
	}
	 public static void testFindByEmailId() throws ApplicationException, DatabaseException {
	        try {
	            StudentDTO bean = model.findByEmailId("test1699373289895@gmail.com");
	             org.junit.Assert.assertNull(bean);
               StudentDTO notnullbean = new StudentDTO();
            notnullbean.setEmail("test1699373289895@gmail.com");
            notnullbean.setCollegeId(1l);
             model.add(notnullbean);
             StudentBean bean2 = model.findByEmailId("test1699373289895@gmail.com");
              org.junit.Assert.assertNotNull(bean2);
	        } catch (ApplicationException e) {
	          org.junit.Assert.fail("Test Find By EmailId fail" + e.getMessage());
	        } catch (DuplicateRecordException e) {
            org.junit.Assert.fail("Test Find By EmailId fail" + e.getMessage());
	        }
	    }
	 public static void testSearch() throws ApplicationException, DatabaseException{
		 try{
			 StudentDTO bean= new StudentDTO();
			 List list=new ArrayList();
			// bean.setFirstName("ram");
			// bean.setEmail("kmalviya30@gmail.com");
			// bean.setCollegeName("RML Maheshwari");
			 bean.setCollegeId(1L);
			 list=model.search(bean,1,10);
               org.junit.Assert.assertNotNull(list);
            org.junit.Assert.assertTrue(list.size() > 0);
			 Iterator it=list.iterator();
			 while(it.hasNext()){ // Corrected loop to use DTO
				 bean=(StudentBean)it.next();
				  org.junit.Assert.assertNotNull(bean.getId());
                 
			 }
		 }catch(ApplicationException e){
			 org.junit.Assert.fail("Test Search fail" + e.getMessage());
		 }
	 }
	 public static void testList() throws ApplicationException, DatabaseException{
		 try{
			 StudentDTO bean=new StudentDTO();
			 List list=new ArrayList();
			 list=model.list(1,10);
            org.junit.Assert.assertNotNull(list);
             org.junit.Assert.assertTrue(list.size() > 0);
			 Iterator it = list.iterator(); // Corrected loop to use DTO
			 while(it.hasNext()){
				 bean=(StudentBean)it.next();
				 org.junit.Assert.assertNotNull(bean.getId());
                 
			 }
		 }catch(ApplicationException e){
			  org.junit.Assert.fail("Test List fail" + e.getMessage());
		 }
		 
	 }
	
}
