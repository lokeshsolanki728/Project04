import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rays.pro4.DTO.SubjectDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.SubjectModel;

public class SubjectModelTest {

    SubjectModel model = new SubjectModel();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAdd() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto = new SubjectDTO();
        dto.setSubjectName("Physics");
        dto.setDescription("Study of matter and energy");
        dto.setCourseId(1L); // Assuming a course with ID 1 exists
        dto.setCourseName("Science");
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        SubjectDTO addedDto = model.findByPK(pk);
        assertNotNull(addedDto);
        assertEquals("Physics", addedDto.getSubjectName());
    }

    @Test
    public void testAddDuplicate() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto1 = new SubjectDTO();
        dto1.setSubjectName("Chemistry");
        dto1.setDescription("Study of elements and compounds");
        dto1.setCourseId(1L);
        dto1.setCourseName("Science");
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.add(dto1);

        SubjectDTO dto2 = new SubjectDTO();
        dto2.setSubjectName("Chemistry");
        dto2.setDescription("Study of elements and compounds");
        dto2.setCourseId(1L);
        dto2.setCourseName("Science");
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));

        assertThrows(DuplicateRecordException.class, () -> {
            model.add(dto2);
        });
    }

    @Test
    public void testDelete() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto = new SubjectDTO();
        dto.setSubjectName("Biology");
        dto.setDescription("Study of living organisms");
        dto.setCourseId(1L);
        dto.setCourseName("Science");
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        model.delete(pk);
        SubjectDTO deletedDto = model.findByPK(pk);
        assertNull(deletedDto);
    }

    @Test
    public void testUpdate() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto = new SubjectDTO();
        dto.setSubjectName("History");
        dto.setDescription("Study of past events");
        dto.setCourseId(2L); // Assuming another course with ID 2 exists
        dto.setCourseName("Humanities");
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        SubjectDTO dtoToUpdate = model.findByPK(pk);
        dtoToUpdate.setSubjectName("World History");
        dtoToUpdate.setDescription("Study of global historical events");
        dtoToUpdate.setModifiedBy("updater");
        dtoToUpdate.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.update(dtoToUpdate);
        SubjectDTO updatedDto = model.findByPK(pk);
        assertEquals("World History", updatedDto.getSubjectName());
        assertEquals("Study of global historical events", updatedDto.getDescription());
        assertEquals("updater", updatedDto.getModifiedBy());
    }

    @Test
    public void testUpdateDuplicateName() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto1 = new SubjectDTO();
        dto1.setSubjectName("Geography");
        dto1.setDescription("Study of the Earth's surface");
        dto1.setCourseId(2L);
        dto1.setCourseName("Humanities");
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk1 = model.add(dto1);

        SubjectDTO dto2 = new SubjectDTO();
        dto2.setSubjectName("Economics");
        dto2.setDescription("Study of production, distribution, and consumption of goods and services");
        dto2.setCourseId(2L);
        dto2.setCourseName("Humanities");
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk2 = model.add(dto2);

        SubjectDTO dtoToUpdate = model.findByPK(pk2);
        dtoToUpdate.setSubjectName("Geography");

        assertThrows(DuplicateRecordException.class, () -> {
            model.update(dtoToUpdate);
        });
    }

    @Test
    public void testFindByName() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto = new SubjectDTO();
        dto.setSubjectName("Political Science");
        dto.setDescription("Study of political systems");
        dto.setCourseId(2L);
        dto.setCourseName("Humanities");
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.add(dto);
        SubjectDTO foundDto = model.findByName("Political Science");
        assertNotNull(foundDto);
        assertEquals("Political Science", foundDto.getSubjectName());
    }

    @Test
    public void testFindByPK() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto = new SubjectDTO();
        dto.setSubjectName("Sociology");
        dto.setDescription("Study of society");
        dto.setCourseId(2L);
        dto.setCourseName("Humanities");
        dto.setCreatedBy("test");
        dto.setModifiedBy("test");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        SubjectDTO foundDto = model.findByPK(pk);
        assertNotNull(foundDto);
        assertEquals(pk, foundDto.getId());
    }

    @Test
    public void testSearch() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto1 = new SubjectDTO();
        dto1.setSubjectName("Maths");
        dto1.setDescription("Study of numbers");
        dto1.setCourseId(1L);
        dto1.setCourseName("Science");
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto1);

        SubjectDTO dto2 = new SubjectDTO();
        dto2.setSubjectName("Statistics");
        dto2.setDescription("Study of data");
        dto2.setCourseId(1L);
        dto2.setCourseName("Science");
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto2);

        SubjectDTO searchDto = new SubjectDTO();
        searchDto.setCourseId(1L);
        List<SubjectDTO> list = model.search(searchDto);
        assertNotNull(list);
        assertTrue(list.size() >= 2); // Assuming at least Maths and Statistics are added for courseId 1
    }

    @Test
    public void testList() throws ApplicationException, DuplicateRecordException {
        SubjectDTO dto1 = new SubjectDTO();
        dto1.setSubjectName("Art");
        dto1.setDescription("Study of creative expression");
        dto1.setCourseId(3L); // Assuming a course with ID 3 exists
        dto1.setCourseName("Fine Arts");
        dto1.setCreatedBy("test");
        dto1.setModifiedBy("test");
        dto1.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto1.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto1);

        SubjectDTO dto2 = new SubjectDTO();
        dto2.setSubjectName("Music");
        dto2.setDescription("Study of sound");
        dto2.setCourseId(3L);
        dto2.setCourseName("Fine Arts");
        dto2.setCreatedBy("test");
        dto2.setModifiedBy("test");
        dto2.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto2.setModifiedDatetime(new Timestamp(new Date().getTime()));
        model.add(dto2);

        List<SubjectDTO> list = model.list();
        assertNotNull(list);
        assertTrue(list.size() >= 2); // Assuming at least Art and Music are added
    }
}