package com.rays.proj4.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rays.pro4.DTO.CollegeDTO;
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
import org.junit.Test;

public class CollegeModelTest {
    static CollegeModel model = new CollegeModel();

	public static CollegeDTO createCollegeDTO() {
		CollegeDTO dto = new CollegeDTO();
		dto.setName("TestCollege" + new Date().getTime());
		dto.setAddress("TestAddress");
		dto.setState("TestState");
		dto.setCity("TestCity");
		dto.setPhoneNo("1234567890");
		dto.setCreatedBy("TestAdmin");
		dto.setModifiedBy("TestAdmin");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		return dto;
    }

	@Test
	public void testAdd() throws DuplicateRecordException {
		try {
			CollegeDTO dto = createCollegeDTO();
			long pk = model.add(dto);
			CollegeDTO addedDTO = model.findByPK(pk);
			assertNotNull(addedDTO);
			assertEquals(dto.getName(), addedDTO.getName());
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testDelete() throws DuplicateRecordException, ApplicationException {
		CollegeDTO dto = createCollegeDTO();
		try {
			long pk = model.add(dto);
			dto.setId(pk);
			model.delete(dto);
			CollegeDTO deletedDTO = model.findByPK(pk);
			assertNull(deletedDTO);
		} catch (RecordNotFoundException e) {
			assertTrue(true);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testUpdate() throws DuplicateRecordException, ApplicationException {
		try {
			CollegeDTO dto = createCollegeDTO();
			long pk = model.add(dto);
			dto.setId(pk);
			dto.setName("UpdatedCollege");
			dto.setAddress("UpdatedAddress");
			model.update(dto);

			CollegeDTO updatedDTO = model.findByPK(pk);
			assertNotNull(updatedDTO);
			assertEquals("UpdatedCollege", updatedDTO.getName());
			assertEquals("UpdatedAddress", updatedDTO.getAddress());
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testFindByPK() throws DuplicateRecordException, ApplicationException {
		try {
			CollegeDTO dto = createCollegeDTO();
			long pk = model.add(dto);
			CollegeDTO foundDTO = model.findByPK(pk);
			assertNotNull(foundDTO);
			assertEquals(pk, foundDTO.getId());
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testFindByName() throws DuplicateRecordException, ApplicationException {
		try {
			CollegeDTO dto = createCollegeDTO();
			model.add(dto);
			CollegeDTO foundDTO = model.findByName(dto.getName());
			assertNotNull(foundDTO);
			assertEquals(dto.getName(), foundDTO.getName());
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testSearch() {
		try {
			CollegeDTO dto = createCollegeDTO();
			model.add(dto); // Add a test college

			CollegeDTO searchDTO = new CollegeDTO();
			searchDTO.setName("TestCollege"); // Search by part of the name

			List<CollegeDTO> list = model.search(searchDTO, 0, 0);
			assertNotNull(list);
			assertTrue(!list.isEmpty());

			boolean found = false;
			for (CollegeDTO college : list) {
				if (college.getName().startsWith("TestCollege")) {
					found = true;
					break;
				}
			}
			assertTrue("Test college not found in search results", found);

		} catch (ApplicationException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (DuplicateRecordException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testList() {
		try {
			List<CollegeDTO> list = model.list(1, 10);
			assertNotNull(list);
			assertTrue(!list.isEmpty());

			for (CollegeDTO college : list) {
				assertNotNull(college);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
