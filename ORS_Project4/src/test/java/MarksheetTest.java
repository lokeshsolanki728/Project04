package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Model.MarksheetModel;
import org.junit.Assert;



public class MarksheetTest {

	public static MarksheetModel model = new MarksheetModel();

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
			MarksheetBean bean = new MarksheetBean();
			bean.setRollNo(rollNo);
			bean.setPhysics(80);
			bean.setChemistry(70);
			bean.setMaths(90);
			bean.setStudentld(1L);
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
			MarksheetBean addedBean = new MarksheetBean();
			addedBean.setRollNo("delete" + System.currentTimeMillis());
			addedBean.setPhysics(80);
			addedBean.setChemistry(70);
			addedBean.setMaths(90);
			addedBean.setStudentld(1L);
			Long pk = model.add(addedBean);

			MarksheetBean bean = new MarksheetBean();
			bean.setId(pk);
			model.delete(bean);

			MarksheetBean deleteBean = model.findByPK(pk);
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
			MarksheetBean addedBean = new MarksheetBean();
			addedBean.setRollNo("update" + System.currentTimeMillis());
			addedBean.setPhysics(80);
			addedBean.setChemistry(70);
			addedBean.setMaths(90);
			addedBean.setStudentld(1L);
			Long pk = model.add(addedBean);
			MarksheetBean bean = model.findByPK(pk);

			bean.setStudentld(2L);
			bean.setRollNo("updatedRollNo");
			bean.setChemistry(95);
			bean.setPhysics(85);
			bean.setMaths(75);

			model.update(bean);

			MarksheetBean updatedBean = model.findByPK(pk);
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
			MarksheetBean newBean = new MarksheetBean();
			newBean.setRollNo(rollNo);
			newBean.setPhysics(80);
			newBean.setChemistry(70);
			newBean.setMaths(90);
			newBean.setStudentld(1L);
			model.add(newBean);

			MarksheetBean bean = model.findByRollNo(rollNo);
			Assert.assertNotNull(bean);
			Assert.assertEquals(rollNo, bean.getRollNo());
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		} catch (DuplicateRecordException e) {
			Assert.fail("Duplicate record exception should not be thrown: " + e.getMessage());
		}
	}

	public static void testFindByPK() {
		try {
			MarksheetBean addedBean = new MarksheetBean();
			addedBean.setRollNo("findByPK" + System.currentTimeMillis());
			addedBean.setPhysics(80);
			addedBean.setChemistry(70);
			addedBean.setMaths(90);
			addedBean.setStudentld(1L);
			Long pk = model.add(addedBean);
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
			MarksheetBean bean = new MarksheetBean();
			List<MarksheetBean> list = model.search(bean, 1, 10);
			Assert.assertTrue(list.size() >= 0);
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

	public static void testList() {
		try {
			List<MarksheetBean> list = model.list(1, 6);
			Assert.assertTrue(list.size() >= 0);
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

	public static void testMeritList() {
		try {
			MarksheetBean bean = new MarksheetBean();
			List list = new ArrayList();
			list = model.getMeritList(1, 5);
			Assert.assertTrue(list.size() >= 0);
		} catch (ApplicationException e) {
			Assert.fail("Application exception occurred: " + e.getMessage());
		}
	}

}
