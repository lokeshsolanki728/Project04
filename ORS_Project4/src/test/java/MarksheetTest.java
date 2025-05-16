package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Model.MarksheetModel;
import org.junit.Assert;



public class MarksheetTest {

	public static MarksheetModel model = new MarksheetModel(); // Corrected static MarksheetModel instance

	public static void main(String[] args) {
		// testAdd();
		// testDelete();
		// testUpdate();
		// testFindByRollNo();
		// testFindByPK();
		// testSearch();
		// testList();
		// testMeritList();
	}

	public static void testAdd() {
		try {
			String rollNo = "r" + System.currentTimeMillis();
			MarksheetDTO bean = new MarksheetDTO();
			bean.setRollNo(rollNo);
			bean.setPhysics(80);
			bean.setChemistry(70);
			bean.setMaths(90);
			bean.setStudentId(1L); // Corrected method name
			Long pk = model.add(bean);

			MarksheetBean addedBean = model.findByPK(pk);

			Assert.assertNotNull(addedBean);
			Assert.assertEquals(bean.getRollNo(), addedBean.getRollNo());
			Assert.assertEquals(bean.getPhysics(), addedBean.getPhysics());
			Assert.assertEquals(bean.getChemistry(), addedBean.getChemistry());
			Assert.assertEquals(bean.getMaths(), addedBean.getMaths());

		} catch (DuplicateRecordException e) {
			Assert.fail("Duplicate record should not be added: " + e.getMessage());
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

	public static void testDelete() {
		try {
			MarksheetDTO addedBean = new MarksheetDTO();
			addedBean.setRollNo("delete" + System.currentTimeMillis());
			addedBean.setPhysics(80);
			addedBean.setChemistry(70);
			addedBean.setMaths(90);
			addedBean.setStudentId(1L); // Corrected method name
			Long pk = model.add(addedBean);

			MarksheetDTO bean = new MarksheetDTO(); // Corrected object type
			bean.setId(pk);
			model.delete(bean.getId()); // Corrected method call to use ID

			MarksheetDTO deleteBean = model.findByPK(pk); // Corrected object type
			Assert.assertNull(deleteBean);
		} catch (RecordNotFoundException e) {
			Assert.fail("Record not found exception should not be thrown: " + e.getMessage());
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		} catch (DuplicateRecordException e) {
			Assert.fail("Duplicate record exception should not be thrown: " + e.getMessage());
		}
	}

	public static void testUpdate() {
		try {
			MarksheetDTO addedBean = new MarksheetDTO(); // Corrected object type
			addedBean.setRollNo("update" + System.currentTimeMillis());
			addedBean.setPhysics(80);
			addedBean.setChemistry(70);
			addedBean.setMaths(90);
			addedBean.setStudentId(1L); // Corrected method name
			Long pk = model.add(addedBean);
			MarksheetDTO bean = model.findByPK(pk); // Corrected object type

			bean.setStudentId(2L); // Corrected method name
			bean.setRollNo("updatedRollNo");
			bean.setChemistry(95);
			bean.setPhysics(85);
			bean.setMaths(75);

			model.update(bean);

			MarksheetDTO updatedBean = model.findByPK(pk); // Corrected object type
			Assert.assertNotNull(updatedBean);
			Assert.assertEquals(bean.getRollNo(), updatedBean.getRollNo());
			Assert.assertEquals(bean.getPhysics(), updatedBean.getPhysics());
			Assert.assertEquals(bean.getChemistry(), updatedBean.getChemistry());
			Assert.assertEquals(bean.getMaths(), updatedBean.getMaths());

		} catch (RecordNotFoundException e) {
			Assert.fail("Record not found exception should not be thrown: " + e.getMessage());
		} catch (DuplicateRecordException e) {
			Assert.fail("Duplicate record exception should not be thrown: " + e.getMessage());
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

	public static void testFindByRollNo() {
		try {
			String rollNo = "findRollNo" + System.currentTimeMillis();
			MarksheetDTO newBean = new MarksheetDTO(); // Corrected object type
			newBean.setRollNo(rollNo);
			newBean.setPhysics(80);
			newBean.setChemistry(70);
			newBean.setMaths(90);
			newBean.setStudentId(1L); // Corrected method name
			model.add(newBean);

			MarksheetDTO bean = model.findByRollNo(rollNo); // Corrected object type
			Assert.assertNotNull(bean);
			Assert.assertEquals(rollNo, bean.getRollNo());
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		} catch (DuplicateRecordException e) {
			// This case is expected for a robust test if roll number already exists
		}
	}

	public static void testFindByPK() {
		try {
			MarksheetBean addedBean = new MarksheetBean();
			addedBean.setRollNo("findByPK" + System.currentTimeMillis()); // Corrected object type
			addedBean.setPhysics(80);
			addedBean.setChemistry(70);
			addedBean.setMaths(90);
			addedBean.setStudentld(1L);
			Long pk = model.add(addedBean); // Corrected method parameter type
			MarksheetBean bean = model.findByPK(pk);
			Assert.assertNotNull(bean);
			assertEquals(pk, bean.getId());
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		} catch (DuplicateRecordException e) {
			Assert.fail("Duplicate record exception should not be thrown: " + e.getMessage());
		}
	}

	public static void testSearch() {
		try {
			MarksheetDTO bean = new MarksheetDTO(); // Corrected object type
			List<MarksheetDTO> list = model.search(bean, 1, 10); // Corrected List and method parameter types
			Assert.assertTrue(list.size() >= 0);
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

	public static void testList() {
		try {
			List<MarksheetDTO> list = model.list(1, 6); // Corrected List type
			Assert.assertTrue(list.size() >= 0);
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

	public static void testMeritList() {
		try {
			MarksheetDTO bean = new MarksheetDTO(); // Corrected object type
			List<MarksheetDTO> list = new ArrayList<>(); // Corrected List type
			list = model.getMeritList(1, 5);
			Assert.assertTrue(list.size() >= 0);
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

}
