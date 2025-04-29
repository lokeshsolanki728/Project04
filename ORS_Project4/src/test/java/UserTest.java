package com.rays.proj4.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.UserModel;
import org.junit.Assert;

/**
 * User Model Test classes.
 *
 * @author Lokesh SOlanki
 */
public class UserTest {

    public static void main(String[] args) throws ApplicationException, DuplicateRecordException, ParseException {
        testInsert();
        testDelete();
        testLogin();
        testFindByPk();
        testUpdate();
        testSearch();
        getRoleid();
        getList();
        authenticate();
    }

    private static void authenticate() {
        try {
            UserModel model = new UserModel();
            UserBean bean = new UserBean();
            String login = "test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com";
            String password = "password";
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin(login);
            bean.setPassword(password);
            bean.setDob(sdf.parse("01-01-1990"));
            bean.setRoleId(1L);
            bean.setGender("Male");
            bean.setMobileNo("9999999999");
            bean.setConfirmPassword(password);
            model.add(bean);

            UserBean authenticatedUser = model.authenticate(login, password);
            Assert.assertNotNull(authenticatedUser);
            Assert.assertEquals(login, authenticatedUser.getLogin());
            System.out.println("Successfully login");

        } catch (ApplicationException | ParseException | DuplicateRecordException e) {
            e.printStackTrace();
            Assert.fail("Exception during authentication: " + e.getMessage());
        }
    }

    private static void getList() {
        try {
            UserModel model = new UserModel();
            List<UserBean> list = model.list(1, 10);
            Assert.assertTrue(list.size() > 0);
            for (UserBean bean : list) {
                Assert.assertNotNull(bean.getId());
                System.out.println(bean.getId());
                System.out.println(bean.getFirstName());
                System.out.println(bean.getLastName());
                System.out.println(bean.getLogin());
                System.out.println(bean.getPassword());
                System.out.println(bean.getDob());
                System.out.println(bean.getRoleId());
                System.out.println(bean.getUnSuccessfulLogin());
                System.out.println(bean.getGender());
                System.out.println(bean.getLastLogin());
                System.out.println(bean.getLock());
                System.out.println(bean.getMobileNo());
                System.out.println(bean.getCreatedBy());
                System.out.println(bean.getModifiedBy());
                System.out.println(bean.getCreatedDatetime());
                System.out.println(bean.getModifiedDatetime());
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
            Assert.fail("Exception during getList: " + e.getMessage());
        }
    }

    private static void getRoleid() {
        try {
            UserModel model = new UserModel();
            UserBean bean = new UserBean();
            bean.setRoleId(1L);
            List<UserBean> list = model.getRoles(bean);
            Assert.assertTrue(list.size() >= 0);
            for (UserBean userBean : list) {
                System.out.println(userBean.getId());
                System.out.println(userBean.getFirstName());
                System.out.println(userBean.getLastName());
                System.out.println(userBean.getLogin());
                System.out.println(userBean.getPassword());
                System.out.println(userBean.getDob());
                System.out.println(userBean.getRoleId());
                System.out.println(userBean.getUnSuccessfulLogin());
                System.out.println(userBean.getGender());
                System.out.println(userBean.getLastLogin());
                System.out.println(userBean.getLock());
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
            Assert.fail("Exception during getRoles: " + e.getMessage());
        }
    }

    private static void testSearch() {
        try {
            UserModel model = new UserModel();
            UserBean bean = new UserBean();
            List<UserBean> list = model.search(bean, 0, 10);
            Assert.assertTrue(list.size() >= 0);

            for (UserBean userBean : list) {
                System.out.println(userBean.getId());
                System.out.println(userBean.getFirstName());
                System.out.println(userBean.getLastName());
                System.out.println(userBean.getLogin());
                System.out.println(userBean.getPassword());
                System.out.println(userBean.getDob());
                System.out.println(userBean.getRoleId());
                System.out.println(userBean.getUnSuccessfulLogin());
                System.out.println(userBean.getGender());
                System.out.println(userBean.getLastLogin());
                System.out.println(userBean.getLock());
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
            Assert.fail("Exception during search: " + e.getMessage());
        }
    }

    private static void testUpdate() throws DuplicateRecordException, ParseException, ApplicationException {
        UserModel model = new UserModel();
        UserBean bean = new UserBean();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        bean.setFirstName("Test");
        bean.setLastName("User");
        bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
        bean.setPassword("password");
        bean.setDob(sdf.parse("01-01-1990"));
        bean.setRoleId(1L);
        bean.setGender("Male");
        bean.setMobileNo("9999999999");
        bean.setConfirmPassword("password");
        long pk = model.add(bean);
        try {
             bean = model.findByPK(pk);
            Assert.assertNotNull(bean);

            bean.setFirstName("Updated");
            bean.setLastName("Updated");
            bean.setLogin("updated" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
            bean.setPassword("updated");
            model.update(bean);

            UserBean updatedBean = model.findByPK(bean.getId());
            Assert.assertEquals("Updated", updatedBean.getFirstName());
            Assert.assertEquals("Updated", updatedBean.getLastName());
            Assert.assertEquals(bean.getLogin(), updatedBean.getLogin());
            Assert.assertEquals("updated", updatedBean.getPassword());

            System.out.println("test update succ");

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            Assert.fail("Exception during update: " + e.getMessage());
        } finally {
           model.delete(bean);
        }

    }

    private static void testFindByPk() throws ParseException, DuplicateRecordException {
        try {
            UserModel model = new UserModel();
            UserBean bean = new UserBean();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
            bean.setPassword("password");
            bean.setDob(sdf.parse("01-01-1990"));
            bean.setRoleId(1L);
            bean.setGender("Male");
            bean.setMobileNo("9999999999");
            bean.setConfirmPassword("password");
            long pk = model.add(bean);
           try{
               UserBean retrievedBean = model.findByPK(pk);
            Assert.assertNotNull(retrievedBean);
            Assert.assertEquals(bean.getLogin(), retrievedBean.getLogin());
            System.out.println(retrievedBean.getId());
            System.out.println(retrievedBean.getFirstName());
            System.out.println(retrievedBean.getLastName());
            System.out.println(retrievedBean.getLogin());
            System.out.println(retrievedBean.getPassword());
            System.out.println(retrievedBean.getDob());
            System.out.println(retrievedBean.getRoleId());
            System.out.println(retrievedBean.getUnSuccessfulLogin());
            System.out.println(retrievedBean.getGender());
            System.out.println(retrievedBean.getLastLogin());
            System.out.println(retrievedBean.getLock());
           }catch (ApplicationException e){
               e.printStackTrace();
               Assert.fail("Exception during findByPK: " + e.getMessage());
           }finally {
               model.delete(bean);
           }
        } catch (ApplicationException | ParseException | DuplicateRecordException e) {
            e.printStackTrace();
            Assert.fail("Exception during findByPK: " + e.getMessage());
        }
    }

    private static void testLogin() throws ParseException, DuplicateRecordException {
        try {
            UserModel model = new UserModel();
             UserBean bean = new UserBean();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
            bean.setPassword("password");
            bean.setDob(sdf.parse("01-01-1990"));
            bean.setRoleId(1L);
            bean.setGender("Male");
            bean.setMobileNo("9999999999");
            bean.setConfirmPassword("password");
            long pk = model.add(bean);
            try {
                UserBean retrievedBean = model.findByLogin(bean.getLogin());

                Assert.assertNotNull(retrievedBean);
                Assert.assertEquals(bean.getLogin(), retrievedBean.getLogin());
                System.out.println(retrievedBean.getId());
                System.out.println(retrievedBean.getFirstName());
                System.out.println(retrievedBean.getLastName());
                System.out.println(retrievedBean.getLogin());
                System.out.println(retrievedBean.getPassword());
                System.out.println(retrievedBean.getDob());
                System.out.println(retrievedBean.getRoleId());
                System.out.println(retrievedBean.getUnSuccessfulLogin());
                System.out.println(retrievedBean.getGender());
                System.out.println(retrievedBean.getLastLogin());
                System.out.println(retrievedBean.getLock());
            }catch (ApplicationException e){
                 e.printStackTrace();
                 Assert.fail("Exception during findByLogin: " + e.getMessage());
            }finally {
                model.delete(bean);
            }
        } catch (ApplicationException | ParseException | DuplicateRecordException e) {
            e.printStackTrace();
            Assert.fail("Exception during findByLogin: " + e.getMessage());
        }
    }

    public static void testDelete() throws ApplicationException, ParseException, DuplicateRecordException {
        UserModel model = new UserModel();
        UserBean bean = new UserBean();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        bean.setFirstName("Test");
        bean.setLastName("User");
        bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
        bean.setPassword("password");
        bean.setDob(sdf.parse("01-01-1990"));
        bean.setRoleId(1L);
        bean.setGender("Male");
        bean.setMobileNo("9999999999");
        bean.setConfirmPassword("password");
        long pk = model.add(bean);
        try {

            model.delete(bean);
            UserBean deletedBean = model.findByPK(pk);
            Assert.assertNull(deletedBean);
        } catch (ApplicationException e) {
            e.printStackTrace();
            Assert.fail("Exception during delete: " + e.getMessage());
        }finally {
            model.delete(bean);
        }
    }

    public static void testInsert() throws ParseException, DuplicateRecordException {
        try {
            UserModel model = new UserModel();
            UserBean bean = new UserBean();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
            bean.setPassword("password");
            bean.setDob(sdf.parse("01-01-1990"));
            bean.setRoleId(1L);
            bean.setGender("Male");
            bean.setMobileNo("9999999999");
            bean.setConfirmPassword("password");
            long pk = model.add(bean);
            UserBean addedbean = model.findByPK(pk);
            Assert.assertNotNull(addedbean);
            Assert.assertEquals("Test", addedbean.getFirstName());
            Assert.assertEquals(bean.getLogin(), addedbean.getLogin());
            System.out.println("Test add succ");
            System.out.println("record insert");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception during insert: " + e.getMessage());
        }
    }
}