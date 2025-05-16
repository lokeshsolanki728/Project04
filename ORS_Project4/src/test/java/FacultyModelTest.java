import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.FacultyDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.FacultyModel;

public class FacultyModelTest {

    FacultyModel model = new FacultyModel();

    @Before
    public void setUp() throws Exception {
        // Clean up any existing data or set up test environment
        // This is optional, but good practice for isolated tests
    }

    @Test
    public void testAdd() throws ApplicationException, DuplicateRecordException {
        FacultyDTO dto = new FacultyDTO();
        dto.setFirstName("Test");
        dto.setLastName("Faculty");
        dto.setGender("Male");
        dto.setEmailId("testfaculty@example.com");
        dto.setMobileNo("1234567890");
        dto.setDob(new Date());
        dto.setCollegeId(1L); // Assuming a college with ID 1 exists
        dto.setCollegeName("Test College");
        dto.setCourseId(1L); // Assuming a course with ID 1 exists
        dto.setCourseName("Test Course");
        dto.setSubjectId(1L); // Assuming a subject with ID 1 exists
        dto.setSubjectName("Test Subject");
        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        assertTrue(pk > 0);

        FacultyDTO addedDto = model.findByPK(pk);
        assertNotNull(addedDto);
        assertEquals("Test", addedDto.getFirstName());

        // Test DuplicateRecordException
        FacultyDTO duplicateDto = new FacultyDTO();
        duplicateDto.setEmailId("testfaculty@example.com");
        duplicateDto.setFirstName("Another");
        duplicateDto.setLastName("Faculty");
        duplicateDto.setGender("Female");
        duplicateDto.setMobileNo("0987654321");
        duplicateDto.setDob(new Date());
        duplicateDto.setCollegeId(1L);
        duplicateDto.setCollegeName("Test College");
        duplicateDto.setCourseId(1L);
        duplicateDto.setCourseName("Test Course");
        duplicateDto.setSubjectId(1L);
        duplicateDto.setSubjectName("Test Subject");
        duplicateDto.setCreatedBy("admin");
        duplicateDto.setModifiedBy("admin");
        duplicateDto.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        duplicateDto.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));

        try {
            model.add(duplicateDto);
            fail("DuplicateRecordException was not thrown");
        } catch (DuplicateRecordException e) {
            // Expected exception
        }
    }

    @Test
    public void testDelete() throws ApplicationException, DuplicateRecordException {
        // Add a faculty to delete
        FacultyDTO dto = new FacultyDTO();
        dto.setFirstName("Delete");
        dto.setLastName("Faculty");
        dto.setGender("Male");
        dto.setEmailId("deletefaculty@example.com");
        dto.setMobileNo("1111111111");
        dto.setDob(new Date());
        dto.setCollegeId(1L);
        dto.setCollegeName("Test College");
        dto.setCourseId(1L);
        dto.setCourseName("Test Course");
        dto.setSubjectId(1L);
        dto.setSubjectName("Test Subject");
        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        dto.setId(pk);

        model.delete(dto);

        FacultyDTO deletedDto = model.findByPK(pk);
        assertNull(deletedDto);
    }

    @Test
    public void testUpdate() throws ApplicationException, DuplicateRecordException {
        // Add a faculty to update
        FacultyDTO dto = new FacultyDTO();
        dto.setFirstName("Update");
        dto.setLastName("Faculty");
        dto.setGender("Female");
        dto.setEmailId("updatefaculty@example.com");
        dto.setMobileNo("2222222222");
        dto.setDob(new Date());
        dto.setCollegeId(1L);
        dto.setCollegeName("Test College");
        dto.setCourseId(1L);
        dto.setCourseName("Test Course");
        dto.setSubjectId(1L);
        dto.setSubjectName("Test Subject");
        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        dto.setId(pk);

        dto.setFirstName("Updated");
        dto.setLastName("Faculty");
        dto.setGender("Male");
        dto.setMobileNo("3333333333");

        model.update(dto);

        FacultyDTO updatedDto = model.findByPK(pk);
        assertNotNull(updatedDto);
        assertEquals("Updated", updatedDto.getFirstName());
        assertEquals("Faculty", updatedDto.getLastName());
        assertEquals("Male", updatedDto.getGender());
        assertEquals("3333333333", updatedDto.getMobileNo());

        // Test DuplicateRecordException during update
        FacultyDTO anotherDto = new FacultyDTO();
        anotherDto.setFirstName("Another");
        anotherDto.setLastName("One");
        anotherDto.setGender("Male");
        anotherDto.setEmailId("anotherone@example.com");
        anotherDto.setMobileNo("4444444444");
        anotherDto.setDob(new Date());
        anotherDto.setCollegeId(1L);
        anotherDto.setCollegeName("Test College");
        anotherDto.setCourseId(1L);
        anotherDto.setCourseName("Test Course");
        anotherDto.setSubjectId(1L);
        anotherDto.setSubjectName("Test Subject");
        anotherDto.setCreatedBy("admin");
        anotherDto.setModifiedBy("admin");
        anotherDto.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        anotherDto.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));
        long pk2 = model.add(anotherDto);

        dto.setEmailId("anotherone@example.com"); // Try to update to a duplicate email

        try {
            model.update(dto);
            fail("DuplicateRecordException was not thrown during update");
        } catch (DuplicateRecordException e) {
            // Expected exception
        } finally {
            // Clean up the added faculty
            anotherDto.setId(pk2);
            model.delete(anotherDto);
        }
    }

    @Test
    public void testFindByEmailId() throws ApplicationException, DuplicateRecordException {
        FacultyDTO dto = new FacultyDTO();
        dto.setFirstName("Find");
        dto.setLastName("ByEmail");
        dto.setGender("Male");
        dto.setEmailId("findbyemail@example.com");
        dto.setMobileNo("5555555555");
        dto.setDob(new Date());
        dto.setCollegeId(1L);
        dto.setCollegeName("Test College");
        dto.setCourseId(1L);
        dto.setCourseName("Test Course");
        dto.setSubjectId(1L);
        dto.setSubjectName("Test Subject");
        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));

        model.add(dto);

        FacultyDTO foundDto = model.findByEmailId("findbyemail@example.com");
        assertNotNull(foundDto);
        assertEquals("Find", foundDto.getFirstName());

        FacultyDTO notFoundDto = model.findByEmailId("nonexistent@example.com");
        assertNull(notFoundDto);
    }

    @Test
    public void testFindByPK() throws ApplicationException, DuplicateRecordException {
        FacultyDTO dto = new FacultyDTO();
        dto.setFirstName("Find");
        dto.setLastName("ByPK");
        dto.setGender("Female");
        dto.setEmailId("findbypk@example.com");
        dto.setMobileNo("6666666666");
        dto.setDob(new Date());
        dto.setCollegeId(1L);
        dto.setCollegeName("Test College");
        dto.setCourseId(1L);
        dto.setCourseName("Test Course");
        dto.setSubjectId(1L);
        dto.setSubjectName("Test Subject");
        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));

        long pk = model.add(dto);

        FacultyDTO foundDto = model.findByPK(pk);
        assertNotNull(foundDto);
        assertEquals("Find", foundDto.getFirstName());

        FacultyDTO notFoundDto = model.findByPK(-1L); // Assuming -1 is an invalid PK
        assertNull(notFoundDto);
    }

    @Test
    public void testSearch() throws ApplicationException, DuplicateRecordException {
        // Add some data for searching
        FacultyDTO dto1 = new FacultyDTO();
        dto1.setFirstName("Search1");
        dto1.setLastName("Faculty");
        dto1.setGender("Male");
        dto1.setEmailId("search1@example.com");
        dto1.setMobileNo("7777777777");
        dto1.setDob(new Date());
        dto1.setCollegeId(1L);
        dto1.setCollegeName("Test College");
        dto1.setCourseId(1L);
        dto1.setCourseName("Test Course");
        dto1.setSubjectId(1L);
        dto1.setSubjectName("Test Subject");
        dto1.setCreatedBy("admin");
        dto1.setModifiedBy("admin");
        dto1.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));
        model.add(dto1);

        FacultyDTO dto2 = new FacultyDTO();
        dto2.setFirstName("Search2");
        dto2.setLastName("Other");
        dto2.setGender("Female");
        dto2.setEmailId("search2@example.com");
        dto2.setMobileNo("8888888888");
        dto2.setDob(new Date());
        dto2.setCollegeId(2L); // Assuming a college with ID 2 exists
        dto2.setCollegeName("Another College");
        dto2.setCourseId(2L); // Assuming a course with ID 2 exists
        dto2.setCourseName("Another Course");
        dto2.setSubjectId(2L); // Assuming a subject with ID 2 exists
        dto2.setSubjectName("Another Subject");
        dto2.setCreatedBy("admin");
        dto2.setModifiedBy("admin");
        dto2.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));
        model.add(dto2);

        // Search by first name
        FacultyDTO searchDto = new FacultyDTO();
        searchDto.setFirstName("Search1");
        List<FacultyDTO> searchList = model.search(searchDto);
        assertNotNull(searchList);
        assertEquals(1, searchList.size());
        assertEquals("Search1", searchList.get(0).getFirstName());

        // Search by college ID
        searchDto = new FacultyDTO();
        searchDto.setCollegeId(2L);
        searchList = model.search(searchDto);
        assertNotNull(searchList);
        assertEquals(1, searchList.size());
        assertEquals("Search2", searchList.get(0).getFirstName());

        // Search with pagination
        searchDto = new FacultyDTO();
        List<FacultyDTO> paginatedList = model.search(searchDto, 1, 1);
        assertNotNull(paginatedList);
        assertEquals(1, paginatedList.size());
    }

    @Test
    public void testList() throws ApplicationException, DuplicateRecordException {
        // Add some data for listing
        FacultyDTO dto1 = new FacultyDTO();
        dto1.setFirstName("List1");
        dto1.setLastName("Faculty");
        dto1.setGender("Male");
        dto1.setEmailId("list1@example.com");
        dto1.setMobileNo("9999999999");
        dto1.setDob(new Date());
        dto1.setCollegeId(1L);
        dto1.setCollegeName("Test College");
        dto1.setCourseId(1L);
        dto1.setCourseName("Test Course");
        dto1.setSubjectId(1L);
        dto1.setSubjectName("Test Subject");
        dto1.setCreatedBy("admin");
        dto1.setModifiedBy("admin");
        dto1.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));
        model.add(dto1);

        FacultyDTO dto2 = new FacultyDTO();
        dto2.setFirstName("List2");
        dto2.setLastName("Other");
        dto2.setGender("Female");
        dto2.setEmailId("list2@example.com");
        dto2.setMobileNo("0000000000");
        dto2.setDob(new Date());
        dto2.setCollegeId(2L);
        dto2.setCollegeName("Another College");
        dto2.setCourseId(2L);
        dto2.setCourseName("Another Course");
        dto2.setSubjectId(2L);
        dto2.setSubjectName("Another Subject");
        dto2.setCreatedBy("admin");
        dto2.setModifiedBy("admin");
        dto2.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new java.sql.Timestamp(new Date().getTime()));
        model.add(dto2);


        List<FacultyDTO> allList = model.list();
        assertNotNull(allList);
        assertTrue(allList.size() >= 2); // Assuming at least 2 entries after adding

        List<FacultyDTO> paginatedList = model.list(1, 1);
        assertNotNull(paginatedList);
        assertEquals(1, paginatedList.size());
    }
}