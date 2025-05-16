import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.MarksheetModel;
import com.rays.pro4.Model.StudentModel;
import com.rays.pro4.Util.JDBCDataSource;

public class MarksheetModelTest {

    MarksheetModel model = new MarksheetModel();
    StudentModel studentModel = new StudentModel();

    @Before
    public void setUp() throws Exception {
        // Clean up the database before each test
        JDBCDataSource.runSQL("DELETE FROM ST_MARKSHEET");
        JDBCDataSource.runSQL("DELETE FROM ST_STUDENT");
    }

    @After
    public void tearDown() throws Exception {
        // Clean up the database after each test
        JDBCDataSource.runSQL("DELETE FROM ST_MARKSHEET");
        JDBCDataSource.runSQL("DELETE FROM ST_STUDENT");
    }

    @Test
    public void testAdd() throws Exception {
        StudentDTO student = addTestStudent();

        MarksheetDTO dto = new MarksheetDTO();
        dto.setRollNo("101");
        dto.setStudentId(student.getId());
        dto.setPhysics(70);
        dto.setChemistry(80);
        dto.setMaths(90);
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        MarksheetDTO addedDto = model.findByPK(pk);
        assertNotNull(addedDto);
        assertEquals("101", addedDto.getRollNo());
    }

    @Test(expected = DuplicateRecordException.class)
    public void testAddDuplicate() throws Exception {
        StudentDTO student = addTestStudent();

        MarksheetDTO dto1 = new MarksheetDTO();
        dto1.setRollNo("102");
        dto1.setStudentId(student.getId());
        dto1.setPhysics(75);
        dto1.setChemistry(85);
        dto1.setMaths(95);
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto1);

        MarksheetDTO dto2 = new MarksheetDTO();
        dto2.setRollNo("102"); // Duplicate Roll No
        dto2.setStudentId(student.getId());
        dto2.setPhysics(80);
        dto2.setChemistry(90);
        dto2.setMaths(100);
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto2);
    }

    @Test
    public void testDelete() throws Exception {
        StudentDTO student = addTestStudent();

        MarksheetDTO dto = new MarksheetDTO();
        dto.setRollNo("103");
        dto.setStudentId(student.getId());
        dto.setPhysics(60);
        dto.setChemistry(70);
        dto.setMaths(80);
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        model.delete(pk);
        MarksheetDTO deletedDto = model.findByPK(pk);
        assertNull(deletedDto);
    }

    @Test
    public void testUpdate() throws Exception {
        StudentDTO student = addTestStudent();

        MarksheetDTO dto = new MarksheetDTO();
        dto.setRollNo("104");
        dto.setStudentId(student.getId());
        dto.setPhysics(55);
        dto.setChemistry(65);
        dto.setMaths(75);
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        MarksheetDTO updatedDto = model.findByPK(pk);
        updatedDto.setPhysics(99);
        model.update(updatedDto);

        MarksheetDTO afterUpdateDto = model.findByPK(pk);
        assertEquals(99, afterUpdateDto.getPhysics());
    }

    @Test(expected = DuplicateRecordException.class)
    public void testUpdateDuplicate() throws Exception {
        StudentDTO student = addTestStudent();
        StudentDTO student2 = addTestStudent("StudentTwo", "Test", "studenttwo@test.com");

        MarksheetDTO dto1 = new MarksheetDTO();
        dto1.setRollNo("105");
        dto1.setStudentId(student.getId());
        dto1.setPhysics(50);
        dto1.setChemistry(60);
        dto1.setMaths(70);
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto1);

        MarksheetDTO dto2 = new MarksheetDTO();
        dto2.setRollNo("106");
        dto2.setStudentId(student2.getId());
        dto2.setPhysics(55);
        dto2.setChemistry(65);
        dto2.setMaths(75);
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
        long pk2 = model.add(dto2);

        MarksheetDTO updateTargetDto = model.findByPK(pk2);
        updateTargetDto.setRollNo("105"); // Attempt to set duplicate roll no
        model.update(updateTargetDto);
    }


    @Test
    public void testFindByRollNo() throws Exception {
        StudentDTO student = addTestStudent();

        MarksheetDTO dto = new MarksheetDTO();
        dto.setRollNo("107");
        dto.setStudentId(student.getId());
        dto.setPhysics(88);
        dto.setChemistry(77);
        dto.setMaths(66);
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto);

        MarksheetDTO foundDto = model.findByRollNo("107");
        assertNotNull(foundDto);
        assertEquals("107", foundDto.getRollNo());
    }

    @Test
    public void testFindByPK() throws Exception {
        StudentDTO student = addTestStudent();

        MarksheetDTO dto = new MarksheetDTO();
        dto.setRollNo("108");
        dto.setStudentId(student.getId());
        dto.setPhysics(91);
        dto.setChemistry(81);
        dto.setMaths(71);
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
        long pk = model.add(dto);

        MarksheetDTO foundDto = model.findByPK(pk);
        assertNotNull(foundDto);
        assertEquals(pk, foundDto.getId());
    }

    @Test
    public void testSearch() throws Exception {
        StudentDTO student1 = addTestStudent("Search", "Student1", "search1@test.com");
        StudentDTO student2 = addTestStudent("Search", "Student2", "search2@test.com");

        MarksheetDTO dto1 = new MarksheetDTO();
        dto1.setRollNo("201");
        dto1.setStudentId(student1.getId());
        dto1.setPhysics(70);
        dto1.setChemistry(80);
        dto1.setMaths(90);
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto1);

        MarksheetDTO dto2 = new MarksheetDTO();
        dto2.setRollNo("202");
        dto2.setStudentId(student2.getId());
        dto2.setPhysics(75);
        dto2.setChemistry(85);
        dto2.setMaths(95);
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto2);


        MarksheetDTO searchDto = new MarksheetDTO();
        searchDto.setRollNo("201");
        List<MarksheetDTO> list = model.search(searchDto, 1, 10);
        assertEquals(1, list.size());
        assertEquals("201", list.get(0).getRollNo());

        searchDto = new MarksheetDTO();
        searchDto.setName("Search Student"); // Assuming student name is concatenated in Marksheet
        list = model.search(searchDto, 1, 10);
        assertTrue(list.size() >= 2);

    }

    @Test
    public void testList() throws Exception {
        StudentDTO student1 = addTestStudent("List", "Student1", "list1@test.com");
        StudentDTO student2 = addTestStudent("List", "Student2", "list2@test.com");


        MarksheetDTO dto1 = new MarksheetDTO();
        dto1.setRollNo("301");
        dto1.setStudentId(student1.getId());
        dto1.setPhysics(70);
        dto1.setChemistry(80);
        dto1.setMaths(90);
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto1);

        MarksheetDTO dto2 = new MarksheetDTO();
        dto2.setRollNo("302");
        dto2.setStudentId(student2.getId());
        dto2.setPhysics(75);
        dto2.setChemistry(85);
        dto2.setMaths(95);
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto2);

        List<MarksheetDTO> list = model.list(1, 10);
        assertTrue(list.size() >= 2);
    }

    private StudentDTO addTestStudent() throws ApplicationException, DuplicateRecordException {
        StudentDTO student = new StudentDTO();
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setDob(new Date());
        student.setMobileNo("1234567890");
        student.setCollegeId(1L);
        student.setCollegeName("Test College");
        student.setCourseId(1L);
        student.setCourseName("Test Course");
        student.setEmailId("teststudent@test.com");
        student.setCreatedBy("test");
        student.setModifiedBy("test");
        student.setCreatedDatetime(new Timestamp(new Date().getTime()));
        student.setModifiedDatetime(new Timestamp(new Date().getTime()));
        long pk = studentModel.add(student);
        student.setId(pk);
        return student;
    }

     private StudentDTO addTestStudent(String firstName, String lastName, String email) throws ApplicationException, DuplicateRecordException {
        StudentDTO student = new StudentDTO();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setDob(new Date());
        student.setMobileNo("1234567890");
        student.setCollegeId(1L);
        student.setCollegeName("Test College");
        student.setCourseId(1L);
        student.setCourseName("Test Course");
        student.setEmailId(email);
        student.setCreatedBy("test");
        student.setModifiedBy("test");
        student.setCreatedDatetime(new Timestamp(new Date().getTime()));
        student.setModifiedDatetime(new Timestamp(new Date().getTime()));
        long pk = studentModel.add(student);
        student.setId(pk);
        return student;
    }
}