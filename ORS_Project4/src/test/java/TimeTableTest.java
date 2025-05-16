package com.rays.proj4.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.List;

import com.rays.pro4.DTO.TimeTableDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.TimeTableModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TimeTable Model Test classes.
 *
 * @author Lokesh SOlanki
 *
 */
public class TimeTableTest {

    public static TimeTableModel model = new TimeTableModel();

    public static void main(String[] args) throws Exception {
        testadd();
        testdelete();
        testupdate();
        testfindBypk();
        testlist();
        testsearch();

    }

    public static void testadd() throws ParseException {
        try {
            TimeTableDTO bean = new TimeTableDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            bean.setCourseId(new Date().getTime()/1000);
            bean.setCourseName("Course" + new Date().getTime()/1000);
            bean.setSubjectId(new Date().getTime()/1000);
            bean.setSubjectName("Subject" + new Date().getTime()/1000);
            bean.setSemester("Semester" + new Date().getTime()/1000);
            bean.setExamDate(sdf.parse("22/09/2024"));
            bean.setExamTime("10 am to 1 pm");
            bean.setCreatedBy("admin");
            bean.setModifiedBy("admin");
            bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
            bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
            long pk = model.add(bean);

            TimeTableDTO addedBean = model.findByPK(pk);
            assertNotNull(addedBean);
            assertEquals(bean.getCourseId(), addedBean.getCourseId());
            assertEquals(bean.getCourseName(), addedBean.getCourseName());
            assertEquals(bean.getSubjectId(), addedBean.getSubjectId());
            assertEquals(bean.getSubjectName(), addedBean.getSubjectName());
            assertEquals(bean.getSemester(), addedBean.getSemester());
            assertEquals(bean.getExamTime(), addedBean.getExamTime());


        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testdelete() {
        try {
            TimeTableDTO bean = new TimeTableDTO();
            long pk = 1L;

            bean.setId(pk);
            model.delete(bean);
            
            TimeTableBean deleteBean = model.findByPK(pk);
            assertTrue(deleteBean == null);

        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testupdate() throws ParseException, DuplicateRecordException {
        try {
            TimeTableDTO bean = new TimeTableDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            bean.setCourseId(new Date().getTime()/1000);
            bean.setCourseName("Updated Course" + new Date().getTime()/1000);
            bean.setSubjectId(new Date().getTime()/1000);
            bean.setSubjectName("Updated Subject" + new Date().getTime()/1000);
            bean.setSemester("Updated Semester" + new Date().getTime()/1000);
            bean.setExamDate(sdf.parse("22/08/2025"));
            bean.setExamTime("1 to 4 pm");
            bean.setCreatedBy("admin");
            bean.setModifiedBy("admin");
            bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
            bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
            long pk = model.add(bean);

            TimeTableDTO updateBean = model.findByPK(pk);

            updateBean.setExamTime("Updated time");
            model.update(updateBean);
            
            TimeTableBean updatedBean = model.findByPK(pk);
            assertEquals("Updated time", updatedBean.getExamTime());

        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testfindBypk() {
        try {
            TimeTableDTO bean = new TimeTableDTO();
            bean=model.findByPK(1);
            if(bean != null){
                assertNotNull(bean);
            }

        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testlist() throws Exception {
        try {
            TimeTableDTO bean = new TimeTableDTO();
            List list = new ArrayList();
            list = model.list(1, 10);
            assertTrue(list.size() > 0);
        } catch (ApplicationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static void testsearch() throws ApplicationException {
        TimeTableDTO bean = new TimeTableDTO();
        List list = new ArrayList();
        bean.setSubjectName("Subject");
        list = model.search(bean, 0, 0);
        assertTrue(list.size() > 0);
    }

}
