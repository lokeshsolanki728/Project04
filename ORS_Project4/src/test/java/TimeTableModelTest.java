package com.rays.pro4.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.CourseDTO;
import com.rays.pro4.DTO.SubjectDTO;
import com.rays.pro4.DTO.TimeTableDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CourseModel;
import com.rays.pro4.Model.SubjectModel;
import com.rays.pro4.Model.TimeTableModel;

public class TimeTableModelTest {

	TimeTableModel model = new TimeTableModel();
	CourseModel courseModel = new CourseModel();
	SubjectModel subjectModel = new SubjectModel();

	private long addedTimeTableId = 0;

	@Before
	public void setUp() throws ApplicationException, DuplicateRecordException {
		// Add a Course and Subject for testing
		CourseDTO course = new CourseDTO();
		course.setName("Test Course");
		course.setDescription("Description for test course");
		course.setDuration("1 Year");
		course.setCreatedBy("test");
		course.setModifiedBy("test");
		course.setCreatedDatetime(new Timestamp(new Date().getTime()));
		course.setModifiedDatetime(new Timestamp(new Date().getTime()));
		long courseId = courseModel.add(course);

		SubjectDTO subject = new SubjectDTO();
		subject.setSubjectName("Test Subject");
		subject.setDescription("Description for test subject");
		subject.setCourseId(courseId);
		subject.setCreatedBy("test");
		subject.setModifiedBy("test");
		subject.setCreatedDatetime(new Timestamp(new Date().getTime()));
		subject.setModifiedDatetime(new Timestamp(new Date().getTime()));
		long subjectId = subjectModel.add(subject);

		// Add a TimeTable for testing
		TimeTableDTO dto = new TimeTableDTO();
		dto.setCourseId(courseId);
		dto.setCourseName("Test Course");
		dto.setSubjectId(subjectId);
		dto.setSubjectName("Test Subject");
		dto.setSemester("1st");
		try {
			dto.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-07-20"));
		} catch (ParseException e) {
			fail("Date parsing failed");
		}
		dto.setExamTime("9:00 AM");
		dto.setCreatedBy("test");
		dto.setModifiedBy("test");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

		addedTimeTableId = model.add(dto);
	}

	@After
	public void tearDown() throws ApplicationException {
		if (addedTimeTableId > 0) {
			TimeTableDTO dtoToDelete = new TimeTableDTO();
			dtoToDelete.setId(addedTimeTableId);
			model.delete(dtoToDelete);
		}
	}

	@Test
	public void testAdd() {
		try {
			TimeTableDTO newDto = new TimeTableDTO();
			newDto.setCourseId(1L); // Assuming a course with ID 1 exists or add a new one
			newDto.setCourseName("Another Course");
			newDto.setSubjectId(1L); // Assuming a subject with ID 1 exists or add a new one
			newDto.setSubjectName("Another Subject");
			newDto.setSemester("2nd");
			try {
				newDto.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-08-15"));
			} catch (ParseException e) {
				fail("Date parsing failed");
			}
			newDto.setExamTime("2:00 PM");
			newDto.setCreatedBy("test");
			newDto.setModifiedBy("test");
			newDto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			newDto.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(newDto);
			assertNotNull("Failed to add TimeTable", model.findByPK(pk));

			// Clean up the added TimeTable
			TimeTableDTO addedDto = new TimeTableDTO();
			addedDto.setId(pk);
			model.delete(addedDto);

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during add test: " + e.getMessage());
		}
	}

	@Test
	public void testDelete() {
		try {
			TimeTableDTO dtoToDelete = new TimeTableDTO();
			dtoToDelete.setId(addedTimeTableId);
			model.delete(dtoToDelete);
			assertNull("Failed to delete TimeTable", model.findByPK(addedTimeTableId));
			addedTimeTableId = 0; // Reset to avoid deletion in tearDown
		} catch (ApplicationException e) {
			fail("Exception during delete test: " + e.getMessage());
		}
	}

	@Test
	public void testUpdate() {
		try {
			TimeTableDTO dtoToUpdate = model.findByPK(addedTimeTableId);
			assertNotNull("TimeTable not found for update", dtoToUpdate);

			dtoToUpdate.setExamTime("10:00 AM");
			dtoToUpdate.setModifiedBy("test_update");
			dtoToUpdate.setModifiedDatetime(new Timestamp(new Date().getTime()));

			model.update(dtoToUpdate);
			TimeTableDTO updatedDto = model.findByPK(addedTimeTableId);
			assertEquals("Update failed for exam time", "10:00 AM", updatedDto.getExamTime());
			assertEquals("Update failed for modified by", "test_update", updatedDto.getModifiedBy());

		} catch (ApplicationException | DuplicateRecordException e) {
			fail("Exception during update test: " + e.getMessage());
		}
	}

	@Test
	public void testFindByPK() {
		try {
			TimeTableDTO foundDto = model.findByPK(addedTimeTableId);
			assertNotNull("TimeTable not found by PK", foundDto);
			assertEquals("Found TimeTable has incorrect ID", addedTimeTableId, foundDto.getId());
		} catch (ApplicationException e) {
			fail("Exception during findByPK test: " + e.getMessage());
		}
	}

	@Test
	public void testSearch() {
		try {
			TimeTableDTO searchDto = new TimeTableDTO();
			searchDto.setSemester("1st");

			List<TimeTableDTO> results = model.search(searchDto, 1, 10, null, null);
			assertNotNull("Search results are null", results);
			// Further assertions can be made based on expected search results
			// assertEquals("Incorrect number of search results", 1, results.size());

		} catch (ApplicationException e) {
			fail("Exception during search test: " + e.getMessage());
		}
	}

	@Test
	public void testList() {
		try {
			List<TimeTableDTO> list = model.list(1, 10, null, null);
			assertNotNull("List results are null", list);
			// Further assertions can be made based on the expected list size and content

		} catch (ApplicationException e) {
			fail("Exception during list test: " + e.getMessage());
		}
	}

	@Test
	public void testCheckByCourseDate() {
		try {
			TimeTableDTO foundDto = model.checkByCourseDate(model.findByPK(addedTimeTableId).getCourseId(),
					model.findByPK(addedTimeTableId).getSemester(),
					new java.sql.Date(model.findByPK(addedTimeTableId).getExamDate().getTime()));
			assertNotNull("TimeTable not found by Course and Date", foundDto);
			assertEquals("Found TimeTable has incorrect ID", addedTimeTableId, foundDto.getId());
		} catch (ApplicationException e) {
			fail("Exception during checkByCourseDate test: " + e.getMessage());
		}
	}

	@Test
	public void testCheckBySemesterSubject() {
		try {
			TimeTableDTO foundDto = model.checkBySemesterSubject(model.findByPK(addedTimeTableId).getCourseId(),
					model.findByPK(addedTimeTableId).getSubjectId(),
					model.findByPK(addedTimeTableId).getSemester());
			assertNotNull("TimeTable not found by Semester and Subject", foundDto);
			assertEquals("Found TimeTable has incorrect ID", addedTimeTableId, foundDto.getId());
		} catch (ApplicationException e) {
			fail("Exception during checkBySemesterSubject test: " + e.getMessage());
		}
	}

	@Test
	public void testAddDuplicate() {
		try {
			TimeTableDTO existingDto = model.findByPK(addedTimeTableId);
			assertNotNull("Existing TimeTable not found for duplicate test", existingDto);

			TimeTableDTO duplicateDto = new TimeTableDTO();
			duplicateDto.setCourseId(existingDto.getCourseId());
			duplicateDto.setCourseName(existingDto.getCourseName());
			duplicateDto.setSubjectId(existingDto.getSubjectId());
			duplicateDto.setSubjectName(existingDto.getSubjectName());
			duplicateDto.setSemester(existingDto.getSemester());
			duplicateDto.setExamDate(existingDto.getExamDate());
			duplicateDto.setExamTime("Different Time"); // Change time to avoid exact duplicate check
			duplicateDto.setCreatedBy("test");
			duplicateDto.setModifiedBy("test");
			duplicateDto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			duplicateDto.setModifiedDatetime(new Timestamp(new Date().getTime()));

			model.add(duplicateDto);
			fail("DuplicateRecordException was not thrown for adding a duplicate TimeTable");
		} catch (DuplicateRecordException e) {
			// Expected exception
			System.out.println("Caught expected DuplicateRecordException: " + e.getMessage());
		} catch (ApplicationException e) {
			fail("ApplicationException during duplicate add test: " + e.getMessage());
		}
	}

	@Test
	public void testUpdateDuplicate() {
		try {
			// Add another TimeTable for update conflict
			TimeTableDTO anotherDto = new TimeTableDTO();
			anotherDto.setCourseId(model.findByPK(addedTimeTableId).getCourseId());
			anotherDto.setCourseName(model.findByPK(addedTimeTableId).getCourseName());
			anotherDto.setSubjectId(model.findByPK(addedTimeTableId).getSubjectId());
			anotherDto.setSubjectName(model.findByPK(addedTimeTableId).getSubjectName());
			anotherDto.setSemester("3rd"); // Different semester
			try {
				anotherDto.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-09-01"));
			} catch (ParseException e) {
				fail("Date parsing failed");
			}
			anotherDto.setExamTime("1:00 PM");
			anotherDto.setCreatedBy("test");
			anotherDto.setModifiedBy("test");
			anotherDto.setCreatedDatetime(new Timestamp(new Date().getTime()));
			anotherDto.setModifiedDatetime(new Timestamp(new Date().getTime()));
			long anotherTimeTableId = model.add(anotherDto);

			TimeTableDTO dtoToUpdate = model.findByPK(addedTimeTableId);
			assertNotNull("TimeTable not found for duplicate update test", dtoToUpdate);

			// Try to update the first TimeTable to conflict with the second one
			dtoToUpdate.setCourseId(model.findByPK(anotherTimeTableId).getCourseId());
			dtoToUpdate.setSubjectId(model.findByPK(anotherTimeTableId).getSubjectId());
			dtoToUpdate.setSemester(model.findByPK(anotherTimeTableId).getSemester());


			model.update(dtoToUpdate);
			fail("DuplicateRecordException was not thrown for updating to a duplicate TimeTable");

			// Clean up the added TimeTable for update conflict
			TimeTableDTO dtoToDelete = new TimeTableDTO();
			dtoToDelete.setId(anotherTimeTableId);
			model.delete(dtoToDelete);

		} catch (DuplicateRecordException e) {
			// Expected exception
			System.out.println("Caught expected DuplicateRecordException: " + e.getMessage());
		} catch (ApplicationException e) {
			fail("ApplicationException during duplicate update test: " + e.getMessage());
		}
	}
}