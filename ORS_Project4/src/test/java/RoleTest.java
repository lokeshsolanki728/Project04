package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.RoleModel;

/**
 * Role Model Test classes.
 *
 * @author Lokesh SOlanki
 *
 */
public class RoleTest {

    public static RoleModel model = new RoleModel();

    public static void main(String[] args) throws ApplicationException {

        testAdd();
        testDelete();
        testUpdate();
        testFindByPK();
        testFindByName();
        testSearch();
        testList();
    }

    public static void testAdd() {
        try {
            RoleBean bean = new RoleBean();
            bean.setName("TestRole");
            bean.setDescription("Test Description");
            bean.setCreatedBy("Admin");
            bean.setModifiedBy("Admin");
            bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
            bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
            long pk = model.add(bean);
            RoleBean addedbean = model.findByPK(pk);
            assertNotNull(addedbean);
            assertEquals(bean.getName(), addedbean.getName());
            assertEquals(bean.getDescription(), addedbean.getDescription());

        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testDelete() throws ApplicationException {

        try {
            RoleBean bean = new RoleBean();
            bean.setName("ToDelete");
            bean.setDescription("ToDelete Description");
            long pk = model.add(bean);
            bean.setId(pk);
            model.delete(bean);
            RoleBean deletebean = model.findByPK(pk);
            assertNull(deletebean);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

    }

    public static void testUpdate() {
        try {
            RoleBean bean = model.findByName("TestRole");
            bean.setName("UpdateRole");
            bean.setDescription("update Desc");
            model.update(bean);
            RoleBean updatebean = model.findByPK(bean.getId());
            assertEquals("UpdateRole", updatebean.getName());
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testFindByPK() {
        try {
            RoleBean bean = model.findByName("UpdateRole");
            RoleBean getBean = model.findByPK(bean.getId());
            assertNotNull(getBean);
            assertEquals(bean.getName(), getBean.getName());
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }

    }

    public static void testFindByName() {
        try {
            RoleBean bean = model.findByName("UpdateRole");
            assertNotNull(bean);
            assertEquals("UpdateRole", bean.getName());
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testSearch() {
        try {
            RoleBean bean = new RoleBean();
            List list = new ArrayList();
            list = model.search(bean, 0, 0);
            assertFalse(list.isEmpty());
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testList() {
        try {
            List list = model.list(0, 0);
            assertFalse(list.isEmpty());
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
