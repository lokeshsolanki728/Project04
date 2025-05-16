package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.RoleDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.RoleModel;

public class RoleModelTest {

    private RoleModel model = new RoleModel();

    // Helper method to create a new RoleDTO for testing
    private RoleDTO createTestRole(String name) {
        RoleDTO dto = new RoleDTO();
        dto.setName(name);
        dto.setDescription("Test Description for " + name);
        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
        return dto;
    }

    @Before
    public void setUp() throws Exception {
        // Clean up any existing test data before each test
        // Note: Searching by name might not be precise enough for cleanup if
        // multiple roles share parts of the name. Consider adding a unique identifier
        // or more specific search criteria for robust cleanup in real applications.
        // For this example, we'll assume test data names are unique enough.
        RoleDTO cleanupDto1 = new RoleDTO();
        cleanupDto1.setName("Test Role");
        List<RoleDTO> list1 = model.search(cleanupDto1, 0, 0);
        for (RoleDTO dto : list1) {
            model.delete(dto);
        }

        RoleDTO cleanupDto2 = new RoleDTO();
        cleanupDto2.setName("Updated Test Role");
        List<RoleDTO> list2 = model.search(cleanupDto2, 0, 0);
        for (RoleDTO dto : list2) {
            model.delete(dto);
        }
        
        RoleDTO cleanupDto3 = new RoleDTO();
        cleanupDto3.setName("Duplicate Role");
        List<RoleDTO> list3 = model.search(cleanupDto3, 0, 0);
        for (RoleDTO dto : list3) {
            model.delete(dto);
        }
         RoleDTO cleanupDto4 = new RoleDTO();
        cleanupDto4.setName("Role to Delete");
        List<RoleDTO> list4 = model.search(cleanupDto4, 0, 0);
        for (RoleDTO dto : list4) {
            model.delete(dto);
        }
        RoleDTO cleanupDto5 = new RoleDTO();
        cleanupDto5.setName("Role to Update");
        List<RoleDTO> list5 = model.search(cleanupDto5, 0, 0);
        for (RoleDTO dto : list5) {
            model.delete(dto);
        }
         RoleDTO cleanupDto6 = new RoleDTO();
        cleanupDto6.setName("Find By Name Role");
        List<RoleDTO> list6 = model.search(cleanupDto6, 0, 0);
        for (RoleDTO dto : list6) {
            model.delete(dto);
        }
         RoleDTO cleanupDto7 = new RoleDTO();
        cleanupDto7.setName("Find By PK Role");
        List<RoleDTO> list7 = model.search(cleanupDto7, 0, 0);
        for (RoleDTO dto : list7) {
            model.delete(dto);
        }
         RoleDTO cleanupDto8 = new RoleDTO();
        cleanupDto8.setName("Search Role 1");
        List<RoleDTO> list8 = model.search(cleanupDto8, 0, 0);
        for (RoleDTO dto : list8) {
            model.delete(dto);
        }
          RoleDTO cleanupDto9 = new RoleDTO();
        cleanupDto9.setName("Another Search Role");
        List<RoleDTO> list9 = model.search(cleanupDto9, 0, 0);
        for (RoleDTO dto : list9) {
            model.delete(dto);
        }
          RoleDTO cleanupDto10 = new RoleDTO();
        cleanupDto10.setName("List Role 1");
        List<RoleDTO> list10 = model.search(cleanupDto10, 0, 0);
        for (RoleDTO dto : list10) {
            model.delete(dto);
        }
           RoleDTO cleanupDto11 = new RoleDTO();
        cleanupDto11.setName("List Role 2");
        List<RoleDTO> list11 = model.search(cleanupDto11, 0, 0);
        for (RoleDTO dto : list11) {
            model.delete(dto);
        }
    }

    @After
    public void tearDown() throws Exception {
        // Clean up test data after each test
         RoleDTO cleanupDto1 = new RoleDTO();
        cleanupDto1.setName("Test Role");
        List<RoleDTO> list1 = model.search(cleanupDto1, 0, 0);
        for (RoleDTO dto : list1) {
            model.delete(dto);
        }

        RoleDTO cleanupDto2 = new RoleDTO();
        cleanupDto2.setName("Updated Test Role");
        List<RoleDTO> list2 = model.search(cleanupDto2, 0, 0);
        for (RoleDTO dto : list2) {
            model.delete(dto);
        }
        
        RoleDTO cleanupDto3 = new RoleDTO();
        cleanupDto3.setName("Duplicate Role");
        List<RoleDTO> list3 = model.search(cleanupDto3, 0, 0);
        for (RoleDTO dto : list3) {
            model.delete(dto);
        }
         RoleDTO cleanupDto4 = new RoleDTO();
        cleanupDto4.setName("Role to Delete");
        List<RoleDTO> list4 = model.search(cleanupDto4, 0, 0);
        for (RoleDTO dto : list4) {
            model.delete(dto);
        }
        RoleDTO cleanupDto5 = new RoleDTO();
        cleanupDto5.setName("Role to Update");
        List<RoleDTO> list5 = model.search(cleanupDto5, 0, 0);
        for (RoleDTO dto : list5) {
            model.delete(dto);
        }
         RoleDTO cleanupDto6 = new RoleDTO();
        cleanupDto6.setName("Find By Name Role");
        List<RoleDTO> list6 = model.search(cleanupDto6, 0, 0);
        for (RoleDTO dto : list6) {
            model.delete(dto);
        }
         RoleDTO cleanupDto7 = new RoleDTO();
        cleanupDto7.setName("Find By PK Role");
        List<RoleDTO> list7 = model.search(cleanupDto7, 0, 0);
        for (RoleDTO dto : list7) {
            model.delete(dto);
        }
         RoleDTO cleanupDto8 = new RoleDTO();
        cleanupDto8.setName("Search Role 1");
        List<RoleDTO> list8 = model.search(cleanupDto8, 0, 0);
        for (RoleDTO dto : list8) {
            model.delete(dto);
        }
          RoleDTO cleanupDto9 = new RoleDTO();
        cleanupDto9.setName("Another Search Role");
        List<RoleDTO> list9 = model.search(cleanupDto9, 0, 0);
        for (RoleDTO dto : list9) {
            model.delete(dto);
        }
          RoleDTO cleanupDto10 = new RoleDTO();
        cleanupDto10.setName("List Role 1");
        List<RoleDTO> list10 = model.search(cleanupDto10, 0, 0);
        for (RoleDTO dto : list10) {
            model.delete(dto);
        }
           RoleDTO cleanupDto11 = new RoleDTO();
        cleanupDto11.setName("List Role 2");
        List<RoleDTO> list11 = model.search(cleanupDto11, 0, 0);
        for (RoleDTO dto : list11) {
            model.delete(dto);
        }
    }


    @Test
    public void testAdd() {
        try {
            RoleDTO dto = createTestRole("Test Role");
            long pk = model.add(dto);
            RoleDTO addedDto = model.findByPK(pk);
            assertNotNull(addedDto);
            assertEquals(dto.getName(), addedDto.getName());
            assertEquals(dto.getDescription(), addedDto.getDescription());

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during add test: " + e.getMessage());
        }
    }

    @Test
    public void testAddDuplicate() {
        try {
            RoleDTO dto1 = createTestRole("Duplicate Role");
            model.add(dto1);

            RoleDTO dto2 = createTestRole("Duplicate Role");
            model.add(dto2); // This should throw DuplicateRecordException

            fail("DuplicateRecordException was not thrown for duplicate role name.");

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
            RoleDTO dto = createTestRole("Role to Delete");
            long pk = model.add(dto);

            RoleDTO addedDto = model.findByPK(pk);
            assertNotNull(addedDto);

            model.delete(addedDto);
            RoleDTO deletedDto = model.findByPK(pk);
            assertNull(deletedDto);

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during delete test: " + e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        try {
            RoleDTO dto = createTestRole("Role to Update");
            long pk = model.add(dto);

            RoleDTO addedDto = model.findByPK(pk);
            assertNotNull(addedDto);

            addedDto.setName("Updated Test Role");
            addedDto.setDescription("Updated Description");
            model.update(addedDto);

            RoleDTO updatedDto = model.findByPK(pk);
            assertNotNull(updatedDto);
            assertEquals("Updated Test Role", updatedDto.getName());
            assertEquals("Updated Description", updatedDto.getDescription());

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during update test: " + e.getMessage());
        }
    }
    
     @Test
    public void testUpdateDuplicateName() {
        try {
            RoleDTO dto1 = createTestRole("Update Duplicate Role 1");
            RoleDTO dto2 = createTestRole("Update Duplicate Role 2");
            
            long pk1 = model.add(dto1);
            model.add(dto2);
            
            RoleDTO updateDto = model.findByPK(pk1);
            updateDto.setName("Update Duplicate Role 2"); // Try to update to a name that already exists
            
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
            RoleDTO dto = createTestRole("Find By Name Role");
            model.add(dto);

            RoleDTO foundDto = model.findByName("Find By Name Role");
            assertNotNull(foundDto);
            assertEquals("Find By Name Role", foundDto.getName());

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during findByName test: " + e.getMessage());
        }
    }

    @Test
    public void testFindByPK() {
        try {
            RoleDTO dto = createTestRole("Find By PK Role");
            long pk = model.add(dto);

            RoleDTO foundDto = model.findByPK(pk);
            assertNotNull(foundDto);
            assertEquals(pk, foundDto.getId());

            RoleDTO notFoundDto = model.findByPK(-1L); // Non-existent PK
            assertNull(notFoundDto);

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during findByPK test: " + e.getMessage());
        }
    }

    @Test
    public void testSearch() {
        try {
            RoleDTO dto1 = createTestRole("Search Role 1");
            RoleDTO dto2 = createTestRole("Another Search Role");
            model.add(dto1);
            model.add(dto2);

            RoleDTO searchDto = new RoleDTO();
            searchDto.setName("Search");
            List<RoleDTO> list = model.search(searchDto, 0, 0);
            assertTrue(list.size() >= 1); // At least one role containing "Search" should be found

            boolean found = false;
            for (RoleDTO dto : list) {
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
            RoleDTO dto1 = createTestRole("List Role 1");
            RoleDTO dto2 = createTestRole("List Role 2");
            model.add(dto1);
            model.add(dto2);

            List<RoleDTO> list = model.list();
            assertTrue(list.size() >= 2); // Should have at least the added roles

        } catch (ApplicationException | DuplicateRecordException e) {
            e.printStackTrace();
            fail("Exception during list test: " + e.getMessage());
        }
    }
}