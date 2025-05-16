package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.CourseDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;

public class CourseModelTest {

    private CourseModel model = new CourseModel();

    // Helper method to create a new CourseDTO for testing
    private CourseDTO createTestCourse(String name) {
        CourseDTO dto = new CourseDTO();
        dto.setName(name);
        dto.setDescription("Test Description for " + name);
        dto.setDuration("1 Year");
        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
        return dto;
    }

    @Before
    public void setUp() throws Exception {
        // Clean up any existing test data before each test
        List<CourseDTO> list = model.search(createTestCourse("Test Course"), 0, 0, null, null);
        for (CourseDTO dto : list) {
            model.delete(dto);
        }
        list = model.search(createTestCourse("Updated Test Course"), 0, 0, null, null);
        for (CourseDTO dto : list) {
            model.delete(dto);
        }
    }
    
    @After
    public void tearDown() throws Exception {
        // Clean up test data after each test
        List<CourseDTO> list = model.search(createTestCourse("Test Course"), 0, 0, null, null);
        for (CourseDTO dto : list) {
            model.delete(dto);
        }
        list = model.search(createTestCourse("Updated Test Course"), 0, 0, null, null);
        for (CourseDTO dto : list) {
            model.delete(dto);
        }
    }


    @Test
    public void testAdd() {
        try {
            CourseDTO dto = createTestCourse("Test Course");
            long pk = model.add(dto);
            CourseDTO addedDto = model.findByPK(pk);
            assertNotNull(addedDto);
            assertEquals(dto.getName(), addedDto.getName());
            assertEquals(dto.getDescription(), addedDto.getDescription());
            assertEquals(dto.getDuration(), addedDto.getDuration());

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during add test: " + e.getMessage());
        }
    }

    @Test
    public void testAddDuplicate() {
        try {
            CourseDTO dto1 = createTestCourse("Duplicate Course");
            model.add(dto1);

            CourseDTO dto2 = createTestCourse("Duplicate Course");
            model.add(dto2); // This should throw DuplicateRecordException

            fail("DuplicateRecordException was not thrown for duplicate course name.");

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
            CourseDTO dto = createTestCourse("Course to Delete");
            long pk = model.add(dto);

            CourseDTO addedDto = model.findByPK(pk);
            assertNotNull(addedDto);

            model.delete(addedDto);
            CourseDTO deletedDto = model.findByPK(pk);
            assertNull(deletedDto);

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during delete test: " + e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        try {
            CourseDTO dto = createTestCourse("Course to Update");
            long pk = model.add(dto);

            CourseDTO addedDto = model.findByPK(pk);
            assertNotNull(addedDto);

            addedDto.setName("Updated Test Course");
            addedDto.setDescription("Updated Description");
            addedDto.setDuration("2 Years");
            model.update(addedDto);

            CourseDTO updatedDto = model.findByPK(pk);
            assertNotNull(updatedDto);
            assertEquals("Updated Test Course", updatedDto.getName());
            assertEquals("Updated Description", updatedDto.getDescription());
            assertEquals("2 Years", updatedDto.getDuration());

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during update test: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpdateDuplicateName() {
        try {
            CourseDTO dto1 = createTestCourse("Update Duplicate Course 1");
            CourseDTO dto2 = createTestCourse("Update Duplicate Course 2");
            
            long pk1 = model.add(dto1);
            model.add(dto2);
            
            CourseDTO updateDto = model.findByPK(pk1);
            updateDto.setName("Update Duplicate Course 2"); // Try to update to a name that already exists
            
            model.update(updateDto);
            
            fail("DuplicateRecordException was not thrown for duplicate name during update.");
            
        } catch (DuplicateRecordException e) {
            assertNotNull(e); // Expecting DuplicateRecordException
        } catch (ApplicationException e) {
            e.printStackTrace();
            fail("ApplicationException during update duplicate name test: " + e.getMessage());
        }
    }


    @Test
    public void testFindByName() {
        try {
            CourseDTO dto = createTestCourse("Find By Name Course");
            model.add(dto);

            CourseDTO foundDto = model.findByName("Find By Name Course");
            assertNotNull(foundDto);
            assertEquals("Find By Name Course", foundDto.getName());

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during findByName test: " + e.getMessage());
        }
    }

    @Test
    public void testFindByPK() {
        try {
            CourseDTO dto = createTestCourse("Find By PK Course");
            long pk = model.add(dto);

            CourseDTO foundDto = model.findByPK(pk);
            assertNotNull(foundDto);
            assertEquals(pk, foundDto.getId());

            CourseDTO notFoundDto = model.findByPK(-1L); // Non-existent PK
            assertNull(notFoundDto);

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during findByPK test: " + e.getMessage());
        }
    }

    @Test
    public void testSearch() {
        try {
            CourseDTO dto1 = createTestCourse("Search Course 1");
            CourseDTO dto2 = createTestCourse("Another Search Course");
            model.add(dto1);
            model.add(dto2);

            CourseDTO searchDto = new CourseDTO();
            searchDto.setName("Search");
            List<CourseDTO> list = model.search(searchDto, 0, 0, null, null);
            assertTrue(list.size() >= 1); // At least one course containing "Search" should be found

            boolean found = false;
            for (CourseDTO dto : list) {
                if (dto.getName().contains("Search")) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during search test: " + e.getMessage());
        }
    }

    @Test
    public void testList() {
        try {
            CourseDTO dto1 = createTestCourse("List Course 1");
            CourseDTO dto2 = createTestCourse("List Course 2");
            model.add(dto1);
            model.add(dto2);

            List<CourseDTO> list = model.list();
            assertTrue(list.size() >= 2); // Should have at least the added courses

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during list test: " + e.getMessage());
        }
    }
}