package com.rays.proj4.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Model.BaseModel;
import com.rays.pro4.Model.CollegeModel;


/**
 * College Model Test classes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class CollegeModelTest {
    static CollegeModel model = new CollegeModel();

    public static void main(String[] args) throws DuplicateRecordException, RecordNotFoundException {

        testAdd();
        testDelete();
        searchFindByName();
        searchFindByPk();
        update();
        search();
        list();
    }


	private static void list() {
		try {
			CollegeBean bean = new CollegeBean();
			List list = new ArrayList();
			CollegeModel  model = new CollegeModel();
			list = model.list(1,2);
            assertNotNull(list);
            assertEquals(true, list.size()>0);

			Iterator it = list.iterator();
			while (it.hasNext()) {
				bean = (CollegeBean) it.next();
			   assertNotNull(bean);
			}
		} catch (ApplicationException e) {
            e.printStackTrace();
            assertEquals(false, true);
        }
	}

    private static void search() {
        try {
            CollegeBean bean = new CollegeBean();
            List list = new ArrayList();
            bean.setName("IIT");

            CollegeModel model = new CollegeModel();
            list = model.search(bean, 0, 0);
            assertNotNull(list);
            assertEquals(true, list.size() > 0);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                bean = (CollegeBean) it.next();
                assertNotNull(bean);
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertEquals(false, true);
        }

    }

    private static void update() throws DuplicateRecordException, RecordNotFoundException, ApplicationException {

        CollegeBean bean = new CollegeBean();
        long pk = testAdd();
        bean = model.findByPK(pk);
        bean.setName("AU University");
        bean.setAddress("Lonavala");
        model.update(bean);

        CollegeBean updatedBean = model.findByPK(pk);
        assertNotNull(updatedBean);
        assertEquals("AU University", updatedBean.getName());

    }

    private static void searchFindByPk() throws DuplicateRecordException, RecordNotFoundException, ApplicationException {


        long pk = testAdd();
        CollegeBean bean = model.findByPK(pk);
        assertNotNull(bean);
        assertEquals(pk, bean.getId());

    }

    private static void searchFindByName() throws DuplicateRecordException, RecordNotFoundException, ApplicationException {

        long pk = testAdd();
        CollegeBean testBean = model.findByPK(pk);
        CollegeBean bean = model.findByName(testBean.getName());

        assertNotNull(bean);
        assertEquals(testBean.getName(), bean.getName());


    }

    private static void testDelete() throws DuplicateRecordException, RecordNotFoundException, ApplicationException {

        long pk = testAdd();
        CollegeBean bean = new CollegeBean();
        bean.setId(pk);
        model.delete(bean);
        try {
            model.findByPK(pk);
            assertEquals(true, false);
        } catch (RecordNotFoundException e) {
            assertEquals(true, true);
        }

    }

    private static long testAdd() throws DuplicateRecordException {
		try {

            CollegeBean bean = new CollegeBean();
            bean.setName("JIT" + new Date().getTime());
            bean.setAddress("Borawan");
            bean.setState("mp");
            bean.setCity("Khargone");
            bean.setPhoneNo("767856545465");
            bean.setCreatedBy("Admin");
            bean.setModifiedBy("Admin");
            bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
            bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

            long pk = model.add(bean);
            CollegeBean addedBean = model.findByPK(pk);
            assertNotNull(addedBean);
            assertEquals("JIT" + bean.getName().substring(3), addedBean.getName());
            return pk;
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertEquals(true, false);
        }
		return 0;
    }
    private static void testDelete1() {

		try {
			CollegeBean bean = new CollegeBean();
			bean.setId(15L);
			CollegeModel model = new CollegeModel();
			model.delete(bean);
			System.out.println("record delete");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}   
}
