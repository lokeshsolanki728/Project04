package com.rays.proj4.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.rays.pro4.DTO.UserDTO;
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
 UserDTO dto = new UserDTO();
            String login = "test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com";
            String password = "password";
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin(login);
            bean.setPassword(password);
 dto.setDob(sdf.parse("01-01-1990"));
 dto.setRoleId(1L);
 dto.setGender("Male");
 dto.setMobileNo("9999999999");
 dto.setConfirmPassword(password);
            model.add(dto);

 UserDTO authenticatedUser = model.authenticate(login, password);
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
 List<UserDTO> list = model.list(1, 10);
            Assert.assertTrue(list.size() > 0);
            for (UserDTO dto : list) {
                Assert.assertNotNull(dto.getId());
 System.out.println(dto.getId());
 System.out.println(dto.getFirstName());
 System.out.println(dto.getLastName());
 System.out.println(dto.getLogin());
 System.out.println(dto.getPassword());
 System.out.println(dto.getDob());
 System.out.println(dto.getRoleId());
 System.out.println(dto.getUnSuccessfulLogin());
 System.out.println(dto.getGender());
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
 UserDTO dto = new UserDTO();
 dto.setRoleId(1L);
 List<UserDTO> list = model.getRoles(dto);
            Assert.assertTrue(list.size() >= 0);
            for (UserDTO userDTO : list) {
 System.out.println(userDTO.getId());
 System.out.println(userDTO.getFirstName());
 System.out.println(userDTO.getLastName());
 System.out.println(userDTO.getLogin());
 System.out.println(userDTO.getPassword());
 System.out.println(userDTO.getDob());
 System.out.println(userDTO.getRoleId());
 System.out.println(userDTO.getUnSuccessfulLogin());
 System.out.println(userDTO.getGender());
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
 UserDTO dto = new UserDTO();
 List<UserDTO> list = model.search(dto, 0, 10);
            Assert.assertTrue(list.size() >= 0);

            for (UserDTO userDTO : list) {
 System.out.println(userDTO.getId());
 System.out.println(userDTO.getFirstName());
 System.out.println(userDTO.getLastName());
 System.out.println(userDTO.getLogin());
 System.out.println(userDTO.getPassword());
 System.out.println(userDTO.getDob());
 System.out.println(userDTO.getRoleId());
 System.out.println(userDTO.getUnSuccessfulLogin());
 System.out.println(userDTO.getGender());
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
 UserDTO dto = new UserDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        bean.setFirstName("Test");
        bean.setLastName("User");
        bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
        bean.setPassword("password");
 dto.setDob(sdf.parse("01-01-1990"));
 dto.setRoleId(1L);
 dto.setGender("Male");
 dto.setMobileNo("9999999999");
 dto.setConfirmPassword("password");
 long pk = model.add(dto);
        try {
 dto = model.findByPK(pk);
            Assert.assertNotNull(dto);

 dto.setFirstName("Updated");
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
 UserDTO dto = new UserDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
            bean.setPassword("password");
 dto.setDob(sdf.parse("01-01-1990"));
 dto.setRoleId(1L);
 dto.setGender("Male");
 dto.setMobileNo("9999999999");
 dto.setConfirmPassword("password");
 long pk = model.add(dto);
           try{
 UserDTO retrievedDTO = model.findByPK(pk);
            Assert.assertNotNull(retrievedBean);
            Assert.assertEquals(bean.getLogin(), retrievedBean.getLogin());
 System.out.println(retrievedDTO.getId());
 System.out.println(retrievedDTO.getFirstName());
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
 UserDTO dto = new UserDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
            bean.setPassword("password");
 dto.setDob(sdf.parse("01-01-1990"));
 dto.setRoleId(1L);
 dto.setGender("Male");
 dto.setMobileNo("9999999999");
 dto.setConfirmPassword("password");
 long pk = model.add(dto);
            try {
 UserDTO retrievedDTO = model.findByLogin(dto.getLogin());

                Assert.assertNotNull(retrievedDTO);
                Assert.assertEquals(dto.getLogin(), retrievedDTO.getLogin());
 System.out.println(retrievedDTO.getId());
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
 UserDTO dto = new UserDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        bean.setFirstName("Test");
        bean.setLastName("User");
        bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
        bean.setPassword("password");
 dto.setDob(sdf.parse("01-01-1990"));
 dto.setRoleId(1L);
 dto.setGender("Male");
 dto.setMobileNo("9999999999");
 dto.setConfirmPassword("password");
 long pk = model.add(dto);
        try {
 model.delete(dto);
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
 UserDTO dto = new UserDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            bean.setFirstName("Test");
            bean.setLastName("User");
            bean.setLogin("test" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
            bean.setPassword("password");
 dto.setDob(sdf.parse("01-01-1990"));
 dto.setRoleId(1L);
 dto.setGender("Male");
 dto.setMobileNo("9999999999");
 dto.setConfirmPassword("password");
 long pk = model.add(dto);
 UserDTO addedDTO = model.findByPK(pk);
            Assert.assertNotNull(addedDTO);
            Assert.assertEquals("Test", addedDTO.getFirstName());
            Assert.assertEquals(bean.getLogin(), addedbean.getLogin());
            System.out.println("Test add succ");
            System.out.println("record insert");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception during insert: " + e.getMessage());
        }
    }
}