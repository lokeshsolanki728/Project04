import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CollegeModel;
import com.rays.pro4.Model.StudentModel;

public class StudentModelTest {

	private StudentModel model;
	private CollegeModel collegeModel;

	@Before
	public void setUp() throws Exception {
		model = new StudentModel();
		collegeModel = new CollegeModel();
		// Add a college for testing
		CollegeDTO collegeDto = new CollegeDTO();
		collegeDto.setName("Test College");
		collegeDto.setAddress("Test Address");
		collegeDto.setState("Test State");
		collegeDto.setCity("Test City");
		collegeDto.setPhoneNo("1234567890");
		collegeDto.setCreatedBy("test");
		collegeDto.setModifiedBy("test");
		collegeDto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		collegeDto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		collegeModel.add(collegeDto);
	}

	@After
	public void tearDown() throws Exception {
		// Clean up test data if necessary
		// For a robust test suite, you might want to delete the added college and students here.
	}

	@Test
	public void testAdd() {
		try {
			StudentDTO dto = new StudentDTO();
			dto.setFirstName("Test");
			dto.setLastName("Student");
			dto.setDob(new Date());
			dto.setMobileNo("9876543210");
			dto.setEmail("test.student@example.com");
			dto.setCollegeId(1); // Assuming the test college gets ID 1
			dto.setCreatedBy("test");
			dto.setModifiedBy("test");
			dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(dto);
			assertTrue(pk > 0);

			StudentDTO addedStudent = model.findByPK(pk);
			assertNotNull(addedStudent);
			assertEquals("Test", addedStudent.getFirstName());

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during add test: " + e.getMessage());
		}
	}

	@Test
	public void testAddDuplicateEmail() {
		try {
			StudentDTO dto1 = new StudentDTO();
			dto1.setFirstName("Test1");
			dto1.setLastName("Student1");
			dto1.setDob(new Date());
			dto1.setMobileNo("1111111111");
			dto1.setEmail("duplicate.email@example.com");
			dto1.setCollegeId(1);
			dto1.setCreatedBy("test");
			dto1.setModifiedBy("test");
			dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
			model.add(dto1);

			StudentDTO dto2 = new StudentDTO();
			dto2.setFirstName("Test2");
			dto2.setLastName("Student2");
			dto2.setDob(new Date());
			dto2.setMobileNo("2222222222");
			dto2.setEmail("duplicate.email@example.com"); // Duplicate email
			dto2.setCollegeId(1);
			dto2.setCreatedBy("test");
			dto2.setModifiedBy("test");
			dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));

			model.add(dto2);
			fail("Expected DuplicateRecordException was not thrown");

		} catch (DuplicateRecordException e) {
			assertNotNull(e);
		} catch (ApplicationException e) {
			fail("ApplicationException during duplicate email test: " + e.getMessage());
		}
	}

	@Test
	public void testDelete() {
		try {
			StudentDTO dto = new StudentDTO();
			dto.setFirstName("Delete");
			dto.setLastName("Student");
			dto.setDob(new Date());
			dto.setMobileNo("9999999999");
			dto.setEmail("delete.student@example.com");
			dto.setCollegeId(1);
			dto.setCreatedBy("test");
			dto.setModifiedBy("test");
			dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(dto);
			assertTrue(pk > 0);

			StudentDTO addedStudent = model.findByPK(pk);
			assertNotNull(addedStudent);

			model.delete(addedStudent);

			StudentDTO deletedStudent = model.findByPK(pk);
			assertNull(deletedStudent);

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during delete test: " + e.getMessage());
		}
	}

	@Test
	public void testUpdate() {
		try {
			StudentDTO dto = new StudentDTO();
			dto.setFirstName("Original");
			dto.setLastName("Name");
			dto.setDob(new Date());
			dto.setMobileNo("1231231230");
			dto.setEmail("original.name@example.com");
			dto.setCollegeId(1);
			dto.setCreatedBy("test");
			dto.setModifiedBy("test");
			dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(dto);
			assertTrue(pk > 0);

			StudentDTO studentToUpdate = model.findByPK(pk);
			assertNotNull(studentToUpdate);

			studentToUpdate.setFirstName("Updated");
			studentToUpdate.setLastName("Name");
			studentToUpdate.setMobileNo("0987654321");
			studentToUpdate.setModifiedBy("updater");
			studentToUpdate.setModifiedDatetime(new Timestamp(new Date().getTime()));

			model.update(studentToUpdate);

			StudentDTO updatedStudent = model.findByPK(pk);
			assertNotNull(updatedStudent);
			assertEquals("Updated", updatedStudent.getFirstName());
			assertEquals("Name", updatedStudent.getLastName());
			assertEquals("0987654321", updatedStudent.getMobileNo());

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during update test: " + e.getMessage());
		}
	}

	@Test
	public void testFindByEmailId() {
		try {
			StudentDTO dto = new StudentDTO();
			dto.setFirstName("Find");
			dto.setLastName("Email");
			dto.setDob(new Date());
			dto.setMobileNo("5555555555");
			dto.setEmail("find.by.email@example.com");
			dto.setCollegeId(1);
			dto.setCreatedBy("test");
			dto.setModifiedBy("test");
			dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

			model.add(dto);

			StudentDTO foundStudent = model.findByEmailId("find.by.email@example.com");
			assertNotNull(foundStudent);
			assertEquals("Find", foundStudent.getFirstName());

			StudentDTO notFoundStudent = model.findByEmailId("nonexistent.email@example.com");
			assertNull(notFoundStudent);

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during findByEmailId test: " + e.getMessage());
		}
	}

	@Test
	public void testFindByPK() {
		try {
			StudentDTO dto = new StudentDTO();
			dto.setFirstName("Find");
			dto.setLastName("PK");
			dto.setDob(new Date());
			dto.setMobileNo("7777777777");
			dto.setEmail("find.by.pk@example.com");
			dto.setCollegeId(1);
			dto.setCreatedBy("test");
			dto.setModifiedBy("test");
			dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(dto);
			assertTrue(pk > 0);

			StudentDTO foundStudent = model.findByPK(pk);
			assertNotNull(foundStudent);
			assertEquals(pk, foundStudent.getId());

			StudentDTO notFoundStudent = model.findByPK(-1); // Non-existent PK
			assertNull(notFoundStudent);

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during findByPK test: " + e.getMessage());
		}
	}

	@Test
	public void testSearch() {
		try {
			// Add some test students
			StudentDTO dto1 = new StudentDTO();
			dto1.setFirstName("Search");
			dto1.setLastName("Student1");
			dto1.setDob(new Date());
			dto1.setMobileNo("1000000001");
			dto1.setEmail("search.student1@example.com");
			dto1.setCollegeId(1);
			dto1.setCreatedBy("test");
			dto1.setModifiedBy("test");
			dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
			model.add(dto1);

			StudentDTO dto2 = new StudentDTO();
			dto2.setFirstName("Another");
			dto2.setLastName("SearchStudent");
			dto2.setDob(new Date());
			dto2.setMobileNo("1000000002");
			dto2.setEmail("search.student2@example.com");
			dto2.setCollegeId(1);
			dto2.setCreatedBy("test");
			dto2.setModifiedBy("test");
			dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
			model.add(dto2);

			// Search by first name
			StudentDTO searchDto1 = new StudentDTO();
			searchDto1.setFirstName("Search");
			List<StudentDTO> list1 = model.search(searchDto1, "FIRST_NAME", "ASC");
			assertTrue(list1.size() > 0);
			assertEquals("Search", list1.get(0).getFirstName());

			// Search by last name
			StudentDTO searchDto2 = new StudentDTO();
			searchDto2.setLastName("SearchStudent");
			List<StudentDTO> list2 = model.search(searchDto2, "LAST_NAME", "ASC");
			assertTrue(list2.size() > 0);
			assertEquals("SearchStudent", list2.get(0).getLastName());

			// Search with pagination
			List<StudentDTO> list3 = model.search(null, 1, 10, "FIRST_NAME", "ASC");
			assertTrue(list3.size() > 0);

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during search test: " + e.getMessage());
		}
	}

	@Test
	public void testList() {
		try {
			// Add some test students
			StudentDTO dto1 = new StudentDTO();
			dto1.setFirstName("List");
			dto1.setLastName("Student1");
			dto1.setDob(new Date());
			dto1.setMobileNo("2000000001");
			dto1.setEmail("list.student1@example.com");
			dto1.setCollegeId(1);
			dto1.setCreatedBy("test");
			dto1.setModifiedBy("test");
			dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
			model.add(dto1);

			StudentDTO dto2 = new StudentDTO();
			dto2.setFirstName("List");
			dto2.setLastName("Student2");
			dto2.setDob(new Date());
			dto2.setMobileNo("2000000002");
			dto2.setEmail("list.student2@example.com");
			dto2.setCollegeId(1);
			dto2.setCreatedBy("test");
			dto2.setModifiedBy("test");
			dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
			dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
			model.add(dto2);

			List<StudentDTO> allStudents = model.list("FIRST_NAME", "ASC");
			assertTrue(allStudents.size() > 0);

			List<StudentDTO> paginatedStudents = model.list(1, 10, "FIRST_NAME", "ASC");
			assertTrue(paginatedStudents.size() > 0);

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during list test: " + e.getMessage());
		}
	}
}