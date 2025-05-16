package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.RoleDTO;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Model.RoleModel;
import com.rays.pro4.Model.UserModel;

public class UserModelTest {

	private UserModel model = new UserModel();
	private RoleModel roleModel = new RoleModel();

	@Before
	public void setUp() throws Exception {
		// Clean up any existing test data before each test
		// This is a basic cleanup, more robust cleanup might be needed based on your
		// data
		UserDTO cleanupDto = new UserDTO();
		cleanupDto.setLogin("test@example.com");
		List<UserDTO> list = model.search(cleanupDto, 0, 0, null, null);
		for (UserDTO dto : list) {
			model.delete(dto);
		}

		UserDTO cleanupUpdatedDto = new UserDTO();
		cleanupUpdatedDto.setLogin("updatedtest@example.com");
		list = model.search(cleanupUpdatedDto, 0, 0, null, null);
		for (UserDTO dto : list) {
			model.delete(dto);
		}

		// Ensure a default role exists for testing
		RoleDTO adminRole = roleModel.findByName("Admin");
		if (adminRole == null) {
			adminRole = new RoleDTO();
			adminRole.setName("Admin");
			adminRole.setDescription("Administrator Role");
			adminRole.setCreatedBy("test");
			adminRole.setModifiedBy("test");
			adminRole.setCreatedDatetime(new Timestamp(new Date().getTime()));
			adminRole.setModifiedDatetime(new Timestamp(new Date().getTime()));
			roleModel.add(adminRole);
		}
	}

	// Helper method to create a new UserDTO for testing
	private UserDTO createTestUser(String login, String password) {
		UserDTO dto = new UserDTO();
		dto.setFirstName("Test");
		dto.setLastName("User");
		dto.setLogin(login);
		dto.setPassword(password);
		dto.setDob(new Date());
		dto.setMobileNo("1234567890");
		RoleDTO role = null;
		try {
			role = roleModel.findByName("Admin");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		dto.setRoleId(role.getId());
		dto.setRoleName(role.getName());
		dto.setCreatedBy("test");
		dto.setModifiedBy("test");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		return dto;
	}

	@Test
	public void testAdd() {
		try {
			UserDTO dto = createTestUser("test@example.com", "password");
			long pk = model.add(dto);
			UserDTO addedDto = model.findByPK(pk);
			assertNotNull(addedDto);
			assertEquals(dto.getLogin(), addedDto.getLogin());
			assertEquals(dto.getPassword(), addedDto.getPassword()); // Note: Password should ideally be hashed in a real application

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during add test: " + e.getMessage());
		}
	}

	@Test
	public void testAddDuplicateLogin() {
		try {
			UserDTO dto1 = createTestUser("duplicate@example.com", "pass1");
			model.add(dto1);

			UserDTO dto2 = createTestUser("duplicate@example.com", "pass2");
			model.add(dto2); // This should throw DuplicateRecordException

			fail("DuplicateRecordException was not thrown for duplicate login ID.");

		} catch (DuplicateRecordException e) {
			assertNotNull(e); // Expecting DuplicateRecordException
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail("ApplicationException during duplicate add test: " + e.getMessage());
		}
	}

	@Test
	public void testDelete() {
		try {
			UserDTO dto = createTestUser("delete@example.com", "password");
			long pk = model.add(dto);

			UserDTO addedDto = model.findByPK(pk);
			assertNotNull(addedDto);

			model.delete(addedDto);
			UserDTO deletedDto = model.findByPK(pk);
			assertNull(deletedDto);

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during delete test: " + e.getMessage());
		} catch (RecordNotFoundException e) {
			assertNotNull(e); // Expecting RecordNotFoundException after deletion
		}
	}

	@Test
	public void testUpdate() {
		try {
			UserDTO dto = createTestUser("update@example.com", "password");
			long pk = model.add(dto);

			UserDTO addedDto = model.findByPK(pk);
			assertNotNull(addedDto);

			addedDto.setFirstName("Updated");
			addedDto.setLastName("User");
			addedDto.setMobileNo("9876543210");
			model.update(addedDto);

			UserDTO updatedDto = model.findByPK(pk);
			assertNotNull(updatedDto);
			assertEquals("Updated", updatedDto.getFirstName());
			assertEquals("User", updatedDto.getLastName());
			assertEquals("9876543210", updatedDto.getMobileNo());

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during update test: " + e.getMessage());
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			fail("RecordNotFoundException during update test: " + e.getMessage());
		}
	}

	@Test
	public void testUpdateDuplicateLogin() {
		try {
			UserDTO dto1 = createTestUser("update1@example.com", "pass1");
			UserDTO dto2 = createTestUser("update2@example.com", "pass2");

			long pk1 = model.add(dto1);
			model.add(dto2);

			UserDTO updateDto = model.findByPK(pk1);
			updateDto.setLogin("update2@example.com"); // Try to update to a login that already exists

			model.update(updateDto);

			fail("DuplicateRecordException was not thrown for duplicate login ID during update.");

		} catch (DuplicateRecordException e) {
			assertNotNull(e); // Expecting DuplicateRecordException
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail("ApplicationException during update duplicate login test: " + e.getMessage());
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			fail("RecordNotFoundException during update duplicate login test: " + e.getMessage());
		}
	}


	@Test
	public void testFindByLoginId() {
		try {
			UserDTO dto = createTestUser("findbylogin@example.com", "password");
			model.add(dto);

			UserDTO foundDto = model.findByLogin("findbylogin@example.com");
			assertNotNull(foundDto);
			assertEquals("findbylogin@example.com", foundDto.getLogin());

			UserDTO notFoundDto = model.findByLogin("nonexistent@example.com");
			assertNull(notFoundDto);

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during findByLoginId test: " + e.getMessage());
		}
	}

	@Test
	public void testFindByPK() {
		try {
			UserDTO dto = createTestUser("findbypk@example.com", "password");
			long pk = model.add(dto);

			UserDTO foundDto = model.findByPK(pk);
			assertNotNull(foundDto);
			assertEquals(pk, foundDto.getId());

			UserDTO notFoundDto = model.findByPK(-1L); // Non-existent PK
			assertNull(notFoundDto);

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during findByPK test: " + e.getMessage());
		}
	}

	@Test
	public void testSearch() {
		try {
			UserDTO dto1 = createTestUser("search1@example.com", "pass1");
			UserDTO dto2 = createTestUser("search2@example.com", "pass2");
			model.add(dto1);
			model.add(dto2);

			UserDTO searchDto = new UserDTO();
			searchDto.setFirstName("Test");
			List<UserDTO> list = model.search(searchDto, 0, 0, null, null);
			assertTrue(list.size() >= 2); // At least the two added users should be found

			boolean found1 = false;
			boolean found2 = false;
			for (UserDTO dto : list) {
				if (dto.getLogin().equals("search1@example.com")) {
					found1 = true;
				}
				if (dto.getLogin().equals("search2@example.com")) {
					found2 = true;
				}
			}
			assertTrue(found1);
			assertTrue(found2);

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during search test: " + e.getMessage());
		}
	}

	@Test
	public void testList() {
		try {
			UserDTO dto1 = createTestUser("list1@example.com", "pass1");
			UserDTO dto2 = createTestUser("list2@example.com", "pass2");
			model.add(dto1);
			model.add(dto2);

			List<UserDTO> list = model.list();
			assertTrue(list.size() >= 2); // Should have at least the added users

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during list test: " + e.getMessage());
		}
	}

	@Test
	public void testAuthenticate() {
		try {
			UserDTO dto = createTestUser("authenticate@example.com", "testpassword");
			model.add(dto);

			UserDTO authenticatedUser = model.authenticate("authenticate@example.com", "testpassword");
			assertNotNull(authenticatedUser);
			assertEquals("authenticate@example.com", authenticatedUser.getLogin());

			UserDTO incorrectPasswordUser = model.authenticate("authenticate@example.com", "wrongpassword");
			assertNull(incorrectPasswordUser);

			UserDTO nonExistentUser = model.authenticate("nonexistent@example.com", "password");
			assertNull(nonExistentUser);

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during authenticate test: " + e.getMessage());
		}
	}

	@Test
	public void testChangePassword() {
		try {
			UserDTO dto = createTestUser("changepassword@example.com", "oldpassword");
			long pk = model.add(dto);

			boolean changed = model.changePassword(pk, "oldpassword", "newpassword");
			assertTrue(changed);

			UserDTO updatedUser = model.findByPK(pk);
			assertNotNull(updatedUser);
			assertEquals("newpassword", updatedUser.getPassword()); // Note: Password should ideally be hashed

			boolean incorrectChange = model.changePassword(pk, "wrongoldpassword", "anothernewpassword");
			assertFalse(incorrectChange);

			UserDTO nonExistentUser = createTestUser("nonexistentchangepassword@example.com", "password");
			boolean changeNonExistent = model.changePassword(nonExistentUser.getId(), "password", "new");
			assertFalse(changeNonExistent);


		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during changePassword test: " + e.getMessage());
		}
	}

	@Test
	public void testResetPassword() {
		try {
			UserDTO dto = createTestUser("resetpassword@example.com", "oldpassword");
			long pk = model.add(dto);

			boolean reset = model.resetPassword("resetpassword@example.com", "newgeneratedpassword");
			assertTrue(reset);

			UserDTO updatedUser = model.findByLogin("resetpassword@example.com");
			assertNotNull(updatedUser);
			assertEquals("newgeneratedpassword", updatedUser.getPassword()); // Note: Password should ideally be hashed

			boolean resetNonExistent = model.resetPassword("nonexistentresetpassword@example.com", "new");
			assertFalse(resetNonExistent);

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
			fail("Exception during resetPassword test: " + e.getMessage());
		}
	}
}